package org.moon.log.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.base.repository.BaseRepository;
import org.moon.log.domain.Log;
import org.springframework.stereotype.Repository;

import java.util.Map;


/**
 * 日志数据库操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Repository
public interface LogRepository extends BaseRepository<Log>{

    /**
     * 获取会诊详情
     * @param id
     * @return
     */
    public Log getDetail(Long id);

    @Override
    public Long save(@Param("log")Log log);
}
