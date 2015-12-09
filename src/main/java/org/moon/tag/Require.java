package org.moon.tag;

import org.apache.log4j.Logger;
import org.moon.tag.util.RequestUtils;
import org.moon.utils.HttpUtils;
import org.moon.utils.Objects;
import org.moon.utils.PropertiesUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * @author Gavin
 * @date 2013-8-6 上午12:27:21
 */
public class Require extends TagSupport {

    private static final long serialVersionUID = -2307579042925929168L;

    @Override
    public int doStartTag() throws JspException {
        return EVAL_PAGE;
    }

    private Logger log = Logger.getLogger(getClass());
    private String type;
    private String src;
    private Properties p;
    private Pattern pattern = Pattern.compile("^\\s*\\{.*\\}\\s*$");
    private String cssDefaultFolder, jsDefaultFolder;

    private String themeName, themeCookieName;
    {
        try {
            p = PropertiesUtils.loadPropertiesFile("~system~requireTag.properties");
            p.putAll(PropertiesUtils.loadPropertiesFileIfExist("requireTag.properties"));
            cssDefaultFolder = p.getProperty("default.css.folder", "css/pages/");
            jsDefaultFolder = p.getProperty("default.js.folder", "js/pages/");
            themeName = p.getProperty("theme.name", "moonTheme");
            themeCookieName = p.getProperty("theme.cookie.name", "moonTheme");
        } catch (FileNotFoundException e) {
            log.error("require初始化失败,未找到配置文件");
            e.printStackTrace();
        } catch (IOException e1) {
            log.error("require初始化失败,读取配置文件出错");
            e1.printStackTrace();
        }
    }


    @Override
    public int doEndTag() throws JspException {
        JspWriter out = this.pageContext.getOut();
        HttpServletRequest request = (HttpServletRequest) this.pageContext.getRequest();
        //Get the website theme, default use 'default' theme
        String theme = Objects.safeGetValue(()-> HttpUtils.getCookie(request, themeCookieName).getValue(),"default");


        StringBuilder sb = new StringBuilder();
        String contextPath = RequestUtils.getContextPath(pageContext);
        if (type == null || "js".equals(type)) {//for js
            for (String s : src.split(",")) {

                if(s.equals(themeName)){
                    importThemeResources(contextPath, theme);
                    continue;
                }

                if (pattern.matcher(s).matches()) {//当是{Name}时,从默认路径获取
                    s = s.replaceAll("[\\s\\{\\}]", "");
                    sb.append(importCss(contextPath, cssDefaultFolder + s + ".css"));
                    sb.append(importJs(contextPath, jsDefaultFolder + s + ".js"));
                } else if (p.containsKey("js." + s)) {
                    if (p.containsKey("js." + s)) {
                        sb.append(importJs(contextPath, p.getProperty("js." + s)));
                    }
                } else if (s.endsWith(".js")) {
                    sb.append(importJs(contextPath, s));
                }
                if (type == null && p.containsKey("css." + s)) {
                    sb.append(importCss(contextPath, p.getProperty("css." + s)));
                }
            }
        } else {//for css
            for (String s : src.split(",")) {
                if (p.containsKey("css." + s)) {
                    sb.append(importCss(contextPath, p.getProperty("css." + s)));
                } else {
                    sb.append(importCss(contextPath, s));
                }
            }
        }
        try {
            out.print(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return EVAL_PAGE;
    }

    /**
     * import theme resources, include js and css if present
     * @param contextPath context path
     * @param theme theme name
     */
    private String importThemeResources(String contextPath, String theme){
        StringBuilder resources = new StringBuilder();

        if(p.contains("js.theme." + theme)) {
            resources.append("<script type=\"text/javascript\" src=\"")
                    .append(contextPath)
                    .append(p.getProperty("js.theme." + theme))
                    .append("\"></script>\n");
        }

        if(p.contains("css.theme." + theme)) {
            resources.append(" <link rel=\"stylesheet\" href=\"")
                    .append(contextPath)
                    .append(p.getProperty("css.theme." + theme))
                    .append("\" type=\"text/css\" />\n");
        }

        return resources.toString();
    }

    private String importJs(String contextPath, String path) {
        StringBuilder js = new StringBuilder();
        for (String jsPath : path.split(",")) {
            js.append("<script type=\"text/javascript\" src=\"").append(contextPath).append(jsPath).append("\"></script>\n");
        }
        return js.toString();
    }

    private String importCss(String contextPath, String path) {
        StringBuilder css = new StringBuilder();
        for (String cssPath : path.split(",")) {
            css.append(" <link rel=\"stylesheet\" href=\"").append(contextPath).append(cssPath).append("\" type=\"text/css\" />\n");
        }
        return css.toString();
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSrc(String src) {
        this.src = src;
    }
}
