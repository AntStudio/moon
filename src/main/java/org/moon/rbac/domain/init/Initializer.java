package org.moon.rbac.domain.init;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.Permission;
import org.moon.rbac.domain.init.helper.MenuMappingHelper;
import org.moon.rbac.domain.init.helper.PermissionMappingHelper;
import org.moon.rbac.service.MenuService;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.Constants;
import org.moon.utils.ParamUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;


public class Initializer implements ApplicationListener<ContextRefreshedEvent> {
	
	private Logger log = Logger.getLogger(getClass());
	@Resource
	private MenuService menuService;

	@Resource
	private PermissionService permissionService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event instanceof ContextRefreshedEvent) {
			log.info("[spring-rbac]Starting init menu");
			Map<String, Menu> systemMenus = menuService.getSystemMenusByCode();
			if (systemMenus.keySet().size() == 0) {
				menuService.addMenus(MenuMappingHelper.getMappingMenus());
			} else {
				List<Menu> addMenus = new ArrayList<Menu>(), deleteMenus = new ArrayList<Menu>(), updateMenus = new ArrayList<Menu>();
				Map<String, Menu> mappingMenu = MenuMappingHelper
						.getMenusMapByCode();
				for (String code : mappingMenu.keySet()) {

					if (systemMenus.containsKey(code)) {
						updateMenus.add(getUpdateMenu(mappingMenu.get(code),
								systemMenus.get(code)));
						systemMenus.remove(code);
					} else
						addMenus.add(mappingMenu.get(code));
				}
				for (Menu m : systemMenus.values()) {
					deleteMenus.add(m);
				}

				if (addMenus.size() != 0)
					menuService.addMenus(addMenus);
				if (deleteMenus.size() != 0)
					menuService.deleteMenus(deleteMenus);
				for (Menu m : updateMenus) {
					m.update();
				}
			}

			
			log.info("[spring-rbac]Starting init the permissions");
			Map<String,Object> paramsMap = ParamUtils.getDefaultParamMap();
			paramsMap.put("ps", 10000);
			Map<String, Permission> permissions = permissionService
					.getPermissionsByCode(paramsMap);
			if (permissions.keySet().size() == 0) {
				permissionService.batchSave(PermissionMappingHelper
						.getMappedPermissions());
			} else {
				List<Permission> addPermissions = new ArrayList<Permission>(), deletePermissions = new ArrayList<Permission>(), updatePermissions = new ArrayList<Permission>();
				Map<String, Permission> mappedPermissions = PermissionMappingHelper
						.getPermissionsMapByCode();
				for (String code : mappedPermissions.keySet()) {
					if (permissions.containsKey(code)) {
						updatePermissions
								.add(getUpdatePermission(
										mappedPermissions.get(code),
										permissions.get(code)));
						permissions.remove(code);
					} else
						addPermissions.add(mappedPermissions.get(code));
				}
				for (Permission p : permissions.values()) {
					deletePermissions.add(p);
				}
				if (addPermissions.size() != 0)
					permissionService.batchSave(addPermissions);
				if (deletePermissions.size() != 0)
					permissionService.delete(deletePermissions);
				for (Permission p : updatePermissions) {
					p.saveOrUpdate();
				}
			}
			
			
		}

		

	}

	private Menu getUpdateMenu(Menu mappingMenu, Menu systemMenu) {
		systemMenu
				.setParentCode((mappingMenu.getParentCode() == null || mappingMenu
						.getParentCode().equals("")) ? null : mappingMenu
						.getParentCode());
		systemMenu.setMenuName(mappingMenu.getMenuName());
		systemMenu.setUrl(mappingMenu.getUrl());
		systemMenu.setCreateBy(Constants.SYSTEM_USERID);
		systemMenu.setDeleteFlag(false);
		return systemMenu;
	}

	private Permission getUpdatePermission(Permission mappingPermission,
			Permission permission) {

		permission.setName(mappingPermission.getName());
		permission.setCode(mappingPermission.getCode());
		return permission;
	}

}
