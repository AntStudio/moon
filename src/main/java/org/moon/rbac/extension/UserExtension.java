package org.moon.rbac.extension;

/**
 * 用于扩展User领域相关的接口
 * @author:Gavin
 * @date 10/7/2014
 */
public interface UserExtension {

    /**
     * 获取额外的列定义
     * @return
     */
    public String[] getExtraColumnDefinitions();
}
