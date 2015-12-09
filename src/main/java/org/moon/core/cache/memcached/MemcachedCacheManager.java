package org.moon.core.cache.memcached;

import net.rubyeye.xmemcached.MemcachedClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Memcached cache manager
 * @author GavinCook
 * @since 1.0.0
 */
public class MemcachedCacheManager implements CacheManager,InitializingBean{

    private final ConcurrentHashMap<String,MemcachedCache> cacheMap= new ConcurrentHashMap<String,MemcachedCache>(16);

    private MemcachedClient memcachedClient;

    /**
     * default expiry time, an hour
     */
    private int expiry = 60 * 60 ;


    @Override
    public Cache getCache(String key) {
        MemcachedCache cache = cacheMap.get(key);
        if(Objects.isNull(cache)){
            cache = createCache(key,expiry);
            cacheMap.put(key,cache);
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableCollection(cacheMap.keySet());
    }

    /**
     * config the cache by cache names, use the default expiry for all caches
     * @param cacheNames cache names
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCache(name,expiry));
            }
        }
    }

    /**
     * 配置缓存，可针对每个缓存设置不同的过期时间
     * config cache with the name and the expiry for specific one
     * @param cacheSettings cache settings
     */
    public void setCacheSettings(Collection<CacheSetting> cacheSettings) {
        if (cacheSettings != null) {
            for (CacheSetting cacheSetting : cacheSettings) {
                this.cacheMap.put(cacheSetting.getName(), createCache(cacheSetting.getName(),cacheSetting.getExpiry()));
            }
        }
    }

    public void setExpiry(int expiry){
        this.expiry = expiry;
    }

    public int getExpiry(){
        return this.expiry;
    }

    public MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }

    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(memcachedClient,"Must set the memcachedClient for MemcachedCacheManager");
    }

    private MemcachedCache createCache(String name,int expiry){
        return new MemcachedCache(name, memcachedClient,expiry);
    }
}
