package org.moon.core.orm.mybatis.criterion;

import java.io.Serializable;

import org.springframework.util.Assert;

/**
 * 排序
 * @author Gavin
 * @date Jun 8, 2014
 */
public class Order implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean asc = true;
	
	public Order(String name,boolean asc){
		Assert.notNull(name,"The sort column should not be null.");
		this.name = name;
		this.asc = asc;
	}
	
	public static Order asc(String name){
		return new Order(name, true);
	}
	
	public static Order desc(String name){
		return new Order(name, false);
	}
	
	public String toSqlString(){
		return name+" "+(asc?" asc ":" desc");
	}
}
