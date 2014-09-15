package org.moon.exception;

/**
 * 应用级别的RunTime异常
 * @author:Gavin
 * @date 9/15/2014
 */
public class ApplicationRunTimeException extends RuntimeException{

    public ApplicationRunTimeException() {
        super();
    }

    public ApplicationRunTimeException(String message) {
        super(message);
    }

    public ApplicationRunTimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationRunTimeException(Throwable cause) {
        super(cause);
    }
}
