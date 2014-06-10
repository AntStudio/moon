package org.moon.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.moon.core.spring.ApplicationContextHelper;
import org.moon.dictionary.domain.Dictionary;
import org.moon.dictionary.repository.DictionaryRepository;
import org.moon.dictionary.service.DictionaryService;

/**
 * 字典下拉框(tag-name-->dicSelect)
 * @author Gavin
 * @date May 30, 2014
 */
public class DictionarySelect extends TagSupport{

	private static final long serialVersionUID = -1L;
	
	private DictionaryService dictionaryService = ApplicationContextHelper.getBean(DictionaryService.class);
	
	//字典代码
	private String code;

	private String css;
	
	@Override
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer("<select ");
		if(css!=null){
			sb.append(" class=\"").append(css).append("\"");
		}
		sb.append(" >");
		
		for(Map m:getDicForCode()){
			sb.append("<option value=\"").append(m.get("code")).append("\">").append(m.get("name"))
			  .append("</option>");
		}
		sb.append("<select>");
		try {
			pageContext.getOut().write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return EVAL_PAGE;
	}
	
	private List<Map> getDicForCode(){
		return dictionaryService.list();
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setCss(String css) {
		this.css = css;
	}
}
