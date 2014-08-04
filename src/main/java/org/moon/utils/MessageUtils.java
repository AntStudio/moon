package org.moon.utils;

import org.moon.base.domain.BaseDomain;

import java.lang.reflect.Field;
import java.util.*;


/**
 * to wrap information return to front end
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
public class MessageUtils {

	 /**
	  * construct the Map message with the result
	  * @param success
	  * @return
	  */
	public static Map<String,Object> getMapMessage(boolean success){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("success", success);
		return  m;
	}
	
	/**
	 * 根据返回成功标志和参数名、参数值,获取返回message
	 * @param success
	 * @param name
	 * @param value
	 * @return
	 */
	public static Map<String,Object> getMapMessage(boolean success,String name,Object value){
		Map<String,Object> m = getMapMessage(success);
		m.put(name, value);
		return m;
	}
	

	public static List<Map<String,Object>> covertDomain2Map(List<? extends BaseDomain> list,String...properties) {
		List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
		if(list==null||list.size()==0){
			return resultList;
		}
		Class<?> domain = list.get(0).getClass();
		Iterator<?> itor = list.iterator();
		Object temp;
		Field field;
		if(properties==null||properties.length==0)
			try{
			while(itor.hasNext()){
				Map<String,Object> m = new HashMap<String,Object>();
				temp = itor.next();
				for(Field f:domain.getDeclaredFields()){
					m.put(f.getName(), f.get(temp));
				}
				resultList.add(m);
			}
			}catch(Exception e){
				e.printStackTrace();
			}
		else
			try{
			while(itor.hasNext()){
				Map<String,Object> m = new HashMap<String,Object>();
				temp = itor.next();
				for(String property:properties){
					try{
						field = domain.getDeclaredField(property);
						m.put(field.getName(),field.get(temp));
					}catch(Exception e){//当属性为父类属性时，调用getter方法
						m.put(property, domain.getMethod("get"+
					property.substring(0,1).toUpperCase()+property.substring(1)).invoke(temp));
					}
				}
				resultList.add(m);
			}
			}catch(Exception e){
				
				e.printStackTrace();
			}
		return resultList;
	}
	
	public static Map<String,Object> toFlexGridPager(List<?> result,int page,Long total){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("rows", result);
		m.put("page", page);
		m.put("total", total);
		return m;
	}
}
