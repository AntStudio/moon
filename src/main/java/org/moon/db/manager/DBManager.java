package org.moon.db.manager;

import com.reeham.component.ddd.model.CachingModelContainer;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.moon.db.manager.repository.DBManagerRepository;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.rbac.domain.Menu;
import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.helper.PermissionMappingHelper;
import org.moon.rbac.service.MenuService;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.Maps;
import org.moon.utils.Resources;
import org.moon.utils.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private Logger log = LoggerFactory.getLogger(DBManager.class);
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
                log.debug("prepare to execute {}",sql);
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
            throw new ApplicationRunTimeException(e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    /**
     * 获取数据表
     * @return
     */
    public List<Map> listTables(){
        Connection connection = null;
        try {
            List<Map> tables = new ArrayList<Map>();
            connection = sqlSessionFactoryBean.getObject().openSession().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseName = getDatabaseName(metaData);
            ResultSet tableResultSet = metaData.getTables(databaseName, null, null, null);
            while(tableResultSet.next()) {
                if("TABLE".equals(tableResultSet.getString("TABLE_TYPE"))) {
                    tables.add(Maps.mapIt("tableName",tableResultSet.getString("TABLE_NAME"),
                                          "catalogName",tableResultSet.getString("TABLE_CAT"),
                                          "schemaName",tableResultSet.getString("TABLE_SCHEM")));
                }
            }
            return tables;
        } catch (Exception e) {
           throw new ApplicationRunTimeException(e);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ApplicationRunTimeException(e);
                }
            }
        }
    }

    /**
     * 获取数据库信息，数据库类型和版本号
     * @return
     */
    public  Map<String,String> getDbInfo(){
        Map<String,String> dbInfo = new HashMap<String, String>();
        Connection connection = null;
        try {
            connection = sqlSessionFactoryBean.getObject().openSession().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            dbInfo.put("name",metaData.getDatabaseProductName());
            dbInfo.put("version",metaData.getDatabaseProductVersion());
        } catch (Exception e) {
            throw new ApplicationRunTimeException(e);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ApplicationRunTimeException(e);
                }
            }
        }
        return dbInfo;
    }


    public List<Map> getTableDetails(String tableName){
        List<Map> columns = new ArrayList<Map>();
        Connection connection = null;
        try {
            connection = sqlSessionFactoryBean.getObject().openSession().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            String databaseName = getDatabaseName(metaData);
            ResultSet columnResultSet = metaData.getColumns(databaseName,null,tableName,null);
            while(columnResultSet.next()) {
                columns.add(Maps.mapIt(     "name" , columnResultSet.getString("COLUMN_NAME"),
                                            "type" , columnResultSet.getString("TYPE_NAME"),
                                            "size" , columnResultSet.getString("COLUMN_SIZE"),
                                        "nullable" , columnResultSet.getString("IS_NULLABLE"),
                                   "autoIncrement" , columnResultSet.getString("IS_AUTOINCREMENT"),
                                         "remarks" , columnResultSet.getString("REMARKS"),
                                "columnDefinition" , columnResultSet.getString("COLUMN_DEF")));
            }
            return columns;
        } catch (Exception e) {
            throw new ApplicationRunTimeException(e);
        }finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new ApplicationRunTimeException(e);
                }
            }
        }
    }

    /**
     * 获取数据库名称
     * @param metaData
     * @return
     */
    public String getDatabaseName(DatabaseMetaData metaData){
        try {
            String databaseName = "";
            String url = metaData.getURL();
            Pattern pattern = Pattern.compile("^.*/([\\w_\\d^\\.]+)(\\\\?.*)?$");
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                databaseName = matcher.group(1);
            }
            return databaseName;
        }catch (Exception e){
            throw new ApplicationRunTimeException(e);
        }
    }

}
