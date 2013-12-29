package org.antstudio.blog.action;

import java.sql.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.antstudio.blog.domain.Registration;
import org.antstudio.moon.rest.annotation.Post;
import org.antstudio.rbac.domain.User;
import org.antstudio.rbac.domain.annotation.MenuMapping;
import org.antstudio.rbac.service.UserService;
import org.antstudio.utils.MessageUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.reeham.component.ddd.model.ModelContainer;

/**
 * 签到处理
 * @author Gavin
 * @date 2013-12-29
 */
@Controller
public class RegistrationAction {

	@Resource
	private ModelContainer container;
	@Resource
	private UserService userService;
	
	@RequestMapping("/blog/registration")
	@MenuMapping(code="br001",name="每日签到",url="/blog/registration",parentCode="pmanage")
	public ModelAndView showRegistrationPage(){
		return new ModelAndView("/pages/blog/registration");
	}
	

	@Post("/blog/registration/save")
	public @ResponseBody Map<String,Object> registrate(@RequestParam("type")int type,HttpServletRequest request){
		User currentUser = userService.getCurrentUser(request);
		Registration registration = new Registration();
		registration.setTimeType(type);
		if(currentUser.isSystemUser()){
			registration.setUserId(-1L);
		}else{
			registration.setUserId(currentUser.getId());
		}
		registration.setTime(new Date(System.currentTimeMillis()));
		container.enhanceModel(registration);
		registration.save();
		return MessageUtils.getMapMessage(true);
	}
	
}
