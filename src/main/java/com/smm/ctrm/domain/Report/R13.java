

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class R13 extends HibernateEntity {
	private static final long serialVersionUID = 1461719402486L;
	/**
	 *
	 */
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 *
	 */
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 *
	 */
	@JsonProperty(value = "EstimateSaleDate")
	private Date EstimateSaleDate;
	/**
	 *
	 */
	@JsonProperty(value = "QP")
	private Date QP;
	/**
	 *
	 */
	@JsonProperty(value = "PromptDate")
	private String PromptDate;
	
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
	
	public Date getEstimateSaleDate(){
		return EstimateSaleDate;
	}
	public void setEstimateSaleDate(Date EstimateSaleDate){
		this.EstimateSaleDate=EstimateSaleDate;
	}
	
	public Date getQP(){
		return QP;
	}
	public void setQP(Date QP){
		this.QP=QP;
	}
	
	public String getPromptDate(){
		return PromptDate;
	}
	public void setPromptDate(String PromptDate){
		this.PromptDate=PromptDate;
	}

}