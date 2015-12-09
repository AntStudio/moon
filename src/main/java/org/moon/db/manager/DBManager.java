package org.moon.db.manager;

import com.reeham.component.ddd.model.CachingModelContainer;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.moon.core.init.helper.MenuMappingHelper;
import org.moon.core.init.helper.PermissionMappingHelper;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.core.spring.config.annotation.Config;
import org.moon.db.manager.repository.DBManagerRepository;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.rbac.domain.Menu;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 数据库管理类
 * 
 * @author Gavin
 * @date 2014-3-5
 */
@Component
public class DBManager implements ApplicationContextAware,InitializingBean {

    // 可以注入数据库方言..待实现

    @Resource
    private DBManagerRepository dbManagerRepository;

    @Resource
    private PermissionService permissionService;

    @Resource
    private CachingModelContainer cachingModelContainer;

    @Resource
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Resource
    private SystemSettingService systemSettingService;

    private String webPath;

    private Logger log = LoggerFactory.getLogger(DBManager.class);

    /**
     * 数据库安装路径
     */
    private String dbInstallDir;

    @Config("backup.folder")
    private String backupFolder;

    @Value("#{db.username}")
    private String dbUserName;

    @Value("#{db.password}")
    private String dbPassword;

    private String dataBaseName = "xheart";

    /**
     * 在设置表中的key
     */
    private final String dbHostKey = "db.host";

    private final String dbPortKey = "db.port";

    private final String dbNameKey = "db.name";

    private final String dbUserNameKey = "db.username";

    private final String dbPasswordKey = "db.password";

    /**
     * mysql,mysqldump可执行文件所在的路径
     */
    private final String dbExecutableDirKey = "db.executableDir";



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
        dbManagerRepository.executeUpdate(sql.toString());
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
        permissionService.batchSave(PermissionMappingHelper.getPermissionsMapByCode().values());
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
                        DBManagerRepository.class.getName()+".executeUpdate",
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

    /**
     * 备份数据库
     * 调用cmd执行备份,需要安装mysql客户端环境.
     * 多条命令使用&&分隔
     * @return
     */
    public synchronized boolean backupDataBase(){
        StringBuilder command = new StringBuilder();
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss-SSS");
        if(!getDBExecutablePath().startsWith(File.separator)){
            command.append(getDBExecutablePath().charAt(0)).append(":").append("&&");
        }
        if(!Commands.isWindows()){
            command.append("./");
        }
        command.append("mysqldump -h ")
                .append(getBackupHost()).append(" -P ").append(getBackupPort()).append(" -u ")
                .append(getBackupUserName()).append(" -p").append(getBackupPassword()).append(" ")
                .append(getBackupName()).append(">\"").append(backupFolder).append("backup")
                .append(formatter.format(time)).append(".sql\"");
        Process p ;
        try {
            ProcessBuilder builder = Commands.exec(command.toString());
            builder.directory(new File(getDBExecutablePath()));
            p = builder.start();
            int processComplete = p.waitFor();
            if (processComplete == 0) {
                log.info("Database Backup successfully!");
                return true;
            } else {
                InputStream errorStream = p.getErrorStream();
                byte[] error = new byte[errorStream.available()];
                errorStream.read(error);
                errorStream.close();
                String errorString = new String(error);
                log.error(errorString);
                throw new ApplicationRunTimeException(errorString);
            }
        }catch (IOException e){
            throw new ApplicationRunTimeException(e.getMessage());
        }catch (InterruptedException e){
            throw new ApplicationRunTimeException(e.getMessage());
        }
    }

