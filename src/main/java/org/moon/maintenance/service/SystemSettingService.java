package org.moon.maintenance.service;

import java.util.Map;

/**
 * 系统设置
 * @author:Gavin
 * @date 2014/11/18 0018
 */
public interface SystemSettingService{

    public void updateSetting(Map<String, String> settings);

    public Map getSettingMap(String prefix);

    public Map getSettingMap();

    public Map getSetting(String name);
}
