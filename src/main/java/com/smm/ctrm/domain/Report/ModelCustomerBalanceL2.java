

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class ModelCustomerBalanceL2 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402484L;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@JsonProperty(value = "LegalId")
	private String LegalId;
	/**
	 *
	 */
	@JsonProperty(value = "LegalName")
	private String LegalName;
	/**
	 *
	 */
	@JsonProperty(value = "SumInitBalance")
	private BigDecimal SumInitBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumFund")
	private BigDecimal SumFund;
	/**
	 *
	 */
	@JsonProperty(value = "VirtualLastBalance")
	private BigDecimal VirtualLastBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumLastBalance")
	private BigDecimal SumLastBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumLotInvoiced")
	private BigDecimal SumLotInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumLotUnInvoiced")
	private BigDecimal SumLotUnInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumLotInvoicedMinusSumFund")
	private BigDecimal SumLotInvoicedMinusSumFund;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerInitBalance")
	private BigDecimal CustomerInitBalance;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerLastBalance")
	private BigDecimal CustomerLastBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumDueBalance4Purchase")
	private BigDecimal SumDueBalance4Purchase;
	/**
	 *
	 */
	@JsonProperty(value = "SumDueBalance4Sell")
	private BigDecimal SumDueBalance4Sell;
	/**
	 *
	 */
	@JsonProperty(value = "SumDueBalance")
	private BigDecimal SumDueBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumAmount4Pay")
	private BigDecimal SumAmount4Pay;
	/**
	 * 汇总后的实际余额 - 收款
	 */
	@JsonProperty(value = "SumAmount4Receive")
	private BigDecimal SumAmount4Receive;
	/**
	 * 汇总后的实际余额
	 */
	@JsonProperty(value = "SumAmount")
	private BigDecimal SumAmount;
	/**
	 *
	 */
	@JsonProperty(value = "ListOfL3")
	private List<ModelCustomerBalanceL3> ListOfL3;
	/**
	 * 1采购数量
	 */
	@JsonProperty(value = "SumPQuantity")
	private BigDecimal SumPQuantity;
	/**
	 * 2付款金额
	 */
	@JsonProperty(value = "SumPAmountPaid")
	private BigDecimal SumPAmountPaid;
	/**
	 * 3.未付
	 */
	@JsonProperty(value = "SumPAmountUnPaid")
	private BigDecimal SumPAmountUnPaid;
	/**
	 *
	 */
	@JsonProperty(value = "SumPQuantityInvoiced")
	private BigDecimal SumPQuantityInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumPQuantityUnInvoiced")
	private BigDecimal SumPQuantityUnInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumPAmountInvoiced")
	private BigDecimal SumPAmountInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumPAmountUnInvoiced")
	private BigDecimal SumPAmountUnInvoiced;
	/**
	 * 销售 ---------
	 */
	@JsonProperty(value = "SumSQuantity")
	private BigDecimal SumSQuantity;
	/**
	 *
	 */
	@JsonProperty(value = "SumSAmountReceived")
	private BigDecimal SumSAmountReceived;
	/**
	 *
	 */
	@JsonProperty(value = "SumSAmountUnReceived")
	private BigDecimal SumSAmountUnReceived;
	/**
	 *
	 */
	@JsonProperty(value = "SumSQuantityInvoiced")
	private BigDecimal SumSQuantityInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumSQuantityUnInvoiced")
	private BigDecimal SumSQuantityUnInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumSAmountInvoiced")
	private BigDecimal SumSAmountInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SumSAmountUnInvoiced")
	private BigDecimal SumSAmountUnInvoiced;
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getLegalId(){
		return LegalId;
	}
	public void setLegalId(String LegalId){
		this.LegalId=LegalId;
	}
	
	public String getLegalName(){
		return LegalName;
	}
	public void setLegalName(String LegalName){
		this.LegalName=LegalName;
	}
	
	public BigDecimal getSumInitBalance(){
		return SumInitBalance;
	}
	public void setSumInitBalance(BigDecimal SumInitBalance){
		this.SumInitBalance=SumInitBalance;
	}
	
	public BigDecimal getSumFund(){
		return SumFund;
	}
	public void setSumFund(BigDecimal SumFund){
		this.SumFund=SumFund;
	}
	
	public BigDecimal getVirtualLastBalance(){
		return VirtualLastBalance;
	}
	public void setVirtualLastBalance(BigDecimal VirtualLastBalance){
		this.VirtualLastBalance=VirtualLastBalance;
	}
	
	public BigDecimal getSumLastBalance(){
		return SumLastBalance;
	}
	public void setSumLastBalance(BigDecimal SumLastBalance){
		this.SumLastBalance=SumLastBalance;
	}
	
	public BigDecimal getSumLotInvoiced(){
		return SumLotInvoiced;
	}
	public void setSumLotInvoiced(BigDecimal SumLotInvoiced){
		this.SumLotInvoiced=SumLotInvoiced;
	}
	
	public BigDecimal getSumLotUnInvoiced(){
		return SumLotUnInvoiced;
	}
	public void setSumLotUnInvoiced(BigDecimal SumLotUnInvoiced){
		this.SumLotUnInvoiced=SumLotUnInvoiced;
	}
	
	public BigDecimal getSumLotInvoicedMinusSumFund(){
		return SumLotInvoicedMinusSumFund;
	}
	public void setSumLotInvoicedMinusSumFund(BigDecimal SumLotInvoicedMinusSumFund){
		this.SumLotInvoicedMinusSumFund=SumLotInvoicedMinusSumFund;
	}
	
	public BigDecimal getCustomerInitBalance(){
		return CustomerInitBalance;
	}
	public void setCustomerInitBalance(BigDecimal CustomerInitBalance){
		this.CustomerInitBalance=CustomerInitBalance;
	}
	
	public BigDecimal getCustomerLastBalance(){
		return CustomerLastBalance;
	}
	public void setCustomerLastBalance(BigDecimal CustomerLastBalance){
		this.CustomerLastBalance=CustomerLastBalance;
	}
	
	public BigDecimal getSumDueBalance4Purchase(){
		return SumDueBalance4Purchase;
	}
	public void setSumDueBalance4Purchase(BigDecimal SumDueBalance4Purchase){
		this.SumDueBalance4Purchase=SumDueBalance4Purchase;
	}
	
	public BigDecimal getSumDueBalance4Sell(){
		return SumDueBalance4Sell;
	}
	public void setSumDueBalance4Sell(BigDecimal SumDueBalance4Sell){
		this.SumDueBalance4Sell=SumDueBalance4Sell;
	}
	
	public BigDecimal getSumDueBalance(){
		return SumDueBalance;
	}
	public void setSumDueBalance(BigDecimal SumDueBalance){
		this.SumDueBalance=SumDueBalance;
	}
	
	public BigDecimal getSumAmount4Pay(){
		return SumAmount4Pay;
	}
	public void setSumAmount4Pay(BigDecimal SumAmount4Pay){
		this.SumAmount4Pay=SumAmount4Pay;
	}
	
	public BigDecimal getSumAmount4Receive(){
		return SumAmount4Receive;
	}
	public void setSumAmount4Receive(BigDecimal SumAmount4Receive){
		this.SumAmount4Receive=SumAmount4Receive;
	}
	
	public BigDecimal getSumAmount(){
		return SumAmount;
	}
	public void setSumAmount(BigDecimal SumAmount){
		this.SumAmount=SumAmount;
	}
	
	public List<ModelCustomerBalanceL3> getListOfL3(){
		return ListOfL3;
	}
	public void setListOfL3(List<ModelCustomerBalanceL3> ListOfL3){
		this.ListOfL3=ListOfL3;
	}
	
	public BigDecimal getSumPQuantity(){
		return SumPQuantity;
	}
	public void setSumPQuantity(BigDecimal SumPQuantity){
		this.SumPQuantity=SumPQuantity;
	}
	
	public BigDecimal getSumPAmountPaid(){
		return SumPAmountPaid;
	}
	public void setSumPAmountPaid(BigDecimal SumPAmountPaid){
		this.SumPAmountPaid=SumPAmountPaid;
	}
	
	public BigDecimal getSumPAmountUnPaid(){
		return SumPAmountUnPaid;
	}
	public void setSumPAmountUnPaid(BigDecimal SumPAmountUnPaid){
		this.SumPAmountUnPaid=SumPAmountUnPaid;
	}
	
	public BigDecimal getSumPQuantityInvoiced(){
		return SumPQuantityInvoiced;
	}
	public void setSumPQuantityInvoiced(BigDecimal SumPQuantityInvoiced){
		this.SumPQuantityInvoiced=SumPQuantityInvoiced;
	}
	
	public BigDecimal getSumPQuantityUnInvoiced(){
		return SumPQuantityUnInvoiced;
	}
	public void setSumPQuantityUnInvoiced(BigDecimal SumPQuantityUnInvoiced){
		this.SumPQuantityUnInvoiced=SumPQuantityUnInvoiced;
	}
	
	public BigDecimal getSumPAmountInvoiced(){
		return SumPAmountInvoiced;
	}
	public void setSumPAmountInvoiced(BigDecimal SumPAmountInvoiced){
		this.SumPAmountInvoiced=SumPAmountInvoiced;
	}
	
	public BigDecimal getSumPAmountUnInvoiced(){
		return SumPAmountUnInvoiced;
	}
	public void setSumPAmountUnInvoiced(BigDecimal SumPAmountUnInvoiced){
		this.SumPAmountUnInvoiced=SumPAmountUnInvoiced;
	}
	
	public BigDecimal getSumSQuantity(){
		return SumSQuantity;
	}
	public void setSumSQuantity(BigDecimal SumSQuantity){
		this.SumSQuantity=SumSQuantity;
	}
	
	public BigDecimal getSumSAmountReceived(){
		return SumSAmountReceived;
	}
	public void setSumSAmountReceived(BigDecimal SumSAmountReceived){
		this.SumSAmountReceived=SumSAmountReceived;
	}
	
	public BigDecimal getSumSAmountUnReceived(){
		return SumSAmountUnReceived;
	}
	public void setSumSAmountUnReceived(BigDecimal SumSAmountUnReceived){
		this.SumSAmountUnReceived=SumSAmountUnReceived;
	}
	
	public BigDecimal getSumSQuantityInvoiced(){
		return SumSQuantityInvoiced;
	}
	public void setSumSQuantityInvoiced(BigDecimal SumSQuantityInvoiced){
		this.SumSQuantityInvoiced=SumSQuantityInvoiced;
	}
	
	public BigDecimal getSumSQuantityUnInvoiced(){
		return SumSQuantityUnInvoiced;
	}
	public void setSumSQuantityUnInvoiced(BigDecimal SumSQuantityUnInvoiced){
		this.SumSQuantityUnInvoiced=SumSQuantityUnInvoiced;
	}
	
	public BigDecimal getSumSAmountInvoiced(){
		return SumSAmountInvoiced;
	}
	public void setSumSAmountInvoiced(BigDecimal SumSAmountInvoiced){
		this.SumSAmountInvoiced=SumSAmountInvoiced;
	}
	
	public BigDecimal getSumSAmountUnInvoiced(){
		return SumSAmountUnInvoiced;
	}
	public void setSumSAmountUnInvoiced(BigDecimal SumSAmountUnInvoiced){
		this.SumSAmountUnInvoiced=SumSAmountUnInvoiced;
	}

}