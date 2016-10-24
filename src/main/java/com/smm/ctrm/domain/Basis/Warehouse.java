

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Warehouse", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Warehouse extends HibernateEntity {
	private static final long serialVersionUID = 1461832991336L;
	/**
	 * 仓库名称 
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 *  联系人
	 */
	@Column(name = "Liaison", length = 30)
	@JsonProperty(value = "Liaison")
	private String Liaison;
	/**
	 *  联系电话
	 */
	@Column(name = "Phone", length = 30)
	@JsonProperty(value = "Phone")
	private String Phone;
	/**
	 * 地址 
	 */
	@Column(name = "Address", length = 100)
	@JsonProperty(value = "Address")
	private String Address;
	/**
	 * 排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex")
	private Integer OrderIndex;
	/**
	 *  备注
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
	
	public String getLiaison(){
		return Liaison;
	}
	public void setLiaison(String Liaison){
		this.Liaison=Liaison;
	}
	
	public String getPhone(){
		return Phone;
	}
	public void setPhone(String Phone){
		this.Phone=Phone;
	}
	
	public String getAddress(){
		return Address;
	}
	public void setAddress(String Address){
		this.Address=Address;
	}
	@JsonProperty(defaultValue="0")
	public Integer getOrderIndex(){
		return OrderIndex;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}