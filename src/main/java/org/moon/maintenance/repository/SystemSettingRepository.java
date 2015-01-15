package org.moon.maintenance.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统设置仓储类
 * @author:Gavin
 * @date 2014/11/18 0018
 */
@Repository
public interface SystemSettingRepository extends BaseRepository{

    public void addSetting(@Param("name") String name, @Param("value") String value);

    public void updateSetting(@Param("name") String name, @Param("value") String value);

    public List<Map> listSettings(@Param("prefix") String prefix);

    public Map getSetting(@Param("name") String name);
}
