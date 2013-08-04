package com.greejoy.rbac.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.greejoy.base.domain.BaseDomain;
import com.greejoy.rbac.domain.repository.RoleEvent;
import com.greejoy.rbac.domain.repository.UserEvent;
import com.greejoy.utils.Constants;
import com.reeham.component.ddd.annotation.Model;



/**
 * the domain for user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 9:00:43
 */
@Model
public class User extends BaseDomain{

	public User(){
		
	}
	
	/**
	 * the Identifier for  user in current session 
	 */
	public static String CURRENT_USER_ID = "CURRENT_USER_ID";
	/**
	 * the login name for user
	 */
	private String userName;
	
	/**
	 * the real name for user
	 */
	private String realName;
	
	/**
	 * the login password for user
	 */
	private String password;
	
	/**
	 * the  id of role which distributed to user
	 */
	private Long roleId;
	
	/**
	 * if the user active or forbid,default forbid
	 */
	private boolean active = false;
	
	/**
	 * the time when user created
	 */
	private Date createTime;
	
	/**
	 * the time when the user last login
	 */
	private Date lastLoginTime;
	
	private Long createBy;
	
	/**
	 * the sex of user,'0' is for male,'1' is for female.default '0'
	 */
	private int sex = 0;
	
	/**
	 * the way of contact user,such as:email,mobile,telephone,or address
	 */
	private String contact;
	
	/**
	 * the menus for user across the it's role .there only contains top menus for user.
	 */
	private List<Menu> menus;

	@Resource
	private RoleEvent roleEvent;
	
	@Resource
	private UserEvent userEvent;
	
	
	public Role getRole() {
		if(Constants.SYSTEM_ROLEID.equals(roleId))
			return new Role(roleId);
		if(roleId==null||roleId<=0)
			return null;
		return (Role) roleEvent.getRole(this).getEventResult();
	}
	
	
 public void addUser(){
	 userEvent.addUser(this);
 }
	
	
	
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the realName
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * @param realName the realName to set
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		if(Constants.SYSTEM_USERID.equals(id))
			return Constants.SYSTEM_PASSWORD;
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
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
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the lastLoginTime
	 */
	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	/**
	 * @param lastLoginTime the lastLoginTime to set
	 */
	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	/**
	 * @return the sex
	 */
	public int getSex() {
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public void setSex(int sex) {
		this.sex = sex;
	}

	/**
	 * @return the contact
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * @return the roleId
	 */
	public Long getRoleId() {
		return roleId;
	}

	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	 public void setRole(Role role){
		 this.roleId = role.getId();
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






	public Map<String,Object> toMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", getId());
		m.put("user_name", getUserName());
		if(roleId==null||getRole()==null)
			m.put("role_name", "还未分配角色");
		else
		m.put("role_name", getRole().getRoleName());
		return m;
		
	}
	
	public Map<String,Object> toAllMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", getId());
		m.put("userName", getUserName());
		if(roleId==null||getRole()==null)
			m.put("roleName", "还未分配角色");
		else
		m.put("roleName", getRole().getRoleName());
		m.put("password", password);
		m.put("realName", realName);
		return m;
	}
	
	
	public void updateUser(){
		if(!isSystemUser())
		userEvent.update(this);
	}
	
	public boolean isSystemUser(){
		return getId().equals(Constants.SYSTEM_USERID);
	}
}
