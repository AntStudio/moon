package org.moon.rbac.action;

import org.moon.base.action.BaseAction;
import org.moon.core.session.SessionContext;
import org.moon.core.spring.annotation.FormParam;
import org.moon.log.domain.Log;
import org.moon.log.helper.LogType;
import org.moon.log.service.LogService;
import org.moon.maintenance.service.SystemSettingService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.domain.annotation.WebUser;
import org.moon.rbac.helper.Permissions;
import org.moon.rbac.repository.UserRepository;
import org.moon.rbac.service.RoleService;
import org.moon.rbac.service.UserService;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.rest.annotation.Put;
import org.moon.support.token.Token;
import org.moon.support.token.service.TokenService;
import org.moon.utils.Maps;
import org.moon.utils.Objects;
import org.moon.utils.ParamUtils;
import org.moon.utils.Tokens;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-12-2
 */
@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction {

    @Resource
    private UserService userService;

    @Resource
    private SystemSettingService systemSettingService;

    @Resource
    private RoleService roleService;

    @Resource
    private TokenService tokenService;

    @Resource
    private LogService logService;
    /**
     * 显示登录页面
     *
     * @return
     */
    @Get("/login")
    public ModelAndView showLoginPage(@RequestParam(value = "from", defaultValue = "") String from) {
        return new ModelAndView("pages/login", "from", from);
    }

    /**
     * 显示用户列表页面
     * @param request
     * @return
     */
    @Get("")
    @MenuMapping(url = "/user", name = "用户列表", code = "platform_1", parentCode = "platform")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    public ModelAndView userList(HttpServletRequest request) {
        return new ModelAndView("pages/rbac/userList");
    }

    /**
     * 登录验证
     *
     * @return
     */
    @Post("/login/validate")
    @ResponseBody
    public WebResponse userValidate(@RequestParam("userName") String userName,
                                    @RequestParam("password") String password,
                                    HttpServletRequest request) {
        User user = new User();
        user.setUserName(userName);
        user.setPassword(password);
        user.setId(null);//设置id为null，此后用id是否为空判断是否成功登录
        user = userService.login(user);
        if (Objects.isNull(user) || Objects.isNull(user.getId())) {
            new Log(-1L, "登录失败", "{userName:" + userName + ",password:" + password + "}").save();
            logService.log(LogType.LOGIN,"登录失败","{userName:" + userName + ",password:" + password + "}");
            return WebResponse.build().setSuccess(false);
        } else {
            request.getSession().setAttribute(User.CURRENT_USER_ID, user.getId());
            logService.log(LogType.LOGIN,"登录成功","{userId:" + user.getId()+"}");
            Token token = new Token();
            token.setToken(Tokens.getTokenString(userName, password));
            token.expiryIn(30, Calendar.DATE);
            token.setUserId(user.getId());
            tokenService.addOrUpdateToken(token);
            userService.loginRecord(user.getId(),SessionContext.getRequest().getRemoteAddr());
            return WebResponse.success(token.getToken());
        }
    }

    /**
     * 获取用户列表
     * @param request
     * @return
     */
    @Get("/~/list")
    @ResponseBody
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    public WebResponse getUsersList(HttpServletRequest request) {
        Map params = ParamUtils.getParamsMapForPager(request);
        params.put("delete_flag", false);
        return WebResponse.build().setResult(userService.listForPage(UserRepository.class, "listWithRole", params));
    }

    /**
     * 添加用户
     * @param creator
     * @param user
     * @return
     */
    @Post("/~/add")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse add(@WebUser User creator, @FormParam(value = "user") User user) {
        user.setCreateBy(creator.getId());
        user.sync(user.encryptPassword().save());
        return WebResponse.build().setSuccess(true);
    }

    @Get("/~/get/{id}")
    @ResponseBody
    public WebResponse getUser(@PathVariable("id") Long id) {
        return WebResponse.build().setResult(userService.get(id).toAllMap());
    }

    @Post("/~/update")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse updateUser(@FormParam("user") User user) {
        User oldUser = userService.get(user.getId());
        oldUser.setUserName(user.getUserName());
        oldUser.setRealName(user.getRealName());
        oldUser.setPassword(user.getPassword());
        oldUser.sync(oldUser.encryptPassword().update());
        return WebResponse.build();
    }

    @Post("/~/logicDelete")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse logicDeleteUser(@RequestParam("ids") Long[] ids) {
        userService.delete(ids, true);
        return WebResponse.build();
    }

    /**
     * 获取用户所在的角色路径
     *
     * @return
     */
    @Get("/~/getRolePath")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse getRolePath(@RequestParam("uid") Long uid) {
        String rolePath = "";
        Role role = userService.get(uid).getRole();
        if (role != null) {
            rolePath = role.getRolePath();
        }
        return WebResponse.build().setResult(rolePath);
    }

    /**
     * 登出操作
     *
     * @return
     */
    @Get("/loginOut")
    public ModelAndView loginOut(HttpServletRequest request) {
        request.getSession().setAttribute(User.CURRENT_USER_ID, null);
        return new ModelAndView("pages/login");
    }

    @LoginRequired
    @Get("/~/changePassword")
    @MenuMapping(code = "platform_5", name = "修改密码", url = "/user/~/changePassword", parentCode = "platform")
    public ModelAndView showChangePasswordPage(@WebUser User user) {
        String info = null;
        if (user.isSysUser()) {
            info = "对不起，系统管理员不能修改密码.";
        }
        return new ModelAndView("pages/rbac/changePassword", "info", info);
    }

    /**
     * 密码匹配,用于密码修改
     *
     * @param newPassword
     * @return
     */
    @LoginRequired
    @ResponseBody
    @Get("/~/matchOldPassword")
    public WebResponse matchOldPassword(@WebUser User user, @RequestParam("password") String newPassword) {
        if (user.getPassword().equals(newPassword)) {
            return WebResponse.build().setSuccess(true);
        } else {
            return WebResponse.build().setSuccess(false);
        }
    }

    /**
     * 修改密码
     *
     * @param user
     * @param newPassword
     * @return
     */
    @LoginRequired
    @Post("/~/doChangePassword")
    @ResponseBody
    public WebResponse changePassword(@WebUser User user, @RequestParam("password") String newPassword) {
        if (user.isSysUser()) {
            return WebResponse.build().setSuccess(false);
        }
        user.setPassword(newPassword);
        user.sync(user.encryptPassword().update());
        return WebResponse.build().setSuccess(true);
    }

    /**
     * 检查用户名是否存在
     *
     * @param userName
     * @return
     */
    @LoginRequired
    @Post("/~/checkUserName")
    @ResponseBody
    public WebResponse checkUserName(@RequestParam("userName") String userName) {
        return WebResponse.build().setResult(userService.isUserNameExists(userName));
    }


    /**
     * 获取系统角色和用户角色配置
     *
     * @return
     */
    @Get("/~/role/list")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse getRoles() {
        Map<String, Object> result = new HashMap<>();
        systemSettingService.getSettingMap("role.").forEach(result::put);
        result.put("roles", roleService.getAllRoles());
        return WebResponse.build().setResult(result);
    }

    /**
     * 更新用户类型和角色的关系
     *
     * @return
     */
    @Post("/~/role/update")
    @PermissionMapping(code = Permissions.USER_MANAGEMENT, name = Permissions.USER_MANAGEMENT_DESCRIPTION)
    @ResponseBody
    public WebResponse updateUserRoleSetting(HttpServletRequest request) {
        Map<String, Object> params = ParamUtils.getParamMapFromRequest(request);
        Map<String, Object> userTypeRoleRel = new HashMap<String, Object>();
        for (String key : params.keySet()) {
            if (key.indexOf("userType") != -1) {
                userTypeRoleRel.put("role.".concat(key), params.get(key));
            }
        }
        systemSettingService.updateSetting(userTypeRoleRel);
        return WebResponse.build();
    }

    @Put("/~/tester")
    @ResponseBody
    public WebResponse updateUserTesterStatus(@RequestBody User user){
        User userFromDB = userService.get(user.getId());
        userFromDB.setIsTester(user.isTester());
        sync(userFromDB.update());
        return WebResponse.success();
    }
}
