package org.moon.rbac.interceptor;

import com.reeham.component.ddd.model.ModelContainer;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.moon.core.session.SessionContext;
import org.moon.log.domain.Log;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.*;
import org.moon.rbac.service.UserService;
import org.moon.utils.Constants;
import org.moon.utils.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * 拦截器，主要拦截系统级菜单和权限注解
 * @author Gavin
 * @version 1.0
 * @date 2012-12-22
 */
@Component
public class RbacInterceptor implements MethodInterceptor {

	@Resource
	private UserService userService;
	@Resource
	private ModelContainer modelContainer;
	
	private Logger log = Logger.getLogger(RbacInterceptor.class);
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		boolean hasPermission = true, accessMenu = true;
		Long currentUserId = (Long) SessionContext.getSession().getAttribute(User.CURRENT_USER_ID);
		Role currentRole = null;
		User currentUser = null;
		HttpServletRequest currentServletRequest = SessionContext.getRequest();
		String from = currentServletRequest.getRequestURL()
				+(currentServletRequest.getQueryString()==null?"":("?"+currentServletRequest.getQueryString()));
		Method method = methodInvocation.getMethod();
		if((method.isAnnotationPresent(LoginRequired.class)||method.getDeclaringClass().isAnnotationPresent(LoginRequired.class))
				&&currentUserId==null){//需要登录的操作，如果没有登录返回登录页面
		    SessionContext.getResponse().sendRedirect(SessionContext.getFullPath()+"/user/login?from="+from);
		    return null;
		}
		
		if(currentUserId!=null){
			currentUser = userService.get(currentUserId);
			currentRole = currentUser.getRole();
		}
		// 权限拦截
		if (method.isAnnotationPresent(PermissionMapping.class)) {
			if(currentRole==null){
				hasPermission = false;
			} else {
				if (currentRole.hasPermission(methodInvocation.getMethod().getAnnotation(PermissionMapping.class)
						.code())) {
				    log.debug("当前角色具有"+ methodInvocation.getMethod().getAnnotation(PermissionMapping.class).code() + "权限");
					hasPermission = true;
				} else {
				    log.warn("当前角色没有"+ methodInvocation.getMethod().getAnnotation(PermissionMapping.class).code() + "权限");
					hasPermission = false;
				}
			}
		}
		// 菜单拦截
        if (method.isAnnotationPresent(MenuMapping.class)) {
            if(method.isAnnotationPresent(NoMenuIntercept.class) || method.getDeclaringClass().isAnnotationPresent(NoMenuIntercept.class)){
                accessMenu = true;
            }else if (currentRole == null) {
                SessionContext.getResponse().sendRedirect("user/login?from="+from);
            } else {
                if (currentRole.hasMenu((method.getAnnotation(MenuMapping.class).code()))) {
                    log.debug("当前角色可以访问 " + method.getAnnotation(MenuMapping.class).code() + "菜单");
                    accessMenu = true;
                } else {
                    log.warn("当前角色不能访问 " + method.getAnnotation(MenuMapping.class).code() + "菜单");
                    accessMenu = false;
                }
            }
        }
		//日志记录
        if (method.isAnnotationPresent(LogRecord.class)) {
            if (currentUserId != null) {// 只对登录的用户进行日志处理,主要处理操作日志
                LogRecord logRecord = method.getAnnotation(LogRecord.class);
                Log log = new Log(currentUser.getUserName(), currentUserId, logRecord.action());
                modelContainer.enhanceModel(log).save();
            }

        }
			//具有相应的权限和菜单
        if (hasPermission && accessMenu) {
            Object o = null;
            try {
                o = methodInvocation.proceed();
            } catch (Exception e) {// 捕获系统级日志,记录详细信息
                String message = e.getClass().getName() + ":" + e.getLocalizedMessage();
                StringBuffer bf = new StringBuffer(message + "\n");
                for (StackTraceElement se : e.getStackTrace()) {
                    bf.append("at " + se.getClassName()
                                            + "."
                                            + se.getMethodName()
                                            + "("
                                            + se.getFileName()
                                            + ":"
                                            + se.getLineNumber()
                                            + ")\n");
                }
                log.error(bf);
                message = Strings.subString(message, 0, 200);
                
                if (currentUser == null) {
                   new Log("Not Login", -1L, message, bf.toString(), Constants.SYSTEM_LOG).save();
                } else {
                    new Log(currentUser.getUserName(), currentUserId, message, bf.toString(), Constants.SYSTEM_LOG).save();
                }
                return WebResponse.build().setPermission(false).setSuccess(false).setThrowable(e);
            }
            return o;

        }
		
		//不具有权限或菜单,返回json数据{"permission":"noPermission"}
        if (method.isAnnotationPresent(ResponseBody.class)) {
            SessionContext.getResponse().setContentType("text/plain; charset=UTF-8");
            SessionContext.getResponse().getWriter().write("{\"permission\":\"noPermission\"}");
            return null;
        }
		return new ModelAndView("pages/accessError","hasPermission",hasPermission).addObject("accessMenu", accessMenu);
	}

}
