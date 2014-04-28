package org.moon.tag.util;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.PageContext;

/**
 * request 工具类
 * @author Gavin
 * @date 2013-8-6 上午12:08:54
 */
public class RequestUtils {

	/**
	 * 获取上下文路径
	 * @param pageContext
	 * @return
	 */
	public static String getContextPath(PageContext pageContext){
		ServletRequest request = pageContext.getRequest();
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/"+pageContext.getServletContext().getContextPath()+"/";
	}
	
}
