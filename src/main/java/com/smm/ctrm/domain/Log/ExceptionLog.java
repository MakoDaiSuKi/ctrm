

package com.smm.ctrm.domain.Log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "ExceptionLog")

public class ExceptionLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723934976L;
	/**
	 * 异常信息内容
	 */
	@Lob
	@Column(name = "ExceptionMessage")
	@JsonProperty(value = "ExceptionMessage")
	private String ExceptionMessage;
	/**
	 * 操作的是张表
	 */
	@Column(name = "ClassName")
	@JsonProperty(value = "ClassName")
	private String ClassName;
	/**
	 * 操作名称或方法名。原来的名称是OperateName。得构以后名称是MethodName
	 */
	@Column(name = "MethodName")
	@JsonProperty(value = "MethodName")
	private String MethodName;
	/**
	 *
	 */
	@Column(name = "LevelName", length = 50)
	@JsonProperty(value = "LevelName")
	private String LevelName;
	
	public String getExceptionMessage(){
		return ExceptionMessage;
	}
	public void setExceptionMessage(String ExceptionMessage){
		this.ExceptionMessage=ExceptionMessage;
	}
	
	public String getClassName(){
		return ClassName;
	}
	public void setClassName(String ClassName){
		this.ClassName=ClassName;
	}
	
	public String getMethodName(){
		return MethodName;
	}
	public void setMethodName(String MethodName){
		this.MethodName=MethodName;
	}
	
	public String getLevelName(){
		return LevelName;
	}
	public void setLevelName(String LevelName){
		this.LevelName=LevelName;
	}

}