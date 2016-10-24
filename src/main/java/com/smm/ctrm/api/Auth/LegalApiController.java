



package com.smm.ctrm.api.Auth;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.LegalService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Auth/Legal/")
public class LegalApiController {

	@Resource
	private LegalService legalService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Legal> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return legalService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Legal> tempVar = new ActionResult<Legal>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 不带分页查询
	 * 
	 * @return
	 */
	@RequestMapping("Legals")
	@ResponseBody
	public ActionResult<List<Legal>> Legals(HttpServletRequest request) {
		String orgId = LoginHelper.GetLoginInfo(request).OrgId;
		return legalService.Legals(orgId);
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
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Contract);
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult = checkService.deletable(id, "LegalId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			return legalService.Delete(id);
		} catch (Exception ex) {
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
	 * @param legal
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Legal> Save(HttpServletRequest request, @RequestBody Legal legal) {
		try {
			if (legal.getId()!=null) {
				
				legal.setUpdatedAt(new Date());
				legal.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				legal.setUpdatedId(LoginHelper.GetLoginInfo(request).UserId);
				if (legal.getCreatedId() == null) {
					legal.setCreatedId(legal.getUpdatedId());
				}
			} else {
				legal.setCreatedAt(new Date());
				legal.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				legal.setCreatedId(LoginHelper.GetLoginInfo(request).UserId);
			}

			return legalService.Save(legal);
		} catch (Exception e) {
			ActionResult<Legal> tempVar = new ActionResult<Legal>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("save fail");
			return tempVar;
		}
	}

	/**
	 * 指定控件向上移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Id can not be empty");
			return tempVar;
		}

		try {
			legalService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 指定控件向下移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Id can not be empty");
			return tempVar;
		}

		try {
			legalService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 指定控件向下移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("ModifyDefaultLegal")
	@ResponseBody
	public ActionResult<String> ModifyDefaultLegal(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Id can not be empty");
			return tempVar;
		}

		try {
			legalService.ModifyDefaultLegal(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
	public static void main(String[] args) {
		BigDecimal bg = new BigDecimal("5.215");
		BigDecimal bg2 = new BigDecimal("5.225");
		System.out.println(bg.setScale(2, RoundingMode.HALF_EVEN));
		System.out.println(bg2.setScale(2, RoundingMode.HALF_EVEN));
	}
}