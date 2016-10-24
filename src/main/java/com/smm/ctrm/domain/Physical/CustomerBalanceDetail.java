
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class CustomerBalanceDetail extends HibernateEntity {
	private static final long serialVersionUID = 1461832991340L;
	/**
	 *
	 */
	@JsonProperty(value = "ContractNo")
	private String ContractNo;
	/**
	 *
	 */
	@JsonProperty(value = "LotNo")
	private String LotNo;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 *
	 */
	@JsonProperty(value = "Brand")
	private String Brand;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceDate")
	private Date InvoiceDate;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceQuantity")
	private BigDecimal InvoiceQuantity;
	/**
	 *
	 */
	@JsonProperty(value = "InvoicePrice")
	private BigDecimal InvoicePrice;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceMajor")
	private BigDecimal InvoiceMajor;
	/**
	 *
	 */
	@JsonProperty(value = "InovicePremium")
	private BigDecimal InovicePremium;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceAmount")
	private BigDecimal InvoiceAmount;
	/**
	 *
	 */
	@JsonProperty(value = "FundAmount1")
	private BigDecimal FundAmount1;
	/**
	 *
	 */
	@JsonProperty(value = "FundAmount2")
	private BigDecimal FundAmount2;
	/**
	 *
	 */
	@JsonProperty(value = "DiffAmount")
	private BigDecimal DiffAmount;
	/**
	 *
	 */
	@JsonProperty(value = "FundDate1")
	private Date FundDate1;
	/**
	 *
	 */
	@JsonProperty(value = "FundDate2")
	private Date FundDate2;
	/**
	 *
	 */
	@JsonProperty(value = "Comment")
	private String Comment;

	public String getContractNo() {
		return ContractNo;
	}

	public void setContractNo(String ContractNo) {
		this.ContractNo = ContractNo;
	}

	public String getLotNo() {
		return LotNo;
	}

	public void setLotNo(String LotNo) {
		this.LotNo = LotNo;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String InvoiceNo) {
		this.InvoiceNo = InvoiceNo;
	}

	public String getBrand() {
		return Brand;
	}

	public void setBrand(String Brand) {
		this.Brand = Brand;
	}

	public Date getInvoiceDate() {
		return InvoiceDate;
	}

	public void setInvoiceDate(Date InvoiceDate) {
		this.InvoiceDate = InvoiceDate;
	}

	public BigDecimal getInvoiceQuantity() {
		return InvoiceQuantity;
	}

	public void setInvoiceQuantity(BigDecimal InvoiceQuantity) {
		this.InvoiceQuantity = InvoiceQuantity;
	}

	public BigDecimal getInvoicePrice() {
		return InvoicePrice;
	}

	public void setInvoicePrice(BigDecimal InvoicePrice) {
		this.InvoicePrice = InvoicePrice;
	}

	public BigDecimal getInvoiceMajor() {
		return InvoiceMajor;
	}

	public void setInvoiceMajor(BigDecimal InvoiceMajor) {
		this.InvoiceMajor = InvoiceMajor;
	}

	public BigDecimal getInovicePremium() {
		return InovicePremium;
	}

	public void setInovicePremium(BigDecimal InovicePremium) {
		this.InovicePremium = InovicePremium;
	}

	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal InvoiceAmount) {
		this.InvoiceAmount = InvoiceAmount;
	}

	public BigDecimal getFundAmount1() {
		return FundAmount1 != null ? FundAmount1 : new BigDecimal(0);
	}

	public void setFundAmount1(BigDecimal FundAmount1) {
		this.FundAmount1 = FundAmount1;
	}

	public BigDecimal getFundAmount2() {
		return FundAmount2 != null ? FundAmount2 : new BigDecimal(0);
	}

	public void setFundAmount2(BigDecimal FundAmount2) {
		this.FundAmount2 = FundAmount2;
	}

	public BigDecimal getDiffAmount() {
		return DiffAmount;
	}

	public void setDiffAmount(BigDecimal DiffAmount) {
		this.DiffAmount = DiffAmount;
	}

	public Date getFundDate1() {
		return FundDate1;
	}

	public void setFundDate1(Date FundDate1) {
		this.FundDate1 = FundDate1;
	}

	public Date getFundDate2() {
		return FundDate2;
	}

	public void setFundDate2(Date FundDate2) {
		this.FundDate2 = FundDate2;
	}

	public String getComment() {
		return Comment;
	}

	public void setComment(String Comment) {
		this.Comment = Comment;
	}

}