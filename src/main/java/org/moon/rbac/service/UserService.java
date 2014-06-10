package org.moon.rbac.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.moon.base.service.BaseService;
import org.moon.pagination.Pager;
import org.moon.rbac.domain.User;

import com.reeham.component.ddd.model.ModelLoader;

/**
 * the service interface for the User
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
public interface UserService extends BaseService<User>,ModelLoader{

	/**
	 * login validate
	 * @param user
	 * @return when validate failed,return the full user info; 
	 * 		   when validate successfully,return the user info what pass in,
	 *         sure the user id is <code>null</code>
	 */
	public User login(User user);
	
	public User getCurrentUser(HttpServletRequest request);
	
	public Long getCurrentUserId(HttpServletRequest request);
	
	/**
	 * check if the user is system admin user
	 * @param user
	 * @return
	 */
	public boolean isSysUser(User user);
	
	public List<User> getUsersByCreator(Map<String,Object> paramsMap);
	
	public List<Map<String,Object>> getUsersByCreatorForMap(Map<String,Object> paramsMap);
	
	public Pager getUsersByCreatorForPager(Map<String,Object> paramsMap);
	
	public boolean update(User user);
	
	public void delete(Long ids[],boolean logicDel);
	
	public boolean isUserNameExists(String userName);
	
}
