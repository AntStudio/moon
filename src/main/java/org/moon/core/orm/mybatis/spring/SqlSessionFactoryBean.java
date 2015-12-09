package org.moon.core.orm.mybatis.spring;

import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NestedIOException;

import java.io.IOException;

/**
 * 简单封装mybatis的sessionFactory创建，将错误异常打出.
 * 因为{@link org.springframework.beans.factory.support.AbstractBeanFactory#getTypeForFactoryBean}
 * 会将异常忽略掉，使用debug形式输出
 * @author GavinCook
 * @date 2015/6/8 0008
 */
public class SqlSessionFactoryBean extends org.mybatis.spring.SqlSessionFactoryBean {
    private Logger log = LoggerFactory.getLogger(SqlSessionFactoryBean.class);

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        try {
            return super.buildSqlSessionFactory();
        }catch (NestedIOException e){
            log.error("Failed to parse config resource",e);
            throw e;
        }finally {
            ErrorContext.instance().reset();
        }
    }
}
