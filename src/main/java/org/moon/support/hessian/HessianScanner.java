package org.moon.support.hessian;

import org.moon.support.hessian.annotation.Hessian;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.remoting.caucho.HessianServiceExporter;


/**
 * 用于扫描hessian服务
 * @author Gavin
 * @version 1.0
 * @date 2013-2-24
 */
public class HessianScanner implements BeanPostProcessor,BeanFactoryAware{

	private DefaultListableBeanFactory bf;
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		Class<?> beanClass = bean.getClass();
		Class<?>[] interfaces = beanClass.getInterfaces();
		Hessian hessian = beanClass.getAnnotation(Hessian.class);
		if(hessian!=null){
			BeanDefinitionBuilder userBeanDefinitionBuilder = BeanDefinitionBuilder  
	                .genericBeanDefinition(HessianServiceExporter.class);  
	        userBeanDefinitionBuilder.addPropertyValue("service", bf.getBean(beanClass));  
	        userBeanDefinitionBuilder.addPropertyValue("serviceInterface",hessian.serviceInterface().equals(Hessian.class)?interfaces[0]:hessian.serviceInterface());  
	        HessianServiceHelper.addHessianService((hessian.path().startsWith("/")?"":"/")+hessian.path(),  userBeanDefinitionBuilder.getRawBeanDefinition());
		}
		return bean;
	
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory bf) throws BeansException {
		this.bf = (DefaultListableBeanFactory) bf;
	}
}
