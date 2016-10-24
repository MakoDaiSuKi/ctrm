
package com.smm.ctrm.domain.Physical;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Invoice2Storage", schema = "Physical")

public class Invoice2Storage extends HibernateEntity {
	private static final long serialVersionUID = 1461832991342L;
	/**
	 *
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	/**
	 *
	 */
	@Column(name = "StorageId")
	@JsonProperty(value = "StorageId")
	private String StorageId;
	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String InvoiceId) {
		this.InvoiceId = InvoiceId;
	}

	public String getStorageId() {
		return StorageId;
	}

	public void setStorageId(String StorageId) {
		this.StorageId = StorageId;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

}