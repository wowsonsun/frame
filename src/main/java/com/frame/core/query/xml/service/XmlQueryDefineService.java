package com.frame.core.query.xml.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frame.core.query.xml.DataFilter;
import com.frame.core.query.xml.DefaultDataFilter;
import com.frame.core.query.xml.QueryCondition;
import com.frame.core.query.xml.QueryConditions;
import com.frame.core.query.xml.QueryHqlResolver;
import com.frame.core.query.xml.defination.ColumnDefination;
import com.frame.core.query.xml.defination.PageDefination;
import com.frame.dao.GeneralDao;

@Transactional
@Service
public class XmlQueryDefineService {
	private final static Logger LOGGER = LoggerFactory.getLogger(XmlQueryDefineService.class);
	public static class DataSetTransferException extends RuntimeException{
		private static final long serialVersionUID = 1L;
		public DataSetTransferException(Throwable t){super(t);}
	}
	@Autowired
	GeneralDao dao;
	@SuppressWarnings("unchecked")
	public Object list(PageDefination pageDefination,QueryConditions conditions){
		String selectHql=QueryHqlResolver.generateSelect(pageDefination.getQueryDefination(), conditions);
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
		DataFilter defaultDataFilter=new DefaultDataFilter();
		for (Map<String,Object> map : listMap) {
			String[] stra=new String[pageDefination.getQueryDefination().getColumns().size()];
			int j=0;
			for (Iterator<ColumnDefination> iterator = pageDefination.getQueryDefination().getColumns().iterator(); iterator.hasNext();j++) {
				ColumnDefination column = iterator.next();
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
	public int totalPageCount(PageDefination pageDefination,QueryConditions conditions){
		String selectHql=QueryHqlResolver.generateCount(pageDefination.getQueryDefination(), conditions.getConditions());
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
	public static void main(String[] args) {
		long count=21L;
		int pageSize=20;
		System.out.println((int) ((count+pageSize-1)/pageSize));
	}
}
