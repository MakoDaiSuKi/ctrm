package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * 头寸平仓表
 * 
 * @author zengshihua
 *
 */
@Entity
@Table(name = "PositionUnravel", schema = "Physical")
public class PositionUnravel extends HibernateEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7842230534782667641L;

	/**
	 * 原始头寸Id
	 */
	@Column(name = "SourcePtId")
	@JsonProperty(value = "SourcePtId")
	private String SourcePtId;

	/**
	 * 平仓头寸Id
	 */
	@Column(name = "UnravelPtId")
	@JsonProperty(value = "UnravelPtId")
	private String UnravelPtId;

	/**
	 * 原始头寸价格
	 */
	@Column(name = "SourcePtPrice")
	@JsonProperty(value = "SourcePtPrice")
	private BigDecimal SourcePtPrice;

	/**
	 * 平仓头寸价格
	 */
	@Column(name = "UnravelPtPrice")
	@JsonProperty(value = "UnravelPtPrice")
	private BigDecimal UnravelPtPrice;

	/**
	 * 平仓数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;

	/**
	 * 平仓盈亏
	 */
	@Column(name = "ProfitAndLoss")
	@JsonProperty(value = "ProfitAndLoss")
	private BigDecimal ProfitAndLoss;

	/**
	 * 平仓时间
	 */
	@Column(name = "UnravelDate")
	@JsonProperty(value = "UnravelDate")
	private Date UnravelDate;

	/**
	 * 创建人Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;

	public String getSourcePtId() {
		return SourcePtId;
	}

	public void setSourcePtId(String sourcePtId) {
		SourcePtId = sourcePtId;
	}

	public String getUnravelPtId() {
		return UnravelPtId;
	}

	public void setUnravelPtId(String unravelPtId) {
		UnravelPtId = unravelPtId;
	}

	public BigDecimal getSourcePtPrice() {
		return SourcePtPrice;
	}

	public void setSourcePtPrice(BigDecimal sourcePtPrice) {
		SourcePtPrice = sourcePtPrice;
	}

	public BigDecimal getUnravelPtPrice() {
		return UnravelPtPrice;
	}

	public void setUnravelPtPrice(BigDecimal unravelPtPrice) {
		UnravelPtPrice = unravelPtPrice;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}

	public BigDecimal getProfitAndLoss() {
		return ProfitAndLoss;
	}

	public void setProfitAndLoss(BigDecimal profitAndLoss) {
		ProfitAndLoss = profitAndLoss;
	}

	public Date getUnravelDate() {
		return UnravelDate;
	}

	public void setUnravelDate(Date unravelDate) {
		UnravelDate = unravelDate;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String createdId) {
		CreatedId = createdId;
	}

	
}
