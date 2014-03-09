package org.antstudio.db.manager;

import java.util.List;

import javax.annotation.Resource;

import org.antstudio.base.init.repository.TableCreatorRepository;
import org.antstudio.db.manager.repository.DBManagerRepository;
import org.antstudio.rbac.domain.init.helper.MenuMappingHelper;
import org.antstudio.rbac.domain.init.helper.PermissionMappingHelper;
import org.antstudio.rbac.service.MenuService;
import org.antstudio.rbac.service.PermissionService;
import org.springframework.stereotype.Component;

import com.reeham.component.ddd.model.CachingModelContainer;

/**
 * 数据库管理类
 * @author Gavin
 * @date 2014-3-5
 */
@Component
public class DBManager {

	//可以注入数据库方言..待实现
	
	
	//想法:做一个可配置的通知,如某种配置文件,在项目启动后，自动读取通知,这样可以使用户及时得到信息更新.
	@Resource
	private DBManagerRepository dbManagerRepository;
	@Resource
	private TableCreatorRepository tableCreatorRepository;
	@Resource
	private PermissionService permissionService;
	@Resource
	private MenuService menuService;
	@Resource
	private CachingModelContainer cachingModelContainer;
	
	public void dropTables(List<String> tables){
		StringBuilder sql = new StringBuilder();
		sql.append("SET FOREIGN_KEY_CHECKS = 0;");//忽略外键
		sql.append("Drop table if exists ");
		for(String table:tables){
			sql.append(table).append(",");
		}
		sql.deleteCharAt(sql.length()-1);
		sql.append(" CASCADE ;");
		sql.append("SET FOREIGN_KEY_CHECKS = 1;");//恢复外键检查
		dbManagerRepository.excuteUpdate(sql.toString());
	}
	
	public void createTableIfNecessary(){
		tableCreatorRepository.createTableIfNecessary();
	}
	
	public void reLoadPermissions(){
		cachingModelContainer.clearModelCache();
		permissionService.batchSave(PermissionMappingHelper.getMappedPermissions());
	}
	
	public void reLoadMenus(){
		cachingModelContainer.clearModelCache();
		menuService.addMenus(MenuMappingHelper.getMappingMenus());
	}
}
