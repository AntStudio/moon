package org.moon.rbac.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.moon.rbac.domain.Permission;
import org.springframework.stereotype.Repository;


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
	
	public Integer getPermission_count(Map<String,Object> paramMap);
	
	public void assignAddPermission(@Param("pids")Long[] pids,@Param("rids")Long[] rids);
	
	public void assignDeletePermission(@Param("pids")Long[]  pids,@Param("rids")Long[] rids);
	
	public List<Long> getPermissionsByRole(Map<String,Object> paramsMa);
	
	public Integer getPermissionsByRole_count(Map<String,Object> paramsMap);
	
}
