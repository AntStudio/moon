package org.moon.core.orm.mybatis.criterion;

import org.moon.utils.Strings;

import java.io.Serializable;

/**
 * 简单的sql条件限制
 * @author Gavin
 * @date 2014-06-04
 */
public final class SimpleCriterion implements Criterion,Serializable{

	private static final long serialVersionUID = 1L;
	private String sqlString;
	
	@Override
	public String toSqlString() {
		return sqlString;
	}

	public  SimpleCriterion(String name,String op,Object value){
		this.sqlString = name + op + Strings.wrapIfNecessary(value);
	}
	
	public  SimpleCriterion(String sqlString){
		this.sqlString = sqlString;
	}
}
