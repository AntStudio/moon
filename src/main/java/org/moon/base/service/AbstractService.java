package org.moon.base.service;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.moon.base.repository.CommonRepository;
import org.moon.core.session.SessionContext;
import org.moon.core.spring.config.annotation.Config;
import org.moon.exception.ApplicationRunTimeException;
import org.moon.pagination.PageCondition;
import org.moon.pagination.Pager;
import org.moon.utils.*;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * The default implementation for the {@link BaseService}, for the <code>listForPage</code>, there must have the
 * statement defined with identifier: <code>{statementId}_count</code>, this mainly use to query the total count for
 * pagination
 * @author GavinCook
 * @since 1.0.0
 */
public abstract class AbstractService implements BaseService {

    @Resource
    private CommonRepository repository;

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Resource
    private SqlSessionFactoryBean sqlSessionFactoryBean;

    @Override
    public Pager listForPage(Class clazz, String statementId) {
        return listForPage(clazz,statementId, ParamUtils.getDefaultParamMap());
    }

    /**
     * list data for pager with query parameters, it would try to construct pageCondition
     * from the current request(it's an thread local variables) and pass to the sql query
     * @param clazz class namespace
     * @param statementId the statement id which defined in the sql mapper file
     * @param params query parameter
     * @return the data wrapped into pager
     */
    @Override
    public Pager listForPage(Class clazz, String statementId, Map<String,Object> params) {
        return listForPage(clazz,statementId,params,null);
    }

    /**
     * list data for pager with query parameters, if the <code>pageCondition</code> parameter is <code>null</code>,
     * it would try to construct pageCondition from the current request(it's an thread local variables)
     * @param clazz class namespace
     * @param statementId the statement id which defined in the sql mapper file
     * @param params query parameter
     * @param pageCondition paging parameter
     * @return the data wrapped into pager
     */
    @Override
    public Pager listForPage(Class clazz, String statementId, Map<String,Object> params, PageCondition pageCondition) {
        Assert.notNull(clazz, "The namespace class must not be null.");
        Assert.notNull(statementId,"Can't specify null statementId for execute");
        SqlSession sqlSession = null;
        statementId = statementId.trim();
        if(Objects.isNull(params)){
            params = Maps.mapItSO();
        }
        if(Objects.isNull(pageCondition)){
            pageCondition = ParamUtils.getPageConditionFromRequest(SessionContext.getRequest());
        }
        params.put("pageCondition",pageCondition);
        try {
            sqlSession = sqlSessionFactoryBean.getObject().openSession(ExecutorType.REUSE);
            String queryStatementId = Strings.connect(clazz.getPackage().getName(), ".",
                    clazz.getSimpleName(), ".", statementId) ;
            String countStatementId = queryStatementId + "_count" ;
            List resultList = sqlSession.selectList(queryStatementId, params);

            int count = sqlSession.selectOne(countStatementId, params);
            int pageIndex = Objects.nonNull(pageCondition)?pageCondition.getPageIndex():ParamUtils.getPageIndex(params);
            int pageSize =  Objects.nonNull(pageCondition)?pageCondition.getLimit():ParamUtils.getPageSize(params);
            return new Pager(count,resultList, pageSize, pageIndex);
        } catch (Exception e) {
            throw new ApplicationRunTimeException(e);
        }finally {
            if(Objects.nonNull(sqlSession)){
                sqlSession.close();
            }
        }
    }
}
