package com.frame.core.query.xml.service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.frame.core.components.BaseEntity;
import com.frame.core.query.xml.definition.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frame.core.query.xml.DataFilter;
import com.frame.core.query.xml.DefaultDataFilter;
import com.frame.core.query.xml.QueryHqlResolver;
import com.frame.dao.GeneralDao;
import org.springframework.util.StringUtils;

@Transactional
@Service
public class XmlQueryDefineService {
	private final static Logger LOGGER = LoggerFactory.getLogger(XmlQueryDefineService.class);
	public static class DataSetTransferException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public DataSetTransferException(Throwable t){super(t);}
	}
	public static class QueryConditionParseException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public QueryConditionParseException(String message){super(message);}
	}
	private DataFilter defaultDataFilter=new DefaultDataFilter();
	@Autowired
	GeneralDao dao;
	@SuppressWarnings("unchecked")
	public Object list(PageDefinition pageDefinition,QueryConditions conditions){
		String selectHql=QueryHqlResolver.generateSelect(pageDefinition.getQueryDefinition(), conditions);
		if (LOGGER.isInfoEnabled())
			LOGGER.info("生成的查询HQL："+selectHql);
		else System.out.println("生成的查询HQL："+selectHql);
		Query query=dao.getSessionFactory().getCurrentSession().createQuery(selectHql); 
		int i=0;
		if (conditions.getConditions()!=null) for (QueryCondition condition : conditions.getConditions()) {
			query.setParameter(i, condition.getValue());
			i++;
		}
		query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		query.setMaxResults(conditions.getPageSize());
		query.setFirstResult(conditions.getPageSize()*(conditions.getPage()-1));
		//分页
		List<Map<String,Object>> listMap=query.list();
		List<String[]> list=new ArrayList<String[]>();
		for (Map<String,Object> map : listMap) {
			String[] stra=new String[pageDefinition.getQueryDefinition().getColumns().size()];
			int j=0;
			for (Iterator<ColumnDefinition> iterator = pageDefinition.getQueryDefinition().getColumns().iterator(); iterator.hasNext(); j++) {
				ColumnDefinition column = iterator.next();
				if (column.getStaticColumnData()!=null) stra[j]=column.getStaticColumnData();
				else if (column.getFilter()!=null){
					try {
						DataFilter f=(DataFilter) column.getFilter().newInstance();
						stra[j]=f.filt(map.get("col_"+j));
					} catch (Exception e) {
						throw new DataSetTransferException(e);
					}
				}else stra[j]=defaultDataFilter.filt(map.get("col_"+j));
			}
			list.add(stra);
		}
		return list;
	}
	public int totalPageCount(PageDefinition pageDefinition,QueryConditions conditions){
		String selectHql=QueryHqlResolver.generateCount(pageDefinition.getQueryDefinition(), conditions.getConditions());
		if (LOGGER.isInfoEnabled())
			LOGGER.info("生成的计数HQL："+selectHql);
		else System.out.println("生成的计数HQL："+selectHql);
		Object[] params=new Object[conditions.getConditions()==null?0:conditions.getConditions().size()];
		int i=0;
		if (conditions.getConditions()!=null) for (Iterator<QueryCondition> iterator = conditions.getConditions().iterator(); iterator.hasNext();i++) {
			QueryCondition condition = iterator.next();
			params[i]=condition.getValue();
		}
		long count=dao.getUnique(selectHql, params);
		int pageSize=conditions.getPageSize();
		int totalPageCount=(int) ((count+pageSize-1)/pageSize);
		if (totalPageCount<1) totalPageCount=1;
		return totalPageCount;
	}
	@Autowired
	public Gson gson;
	/**
	 * TODO
	 * 1.设置值
	 * 2.找到类型放进去
	 * 3.如果类型为SELECT 的话要初始化要选择的数据
	 * @param queryConditions
	 * @param queryDefinition
	 * @return
	 */
	public QueryConditions prepareQueryCondition(QueryConditions queryConditions,QueryDefinition queryDefinition){
		List<QueryConditionDefine> res=queryDefinition.getQueryConditionDefines();
		for(QueryConditionDefine q : res){
			if (queryConditions.getConditions()!=null) for (QueryCondition q2:queryConditions.getConditions()){
				if (q.equalsField(q2)){
					q.setValue(defaultDataFilter.filt(q2.getValue()));
					break;
				}
			}
			if (q.getValue()==null) q.setValue(q.getDefaultValue());
			Class<?> type=null;
			for (MappedClassEntry mappedClass:queryDefinition.getMappedClass()){
				if (StringUtils.isEmpty(mappedClass.getAlias())&&StringUtils.isEmpty(q.getAlias())||mappedClass.getAlias()!=null&&mappedClass.getAlias().equals(q.getAlias())){
					try {
						type=resolveFieldClass(mappedClass.getMappedClass(),q.getField());
						break;
					} catch (NoSuchMethodException e) {
					}
				}
				if (mappedClass.getJoin()!=null) for (JoinEntry joinEntry:mappedClass.getJoin()){
					if (StringUtils.isEmpty(joinEntry.getAs())&&StringUtils.isEmpty(q.getAlias())||joinEntry.getAs()!=null&&joinEntry.getAs().equals(q.getAlias())){
						try {
							Class<?> optionClass=resolveFieldClass(mappedClass.getMappedClass(),joinEntry.getField());
							if (q.getOptionClass()==null) q.setOptionClass(optionClass);
							type=resolveFieldClass(optionClass,q.getField());
							break;
						} catch (NoSuchMethodException e) {
						}
					}
				}
				if (type!=null) break;
			}
			if (type!=null) q.setType(type);
			else throw new QueryConditionParseException("No such field found in queryDedination! alias:"+q.getAlias()+" field:"+q.getField());
			if ("SELECT".equals(q.getInputType())){
				if (!StringUtils.isEmpty(q.getStaticData())){
					q.setParsedData(gson.fromJson(q.getStaticData(),new TypeToken<List<Map<String,String>>>(){}.getType()));
				}else{
					if (q.getOptionClass()!=null&&BaseEntity.class.isAssignableFrom(q.getOptionClass())){
						List<?> list= dao.findMap("select "+q.getSelectTextField()+" as text,"+q.getSelectValueField() +" as value from "+q.getOptionClass().getName()+" ");
						q.setParsedData(list);
					}else throw new QueryConditionParseException("Select option data parse Exception.No static data found nor optionClass found.");
				}
			}
		}
		queryConditions.setConditions(new ArrayList<QueryCondition>(res));
		return queryConditions;
	}
	private static Class<?> resolveFieldClass(Class<?> cls,String field) throws NoSuchMethodException {
		String methodName="get"+Character.toUpperCase(field.charAt(0))+field.substring(1);
		Method method=cls.getMethod(methodName);
		return method.getReturnType();
	}
	public static void main(String[] args) {
		System.out.println(List.class.isAssignableFrom(ArrayList.class));
	}
}
