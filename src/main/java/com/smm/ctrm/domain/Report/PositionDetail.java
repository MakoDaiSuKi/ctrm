

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
public class PositionDetail {
	private static final long serialVersionUID = 1461719402485L;
	/**
	 *
	 */
	@JsonProperty(value = "Id")
	private String Id;
	/**
	 *
	 */
	@JsonProperty(value = "PromptDate")
	private String PromptDate;
	/**
	 * 交易方向
	 */
	@JsonProperty(value = "LS")
	private String LS;
	/**
	 * 开仓/关仓
	 */
	@JsonProperty(value = "WareouseStatus")
	private String WareouseStatus;
	/**
	 * 头寸数量
	 */
	@JsonProperty(value = "PositionQuantity")
	private BigDecimal PositionQuantity;
	/**
	 * 成交均价
	 */
	@JsonProperty(value = "TradePrice")
	private BigDecimal TradePrice;
	/**
	 * 调期价差
	 */
	@JsonProperty(value = "CarryDiffPrice")
	private BigDecimal CarryDiffPrice;
	/**
	 * 交易费用
	 */
	@JsonProperty(value = "TradeFee")
	private BigDecimal TradeFee;
	/**
	 * 合计价格
	 */
	@JsonProperty(value = "OurPirce")
	private BigDecimal OurPirce;
	/**
	 * 盈亏
	 */
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 品牌
	 */
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 * 经济商
	 */
	@JsonProperty(value = "BrokerNames")
	private String BrokerNames;
	/**
	 * 建仓完成比例
	 */
	@JsonProperty(value = "TakePositionRatio")
	private String TakePositionRatio;
	/**
	 * 点价销售与平仓差值
	 */
	@JsonProperty(value = "DiffOfPriceAndPosition")
	private String DiffOfPriceAndPosition;
	/**
	 * 交易号
	 */
	@JsonProperty(value = "OurRef")
	private String OurRef;
	
	public String getId(){
		return Id;
	}
	public void setId(String Id){
		this.Id=Id;
	}
	
	public String getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(String PromptDate){
		this.PromptDate=PromptDate;
	}
	
	public String getLS(){
		return LS;
	}
	public void setLS(String LS){
		this.LS=LS;
	}
	
	public String getWareouseStatus(){
		return WareouseStatus;
	}
	public void setWareouseStatus(String WareouseStatus){
		this.WareouseStatus=WareouseStatus;
	}
	
	public BigDecimal getPositionQuantity(){
		return PositionQuantity;
	}
	public void setPositionQuantity(BigDecimal PositionQuantity){
		this.PositionQuantity=PositionQuantity;
	}
	
	public BigDecimal getTradePrice(){
		return TradePrice;
	}
	public void setTradePrice(BigDecimal TradePrice){
		this.TradePrice=TradePrice;
	}
	
	public BigDecimal getCarryDiffPrice(){
		return CarryDiffPrice;
	}
	public void setCarryDiffPrice(BigDecimal CarryDiffPrice){
		this.CarryDiffPrice=CarryDiffPrice;
	}
	
	public BigDecimal getTradeFee(){
		return TradeFee;
	}
	public void setTradeFee(BigDecimal TradeFee){
		this.TradeFee=TradeFee;
	}
	
	public BigDecimal getOurPirce(){
		return OurPirce;
	}
	public void setOurPirce(BigDecimal OurPirce){
		this.OurPirce=OurPirce;
	}
	
	public BigDecimal getPnL(){
		return PnL;
	}
	public void setPnL(BigDecimal PnL){
		this.PnL=PnL;
	}
	
	public String getBrandNames(){
		return BrandNames;
	}
	public void setBrandNames(String BrandNames){
		this.BrandNames=BrandNames;
	}
	
	public String getBrokerNames(){
		return BrokerNames;
	}
	public void setBrokerNames(String BrokerNames){
		this.BrokerNames=BrokerNames;
	}
	
	public String getTakePositionRatio(){
		return TakePositionRatio;
	}
	public void setTakePositionRatio(String TakePositionRatio){
		this.TakePositionRatio=TakePositionRatio;
	}
	
	public String getDiffOfPriceAndPosition(){
		return DiffOfPriceAndPosition;
	}
	public void setDiffOfPriceAndPosition(String DiffOfPriceAndPosition){
		this.DiffOfPriceAndPosition=DiffOfPriceAndPosition;
	}
	
	public String getOurRef(){
		return OurRef;
	}
	public void setOurRef(String OurRef){
		this.OurRef=OurRef;
	}

}