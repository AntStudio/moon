package org.moon.core.spring.config;

import org.moon.core.spring.config.annotation.Config;
import org.moon.core.spring.config.exception.ConfigAutowireException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * 配置注入
 * @author Gavin
 * @date 2014-4-29
 */
@Component
public class ConfigAutowireHandler implements BeanPostProcessor{

	@Resource
	private ConfigHolder configHolder;

    private Logger log = LoggerFactory.getLogger(ConfigAutowireHandler.class);

	@Override
	public Object postProcessBeforeInitialization(final Object bean, final String beanName)
			throws BeansException {

		try {
			 ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {
	           public void doWith(Field field) throws IllegalArgumentException,IllegalAccessException {
	        	   
	        	   	Config config = field.getAnnotation(Config.class);
					if(config!=null){
						if(!field.isAccessible()){
							field.setAccessible(true);
						}
                        Object value = configHolder.get(config.value(),config.defaultVal());
                        Class<?> targetClass = field.getType();
                        try {
                            if (targetClass == int.class || targetClass == Integer.class) {
                                field.setInt(bean, Integer.parseInt(value + ""));
                            } else if (targetClass == long.class || targetClass == Long.class) {
                                field.setLong(bean, Long.parseLong(value + ""));
                            } else if (targetClass == boolean.class || targetClass == Boolean.class) {
                                field.setBoolean(bean, Boolean.valueOf(value + ""));
                                System.out.println(field.getBoolean(bean)+"...............");
                            } else if (targetClass == short.class || targetClass == Short.class) {
                                field.setShort(bean, Short.parseShort(value + ""));
                            } else if (targetClass == double.class || targetClass == Double.class) {
                                field.setDouble(bean, Double.parseDouble(value + ""));
                            } else if (targetClass == float.class || targetClass == Float.class) {
                                field.setFloat(bean, Float.parseFloat(value + ""));
                            } else {
                                field.set(bean, value);
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            log.error("there throw an exception when do autowire config for {}.{}, the exception is:{}",
                                    beanName,field.getName(),e.getCause());

                        }
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
