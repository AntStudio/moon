package org.moon.support.hessian;

import org.springframework.beans.factory.config.BeanDefinition;

import java.util.HashMap;
import java.util.Map;

public class HessianServiceHelper {

	public static Map<String,BeanDefinition> hessianServices = new HashMap<String,BeanDefinition>();
	
	public static void addHessianService(String beanName, BeanDefinition beanDefinition){
		hessianServices.put(beanName, beanDefinition);
	}
	
	public static Map<String,BeanDefinition> getHessianServiceMap(){
		return hessianServices;
	}
}
