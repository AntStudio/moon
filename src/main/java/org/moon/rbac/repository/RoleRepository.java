package org.moon.rbac.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.rbac.domain.Role;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface RoleRepository extends BaseRepository<Role>{

	public List<Long> getSubRoles(@Param("rid") Long rid,@Param("deleteFlag")boolean deleteFlag);
	
	public void assign(@Param("rid")Long rid,@Param("uid")Long uid);
	
	public boolean hasPermission(@Param("rid")Long rid,@Param("code")String code);
	
	public boolean hasMenu(@Param("rid")Long rid,@Param("code")String code);
}
