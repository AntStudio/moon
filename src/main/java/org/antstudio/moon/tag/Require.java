package org.antstudio.moon.tag;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.antstudio.moon.tag.util.RequestUtils;
import org.antstudio.utils.PropertiesUtils;
import org.apache.log4j.Logger;
/**
 * 
 * @author Gavin
 * @date 2013-8-6 上午12:27:21
 */
public class Require extends TagSupport{

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
	private String cssDefaultFolder,jsDefaultFolder;
	{
		try {
			p = PropertiesUtils.loadPropertiesFile("~system~requireTag.properties");
			p.putAll(PropertiesUtils.loadPropertiesFileIfExist("requireTag.properties"));
			cssDefaultFolder = p.getProperty("default.css.folder", "css/pages/");
			jsDefaultFolder = p.getProperty("default.js.folder", "js/pages/");
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
		StringBuilder sb =  new StringBuilder();
		String contextPath = RequestUtils.getContextPath(pageContext);
		if(type==null||"js".equals(type)){//for js
			for(String s:src.split(",")){
				if(pattern.matcher(s).matches()){//当时{Name}时,从默认路径获取
					s = s.replaceAll("[\\s\\{\\}]", "");
					sb.append(importCss(contextPath+cssDefaultFolder+s+".css"));
					sb.append(importJs(contextPath+jsDefaultFolder+s+".js"));
				}else if(p.containsKey("js."+s)){
					if(p.containsKey("js."+s)){
						sb.append(importJs(contextPath+p.getProperty("js."+s)));
					}
				}else{
					sb.append(importJs(contextPath+s));
				}
				if(type==null&&p.containsKey("css."+s)){
					sb.append(importCss(contextPath+p.getProperty("css."+s)));
				}
			}
		}else{//for css
			for(String s:src.split(",")){
				if(p.containsKey("css."+s)){
					sb.append(importCss(contextPath+p.getProperty("css."+s)));
				}else{
					sb.append(importCss(contextPath+s));
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
	
	private String importJs(String path){
		return "<script type=\"text/javascript\" src=\""+path+"\"></script>\n";
	}
	
	private String importCss(String path){
		return " <link rel=\"stylesheet\" href=\""+path+"\" type=\"text/css\" />\n";
	}
	
	public void setType(String type) {
		this.type = type;
	}
	public void setSrc(String src) {
		this.src = src;
	}
}
