package org.moon.rbac.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 获取当前登录的用户，从Session中的CURRENT_USER_ID中获取
 * @author Gavin
 * Jul 2, 2014
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebUser {
	
}
