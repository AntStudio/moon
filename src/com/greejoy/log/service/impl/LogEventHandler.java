package com.greejoy.log.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.greejoy.log.domain.Log;
import com.greejoy.log.repository.LogRepository;
import com.reeham.component.ddd.annotation.OnEvent;

/**
 * 日志事件处理器
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Component
public class LogEventHandler {

	@Resource
	private LogRepository logRepository;
	
	@OnEvent("saveOrUpdateLog")
	public void saveOrUpdate(Log log){
		if(log.getId()==null)
			logRepository.save(log);
		else
			logRepository.update(log);
	}
	
}
