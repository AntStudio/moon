package org.moon.test.json;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
/**
 * @author Gavin
 * @date 2013-12-15 下午1:58:49
 */


@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration  
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-servlet.xml","file:src/main/webapp/WEB-INF/applicationContext.xml" })  
public class JsonTest {

	@Autowired  
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	 
	@Before
	public  void init(){
		 this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	
	@Test
	public void test() throws Exception{
		System.out.println(mockMvc.perform(get("/test")).andReturn().getResponse().getContentAsString());
	}
}
