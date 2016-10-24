

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class TradeReport extends HibernateEntity {
	private static final long serialVersionUID = 1461832991351L;
	/**
	 *
	 */
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	/**
	 *
	 */
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	/**
	 *
	 */
	@JsonProperty(value = "LegalName")
	private String LegalName;
	/**
	 * 品牌
	 */
	@JsonProperty(value = "BrandName")
	private String BrandName;
	/**
	 * 品种
	 */
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@JsonProperty(value = "ProductName")
	private String ProductName;
	/**
	 * 期初库存信息
	 */
	@JsonProperty(value = "SettleDailyInit")
	private SettleDaily SettleDailyInit;
	/**
	 * 期末库存信息
	 */
	@JsonProperty(value = "SettleDaily")
	private SettleDaily SettleDaily;
	/**
	 * 本期入库
	 */
	@JsonProperty(value = "StoragesIn")
	private List<TradeReportDetailBuy> StoragesIn;
	/**
	 * 本期出库
	 */
	@JsonProperty(value = "StoragesOut")
	private List<TradeReportDetailBuy> StoragesOut;
	/**
	 * 销售发票信息
	 */
	@JsonProperty(value = "Invoice4Sales")
	private List<TradeReportDetailSale> Invoice4Sales;
	/**
	 * 销售发票组成 
	 */
	@JsonProperty(value = "InvoiceContainBuyAndSale")
	private List<TradeReportDetailSaleAndBuy> InvoiceContainBuyAndSale;
	/**
	 * 销售数量与出库重量差
	 */
	@JsonProperty(value = "SaleQuantityDiff")
	private BigDecimal SaleQuantityDiff;
	/**
	 * 累计库存（当前期末库存）
	 */
	@JsonProperty(value = "Quantity4Stock")
	private BigDecimal Quantity4Stock;
	/**
	 * 库存成本
	 */
	@JsonProperty(value = "Amount4Stock")
	private BigDecimal Amount4Stock;
	
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
	
	public String getLegalName(){
		return LegalName;
	}
	public void setLegalName(String LegalName){
		this.LegalName=LegalName;
	}
	
	public String getBrandName(){
		return BrandName;
	}
	public void setBrandName(String BrandName){
		this.BrandName=BrandName;
	}
	
	public String getCommodityName(){
		return CommodityName;
	}
	public void setCommodityName(String CommodityName){
		this.CommodityName=CommodityName;
	}
	
	public String getProductName(){
		return ProductName;
	}
	public void setProductName(String ProductName){
		this.ProductName=ProductName;
	}
	
	public SettleDaily getSettleDailyInit(){
		return SettleDailyInit;
	}
	public void setSettleDailyInit(SettleDaily SettleDailyInit){
		this.SettleDailyInit=SettleDailyInit;
	}
	
	public SettleDaily getSettleDaily(){
		return SettleDaily;
	}
	public void setSettleDaily(SettleDaily SettleDaily){
		this.SettleDaily=SettleDaily;
	}
	
	public List<TradeReportDetailBuy> getStoragesIn(){
		return StoragesIn;
	}
	public void setStoragesIn(List<TradeReportDetailBuy> StoragesIn){
		this.StoragesIn=StoragesIn;
	}
	
	public List<TradeReportDetailBuy> getStoragesOut(){
		return StoragesOut;
	}
	public void setStoragesOut(List<TradeReportDetailBuy> StoragesOut){
		this.StoragesOut=StoragesOut;
	}
	
	public List<TradeReportDetailSale> getInvoice4Sales(){
		return Invoice4Sales;
	}
	public void setInvoice4Sales(List<TradeReportDetailSale> Invoice4Sales){
		this.Invoice4Sales=Invoice4Sales;
	}
	
	public List<TradeReportDetailSaleAndBuy> getInvoiceContainBuyAndSale(){
		return InvoiceContainBuyAndSale;
	}
	public void setInvoiceContainBuyAndSale(List<TradeReportDetailSaleAndBuy> InvoiceContainBuyAndSale){
		this.InvoiceContainBuyAndSale=InvoiceContainBuyAndSale;
	}
	
	public BigDecimal getSaleQuantityDiff(){
		return SaleQuantityDiff;
	}
	public void setSaleQuantityDiff(BigDecimal SaleQuantityDiff){
		this.SaleQuantityDiff=SaleQuantityDiff;
	}
	
	public BigDecimal getQuantity4Stock(){
		return Quantity4Stock;
	}
	public void setQuantity4Stock(BigDecimal Quantity4Stock){
		this.Quantity4Stock=Quantity4Stock;
	}
	
	public BigDecimal getAmount4Stock(){
		return Amount4Stock;
	}
	public void setAmount4Stock(BigDecimal Amount4Stock){
		this.Amount4Stock=Amount4Stock;
	}

}