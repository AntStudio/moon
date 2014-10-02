package org.moon.utils;

import org.moon.core.orm.mybatis.DataConverter;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * dto工具类
 * @author Gavin
 * Jun 30, 2014
 */
public class Dtos {
	
	/**
	 * 根据converter转化对象
	 * @param objs
	 * @param converter
	 * @return
	 */
	public static <T> List convert(List<T> objs, DataConverter<T> converter){
		Assert.notNull(objs,"The data used to convert should not be null");
		Assert.notNull(converter,"The data converter should not be null");
		List<Object> results = new ArrayList<Object>();
		for(T t:objs){
			results.add(converter.convert(t));
		}
		return results;
	}

    public static DataConverter newUnderlineToCamelBakConverter(){
        return new UnderlineToCamelBakConverter();
    }

    public static DataConverter newUnderlineToCamelBakConverter(boolean isInclude,String...params){
        return new UnderlineToCamelBakConverter(isInclude,params);
    }

    private static class UnderlineToCamelBakConverter implements  DataConverter<Map<String,?>>{

        private String[] include,exclude;

        private UnderlineToCamelBakConverter(){}

        private UnderlineToCamelBakConverter(boolean isInclude,String...params){
            if(isInclude) {
                this.include = params;
            }else{
                this.exclude = params;
            }
        }

        private boolean isInclude(String key){
            if(Objects.isNull(include)){//when not set,all include
                return true;
            }
            for(String includeElement:include){
                if(Objects.nonNull(includeElement) && includeElement.equals(key)){
                    return true;
                }
            }
            return false;
        }

        private boolean isExclude(String key){//when not set, all not exclude
            if(Objects.isNull(exclude)){
                return false;
            }
            for(String excludeElement:exclude){
                if(Objects.nonNull(excludeElement) && excludeElement.equals(key)){
                    return true;
                }
            }
            return false;
        }

        @Override
        public Object convert(Map<String,?> map) {
            Map newMap = new HashMap();
            for(Map.Entry<String,?> e:map.entrySet()){
                if(isInclude(e.getKey())&&!isExclude(e.getKey())) {
                    newMap.put(Strings.changeUnderlineToCamelBak(e.getKey()), e.getValue());
                }
            }
            return newMap;
        }
    }

}
