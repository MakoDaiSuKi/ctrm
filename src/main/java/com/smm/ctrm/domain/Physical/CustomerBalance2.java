
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

public class CustomerBalance2 extends HibernateEntity {
	private static final long serialVersionUID = 1461832991340L;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	/**
	 *
	 */
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	/**
	 *
	 */
	@JsonProperty(value = "InitInvoiceQutnatiy")
	private BigDecimal InitInvoiceQutnatiy;
	/**
	 *
	 */
	@JsonProperty(value = "InitInvoiceAmount")
	private BigDecimal InitInvoiceAmount;
	/**
	 *
	 */
	@JsonProperty(value = "InitFundAmount")
	private BigDecimal InitFundAmount;
	/**
	 *
	 */
	@JsonProperty(value = "InitDiffAmount")
	private BigDecimal InitDiffAmount;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceQutnatiy")
	private BigDecimal InvoiceQutnatiy;
	/**
	 *
	 */
	@JsonProperty(value = "InvoiceAmount")
	private BigDecimal InvoiceAmount;
	/**
	 *
	 */
	@JsonProperty(value = "FundAmount")
	private BigDecimal FundAmount;
	/**
	 *
	 */
	@JsonProperty(value = "DiffAmount")
	private BigDecimal DiffAmount;
	/**
	 *
	 */
	@JsonProperty(value = "YearInvoiceQutnatiy")
	private BigDecimal YearInvoiceQutnatiy;
	/**
	 *
	 */
	@JsonProperty(value = "YearInvoiceAmount")
	private BigDecimal YearInvoiceAmount;
	/**
	 *
	 */
	@JsonProperty(value = "YearFundAmount")
	private BigDecimal YearFundAmount;
	/**
	 *
	 */
	@JsonProperty(value = "YearDiffAmount")
	private BigDecimal YearDiffAmount;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerBalanceDetails")
	private List<CustomerBalanceDetail> CustomerBalanceDetails;

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date StartDate) {
		this.StartDate = StartDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date EndDate) {
		this.EndDate = EndDate;
	}

	public BigDecimal getInitInvoiceQutnatiy() {
		return InitInvoiceQutnatiy;
	}

	public void setInitInvoiceQutnatiy(BigDecimal InitInvoiceQutnatiy) {
		this.InitInvoiceQutnatiy = InitInvoiceQutnatiy;
	}

	public BigDecimal getInitInvoiceAmount() {
		return InitInvoiceAmount;
	}

	public void setInitInvoiceAmount(BigDecimal InitInvoiceAmount) {
		this.InitInvoiceAmount = InitInvoiceAmount;
	}

	public BigDecimal getInitFundAmount() {
		return InitFundAmount;
	}

	public void setInitFundAmount(BigDecimal InitFundAmount) {
		this.InitFundAmount = InitFundAmount;
	}

	public BigDecimal getInitDiffAmount() {
		return InitDiffAmount;
	}

	public void setInitDiffAmount(BigDecimal InitDiffAmount) {
		this.InitDiffAmount = InitDiffAmount;
	}

	public BigDecimal getInvoiceQutnatiy() {
		return InvoiceQutnatiy;
	}

	public void setInvoiceQutnatiy(BigDecimal InvoiceQutnatiy) {
		this.InvoiceQutnatiy = InvoiceQutnatiy;
	}

	public BigDecimal getInvoiceAmount() {
		return InvoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal InvoiceAmount) {
		this.InvoiceAmount = InvoiceAmount;
	}

	public BigDecimal getFundAmount() {
		return FundAmount;
	}

	public void setFundAmount(BigDecimal FundAmount) {
		this.FundAmount = FundAmount;
	}

	public BigDecimal getDiffAmount() {
		return DiffAmount;
	}

	public void setDiffAmount(BigDecimal DiffAmount) {
		this.DiffAmount = DiffAmount;
	}

	public BigDecimal getYearInvoiceQutnatiy() {
		return YearInvoiceQutnatiy;
	}

	public void setYearInvoiceQutnatiy(BigDecimal YearInvoiceQutnatiy) {
		this.YearInvoiceQutnatiy = YearInvoiceQutnatiy;
	}

	public BigDecimal getYearInvoiceAmount() {
		return YearInvoiceAmount;
	}

	public void setYearInvoiceAmount(BigDecimal YearInvoiceAmount) {
		this.YearInvoiceAmount = YearInvoiceAmount;
	}

	public BigDecimal getYearFundAmount() {
		return YearFundAmount;
	}

	public void setYearFundAmount(BigDecimal YearFundAmount) {
		this.YearFundAmount = YearFundAmount;
	}

	public BigDecimal getYearDiffAmount() {
		return YearDiffAmount;
	}

	public void setYearDiffAmount(BigDecimal YearDiffAmount) {
		this.YearDiffAmount = YearDiffAmount;
	}

	public List<CustomerBalanceDetail> getCustomerBalanceDetails() {
		return CustomerBalanceDetails;
	}

	public void setCustomerBalanceDetails(List<CustomerBalanceDetail> CustomerBalanceDetails) {
		this.CustomerBalanceDetails = CustomerBalanceDetails;
	}

}