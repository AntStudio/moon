package org.moon.maintenance.service.impl;

import org.moon.maintenance.repository.SystemSettingRepository;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.utils.Objects;
import org.moon.utils.Pair;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author GavinCook
 * @date 2014/11/18 0018
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService{

    @Resource
    private SystemSettingRepository repository;

    @Override
    public void updateSetting(Map<String, Object> settings) {

        Map<String,String> settingMap = getSettingMap();

        for (String key : settings.keySet()) {
            if (settingMap.containsKey(key)) {
                updateSetting(key,settings.get(key)+"");
            } else {
                repository.addSetting(key, settings.get(key)+"");
            }
        }

    }

    @Override
    @CacheEvict(value = "cache" , key="'setting'.concat(#key)")
    public void updateSetting(String key, String value) {
        repository.updateSetting(key, value);
    }

    @Override
    public Map<String,String> getSettingMap(String prefix) {
        List<Pair<String,String>> existSettings = repository.listSettings(prefix);
        Map<String,String> settingMap = new HashMap<String,String>();
        if(Objects.nonNull(existSettings)) {
            for (Pair<String,String> p : existSettings) {
                settingMap.put(p.getKey(), p.getValue());
            }
        }
        return settingMap;

    }

    @Override
    public Map<String,String> getSettingMap() {
        return getSettingMap(null);
    }

    @Override
    @Cacheable(value = "cache" , key="'setting'.concat(#name)", unless = "#result==null")
    public Pair<String,String> getSetting(String name) {
        return repository.getSetting(name);
    }

    @Override
    public String getSetting(String name, String defaultValue) {
        Pair<String,String> p = getSetting(name);
        if(Objects.nonNull(p)){
            return p.getValue();
        }
        return defaultValue;
    }


}
