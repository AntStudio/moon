package org.moon.core.orm.mybatis.valueenum;

/**
 * 带有值的枚举类接口,用于枚举类中的name和值不对应的情况.目前主要用于MyBatis的枚举类解析
 * @author gavin
 * @date 2015-04-30
 */
public interface ValueEnum<V,E extends Enum<E>> {

    /**
     * 获取枚举示例对应的值
     * @return
     */
    public V value();

    /**
     * 根据值获取枚举对象
     * @param value
     * @return
     */
    public E valueOf(V value);
}
