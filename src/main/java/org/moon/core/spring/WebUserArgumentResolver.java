package org.moon.core.spring;


import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rbac.service.UserService;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WebUserArgumentResolver implements HandlerMethodArgumentResolver{

	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Resource
	private UserService userService;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(WebUser.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		log.debug("Trying to inject @WebUser for {} in method:{}",parameter.getParameterName(),parameter.getMethod().getName());
		
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpSession session = request.getSession();
		Object userId = session.getAttribute(User.CURRENT_USER_ID);
		if(Objects.isNull(userId)){
			log.debug("There is no logined user,failed to inject");
			return null;
		}else{
			log.debug("Success inject @WebUser for {} in method:{}",parameter.getParameterName(),parameter.getMethod().getName());
			Long uid =(Long)userId;
			return userService.get(uid);
		}
	}

}
