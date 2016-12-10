package com.frame.webapp.controller.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frame.core.query.xml.GeneralController;
import com.frame.core.query.xml.annoation.PageDefinition;
@Controller
@RequestMapping("/menu")
@PageDefinition("pageDefinition.xml")
public class MenuController extends GeneralController {
	public static void main(String[] args) {
		MenuController m=new MenuController();
		System.out.println(m);
	}
}
