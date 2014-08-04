package org.moon.core.orm.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 当为null时，不处理该字段。
 * 使用场景：<p>
 * 配合数据库默认值使用，当为空不处理该字段，插入数据时就会使用数据库的默认值
 * @author Gavin
 * @date 2014年7月13日 下午2:53:35
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreNull {
	
}
