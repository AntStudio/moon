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

public class Criteria implements Serializable {

	private  ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
	
	private static final long serialVersionUID = 5084662034070903291L;

	private Collection<Criterion> criteria = new ArrayList<Criterion>();
	
	private Collection<Order> orders = new ArrayList<Order>();
	private int offset,limit;
	
	public Criteria add(Criterion criterion) {
		this.criteria.add(criterion);
		return this;
	}

	public Criteria offset(int offset){
		this.offset = offset;
		return this;
	}
	
	public Criteria limit(int limit){
		this.limit = limit;
		return this;
	}
	
	public Criteria order(Order order){
		orders.add(order);
		return this;
	}
	
	public void toSqlString(){
		StringBuffer sql = new StringBuffer();
		sql.append(Strings.join(criteria, " AND ",new Strings.StringCustomerHandler<Criterion>() {
			@Override
			public String handle(Criterion c) {
				return c.toSqlString();
			}
		}));
		
		sql.append(" order by ");
		
		sql.append(Strings.join(orders, " , ",new Strings.StringCustomerHandler<Order>() {
			@Override
			public String handle(Order o) {
				return o.toSqlString();
			}
		}));
		
		sql.append(" ").append(applicationContext.getBean(Dialect.class).getLimitSql(offset, limit));
	}
}
