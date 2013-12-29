package org.antstudio.blog.event;

import org.antstudio.blog.domain.Registration;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

/**
 * @author Gavin
 * @date 2013-12-29 下午11:19:56
 */
@Introduce("message")
public class RegistrationEvent {

	@Send("saveOrUpdateRegistration")
	public DomainMessage saveOrUpdate(Registration registration){
		return new DomainMessage(registration);
	}
	
}
