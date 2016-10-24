



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.LegalBankService;
import com.smm.ctrm.domain.Basis.LegalBank;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Basis/LegalBank/")
public class LegalBankApiController {
	
	@Resource
	private LegalBankService legalBankService;


	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("LegalBanks")
	@ResponseBody
	public ActionResult<List<LegalBank>> LegalBanks(HttpServletRequest request) {
		return legalBankService.LegalBanks();
	}

	@RequestMapping("LegalBanksByLegalId")
	@ResponseBody
	public ActionResult<List<LegalBank>> LegalBanksByLegalId(HttpServletRequest request, @RequestBody String id) {
		return legalBankService.LegalBanksByLegalId(id);
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackLegalBanks")
	@ResponseBody
	public ActionResult<List<LegalBank>> BackLegalBanks(HttpServletRequest request) {
		return legalBankService.BackLegalBanks();
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
			legalBankService.Delete(id);
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

	/**
	 * 保存
	 * 
	 * @param legalBank
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<LegalBank> Save(HttpServletRequest request, @RequestBody LegalBank legalBank) {
		if (legalBank.getId()!=null) {
			legalBank.setUpdatedAt(new Date());
			legalBank.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			legalBank.setCreatedAt(new Date());
			legalBank.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}

		return legalBankService.Save(legalBank);
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<LegalBank> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			LegalBank bank = legalBankService.GetById(id);
			ActionResult<LegalBank> tempVar = new ActionResult<LegalBank>();
			tempVar.setSuccess(true);
			tempVar.setData(bank);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<LegalBank> tempVar2 = new ActionResult<LegalBank>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}
}