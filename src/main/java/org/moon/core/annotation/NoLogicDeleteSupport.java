package org.moon.core.annotation;

import java.lang.annotation.*;

/**
 * 不支持逻辑删除标示
 * @author Gavin
 * Jul 2, 2014
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogicDeleteSupport {

}
