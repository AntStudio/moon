package org.moon.core.orm.mybatis.wrapper;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.MapWrapper;
import org.moon.utils.Strings;

import java.util.Map;

/**
 * 用于转换字段名为驼峰形式的包装器
 * @author Gavin
 * @date Sep 15, 2014
 */
public class CamelbakKeyMapWrapper extends MapWrapper {
    private Map map ;
    public CamelbakKeyMapWrapper(MetaObject metaObject, Map<String, Object> map) {
        super(metaObject,map);
        this.map = map;
    }

    @Override
    public void set(PropertyTokenizer prop, Object value) {
        if (prop.getIndex() != null) {
            Object collection = resolveCollection(prop, map);
            setCollectionValue(prop, collection, value);
        } else {
            map.put(Strings.changeUnderlineToCamelBak(prop.getName()), value);
        }
    }
}
