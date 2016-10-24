

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;

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
@Entity
@Table(name = "CustomerBalance_New", schema="Basis")
public class CustomerBalance_new extends HibernateEntity {
	private static final long serialVersionUID = 1461832991340L;
	
	/**
	 *  期初余额
	 */
	@Column(name = "InitBalance")
	@JsonProperty(value = "InitBalance")
	private BigDecimal InitBalance;
	/**
	 *  应收
	 */
	@Column(name = "Receipt")
	@JsonProperty(value = "Receipt")
	private BigDecimal Receipt;
	/**
	 *  应付
	 */
	@Column(name = "Pay")
	@JsonProperty(value = "Pay")
	private BigDecimal Pay;
	/**
	 *  应收(暂估)
	 */
	@Column(name = "BudgetReceipt")
	@JsonProperty(value = "BudgetReceipt")
	private BigDecimal BudgetReceipt;
	
	/**
	 *  应付(暂估)
	 */
	@Column(name = "BudgetPay")
	@JsonProperty(value = "BudgetPay")
	private BigDecimal BudgetPay;
	
	/**
	 *  实收 C
	 */
	@Column(name = "RealReceipt")
	@JsonProperty(value = "RealReceipt")
	private BigDecimal RealReceipt;
	
	/**
	 *  实付 D
	 */
	@Column(name = "RealPay")
	@JsonProperty(value = "RealPay")
	private BigDecimal RealPay;
	
	/**
	 *  期末余额（含暂估） = 期初余额 + (应收-实收)  - (应付 -实付） + 应收（暂估） - 应付（暂估）
	 */
	@Column(name = "BudgetEndingBalance")
	@JsonProperty(value = "BudgetEndingBalance")
	private BigDecimal BudgetEndingBalance;
	
	/**
	 *  期末余额 = 期初余额 + (应收-实收)  - (应付 -实付）
	 */
	@Column(name = "EndingBalance")
	@JsonProperty(value = "EndingBalance")
	private BigDecimal EndingBalance;
	
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 客户
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/*@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)*/
	@Transient
	@JsonProperty(value = "Customer")
	private Customer Customer;
	public Customer getCustomer() {
		return Customer;
	}
	public void setCustomer(Customer customer) {
		Customer = customer;
	}
	
	@Transient
	@JsonProperty(value = "Legal")
	private Legal Legal;
	public Legal getLegal() {
		return Legal;
	}
	public void setLegal(Legal legal) {
		Legal = legal;
	}

	/**
	 *  内部台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	/*@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;*/
	
	
	public BigDecimal getInitBalance() {
		return InitBalance;
	}
	public void setInitBalance(BigDecimal initBalance) {
		InitBalance = initBalance;
	}
	public BigDecimal getReceipt() {
		return Receipt!=null?Receipt:BigDecimal.ZERO;
	}
	public void setReceipt(BigDecimal receipt) {
		Receipt = receipt;
	}
	public BigDecimal getPay() {
		return Pay!=null?Pay:BigDecimal.ZERO;
	}
	public void setPay(BigDecimal pay) {
		Pay = pay;
	}
	public BigDecimal getBudgetReceipt() {
		return BudgetReceipt!=null?BudgetReceipt:BigDecimal.ZERO;
	}
	public void setBudgetReceipt(BigDecimal budgetReceipt) {
		BudgetReceipt = budgetReceipt;
	}
	public BigDecimal getBudgetPay() {
		return BudgetPay!=null?BudgetPay:BigDecimal.ZERO;
	}
	public void setBudgetPay(BigDecimal budgetPay) {
		BudgetPay = budgetPay;
	}
	public BigDecimal getRealReceipt() {
		return RealReceipt!=null?RealReceipt:BigDecimal.ZERO;
	}
	public void setRealReceipt(BigDecimal realReceipt) {
		RealReceipt = realReceipt;
	}
	public BigDecimal getRealPay() {
		return RealPay!=null?RealPay:BigDecimal.ZERO;
	}
	public void setRealPay(BigDecimal realPay) {
		RealPay = realPay;
	}
	public BigDecimal getBudgetEndingBalance() {
		return BudgetEndingBalance!=null?BudgetEndingBalance:BigDecimal.ZERO;
	}
	public void setBudgetEndingBalance(BigDecimal budgetEndingBalance) {
		BudgetEndingBalance = budgetEndingBalance;
	}
	public BigDecimal getEndingBalance() {
		return EndingBalance!=null?EndingBalance:BigDecimal.ZERO;
	}
	public void setEndingBalance(BigDecimal endingBalance) {
		EndingBalance = endingBalance;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
	}
	public String getCustomerId() {
		return CustomerId;
	}
	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	public String getLegalId() {
		return LegalId;
	}
	public void setLegalId(String legalId) {
		LegalId = legalId;
	}
}