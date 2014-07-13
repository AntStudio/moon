package org.moon.rbac.domain.eventhandler;

import java.util.List;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.repository.PermissionRepository;
import org.moon.rbac.service.PermissionService;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;

/**
 * @author Gavin
 * @date Jul 9, 2014
 */
@Component
public class PermissionEventHandler extends BaseEventHandler<Permission, PermissionService>{

	@Resource
	private PermissionRepository permissionRepository;
	
	@Resource
	private ModelContainer modelContainer;
	
	@OnEvent("permission/getPermissionForRole")
	public List<Permission> getPermissionForRole(Long rid){
		return modelContainer.identifiersToModels((List)permissionRepository.getPermissionsByRole(rid),Permission.class,this.service);
	}
	
}
