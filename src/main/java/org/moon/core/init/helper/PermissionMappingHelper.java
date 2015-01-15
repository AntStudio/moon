package org.moon.core.init.helper;

import org.moon.rbac.domain.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 权限映射辅助类
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
public class PermissionMappingHelper {

	/**
	 * 所有的权限信息
	 */
	private static List<Permission> mappedPermission = new ArrayList<Permission>();
	
	/**
	 * 根据code--》permission的映射存储的当前系统的权限信息	
	 */
	private static Map<String,Permission> permissionsMapByCode = new HashMap<String,Permission>();
	
	public static void addMappingPermission(Permission permission){
		mappedPermission.add(permission);
		permissionsMapByCode.put(permission.getCode(), permission);
	}
	

	public static List<Permission> getMappedPermissions(){
		return mappedPermission;
	}
	 
	
	public static Map<String,Permission> getPermissionsMapByCode(){
		return permissionsMapByCode;
	}

	
}
