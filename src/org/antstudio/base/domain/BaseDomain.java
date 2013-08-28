package org.antstudio.base.domain;

import java.io.Serializable;

/**
 * 域模型的基本对象,包含了一些公共基本属性,如:
 * <pre>id</pre>
 * <pre>deleteFlag(删除标志,用于逻辑删除)</pre>
 * <pre>domainNo(域编号,可用于处理多个域的数据)</pre>
 * 一般的域模型都会继承于 {@link BaseDomain}
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
public class BaseDomain implements Serializable{

	/**
	 * 版本序列标示
	 */
	private static final long serialVersionUID = 3121315303319780752L;

	/**
	 * 域模型对象的id
	 */
	protected Long id;
	
	/**
	 * 如果 <code>deleteFlag 为 true;</code>,则表示删除
	 */
	protected boolean deleteFlag;
	
	/**
	 * 域编号,主要用于多个用户或其他原因引起的数据分块，则此字段可用于标示每个不同数据块
	 */
	protected Long domainNo;

	public BaseDomain(){}
	
	public BaseDomain(Long id){
		this.id = id;
	}
	
	/**
	 * @return 返回id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param 设置id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return 返回删除标示
	 */
	public boolean isDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param 设置删除标示
	 */
	public void setDeleteFlag(boolean deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	/**
	 * @return 返回域编号
	 */
	public Long getDomainNo() {
		return domainNo;
	}

	/**
	 * @param 设置域编号
	 */
	public void setDomainNo(Long domainNo) {
		this.domainNo = domainNo;
	}
	
}
