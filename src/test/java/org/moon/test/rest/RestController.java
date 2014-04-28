package org.moon.test.rest;

import org.moon.rest.annotation.Delete;
import org.moon.rest.annotation.Get;
import org.moon.rest.annotation.Post;
import org.moon.rest.annotation.Put;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RestController {

	@Post("/postTest")
	public @ResponseBody String postTest(){
		System.out.println("This is post Test");
		return "postTest";
	}
	
	@Get("/getTest")
	public @ResponseBody String getTest(){
		System.out.println("This is get Test");
		return "getTest";
	}
	
	
	@Put("/putTest")
	public @ResponseBody String putTest(){
		System.out.println("This is put Test");
		return "putTest";
	}
	
	
	@Delete("/deleteTest")
	public @ResponseBody String deleteTest(){
		System.out.println("This is delete Test");
		return "deleteTest";
	}
	
}
