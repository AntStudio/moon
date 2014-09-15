package org.moon.core.orm.mybatis.wrapper;

import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;

import java.util.Map;

/**
 * 用于转换字段名为驼峰形式的适配工厂
 * <p>使用方式：</p>
 * <p>
 *     配置在mybatis的配置文件中：
 *     <code>
 *      &lt;objectWrapperFactory type="org.moon.core.orm.mybatis.wrapper.CamelbakKeyMapWrapperFactory"&gt;&lt;/objectWrapperFactory&gt;
 *     </code>
 * </p>
 * @author Gavin
 * @date Sep 15, 2014
 */
public class CamelbakKeyMapWrapperFactory implements ObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        return object instanceof Map;
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        return new CamelbakKeyMapWrapper(metaObject,(Map)object);
    }
}
