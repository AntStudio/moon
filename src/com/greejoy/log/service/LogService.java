package com.greejoy.log.service;

import java.util.List;
import java.util.Map;

import com.greejoy.log.domain.Log;
import com.greejoy.pagination.Pager;

/**
 * 日志操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
public interface LogService {

	/**
	 * 查询日志，返回页面形式的日志
	 * @param params
	 * @return
	 */
	public Pager getLogsForPage(Map<String,Object> params);
	
	public List<Log> getLogs(Map<String,Object> params);
	/**
	 * 缓存中加载
	 * @param id
	 * @return
	 */
	public Log getModel(Long id);
	
	/**
	 * 数据库加载
	 * @param id
	 * @return
	 */
	public Log getLog(Long id);
	
	/**
	 * 保存日志
	 * @param log
	 */
	public void log(Log log);
	
}
