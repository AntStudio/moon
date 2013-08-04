package com.greejoy.support.mybatis;

/**
 * sql条件
 * @author Gavin
 * @version 1.0
 * @date 2013-3-10
 */
public interface Criterion{

	public String toSqlString();
	
	public void setSql(String sql);
	
	public boolean isLimit();
	
	public boolean isOrder();
	
	public boolean isGroup();
	
	public boolean isColumn();
}
