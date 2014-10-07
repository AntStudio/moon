package org.moon.rbac.action;

import org.moon.base.action.BaseAction;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rbac.service.MenuService;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.core.spring.annotation.FormParam;
import org.moon.utils.ClassPropertiesUtil;
import org.moon.utils.Objects;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;

/**
 * 菜单控制器
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-4
 */
@Controller
@RequestMapping("/menu")
public class MenuAction extends BaseAction {

	@Resource
	private MenuService menuService;

    /**
     * 跳转菜单管理界面
     * @return
     */
    @MenuMapping(url = "/menu", name = "菜单管理", code = "platform_2", parentCode = "platform")
    @Get("")
    public ModelAndView showMenuPage() {
        return new ModelAndView("/pages/rbac/menu");
    }

	/**
	 * 获取子菜单(默认获取deleteFlag=false的菜单),用于前台菜单显示
	 * 
	 * @param pid
	 *            父菜单id
	 * @return
	 */
	@Get("/getSubMenus")
	public @ResponseBody WebResponse getSubMenus(@WebUser User user, @RequestParam("pid") Long pid) {
        if (Objects.isNull(pid) || -1L == pid) {
			pid = null;
		}
		return WebResponse.build().setResult(menuService.getSubMenusForRole(pid, user.getRoleId()));
	}

	/**
	 * 根据角色获取菜单数据,返回一个map的 list,当该菜单分配给了这个角色，那么map.checked为true,否则为false,用于菜单管理
	 * 
	 * @param parentMenuId
	 *            父菜单id
	 * @param rid
	 *            角色id
	 * @return
	 */
	@Get("/getAssignMenuData")
	@ResponseBody
	public WebResponse getAssignMenuData(@RequestParam("pid") Long parentMenuId, @RequestParam("rid") Long rid) {
        if(Objects.isNull(parentMenuId)||parentMenuId<1){
            parentMenuId = null;
        }
		return WebResponse.build().setResult(menuService.getMenusWithStatus(parentMenuId, rid));
	}

	/**
	 * 分配菜单给角色,将分配状态改变了的菜单id(即参数ids)传回,chechStatus记录每个菜单是否勾选(是否要分配).
	 * 这里传回的id全部是需要修改的,这里是由前台ztree控件保证的.
	 * 
	 * @param menuIds
	 * @param rid
	 * @param checkStatus
	 * @return
	 */
	@Post("/assignMenu")
	@PermissionMapping(code = "000002", name = "分配菜单")
	@ResponseBody
	public WebResponse assignMenu(@RequestParam("ids") Long[] menuIds, @RequestParam("rid") Long rid,
			@RequestParam("checkStatus") Boolean[] checkStatus) {
		menuService.assignMenuToRole(menuIds, checkStatus, rid);
		return WebResponse.build();
	}

	@Post("/add")
	@ResponseBody
	@PermissionMapping(code = "000001", name = "添加菜单信息")
	public WebResponse add(@FormParam("menu") Menu menu) {
		if(menu.getParentId()==-1){
			menu.setParentId(null);
		}
		sync(menu.save());
		return WebResponse.build();
	}

	@Post("/update")
	@ResponseBody
	public WebResponse update(@FormParam("menu") Menu menu) {
		if (menu.getId() != null) {
			Menu oldMenu = menuService.get(menu.getId());
			menu = (Menu) ClassPropertiesUtil.copyProperties(menu, oldMenu, true, "menuName", "url");
            sync(menu.update());
		}
		return WebResponse.build();
	}

	@Post("/delete")
	@ResponseBody
	public WebResponse deleteMenu(@FormParam("menu") Menu menu) {
		if (menu.getId() != null) {
			sync(menu.delete());
		}
		return WebResponse.build();
	}

    /**
     * 菜单排序
     * @param parentId
     * @param childrenIds
     * @return
     */
	@Post("/sort")
	@ResponseBody
	public WebResponse sortMenu(@RequestParam("parentMenuId") Long parentId,
			@RequestParam("childrenMenuIds[]") Long[] childrenIds) {
		menuService.sortMenus(parentId, childrenIds);
		return WebResponse.build();
	}
}
