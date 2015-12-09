package org.moon.maintenance.service;

import org.moon.utils.Pair;

import java.util.Map;

/**
 * 系统设置
 * @author GavinCook
 * @date 2014/11/18 0018
 */
public interface SystemSettingService {

    void updateSetting(Map<String, Object> settings);

    void updateSetting(String key, String value);

    Map<String,String> getSettingMap(String prefix);

    Map<String,String> getSettingMap();

    Pair<String,String> getSetting(String name);

    String getSetting(String name, String defaultValue);
}
