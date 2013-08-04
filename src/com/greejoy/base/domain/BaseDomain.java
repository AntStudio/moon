package com.greejoy.base.domain;

import java.io.Serializable;

import com.reeham.component.ddd.annotation.Model;

/**
 * the basic domain for domains
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public class BaseDomain implements Serializable{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 3121315303319780752L;


	/**
	 * the id for domain
	 */
	protected Long id;
	
	/**
	 * if <code>true</code>,means deleted
	 */
	protected boolean deleteFlag;
	
	/**
	 * the number for different domain
	 */
	protected Long domainNo;

	public BaseDomain(){
		
	}
	public BaseDomain(Long id){
		this.id = id;
	}
	
	
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the deleteFlag
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
	
	public boolean getDeleteFlag() {
		return deleteFlag;
	}

	

	/**
	 * @return the domainNo
	 */
	public Long getDomainNo() {
		return domainNo;
	}

	/**
	 * @param domainNo the domainNo to set
	 */
	public void setDomainNo(Long domainNo) {
		this.domainNo = domainNo;
	}
	
	
	
}
