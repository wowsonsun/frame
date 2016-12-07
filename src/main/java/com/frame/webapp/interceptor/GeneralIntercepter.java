package com.frame.webapp.interceptor;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.frame.core.components.ThreadBinder;
import com.frame.core.components.UserAuthoritySubject;

public class GeneralIntercepter implements HandlerInterceptor {
	private final static Logger LOGGER=LoggerFactory.getLogger(GeneralIntercepter.class);
	public static final String REQUEST_URI_THREAD_KEY=GeneralIntercepter.class.getName()+".requestURI";
	public static final String REQUEST_URI_BEFORE_LOGIN_THREAD_KEY=GeneralIntercepter.class.getName()+".REQUESTURI_BEFOTRLOGIN";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI=request.getServletPath();
		if (requestURI.startsWith("/resources/")) return true;
		if (!UserAuthoritySubject.isUserVerify()&&requestURI.indexOf("login")==-1){
			if (isAjax(request)){
				response.setHeader("SESSION_STATUS", "TIME_OUT");
				return false;
			}else{
				request.getSession().setAttribute(REQUEST_URI_BEFORE_LOGIN_THREAD_KEY, requestURI);
				response.sendRedirect(request.getContextPath()+"/login");
				return false;
			}
		}
		if (ThreadBinder.get(REQUEST_URI_THREAD_KEY)==null) {
			ThreadBinder.set(REQUEST_URI_THREAD_KEY, requestURI);
			LOGGER.info("Intercepter-> set requestURI : "+requestURI);
		}
		return true;
	}
	private static boolean isAjax(HttpServletRequest request){
		return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equals("XMLHttpRequest");
    }
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		if (ex!=null){
			LOGGER.error("", ex);
		}

	}

}
