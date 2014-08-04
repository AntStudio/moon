package org.moon.core.orm.mybatis.dialect;

/**
 * @author Gavin
 * @date 2014-08-04
 */
public interface Dialect {

    public abstract String getCaseInsensitiveLike();

    public abstract String getCaseSensitiveLike();

    public abstract String getLimitSql(int offset,int limit);

}
