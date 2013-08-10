package com.greejoy.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.greejoy.rbac.domain.Role;
import com.greejoy.rbac.repository.MenuRepository;
import com.greejoy.rbac.repository.RoleRepository;
import com.greejoy.rbac.service.RoleService;
import com.greejoy.utils.ClassPropertiesUtil;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private ModelContainer modelContainer;
	
	@Resource
	private RoleRepository roleRepository;

	@Resource
	private MenuRepository menuRepository;
	
	@Override
	public Role getModel(Long id) {
		if(id==null||id<0)
			return null;
		return (Role) modelContainer.getModel(ModelUtils.asModelKey(Role.class, id),this);
	}

	@Override
	public Role get(Long id) {
		return roleRepository.get(id);
	}

	@Override
	public Object loadModel(Object identifier) {
		return get((Long)identifier);
	}

	@Override
	public List<Role> getSubRoles(Long rid,boolean deleteFlag) {
		return modelContainer.identifiersToModels((List)roleRepository.getSubRoles(rid, deleteFlag), Role.class, this);
	}

	@Override
	public List<Map<String, Object>> getSubRolesForMap(Long rid,
			boolean deleteFlag) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Role role: getSubRoles(rid,deleteFlag)){
			list.add(role.toMap());
		}
		return list;
	}

	@Override
	public void addRole(Role role) {
		modelContainer.enhanceModel(role).save();
	}

	@Override
	public void delete(Long[] ids, boolean logicDel) {
		Role role ;
		
		if(logicDel){
			for(Long id:ids){
				role = (Role) modelContainer.getModel(ModelUtils.asModelKey(Role.class,id));
				if(role!=null){
					role.setDeleteFlag(true);//更新缓存
				}
			}
			roleRepository.logicDelete(ids);//sendLogicDeleteMessage(ids);
		}
		else{
			for(Long id:ids){
				modelContainer.removeModel(ModelUtils.asModelKey(Role.class,id));//将物理删除的对象移除缓存
			}
			roleRepository.delete(ids);//sendDeleteMessage(ids);
		}
		
	}

	@Override
	public boolean update(Role role) {
		Role oldRole = getModel(role.getId());
		oldRole = (Role) ClassPropertiesUtil.copyProperties(role,oldRole,true,"roleName");
		oldRole.update();
		//test(oldUser);
		return true;
	}

	@Override
	public List<Map<String, Object>> getAllRoleDataByPermission(Long pid, Long rid) {
		List<Role> roles = getRoleByPermission(pid);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Role role:getSubRoles(rid, false)){
            if(roles.contains(role)){
            	list.add(addProperty(role.toMap(),"checked",true));
            }
            else{
            	list.add(addProperty(role.toMap(),"checked",false));
            }
		}
		return list;
	}

	private Map<String,Object> addProperty(Map<String,Object> m,String key,Object value){
		m.put(key, value);
		return  m;
		
	}
	@Override
	public List<Role> getRoleByPermission(Long pid) {
		return modelContainer.identifiersToModels((List)roleRepository.getRolesByPermission(pid), Role.class, this);
	}




}
