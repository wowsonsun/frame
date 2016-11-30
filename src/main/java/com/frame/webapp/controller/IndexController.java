package com.frame.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.NavigationOption;
import com.frame.core.components.ThreadBinder;
import com.frame.entity.MenuEntity;
import com.frame.service.AuthorityService;
import com.frame.webapp.interceptor.GeneralIntercepter;

@Controller
@RequestMapping({"/"})
public class IndexController extends BaseController{
	@RequestMapping({"/","/index"})
	public Object index(){
		return "index.html";
	}
	@RequestMapping({"/main"})
	public Object main(HttpServletRequest request){
		request.setAttribute("a", "123");
		return "main.jsp";
	}
	@Autowired
	AuthorityService authorityService;
	@RequestMapping({"/decorator"})
	public Object decorator(){
		String requestURI=ThreadBinder.get(GeneralIntercepter.REQUEST_URI_THREAD_KEY);
		List<MenuEntity> menuLocation=authorityService.getMenuLocation(requestURI);
		List<MenuEntity> menuList=authorityService.getMenuList();
		List<NavigationOption> options=ThreadBinder.get(AuthorityService.NAVIGATION_OPTIONS_KEY);
		return new ModelAndView("decorator/decorator")
				.addObject("navigation", menuLocation)
				.addObject("options", options)
				.addObject("menuList", menuList);
	}
	  
}
