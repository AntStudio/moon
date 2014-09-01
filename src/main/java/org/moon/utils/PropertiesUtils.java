package org.moon.utils;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

/**
 * 配置文件读取
 * @author Gavin
 * @date 2013-8-21 上午12:00:42
 */
public class PropertiesUtils {

    private static Logger log = Logger.getLogger(PropertiesUtils.class);
    
	public static Properties loadPropertiesFile(String filePath) throws FileNotFoundException, IOException{
		Properties p = new Properties();
		if(!filePath.startsWith("/")){
			filePath="/"+filePath;
		}
		URL url = PropertiesUtils.class.getResource(filePath);
		if(url == null){
			throw new FileNotFoundException(url+" can not found.");
		}
      
		InputStream in = PropertiesUtils.class.getResourceAsStream(filePath);
        p.load(in);
		return p;
	}
	
	public static Properties loadPropertiesFileIfExist(String filePath){
		try {
			return loadPropertiesFile(filePath);
		} catch (FileNotFoundException e) {
		    log.warn("文件"+filePath+"不存在,不进行加载.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Properties();
	}
}
