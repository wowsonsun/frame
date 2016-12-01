package com.frame.webapp.controller.menu;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frame.core.query.xml.GeneralController;
import com.frame.core.query.xml.annoation.PageDefination;
@Controller
@RequestMapping("/menu")
@PageDefination("pageDefination.xml")
public class MenuController extends GeneralController {
	
}
