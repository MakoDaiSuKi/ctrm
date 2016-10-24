package com.smm.ctrm.domain;

import java.io.Serializable;

public class Token implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8477108737385802692L;

	/**
	 * key
	 */
	private String key;

	/**
	 * token
	 */
	private String token;

	/**
	 * 失效时间 单位：秒
	 */
	private long expire;

	public Token() {
		super();
	}

	public Token(String key, String token, long expire) {
		super();
		this.key = key;
		this.token = token;
		this.expire = expire;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpire() {
		return expire;
	}

	public void setExpire(long expire) {
		this.expire = expire;
	}

	@Override
	public String toString() {
		return "Token [key=" + key + ", token=" + token + ", expire=" + expire + "]";
	}
}
