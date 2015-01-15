package org.moon.rbac.domain.annotation;

import java.lang.annotation.*;

/**
 * 表示该菜单不会进行菜单拦截,不使用该注解时，菜单都会进行菜单拦截
 * Created by Gavin on 2014/11/22 0022.
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoMenuIntercept {

}
