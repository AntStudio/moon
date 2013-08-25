package org.antstudio.rbac.action.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * webComponent测试类
 * @author Gavin
 * @date 2013-8-25 下午7:04:35
 */
@Controller()
@RequestMapping("/webComponentTest")
public class WebComponentTest {

	@RequestMapping("table")
	public String tableTest(){
		
		return "pages/test/table";
	}
	
}
