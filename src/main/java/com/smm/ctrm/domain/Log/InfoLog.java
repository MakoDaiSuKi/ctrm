

package com.smm.ctrm.domain.Log;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class InfoLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723934977L;
	/**
	 *
	 */
	@JsonProperty(value = "UserCode")
	private String UserCode;
	/**
	 *
	 */
	@JsonProperty(value = "UserName")
	private String UserName;
	/**
	 *
	 */
	@JsonProperty(value = "Position")
	private String Position;
	/**
	 *
	 */
	@JsonProperty(value = "Target")
	private String Target;
	/**
	 *
	 */
	@JsonProperty(value = "Type")
	private String Type;
	/**
	 *
	 */
	@JsonProperty(value = "Message")
	private String Message;
	/**
	 *
	 */
	@JsonProperty(value = "Date")
	private Date Date;
	
	public String getUserCode(){
		return UserCode;
	}
	public void setUserCode(String UserCode){
		this.UserCode=UserCode;
	}
	
	public String getUserName(){
		return UserName;
	}
	public void setUserName(String UserName){
		this.UserName=UserName;
	}
	
	public String getPosition(){
		return Position;
	}
	public void setPosition(String Position){
		this.Position=Position;
	}
	
	public String getTarget(){
		return Target;
	}
	public void setTarget(String Target){
		this.Target=Target;
	}
	
	public String getType(){
		return Type;
	}
	public void setType(String Type){
		this.Type=Type;
	}
	
	public String getMessage(){
		return Message;
	}
	public void setMessage(String Message){
		this.Message=Message;
	}
	
	public Date getDate(){
		return Date;
	}
	public void setDate(Date Date){
		this.Date=Date;
	}

}