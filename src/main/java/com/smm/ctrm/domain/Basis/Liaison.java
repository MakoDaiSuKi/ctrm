

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Liaison", schema="Basis")

public class Liaison extends HibernateEntity {
	private static final long serialVersionUID = 1461832991342L;
	/**
	 *
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 *
	 */
	@Column(name = "Email")
	@JsonProperty(value = "Email")
	private String Email;
	/**
	 *
	 */
	@Column(name = "Tel")
	@JsonProperty(value = "Tel")
	private String Tel;
	/**
	 *
	 */
	@Column(name = "Fax")
	@JsonProperty(value = "Fax")
	private String Fax;
	/**
	 *
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
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getEmail(){
		return Email;
	}
	public void setEmail(String Email){
		this.Email=Email;
	}
	
	public String getTel(){
		return Tel;
	}
	public void setTel(String Tel){
		this.Tel=Tel;
	}
	
	public String getFax(){
		return Fax;
	}
	public void setFax(String Fax){
		this.Fax=Fax;
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