package org.moon.core.init.helper;

import org.moon.rbac.domain.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * the container for menu
 * @author Gavin
 * @version 1.0
 * @date 2012-12-10
 */
public class MenuMappingHelper {

	
	
	private static List<Menu> mappingMenus = new ArrayList<Menu>();
	
	private static Map<String,Menu> menusMapByCode = new HashMap<String,Menu>();

    /**
     * 将菜单归类，以父菜单的code归类
     */
    private static Map<String,List<Menu>> menusByParentCode = new HashMap<String,List<Menu>>();

    public static void addMappingMenu(Menu menu){
		mappingMenus.add(menu);
		menusMapByCode.put(menu.getCode(), menu);

        List<Menu> menus = menusByParentCode.get(menu.getParentCode());
        if(menus == null){
            menus = new ArrayList<Menu>();
            menusByParentCode.put(menu.getParentCode(),menus);
        }
        menus.add(menu);
    }
	

	public static List<Menu> getMappingMenus(){
		return mappingMenus;
	}
	 
	
	public static Map<String,Menu> getMenusMapByCode(){
		return menusMapByCode;
	}

    public static Map<String, List<Menu>> getMenusByParentCode() {
        return menusByParentCode;
    }
}
