

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class PositionApprove extends HibernateEntity {
	private static final long serialVersionUID = 1461719402485L;
	/**
	 * 估计出发日期 
	 */
	@JsonProperty(value = "ETDMonth")
	private String ETDMonth;
	/**
	 * 品牌
	 */
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 * 批次数量
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 * 数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *当日计划卖出建仓
	 */
	@JsonProperty(value = "PlanPositionQuantity")
	private BigDecimal PlanPositionQuantity;
	/**
	 *销售点价
	 */
	@JsonProperty(value = "PricePositionQuantity")
	private BigDecimal PricePositionQuantity;
	/**
	 *套保卖出平仓
	 */
	@JsonProperty(value = "PositionQuantity")
	private BigDecimal PositionQuantity;
	/**
	 * 经济商
	 */
	@JsonProperty(value = "BrokerNames")
	private String BrokerNames;
	/**
	 * 到期日
	 */
	@JsonProperty(value = "PromptDate")
	private String PromptDate;
	/**
	 *  批次编号
	 */
	@JsonProperty(value = "FullNoOfLot")
	private String FullNoOfLot;
	/**
	 * 对手方
	 */
	@JsonProperty(value = "FullNoOfCounterparty")
	private String FullNoOfCounterparty;
	/**
	 * 完成确认
	 */
	@JsonProperty(value = "IsComplete")
	private String IsComplete;
	/**
	 * 批次ID
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	/**
	 * 交易方向
	 */
	@JsonProperty(value = "LS")
	private String LS;
	/**
	 * 批次买卖方向
	 */
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 交易号
	 */
	@JsonProperty(value = "OurRef")
	private String OurRef;
	
	public String getETDMonth(){
		return ETDMonth;
	}
	public void setETDMonth(String ETDMonth){
		this.ETDMonth=ETDMonth;
	}
	
	public String getBrandNames(){
		return BrandNames;
	}
	public void setBrandNames(String BrandNames){
		this.BrandNames=BrandNames;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getPlanPositionQuantity(){
		return PlanPositionQuantity;
	}
	public void setPlanPositionQuantity(BigDecimal PlanPositionQuantity){
		this.PlanPositionQuantity=PlanPositionQuantity;
	}
	
	public BigDecimal getPricePositionQuantity(){
		return PricePositionQuantity;
	}
	public void setPricePositionQuantity(BigDecimal PricePositionQuantity){
		this.PricePositionQuantity=PricePositionQuantity;
	}
	
	public BigDecimal getPositionQuantity(){
		return PositionQuantity;
	}
	public void setPositionQuantity(BigDecimal PositionQuantity){
		this.PositionQuantity=PositionQuantity;
	}
	
	public String getBrokerNames(){
		return BrokerNames;
	}
	public void setBrokerNames(String BrokerNames){
		this.BrokerNames=BrokerNames;
	}
	
	public String getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(String PromptDate){
		this.PromptDate=PromptDate;
	}
	
	public String getFullNoOfLot(){
		return FullNoOfLot;
	}
	public void setFullNoOfLot(String FullNoOfLot){
		this.FullNoOfLot=FullNoOfLot;
	}
	
	public String getFullNoOfCounterparty(){
		return FullNoOfCounterparty;
	}
	public void setFullNoOfCounterparty(String FullNoOfCounterparty){
		this.FullNoOfCounterparty=FullNoOfCounterparty;
	}
	
	public String getIsComplete(){
		return IsComplete;
	}
	public void setIsComplete(String IsComplete){
		this.IsComplete=IsComplete;
	}
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public String getLS(){
		return LS;
	}
	public void setLS(String LS){
		this.LS=LS;
	}
	
	public String getSpotDirection(){
		return SpotDirection;
	}
	public void setSpotDirection(String SpotDirection){
		this.SpotDirection=SpotDirection;
	}
	
	public String getOurRef(){
		return OurRef;
	}
	public void setOurRef(String OurRef){
		this.OurRef=OurRef;
	}

}