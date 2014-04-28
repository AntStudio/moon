package org.moon.rbac.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.service.MenuService;
import org.moon.rbac.service.UserService;
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
	public ModelAndView index(HttpServletRequest request) throws Exception{
		User currentUser = userService.getCurrentUser(request);
		return new ModelAndView("pages/index")
		.addObject("currentUser",currentUser)
		.addObject("menus",menuService.getTopMenusByRole(currentUser.getRoleId()));
	}
	
	@RequestMapping("/")
	public void home(HttpServletResponse response) throws Exception{
		response.sendRedirect("index");
	}
}
