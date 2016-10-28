package com.smm.ctrm.dto.res.PositionDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PositionDailyBase {
	
	public PositionDailyBase(){}
	
	public PositionDailyBase(String positionBrokerId, String marketName, String forwardType, String purpose, String commodityId, String commodityName, String instrumentName,
			String lS, String oCS, Date promptDate, BigDecimal hands, BigDecimal quantity, BigDecimal ourPrice,
			String currency) {
		super();
		PositionBrokerId = positionBrokerId;
		MarketName = marketName;
		ForwardType = forwardType;
		Purpose = purpose;
		CommodityId = commodityId;
		CommodityName = commodityName;
		InstrumentName = instrumentName;
		LS = lS;
		OCS = oCS;
		PromptDate = promptDate;
		Hands = hands;
		Quantity = quantity;
		OurPrice = ourPrice;
		Currency = currency;
	}
	
	/**
	 * 头寸Id
	 */
	@JsonProperty(value = "PositionBrokerId")
	private String PositionBrokerId;

	/**
	 * 市场
	 */
	@JsonProperty(value = "MarketName")
	private String MarketName;
	
	/**
	 * 头寸类型
	 */
	@JsonProperty(value = "ForwardType")
	private String ForwardType;
	
	/**
	 * 头寸用途，交易目的 {保值H, 投机S, 套利A}(Dict)
	 */
	@JsonProperty(value = "Purpose")
	private String Purpose;
	
	/**
	 * 品种Id
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	
	/**
	 * 品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	
	/**
	 * 合约名称
	 */
	@JsonProperty(value = "InstrumentName")
	private String InstrumentName;
	
	/**
	 * 头寸方向，{L=买, S=卖}
	 */
	@JsonProperty(value = "LS")
	private String LS;
	
	/**
	 * 开平方向，开平类型。{开Open = O，平Square = S，平今Close = C}
	 */
	@JsonProperty(value = "OCS")
	private String OCS;
	
	/**
	 * 到期日
	 */
	@JsonProperty(value = "PromptDate")
	private Date PromptDate;
	
	/**
	 * 头寸手数。
	 */
	@JsonProperty(value = "Hands")
	private BigDecimal Hands;
	
	/**
	 * 头寸重量。已经从“手数 * 每手数量”计算得出
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 价格
	 */
	@JsonProperty(value = "OurPrice")
	private BigDecimal OurPrice;
	
	/**
	 * 成交金额
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	
	/**
	 * 币种(Dict)
	 */
	@JsonProperty(value = "Currency")
	private String Currency;
	
	/**
	 * 盈亏
	 */
	@JsonProperty(value = "ProfitAndLoss")
	private BigDecimal ProfitAndLoss;

	public String getMarketName() {
		return MarketName;
	}

	public void setMarketName(String marketName) {
		MarketName = marketName;
	}

	public String getPurpose() {
		return Purpose;
	}

	public void setPurpose(String purpose) {
		Purpose = purpose;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}

	public String getInstrumentName() {
		return InstrumentName;
	}

	public void setInstrumentName(String instrumentName) {
		InstrumentName = instrumentName;
	}

	public String getLS() {
		return LS;
	}

	public void setLS(String lS) {
		LS = lS;
	}

	public String getOCS() {
		return OCS;
	}

	public void setOCS(String oCS) {
		OCS = oCS;
	}

	public Date getPromptDate() {
		return PromptDate;
	}

	public void setPromptDate(Date promptDate) {
		PromptDate = promptDate;
	}

	public BigDecimal getHands() {
		return Hands;
	}

	public void setHands(BigDecimal hands) {
		Hands = hands;
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

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public String getPositionBrokerId() {
		return PositionBrokerId;
	}

	public void setPositionBrokerId(String positionBrokerId) {
		PositionBrokerId = positionBrokerId;
	}

	public BigDecimal getProfitAndLoss() {
		return ProfitAndLoss;
	}

	public void setProfitAndLoss(BigDecimal profitAndLoss) {
		ProfitAndLoss = profitAndLoss;
	}

	public String getForwardType() {
		return ForwardType;
	}

	public void setForwardType(String forwardType) {
		ForwardType = forwardType;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}
	
	
}
