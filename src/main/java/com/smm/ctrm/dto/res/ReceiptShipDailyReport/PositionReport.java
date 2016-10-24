package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class PositionReport extends HibernateEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2123347708102200199L;

	public PositionReport() {
		super();
	}

	public PositionReport(String fullNo, BigDecimal quantity, BigDecimal ourPrice, String ourRef, String forwardType,
			String marketName, String commodityName, Date promptDate, Date tradeDate) {
		super();
		FullNo = fullNo;
		Quantity = quantity;
		OurPrice = ourPrice;
		OurRef = ourRef;
		ForwardType = forwardType;
		MarketName = marketName;
		CommodityName = commodityName;
		PromptDate = promptDate;
		TradeDate = tradeDate;
	}

	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 保值数量
	 * 头寸重量。已经从“手数 * 每手数量”计算得出
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 保值价格
	 * 真正的价格
	 */
	@JsonProperty(value = "OurPrice")
	private BigDecimal OurPrice;
	
	/**
	 * Broker交易号
	 */
	@JsonProperty(value = "OurRef")
	private String OurRef;
	
	/**
	 * 头寸类型
	 * {F = 期货，A = 均价, O = 期权}
	 */
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	
	/**
	 * 交易市场
	 */
	@JsonProperty(value = "MarketName")
	private String MarketName;
	
	/**
	 * 交易品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	
	/**
	 * 到期日
	 */
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	
	/**
	 * 业务日期。指头寸交易委托的日期。
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getOurPrice() {
		return OurPrice;
	}

	public void setOurPrice(BigDecimal ourPrice) {
		OurPrice = ourPrice;
	}

	public String getOurRef() {
		return OurRef;
	}

	public void setOurRef(String ourRef) {
		OurRef = ourRef;
	}

	public String getForwardType() {
		return ForwardType;
	}

	public void setForwardType(String forwardType) {
		ForwardType = forwardType;
	}

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String marketName) {
		MarketName = marketName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date promptDate) {
		PromptDate = promptDate;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}
}
