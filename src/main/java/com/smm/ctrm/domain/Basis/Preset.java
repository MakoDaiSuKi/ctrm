

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Preset", schema="Basis")

public class Preset extends HibernateEntity {
	private static final long serialVersionUID = 1461832991334L;
	/**
	 *  参数名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 *  参数关键字
	 */
	@Column(name = "_Key", length = 30)
	@JsonProperty(value = "Key")
	private String Key;
	/**
	 *  参数值
	 */
	@Column(name = "Value", length = 30)
	@JsonProperty(value = "Value")
	private String Value;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getKey(){
		return Key;
	}
	public void setKey(String Key){
		this.Key=Key;
	}
	
	public String getValue(){
		return Value;
	}
	public void setValue(String Value){
		this.Value=Value;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}