package org.moon.core.orm.mybatis.criterion;

import org.moon.utils.Strings;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collection;

/**
 * 限制条件
 * @author Gavin
 * @version 1.0
 * @date 2013-3-10
 */
public class Restrictions {

	public static Criterion eq(String name, Object value) {
		return new SimpleCriterion(name, " = ",value);
	}

	public static Criterion gt(String name, Object value) {
		return new SimpleCriterion(name, " > ", value);
	}

	public static Criterion ge(String name, Object value) {
		return new SimpleCriterion(name, " >= ", value);
	}

	public static Criterion lt(String name, Object value) {
		return new SimpleCriterion(name, " < ", value);
	}

	public static Criterion le(String name, Object value) {
		return new SimpleCriterion(name, " <= ", value);
	}

	public static Criterion isNull(String name) {
		return new SimpleCriterion(name, " is ", null);
	}

	public static Criterion notNull(String name) {
		return new SimpleCriterion(name, " is not ", null);
	}

	public static Criterion notEq(String name, Object value) {
		return new SimpleCriterion(name," <> ", value);
	}

	public static Criterion in(String name, Collection<Object> values) {
		return new InCriterion(name, values);
	}

	public static Criterion notIn(String name, Collection<Object> values) {
		return new SQLCriterion(" not "+new InCriterion(name, values).toSqlString());
	}
	
	/**
	 * 默认like行为
	 * @param name
	 * @param value
	 * @return
	 */
	public static Criterion like(String name, Object value) {
		return like(name,value,MatchMode.EXACT);
	}

	public static Criterion like(String name, Object value,MatchMode matchMode) {
		Assert.notNull(value);
		return new SimpleCriterion(name, " like ",matchMode.toMatchString(value.toString()));
	}
	
	/**
	 * 忽略大小写的模糊查询(Insensitive like)
	 * @param name
	 * @param value
	 * @return
	 */
	public static Criterion ilike(String name, Object value) {
		return ilike(name, value,MatchMode.EXACT);
	}
	
	public static Criterion ilike(String name, Object value,MatchMode matchMode) {
		return new LikeCriterion(name, value,matchMode,true);
	}
	
	/**
	 * 大小写敏感的模糊查询(sensitive like)
	 * @param name
	 * @param value
	 * @return
	 */
	public static Criterion slike(String name, Object value) {
		return slike(name, value,MatchMode.EXACT);
	}
	
	public static Criterion slike(String name, Object value,MatchMode matchMode) {
		return new LikeCriterion(name, value,matchMode,false);
	}
	
	/**
	 * 逻辑与
	 * @param criteria
	 * @return
	 */
	public static Criterion and(Criterion...criteria) {
		return logicJoinCriteria(" AND ",criteria);
	}
	
	/**
	 * 逻辑或
	 * @param criteria
	 * @return
	 */
	public static Criterion or(Criterion...criteria) {
		return logicJoinCriteria(" OR ",criteria);
	}
	
	private static Criterion logicJoinCriteria(String logicOp,Criterion...criteria){
		Assert.noNullElements(criteria,"The criterion should not be null.");
		String sqlString = Strings.join(Arrays.asList(criteria), logicOp, new Strings.StringCustomerHandler<Criterion>() {
			@Override
			public String handle(Criterion t) {
				return t.toSqlString();
			}
		});
		return new SQLCriterion(sqlString);
	}
}
