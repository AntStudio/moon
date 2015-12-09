package org.moon.core.orm.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * MyBatis对<code>LocalDateTimeInNumberHandler</code>类型的处理类,数据库对应类型为INT或BIGINT,存储的是从1970年到现在的秒数.
 * 如果需要更高精度，请使用{@link LocalDateTimeInEpochMillisSecondsHandler}
 * @author GavinCook
 * @date 2015/5/7 0007
 */
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeInEpochSecondsHandler extends BaseTypeHandler<LocalDateTime> {

    //1970-1-1 00:00:00 用于计算距离该时间的秒数
    private static final ZonedDateTime JAN_1_1970 = LocalDateTime.of(1970, 1, 1, 0, 0, 0).atZone(ZoneId.systemDefault());

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setLong(i, Duration.between(JAN_1_1970,parameter.atZone(ZoneId.systemDefault())).toMillis()/1000
                - ZoneId.systemDefault().getRules().getOffset(parameter).getTotalSeconds());
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Long seconds = rs.getLong(columnName);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds * 1000),ZoneId.systemDefault());
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Long seconds = rs.getLong(columnIndex);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds * 1000),ZoneId.systemDefault());
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Long seconds = cs.getLong(columnIndex);
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(seconds * 1000),ZoneId.systemDefault());
    }
}
