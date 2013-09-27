package org.antstudio.pw.repository;

import org.antstudio.pw.domain.TimeStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeStatisticsRepository {

	public void save(@Param("timeStatistics")TimeStatistics timeStatistics);
	
	public void update(@Param("timeStatistics")TimeStatistics timeStatistics);
	
}
