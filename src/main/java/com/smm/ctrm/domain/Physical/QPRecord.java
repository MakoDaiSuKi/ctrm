
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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "QPRecord", schema = "Physical")
public class QPRecord extends HibernateEntity {
	private static final long serialVersionUID = 1461832991345L;
	/**
	 *
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 当前QP
	 */
	@Column(name = "CurrentQP")
	@JsonProperty(value = "CurrentQP")
	private Date CurrentQP;
	/**
	 * 修改后的QP
	 */
	@Column(name = "RevisedQP")
	@JsonProperty(value = "RevisedQP")
	private Date RevisedQP;
	/**
	 * 当前Reuters价格
	 */
	@Column(name = "CurrentQPPrice")
	@JsonProperty(value = "CurrentQPPrice")
	private BigDecimal CurrentQPPrice;
	/**
	 * 修改后的Reuters价格
	 */
	@Column(name = "RevisedQPPrice")
	@JsonProperty(value = "RevisedQPPrice")
	private BigDecimal RevisedQPPrice;
	/**
	 * 价差
	 */
	@Column(name = "PriceDiff")
	@JsonProperty(value = "PriceDiff")
	private BigDecimal PriceDiff;
	/**
	 * 初始QP调期标记
	 */
	@Column(name = "IsInitial")
	@JsonProperty(value = "IsInitial")
	private Boolean IsInitial;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	/**
	 * 多对一：批次
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	// @JsonBackReference("QPRecord_Lot")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private Lot Lot;

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public Date getCurrentQP() {
		return CurrentQP;
	}

	public void setCurrentQP(Date CurrentQP) {
		this.CurrentQP = CurrentQP;
	}

	public Date getRevisedQP() {
		return RevisedQP;
	}

	public void setRevisedQP(Date RevisedQP) {
		this.RevisedQP = RevisedQP;
	}

	public BigDecimal getCurrentQPPrice() {
		return CurrentQPPrice;
	}

	public void setCurrentQPPrice(BigDecimal CurrentQPPrice) {
		this.CurrentQPPrice = CurrentQPPrice;
	}

	public BigDecimal getRevisedQPPrice() {
		return RevisedQPPrice;
	}

	public void setRevisedQPPrice(BigDecimal RevisedQPPrice) {
		this.RevisedQPPrice = RevisedQPPrice;
	}

	public BigDecimal getPriceDiff() {
		return PriceDiff;
	}

	public void setPriceDiff(BigDecimal PriceDiff) {
		this.PriceDiff = PriceDiff;
	}

	public Boolean getIsInitial() {
		return IsInitial;
	}

	public void setIsInitial(Boolean IsInitial) {
		this.IsInitial = IsInitial;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String LotId) {
		this.LotId = LotId;
	}

	public Lot getLot() {
		return Lot;
	}

	public void setLot(Lot Lot) {
		this.Lot = Lot;
	}

}