package com.frame.core.query.xml;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frame.core.components.AjaxResult;
import com.frame.core.components.BaseEntity;
import com.frame.core.query.xml.definition.*;
import com.frame.core.utils.HttpContextUtil;
import com.frame.core.utils.ReflectUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.NavigationOption;
import com.frame.core.components.ThreadBinder;
import com.frame.core.query.xml.service.XmlQueryDefineService;
import com.frame.service.AuthorityService;
import com.google.gson.JsonSyntaxException;
@Controller
public abstract class GeneralController <T extends BaseEntity>{
	public static class GeneralControllerExcuteException extends RuntimeException{
		private static final long serialVersionUID = -7376890176830677560L;
		public GeneralControllerExcuteException(Throwable e){
			super(e);
		}
	}
	private Class<T> targetClass;
    public Class<T> getTargetClass() {
        return targetClass;
    }
	protected final Logger LOGGER=LoggerFactory.getLogger(this.getClass());
	private PageDefinitionHolder pageHolder;
	public PageDefinitionHolder getPageHolder(){return pageHolder;}
	@SuppressWarnings("unchecked")
	public GeneralController(){
        targetClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> loader=this.getClass();
		String xmlFileName=loader.getAnnotation(com.frame.core.query.xml.annoation.PageDefinition.class).value();
		pageHolder=new PageDefinitionHolder(xmlFileName, loader);
		pageHolder.refresh(this);
	}
	@Autowired
	private XmlQueryDefineService service;
    @RequestMapping("/")
	public Object list(QueryConditions queryConditions){
		pageHolder.refresh(this);//根据配置是否更新刷新配置
		queryConditions.parseFromParamString();
		if (queryConditions.getParamString()==null) {//如果是第一次请求 即没有查询条件
			for(SortEntry sortEntry:pageHolder.getPageDefinition().getQueryDefinition().getSortBy()){//将默认的排序压入查询条件
				queryConditions.getSortEntries().add(sortEntry);
			}
		}
		ModelAndView mv=new ModelAndView("/common/list");
		Object list= service.list(pageHolder.getPageDefinition(), queryConditions);//查询得到的结果
		mv.addObject("totalPageCount", service.totalPageCount(pageHolder.getPageDefinition(), queryConditions));//查询页数准备分页
		mv.addObject("pageList",list);
		mv.addObject("pageDefinition", pageHolder.getPageDefinition());//用于拿到定义的列
		mv.addObject("queryConditions", service.prepareQueryCondition(queryConditions,pageHolder.getPageDefinition().getQueryDefinition()));//处理查询条件
		mv.addObject("mergedSort",mergeSortCondition(pageHolder.getPageDefinition().getQueryDefinition().getColumns(),queryConditions.getSortEntries()));//处理排序的条件
		List<NavigationOption> options=new ArrayList<NavigationOption>();
        if(pageHolder.getPageDefinition().getManage()!=null) {
            options.add(new NavigationOption("添加", "addRow()"));
            options.add(new NavigationOption("修改", "manageRow()"));
        }
		if(pageHolder.getPageDefinition().getDelete()!=null) options.add(new NavigationOption("删除", "deleteRow()"));//权限
		HttpContextUtil.getCurrentRequest().setAttribute(AuthorityService.NAVIGATION_OPTIONS_KEY,options);
		return mv;
	}
    private String[] mergeSortCondition(List<ColumnDefinition> columnDefinitions, List<SortEntry> sortEntries){
        String[] mergedSort=new String[columnDefinitions.size()];
        int index=0;
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String order="";
            for (SortEntry sortEntry:sortEntries) {
                boolean isAliasEqual=
                        (columnDefinition.getFromAlias()==null||"".equals(columnDefinition.getFromAlias()))&&(sortEntry.getFromAlias()==null||"".equals(sortEntry.getFromAlias()))
                                || columnDefinition.getFromAlias()!=null&& columnDefinition.getFromAlias().equals(sortEntry.getFromAlias());
                boolean isFieldEqual= columnDefinition.getField().equals(sortEntry.getField());
                if (isFieldEqual&&isAliasEqual){
                    order="sortOrder=\""+sortEntry.getOrder().toUpperCase()+"\" sortIndex=\""+index+"\"";
                    break;
                }
            }
            mergedSort[index]=order;
            index++;
        }
        return mergedSort;
    }
    @RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id){
		if (pageHolder.getPageDefinition().getDelete().getBeforeDelete()!=null){
			Method toInvoke=pageHolder.getPageDefinition().getDelete().getBeforeDeleteMethod();
			Object[] args=new Object[toInvoke.getParameterTypes().length];
			if (pageHolder.getPageDefinition().getDelete().getInjectIndex()!=null){
				args[pageHolder.getPageDefinition().getDelete().getInjectIndex()]=service.get(targetClass,id);
			}
			try {
                toInvoke.setAccessible(true);
				if ((Boolean)toInvoke.invoke(this,args)) service.delete(id,targetClass);
			} catch (Exception e) {
				throw new GeneralControllerExcuteException(e);
			}
		}else{
            service.delete(id,targetClass);
        }

		return new AjaxResult();
	}
    protected boolean beforeDelete(T entity){return true;}
    @RequestMapping(value={"add","edit"},method = RequestMethod.GET)
    public Object managePage(Long id){
        ModelAndView mv=new ModelAndView("common/manage");
        mv.addAllObjects(service.prepareManage(id,pageHolder.getPageDefinition().getManage(),this.targetClass));
        List<NavigationOption> options=new ArrayList<NavigationOption>();
        options.add(new NavigationOption("保存","frame.manage.saveManage()"));
        options.add(new NavigationOption("返回","frame.manage.goBack()"));
        HttpContextUtil.getCurrentRequest().setAttribute(AuthorityService.NAVIGATION_OPTIONS_KEY,options);
        return mv;
    }
    @Autowired
    Gson gson;
	@RequestMapping(value={"add","edit"},method = RequestMethod.POST)
    @ResponseBody
	public Object saveManage(String paramString) throws NoSuchMethodException {
	    service.saveManage(paramString,this);
    	return new AjaxResult();
	}
	public boolean beforeUpdate(T entity){return true;}
	//TODO 这里做这可Controller能够出现的异常。
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response) throws Throwable{
		throw e;
//		return null;
	}

}
