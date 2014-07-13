package org.moon.rbac.repository;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.rbac.domain.Role;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends BaseRepository<Role>{

	public List<Long> getSubRoles(@Param("rid") Long rid,@Param("deleteFlag")boolean deleteFlag);
	
	public void assign(@Param("rid")Long rid,@Param("uid")Long uid);
	
	public boolean hasPermission(@Param("rid")Long rid,@Param("code")String code);
	
	public boolean hasMenu(@Param("rid")Long rid,@Param("code")String code);
}
