package org.moon.rbac.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.base.domain.BaseDomain;
import org.moon.rbac.domain.repository.MenuEvent;
import org.moon.rbac.domain.repository.RoleEvent;
import org.moon.rbac.domain.repository.UserEvent;
import org.moon.utils.Constants;

import com.reeham.component.ddd.annotation.Model;

/**
 * the domain for role
 * @author Gavin
 * @version 1.0
 * @date 2012-11-27
 */
@Model
public class Role extends BaseDomain{

	private static final long serialVersionUID = 2223770816508175289L;
	
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
	
	/**
	 * @return 获取当前角色的顶级菜单
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> getTopMenus() {
		return (List<Menu>) menuLoader.getTopMenusByRole(this).getEventResult();
	}

	public User getCreator(){
		return (User) userLoader.getUser(getCreateBy()).getEventResult();
	}
	
	public void assign(User user){
		roleEvent.assign(this, user);
		user.setRole(this);
	}
	
	@SuppressWarnings("unchecked")
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
	 * 根据权限code判断是否具有某种权限
	 * @param code
	 * @return
	 */
    public boolean hasPermission(String code) {
        if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
            return true;
        }
        return (Boolean) roleEvent.hasPermission(this, code).getEventResult();
    }
	
	/**
	 * 根据菜单code判断是否具有某种菜单访问权限(主要处理系统级菜单)
	 * @param code
	 * @return
	 */
	public boolean accessMenu(String code){
        if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
            return true;
        }
		return (Boolean) roleEvent.accessMenu(this,code).getEventResult();
	}
	
	/**
	 * 获取当前角色的路径，返回顶层节点到改节点的角色id集合，如1,2,3
	 * @return
	 */
	public String getRolePath(){
        if (Constants.SYSTEM_ROLEID.equals(this.getId())) {
            return "";
        }
		StringBuilder path = new StringBuilder();
		List<Long> pathList = new ArrayList<Long>();
		Role temp = (Role) roleEvent.get(getParentId()).getEventResult();
		pathList.add(getId());
		while(temp!=null){
			pathList.add(temp.getId());
			temp = (Role) roleEvent.get(temp.getParentId()).getEventResult(); 
		}
		for(int i = pathList.size()-1;i>=0;i--){
			path.append(","+pathList.get(i));
		}
		return path.substring(1);
	}
	
	
	/*******************  setter/getter *******************/
	
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }
    /******************* /setter/getter *******************/
}
