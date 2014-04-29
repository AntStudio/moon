package org.moon.support.spring.config;

import java.lang.reflect.Field;

import javax.annotation.Resource;

import org.moon.support.spring.config.annotation.Config;
import org.moon.support.spring.config.exception.ConfigAutowireException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * 配置注入
 * @author Gavin
 * @date 2014-4-29
 */
@Component
public class ConfigAutowireHandler implements BeanPostProcessor{

	@Resource
	private ConfigHolder configHolder;
	
	@Override
	public Object postProcessBeforeInitialization(final Object bean,String beanName)
			throws BeansException {

		try {
			 ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
	           public void doWith(Field field) throws IllegalArgumentException,IllegalAccessException {
	        	   
	        	   	Config config = field.getAnnotation(Config.class);
					if(config!=null){
						if(!field.isAccessible()){
							field.setAccessible(true);
						}
						field.set(bean, configHolder.get(config.value()));
					}
					
	           }});
		} catch (Exception e) {
			throw new ConfigAutowireException(beanName+" can't be autowire config successfully",e.getCause());
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

}
