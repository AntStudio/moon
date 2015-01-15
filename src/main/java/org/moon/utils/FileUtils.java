package org.moon.utils;

import org.moon.exception.ApplicationRunTimeException;

import java.io.*;
import java.net.URL;

/**
 * 文件工具类
 * @author Gavin
 * @version 1.0
 * @date 2013-2-18
 */
public class FileUtils {

	/**
	 * 保存文件
	 * @param in
	 * @param path
	 * @throws java.io.IOException
	 */
	public static void save(InputStream in,File file) throws IOException{
		if(!file.exists()){
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);
		byte[] data = new byte[10240];
		int length = 0;
		while((length=in.read(data))!=-1){
			out.write(data,0,length);
		}
		out.flush();
		out.close();
		in.close();
	}

	/**
	 * 获取文件后缀名
	 * @param file
	 * @return
	 */
	public static String getExtname(File file){
		String name = file.getName();
		int position = name.lastIndexOf(".");
		if(position!=-1){
			return name.substring(position+1);
		}
		return "";
	}

	/**
	 * 获取文件前缀名
	 * @param file
	 * @return
	 */
	public static String getPrefixName(File file){
		String name = file.getName();
		int position = name.lastIndexOf(".");
		if(position!=-1){
			return name.substring(0,position);
		}
		return "";
	}

	/**
	 * 获取不存在的文件名，以文件所在路径为父目录。如果存在则累加数字。
	 * <p>如：文件名为mypic.png,如果此时文件夹里已经存在同名文件，那么去获取mypic(1).png是否存在，如果存在则继续累加判断，否则返回</p>
	 * <p>注意：最大累加数字为50，如果累加到50依然没有获取到不存在的文件，那么将使用：文件前缀+当前时间+累加基数+后缀名的方式来处理，然后又继续遍历最多50次</p>
	 * @param file
	 * @return
	 */
	public static File getFileNotExists(File file){
		String baseDir = file.getParent(),
			   prefix = getPrefixName(file),
			   extname = getExtname(file);
		int duplicatNum = 0;
		while(file.exists()){
			duplicatNum++;
			file = new File(baseDir,prefix+"("+duplicatNum+")."+extname);
			if(duplicatNum==50){//每种最多循环50次，也就是最多出现xxx(50).png这样的文件
				prefix =  prefix+System.currentTimeMillis();
				duplicatNum = 0;
			}
		}
		return file;
	}

	/**
	 * 创建文件,如果有依赖的父目录不存在，也会自动创建.
	 * @param path
	 * @param dir
	 * @return
	 * @throws java.io.IOException
	 */
	public static File createIfNotExists(String path,boolean dir) throws IOException{
		File file = new File(path);
		if(!file.exists()){
			if(dir){
				file.mkdirs();
			}else{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
		}
		return file;
	}

    /**
     * 将输入流数据写入输出流
     * @param in
     * @param out
     * @throws java.io.IOException
     */
    public static void write(InputStream in,OutputStream out) throws IOException {
        try {
            byte[] data = new byte[10240];
            int length = 0;
            while ((length = in.read(data)) > -1) {
                out.write(data, 0, length);
            }
        }finally {
            in.close();
            out.close();
        }

    }

    /**
     * 加载资源文件
     * @param filePath
     * @return
     */
    public static InputStream loadFile(String filePath){
        if(!filePath.startsWith("/")){
            filePath="/"+filePath;
        }
        URL url = FileUtils.class.getResource(filePath);
        if(url == null){
            throw new ApplicationRunTimeException(url+" can not found.");
        }
        return FileUtils.class.getResourceAsStream(filePath);
    }
}
