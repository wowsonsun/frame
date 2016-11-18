package com.frame.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.ThreadBinder;
import com.frame.entity.MenuEntity;
import com.frame.interceptor.GeneralIntercepter;
import com.frame.service.AuthorityService;

@Controller
@RequestMapping({"/"})
public class IndexController extends BaseController{
	private static final Logger LOGGER=LoggerFactory.getLogger(IndexController.class);
	@RequestMapping({"/","/index"})
	public Object index(){
		return "index.html";
	}
	@RequestMapping({"/main"})
	public Object main(){
		return "main.jsp";
	}
	@Autowired
	AuthorityService authorityService;
	@RequestMapping({"/decorator"})
	public Object decorator(){
		String requestURI=ThreadBinder.get(GeneralIntercepter.REQUEST_URI_THREAD_KEY);
		List<MenuEntity> menuList=authorityService.getMenuList();
		return new ModelAndView("decorator/decorator")
				.addObject("navigation", "")
				.addObject("menuList", menuList);
	}
	  
}
