package org.moon.utils;

import java.lang.reflect.Field;



/**
 * the util for properties
 * @author Gavin
 * @version 1.0
 * @date 2012-12-7
 */
public class ClassPropertiesUtil {

	/**
	 * copy the properties from src to dest,if copy is false,when the field is null
	 * this field property would not be copied
	 * @param src
	 * @param dest
	 * @param copyNull
	 * @return
	 */
	public static Object copyProperties(Object src,Object dest,String[] include,String[] exclude,boolean copyNull){
		Class<?> srcClass = src.getClass();
		Class<?> destClass = dest.getClass();
		if(srcClass!=destClass)
			try {
				throw new Exception("The class type must be the same between "+src+" and "+dest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		try {
		for(Field f:srcClass.getDeclaredFields()){
			if((copyNull||f.get(src)!=null)&&contains(include,f.getName())&&!contains(exclude,f.getName())){
				//(copyNull||f.get(src)!=null) 判断出可以添加为空时和不可以添加为空并且属性不为空
				if(!f.isAccessible())
					f.setAccessible(true);
				f.set(dest, f.get(src));}
		}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return dest;
	}
	
	public static Object copyProperties(Object src,Object dest,String[] include,String...exclude){
		return copyProperties(src, dest,include,exclude, true);
	}
	
	public static Object copyProperties(Object src,Object dest,boolean isInclude,String...include){
		if(isInclude) {
            return copyProperties(src, dest, include, new String[0], true);
        }
		else {
            return copyProperties(src, dest, new String[0], include, true);
        }
	}
	
	
	
	public static  boolean contains(String[] array,String value){
		for(String s:array){
			if(s.equals(value))
				return true;
		}
		return false;
	}
}
