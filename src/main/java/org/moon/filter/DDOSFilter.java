/*package org.moon.filter;

import org.apache.catalina.comet.CometEvent;
import org.apache.catalina.comet.CometFilterChain;
import org.apache.catalina.filters.RequestFilter;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;*/

/**
 * DDOS 请求过滤器，拦截频繁请求的主机
 * web.xml中配置：
 *
 * <filter>
 *   <filter-name>DDOSFilter</filter-name>
 *   <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
 *</filter>
 *<filter-mapping>
 *   <filter-name>DDOSFilter</filter-name>
 *   <url-pattern>/*</url-pattern>
 *</filter-mapping>
 *
 * applicationContext.xml中配置：
 *  <bean id="DDOSFilter" class="org.moon.filter.DDOSFilter"></bean>
 *  pom.xml中配置：
 *  <dependency>
 *      <groupId>org.apache.tomcat</groupId>
 *      <artifactId>tomcat-catalina</artifactId>
 *      <version>8.0.23</version>
 *      <scope>compile</scope>
 * </dependency>
 * 注意：因为需要tomcat依赖包才能编译，所以使用时去掉注释，按照上面的步骤配置即可
 * @author:Gavin
 * @date 2015/6/11
 */
/*public class DDOSFilter extends RequestFilter {
    private static final Log log = LogFactory.getLog(DDOSFilter.class);

    private ConcurrentHashMap<String,LinkedHashMap<Long,Object>> ipRecord = new ConcurrentHashMap<String,LinkedHashMap<Long,Object>>();

    //访问次数阀值
    private int threshold = 200;

    //触发阀值时，时间段最大值
    private int timeThreshold = 60 * 1000;

    private Object o = new Object();

    Pattern pattern = Pattern.compile(".*(\\.css|\\.js|/plugin/|\\.jpg|\\.png).*");

    public DDOSFilter(){

    }
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = servletRequest.getRemoteAddr();
        if(!pattern.matcher(((HttpServletRequest)servletRequest).getRequestURI()).matches()) {
            record(ip);
        }
        this.process(ip, servletRequest, servletResponse, filterChain);
    }

    @Override
    public void doFilterEvent(CometEvent cometEvent, CometFilterChain cometFilterChain) throws IOException, ServletException {
        this.processCometEvent(cometEvent.getHttpServletRequest().getRemoteAddr(), cometEvent, cometFilterChain);
    }

    @Override
    protected Log getLogger() {
        return log;
    }

    private void record(String ip) throws IOException {
        long now = System.currentTimeMillis();
        LinkedHashMap<Long,Object> recordsOfIp = ipRecord.getOrDefault(ip,new LRUHashMap<Long,Object>(threshold));
        recordsOfIp.put(now , o);
        ipRecord.put(ip,recordsOfIp);
        if(recordsOfIp.size() >= threshold){
            Long first = recordsOfIp.keySet().iterator().next();
            if(now - first <= timeThreshold){
                deny = Pattern.compile(ip+"|"+deny);
            }
        }
    }

    private static class LRUHashMap<K,V> extends LinkedHashMap<K,V>{

        private int cacheSize;

        public LRUHashMap(int cacheSize){
            super();
            this.cacheSize = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K,V> eldest) {
            return size() >= cacheSize;
        }
    }

}*/
