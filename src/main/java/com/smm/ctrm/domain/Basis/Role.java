

package com.smm.ctrm.domain.Basis;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Role", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Role extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 * 1/3 按钮
	 */
	@Transient
	@JsonProperty(value = "Users")
	private List<User> Users;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Resources")
	private List<Resource> Resources;
	/**
	 * 角色名称
	 */
	@Column(name = "Name", length = 30, nullable = false)
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * 备注
	 */
	@Column(name = "Comments", length = 100)
	@JsonProperty(value = "Comments")
	private String Comments;
	
	public List<User> getUsers(){
		return Users;
	}
	public void setUsers(List<User> Users){
		this.Users=Users;
	}
	
	public List<Resource> getResources(){
		return Resources;
	}
	public void setResources(List<Resource> Resources){
		this.Resources=Resources;
	}
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}