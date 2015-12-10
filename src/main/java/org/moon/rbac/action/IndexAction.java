package org.moon.rbac.action;

import org.moon.core.domain.DomainLoader;
import org.moon.core.session.SessionContext;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.support.theme.Theme;
import org.moon.support.theme.ThemeManager;
import org.moon.utils.Objects;
import org.moon.utils.Themes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


/**
 * the action controller for index page
 *
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
@Controller
@LoginRequired
public class IndexAction {
    @Resource
    private DomainLoader domainLoader;

    @Resource
    private SystemSettingService systemSettingService;

    @Autowired(required = false)
    private ThemeManager themeManager;
    /**
     * show the index page
     *
     * @param user
     * @return
     * @throws Exception
     */
    @RequestMapping("/index")
    public ModelAndView index(@WebUser User user, HttpServletResponse response) throws Exception {

        List<Menu> menus = new ArrayList<Menu>();
        if (Objects.nonNull(user.getRoleId())) {
            menus = domainLoader.load(Role.class, user.getRoleId()).getTopMenus();
        }
        String index = systemSettingService.getSetting("user.index.userType"+user.getType(), SessionContext.getContextPath()+"/index");
        if(!index.toLowerCase().startsWith("http://")){
            index = SessionContext.getContextPath()+index;
        }

        if(index.equalsIgnoreCase(SessionContext.getContextPath()+"/index")) {
            return new ModelAndView(Objects.safeGetValue(()->Themes.getThemeIfPresent(themeManager).getIndexPage(),"pages/index"))
                    .addObject("currentUser", user)
                    .addObject("menus", menus);
        }else{
            response.sendRedirect(index);
            return null;
        }
    }

    @RequestMapping("/")
    public void home(HttpServletResponse response) throws Exception {
        response.sendRedirect(SessionContext.getContextPath()+"/index");
    }

    @RequestMapping("/cookie")
    public void home(@RequestParam("cookie")String cookie,HttpServletResponse response) throws Exception {
        response.addCookie(new Cookie("MOON_THEME",cookie));
    }
}
