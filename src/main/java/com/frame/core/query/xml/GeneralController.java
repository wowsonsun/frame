package com.frame.core.query.xml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.query.xml.defination.PageDefination;
import com.frame.core.query.xml.service.XmlQueryDefineService;
import com.google.gson.JsonSyntaxException;
@Controller
public abstract class GeneralController {
	public static class GeneralControllerInitException extends RuntimeException{
		private static final long serialVersionUID = -7376890176830677560L;
		public GeneralControllerInitException(Throwable e){
			super(e);
		}
	}
	protected final Logger LOGGER=LoggerFactory.getLogger(this.getClass()); 
	private PageDefinationHolder pageHolder;
	public GeneralController(){
		Class<?> loader=this.getClass();
		String xmlFileName=loader.getAnnotation(com.frame.core.query.xml.annoation.PageDefination.class).value();
		pageHolder=new PageDefinationHolder(xmlFileName, loader);
	}
	@Autowired
	private XmlQueryDefineService service;
	
	
	
	
	@RequestMapping("/")
	public Object list(QueryConditions queryConditions){
		pageHolder.refreshIfOutOfDate();
		queryConditions.parseFromParamString();
		ModelAndView mv=new ModelAndView("/common/list");
		mv.addObject("pageList", service.list(pageHolder.getPageDefination(), queryConditions));
		mv.addObject("pageDefination", pageHolder.getPageDefination());
		return mv;
	}
	
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response){
		LOGGER.error("Control层捕获到异常。",e);
		return null;
	}
	@ExceptionHandler(value=JsonSyntaxException.class)
	public Object handleJsonSyntaxException(Throwable e,HttpServletRequest request,HttpServletResponse response){
		LOGGER.error("Control层捕获到异常。",e);
		return null;
	}
}
