package org.moon.rbac.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.rbac.domain.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * the repository component for menu
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Repository
public interface MenuRepository extends BaseRepository<Menu>{

    /**
     * 获取角色对应的菜单的子菜单（即分配给该角色的菜单）
     * @param parentId
     * @param rid
     * @return
     */
	public List<Map> getSubMenusByRole(@Param("pid") Long parentId, @Param("rid") Long rid);

    /**
     * 获取子菜单
     * @param parentId
     * @return
     */
    public List<Map> getSubMenus(@Param("pid") Long parentId);

	public Long getByCode(@Param("code") String code);

    public List<Map> getMenusWithStatus(@Param("pid") Long parentMenuId, @Param("rid") Long rid);

	public void addMenusToRole(@Param("menus") List<Menu> menus, @Param("rid") Long rid);
	
	public void removeMenusFromRole(@Param("menus") List<Menu> menus, @Param("rid") Long rid);
	
	public List<Long> getAllMenus(@Param("system") Boolean system, @Param("deleteFlag") boolean deleteFlag);
	
	public void addMenus(@Param("menus") List<Menu> menus);
	
	public void deleteMenus(@Param("menus") List<Menu> menus);
	
	public void update(@Param("menu") Menu menu);
	
	public void sortMenu(@Param("parentId") Long parentId,
                         @Param("childrenIds") Long[] childrenIds, @Param("parentCode") String parentCode);

    public void add(Map<String, Object> params);
}
