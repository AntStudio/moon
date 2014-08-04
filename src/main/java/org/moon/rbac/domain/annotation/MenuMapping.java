package org.moon.rbac.domain.annotation;

import java.lang.annotation.*;

/**
 * this annotation is for @RequestMapping method
 * when application start,the url would be generated to menu
 * @author Gavin
 * @version 1.0
 * @date 2012-12-10
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MenuMapping {

	/**
	 * the url for the menu
	 * @return
	 */
	public String url();
	
	/**
	 * the name for the menu
	 * @return
	 */
	public String name() default "";
	
	/**
	 * the menu code,this is for the auto generated menu
	 * @return
	 */
	public String code() default "";
	
	/**
	 * the parent menu code,this is for the auto generated menu
	 * @return
	 */
	public String parentCode() default "";
	
	/**
	 * the menu order , used to show menu orderly
	 * @return
	 */
	public int menuOrder() default 0;
	
}
