

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class ModelCustomerBalanceL3 extends HibernateEntity {
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
	@JsonProperty(value = "ContractId")
	private String ContractId;
	/**
	 *
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	/**
	 *
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 *
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 *
	 */
	@JsonProperty(value = "AmountDr")
	private BigDecimal AmountDr;
	/**
	 *
	 */
	@JsonProperty(value = "AmountCr")
	private BigDecimal AmountCr;
	/**
	 *
	 */
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 *
	 */
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@JsonProperty(value = "BrandName")
	private String BrandName;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@JsonProperty(value = "QuantityInvoiced")
	private BigDecimal QuantityInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 *
	 */
	@JsonProperty(value = "DueBalance")
	private BigDecimal DueBalance;
	/**
	 *
	 */
	@JsonProperty(value = "LastBalance")
	private BigDecimal LastBalance;
	/**
	 *
	 */
	@JsonProperty(value = "SumInitBalance")
	private BigDecimal SumInitBalance;
	/**
	 * 最新余额 --- 是个汇总的数值。可以按客户、客户+台头
	 */
	@JsonProperty(value = "SumLastBalance")
	private BigDecimal SumLastBalance;
	/**
	 *
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 *
	 */
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 *
	 */
	@JsonProperty(value = "DC")
	private String DC;
	
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
	
	public String getContractId(){
		return ContractId;
	}
	public void setContractId(String ContractId){
		this.ContractId=ContractId;
	}
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getFullNo(){
		return FullNo;
	}
	public void setFullNo(String FullNo){
		this.FullNo=FullNo;
	}
	
	public BigDecimal getAmountDr(){
		return AmountDr;
	}
	public void setAmountDr(BigDecimal AmountDr){
		this.AmountDr=AmountDr;
	}
	
	public BigDecimal getAmountCr(){
		return AmountCr;
	}
	public void setAmountCr(BigDecimal AmountCr){
		this.AmountCr=AmountCr;
	}
	
	public BigDecimal getAmount(){
		return Amount;
	}
	public void setAmount(BigDecimal Amount){
		this.Amount=Amount;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public String getCommodityName(){
		return CommodityName;
	}
	public void setCommodityName(String CommodityName){
		this.CommodityName=CommodityName;
	}
	
	public String getBrandName(){
		return BrandName;
	}
	public void setBrandName(String BrandName){
		this.BrandName=BrandName;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantityInvoiced(){
		return QuantityInvoiced;
	}
	public void setQuantityInvoiced(BigDecimal QuantityInvoiced){
		this.QuantityInvoiced=QuantityInvoiced;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public BigDecimal getDueBalance(){
		return DueBalance;
	}
	public void setDueBalance(BigDecimal DueBalance){
		this.DueBalance=DueBalance;
	}
	
	public BigDecimal getLastBalance(){
		return LastBalance;
	}
	public void setLastBalance(BigDecimal LastBalance){
		this.LastBalance=LastBalance;
	}
	
	public BigDecimal getSumInitBalance(){
		return SumInitBalance;
	}
	public void setSumInitBalance(BigDecimal SumInitBalance){
		this.SumInitBalance=SumInitBalance;
	}
	
	public BigDecimal getSumLastBalance(){
		return SumLastBalance;
	}
	public void setSumLastBalance(BigDecimal SumLastBalance){
		this.SumLastBalance=SumLastBalance;
	}
	
	public Boolean getIsInvoiced(){
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean IsInvoiced){
		this.IsInvoiced=IsInvoiced;
	}
	
	public String getSpotDirection(){
		return SpotDirection;
	}
	public void setSpotDirection(String SpotDirection){
		this.SpotDirection=SpotDirection;
	}
	
	public String getDC(){
		return DC;
	}
	public void setDC(String DC){
		this.DC=DC;
	}

}