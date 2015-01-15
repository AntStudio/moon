package org.moon.maintenance.service.impl;

import org.moon.maintenance.repository.SystemSettingRepository;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.utils.Objects;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author:Gavin
 * @date 2014/11/18 0018
 */
@Service
public class SystemSettingServiceImpl implements SystemSettingService{

    @Resource
    private SystemSettingRepository repository;

    @Override
    public void updateSetting(Map<String, String> settings) {

        Map settingMap = getSettingMap();

        for (String key : settings.keySet()) {
            if (settingMap.containsKey(key)) {
                repository.updateSetting(key, settings.get(key));
            } else {
                repository.addSetting(key, settings.get(key));
            }
        }

    }

    @Override
    public Map getSettingMap(String prefix) {
        List<Map> existSettings = repository.listSettings(prefix);
        Map settingMap = new HashMap();
        if(Objects.nonNull(existSettings)) {
            for (Map m : existSettings) {
                settingMap.put(m.get("name"), m.get("value"));
            }
        }
        return settingMap;

    }

    @Override
    public Map getSettingMap() {
        return getSettingMap(null);
    }

    @Override
    public Map getSetting(String name) {
        return repository.getSetting(name);
    }


}
