package org.moon.rbac.service.impl;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.base.service.AbstractDomainService;
import org.moon.rbac.domain.User;
import org.moon.rbac.repository.UserRepository;
import org.moon.rbac.service.UserService;
import org.moon.utils.Constants;
import org.moon.utils.Objects;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * the implement of user Service
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Introduce("message")
@Service
public class UserServiceImpl extends AbstractDomainService<User> implements UserService {

    @Resource
    private UserRepository userRepository;
    
    @Override
    public User get(Long id) {
    	if(Constants.SYSTEM_USERID.equals(id)){
    		return getSysUser();
    	}
    	return super.get(id);
    }
    
    @Override
    public User login(User user) {
        if (Constants.SYSTEM_USERNAME.equals(user.getUserName())) {// to validate the system user
            return validateSysUser(user);
        }
        user.encryptPassword();
        
        Long userId = userRepository.login(user);
        if(Objects.isNull(userId)){
        	return user;
        }else{
        	return get(userId);
        }
    }

    @Override
    public User getCurrentUser(HttpServletRequest request) {
        return get(getCurrentUserId(request));
    }

    @Override
    public Long getCurrentUserId(HttpServletRequest request) {
        Object uid = request.getSession().getAttribute(User.CURRENT_USER_ID);
        if (uid == null) {
            return null;
        } else {
            return (Long) uid;
        }
    }

    private User validateSysUser(User user) {
        if (Constants.SYSTEM_PASSWORD.equals(user.getPassword())) {
            return getSysUser();
        } else {
            return null;
        }
    }

    private User getSysUser() {
        User sysUser = (User) modelContainer.getModel(ModelUtils.asModelKey(User.class, Constants.SYSTEM_USERID));
        if(sysUser==null){
        	 sysUser = new User();
             sysUser.setId(Constants.SYSTEM_USERID);
             sysUser.setUserName(Constants.SYSTEM_USERNAME);
             sysUser.setRoleId(Constants.SYSTEM_ROLEID);
             modelContainer.addModel(ModelUtils.asModelKey(User.class, Constants.SYSTEM_USERID), sysUser);
        }
        return sysUser;
    }

    @Override
    public void delete(Long[] ids, boolean logicDel) {
        User user;

        if (logicDel) {
            for (Long id : ids) {
                user = (User) modelContainer.getModel(ModelUtils.asModelKey(User.class, id));
                if (user != null) {
                    user.setDeleteFlag(true);// update the cache
                }
            }
            userRepository.logicDeleteUser(ids);
        } else {
            for (Long id : ids) {
                modelContainer.removeModel(ModelUtils.asModelKey(User.class, id));// remove the domain from cache
            }
            userRepository.deleteUser(ids);
        }
    }

	@Override
	public boolean isUserNameExists(String userName) {
		return userRepository.isUserNameExists(userName);
	}
}
