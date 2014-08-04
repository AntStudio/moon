package org.moon.db.manager;

import com.reeham.component.ddd.model.CachingModelContainer;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.moon.db.manager.repository.DBManagerRepository;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.domain.init.helper.MenuMappingHelper;
import org.moon.rbac.domain.init.helper.PermissionMappingHelper;
import org.moon.rbac.service.MenuService;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.Maps;
import org.moon.utils.Resources;
import org.moon.utils.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库管理类
 * 
 * @author Gavin
 * @date 2014-3-5
 */
@Component
public class DBManager {

    // 可以注入数据库方言..待实现

    @Resource
    private DBManagerRepository dbManagerRepository;
    @Resource
    private PermissionService permissionService;
    @Resource
    private MenuService menuService;
    @Resource
    private CachingModelContainer cachingModelContainer;
    @Resource
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    /**
     * 删除数据表,目前只适用于mysql
     * @param tables
     */
    public void dropTables(List<String> tables) {
        StringBuilder sql = new StringBuilder();
        sql.append("SET FOREIGN_KEY_CHECKS = 0;");// 忽略外键
        sql.append("Drop table if exists ");
        for (String table : tables) {
            sql.append(table).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" CASCADE ;");
        sql.append("SET FOREIGN_KEY_CHECKS = 1;");// 恢复外键检查
        dbManagerRepository.excuteUpdate(sql.toString());
    }

    /**
     * 创建缺失的数据表
     */
    public void createTableIfNecessary() {
        batchExecute(Strings.getContentFromInputStream(Resources.load("~schema.config"),"UTF-8"));
    }

    /**
     * 重新加载权限到数据库
     */
    public void reLoadPermissions() {
        cachingModelContainer.clearModelCache();
        permissionService.batchSave(PermissionMappingHelper.getMappedPermissions());
    }

    /**
     * 重新加载菜单到数据库
     */
    public void reLoadMenus() {
        cachingModelContainer.clearModelCache();
        for(Menu m:MenuMappingHelper.getMappingMenus()){
        	m.save();
        }
    }

    public void batchExecute(String sqls) {
        batchExecute(Arrays.asList(sqls.split(";")), 500);
    }

    public void batchExecute(String sqls, int batchSize) {
        batchExecute(Arrays.asList(sqls.split(";")), batchSize);
    }

    public void batchExecute(Iterable<String> sqls) {
        batchExecute(sqls, 500);
    }

    /**
     * 批量执行更新语句
     * @param sqls
     * @param batchSize
     */
    public void batchExecute(Iterable<String> sqls, int batchSize) {
        SqlSession session = null;
        try {
            session = sqlSessionFactoryBean.getObject().openSession(ExecutorType.BATCH, false);
            int currentTime = 1;
            for (String sql : sqls) {
            	if(StringUtils.isEmpty(StringUtils.trimWhitespace(sql))){
            		continue;
            	}
                session.update(
                        "org.moon.db.manager.repository.DBManagerRepository.excuteUpdate",
                        Maps.mapIt("sql", sql));
                if (currentTime % 500 == 0) {
                    session.commit();
                    session.clearCache();
                }
                currentTime++;
            }
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (session != null) {
                session.rollback();
            }
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
