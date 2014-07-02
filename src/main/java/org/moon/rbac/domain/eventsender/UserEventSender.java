package org.moon.rbac.domain.eventsender;

import org.moon.base.domain.eventsender.EventSender;
import org.moon.rbac.domain.User;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;


/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Introduce("message")
public class UserEventSender implements EventSender{

	@Send("role/get")
	public DomainMessage getRole(User user){
		System.out.println(user.getRoleId()+"......sssssss...................");
		return new DomainMessage(user.getRoleId());
	}
}
