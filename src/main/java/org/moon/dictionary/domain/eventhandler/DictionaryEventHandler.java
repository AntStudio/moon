package org.moon.dictionary.domain.eventhandler;

import org.moon.base.domain.eventhandler.BaseEventHandler;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.service.DictionaryService;
import org.springframework.stereotype.Component;

/**
 * 字典事件处理器
 * @author Gavin
 * @date Jun 27, 2014
 */
@Component
public class DictionaryEventHandler extends BaseEventHandler<Dictionary,DictionaryService>{
	
}
