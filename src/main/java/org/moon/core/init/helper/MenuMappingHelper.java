package org.moon.core.init.helper;

import org.moon.rbac.domain.Menu;
import org.moon.utils.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * the helper used to hold all the menu definitions for what annotated {@link org.moon.rbac.domain.annotation.MenuMapping}
 * in controller and the ones config in ~system~menu.xml file when project starting,
 * then when the spring application context is ready, will flush them into the database.
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2012-12-10
 * @see org.moon.core.init.Initializer
 */
public class MenuMappingHelper {

    /**
     * the origin menus from the menu mapping annotations
     */
    private static List<Menu> mappingMenus = new ArrayList<>();

    /**
     * the menus which grouped by the menu parent code
     */
    private static Map<String, List<Menu>> menusByParentCode = new HashMap<String, List<Menu>>();

    /**
     * add a menu into menu mapping helper
     * @param menu the new menu
     */
    public static void addMappingMenu(Menu menu) {
        mappingMenus.add(menu);

        List<Menu> menus = menusByParentCode.get(menu.getParentCode());
        if (Objects.isNull(menus)) {
            menus = new ArrayList<>();
            menusByParentCode.put(menu.getParentCode(), menus);
        }
        menus.add(menu);
    }


    public static List<Menu> getMappingMenus() {
        return mappingMenus;
    }

    public static Map<String, List<Menu>> getMenusByParentCode() {
        return menusByParentCode;
    }
}
