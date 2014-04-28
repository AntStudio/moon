package org.moon.rbac.domain.init.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.moon.rbac.domain.Menu;


/**
 * the container for menu
 * @author Gavin
 * @version 1.0
 * @date 2012-12-10
 */
public class MenuMappingHelper {

	
	
	private static List<Menu> mappingMenus = new ArrayList<Menu>();
	
	private static Map<String,Menu> menusMapByCode = new HashMap<String,Menu>();
	
	public static void addMappingMenu(Menu menu){
		mappingMenus.add(menu);
		menusMapByCode.put(menu.getCode(), menu);
	}
	

	public static List<Menu> getMappingMenus(){
		return mappingMenus;
	}
	 
	
	public static Map<String,Menu> getMenusMapByCode(){
		return menusMapByCode;
	}

	
}
