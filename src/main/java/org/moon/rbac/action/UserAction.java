package org.moon.rbac.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.moon.base.action.BaseAction;
import org.moon.log.domain.Log;
import org.moon.log.service.LogService;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.User;
import org.moon.rbac.domain.annotation.LoginRequired;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.domain.annotation.PermissionMapping;
import org.moon.rbac.service.UserService;
import org.moon.rest.annotation.Post;
import org.moon.support.spring.annotation.FormParam;
import org.moon.utils.MessageUtils;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Gavin
 * @version 1.0
 * @date 2012-12-2
 */
@Controller
@RequestMapping("/user")
public class UserAction extends BaseAction{

	@Resource 
	private UserService userService;
	
	@Resource
	private LogService logService;
	/**
	 * 显示登录页面
	 * @return
	 */
	@RequestMapping(value = "/login")
	public ModelAndView login(@RequestParam("from") String from){
		 return new ModelAndView("pages/login","from",from);
	}
	 
	/**
	 * 登录验证
	 * @param user
	 * @param role
	 * @return
	 */
	 @RequestMapping(value="/login/validate")
	 @ResponseBody
	 public Map<String,Object> userValidate(@FormParam(value="user")User user,HttpServletRequest request){
		 if(user==null){
			 return MessageUtils.getMapMessage(false);
		 }
		 String loginName = user.getUserName();
		 String password = user.getPassword();
		 user.setId(null);//设置id为null，此后用id是否为空判断是否成功登录
		 user = userService.login(user);
		 if(user==null||user.getId()==null){
			logService.log(new Log(loginName,-1L,"登录失败","{userName:"+loginName+",password:"+password+"}"));
			return MessageUtils.getMapMessage(false);
		 }else{
			 request.getSession().setAttribute(User.CURRENT_USER_ID, user.getId());
			 logService.log(new Log(user.getUserName(),user.getId(),"成功登录系统"));
			 return MessageUtils.getMapMessage(true);
		 }
	 }
	 
	 @MenuMapping(url="/user",name="用户列表",code="platform_1",parentCode="platform")
	 @RequestMapping("")
	 @PermissionMapping(code="000008",name="用户列表")
	 public ModelAndView userList(HttpServletRequest request){
		 return new ModelAndView("pages/rbac/userList");
	 }
	 
	 @PermissionMapping(code="000001",name="用户列表")
	 @RequestMapping("/getUsersData")
	 @ResponseBody
	 public Map<String,Object> getUsersData(HttpServletRequest request){
		 Map<String,Object> paramsMap = ParamUtils.getParamsMap(request);
		 paramsMap.put("uid", userService.getCurrentUserId(request));
		 paramsMap.put("deleteFlag", false);
		 return  userService.getUsersByCreatorForPager(paramsMap).toMap();
	 }
	 
	 @RequestMapping("/add")
	 @ResponseBody
	 public Map<String,Object> addUser(@FormParam(value="user")User user,HttpServletRequest request){
		 user.setCreateBy(userService.getCurrentUserId(request));
		 user.encryptPassword();
		 enhance(user).save();
		 return MessageUtils.getMapMessage(true);
	 }
	 
	 @RequestMapping("/get")
	 @ResponseBody
	 public Map<String,Object> getUser(@RequestParam("id")Long id){
		 Map<String,Object> m = new HashMap<String,Object>();
		 m.put("user", userService.get(id).toAllMap());
		 return m;
	 }
	 
	 @RequestMapping("/update")
	 @ResponseBody
	 public Map<String,Object> updateUser(@FormParam("user") User user){
		 userService.update(user);
		 return MessageUtils.getMapMessage(true);
	 }
	 
	 @RequestMapping("/logicDelete")
	 @ResponseBody
	 public Map<String,Object> logicDeleteUser(@RequestParam("ids")Long[] ids){
		 userService.delete(ids, true);
		 return MessageUtils.getMapMessage(true);
		 
	 }
	 
	 /**
	  * 获取用户所在的角色路径
	  * @return
	  */
	 @RequestMapping("/getRolePath")
	 @ResponseBody
	 public Map<String,Object> getRolePath(@RequestParam("uid")Long uid){
		Map<String,Object> path = MessageUtils.getMapMessage(true);
		String rolePath = "";
		Role role = userService.get(uid).getRole();
		if(role!=null){
			rolePath = role.getRolePath();
		}
		path.put("path", rolePath);
		 return path;
	 }
	 
	 /**
	  * 登出操作
	  * @return
	  */
	 @RequestMapping("/loginOut")
	 public ModelAndView loginOut(HttpServletRequest request){
		 request.getSession().setAttribute(User.CURRENT_USER_ID, null);
		 return new ModelAndView("pages/login");
	 }
	 
	 @LoginRequired
	 @RequestMapping("/changePassword")
	 @MenuMapping(code="platform_5",name="修改密码",url="/user/changePassword",parentCode="platform")
	 public ModelAndView showChangePasswordPage(HttpServletRequest request){
		 String info = null ;
		 if(userService.isSysUser(userService.getCurrentUser(request))){
			 info = "对不起，系统管理员不能修改密码.";
		 }
		 return new ModelAndView("pages/rbac/changePassword","info",info);
	 }
	 
	 /**
	  * 密码匹配,用于密码修改
	  * @param newPassword
	  * @return
	  */
	 @LoginRequired
	 @RequestMapping("/matchOldPassword")
	 @ResponseBody
	 public Map<String,Object> matchOldPassword(@RequestParam("password")String newPassword,HttpServletRequest request){
		 if(userService.getCurrentUser(request).getPassword().equals(newPassword))
		 return MessageUtils.getMapMessage(true);
		 else
			 return MessageUtils.getMapMessage(false);
	 }
	 /**
	  * 修改密码
	  * @param newPassword
	  * @param request
	  * @return
	  */
	 @LoginRequired
	 @RequestMapping("/doChangePassword")
	 @ResponseBody
	 public Map<String,Object> changePassword(@RequestParam("password")String newPassword,HttpServletRequest request){
		 User user = userService.getCurrentUser(request);
		 if(user.isSystemUser()){
			 return MessageUtils.getMapMessage(false);
		 }
		 user.setPassword(newPassword);
		 user.updateUser();
		 return MessageUtils.getMapMessage(true);
	 }
	 
	 /**
	  * 检查用户名是否存在
	  * @param userName
	  * @return
	  */
	 @LoginRequired
	 @Post("/checkUserName")
	 @ResponseBody
	 public Map<String,Object> checkUserName(@RequestParam("userName")String userName){
		 return MessageUtils.getMapMessage(true,"userNameExists",userService.isUserNameExists(userName));
	 }
}
