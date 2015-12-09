package org.moon.utils;

import java.util.function.Supplier;

/**
 * Object tools
 * @author GavinCook
 * @since  1.0.0
 */
public class Objects {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean nonNull(Object o) {
        return o != null;
    }

    public static <T> T getDefault(T t, T defaultVal) {
        if (t == null) {
            return defaultVal;
        }
        return t;
    }

    /**
     * get value safety, if there has any exception,use the <code>defaultValue instead</code>
     * @param supplier the value supplier
     * @param defaultValue default value for return when exception thrown
     * @return the value returned by <code>supplier</code>, or <code>defaultValue</code> when exception thrown
     */
    public static <T> T safeGetValue(Supplier<T> supplier, T defaultValue){
        try{
            return supplier.get();
        }catch (Exception e){
            return defaultValue;
        }
    }

    /**
     * cast object to Long safety
     * @param o the object
     * @return Long value for <code>o</code> parameter if cast successfully
     */
    public static Long toLong(Object o){
        if(isNull(o)){
            return null;
        }
        if(o instanceof Long) {
            return (Long) o;
        }else{
            return Long.valueOf(o.toString());
        }
    }

    /**
     * cast object to Long safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return Long value for <code>o</code> parameter if cast successfully
     */
    public static Long toLong(Object o, Long defaultValue){
        Long value ;
        try{
            value = toLong(o);
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * cast object to String safety
     * @param o the object
     * @return String value for <code>o</code> parameter if cast successfully
     */
    public static String toString(Object o){
        if(isNull(o)){
            return null;
        }
        if(o instanceof String) {
            return (String) o;
        }else{
            return o.toString();
        }
    }

    /**
     * cast object to String safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return String value for <code>o</code> parameter if cast successfully
     */
    public static String toString(Object o, String defaultValue){
        String value ;
        try{
            value = toString(o);
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

    /**
     * cast object to int safety
     * @param o the object
     * @param defaultValue the value when parameter <code>o</code> is <code>null</code> or any exception thrown when cast
     * @return int value for <code>o</code> parameter if cast successfully
     */
    public static Integer toInt(Object o,Integer defaultValue){
        Integer value ;
        try{
            if(o instanceof String){
                value = Integer.valueOf(o.toString());
            }else{
                value = (Integer) o;
            }
        }catch (Exception e){
            e.printStackTrace();
            return defaultValue;
        }
        return isNull(value) ? defaultValue : value ;
    }

}
