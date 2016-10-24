



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.PricingShortcutService;
import com.smm.ctrm.domain.Basis.PricingShortcut;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/PricingShortcut/")
public class PricingShortcutApiController {
	
	@Resource
	private PricingShortcutService pricingShortcutService;


	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	
	@RequestMapping("PricingShortcuts")
	@ResponseBody
	public ActionResult<List<PricingShortcut>> PricingShortcuts(HttpServletRequest request) {
		return pricingShortcutService.PricingShortcuts();
	}

	@RequestMapping("BackPricingShortcuts")
	@ResponseBody
	public ActionResult<List<PricingShortcut>> BackPricingShortcuts(HttpServletRequest request) {
		return pricingShortcutService.BackPricingShortcuts();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return pricingShortcutService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
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
			return pricingShortcutService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param pricingShortcut
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<PricingShortcut> Save(HttpServletRequest request, @RequestBody PricingShortcut pricingShortcut) {
		try {
			if (pricingShortcut.getId()!=null) {
				pricingShortcut.setUpdatedAt(new Date());
				pricingShortcut.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				pricingShortcut.setCreatedAt(new Date());
				pricingShortcut.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return pricingShortcutService.Save(pricingShortcut);
		} catch (RuntimeException ex) {
			ActionResult<PricingShortcut> tempVar = new ActionResult<PricingShortcut>();
			tempVar.setSuccess(false);
			tempVar.setData(pricingShortcut);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			pricingShortcutService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			pricingShortcutService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
}