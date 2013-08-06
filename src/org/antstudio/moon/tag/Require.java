package org.antstudio.moon.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.antstudio.moon.tag.util.RequestUtils;
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

	private String type;
	private String src;
	@Override
	public int doEndTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		StringBuilder sb =  new StringBuilder();
		String contextPath = RequestUtils.getContextPath(pageContext);
		if(type==null||"js".equals(type)){//for js
			for(String s:src.split(",")){
				if("jquery".equals(s)){
					sb.append("<script type=\"text/javascript\" src=\""+contextPath+"plugin/jquery/jquery-1.8.3.js\"></script>\n");
				}else if("bootstrap".equals(s)){
					sb.append("<script type=\"text/javascript\" src=\""+contextPath+"plugin/bootstrap/js/bootstrap.min.js\"></script>\n");
				}else if("ev".equals(s)||"easyValidation".equals(s)){//ev==easyValidation
					sb.append("<script type=\"text/javascript\" src=\""+contextPath+"plugin/easyValidation/easyValidation-1.0.js\"></script>\n");
					sb.append(" <link rel=\"stylesheet\" href=\""+contextPath+"plugin/easyValidation/css/tooltip.css\" type=\"text/css\" />\n");
				}else{
					sb.append("<script type=\"text/javascript\" src=\""+contextPath+s+"\"/>");
				}
			}
		}else{//for css
			for(String s:src.split(",")){
				if("bootstrap".equals(s)){
					sb.append(" <link rel=\"stylesheet\" href=\""+contextPath+"plugin/bootstrap/css/bootstrap.min.css\" type=\"text/css\" />\n");
				}else{
					sb.append(" <link rel=\"stylesheet\" href=\""+contextPath+s+"\" type=\"text/css\" />\n");
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
	
	
	public void setType(String type) {
		this.type = type;
	}
	public void setSrc(String src) {
		this.src = src;
	}
}
