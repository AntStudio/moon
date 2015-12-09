package org.moon.log.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.reeham.component.ddd.annotation.Model;
import org.moon.base.domain.BaseDomain;
import org.moon.core.annotation.NoLogicDeleteSupport;
import org.moon.core.orm.mybatis.annotation.IgnoreNull;
import org.moon.core.session.SessionContext;
import org.moon.rbac.domain.User;
import org.moon.utils.Constants;

import javax.persistence.Table;
import javax.persistence.Transient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;


/**
 * 日志领域
 *
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Model
@Table(name = "tab_log")
@NoLogicDeleteSupport
public class Log extends BaseDomain {

    private static final long serialVersionUID = 1L;

    private User user;

    private String action;

    @IgnoreNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss" ,shape = JsonFormat.Shape.ANY , timezone = "GMT+8")
    private Date time;

    private String type;

    private String detail;

    private String ip;

    @Transient
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public Log(){}

    public Log(Long userId, String action) {
        this(Constants.OPERATE_LOG, userId, action, action);
    }

    public Log(Long userId, String action, String detail) {
        this(Constants.OPERATE_LOG,userId, action, detail);
    }

    public Log(String type, Long userId, String action, String detail) {
        this.user = new User();
        this.user.setId(userId);
        this.action = action;
        this.type = type;
        this.detail = detail;
        this.ip = SessionContext.getRequest().getRemoteAddr();
    }

    public Map<String, Object> toMap() {
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("user", user);
        m.put("time", sdf.format(time));
        m.put("action", action);
        m.put("id", id);
        m.put("type", type);
        m.put("ip", ip);
        return m;
    }

    public Map<String, Object> toDetailMap() {
        Map<String, Object> m = toMap();
        m.put("detail", detail == null ? "" : new String(detail));
        return m;
    }

    /**
     * ********************properties getter and setter*******************************
     */

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    /***********************properties getter and setter********************************/

}
