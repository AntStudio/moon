package org.moon.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 属性表格
 * @author:Gavin
 * @date 2015/7/22 0022
 */
public interface TreeGrid {

    @JsonProperty("_hasChildren")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    Boolean hasChildren();

}
