package org.moon.support.template.repository;

import org.apache.ibatis.annotations.Param;
import org.moon.support.template.domain.Template;
import org.springframework.stereotype.Repository;

/**
 * 模板仓储类
 * @author:Gavin
 * @date 2015/6/16 0016
 */
@Repository
public interface TemplateRepository {

    /**
     * 新增，返回新增影响的行数
     * @param template
     * @return
     */
    public int save(@Param("template")Template template);

    /**
     * 更新,返回更新影响的行数
     * @param template
     * @return
     */
    public int update(@Param("template")Template template);

    /**
     * 删除，返回删除影响的行数
     * @param id
     * @return
     */
    public int delete(@Param("id")Long id);

    /**
     * 获取,返回获取到的对象
     * @param id
     * @return
     */
    public Template get(@Param("id")Long id);
}
