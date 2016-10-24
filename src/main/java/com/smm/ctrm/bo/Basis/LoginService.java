package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserSet;
import com.smm.ctrm.dto.res.ActionResult;

public interface LoginService {
	ActionResult<User> Login(String act, String pwd);

	void RemoveSubMenus(List<Menu> baseMenus, List<Resource> resources, Menu menu);

	ActionResult<UserSet> GetUserSettings(String userId);

	Menu SimplifyData(Menu menu);

	Button SimplifyData(Button button);

	List<Menu> GetMenusByUserId(String userId, String account);

	List<Button> GetButtonsByUserId(String userId, String account);
}