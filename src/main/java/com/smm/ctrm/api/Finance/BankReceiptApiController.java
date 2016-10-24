package com.smm.ctrm.api.Finance;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.BankReceiptService;
import com.smm.ctrm.domain.Physical.BankReceipt;
import com.smm.ctrm.domain.apiClient.BankReceiptParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Finance/BankReceipt/")
public class BankReceiptApiController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private BankReceiptService bankReceiptService;
	@Resource
	private CommonService commonService;
	@Resource
	private CheckService checkService;
	// 根据id获取实体
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<BankReceipt> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return bankReceiptService.GetById(id);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/// 查客户名称下的所有水单列表
	@RequestMapping("GetReceiptListByCustomerId")
	@ResponseBody
	public ActionResult<List<BankReceipt>> GetReceiptListByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		try {

			return bankReceiptService.GetReceiptListByCustomerId(customerId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/// 查客户名称下的所有水单列表
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<BankReceipt>> Pager(HttpServletRequest request, @RequestBody BankReceiptParams param) {
		try {
			if (param == null)
				param = new BankReceiptParams();
			Criteria criteria = bankReceiptService.GetCriteria();
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
			RefUtil total = new RefUtil();
			if (StringUtils.isNotBlank(param.getBankReceiptNo())) {
				criteria.add(Restrictions.eq("BankReceiptNo", param.getBankReceiptNo()));
			}
			if (StringUtils.isNotBlank(param.getCustomerId())) {
				criteria.add(Restrictions.eq("CustomerId", param.getCustomerId()));
			}
			// 开始日期
			if (param.getStartDate() != null) {
				criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
			}
			// 结束日期
			if (param.getEndDate() != null) {
				criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
			}
			List<BankReceipt> bankBalances = bankReceiptService.BankReceipt(criteria, param.getPageSize(),
					param.getPageIndex(), total, param.getSortBy(), param.getOrderBy());
			return new ActionResult<>(true, "", bankBalances, total);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/// <summary>
	/// 保存
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<BankReceipt> Save(HttpServletRequest request, @RequestBody BankReceipt bankReceipt) {
		// #region 数据有效性的检查
		if (bankReceipt == null)
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.ParamError);

		try {
			if (StringUtils.isNotBlank(bankReceipt.getId())) {
				bankReceipt.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				bankReceipt.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());

			}

			return bankReceiptService.Save(bankReceipt);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}

	}

	/// 查客户名称下的所有水单列表 yunsq 2016年7月26日 14:44:102
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Fund);
			ActionResult<String> checkResult = checkService.deletable(id, "BankReceiptId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			
			
			return bankReceiptService.Delete(id);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

}
