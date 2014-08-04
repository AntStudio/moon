package org.moon.core.spring;

import org.moon.base.init.DBChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContextEvent;

/**
 * {@link MoonContextListener} 将Spring的{@link ContextLoaderListener}进行了
 * <p>简单的包装，增添了一层数据库的检测.
 * <p>使用时，需要用{@link MoonContextListener}代替{@link ContextLoaderListener}在web.xml中的配置,如：
 * <code>
 * <pre>
 *  &lt;listener&gt;
 *       &lt;listener-class&gt;org.moon.MoonContextListener&lt;/listener-class&gt;
 *  &lt;/listener&gt;
 * </pre></code>
 * @author Gavin
 * @date Jul 17, 2014
 */
public class MoonContextListener extends ContextLoaderListener{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		DBChecker dbChecker = new DBChecker(event.getServletContext());
		if(!dbChecker.isDBValid()){
			logger.warn("Would not init the Spring context,cause by the DataBase issue.");
		}else{
			super.contextInitialized(event);
		}
		
	}
}
