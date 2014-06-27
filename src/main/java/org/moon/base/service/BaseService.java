package org.moon.base.service;

import java.util.List;
import java.util.Map;

import org.moon.core.orm.mybatis.Criteria;
import org.moon.pagination.Pager;



/**
 * 服务类通用接口
 * @author Gavin
 * @version 1.0
 * @Date 2012-11-27  
 */
public interface BaseService<T> {

	/**
	 * 根据id获取对应的领域对象，如果该领域对象不存在于缓存中，那么应该调用{@link #load(Long)}进行领域的加载
	 * @param id
	 * @return
	 */
	public T get(Long id);
	
	/**
	 * 从数据库或者其它地方加载领域
	 * @param id
	 * @return
	 */
	public T load(Long id);
	
	/**
	 * 获取数据列表
	 * @return
	 */
	public List<Map> list();
	
	/**
	 * 根据限制criteria查询数据列表
	 * @param criteria
	 * @return
	 */
	public List<Map> list(Criteria criteria);
	
	/**
	 * 获取分页结果
	 * @param criteria
	 * @return
	 */
	public Pager listForPage(Criteria criteria);
	
	/**
	 * 根据id删除
	 * @param ids
	 */
	public void delete(Long[] ids);
}
