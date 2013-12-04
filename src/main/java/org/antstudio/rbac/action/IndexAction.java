package org.antstudio.rbac.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.antstudio.rbac.domain.User;
import org.antstudio.rbac.domain.annotation.LoginRequired;
import org.antstudio.rbac.service.MenuService;
import org.antstudio.rbac.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * the action controller for index page
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
@Controller
@LoginRequired
public class IndexAction {
	@Resource
	MenuService menuService;
	
	@Resource
	private UserService userService;
	
	/**
	 * show the index page
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/index")
	//@LogRecord(action="登录系统,进入主页")
	public ModelAndView index(HttpServletRequest request) throws Exception{
		//throw new Exception("出错了");
		User currentUser = userService.getCurrentUser(request);
		return new ModelAndView("pages/index")
		.addObject("currentUser",currentUser)
		.addObject("menus",menuService.getTopMenusByRole(currentUser.getRoleId()));
	}
	
}
