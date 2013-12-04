package org.antstudio.rbac.repository;

import java.util.List;

import org.antstudio.rbac.domain.Role;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends BaseRepository<Role>{

	public List<Long> getSubRoles(@Param("rid") Long rid,@Param("deleteFlag")boolean deleteFlag);
	
	public void save(@Param("role")Role role);
	
	public void delete(@Param("ids") Long[] ids);
	
	public void logicDelete(@Param("ids") Long[] ids);
	
	public void update(@Param("role")Role role);
	
	public List<Long> getRolesByPermission(@Param("pid") Long pid);
	
	public boolean hasPermission(@Param("rid")Long rid,@Param("code")String code);
	
	public boolean accessMenu(@Param("rid")Long rid,@Param("code")String code);
}
