
package com.smm.ctrm.api.Auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Basis.UserService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.domain.AccountToBeAudit;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.apiClient.CustomerParams;
import com.smm.ctrm.domain.apiClient.UserParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Auth/User/")
public class UserApiController {

	@Autowired
	private UserService userService;

	@Autowired
	private CommonService commonService;

	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 根据角色Id返回其下属的用户列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetUsersByRoleId")
	@ResponseBody
	public ActionResult<List<User>> GetUsersByRoleId(HttpServletRequest request, @RequestBody String id) {
		try {

			return userService.GetUsersByRoleId(id);
		} catch (Exception ex) {
			ActionResult<List<User>> tempVar = new ActionResult<List<User>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetMenusByUserId")
	@ResponseBody
	public ActionResult<List<Resource>> GetMenusByUserId(HttpServletRequest request, @RequestBody String id) {
		try {

			return userService.GetMenusByUserId(id);
		} catch (Exception ex) {
			ActionResult<List<Resource>> tempVar = new ActionResult<List<Resource>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<User>> Pager(HttpServletRequest request, @RequestBody UserParams param) {
		try {
			if (param == null) {
				param = new UserParams();
			}
			Criteria criteria = userService.GetCriteria();
			RefUtil total = new RefUtil();
			List<User> users = userService.Pager(criteria, param.getPageSize(), param.getPageIndex(),
					total, param.getSortBy(), param.getOrderBy());
			return new ActionResult<>(true, "", users, total);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false,e.getMessage());
		}
	}
	
	/**
	 * 根据用户Id返回单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<User> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return userService.GetById(id);
		} catch (Exception ex) {
			ActionResult<User> tempVar = new ActionResult<User>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 返回用户列表
	 * 
	 * @return
	 */
	@RequestMapping("Users")
	@ResponseBody
	public ActionResult<List<User>> Users(HttpServletRequest request) {
		return userService.Users();
	}

	/**
	 * 只返回拥有交易权限的账号列表
	 * 
	 * @return
	 */
	@RequestMapping("Traders")
	@ResponseBody
	public ActionResult<List<User>> Traders(HttpServletRequest request) {
		return userService.Traders();
	}

	/**
	 * 获取"订单审核组"的账户的列表
	 * 
	 * @return
	 */
	@RequestMapping("ApproversOfContract")
	@ResponseBody
	public ActionResult<List<User>> ApproversOfContract(HttpServletRequest request) {
		return userService.ApproversOfContract();
	}

	/**
	 * 获取"付款审核组"的账户的列表
	 * 
	 * @return
	 */
	@RequestMapping("ApproversOfPayment")
	@ResponseBody
	public ActionResult<List<User>> ApproversOfPayment(HttpServletRequest request) {
		return userService.ApproversOfPayment();
	}

	/**
	 * 获取"客户审核组"的账户的列表
	 * 
	 * @return
	 */
	@RequestMapping("ApproversOfCustomer")
	@ResponseBody
	public ActionResult<List<User>> ApproversOfCustomer(HttpServletRequest request) {
		return userService.ApproversOfCustomer();
	}

	/**
	 * 保存
	 * 
	 * @param user
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<User> Save(HttpServletRequest request, @RequestBody User user) {
		try {
			return userService.Save(user);
		} catch (Exception e) {
			ActionResult<User> tempVar = new ActionResult<User>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(MessageCtrm.SaveFaile);
			return tempVar;
		}
	}

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return userService.Delete(id);
		} catch (Exception e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("delete fail");
			return tempVar;
		}
	}

	@RequestMapping("ChangePassword")
	@ResponseBody
	public ActionResult<String> ChangePassword(HttpServletRequest request, @RequestBody Map<String, String> param) {
		String oldpwd = param.get("Oldpwd");
		String newpwd = param.get("Newpwd");
		if (StringUtils.isBlank(oldpwd) || StringUtils.isBlank(newpwd)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			return tempVar;
		}
		String userId = LoginHelper.GetLoginInfo(request).UserId;
		return userService.ChangePassword(userId, oldpwd, newpwd);
	}

	@RequestMapping("ResetPassword")
	@ResponseBody
	public ActionResult<String> ResetPassword(HttpServletRequest request, @RequestBody Map<String, String> param) {
		// 取操作人员的账号给BL，判断是否有权限重置密码。
		String account = LoginHelper.GetLoginInfo(request).Account;

		String userId = param.get("UserId");
		String newpwd = param.get("Newpwd");
		return userService.ResetPassword(account, userId, newpwd);
	}

	@RequestMapping("AccountsToBeAudit4App")
	@ResponseBody
	public ActionResult<List<User>> AccountsToBeAudit4App(HttpServletRequest request) {
		return userService.AccountsToBeAudit4App();
	}

	@SuppressWarnings("unchecked")
	@RequestMapping("AccountsToBeAudit4App_Manual")
	@ResponseBody
	public ActionResult<List<AccountToBeAudit>> AccountsToBeAudit4App_Manual(HttpServletRequest request) {

		ActionResult accounts = userService.AccountsToBeAudit4App();
		if (accounts == null || accounts.getData() == null || ((List<Resource>) accounts.getData()).size() == 0) {
			ActionResult<List<AccountToBeAudit>> tempVar = new ActionResult<List<AccountToBeAudit>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(0);
			tempVar.setMessage("当前没有待审的账号");
			tempVar.setData(null);
			return tempVar;
		}
		List<AccountToBeAudit> datas = new ArrayList<AccountToBeAudit>();
		List<User> listUser = (List<User>) accounts.getData();
		for (User v : listUser) {

			AccountToBeAudit u = new AccountToBeAudit();
			u.setUserName(v.getName());
			u.setAccountName(v.getAccount());
			u.setCreateTime(DateUtil.doFormatDate(v.getCreatedAt(), "yyyy-MM-dd HH:mm:ss"));
			u.setIsAudited(v.getIsAudited());
			datas.add(u);
		}
		ActionResult<List<AccountToBeAudit>> tempVar2 = new ActionResult<List<AccountToBeAudit>>();
		tempVar2.setSuccess(true);
		tempVar2.setTotal(datas.size());
		tempVar2.setData(datas);
		return tempVar2;
	}

	@RequestMapping("AuditAccount4App")
	@ResponseBody
	public ActionResult<String> AuditAccount4App(HttpServletRequest request, @RequestBody Map<String, String> param) {
		String userId = param.get("AccountId");
		String isAudited = param.get("IsAudited");
		return userService.AuditAccount4App(userId, isAudited);
	}

	/**
	 * 获取“发货单审核组”的账户的列表
	 * 
	 * @return
	 */
	@RequestMapping("ApproversOfShip")
	@ResponseBody
	public ActionResult<List<User>> ApproversOfShip() {
		return userService.ApproversOfShip();
	}

	/**
	 * 获取“发票审核组”的账户的列表
	 * 
	 * @return
	 */
	@RequestMapping("ApproversOfInvoice")
	@ResponseBody
	public ActionResult<List<User>> ApproversOfInvoice() {
		return userService.ApproversOfInvoice();
	}
}