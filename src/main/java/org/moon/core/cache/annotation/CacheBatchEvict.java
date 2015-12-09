package org.moon.core.cache.annotation;

import java.lang.annotation.*;

/**
 * Annotation indicating that batch evict values from caches before method processed, it's happens before method process.
 * there also has another way to "evict" cache batch, see {@link org.moon.core.cache.support.NamespaceHelper}
 * @author GavinCook
 * @since 1.0.0
 * @see org.moon.core.cache.interceptor.CacheBatchEvictInterceptor
 * @see org.moon.core.cache.proxy.CacheBatchEvictProxyCreator
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface CacheBatchEvict {
    /**
     * cache names, can be multiple cache names
     */
    String[] value();


    /**
     * the key stands for what need evict, it support the spring EL. can obtain the xxx parameter by using #xxx in spring EL.
     * the key expression string should return a string array. now there has a simple key generator
     * {@link org.moon.utils.Strings#concatPrefix(String, Object[])}
     * @return the key expression
     */
    String key();

}
