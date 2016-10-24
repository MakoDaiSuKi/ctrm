

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
import com.smm.ctrm.domain.Basis.Customer;
@Entity
@Table(name = "Reconciliation", schema="Report")

public class Reconciliation extends HibernateEntity {
	private static final long serialVersionUID = 1461719402487L;
	/**
	 * 交易日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 应收应付的余额
	 */
	@Column(name = "CR")
	@JsonProperty(value = "CR")
	private BigDecimal CR;
	/**
	 * 盈亏（利润）
	 */
	@Column(name = "PnL")
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 库存金额（全部的库存金额）v1成员名称：StorageSum
	 */
	@Column(name = "AmountTotalStorage")
	@JsonProperty(value = "AmountTotalStorage")
	private BigDecimal AmountTotalStorage;
	/**
	 * 实际在仓库中的库存金额  v1成员名称：Storaged
	 */
	@Column(name = "AmountStoragedOnly")
	@JsonProperty(value = "AmountStoragedOnly")
	private BigDecimal AmountStoragedOnly;
	/**
	 * 在途库存的库存金额   v1成员名称：InTransit
	 */
	@Column(name = "AmountInTransitOnly")
	@JsonProperty(value = "AmountInTransitOnly")
	private BigDecimal AmountInTransitOnly;
	/**
	 * 名义库存的库存金额   v1成员名称：Notional
	 */
	@Column(name = "AmountNotionalOnly")
	@JsonProperty(value = "AmountNotionalOnly")
	private BigDecimal AmountNotionalOnly;
	/**
	 * 银行余额 - 由系统对资金流水进行计算、得出的余额
	 */
	@Column(name = "BankCalculated")
	@JsonProperty(value = "BankCalculated")
	private BigDecimal BankCalculated;
	/**
	 * 银行余额 - 实际录入系统的银行余额
	 */
	@Column(name = "BankActual")
	@JsonProperty(value = "BankActual")
	private BigDecimal BankActual;
	/**
	 * 包括利润在内的总计资产
	 */
	@Column(name = "AssetIncPnL")
	@JsonProperty(value = "AssetIncPnL")
	private BigDecimal AssetIncPnL;
	/**
	 * 正是通过这个值，检验每天的业务有否发生错误
	 */
	@Column(name = "AssetExcPnL")
	@JsonProperty(value = "AssetExcPnL")
	private BigDecimal AssetExcPnL;
	/**
	 * 客户
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
//	@JsonBackReference("Reconciliation_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public BigDecimal getCR(){
		return CR;
	}
	public void setCR(BigDecimal CR){
		this.CR=CR;
	}
	
	public BigDecimal getPnL(){
		return PnL;
	}
	public void setPnL(BigDecimal PnL){
		this.PnL=PnL;
	}
	
	public BigDecimal getAmountTotalStorage(){
		return AmountTotalStorage;
	}
	public void setAmountTotalStorage(BigDecimal AmountTotalStorage){
		this.AmountTotalStorage=AmountTotalStorage;
	}
	
	public BigDecimal getAmountStoragedOnly(){
		return AmountStoragedOnly;
	}
	public void setAmountStoragedOnly(BigDecimal AmountStoragedOnly){
		this.AmountStoragedOnly=AmountStoragedOnly;
	}
	
	public BigDecimal getAmountInTransitOnly(){
		return AmountInTransitOnly;
	}
	public void setAmountInTransitOnly(BigDecimal AmountInTransitOnly){
		this.AmountInTransitOnly=AmountInTransitOnly;
	}
	
	public BigDecimal getAmountNotionalOnly(){
		return AmountNotionalOnly;
	}
	public void setAmountNotionalOnly(BigDecimal AmountNotionalOnly){
		this.AmountNotionalOnly=AmountNotionalOnly;
	}
	
	public BigDecimal getBankCalculated(){
		return BankCalculated;
	}
	public void setBankCalculated(BigDecimal BankCalculated){
		this.BankCalculated=BankCalculated;
	}
	
	public BigDecimal getBankActual(){
		return BankActual;
	}
	public void setBankActual(BigDecimal BankActual){
		this.BankActual=BankActual;
	}
	
	public BigDecimal getAssetIncPnL(){
		return AssetIncPnL;
	}
	public void setAssetIncPnL(BigDecimal AssetIncPnL){
		this.AssetIncPnL=AssetIncPnL;
	}
	
	public BigDecimal getAssetExcPnL(){
		return AssetExcPnL;
	}
	public void setAssetExcPnL(BigDecimal AssetExcPnL){
		this.AssetExcPnL=AssetExcPnL;
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

}