package org.moon.log.service;

import java.util.List;
import java.util.Map;

import org.moon.base.service.BaseService;
import org.moon.log.domain.Log;
import org.moon.pagination.Pager;


/**
 * 日志操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
public interface LogService extends BaseService<Log>{

	/**
	 * 查询日志，返回页面形式的日志
	 * @param params
	 * @return
	 */
	public Pager getLogsForPage(Map<String,Object> params);
	
	public List<Log> getLogs(Map<String,Object> params);

	/**
	 * 保存日志
	 * @param log
	 */
	public void log(Log log);
	
}
