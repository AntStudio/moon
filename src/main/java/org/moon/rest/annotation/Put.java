package org.moon.rest.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * The annotation for Post request
 * this equivalent to @RequstMapping with Put request method
 * @author Gavin
 * @date 2013-12-15 下午5:15:48
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Put {

	String[] value() default{};

}
