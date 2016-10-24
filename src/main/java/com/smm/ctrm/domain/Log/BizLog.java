

package com.smm.ctrm.domain.Log;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.User;
public class BizLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723561188L;
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
	 * 业务信息
	 */
	@Column(name = "Message")
	@JsonProperty(value = "Message")
	private String Message;
	/**
	 * 用户的标识
	 */
	@Column(name = "UserId")
	@JsonProperty(value = "UserId")
	private Long UserId;
//	@JsonBackReference("BizLog_User")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UserId", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "User")
	private User User;
	
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
	
	public Long getUserId(){
		return UserId;
	}
	public void setUserId(Long UserId){
		this.UserId=UserId;
	}
	
	public User getUser(){
		return User;
	}
	public void setUser(User User){
		this.User=User;
	}

}