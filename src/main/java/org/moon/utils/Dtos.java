package org.moon.utils;

import java.util.ArrayList;
import java.util.List;

import org.moon.core.orm.mybatis.DataConverter;
import org.springframework.util.Assert;

/**
 * dto工具类
 * @author Gavin
 * Jun 30, 2014
 */
public class Dtos {
	
	/**
	 * 根据converter转化对象
	 * @param objs
	 * @param coverter
	 * @return
	 */
	public static <T> List<Object> covert(List<T> objs,DataConverter<T> coverter){
		Assert.notNull(objs,"The data used to convert should not be null");
		Assert.notNull(coverter,"The data coverter should not be null");
		List<Object> results = new ArrayList<Object>();
		for(T t:objs){
			results.add(coverter.convert(t));
		}
		return results;
	}
	
}
