package org.moon.core.orm.mybatis.dialect;

import org.springframework.stereotype.Component;

@Component
public class MySQLDiaect extends AbstractDialect {

	@Override
	public String getCaseInsensitiveLike() {
		return " like {0} ";
	}

	@Override
	public String getCaseSensitiveLike() {
		return " like {0} COLLATE utf8_bin ";
	}

	@Override
	public String getLimitSql(int offset, int limit) {
		return " limit "+offset+","+limit;
	}

}
