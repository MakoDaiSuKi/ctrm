package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "ImportTemplate", schema = "Basis")
public class ImportTemplate extends HibernateEntity {
	private static final long serialVersionUID = 1471922355719L;
	/**
	 * 入库的表名
	 */
	@Column(name = "ImportTableName")
	@JsonProperty(value = "ImportTableName")
	private String ImportTableName;
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

	public String getImportTableName() {
		return ImportTableName;
	}

	public void setImportTableName(String ImportTableName) {
		this.ImportTableName = ImportTableName;
	}

	public String getColumnName() {
		return ColumnName;
	}

	public void setColumnName(String ColumnName) {
		this.ColumnName = ColumnName;
	}

	public String getDBName() {
		return DBName;
	}

	public void setDBName(String DBName) {
		this.DBName = DBName;
	}

	public Boolean getIsNeedCheck() {
		return IsNeedCheck;
	}

	public void setIsNeedCheck(Boolean IsNeedCheck) {
		this.IsNeedCheck = IsNeedCheck;
	}

	public Boolean getIsNotNull() {
		return IsNotNull;
	}

	public void setIsNotNull(Boolean IsNotNull) {
		this.IsNotNull = IsNotNull;
	}

	public String getTableColumnName() {
		return TableColumnName;
	}

	public void setTableColumnName(String TableColumnName) {
		this.TableColumnName = TableColumnName;
	}

	public String getTableName() {
		return TableName;
	}

	public void setTableName(String TableName) {
		this.TableName = TableName;
	}

}