package com.smm.ctrm.domain;

public class AccountToBeAudit {

	private String UserName;

	private String AccountName;

	private String CreateTime;

	private boolean IsAudited;

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getAccountName() {
		return AccountName;
	}

	public void setAccountName(String accountName) {
		AccountName = accountName;
	}

	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public boolean isIsAudited() {
		return IsAudited;
	}

	public void setIsAudited(boolean isAudited) {
		IsAudited = isAudited;
	}

}
