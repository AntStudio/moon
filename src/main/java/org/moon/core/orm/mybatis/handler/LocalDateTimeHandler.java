package org.moon.core.orm.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.*;
import java.util.Objects;

/**
 * MyBatis对<code>LocalDateTime</code>类型的处理类,数据库对应类型应为timestamp
 * @author GavinCook
 * @date 2015/5/7 0007
 */
@MappedTypes(LocalDateTime.class)
public class LocalDateTimeHandler extends BaseTypeHandler<LocalDateTime> {

    //1970-1-1 00:00:00 用于计算距离该时间的毫秒数
    private static final ZonedDateTime JAN_1_1970 = LocalDateTime.of(1970, 1, 1, 0, 0, 0).atZone(ZoneId.systemDefault());

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(Duration.between(JAN_1_1970,parameter.atZone(ZoneId.systemDefault())).toMillis()
        - ZoneId.systemDefault().getRules().getOffset(parameter).getTotalSeconds()*1000));
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        if(Objects.nonNull(timestamp)) {
            return rs.getTimestamp(columnName).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            return null;
        }
    }

    @Override
    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        if(Objects.nonNull(timestamp)) {
            return rs.getTimestamp(columnIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            return null;
        }
    }

    @Override
    public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        if(Objects.nonNull(timestamp)) {
            return cs.getTimestamp(columnIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            return null;
        }
    }
}
