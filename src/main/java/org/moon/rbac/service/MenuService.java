package org.moon.rbac.service;

import org.moon.base.service.BaseDomainService;
import org.moon.rbac.domain.Menu;

import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public interface MenuService extends BaseDomainService<Menu> {

	/**
	 * 获取角色可访问的菜单
	 * @param parentMenuId 为父菜单id,若为<code>null</code>,则表示获取角色对应的顶级菜单
	 * @param rid 如果为<code>Constants.SYSTEM_ROLEID</code>,则表示不考虑角色的菜单分配，直接获取子菜单
	 * @return
	 */
	public List<Map> getSubMenusForRole(Long parentMenuId, Long rid);
	
	/**
	 * 获取带有是否分配给对应角色状态的菜单(这里会返回所有的菜单)，用于管理分配菜单时使用
	 * @param pid 父菜单id
	 * @param rid 角色id
	 * @return
	 */
	public List<Map<String,Object>> getMenusWithStatus(Long pid, Long rid);

	/**
	 * 获取顶级菜单
	 * @return
	 */
	public List<Map> getTopMenus();

    /**
     * 给角色分配菜单
     * @param menuIds
     * @param checkStatus 状态数组,<code>true</code>表示分配,<code>false</code>表示取消分配
     * @param rid
     */
	public void assignMenuToRole(Long[] menuIds, Boolean[] checkStatus, Long rid);
	
	/**
	 * 对菜单进行排序
	 * @param parentId 需要排序的菜单的父菜单id
	 * @param childrenIds 需要排序菜单id数组
	 */
	public void sortMenus(Long parentId, Long[] childrenIds);
	
	public Menu getMenuByCode(String code);

	/**
	 * 获取系统数据库中已经有了的系统菜单返回形式：{code-->menu}
	 * @return
	 */
	public List<Map> getSystemMenus();
	
	/**
	 * 批量添加菜单，用于系统启动时添加菜单
	 * @param menus
	 */
	public void addMenus(List<Menu> menus);

    /**
     * 添加菜单，会获取到插入记录的id
     * @param params
     */
    public void add(Map<String, Object> params);
}
