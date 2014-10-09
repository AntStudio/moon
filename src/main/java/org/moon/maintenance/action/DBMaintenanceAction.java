package org.moon.maintenance.action;

import org.moon.db.manager.DBManager;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.MessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 数据库维护控制器
 * @author Gavin
 * @date 2014-3-9 下午4:46:07
 */
@Controller
@LoginRequired
@RequestMapping("/dbMaintenance")
public class DBMaintenanceAction {

	@Resource
	private DBManager dbManager;
	/**
	 * 初始化数据库,此操作会删除数据库内容并重建
	 * @return
	 */
	@Post("/initDb")
	@PermissionMapping(name="初始化数据",code="platform01")
	public @ResponseBody Map<String,Object> initDb(){
		List<String> tableNames = new ArrayList<String>();
		tableNames.add("tab_log");
		tableNames.add("tab_menu");
		tableNames.add("tab_permission");
		tableNames.add("tab_role");
		tableNames.add("tab_role_menu");
		tableNames.add("tab_role_permission");
		tableNames.add("tab_user");
		tableNames.add("tab_dictionary");
		dbManager.dropTables(tableNames);
		dbManager.createTableIfNecessary();
		dbManager.reLoadMenus();
		dbManager.reLoadPermissions();
		return MessageUtils.getMapMessage(true);
	}
	
	@Get("")
	@MenuMapping(code="platform_7",name="数据维护",url="/dbMaintenance",parentCode="platform")
	public ModelAndView showMaintenancePage(){
		return new ModelAndView("/pages/maintenance/DBMaintenance","dbInfo",dbManager.getDbInfo());
	}

    @ResponseBody
    @Get("/tables")
    public WebResponse listTables(){
        return WebResponse.build().setResult(dbManager.listTables());
    }

    @ResponseBody
    @Get("/table/{tableName}")
    public WebResponse getTableDetails(@PathVariable("tableName")String tableName){
        return WebResponse.build().setResult(dbManager.getTableDetails(tableName));
    }
}
