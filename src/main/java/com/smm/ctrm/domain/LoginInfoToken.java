package com.smm.ctrm.domain;

public class LoginInfoToken {

	public String OrgId;
	public String OrgName; // 组织机构的名称
	public String UserId;
	public String Account;
	public String Name; // 登录用户的姓名
	public String AuthToken;
	public String LoginSeq;

	public String getOrgId() {
		return OrgId;
	}

	public void setOrgId(String orgId) {
		OrgId = orgId;
	}

	public String getOrgName() {
		return OrgName;
	}

	public void setOrgName(String orgName) {
		OrgName = orgName;
	}

	public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getAuthToken() {
		return AuthToken;
	}

	public void setAuthToken(String authToken) {
		AuthToken = authToken;
	}

	public String getLoginSeq() {
		return LoginSeq;
	}

	public void setLoginSeq(String loginSeq) {
		LoginSeq = loginSeq;
	}

	public String toString() {

		String accessToken = String.format("Account:{0},AuthToken:{1}", Account, AuthToken);
		return accessToken;
	}

}
