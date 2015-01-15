package org.moon.db.manager.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DBManagerRepository {

	public void excuteUpdate(@Param("sql") String sql);
	
	public void query(@Param("sql") String sql);
	
	public void insert(@Param("sql") String sql);
	
}
