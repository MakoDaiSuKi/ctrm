package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * 保值编号
 * @author zengshihua
 *
 */
@Entity
@Table(name = "HedgeNumber", schema = "Physical")
public class HedgeNumber extends HibernateEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4694534043940457145L;
	
	public static final String HN_CATEGORY_1="现货";
	
	public static final String HN_CATEGORY_2="期货";
	
	
    /**
     * 保值方案名称
     * @return
     */
    @Column(name = "Name")
    @JsonProperty(value = "Name")
    private String Name;
    
    /**
     * 来源编号
     */
    @Column(name = "SourceNos")
    @JsonProperty(value = "SourceNos")
    private String SourceNos;
    
    
	/**
	 * 编号
	 */
	@Column(name = "No")
	@JsonProperty(value = "No")
	public String No;
	
	/**
	 * 序号
	 */
	@Column(name = "SerialNo")
	@JsonProperty(value = "SerialNo")
	private String SerialNo;
	
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	public Date TradeDate;
	/**
	 * 分类
	 */
	@Column(name = "Category")
	@JsonProperty(value = "Category")
	public String Category;
	/**
	 * 说明
	 */
	@Column(name = "Comments")
	@JsonProperty(value = "Comments")
	public String Comments;

	/**
	 * 关联的批次-现货
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@Cascade(CascadeType.REFRESH)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "HedgeNumberMapping", schema = "Physical", joinColumns = {
			@JoinColumn(name = "HegdeNumberId" ,insertable=false,updatable=false) }, inverseJoinColumns = {
					@JoinColumn(name = "LotId",insertable=false,updatable=false) }, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "Lots")
	public List<Lot> Lots;
	/**
	 * 关联的头寸-期货
	 */
	@ManyToMany(fetch = FetchType.EAGER, targetEntity = Position.class)
	@Cascade(CascadeType.REFRESH)
	@Fetch(FetchMode.SELECT)
	@JoinTable(name = "HedgeNumberMapping", schema = "Physical", joinColumns = {
			@JoinColumn(name = "HegdeNumberId",insertable=false,updatable=false) }, inverseJoinColumns = {
					@JoinColumn(name = "PositionId",insertable=false,updatable=false) }, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "Positions")
	public List<Position> Positions;

	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/*@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Created")
	private User Created;*/
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	/*@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Updated")
	private User Updated;*/
	
	/**
	 * 是否是追加
	 * @return
	 */
	@Transient
	@JsonProperty(value = "Flag")
	private boolean Flag;
	
	
	/**
	 * 现货盈亏
	 */
	@Transient
    @JsonProperty(value = "SpotProfitAndLoss")
    public BigDecimal SpotProfitAndLoss;
	
	/**
	 * 期货盈亏
	 */
    @Transient
    @JsonProperty(value = "FuturesProfitAndLoss")
    public BigDecimal FuturesProfitAndLoss;

    /**
     * 总盈亏
     */
    @Transient
    @JsonProperty(value = "SummaryProfitAndLoss")
    public BigDecimal SummaryProfitAndLoss;
    
    /**
     * 采购数量汇总
     */
    @Transient
    @JsonProperty(value = "SummaryBuyQuantity")
    public BigDecimal SummaryBuyQuantity;
   
    /**
     * 销售数量汇总
     */
    @Transient
    @JsonProperty(value = "SummarySaleQuantity")
    public BigDecimal SummarySaleQuantity;
    
    /**
     * 现货采购均价
     */
    @Transient
    @JsonProperty(value = "SpotBuyAvgPrice")
    public BigDecimal SpotBuyAvgPrice;
    
    /**
     * 现货销售均价
     */
    @Transient
    @JsonProperty(value = "SpotSaleAvgPrice")
    public BigDecimal SpotSaleAvgPrice;
    
    /**
     * 期货买数量（汇总）
     */
    @Transient
    @JsonProperty(value = "SummaryFuturesBuyQuantity")
    public BigDecimal SummaryFuturesBuyQuantity;
    
    /**
     * 期货卖数量（汇总）
     */
    @Transient
    @JsonProperty(value = "SummaryyFuturesSaleQuantity")
    public BigDecimal SummaryyFuturesSaleQuantity;
    
    /**
     * 期货买均价
     */
    @Transient
    @JsonProperty(value = "FuturesBuyAvgPrice")
    public BigDecimal FuturesBuyAvgPrice;
    
    /**
     *  期货卖均价
     */
    @Transient
    @JsonProperty(value = "FuturesSaleAvgPrice")
    public BigDecimal FuturesSaleAvgPrice;
	
    /**
     * 敞口数量（采购）
     */
    @Transient
    @JsonProperty(value = "ExposureBuyQuantity")
    public BigDecimal ExposureBuyQuantity;
    
    /**
     * 敞口数量（销售）
     */
    @Transient
    @JsonProperty(value = "ExposureSaleQuantity")
    public BigDecimal ExposureSaleQuantity;
    
    
	public String getNo() {
		return No;
	}

	public void setNo(String no) {
		No = no;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		TradeDate = tradeDate;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String comments) {
		Comments = comments;
	}

	public List<Lot> getLots() {
		return Lots;
	}

	public void setLots(List<Lot> lots) {
		Lots = lots;
	}

	public List<Position> getPositions() {
		return Positions;
	}

	public void setPositions(List<Position> positions) {
		Positions = positions;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String updatedId) {
		UpdatedId = updatedId;
	}

	public String getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}

	public boolean getFlag() {
		return Flag;
	}

	public void setFlag(boolean flag) {
		Flag = flag;
	}

	public BigDecimal getSpotProfitAndLoss() {
		return SpotProfitAndLoss;
	}

	public void setSpotProfitAndLoss(BigDecimal spotProfitAndLoss) {
		SpotProfitAndLoss = spotProfitAndLoss;
	}

	public BigDecimal getFuturesProfitAndLoss() {
		return FuturesProfitAndLoss;
	}

	public void setFuturesProfitAndLoss(BigDecimal futuresProfitAndLoss) {
		FuturesProfitAndLoss = futuresProfitAndLoss;
	}

	public BigDecimal getSummaryProfitAndLoss() {
		return SummaryProfitAndLoss;
	}

	public void setSummaryProfitAndLoss(BigDecimal summaryProfitAndLoss) {
		SummaryProfitAndLoss = summaryProfitAndLoss;
	}

	public BigDecimal getSummaryBuyQuantity() {
		return SummaryBuyQuantity;
	}

	public void setSummaryBuyQuantity(BigDecimal summaryBuyQuantity) {
		SummaryBuyQuantity = summaryBuyQuantity;
	}

	public BigDecimal getSummarySaleQuantity() {
		return SummarySaleQuantity;
	}

	public void setSummarySaleQuantity(BigDecimal summarySaleQuantity) {
		SummarySaleQuantity = summarySaleQuantity;
	}

	public BigDecimal getSpotBuyAvgPrice() {
		return SpotBuyAvgPrice;
	}

	public void setSpotBuyAvgPrice(BigDecimal spotBuyAvgPrice) {
		SpotBuyAvgPrice = spotBuyAvgPrice;
	}

	public BigDecimal getSpotSaleAvgPrice() {
		return SpotSaleAvgPrice;
	}

	public void setSpotSaleAvgPrice(BigDecimal spotSaleAvgPrice) {
		SpotSaleAvgPrice = spotSaleAvgPrice;
	}

	public BigDecimal getSummaryFuturesBuyQuantity() {
		return SummaryFuturesBuyQuantity;
	}

	public void setSummaryFuturesBuyQuantity(BigDecimal summaryFuturesBuyQuantity) {
		SummaryFuturesBuyQuantity = summaryFuturesBuyQuantity;
	}

	public BigDecimal getSummaryyFuturesSaleQuantity() {
		return SummaryyFuturesSaleQuantity;
	}

	public void setSummaryyFuturesSaleQuantity(BigDecimal summaryyFuturesSaleQuantity) {
		SummaryyFuturesSaleQuantity = summaryyFuturesSaleQuantity;
	}

	public BigDecimal getFuturesBuyAvgPrice() {
		return FuturesBuyAvgPrice;
	}

	public void setFuturesBuyAvgPrice(BigDecimal futuresBuyAvgPrice) {
		FuturesBuyAvgPrice = futuresBuyAvgPrice;
	}

	public BigDecimal getFuturesSaleAvgPrice() {
		return FuturesSaleAvgPrice;
	}

	public void setFuturesSaleAvgPrice(BigDecimal futuresSaleAvgPrice) {
		FuturesSaleAvgPrice = futuresSaleAvgPrice;
	}

	public BigDecimal getExposureBuyQuantity() {
		return ExposureBuyQuantity;
	}

	public void setExposureBuyQuantity(BigDecimal exposureBuyQuantity) {
		ExposureBuyQuantity = exposureBuyQuantity;
	}

	public BigDecimal getExposureSaleQuantity() {
		return ExposureSaleQuantity;
	}

	public void setExposureSaleQuantity(BigDecimal exposureSaleQuantity) {
		ExposureSaleQuantity = exposureSaleQuantity;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getSourceNos() {
		return SourceNos;
	}

	public void setSourceNos(String sourceNos) {
		SourceNos = sourceNos;
	}

}
