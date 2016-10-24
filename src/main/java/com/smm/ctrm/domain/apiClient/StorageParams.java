
package com.smm.ctrm.domain.apiClient;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.Basis.Brand;

/**
 * 3. 出入库，class = storage
 * 
 */
public class StorageParams extends ApiGridParams {
	
	/**
	 * 非己抬头
	 */
	public String LegalIdOfNotSelf; 
	
	 /**
	 * 内部客户Id（用于查询外部客户的）
	 */
	public List<String> CustomerIds;
	
	/**
	 * 来源的LegalId
	 */
	public String SourceLegalId;
	
	/**
	 * 
	 */
	public String BrandIds;
	
    /**
     * 车号
     */
    public String TruckNo;
    /**
     * 卡号
     */
    public String CardNo;
    
    public Boolean IsBorrow;

    public String SpecId;
	
    /**
     * 货物来源 设置为空字符则认为是null进行筛选
     */
	private String GoodsSource;
	
	@JsonProperty(value = "IsNoticed")
	private Boolean IsNoticed;
	
	@JsonProperty(value = "LoadDateEnd")
	private Date LoadDateEnd;
	
	@JsonProperty(value = "LoadDateStart")
	private Date LoadDateStart;

	public Boolean getIsNoticed() {
		return IsNoticed;
	}

	public void setIsNoticed(Boolean value) {
		IsNoticed = value;
	}
	
	@JsonProperty(value="CustomerId")
	private String CustomerId;

	
	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	@JsonProperty(value = "CxStatus")
	private String CxStatus;

	
	public String getCxStatus() {
		return CxStatus;
	}

	public void setCxStatus(String value) {
		CxStatus = value;
	}

	@JsonProperty(value = "IsIn")
	private Boolean IsIn;

	
	public Boolean getIsIn() {
		return IsIn;
	}

	public void setIsIn(Boolean value) {
		IsIn = value;
	}

	@JsonProperty(value = "IsOut")
	private Boolean IsOut;

	
	public Boolean getIsOut() {
		return IsOut;
	}

	public void setIsOut(Boolean value) {
		IsOut = value;
	}

	@JsonProperty(value = "IsRebuy")
	private Boolean IsRebuy;

	
	public Boolean getIsRebuy() {
		return IsRebuy;
	}

	public void setIsRebuy(Boolean value) {
		IsRebuy = value;
	}

	@JsonProperty(value = "IsFactory")
	private Boolean IsFactory;

	
	public Boolean getIsFactory() {
		return IsFactory;
	}

	public void setIsFactory(Boolean value) {
		IsFactory = value;
	}

