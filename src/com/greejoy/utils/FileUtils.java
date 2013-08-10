package com.greejoy.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * 文件工具类
 * @author Gavin
 * @version 1.0
 * @date 2013-2-18
 */
public class FileUtils {

	/**
	 * 根据属于刘保存文件
	 * @param in
	 * @param path
	 */
	public static void save(InputStream in,File file){
		try{
		if(!file.exists())
			file.createNewFile();
		FileOutputStream out = new FileOutputStream(file);
		byte[] data = new byte[10240];
		int length = 0;
		while((length=in.read(data))!=-1){
			out.write(data,0,length);
		}
		out.flush();
		out.close();
		in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
