

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
@Table(name = "UserMenu", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserMenu extends HibernateEntity {
	private static final long serialVersionUID = 1461832991336L;
	/**
	 * 
	 */
	@Column(name = "UserId")
	@JsonProperty(value = "UserId")
	private String UserId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = User.class)
	@JoinColumn(name = "UserId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "User")
	private User User;
	/**
	 * 
	 */
	@Column(name = "ResourceId")
	@JsonProperty(value = "MenuId")
	private String MenuId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinColumn(name = "ResourceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Menu")
	private Menu Menu;
	
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
	
	public String getMenuId(){
		return MenuId;
	}
	public void setMenuId(String MenuId){
		this.MenuId=MenuId;
	}
	
	public Menu getMenu(){
		return Menu;
	}
	public void setMenu(Menu Menu){
		this.Menu=Menu;
	}

}