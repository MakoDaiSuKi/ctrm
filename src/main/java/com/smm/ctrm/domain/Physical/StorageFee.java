package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Warehouse;

/**
*
*/
@Entity
@Table(name = "StorageFee", schema = "Physical")
public class StorageFee extends HibernateEntity
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Transient
	@JsonProperty(value = "StorageFeeDetail")
	private List<StorageFeeDetail> StorageFeeDetail;
	
	/**
	 * 费用编号
	 */
	@Column(name = "FeeCode")
	@JsonProperty(value = "FeeCode")
	private String FeeCode;
	
	/**
	 * 仓库
	 */
	@Column(name = "WarehouseId")
	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name = "WarehouseId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Warehouse")
	private Warehouse Warehouse;
	
	/**
	 * 费用类别
	 */
	@Column(name = "FeeType")
	@JsonProperty(value = "FeeType")
	private String FeeType;
	
	
	/**
	 * 数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	
	/**
	 * 金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;

	/**
	 * 币别
	 */
	@Column(name = "Currency")
	@JsonProperty(value = "Currency")
	private String Currency;
	
	/**
	 * 台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
    @JoinColumn(name = "LegalId", insertable = false, updatable = false,foreignKey = @ForeignKey(name="none"))
    @Fetch(FetchMode.SELECT)
    @NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;
    
    /**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	public String getCreatedId() {
		return CreatedId;
	}
	
	/**
	 * 备注
	 */
	@Column(name = "Comments")
	@JsonProperty(value = "Comments")
	private String Comments;

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
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

	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	
	
	public List<StorageFeeDetail> getStorageFeeDetail() {
		return StorageFeeDetail;
	}

	public void setStorageFeeDetail(List<StorageFeeDetail> storageFeeDetail) {
		StorageFeeDetail = storageFeeDetail;
	}

	public String getFeeCode() {
		return FeeCode;
	}

	public void setFeeCode(String feeCode) {
		FeeCode = feeCode;
	}

	public String getWarehouseId() {
		return WarehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		WarehouseId = warehouseId;
	}

	public Warehouse getWarehouse() {
		return Warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		Warehouse = warehouse;
	}

	public String getFeeType() {
		return FeeType;
	}

	public void setFeeType(String feeType) {
		FeeType = feeType;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}

	public Legal getLegal() {
		return Legal;
	}

	public void setLegal(Legal legal) {
		Legal = legal;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	/**
	 * 开始日期
	 */
	@Column(name = "StartDate")
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	
	/**
	 * 截止日期
	 */
	@Column(name = "EndDate")
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
}
