package com.smm.ctrm.domain;

public class SqlParameter {

	private String parameterName;

	private ParameterDirection direction;

	private Object value;

	public SqlParameter() {
		super();
	}

	public SqlParameter(String parameterName, Object value) {
		this.parameterName = parameterName;
		this.direction = ParameterDirection.Input;
		this.value = value;
	}

	public SqlParameter(String parameterName, ParameterDirection direction, Object value) {
		this.parameterName = parameterName;
		this.direction = direction;
		this.value = value;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public ParameterDirection getDirection() {
		return direction;
	}

	public void setDirection(ParameterDirection direction) {
		this.direction = direction;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

}
