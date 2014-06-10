package org.moon.utils;

/**
 * Object工具类
 * @author Gavin
 * @date 2014-6-10 下午11:18:23
 */
public class Objects {

	public static boolean isNull(Object o){
		return o==null;
	}
	
	public static boolean nonNull(Object o){
		return o!=null;
	}
	
	public static <T> T getDefault(T t,T defaultVal){
		if(t==null){
			return defaultVal;
		}
		return t;
	}
}
