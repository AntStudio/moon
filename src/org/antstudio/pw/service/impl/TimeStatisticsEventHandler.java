package org.antstudio.pw.service.impl;

import javax.annotation.Resource;

import org.antstudio.pw.domain.TimeStatistics;
import org.antstudio.pw.repository.TimeStatisticsRepository;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.annotation.OnEvent;

@Component
public class TimeStatisticsEventHandler {

	@Resource
	private TimeStatisticsRepository timeStatisticsRepository;
	
	@OnEvent("saveOrUpdateTimeStatistics")
	public void saveOrUpdate(TimeStatistics timeStatistics){
		if(timeStatistics.getId()==null){
			System.out.println("保存啊啊啊啊 ");
			timeStatisticsRepository.save(timeStatistics);
		}
		else{
			System.out.println("更行啊啊啊啊啊");
			timeStatisticsRepository.update(timeStatistics);
		}
	}
	
}
