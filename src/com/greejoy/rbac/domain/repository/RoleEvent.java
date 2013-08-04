package com.greejoy.rbac.domain.repository;

import com.greejoy.rbac.domain.Role;
import com.greejoy.rbac.domain.User;
import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-2
 */
@Introduce("message")
public class RoleEvent {

	@Send("assignRole2User")
	public DomainMessage assign(Role role,User user){
		Object[] params = {role,user};
		return new DomainMessage(params);
	}
	
	@Send("getRole")
	public DomainMessage getRole(User user){
		return new DomainMessage(user.getRoleId());
	}
	
	@Send("getSubRoles")
	public DomainMessage getSubRoles(Role role){
		return new DomainMessage(role);
	}
	
	@Send("saveRole")
	public DomainMessage save(Role role){
		return new DomainMessage(role);
	}
	
	@Send("updateRole")
	public DomainMessage update(Role role){
		return new DomainMessage(role);
	}
	
	@Send("hasPermission")
	public DomainMessage hasPermission(Role role,String code){
		Object[] params = {role.getId(),code};
		return new DomainMessage(params);
	}
	@Send("accessMenu")
	public DomainMessage accessMenu(Role role,String code){
		Object[] params = {role.getId(),code};
		return new DomainMessage(params);
	}
}
