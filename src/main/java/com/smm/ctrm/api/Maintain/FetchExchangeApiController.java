



package com.smm.ctrm.api.Maintain;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.FetchExchangeService;
import com.smm.ctrm.domain.Maintain.FetchExchange;
import com.smm.ctrm.domain.apiClient.ExchangeParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Maintain/FetchExchange/")
public class FetchExchangeApiController {

	@Resource
	private FetchExchangeService fetchExchangeService;

	@Resource
	private CommonService commonService;


	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<FetchExchange> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return fetchExchangeService.GetById(id);
		} catch (Exception ex) {
			ActionResult<FetchExchange> tempVar = new ActionResult<FetchExchange>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param lme
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody FetchExchange lme) {
		try {
			if (lme.getId()!=null) {
				lme.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				lme.setUpdatedAt(new Date());
			} else {
				lme.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				lme.setCreatedAt(new Date());
			}
			return fetchExchangeService.Save(lme);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
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
			return fetchExchangeService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	// .NET attributes:
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult Pager(HttpServletRequest request, @RequestBody ExchangeParams param) {
		/*
		 * int total = 0; if (param == null) { param = new ExchangeParams(); }
		 * // typing in Java: var criteria = fetchExchangeService.GetCriteria();
		 * 
		 * param.SortBy = getCommonService().FormatSortBy(param.SortBy);
		 * 
		 * RefObject<Integer> tempRef_total = new RefObject<Integer>(total); //
		 * typing in Java: var lmes =
		 * fetchExchangeService.FetchExchanges(criteria, param.PageSize,
		 * param.PageIndex, tempRef_total, param.SortBy, param.OrderBy); total =
		 * tempRef_total.argvalue;
		 * 
		 * ActionResult<java.util.List<FetchExchange>> tempVar = new
		 * ActionResult<java.util.List<FetchExchange>>(); tempVar.Data = lmes;
		 * tempVar.Total = total; tempVar.Success = true; return tempVar;
		 */
		return null;
	}

}