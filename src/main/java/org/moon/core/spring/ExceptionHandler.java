package org.moon.core.spring;

import org.moon.log.domain.Log;
import org.moon.rbac.domain.User;
import org.moon.rbac.service.UserService;
import org.moon.utils.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 平台异常处理,主要用于日志的记录
 * @author Gavin
 * @version 1.0
 * @date 2013-1-9
 */
@Component
public class ExceptionHandler extends SimpleMappingExceptionResolver  implements HandlerExceptionResolver   {

	@Resource
	private UserService userService;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		System.out.println(determineViewName(ex, request)+"...................");
		User  currentUser = userService.getCurrentUser(request);
			
		//捕获系统级日志,记录详细信息
		 String message = ex.getClass().getName()+":"+ex.getLocalizedMessage();
		 StringBuffer bf = new StringBuffer(message+"\n");
		 for(StackTraceElement se:ex.getStackTrace()){
			 bf.append("at "+se.getClassName()+"."+se.getMethodName()+"("+se.getFileName()+":"+se.getLineNumber()+")\n");
		 }
		 if(currentUser!=null) {
		  new Log(currentUser.getUserName(),currentUser.getId(),message,bf.toString(),Constants.SYSTEM_LOG).save();
		 }  else {
			 new Log("NOT LOGINED",-1L,message,"当前用户未登录,The Session id is "+request.getSession().getId()+"\n"+bf.toString(),Constants.SYSTEM_LOG).save();
		 }
	
		return null;
	}

}
