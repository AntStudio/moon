package org.moon.core.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class SessionContext {
	private static ThreadLocal<HttpServletRequest> requestLocal= new ThreadLocal<HttpServletRequest>();  
    private static ThreadLocal<HttpServletResponse> responseLocal= new ThreadLocal<HttpServletResponse>();  
      
   public static HttpServletRequest getRequest() {  
       return (HttpServletRequest)requestLocal.get();  
   }  
   public static void setRequest(HttpServletRequest request) {  
       requestLocal.set(request);  
   }  
   
    public static String getContextPath(){
        HttpServletRequest request = getRequest();
        StringBuilder contextPath = new StringBuilder();
        if(request.getProtocol().contains("HTTPS")){
            contextPath.append("https://");
        }else{
            contextPath.append("http://");
        }

        contextPath.append(request.getServerName());

        if(request.getServerPort()!=80){
            contextPath.append(":").append(request.getServerPort());
        }
        contextPath.append(request.getContextPath());
        return contextPath.toString();
    }
   
   /**
    * 返回web应用的真实路径,此方法只能创建了Session后才能使用
    * @return
    * @see {@link org.moon.core.spring.ApplicationContextHelper#getWebAppPath(org.springframework.context.ApplicationContext)}
    */
   public static String getWebAppPath(){
	   try{
		   return URLDecoder.decode(getSession().getServletContext().getRealPath("/"),"UTF-8");
	   }catch(UnsupportedEncodingException e){
		   e.printStackTrace();
		   return "";
	   }
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
