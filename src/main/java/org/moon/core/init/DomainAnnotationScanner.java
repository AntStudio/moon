package org.moon.core.init;

import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.helper.PermissionMappingHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;


public class DomainAnnotationScanner implements BeanPostProcessor{

	
	public DomainAnnotationScanner(){
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName)
			throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName)
			throws BeansException {
		 Class<?> beanClass = bean.getClass();
         MenuMapping menuMapping;
         for(Method method:beanClass.getMethods()){
        	 if(method.isAnnotationPresent(MenuMapping.class)){//扫描@MenuMapping注解,并添加到mappingMenu
        		 Menu menu = new Menu();
        		 menuMapping =  method.getAnnotation(MenuMapping.class);
        		 menu.setUrl(menuMapping.url());
        		 menu.setCode(menuMapping.code());
        		 menu.setMenuName(menuMapping.name());
        		 menu.setParentCode(menuMapping.parentCode().equals("")?null:menuMapping.parentCode());
        		 MenuMappingHelper.addMappingMenu(menu);
        		 
        	 }
	 		 if(method.isAnnotationPresent(PermissionMapping.class)){//扫描@PermissionMapping
    			PermissionMapping permissionMapping = method.getAnnotation(PermissionMapping.class);
    			PermissionMappingHelper.addMappingPermission(new Permission(permissionMapping.code(),permissionMapping.name()));
    		 }
         }
		return bean;
	}
}
