package com.greejoy.rbac.domain.repository;

import com.greejoy.rbac.domain.User;
import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * load user for other domain
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Introduce("message")
public class UserEvent {

	@Send("getUser")
	public DomainMessage getUser(Long id){
		return new DomainMessage(id);
	}
	
	@Send("getUsersByCreater")
	public DomainMessage getUsers(User user){
		return new DomainMessage(user.getId());
	}
	
	@Send("addUser")
	public DomainMessage addUser(User user){
		return new DomainMessage(user);
	}
	
	@Send("updateUser")
	public DomainMessage update(User user){
		return new DomainMessage(user);
	}
}
