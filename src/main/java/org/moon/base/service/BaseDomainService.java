package org.moon.base.service;

import com.reeham.component.ddd.model.ModelLoader;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.pagination.Pager;

import java.util.List;
import java.util.Map;



/**
 * 服务类通用接口
 * @author Gavin
 * @version 1.0
 * @Date 2012-11-27  
 */
public interface BaseDomainService<T> extends ModelLoader,BaseService{

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
	 * 根据类型获取领域
	 * @param c
	 * @param id
	 * @return
	 */
	public <K> K loadDomain(Class<K> c, Long id);
	
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
	 * 获取分页结果
	 * @param criteria
	 * @return
	 */
	public Pager listForPage(Criteria criteria, DataConverter<Map> dataConverter);
	
	/**
	 * 将查询结果转换为域模型
	 * @param criteria
	 * @return
	 */
	public List<T> listForDomain(Criteria criteria);

	/**
	 * 根据id删除,logicFlag标示当前是否为逻辑删除
	 * @param ids
	 * @param logicFlag
	 */
	public void delete(Long[] ids, boolean logicFlag);
}
