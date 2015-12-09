package org.moon.base.service;

import org.moon.pagination.PageCondition;
import org.moon.pagination.Pager;

import java.util.Map;

/**
 * @author GavinCook
 * @since 1.0.0
 */
public interface BaseService {

    /**
     * list data for pager without parameters
     * @param clazz the namespace for sql statement
     * @param statementId the statement id which defined in the sql mapper file
     * @return the data wrapped into pager
     * @see {@link BaseDomainService#listForPage(Class, String, java.util.Map)}
     */
    Pager listForPage(Class clazz,String statementId);

    /**
     * list data for pager with query parameters
     * @param clazz class namespace
     * @param statementId the statement id which defined in the sql mapper file
     * @param params query parameter
     * @return the data wrapped into pager
     */
    Pager listForPage(Class clazz,String statementId,Map<String,Object> params);

    /**
     * list data for pager with query parameters
     * @param clazz class namespace
     * @param statementId the statement id which defined in the sql mapper file
     * @param params query parameter
     * @param pageCondition paging parameter
     * @return the data wrapped into pager
     */
    Pager listForPage(Class clazz,String statementId,Map<String,Object> params,PageCondition pageCondition);

}
