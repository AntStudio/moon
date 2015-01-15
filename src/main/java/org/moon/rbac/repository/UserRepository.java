package org.moon.rbac.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.rbac.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Repository
public interface UserRepository{

    public User get(Long id);
    
	/**
	 * 给用户分配角色
	 * @param rid
	 * @param uid
	 */
	public void assign(@Param("rid") Long rid, @Param("uid") Long uid);
	
	/**
	 * 登录验证
	 * @param user
	 * @return
	 */
	public Long login(@Param("user") User user);
	
	public List<Long> getUsersByCreator(Map<String, Object> paramsMap);
	
	public Integer getUsersByCreatorCount(Map<String, Object> paramsMap);
	
	public Long addUser(@Param("user") User user);
	
	public void updateUser(@Param("user") User user);
	
	public void deleteUser(@Param("ids") Long[] ids);
	
	public void logicDeleteUser(@Param("ids") Long[] ids);
	
	public boolean isUserNameExists(@Param("userName") String userName);
}
