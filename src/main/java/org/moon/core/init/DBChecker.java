package org.moon.core.init;

import org.moon.core.spring.MoonContextListener;
import org.moon.core.spring.MoonServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库连接检查，用于项目启动时检查数据库先决性，如果未通过检查则不会加载Spring
 * <p>需要配合{@link MoonContextListener} 和{@link MoonServlet}使用,具体用法参见这两个类
 * @author Gavin
 * @date Jul 17, 2014
 */
public class DBChecker {

	public static String DBCHECKER = "MOON_DBChecker";
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private ServletContext servletContext;
	
	private Properties p = new Properties();
	
	private static int NOTCHECKED = 0,
					   NORMAL = 1,
					   EXCEPTION = 2;
	
	private int status = NOTCHECKED;
	public DBChecker(ServletContext servletContext){
		this.servletContext = servletContext;
		servletContext.setAttribute(DBCHECKER, this);
		try {
			p.load(this.servletContext.getResourceAsStream("/WEB-INF/config/DBPool.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		check();
	}
	
	
	private void check(){
		try{
			Class.forName(p.getProperty("driver"));
			DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
			status = NORMAL;
		}catch (Exception e){
			logger.error("The DataBase can't connected. "+e.getMessage());
            e.printStackTrace();
			status = EXCEPTION;
			return;
		}
	}
	
	public void reCheck(){
		check();
	}
	
	public boolean isDBValid(){
		return status == NORMAL;
	}
	
	public void saveDBSettings(Map<String,String> params) throws FileNotFoundException, IOException{
		File f = new File(this.servletContext.getRealPath("/WEB-INF/config/DBPool.properties"));
		FileOutputStream out = new FileOutputStream(f);
		try{
			p.putAll(params); 
			p.store(out, "呵呵");
		}finally{
			out.close();
		}
		
	}
}
