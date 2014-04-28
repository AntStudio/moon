package org.moon.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * the Util related HttpSession
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
public class SessionUtils {

	/**
	 * get the session from request
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession();
	}
	
	
}
