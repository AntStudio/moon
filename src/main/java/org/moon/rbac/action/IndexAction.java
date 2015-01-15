package org.moon.rbac.action;

import org.moon.core.domain.DomainLoader;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.utils.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


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
	private DomainLoader domainLoader;
	
	/**
	 * show the index page
	 * @param user
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping("/index")
	public ModelAndView index(@WebUser User user) throws Exception{

        List<Menu> menus = new ArrayList<Menu>();
        if(Objects.nonNull(user.getRoleId())){
            menus = domainLoader.load(Role.class, user.getRoleId()).getTopMenus();
        }
		return new ModelAndView("pages/index")
		.addObject("currentUser",user)
		.addObject("menus",menus);
	}
	
	@RequestMapping("/")
	public void home(HttpServletResponse response) throws Exception{
		response.sendRedirect("index");
	}
}
