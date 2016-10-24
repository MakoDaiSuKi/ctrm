

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Commodity", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Commodity extends HibernateEntity {
	private static final long serialVersionUID = 1461832991322L;
	/**
	 * 商品品种 
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	
	private String Name;
	/**
	 * 编码，必须是2位字母、均大写 
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 计量单位 (Dict)
	 */
	@Column(name = "Unit", length = 3)
	@JsonProperty(value = "Unit")
	private String Unit;
	/**
	 * 数量小数位数
	 */
	@Column(name = "Digits")
	@JsonProperty(value = "Digits",defaultValue="0")
	private Integer Digits;
	/**
	 * 单价小数位数
	 */
	@Column(name = "Digits4Price")
	@JsonProperty(value = "Digits4Price",defaultValue="0")
	private Integer Digits4Price = 0;
	/**
	 * 每手数量
	 */
	@Column(name = "QuantityPerLot")
	@JsonProperty(value = "QuantityPerLot",defaultValue="0")
	private Integer QuantityPerLot;
	/**
	 * 排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex",defaultValue="0")
	private Integer OrderIndex;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 禁止同步lme价格
	 */
	@Column(name = "IsFobiddenSyncLme")
	@JsonProperty(value = "IsFobiddenSyncLme",defaultValue="false")
	private Boolean IsFobiddenSyncLme=false;
	
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
	
	public String getUnit(){
		return Unit;
	}
	public void setUnit(String Unit){
		this.Unit=Unit;
	}
	
	public Integer getDigits(){
		return Digits!=null?Digits:0;
	}
	public void setDigits(Integer Digits){
		this.Digits=Digits!=null?Digits:0;
	}
	
	public Integer getDigits4Price(){
		return Digits4Price!=null?Digits4Price:0;
	}
	public void setDigits4Price(Integer Digits4Price){
		this.Digits4Price=Digits4Price!=null?Digits4Price:0;
	}
	
	public Integer getQuantityPerLot(){
		return QuantityPerLot!=null?QuantityPerLot:0;
	}
	public void setQuantityPerLot(Integer QuantityPerLot){
		this.QuantityPerLot=QuantityPerLot!=null?QuantityPerLot:0;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex!=null?OrderIndex:0;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex!=null?OrderIndex:0;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public Boolean getIsFobiddenSyncLme(){
		return IsFobiddenSyncLme!=null?IsFobiddenSyncLme:false;
	}
	public void setIsFobiddenSyncLme(Boolean IsFobiddenSyncLme){
		this.IsFobiddenSyncLme=IsFobiddenSyncLme!=null?IsFobiddenSyncLme:false;
	}

}