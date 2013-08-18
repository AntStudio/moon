package org.antstudio.log.domain.respository;

import org.antstudio.log.domain.Log;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

@Introduce("message")
public class LogEvent {

	@Send("saveOrUpdateLog")
	public DomainMessage saveOrUpdate(Log log){
		return new DomainMessage(log);
	}
	
}
