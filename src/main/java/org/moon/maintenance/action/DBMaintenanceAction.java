package org.moon.maintenance.action;

import org.apache.http.entity.ContentType;
import org.moon.db.manager.DBManager;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rbac.helper.Permissions;
import org.moon.rest.annotation.Delete;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.Constants;
import org.moon.utils.FileUtils;
import org.moon.utils.MessageUtils;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
@PermissionMapping(code = Permissions.DATA_MAINTENANCE,name = Permissions.DATA_MAINTENANCE_DESCRIPTION)
@RequestMapping("/dbMaintenance")
public class DBMaintenanceAction {

	@Resource
	private DBManager dbManager;

    @Resource
    private CacheManager cacheManager;

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
		return new ModelAndView("pages/maintenance/DBMaintenance","dbInfo",dbManager.getDbInfo());
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

    /**
     * 显示数据库备份恢复页面
     * @return
     */
    @Get("/backup.html")
    @MenuMapping(code="platform_10",name="备份与恢复",url="/dbMaintenance/backup.html",parentCode="platform")
    public ModelAndView showDBBackupPage(){
        return new ModelAndView("pages/maintenance/DBBackup");
    }

    @ResponseBody
    @Post("/backup")
    public WebResponse backupDataBase(){
        return WebResponse.success(dbManager.backupDataBase());
    }

    /**
     * 获取已备份的文件列表
     * @return
     */
    @Get("/backup/files")
    @ResponseBody
    public WebResponse listBackupFiles(){
        return WebResponse.success(dbManager.listBackupFiles());
    }

    /**
     * 删除数据库备份文件
     * @param fileName
     * @return
     */
    @Delete("/backup/files/{fileName:.+}")
    @ResponseBody
    public WebResponse deleteBackupFile(@PathVariable("fileName") String fileName){
        dbManager.deleteBackupFile(fileName);
        return WebResponse.success();
    }

    /**
     * 恢复数据库
     * @param fileName
     * @return
     */
    @ResponseBody
    @Post("/restore/{fileName:.+}")
    public WebResponse restoreDataBase(@PathVariable("fileName")String fileName){
        return WebResponse.build().setResult(dbManager.restoreDataBase(fileName));
    }

    /**
     * 下载数据库文件
     * @param fileName
     * @return
     */
    @Get("/backup/files/{fileName:.+}")
    public void downloadBackupFile(@PathVariable("fileName")String fileName,HttpServletRequest request,
                                          HttpServletResponse response) throws IOException {
        File backupFile = dbManager.getBackupFile(fileName);
        response.setHeader("Content-Type", ContentType.APPLICATION_OCTET_STREAM.getMimeType());
        FileUtils.write(new FileInputStream(backupFile),response.getOutputStream());
    }

    /**
     * 上传备份文件
     * @param backupFile
     * @return
     * 返回以保存的数据库备份文件的名字
     */
    @Post("/backup/files")
    @ResponseBody
    public WebResponse uploadBackupFile(@RequestParam("backupFile") MultipartFile  backupFile) throws IOException {
        return WebResponse.success(dbManager.addBackupFile(backupFile).getName());
    }

    /**
     * 清空缓存
     * @return
     */
    @Post("/cache/cleanup")
    @ResponseBody
    public WebResponse clearCache(){
        cacheManager.getCacheNames().stream().forEach(cacheName-> {
            cacheManager.getCache(cacheName).clear();
        });
        return WebResponse.success();
    }
}
