package com.greejoy.support.mybatis;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DefaultCriterion implements Criterion,Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 2039282758924709493L;

	private String sql;
	
	private boolean limit = false;
	
	private boolean order = false;
	
	private boolean group = false;
	
	private boolean column = false;
	
	/**
	 * @return the sql
	 */
	public String getSql() {
		return sql;
	}
	/**
	 * @param sql the sql to set
	 */
	public void setSql(String sql) {
		this.sql = sql;
		if(sql.toUpperCase().contains("LIMIT "))
			this.limit = true; 
		else if(sql.toUpperCase().contains("ORDER "))
			this.order = true;
		else if(sql.toUpperCase().contains("GROUP BY "))
			this.group = true;
		else if(sql.matches("\\[.+\\]")){
			Pattern pattern = Pattern.compile("[^\\[\\]]+");
			Matcher m = pattern.matcher(sql);
			if(m.find())
			this.sql = m.group();
			this.column = true;
		}
	
	}
	
	public DefaultCriterion(String sql){
		this.sql = sql;
		if(sql.toUpperCase().contains("LIMIT "))
			this.limit = true; 
		else if(sql.toUpperCase().contains("ORDER "))
			this.order = true;
		else if(sql.toUpperCase().contains("GROUP BY "))
			this.group = true;
		else if(sql.matches("\\[.+\\]")){
			Pattern pattern = Pattern.compile("[^\\[\\]]+");
			Matcher m = pattern.matcher(sql);
			if(m.find())
			this.sql = m.group();
			this.column = true;
		
		}
			
	}
	
	public DefaultCriterion(String name,String relation,String value,boolean limit,boolean order){
		this.sql = name+" "+relation+" "+value;
		this.limit = limit;
		this.order = order;
	}
	
	public DefaultCriterion(String name,String relation,String value){
		this(name, relation, value, false, false);
	}
	@Override
	public String toSqlString() {
		if(isLimit()){
			return sql.toUpperCase().replaceFirst("LIMIT\\s", " ");
		}
		else if(isOrder()){
			return sql.toUpperCase().replaceFirst("ORDER\\s", " ").replaceFirst("BY ", " ");
		} else if(isGroup()){
			return sql.toUpperCase().replaceFirst("GROUP BY\\s", " ");
		}
		return sql;
	}
	@Override
	public boolean isLimit() {
		return limit;
	}
	
	@Override
	public boolean isOrder() {
		return order;
	}
	/**
	 * @return the group
	 */
	public boolean isGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(boolean group) {
		this.group = group;
	}
	
	@Override
	public boolean isColumn() {
		return column;
	}
	
}
