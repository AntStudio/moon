package com.greejoy.log.interceptor;

import javax.annotation.Resource;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import com.greejoy.rbac.domain.User;
import com.greejoy.rbac.service.UserService;
import com.greejoy.support.session.SessionContext;

/**
 * 日志拦截器
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Component
@Deprecated
public class LogInterceptor implements MethodInterceptor{

	@Resource
	private UserService userService;

	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Long currentUserId = (Long)SessionContext.getSession().getAttribute(User.CURRENT_USER_ID);
		return null;
	}

}
