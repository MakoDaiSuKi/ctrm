package com.smm.ctrm.translate.element;

import java.util.ArrayList;
import java.util.List;

public class Domain {
	
	List<Property> entityList = new ArrayList<>();
	
	private String comment;
	
	private String table;
	
	private String schema;
	
	private String nameType;
	
	private String className;
	
	private String namespace;

	@Override
	public String toString() {
		return "Domain [entityList=" + entityList + ", comment=" + comment + ", table=" + table + ", schema=" + schema
				+ ", nameType=" + nameType + ", className=" + className + "]";
	}

	public List<Property> getEntityList() {
		return entityList;
	}

	public void setEntityList(List<Property> entityList) {
		this.entityList = entityList;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
