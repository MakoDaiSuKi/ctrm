package com.smm.ctrm.util;

public enum ParameterType {

	DECIMAL("DECIMAL", "DECIMAL"), VARCHAR("VARCHAR", "VARCHAR");

	private String code;
	private String message;

	private ParameterType(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
