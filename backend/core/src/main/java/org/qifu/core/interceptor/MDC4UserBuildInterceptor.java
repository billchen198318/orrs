package org.qifu.core.interceptor;

import org.qifu.core.model.User;
import org.qifu.core.util.UserUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class MDC4UserBuildInterceptor implements HandlerInterceptor {
	private static final String USERID_KEY_NAME = "userId";
	
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String logUserId = "";
		User u = null;
		if ((u = UserUtils.getCurrentUser()) != null) {
			logUserId = u.getUserId();
		}
		org.slf4j.MDC.put(USERID_KEY_NAME, logUserId);
		return true;
	}
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	org.slf4j.MDC.remove(USERID_KEY_NAME);
    }	
    
}
