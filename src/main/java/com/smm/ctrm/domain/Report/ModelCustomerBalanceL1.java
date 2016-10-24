

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class ModelCustomerBalanceL1 extends HibernateEntity {
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
	@JsonProperty(value = "SumFund")
	private BigDecimal SumFund;
	/**
	 *
	 */
	@JsonProperty(value = "SumInitBalance")
	private BigDecimal SumInitBalance;
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
	@JsonProperty(value = "ListOfL2")
	private List<ModelCustomerBalanceL2> ListOfL2;
	
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
	
	public BigDecimal getSumFund(){
		return SumFund;
	}
	public void setSumFund(BigDecimal SumFund){
		this.SumFund=SumFund;
	}
	
	public BigDecimal getSumInitBalance(){
		return SumInitBalance;
	}
	public void setSumInitBalance(BigDecimal SumInitBalance){
		this.SumInitBalance=SumInitBalance;
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
	
	public List<ModelCustomerBalanceL2> getListOfL2(){
		return ListOfL2;
	}
	public void setListOfL2(List<ModelCustomerBalanceL2> ListOfL2){
		this.ListOfL2=ListOfL2;
	}

}