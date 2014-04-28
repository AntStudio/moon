package org.moon.test.json;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Gavin
 * @date 2013-12-14 下午10:12:43
 */
@Controller
public class TestController {

	@RequestMapping("/test")
	public @ResponseBody s test(){
		System.out.println("测试执行了");
		return new s("你好");
	}
	
	
}


class s{
	private String id;
	
	public s(String id){
		this.id = id;
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
	}
}