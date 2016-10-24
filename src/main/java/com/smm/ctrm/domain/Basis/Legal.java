

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Legal", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Legal extends HibernateEntity {
	private static final long serialVersionUID = 1461832991323L;
	/**
	 *  内部台头名字
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 *  内部台头代码
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 内部台头地址
	 */
	@Column(name = "Address", length = 100)
	@JsonProperty(value = "Address")
	private String Address;
	/**
	 *  备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *  排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex",defaultValue="0")
	private Integer OrderIndex;
	/**
	 * 是否是内部客户{true = 是内部客户,  false = 不是内部客户}
	 */
	@Column(name = "IsInternalCustomer")
	@JsonProperty(value = "IsInternalCustomer",defaultValue="false")
	private Boolean IsInternalCustomer;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	/**
	 * 作为内部客户时的客户标识
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	/**
	 * 作为内部客户时的客户名称
	 */
	@Column(name = "CustomerName")
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *  
	 */
	@Column(name = "OrgId")
	@JsonProperty(value = "OrgId")
	private String OrgId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Org.class)
	@JoinColumn(name = "OrgId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Org")
	private Org Org;
	
	/**
	 * 是否允许换货
	 */
	@Column(name="IsExchangeGoods")
	@JsonProperty("IsExchangeGoods")
	private Boolean IsExchangeGoods;
	
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
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}
	
	public Boolean getIsInternalCustomer(){
		return IsInternalCustomer;
	}
	public void setIsInternalCustomer(Boolean IsInternalCustomer){
		this.IsInternalCustomer=IsInternalCustomer;
	}
	
	public String getCreatedId(){
		return CreatedId;
	}
	public void setCreatedId(String CreatedId){
		this.CreatedId=CreatedId;
	}
	
	public String getUpdatedId(){
		return UpdatedId;
	}
	public void setUpdatedId(String UpdatedId){
		this.UpdatedId=UpdatedId;
	}
	
	public String getCustomerId(){
		return CustomerId;
	}
	public void setCustomerId(String CustomerId){
		this.CustomerId=CustomerId;
	}
	
	public String getCustomerName(){
		return CustomerName;
	}
	public void setCustomerName(String CustomerName){
		this.CustomerName=CustomerName;
	}
	
	public String getOrgId(){
		return OrgId;
	}
	public void setOrgId(String OrgId){
		this.OrgId=OrgId;
	}
	
	public Org getOrg(){
		return Org;
	}
	public void setOrg(Org Org){
		this.Org=Org;
	}
	public Boolean getIsExchangeGoods() {
		return IsExchangeGoods!=null?IsExchangeGoods:false;
	}
	public void setIsExchangeGoods(Boolean isExchangeGoods) {
		IsExchangeGoods = isExchangeGoods;
	}

}