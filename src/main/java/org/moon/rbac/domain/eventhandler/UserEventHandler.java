package org.moon.rbac.domain.eventhandler;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.service.RoleService;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * the handler of event related to user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Component
public class UserEventHandler extends BaseEventHandler<User>{
	
	@Resource
	private RoleService roleService;
	
	@OnEvent("role/get")
	public Role getRole(Long roleId){
		Assert.notNull(roleId,"Can't get role for null id");
		return roleService.get(roleId);
	}
}
