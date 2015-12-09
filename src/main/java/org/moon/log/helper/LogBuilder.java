package org.moon.log.helper;

import org.moon.log.domain.Log;
import org.moon.rbac.domain.User;

import java.util.Date;

/**
 * 日志构造器
 * @author:Gavin
 * @date 2015/6/9 0009
 */
public class LogBuilder {

    private Log log = new Log();

    public LogBuilder type(String type){
        log.setType(type);
        return this;
    }

    public LogBuilder userId(Long userId){
        User user = new User();
        user.setId(userId);
        log.setUser(user);
        return this;
    }

    public LogBuilder action(String action){
        log.setAction(action);
        return this;
    }

    public LogBuilder detail(String detail){
        log.setDetail(detail);
        return this;
    }

    public LogBuilder ip(String ip){
        log.setIp(ip);
        return this;
    }

    public Log build(){
        log.setTime(new Date());
        return log;
    }
}
