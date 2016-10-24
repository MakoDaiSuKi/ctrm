

package com.smm.ctrm.domain.Report;

import java.math.BigDecimal;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
public class RptApplyInvoiceDetail extends HibernateEntity {
	private static final long serialVersionUID = 1461719402485L;
	
	/**
	 *
	 */
	@JsonProperty(value = "PID")
	private String PID;
	/**
	 *
	 */
	@JsonProperty(value = "LotQuantity")
	private BigDecimal LotQuantity;
	/**
	 *
	 */
	@JsonProperty(value = "LotPrice")
	private BigDecimal LotPrice;
	/**
	 *
	 */
	@JsonProperty(value = "LegalId")
	private String LegalId;
//	@JsonBackReference("RptApplyInvoiceDetail_Legal")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 *
	 */
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
//	@JsonBackReference("RptApplyInvoiceDetail_Customer")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false)
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	
	
	public String getPID(){
		return PID;
	}
	public void setPID(String PID){
		this.PID=PID;
	}
	
	public BigDecimal getLotQuantity(){
		return LotQuantity;
	}
	public void setLotQuantity(BigDecimal LotQuantity){
		this.LotQuantity=LotQuantity;
	}
	
	public BigDecimal getLotPrice(){
		return LotPrice;
	}
	public void setLotPrice(BigDecimal LotPrice){
		this.LotPrice=LotPrice;
	}
	
	public String getLegalId(){
		return LegalId;
	}
	public void setLegalId(String LegalId){
		this.LegalId=LegalId;
	}
	
	public Legal getLegal(){
		return Legal;
	}
	public void setLegal(Legal Legal){
		this.Legal=Legal;
	}
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public Customer getCustomer(){
		return Customer;
	}
	public void setCustomer(Customer Customer){
		this.Customer=Customer;
	}

}