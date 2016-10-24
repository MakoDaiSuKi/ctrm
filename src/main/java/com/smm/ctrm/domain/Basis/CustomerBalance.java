

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;

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
@Table(name = "CustomerBalance", schema="Basis")

public class CustomerBalance extends HibernateEntity {
	private static final long serialVersionUID = 1461832991340L;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "TabNo",defaultValue="0")
	private Integer TabNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "TabName")
	private String TabName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "RowNo")
	private Integer RowNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LegalName")
	private String LegalName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerAddress")
	private String CustomerAddress;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerTel")
	private String CustomerTel;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerFax")
	private String CustomerFax;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ValidateResult")
	private String ValidateResult;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ProcessResult")
	private String ProcessResult;
	/**
	 *  期初余额
	 */
	@Column(name = "InitBalance")
	@JsonProperty(value = "InitBalance")
	private BigDecimal InitBalance;
	/**
	 *  应收应付余额
	 */
	@Column(name = "DueBalance")
	@JsonProperty(value = "DueBalance")
	private BigDecimal DueBalance;
	/**
	 *  已付余额
	 */
	@Column(name = "PaidBalance")
	@JsonProperty(value = "PaidBalance")
	private BigDecimal PaidBalance;
	/**
	 *  最新余额
	 */
	@Column(name = "LastBalance")
	@JsonProperty(value = "LastBalance")
	private BigDecimal LastBalance;
	/**
	 * 货币(Dict)
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 客户
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 *  内部台头
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	@JoinColumn(name = "LegalId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	
	public Integer getTabNo(){
		return TabNo;
	}
	public void setTabNo(Integer TabNo){
		this.TabNo=TabNo;
	}
	
	public String getTabName(){
		return TabName;
	}
	public void setTabName(String TabName){
		this.TabName=TabName;
	}
	
	public Integer getRowNo(){
		return RowNo;
	}
	public void setRowNo(Integer RowNo){
		this.RowNo=RowNo;
	}
	
	public String getLegalName(){
		return LegalName;
	}
	public void setLegalName(String LegalName){
		this.LegalName=LegalName;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getCustomerAddress(){
		return CustomerAddress;
	}
	public void setCustomerAddress(String CustomerAddress){
		this.CustomerAddress=CustomerAddress;
	}
	
	public String getCustomerTel(){
		return CustomerTel;
	}
	public void setCustomerTel(String CustomerTel){
		this.CustomerTel=CustomerTel;
	}
	
	public String getCustomerFax(){
		return CustomerFax;
	}
	public void setCustomerFax(String CustomerFax){
		this.CustomerFax=CustomerFax;
	}
	
	public String getCommodityName(){
		return CommodityName;
	}
	public void setCommodityName(String CommodityName){
		this.CommodityName=CommodityName;
	}
	
	public String getValidateResult(){
		return ValidateResult;
	}
	public void setValidateResult(String ValidateResult){
		this.ValidateResult=ValidateResult;
	}
	
	public String getProcessResult(){
		return ProcessResult;
	}
	public void setProcessResult(String ProcessResult){
		this.ProcessResult=ProcessResult;
	}
	
	public BigDecimal getInitBalance(){
		return InitBalance;
	}
	public void setInitBalance(BigDecimal InitBalance){
		this.InitBalance=InitBalance;
	}
	
	public BigDecimal getDueBalance(){
		return DueBalance;
	}
	public void setDueBalance(BigDecimal DueBalance){
		this.DueBalance=DueBalance;
	}
	
	public BigDecimal getPaidBalance(){
		return PaidBalance;
	}
	public void setPaidBalance(BigDecimal PaidBalance){
		this.PaidBalance=PaidBalance;
	}
	
	public BigDecimal getLastBalance(){
		return LastBalance;
	}
	public void setLastBalance(BigDecimal LastBalance){
		this.LastBalance=LastBalance;
	}
	
	public String getCurrency(){
		return Currency;
	}
	public void setCurrency(String Currency){
		this.Currency=Currency;
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
	
	public String getCommodityId(){
		return CommodityId;
	}
	public void setCommodityId(String CommodityId){
		this.CommodityId=CommodityId;
	}
	
	public Commodity getCommodity(){
		return Commodity;
	}
	public void setCommodity(Commodity Commodity){
		this.Commodity=Commodity;
	}

}