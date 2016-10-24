package com.smm.ctrm.domain.Physical;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.SimpleEntity;

@Entity
@Table(name = "InvoiceStorage", schema = "Physical")
public class InvoiceStorage extends SimpleEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1872726855130448538L;

	public InvoiceStorage() {
			super();
		}

	public InvoiceStorage(String invoiceId, String storageId) {
			super();
			InvoiceId = invoiceId;
			StorageId = storageId;
		}

	/**
	 * FinishedProduct表的id
	 */
	@Column(name = "InvoiceId")
	@JsonProperty("InvoiceId")
	private String InvoiceId;

	/**
	 * 商品明细id，对应Storage表
	 */
	@Column(name = "StorageId")
	@JsonProperty("StorageId")
	private String StorageId;

	public String getStorageId() {
		return StorageId;
	}

	public void setStorageId(String StorageId) {
		this.StorageId = StorageId;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String invoiceId) {
		InvoiceId = invoiceId;
	}


}