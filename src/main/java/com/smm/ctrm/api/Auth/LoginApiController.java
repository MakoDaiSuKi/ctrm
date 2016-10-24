
package com.smm.ctrm.api.Auth;

import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.LoginService;
import com.smm.ctrm.bo.Logs.CommonLogService;
import com.smm.ctrm.bo.Logs.MessageLogService;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Log.ExceptionLog;
import com.smm.ctrm.domain.Log.MessageLog;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.FileUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Auth/Login/")
public class LoginApiController {

	@Resource
	private LoginService loginService;

	@Resource
	private CommonLogService commonLogService;

	@Resource
	private MessageLogService messageLogService;

	private final String FAILED_VALID_ORG = "登录失败，不存在此机构！";

	private static final Logger logger = Logger.getLogger(LoginApiController.class);

	@RequestMapping("Login4App")
	@ResponseBody
	public ActionResult<LoginInfo> Login4App(HttpServletRequest request, @RequestBody Map<String, String> param) {

		try {
			String account = param.get("Account");
			String password = param.get("Password");
			ActionResult<User> result = loginService.Login(account, password);

			LoginInfo loginInfo = new LoginInfo();
			if (result.isSuccess()) {
				User user = (User) result.getData();
				loginInfo.setOrgId(user.getOrgId());
				loginInfo.setOrgName(user.getOrg().getName());
				loginInfo.setUserId(user.getId());
				loginInfo.setAccount(user.getAccount());
				loginInfo.setName(user.getName());

				// 从系统参数获取登录时间
				final int effectiveHours = 60;// 一天

				LoginHelper.SignIn(loginInfo, effectiveHours);

				MessageLog messageLog = new MessageLog();
				messageLog.setCreatedAt(new Date());
				messageLog.setCreatedBy(loginInfo.getAccount());
				messageLog.setCreaterId(loginInfo.getUserId());
				messageLog.setCreaterName(loginInfo.getName());
				messageLog.setMessageInfo(result.getMessage());
				messageLog.setMethodName("Login4App");
				messageLogService.Save(messageLog);
			} else {
				MessageLog messageLog = new MessageLog();
				messageLog.setCreatedAt(new Date());
				messageLog.setCreatedBy(account);
				messageLog.setCreaterId(null);
				messageLog.setCreaterName("");
				messageLog.setMessageInfo(result.getMessage());
				messageLog.setMethodName("Login4App");
				messageLogService.Save(messageLog);
				return new ActionResult<>(false, result.getMessage());
			}
			ActionResult<LoginInfo> tempVar4 = new ActionResult<LoginInfo>();
			tempVar4.setSuccess(true);
			tempVar4.setStatus(result.getStatus());
			tempVar4.setMessage(result.getMessage());
			tempVar4.setData(loginInfo);
			return tempVar4;
		} catch (Exception e) {
			ActionResult<LoginInfo> tempVar5 = new ActionResult<LoginInfo>();
			tempVar5.setSuccess(false);
			tempVar5.setMessage(e.toString());
			logger.error(e.getMessage(), e);
			return tempVar5;
		}
	}

