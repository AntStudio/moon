package org.antstudio.blog.domain;

import java.sql.Date;

import javax.annotation.Resource;

import org.antstudio.base.domain.BaseDomain;
import org.antstudio.blog.event.RegistrationEvent;

import com.reeham.component.ddd.annotation.Model;

/**
 * 签到领域模型
 * @author Gavin
 * @date 2013-12-29
 */
@Model
public class Registration extends BaseDomain{

	private static final long serialVersionUID = 1L;

	private Date time;
	
	private int timeType;

	private Long userId;
	
	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	
	public int getTimeType() {
		return timeType;
	}

	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}


	@Resource
	private RegistrationEvent registrationEvent;
	
	public void save(){
		registrationEvent.saveOrUpdate(this);
	}
	
}
