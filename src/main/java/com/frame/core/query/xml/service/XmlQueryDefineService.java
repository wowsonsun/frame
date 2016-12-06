package com.frame.core.query.xml.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.frame.core.query.xml.QueryConditions;
import com.frame.core.query.xml.QueryHqlResolver;
import com.frame.core.query.xml.defination.PageDefination;
import com.frame.dao.GeneralDao;

@Service
public class XmlQueryDefineService {
	@Autowired
	GeneralDao dao;
	public Object list(PageDefination pageDefination,QueryConditions conditions){
		String selectHql=QueryHqlResolver.generateSelect(pageDefination.getQueryDefination(), conditions.getConditions());
		System.out.println(selectHql);
		//TODO pring info 
		return null;
	}
}
