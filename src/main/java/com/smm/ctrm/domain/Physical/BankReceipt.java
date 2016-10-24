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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;

/**
 * @author zhaoyutao
 *
 */
@Entity
@Table(name = "BankReceipt", schema = "Physical")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BankReceipt extends HibernateEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3048392333755971241L;

	/**
	 * 银行水单号
	 */
	@Column(name = "BankReceiptNo")
	@JsonProperty(value = "BankReceiptNo")
	private String BankReceiptNo;

	/**
	 * 客户ID
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 客户台头ID
	 */
	@Column(name = "CustomerTitleId")
	@JsonProperty(value = "CustomerTitleId")
	private String CustomerTitleId;

	/**
	 * 水单金额
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;

	/**
	 * 已认领金额
	 */
	@Column(name = "ConfirmedAmount")
	@JsonProperty(value = "ConfirmedAmount")
	private BigDecimal ConfirmedAmount;

	/**
	 * 未认领金额
	 */
	@Column(name = "UnConfirmAmount")
	@JsonProperty(value = "UnConfirmAmount")
	private BigDecimal UnConfrimAmount;

	/**
	 * 货币币种
	 */
	@Column(name = "Currency")
	@JsonProperty(value = "Currency")
	private String Currency;

	/**
	 * 收款日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 付款银行
	 */
	@Column(name = "PaymentBankId")
	@JsonProperty(value = "PaymentBankId")
	private String PaymentBankId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Bank.class)
	@JoinColumn(name = "PaymentBankId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "PayBank")
	private Bank PayBank;

	/**
	 * 收款银行
	 */
	@Column(name = "DebitBankId")
	@JsonProperty(value = "DebitBankId")
	private String DebitBankId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Bank.class)
	@JoinColumn(name = "DebitBankId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "DebitBank")
	private Bank DebitBank;

	/**
	 * 付款账号
	 */
	@Column(name = "PaymentBankAccount")
	@JsonProperty(value = "PaymentBankAccount")
	private String PaymentBankAccount;

	/**
	 * 水单状态
	 */
	@Column(name = "Status")
	@JsonProperty(value = "Status")
	private Integer Status;

	/**
	 * 创建者
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "User")
	private User User;

	/**
	 * 更新者
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	
	/**
	 * 水单导入时使用
	 */
	@Transient
	@JsonProperty(value = "IsSelect")
	private Boolean IsSelect;

	public String getBankReceiptNo() {
		return BankReceiptNo;
	}

	public void setBankReceiptNo(String bankReceiptNo) {
		BankReceiptNo = bankReceiptNo;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer customer) {
		Customer = customer;
	}

	public String getCustomerTitleId() {
		return CustomerTitleId;
	}

	public void setCustomerTitleId(String customerTitleId) {
		CustomerTitleId = customerTitleId;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal amount) {
		Amount = amount;
	}

	public BigDecimal getConfirmedAmount() {
		return ConfirmedAmount;
	}

	public void setConfirmedAmount(BigDecimal confirmedAmount) {
		ConfirmedAmount = confirmedAmount;
	}

	public BigDecimal getUnConfrimAmount() {
		return UnConfrimAmount;
	}

	public void setUnConfrimAmount(BigDecimal unConfrimAmount) {
		UnConfrimAmount = unConfrimAmount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getPaymentBankId() {
		return PaymentBankId;
	}

	public void setPaymentBankId(String paymentBankId) {
		PaymentBankId = paymentBankId;
	}

	public Bank getPayBank() {
		return PayBank;
	}

	public void setPayBank(Bank payBank) {
		PayBank = payBank;
	}

	public String getDebitBankId() {
		return DebitBankId;
	}

	public void setDebitBankId(String debitBankId) {
		DebitBankId = debitBankId;
	}

	public Bank getDebitBank() {
		return DebitBank;
	}

	public void setDebitBank(Bank debitBank) {
		DebitBank = debitBank;
	}

	public String getPaymentBankAccount() {
		return PaymentBankAccount;
	}

	public void setPaymentBankAccount(String paymentBankAccount) {
		PaymentBankAccount = paymentBankAccount;
	}

	public Integer getStatus() {
		return Status;
	}

	public void setStatus(Integer status) {
		Status = status;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	public User getUser() {
		return User;
	}

	public void setUser(User user) {
		User = user;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String updatedId) {
		UpdatedId = updatedId;
	}

	public Boolean getIsSelect() {
		return IsSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		IsSelect = isSelect;
	}

}
