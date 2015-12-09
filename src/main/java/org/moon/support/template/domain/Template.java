package org.moon.support.template.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reeham.component.ddd.annotation.Model;
import org.moon.base.domain.BaseDomain;
import org.moon.rbac.domain.User;

import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 模板
 * @author:Gavin
 * @date 2015/6/16 0016
 */
@Model
@Table(name="tab_template")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Template extends BaseDomain{
    private String name;
    private String content;
    private LocalDateTime time;
    private LocalDateTime lastUpdateTime;
    private User user;
    private String cover;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public LocalDateTime getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(LocalDateTime lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}


