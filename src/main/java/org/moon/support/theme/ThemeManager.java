package org.moon.support.theme;

import java.util.Collection;

/**
 * theme manager
 * @author GavinCook
 * @since 1.0.0
 **/
public interface ThemeManager {

    /**
     * register a theme into theme manager
     * @param theme the theme
     */
    void register(Theme theme);

    /**
     * get the theme for special name, the name is case insensitive
     * @param name the theme name
     */
    Theme getTheme(String name);

    /**
     * get the default theme
     */
    Theme getDefaultTheme();

    /**
     * get all the themes which registered into current theme manager,
     * Notice: the themes collection should be unmodified
     */
    Collection<Theme> getThemes();
}
