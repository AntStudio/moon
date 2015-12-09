package org.moon.log.service;

import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.service.BaseDomainService;
import org.moon.log.domain.Log;


/**
 * 日志操作接口
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
public interface LogService extends BaseDomainService<Log> {

    /**
     * 存储日志
     * @param type 日志类型 @{@link com.heartbridge.log.LogType}
     * @param userId 用户ID
     * @param action 简要描述
     * @param detail 详情
     */
    public DomainMessage log(String type, Long userId, String action, String detail);

    /**
     * 存储日志(从会话中获取当前登录用户)
     * @param type 日志类型 @{@link com.heartbridge.log.LogType}
     * @param action 简要描述
     * @param detail 详情
     */
    public DomainMessage log(String type, String action, String detail);

    /**
     * 获取详细的日志
     * @param id
     * @return
     */
    public Log getDetail(Long id);
}
