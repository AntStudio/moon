package org.moon.core.spring;

import org.moon.core.init.DBChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link MoonServlet} 将Spring的{@link DispatcherServlet}进行了
 * <p>简单的包装，增添了一层数据库的检测.
 * <p>使用时，需要用{@link MoonServlet}代替{@link DispatcherServlet}在web.xml中的配置,如：
 * <code>
 * <pre>
 * &lt;servlet&gt;
 *   &lt;servlet-name&gt;spring&lt;/servlet-name&gt;
 *   &lt;servlet-class&gt;org.moon.MoonServlet&lt;/servlet-class&gt;
 * &lt;/servlet&gt;
 * </pre></code>
 * @author Gavin
 * @date Jul 17, 2014
 */
@SuppressWarnings("serial")
public class MoonServlet extends DispatcherServlet{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ServletConfig config;
	
	private DBChecker dbChecker;
	@Override
	public void init(ServletConfig config) throws ServletException {
		this.config = config;
		dbChecker = (DBChecker) config.getServletContext().getAttribute(DBChecker.DBCHECKER);
		if(dbChecker.isDBValid()){
			super.init(config);
		}else{
			logger.warn("Would not init the Spring Servlet,Caused by the Database issue.");
		}
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		//数据库合法时,直接使用Spring Servlet的逻辑处理
		if(dbChecker.isDBValid()){
			super.service(request, response);
		}else{
			if(request.getRequestURI().equals(request.getContextPath()+"/DBSetting") && "POST".equals(request.getMethod())){
				saveDBSettings(request);//保存数据库配置
				if(dbChecker.isDBValid()){//跳转到主页面
					response.sendRedirect("");
				}
			}
			if(!dbChecker.isDBValid()){//显示数据库设置界面
				response.setContentType("text/html");
				InputStream in = request.getSession().getServletContext().getResourceAsStream("/WEB-INF/classes/dbSetting.html");
				OutputStream out = response.getOutputStream();
				byte[] data = new byte[1024];
				int position = -1;
				while((position=in.read(data))!=-1){
					out.write(data,0,position);
				}
				in.close();
				out.close();
			}
		}
	}
	
	/**
	 * 初始化Spring Servlet
	 * @throws ServletException
	 */
	private void initSpring() throws ServletException{
		new MoonContextListener().initWebApplicationContext(config.getServletContext());
		this.init(config);
	}
	
	/**
	 * 保存并验证数据库配置，及时数据库配置无法通过验证也会保存到数据库配置文件里
	 * @param request
	 */
	private void saveDBSettings(HttpServletRequest request){
		try {
			Map<String,String> params = new HashMap<String, String>();
			Map<String,String[]> originMap = request.getParameterMap();
			for(String key:originMap.keySet()){
				params.put(key, request.getParameter(key));
			}
			dbChecker.saveDBSettings(params);
			dbChecker.reCheck();
			if(dbChecker.isDBValid()){
				initSpring();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
