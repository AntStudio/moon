package com.greejoy.rbac.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.greejoy.pagination.Pager;
import com.greejoy.rbac.domain.User;
import com.reeham.component.ddd.model.ModelLoader;

/**
 * the service interface for the User
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
public interface UserService extends BaseService<User>,ModelLoader{

	/**
	 * 登录验证，登录成功返回该user,
	 * 登录失败返回传入的user参数，应只包含userName和password，其中id应为null
	 * @param user
	 * @return
	 */
	public User login(User user);
	
	public User getCurrentUser(HttpServletRequest request);
	
	public Long getCurrentUserId(HttpServletRequest request);
	
	//public List<User> getUsers
	
	/**
	 * check if the user is system admin user
	 * @param user
	 * @return
	 */
	public boolean isSysUser(User user);
	
	public List<User> getUsersByCreator(Map<String,Object> paramsMap);
	
	public List<Map<String,Object>> getUsersByCreatorForMap(Map<String,Object> paramsMap);
	
	public Pager getUsersByCreatorForPager(Map<String,Object> paramsMap);
	
	public void addUser(User user);
	
	
	public boolean update(User user);
	
	public void delete(Long ids[],boolean logicDel);
	
	
}
