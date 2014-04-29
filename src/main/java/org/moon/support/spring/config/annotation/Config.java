package org.moon.support.spring.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于注入资源文件的配置值,value为配置的名称.目前只加载moon.properties配置文件
 * @author Gavin
 * @date 2014-4-29
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Config {

	String value();
	
	String defaultVal() default "";
	
}
