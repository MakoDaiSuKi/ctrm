



package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.BankService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Bank;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Basis/Bank/")
public class BankApiController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private BankService bankService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Banks")
	@ResponseBody
	public ActionResult<List<Bank>> Banks(HttpServletRequest request) {
		return bankService.Banks();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackBanks")
	@ResponseBody
	public ActionResult<List<Bank>> BackBanks(HttpServletRequest request) {
		return bankService.BackBanks();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Bank> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return bankService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Bank> tempVar = new ActionResult<Bank>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param bank
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Bank> Save(HttpServletRequest request, @RequestBody Bank bank) {
		if (bank.getId()!=null) {
			bank.setUpdatedAt(new Date());
			bank.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			bank.setCreatedAt(new Date());
			bank.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return bankService.Save(bank);
	}

	/**
	 * 根据Id删除单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.LegalBank);
			tableList.add(TableNameConst.CustomerBank);
			ActionResult<String> checkResult = checkService.deletable(id, "BankId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			
			tableList = new ArrayList<>();
			tableList.add(TableNameConst.BankReceipt);
			checkResult = checkService.deletable(id, "PayBank", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			
			checkResult = checkService.deletable(id, "DebitBank", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			
			bankService.Delete(id);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setStatus(ActionStatus.SUCCESS);
			tempVar.setMessage(MessageCtrm.DeleteSuccess);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}
}