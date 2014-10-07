package org.moon.core.Domain;

import org.moon.base.service.BaseService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域加载器,使用时需要先注册领域的加载器,为<code>BaseService</code>的实现类,
 * 然后再调用{@link #load(Class, Long)}方法加载领域。
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
