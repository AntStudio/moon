package org.antstudio.log.service;

import java.util.List;
import java.util.Map;

import org.antstudio.log.domain.Log;
import org.antstudio.pagination.Pager;
import org.antstudio.rbac.service.BaseService;


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
