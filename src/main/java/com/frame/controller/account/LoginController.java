package com.frame.controller.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.controller.BaseController;
import com.frame.service.account.UserService;

@Controller
@RequestMapping({"/"})
public class LoginController extends BaseController{
	@Autowired
	private UserService userService;
	@RequestMapping("/login")
	public Object login(String userLoginVerification,String userPassword){
		if(userService.login(userLoginVerification, userPassword)==0){
			return "redirect:/main";
		}else{
			return new ModelAndView("account/login").addObject("userLoginVerification", userLoginVerification).addObject("message", userLoginVerification==null?"":"1");
		}
	}
}
