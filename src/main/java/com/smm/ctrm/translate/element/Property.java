package com.smm.ctrm.translate.element;

import java.util.HashMap;
import java.util.Map;

public class Property {
	
	private String comment;
	
	private String annotion;
	
	private Map<String, String> annotionMap = new HashMap<>();

	private String type;
	
	private String column;
	
	private String reference;
	
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	private String property;

	public Map<String, String> getAnnotionMap() {
		return annotionMap;
	}

	public void setAnnotionMap(Map<String, String> annotionMap) {
		this.annotionMap = annotionMap;
	}
	
	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}

	
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getAnnotion() {
		return annotion;
	}

	public void setAnnotion(String annotion) {
		this.annotion = annotion;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	@Override
	public String toString() {
		return "Property [comment=" + comment + ", annotion=" + annotion + ", annotionMap=" + annotionMap + ", type="
				+ type + ", column=" + column + ", reference=" + reference + ", property=" + property + "]";
	}
}
