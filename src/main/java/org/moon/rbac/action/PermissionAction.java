package org.moon.rbac.action;

import org.moon.core.domain.DomainLoader;
import org.moon.core.orm.mybatis.Criteria;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.Role;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rbac.service.PermissionService;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.utils.Maps;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
	@Resource
	private DomainLoader domainLoader;
	
	@Get("")
	@MenuMapping(name="权限管理",url="/permission",code="platform_3",parentCode="platform")
	public ModelAndView showPermissionManagePage(){
		return new ModelAndView("pages/rbac/permission");
	}
	
	/**
	 * 获取所有的权限列表
	 * @return
	 */
	@Get("/getPermissionData")
	public @ResponseBody WebResponse list(HttpServletRequest request){
		Criteria criteria = ParamUtils.getParamsAsCerteria(request);
		return WebResponse.build().setResult(permissionService.listForPage(criteria));
	}
	
	
	/**
	 * 根据角色获取相应的权限
	 * @param rid
	 * @return
	 */
	@Get("/getPermissionDataByRole")
	public  @ResponseBody WebResponse getPermissionData(@RequestParam("rid")Long rid,HttpServletRequest request){
		final Role role = domainLoader.load(Role.class, rid);
		DataConverter<Map> dto = new DataConverter<Map>() {
			@Override
			public Object convert(Map p) {
				return Maps.mapIt("id"      , p.get("id"),
						  		  "name"    , p.get("name"),
						  		  "code"    , p.get("code"),
						  		  "checked" , role.hasPermission((String)p.get("code")));
			}
		};
		
		return WebResponse.build().setResult(permissionService.listForPage(ParamUtils.getParamsAsCerteria(request), dto));
	}
	
	/**
	 * 分配权限
	 * @param pids
	 * @param rids
	 * @return
	 */
	@Post("/assignPermission")
    public @ResponseBody WebResponse assignPermission(@RequestParam("pids") Long[] pids,
                            @RequestParam("status") Boolean[] status, @RequestParam("rids") Long[] rids) {
        permissionService.assignPermission(pids, status, rids);
        return WebResponse.build();
    }
	
}
