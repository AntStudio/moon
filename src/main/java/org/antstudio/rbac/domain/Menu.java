package org.antstudio.rbac.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.antstudio.base.domain.BaseDomain;
import org.antstudio.rbac.domain.repository.MenuEvent;

import com.reeham.component.ddd.annotation.Model;

/**
 * the domain for menu 
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
@Model
public class Menu extends BaseDomain{

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 8687328492330629794L;

	/**
	 * the name for menu
	 */
	private String menuName;
	
	/**
	 * the url which menu linked
	 */
	private String url;
	
	/**
	 * the id for parent menu,if is the top menu,parentId is <code>null</code>
	 */
	private Long parentId;
	
	private String code;
	
	private String parentCode;
 
	private Long createBy;
	
	
	public Menu(){
		
	}
	
	public Menu(String menuName,String url,String code,String parentCode){
		this.menuName = menuName;
		this.url = url;
		this.code = code;
		this.parentCode = parentCode;
	}
	
	
	/**
	 * if <code>true</code> ,menu is leaf menu.else it has sub menus.default 'false'
	 */
	private boolean leaf = false;
 
	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}

	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the parentId
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * @param parentId the parentId to set
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	@Resource
	private MenuEvent menuEvent;
	/**
	 * @return the subMenu
	 */


	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the leaf
	 */
	public boolean isLeaf() {
		return leaf;
	}

	/**
	 * @return the parentCode
	 */
	public String getParentCode() {
		return parentCode;
	}

	/**
	 * @param parentCode the parentCode to set
	 */
	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	/**
	 * @param leaf the leaf to set
	 */
	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}
	
	public void saveOrUpdate(){
		menuEvent.saveOrUpdate(this);
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> menuMap = new HashMap<String,Object>();
		menuMap.put("id", getId());
		menuMap.put("menuName", this.menuName);
		menuMap.put("url", this.getUrl());
		menuMap.put("leaf", this.leaf);
		return menuMap;
	}

	/**
	 * @return the createBy
	 */
	public Long getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy to set
	 */
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	
	/**
	 * 是否是系统菜单
	 * @return
	 */
	public Boolean isSystemMenu(){
		return code!=null;
	}
	
	public Menu getParent(){
		if(parentId==null&&parentCode==null)//已经是顶级菜单
	   return null;
		else
			return((Menu) menuEvent.getParent(this).getEventResult());
			
	}
	
	 
}
