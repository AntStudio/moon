package org.moon.message;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.moon.exception.ApplicationRunTimeException;

/**
 * 返回数据类型,主要用于json数据类型
 *
 * @author Gavin
 * @date 2014-05-08
 */
public class WebResponse {

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

    public WebResponse() {
    }

    public static WebResponse build() {
        return new WebResponse();
    }

    public static WebResponse fail(Object result) {
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(false);
        webResponse.setResult(result);
        return webResponse;
    }

    public static WebResponse success(Object result) {
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(true);
        webResponse.setResult(result);
        return webResponse;
    }

    public static WebResponse success() {
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(true);
        return webResponse;
    }

    public static WebResponse fail() {
        WebResponse webResponse = new WebResponse();
        webResponse.setSuccess(false);
        return webResponse;
    }

    public WebResponse setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public WebResponse setResult(Object result) {
        this.result = result;
        return this;
    }

    public WebResponse setThrowable(Throwable throwable) {
        this.throwable = throwable;
        return this;
    }

    public WebResponse setPermission(boolean permission) {
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

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new ApplicationRunTimeException(e);
        }
    }
}
