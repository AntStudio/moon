package org.moon.dictionary.service;

import java.util.List;
import java.util.Map;

import org.moon.base.service.BaseService;
import org.moon.dictionary.domain.Dictionary;

public interface DictionaryService extends BaseService<Dictionary>{

	public List<Map> list();
	
}
