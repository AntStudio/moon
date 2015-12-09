package org.moon.rbac.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.rbac.domain.Role;
import org.moon.utils.Objects;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public interface RoleRepository extends BaseRepository<Role>{

	public List<Map> getSubRoles(@Param("rid") Long rid,@Param("deleteFlag")boolean deleteFlag);

    public List<Map<String,Object>> getRolesWithStatusForPermission(@Param("rid") Long rid,@Param("pid")Long pid);

	public void assign(@Param("rid")Long rid,@Param("uid")Long uid);
	
	public boolean hasPermission(@Param("rid")Long rid,@Param("code")String code);
	
	public boolean hasMenu(@Param("rid")Long rid,@Param("code")String code);

	public List<Map<String,Object>> getAllRoles();
}
