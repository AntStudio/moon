package org.moon.rbac.domain.repository;

import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;

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

	@Send("role/assignRole2User")
	public DomainMessage assign(Role role,User user){
		Object[] params = {role,user};
		return new DomainMessage(params);
	}
	
	@Send("role/getSubRoles")
	public DomainMessage getSubRoles(Role role){
		return new DomainMessage(role);
	}
	
	@Send("role/hasPermission")
	public DomainMessage hasPermission(Role role,String code){
		Object[] params = {role.getId(),code};
		return new DomainMessage(params);
	}
	
	@Send("role/accessMenu")
	public DomainMessage accessMenu(Role role,String code){
		Object[] params = {role.getId(),code};
		return new DomainMessage(params);
	}
	
	@Send("role/get")
    public DomainMessage get(Long id){
        return new DomainMessage(id);
    }
}
