
package com.smm.ctrm.domain.Physical;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Attachment", schema = "Physical")

public class vAttachment extends HibernateEntity {
	private static final long serialVersionUID = 1461832991339L;
	/**
	 * 上传日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 文件名称
	 */
	@Column(name = "FileName")
	@JsonProperty(value = "FileName")
	private String FileName;
	/**
	 * 文件路径
	 */
	@Column(name = "FileUrl")
	@JsonProperty(value = "FileUrl")
	private String FileUrl;
	/**
	 * 是否为外部文档
	 */
	@Column(name = "IsOutDocument")
	@JsonProperty(value = "IsOutDocument")
	private Boolean IsOutDocument;
	/**
	 * 是哪个客户的
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 *
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	/**
	 *
	 */
	@Column(name = "StorageId")
	@JsonProperty(value = "StorageId")
	private String StorageId;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	/**
	 *
	 */
	@Column(name = "FundId")
	@JsonProperty(value = "FundId")
	private String FundId;
	/**
	 *
	 */
	@Column(name = "PricingId")
	@JsonProperty(value = "PricingId")
	private String PricingId;
	/**
	 *
	 */
	@Column(name = "PositionId")
	@JsonProperty(value = "PositionId")
	private String PositionId;
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
	public Date getTradeDate() {
		return TradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	public String getFileUrl() {
		return FileUrl;
	}
	public void setFileUrl(String fileUrl) {
		FileUrl = fileUrl;
	}
	public Boolean getIsOutDocument() {
		return IsOutDocument;
	}
	public void setIsOutDocument(Boolean isOutDocument) {
		IsOutDocument = isOutDocument;
	}
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	public String getContractId() {
		return ContractId;
	}
	public void setContractId(String contractId) {
		ContractId = contractId;
	}
	public String getStorageId() {
		return StorageId;
	}
	public void setStorageId(String storageId) {
		StorageId = storageId;
	}
	public String getInvoiceId() {
		return InvoiceId;
	}
	public void setInvoiceId(String invoiceId) {
		InvoiceId = invoiceId;
	}
	public String getFundId() {
		return FundId;
	}
	public void setFundId(String fundId) {
		FundId = fundId;
	}
	public String getPricingId() {
		return PricingId;
	}
	public void setPricingId(String pricingId) {
		PricingId = pricingId;
	}
	public String getPositionId() {
		return PositionId;
	}
	public void setPositionId(String positionId) {
		PositionId = positionId;
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