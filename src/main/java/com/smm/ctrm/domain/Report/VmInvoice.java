

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class VmInvoice extends HibernateEntity {
	private static final long serialVersionUID = 1461719402488L;
	/**
	 * 客户名称（往来单位）
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 批次编号{0, 10,20,.....}
	 */
	@JsonProperty(value = "LotNo")
	private Integer LotNo;
	/**
	 * 合同数量
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 * 已经点价的数量之和 
	 */
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 结算日期 --- 原始的日期。在新增记录时，COPY一个值给这个字段，供将来可能需要的查询
	 */
	@JsonProperty(value = "OriginalQP")
	private Date OriginalQP;
	/**
	 * 抵押物是现金时，是多少金额（保证金，Fund表中Sum而来） 
	 */
	@JsonProperty(value = "DepositAmount")
	private BigDecimal DepositAmount;
	/**
	 * 单据号
	 */
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;
	/**
	 * 出具日期 
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 发票数量 
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 价格 
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 合同约定的升贴水
	 */
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public Integer getLotNo(){
		return LotNo;
	}
	public void setLotNo(Integer LotNo){
		this.LotNo=LotNo;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getQuantityPriced(){
		return QuantityPriced;
	}
	public void setQuantityPriced(BigDecimal QuantityPriced){
		this.QuantityPriced=QuantityPriced;
	}
	
	public Date getOriginalQP(){
		return OriginalQP;
	}
	public void setOriginalQP(Date OriginalQP){
		this.OriginalQP=OriginalQP;
	}
	
	public BigDecimal getDepositAmount(){
		return DepositAmount;
	}
	public void setDepositAmount(BigDecimal DepositAmount){
		this.DepositAmount=DepositAmount;
	}
	
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

}