

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class R11 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	
	@JsonProperty(value = "QuantityDiff")
	private BigDecimal QuantityDiff;
	
	/**
	 * 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 * 合同数量
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 实际的交付数量
	 */
	@JsonProperty(value = "QuantityDelivered")
	private BigDecimal QuantityDelivered;
	/**
	 * 已经点价的数量之和 
	 */
	@JsonProperty(value = "QuantityPriced")
	private BigDecimal QuantityPriced;
	/**
	 * 改点未点
	 */
	@JsonProperty(value = "QuantityPricedDiff")
	private BigDecimal QuantityPricedDiff;
	/**
	 * 已经保值的数量之和 
	 */
	@JsonProperty(value = "QuantityHedged")
	private BigDecimal QuantityHedged;
	/**
	 * 临时变量
	 */
	@JsonProperty(value = "Temp")
	private BigDecimal Temp;
	/**
	 * 该保未保 
	 */
	@JsonProperty(value = "QuantityHedgedDiff")
	private BigDecimal QuantityHedgedDiff;
	/**
	 * 原始数量
	 */
	@JsonProperty(value = "QuantityOriginal")
	private BigDecimal QuantityOriginal;
	
	public String getFullNo(){
		return FullNo;
	}
	public void setFullNo(String FullNo){
		this.FullNo=FullNo;
	}
	
	public BigDecimal getQuantity(){
		return Quantity;
	}
	public void setQuantity(BigDecimal Quantity){
		this.Quantity=Quantity;
	}
	
	public BigDecimal getQuantityDelivered(){
		return QuantityDelivered;
	}
	public void setQuantityDelivered(BigDecimal QuantityDelivered){
		this.QuantityDelivered=QuantityDelivered;
	}
	
	public BigDecimal getQuantityPriced(){
		return QuantityPriced;
	}
	public void setQuantityPriced(BigDecimal QuantityPriced){
		this.QuantityPriced=QuantityPriced;
	}
	
	public BigDecimal getQuantityPricedDiff(){
		return QuantityPricedDiff;
	}
	public void setQuantityPricedDiff(BigDecimal QuantityPricedDiff){
		this.QuantityPricedDiff=QuantityPricedDiff;
	}
	
	public BigDecimal getQuantityHedged(){
		return QuantityHedged;
	}
	public void setQuantityHedged(BigDecimal QuantityHedged){
		this.QuantityHedged=QuantityHedged;
	}
	
	public BigDecimal getTemp(){
		return Temp;
	}
	public void setTemp(BigDecimal Temp){
		this.Temp=Temp;
	}
	
	public BigDecimal getQuantityHedgedDiff(){
		return QuantityHedgedDiff;
	}
	public void setQuantityHedgedDiff(BigDecimal QuantityHedgedDiff){
		this.QuantityHedgedDiff=QuantityHedgedDiff;
	}
	
	public BigDecimal getQuantityOriginal(){
		return QuantityOriginal;
	}
	public void setQuantityOriginal(BigDecimal QuantityOriginal){
		this.QuantityOriginal=QuantityOriginal;
	}
	public BigDecimal getQuantityDiff() {
		return QuantityDiff;
	}
	public void setQuantityDiff(BigDecimal quantityDiff) {
		QuantityDiff = quantityDiff;
	}

}