package org.moon.rbac.domain.eventsender;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.domain.eventsender.EventSender;
import org.moon.rbac.domain.User;


/**
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Introduce("message")
public class UserEventSender implements EventSender{
	
	@Send("role/get")
	public DomainMessage getRole(User user){
		return new DomainMessage(user.getRoleId());
	}
}
