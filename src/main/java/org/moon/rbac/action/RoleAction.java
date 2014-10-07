package org.moon.rbac.action;

import org.moon.base.action.BaseAction;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rbac.service.PermissionService;
import org.moon.rbac.service.RoleService;
import org.moon.rbac.service.UserService;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.core.spring.annotation.FormParam;
import org.moon.utils.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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

	@Resource
	private PermissionService permissionService;
	
	@Get("")
	@MenuMapping(url = "/role", name = "角色管理", code = "platform_4", parentCode = "platform")
	public String showRolePage() {
		return "pages/rbac/role";
	}

	@Get("/getSubRoles")
	public @ResponseBody
	WebResponse getSubRoles(@RequestParam("id") Long rid) {
		if (rid == -1L) {
			rid = null;
			return WebResponse.build().setResult(roleService.getTopRoles());
		}
		Role role = roleService.get(rid);
		return WebResponse.build().setResult(role.getSubRoles());
	}

	@Post("/add")
	public @ResponseBody
	WebResponse add(@WebUser User user,@FormParam("role") Role role, HttpServletRequest request) {
		role.setCreateBy(user.getId());
		if (Objects.isNull(role.getParentId()) || role.getParentId() == -1L) {
			role.setParentId(null);
		}
		role.sync(role.save());
		return WebResponse.build().setSuccess(true);
	}

	@Post("/logicDelete")
	public @ResponseBody
	WebResponse logicDelete(@RequestParam("ids") Long[] ids) {
		roleService.delete(ids, true);
		return WebResponse.build().setSuccess(true);
	}

	@Post("/update")
	public @ResponseBody
	WebResponse update(@FormParam("role") Role role) {
		Role roleCache = roleService.get(role.getId());
		roleCache.setRoleName(role.getRoleName());
		roleCache.sync(roleCache.update());
		return WebResponse.build();
	}

	@Post("/assignRoleToUser")
	public @ResponseBody
	WebResponse assignRoleToUser(@RequestParam("rid") Long rid, @RequestParam("uid") Long uid) {
		sync(roleService.get(rid).assign(userService.get(uid)));
		return WebResponse.build();
	}

	@Get("/getRoleDataByPermission")
	public @ResponseBody
	WebResponse getRoleDataByPermission(@RequestParam("pid") Long pid, @RequestParam("rid") Long rid) {
		if (rid == -1L) {
			rid = null;
		}
		Permission permission = permissionService.get(pid);
		return WebResponse.build().setResult(roleService.getRolesWithStatusForPermission(permission, rid));
	}
}
