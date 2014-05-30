package org.moon.message;


/**
 * 返回数据类型,主要用于json数据类型
 * @author Gavin
 * @date 2014-05-08
 */
public class WebResponse{
	
	/**
	 * 执行状态是否成功
	 */
	private boolean success = true;
	
	/**
	 * 返回的结果集
	 */
	private Object result;
	
	/**
	 * 异常信息
	 */
	private Throwable throwable;
	
	/**
	 * 是否具有权限
	 */
	private boolean permission = true;
	
	public WebResponse(){}
	
	public static WebResponse build(){
		return new WebResponse();
	}
	
	public WebResponse setSuccess(boolean success){
		this.success = success;
		return this;
	}
	
	public WebResponse setResult(Object result){
		this.result = result;
		return this;
	}
	
	public WebResponse setThrowable(Throwable throwable){
		this.throwable = throwable;
		return this;
	}
	
	public WebResponse setPermission(boolean permission){
		this.permission = permission;
		return this;
	}

	public boolean isSuccess() {
		return success;
	}

	public Object getResult() {
		return result;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public boolean isPermission() {
		return permission;
	}
}
