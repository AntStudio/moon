package com.greejoy.rbac.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 权限映射器
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PermissionMapping {
	
	/**
	 * 权限代码
	 * @return
	 */
	public String code();
	
	/**
	 * 权限名称
	 * @return
	 */
	public String name();
	
}
