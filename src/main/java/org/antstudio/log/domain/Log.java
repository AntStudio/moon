package org.antstudio.log.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.antstudio.base.domain.BaseDomain;
import org.antstudio.log.domain.respository.LogEvent;
import org.antstudio.utils.Constants;

import com.reeham.component.ddd.annotation.Model;


/**
 * 日志领域
 * @author Gavin
 * @version 1.0
 * @date 2013-1-7
 */
@Model
public class Log extends BaseDomain{

	public Log(){}
	
	public Log(String action){
		this(action,action,Constants.OPERATE_LOG);
	}
	public Log(String action,String detail){
		this(action,detail,detail);
	}
	public Log(String action,String detail,String type){
		this.action = action;
		this.detail = detail.getBytes();
		this.type = type;
	}
	
	public Log(String userName,Long userId,String action){
		this(userName,userId,action,action,Constants.OPERATE_LOG);
	}
	
	public Log(String userName,Long userId,String action,String detail){
		this(userName,userId,action,detail,Constants.OPERATE_LOG);
	}
	
	public Log(String userName,Long userId,String action,String detail,String type){
		
		this.userName = userName;
		this.userId = userId;
		this.action = action;
		this.type = type;
		this.detail = detail.getBytes();
	}
	
	
	private String userName;
	
	private Long userId;
	
	private String action;
	
	private Date time;
	
	private String type;
	
	private byte[] detail;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	@Resource
	private LogEvent logEvent;
	
	/**
	 * 保存或更新操作
	 */
	public void saveOrUpdate(){
		logEvent.saveOrUpdate(this);	
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("user_name", userName);
		m.put("user_id", userId);
		m.put("time",sdf.format(time));
		m.put("action",  action);
		m.put("id", id);
		m.put("type", type);
		return m;
	}
	
	public Map<String,Object> toDetailMap(){
		Map<String,Object> m = toMap();
		m.put("detail", detail==null?"":new String(detail));
		return m;
	}
	/***********************properties getter and setter********************************/
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the time
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the detail
	 */
	public byte[] getDetail() {
		return detail;
	}

	/**
	 * @param detail the detail to set
	 */
	public void setDetail(byte[] detail) {
		this.detail = detail;
	}
	
	/***********************properties getter and setter********************************/
	
}
