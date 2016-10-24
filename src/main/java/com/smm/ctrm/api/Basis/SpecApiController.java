
package com.smm.ctrm.api.Basis;

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

import com.smm.ctrm.bo.Basis.SpecService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Spec/")
public class SpecApiController {

	@Resource
	private SpecService specService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 不含分页，返回全部的规格的列表
	 * 
	 * @return
	 */
	@RequestMapping("Specs")
	@ResponseBody
	public ActionResult<List<Spec>> Specs(HttpServletRequest request) {
		return specService.Specs();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackSpecs")
	@ResponseBody
	public ActionResult<List<Spec>> BackSpecs(HttpServletRequest request) {
		return specService.BackSpecs();
	}

	/**
	 * 不含分页，返回指定品种的规格的列表
	 * 
	 * @return
	 */
	@RequestMapping("SpecsByCommodityId")
	@ResponseBody
	public ActionResult<List<Spec>> SpecsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return specService.SpecsByCommodityId(id);
		} catch (RuntimeException ex) {
			ActionResult<List<Spec>> tempVar = new ActionResult<List<Spec>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Spec> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return specService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Spec> tempVar = new ActionResult<Spec>();
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

			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Lot);
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult = checkService.deletable(id, "SpecId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			return specService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param spec
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Spec> Save(HttpServletRequest request, @RequestBody Spec spec) {
		if (spec.getId() != null) {
			spec.setUpdatedAt(new Date());
			spec.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			spec.setCreatedAt(new Date());
			spec.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return specService.Save(spec);
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
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			specService.MoveUp(id);
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
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			specService.MoveUp(id);
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