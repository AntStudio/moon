package org.moon.test.rest;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringJUnit4ClassRunner.class)  
@WebAppConfiguration  
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring-servlet.xml","file:src/main/webapp/WEB-INF/applicationContext.xml" })  
public class RestTest {

	@Autowired  
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	 
	@Before
	public  void init(){
		 this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	
	@Test
	public void getTest() throws Exception{
		System.out.println(mockMvc.perform(get("/getTest")).andReturn().getResponse().getContentAsString());
	}
	
	@Test
	public void postTest() throws Exception{
		System.out.println(mockMvc.perform(post("/postTest")).andReturn().getResponse().getContentAsString());
	}
	
	
	@Test
	public void putTest() throws Exception{
		System.out.println(mockMvc.perform(put("/putTest")).andReturn().getResponse().getContentAsString());
	}
	
	@Test
	public void deleteTest() throws Exception{
		System.out.println(mockMvc.perform(delete("/deleteTest")).andReturn().getResponse().getContentAsString());
	}
}
	
