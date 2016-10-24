

package com.smm.ctrm.domain.Basis;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yutao.zhao
 * 菜单
 */
@Entity
@DiscriminatorValue(value="Menu")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Menu extends Resource{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1548977874715759517L;
	/**
	 * Web 菜单图标
	 */
	@Column(name = "Icon", length = 100)
	@JsonProperty(value = "Icon")
	private String Icon;
	/**
	 * Web 菜单链接
	 */
	@Column(name = "Url", length = 100)
	@JsonProperty(value = "Url")
	private String Url;
	/**
	 * WinForm 菜单图片
	 */
	@Column(name = "Image", length = 100)
	@JsonProperty(value = "Image")
	private String Image;
	/**
	 * WinForm窗体名称
	 */
	@Column(name = "FrmName", length = 100)
	@JsonProperty(value = "FrmName")
	private String FrmName;
	/**
	 * WinForm窗体类型
	 */
	@Column(name = "IsMdiFrm")
	@JsonProperty(value = "IsMdiFrm")
	private Boolean IsMdiFrm;
	/**
	 * 是否报表类的窗体。报表窗体使用区别的打开方式。
	 */
	@Column(name = "IsReport")
	@JsonProperty(value = "IsReport")
	private Boolean IsReport;
	@JsonBackReference(value="Menu_Parent")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Menu.class)
	@JoinColumn(foreignKey = @ForeignKey(name="none"), insertable = false, updatable = false, name = "ParentId")
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Parent")
	private Menu Parent;
	/**
	 *
	 */
	@JsonProperty(value = "Children")
	@Transient
	private List<Menu> Children;
	
	
	public String getIcon(){
		return Icon;
	}
	public void setIcon(String Icon){
		this.Icon=Icon;
	}
	
	public String getUrl(){
		return Url;
	}
	public void setUrl(String Url){
		this.Url=Url;
	}
	
	public String getImage(){
		return Image;
	}
	public void setImage(String Image){
		this.Image=Image;
	}
	
	public String getFrmName(){
		return FrmName;
	}
	public void setFrmName(String FrmName){
		this.FrmName=FrmName;
	}
	
	public Boolean getIsMdiFrm(){
		return IsMdiFrm!=null?IsMdiFrm:false;
	}
	public void setIsMdiFrm(Boolean IsMdiFrm){
		this.IsMdiFrm=IsMdiFrm;
	}
	
	public Boolean getIsReport(){
		return IsReport!=null?IsReport:false;
	}
	public void setIsReport(Boolean IsReport){
		this.IsReport=IsReport;
	}
	
	public Menu getParent(){
		return Parent;
	}
	public void setParent(Menu Parent){
		this.Parent=Parent;
	}
	
	public List<Menu> getChildren(){
		return Children;
	}
	public void setChildren(List<Menu> Children){
		this.Children=Children;
	}
}