package org.moon.support.hessian.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

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
