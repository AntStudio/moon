package org.moon.rbac.domain;

import java.util.HashMap;
import java.util.Map;

import org.moon.base.domain.BaseDomain;

import com.reeham.component.ddd.annotation.Model;

/**
 * the domain for permission
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Model
public class Permission extends BaseDomain {

    private static final long serialVersionUID = -3163741468593949374L;

    public Permission() {

    }

    public Permission(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private String name;

    private String code;

    private Long   createBy;

    public void saveOrUpdate() {

    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code
     *            the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the createBy
     */
    public Long getCreateBy() {
        return createBy;
    }

    /**
     * @param createBy
     *            the createBy to set
     */
    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", name);
        m.put("id", id);
        m.put("code", code);
        return m;
    }
}
