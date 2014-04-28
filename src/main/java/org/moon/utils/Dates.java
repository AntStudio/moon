package org.moon.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author Gavin
 * @date 2014-2-23 上午11:31:00
 */
public class Dates {

	/**
	 * 获取格式化后的日期字符串
	 * @param format
	 * @return
	 */
	public static String getFormatedToday(String format){
		if(format==null){
			format = "yyyy-MM-dd";
		}
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
}
