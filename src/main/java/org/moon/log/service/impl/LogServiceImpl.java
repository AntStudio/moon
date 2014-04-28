package org.moon.log.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.moon.log.domain.Log;
import org.moon.log.repository.LogRepository;
import org.moon.log.service.LogService;
import org.moon.pagination.Pager;
import org.moon.rbac.domain.User;
import org.moon.rbac.repository.UserRepository;
import org.moon.support.session.SessionContext;
import org.moon.utils.Constants;
import org.springframework.stereotype.Service;

import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelLoader;
import com.reeham.component.ddd.model.ModelUtils;

@Service
public class LogServiceImpl implements LogService,ModelLoader{
	@Resource
	private ModelContainer modelContainer;
	@Resource
	private LogRepository logRepository;
	@Resource
	private UserRepository userRepository;
	@Override
	public Pager getLogsForPage(Map<String, Object> params) {
		
		return new Pager(logRepository.getLogs_count(params),getLogsForMap(params),params);
	}

	public List<Map<String,Object>> getLogsForMap(Map<String, Object> params){
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Log log:getLogs(params)){
			list.add(log.toMap());
		}
		return list;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Log> getLogs(Map<String, Object> params) {
		return modelContainer.identifiersToModels((List)logRepository.getLogs(params), Log.class, this);
	}
	
	@Override
	@OnEvent("log/get")
	public Log get(Long id) {
		return (Log) modelContainer.getModel(ModelUtils.asModelKey(Log.class, id));
	}

	@Override
	public Log load(Long id) {
		return logRepository.getLog(id);
	}

	@Override
	public Object loadModel(Object identifier) {
		return load((Long)identifier);
	}

	@Override
	public void log(Log log) {
		if(log.getUserId()==null||log.getUserName()==null)
		{
			final Object userId = SessionContext.getSession().getAttribute(User.CURRENT_USER_ID);
			if(userId==null){
				log.setUserId(-1L);
				log.setUserName("Not Login");
			}else{
				log.setUserId((Long)userId);
				User user = (User) modelContainer.getModel(ModelUtils.asModelKey(User.class, userId),new ModelLoader() {
					@Override
					public Object loadModel(Object identifier) {
						if(Constants.SYSTEM_USERID.equals(userId))//返回系统级用户
							return new User(){ 
								private static final long serialVersionUID = 1L;
							{
								 setUserName(Constants.SYSTEM_USERNAME);
								 setId(Constants.SYSTEM_USERID);
								 setUserName(Constants.SYSTEM_USERNAME);
								 setRoleId(Constants.SYSTEM_ROLEID);
								}};
						return userRepository.get((Long)identifier);//缓存中不存在该用户，从数据库中加载
					}
				});
				log.setUserName(user.getUserName());
			}
		}
		modelContainer.enhanceModel(log).save();
	}

}
