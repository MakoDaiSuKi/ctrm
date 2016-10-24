

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
@Table(name = "UserRole", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserRole extends HibernateEntity {
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
	@Column(name = "RoleId")
	@JsonProperty(value = "RoleId")
	private String RoleId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Role.class)
	@JoinColumn(name = "RoleId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Role")
	private Role Role;
	
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
	
	public String getRoleId(){
		return RoleId;
	}
	public void setRoleId(String RoleId){
		this.RoleId=RoleId;
	}
	
	public Role getRole(){
		return Role;
	}
	public void setRole(Role Role){
		this.Role=Role;
	}

}