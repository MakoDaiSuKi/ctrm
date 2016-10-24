

package com.smm.ctrm.domain.Log;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class ErrorLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723561189L;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 调用方法名称
	 */
	@Column(name = "Method")
	@JsonProperty(value = "Method")
	private String Method;
	/**
	 * 出错或者警告信息
	 */
	@Column(name = "Message")
	@JsonProperty(value = "Message")
	private String Message;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getMethod(){
		return Method;
	}
	public void setMethod(String Method){
		this.Method=Method;
	}
	
	public String getMessage(){
		return Message;
	}
	public void setMessage(String Message){
		this.Message=Message;
	}

}