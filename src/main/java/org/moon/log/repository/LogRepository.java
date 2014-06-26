package org.moon.log.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.moon.log.domain.Log;
import org.springframework.stereotype.Repository;


/**
 * 日志数据库操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Repository
public interface LogRepository {

	/**
	 * 添加新日志
	 * @param log
	 */
	public void save(@Param("log")Log log);
	
	/**
	 * 更新日志
	 * @param log
	 */
	public void update(@Param("log")Log log);
	
	/**
	 * 查询日志
	 * @param params
	 * @return
	 */
	public List<Long> getLogs(Map<String,Object> params);
	
	public Integer getLogs_count(Map<String,Object> params);
	
	/**
	 * 获取日志详情
	 * @param id
	 * @return
	 */
	public Log getLog(@Param("id")Long id);
	
	
}
