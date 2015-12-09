package org.moon.base.repository;

import org.apache.ibatis.annotations.*;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.SQLProvider;

import java.util.List;
import java.util.Map;

/**
 * common repository , contains the save, update, delete, get, list operations. this one can only handle the domain with
 * single table, not support the domain with multiple tables.
 * @author GavinCook
 * @since 1.0.0
 * @see SQLProvider
 */
public interface BaseRepository<T> {

    @InsertProvider(type = SQLProvider.class, method = "save")
    @SelectKey(statement = "SELECT LAST_INSERT_ID() AS id", before = false, keyProperty = "o.id", resultType = Long.class)
    Long save(@Param("o") T t);

    @UpdateProvider(type = SQLProvider.class, method = "update")
    void update(@Param("o") T t);

    @DeleteProvider(type = SQLProvider.class, method = "delete")
    void delete(@Param("domain") Class<T> t, @Param("ids") Long[] ids);

    @SelectProvider(type = SQLProvider.class, method = "get")
    T get(@Param("domain") Class<T> t, @Param("id") Long id);

    @SelectProvider(type = SQLProvider.class, method = "list")
    List<Map> list(@Param("domain") Class<T> t, @Param("criteria") Criteria criteria);

    @SelectProvider(type = SQLProvider.class, method = "listIds")
    List<Long> listIds(@Param("domain") Class<T> t, @Param("criteria") Criteria criteria);

    @SelectProvider(type = SQLProvider.class, method = "count")
    Integer count(@Param("domain") Class<T> t, @Param("criteria") Criteria criteria);
}
