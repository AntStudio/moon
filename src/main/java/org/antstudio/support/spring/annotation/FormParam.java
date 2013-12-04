package org.antstudio.support.spring.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * this annotation used for the @RequestMapping method,
 * use like this:
 * in the page there are parameters such as:user.id、user.userName、user.password and etc.
 * in the hanlder method: public ModelAndView userValidate(@FormParam(value="user")User user){}
 * then the user parameter would be made up with  the parameter which name start with 'user.'
 * @author Gavin
 * @version 1.0
 * @date 2012-12-3
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FormParam {

	String value();
	
	boolean model() default false;
}
