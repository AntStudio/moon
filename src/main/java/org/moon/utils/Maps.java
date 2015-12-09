package org.moon.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Map tools
 * @author GavinCook
 * @since 1.0.0
 */
public class Maps {

    /**
     * map all the parameters into a HashMap, the odds for key ,evens for value.now this deprecated,
     * use {@link #mapIt(Class, Class, Object...)} instead
     * @param params the data which need mapped
     * @return the map constructed by the parameters
     * @see {@link #mapIt(Class, Class, Object...)}
     */
    @Deprecated
    public static Map mapIt(Object... params) {
        Map m = new HashMap();
        int length = params.length;
        if (params == null || length < 2) {
            return m;
        } else {
            if (length % 2 != 0) {
                length--;//ignore the last when the parameters length is not even
            }
            for (int i = 0; i < length; ) {
                m.put(params[i++], params[i++]);
            }
        }
        return m;
    }

    /**
     * map all the parameters into a HashMap, the odds for key ,evens for value.
     * @param keyClass the key class
     * @param valueClass the value class
     * @param params the map data
     * @return the map constructed by the params
     */
    @SuppressWarnings({"unchecked","unused"})
    public static <K, V> Map<K, V> mapIt(Class<K> keyClass, Class<V> valueClass, Object... params) {
        Map<K, V> m = new HashMap<>();
        int length;
        if (params == null || (length = params.length) < 2) {
            return m;
        } else {
            if (length % 2 != 0) {
                length--;//当传入的参数不是偶数个数，忽略最后一个
            }
            for (int i = 0; i < length; ) {
                m.put((K)params[i++], (V)params[i++]);
            }
        }
        return m;
    }

    /**
     * map all the parameters into a HashMap, the odds for key ,evens for value. the SO means the key is String, and
     * the value is Object
     * @param params the map data
     * @return the map constructed by the params
     */
    @SuppressWarnings({"unchecked","unused"})
    public static Map<String, Object> mapItSO(Object... params) {
        return mapIt(String.class, Object.class, params);
    }

    /**
     * use <code>ObjectMapper</code> cast object to map
     * @param keyClass the key class
     * @param valueClass the value class
     * @param o the object which need casted
     * @return the map for the object o
     */
    @SuppressWarnings({"unchecked","unused"})
    public static <K, V> Map<K, V> toMap(Class<K> keyClass, Class<V> valueClass,Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(o, Map.class);
    }
}
