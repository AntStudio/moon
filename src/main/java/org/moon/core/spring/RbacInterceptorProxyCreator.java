package org.moon.core.spring;

import org.springframework.aop.Advisor;
import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.autoproxy.AbstractAutoProxyCreator;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * RBAC 拦截代理生成器,目前只处理@Controller类型
 */
public class RbacInterceptorProxyCreator extends AbstractAutoProxyCreator {

    private static final long serialVersionUID = 6516234963003237580L;

    private String[] basePackages;

    private String[] annotations;

    private String[] interceptors;

    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource targetSource)
            throws BeansException {
        if (isController(beanClass)) {
            return getInterceptorsByNames();
        }
        return DO_NOT_PROXY;
    }

    private boolean isController(Class<?> beanClass) {
        return beanClass.isAnnotationPresent(Controller.class) || beanClass.isAnnotationPresent(RestController.class);
    }

    private Object[] getInterceptorsByNames() {
        if (interceptors == null) {
            interceptors = new String[1];
            interceptors[0] = "rbacInterceptor";
        }
        Object[] advisors = new Object[interceptors.length];
        for (int i = 0, j = interceptors.length; i < j; i++) {
            advisors[i] = getBeanFactory().getBean(interceptors[i]);
        }
        return advisors;
    }

    @Override
    protected Object wrapIfNecessary(Object bean, String beanName, Object cacheKey) {
        if (AopUtils.isCglibProxy(bean) || AopUtils.isAopProxy(bean) || AopUtils.isJdkDynamicProxy(bean)) {
            Class<?> target = AopUtils.getTargetClass(bean);
            if (bean instanceof Advised) {
                Object[] specificInterceptors = getAdvicesAndAdvisorsForBean(target, beanName, null);
                Advisor[] advisors = buildAdvisors(beanName, specificInterceptors);
                if (specificInterceptors != DO_NOT_PROXY) {
                    for (Advisor advisor : advisors) ((Advised) bean).addAdvisor(advisor);
                }
            }
            return bean;
        } else {
            return super.wrapIfNecessary(bean, beanName, cacheKey);
        }
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
