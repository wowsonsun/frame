package com.frame.webapp.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.ThreadBinder;
import com.frame.service.account.UserService;
import com.frame.webapp.controller.BaseController;
import com.frame.webapp.interceptor.GeneralIntercepter;

@Controller
@RequestMapping({"/"})
public class LoginController extends BaseController{
	@Autowired
	private UserService userService;
	@RequestMapping("/login")
	public Object login(String userLoginVerification,String userPassword){
		if(userService.login(userLoginVerification, userPassword)==0){
			ThreadBinder.set(GeneralIntercepter.REQUEST_URI_THREAD_KEY, "/main");
			LOGGER.info("login-> set requestURI : /main");
			return "redirect:/main";
		}else{
			return new ModelAndView("account/login").addObject("userLoginVerification", userLoginVerification).addObject("message", userLoginVerification==null?"":"1");
		}
	}
}
