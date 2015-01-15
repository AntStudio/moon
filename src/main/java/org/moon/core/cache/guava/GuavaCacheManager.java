package org.moon.core.cache.guava;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Google guava 缓存管理,一般来说优先使用Memcached
 * @author:Gavin
 * @date 2015/1/14 0014
 */
@Component
public class GuavaCacheManager {
    //默认7天过期，最大1w个缓存值
    private Cache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(10000)
            .expireAfterWrite(7, TimeUnit.DAYS)
            .build();

    /**
     * 取值
     * @param key
     * @return
     */
    public Object get(String key){
        return cache.getIfPresent(key);
    }

    /**
     * 存缓存
     * @param key
     * @param value
     */
    public void put(String key,Object value){
        cache.put(key,value);
    }
}
