package org.moon.rbac.service.impl;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelLoader;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.base.service.AbstractDomainService;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.repository.PermissionRepository;
import org.moon.rbac.service.PermissionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限服务实现类
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Service
public class PermissionServiceImpl extends AbstractDomainService<Permission> implements PermissionService,ModelLoader{
	
	@Resource
	private PermissionRepository permissionRepository;
	@Resource
	private ModelContainer modelContainer;
	
	
	
	@Override
	public void batchSave(List<Permission> permissions) {
		if(permissions.size()!=0)
		permissionRepository.batchSave(permissions);
	}

	@Override
	public Object loadModel(Object identifier) {
		return load((Long) identifier);
	}


	@Override
	@OnEvent("permission/get")
	public Permission get(Long id) {
		return (Permission) modelContainer.getModel(ModelUtils.asModelKey(Permission.class, id), this);
	}

	@Override
    public void assignPermission(Long[] pids, Boolean[] status, Long[] rids) {
	    boolean singlePermission = (pids.length==1);
        List<Long> adds = new ArrayList<Long>();
        List<Long> deletes = new ArrayList<Long>();
        if(!singlePermission){//给角色分配权限 1角色--》多权限
            for (int i = 0, j = status.length; i < j; i++) {
                if(pids[i]>0){
                    if (status[i]) {
                        adds.add(pids[i]);
                    } else {
                        deletes.add(pids[i]);
                    }
                }
            }
            if (adds.size() > 0) {
                permissionRepository.assignAddPermission(adds.toArray(new Long[0]), rids);
            }
            if (deletes.size() > 0) {
                permissionRepository.assignDeletePermission(deletes.toArray(new Long[0]), rids);
            }
        }else{//给角色分配权限 1权限--》多角色
            for (int i = 0, j = status.length; i < j; i++) {
                if(rids[i]>0){
                    if (status[i]) {
                        adds.add(rids[i]);
                    } else {
                        deletes.add(rids[i]);
                    }
                }
            }
            if (adds.size() > 0) {
                permissionRepository.assignAddPermission(pids, adds.toArray(new Long[0]));
            }
            if (deletes.size() > 0) {
                permissionRepository.assignDeletePermission(pids, deletes.toArray(new Long[0]));
            }
        }
       
    }

	@Override
	public Map<String, Permission> getPermissionsByCode() {
		Map<String,Permission> m = new HashMap<String,Permission>();
		List<Permission> permissions = listForDomain(null);
		for(Permission p:permissions){
			m.put(p.getCode(), p);
		}
		return m;
	}
}
