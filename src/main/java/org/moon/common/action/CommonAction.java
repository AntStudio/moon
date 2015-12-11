package org.moon.common.action;

import org.moon.message.WebResponse;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.support.theme.Theme;
import org.moon.support.theme.ThemeManager;
import org.moon.support.theme.annotation.NoTheme;
import org.moon.utils.FileUtils;
import org.moon.utils.Objects;
import org.moon.utils.Strings;
import org.moon.utils.Themes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * common controller
 *
 * @author GavinCook
 * @since 1.0.0
 */
@Controller
public class CommonAction {

    @Get("/turn")
    public ModelAndView turn(@RequestParam("page") String page) {
        return new ModelAndView(page);
    }

    @Autowired(required = false)
    private ThemeManager themeManager;

    @Post("/theme")
    @ResponseBody
    public WebResponse setTheme(@RequestParam("theme") String theme, HttpServletResponse response) throws Exception {
        Cookie cookie = new Cookie("MOON_THEME", theme);
        //the theme cookie expiry time 10 years
        cookie.setMaxAge(10 * 365 * 24 * 3600);
        response.addCookie(cookie);

        return WebResponse.success();
    }

    /**
     * 主题设置页面
     * @return
     */
    @Get("/theme")
    @NoTheme
    public ModelAndView showThemeSettingPage(){
        Collection<Theme> themes = Objects.safeGetValue(()->themeManager.getThemes(), null);
        Theme currentTheme = null;
        if(themes != null && !themes.isEmpty()){
            currentTheme = Themes.getThemeIfPresent();
        }
        return new ModelAndView("pages/themeSetting", "themes", themes).addObject("currentTheme",currentTheme);
    }
}
