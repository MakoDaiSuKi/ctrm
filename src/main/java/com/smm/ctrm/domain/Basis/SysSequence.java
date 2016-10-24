

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class SysSequence extends HibernateEntity {
	private static final long serialVersionUID = 1461832991335L;
	/**
	 *
	 */
	@JsonProperty(value = "IdentityId")
	private BigDecimal IdentityId;
	/**
	 *
	 */
	@JsonProperty(value = "Code")
	private String Code;
	
	public BigDecimal getIdentityId(){
		return IdentityId;
	}
	public void setIdentityId(BigDecimal IdentityId){
		this.IdentityId=IdentityId;
	}
	
	public String getCode(){
		return Code;
	}
	public void setCode(String Code){
		this.Code=Code;
	}

}