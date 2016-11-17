package com.frame.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.frame.controller.IndexController;
import com.frame.core.components.ThreadBinder;
import com.frame.core.components.UserAuthoritySubject;
import com.frame.core.utils.ApplicationConfigUtil;

public class GeneralIntercepter implements HandlerInterceptor {
	private final static Logger LOGGER=LoggerFactory.getLogger(GeneralIntercepter.class);
	private final static List<String> IGNORE_LIST=new ArrayList<String>();
	static{
		Properties p=ApplicationConfigUtil.load("ignores", "/config/ignore.properties", GeneralIntercepter.class);
		for (Object o : p.keySet()) {
			IGNORE_LIST.add(o.toString());
		}
	}
	public static final String REQUEST_URI_THREAD_KEY="requestURI";
	private boolean matchIgnore(String requestURI){
		for (String string : IGNORE_LIST) {
			if (requestURI.matches(string)) return true;
		}
		return false;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestURI=request.getServletPath();
		if (matchIgnore(requestURI)) return true;
		if (!UserAuthoritySubject.isUserVerify()&&requestURI.indexOf("login")==-1){
			response.sendRedirect(request.getContextPath()+"/login");
			return false;
		}
		if (ThreadBinder.get(REQUEST_URI_THREAD_KEY)==null) 
			ThreadBinder.set(REQUEST_URI_THREAD_KEY, requestURI);
		return true;
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
