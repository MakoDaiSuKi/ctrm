

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Org", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Org extends HibernateEntity {
	private static final long serialVersionUID = 1461832991324L;
	/**
	 * 机构名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 机构代码
	 */
	@Column(name = "Code")
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 机构地址
	 */
	@Column(name = "Address")
	@JsonProperty(value = "Address")
	private String Address;
	/**
	 *
	 */
	@Column(name = "Zip")
	@JsonProperty(value = "Zip")
	private String Zip;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getCode(){
		return Code;
	}
	public void setCode(String Code){
		this.Code=Code;
	}
	
	public String getAddress(){
		return Address;
	}
	public void setAddress(String Address){
		this.Address=Address;
	}
	
	public String getZip(){
		return Zip;
	}
	public void setZip(String Zip){
		this.Zip=Zip;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}