package org.moon.rbac.service.impl;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.repository.RoleRepository;
import org.moon.rbac.repository.UserRepository;
import org.moon.utils.Constants;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-2
 */
@Component
public class RoleEventHandler extends BaseEventHandler<Role>{

	
	@Resource
	private UserRepository userRepository;
	@Resource
	private RoleRepository roleRepository;
	
	@OnEvent("role/assignRole2User")
	public void assign(Object[] params){
		Long rid = ((Role)params[0]).getId();
		Long uid = ((User)params[1]).getId();
		userRepository.assign(rid, uid);
	}
	
	@OnEvent("role/hasPermission")
	public boolean hasPermission(Object[] params){
		Long rid = (Long) params[0];
		String code = (String) params[1];
        if (Constants.SYSTEM_ROLEID.equals(rid)) {
            return true;
        } else {
            return roleRepository.hasPermission(rid, code);
        }
		
	}
	
	@OnEvent("role/accessMenu")
	public boolean accessMenu(Object[] params){
		Long rid = (Long) params[0];
		String code = (String) params[1];
        if (Constants.SYSTEM_ROLEID.equals(rid)) {
            return true;
        } else {
            return roleRepository.accessMenu(rid, code);
        }
	}

    @Override
    public Role save(Role role) {
        roleRepository.save(role);
        return role;
    }

    @Override
    public void delete(Role role) {
        
    }

    @Override
    public void update(Role role) {
        roleRepository.update(role);
    }
}
