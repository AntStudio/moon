package org.moon.rbac.action.test;

import org.moon.rbac.domain.annotation.MenuMapping;
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
	@MenuMapping(code="test_1",name="table测试",parentCode="test",url="/webComponentTest/table")
	public String tableTest(){
		
		return "pages/test/table";
	}
	
}
