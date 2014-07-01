package org.moon.log.repository;

import org.moon.base.repository.BaseRepository;
import org.moon.log.domain.Log;
import org.springframework.stereotype.Repository;


/**
 * 日志数据库操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Repository
public interface LogRepository extends BaseRepository<Log>{

}
