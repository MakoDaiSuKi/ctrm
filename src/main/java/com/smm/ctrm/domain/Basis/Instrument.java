

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Instrument", schema="Basis")

public class Instrument extends HibernateEntity {
	private static final long serialVersionUID = 1461832991323L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "MarketName")
	private String MarketName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "MarketCode")
	private String MarketCode;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * {F = 期货，A = 均价, O = 期权}
	 */
	@Column(name = "ForwardType")
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	/**
	 * 合约名称 
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 合约代号
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 每手数量 
	 */
	@Column(name = "QuantityPerLot")
	@JsonProperty(value = "QuantityPerLot")
	private BigDecimal QuantityPerLot;
	/**
	 * 到期日。本表中只有market=SFE时才需要有到期日。 
	 */
	@Column(name = "PromptDate")
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	/**
	 *  
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 交易市场 
	 */
	@Column(name = "MarketId")
	@JsonProperty(value = "MarketId")
	private String MarketId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Market")
	private Market Market;
	/**
	 * 商品品种 
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	public String getMarketName(){
		return MarketName;
	}
	public void setMarketName(String MarketName){
		this.MarketName=MarketName;
	}
	
	public String getMarketCode(){
		return MarketCode;
	}
	public void setMarketCode(String MarketCode){
		this.MarketCode=MarketCode;
	}
	
	public String getCommodityCode(){
		return CommodityCode;
	}
	public void setCommodityCode(String CommodityCode){
		this.CommodityCode=CommodityCode;
	}
	
	public String getCommodityName(){
		return CommodityName;
	}
	public void setCommodityName(String CommodityName){
		this.CommodityName=CommodityName;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
	}
	
	public String getForwardType(){
		return ForwardType;
	}
	public void setForwardType(String ForwardType){
		this.ForwardType=ForwardType;
	}
	
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
	
	public BigDecimal getQuantityPerLot(){
		return QuantityPerLot;
	}
	public void setQuantityPerLot(BigDecimal QuantityPerLot){
		this.QuantityPerLot=QuantityPerLot;
	}
	
	public Date getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(Date PromptDate){
		this.PromptDate=PromptDate;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public String getMarketId(){
		return MarketId;
	}
	public void setMarketId(String MarketId){
		this.MarketId=MarketId;
	}
	
	public Market getMarket(){
		return Market;
	}
	public void setMarket(Market Market){
		this.Market=Market;
	}
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public Commodity getCommodity(){
		return Commodity;
	}
	public void setCommodity(Commodity Commodity){
		this.Commodity=Commodity;
	}

}