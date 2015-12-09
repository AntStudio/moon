package org.moon.maintenance.service;

import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author:Hinsteny
 * @date 2015/05/13 0018
 */
public interface AnalyticalService {

    public List<Map<String,Object>> analyseRegisterByDate(Map<String, Object> params);

    public List<Map<String,Object>> analyseRegisterByMonth(Map<String, Object> params);

    public List<Map<String,Object>> analyseRegisterByType();

    public List<Map<String,Object>> analyseRegisterDoctorByStatus(Map<String, Object> params);

}
