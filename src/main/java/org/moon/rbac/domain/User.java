package org.moon.rbac.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.reeham.component.ddd.annotation.Model;
import org.moon.base.domain.BaseDomain;
import org.moon.core.orm.mybatis.annotation.IgnoreNull;
import org.moon.rbac.domain.eventsender.UserEventSender;
import org.moon.utils.Constants;
import org.moon.utils.MD5;
import org.moon.utils.Objects;

import javax.annotation.Resource;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * the domain for user
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27 9:00:43
 */
@Model
@Table(name="tab_user")
public class User extends BaseDomain{

	private static final long serialVersionUID = -7660365552913856672L;

	@Transient
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
	@JsonIgnore
	@IgnoreNull
	private transient String password;
	
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
	private Date createTime = new Date();
	
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

	@JsonInclude(JsonInclude.Include.NON_NULL)
    private int type;

    private String idNumber;

    private String phoneNumber;

    private String avatar;

    private Date birthday;

    private String description;

    //三方应用需要使用的名字
    private String thirdName;

	private String email;

	private boolean isTester;
    /**
	 * 密码是否加密
	 */
	@Transient
	private boolean isEncrypt = false;
	
	@Resource
	private transient UserEventSender userEventSender;

	@JsonIgnore
	public Role getRole() {
		if(Constants.SYSTEM_ROLEID.equals(roleId)){
			return new Role(roleId);
		}
		if(roleId==null||roleId<=0){
			return null;
		}
        return (Role) userEventSender.getRole(this).getEventResult();
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", getId());
		m.put("userName", getUserName());
		if(roleId==null||getRole()==null){
			m.put("roleName", "还未分配角色");
		}else{
			m.put("roleName", getRole().getRoleName());
		}
		return m;
		
	}
	
	public Map<String,Object> toAllMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("id", getId());
		m.put("userName", getUserName());
		if(roleId==null||getRole()==null){
			m.put("roleName", "还未分配角色");
		}else{
			m.put("roleName", getRole().getRoleName());
		}
		m.put("realName", realName);
        m.put("idNumber",idNumber);
        m.put("type",type);
        m.put("phoneNumber",phoneNumber);
        m.put("avatar",avatar);
        m.put("sex",sex);
        m.put("thirdName",thirdName);
		m.put("email",email);
		if(Objects.nonNull(birthday)){
			m.put("birthday",sdf.format(birthday));
		}
        m.put("description",description);
		return m;
	}

    /**
     * 返回不包括隐私信息的数据
     * @return
     */
    public Map<String,Object> toMapWithoutPrivacy(){
        Map<String,Object> m = toAllMap();
        m.remove("phoneNumber");
        m.remove("userName");
        m.remove("idNumber");
        return m;
    }

	public boolean isSysUser(){
		return Constants.SYSTEM_USERID.equals(id);
	}
	
	public User encryptPassword(){
	    if(this.password!=null&&!isEncrypt){//密码不为空,并且未加密
	        isEncrypt = true;
	        this.password = MD5.getCryptographicPassword(password);
	    }
	    return this;
	}
	
	/********************  setter/getter  ********************/
	
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
	@JsonIgnore
	public String getPassword() {
		if(Constants.SYSTEM_USERID.equals(id))
			return new String(Constants.getSystemUserPassword());
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isTester() {
		return isTester;
	}

	public void setIsTester(boolean isTester) {
		this.isTester = isTester;
	}

	/******************** /setter/getter  ********************/
}
