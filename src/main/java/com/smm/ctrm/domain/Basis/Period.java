

package com.smm.ctrm.domain.Basis;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "Period", schema="Basis")

public class Period extends HibernateEntity {
	private static final long serialVersionUID = 1461832991334L;
	/**
	 * 账期名称
	 */
	@Column(name = "Name")
	@JsonProperty(value = "Name")
	private String Name;
	/**
	 * /开始日期
	 */
	@Column(name = "StartDate")
	@JsonProperty(value = "StartDate")
	private Date StartDate;
	/**
	 * 结束日期
	 */
	@Column(name = "EndDate")
	@JsonProperty(value = "EndDate")
	private Date EndDate;
	/**
	 * 是否是当前时刻（概念模糊）
	 */
	@Column(name = "IsCurrent")
	@JsonProperty(value = "IsCurrent",defaultValue="false")
	private Boolean IsCurrent;
	/**
	 *
	 */
	@Column(name = "Comments")
	@JsonProperty(value = "Comments")
	private String Comments;
	
	public String getName(){
		return Name;
	}
	public void setName(String Name){
		this.Name=Name;
	}
	
	public Date getStartDate(){
		return StartDate;
	}
	public void setStartDate(Date StartDate){
		this.StartDate=StartDate;
	}
	
	public Date getEndDate(){
		return EndDate;
	}
	public void setEndDate(Date EndDate){
		this.EndDate=EndDate;
	}
	
	public Boolean getIsCurrent(){
		return IsCurrent;
	}
	public void setIsCurrent(Boolean IsCurrent){
		this.IsCurrent=IsCurrent;
	}
	
	public String getComments(){
		return Comments;
	}
	public void setComments(String Comments){
		this.Comments=Comments;
	}

}