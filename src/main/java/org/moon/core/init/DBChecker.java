package org.moon.core.init;

import org.moon.core.spring.MoonContextListener;
import org.moon.core.spring.MoonServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DriverManager;
import java.util.Map;
import java.util.Properties;

/**
 * this tool used to check if the database can connect normally or not. if there has some issues when connecting db, the
 * project would not loading the spring container or others.
 *
 * @author GavinCook
 * @since 1.0.0
 * @date 2014-07-17
 * @see MoonContextListener
 * @see MoonServlet
 */
public class DBChecker {

    /**
     * the attribute name which set the db checker instance into the servletContext
     */
    public static String DB_CHECKER = "MOON_DBChecker";

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ServletContext servletContext;

    /**
     * properties config in file : "/WEB-INF/config/DBPool.properties"
     */
    private Properties p = new Properties();

    private static int NOTCHECKED = 0,
            NORMAL = 1,
            EXCEPTION = 2;

    private int status = NOTCHECKED;

    /**
     * the db setting file path
     */
    private final String dbSettingFile = "/WEB-INF/config/DBPool.properties";

    public DBChecker(ServletContext servletContext) {
        this.servletContext = servletContext;
        servletContext.setAttribute(DB_CHECKER, this);
        try {
            p.load(this.servletContext.getResourceAsStream(dbSettingFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        check();
    }

    /**
     * check the db status, the result can be obtained from the <code>status</code> property
     */
    private void check() {
        try {
            Class.forName(p.getProperty("driver"));
            DriverManager.getConnection(p.getProperty("url"), p.getProperty("username"), p.getProperty("password"));
            status = NORMAL;
        } catch (Exception e) {
            logger.error("The DataBase can't connected. " + e.getMessage());
            e.printStackTrace();
            status = EXCEPTION;
        }
    }

    public void reCheck() {
        check();
    }

    public boolean isDBValid() {
        return status == NORMAL;
    }

    public void saveDBSettings(Map<String, String> params) throws IOException {
        File f = new File(this.servletContext.getRealPath(dbSettingFile));
        try (FileOutputStream out = new FileOutputStream(f)) {
            p.putAll(params);
            p.store(out, "update the DBPool.properties");
        }
    }

}
