package org.moon.utils;

/**
 * the Constant for the application
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
public class Constants {

	public static String SYSTEM_USERNAME = "system_user";
	
	public static Long SYSTEM_USERID = 0L;
	
	public static String SYSTEM_PASSWORD = MD5.getMD5(SYSTEM_USERNAME+Dates.getFormatedToday(null));
	
	public static Long SYSTEM_ROLEID = SYSTEM_USERID-2323;
	
	public static Long DEFAULT_DOMAINNO = SYSTEM_ROLEID-2L;
	
	public static Integer DEFAULT_PAGESIZE = 15;

    public static int DEFAULT_PAGEINDEX = 1;
	
	
	/**
	 * 日志类型 系统日志
	 */
	public static String SYSTEM_LOG = "SYSTEM_LOG";
	
	/**
	 * 操作日志
	 */
	public static String OPERATE_LOG = "OPERATE_LOG";

}
