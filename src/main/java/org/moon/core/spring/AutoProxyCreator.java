package org.moon.core.spring;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;

public class AutoProxyCreator extends AbstractAutoProxyCreator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6516234963003237580L;
	
	private String[] basePackages;
	
	private String[] annotations;

	private String[] interceptors;


	
	@Override
	protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
			throws BeansException {
		if(isController(beanClass))
			return getInterceptorsByNames();
		return DO_NOT_PROXY;
	}
	
	private boolean isController(Class<?> beanClass){
		return beanClass.isAnnotationPresent(Controller.class);
	}
	private Object[] getInterceptorsByNames(){
		if(interceptors==null){
			interceptors = new String[1];
			interceptors[0] = "rbacInterceptor";
		}
		Object[] advisors = new Object[interceptors.length];
		for(int i= 0,j=interceptors.length;i<j;i++){
			advisors[i] = getBeanFactory().getBean(interceptors[i]);
		}
		return advisors;
	}
	/**
	 * 根据class包名检查class是否在相应的package下
	 * @param basePackages
	 * @param classPackageName
	 * @return
	 */
	public boolean underPackages(String classPackageName){
		if(basePackages==null)
			return false;
		for(String basePackage:basePackages){
			if(classPackageName.startsWith(basePackage))
				return true;
		}
			return false;
	}

	/**
	 * @return the basePackages
	 */
	public String[] getBasePackages() {
		return basePackages;
	}

	/**
	 * @param basePackages the basePackages to set
	 */
	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
	}

	/**
	 * @return the annotations
	 */
	public String[] getAnnotations() {
		return annotations;
	}

	/**
	 * @param annotations the annotations to set
	 */
	public void setAnnotations(String[] annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the interceptors
	 */
	public String[] getInterceptors() {
		return interceptors;
	}

	/**
	 * @param interceptors the interceptors to set
	 */
	public void setInterceptors(String[] interceptors) {
		this.interceptors = interceptors;
	}


}
