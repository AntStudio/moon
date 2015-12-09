package org.moon.rbac.service.impl;

import com.reeham.component.ddd.model.ModelContainer;
import org.moon.base.service.AbstractDomainService;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.Role;
import org.moon.rbac.repository.RoleRepository;
import org.moon.rbac.service.RoleService;
import org.moon.utils.Constants;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl extends AbstractDomainService<Role> implements RoleService {

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
	public List<Map<String, Object>> getRolesWithStatusForPermission(
            Permission permission, Long rid) {
		return roleRepository.getRolesWithStatusForPermission(rid,permission.getId());
	}

	@Override
	public List<Map> getTopRoles() {
        return roleRepository.getSubRoles(null, false);
	}

	@Override
	public List<Map<String, Object>> getAllRoles() {
		return roleRepository.getAllRoles();
	}

}
