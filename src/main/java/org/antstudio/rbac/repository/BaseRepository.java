package org.antstudio.rbac.repository;

/**
 * the base repository for domain
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
public interface BaseRepository<T> {

	/**
	 * to get the domain from repository based on the id parameter
	 * @param id
	 * @return
	 */
	public T get(Long id);
	
	
}
