package org.moon.log.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.reeham.component.ddd.model.ModelContainer;
import com.reeham.component.ddd.model.ModelUtils;
import org.moon.core.orm.mybatis.DataConverter;
import org.moon.log.domain.Log;
import org.moon.log.service.LogService;
import org.moon.message.WebResponse;
import org.moon.rbac.domain.annotation.MenuMapping;
import org.moon.rest.annotation.Get;
import org.moon.utils.ParamUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Resource
    ModelContainer modelContainer;
	/**
	 * 查看日志
	 * @return
	 */
	@Get("")
	@MenuMapping(code="platform_6",name="日志列表",parentCode="platform",url="/log")
	public ModelAndView showPage(){
		return new ModelAndView("pages/log/log");
	}

	@Get("/list")
	public @ResponseBody WebResponse list(HttpServletRequest request){
		DataConverter<Log> converter = new DataConverter<Log>() {
			@Override
			public Object convert(Log t) {
				return t.toMap();
			}
		};
		return WebResponse.build().setResult(logService.listForPage(ParamUtils.getParamsAsCerteria(request),converter));
	}

	/**
	 * 根据日志id获取日志详情
	 * @param id
	 * @return
	 */
	@Get("/get/{id}")
	public @ResponseBody WebResponse getLogDetail(@PathVariable("id")Long id){
		return WebResponse.build().setResult(logService.get(id).toDetailMap());
	}
}
