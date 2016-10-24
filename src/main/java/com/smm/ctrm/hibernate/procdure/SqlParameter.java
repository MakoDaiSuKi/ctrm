package com.smm.ctrm.hibernate.procdure;

import javax.persistence.ParameterMode;

public class SqlParameter<T> {
	
	private String name;
	
	private ParameterMode mode;
	
	private Class<T> type;
	
	private T value;

	/**
	 * @param name
	 * @param value
	 * @param mode
	 */
	public SqlParameter(String name, T value, Class<T> type, ParameterMode mode) {
		super();
		this.name = name;
		this.mode = mode;
		this.value = value;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ParameterMode getMode() {
		return mode;
	}

	public void setMode(ParameterMode mode) {
		this.mode = mode;
	}

	public Class<T> getType() {
		return type;
	}

	public void setType(Class<T> type) {
		this.type = type;
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
	

}
