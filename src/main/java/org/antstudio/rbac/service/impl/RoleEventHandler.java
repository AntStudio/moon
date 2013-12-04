package org.antstudio.rbac.service.impl;

import java.util.List;

import javax.annotation.Resource;
import javax.management.relation.RoleResult;

import org.antstudio.rbac.domain.Role;
import org.antstudio.rbac.domain.User;
import org.antstudio.rbac.repository.RoleRepository;
import org.antstudio.rbac.repository.UserRepository;
import org.antstudio.rbac.service.RoleService;
import org.antstudio.utils.Constants;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-2
 */
@Component
public class RoleEventHandler {

	
	@Resource
	private UserRepository userRepository;
	
	@Resource
	private RoleRepository roleRepository;
	@Resource
	private RoleService roleService;
	@OnEvent("assignRole2User")
	public void assign(Object[] params){
		Long rid = ((Role)params[0]).getId();
		Long uid = ((User)params[1]).getId();
		userRepository.assign(rid, uid);
	}
	@OnEvent("getRole")
	public Role getRole(Long rid){
		return roleService.getModel(rid);
	}
	
	@OnEvent("getSubRoles")
	public List<Role> getSubRoles(Long rid){
		return roleService.getSubRoles(rid,false);
	}
	
	@OnEvent("saveRole")
	public void saveRole(Role role){
		  roleRepository.save(role);
	}
	
	@OnEvent("updateRole")
	public void updateRole(Role role){
		  roleRepository.update(role);
	}
	
	@OnEvent("hasPermission")
	public boolean hasPermission(Object[] params){
		Long rid = (Long) params[0];
		String code = (String) params[1];
		if(Constants.SYSTEM_ROLEID.equals(rid))
			return true;
		else
			return roleRepository.hasPermission(rid,code);
		
	}
	
	@OnEvent("accessMenu")
	public boolean accessMenu(Object[] params){
		Long rid = (Long) params[0];
		String code = (String) params[1];
		if(Constants.SYSTEM_ROLEID.equals(rid))
			return true;
		else
			return roleRepository.accessMenu(rid,code);
	}
}
