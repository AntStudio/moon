package org.moon.core.cache.memcached;

import net.rubyeye.xmemcached.MemcachedClient;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.utils.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

/**
 * Spring cache on top of memcached
 * @author GavinCook
 * @since 1.0.0
 */
public class MemcachedCache implements Cache {

    private final String cacheName;

    private final MemcachedClient memcachedClient;

    /**
     * the cache value expiry time in seconds
     */
    private final int expiry;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public MemcachedCache(String cacheName, MemcachedClient memcachedClient,int expiry){
        this.cacheName = cacheName;
        this.memcachedClient = memcachedClient;
        this.expiry = expiry;
    }

    @Override
    public String getName() {
        return cacheName;
    }

    @Override
    public Object getNativeCache() {
        return memcachedClient;
    }

    @Override
    public ValueWrapper get(Object o) {
        logger.trace("Trying to get key:{} from cache",o);
        String key = castToString(o);
        try {
            Object result = memcachedClient.get(key);
            logger.trace("Get key:{} from cache,the value is : {}",o,result);
            return Objects.isNull(result) ? null : new SimpleValueWrapper(result);
        }catch (Exception e){
            throw new IllegalStateException("Failed to get key:["+key+"] from cache:["+cacheName+"]",e);
        }
    }

    @Override
    public <T> T get(Object o, Class<T> tClass) {
        logger.trace("Trying to get key:{} from cache",o);
        String key = castToString(o);
        try {
            Object value = memcachedClient.get(key);
            if (Objects.nonNull(value) && Objects.nonNull(tClass) && !tClass.isInstance(value)) {
                throw new ApplicationRunTimeException("Cached value is not of required type [" + tClass.getName() + "]: " + value);
            }
            logger.trace("Get key:{} from cache,the value is : {}",o,value);
            return (T) value;
        }catch (Exception e){
            throw new IllegalStateException("Failed to get key:["+key+"] from cache:["+cacheName+"]",e);
        }
    }

    @Override
    public void put(Object o, Object o2) {
        logger.trace("Trying to put key:{}, value: {} into cache",o,o2);
        String key = castToString(o);
        try {
            memcachedClient.set(key,expiry,o2);
            logger.trace("Put key:{}, value: {} into cache successfully",o,o2);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Failed to put {"+key+":"+o2+"} into cache:["+cacheName+"]",e);
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object o, Object o2) {
        logger.trace("Trying to add key:{}, value: {} into cache",o,o2);
        String key = castToString(o);
        try {
            Object result = memcachedClient.add(key, expiry, o2);
            logger.trace("Add key:{}, value: {} into cache successfully",o,o2);
            return Objects.isNull(result) ? null : new SimpleValueWrapper(result);
        }catch (Exception e){
            throw new IllegalStateException("Failed to put {"+key+":"+o2+"} into cache:["+cacheName+"]",e);
        }
    }

    @Override
    public void evict(Object o) {
        logger.trace("Trying to evict key:{} from cache",o);
        String key = castToString(o);
        try {
            memcachedClient.delete(key);
            logger.trace("Evict key:{} from cache successfully",o);
        }catch (Exception e){
            throw new IllegalStateException("Failed to evict key:["+key+"] from cache:["+cacheName+"]",e);
        }
    }

    @Override
    public void clear() {
        logger.trace("Trying to clear cache");
        try {
            memcachedClient.flushAllWithNoReply();
            logger.trace("Clear cache successfully");
        }catch (Exception e){
            throw new IllegalStateException("Failed to clear cache:["+cacheName+"]",e);
        }
    }

    private String castToString(Object o){
        if(Objects.isNull(o)){
            return null;
        }else if(o instanceof String){
            return cacheName.concat((String)o);
        }else {
            return cacheName.concat(o.toString());
        }
    }
}
