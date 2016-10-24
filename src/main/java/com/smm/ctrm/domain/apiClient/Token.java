package com.smm.ctrm.domain.apiClient;

import org.springframework.http.HttpStatus;

public class Token {
	
	/**
	 * 正常
	 */
	public static final int NORMAL = 1;
	/**
	 * 不存在
	 */
	public static final int UNAUTHORIZED = 401;
	/**
	 * 已更新
	 */
	public static final int FORBIDDEN = 403;
	
	/**
	 * 是否有效
	 */
	private boolean isValid;
	
	/**
	 * 1 正常  2 不存在 3 已更新
	 */
	private int status;
	
	private HttpStatus httpStatus;

	public boolean getIsValid() {
		return isValid;
	}

	public void setIsValid(boolean isValid) {
		this.isValid = isValid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}
	
}
