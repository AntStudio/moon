package org.moon.core.orm.mybatis;

import org.apache.ibatis.jdbc.SQL;
import org.moon.base.domain.BaseDomain;
import org.moon.core.orm.mybatis.annotation.IgnoreNull;
import org.moon.utils.Iterators;
import org.moon.utils.Objects;
import org.moon.utils.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.ReflectionUtils.FieldCallback;
import org.springframework.util.ReflectionUtils.FieldFilter;

import javax.annotation.Resource;
import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * mybatis注解sql提供器. 包括单表增删查改的sql.
 * 
 * @author Gavin
 * @date 2014-05-08
 */
public class SQLProvider {

	private Logger logger = LoggerFactory.getLogger(SQLProvider.class);

	/**
	 * 持久化域对象,传入的对象需以'o'为key,并且要继承自{@link org.moon.base.domain.BaseDomain}
	 * @param params
	 * @return
	 */
	public String save(Map<String, Object> params) {
		final BaseDomain bd = (BaseDomain) params.get("o");
		final SQL sql = new SQL().INSERT_INTO(getTable(bd.getClass()));
		String sqlString;
		
		ReflectionUtils.doWithFields(bd.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				field.setAccessible(true);
				if(!(field.isAnnotationPresent(IgnoreNull.class)&&Objects.isNull(field.get(bd)))){//过滤掉@IgnoreNull的空值字段
					sql.VALUES(getColumn(field), wrapColumnValue(field.get(bd)));
				}
			}
		}, new FieldFilter() {//只提取没有对应注解的非静态字段
			@Override
			public boolean matches(Field field) {
				return !(field.isAnnotationPresent(Resource.class)
						|| field.isAnnotationPresent(Autowired.class)
						|| field.isAnnotationPresent(Transient.class) 
						|| Modifier.isStatic(field.getModifiers())
						|| Modifier.isFinal(field.getModifiers())
						||field.isAnnotationPresent(Transient.class)
						||(!bd.supportLogicDelete()&&"deleteFlag".equals(field.getName())));
			}
		});
		sqlString = sql.toString();
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}

	public String update(Map<String,Object> params){
		final BaseDomain bd = (BaseDomain) params.get("o");
		final SQL sql = new SQL().UPDATE(getTable(bd.getClass()));
		String sqlString;
		
		ReflectionUtils.doWithFields(bd.getClass(), new FieldCallback() {
			@Override
			public void doWith(Field field) throws IllegalArgumentException,
					IllegalAccessException {
				field.setAccessible(true);
				if(field.getName().equals("id")){
					sql.WHERE(equal(getColumn(field), wrapColumnValue(field.get(bd))));
				}
				sql.SET(equal(getColumn(field), wrapColumnValue(field.get(bd))));
			}
		}, new FieldFilter() {//只提取没有对应注解的非静态字段
			@Override
			public boolean matches(Field field) {
				return !(field.isAnnotationPresent(Resource.class)
						|| field.isAnnotationPresent(Autowired.class)
						|| field.isAnnotationPresent(Transient.class) 
						|| Modifier.isStatic(field.getModifiers())
						|| Modifier.isFinal(field.getModifiers())
						||field.isAnnotationPresent(Transient.class)
						||(!bd.supportLogicDelete()&&"deleteFlag".equals(field.getName())));
			}
		});
		
		sqlString = sql.toString();
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}
	 
	public String delete(Map<String,Object> params){
		String sqlString;
		SQL sql = new SQL();
		String tableName = getTable((Class<? extends BaseDomain>) params.get("domain"));
		Long[] ids = (Long[]) Objects.getDefault(params.get("ids"),new Long[]{});
		final StringBuilder idsCondition = new StringBuilder("(-1");
		
		Iterators.forEach(Arrays.asList(ids).iterator(),new Iterators.CustomerHandler<Long>() {
			@Override
			public void handle(Long id) {
				idsCondition.append(",").append(id);
			}
		});
		idsCondition.append(")");
		
		sql.DELETE_FROM(tableName).WHERE("id in "+idsCondition.toString());
		
		sqlString = sql.toString();
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}
	
	/**
	 * 获取单表数据列表(暂不支持多表)
	 * 
	 * @param params
	 * @return
	 */
	public String list(Map<String, Object> params) {
		Class domainClass = (Class<? extends BaseDomain>) params.get("domain");
		Criteria criteria = (Criteria) params.get("criteria");
		String tableName = getTable(domainClass);
		String sqlString;
		SQL sql = new SQL().SELECT("*").FROM(tableName);
		
		if(Objects.nonNull(criteria)&&criteria.nonEmpty()){
			sql.WHERE(criteria.toSqlString());
		}
		sqlString = sql.toString();
		if(Objects.nonNull(criteria)&&criteria.isSorted()){
			sqlString+=criteria.toOrderSqlString();
		}
		if(Objects.nonNull(criteria)&&criteria.isLimited()){
			sqlString+=criteria.toLimitSqlString();
		}
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}


	/**
	 * 计数
	 * @param params
	 * @return
	 */
	public String count(Map<String, Object> params) {
		Class domainClass = (Class<? extends BaseDomain>) params.get("domain");
		Criteria criteria = (Criteria) params.get("criteria");
		String tableName = getTable(domainClass);
		String sqlString;
		SQL sql = new SQL().SELECT("count(*)").FROM(tableName);
		
		if(Objects.nonNull(criteria)&&criteria.nonEmpty()){
			sql.WHERE(criteria.toSqlString());
		}
		
		sqlString = sql.toString();
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}
	
	/**
	 * 获取单表数据列表(暂不支持多表)
	 * 
	 * @param params
	 * @return
	 */
	public String listIds(Map<String, Object> params) {
		Class domainClass = (Class<? extends BaseDomain>) params.get("domain");
		Criteria criteria = (Criteria) params.get("criteria");
		String tableName = getTable(domainClass);
		String sqlString;
		SQL sql = new SQL().SELECT("id").FROM(tableName);
		
		if(Objects.nonNull(criteria)&&criteria.nonEmpty()){
			sql.WHERE(criteria.toSqlString());
		}
		sqlString = sql.toString();
		if(Objects.nonNull(criteria)&&criteria.isSorted()){
			sqlString+=criteria.toOrderSqlString();
		}
		if(Objects.nonNull(criteria)&&criteria.isLimited()){
			sqlString+=criteria.toLimitSqlString();
		}
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}
	
	/**
	 * 
	 * @param params
	 * @return
	 */
	public String get(Map<String, Object> params) {
		Long id = (Long) params.get("id");
		Class domainClass = (Class<? extends BaseDomain>) params.get("domain");
		String tableName = getTable(domainClass);
		String sqlString;
		SQL sql = new SQL().SELECT("*").FROM(tableName).WHERE("id="+id);
		
		sqlString = sql.toString();
		logger.debug("{} : {}" ,logger.getName(), sqlString);
		return sqlString;
	}
	
	
	/**
	 * <p>
	 * 获取当前Domain所指向的Table,如果没有{@link javax.persistence.Table}注解,默认使用 tab_
	 * {@link Strings#lowerFirst(domainName)}
	 * 
	 * @param domainClass
	 * @return
	 */
	private String getTable(Class<? extends BaseDomain> domainClass) {
		Table domainTable = (Table) domainClass.getAnnotation(Table.class);
		String tableName;
		if (domainTable == null) {
			tableName = "tab_"
					+ Strings.lowerFirst(domainClass.getSimpleName());
		} else {
			tableName = domainTable.name();
		}
		return tableName;
	}

	/**
	 * 获取字段名,首先提取{@link javax.persistence.Column},如果不存在则使用属性名(的下划线形式)
	 * 
	 * @param field
	 * @return
	 */
	private String getColumn(Field field) {
		Column column = field.getAnnotation(Column.class);
		String columnName;
		if (column != null) {
			columnName = column.name();
		} else {
			columnName = field.getName();
		}
		return columnName;
	}

	/**
	 * 字段值包装,对字符串类型和时间类型进行单引号填充
	 * @param value
	 * @return
	 */
	private String wrapColumnValue(Object value){
		if(  value instanceof String
		   ||value instanceof Date){
			return "'"+value+"'";
		}else if(value instanceof byte[]){
			return new String("'"+new String((byte[])value)+"'");
		}
		return value+"";
	}
	
	/**
	 * 返回字段=字段值,
	 * 可用于update语句的set字句和 where条件中的=条件
	 * @param column
	 * @param value
	 * @return
	 */
	private String equal(String column,Object value){
		return column+"="+value;
	}
}
