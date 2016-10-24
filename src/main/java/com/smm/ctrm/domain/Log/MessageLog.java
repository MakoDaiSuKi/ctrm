

package com.smm.ctrm.domain.Log;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "MessageLog")

public class MessageLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723934977L;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreaterId")
	@JsonProperty(value = "CreaterId")
	private String CreaterId;
	/**
	 * 创建者姓名
	 */
	@Column(name = "CreaterName")
	@JsonProperty(value = "CreaterName")
	private String CreaterName;
	/**
	 * 操作的是哪条数据
	 */
	@Column(name = "DataId")
	@JsonProperty(value = "DataId")
	private String DataId;
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
	 * 信息描述。等同于result.Message的消息，保存在此处。
	 */
	@Column(name = "MessageInfo")
	@JsonProperty(value = "MessageInfo")
	private String MessageInfo;
	
	public String getCreaterId(){
		return CreaterId;
	}
	public void setCreaterId(String CreaterId){
		this.CreaterId=CreaterId;
	}
	
	public String getCreaterName(){
		return CreaterName;
	}
	public void setCreaterName(String CreaterName){
		this.CreaterName=CreaterName;
	}
	
	public String getDataId(){
		return DataId;
	}
	public void setDataId(String DataId){
		this.DataId=DataId;
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
	
	public String getMessageInfo(){
		return MessageInfo;
	}
	public void setMessageInfo(String MessageInfo){
		this.MessageInfo=MessageInfo;
	}

}