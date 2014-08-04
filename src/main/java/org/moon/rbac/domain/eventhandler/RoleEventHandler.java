package org.moon.rbac.domain.eventhandler;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Role;
import org.moon.rbac.repository.RoleRepository;
import org.moon.rbac.service.RoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Gavin
 * @date Jul 4, 2014
 */
@Component
public class RoleEventHandler extends BaseEventHandler<Role,RoleService>{
	
	@Resource
	private RoleRepository repository;
	
	@Resource
	private ModelContainer modelContainer;
	
	@OnEvent("role/getSubRoles")
	public List<Role> getSubRoles(Long rid){
		return modelContainer.identifiersToModels((List)repository.getSubRoles(rid,false), Role.class, this.service);
	}
	
	@OnEvent("role/hasPermission")
	public boolean hasPermission(Map params){
		Long rid = (Long) params.get("roleId");
		String permissionCode = (String) params.get("code");
		return repository.hasPermission(rid, permissionCode);
	}
	
	@OnEvent("role/hasMenu")
	public boolean hasMenu(Map params){
		Long rid = (Long) params.get("roleId");
		String menuCode = (String) params.get("code");
		return repository.hasMenu(rid, menuCode);
	}
	
	@OnEvent("role/assign")
	public void assignRoleToUser(Map params){
		Long rid = (Long) params.get("roleId");
		Long uid = (Long) params.get("userId");
		repository.assign(rid, uid);
	}
}
