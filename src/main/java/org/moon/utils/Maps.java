package org.moon.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Map工具类
 * @author Gavin
 * @date 2014年3月11日
 */
public class Maps {

    /**
     * 根据传入的数组生成一个HashMap,偶数下标表示key,奇数表示value.
     * @param params 如果传入的参数不是偶数个数，忽略最后一个。如果传入null,则直接返回一个空的map.
     * @return
     */
    public static Map mapIt(Object...params){
        Map m = new HashMap();
        int length = params.length;
        if(params==null||length<2){
            return m;
        }else{
            if(length%2!=0){
                length--;//当传入的参数不是偶数个数，忽略最后一个
            }
            for(int i=0;i<length;){
                m.put(params[i++], params[i++]);
            }
        }
        return m;
    }
    
}
