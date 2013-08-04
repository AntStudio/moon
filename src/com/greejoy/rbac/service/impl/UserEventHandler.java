package com.greejoy.rbac.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.greejoy.rbac.domain.User;
import com.greejoy.rbac.repository.UserRepository;
import com.greejoy.rbac.service.UserService;
import com.reeham.component.ddd.annotation.OnEvent;

/**
 * the handler of event related to user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Component
public class UserEventHandler {

	@Resource
	private UserRepository userRepository;
	
	@Resource
	private UserService userService;

	/**
	 * get the user from model cache,if not exist,get from DB
	 * @param uid
	 * @return
	 */
	@OnEvent("getUser")
	public User getUser(Long uid){
		return userService.getModel(uid);
	}
	
	@OnEvent("addUser")
	public void addUser(User user){
		userRepository.addUser(user);
	}
	
	@OnEvent("updateUser")
	public void updateUser(User user){
		userRepository.updateUser(user);
	}
	
	@OnEvent("deleteUser")
	public void deleteUser(Long[] ids){
		userRepository.deleteUser(ids);
	}
	
	@OnEvent("logicDeleteUser")
	public void sendLogicDeleteMessage(Long[] ids){
		userRepository.logicDeleteUser(ids);
	}
}
