package org.moon.rbac.service;

import javax.servlet.http.HttpServletRequest;

import org.moon.base.service.BaseService;
import org.moon.rbac.domain.User;

/**
 * the service interface for the User
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
public interface UserService extends BaseService<User>{

	public User login(User user);
	
	public User getCurrentUser(HttpServletRequest request);
	
	public Long getCurrentUserId(HttpServletRequest request);
	
	public void delete(Long ids[],boolean logicDel);
	
	public boolean isUserNameExists(String userName);
	
}
