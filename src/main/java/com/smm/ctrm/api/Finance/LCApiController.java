
package com.smm.ctrm.api.Finance;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.LCService;
import com.smm.ctrm.domain.Physical.LC;
import com.smm.ctrm.domain.apiClient.LCParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Finance/LC/")
public class LCApiController {

	@Resource
	private LCService lCService;

	@Resource
	private CommonService commonService;

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<LC>> Pager(HttpServletRequest request, @RequestBody(required = false) LCParams param) {

		if (param == null) {
			param = new LCParams();
		}
		Criteria criteria = lCService.GetCriteria();
		if (!StringUtils.isBlank(param.getKeyword())) {
			Criterion a = Restrictions.like("LcNo", "%" + param.getKeyword() + "%");
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(a);
			criteria.add(disjunction);
		}
		// 开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("PromptDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("PromptDate", param.getEndDate()));
		}
		// 开始日期
		if (param.getIssueStartDate() != null) {
			criteria.add(Restrictions.ge("IssueDate", param.getIssueStartDate()));
		}
		// 结束日期
		if (param.getIssueEndDate() != null) {
			criteria.add(Restrictions.le("IssueDate", param.getIssueEndDate()));
		}
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		List<LC> lcs = lCService.LCs(criteria, param.getPageSize(), param.getPageIndex(), total, param.getSortBy(),
				param.getOrderBy());

		lcs = commonService.SimplifyDataLCList(lcs);

		ActionResult<List<LC>> tempVar = new ActionResult<List<LC>>();
		tempVar.setData(lcs);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<LC> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return lCService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<LC> tempVar = new ActionResult<LC>();
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
	@RequestMapping("LCs")
	@ResponseBody
	public ActionResult<List<LC>> LCs(HttpServletRequest request) {
		List<LC> lcs = lCService.LCs();
		ActionResult<List<LC>> tempVar = new ActionResult<List<LC>>();
		tempVar.setData(lcs);
		tempVar.setSuccess(true);
		return tempVar;
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
			return lCService.Delete(id);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("delte fail");
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param lc
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<LC> Save(HttpServletRequest request, @RequestBody LC lc) {
		if (lc.getId() != null) {
			lc.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			lc.setCreatedAt(new Date());
		} else {
			lc.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			lc.setCreatedAt(new Date());
		}
		return lCService.Save(lc);
	}

	/**
	 * 导入
	 * 
	 * @param lc
	 * @return
	 */
	@RequestMapping("Import1By1")
	@ResponseBody
	public ActionResult<String> Import1By1(HttpServletRequest request, @RequestBody LC lc) {
		lc.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		lc.setCreatedAt(new Date());
		return lCService.Import1By1(lc);
	}
}