	@RequestMapping("Login4Winform")
	@ResponseBody
	public ActionResult<LoginInfo> Login4Winform(@RequestBody Map<String, String> params) {

		logger.info("-------------- in Login4Winform ");

		try {

			String account = params.get("Account");// request.getParameter("Account");
			String password = params.get("Password");// request.getParameter("Password");
			String orgName = params.get("OrgCode");

			if (StringUtils.isBlank(orgName))
				throw new Exception("orgName is null");

			if (!LoginHelper.setDataSource(orgName)) {
				return new ActionResult<>(Boolean.FALSE, FAILED_VALID_ORG);
			}
			ActionResult<User> result = loginService.Login(account, password);

			LoginInfo loginInfo = new LoginInfo();

			if (!result.isSuccess()) {
				MessageLog tempVar = new MessageLog();
				tempVar.setCreatedAt(new Date());
				tempVar.setCreatedBy(account);
				tempVar.setCreaterId(null);
				tempVar.setCreaterName(null);
				tempVar.setMessageInfo(result.getMessage());
				tempVar.setMethodName("Login4Winform");
				messageLogService.Save(tempVar);
				return new ActionResult<>(false, result.getMessage());
			}

			User user = result.getData();

			loginInfo.setOrgId(user.getOrgId());
			loginInfo.setOrgName(URLDecoder.decode(orgName, "utf-8"));
			loginInfo.setUserId(user.getId());
			loginInfo.setAccount(user.getAccount());
			loginInfo.setName(user.getName());

			loginInfo.setMenus(loginService.GetMenusByUserId(loginInfo.getUserId(), loginInfo.getAccount()));

			loginInfo.setButtons(loginService.GetButtonsByUserId(loginInfo.getUserId(), loginInfo.getAccount()));

			// 从系统参数获取登录时间
			final int effectiveHours = 60 * 60 * 24;

			LoginHelper.SignIn(loginInfo, effectiveHours);

			// 更新用户登陆次数及时间
			// 添加登陆日志
			MessageLog messageLog = new MessageLog();
			messageLog.setCreatedAt(new Date());
			messageLog.setCreatedBy(loginInfo.getAccount());
			messageLog.setCreaterId(loginInfo.getUserId());
			messageLog.setCreaterName(loginInfo.getName());
			messageLog.setMessageInfo(result.getMessage());
			messageLog.setMethodName("Login4Winform");

			messageLogService.Save(messageLog);

			ActionResult<LoginInfo> tempVar4 = new ActionResult<LoginInfo>();
			tempVar4.setSuccess(true);
			tempVar4.setStatus(ActionStatus.SUCCESS);
			tempVar4.setMessage(result.getMessage());
			tempVar4.setData(loginInfo);
			return tempVar4;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Logout4Winform")
	@ResponseBody
	public ActionResult<String> Logout4Winform() {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			MessageLog messageLog = new MessageLog();
			messageLog.setCreatedAt(new Date());
			messageLog.setCreatedBy(loginInfo.getAccount());
			messageLog.setCreaterId(loginInfo.getUserId());
			messageLog.setCreaterName(loginInfo.getName());
			messageLog.setMessageInfo("Logout successfully");
			messageLog.setMethodName("Logout");
			messageLogService.Save(messageLog);
			LoginHelper.SingOut(loginInfo);
			return new ActionResult<>(true, "成功注销");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("Logout4App")
	@ResponseBody
	public ActionResult<String> Logout4App() {
		try {
			LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
			MessageLog messageLog = new MessageLog();
			messageLog.setCreatedAt(new Date());
			messageLog.setCreatedBy(loginInfo.getAccount());
			messageLog.setCreaterId(loginInfo.getUserId());
			messageLog.setCreaterName(loginInfo.getName());
			messageLog.setMessageInfo("Logout successfully");
			messageLog.setMethodName("Logout");
			messageLogService.Save(messageLog);
			LoginHelper.SingOut(loginInfo);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage("成功注销");
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(@RequestBody ExceptionLog exceptionLog) {
		exceptionLog.setCreatedAt(new Date());
		exceptionLog.setLevelName("Exception");
		return commonLogService.SaveExceptionLog(exceptionLog);
	}

	@RequestMapping("Login5Winform")
	@ResponseBody
	public ActionResult<List<String>> Login5Winform(@RequestBody LoginInfo loginInfo) {
		logger.info("进入Login5Winform方法,参数:"+loginInfo.toString());
		try {
			if (!LoginHelper.setDataSource(loginInfo.getOrgName())) {
				logger.info("***登录部门名称有误***" + loginInfo.getOrgName());
				return new ActionResult<>(Boolean.FALSE, FAILED_VALID_ORG);
			}
			/**
			 * 从系统参数获取登录时间
			 */
			logger.info("开始将token存至redis");
			final int effectiveHours = 60 * 60 * 24;// 一天
			LoginHelper.SignIn2(loginInfo, effectiveHours);
			logger.info("结束redis操作");
			logger.info("开始读取API列表");
			/**
			 * 获取API列表
			 */
			String apiList = FileUtil.read();
			logger.info("结束读取API列表");
			return new ActionResult<>(true, "操作成功.", JSONUtil.doConvertStringToList(apiList));
		} catch (Exception ex) {
			logger.error("Login5Winform异常", ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}