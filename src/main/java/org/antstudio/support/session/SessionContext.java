package org.antstudio.support.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionContext {
	private static ThreadLocal<HttpServletRequest> requestLocal= new ThreadLocal<HttpServletRequest>();  
    private static ThreadLocal<HttpServletResponse> responseLocal= new ThreadLocal<HttpServletResponse>();  
      
   public static HttpServletRequest getRequest() {  
       return (HttpServletRequest)requestLocal.get();  
   }  
   public static void setRequest(HttpServletRequest request) {  
       requestLocal.set(request);  
   }  
   
   /**
    * 返回请求的具体访问路径
    * @return
    */
   public static String getFullPath(){
	   HttpServletRequest request = getRequest();
	   StringBuilder fullPath = new StringBuilder();
	   if(request.getProtocol().contains("HTTPS")){
		   fullPath.append("https://");
	   }else{
		   fullPath.append("http://");
	   }
	   
	   fullPath.append(request.getServerName());
	   
	   if(request.getServerPort()!=80){
		   fullPath.append(":").append(request.getServerPort());
	   }
	   fullPath.append(request.getContextPath());
	   return fullPath.toString();
   }
   
   /**
    * 返回web应用的真实路径
    * @return
    */
   public static String getWebAppPath(){
	   return getSession().getServletContext().getRealPath("/");
   }
   public static HttpServletResponse getResponse() {  
       return (HttpServletResponse)responseLocal.get();  
   }  
   public static void setResponse(HttpServletResponse response) {  
       responseLocal.set(response);  
   }  
   public static HttpSession getSession() {  
       return (HttpSession)((HttpServletRequest)requestLocal.get()).getSession();  
   }  
	
}
