package com.frame.test.junit;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;

import com.frame.core.query.xml.QueryConditions;
import com.frame.webapp.controller.menu.MenuController;

public class Test1 extends AbstractBaseTest {
	@Autowired
	MenuController m;
	@Override
	public void dotest() throws Exception {
		m.list(new QueryConditions());
		
	}

}
