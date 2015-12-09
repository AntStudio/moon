package org.moon.core.cache.support;

import org.moon.core.spring.ApplicationContextHelper;
import org.moon.utils.Objects;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

/**
 * <p>cache namespace helper, it's useful tool for add namespace feature for cache. with namespace , can easily 'remove' all
 * cache values in the one namespace, actually the namespace contains a counter, once call {@link #removeNP(String)} , the
 * counter would plus one, then make the old namespace not used, looks like the cache values is removed in the old namespace.
 * these cache values would not remove util the cache client clear for space or expiry time.</p>
 * <p>the namespace helper need a cache named "namespace", this cache used to hold the counter for every namespace.</p>
 * @author GavinCook
 * @since 1.0.0
 */
public class NamespaceHelper {


    private static Cache cache;

    public static void init(){
        if(Objects.isNull(cache)) {
            CacheManager cacheManager = ApplicationContextHelper.getApplicationContext().getBean(CacheManager.class);
            cache = cacheManager.getCache("namespace");
        }
    }

    /**
     * get the key for namespace, it will follow this format:
     * <code>#{namespaceKey}{times}:{key}</code>, ie:#area2:8, the <code>times</code> stands for the
     * counter result for namespace : <code>namespaceKey</code>.
     * @param namespaceKey the namespace name
     * @param key the cache key
     * @return the cache key with namespace
     */
    public static String withNP(String namespaceKey,Object key){
        init();
        Integer times = Objects.getDefault(cache.get(namespaceKey, Integer.class), 0);
        return "#" + namespaceKey + times + ":" + key;
    }

    /**
     * "remove" all the cache values under the namespace named <code>namespaceKey</code>, actually it would make the counter
     * for current namespace plus one, then there would not visit the namespace with the old counter result. finally, the real
     * clear operation would be do by the cache client itself.
     * @param namespaceKey the namespace name
     * @return empty string
     */
    public static String removeNP(String namespaceKey){
        init();
        Integer times = Objects.getDefault(cache.get(namespaceKey,Integer.class),0);
        if(times < 100) {
            times++;
        }else {
            times = 0;
        }
        cache.put(namespaceKey,times);
        return "";
    }
}
