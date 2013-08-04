package com.greejoy.rbac.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.greejoy.base.domain.BaseDomain;
import com.greejoy.rbac.domain.repository.MenuEvent;
import com.greejoy.rbac.domain.repository.RoleEvent;
import com.greejoy.rbac.domain.repository.UserEvent;
import com.greejoy.rbac.service.RoleService;
import com.greejoy.utils.Constants;
import com.reeham.component.ddd.annotation.Model;

/**
 * the domain for role
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Model
public class Role extends BaseDomain{

	public Role(Long id){
		this.id = id;
	}
	public Role(){
		
	}
	/**
	 * the name for every role
	 */
	private String roleName;
	
	/**
	 * if the role active or forbid
	 */
	private boolean active;
	
	/**
	 * the id for user which created the role
	 */
	private Long createBy;
	
	private Long parentId;
	
	@Resource
	private MenuEvent menuLoader;
	
	@Resource
	private UserEvent userLoader;
	
	@Resource
	private RoleEvent roleEvent;
	
	@Resource
	private RoleService roleService;
	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
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
	 * @return the top menus for role
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getTopMenus() {
		return (List<Menu>) menuLoader.getTopMenusByRole(this).getEventResult();
	}

 
	/**
	 * get the creator of the role
	 * @return
	 */
	public User getCreator(){
		return (User) userLoader.getUser(getCreateBy()).getEventResult();
	}
	
	
	public void assign(User user){
		roleEvent.assign(this, user);
		user.setRole(this);
	}
	
	public List<Role> getSubRoles(){
		return (List<Role>) roleEvent.getSubRoles(this);
	}
	
	
	public Map<String,Object> toMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", id);
		m.put("roleName", roleName);
		return m;
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
	
	public void save(){
		roleEvent.save(this);
	}
	
	public void update(){
		roleEvent.update(this);
	}
	
	/**
	 * 根据权限code判断是否具有某种权限
	 * @param code
	 * @return
	 */
	public boolean hasPermission(String code){
		if(Constants.SYSTEM_ROLEID.equals(this.getId()))
			return true;
		return (Boolean) roleEvent.hasPermission(this,code).getEventResult();
	}
	
	/**
	 * 根据菜单code判断是否具有某种菜单访问权限(主要处理系统级菜单)
	 * @param code
	 * @return
	 */
	public boolean accessMenu(String code){
		if(Constants.SYSTEM_ROLEID.equals(this.getId()))
			return true;
		return (Boolean) roleEvent.accessMenu(this,code).getEventResult();
	}
	
	/**
	 * 获取当前角色的路径，返回顶层节点到改节点的角色id集合，如1,2,3
	 * @return
	 */
	public String getRolePath(){
		if(Constants.SYSTEM_ROLEID.equals(this.getId()))
			return "";
		StringBuilder path = new StringBuilder();
		//StringBuilder path = new StringBuilder();
		List<Long> pathList = new ArrayList<Long>();
		Role temp = roleService.getModel(getParentId());
		pathList.add(getId());
		while(temp!=null){
			//path.append(","+temp.getId());
			pathList.add(temp.getId());
			temp = roleService.getModel(temp.getParentId());
		}
		for(int i = pathList.size()-1;i>=0;i--){
			path.append(","+pathList.get(i));
		}
		return path.substring(1);
	}
}
