

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
@Table(name = "Spec", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Spec extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 *  规格名称
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 *  规格代码
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 *  备份
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *  排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex")
	private Integer OrderIndex;
	/**
	 *  品种
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
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex!=null?OrderIndex:0;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
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