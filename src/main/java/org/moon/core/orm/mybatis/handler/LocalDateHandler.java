package org.moon.core.orm.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;

import java.sql.*;
import java.time.*;
import java.util.Objects;

/**
 * MyBatis对<code>LocalDate</code>类型的处理类
 * @author:Gavin
 * @date 2015/5/7 0007
 */
@MappedTypes(LocalDate.class)
public class LocalDateHandler extends BaseTypeHandler<LocalDate> {

    //1970-1-1 00:00:00 用于计算距离该时间的毫秒数
    private static final ZonedDateTime JAN_1_1970 = LocalDateTime.of(1970, 1, 1, 0, 0, 0).atZone(ZoneId.systemDefault());

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalDate parameter, JdbcType jdbcType) throws SQLException {
        ps.setTimestamp(i, new Timestamp(Duration.between(JAN_1_1970,parameter.atTime(0,0).atZone(ZoneId.systemDefault())).toMillis()
        - ZoneId.systemDefault().getRules().getOffset(parameter.atTime(0,0)).getTotalSeconds()*1000));
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        if(Objects.nonNull(timestamp)) {
            return rs.getTimestamp(columnName).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            return null;
        }
    }

    @Override
    public LocalDate getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnIndex);
        if(Objects.nonNull(timestamp)) {
            return rs.getTimestamp(columnIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            return null;
        }
    }

    @Override
    public LocalDate getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp timestamp = cs.getTimestamp(columnIndex);
        if(Objects.nonNull(timestamp)) {
            return cs.getTimestamp(columnIndex).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            return null;
        }
    }
}
