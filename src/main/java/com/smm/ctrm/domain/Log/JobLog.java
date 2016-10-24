

package com.smm.ctrm.domain.Log;

import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
public class JobLog extends HibernateEntity {
	private static final long serialVersionUID = 1461723934977L;
	/**
	 * 执行的时间在什么时候
	 */
	@Column(name = "Offset")
	@JsonProperty(value = "Offset")
	private Date Offset;
	/**
	 * 执行了多长的时间
	 */
	@Column(name = "RunTime")
	@JsonProperty(value = "RunTime")
	private String RunTime;
	
	public Date getOffset(){
		return Offset;
	}
	public void setOffset(Date Offset){
		this.Offset=Offset;
	}
	
	public String getRunTime(){
		return RunTime;
	}
	public void setRunTime(String RunTime){
		this.RunTime=RunTime;
	}

}