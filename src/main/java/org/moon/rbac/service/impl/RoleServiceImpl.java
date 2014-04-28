package org.moon.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.rbac.domain.Role;
import org.moon.rbac.repository.RoleRepository;
import org.moon.rbac.service.RoleService;
import org.springframework.stereotype.Service;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;

@Service
public class RoleServiceImpl implements RoleService {

	@Resource
	private ModelContainer modelContainer;
	
	@Resource
	private RoleRepository roleRepository;

	@Override
	@OnEvent("role/get")
	public Role get(Long id) {
        if (id == null || id < 0) {
            return null;
        }
		return (Role) modelContainer.getModel(ModelUtils.asModelKey(Role.class, id),this);
	}

	@Override
	public Role load(Long id) {
		return roleRepository.get(id);
	}

	@Override
	public Object loadModel(Object identifier) {
		return load((Long)identifier);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	@OnEvent("role/getSubRoles")
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
			roleRepository.delete(ids);
		}
		
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Role> getRoleByPermission(Long pid) {
		return modelContainer.identifiersToModels((List)roleRepository.getRolesByPermission(pid), Role.class, this);
	}




}
