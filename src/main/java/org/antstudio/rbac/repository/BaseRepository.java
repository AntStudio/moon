package org.antstudio.rbac.repository;

/**
 * 通用仓储类
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
public interface BaseRepository<T> {

	/**
	 * 根据id加载领域
	 * @param id
	 * @return
	 */
	public T get(Long id);
	
	public Long save(T domain);
	
	public Long update(T domain,String...columns);
	
	public Long delete(Long id);
	
}
