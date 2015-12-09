package org.moon.maintenance.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.utils.Pair;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统分析类
 * @author:Hinsteny
 * @date 2015/5/12
 */
@Repository
public interface AnalyticalRepository extends BaseRepository{

    public List<Map<String,Object>> countRegisterByDate(Map<String, Object> params);

    public List<Map<String,Object>> countRegisterByMonth(Map<String, Object> params);

    public List<Map<String,Object>> countRegisterByType();

    public List<Map<String,Object>> countRegisterDoctorByStatus(Map<String, Object> params);

    /**
     * 获取一周内的注册人数统计
     * @param start
     * @param end
     * @return
     */
    public List<Pair<Integer,Long>> countRegisterByWeek(@Param("startRegisterTime")LocalDate start,@Param("endRegisterTime")LocalDate end);
}