	@JsonProperty(value = "ContractId")
	private String ContractId;

	
	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String value) {
		ContractId = value;
	}

	@JsonProperty(value = "LotId")
	private String LotId;

	
	public String getLotId() {
		return LotId;
	}

	public void setLotId(String value) {
		LotId = value;
	}

	@JsonProperty(value = "LegalId")
	private String LegalId;

	
	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String value) {
		LegalId = value;
	}

	@JsonProperty(value = "LegalCode")
	private String LegalCode;

	
	public String getLegalCode() {
		return LegalCode;
	}

	public void setLegalCode(String value) {
		LegalCode = value;
	}

	@JsonProperty(value = "SourceDeliveryId")
	private String SourceDeliveryId;

	
	public String getSourceDeliveryId() {
		return SourceDeliveryId;
	}

	public void setSourceDeliveryId(String value) {
		SourceDeliveryId = value;
	}

	@JsonProperty(value = "DestinationDeliveryId")
	private String DestinationDeliveryId;

	
	public String getDestinationDeliveryId() {
		return DestinationDeliveryId;
	}

	public void setDestinationDeliveryId(String value) {
		DestinationDeliveryId = value;
	}

	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;

	
	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(Boolean value) {
		IsInvoiced = value;
	}

	@JsonProperty(value = "HasNoticeId")
	private Boolean HasNoticeId;

	
	public Boolean getHasNoticeId() {
		return HasNoticeId;
	}

	public void setHasNoticeId(Boolean value) {
		HasNoticeId = value;
	}

	@JsonProperty(value = "IsVirtual")
	private Boolean IsVirtual;

	
	public Boolean getIsVirtual() {
		return IsVirtual;
	}

	public void setIsVirtual(Boolean value) {
		IsVirtual = value;
	}

	@JsonProperty(value = "BrandId")
	private String BrandId;

	
	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String value) {
		BrandId = value;
	}

	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;

	
	public String getWarehouseId() {
		return WarehouseId;
	}

	public void setWarehouseId(String value) {
		WarehouseId = value;
	}

	@JsonProperty(value = "CommodityIds")
	private String CommodityIds;

	
	public String getCommodityIds() {
		return CommodityIds;
	}

	public void setCommodityIds(String value) {
		CommodityIds = value;
	}

	@JsonProperty(value = "StartDate")
	private Date StartDate;

	
	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date value) {
		StartDate = value;
	}

	@JsonProperty(value = "EndDate")
	private Date EndDate;

	
	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date value) {
		EndDate = value;
	}
	@JsonProperty(value = "HistoryDate")
	private Date HistoryDate;

	@JsonProperty(value = "MT")
	private String MT;

	
	public String getMT() {
		return MT;
	}

	public void setMT(String value) {
		MT = value;
	}

	/**
	 * 是否为最初Bvi收货明细
	 * 
	 */
	@JsonProperty(value = "HasBviSource")
	private Boolean HasBviSource;

	
	public Boolean getHasBviSource() {
		return HasBviSource;
	}

	public void setHasBviSource(Boolean value) {
		HasBviSource = value;
	}

	@JsonProperty(value = "TransitStatus")
	private String TransitStatus;

	
	public String getTransitStatus() {
		return TransitStatus;
	}

	public void setTransitStatus(String value) {
		TransitStatus = value;
	}

	@JsonProperty(value = "LegalIds")
	private String LegalIds;

	
	public String getLegalIds() {
		return LegalIds;
	}

	public void setLegalIds(String value) {
		LegalIds = value;
	}

	/**
	 * 经纪商
	 * 
	 */
	@JsonProperty(value = "BrokerIds")
	private String BrokerIds;

	
	public String getBrokerIds() {
		return BrokerIds;
	}

	public void setBrokerIds(String value) {
		BrokerIds = value;
	}

	@JsonProperty(value = "BrokerName")
	private String BrokerName;

	
	public String getBrokerName() {
		return BrokerName;
	}

	public void setBrokerName(String value) {
		BrokerName = value;
	}
	@JsonProperty(value = "Brands")
	private List<Brand> Brands;

	public List<Brand> getBrands() {
		return Brands;
	}

	public void setBrands(List<Brand> value) {
		Brands = value;
	}
	@JsonProperty(value = "CommodityIdList")
	private List<String> CommodityIdList;

	public List<String> getCommodityIdList() {
		return CommodityIdList;
	}

	public void setCommodityIdList(List<String> value) {
		CommodityIdList = value;
	}

	@JsonProperty(value = "CommodityId")
	private String CommodityId;

	
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String value) {
		CommodityId = value;
	}

	/**
	 * 是否包含分拆批次的交付明细
	 * 
	 */
	@JsonProperty(value = "IsContainSplit")
	private Boolean IsContainSplit;

	
	public Boolean getIsContainSplit() {
		return IsContainSplit;
	}

	public void setIsContainSplit(Boolean value) {
		IsContainSplit = value;
	}

	@JsonProperty(value = "ProductName")
	private String ProductName;

	
	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String value) {
		ProductName = value;
	}

	@JsonProperty(value = "IsReturn")
	private Boolean IsReturn;

	
	public Boolean getIsReturn() {
		return IsReturn;
	}

	public void setIsReturn(Boolean value) {
		IsReturn = value;
	}
	
	public String getGoodsSource() {
		return GoodsSource;
	}

	public void setGoodsSource(String goodsSource) {
		GoodsSource = goodsSource;
	}

	public String getLegalIdOfNotSelf() {
		return LegalIdOfNotSelf;
	}

	public void setLegalIdOfNotSelf(String legalIdOfNotSelf) {
		LegalIdOfNotSelf = legalIdOfNotSelf;
	}

	public List<String> getCustomerIds() {
		return CustomerIds;
	}

	public void setCustomerIds(List<String> customerIds) {
		CustomerIds = customerIds;
	}

	public String getBrandIds() {
		return BrandIds;
	}

	public void setBrandIds(String brandIds) {
		BrandIds = brandIds;
	}

	public String getTruckNo() {
		return TruckNo;
	}

	public void setTruckNo(String truckNo) {
		TruckNo = truckNo;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String cardNo) {
		CardNo = cardNo;
	}

	public Boolean getIsBorrow() {
		return IsBorrow;
	}

	public void setIsBorrow(Boolean isBorrow) {
		IsBorrow = isBorrow;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String specId) {
		SpecId = specId;
	}

	public String getSourceLegalId() {
		return SourceLegalId;
	}

	public void setSourceLegalId(String sourceLegalId) {
		SourceLegalId = sourceLegalId;
	}

	public Date getHistoryDate() {
		return HistoryDate;
	}

	public void setHistoryDate(Date historyDate) {
		HistoryDate = historyDate;
	}

	public Date getLoadDateEnd() {
		return LoadDateEnd;
	}

	public void setLoadDateEnd(Date loadDateEnd) {
		LoadDateEnd = loadDateEnd;
	}

	public Date getLoadDateStart() {
		return LoadDateStart;
	}

	public void setLoadDateStart(Date loadDateStart) {
		LoadDateStart = loadDateStart;
	}
}