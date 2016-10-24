

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Bvi2Sm", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bvi2Sm extends HibernateEntity {
	private static final long serialVersionUID = 1461832991322L;
	/**
	 *
	 */
	@Column(name = "BviLegalId")
	@JsonProperty(value = "BviLegalId")
	private String BviLegalId;
	/**
	 *
	 */
	@Column(name = "BviCustomerId")
	@JsonProperty(value = "BviCustomerId")
	private String BviCustomerId;
	/**
	 *
	 */
	@Column(name = "BviTitleId")
	@JsonProperty(value = "BviTitleId")
	private String BviTitleId;
	/**
	 *
	 */
	@Column(name = "BviLegalName")
	@JsonProperty(value = "BviLegalName")
	private String BviLegalName;
	/**
	 *
	 */
	@Column(name = "BviLegalCode")
	@JsonProperty(value = "BviLegalCode")
	private String BviLegalCode;
	/**
	 *
	 */
	@Column(name = "BviCustomerName")
	@JsonProperty(value = "BviCustomerName")
	private String BviCustomerName;
	/**
	 *
	 */
	@Column(name = "BviTitleName")
	@JsonProperty(value = "BviTitleName")
	private String BviTitleName;
	/**
	 *
	 */
	@Column(name = "SmLegalId")
	@JsonProperty(value = "SmLegalId")
	private String SmLegalId;
	/**
	 *
	 */
	@Column(name = "SmCustomerId")
	@JsonProperty(value = "SmCustomerId")
	private String SmCustomerId;
	/**
	 *
	 */
	@Column(name = "SmTitleId")
	@JsonProperty(value = "SmTitleId")
	private String SmTitleId;
	/**
	 *
	 */
	@Column(name = "SmLegalName")
	@JsonProperty(value = "SmLegalName")
	private String SmLegalName;
	/**
	 *
	 */
	@Column(name = "SmLegalCode")
	@JsonProperty(value = "SmLegalCode")
	private String SmLegalCode;
	/**
	 *
	 */
	@Column(name = "SmCustomerName")
	@JsonProperty(value = "SmCustomerName")
	private String SmCustomerName;
	/**
	 *
	 */
	@Column(name = "SmTitleName")
	@JsonProperty(value = "SmTitleName")
	private String SmTitleName;
	
	public String getBviLegalId(){
		return BviLegalId;
	}
	public void setBviLegalId(String BviLegalId){
		this.BviLegalId=BviLegalId;
	}
	
	public String getBviCustomerId(){
		return BviCustomerId;
	}
	public void setBviCustomerId(String BviCustomerId){
		this.BviCustomerId=BviCustomerId;
	}
	
	public String getBviTitleId(){
		return BviTitleId;
	}
	public void setBviTitleId(String BviTitleId){
		this.BviTitleId=BviTitleId;
	}
	
	public String getBviLegalName(){
		return BviLegalName;
	}
	public void setBviLegalName(String BviLegalName){
		this.BviLegalName=BviLegalName;
	}
	
	public String getBviLegalCode(){
		return BviLegalCode;
	}
	public void setBviLegalCode(String BviLegalCode){
		this.BviLegalCode=BviLegalCode;
	}
	
	public String getBviCustomerName(){
		return BviCustomerName;
	}
	public void setBviCustomerName(String BviCustomerName){
		this.BviCustomerName=BviCustomerName;
	}
	
	public String getBviTitleName(){
		return BviTitleName;
	}
	public void setBviTitleName(String BviTitleName){
		this.BviTitleName=BviTitleName;
	}
	
	public String getSmLegalId(){
		return SmLegalId;
	}
	public void setSmLegalId(String SmLegalId){
		this.SmLegalId=SmLegalId;
	}
	
	public String getSmCustomerId(){
		return SmCustomerId;
	}
	public void setSmCustomerId(String SmCustomerId){
		this.SmCustomerId=SmCustomerId;
	}
	
	public String getSmTitleId(){
		return SmTitleId;
	}
	public void setSmTitleId(String SmTitleId){
		this.SmTitleId=SmTitleId;
	}
	
	public String getSmLegalName(){
		return SmLegalName;
	}
	public void setSmLegalName(String SmLegalName){
		this.SmLegalName=SmLegalName;
	}
	
	public String getSmLegalCode(){
		return SmLegalCode;
	}
	public void setSmLegalCode(String SmLegalCode){
		this.SmLegalCode=SmLegalCode;
	}
	
	public String getSmCustomerName(){
		return SmCustomerName;
	}
	public void setSmCustomerName(String SmCustomerName){
		this.SmCustomerName=SmCustomerName;
	}
	
	public String getSmTitleName(){
		return SmTitleName;
	}
	public void setSmTitleName(String SmTitleName){
		this.SmTitleName=SmTitleName;
	}

}