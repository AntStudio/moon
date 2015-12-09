package org.moon.maintenance.service.impl;

import org.moon.maintenance.repository.AnalyticalRepository;
import org.moon.maintenance.repository.SystemSettingRepository;
import org.moon.maintenance.service.AnalyticalService;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.message.WebResponse;
import org.moon.utils.Objects;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统设置
 * @author:Gavin
 * @date 2015/05/18 0018
 */
@Service
public class AnalyticalServiceImpl implements AnalyticalService {

    @Resource
    private AnalyticalRepository analyticalRepository;

    @Override
    public List<Map<String, Object>> analyseRegisterByDate(Map<String, Object> params) {
        return analyticalRepository.countRegisterByDate(params);
    }

    @Override
    public List<Map<String, Object>> analyseRegisterByMonth(Map<String, Object> params) {
        return analyticalRepository.countRegisterByMonth(params);
    }

    @Override
    public List<Map<String, Object>> analyseRegisterByType() {
        return analyticalRepository.countRegisterByType();
    }

    @Override
    public List<Map<String, Object>> analyseRegisterDoctorByStatus(Map<String, Object> params) {
        return analyticalRepository.countRegisterDoctorByStatus(params);
    }

}
