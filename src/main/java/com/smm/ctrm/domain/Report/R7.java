

package com.smm.ctrm.domain.Report;

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Physical.Lot;
@Entity
@Table(name = "Invoice", schema = "Physical")
public class R7 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402487L;
	/**
	 * 单据号 InvoiceNo = Prefix + SerialNo + Suffix
	 */
	@Column(name = "InvoiceNo", length = 64)
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 * 出具日期 
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 数量 
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 价格 
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 合同约定的升贴水
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * 已点价格（不含升贴水）
	 */
	@Transient
	@JsonProperty(value = "IsPriced")
	private BigDecimal IsPriced;
	/**
	 * 合同号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@Transient
	@JsonProperty(value = "LotFullNo")
	private String LotFullNo;
	/**
	 * 合同数量
	 */
	@Transient
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 * 已点数量
	 */
	@Transient
	@JsonProperty(value = "LotQuantityPriced")
	private BigDecimal LotQuantityPriced;
	/**
	 * 往来单位
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 原QP
	 */
	@Transient
	@JsonProperty(value = "LotQP")
	private Date LotQP;
	
	/**
	 * 属于哪个批次
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;

	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;
	
	
	/**
	 * 客户标识
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	@ManyToOne(fetch = FetchType.LAZY, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	
	public String getInvoiceNo(){
		return InvoiceNo;
	}
	public void setInvoiceNo(String InvoiceNo){
		this.InvoiceNo=InvoiceNo;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getPremium(){
		return Premium;
	}
	public void setPremium(BigDecimal Premium){
		this.Premium=Premium;
	}
	
	public String getLotFullNo(){
		return LotFullNo;
	}
	public void setLotFullNo(String LotFullNo){
		this.LotFullNo=LotFullNo;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getLotQuantityPriced(){
		return LotQuantityPriced;
	}
	public void setLotQuantityPriced(BigDecimal LotQuantityPriced){
		this.LotQuantityPriced=LotQuantityPriced;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public Date getLotQP(){
		return LotQP;
	}
	public void setLotQP(Date LotQP){
		this.LotQP=LotQP;
	}
	public BigDecimal getIsPriced() {
		return Price.subtract(Premium != null ? Premium : BigDecimal.ZERO);
	}
	public String getLotId() {
		return LotId;
	}
	public void setLotId(String lotId) {
		LotId = lotId;
	}
	public Lot getLot() {
		return Lot;
	}
	public void setLot(Lot lot) {
		Lot = lot;
	}
	public void setIsPriced(BigDecimal isPriced) {
		IsPriced = isPriced;
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

}