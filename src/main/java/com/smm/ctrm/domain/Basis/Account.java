package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Account", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Account extends HibernateEntity {
	private static final long serialVersionUID = 1461832991321L;
	/**
	 * 科目名称
	 */
	@Column(name = "Name", length = 30)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 科目代码
	 */
	@Column(name = "Code", length = 30)
	@JsonProperty(value = "Code")
	private String Code;
	/**
	 * 科目别名
	 */
	@Column(name = "Alias", length = 30)
	@JsonProperty(value = "Alias")
	private String Alias;
	/**
	 * {贷方Dr = D ,借方Cr = C}
	 */
	@Column(name = "DC", length = 1)
	@JsonProperty(value = "DC")
	private String DC;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 禁止删除
	 */
	@Column(name = "IsAllowDelete")
	@JsonProperty(value = "IsAllowDelete",defaultValue="false")
	private Boolean IsAllowDelete;
	/**
	 *  排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex")
	private Integer OrderIndex;
	/**
	 * 父节点
	 */
	@Column(name = "ParentId")
	@JsonProperty(value = "ParentId")
	private String ParentId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
	@JoinColumn(name = "ParentId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Parent")
	private Account Parent;
	@OneToMany(fetch = FetchType.EAGER, targetEntity = Account.class, cascade=CascadeType.ALL, orphanRemoval=true)
	@JoinColumn(name="ParentId", foreignKey=@ForeignKey(name="none"))
	@Fetch(FetchMode.SUBSELECT)
	@JsonProperty(value = "Children")
	private List<Account> Children;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "CreatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Created")
	private User Created;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UpdatedId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@JsonProperty(value = "Updated")
	private User Updated;
	@Transient
	private BigDecimal Quantity;
	
	public BigDecimal getQuantity() {
		return Quantity;
	}
	public void setQuantity(BigDecimal quantity) {
		Quantity = quantity;
	}
	
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
	
	public String getAlias(){
		return Alias;
	}
	public void setAlias(String Alias){
		this.Alias=Alias;
	}
	
	public String getDC(){
		return DC;
	}
	public void setDC(String DC){
		this.DC=DC;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public Boolean getIsAllowDelete(){
		return IsAllowDelete;
	}
	public void setIsAllowDelete(Boolean IsAllowDelete){
		this.IsAllowDelete=IsAllowDelete;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}
	
	public String getParentId(){
		return ParentId;
	}
	public void setParentId(String ParentId){
		this.ParentId=ParentId;
	}
	public Account getParent(){
		return Parent;
	}
	public void setParent(Account Parent){
		this.Parent=Parent;
	}
	public List<Account> getChildren(){
		return Children;
	}
	public void setChildren(List<Account> Children){
		this.Children=Children;
	}
	
	public String getCreatedId(){
		return CreatedId;
	}
	public void setCreatedId(String CreatedId){
		this.CreatedId=CreatedId;
	}
	
	public User getCreated(){
		return Created;
	}
	public void setCreated(User Created){
		this.Created=Created;
	}
	
	public String getUpdatedId(){
		return UpdatedId;
	}
	public void setUpdatedId(String UpdatedId){
		this.UpdatedId=UpdatedId;
	}
	
	public User getUpdated(){
		return Updated;
	}
	public void setUpdated(User Updated){
		this.Updated=Updated;
	}

}