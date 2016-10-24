

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Physical.Lot;
public class R8 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402487L;

	/**
	 * 合同号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
	 */
	@JsonProperty(value = "LotFullNo")
	private String LotFullNo;
	/**
	 * 合同数量
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 * 已点数量
	 */
	@JsonProperty(value = "LotQuantityPriced")
	private BigDecimal LotQuantityPriced;
	/**
	 * 往来单位
	 */
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 * 原QP
	 */
	@JsonProperty(value = "LotQP")
	private Date LotQP;
	
	@JsonProperty(value = "R7s")
	private List<R7> R7s; 
	
	public String getLotFullNo(){
		return LotFullNo;
	}
	public void setLotFullNo(String LotFullNo){
		this.LotFullNo=LotFullNo;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getLotQuantityPriced(){
		return LotQuantityPriced;
	}
	public void setLotQuantityPriced(BigDecimal LotQuantityPriced){
		this.LotQuantityPriced=LotQuantityPriced;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public Date getLotQP(){
		return LotQP;
	}
	public void setLotQP(Date LotQP){
		this.LotQP=LotQP;
	}
	public List<R7> getR7s() {
		return R7s;
	}
	public void setR7s(List<R7> r7s) {
		R7s = r7s;
	}

}