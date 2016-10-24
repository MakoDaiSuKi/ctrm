
package com.smm.ctrm.api.Auth;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.UserSetService;
import com.smm.ctrm.domain.Basis.UserSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Auth/UserSet/")
public class UserSetApiController {

	@Resource
	private UserSetService userSetService;

	/**
	 * 取得唯一的一条记录
	 * 
	 * @return
	 */
	@RequestMapping("MyUserSet")
	@ResponseBody
	public ActionResult<UserSet> MyUserSet(HttpServletRequest request) {
		String userId = LoginHelper.GetLoginInfo(request).UserId;
		return userSetService.MyUserSet(userId);
	}

	/**
	 * 保存
	 * 
	 * @param userSettings
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<UserSet> Save(HttpServletRequest request, @RequestBody UserSet userSettings) {
		try {
			return userSetService.Save(userSettings);
		} catch (RuntimeException e) {
			return new ActionResult<>(false, MessageCtrm.SaveFaile);
		}
	}
}