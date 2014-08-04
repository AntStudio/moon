package org.moon.rest.annotation;

import java.lang.annotation.*;
/**
 * The annotation for Post request
 * this equivalent to @RequstMapping with Get request method
 * @author Gavin
 * @date 2013-12-15 下午5:15:48
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Get {

	String[] value() default{};

}
