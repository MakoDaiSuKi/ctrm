package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "StorageTemplate", schema="Basis")
public class StorageTemplate extends HibernateEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3913105904669279473L;
	/**
	 * 字段中文名称
	 */
	@Column(name = "ColumnName")
	@JsonProperty(value = "ColumnName")
	private String ColumnName;

	/**
	 * 数据库对应字段名称
	 */
	@Column(name = "DBName")
	@JsonProperty(value = "DBName")
	private String DBName;

	/**
	 * 是否需要校验
	 */
	@Column(name = "IsNeedCheck")
	@JsonProperty(value = "IsNeedCheck")
	private Boolean IsNeedCheck;

	/**
	 * 是否必填
	 */
	@Column(name = "IsNotNull")
	@JsonProperty(value = "IsNotNull")
	private Boolean IsNotNull;

	/**
	 * ID字段所在表的名称字段
	 */
	@Column(name = "TableColumnName")
	@JsonProperty(value = "TableColumnName")
	private String TableColumnName;

	/**
	 * ID字段所在表
	 */
	@Column(name = "TableName")
	@JsonProperty(value = "TableName")
	private String TableName;

	public String getColumnName() {
		return ColumnName;
	}

	public void setColumnName(String columnName) {
		ColumnName = columnName;
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String dBName) {
		DBName = dBName;
	}

	public Boolean getIsNeedCheck() {
		return IsNeedCheck==null ? Boolean.FALSE : IsNeedCheck;
	}

	public void setIsNeedCheck(Boolean isNeedCheck) {
		IsNeedCheck = isNeedCheck;
	}

	public Boolean getIsNotNull() {
		return IsNotNull==null ? Boolean.FALSE : IsNotNull;
	}

	public void setIsNotNull(Boolean isNotNull) {
		IsNotNull = isNotNull;
	}

	public String getTableColumnName() {
		return TableColumnName;
	}

	public void setTableColumnName(String tableColumnName) {
		TableColumnName = tableColumnName;
	}

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String tableName) {
		TableName = tableName;
	}

}
