package org.moon.core.orm.mybatis.handler;

import org.apache.ibatis.type.EnumTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.moon.core.orm.mybatis.valueenum.ValueEnum;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 带有值的枚举类型处理
 * @author gavin
 * @date 2015/4/30
 */
public class ValueEnumTypeHandler<E extends Enum<E>> extends EnumTypeHandler<E> {

    private boolean isValueEnumType = false;
    private Class<E> type;
    private final E[] enums;
    private ValueEnum valueEnumsInstance = null;
    public ValueEnumTypeHandler(Class<E> type) {
        super(type);
        this.type = type;
        this.enums = type.getEnumConstants();
        Class<?>[] interfaces = type.getInterfaces();
        for(Class<?> clazz:interfaces){
            if(clazz == ValueEnum.class){
                this.valueEnumsInstance = (ValueEnum) enums[0];
                isValueEnumType = true;
                break;
            }
        }
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        if(isValueEnumType){
            ValueEnum valueEnum = (ValueEnum)parameter;
            if (jdbcType == null) {
                ps.setObject(i,valueEnum.value());
            } else {
                ps.setObject(i, valueEnum.value(), jdbcType.TYPE_CODE);
            }
        }else{
            super.setNonNullParameter(ps, i, parameter, jdbcType);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if(isValueEnumType){
            Object o = rs.getObject(columnName);
            return (E) valueEnumsInstance.valueOf(o);
        }else{
            return super.getNullableResult(rs,columnName);
        }
    }

    @Override
    public E getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if(isValueEnumType){
            Object o = rs.getObject(columnIndex);
            return (E) valueEnumsInstance.valueOf(o);
        }else{
            return super.getNullableResult(rs,columnIndex);
        }
    }

    @Override
    public E getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if(isValueEnumType){
            Object o = cs.getObject(columnIndex);
            return (E) valueEnumsInstance.valueOf(o);
        }else{
            return super.getNullableResult(cs,columnIndex);
        }
    }

//    public static void main(String[] args) {
//        Class<ValueEnum<Integer,ConsultationType>> s = null ;
//        System.out.println(s.getName());
//    }
}
