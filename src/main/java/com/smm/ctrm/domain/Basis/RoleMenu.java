

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
@Table(name = "RoleMenu", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RoleMenu extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 * 
	 */
	@Column(name = "MenuId")
	@JsonProperty(value = "MenuId")
	private String MenuId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Menu.class)
	@JoinColumn(name = "MenuId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Menu")
	private Menu Menu;
	/**
	 *
	 */
	@Column(name = "ButtonId")
	@JsonProperty(value = "ButtonId")
	private String ButtonId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Button.class)
	@JoinColumn(name = "ButtonId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Button")
	private Button Button;
	/**
	 * 
	 */
	@Column(name = "ResourceId")
	@JsonProperty(value = "ResourceId")
	private String ResourceId;
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
	
	public String getButtonId(){
		return ButtonId;
	}
	public void setButtonId(String ButtonId){
		this.ButtonId=ButtonId;
	}
	
	public Button getButton(){
		return Button;
	}
	public void setButton(Button Button){
		this.Button=Button;
	}
	
	public String getResourceId(){
		return ResourceId;
	}
	public void setResourceId(String ResourceId){
		this.ResourceId=ResourceId;
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