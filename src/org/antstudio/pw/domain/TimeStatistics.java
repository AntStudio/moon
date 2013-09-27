package org.antstudio.pw.domain;

import javax.annotation.Resource;

import org.antstudio.base.domain.BaseDomain;
import org.antstudio.pw.action.repository.TimeStatisticsEvent;

import com.reeham.component.ddd.annotation.Model;

/**
 * 
 * @author Gavin
 * @date 2013-9-3 上午7:48:37
 */
@Model
public class TimeStatistics extends BaseDomain{

	private static final long serialVersionUID = 1L;

	private String time;
	
	private String type;

	private Long user_id;
	
	
	@Resource
	private TimeStatisticsEvent timeStatisticsEvent;
	
	public void saveOrUpdate(){
		System.out.println(timeStatisticsEvent+"..............");
		 timeStatisticsEvent.saveOrUpdate(this);
	}
	
	
	
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	
}
