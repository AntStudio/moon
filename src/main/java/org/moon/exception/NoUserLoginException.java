package org.moon.exception;

/**
 * 未登录异常
 * @author:Gavin
 * @date 9/15/2014
 */
public class NoUserLoginException extends ApplicationRunTimeException{

    public NoUserLoginException() {
        super();
    }

    public NoUserLoginException(String message) {
        super(message);
    }

    public NoUserLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoUserLoginException(Throwable cause) {
        super(cause);
    }
}
