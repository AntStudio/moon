package org.moon.core.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 设置应用上下文,供如标签库使用
 * @author Gavin
 * @date May 30, 2014
 */
@Component
public class ApplicationContextHelper implements ApplicationContextAware{

	private static ApplicationContext applicationContext;
	
	@SuppressWarnings("static-access")
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		Assert.notNull(applicationContext, "applicationContext should not null");
		return applicationContext;
	}

	public static <T> T getBean(Class<T> c){
		return getApplicationContext().getBean(c);
	}
}
