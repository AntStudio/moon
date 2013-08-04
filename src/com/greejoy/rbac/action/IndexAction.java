package com.greejoy.rbac.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.greejoy.rbac.domain.User;
import com.greejoy.rbac.domain.annotation.LogRecord;
import com.greejoy.rbac.domain.annotation.LoginRequired;
import com.greejoy.rbac.service.MenuService;
import com.greejoy.rbac.service.UserService;

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
