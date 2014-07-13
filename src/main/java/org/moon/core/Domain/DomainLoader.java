package org.moon.core.Domain;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.moon.base.service.BaseService;
import org.springframework.stereotype.Component;

/**
 * 领域加载器
 * @author Gavin
 * @date 2014年7月5日 下午12:43:53
 */
@Component
public class DomainLoader {

	private Map<Class,BaseService> loaders = new ConcurrentHashMap<Class, BaseService>(); 
	
	public void registerDomainLoader(Class c,BaseService s){
		loaders.put(c, s);
	}
	
	public <T> T load(Class<T> c,Long id){
		return (T) loaders.get(c).get(id);
	}
}
