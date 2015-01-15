package org.moon.core.domain;

import org.moon.base.service.BaseDomainService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 领域加载器,使用时需要先注册领域的加载器,为<code>BaseDomainService</code>的实现类,
 * 然后再调用{@link #load(Class, Long)}方法加载领域。
 * @author Gavin
 * @date 2014年7月5日 下午12:43:53
 */
@Component
public class DomainLoader {

	private Map<Class,BaseDomainService> loaders = new ConcurrentHashMap<Class, BaseDomainService>();
	
	public void registerDomainLoader(Class c,BaseDomainService s){
		loaders.put(c, s);
	}
	
	public <T> T load(Class<T> c,Long id){
		return (T) loaders.get(c).get(id);
	}
}
