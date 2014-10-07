package org.moon.core.spring.config.exception;

import org.springframework.beans.BeansException;

/**
 * @author Gavin
 * @date 2014-4-30
 */
public class ConfigAutowireException extends BeansException{

	private static final long serialVersionUID = -8396282668264775392L;

	public ConfigAutowireException(String msg) {
		super(msg);
	}

	public ConfigAutowireException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
