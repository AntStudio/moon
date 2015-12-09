package org.moon.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * the Util related Http
 *
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
public class HttpUtils {

	/**
	 * get the session from request
	 * @param request
	 * @return
	 */
	public static HttpSession getSession(HttpServletRequest request) {
		return request.getSession();
	}

	/**
	 * get the special named cookie from request , if not found, return <code>null</code>
	 * @param request the http request
	 * @param cookieName the special cookie name
	 * @return the special named cookie if presented, OR return <code>null</code>
	 */
	public static Cookie getCookie(HttpServletRequest request, String cookieName){
		Cookie cookie = null;
		for(Cookie c : request.getCookies()){
			if(c.getName().equalsIgnoreCase(cookieName)){
				cookie = c;
				break;
			}
		}
		return cookie;
	}

}
