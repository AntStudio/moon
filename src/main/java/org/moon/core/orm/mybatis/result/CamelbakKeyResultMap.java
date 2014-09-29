package org.moon.core.orm.mybatis.result;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * 用于MyBatis返回结果时，将key由下划线转为驼峰形式。这里仅仅声明一种Map类型，
 * 配合{@link org.moon.core.orm.mybatis.wrapper.CamelbakKeyMapWrapperFactory}使用，
 * 在Mybatis的sql映射中配置：
 * <code>
 *     resultType="org.moon.core.orm.mybatis.result.CamelbakKeyResultMap"
 * </code>
 * 或者使用别名：
 * <code>
 *     resultType="CamelbakKeyResultMap"
 * </code>
 * @author:Gavin
 * @date 9/24/2014
 */
public class CamelbakKeyResultMap<K,V> extends HashMap<K,V> {



}
