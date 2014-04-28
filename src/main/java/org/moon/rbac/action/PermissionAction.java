package org.moon.rbac.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.service.PermissionService;
import org.moon.utils.MessageUtils;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	@MenuMapping(name="权限管理",url="/permission",code="platform_3",parentCode="platform")
	public ModelAndView showPermissionManagePage(){
		return new ModelAndView("pages/rbac/permission");
	}
	
	/**
	 * 获取所有的权限列表
	 * @return
	 */
	@RequestMapping("/getPermissionData")
	@ResponseBody
	public  Map<String,Object> getPermissionData(HttpServletRequest request){
		Map<String,Object> paramsMap = ParamUtils.getParamsMap(request);
		
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
    public Map<String, Object> assignPermission(@RequestParam("pids") Long[] pids,
                            @RequestParam("status") Boolean[] status, @RequestParam("rids") Long[] rids) {
        permissionService.assignPermission(pids, status, rids);
        return MessageUtils.getMapMessage(true);
    }
	
}
