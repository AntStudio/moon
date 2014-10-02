package org.moon.utils;

import org.moon.base.domain.BaseDomain;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 域模型工具类
 * @author:Gavin
 * @date 10/2/2014
 */
public class Domains {

    /**
     * 将Map转化为域模型对象
     * @param map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends BaseDomain> T convertMapToDomain(Map<?,?> map,Class<T> clazz){
        assert clazz != null;
        T t = null;
        try {
            t = clazz.newInstance();
            Method[] methods = clazz.getMethods();
            if(Objects.nonNull(map)){
                for(Map.Entry entry : map.entrySet()){
                    String key = (String) entry.getKey();
                    try {
                        Field f = clazz.getDeclaredField(key);
                        if(!f.isAccessible()){
                            f.setAccessible(true);
                        }
                        f.set(t,entry.getValue());
                    }catch (NoSuchFieldException e){//没有该字段定义，尝试setter方法
                        for (Method m : methods) {
                            if (m.getName().equals(("set" + Strings.upperFirst(key)))
                                    && m.getParameterTypes().length == 1) {
                                if (!m.isAccessible()) {
                                    m.setAccessible(true);
                                }
                                if(key.equals("id")){
                                    m.invoke(t, Long.parseLong(entry.getValue()+""));
                                }else {
                                    m.invoke(t, entry.getValue());
                                }
                                break;
                            }
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }
}
