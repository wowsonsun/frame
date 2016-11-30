package com.frame.core.query.xml;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class GeneralController {
protected final Logger LOGGER=LoggerFactory.getLogger(this.getClass()); 
	
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response){
		LOGGER.error("Control层捕获到异常。",e);
		return null;
	}
}
