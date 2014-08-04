package org.moon.core.orm.mybatis.dialect;

/**
 * 数据库方言(目前只支持MySQL)
 * @author Gavin
 * @date Jun 8, 2014
 */
public abstract class AbstractDialect implements Dialect{

	public abstract String getCaseInsensitiveLike();
	
	public abstract String getCaseSensitiveLike();
	
	public abstract String getLimitSql(int offset,int limit);
}
