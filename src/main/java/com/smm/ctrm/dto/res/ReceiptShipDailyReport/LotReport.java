package com.smm.ctrm.dto.res.ReceiptShipDailyReport;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class LotReport extends HibernateEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1533174198133718257L;

	public LotReport(){super();}
	
	public LotReport(String id, String legalName, String commodityName, String specName, String brandNames, String warehouseName,
			String customerName, String fullNo, BigDecimal final1, BigDecimal quantity, BigDecimal quantityDelivered,
			String transactionType, Date tradeDate, Date createdAt, String todayChange) {
		super();
		super.setId(id);
		super.setCreatedAt(createdAt);
		LegalName = legalName;
		CommodityName = commodityName;
		SpecName = specName;
		BrandNames = brandNames;
		WarehouseName = warehouseName;
		CustomerName = customerName;
		FullNo = fullNo;
		Final = final1;
		Quantity = quantity;
		QuantityDelivered = quantityDelivered;
		TransactionType = transactionType;
		TradeDate = tradeDate;
		TodayChange = todayChange;
	}

	/**
	 * 抬头
	 */
	@JsonProperty(value = "LegalName")
	private String LegalName;
	
	/**
	 * 品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	
	/**
	 * 规格
	 */
	@JsonProperty(value = "SpecName")
	private String SpecName;
	
	/**
	 * 品牌
	 */
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	
	/**
	 * 仓库
	 */
	@JsonProperty(value = "WarehouseName")
	private String WarehouseName;

	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	
	/**
	 * 批次号
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	
	/**
	 * 销售价格
	 */
	@JsonProperty(value = "Final")
	private BigDecimal Final;
	
	/**
	 * 批次数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 实际的交付数量
	 */
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	
	/**
	 * 交易类型
	 */
	@JsonProperty(value = "TransactionType")
	private String TransactionType;
	
	/**
	 * 批次业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	
	/**
	 * 今日变动
	 */
	@JsonProperty(value = "TodayChange")
	private String TodayChange;

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String legalName) {
		LegalName = legalName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String specName) {
		SpecName = specName;
	}

	public String getBrandNames() {
		return BrandNames;
	}

	public void setBrandNames(String brandNames) {
		BrandNames = brandNames;
	}

	public String getWarehouseName() {
		return WarehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		WarehouseName = warehouseName;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public BigDecimal getFinal() {
		return Final;
	}

	public void setFinal(BigDecimal final1) {
		Final = final1;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}

	public String getTransactionType() {
		return TransactionType;
	}

	public void setTransactionType(String transactionType) {
		TransactionType = transactionType;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getTodayChange() {
		return TodayChange;
	}

	public void setTodayChange(String todayChange) {
		TodayChange = todayChange;
	}

}
