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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Commodity;

@Entity
@Table(name = "SpotPriceEstimate", schema = "Physical")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SpotPriceEstimate extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4979813360458357007L;

	/**
	 * 日期
	 */
	@Column(name = "EstimateDate")
	@JsonProperty(value = "EstimateDate")
	private Date EstimateDate;

	/**
	 * 买入价
	 */
	@Column(name = "BuyPrice")
	@JsonProperty(value = "BuyPrice")
	private BigDecimal BuyPrice;

	/**
	 * 卖出价
	 */
	@Column(name = "SellPrice")
	@JsonProperty(value = "SellPrice")
	private BigDecimal SellPrice;

	/**
	 * 币别
	 */
	@Column(name = "Currency")
	@JsonProperty(value = "Currency")
	private String Currency;
	
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
	
	
	
	public Date getEstimateDate() {
		return EstimateDate;
	}
	public void setEstimateDate(Date estimateDate) {
		EstimateDate = estimateDate;
	}
	public BigDecimal getBuyPrice() {
		return BuyPrice;
	}
	public void setBuyPrice(BigDecimal buyPrice) {
		BuyPrice = buyPrice;
	}
	public BigDecimal getSellPrice() {
		return SellPrice;
	}
	public void setSellPrice(BigDecimal sellPrice) {
		SellPrice = sellPrice;
	}
	public String getCurrency() {
		return Currency;
	}
	public void setCurrency(String currency) {
		Currency = currency;
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
	
}
