package com.smm.ctrm.domain;

/**
 * Created by hao.zheng on 2016/5/5.
 *
 */
public class LoginModel {

	private String OrgCode;
	private String Account;
	private String Password;

	public String getOrgCode() {
		return OrgCode;
	}

	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}

	public String getAccount() {
		return Account;
	}

	public void setAccount(String account) {
		Account = account;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}
}
