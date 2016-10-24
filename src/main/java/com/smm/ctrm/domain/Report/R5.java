

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
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Physical.Contract;

@Entity
@Table(name = "Lot", schema = "Physical")
public class R5 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402487L;
	/**
	 * 品种
	 */
	@Transient
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
	/**
	 * 现货交易类型，eg {采购:P，销售:S，转口:I，生产，...} (Dict)
	 */
	@Column(name = "SpotDirection", length = 2)
	@JsonProperty(value = "SpotDirection")
	private String SpotDirection;
	/**
	 * 合同号
	 */
	@Transient
	@JsonProperty(value = "ContractHeadNo")
	private String ContractHeadNo;
	/**
	 * 合同号
	 */
	@Column(name = "FullNo")
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 点价方式 {PAF + FA的组合模式}
	 */
	@Column(name = "PricingType", length = 30)
	@JsonProperty(value = "PricingType")
	private String PricingType;
	/**
	 * 主点价方式 
	 */
	@Column(name = "MajorType", length = 30)
	@JsonProperty(value = "MajorType")
	private String MajorType;
	/**
	 * 往来单位
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *  商品名称
	 */
	@Column(name = "Product", length = 64)
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 合同数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 商品价格 Price = Major + Premium
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 已经点价的数量之和 
	 */
	@Column(name = "QuantityPriced")
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 主价点价的每天数量
	 */
	@Column(name = "QtyPerMainDay")
	@JsonProperty(value = "QtyPerMainDay")
	private BigDecimal QtyPerMainDay;
	/**
	 *
	 */
	@Column(name = "MajorStartDate")
	@JsonProperty(value = "MajorStartDate")
	private Date MajorStartDate;
	/**
	 *
	 */
	@Column(name = "MajorEndDate")
	@JsonProperty(value = "MajorEndDate")
	private Date MajorEndDate;
	/**
	 * 主价点价时的所需天数
	 */
	@Column(name = "MajorDays")
	@JsonProperty(value = "MajorDays")
	private Integer MajorDays;
	/**
	 * 点价的标识
	 */
	@Column(name = "IsPriced")
	@JsonProperty(value = "IsPriced")
	private Boolean IsPriced;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "IsPricedString")
	private String IsPricedString;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 *｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
	 */
	@Column(name = "MajorBasis", length = 30)
	@JsonProperty(value = "MajorBasis")
	private String MajorBasis;
	
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	@JoinColumn(name = "ContractId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Contract")
	private Contract Contract;
	
	public String getCommodityCode(){
		return CommodityCode;
	}
	public void setCommodityCode(String CommodityCode){
		this.CommodityCode=CommodityCode;
	}
	
	public String getSpotDirection(){
		return SpotDirection;
	}
	public void setSpotDirection(String SpotDirection){
		this.SpotDirection=SpotDirection;
	}
	
	public String getContractHeadNo(){
		return ContractHeadNo;
	}
	public void setContractHeadNo(String ContractHeadNo){
		this.ContractHeadNo=ContractHeadNo;
	}
	
	public String getPricingType(){
		return PricingType;
	}
	public void setPricingType(String PricingType){
		this.PricingType=PricingType;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getProduct(){
		return Product;
	}
	public void setProduct(String Product){
		this.Product=Product;
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
	
	public BigDecimal getQuantityPriced(){
		return QuantityPriced;
	}
	public void setQuantityPriced(BigDecimal QuantityPriced){
		this.QuantityPriced=QuantityPriced;
	}
	
	public BigDecimal getQtyPerMainDay(){
		return QtyPerMainDay;
	}
	public void setQtyPerMainDay(BigDecimal QtyPerMainDay){
		this.QtyPerMainDay=QtyPerMainDay;
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
	
	public Boolean getIsPriced(){
		return IsPriced;
	}
	public void setIsPriced(Boolean IsPriced){
		this.IsPriced=IsPriced;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
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
	public String getIsPricedString() {
		return (IsPriced!=null&&IsPriced) ? "已完成" : "未完成";
	}
	public void setMajorBasis(String MajorBasis){
		this.MajorBasis=MajorBasis;
	}
	public String getCommodityId() {
		return CommodityId;
	}
	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}
	public Commodity getCommodity() {
		return Commodity;
	}
	public void setCommodity(Commodity commodity) {
		Commodity = commodity;
	}
	public Contract getContract() {
		return Contract;
	}
	public void setContract(Contract contract) {
		Contract = contract;
	}
	public void setIsPricedString(String isPricedString) {
		IsPricedString = isPricedString;
	}
	public String getFullNo() {
		return FullNo;
	}
	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}
	public String getMajorType() {
		return MajorType;
	}
	public void setMajorType(String majorType) {
		MajorType = majorType;
	}

}