package com.greejoy.rbac.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.greejoy.rbac.domain.Permission;
import com.greejoy.rbac.domain.annotation.MenuMapping;
import com.greejoy.rbac.service.PermissionService;
import com.greejoy.utils.MessageUtils;
import com.greejoy.utils.ParamUtils;

/**
 * 权限控制类
 * @author Gavin
 * @version 1.0
 * @date 2012-12-14
 */
@Controller
@RequestMapping("/permission")
public class PermissionAction {

	@Resource
	private PermissionService permissionService;
	@RequestMapping("")
	@MenuMapping(name="权限管理",url="/permission",code="100001",parentCode="100000")
	public ModelAndView showPermissionManagePage(){
		return new ModelAndView("pages/rbac/permission");
	}
	
	/**
	 * 获取所有的权限列表
	 * @return
	 */
	@RequestMapping("/getPermissionData")
	@ResponseBody
	public  Map<String,Object> getPermissionData(){
		Map<String,Object> paramsMap = ParamUtils.getDefaultParamMap();
		
		return permissionService.getPermissionsForPage(paramsMap).toMap();
	}
	
	/**
	 * 根据角色获取相应的权限
	 * @param rid
	 * @return
	 */
	@RequestMapping("/getPermissionDataByRole")
	@ResponseBody
	public  Map<String,Object> getPermissionData(@RequestParam("rid")Long rid){
		Map<String,Object> paramsMap = ParamUtils.getDefaultParamMap();
		paramsMap.put("rid", rid);
		return permissionService.getPermissionsByRoleForPage(paramsMap).toMap();
	}
	
	/**
	 * 分配权限
	 * @param pids
	 * @param rid
	 * @return
	 */
	@RequestMapping("/assignPermission")
	@ResponseBody
   public Map<String,Object> assignPermission(@RequestParam("ids")Long[] pids,@RequestParam("status")Boolean[] status,@RequestParam("rid")Long rid){ 
	   permissionService.assignPermission(pids,status,rid);
	   return MessageUtils.getMapMessage(true);
   }
	
}
