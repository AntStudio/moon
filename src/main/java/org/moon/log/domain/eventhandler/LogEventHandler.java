package org.moon.log.domain.eventhandler;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.log.domain.Log;
import org.moon.log.service.LogService;
import org.springframework.stereotype.Component;

/**
 * @author Gavin
 * @date 2014-1-12
 */
@Component
public class LogEventHandler extends BaseEventHandler<Log,LogService>{
	
}
