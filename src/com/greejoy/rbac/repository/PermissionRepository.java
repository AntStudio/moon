package com.greejoy.rbac.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.greejoy.rbac.domain.Permission;

/**
 * 权限数据库操作
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Repository
public interface PermissionRepository {

	public void save(@Param("permission")Permission permission);
	
	public void update(@Param("permission")Permission permission);
	
	public void batchSave(@Param("permissions")List<Permission> permissions);
	
	public void delete(@Param("permissions")List<Permission> permissions);
	
	public Permission get(@Param("id")Long id);
	
	public List<Long> getPermissions(Map<String,Object> paramsMap);
	
	public Long getPermission_count(Map<String,Object> paramMap);
	
	public void assignAddPermission(@Param("pids")List<Long> pids,@Param("rid")Long rid);
	
	public void assignDeletePermission(@Param("pids")List<Long>  pids,@Param("rid")Long rid);
	
	public List<Long> getPermissionsByRole(Map<String,Object> paramsMa);
	
	public Long getPermissionsByRole_count(Map<String,Object> paramsMap);
	
}
