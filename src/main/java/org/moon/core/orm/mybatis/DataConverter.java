package org.moon.core.orm.mybatis;

/**
 * 对象转化，主要用于在需要对查询结果进行封装的场景
 * @author Gavin
 * Jun 30, 2014
 */
public interface DataConverter<T> {

	public Object convert(T t);
	
}
