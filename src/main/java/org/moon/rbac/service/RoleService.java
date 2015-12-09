package org.moon.rbac.service;

import org.moon.base.service.BaseDomainService;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.Role;
import org.moon.utils.Constants;

import java.util.List;
import java.util.Map;

public interface RoleService extends BaseDomainService<Role> {

	public static final Role systemRole = new Role(Constants.SYSTEM_ROLEID);

	/**
	 * 根据权限获取父级为rid的角色信息,如果该权限分配给了角色，那么checked属性为true，否者为false
	 * @param permission
	 * @param rid
	 * @return
	 */
	public List<Map<String,Object>> getRolesWithStatusForPermission(Permission permission, Long rid);
	
	/**
	 * 获取顶级角色
	 * @return
	 */
	public List<Map> getTopRoles();

	/**
	 * 获取所有的角色列表
	 * @return
	 */
	public List<Map<String,Object>> getAllRoles();
}
