package org.moon.tag;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.dictionary.service.DictionaryService;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //控件名称(用于表单提交的名字)
    private String name;

	private String css;

    //默认选择项的值
    private String defaultVal;

    //默认选择项的显示值,当有defaultVal时,优先使用defaultVal
    private String defaultText;

    //额外的下拉数据(可以自定义添加下拉项)格式为：[{"text":"xxx","value":"xxx"},...]
    private String extraData;

    private boolean append = true;//额外下拉数据是否追加到后面,如果为false则添加到下拉项的最前面

	@Override
	public int doEndTag() throws JspException {
		StringBuffer sb = new StringBuffer("<select ");
		if(css != null){
			sb.append(" class=\"").append(css).append("\"");
		}
        if(name != null){
			sb.append(" name=\"").append(name).append("\"");
		}
		sb.append(" >");

        boolean useVal = defaultVal != null;//使用值进行默认选择
        boolean useText = !useVal && defaultText != null; //使用显示值进行默认选择

        List<Map<String,Object>> list = getDicForCode();
        List<Map<String,Object>> extraDataList = renderExtraData();
        if(extraDataList != null) {
            if(append) {
                list.addAll(extraDataList);
            }else{
                list.addAll(0,extraDataList);
            }
        }
		for(Map m:list){
            sb.append("<option value=\"").append(m.get("code")).append("\" ")
              .append(useVal?(defaultVal.equals(m.get("code"))?"selected":""):(useText?(defaultText.equals(m.get("name"))?"selected":""):""))
              .append(" >").append(m.get("name"))
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
	
	private List<Map<String,Object>> getDicForCode(){
		return dictionaryService.listChildrenByCode(code);
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public void setCss(String css) {
		this.css = css;
	}

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultVal(String defaultVal) {
        this.defaultVal = defaultVal;
    }

    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    private List<Map<String,Object>> renderExtraData() {
        if(extraData != null) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Map<String,Object>> list = objectMapper.readValue(extraData, ArrayList.class);
                for(Map m:list){
                    m.put("code",m.get("value"));
                    m.put("name",m.get("text"));
                }
                return list;
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
