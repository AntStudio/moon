package org.antstudio.pw.action.repository;

import org.antstudio.pw.domain.TimeStatistics;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;

@Introduce("message")
public class TimeStatisticsEvent {

	@Send("saveOrUpdateTimeStatistics")
	public DomainMessage saveOrUpdate(TimeStatistics timeStatistics){
		return new DomainMessage(timeStatistics);
	}
	
}
