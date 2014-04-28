package org.moon.tag;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.moon.utils.Constants;

/**
 * 系统用户密码
 * @author Gavin
 * @date 2014-2-23 上午11:56:48
 */
public class SystemUserPassword extends TagSupport{

	private static final long serialVersionUID = 1L;

	@Override
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public int doEndTag(){
		JspWriter out = this.pageContext.getOut();
		try{
			out.print(Constants.SYSTEM_PASSWORD);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
}
 