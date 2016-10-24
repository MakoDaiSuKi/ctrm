

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "EstPremiumSetup", schema="Basis")

public class EstPremiumSetup extends HibernateEntity {
	private static final long serialVersionUID = 1461832991322L;
	/**
	 *  预计销售地点
	 */
	@Column(name = "EstDischarging", length = 64)
	@JsonProperty(value = "EstDischarging")
	private String EstDischarging;
	/**
	 *  预计销售升贴水
	 */
	@Column(name = "EstPremium")
	@JsonProperty(value = "EstPremium")
	private BigDecimal EstPremium;
	
	public String getEstDischarging(){
		return EstDischarging;
	}
	public void setEstDischarging(String EstDischarging){
		this.EstDischarging=EstDischarging;
	}
	
	public BigDecimal getEstPremium(){
		return EstPremium;
	}
	public void setEstPremium(BigDecimal EstPremium){
		this.EstPremium=EstPremium;
	}

}