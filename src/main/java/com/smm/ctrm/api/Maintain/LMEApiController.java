



package com.smm.ctrm.api.Maintain;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.LMEService;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.apiClient.QuotationParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/LME/")
public class LMEApiController {
	
	@Resource
	private LMEService LMEService;

	@Resource
	private CommonService commonService;


	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("LMEs")
	@ResponseBody
	public ActionResult<List<LME>> LMEs(HttpServletRequest request) {
		List<LME> lmes = LMEService.LMEs();
		ActionResult<List<LME>> tempVar = new ActionResult<List<LME>>();
		tempVar.setData(lmes);
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<LME> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return LMEService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<LME> tempVar = new ActionResult<LME>();
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
	public ActionResult Save(HttpServletRequest request, @RequestBody LME lme) {
		try {
			if (lme.getId()!=null) {
				lme.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				lme.setUpdatedAt(new Date());
			} else {
				lme.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				lme.setCreatedAt(new Date());
			}
			return LMEService.Save(lme);
		} catch (RuntimeException ex) {
			ActionResult<LME> tempVar = new ActionResult<LME>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 特殊用法：在服务层直接调用远程接口，完成数据的同步
	 * @return
	 */
	@RequestMapping("Sync")
	@ResponseBody
	public ActionResult<String> Sync(HttpServletRequest request) {
		return LMEService.Sync();
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
			return LMEService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult Pager(HttpServletRequest request, @RequestBody QuotationParams param) {
		//List<LME> aaa = LMEService.LMEs();
		if (param == null) {
			param = new QuotationParams();
		}
		Criteria criteria = LMEService.GetCriteria();
		// 市场品种标识
		if (param.getMarketId() != null) {
			criteria.add(Restrictions.eq("MarketId", param.getMarketId()));
		}
		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}
		// 开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total=new RefUtil();
		List<LME> lmes = LMEService.LMEs(criteria, param.getPageSize(), param.getPageIndex(), param.getSortBy(),
				param.getOrderBy(),total);
		
		ActionResult<List<LME>> tempVar = new ActionResult<List<LME>>();
		tempVar.setData(lmes);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);
		return tempVar;
	}
}