package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "StorageFeeDetail", schema = "Physical")
public class StorageFeeDetail extends HibernateEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transient
	@JsonProperty(value = "StorageDays")
	private Integer StorageDays;
	
	@Transient
	@JsonProperty(value = "ApportionPrice")
	private BigDecimal ApportionPrice;
	
	/**
	 * 费用ID
	 */
	@Column(name = "StorageFeeId")
	@JsonProperty(value = "StorageFeeId")
	private String StorageFeeId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = StorageFee.class)
	@JoinColumn(name = "StorageFeeId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "StorageFee")
	private StorageFee StorageFee;
	
	/**
	 * 货物明细Id
	 */
	@Column(name = "StorageId")
	@JsonProperty(value = "StorageId")
	private String StorageId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	@JoinColumn(name = "StorageId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Storage")
	private Storage Storage;
	
	/**
	 * 费用
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	
	public Integer getStorageDays() {
		return StorageDays;
	}

	public void setStorageDays(Integer storageDays) {
		StorageDays = storageDays;
	}

	public BigDecimal getApportionPrice() {
		return ApportionPrice;
	}

	public void setApportionPrice(BigDecimal apportionPrice) {
		ApportionPrice = apportionPrice;
	}

	public String getStorageFeeId() {
		return StorageFeeId;
	}

	public void setStorageFeeId(String storageFeeId) {
		StorageFeeId = storageFeeId;
	}

	public StorageFee getStorageFee() {
		return StorageFee;
	}

	public void setStorageFee(StorageFee storageFee) {
		StorageFee = storageFee;
	}

	public String getStorageId() {
		return StorageId;
	}

	public void setStorageId(String storageId) {
		StorageId = storageId;
	}

	public Storage getStorage() {
		return Storage;
	}

	public void setStorage(Storage storage) {
		Storage = storage;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal price) {
		Price = price;
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
}
