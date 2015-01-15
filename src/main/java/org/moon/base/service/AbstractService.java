package org.moon.base.service;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.moon.base.repository.CommonRepository;
import org.moon.core.spring.config.annotation.Config;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.pagination.Pager;
import org.moon.utils.Dtos;
import org.moon.utils.Objects;
import org.moon.utils.ParamUtils;
import org.moon.utils.Strings;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author:Gavin
 * @date 2014/11/19 0019
 */
public abstract class AbstractService implements BaseService {

    @Resource
    private CommonRepository repository;

    @Resource
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    /**
     * 是否使用驼峰命名规则转换字段名字
     */
    @Config(value = "useCamelbakKeyMapWrapper.flag",defaultVal = "true")
    private  String useCamelbakKeyMapWrapper;

    @Override
    public Pager listForPage(Class clazz, String statementId) {
        return listForPage(clazz,statementId, ParamUtils.getDefaultParamMap());
    }

    @Override
    public Pager listForPage(Class clazz, String statementId, Map params) {
        Assert.notNull(clazz, "The namespace class must not be null.");
        Assert.notNull(statementId,"Can't specify null statementId for execute");
        SqlSession sqlSession = null;
        statementId = statementId.trim();
        try {
            sqlSession = sqlSessionFactoryBean.getObject().openSession(ExecutorType.REUSE);
            String queryStatementId = Strings.connect(clazz.getPackage().getName(), ".",
                    clazz.getSimpleName(), ".", statementId) ;
            String countStatementId = queryStatementId + "_count" ;
            List resultList = sqlSession.selectList(queryStatementId,params);
            if(!"true".equals(useCamelbakKeyMapWrapper)){
                resultList = Dtos.convert(resultList, Dtos.newUnderlineToCamelBakConverter());
            }
            int count = sqlSession.selectOne(countStatementId,params);
            return new Pager(count,resultList, ParamUtils.getPageSize(params),ParamUtils.getPageIndex(params));
        } catch (Exception e) {
            throw new ApplicationRunTimeException(e);
        }finally {
            if(Objects.nonNull(sqlSession)){
                sqlSession.close();
            }
        }
    }
}
