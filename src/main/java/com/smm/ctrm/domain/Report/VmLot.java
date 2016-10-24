

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class VmLot extends HibernateEntity {
	private static final long serialVersionUID = 1461719402488L;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 合同号
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 盈亏
	 */
	@JsonProperty(value = "PnL")
	private BigDecimal PnL;
	/**
	 * 合同数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 已交付数量 = 实际的交付数量
	 */
	@JsonProperty(value = "QuantityStoraged")
	private BigDecimal QuantityStoraged;
	/**
	 * 已经开票的净重之和 
	 */
	@JsonProperty(value = "QuantityInvoiced")
	private BigDecimal QuantityInvoiced;
	/**
	 * 未开票数量
	 */
	@JsonProperty(value = "QuantityUnInvoiced")
	private BigDecimal QuantityUnInvoiced;
	/**
	 * 已点数量 
	 */
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 已经保值的数量之和 
	 */
	@JsonProperty(value = "QuantityHedged")
	private BigDecimal QuantityHedged;
	/**
	 * IsStoraged = false时，DueBalance才 = Quantity * Price
	 */
	@JsonProperty(value = "DueBalance")
	private BigDecimal DueBalance;
	/**
	 * 已经发生的余额
	 */
	@JsonProperty(value = "PaidBalance")
	private BigDecimal PaidBalance;
	/**
	 * 最新余额 = 应收应付 - 已经发生
	 */
	@JsonProperty(value = "LastBalance")
	private BigDecimal LastBalance;
	/**
	 * 收发货的标识
	 */
	@JsonProperty(value = "IsDelivered")
	private Boolean IsDelivered;
	/**
	 * 收或者开，发票的标识
	 */
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 * 点价的标识
	 */
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 * 保值的标识。如果是FALSE，表示存在敞口。
	 */
	@JsonProperty(value = "IsHedged")
	private Boolean IsHedged;
	/**
	 * 收付款的标识
	 */
	@JsonProperty(value = "IsFunded")
	private Boolean IsFunded;
	/**
	 * 盈亏结算的标识。完成盈亏结算 不等于 会计记账
	 */
	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;
	/**
	 * 会计记账的标识
	 */
	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;
	/**
	 * 品牌名称
	 */
	@JsonProperty(value = "BrandNames")
	private String BrandNames;
	/**
	 * 已点价格 Price = Major + Premium
	 */
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 点价类型
	 */
	@JsonProperty(value = "MajorType")
	private String MajorType;
	/**
	 * 市场价格
	 */
	@JsonProperty(value = "M2MPrice")
	private BigDecimal M2MPrice;
	/**
	 * （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果 
	 */
	@JsonProperty(value = "Major")
	private BigDecimal Major;
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
	 */
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 *｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
	 */
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;
	/**
	 * 均价开始日期
	 */
	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;
	/**
	 * 均价结束日期
	 */
	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;
	/**
	 * 均价天数
	 */
	@JsonProperty(value = "MajorDays")
	private Integer MajorDays;
	/**
	 * 每天点价数量
	 */
	@JsonProperty(value = "QtyPerMainDay")
	private BigDecimal QtyPerMainDay;
	/**
	 * 结算日期
	 */
	@JsonProperty(value = "QP")
	private Date QP;
	/**
	 * 货币(Dict)
	 */
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 品种ID（合同中来）
	 */
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	/**
	 * 交易类型（合同中来）
	 */
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 价格偏差
	 */
	@JsonProperty(value = "PriceDiff")
	private BigDecimal PriceDiff;
	/**
	 * 敞口
	 */
	@JsonProperty(value = "Exposure")
	private BigDecimal Exposure;
	/**
	 *  商品名称（合同中来）
	 */
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 签约日期（合同中来）
	 */
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 交付方式（合同中来）
	 */
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;
	/**
	 * 估计出发日期（交付期始）
	 */
	@JsonProperty(value = "ETD")
	private Date ETD;
	/**
	 * 估计到达日期（交付期止）
	 */
	@JsonProperty(value = "ETA")
	private Date ETA;
	/**
	 * 实际到达日期 
	 */
	@JsonProperty(value = "ATA")
	private Date ATA;
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getFullNo(){
		return FullNo;
	}
	public void setFullNo(String FullNo){
		this.FullNo=FullNo;
	}
	
	public BigDecimal getPnL(){
		return PnL;
	}
	public void setPnL(BigDecimal PnL){
		this.PnL=PnL;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantityStoraged(){
		return QuantityStoraged;
	}
	public void setQuantityStoraged(BigDecimal QuantityStoraged){
		this.QuantityStoraged=QuantityStoraged;
	}
	
	public BigDecimal getQuantityInvoiced(){
		return QuantityInvoiced;
	}
	public void setQuantityInvoiced(BigDecimal QuantityInvoiced){
		this.QuantityInvoiced=QuantityInvoiced;
	}
	
	public BigDecimal getQuantityUnInvoiced(){
		return QuantityUnInvoiced;
	}
	public void setQuantityUnInvoiced(BigDecimal QuantityUnInvoiced){
		this.QuantityUnInvoiced=QuantityUnInvoiced;
	}
	
	public BigDecimal getQuantityPriced(){
		return QuantityPriced;
	}
	public void setQuantityPriced(BigDecimal QuantityPriced){
		this.QuantityPriced=QuantityPriced;
	}
	
	public BigDecimal getQuantityHedged(){
		return QuantityHedged;
	}
	public void setQuantityHedged(BigDecimal QuantityHedged){
		this.QuantityHedged=QuantityHedged;
	}
	
	public BigDecimal getDueBalance(){
		return DueBalance;
	}
	public void setDueBalance(BigDecimal DueBalance){
		this.DueBalance=DueBalance;
	}
	
	public BigDecimal getPaidBalance(){
		return PaidBalance;
	}
	public void setPaidBalance(BigDecimal PaidBalance){
		this.PaidBalance=PaidBalance;
	}
	
	public BigDecimal getLastBalance(){
		return LastBalance;
	}
	public void setLastBalance(BigDecimal LastBalance){
		this.LastBalance=LastBalance;
	}
	
	public Boolean getIsDelivered(){
		return IsDelivered;
	}
	public void setIsDelivered(Boolean IsDelivered){
		this.IsDelivered=IsDelivered;
	}
	
	public Boolean getIsInvoiced(){
		return IsInvoiced;
	}
	public void setIsInvoiced(Boolean IsInvoiced){
		this.IsInvoiced=IsInvoiced;
	}
	
	public Boolean getIsPriced(){
		return IsPriced;
	}
	public void setIsPriced(Boolean IsPriced){
		this.IsPriced=IsPriced;
	}
	
	public Boolean getIsHedged(){
		return IsHedged;
	}
	public void setIsHedged(Boolean IsHedged){
		this.IsHedged=IsHedged;
	}
	
	public Boolean getIsFunded(){
		return IsFunded;
	}
	public void setIsFunded(Boolean IsFunded){
		this.IsFunded=IsFunded;
	}
	
	public Boolean getIsSettled(){
		return IsSettled;
	}
	public void setIsSettled(Boolean IsSettled){
		this.IsSettled=IsSettled;
	}
	
	public Boolean getIsAccounted(){
		return IsAccounted;
	}
	public void setIsAccounted(Boolean IsAccounted){
		this.IsAccounted=IsAccounted;
	}
	
	public String getBrandNames(){
		return BrandNames;
	}
	public void setBrandNames(String BrandNames){
		this.BrandNames=BrandNames;
	}
	
	public BigDecimal getPrice(){
		return Price;
	}
	public void setPrice(BigDecimal Price){
		this.Price=Price;
	}
	
	public String getMajorType(){
		return MajorType;
	}
	public void setMajorType(String MajorType){
		this.MajorType=MajorType;
	}
	
	public BigDecimal getM2MPrice(){
		return M2MPrice;
	}
	public void setM2MPrice(BigDecimal M2MPrice){
		this.M2MPrice=M2MPrice;
	}
	
	public BigDecimal getMajor(){
		return Major;
	}
	public void setMajor(BigDecimal Major){
		this.Major=Major;
	}
	
	public BigDecimal getPremium(){
		return Premium;
	}
	public void setPremium(BigDecimal Premium){
		this.Premium=Premium;
	}
	
	public String getMajorBasis(){
		return MajorBasis;
	}
	public void setMajorBasis(String MajorBasis){
		this.MajorBasis=MajorBasis;
	}
	
	public Date getMajorStartDate(){
		return MajorStartDate;
	}
	public void setMajorStartDate(Date MajorStartDate){
		this.MajorStartDate=MajorStartDate;
	}
	
	public Date getMajorEndDate(){
		return MajorEndDate;
	}
	public void setMajorEndDate(Date MajorEndDate){
		this.MajorEndDate=MajorEndDate;
	}
	
	public Integer getMajorDays(){
		return MajorDays;
	}
	public void setMajorDays(Integer MajorDays){
		this.MajorDays=MajorDays;
	}
	
	public BigDecimal getQtyPerMainDay(){
		return QtyPerMainDay;
	}
	public void setQtyPerMainDay(BigDecimal QtyPerMainDay){
		this.QtyPerMainDay=QtyPerMainDay;
	}
	
	public Date getQP(){
		return QP;
	}
	public void setQP(Date QP){
		this.QP=QP;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
	}
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public String getSpotDirection(){
		return SpotDirection;
	}
	public void setSpotDirection(String SpotDirection){
		this.SpotDirection=SpotDirection;
	}
	
	public BigDecimal getPriceDiff(){
		return PriceDiff;
	}
	public void setPriceDiff(BigDecimal PriceDiff){
		this.PriceDiff=PriceDiff;
	}
	
	public BigDecimal getExposure(){
		return Exposure;
	}
	public void setExposure(BigDecimal Exposure){
		this.Exposure=Exposure;
	}
	
	public String getProduct(){
		return Product;
	}
	public void setProduct(String Product){
		this.Product=Product;
	}
	
	public Date getTradeDate(){
		return TradeDate;
	}
	public void setTradeDate(Date TradeDate){
		this.TradeDate=TradeDate;
	}
	
	public String getDeliveryTerm(){
		return DeliveryTerm;
	}
	public void setDeliveryTerm(String DeliveryTerm){
		this.DeliveryTerm=DeliveryTerm;
	}
	
	public Date getETD(){
		return ETD;
	}
	public void setETD(Date ETD){
		this.ETD=ETD;
	}
	
	public Date getETA(){
		return ETA;
	}
	public void setETA(Date ETA){
		this.ETA=ETA;
	}
	
	public Date getATA(){
		return ATA;
	}
	public void setATA(Date ATA){
		this.ATA=ATA;
	}

}