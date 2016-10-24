

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class R14 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	/**
	 *
	 */
	@JsonProperty(value = "MarketName")
	private String MarketName;
	/**
	 *
	 */
	@JsonProperty(value = "MarketId")
	private String MarketId;
	/**
	 *
	 */
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
	/**
	 *
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	/**
	 *
	 */
	@JsonProperty(value = "PromptDate")
	private String PromptDate;
	/**
	 *
	 */
	@JsonProperty(value = "SP")
	private BigDecimal SP;
	/**
	 *
	 */
	@JsonProperty(value = "MG")
	private BigDecimal MG;
	/**
	 *
	 */
	@JsonProperty(value = "HG")
	private BigDecimal HG;
	
	public String getMarketName(){
		return MarketName;
	}
	public void setMarketName(String MarketName){
		this.MarketName=MarketName;
	}
	
	public String getMarketId(){
		return MarketId;
	}
	public void setMarketId(String MarketId){
		this.MarketId=MarketId;
	}
	
	public String getCommodityCode(){
		return CommodityCode;
	}
	public void setCommodityCode(String CommodityCode){
		this.CommodityCode=CommodityCode;
	}
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public String getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(String PromptDate){
		this.PromptDate=PromptDate;
	}
	
	public BigDecimal getSP(){
		return SP;
	}
	public void setSP(BigDecimal SP){
		this.SP=SP;
	}
	
	public BigDecimal getMG(){
		return MG;
	}
	public void setMG(BigDecimal MG){
		this.MG=MG;
	}
	
	public BigDecimal getHG(){
		return HG;
	}
	public void setHG(BigDecimal HG){
		this.HG=HG;
	}

}