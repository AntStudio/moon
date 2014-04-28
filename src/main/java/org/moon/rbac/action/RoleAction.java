package org.moon.rbac.action;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.moon.base.action.BaseAction;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.service.RoleService;
import org.moon.rbac.service.UserService;
import org.moon.support.spring.annotation.FormParam;
import org.moon.utils.MessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
@Controller
@RequestMapping("/role")
public class RoleAction extends BaseAction {

	@Resource
	private RoleService roleService;
	@Resource
	private UserService userService;

	@MenuMapping(url="/role",name="角色管理",code="platform_4",parentCode="platform")
	@RequestMapping("")
	public String showRole(){
		return "pages/rbac/role";
	}
	
	@RequestMapping("/getRoleData")
	@ResponseBody
	public List<Map<String,Object>> getRoleDate(@RequestParam("id")Long rid){
		if(rid==-1L){
			rid = null;
		}
		return roleService.getSubRolesForMap(rid, false);
	}
	
	@RequestMapping("/add")
	@ResponseBody
	public Map<String,Object> addRole(@FormParam("role") Role role,HttpServletRequest request){
		role.setCreateBy(userService.getCurrentUserId(request));
		if(role.getParentId()==null||role.getParentId()==-1L){
			role.setParentId(null);
		}
		enhance(role).save();
		return MessageUtils.getMapMessage(true);
	}
	
	@RequestMapping("/logicDelete")
    @ResponseBody
	public Map<String,Object> deleteRole(@RequestParam("ids")Long[] ids){
		roleService.delete(ids, true);
		return MessageUtils.getMapMessage(true);
	}
	
	 @RequestMapping("/update")
	 @ResponseBody
	 public Map<String,Object> updateRole(@FormParam("role") Role role){
	     Role roleCache = roleService.get(role.getId());
	     roleCache.setRoleName(role.getRoleName());
	     roleCache.update();
		 return MessageUtils.getMapMessage(true);
	 }
	 
	 @RequestMapping("/assignRoleToUser")
	 @ResponseBody
	 public Map<String,Object> assignRoleToUser(@RequestParam("rid")Long rid,@RequestParam("uid")Long uid){
		 roleService.get(rid).assign(userService.get(uid));
		 return MessageUtils.getMapMessage(true);
	 }
	 
	@RequestMapping("/getRoleDataByPermission")
	@ResponseBody
	public  List<Map<String,Object>> getRoleDataByPermission(@RequestParam("pid")Long pid,@RequestParam("rid")Long rid){
		 if(rid==-1){
			 rid = null;
		 }
		 return roleService.getAllRoleDataByPermission(pid, rid);
	}
}
