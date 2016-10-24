

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class R12 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	/**
	 * 结算日期 
	 */
	@JsonProperty(value = "QP")
	private Date QP;
	/**
	 * 采购合同数量
	 */
	@JsonProperty(value = "P_Quantity")
	private BigDecimal P_Quantity;
	/**
	 * 采购已经点价的数量之和 
	 */
	@JsonProperty(value = "P_QuantityPriced")
	private BigDecimal P_QuantityPriced;
	/**
	 * 采购已经保值的数量之和 
	 */
	@JsonProperty(value = "P_QuantityHedged")
	private BigDecimal P_QuantityHedged;
	/**
	 * 采购保值偏差数量
	 */
	@JsonProperty(value = "P_DiffHedged")
	private BigDecimal P_DiffHedged;
	/**
	 * 采购应点数量
	 */
	@JsonProperty(value = "P_DiffPriced")
	private BigDecimal P_DiffPriced;
	/**
	 * 销售合同数量
	 */
	@JsonProperty(value = "S_Quantity")
	private BigDecimal S_Quantity;
	/**
	 * 销售已经点价的数量之和 
	 */
	@JsonProperty(value = "S_QuantityPriced")
	private BigDecimal S_QuantityPriced;
	/**
	 * 销售已经保值的数量之和 
	 */
	@JsonProperty(value = "S_QuantityHedged")
	private BigDecimal S_QuantityHedged;
	/**
	 * 销售保值偏差数量
	 */
	@JsonProperty(value = "S_DiffHedged")
	private BigDecimal S_DiffHedged;
	/**
	 * 销售应点数量
	 */
	@JsonProperty(value = "S_DiffPriced")
	private BigDecimal S_DiffPriced;
	/**
	 * 净头寸
	 */
	@JsonProperty(value = "CleanHedged")
	private BigDecimal CleanHedged;
	/**
	 * 应点数量
	 */
	@JsonProperty(value = "MustPriced")
	private BigDecimal MustPriced;
	/**
	 * 敞口数量
	 */
	@JsonProperty(value = "ExposureQuantity")
	private BigDecimal ExposureQuantity;
	
	 /**
	  * 点价数量
	  */
	@JsonProperty(value = "QuantityPriced")
    public BigDecimal QuantityPriced;
    
	public Date getQP(){
		return QP;
	}
	public void setQP(Date QP){
		this.QP=QP;
	}
	
	public BigDecimal getP_Quantity(){
		return P_Quantity;
	}
	public void setP_Quantity(BigDecimal P_Quantity){
		this.P_Quantity=P_Quantity;
	}
	
	public BigDecimal getP_QuantityPriced(){
		return P_QuantityPriced;
	}
	public void setP_QuantityPriced(BigDecimal P_QuantityPriced){
		this.P_QuantityPriced=P_QuantityPriced;
	}
	
	public BigDecimal getP_QuantityHedged(){
		return P_QuantityHedged;
	}
	public void setP_QuantityHedged(BigDecimal P_QuantityHedged){
		this.P_QuantityHedged=P_QuantityHedged;
	}
	
	public BigDecimal getP_DiffHedged(){
		return P_DiffHedged;
	}
	public void setP_DiffHedged(BigDecimal P_DiffHedged){
		this.P_DiffHedged=P_DiffHedged;
	}
	
	public BigDecimal getP_DiffPriced(){
		return P_DiffPriced;
	}
	public void setP_DiffPriced(BigDecimal P_DiffPriced){
		this.P_DiffPriced=P_DiffPriced;
	}
	
	public BigDecimal getS_Quantity(){
		return S_Quantity;
	}
	public void setS_Quantity(BigDecimal S_Quantity){
		this.S_Quantity=S_Quantity;
	}
	
	public BigDecimal getS_QuantityPriced(){
		return S_QuantityPriced;
	}
	public void setS_QuantityPriced(BigDecimal S_QuantityPriced){
		this.S_QuantityPriced=S_QuantityPriced;
	}
	
	public BigDecimal getS_QuantityHedged(){
		return S_QuantityHedged;
	}
	public void setS_QuantityHedged(BigDecimal S_QuantityHedged){
		this.S_QuantityHedged=S_QuantityHedged;
	}
	
	public BigDecimal getS_DiffHedged(){
		return S_DiffHedged;
	}
	public void setS_DiffHedged(BigDecimal S_DiffHedged){
		this.S_DiffHedged=S_DiffHedged;
	}
	
	public BigDecimal getS_DiffPriced(){
		return S_DiffPriced;
	}
	public void setS_DiffPriced(BigDecimal S_DiffPriced){
		this.S_DiffPriced=S_DiffPriced;
	}
	
	public BigDecimal getCleanHedged(){
		return CleanHedged;
	}
	public void setCleanHedged(BigDecimal CleanHedged){
		this.CleanHedged=CleanHedged;
	}
	
	public BigDecimal getMustPriced(){
		return MustPriced;
	}
	public void setMustPriced(BigDecimal MustPriced){
		this.MustPriced=MustPriced;
	}
	
	public BigDecimal getExposureQuantity(){
		return ExposureQuantity;
	}
	public void setExposureQuantity(BigDecimal ExposureQuantity){
		this.ExposureQuantity=ExposureQuantity;
	}
	public BigDecimal getQuantityPriced() {
		return QuantityPriced;
	}
	public void setQuantityPriced(BigDecimal quantityPriced) {
		QuantityPriced = quantityPriced;
	}

}