

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Market", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Market extends HibernateEntity {
	private static final long serialVersionUID = 1461832991323L;
	public static final String MARK_TYPE_Physical = "P";
    public static final String MARK_TYPE_Futures = "F";
	/**
	 * 市场名称 
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 市场编码
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 市场类型，{期货，现货} (Dict)
	 */
	@Column(name = "MarketType", length = 30)
	@JsonProperty(value = "MarketType")
	private String MarketType;
	/**
	 * 货币种类 (Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 所在时区, eg EST 
	 */
	@Column(name = "TimeZone", length = 10)
	@JsonProperty(value = "TimeZone")
	private String TimeZone;
	/**
	 *  备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *  排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex")
	private Integer OrderIndex;
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getCode(){
		return Code;
	}
	public void setCode(String Code){
		this.Code=Code;
	}
	
	public String getMarketType(){
		return MarketType;
	}
	public void setMarketType(String MarketType){
		this.MarketType=MarketType;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
	}
	
	public String getTimeZone(){
		return TimeZone;
	}
	public void setTimeZone(String TimeZone){
		this.TimeZone=TimeZone;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	@JsonProperty(defaultValue="0")
	public Integer getOrderIndex(){
		return OrderIndex;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}

}