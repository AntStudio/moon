package org.moon.rbac.domain;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.base.domain.BaseDomain;
import org.moon.rbac.domain.repository.MenuEvent;

import com.reeham.component.ddd.annotation.Model;

/**
 * 菜单领域
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 
 */
@Model
public class Menu extends BaseDomain{

	private static final long serialVersionUID = 8687328492330629794L;

	private String menuName;
	
	private String url;
	
	private Long parentId;
	
	private String code;
	
	private String parentCode;
 
    private Long   createBy;
    
    private int   menuOrder;

    /**
     * 是否是叶子菜单，即是否有子菜单
     */
    private boolean leaf  = false;
	
	@Resource
    private MenuEvent menuEvent;
	public Menu(){
		
	}
	
	public Menu(String menuName,String url,String code,String parentCode){
		this.menuName = menuName;
		this.url = url;
		this.code = code;
		this.parentCode = parentCode;
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
	 * 是否是系统菜单
	 * @return
	 */
	public Boolean isSystemMenu(){
		return code!=null;
	}
	
    public Menu getParent() {
        if (parentId == null && parentCode == null) {// 已经是顶级菜单
            return null;
        } else {
            return ((Menu) menuEvent.getParent(this).getEventResult());
        }

    }
	
	/********************  getter/setter *******************/
    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    
    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public String getParentCode() {
        return parentCode;
    }
    
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }
    
    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

	public int getMenuOrder() {
		return menuOrder;
	}

	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
    
    /******************** /getter/setter *******************/
	 
}
