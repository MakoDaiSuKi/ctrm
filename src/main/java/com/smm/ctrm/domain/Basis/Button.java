

package com.smm.ctrm.domain.Basis;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author yutao.zhao
 * 按钮
 */
@Entity
@DiscriminatorValue(value="Button")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Button extends Resource{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7745508331150681999L;
	@Column(name = "ButtonNo", length = 30)
	@JsonProperty(value = "ButtonNo")
	private String ButtonNo;
	/**
	 *
	 */
	@Column(name = "FormNo", length = 30)
	@JsonProperty(value = "FormNo")
	private String FormNo;
	
	
	public String getButtonNo(){
		return ButtonNo;
	}
	public void setButtonNo(String ButtonNo){
		this.ButtonNo=ButtonNo;
	}
	
	public String getFormNo(){
		return FormNo;
	}
	public void setFormNo(String FormNo){
		this.FormNo=FormNo;
	}
}