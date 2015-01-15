package org.moon.rbac.service;

import org.moon.base.service.BaseDomainService;
import org.moon.rbac.domain.Permission;

import java.util.List;
import java.util.Map;


/**
 * 权限服务接口
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
public interface PermissionService extends BaseDomainService<Permission> {

	/**
	 * 批量添加权限信息
	 * @param permissions
	 */
	public void batchSave(List<Permission> permissions);
	
	/**
	 * 分配权限给角色
	 * @param pids
	 * @param rid
	 */
	public void assignPermission(Long[] pids, Boolean[] status, Long[] rids);
	
	/**
	 * 获取权限信息，用于系统启动时添加处理权限信息，返回形式：{code-->permission}
	 * @return
	 */
	public Map<String,Permission> getPermissionsByCode();
}
