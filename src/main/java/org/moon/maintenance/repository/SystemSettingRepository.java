package org.moon.maintenance.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.utils.Pair;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 系统设置仓储类
 * @author GavinCook
 * @date 2014/11/18 0018
 */
@Repository
public interface SystemSettingRepository extends BaseRepository{

     void addSetting(@Param("name")String name,@Param("value")String value);

     void updateSetting(@Param("name")String name,@Param("value")String value);

     List<Pair<String,String>> listSettings(@Param("prefix")String prefix);

     Pair<String,String> getSetting(@Param("name")String name);
}
