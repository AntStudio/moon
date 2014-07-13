package org.moon.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.base.service.AbstractService;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.Role;
import org.moon.rbac.repository.RoleRepository;
import org.moon.rbac.service.RoleService;
import org.moon.utils.Constants;
import org.moon.utils.Maps;
import org.moon.utils.Objects;
import org.springframework.stereotype.Service;

import com.reeham.component.ddd.model.ModelContainer;

@Service
public class RoleServiceImpl extends AbstractService<Role> implements RoleService {

	@Resource
	private ModelContainer modelContainer;

	@Resource
	private RoleRepository roleRepository;

	@Override
	public Role get(Long id) {
		if(Constants.SYSTEM_ROLEID.equals(id)){
			systemRole.enhanceIfNecessary();
			return systemRole;
		}
		return super.get(id);
	}
	
	@Override
	public List<Map<String, Object>> getAllRolesByPermission(
			Permission permission, Long rid) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Role> roles;
		if (Objects.isNull(rid)) {
			roles = getTopRoles();
		} else {
			roles = get(rid).getSubRoles();
		}

		for (Role role : roles) {
			list.add(Maps.mapIt("id", role.getId(), 
								"roleName",role.getRoleName(),
								"checked",role.hasPermission(permission.getCode()))
				     );
		}

		return list;
	}

	@Override
	public List<Role> getTopRoles() {
		return modelContainer.identifiersToModels(
				(List) roleRepository.getSubRoles(null, false), Role.class,
				this);
	}

}
