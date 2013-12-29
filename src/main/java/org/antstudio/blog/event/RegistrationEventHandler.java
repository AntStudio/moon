package org.antstudio.blog.event;

import javax.annotation.Resource;

import org.antstudio.blog.domain.Registration;
import org.antstudio.blog.repository.RegistrationRepository;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

/**
 * @author Gavin
 * @date 2013-12-29
 */
@Component
public class RegistrationEventHandler {

	@Resource
	private RegistrationRepository registrationRepository;
	
	@OnEvent("saveOrUpdateRegistration")
	public void saveOrUpdate(Registration registration){
		if(registration.getId()==null)
			registrationRepository.save(registration);
		else
			registrationRepository.update(registration);
	}
	
}
