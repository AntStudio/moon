package org.moon.dictionary.domain;

import javax.persistence.Table;

import org.moon.base.domain.BaseDomain;

import com.reeham.component.ddd.annotation.Model;

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
	
	private int parentId;

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

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	/******************** /getter/setter ********************/
	
}
