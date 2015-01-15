package org.moon.dictionary.domain;

import com.reeham.component.ddd.annotation.Model;
import org.moon.base.domain.BaseDomain;

import javax.persistence.Table;

/**
 * 
 * @author Gavin
 * @date 2014-05-08
 */
@Model
@Table(name="tab_dictionary")
public class Dictionary extends BaseDomain{

	private static final long serialVersionUID = -1227905639614891514L;
	
	private String code;
	
	private String name;
	
	private Long parentId;

    private boolean isFinal;//是否是不允许修改的

	/******************** getter/setter ********************/
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

    public boolean isIfFinal() {
        return isFinal;
    }

    public void setIfFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    /******************** /getter/setter ********************/
	
}
