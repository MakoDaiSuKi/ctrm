package com.smm.ctrm.domain.Physical;

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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Market;
@Entity
@Table(name = "Lot", schema="Physical")
public class Lot4MTM3 extends HibernateEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5588465152041273812L;
	
	public static final String sdType_B="B";//采购
	public static final String sdType_S="S";//销售
	public static final String MajorType_P="P";//点价
	public static final String MajorType_A="A";//均价
	public static final String MajorType_F="F";//固定价
	
	@Transient
	@JsonProperty(value="AvgMajor4Pricing")
	private BigDecimal AvgMajor4Pricing;

	/**
	 * 批次号
	 */
	@Column(name="FullNo")
	@JsonProperty(value="FullNo")
	private String FullNo;
	/**
	 * 客户名称
	 */
	@Column(name="CustomerName")
	@JsonProperty(value="CustomerName")
	@Transient
	private String CustomerName;
	/**
	 * 批次数量
	 */
	@Column(name="Quantity")
	@JsonProperty(value="Quantity")
	private BigDecimal Quantity;
	/**
	 * 点价数量
	 */
	@Column(name="QuantityPriced")
	@JsonProperty(value="QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 交付数量
	 */
	@Column(name="QuantityDelivered")
	@JsonProperty(value="QuantityDelivered")
	private BigDecimal QuantityDelivered;
	/**
	 * 点价价格
	 */
	@Column(name="AvgPrice4Pricing")
	@JsonProperty(value="AvgPrice4Pricing")
	@Transient
	private BigDecimal AvgPrice4Pricing;
	/**
	 * 保值价格
	 */
	@Column(name="AvgPrice4Position")
	@JsonProperty(value="AvgPrice4Position")
	@Transient
	private BigDecimal AvgPrice4Position;
	
	
	/**
	 * 点价价格-销售(点价加权平均)
	 */
	@Column(name="AvgPrice4Pricing")
	@JsonProperty(value="AvgPrice4Pricing")
	@Transient
	private BigDecimal AvgPrice4Pricing_S;
	/**
	 * 保值价格-销售(保值加权平均)
	 */
	@Column(name="AvgPrice4Position")
	@JsonProperty(value="AvgPrice4Position")
	@Transient
	private BigDecimal AvgPrice4Position_S;
	
	
	/**
	 * 实时价格
	 */
	@Column(name="M2MPrice")
	@JsonProperty(value="M2MPrice")
	@Transient
	private BigDecimal M2MPrice;
	/**
	 * 升贴水
	 */
	@Column(name="Premium")
	@JsonProperty(value="Premium")
	private BigDecimal Premium;
	/**
	 * 发生费用
	 */
	@Column(name="RealFee")
	@JsonProperty(value="RealFee")
	private BigDecimal RealFee;
	/**
	 * 实时利润
	 */
	@Column(name="M2MProfit")
	@JsonProperty(value="M2MProfit")
	@Transient
	private BigDecimal M2MProfit;

	/**以下数据不需要返回客户端***/
	@Column(name="IsPriced")
	private boolean IsPriced;
	
	@Column(name="CustomerId")
	@JsonProperty(value="CustomerId")
	private String CustomerId;
	
	
	/**
	 * 现货交易类型，eg {采购:B，销售:S，转口:I，生产，...} (Dict)
	 */
    @Column(name = "SpotDirection", length = 2)
    public String SpotDirection;
    
	/**
	 * 收发货的标识
	 */
    @Column(name = "IsDelivered")
    public boolean IsDelivered;
	

    /**
     * 主价市场的标识
     */
    @Column(name = "MajorMarketId")
    public String MajorMarketId;
    
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Market.class)
	@JoinColumn(name = "MajorMarketId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action=NotFoundAction.IGNORE)
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
     * 预计销售日期（QP）
     */
    @Column(name = "QP")
    private Date QP;
    
    /**
     *   点价类型
     *  { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
     */
    @Column(name = "MajorType")
    private String MajorType;
    
    @Column(name = "IsInvoiced")
    private boolean IsInvoiced;
    
    @Column(name = "IsHedged")
    private boolean IsHedged;
    
    @Column(name = "IsFunded")
    private boolean IsFunded;
    
    /**
     * 已经保值的数量之和 
     */
    @Column(name = "QuantityHedged")
    public BigDecimal QuantityHedged;
    
    @Column(name = "LegalId")
    private String LegalId;
    
    public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String fullNo) {
		FullNo = fullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getQuantityPriced() {
		return QuantityPriced!=null?QuantityPriced:new BigDecimal(0);
	}

	public void setQuantityPriced(BigDecimal quantityPriced) {
		QuantityPriced = quantityPriced;
	}

	public BigDecimal getQuantityDelivered() {
		return QuantityDelivered;
	}

	public void setQuantityDelivered(BigDecimal quantityDelivered) {
		QuantityDelivered = quantityDelivered;
	}

	public BigDecimal getAvgPrice4Pricing() {
		return AvgPrice4Pricing;
	}

	public void setAvgPrice4Pricing(BigDecimal avgPrice4Pricing) {
		AvgPrice4Pricing = avgPrice4Pricing;
	}

	public BigDecimal getAvgPrice4Position() {
		return AvgPrice4Position;
	}

	public void setAvgPrice4Position(BigDecimal avgPrice4Position) {
		AvgPrice4Position = avgPrice4Position;
	}

	public BigDecimal getM2MPrice() {
		return M2MPrice;
	}

	public void setM2MPrice(BigDecimal m2mPrice) {
		M2MPrice = m2mPrice;
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

	public BigDecimal getM2MProfit() {
		return M2MProfit;
	}

	public void setM2MProfit(BigDecimal m2mProfit) {
		M2MProfit = m2mProfit;
	}

	
	
	/**以下字段不需要返回客户端**/
	@JsonIgnore
	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}
	@JsonIgnore
	public String getSpotDirection() {
		return SpotDirection;
	}

	public void setSpotDirection(String spotDirection) {
		SpotDirection = spotDirection;
	}
	@JsonIgnore
	public boolean getIsDelivered() {
		return IsDelivered;
	}

	public void setIsDelivered(boolean isDelivered) {
		IsDelivered = isDelivered;
	}
	@JsonIgnore
	public String getMajorMarketId() {
		return MajorMarketId;
	}
	
	public void setMajorMarketId(String majorMarketId) {
		MajorMarketId = majorMarketId;
	}
	@JsonIgnore
	public Market getMajorMarket() {
		return MajorMarket;
	}

	public void setMajorMarket(Market majorMarket) {
		MajorMarket = majorMarket;
	}
	@JsonIgnore
	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String commodityId) {
		CommodityId = commodityId;
	}
	@JsonIgnore
	public Commodity getCommodity() {
		return Commodity;
	}

	public void setCommodity(Commodity commodity) {
		Commodity = commodity;
	}
	@JsonIgnore
	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String contractId) {
		ContractId = contractId;
	}
	@JsonIgnore
	public Contract getContract() {
		return Contract;
	}

	public void setContract(Contract contract) {
		Contract = contract;
	}
	@JsonIgnore
	public Date getQP() {
		return QP;
	}

	public void setQP(Date qP) {
		QP = qP;
	}
	
	@JsonIgnore
	public boolean getIsPriced() {
		return IsPriced;
	}

	public void setIsPriced(boolean isPriced) {
		IsPriced = isPriced;
	}
	@JsonIgnore
	public String getMajorType() {
		return MajorType;
	}

	public void setMajorType(String majorType) {
		MajorType = majorType;
	}
	@JsonIgnore
	public boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(boolean isInvoiced) {
		IsInvoiced = isInvoiced;
	}
	@JsonIgnore
	public boolean getIsHedged() {
		return IsHedged;
	}

	public void setIsHedged(boolean isHedged) {
		IsHedged = isHedged;
	}
	@JsonIgnore
	public boolean getIsFunded() {
		return IsFunded;
	}

	public void setIsFunded(boolean isFunded) {
		IsFunded = isFunded;
	}
	@JsonIgnore
	public BigDecimal getQuantityHedged() {
		return QuantityHedged;
	}

	public void setQuantityHedged(BigDecimal quantityHedged) {
		QuantityHedged = quantityHedged;
	}

	public BigDecimal getAvgPrice4Pricing_S() {
		return AvgPrice4Pricing_S!=null?AvgPrice4Pricing_S:new BigDecimal(0);
	}

	public void setAvgPrice4Pricing_S(BigDecimal avgPrice4Pricing_S) {
		AvgPrice4Pricing_S = avgPrice4Pricing_S;
	}

	public BigDecimal getAvgPrice4Position_S() {
		return AvgPrice4Position_S!=null?AvgPrice4Position_S:new BigDecimal(0);
	}

	public void setAvgPrice4Position_S(BigDecimal avgPrice4Position_S) {
		AvgPrice4Position_S = avgPrice4Position_S;
	}

	public BigDecimal getAvgMajor4Pricing() {
		return AvgMajor4Pricing!=null?AvgMajor4Pricing:new BigDecimal(0);
	}

	public void setAvgMajor4Pricing(BigDecimal avgMajor4Pricing) {
		AvgMajor4Pricing = avgMajor4Pricing;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String legalId) {
		LegalId = legalId;
	}
	
}
