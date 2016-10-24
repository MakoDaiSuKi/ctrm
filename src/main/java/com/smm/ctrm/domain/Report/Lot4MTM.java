

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class Lot4MTM extends HibernateEntity {
	private static final long serialVersionUID = 1461719402483L;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4BviB")
	private String LotId4BviB;
	/**
	 *
	 */
	@JsonProperty(value = "FullNo4Bvi")
	private String FullNo4Bvi;
	/**
	 * 批次数量
	 */
	@JsonProperty(value = "Quantity4Bvi")
	private BigDecimal Quantity4Bvi;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4BviB")
	private String CustomerName4BviB;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4BviB")
	private String InvoiceNo4BviB;
	/**
	 * 点价数量
	 */
	@JsonProperty(value = "PricingQuantity4Bvi")
	private BigDecimal PricingQuantity4Bvi;
	/**
	 * 保值原价（均价）
	 */
	@JsonProperty(value = "Price4PositionBviB")
	private BigDecimal Price4PositionBviB;
	/**
	 * 点价价格（均价）
	 */
	@JsonProperty(value = "Price4PricingBviB")
	private BigDecimal Price4PricingBviB;
	/**
	 * 点价数量*（保值原价 - 点价价格）  = 点价利润
	 */
	@JsonProperty(value = "Profit4BviBFuture")
	private BigDecimal Profit4BviBFuture;
	/**
	 * Bvi采购点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4BviBFuture")
	private BigDecimal PriceOfProfit4BviBFuture;
	/**
	 * 升贴水（均价）
	 */
	@JsonProperty(value = "Premium4BviB")
	private BigDecimal Premium4BviB;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4BviB")
	private BigDecimal EstimateFee4BviB;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4BviB")
	private BigDecimal RealFee4BviB;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialBviB")
	private BigDecimal Spread4InitialBviB;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpBviB")
	private BigDecimal Spread4QpBviB;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotBviB")
	private BigDecimal Spread4LotBviB;
	/**
	 * 装运地点
	 */
	@JsonProperty(value = "Loading")
	private String Loading;
	/**
	 * 交付方式
	 */
	@JsonProperty(value = "DeliveryTerm4Bvi")
	private String DeliveryTerm4Bvi;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4BviBSpot")
	private BigDecimal Profit4BviBSpot;
	/**
	 * Bvi采购贸易利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4BviBSpot")
	private BigDecimal PriceOfProfit4BviBSpot;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4BviS")
	private String LotId4BviS;
	/**
	 * 批次
	 */
	@JsonProperty(value = "FullNo4BviS")
	private String FullNo4BviS;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4BviS")
	private String CustomerName4BviS;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4BviS")
	private String InvoiceNo4BviS;
	/**
	 * 点价利润
	 */
	@JsonProperty(value = "Profit4BviSFuture")
	private BigDecimal Profit4BviSFuture;
	/**
	 * Bvi销售点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4BviSFuture")
	private BigDecimal PriceOfProfit4BviSFuture;
	/**
	 * 点价价格
	 */
	@JsonProperty(value = "Price4PricingBviS")
	private BigDecimal Price4PricingBviS;
	/**
	 * 升贴水
	 */
	@JsonProperty(value = "Premium4BviS")
	private BigDecimal Premium4BviS;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4BviS")
	private BigDecimal EstimateFee4BviS;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4BviS")
	private BigDecimal RealFee4BviS;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialBviS")
	private BigDecimal Spread4InitialBviS;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpBviS")
	private BigDecimal Spread4QpBviS;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotBviS")
	private BigDecimal Spread4LotBviS;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4BviSSpot")
	private BigDecimal Profit4BviSSpot;
	/**
	 * Bvi销售贸易利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4BviSSpot")
	private BigDecimal PriceOfProfit4BviSSpot;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4SmB")
	private String LotId4SmB;
	/**
	 * 批次
	 */
	@JsonProperty(value = "FullNo4SmB")
	private String FullNo4SmB;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4SmB")
	private String CustomerName4SmB;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4SmB")
	private String InvoiceNo4SmB;
	/**
	 * 点价利润
	 */
	@JsonProperty(value = "Profit4SmBFuture")
	private BigDecimal Profit4SmBFuture;
	/**
	 * SM采购点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4SmBFuture")
	private BigDecimal PriceOfProfit4SmBFuture;
	/**
	 * 点价价格
	 */
	@JsonProperty(value = "Price4PricingSmB")
	private BigDecimal Price4PricingSmB;
	/**
	 * 升贴水
	 */
	@JsonProperty(value = "Premium4SmB")
	private BigDecimal Premium4SmB;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4SmB")
	private BigDecimal EstimateFee4SmB;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4SmB")
	private BigDecimal RealFee4SmB;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialSmB")
	private BigDecimal Spread4InitialSmB;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpSmB")
	private BigDecimal Spread4QpSmB;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotSmB")
	private BigDecimal Spread4LotSmB;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4SmBSpot")
	private BigDecimal Profit4SmBSpot;
	/**
	 * SM采购贸易利润
	 */
	@JsonProperty(value = "PriceOfProfit4SmBSpot")
	private BigDecimal PriceOfProfit4SmBSpot;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4SmS")
	private String LotId4SmS;
	/**
	 * 销售批次
	 */
	@JsonProperty(value = "FullNo4Sm")
	private String FullNo4Sm;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4SmS")
	private String CustomerName4SmS;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4SmS")
	private String InvoiceNo4SmS;
	/**
	 * 销售数量
	 */
	@JsonProperty(value = "Quantity4Sm")
	private BigDecimal Quantity4Sm;
	/**
	 * 点价数量
	 */
	@JsonProperty(value = "PricingQuantity4Sm")
	private BigDecimal PricingQuantity4Sm;
	/**
	 * 保值原价（均价）
	 */
	@JsonProperty(value = "Price4PositionSmS")
	private BigDecimal Price4PositionSmS;
	/**
	 * 点价价格（均价）
	 */
	@JsonProperty(value = "Price4PricingSmS")
	private BigDecimal Price4PricingSmS;
	/**
	 * 点价数量*（点价价格 - 保值原价）
	 */
	@JsonProperty(value = "Profit4SmSFuture")
	private BigDecimal Profit4SmSFuture;
	/**
	 * SM销售点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4SmSFuture")
	private BigDecimal PriceOfProfit4SmSFuture;
	/**
	 * 升贴水
	 */
	@JsonProperty(value = "Premium4SmS")
	private BigDecimal Premium4SmS;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4SmS")
	private BigDecimal EstimateFee4SmS;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4SmS")
	private BigDecimal RealFee4SmS;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialSmS")
	private BigDecimal Spread4InitialSmS;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpSmS")
	private BigDecimal Spread4QpSmS;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotSmS")
	private BigDecimal Spread4LotSmS;
	/**
	 * 交付方式
	 */
	@JsonProperty(value = "DeliveryTerm4Sm")
	private String DeliveryTerm4Sm;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4SmSSpot")
	private BigDecimal Profit4SmSSpot;
	/**
	 * SM销售贸易利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4SmSSpot")
	private BigDecimal PriceOfProfit4SmSSpot;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4ReBuyS")
	private String LotId4ReBuyS;
	/**
	 * 批次
	 */
	@JsonProperty(value = "FullNo4ReBuyS")
	private String FullNo4ReBuyS;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4ReBuyS")
	private String CustomerName4ReBuyS;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4ReBuyS")
	private String InvoiceNo4ReBuyS;
	/**
	 * 点价利润
	 */
	@JsonProperty(value = "Profit4ReBuySFuture")
	private BigDecimal Profit4ReBuySFuture;
	/**
	 * Bvi销售点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4ReBuySFuture")
	private BigDecimal PriceOfProfit4ReBuySFuture;
	/**
	 * 点价价格
	 */
	@JsonProperty(value = "Price4PricingReBuyS")
	private BigDecimal Price4PricingReBuyS;
	/**
	 * 升贴水
	 */
	@JsonProperty(value = "Premium4ReBuyS")
	private BigDecimal Premium4ReBuyS;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4ReBuyS")
	private BigDecimal EstimateFee4ReBuyS;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4ReBuyS")
	private BigDecimal RealFee4ReBuyS;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialReBuyS")
	private BigDecimal Spread4InitialReBuyS;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpReBuyS")
	private BigDecimal Spread4QpReBuyS;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotReBuyS")
	private BigDecimal Spread4LotReBuyS;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4ReBuySSpot")
	private BigDecimal Profit4ReBuySSpot;
	/**
	 * Bvi销售贸易利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4ReBuySSpot")
	private BigDecimal PriceOfProfit4ReBuySSpot;
	/**
	 *
	 */
	@JsonProperty(value = "LotId4ReBuyB")
	private String LotId4ReBuyB;
	/**
	 * 批次
	 */
	@JsonProperty(value = "FullNo4ReBuyB")
	private String FullNo4ReBuyB;
	/**
	 * 客户名称
	 */
	@JsonProperty(value = "CustomerName4ReBuyB")
	private String CustomerName4ReBuyB;
	/**
	 * 发票号
	 */
	@JsonProperty(value = "InvoiceNo4ReBuyB")
	private String InvoiceNo4ReBuyB;
	/**
	 * 点价利润
	 */
	@JsonProperty(value = "Profit4ReBuyBFuture")
	private BigDecimal Profit4ReBuyBFuture;
	/**
	 * SM采购点价利润单价
	 */
	@JsonProperty(value = "PriceOfProfit4ReBuyBFuture")
	private BigDecimal PriceOfProfit4ReBuyBFuture;
	/**
	 * 点价价格
	 */
	@JsonProperty(value = "Price4PricingReBuyB")
	private BigDecimal Price4PricingReBuyB;
	/**
	 * 升贴水
	 */
	@JsonProperty(value = "Premium4ReBuyB")
	private BigDecimal Premium4ReBuyB;
	/**
	 * 预估费用
	 */
	@JsonProperty(value = "EstimateFee4ReBuyB")
	private BigDecimal EstimateFee4ReBuyB;
	/**
	 * 实际费用
	 */
	@JsonProperty(value = "RealFee4ReBuyB")
	private BigDecimal RealFee4ReBuyB;
	/**
	 * Initial Spread
	 */
	@JsonProperty(value = "Spread4InitialReBuyB")
	private BigDecimal Spread4InitialReBuyB;
	/**
	 * QP Spread
	 */
	@JsonProperty(value = "Spread4QpReBuyB")
	private BigDecimal Spread4QpReBuyB;
	/**
	 * Lot Spread
	 */
	@JsonProperty(value = "Spread4LotReBuyB")
	private BigDecimal Spread4LotReBuyB;
	/**
	 * 贸易利润
	 */
	@JsonProperty(value = "Profit4ReBuyBSpot")
	private BigDecimal Profit4ReBuyBSpot;
	/**
	 * SM采购贸易利润
	 */
	@JsonProperty(value = "PriceOfProfit4ReBuyBSpot")
	private BigDecimal PriceOfProfit4ReBuyBSpot;
	/**
	 * 点价利润之和
	 */
	@JsonProperty(value = "Profit4Futre")
	private BigDecimal Profit4Futre;
	/**
	 * 点价利润之和单价
	 */
	@JsonProperty(value = "PriceOfProfit4Futre")
	private BigDecimal PriceOfProfit4Futre;
	/**
	 * 贸易利润之和
	 */
	@JsonProperty(value = "Profit4Spot")
	private BigDecimal Profit4Spot;
	/**
	 * 贸易利润之和单价
	 */
	@JsonProperty(value = "PriceOfProfit4Spot")
	private BigDecimal PriceOfProfit4Spot;
	/**
	 * 总利润
	 */
	@JsonProperty(value = "Profit")
	private BigDecimal Profit;
	/**
	 * 总利润单价
	 */
	@JsonProperty(value = "PriceOfProfit")
	private BigDecimal PriceOfProfit;
	/**
	 * 用于记录报表的主线Lotid
	 */
	@JsonProperty(value = "LotId")
	private String LotId;
	/**
	 *是否结算
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 * 客户是否开票
	 */
	@JsonProperty(value = "IsCustInvoiced")
	private Boolean IsCustInvoiced;
	/**
	 * 外部销售批次的品牌
	 */
	@JsonProperty(value = "BrandNames4Sms")
	private String BrandNames4Sms;
	/**
	 * 到达地点
	 */
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 *
	 */
	@JsonProperty(value = "AccountDate")
	private String AccountDate;
	/**
	 *
	 */
	@JsonProperty(value = "AccountYear")
	private String AccountYear;
	/**
	 *
	 */
	@JsonProperty(value = "AccountMonth")
	private String AccountMonth;
	/**
	 * 发票创建者Id
	 */
	@JsonProperty(value = "InvoiceCreatedId")
	private String InvoiceCreatedId;
	/**
	 * 发票创建者姓名
	 */
	@JsonProperty(value = "InvoiceCreaterName")
	private String InvoiceCreaterName;
	
	public String getLotId4BviB(){
		return LotId4BviB;
	}
	public void setLotId4BviB(String LotId4BviB){
		this.LotId4BviB=LotId4BviB;
	}
	
	public String getFullNo4Bvi(){
		return FullNo4Bvi;
	}
	public void setFullNo4Bvi(String FullNo4Bvi){
		this.FullNo4Bvi=FullNo4Bvi;
	}
	
	public BigDecimal getQuantity4Bvi(){
		return Quantity4Bvi;
	}
	public void setQuantity4Bvi(BigDecimal Quantity4Bvi){
		this.Quantity4Bvi=Quantity4Bvi;
	}
	
	public String getCustomerName4BviB(){
		return CustomerName4BviB;
	}
	public void setCustomerName4BviB(String CustomerName4BviB){
		this.CustomerName4BviB=CustomerName4BviB;
	}
	
	public String getInvoiceNo4BviB(){
		return InvoiceNo4BviB;
	}
	public void setInvoiceNo4BviB(String InvoiceNo4BviB){
		this.InvoiceNo4BviB=InvoiceNo4BviB;
	}
	
	public BigDecimal getPricingQuantity4Bvi(){
		return PricingQuantity4Bvi;
	}
	public void setPricingQuantity4Bvi(BigDecimal PricingQuantity4Bvi){
		this.PricingQuantity4Bvi=PricingQuantity4Bvi;
	}
	
	public BigDecimal getPrice4PositionBviB(){
		return Price4PositionBviB;
	}
	public void setPrice4PositionBviB(BigDecimal Price4PositionBviB){
		this.Price4PositionBviB=Price4PositionBviB;
	}
	
	public BigDecimal getPrice4PricingBviB(){
		return Price4PricingBviB;
	}
	public void setPrice4PricingBviB(BigDecimal Price4PricingBviB){
		this.Price4PricingBviB=Price4PricingBviB;
	}
	
	public BigDecimal getProfit4BviBFuture(){
		return Profit4BviBFuture;
	}
	public void setProfit4BviBFuture(BigDecimal Profit4BviBFuture){
		this.Profit4BviBFuture=Profit4BviBFuture;
	}
	
	public BigDecimal getPriceOfProfit4BviBFuture(){
		return PriceOfProfit4BviBFuture;
	}
	public void setPriceOfProfit4BviBFuture(BigDecimal PriceOfProfit4BviBFuture){
		this.PriceOfProfit4BviBFuture=PriceOfProfit4BviBFuture;
	}
	
	public BigDecimal getPremium4BviB(){
		return Premium4BviB;
	}
	public void setPremium4BviB(BigDecimal Premium4BviB){
		this.Premium4BviB=Premium4BviB;
	}
	
	public BigDecimal getEstimateFee4BviB(){
		return EstimateFee4BviB;
	}
	public void setEstimateFee4BviB(BigDecimal EstimateFee4BviB){
		this.EstimateFee4BviB=EstimateFee4BviB;
	}
	
	public BigDecimal getRealFee4BviB(){
		return RealFee4BviB;
	}
	public void setRealFee4BviB(BigDecimal RealFee4BviB){
		this.RealFee4BviB=RealFee4BviB;
	}
	
	public BigDecimal getSpread4InitialBviB(){
		return Spread4InitialBviB;
	}
	public void setSpread4InitialBviB(BigDecimal Spread4InitialBviB){
		this.Spread4InitialBviB=Spread4InitialBviB;
	}
	
	public BigDecimal getSpread4QpBviB(){
		return Spread4QpBviB;
	}
	public void setSpread4QpBviB(BigDecimal Spread4QpBviB){
		this.Spread4QpBviB=Spread4QpBviB;
	}
	
	public BigDecimal getSpread4LotBviB(){
		return Spread4LotBviB;
	}
	public void setSpread4LotBviB(BigDecimal Spread4LotBviB){
		this.Spread4LotBviB=Spread4LotBviB;
	}
	
	public String getLoading(){
		return Loading;
	}
	public void setLoading(String Loading){
		this.Loading=Loading;
	}
	
	public String getDeliveryTerm4Bvi(){
		return DeliveryTerm4Bvi;
	}
	public void setDeliveryTerm4Bvi(String DeliveryTerm4Bvi){
		this.DeliveryTerm4Bvi=DeliveryTerm4Bvi;
	}
	
	public BigDecimal getProfit4BviBSpot(){
		return Profit4BviBSpot;
	}
	public void setProfit4BviBSpot(BigDecimal Profit4BviBSpot){
		this.Profit4BviBSpot=Profit4BviBSpot;
	}
	
	public BigDecimal getPriceOfProfit4BviBSpot(){
		return PriceOfProfit4BviBSpot;
	}
	public void setPriceOfProfit4BviBSpot(BigDecimal PriceOfProfit4BviBSpot){
		this.PriceOfProfit4BviBSpot=PriceOfProfit4BviBSpot;
	}
	
	public String getLotId4BviS(){
		return LotId4BviS;
	}
	public void setLotId4BviS(String LotId4BviS){
		this.LotId4BviS=LotId4BviS;
	}
	
	public String getFullNo4BviS(){
		return FullNo4BviS;
	}
	public void setFullNo4BviS(String FullNo4BviS){
		this.FullNo4BviS=FullNo4BviS;
	}
	
	public String getCustomerName4BviS(){
		return CustomerName4BviS;
	}
	public void setCustomerName4BviS(String CustomerName4BviS){
		this.CustomerName4BviS=CustomerName4BviS;
	}
	
	public String getInvoiceNo4BviS(){
		return InvoiceNo4BviS;
	}
	public void setInvoiceNo4BviS(String InvoiceNo4BviS){
		this.InvoiceNo4BviS=InvoiceNo4BviS;
	}
	
	public BigDecimal getProfit4BviSFuture(){
		return Profit4BviSFuture;
	}
	public void setProfit4BviSFuture(BigDecimal Profit4BviSFuture){
		this.Profit4BviSFuture=Profit4BviSFuture;
	}
	
	public BigDecimal getPriceOfProfit4BviSFuture(){
		return PriceOfProfit4BviSFuture;
	}
	public void setPriceOfProfit4BviSFuture(BigDecimal PriceOfProfit4BviSFuture){
		this.PriceOfProfit4BviSFuture=PriceOfProfit4BviSFuture;
	}
	
	public BigDecimal getPrice4PricingBviS(){
		return Price4PricingBviS;
	}
	public void setPrice4PricingBviS(BigDecimal Price4PricingBviS){
		this.Price4PricingBviS=Price4PricingBviS;
	}
	
	public BigDecimal getPremium4BviS(){
		return Premium4BviS;
	}
	public void setPremium4BviS(BigDecimal Premium4BviS){
		this.Premium4BviS=Premium4BviS;
	}
	
	public BigDecimal getEstimateFee4BviS(){
		return EstimateFee4BviS;
	}
	public void setEstimateFee4BviS(BigDecimal EstimateFee4BviS){
		this.EstimateFee4BviS=EstimateFee4BviS;
	}
	
	public BigDecimal getRealFee4BviS(){
		return RealFee4BviS;
	}
	public void setRealFee4BviS(BigDecimal RealFee4BviS){
		this.RealFee4BviS=RealFee4BviS;
	}
	
	public BigDecimal getSpread4InitialBviS(){
		return Spread4InitialBviS;
	}
	public void setSpread4InitialBviS(BigDecimal Spread4InitialBviS){
		this.Spread4InitialBviS=Spread4InitialBviS;
	}
	
	public BigDecimal getSpread4QpBviS(){
		return Spread4QpBviS;
	}
	public void setSpread4QpBviS(BigDecimal Spread4QpBviS){
		this.Spread4QpBviS=Spread4QpBviS;
	}
	
	public BigDecimal getSpread4LotBviS(){
		return Spread4LotBviS;
	}
	public void setSpread4LotBviS(BigDecimal Spread4LotBviS){
		this.Spread4LotBviS=Spread4LotBviS;
	}
	
	public BigDecimal getProfit4BviSSpot(){
		return Profit4BviSSpot;
	}
	public void setProfit4BviSSpot(BigDecimal Profit4BviSSpot){
		this.Profit4BviSSpot=Profit4BviSSpot;
	}
	
	public BigDecimal getPriceOfProfit4BviSSpot(){
		return PriceOfProfit4BviSSpot;
	}
	public void setPriceOfProfit4BviSSpot(BigDecimal PriceOfProfit4BviSSpot){
		this.PriceOfProfit4BviSSpot=PriceOfProfit4BviSSpot;
	}
	
	public String getLotId4SmB(){
		return LotId4SmB;
	}
	public void setLotId4SmB(String LotId4SmB){
		this.LotId4SmB=LotId4SmB;
	}
	
	public String getFullNo4SmB(){
		return FullNo4SmB;
	}
	public void setFullNo4SmB(String FullNo4SmB){
		this.FullNo4SmB=FullNo4SmB;
	}
	
	public String getCustomerName4SmB(){
		return CustomerName4SmB;
	}
	public void setCustomerName4SmB(String CustomerName4SmB){
		this.CustomerName4SmB=CustomerName4SmB;
	}
	
	public String getInvoiceNo4SmB(){
		return InvoiceNo4SmB;
	}
	public void setInvoiceNo4SmB(String InvoiceNo4SmB){
		this.InvoiceNo4SmB=InvoiceNo4SmB;
	}
	
	public BigDecimal getProfit4SmBFuture(){
		return Profit4SmBFuture;
	}
	public void setProfit4SmBFuture(BigDecimal Profit4SmBFuture){
		this.Profit4SmBFuture=Profit4SmBFuture;
	}
	
	public BigDecimal getPriceOfProfit4SmBFuture(){
		return PriceOfProfit4SmBFuture;
	}
	public void setPriceOfProfit4SmBFuture(BigDecimal PriceOfProfit4SmBFuture){
		this.PriceOfProfit4SmBFuture=PriceOfProfit4SmBFuture;
	}
	
	public BigDecimal getPrice4PricingSmB(){
		return Price4PricingSmB;
	}
	public void setPrice4PricingSmB(BigDecimal Price4PricingSmB){
		this.Price4PricingSmB=Price4PricingSmB;
	}
	
	public BigDecimal getPremium4SmB(){
		return Premium4SmB;
	}
	public void setPremium4SmB(BigDecimal Premium4SmB){
		this.Premium4SmB=Premium4SmB;
	}
	
	public BigDecimal getEstimateFee4SmB(){
		return EstimateFee4SmB;
	}
	public void setEstimateFee4SmB(BigDecimal EstimateFee4SmB){
		this.EstimateFee4SmB=EstimateFee4SmB;
	}
	
	public BigDecimal getRealFee4SmB(){
		return RealFee4SmB;
	}
	public void setRealFee4SmB(BigDecimal RealFee4SmB){
		this.RealFee4SmB=RealFee4SmB;
	}
	
	public BigDecimal getSpread4InitialSmB(){
		return Spread4InitialSmB;
	}
	public void setSpread4InitialSmB(BigDecimal Spread4InitialSmB){
		this.Spread4InitialSmB=Spread4InitialSmB;
	}
	
	public BigDecimal getSpread4QpSmB(){
		return Spread4QpSmB;
	}
	public void setSpread4QpSmB(BigDecimal Spread4QpSmB){
		this.Spread4QpSmB=Spread4QpSmB;
	}
	
	public BigDecimal getSpread4LotSmB(){
		return Spread4LotSmB;
	}
	public void setSpread4LotSmB(BigDecimal Spread4LotSmB){
		this.Spread4LotSmB=Spread4LotSmB;
	}
	
	public BigDecimal getProfit4SmBSpot(){
		return Profit4SmBSpot;
	}
	public void setProfit4SmBSpot(BigDecimal Profit4SmBSpot){
		this.Profit4SmBSpot=Profit4SmBSpot;
	}
	
	public BigDecimal getPriceOfProfit4SmBSpot(){
		return PriceOfProfit4SmBSpot;
	}
	public void setPriceOfProfit4SmBSpot(BigDecimal PriceOfProfit4SmBSpot){
		this.PriceOfProfit4SmBSpot=PriceOfProfit4SmBSpot;
	}
	
	public String getLotId4SmS(){
		return LotId4SmS;
	}
	public void setLotId4SmS(String LotId4SmS){
		this.LotId4SmS=LotId4SmS;
	}
	
	public String getFullNo4Sm(){
		return FullNo4Sm;
	}
	public void setFullNo4Sm(String FullNo4Sm){
		this.FullNo4Sm=FullNo4Sm;
	}
	
	public String getCustomerName4SmS(){
		return CustomerName4SmS;
	}
	public void setCustomerName4SmS(String CustomerName4SmS){
		this.CustomerName4SmS=CustomerName4SmS;
	}
	
	public String getInvoiceNo4SmS(){
		return InvoiceNo4SmS;
	}
	public void setInvoiceNo4SmS(String InvoiceNo4SmS){
		this.InvoiceNo4SmS=InvoiceNo4SmS;
	}
	
	public BigDecimal getQuantity4Sm(){
		return Quantity4Sm;
	}
	public void setQuantity4Sm(BigDecimal Quantity4Sm){
		this.Quantity4Sm=Quantity4Sm;
	}
	
	public BigDecimal getPricingQuantity4Sm(){
		return PricingQuantity4Sm;
	}
	public void setPricingQuantity4Sm(BigDecimal PricingQuantity4Sm){
		this.PricingQuantity4Sm=PricingQuantity4Sm;
	}
	
	public BigDecimal getPrice4PositionSmS(){
		return Price4PositionSmS;
	}
	public void setPrice4PositionSmS(BigDecimal Price4PositionSmS){
		this.Price4PositionSmS=Price4PositionSmS;
	}
	
	public BigDecimal getPrice4PricingSmS(){
		return Price4PricingSmS;
	}
	public void setPrice4PricingSmS(BigDecimal Price4PricingSmS){
		this.Price4PricingSmS=Price4PricingSmS;
	}
	
	public BigDecimal getProfit4SmSFuture(){
		return Profit4SmSFuture;
	}
	public void setProfit4SmSFuture(BigDecimal Profit4SmSFuture){
		this.Profit4SmSFuture=Profit4SmSFuture;
	}
	
	public BigDecimal getPriceOfProfit4SmSFuture(){
		return PriceOfProfit4SmSFuture;
	}
	public void setPriceOfProfit4SmSFuture(BigDecimal PriceOfProfit4SmSFuture){
		this.PriceOfProfit4SmSFuture=PriceOfProfit4SmSFuture;
	}
	
	public BigDecimal getPremium4SmS(){
		return Premium4SmS;
	}
	public void setPremium4SmS(BigDecimal Premium4SmS){
		this.Premium4SmS=Premium4SmS;
	}
	
	public BigDecimal getEstimateFee4SmS(){
		return EstimateFee4SmS;
	}
	public void setEstimateFee4SmS(BigDecimal EstimateFee4SmS){
		this.EstimateFee4SmS=EstimateFee4SmS;
	}
	
	public BigDecimal getRealFee4SmS(){
		return RealFee4SmS;
	}
	public void setRealFee4SmS(BigDecimal RealFee4SmS){
		this.RealFee4SmS=RealFee4SmS;
	}
	
	public BigDecimal getSpread4InitialSmS(){
		return Spread4InitialSmS;
	}
	public void setSpread4InitialSmS(BigDecimal Spread4InitialSmS){
		this.Spread4InitialSmS=Spread4InitialSmS;
	}
	
	public BigDecimal getSpread4QpSmS(){
		return Spread4QpSmS;
	}
	public void setSpread4QpSmS(BigDecimal Spread4QpSmS){
		this.Spread4QpSmS=Spread4QpSmS;
	}
	
	public BigDecimal getSpread4LotSmS(){
		return Spread4LotSmS;
	}
	public void setSpread4LotSmS(BigDecimal Spread4LotSmS){
		this.Spread4LotSmS=Spread4LotSmS;
	}
	
	public String getDeliveryTerm4Sm(){
		return DeliveryTerm4Sm;
	}
	public void setDeliveryTerm4Sm(String DeliveryTerm4Sm){
		this.DeliveryTerm4Sm=DeliveryTerm4Sm;
	}
	
	public BigDecimal getProfit4SmSSpot(){
		return Profit4SmSSpot;
	}
	public void setProfit4SmSSpot(BigDecimal Profit4SmSSpot){
		this.Profit4SmSSpot=Profit4SmSSpot;
	}
	
	public BigDecimal getPriceOfProfit4SmSSpot(){
		return PriceOfProfit4SmSSpot;
	}
	public void setPriceOfProfit4SmSSpot(BigDecimal PriceOfProfit4SmSSpot){
		this.PriceOfProfit4SmSSpot=PriceOfProfit4SmSSpot;
	}
	
	public String getLotId4ReBuyS(){
		return LotId4ReBuyS;
	}
	public void setLotId4ReBuyS(String LotId4ReBuyS){
		this.LotId4ReBuyS=LotId4ReBuyS;
	}
	
	public String getFullNo4ReBuyS(){
		return FullNo4ReBuyS;
	}
	public void setFullNo4ReBuyS(String FullNo4ReBuyS){
		this.FullNo4ReBuyS=FullNo4ReBuyS;
	}
	
	public String getCustomerName4ReBuyS(){
		return CustomerName4ReBuyS;
	}
	public void setCustomerName4ReBuyS(String CustomerName4ReBuyS){
		this.CustomerName4ReBuyS=CustomerName4ReBuyS;
	}
	
	public String getInvoiceNo4ReBuyS(){
		return InvoiceNo4ReBuyS;
	}
	public void setInvoiceNo4ReBuyS(String InvoiceNo4ReBuyS){
		this.InvoiceNo4ReBuyS=InvoiceNo4ReBuyS;
	}
	
	public BigDecimal getProfit4ReBuySFuture(){
		return Profit4ReBuySFuture;
	}
	public void setProfit4ReBuySFuture(BigDecimal Profit4ReBuySFuture){
		this.Profit4ReBuySFuture=Profit4ReBuySFuture;
	}
	
	public BigDecimal getPriceOfProfit4ReBuySFuture(){
		return PriceOfProfit4ReBuySFuture;
	}
	public void setPriceOfProfit4ReBuySFuture(BigDecimal PriceOfProfit4ReBuySFuture){
		this.PriceOfProfit4ReBuySFuture=PriceOfProfit4ReBuySFuture;
	}
	
	public BigDecimal getPrice4PricingReBuyS(){
		return Price4PricingReBuyS;
	}
	public void setPrice4PricingReBuyS(BigDecimal Price4PricingReBuyS){
		this.Price4PricingReBuyS=Price4PricingReBuyS;
	}
	
	public BigDecimal getPremium4ReBuyS(){
		return Premium4ReBuyS;
	}
	public void setPremium4ReBuyS(BigDecimal Premium4ReBuyS){
		this.Premium4ReBuyS=Premium4ReBuyS;
	}
	
	public BigDecimal getEstimateFee4ReBuyS(){
		return EstimateFee4ReBuyS;
	}
	public void setEstimateFee4ReBuyS(BigDecimal EstimateFee4ReBuyS){
		this.EstimateFee4ReBuyS=EstimateFee4ReBuyS;
	}
	
	public BigDecimal getRealFee4ReBuyS(){
		return RealFee4ReBuyS;
	}
	public void setRealFee4ReBuyS(BigDecimal RealFee4ReBuyS){
		this.RealFee4ReBuyS=RealFee4ReBuyS;
	}
	
	public BigDecimal getSpread4InitialReBuyS(){
		return Spread4InitialReBuyS;
	}
	public void setSpread4InitialReBuyS(BigDecimal Spread4InitialReBuyS){
		this.Spread4InitialReBuyS=Spread4InitialReBuyS;
	}
	
	public BigDecimal getSpread4QpReBuyS(){
		return Spread4QpReBuyS;
	}
	public void setSpread4QpReBuyS(BigDecimal Spread4QpReBuyS){
		this.Spread4QpReBuyS=Spread4QpReBuyS;
	}
	
	public BigDecimal getSpread4LotReBuyS(){
		return Spread4LotReBuyS;
	}
	public void setSpread4LotReBuyS(BigDecimal Spread4LotReBuyS){
		this.Spread4LotReBuyS=Spread4LotReBuyS;
	}
	
	public BigDecimal getProfit4ReBuySSpot(){
		return Profit4ReBuySSpot;
	}
	public void setProfit4ReBuySSpot(BigDecimal Profit4ReBuySSpot){
		this.Profit4ReBuySSpot=Profit4ReBuySSpot;
	}
	
	public BigDecimal getPriceOfProfit4ReBuySSpot(){
		return PriceOfProfit4ReBuySSpot;
	}
	public void setPriceOfProfit4ReBuySSpot(BigDecimal PriceOfProfit4ReBuySSpot){
		this.PriceOfProfit4ReBuySSpot=PriceOfProfit4ReBuySSpot;
	}
	
	public String getLotId4ReBuyB(){
		return LotId4ReBuyB;
	}
	public void setLotId4ReBuyB(String LotId4ReBuyB){
		this.LotId4ReBuyB=LotId4ReBuyB;
	}
	
	public String getFullNo4ReBuyB(){
		return FullNo4ReBuyB;
	}
	public void setFullNo4ReBuyB(String FullNo4ReBuyB){
		this.FullNo4ReBuyB=FullNo4ReBuyB;
	}
	
	public String getCustomerName4ReBuyB(){
		return CustomerName4ReBuyB;
	}
	public void setCustomerName4ReBuyB(String CustomerName4ReBuyB){
		this.CustomerName4ReBuyB=CustomerName4ReBuyB;
	}
	
	public String getInvoiceNo4ReBuyB(){
		return InvoiceNo4ReBuyB;
	}
	public void setInvoiceNo4ReBuyB(String InvoiceNo4ReBuyB){
		this.InvoiceNo4ReBuyB=InvoiceNo4ReBuyB;
	}
	
	public BigDecimal getProfit4ReBuyBFuture(){
		return Profit4ReBuyBFuture;
	}
	public void setProfit4ReBuyBFuture(BigDecimal Profit4ReBuyBFuture){
		this.Profit4ReBuyBFuture=Profit4ReBuyBFuture;
	}
	
	public BigDecimal getPriceOfProfit4ReBuyBFuture(){
		return PriceOfProfit4ReBuyBFuture;
	}
	public void setPriceOfProfit4ReBuyBFuture(BigDecimal PriceOfProfit4ReBuyBFuture){
		this.PriceOfProfit4ReBuyBFuture=PriceOfProfit4ReBuyBFuture;
	}
	
	public BigDecimal getPrice4PricingReBuyB(){
		return Price4PricingReBuyB;
	}
	public void setPrice4PricingReBuyB(BigDecimal Price4PricingReBuyB){
		this.Price4PricingReBuyB=Price4PricingReBuyB;
	}
	
	public BigDecimal getPremium4ReBuyB(){
		return Premium4ReBuyB;
	}
	public void setPremium4ReBuyB(BigDecimal Premium4ReBuyB){
		this.Premium4ReBuyB=Premium4ReBuyB;
	}
	
	public BigDecimal getEstimateFee4ReBuyB(){
		return EstimateFee4ReBuyB;
	}
	public void setEstimateFee4ReBuyB(BigDecimal EstimateFee4ReBuyB){
		this.EstimateFee4ReBuyB=EstimateFee4ReBuyB;
	}
	
	public BigDecimal getRealFee4ReBuyB(){
		return RealFee4ReBuyB;
	}
	public void setRealFee4ReBuyB(BigDecimal RealFee4ReBuyB){
		this.RealFee4ReBuyB=RealFee4ReBuyB;
	}
	
	public BigDecimal getSpread4InitialReBuyB(){
		return Spread4InitialReBuyB;
	}
	public void setSpread4InitialReBuyB(BigDecimal Spread4InitialReBuyB){
		this.Spread4InitialReBuyB=Spread4InitialReBuyB;
	}
	
	public BigDecimal getSpread4QpReBuyB(){
		return Spread4QpReBuyB;
	}
	public void setSpread4QpReBuyB(BigDecimal Spread4QpReBuyB){
		this.Spread4QpReBuyB=Spread4QpReBuyB;
	}
	
	public BigDecimal getSpread4LotReBuyB(){
		return Spread4LotReBuyB;
	}
	public void setSpread4LotReBuyB(BigDecimal Spread4LotReBuyB){
		this.Spread4LotReBuyB=Spread4LotReBuyB;
	}
	
	public BigDecimal getProfit4ReBuyBSpot(){
		return Profit4ReBuyBSpot;
	}
	public void setProfit4ReBuyBSpot(BigDecimal Profit4ReBuyBSpot){
		this.Profit4ReBuyBSpot=Profit4ReBuyBSpot;
	}
	
	public BigDecimal getPriceOfProfit4ReBuyBSpot(){
		return PriceOfProfit4ReBuyBSpot;
	}
	public void setPriceOfProfit4ReBuyBSpot(BigDecimal PriceOfProfit4ReBuyBSpot){
		this.PriceOfProfit4ReBuyBSpot=PriceOfProfit4ReBuyBSpot;
	}
	
	public BigDecimal getProfit4Futre(){
		return Profit4Futre;
	}
	public void setProfit4Futre(BigDecimal Profit4Futre){
		this.Profit4Futre=Profit4Futre;
	}
	
	public BigDecimal getPriceOfProfit4Futre(){
		return PriceOfProfit4Futre;
	}
	public void setPriceOfProfit4Futre(BigDecimal PriceOfProfit4Futre){
		this.PriceOfProfit4Futre=PriceOfProfit4Futre;
	}
	
	public BigDecimal getProfit4Spot(){
		return Profit4Spot;
	}
	public void setProfit4Spot(BigDecimal Profit4Spot){
		this.Profit4Spot=Profit4Spot;
	}
	
	public BigDecimal getPriceOfProfit4Spot(){
		return PriceOfProfit4Spot;
	}
	public void setPriceOfProfit4Spot(BigDecimal PriceOfProfit4Spot){
		this.PriceOfProfit4Spot=PriceOfProfit4Spot;
	}
	
	public BigDecimal getProfit(){
		return Profit;
	}
	public void setProfit(BigDecimal Profit){
		this.Profit=Profit;
	}
	
	public BigDecimal getPriceOfProfit(){
		return PriceOfProfit;
	}
	public void setPriceOfProfit(BigDecimal PriceOfProfit){
		this.PriceOfProfit=PriceOfProfit;
	}
	
	public String getLotId(){
		return LotId;
	}
	public void setLotId(String LotId){
		this.LotId=LotId;
	}
	
	public Boolean getIsInvoiced(){
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean IsInvoiced){
		this.IsInvoiced=IsInvoiced;
	}
	
	public Boolean getIsCustInvoiced(){
		return IsCustInvoiced;
	}
	public void setIsCustInvoiced(Boolean IsCustInvoiced){
		this.IsCustInvoiced=IsCustInvoiced;
	}
	
	public String getBrandNames4Sms(){
		return BrandNames4Sms;
	}
	public void setBrandNames4Sms(String BrandNames4Sms){
		this.BrandNames4Sms=BrandNames4Sms;
	}
	
	public String getDischarging(){
		return Discharging;
	}
	public void setDischarging(String Discharging){
		this.Discharging=Discharging;
	}
	
	public String getAccountDate(){
		return AccountDate;
	}
	public void setAccountDate(String AccountDate){
		this.AccountDate=AccountDate;
	}
	
	public String getAccountYear(){
		return AccountYear;
	}
	public void setAccountYear(String AccountYear){
		this.AccountYear=AccountYear;
	}
	
	public String getAccountMonth(){
		return AccountMonth;
	}
	public void setAccountMonth(String AccountMonth){
		this.AccountMonth=AccountMonth;
	}
	
	public String getInvoiceCreatedId(){
		return InvoiceCreatedId;
	}
	public void setInvoiceCreatedId(String InvoiceCreatedId){
		this.InvoiceCreatedId=InvoiceCreatedId;
	}
	
	public String getInvoiceCreaterName(){
		return InvoiceCreaterName;
	}
	public void setInvoiceCreaterName(String InvoiceCreaterName){
		this.InvoiceCreaterName=InvoiceCreaterName;
	}

}