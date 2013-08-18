package org.antstudio.rbac.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.antstudio.rbac.domain.Role;
import org.antstudio.rbac.domain.User;
import org.antstudio.rbac.domain.annotation.MenuMapping;
import org.antstudio.rbac.service.RoleService;
import org.antstudio.rbac.service.UserService;
import org.antstudio.support.spring.annotation.FormParam;
import org.antstudio.utils.MessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.reeham.component.ddd.model.ModelContainer;

/**
 * the action controller of role
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
@Controller
@RequestMapping("/role")
public class RoleAction {

	@Resource
	private RoleService roleService;
	@Resource
	private UserService userService;

	@MenuMapping(url="/role",name="角色管理",code="100004",parentCode="100000")
	@RequestMapping("")
	public String showRole(){
		
		return "pages/rbac/role";
	}
	
	@RequestMapping("/getRoleData")
	@ResponseBody
	public List<Map<String,Object>> getRoleDate(@RequestParam("id")Long rid){
		if(rid==-1L)
			rid = null;
		return roleService.getSubRolesForMap(rid, false);
	}
	
	@RequestMapping("/addRole")
	@ResponseBody
	public Map<String,Object> addRole(@FormParam("role") Role role,HttpServletRequest request){
		role.setCreateBy(userService.getCurrentUserId(request));
		if(role.getParentId()==null||role.getParentId()==-1L)
			role.setParentId(null);
		roleService.addRole(role);
		return MessageUtils.getMapMessage(true);
	}
	
	@RequestMapping("/logicDeleteRole")
    @ResponseBody
	public Map<String,Object> deleteRole(@RequestParam("ids")Long[] ids){
		roleService.delete(ids, true);
		return MessageUtils.getMapMessage(true);
	}
	
	 @RequestMapping("/updateRole")
	 @ResponseBody
	 public Map<String,Object> updateRole(@FormParam("role") Role role){
		 roleService.update(role);
		 return MessageUtils.getMapMessage(true);
		 
	 }
	 
	 @RequestMapping("/assignRoleToUser")
	 @ResponseBody
	 public Map<String,Object> assignRoleToUser(@RequestParam("rid")Long rid,@RequestParam("uid")Long uid){
		 roleService.getModel(rid).assign(userService.getModel(uid));
		 return MessageUtils.getMapMessage(true);
	 }
	 
	 @RequestMapping("/getRoleDataByPermission")
	 @ResponseBody
	public  List<Map<String,Object>> getRoleDataByPermission(@RequestParam("pid")Long pid,@RequestParam("rid")Long rid){
		 if(rid==-1)
			 rid = null;
		 return roleService.getAllRoleDataByPermission(pid, rid);
	}
}
