package org.moon.utils;

import org.moon.core.session.SessionContext;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.support.theme.Theme;
import org.moon.support.theme.ThemeManager;

/**
 * helper class for {@link org.moon.support.theme.Theme}
 * @author GavinCook
 * @since 1.0.0
 **/
public class Themes {

    private static String themeCookieName = "MOON_THEME";

    /**
     * try to get the theme from the theme manager
     * @param themeManager theme manager, can be <code>null</code>
     */
    public static Theme getThemeIfPresent(ThemeManager themeManager){
        if(Objects.isNull(themeManager)){
            return null;
        }
        return Objects.safeGetValue(()->
                        themeManager.getTheme( HttpUtils.getCookie(SessionContext.getRequest(), themeCookieName).getValue()),
                themeManager.getDefaultTheme());
    }

    public static Theme getThemeIfPresent(){
        ThemeManager themeManager = ApplicationContextHelper.getBean(ThemeManager.class);
        if(Objects.isNull(themeManager)){
            return null;
        }
        return Objects.safeGetValue(()->
                        themeManager.getTheme( HttpUtils.getCookie(SessionContext.getRequest(), themeCookieName).getValue()),
                themeManager.getDefaultTheme());
    }


}
