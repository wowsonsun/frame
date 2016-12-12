package com.frame.test.junit;

import org.springframework.beans.factory.annotation.Autowired;

import com.frame.core.query.xml.definition.QueryConditions;
import com.frame.webapp.controller.menu.MenuController;

public class Test1 extends AbstractBaseTest {
	@Autowired
	MenuController m;
	@Override
	public void dotest() throws Exception {
		m.list(new QueryConditions());
		
	}

}
