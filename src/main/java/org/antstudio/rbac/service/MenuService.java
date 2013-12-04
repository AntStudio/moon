package org.antstudio.rbac.service;

import java.util.List;
import java.util.Map;

import org.antstudio.rbac.domain.Menu;
import org.antstudio.rbac.domain.Role;

import com.reeham.component.ddd.model.ModelLoader;

/**
 * the service interface for the menu
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public interface MenuService extends BaseService<Menu>,ModelLoader{

	public List<Menu> getSubMenus(Long parentId);
	 
	public List<Menu> getSubMenusByRole(Long parentId,Long rid);
	
	public List<Map<String,Object>> getSubMenusByRoleForMap(Long parentId,Long rid);
	
	public List<Map<String,Object>> getSubMenusForMap(Long parentId);

	public List<Menu> getTopMenusByRole(Long rid);
	
	public List<Map<String,Object>> getAssignMenuData(Long pid,Long rid);
	
	public void assignMenuToRole(Long[] menuIds,Boolean[] checkStatus,Long rid);
	
	public void addMenusToRole(List<Menu> menus,Long rid);
	
	public void removeMenuToRole(List<Menu> menus,Long rid);
	
	/**
	 * @param system    if <code>system is true</code>,get the system menus,which with @MenuMapping annotation,
	 * 					if system is <code>false</code>,get the customer menus,which created in the menus management
	 * 					if system is <code>null</code>,get all the menus 
	 * @return
	 */
	public List<Menu> getAllMenus(Boolean system);
	
	public Map<String,Menu> getSystemMenusByCode();
	
	public void addMenus(List<Menu> menus);
	
	public void deleteMenus(List<Menu> menus);
	
	public void update(Menu menu);
	
	
	public Menu getMenuByCode(String code);
	
}
