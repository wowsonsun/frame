package com.frame.core.query.xml;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.frame.core.components.AjaxResult;
import com.frame.core.query.xml.definition.ColumnDefinition;
import com.frame.core.query.xml.definition.QueryConditions;
import com.frame.core.query.xml.definition.QueryDefinition;
import com.frame.core.query.xml.definition.SortEntry;
import com.frame.core.utils.HttpContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.NavigationOption;
import com.frame.core.components.ThreadBinder;
import com.frame.core.query.xml.service.XmlQueryDefineService;
import com.frame.service.AuthorityService;
import com.google.gson.JsonSyntaxException;
@Controller
public abstract class GeneralController {
	public static class GeneralControllerInitException extends RuntimeException{
		private static final long serialVersionUID = -7376890176830677560L;
		public GeneralControllerInitException(Throwable e){
			super(e);
		}
	}
	protected final Logger LOGGER=LoggerFactory.getLogger(this.getClass());
	private PageDefinitionHolder pageHolder;
	public GeneralController(){
		Class<?> loader=this.getClass();
		String xmlFileName=loader.getAnnotation(com.frame.core.query.xml.annoation.PageDefinition.class).value();
		pageHolder=new PageDefinitionHolder(xmlFileName, loader);
	}
	@Autowired
	private XmlQueryDefineService service;
	@RequestMapping("/")
	public Object list(QueryConditions queryConditions){
		pageHolder.refreshIfOutOfDate();
		queryConditions.parseFromParamString();
		if (queryConditions.getParamString()==null) {
			for(SortEntry sortEntry:pageHolder.getPageDefinition().getQueryDefinition().getSortBy()){
				queryConditions.getSortEntries().add(sortEntry);
			}
		}
		ModelAndView mv=new ModelAndView("/common/list");
		Object list= service.list(pageHolder.getPageDefinition(), queryConditions);
		mv.addObject("totalPageCount", service.totalPageCount(pageHolder.getPageDefinition(), queryConditions));
		mv.addObject("pageList",list);
		mv.addObject("pageDefinition", pageHolder.getPageDefinition());
		mv.addObject("queryConditions", service.prepareQueryCondition(queryConditions,pageHolder.getPageDefinition().getQueryDefinition()));
		mv.addObject("mergedSort",mergeSortCondition(pageHolder.getPageDefinition().getQueryDefinition().getColumns(),queryConditions.getSortEntries()));
		List<NavigationOption> options=new ArrayList<NavigationOption>();
		options.add(new NavigationOption("添加", "void(0)"));
		options.add(new NavigationOption("修改", "void(0)"));
		if(pageHolder.getPageDefinition().getDelete()!=null) options.add(new NavigationOption("删除", "deleteRow()"));//权限
		ThreadBinder.set(AuthorityService.NAVIGATION_OPTIONS_KEY,options);
		return mv;
	}
	@RequestMapping("/delete")
	@ResponseBody
	public Object delete(Long id){
		service.delete(id,pageHolder.getPageDefinition().getQueryDefinition().getMappedClass().get(0).getMappedClass());
		return new AjaxResult();
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
	//TODO 处理删除异常
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response) throws Throwable{
		throw e;
//		return null;
	}

}
