package org.antstudio.rbac.repository;

import java.util.List;
import java.util.Map;

import org.antstudio.rbac.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * the repository for user domain
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Repository
public interface UserRepository extends BaseRepository<User>{

	/**
	 * assign role to user
	 * @param rid
	 * @param uid
	 */
	public void assign(@Param("rid")Long rid,@Param("uid")Long uid);
	
	public Long login(@Param("user")User user);
	
	public List<Long> getUsersByCreator(Map<String,Object> paramsMap);
	
	public Long getUsersByCreator_count(Map<String,Object> paramsMap);
	
	public void addUser(@Param("user") User user);
	
	public void updateUser(@Param("user") User user);
	
	public void deleteUser(@Param("ids") Long[] ids);
	
	public void logicDeleteUser(@Param("ids") Long[] ids);
	
	
}
