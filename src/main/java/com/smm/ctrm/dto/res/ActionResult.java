package com.smm.ctrm.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.smm.ctrm.util.RefUtil;

import java.io.Serializable;

/**
 * Created by zhenghao on 2016/4/21.
 * api调用返回对象
 */
@JsonPropertyOrder(value={"Success", "Total", "Status", "Message", "Data"})
public class ActionResult<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5120498599753158517L;

	//接口处理状态  成功返回 true ， 否则返回 false
    @JsonProperty(value = "Success")
    private boolean success=true;

    //返回信息，一般为异常信息
    @JsonProperty(value = "Message")
    private String message;

    //返回数据
    @JsonProperty(value = "Data")
    private T data;

    //返回数量
    @JsonProperty(value = "Total")
    private int total=0;
    
    @JsonProperty(value = "Status")
    private String Status;


    public ActionResult() {
    }

    public ActionResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public ActionResult(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
    public ActionResult(boolean success, String message, String Status) {
        this.success = success;
        this.message = message;
        this.Status = Status;
    }
    public ActionResult(boolean success, String message,T data, String Status) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.Status = Status;
    }

    public ActionResult(boolean success, String message, T data, RefUtil total) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
		this.total = total.getTotal();
	}

	public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    public void setTotal(RefUtil refUtil) {
        this.total = refUtil.getTotal();
    }

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}
    
}
