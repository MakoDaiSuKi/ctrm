



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.FeeSetupService;
import com.smm.ctrm.domain.Basis.FeeSetup;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/FeeSetup/")
public class FeeSetupApiController {
	
	@Autowired
	private FeeSetupService feeSetupService;

	
	/**
	 * 保存
	 * 
	 * @param feeSetup
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<FeeSetup> Save(HttpServletRequest request, @RequestBody FeeSetup feeSetup) {
		if (feeSetup.getId()!=null) {
			feeSetup.setUpdatedAt(new Date());
			feeSetup.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			feeSetup.setCreatedAt(new Date());
			feeSetup.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return feeSetupService.Save(feeSetup);
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String Id) {
		try {
			
			return feeSetupService.Delete(Id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<FeeSetup> GetById(HttpServletRequest request, @RequestBody String Id) {
		try {
			
			return feeSetupService.GetById(Id);
		} catch (RuntimeException ex) {
			ActionResult<FeeSetup> tempVar = new ActionResult<FeeSetup>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("FeeSetups")
	@ResponseBody
	public ActionResult<List<FeeSetup>> FeeSetups(HttpServletRequest request) {
		return feeSetupService.FeeSetups();
	}

	@RequestMapping("FeeSetupsByFeeType")
	@ResponseBody
	public ActionResult<List<FeeSetup>> FeeSetupsByFeeType(HttpServletRequest request, @RequestBody String feeType) {
		return feeSetupService.FeeSeupsByFeeType(feeType);
	}
}