package org.moon.log.service.impl;

import javax.annotation.Resource;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.log.domain.Log;
import org.moon.log.repository.LogRepository;
import org.springframework.stereotype.Component;

/**
 * @author Gavin
 * @date 2014-1-12
 */
@Component
public class LogEventHandler extends BaseEventHandler<Log>{

	@Resource
	private LogRepository logRepository;
	@Override
	public Log save(Log log) {
		logRepository.save(log);
		return log;
	}

	@Override
	public void delete(Log domain) {
		
	}

	@Override
	public void update(Log domain) {
		
	}

	
}
