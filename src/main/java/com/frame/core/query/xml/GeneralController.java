package com.frame.core.query.xml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import com.frame.core.query.xml.defination.PageDefination;
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
	
	
	
	
	
	@RequestMapping("/")
	public Object list(){
		pageHolder.refreshIfOutOfDate();
		return "/common/list";
	}
	
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response){
		LOGGER.error("Control层捕获到异常。",e);
		return null;
	}
}
