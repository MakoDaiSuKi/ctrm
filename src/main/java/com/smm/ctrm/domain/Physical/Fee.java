
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

@Entity
@Table(name = "Fee", schema = "Physical")

public class Fee extends HibernateEntity {
	private static final long serialVersionUID = 1461832991341L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * 序号
	 */
	@Column(name = "SerialNo")
	@JsonProperty(value = "SerialNo")
	private Integer SerialNo;
	/**
	 * 费用的类别
	 */
	@Column(name = "FeeCode")
	@JsonProperty(value = "FeeCode")
	private String FeeCode;
	/**
	 * 费用的基准
	 */
	@Column(name = "FeeBasis")
	@JsonProperty(value = "FeeBasis")
	private String FeeBasis;
	/**
	 * 币种
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 费率
	 */
	@Column(name = "Rate")
	@JsonProperty(value = "Rate")
	private BigDecimal Rate;
	/**
	 * 估算的费用的金额
	 */
	@Column(name = "AmountEstimated")
	@JsonProperty(value = "AmountEstimated")
	private BigDecimal AmountEstimated;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 实际发生的费用的金额 --- 由系统自动统计
	 */
	@Column(name = "AmountDone")
	@JsonProperty(value = "AmountDone")
	private BigDecimal AmountDone;
	/**
	 * 是否已经冲销指定类别的费用
	 */
	@Column(name = "IsEliminated")
	@JsonProperty(value = "IsEliminated")
	private Boolean IsEliminated;
	/**
	 * 多对一：合同
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	@JoinColumn(name = "ContractId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Contract")
	private Contract Contract;
	/**
	 * 多对一：批次
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	// @JsonBackReference("Fee_Lot")
//	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
//	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
//	@NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Lot")
	private Lot Lot;

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public Integer getSerialNo() {
		return SerialNo;
	}

	public void setSerialNo(Integer SerialNo) {
		this.SerialNo = SerialNo;
	}

	public String getFeeCode() {
		return FeeCode;
	}

	public void setFeeCode(String FeeCode) {
		this.FeeCode = FeeCode;
	}

	public String getFeeBasis() {
		return FeeBasis;
	}

	public void setFeeBasis(String FeeBasis) {
		this.FeeBasis = FeeBasis;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public BigDecimal getRate() {
		if(Rate == null){
			Rate = new BigDecimal(0);
		}
		return Rate;
	}

	public void setRate(BigDecimal Rate) {
		this.Rate = Rate;
	}

	public BigDecimal getAmountEstimated() {
		if(AmountEstimated == null){
			AmountEstimated = new BigDecimal(0);
		}
		return AmountEstimated;
	}

	public void setAmountEstimated(BigDecimal AmountEstimated) {
		this.AmountEstimated = AmountEstimated;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public BigDecimal getAmountDone() {
		if(AmountDone == null){
			AmountDone = new BigDecimal(0);
		}
		return AmountDone;
	}

	public void setAmountDone(BigDecimal AmountDone) {
		this.AmountDone = AmountDone;
	}

	public Boolean getIsEliminated() {
		return IsEliminated;
	}

	public void setIsEliminated(Boolean IsEliminated) {
		this.IsEliminated = IsEliminated;
	}

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String ContractId) {
		this.ContractId = ContractId;
	}

	public Contract getContract() {
		return Contract;
	}

	public void setContract(Contract Contract) {
		this.Contract = Contract;
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