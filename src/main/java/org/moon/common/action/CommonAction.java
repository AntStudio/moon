package org.moon.common.action;

import org.moon.rest.annotation.Get;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * 公共的控制器
 * @author Gavin
 * @date 2014-4-20 下午4:42:59
 */
@Controller
public class CommonAction {

	@Get("/turn")
	public ModelAndView turn(@RequestParam("page") String page){
		return new ModelAndView(page);
	}
	
}
