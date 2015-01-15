package org.moon.base.service;

import org.moon.pagination.Pager;

import java.util.Map;

/**
 * @author:Gavin
 * @date 2014/11/19 0019
 */
public interface BaseService {

    /**
     * 不接收参数的查询,默认返回第一页
     * @param clazz
     * @param statementId
     * @return
     * @see {@link BaseDomainService#listForPage(Class, String, java.util.Map)}
     */
    public Pager listForPage(Class clazz, String statementId);

    /**
     * 根据配置在mapper xml中的语句id获取分页结果,在改命名空间应至少有：
     * <code>{statementId}</code>和<code>{statementId}_count</code>两个语句,
     * <code>statementId</code>用于返回查询结果集,
     * <code>{statementId}_count</code>用于返回查询总数
     * @param clazz 命名空间类
     * @param statementId
     * @param params 查询条件(包含分页信息)
     * @return
     */
    public Pager listForPage(Class clazz, String statementId, Map params);

}
