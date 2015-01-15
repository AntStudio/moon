package org.moon.core.cache.memcached;

/**
 * 用于缓存数据不存在时加载数据
 * Created by Gavin on 2015/1/6 0006.
 */
public interface Loader<T> {

    public T load();

}
