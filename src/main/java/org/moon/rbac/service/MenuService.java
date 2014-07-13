package org.moon.rbac.service;

import java.util.List;
import java.util.Map;

import org.moon.base.service.BaseService;
import org.moon.rbac.domain.Menu;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public interface MenuService extends BaseService<Menu>{

	/**
	 * 获取角色可访问的菜单
	 * @param parentMenuId 为父菜单id,若为null,则表示获取角色对应的顶级菜单
	 * @param rid
	 * @return
	 */
	public List<Menu> getSubMenusForRole(Long parentMenuId,Long rid);
	
	/**
	 * 获取带有是否分配给对应角色状态的菜单，用于管理分配菜单时使用
	 * @param pid
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getMenusWithStatus(Long pid,Long rid);
	
	
	/**
	 * 获取顶级菜单
	 * @return
	 */
	public List<Menu> getTopMenus();
	
	public void assignMenuToRole(Long[] menuIds,Boolean[] checkStatus,Long rid);
	
	/**
	 * 对菜单进行排序
	 * @param parentId 需要排序的菜单的父菜单id
	 * @param childrenIds 需要排序菜单id数组
	 */
	public void sortMenus(Long parentId,Long[] childrenIds);
	
	public Menu getMenuByCode(String code);

	/**
	 * 获取系统数据库中已经有了的系统菜单返回形式：{code-->menu}
	 * @return
	 */
	public Map<String,Menu> getSystemMenusByCode();
	
	/**
	 * 批量添加菜单，用于系统启动时添加菜单
	 * @param menus
	 */
	public void addMenus(List<Menu> menus);
}
