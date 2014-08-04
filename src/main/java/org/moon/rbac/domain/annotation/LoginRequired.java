package org.moon.rbac.domain.annotation;

import java.lang.annotation.*;
/**
 * 登录拦截标示
 * @author Gavin
 * @version 1.0
 * @date 2012-12-28
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginRequired {

}
