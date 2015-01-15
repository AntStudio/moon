package org.moon.rbac.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.rbac.domain.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * 权限数据库操作
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Repository
public interface PermissionRepository extends BaseRepository<Permission>{

	public void batchSave(@Param("permissions") List<Permission> permissions);
	
	public void assignAddPermission(@Param("pids") Long[] pids, @Param("rids") Long[] rids);

	public void assignDeletePermission(@Param("pids") Long[] pids, @Param("rids") Long[] rids);

	public List<Map> getPermissionsByRole(@Param("rid") Long rid);

}
