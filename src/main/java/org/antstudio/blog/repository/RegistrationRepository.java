package org.antstudio.blog.repository;

import java.util.List;
import java.util.Map;

import org.antstudio.blog.domain.Registration;
import org.apache.ibatis.annotations.Param;


/**
 * 
 * @author Gavin
 * @date 2013-12-29
 */
public interface RegistrationRepository {

	public void save(@Param("registration")Registration registration);
	
	public void update(@Param("registration")Registration registration);
	
	public List<Long> list(Map<String,Object> params);
	
}
