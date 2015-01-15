package org.moon.dictionary.cache.memcached;

import org.moon.core.cache.memcached.Loader;
import org.moon.core.cache.memcached.MemCachedManager;
import org.moon.dictionary.service.DictionaryService;
import org.moon.utils.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 字典缓存管理
 * @author:Gavin
 * @date 2015/1/12 0012
 */
@Component
public class DictionaryCacheManager {

    @Resource
    private MemCachedManager memCachedManager;

    @Resource
    private DictionaryService dictionaryService;

    private final int expire = 7 * 24 * 3600;//缓存时间,7天

    private final String dictionaryKey = "dic#";

    /**
     * 根据字典代码获取字典
     * @param code
     * @return
     */
    public Map<String,Object> getDictionary(final String code){
        return memCachedManager.get(dictionaryKey+code, Map.class, new Loader<Map>() {
            @Override
            public Map load() {
                return dictionaryService.getDictionaryByCode(Maps.mapIt("code",code));
            }
        },expire);
    }

    /**
     * 根据字典code获取字典标识(ID)
     * @param code
     * @return
     */
    public Long getDictionaryId(String code){
        Map<String,Object> dictionary = getDictionary(code);
        Object id = dictionary.get("id");
        try{
            return (Long)id;
        }catch (Exception e){
            return Long.parseLong(id+"");
        }
    }
}
