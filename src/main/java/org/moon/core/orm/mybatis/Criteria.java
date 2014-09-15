package org.moon.core.orm.mybatis;

import org.moon.core.orm.mybatis.criterion.Criterion;
import org.moon.core.orm.mybatis.criterion.Order;
import org.moon.core.orm.mybatis.dialect.AbstractDialect;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.utils.Constants;
import org.moon.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 数据查询限制条件，主要用于支持单表查询，多表查询使用xml配置方式处理
 */
public class Criteria implements Serializable {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 目前支持一种数据库，多数据库暂时不支持
	 */
	private  ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
	
	private static final long serialVersionUID = 5084662034070903291L;

	private Collection<Criterion> criteria = new ArrayList<Criterion>();
	
	private Collection<Order> orders = new ArrayList<Order>();
	
	private int offset = 0 ,limit = Constants.DEFAULT_PAGESIZE,currentPage = Constants.DEFAULT_PAGEINDEX;
	
	private boolean statusChanged = false;//标示Criteria中是否有状态发生变化，如果发生了变化，toSqlString()需要重新构建sql语句，否则直接返回已有的sql语句
	
	private boolean isLimited = false;//是否需要加限制
	
	private String sqlString = "";
	
	private String limitSql = "";
	
	private String orderSql = "";
	
	private boolean empty = true;//标示是否有限制条件
	
	public Criteria add(Criterion criterion) {
		this.criteria.add(criterion);
		empty = false;
		triggerStatusChange();
		logger.debug("Setting criterion : {} for criteria ",criterion.toString());
		return this;
	}

	public Criteria offset(int offset){
		this.offset = offset;
		triggerStatusChange();
		logger.debug("Setting offset {} for criteria ",offset);
		return this;
	}
	
	public Criteria currentPage(int currentPage){
		this.currentPage = currentPage;
		triggerStatusChange();
		logger.debug("Setting current page {} for criteria ",currentPage);
		return this;
	}
	
	public Criteria limit(int limit){
		Assert.isTrue(limit>=0, "the limit of Criteria should be large than 0.");
		this.isLimited = true;
		this.limit = limit;
		triggerStatusChange();
		logger.debug("Setting limit {} for criteria ",limit);
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
	
	public String toLimitSqlString(){
		if(StringUtils.isEmpty(limitSql)){
			generateSqlString();
		}
		return limitSql;
	}
	
	public String toOrderSqlString(){
		if(StringUtils.isEmpty(orderSql)){
			generateSqlString();
		}
		return orderSql;
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
			orderSql = " order by ";
			
			orderSql+=(Strings.join(orders, " , ",new Strings.StringCustomerHandler<Order>() {
				@Override
				public String handle(Order o) {
					return o.toSqlString();
				}
			}));
		}
		if(isLimited){//当limit大于0时(不等于初始值)，才加limit后缀
			if(offset<=0){
				offset = (currentPage-1)*limit; 
			}
			limitSql = " "+applicationContext.getBean(AbstractDialect.class).getLimitSql(offset, limit);
		}
		sqlString =  sql.toString();
		logger.debug("SQL generated successfully.{criteria sql:{} ,order sql :{}, limit sql: {} }",sqlString,orderSql,limitSql);
		statusChanged = false;
		return sqlString;
	}
	
	public int getPageIndex(){
		return currentPage;
	}
	
	public boolean isSorted(){
		return orders.size()>0;
	}
	
	public int getPageSize(){
		return limit;
	}

	public boolean isLimited() {
		return isLimited;
	}
}
