package org.moon.rbac.domain.eventsender;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.domain.eventsender.EventSender;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.utils.Maps;

/**
 * @author Gavin
 * @date Jul 3, 2014
 */
@Introduce("message")
public class RoleEventSender implements EventSender {

	@Send("user/get")
	public DomainMessage getUser(Long id) {
		return new DomainMessage(id);
	}

	@Send("menu/getTopMenusForRole")
	public DomainMessage getTopMenusForRole(Role role) {
		return new DomainMessage(role.getId());
	}

	@Send("permission/getPermissionForRole")
	public DomainMessage getPermissionForRole(Role role) {
		return new DomainMessage(role.getId());
	}
	
	@Send("role/getSubRoles")
	public DomainMessage getSubRoles(Role role) {
		return new DomainMessage(role.getId());
	}

	@Send("role/hasPermission")
	public DomainMessage hasPermission(Role role, String permissionCode) {
		return new DomainMessage(Maps.mapIt("roleId", role.getId(), "code", permissionCode));
	}

	@Send("role/hasMenu")
	public DomainMessage hasMenu(Role role,String menuCode) {
		return new DomainMessage(Maps.mapIt("roleId", role.getId(), "code", menuCode));
	}
	
	@Send("role/assign")
	public DomainMessage assignRoleToUser(Role role,User user){
		return new DomainMessage(Maps.mapIt("roleId",role.getId(),"userId",user.getId()));
	}
	
	@Send("role/get")
	public DomainMessage getParentRole(Role role) {
		return new DomainMessage(role.getParentId());
	}
}
