package org.antstudio.rbac.service.impl;

import javax.annotation.Resource;

import org.antstudio.rbac.domain.Permission;
import org.antstudio.rbac.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * 权限事件处理器
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Component
public class PermissionEventHandler {

	@Resource
	private PermissionRepository permissionRepository;
	
	
	@OnEvent("saveOrUpdatePermission")
	public void saveOrUpdate(Permission permission){
		
		if(permission.getId()==null)
			permissionRepository.save(permission);
		else
			permissionRepository.update(permission);
		
	}
	
}
