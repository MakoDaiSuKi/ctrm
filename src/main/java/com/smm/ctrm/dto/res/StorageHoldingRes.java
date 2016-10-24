package com.smm.ctrm.dto.res;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StorageHoldingRes {
	
	public StorageHoldingRes(String id, String legalName, String commodityName, String brandName, String transitStatus,
			String warehouseName, Date tradeDate, String projectName, String cardNo, String storageNo, String truckNo,
			String product, BigDecimal quantity, BigDecimal price, BigDecimal fee, String currency) {
		super();
		Id = id;
		LegalName = legalName;
		CommodityName = commodityName;
		BrandName = brandName;
		TransitStatus = transitStatus;
		WarehouseName = warehouseName;
		TradeDate = tradeDate;
		ProjectName = projectName;
		CardNo = cardNo;
		StorageNo = storageNo;
		TruckNo = truckNo;
		Product = product;
		Quantity = quantity;
		Price = price;
		Fee = fee;
		Currency = currency;
	}

	@JsonProperty(value = "Id")
	private String Id;
	
	@JsonProperty(value = "LegalName")
	private String LegalName;
	
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	
	/**
	 * 品牌名称
	 */
	@JsonProperty(value = "BrandName")
	private String BrandName;
	
	/**
	 *
	 */
	@JsonProperty(value = "TransitStatus")
	private String TransitStatus;
	
	@JsonProperty(value = "WarehouseName")
	private String WarehouseName;
	
	/**
	 * 业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	
	/**
	 * 项目名称
	 */
	@JsonProperty(value = "ProjectName")
	private String ProjectName;
	
	/**
	 *
	 */
	@JsonProperty(value = "CardNo")
	private String CardNo;
	
	/**
	 * 纸质单据编号，对于
	 */
	@JsonProperty(value = "StorageNo")
	private String StorageNo;
	
	/**
	 * 卡车号
	 */
	@JsonProperty(value = "TruckNo")
	private String TruckNo;
	
	/**
	 * 产品
	 */
	@JsonProperty(value = "Product")
	private String Product;
	
	/**
	 * 净重，相当于实际出入库的数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 包括了Fee，综合成本
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	
	/**
	 * 综合成本，包括在Price中
	 */
	@JsonProperty(value = "Fee")
	private BigDecimal Fee;
	
	/**
	 *
	 */
	@JsonProperty(value = "Currency")
	private String Currency;

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

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String brandName) {
		BrandName = brandName;
	}

	public String getTransitStatus() {
		return TransitStatus;
	}

	public void setTransitStatus(String transitStatus) {
		TransitStatus = transitStatus;
	}

	public String getWarehouseName() {
		return WarehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		WarehouseName = warehouseName;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String projectName) {
		ProjectName = projectName;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public String getStorageNo() {
		return StorageNo;
	}

	public void setStorageNo(String storageNo) {
		StorageNo = storageNo;
	}

	public String getTruckNo() {
		return TruckNo;
	}

	public void setTruckNo(String truckNo) {
		TruckNo = truckNo;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
	}

	public BigDecimal getFee() {
		return Fee;
	}

	public void setFee(BigDecimal fee) {
		Fee = fee;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}
	
}
