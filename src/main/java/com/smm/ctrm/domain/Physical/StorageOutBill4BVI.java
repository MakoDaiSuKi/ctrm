
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class StorageOutBill4BVI extends HibernateEntity {
	private static final long serialVersionUID = 1461832991346L;
	/**
	 * 给指定的批次发货，一次可以发多个
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 品牌
	 */
	@JsonProperty(value = "BrandName")
	private String BrandName;
	/**
	 * 品名
	 */
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 采购发票号
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 * 采购批次号
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 数量（按照采购发票汇总销售发票货物数量）
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 单位
	 */
	@JsonProperty(value = "Unit")
	private String Unit;
	/**
	 * 采购发票单价（不含税单价）
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String BrandName) {
		this.BrandName = BrandName;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String Product) {
		this.Product = Product;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

}