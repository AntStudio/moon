package org.moon.rbac.domain;

import com.reeham.component.ddd.annotation.Model;
import org.moon.base.domain.BaseDomain;
import org.moon.core.annotation.NoLogicDeleteSupport;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限领域
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Model
@NoLogicDeleteSupport
public class Permission extends BaseDomain {

    private static final long serialVersionUID = -3163741468593949374L;

    public Permission() { }

    public Permission(String code, String name,String pointcut) {
        this.code = code;
        this.name = name;
        this.pointcut = pointcut;
    }

    private String name;

    private String code;

    private String pointcut;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("name", name);
        m.put("id", id);
        m.put("code", code);
        m.put("pointcut",pointcut);
        return m;
    }
}
