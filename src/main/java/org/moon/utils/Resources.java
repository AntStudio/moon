package org.moon.utils;

import java.io.InputStream;

/**
 * 资源文件工具类
 * @author Gavin
 * @date 2014年3月11日
 */
public class Resources {

    /**
     * 加载资源文件
     * @param path
     * @return
     */
    public static InputStream load(String path){
        if(path==null){
            return null;
        }
        if(!path.startsWith("/")){
            path="/"+path;
        }
        return Resources.class.getResourceAsStream(path);
    }
    
}
