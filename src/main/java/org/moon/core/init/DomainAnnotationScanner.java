package org.moon.core.init;

import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.helper.PermissionMappingHelper;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * a scanner for annotation {@link MenuMapping} and {@link PermissionMapping} config in the controller layer.
 * the {@link MenuMapping} would covert into {@link Menu} and keep them in {@link MenuMappingHelper}.
 * for the {@link PermissionMapping}, there has two level, one for class level, which is the permission for all the method
 * in this class. another one is the specific method. the permission interceptor priority order is : first class permission,
 * then method permission if has some. The permission store format: [M] prefix for method level permission, [C] prefix for
 * class level permission.
 * The intercept logic is in {@link org.moon.rbac.interceptor.RbacInterceptor}
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2014-07-17
 */
public class DomainAnnotationScanner implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName)
            throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        MenuMapping menuMapping;
        String className = beanClass.getName();
        for (Method method : beanClass.getMethods()) {
            if (method.isAnnotationPresent(MenuMapping.class)) {//scan @MenuMapping
                Menu menu = new Menu();
                menuMapping = method.getAnnotation(MenuMapping.class);
                menu.setUrl(menuMapping.url());
                menu.setCode(menuMapping.code());
                menu.setMenuName(menuMapping.name());
                menu.setParentCode(menuMapping.parentCode().equals("") ? null : menuMapping.parentCode());
                MenuMappingHelper.addMappingMenu(menu);

            }
            if (method.isAnnotationPresent(PermissionMapping.class)) {//scan @PermissionMapping
                PermissionMapping permissionMapping = method.getAnnotation(PermissionMapping.class);
                PermissionMappingHelper.addMappingPermission(
                        new Permission(permissionMapping.code(),permissionMapping.name(),"[M]"+className+"."+method.getName()));
            }
        }

        //obtain the class level permission
        if (beanClass.isAnnotationPresent(PermissionMapping.class)) {
            PermissionMapping permissionMapping = beanClass.getAnnotation(PermissionMapping.class);
            PermissionMappingHelper.addMappingPermission(
                    new Permission(permissionMapping.code(),permissionMapping.name(),"[C]"+beanClass.getName()));
        }
        return bean;
    }
}
