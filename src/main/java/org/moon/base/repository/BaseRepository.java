package org.moon.base.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.SQLProvider;

/**
 * 通用的仓储类,包含通用的增删查改方法
 * @author Gavin
 * @date  2014-05-08
 */
public interface BaseRepository<T> {

	/**
	 * 保存方法(o-->object)
	 * @param t
	 * @return
	 */
	@InsertProvider(type=SQLProvider.class,method="save")
	public Long save(@Param("o") T t);
	
	@UpdateProvider(type=SQLProvider.class,method="update")
	public void update(@Param("o") T t);
	
	@DeleteProvider(type=SQLProvider.class,method="delete")
	public void delete(@Param("domain")Class<T> t,@Param("ids") Long[] ids);
	
	public void logicDelete(@Param("ids") Long[] ids);
	
	@SelectProvider(type=SQLProvider.class,method="get")
	@ConstructorArgs(value = {
			@Arg(column="id",javaType=Integer.class),
			@Arg(column="url",javaType=String.class)
			})
    public T get(@Param("domain")Class<T> t,@Param("id")Long id);
    
    @SelectProvider(type=SQLProvider.class,method="list")
    public List<Map> list(@Param("domain")Class<T> t,@Param("criteria")Criteria criteria);
}
