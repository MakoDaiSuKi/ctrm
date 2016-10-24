

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "FeeSetup", schema="Basis")

public class FeeSetup extends HibernateEntity {
	private static final long serialVersionUID = 1461832991323L;
	/**
	 * 抬头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	/**
	 * 现货交易方向 {B, S} (Dict)
	 */
	@Column(name = "SpotDirection", length = 2)
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 费用类型
	 */
	@Column(name = "FeeType", length = 30)
	@JsonProperty(value = "FeeType")
	private String FeeType;
	/**
	 *     装运地点
	 */
	@Column(name = "Loading", length = 64)
	@JsonProperty(value = "Loading")
	private String Loading;
	/**
	 *     缺货地点
	 */
	@Column(name = "Discharging", length = 64)
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 *     计费方式
	 */
	@Column(name = "CalculateType", length = 30)
	@JsonProperty(value = "CalculateType")
	private String CalculateType;
	/**
	 *     单价
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 价差,目的地点运费单价 -目的地为上海的运费单价
	 */
	@Column(name = "PriceDiff")
	@JsonProperty(value = "PriceDiff")
	private BigDecimal PriceDiff;
	
	public String getLegalId(){
		return LegalId;
	}
	public void setLegalId(String LegalId){
		this.LegalId=LegalId;
	}
	
	public String getSpotDirection(){
		return SpotDirection;
	}
	public void setSpotDirection(String SpotDirection){
		this.SpotDirection=SpotDirection;
	}
	
	public String getFeeType(){
		return FeeType;
	}
	public void setFeeType(String FeeType){
		this.FeeType=FeeType;
	}
	
	public String getLoading(){
		return Loading;
	}
	public void setLoading(String Loading){
		this.Loading=Loading;
	}
	
	public String getDischarging(){
		return Discharging;
	}
	public void setDischarging(String Discharging){
		this.Discharging=Discharging;
	}
	
	public String getCalculateType(){
		return CalculateType;
	}
	public void setCalculateType(String CalculateType){
		this.CalculateType=CalculateType;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getPriceDiff(){
		return PriceDiff;
	}
	public void setPriceDiff(BigDecimal PriceDiff){
		this.PriceDiff=PriceDiff;
	}

}