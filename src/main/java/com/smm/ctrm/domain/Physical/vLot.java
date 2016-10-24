package com.smm.ctrm.domain.Physical;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by zhenghao on 2016/7/7.
 *
 */
@Entity
@Table(name = "Lot", schema = "Physical")
public class vLot extends LotBase {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3402637831927325563L;

	@Column(name = "CounterpartId")
    @JsonProperty(value = "CounterpartId")
    private String CounterpartId;

    /**
     * 多对一：合同
     */
    @Column(name = "ContractId")
    @JsonProperty(value = "ContractId")
    private String ContractId;

    @Column(name = "LegalId")
    @JsonProperty(value = "LegalId")
    private String LegalId;

    /**
     * 交易对手 = 客户或者供应商
     */
    @Column(name = "CustomerId")
    @JsonProperty(value = "CustomerId")
    private String CustomerId;

    /**
     * 品种
     */
    @Column(name = "CommodityId")
    @JsonProperty(value = "CommodityId")
    private String CommodityId;

    /**
     * 多对一：规格
     */
    @Column(name = "SpecId")
    @JsonProperty(value = "SpecId")
    private String SpecId;

    /**
     * 多对一：原产地
     */
    @Column(name = "OriginId")
    @JsonProperty(value = "OriginId")
    private String OriginId;

    /**
     * 多对一：仓库 = add by zhu yixin on 2014/8/5，为了迎合v1的需求
     */
    @Column(name = "WarehouseId")
    @JsonProperty(value = "WarehouseId")
    private String WarehouseId;

    /**
     * 多对一：主价市场的标识
     */
    @Column(name = "MajorMarketId")
    @JsonProperty(value = "MajorMarketId")
    private String MajorMarketId;

    /**
     * 多对一：升贴水市场的标识
     */
    @Column(name = "PremiumMarketId")
    @JsonProperty(value = "PremiumMarketId")
    private String PremiumMarketId;

    /**
     * 创建者Id
     */
    @Column(name = "CreatedId")
    @JsonProperty(value = "CreatedId")
    private String CreatedId;

    /**
     * 更新者Id
     */
    @Column(name = "UpdatedId")
    @JsonProperty(value = "UpdatedId")
    private String UpdatedId;


    public String getCounterpartId() {
        return CounterpartId;
    }

    public void setCounterpartId(String counterpartId) {
        CounterpartId = counterpartId;
    }

    public String getContractId() {
        return ContractId;
    }

    public void setContractId(String contractId) {
        ContractId = contractId;
    }

    public String getLegalId() {
        return LegalId;
    }

    public void setLegalId(String legalId) {
        LegalId = legalId;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getCommodityId() {
        return CommodityId;
    }

    public void setCommodityId(String commodityId) {
        CommodityId = commodityId;
    }

    public String getSpecId() {
        return SpecId;
    }

    public void setSpecId(String specId) {
        SpecId = specId;
    }

    public String getOriginId() {
        return OriginId;
    }

    public void setOriginId(String originId) {
        OriginId = originId;
    }

    public String getWarehouseId() {
        return WarehouseId;
    }

    public void setWarehouseId(String warehouseId) {
        WarehouseId = warehouseId;
    }

    public String getMajorMarketId() {
        return MajorMarketId;
    }

    public void setMajorMarketId(String majorMarketId) {
        MajorMarketId = majorMarketId;
    }

    public String getPremiumMarketId() {
        return PremiumMarketId;
    }

    public void setPremiumMarketId(String premiumMarketId) {
        PremiumMarketId = premiumMarketId;
    }

    public String getCreatedId() {
        return CreatedId;
    }

    public void setCreatedId(String createdId) {
        CreatedId = createdId;
    }

    public String getUpdatedId() {
        return UpdatedId;
    }

    public void setUpdatedId(String updatedId) {
        UpdatedId = updatedId;
    }
}
