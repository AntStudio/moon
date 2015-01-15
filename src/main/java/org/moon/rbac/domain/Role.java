package org.moon.rbac.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reeham.component.ddd.annotation.Model;
import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.domain.BaseDomain;
import org.moon.rbac.domain.eventsender.RoleEventSender;
import org.moon.utils.Constants;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Model
public class Role extends BaseDomain {
	private static final long serialVersionUID = 2223770816508175289L;

	public Role(Long id) {
		this.id = id;
	}

	public Role() {}

	private String roleName;

	private Long createBy;

	private Long parentId;

	@Resource
	private RoleEventSender roleEventSender;

	/**
	 * @return 获取当前角色的顶级菜单
	 */
	@JsonIgnore
	public List<Menu> getTopMenus() {
		return (List<Menu>) roleEventSender.getTopMenusForRole(this).getEventResult();
	}

	@JsonIgnore
	public User getCreator() {
		return (User) roleEventSender.getUser(getCreateBy()).getEventResult();
	}

	@JsonIgnore
	public DomainMessage assign(User user) {
		DomainMessage message = roleEventSender.assignRoleToUser(this, user);
		user.setRole(this);
		return message;
	}

	@JsonIgnore
	public List<Role> getSubRoles() {
		return (List<Role>) roleEventSender.getSubRoles(this).getEventResult();
	}
	@JsonIgnore
	public Map<String, Object> toMap() {
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("id", id);
		m.put("roleName", roleName);
		return m;
	}

	/**
	 * 根据权限code判断是否具有某种权限
	 * 
	 * @param code
	 * @return
	 */
	@JsonIgnore
	public boolean hasPermission(String code) {
		if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
			return true;
		}
		return (Boolean) roleEventSender.hasPermission(this, code).getEventResult();
	}

	/**
	 * 根据菜单code判断是否具有某种菜单访问权限(主要处理系统级菜单)
	 * 
	 * @param code
	 * @return
	 */
	@JsonIgnore
	public boolean hasMenu(String code) {
		if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
			return true;
		}
		return (Boolean) roleEventSender.hasMenu(this, code).getEventResult();
	}

	@JsonIgnore
	public List<Permission> getPermission(){
		return (List<Permission>)roleEventSender.getPermissionForRole(this).getEventResult();
	}
	
	/**
	 * 获取当前角色的路径，返回顶层节点到改节点的角色id集合，如1,2,3
	 * 
	 * @return
	 */
	public String getRolePath() {
		if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
			return "";
		}
		StringBuilder path = new StringBuilder();
		List<Long> pathList = new ArrayList<Long>();
		Role temp = this;
		pathList.add(getId());
		while (temp.getParentId() != null) {
			temp = (Role) roleEventSender.getParentRole(temp).getEventResult();
			pathList.add(temp.getId());
		}
		for (int i = pathList.size() - 1; i >= 0; i--) {
			path.append("," + pathList.get(i));
		}
		return path.substring(1);
	}

	/******************* setter/getter *******************/

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}
	/******************* /setter/getter *******************/
}
