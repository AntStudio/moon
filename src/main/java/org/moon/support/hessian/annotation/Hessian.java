package org.moon.support.hessian.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

/**
 * @Hessian 用于将对象发布为hessian服务
 * @author Gavin
 * @version 1.0
 * @date 2013-2-23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Hessian {

	public String path() default "";
	
	public Class<?> serviceInterface() default Hessian.class;
	
}
