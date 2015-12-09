package org.moon.core.orm.mybatis.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.moon.exception.ApplicationRunTimeException;

import java.io.UnsupportedEncodingException;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * <code>text</code>类型的处理类
 * @author:Gavin
 * @date 2015/5/7 0007
 */
public class TextTypeHandler extends BaseTypeHandler<String> {


    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType)
            throws SQLException {
        ps.setString(i, parameter);
    }

    @Override
    public String getNullableResult(ResultSet rs, String columnName)
            throws SQLException {
        try {
            return new String(rs.getBytes(columnName),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationRunTimeException(e);
        }
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex)
            throws SQLException {
        try {
            return new String(rs.getBytes(columnIndex),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationRunTimeException(e);
        }
    }

    @Override
    public String getNullableResult(CallableStatement cs, int columnIndex)
            throws SQLException {
        try {
            return new String(cs.getBytes(columnIndex),"utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationRunTimeException(e);
        }
    }
}
