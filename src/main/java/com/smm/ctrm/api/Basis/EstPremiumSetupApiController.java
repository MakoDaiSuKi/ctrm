



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.EstPremiumSetupService;
import com.smm.ctrm.domain.Basis.EstPremiumSetup;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/EstPremiumSetup/")
public class EstPremiumSetupApiController {

	@Resource
	private EstPremiumSetupService estPremiumSetupService;


	/**
	 * 保存
	 * 
	 * @param estPremiumSetup
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<EstPremiumSetup> Save(HttpServletRequest request, @RequestBody EstPremiumSetup estPremiumSetup) {
		if (estPremiumSetup.getId()!=null) {
			estPremiumSetup.setUpdatedAt(new Date());
			estPremiumSetup.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			estPremiumSetup.setCreatedAt(new Date());
			estPremiumSetup.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return estPremiumSetupService.Save(estPremiumSetup);
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String Id) {
		try {
			return estPremiumSetupService.Delete(Id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<EstPremiumSetup> GetById(HttpServletRequest request, @RequestBody String Id) {
		try {
			
			return estPremiumSetupService.GetById(Id);
		} catch (RuntimeException ex) {
			ActionResult<EstPremiumSetup> tempVar = new ActionResult<EstPremiumSetup>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("EstPremiumSetups")
	@ResponseBody
	public ActionResult<List<EstPremiumSetup>> EstPremiumSetups(HttpServletRequest request) {
		return estPremiumSetupService.EstPremiumSetups();
	}

}