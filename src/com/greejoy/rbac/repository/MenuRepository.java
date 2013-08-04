package com.greejoy.rbac.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.greejoy.rbac.domain.Menu;
import com.greejoy.rbac.domain.User;

/**
 * the repository component for menu
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Repository
public interface MenuRepository extends BaseRepository<Menu>{

	public Long getByCode(@Param("code")String code);
	
	public List<Long> getSubMenu(@Param("pid")Long parentId);

	public List<Long> getSubMenuByRole(@Param("pid")Long parentId,@Param("rid")Long rid);
	
	public void addMenusToRole(@Param("menus")List<Menu> menus,@Param("rid")Long rid);
	
	public void removeMenusFromRole(@Param("menus")List<Menu> menus,@Param("rid")Long rid);
	
	public List<Long> getAllMenus(@Param("system")Boolean system,@Param("deleteFlag")boolean deleteFlag);
	
	public void addMenus(@Param("menus")List<Menu> menus);
	
	public void deleteMenus(@Param("menus")List<Menu> menus);
	
	public void update(@Param("menu")Menu menu);
}
