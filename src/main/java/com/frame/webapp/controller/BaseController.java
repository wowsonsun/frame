package com.frame.webapp.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * 这里面写异常处理的逻辑，
 * 1.进行一些通用的异常处理，比如SQL异常，乐观锁异常什么的。
 * 2.判断是不是AJAX异常  如果是 使用AjaxResult
 * 3.否则返回一个通用的异常处理页面
 * --有可能异常的相关信息需要打印至专门的日志文件里面。
 * @author Defferson.Cheng
 *
 */
@Controller
public abstract class BaseController {
	protected final Logger LOGGER=LoggerFactory.getLogger(this.getClass()); 
	
	@ExceptionHandler(value=Throwable.class)
	public Object handleException(Throwable e,HttpServletRequest request,HttpServletResponse response){
		LOGGER.error("Control层捕获到异常。",e);
		return null;
	}
	
}
