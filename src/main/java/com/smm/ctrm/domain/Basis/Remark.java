

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Remark", schema="Basis")

public class Remark extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 * {CNT = 合同, DLR = 交付，STR = 出入库，FND = 资金，INV = 发票，PST = 头寸，CST = 客户 }
	 */
	@Column(name = "MarkType")
	@JsonProperty(value = "MarkType")
	private String MarkType;
	/**
	 *
	 */
	@Column(name = "MarkName")
	@JsonProperty(value = "MarkName")
	private String MarkName;
	/**
	 *
	 */
	@Column(name = "BackColor")
	@JsonProperty(value = "BackColor",defaultValue="0")
	private Integer BackColor;
	/**
	 *
	 */
	@Column(name = "OrderNumber")
	@JsonProperty(value = "OrderNumber",defaultValue="0")
	private Integer OrderNumber;
	/**
	 *
	 */
	@Column(name = "Comments")
	@JsonProperty(value = "Comments")
	private String Comments;
	
	public String getMarkType(){
		return MarkType;
	}
	public void setMarkType(String MarkType){
		this.MarkType=MarkType;
	}
	
	public String getMarkName(){
		return MarkName;
	}
	public void setMarkName(String MarkName){
		this.MarkName=MarkName;
	}
	
	public Integer getBackColor(){
		return BackColor;
	}
	public void setBackColor(Integer BackColor){
		this.BackColor=BackColor;
	}
	
	public Integer getOrderNumber(){
		return OrderNumber;
	}
	public void setOrderNumber(Integer OrderNumber){
		this.OrderNumber=OrderNumber;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}