package org.moon.rbac.service;

import org.moon.base.service.BaseService;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.Role;
import org.moon.utils.Constants;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseService<Role>{

	public static Role systemRole = new Role(Constants.SYSTEM_ROLEID);
	/**
	 * 根据权限获取父级为rid的角色信息,如果该权限分配给了角色，那么checked属性为true，否者为false
	 * @param pid
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getAllRolesByPermission(Permission permission,Long rid);
	
	/**
	 * 获取顶级角色
	 * @return
	 */
	public List<Role> getTopRoles();
}
