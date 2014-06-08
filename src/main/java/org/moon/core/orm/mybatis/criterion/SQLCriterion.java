package org.moon.core.orm.mybatis.criterion;

import org.springframework.util.Assert;

/**
 * sql语句限制
 * @author Gavin
 * @date Jun 8, 2014
 */
public class SQLCriterion implements Criterion {

	private String sqlString;
	
	public SQLCriterion(String sqlString){
		Assert.notNull(sqlString,"sql criterion should not be null");
		this.sqlString = sqlString;
	}
	
	@Override
	public String toSqlString() {
		return sqlString;
	}

}
