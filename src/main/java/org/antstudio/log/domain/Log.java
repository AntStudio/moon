package org.antstudio.log.domain;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.antstudio.base.domain.BaseDomain;
import org.antstudio.support.session.SessionContext;
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

	private static final long serialVersionUID = 1L;

	private String userName;
	
	private Long userId;
	
	private String action;
	
	private Date time;
	
	private String type;
	
	private byte[] detail;
	
	private String ip;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
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
		this.ip = SessionContext.getRequest().getRemoteAddr();
	}
	
	public Map<String,Object> toMap(){
		Map<String,Object> m = new HashMap<String,Object>();
		m.put("user_name", userName);
		m.put("user_id", userId);
		m.put("time",sdf.format(time));
		m.put("action",  action);
		m.put("id", id);
		m.put("type", type);
		m.put("ip", ip);
		return m;
	}
	
	public Map<String,Object> toDetailMap(){
		Map<String,Object> m = toMap();
		m.put("detail", detail==null?"":new String(detail));
		return m;
	}
	
	
	/***********************properties getter and setter********************************/
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Date getTime() {
		return time;
	}

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
	
	/***********************properties getter and setter********************************/
	
}
