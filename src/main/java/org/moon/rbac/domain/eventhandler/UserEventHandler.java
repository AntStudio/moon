package org.moon.rbac.domain.eventhandler;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.rbac.domain.User;
import org.moon.rbac.service.UserService;
import org.springframework.stereotype.Component;

/**
 * the handler of event related to user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Component
public class UserEventHandler extends BaseEventHandler<User,UserService>{
	
}
