package com.greejoy.log.domain.respository;

import com.greejoy.log.domain.Log;
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
