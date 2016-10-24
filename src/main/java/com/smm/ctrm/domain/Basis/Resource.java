

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Resource", schema="Basis")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="ResourceType", length=10, discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("Basic")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Resource extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;


    public static final String TYPE_Menu="Menu";

    public static final String TYPE_Button="Button";


	/**
	 *
	 */
	@Column(name = "IsBeginGroup")
	@JsonProperty(value = "IsBeginGroup")
	private Boolean IsBeginGroup;
	/**
	 * 仅供开发用途，不显示在用户的菜单权限中
	 */
	@Column(name = "IsDevOnly")
	@JsonProperty(value = "IsDevOnly")
	private Boolean IsDevOnly;
	/**
	 * 资源类型包括: {menu, button, report}
	 */
	@Column(name = "ResourceType", insertable=false, updatable=false)
	@JsonProperty(value = "ResourceType")
	private String ResourceType;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "ResType")
	private String ResType;
	/**
	 * 语言
	 */
	@Column(name = "Lang", length = 10, nullable = false)
	@JsonProperty(value = "Lang")
	private String Lang;
	/**
	 * 资源名称
	 */
	@Column(name = "Name", nullable = false)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 排序
	 */
	@Column(name = "OrderIndex")
	@JsonProperty(value = "OrderIndex",defaultValue="0")
	private Integer OrderIndex;
	/**
	 * 禁止显示, true = 不显示, false = 显示
	 */
	@Column(name = "IsForbidden")
	@JsonProperty(value = "IsForbidden")
	private Boolean IsForbidden;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 *
	 */
	@Column(name = "ParentId")
	@JsonProperty(value = "ParentId")
	private String ParentId;
	
	public Boolean getIsBeginGroup(){
		return IsBeginGroup!=null?IsBeginGroup:false;
	}
	public void setIsBeginGroup(Boolean IsBeginGroup){
		this.IsBeginGroup=IsBeginGroup;
	}
	
	public Boolean getIsDevOnly(){
		return IsDevOnly!=null?IsBeginGroup:false;
	}
	public void setIsDevOnly(Boolean IsDevOnly){
		this.IsDevOnly=IsDevOnly;
	}
	
	public String getResourceType(){
		return ResourceType;
	}
	public void setResourceType(String ResourceType){
		this.ResourceType=ResourceType;
	}
	
	public String getResType(){
		return ResType;
	}
	public void setResType(String ResType){
		this.ResType=ResType;
	}
	
	public String getLang(){
		return Lang;
	}
	public void setLang(String Lang){
		this.Lang=Lang;
	}
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public Integer getOrderIndex(){
		return OrderIndex!=null?OrderIndex:0;
	}
	public void setOrderIndex(Integer OrderIndex){
		this.OrderIndex=OrderIndex;
	}
	
	public Boolean getIsForbidden(){
		return IsForbidden!=null?IsForbidden:false;
	}
	public void setIsForbidden(Boolean IsForbidden){
		this.IsForbidden=IsForbidden;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}
	
	public String getParentId(){
		return ParentId;
	}
	public void setParentId(String ParentId){
		this.ParentId=ParentId;
	}

}