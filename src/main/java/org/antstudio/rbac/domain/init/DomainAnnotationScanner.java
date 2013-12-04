package org.antstudio.rbac.domain.init;

import java.lang.reflect.Method;

import org.antstudio.rbac.domain.Menu;
import org.antstudio.rbac.domain.Permission;
import org.antstudio.rbac.domain.annotation.MenuMapping;
import org.antstudio.rbac.domain.annotation.PermissionMapping;
import org.antstudio.rbac.domain.init.helper.MenuMappingHelper;
import org.antstudio.rbac.domain.init.helper.PermissionMappingHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;


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
