package com.greejoy.rbac.service;


/**
 * the interface for basic service
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27  
 */
public interface BaseService<T> {

  
	/**
	 * persistence a object into database
	 * @param o
	 *//*
	public void save(T o);
	
	*//**
	 * update the object
	 * @param o
	 * @return
	 *//*
	public Integer update(T o);
	
	*//**
	 * delete logically all the object across the id array 
	 * @see {@code com.greejoy.rbac.service.BaseService.delete(Long ids[],boolean logicDel)} 
	 * @param ids
	 *//*
	public void delete(Long[] ids);
	
	*//**
	 * delete logically the object across the id
	 * @param id
	 * @see {@code com.greejoy.rbac.service.BaseService.delete(Long id,boolean logicDel)} 
	 *//*
	public void delete(Long id);
	
	*//**
	 * if logicDel is <code>true</code>,just update the deleteFlag value to true,
	 * else delete all the records by the id array
	 * @param ids
	 * @param logicDel
	 * @see {@code com.greejoy.rbac.service.BaseService.delete(Long[] ids)} 
	 *//*
	public void delete(Long ids[],boolean logicDel);
	
	*//**
	 * if logicDel is <code>true</code>,just update the deleteFlag value to true,
	 * else delete the record by the id 
	 * @param id
	 * @param logicDel
     * @see {@code com.greejoy.rbac.service.BaseService.delete(Long id)} 
	 *//*
	public void delete(Long id,boolean logicDel);
	*/
	
	
	/**
	 * get the domain from model container cache,if not exist,get from database.
	 * @param id
	 * @return
	 */
	public T getModel(Long id);
	
	public T get(Long id);
}
