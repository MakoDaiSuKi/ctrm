package com.smm.ctrm.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 期货行情返回参数
 * 
 * @author zengshihua
 *
 */
public class MainmetaResult {

	private Integer code;
	private String msg;
	@JsonProperty(value="data")
	private List<Mainmeta> mainmeta;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<Mainmeta> getMainmeta() {
		return mainmeta;
	}

	public void setMainmeta(List<Mainmeta> mainmeta) {
		this.mainmeta = mainmeta;
	}

}
