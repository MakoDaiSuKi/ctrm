package com.smm.ctrm.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Menu;


public class LoginInfo {

	public String OrgId;
	public String OrgName; // 组织机构的名称
	public String UserId;
	public String Account;
	public String Name; // 登录用户的姓名
	public String AuthToken;
	//public String LoginSeq;
	public List<Menu> Menus;
	/// <summary>
	/// 用户按钮权限
	/// </summary>
	public List<Button> Buttons;
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
	/*public String getLoginSeq() {
		return LoginSeq;
	}

	public void setLoginSeq(String loginSeq) {
		LoginSeq = loginSeq;
	}*/
	
	public List<Menu> getMenus() {
		return Menus;
	}

	public void setMenus(List<Menu> menus) {
		Menus = menus;
	}
	
	public List<Button> getButtons() {
		return Buttons;
	}

	public void setButtons(List<Button> buttons) {
		Buttons = buttons;
	}

	public String toString() {

		String accessToken = String.format("OrgName:%s, Account:%s, AuthToken:%s", OrgName, Account, AuthToken);
		return accessToken;
	}
}
