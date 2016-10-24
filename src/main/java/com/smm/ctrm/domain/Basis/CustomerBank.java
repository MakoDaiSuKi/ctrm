

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "CustomerBank", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CustomerBank extends HibernateEntity {
	private static final long serialVersionUID = 1461832991341L;
	/**
	 * 分理处名称
	 */
	@Column(name = "Branch")
	@JsonProperty(value = "Branch")
	private String Branch;
	/**
	 * 银行账号
	 */
	@Column(name = "Account")
	@JsonProperty(value = "Account")
	private String Account;
	/**
	 * 分理处地址
	 */
	@Column(name = "Address")
	@JsonProperty(value = "Address")
	private String Address;
	/**
	 * 用于打印输出（包含，银行名称，账号等信息）
	 */
	@Column(name = "BankNote", length = 5000)
	@JsonProperty(value = "BankNote")
	private String BankNote;
	/**
	 * 客户台头
	 */
	@Column(name = "CustomerTitleId")
	@JsonProperty(value = "CustomerTitleId")
	private String CustomerTitleId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = CustomerTitle.class)
	@JoinColumn(name = "CustomerTitleId", insertable = false, updatable = false, foreignKey=@ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "CustomerTitle")
	private CustomerTitle CustomerTitle;
	/**
	 * 银行标识
	 */
	@Column(name = "BankId")
	@JsonProperty(value = "BankId")
	private String BankId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Bank.class)
	@JoinColumn(name = "BankId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Bank")
	private Bank Bank;
	
	public String getBranch(){
		return Branch;
	}
	public void setBranch(String Branch){
		this.Branch=Branch;
	}
	
	public String getAccount(){
		return Account;
	}
	public void setAccount(String Account){
		this.Account=Account;
	}
	
	public String getAddress(){
		return Address;
	}
	public void setAddress(String Address){
		this.Address=Address;
	}
	
	public String getBankNote(){
		return BankNote;
	}
	public void setBankNote(String BankNote){
		this.BankNote=BankNote;
	}
	
	public String getCustomerTitleId(){
		return CustomerTitleId;
	}
	public void setCustomerTitleId(String CustomerTitleId){
		this.CustomerTitleId=CustomerTitleId;
	}
	
	public CustomerTitle getCustomerTitle(){
		return CustomerTitle;
	}
	public void setCustomerTitle(CustomerTitle CustomerTitle){
		this.CustomerTitle=CustomerTitle;
	}
	
	public String getBankId(){
		return BankId;
	}
	public void setBankId(String BankId){
		this.BankId=BankId;
	}
	
	public Bank getBank(){
		return Bank;
	}
	public void setBank(Bank Bank){
		this.Bank=Bank;
	}

}