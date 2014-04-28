package org.moon.rbac.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.moon.pagination.Pager;
import org.moon.rbac.domain.User;
import org.moon.rbac.repository.UserRepository;
import org.moon.rbac.service.UserService;
import org.moon.utils.ClassPropertiesUtil;
import org.moon.utils.Constants;
import org.springframework.stereotype.Service;

import com.reeham.component.ddd.annotation.Introduce;
import com.reeham.component.ddd.annotation.OnEvent;
import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;

/**
 * the implement of user Service
 * 
 * @author Gavin
 * @version 1.0
 * @date 2012-11-29
 */
@Introduce("message")
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;
    @Resource
    public ModelContainer  modelContainer;

    @Override
    @OnEvent("user/get")
    public User get(Long id) {
        if (Constants.SYSTEM_USERID.equals(id)) {
            return (User) modelContainer.getModel(ModelUtils.asModelKey(User.class, id), this);
        }
        if (id == null || id <= 0)
            return null;
        return (User) modelContainer.getModel(ModelUtils.asModelKey(User.class, id), this);
    }

    @Override
    public User load(Long id) {
        if (Constants.SYSTEM_USERID.equals(id)) {
            return getSysUser();
        }
        return userRepository.get(id);
    }

    @Override
    public Object loadModel(Object identifier) {
        return load((Long) identifier);
    }

    @Override
    public User login(User user) {
        if (isSysUser(user)) {// to validate the system user
            return validateSysUser(user);
        }
        user.encryptPassword();
        Object loginUser = get(userRepository.login(user));
        if (loginUser != null)
            return (User) loginUser;
        else
            return user;

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

    @Override
    public boolean isSysUser(User user) {
        if (Constants.SYSTEM_USERNAME.equals(user.getUserName()))
            return true;
        else
            return false;
    }

    private User validateSysUser(User user) {
        if (Constants.SYSTEM_PASSWORD.equals(user.getPassword())) {
            return getSysUser();
        } else {
            return null;
        }
    }

    private User getSysUser() {
        User sysUser = new User();
        sysUser.setId(Constants.SYSTEM_USERID);
        sysUser.setUserName(Constants.SYSTEM_USERNAME);
        sysUser.setRoleId(Constants.SYSTEM_ROLEID);
        if (modelContainer.getModel(ModelUtils.asModelKey(User.class, Constants.SYSTEM_USERID)) == null)
            modelContainer.addModel(ModelUtils.asModelKey(User.class, Constants.SYSTEM_USERID), sysUser);
        return sysUser;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<User> getUsersByCreator(Map<String, Object> paramsMap) {
        return modelContainer.identifiersToModels((List) userRepository.getUsersByCreator(paramsMap), User.class, this);
    }

    @Override
    public List<Map<String, Object>> getUsersByCreatorForMap(Map<String, Object> paramsMap) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (User user : getUsersByCreator(paramsMap)) {
            list.add(user.toMap());
        }
        return list;
    }

    @Override
    public Pager getUsersByCreatorForPager(Map<String, Object> paramsMap) {

        return new Pager(userRepository.getUsersByCreatorCount(paramsMap), getUsersByCreatorForMap(paramsMap), paramsMap);
    }

    @Override
    public boolean update(User user) {
        if (user == null || user.getId() == null)
            return false;
        User oldUser = get(user.getId());
        oldUser = (User) ClassPropertiesUtil.copyProperties(user, oldUser, true, "userName", "password", "realName");
        oldUser.updateUser();
        return true;

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
