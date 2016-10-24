
package com.smm.ctrm.api.Finance;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.BankBalanceService;
import com.smm.ctrm.domain.Physical.BankBalance;
import com.smm.ctrm.domain.apiClient.BankBalanceParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Finance/BankBalance/")
public class BankBalanceApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private BankBalanceService bankBalanceService;

	@Resource
	private CommonService commonService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<BankBalance> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return bankBalanceService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */

	@RequestMapping("GetBankBalancesByTradeDate")
	@ResponseBody
	public ActionResult<List<BankBalance>> GetBankBalancesByTradeDate(HttpServletRequest request,
			@RequestBody String id) {
		try {
			Date today = DateUtil.doSFormatDate(id, "yyyyMMdd");
			return bankBalanceService.GetBankBalancesByTradeDate(today);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("BankBalances")
	@ResponseBody
	public ActionResult<List<BankBalance>> BankBalances(HttpServletRequest request) {
		try {
			List<BankBalance> bankbalances = bankBalanceService.BankBalances();
			return new ActionResult<>(Boolean.TRUE, "", bankbalances);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return bankBalanceService.Delete(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.DeleteFaile);
		}
	}

	/**
	 * 保存
	 * 
	 * @param bankBalances
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<BankBalance> Save(HttpServletRequest request, @RequestBody List<BankBalance> bankBalances) {
		if (bankBalances == null || bankBalances.isEmpty()) {
			ActionResult<BankBalance> tempVar = new ActionResult<BankBalance>();
			tempVar.setSuccess(false);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}
		try {
			for (BankBalance balance : bankBalances) {
				if (balance.getId() != null) {
					balance.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
					balance.setUpdatedAt(new Date());
					balance.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
				} else {
					balance.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
					balance.setCreatedAt(new Date());
					balance.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
				}
			}
			return bankBalanceService.Save(bankBalances);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.SaveFaile);
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
	public ActionResult<List<BankBalance>> Pager(HttpServletRequest request, @RequestBody BankBalanceParams param) {
		try {
			if (param == null) {
				param = new BankBalanceParams();
			}

			Criteria criteria = bankBalanceService.GetCriteria();

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();

			List<BankBalance> bankBalances = bankBalanceService.BankBalances(criteria, param.getPageSize(),
					param.getPageIndex(), total, param.getSortBy(), param.getOrderBy());

			ActionResult<List<BankBalance>> tempVar = new ActionResult<List<BankBalance>>();
			tempVar.setData(bankBalances);
			tempVar.setTotal(total.getTotal());
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
}