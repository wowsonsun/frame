package com.frame.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.frame.dao.GeneralDao;
import com.frame.entity.MenuEntity;

@Service
@Transactional
public class AuthorityService {
	@Autowired
	GeneralDao dao;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<MenuEntity> getMenuList(){
		//List.size()=0 list.get0 =mainpage
		List list=dao.getHibernateTemplate().find("select child from MenuEntity p join p.children as child where p.parent is null");
		return filtMenu(list);
	}
	private List<MenuEntity> filtMenu(List<MenuEntity> list){
		if (list==null||list.size()==0) return null;
		List<MenuEntity> resList=new ArrayList<MenuEntity>();
		for (MenuEntity menuEntity : list) {
			//TODO 如果含有权限
			menuEntity.setChildren(filtMenu(menuEntity.getChildren()));
			resList.add(menuEntity);
		}
		return resList;
	}
}
