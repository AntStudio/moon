package org.moon.support.hessian;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class HessianExporter implements BeanFactoryAware{

	private DefaultListableBeanFactory bf;
	private Logger log =Logger.getLogger(getClass());
	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.bf = (DefaultListableBeanFactory) beanFactory;
		for(String beanName:HessianServiceHelper.getHessianServiceMap().keySet()){
			log.info("Export hessian service ["+beanName+"] ");
			bf.registerBeanDefinition(beanName, HessianServiceHelper.getHessianServiceMap().get(beanName));
		} 
	}

}
