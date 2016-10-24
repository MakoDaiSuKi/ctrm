

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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
@Entity
@Table(name = "VmPnL4Spot", schema = "Report")

public class VmPnL4Spot extends HibernateEntity {
	private static final long serialVersionUID = 1461719402488L;
	/**
	 *
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 *
	 */
	@JsonProperty(value = "ContractFullNo")
	private String ContractFullNo;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerID")
	private String CustomerID;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@JsonProperty(value = "BrandName")
	private String BrandName;
	/**
	 *
	 */
	@JsonProperty(value = "CorporationName")
	private String CorporationName;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	/**
	 *
	 */
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 *
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 *
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 *
	 */
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 *
	 */
	@JsonProperty(value = "IsStoraged")
	private Boolean IsStoraged;
	/**
	 *
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 *
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
//	@JsonBackReference("VmPnL4Spot_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Lot")
	private Lot Lot;
	/**
	 * 属于哪个发票
	 */
	@Column(name = "InvoiceId")
	@JsonProperty(value = "InvoiceId")
	private String InvoiceId;
//	@JsonBackReference("VmPnL4Spot_Invoice")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Invoice.class)
	@JoinColumn(name = "InvoiceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Invoice")
	private Invoice Invoice;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getContractFullNo(){
		return ContractFullNo;
	}
	public void setContractFullNo(String ContractFullNo){
		this.ContractFullNo=ContractFullNo;
	}
	
	public String getCustomerID(){
		return CustomerID;
	}
	public void setCustomerID(String CustomerID){
		this.CustomerID=CustomerID;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getBrandName(){
		return BrandName;
	}
	public void setBrandName(String BrandName){
		this.BrandName=BrandName;
	}
	
	public String getCorporationName(){
		return CorporationName;
	}
	public void setCorporationName(String CorporationName){
		this.CorporationName=CorporationName;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantityDelivered(){
		return QuantityDelivered;
	}
	public void setQuantityDelivered(BigDecimal QuantityDelivered){
		this.QuantityDelivered=QuantityDelivered;
	}
	
	public BigDecimal getPremium(){
		return Premium;
	}
	public void setPremium(BigDecimal Premium){
		this.Premium=Premium;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getAmount(){
		return Amount;
	}
	public void setAmount(BigDecimal Amount){
		this.Amount=Amount;
	}
	
	public Boolean getIsPriced(){
		return IsPriced;
	}
	public void setIsPriced(Boolean IsPriced){
		this.IsPriced=IsPriced;
	}
	
	public Boolean getIsStoraged(){
		return IsStoraged;
	}
	public void setIsStoraged(Boolean IsStoraged){
		this.IsStoraged=IsStoraged;
	}
	
	public Boolean getIsInvoiced(){
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean IsInvoiced){
		this.IsInvoiced=IsInvoiced;
	}
	
	public BigDecimal getPnL(){
		return PnL;
	}
	public void setPnL(BigDecimal PnL){
		this.PnL=PnL;
	}
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public Lot getLot(){
		return Lot;
	}
	public void setLot(Lot Lot){
		this.Lot=Lot;
	}
	
	public String getInvoiceId(){
		return InvoiceId;
	}
	public void setInvoiceId(String InvoiceId){
		this.InvoiceId=InvoiceId;
	}
	
	public Invoice getInvoice(){
		return Invoice;
	}
	public void setInvoice(Invoice Invoice){
		this.Invoice=Invoice;
	}

}