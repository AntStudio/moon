package org.moon.core.orm.mybatis;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.moon.core.orm.mybatis.criterion.Criterion;
import org.moon.core.orm.mybatis.criterion.Order;
import org.moon.core.orm.mybatis.dialect.Dialect;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.utils.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

public class Criteria implements Serializable {

	/**
	 * 目前支持一种数据库，多数据库暂时不支持
	 */
	private  ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
	
	private static final long serialVersionUID = 5084662034070903291L;

	private Collection<Criterion> criteria = new ArrayList<Criterion>();
	
	private Collection<Order> orders = new ArrayList<Order>();
	
	private int offset = 0 ,limit = -1;
	
	private boolean statusChanged = false;//标示Criteria中是否有状态发生变化，如果发生了变化，toSqlString()需要重新构建sql语句，否则直接返回已有的sql语句
	
	private String sqlString = "";
	
	private boolean empty = true;//标示是否有限制条件
	
	public Criteria add(Criterion criterion) {
		this.criteria.add(criterion);
		triggerStatusChange();
		return this;
	}

	public Criteria offset(int offset){
		this.offset = offset;
		triggerStatusChange();
		return this;
	}
	
	public Criteria limit(int limit){
		Assert.isTrue(limit>=0, "the limit of Criteria should be large than 0.");
		this.limit = limit;
		triggerStatusChange();
		return this;
	}
	
	public Criteria order(Order order){
		orders.add(order);
		triggerStatusChange();
		return this;
	}
	
	public String toSqlString(){
		if(statusChanged){
			return generateSqlString();
		}else{
			return sqlString;
		}
	}
	
	public boolean isEmpty(){
		return empty;
	}
	
	public boolean nonEmpty(){
		return !empty;
	}
	/**
	 * 触发状态改变
	 */
	private void triggerStatusChange(){
		statusChanged = true;
		empty = false;
	}
	
	/**
	 * 重新生成sql语句
	 * @return
	 */
	private String generateSqlString(){
		StringBuffer sql = new StringBuffer();
		sql.append(Strings.join(criteria, " AND ",new Strings.StringCustomerHandler<Criterion>() {
			@Override
			public String handle(Criterion c) {
				return c.toSqlString();
			}
		}));
		
		if(orders.size()>0){
			sql.append(" order by ");
			
			sql.append(Strings.join(orders, " , ",new Strings.StringCustomerHandler<Order>() {
				@Override
				public String handle(Order o) {
					return o.toSqlString();
				}
			}));
		}
		if(limit!=-1){//当limit大于0时(不等于初始值)，才加limit后缀
			sql.append(" ").append(applicationContext.getBean(Dialect.class).getLimitSql(offset, limit));
		}
		sqlString =  sql.toString();
		statusChanged = false;
		return sqlString;
	}
}
