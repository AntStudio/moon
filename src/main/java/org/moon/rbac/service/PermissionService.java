package org.moon.rbac.service;

import java.util.List;
import java.util.Map;

import org.moon.base.service.BaseService;
import org.moon.pagination.Pager;
import org.moon.rbac.domain.Permission;


/**
 * 权限服务接口
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
public interface PermissionService extends BaseService<Permission>{

	/**
	 * 批量添加权限信息
	 * @param permissions
	 */
	public void batchSave(List<Permission> permissions);
	
	/**
	 * 批量删除权限信息
	 * @param permissions
	 */
	public void delete(List<Permission> permissions);
	
	
	/**
	 * 获取所有的权限信息，以code-->permission的map形式返回
	 * @return
	 */
	public Map<String,Permission> getPermissionsByCode(Map<String,Object> paramsMap);
	
	/**
	 * 从缓存中获取权限信息
	 * @param id
	 * @return
	 */
	public Permission get(Long id);
	
	/**
	 * 获取所有的权限信息
	 * @return
	 */
	public List<Permission> getPermissions(Map<String,Object> paramsMap);
	
	/**
	 * 查询权限分页信息
	 * @return
	 */
	public List<Map<String,Object>> getPermissionsForMap(Map<String,Object> paramsMap);
	
	/**
	 * 查询权限分页信息
	 * @return
	 */
	public Pager getPermissionsForPage(Map<String, Object> paramsMap);
	
	/**
	 * 分配权限给角色
	 * @param pids
	 * @param rid
	 */
	public void assignPermission(Long[] pids,Boolean[] status,Long[] rids);
	
	/**
	 * 根据角色获取权限信息
	 * @param rid
	 * @return 返回分页信息
	 */
	public Pager getPermissionsByRoleForPage(Map<String,Object> paramsMap);
	
	/**
	 * 根据角色获取权限信息
	 * @param rid
	 * @return 返回map
	 */
	public List<Map<String,Object>> getPermissionsByRoleForMap(Map<String,Object> paramsMap);
	
}
