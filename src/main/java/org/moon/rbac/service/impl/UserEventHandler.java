package org.moon.rbac.service.impl;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.User;
import org.moon.rbac.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * the handler of event related to user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Component
public class UserEventHandler extends BaseEventHandler<User>{

	@Resource
	private UserRepository userRepository;

    @Override
    public User save(User user) {
    	userRepository.addUser(user);
        return user;
    }

    @Override
    public void delete(User user) {
    	userRepository.deleteUser(new Long[]{user.getId()});
    }

    @Override
    public void update(User user) {
        userRepository.updateUser(user);
    }

   
}
