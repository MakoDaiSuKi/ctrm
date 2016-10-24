package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Market;

@Entity
@Table(name = "Lot", schema = "Physical")
public class Lot4Unpriced extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3576222561444938957L;

	@Column(name = "FullNo")
	@JsonProperty(value = "FullNo")
	private String FullNo;

	/**
	 * 合同数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;

	/**
	 * 实际的交付数量
	 */
	@Column(name = "QuantityDelivered")
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;

	/**
	 * 合同签订日的价格
	 */
	@Transient
	@JsonProperty(value = "TradeDatePrice")
	private BigDecimal TradeDatePrice;
	/**
	 * （2）升贴水部分
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;

	/**
	 * （3.1）各种杂费(实际发生)：都是折算成 ?/MT 的值
	 */
	@Column(name = "RealFee")
	@JsonProperty(value = "RealFee")
	private BigDecimal RealFee;
	
	@Column(name = "LegalId")
    private String LegalId;

	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "M2MPrice")
	private BigDecimal M2MPrice;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "M2MProfit")
	private BigDecimal M2MProfit;

	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;

	/**
	 * 交易对手 = 客户或者供应商
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	
	@Transient
	@JsonProperty(value = "Customer")
	private Customer Customer;

	/**
	 * 主价市场的标识
	 */
	@Column(name = "MajorMarketId")
	public String MajorMarketId;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MajorMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "MajorMarket")
	public Market MajorMarket;

	/**
	 * 品种ID
	 */
	@Column(name = "CommodityId")
	public String CommodityId;

	/**
	 * 品种
	 */
	@Transient
	public Commodity Commodity;
	
	
	 /**
     * 多对一：合同
     */
    @Column(name = "ContractId")
    public String ContractId;
    
    
    @Transient
    public  Contract Contract;
    
    /**
     *   点价类型
     *  { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
     */
    @Column(name = "MajorType")
    private String MajorType;
    
    /**
	 * 点价数量
	 */
	@Column(name="QuantityPriced")
	@JsonProperty(value="QuantityPriced")
	private BigDecimal QuantityPriced;

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}

	public BigDecimal getTradeDatePrice() {
		return TradeDatePrice;
	}

	public void setTradeDatePrice(BigDecimal tradeDatePrice) {
		TradeDatePrice = tradeDatePrice;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal premium) {
		Premium = premium;
	}

	public BigDecimal getRealFee() {
		return RealFee;
	}

	public void setRealFee(BigDecimal realFee) {
		RealFee = realFee;
	}

	public BigDecimal getM2MPrice() {
		return M2MPrice;
	}

	public void setM2MPrice(BigDecimal m2mPrice) {
		M2MPrice = m2mPrice;
	}

	public BigDecimal getM2MProfit() {
		return M2MProfit;
	}

	public void setM2MProfit(BigDecimal m2mProfit) {
		M2MProfit = m2mProfit;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer customer) {
		Customer = customer;
	}

	public String getMajorMarketId() {
		return MajorMarketId;
	}

	public void setMajorMarketId(String majorMarketId) {
		MajorMarketId = majorMarketId;
	}

	public Market getMajorMarket() {
		return MajorMarket;
	}

	public void setMajorMarket(Market majorMarket) {
		MajorMarket = majorMarket;
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

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String contractId) {
		ContractId = contractId;
	}

	public Contract getContract() {
		return Contract;
	}

	public void setContract(Contract contract) {
		Contract = contract;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}

	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String majorType) {
		MajorType = majorType;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}

	public void setQuantityPriced(BigDecimal quantityPriced) {
		QuantityPriced = quantityPriced;
	}

	
	
}
