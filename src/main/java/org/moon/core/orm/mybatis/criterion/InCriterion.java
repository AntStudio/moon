package org.moon.core.orm.mybatis.criterion;

import org.moon.utils.Strings;

import java.util.Collection;

/**
 * in条件
 * @author Gavin
 * @date 2014-06-04
 */
public class InCriterion implements Criterion{

	private String sqlString = "";
	
	@Override
	public String toSqlString() {
		return sqlString;
	}

	public InCriterion(String name, Collection<Object> values) {
		StringBuffer sb = new StringBuffer("(").append(
						Strings.join(values, ",",new Strings.StringCustomerHandler<Object>() {
							@Override
							public String handle(Object v) {
								return Strings.wrapIfNecessary(v);
							}
						})
						).append(")");
		sqlString = name + " in " + sb.toString();
	}
}
