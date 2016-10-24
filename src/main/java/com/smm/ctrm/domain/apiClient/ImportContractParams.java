
package com.smm.ctrm.domain.apiClient;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 单批次合同导入参数
 * 
 * @author zengshihua
 *
 */
public class ImportContractParams implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6204556115205486258L;
	
	//~~~~~~~额外属性~~~~~~~~//
	@JsonProperty(value = "ContractPrefix")
	private String ContractPrefix; 

	// ~~~~~~~~~合同属性~~~~~~~~~~~//
	/**
	 * 业务日期
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	/**
	 * 客户名称ID
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;

	/**
	 * 抬头
	 */
	@JsonProperty(value = "LegalId")
	private String LegalId;

	/**
	 * 抬头ID
	 */
	@JsonProperty(value = "LegalName")
	private String LegalName;

	/**
	 * 可多选品牌，逗号隔开
	 */
	@Column(name = "BrandIds")
	@JsonProperty(value = "BrandIds")
	private String BrandIds;

	/**
	 * 品牌名称
	 */
	@JsonProperty(value = "BrandNames")
	private String BrandNames;

	/**
	 * 数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;

	/**
	 * 购销(现货交易方向){B, S}
	 */
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;

	/**
	 * 品种ID
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	/**
	 * 品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;

	/**
	 * 商品名称
	 */
	@JsonProperty(value = "Product")
	private String Product;

	/**
	 * 规格ID
	 */
	@JsonProperty(value = "SpecId")
	private String SpecId;

	/**
	 * 规格
	 */
	@JsonProperty(value = "SpecName")
	private String SpecName;

	/**
	 * 文档号
	 */
	@JsonProperty(value = "DocumentNo")
	private String DocumentNo;

	// ~~~~~~~~~批次属性~~~~~~~~~//
	
	/**
	 * 是否按照ETA船期点价
	 */
	@JsonProperty(value = "IsEtaPricing")
	private Boolean IsEtaPricing;

	/**
	 * { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
	 */
	@JsonProperty(value = "MajorType")
	private String MajorType;

	/**
	 * （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果
	 */
	@JsonProperty(value = "Major")
	private BigDecimal Major;

	/**
	 * 按ETD月份 ｛M-3, M-2, M-1, M, M+1, M+2, M+3｝M=到货月份
	 */
	@Column(name = "EtaDuaration")
	@JsonProperty(value = "EtaDuaration")
	private String EtaDuaration;

	/**
	 * 主价市场的标识ID
	 */
	@JsonProperty(value = "MajorMarketId")
	private String MajorMarketId;

	/**
	 * 主价市场的标识
	 */
	@JsonProperty(value = "MajorMarketName")
	private String MajorMarketName;

	/**
	 * 市场基准
	 */
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;

	/**
	 * 点价期始
	 */
	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;
	/**
	 * 点价期止
	 */
	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;
	
	/**
	 * 点价信息
	 */
	@JsonProperty(value = "Comments")
	private String Comments;
	

	// ~~~~升贴水部分~~~~~~//
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水
	 */
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;

	/**
	 * { 最低价的均价 = LOW, 最高价的均价 = HIGH, 均价 = AVERAGE}(dict)
	 */
	@JsonProperty(value = "PremiumBasis")
	private String PremiumBasis;

	/**
	 * { 固定升贴水 = F(FIX)，均价升贴水 = A(AVERAGE)}(dict)
	 */
	@Column(name = "PremiumType", length = 30)
	@JsonProperty(value = "PremiumType")
	private String PremiumType;

	/**
	 * 升贴水市场的标识
	 */
	@JsonProperty(value = "PremiumMarketId")
	private String PremiumMarketId;

	/**
	 * 开始日期
	 */
	@Column(name = "PremiumStartDate")
	@JsonProperty(value = "PremiumStartDate")
	private Date PremiumStartDate;
	/**
	 * 结束日期
	 */
	@Column(name = "PremiumEndDate")
	@JsonProperty(value = "PremiumEndDate")
	private Date PremiumEndDate;

	
	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String legalName) {
		LegalName = legalName;
	}

	public String getBrandIds() {
		return BrandIds;
	}

	public void setBrandIds(String brandIds) {
		BrandIds = brandIds;
	}

	public String getBrandNames() {
		return BrandNames;
	}

	public void setBrandNames(String brandNames) {
		BrandNames = brandNames;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String spotDirection) {
		SpotDirection = spotDirection;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String commodityName) {
		CommodityName = commodityName;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String product) {
		Product = product;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String specId) {
		SpecId = specId;
	}

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String specName) {
		SpecName = specName;
	}

	public String getDocumentNo() {
		return DocumentNo;
	}

	public void setDocumentNo(String documentNo) {
		DocumentNo = documentNo;
	}

	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String majorType) {
		MajorType = majorType;
	}

	public BigDecimal getMajor() {
		return Major;
	}

	public void setMajor(BigDecimal major) {
		Major = major;
	}

	public String getEtaDuaration() {
		return EtaDuaration;
	}

	public void setEtaDuaration(String etaDuaration) {
		EtaDuaration = etaDuaration;
	}

	public String getMajorMarketId() {
		return MajorMarketId;
	}

	public void setMajorMarketId(String majorMarketId) {
		MajorMarketId = majorMarketId;
	}

	public String getMajorMarketName() {
		return MajorMarketName;
	}

	public void setMajorMarketName(String majorMarketName) {
		MajorMarketName = majorMarketName;
	}

	public String getMajorBasis() {
		return MajorBasis;
	}

	public void setMajorBasis(String majorBasis) {
		MajorBasis = majorBasis;
	}

	public Date getMajorStartDate() {
		return MajorStartDate;
	}

	public void setMajorStartDate(Date majorStartDate) {
		MajorStartDate = majorStartDate;
	}

	public Date getMajorEndDate() {
		return MajorEndDate;
	}

	public void setMajorEndDate(Date majorEndDate) {
		MajorEndDate = majorEndDate;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal premium) {
		Premium = premium;
	}

	public String getPremiumBasis() {
		return PremiumBasis;
	}

	public void setPremiumBasis(String premiumBasis) {
		PremiumBasis = premiumBasis;
	}

	public String getPremiumType() {
		return PremiumType;
	}

	public void setPremiumType(String premiumType) {
		PremiumType = premiumType;
	}

	public String getPremiumMarketId() {
		return PremiumMarketId;
	}

	public void setPremiumMarketId(String premiumMarketId) {
		PremiumMarketId = premiumMarketId;
	}

	public Date getPremiumStartDate() {
		return PremiumStartDate;
	}

	public void setPremiumStartDate(Date premiumStartDate) {
		PremiumStartDate = premiumStartDate;
	}

	public Date getPremiumEndDate() {
		return PremiumEndDate;
	}

	public void setPremiumEndDate(Date premiumEndDate) {
		PremiumEndDate = premiumEndDate;
	}

	public Boolean getIsEtaPricing() {
		return IsEtaPricing;
	}

	public void setIsEtaPricing(Boolean isEtaPricing) {
		IsEtaPricing = isEtaPricing;
	}

	public String getContractPrefix() {
		return ContractPrefix;
	}

	public void setContractPrefix(String contractPrefix) {
		ContractPrefix = contractPrefix;
	}

}