package org.moon.db.manager.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBManagerRepository {

	public void executeUpdate(@Param("sql")String sql);
	
	public void query(@Param("sql")String sql);
	
	public void insert(@Param("sql")String sql);

    /**
     * 获取数据库安装路径,目前只支持MySQL
     * @return
     */
    public String getInstallDir();
}
