package org.moon.log.action;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.moon.log.service.LogService;
import org.moon.rbac.domain.annotation.MenuMapping;
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
 * @date 2013-1-7
 */
@Controller
@RequestMapping("/log")
public class LogAction {

	@Resource
	private LogService logService;
	/**
	 * 查看日志
	 * @return
	 */
	@RequestMapping("")
	@MenuMapping(code="platform_6",name="日志列表",parentCode="platform",url="/log")
	public ModelAndView showLogPage(){
		return new ModelAndView("pages/log/log");
	}
	
	@RequestMapping("/getLogsData")
	@ResponseBody
	public Map<String,Object> getLogsData(HttpServletRequest request){
		return logService.getLogsForPage(ParamUtils.getParamsMap(request)).toMap();
	}
	
	/**
	 * 根据日志id获取日志详情
	 * @param id
	 * @return
	 */
	@RequestMapping("/getLogDetail")
	@ResponseBody
	public Map<String,Object> getLogDetail(@RequestParam("id")Long id){
		return MessageUtils.getMapMessage(true, "log", logService.get(id).toDetailMap());
	}
}
