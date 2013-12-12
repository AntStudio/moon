package org.antstudio.rest.annotation;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(method=RequestMethod.GET)
public @interface Post {

	//String[] value();
	
}
