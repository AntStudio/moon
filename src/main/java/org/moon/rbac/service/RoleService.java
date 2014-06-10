package org.moon.rbac.service;

import java.util.List;
import java.util.Map;

import org.moon.base.service.BaseService;
import org.moon.rbac.domain.Role;

import com.reeham.component.ddd.model.ModelLoader;

public interface RoleService extends BaseService<Role>,ModelLoader{

	public List<Role> getSubRoles(Long rid,boolean deleteFlag);
	
	public List<Map<String,Object>> getSubRolesForMap(Long rid,boolean deleteFlag); 
	
	public void delete(Long[] ids, boolean logicDel);
	
	/**
	 * 根据权限获取所有的角色信息,如果该权限分配给了角色，那么checked属性为true，否者为false
	 * @param pid
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getAllRoleDataByPermission(Long pid,Long rid);
	
	/**
	 * 根据权限获取对应的角色信息，此处只获取分配了的角色
	 * @param pid
	 * @return
	 */
	public List<Role> getRoleByPermission(Long pid);
	
}
