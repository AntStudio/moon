package org.moon.core.cache.memcached;

/**
 * cache setting holder for cache name and the expiry time for cache value
 * @author GavinCook
 * @since 1.0.0
 */
public class CacheSetting {

    private String name;

    private int expiry;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExpiry() {
        return expiry;
    }

    public void setExpiry(int expiry) {
        this.expiry = expiry;
    }
}
