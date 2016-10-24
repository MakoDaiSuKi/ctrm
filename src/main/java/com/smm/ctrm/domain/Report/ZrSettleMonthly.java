

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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
@Entity
@Table(name = "ZrSettleMonthly", schema="Report")

public class ZrSettleMonthly extends HibernateEntity {
	private static final long serialVersionUID = 1461719402488L;
	/**
	 *
	 */
	@Column(name = "StartDate")
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	/**
	 *
	 */
	@Column(name = "EndDate")
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	/**
	 *
	 */
	@Column(name = "PreQuantity")
	@JsonProperty(value = "PreQuantity")
	private BigDecimal PreQuantity;
	/**
	 *
	 */
	@Column(name = "PrePrice")
	@JsonProperty(value = "PrePrice")
	private BigDecimal PrePrice;
	/**
	 *
	 */
	@Column(name = "PreAmount")
	@JsonProperty(value = "PreAmount")
	private BigDecimal PreAmount;
	/**
	 *
	 */
	@Column(name = "QuantityBuy")
	@JsonProperty(value = "QuantityBuy")
	private BigDecimal QuantityBuy;
	/**
	 *
	 */
	@Column(name = "PriceBuy")
	@JsonProperty(value = "PriceBuy")
	private BigDecimal PriceBuy;
	/**
	 *
	 */
	@Column(name = "AmountBuy")
	@JsonProperty(value = "AmountBuy")
	private BigDecimal AmountBuy;
	/**
	 *
	 */
	@Column(name = "QuantitySell")
	@JsonProperty(value = "QuantitySell")
	private BigDecimal QuantitySell;
	/**
	 *
	 */
	@Column(name = "PriceSell")
	@JsonProperty(value = "PriceSell")
	private BigDecimal PriceSell;
	/**
	 *
	 */
	@Column(name = "AmountSell")
	@JsonProperty(value = "AmountSell")
	private BigDecimal AmountSell;
	/**
	 *
	 */
	@Column(name = "QuantityBalance")
	@JsonProperty(value = "QuantityBalance")
	private BigDecimal QuantityBalance;
	/**
	 *
	 */
	@Column(name = "PriceSettle")
	@JsonProperty(value = "PriceSettle")
	private BigDecimal PriceSettle;
	/**
	 *
	 */
	@Column(name = "AmountBalance")
	@JsonProperty(value = "AmountBalance")
	private BigDecimal AmountBalance;
	/**
	 *
	 */
	@Column(name = "PnL")
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 内部抬头标识
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("ZrSettleMonthly_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 客户标识
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
//	@JsonBackReference("ZrSettleMonthly_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 品种标识
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
//	@JsonBackReference("ZrSettleMonthly_Commodity")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	public Date getStartDate(){
		return StartDate;
	}
	public void setStartDate(Date StartDate){
		this.StartDate=StartDate;
	}
	
	public Date getEndDate(){
		return EndDate;
	}
	public void setEndDate(Date EndDate){
		this.EndDate=EndDate;
	}
	
	public BigDecimal getPreQuantity(){
		return PreQuantity;
	}
	public void setPreQuantity(BigDecimal PreQuantity){
		this.PreQuantity=PreQuantity;
	}
	
	public BigDecimal getPrePrice(){
		return PrePrice;
	}
	public void setPrePrice(BigDecimal PrePrice){
		this.PrePrice=PrePrice;
	}
	
	public BigDecimal getPreAmount(){
		return PreAmount;
	}
	public void setPreAmount(BigDecimal PreAmount){
		this.PreAmount=PreAmount;
	}
	
	public BigDecimal getQuantityBuy(){
		return QuantityBuy;
	}
	public void setQuantityBuy(BigDecimal QuantityBuy){
		this.QuantityBuy=QuantityBuy;
	}
	
	public BigDecimal getPriceBuy(){
		return PriceBuy;
	}
	public void setPriceBuy(BigDecimal PriceBuy){
		this.PriceBuy=PriceBuy;
	}
	
	public BigDecimal getAmountBuy(){
		return AmountBuy;
	}
	public void setAmountBuy(BigDecimal AmountBuy){
		this.AmountBuy=AmountBuy;
	}
	
	public BigDecimal getQuantitySell(){
		return QuantitySell;
	}
	public void setQuantitySell(BigDecimal QuantitySell){
		this.QuantitySell=QuantitySell;
	}
	
	public BigDecimal getPriceSell(){
		return PriceSell;
	}
	public void setPriceSell(BigDecimal PriceSell){
		this.PriceSell=PriceSell;
	}
	
	public BigDecimal getAmountSell(){
		return AmountSell;
	}
	public void setAmountSell(BigDecimal AmountSell){
		this.AmountSell=AmountSell;
	}
	
	public BigDecimal getQuantityBalance(){
		return QuantityBalance;
	}
	public void setQuantityBalance(BigDecimal QuantityBalance){
		this.QuantityBalance=QuantityBalance;
	}
	
	public BigDecimal getPriceSettle(){
		return PriceSettle;
	}
	public void setPriceSettle(BigDecimal PriceSettle){
		this.PriceSettle=PriceSettle;
	}
	
	public BigDecimal getAmountBalance(){
		return AmountBalance;
	}
	public void setAmountBalance(BigDecimal AmountBalance){
		this.AmountBalance=AmountBalance;
	}
	
	public BigDecimal getPnL(){
		return PnL;
	}
	public void setPnL(BigDecimal PnL){
		this.PnL=PnL;
	}
	
	public String getLegalId(){
		return LegalId;
	}
	public void setLegalId(String LegalId){
		this.LegalId=LegalId;
	}
	
	public Legal getLegal(){
		return Legal;
	}
	public void setLegal(Legal Legal){
		this.Legal=Legal;
	}
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public Customer getCustomer(){
		return Customer;
	}
	public void setCustomer(Customer Customer){
		this.Customer=Customer;
	}
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public Commodity getCommodity(){
		return Commodity;
	}
	public void setCommodity(Commodity Commodity){
		this.Commodity=Commodity;
	}

}