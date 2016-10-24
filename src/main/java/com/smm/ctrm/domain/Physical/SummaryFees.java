
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "SummaryFees", schema = "Physical")

public class SummaryFees extends HibernateEntity {
	private static final long serialVersionUID = 1461832991346L;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 费用名称
	 */
	@Column(name = "FeeName")
	@JsonProperty(value = "FeeName")
	private String FeeName;
	/**
	 * 费用的类别
	 */
	@Column(name = "FeeCode")
	@JsonProperty(value = "FeeCode")
	private String FeeCode;
	/**
	 * 币种
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 总净重（数量）,选择的出入库明的总净重
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 总价，AmountDone/Quantity
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 实际发生的费用的金额 --- 由系统自动统计
	 */
	@Column(name = "AmountDone")
	@JsonProperty(value = "AmountDone")
	private BigDecimal AmountDone;
	/**
	 * 计量单位
	 */
	@Column(name = "WeightUnit")
	@JsonProperty(value = "WeightUnit")
	private String WeightUnit;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 发票ID
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
	/**
	 * 关联的发票
	 */
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	/**
	 * 多对多：杂费合计 - 商品明细
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Storage.class)
	@JoinTable(name = "SummFeeStorage", schema = "Physical", joinColumns = {
			@JoinColumn(name = "SummaryFeesId") }, inverseJoinColumns = {
					@JoinColumn(name = "StorageId") }, foreignKey = @ForeignKey(name = "none"))
	@Cascade(CascadeType.SAVE_UPDATE)
	@Fetch(FetchMode.SUBSELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Storages")
	private List<Storage> Storages;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getFeeName() {
		return FeeName;
	}

	public void setFeeName(String FeeName) {
		this.FeeName = FeeName;
	}

	public String getFeeCode() {
		return FeeCode;
	}

	public void setFeeCode(String FeeCode) {
		this.FeeCode = FeeCode;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public BigDecimal getAmountDone() {
		return AmountDone;
	}

	public void setAmountDone(BigDecimal AmountDone) {
		this.AmountDone = AmountDone;
	}

	public String getWeightUnit() {
		return WeightUnit;
	}

	public void setWeightUnit(String WeightUnit) {
		this.WeightUnit = WeightUnit;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public String getInvoiceId() {
		return InvoiceId;
	}

	public void setInvoiceId(String InvoiceId) {
		this.InvoiceId = InvoiceId;
	}

	public Invoice getInvoice() {
		return Invoice;
	}

	public void setInvoice(Invoice Invoice) {
		this.Invoice = Invoice;
	}

	public List<Storage> getStorages() {
		return Storages;
	}

	public void setStorages(List<Storage> Storages) {
		this.Storages = Storages;
	}

}