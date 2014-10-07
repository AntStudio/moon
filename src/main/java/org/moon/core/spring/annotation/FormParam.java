package org.moon.core.spring.annotation;

import java.lang.annotation.*;

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

	/**
	 * 参数名称
	 * @return
	 */
	String value();
	
	/**
	 * 当有文件类型时,是否自动保存
	 * @return
	 */
	boolean autoSave() default false;
	
	/**
	 * 文件保存的路径
	 * @return
	 */
	String savePath() default "";
	
	/**
	 * 是否从域模型缓存中获取
	 * @return
	 */
	boolean model() default false;
}
