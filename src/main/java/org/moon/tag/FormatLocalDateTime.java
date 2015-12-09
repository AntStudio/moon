package org.moon.tag;

import org.moon.utils.Constants;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 格式化LocalDateTime
 * @author GavinCook
 * @since 1.0.0
 */
public class FormatLocalDateTime extends TagSupport{

	private static final long serialVersionUID = 1L;

	private LocalDateTime value;

	private String pattern = "yyyy-MM-dd";

	public LocalDateTime getValue() {
		return value;
	}

	public void setValue(LocalDateTime value) {
		this.value = value;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public int doStartTag() throws JspException {
		return EVAL_PAGE;
	}
	
	@Override
	public int doEndTag(){
		JspWriter out = this.pageContext.getOut();
		try{
			out.print(value.format(DateTimeFormatter.ofPattern(pattern)));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
}
 