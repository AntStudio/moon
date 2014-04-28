package org.moon.rbac.domain.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogRecord {

	/**
	 * 操作信息
	 * @return
	 */
	public String action();
	
	/**
	 * 日志类型
	 * @see org.moon.utils.Constants#OPERATE_LOG
	 * @see org.moon.utils.Constants#OPERATE_LOG
	 * @return
	 */
	public String type() default "OPERATE_LOG";

}
