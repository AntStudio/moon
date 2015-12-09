package org.moon.core.cache.proxy;

import org.moon.core.spring.RbacInterceptorProxyCreator;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopInfrastructureBean;
import org.springframework.aop.framework.ProxyConfig;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * create the proxy for handing {@link org.moon.core.cache.annotation.CacheBatchEvict}.
 * this proxy creator cut in the {@link #postProcessAfterInitialization(Object, String)}, and can do multiple proxy for
 * origin bean, there also another way to do multiple proxy, see {@link RbacInterceptorProxyCreator#getInterceptorsByNames()}
 * @author GavinCook
 * @since 1.0.0
 */
public class CacheBatchEvictProxyCreator extends ProxyConfig implements BeanPostProcessor,AopInfrastructureBean,BeanFactoryAware {

    private static final Field interceptorNamesField = ReflectionUtils.findField(ProxyFactoryBean.class,
            "interceptorNames");

    private String[] interceptorNames = new String[0];

    private final Logger log = LoggerFactory.getLogger(getClass());

    private BeanFactory beanFactory;
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(AopUtils.getTargetClass(bean).isAnnotationPresent(Service.class)) {
            if (ProxyFactoryBean.class.isAssignableFrom(bean.getClass())) {
                ProxyFactoryBean proxyFactoryBean = (ProxyFactoryBean) bean;
                //origin proxy interceptors
                String[] originInterceptors = getInterceptorNames(proxyFactoryBean);
                String[] newInterceptors = new String[originInterceptors.length + interceptorNames.length];
                System.arraycopy(originInterceptors, 0, newInterceptors, 0, originInterceptors.length);
                System.arraycopy(interceptorNames, 0, newInterceptors, originInterceptors.length, interceptorNames.length);
                proxyFactoryBean.setInterceptorNames(newInterceptors);
                return proxyFactoryBean;
            } else {
                ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
                proxyFactoryBean.setBeanFactory(beanFactory);
                proxyFactoryBean.setBeanClassLoader(ClassUtils.getDefaultClassLoader());
                proxyFactoryBean.setInterceptorNames(interceptorNames);
                proxyFactoryBean.copyFrom(this);
                proxyFactoryBean.setTarget(bean);
                return proxyFactoryBean.getObject();
            }
        }
        return bean;
    }

    /**
     * get interceptors from the proxy factory bean
     * @param proxyFactoryBean the proxy factory bean
     * @return the interceptors in proxy factory bean
     */
    private String[] getInterceptorNames(ProxyFactoryBean proxyFactoryBean){
        interceptorNamesField.setAccessible(true);
        try {
            Object obj = interceptorNamesField.get(proxyFactoryBean);
            return Objects.nonNull(obj) ? (String[])obj : new String[0];
        } catch (IllegalAccessException e) {
            log.error("Get the interceptor names for failed.");
            e.printStackTrace();
        }
        return new String[0];
    }

    public void setInterceptorNames(String[] interceptorNames) {
        this.interceptorNames = interceptorNames;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
