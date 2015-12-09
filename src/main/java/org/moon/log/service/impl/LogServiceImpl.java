package org.moon.log.service.impl;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.annotation.Send;
import com.reeham.component.ddd.message.DomainMessage;
import org.moon.base.service.AbstractDomainService;
import org.moon.core.session.SessionContext;
import org.moon.log.domain.Log;
import org.moon.log.helper.LogBuilder;
import org.moon.log.repository.LogRepository;
import org.moon.log.service.LogService;
import org.moon.rbac.domain.User;
import org.moon.utils.Objects;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Introduce("message")
public class LogServiceImpl extends AbstractDomainService<Log> implements LogService {

    @Resource
    private LogRepository logRepository;

    @Override
    @Send("log/add")
    public DomainMessage log(String type, Long userId, String action, String detail) {
        Log log = new LogBuilder().type(type).userId(userId).action(action)
                .ip(SessionContext.getRequest().getRemoteAddr()).detail(detail).build();
        return new DomainMessage(log);
    }

    @Override
    @Send("log/add")
    public DomainMessage log(String type, String action, String detail) {
        Object userId = SessionContext.getSession().getAttribute(User.CURRENT_USER_ID);
        if(Objects.isNull(userId)){
            userId = -1L;
        }
        return log(type, (Long)userId, action, detail);
    }

    @OnEvent("log/add")
    public Log saveLog(Log log) {
        logRepository.save(log);
        return log;
    }

    @Override
    public Log getDetail(Long id) {
        return logRepository.getDetail(id);
    }
}
