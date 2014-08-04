package org.moon.core.orm.mybatis.criterion;

import org.moon.core.orm.mybatis.dialect.AbstractDialect;
import org.moon.core.spring.ApplicationContextHelper;
import org.moon.utils.Strings;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

/**
 * like 限制条件
 * @author Gavin
 * @date Jun 8, 2014
 */
public class LikeCriterion implements Criterion{

	private String sqlString;
	
	private ApplicationContext applicationContext = ApplicationContextHelper.getApplicationContext();
	
	private AbstractDialect dialect ;

	public LikeCriterion(String name,Object value,MatchMode matchMode,boolean caseSensitive){
		Assert.notNull(value);
		init();
		like(name,value,matchMode,caseSensitive);
	}
	
	private void init(){
		dialect = applicationContext.getBean(AbstractDialect.class);
	}
	
	private void like(String name,Object value,MatchMode matchMode,boolean caseSensitive){
		sqlString = name+String.format(caseSensitive?dialect.getCaseSensitiveLike():dialect.getCaseInsensitiveLike(),
				                       Strings.wrapIfNecessary(matchMode.toMatchString(value.toString())));
	}
	
	@Override
	public String toSqlString() {
		return sqlString;
	}

}