    /**
     * 恢复数据库
     * @param fileName
     * @return
     */
    public synchronized boolean restoreDataBase(String fileName){
        StringBuilder command = new StringBuilder();
        if(!getDBExecutablePath().startsWith(File.separator)){
            command.append(getDBExecutablePath().charAt(0)).append(":").append("&&");
        }
        if(!Commands.isWindows()){
            command.append("./");
        }
        command.append("mysql -h ")
                .append(getBackupHost()).append(" -P ").append(getBackupPort()).append(" -u ")
                .append(getBackupUserName()).append(" -p").append(getBackupPassword()).append(" ").append(getBackupName())
                .append(" < \"").append(backupFolder).append(fileName).append("\"");
        Process p ;
        try {
            ProcessBuilder builder = Commands.exec(command.toString());
            builder.directory(new File(getDBExecutablePath()));
            p = builder.start();
            int processComplete = p.waitFor();
            if (processComplete == 0) {
                log.info("Database Restore successfully!");
                return true;
            } else {
                InputStream errorStream = p.getErrorStream();
                byte[] error = new byte[errorStream.available()];
                errorStream.read(error);
                errorStream.close();
                String errorString = new String(error);
                log.error(errorString);
                throw new ApplicationRunTimeException(errorString);
            }
        }catch (IOException e){
            throw new ApplicationRunTimeException(e.getMessage());
        }catch (InterruptedException e){
            throw new ApplicationRunTimeException(e.getMessage());
        }
    }

    /**
     * 初始化webPath
     * @param applicationContext
     * @throws org.springframework.beans.BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.webPath = ApplicationContextHelper.getWebAppPath(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        createBaseDirIfNecessary(webPath);
    }

    /**
     * 列出已有的数据库备份文件
     * @return
     */
    public List listBackupFiles(){
        File backupDir = new File(backupFolder);
        if(backupDir.isDirectory()){
            return Arrays.stream(backupDir.listFiles()).map(file->
                Maps.mapIt("name",file.getName(),"size",FileUtils.getReadableFileSize(file.length()))
            ).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 删除备份文件
     * @param fileName
     */
    public void deleteBackupFile(String fileName){
        File backupFile = new File(backupFolder,fileName);
        backupFile.delete();
    }

    /**
     * 获取数据库备份文件
     * @param fileName
     * @return
     */
    public File getBackupFile(String fileName){
        return new File(backupFolder,fileName);
    }

    public File addBackupFile(MultipartFile file) throws IOException {
        File backupFile = FileUtils.getFileNotExists(new File(backupFolder,file.getOriginalFilename()));
        FileUtils.save(file.getInputStream(),backupFile);
        return backupFile;
    }

    /**
     * 获取备份数据库主机
     * @return
     */
    private String getBackupHost(){
        return systemSettingService.getSetting(dbHostKey, "127.0.0.1");
    }

    private String getBackupUserName(){
        return systemSettingService.getSetting(dbUserNameKey, dbUserName);
    }

    private String getBackupPassword(){
        return systemSettingService.getSetting(dbPasswordKey, dbPassword);
    }

    private String getBackupPort(){
        return systemSettingService.getSetting(dbPortKey, "3306");
    }

    private String getBackupName(){
        return systemSettingService.getSetting(dbNameKey, dataBaseName);
    }

    /**
     * 获取数据库mysql,mysqldump可执行路径，默认为从数据库中获取的安装路径
     * @return
     */
    private String getDBExecutablePath(){
        String executablePathFromDB = getDbInstallDirFromDB();
        if(executablePathFromDB.endsWith(File.separator)){
            executablePathFromDB += File.separator;
        }
        executablePathFromDB += "bin";
        return systemSettingService.getSetting(dbExecutableDirKey, executablePathFromDB);
    }
    /**
     * 从数据库中获取数据库安装路径
     * @return
     */
    private String getDbInstallDirFromDB(){
        if(Objects.isNull(dbInstallDir)){
            dbInstallDir = dbManagerRepository.getInstallDir();
        }
        return dbInstallDir;
    }

    /**
     * 创建基础的文件夹,如果不存在
     * @param webPath
     * @throws IOException
     */
    private void createBaseDirIfNecessary(String webPath) throws IOException{
        if(com.google.common.base.Strings.isNullOrEmpty(backupFolder)){//默认上传路径webappPath/db/
            backupFolder = webPath+File.separator+"db";
        }else if(backupFolder.startsWith("${webappPath}")){
            backupFolder = backupFolder.replace("${webappPath}",webPath ).replaceAll("[/\\\\]+",File.separator.equals("\\")?File.separator+File.separator:File.separator);
        }
        if(!backupFolder.endsWith(File.separator)){
            backupFolder = backupFolder + File.separator;
        }
        FileUtils.createIfNotExists(backupFolder, true);
    }
}
