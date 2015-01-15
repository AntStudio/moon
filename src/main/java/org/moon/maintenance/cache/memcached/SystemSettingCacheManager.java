package org.moon.maintenance.cache.memcached;

import org.moon.core.cache.memcached.Loader;
import org.moon.core.cache.memcached.MemCachedManager;
import org.moon.maintenance.service.SystemSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/** 系统配置缓存
 * @author:Gavin
 * @date 2015/1/14 0014
 */
@Component
public class SystemSettingCacheManager {


    @Resource
    private MemCachedManager memCachedManager;

    @Resource
    private SystemSettingService systemSettingService;

    private final int expire = 7 * 24 * 3600;//缓存时间,7天

    private final String settingKey = "sysSet#";

    /**
     * 根据设置名称获取设置信息
     * @param name
     * @return
     */
    public Map<String,Object> getSetting(final String name){
        return memCachedManager.get(settingKey +name, Map.class, new Loader<Map>() {
            @Override
            public Map load() {
                return systemSettingService.getSetting(name);
            }
        },expire);
    }

    /**
     * 根据设置名称获取设置值
     * @param name
     * @return
     */
    public Object getSettingValue(String name){
        Map<String,Object> setting = getSetting(name);
        return setting.get("value");
    }
}
