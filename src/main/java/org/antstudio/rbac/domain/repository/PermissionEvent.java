package org.antstudio.rbac.domain.repository;

import org.antstudio.rbac.domain.Permission;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * 权限事件发送器
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Introduce("message")
public class PermissionEvent {

	@Send("saveOrUpdatePermission")
	public DomainMessage saveOrUpdate(Permission permission){
		return new DomainMessage(permission);
	}
	
	
}
