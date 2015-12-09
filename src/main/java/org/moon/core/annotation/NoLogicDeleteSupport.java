package org.moon.core.annotation;

import java.lang.annotation.*;

/**
 * mark the class(mainly for domain class) not support the logically delete.
 * cause the the common operations would handle the <code>deleteFlag</code> which used for logically delete. so need annotate
 * the class with {@link NoLogicDeleteSupport} which not support the logically delete
 * @author Gavin
 * Jul 2, 2014
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoLogicDeleteSupport {

}
