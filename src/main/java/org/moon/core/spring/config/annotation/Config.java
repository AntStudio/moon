package org.moon.core.spring.config.annotation;

import java.lang.annotation.*;

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
