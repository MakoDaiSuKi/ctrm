



package com.smm.ctrm.api.Basis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.GlobalSetService;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/GlobalSet/")
public class GlobalSetApiController {
	
	@Resource
	private GlobalSetService globalSetService;


	/**
	 * 取得唯一的一条记录
	 * 
	 * @return
	 */
	@RequestMapping("MyGlobalSet")
	@ResponseBody
	public ActionResult<GlobalSet> MyGlobalSet(HttpServletRequest request) {
		
		String orgId = LoginHelper.GetLoginInfo(request).OrgId;
		return globalSetService.MyGlobalSet(orgId);
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<GlobalSet> Save(HttpServletRequest request, @RequestBody GlobalSet globalSet) {
		try {
			return globalSetService.SaveGlobalSet(globalSet);
		} catch (RuntimeException ex) {
			ActionResult<GlobalSet> tempVar = new ActionResult<GlobalSet>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("InitDatabase")
	@ResponseBody
	public ActionResult<String> InitDatabase(HttpServletRequest request) {
		return globalSetService.InitDatabase();
	}
}