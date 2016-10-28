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
@Table(name = "PositionChangeMonth", schema = "Physical")
public class PositionChangeMonth extends HibernateEntity {

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
	 * 换月头寸Id
	 */
	@Column(name = "ChangeMonthPtId")
	@JsonProperty(value = "ChangeMonthPtId")
	private String ChangeMonthPtId;

	/**
	 * 原始头寸价格
	 */
	@Column(name = "SourcePtPrice")
	@JsonProperty(value = "SourcePtPrice")
	private BigDecimal SourcePtPrice;

	/**
	 * 换月头寸价格
	 */
	@Column(name = "ChangeMonthPtPrice")
	@JsonProperty(value = "ChangeMonthPtPrice")
	private BigDecimal ChangeMonthPtPrice;

	/**
	 * 换月数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;

	/**
	 * 换月盈亏
	 */
	@Column(name = "ProfitAndLoss")
	@JsonProperty(value = "ProfitAndLoss")
	private BigDecimal ProfitAndLoss;

	/**
	 * 换月时间
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

	public String getChangeMonthPtId() {
		return ChangeMonthPtId;
	}

	public void setChangeMonthPtId(String changeMonthPtId) {
		ChangeMonthPtId = changeMonthPtId;
	}

	public BigDecimal getSourcePtPrice() {
		return SourcePtPrice;
	}

	public void setSourcePtPrice(BigDecimal sourcePtPrice) {
		SourcePtPrice = sourcePtPrice;
	}

	public BigDecimal getChangeMonthPtPrice() {
		return ChangeMonthPtPrice;
	}

	public void setChangeMonthPtPrice(BigDecimal changeMonthPtPrice) {
		ChangeMonthPtPrice = changeMonthPtPrice;
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
