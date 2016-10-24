

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
@Table(name = "UserSet", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserSet extends HibernateEntity {
	private static final long serialVersionUID = 1461832991336L;
	/**
	 * 主题（用于web）
	 */
	@Column(name = "Theme", length = 30)
	@JsonProperty(value = "Theme")
	private String Theme;
	/**
	 * 导航方式（用于web）
	 */
	@Column(name = "Navigation", length = 30)
	@JsonProperty(value = "Navigation")
	private String Navigation;
	/**
	 * 每页显示多少条
	 */
	@Column(name = "GridRows", nullable = false)
	@JsonProperty(value = "GridRows",defaultValue="0")
	private Integer GridRows;
	/**
	 * 允许打开标签的最大数量
	 */
	@Column(name = "MaxTabCount")
	@JsonProperty(value = "MaxTabCount",defaultValue="0")
	private Integer MaxTabCount;
	/**
	 * 用户标识
	 */
	@Column(name = "UserId", nullable = false)
	@JsonProperty(value = "UserId")
	private String UserId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UserId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "User")
	private User User;
	
	public String getTheme(){
		return Theme;
	}
	public void setTheme(String Theme){
		this.Theme=Theme;
	}
	
	public String getNavigation(){
		return Navigation;
	}
	public void setNavigation(String Navigation){
		this.Navigation=Navigation;
	}
	
	public Integer getGridRows(){
		return GridRows;
	}
	public void setGridRows(Integer GridRows){
		this.GridRows=GridRows;
	}
	
	public Integer getMaxTabCount(){
		return MaxTabCount!=null?MaxTabCount:0;
	}
	public void setMaxTabCount(Integer MaxTabCount){
		this.MaxTabCount=MaxTabCount;
	}
	
	public String getUserId(){
		return UserId;
	}
	public void setUserId(String UserId){
		this.UserId=UserId;
	}
	
	public User getUser(){
		return User;
	}
	public void setUser(User User){
		this.User=User;
	}

}