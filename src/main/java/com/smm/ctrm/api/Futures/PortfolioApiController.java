



package com.smm.ctrm.api.Futures;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.PortfolioService;
import com.smm.ctrm.domain.Physical.Portfolio;
import com.smm.ctrm.domain.apiClient.PortfolioParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Futures/Portfolio/")
public class PortfolioApiController {
	
	@Resource
	private PortfolioService portfolioService;

	@Resource
	private CommonService commonService;


	/**
	 * 分页查询
	 * @param param
	 * @return
	 */
	public ActionResult<List<Portfolio>> Pager(HttpServletRequest request, @RequestBody PortfolioParams param) {
		
		if (param == null) {
			param = new PortfolioParams();
		}
		Criteria criteria = portfolioService.GetCriteria();
		// 开始日期
		if (param.getStartDate() != null ) {
			criteria.add(Restrictions.ge("StartDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null ) {
			criteria.add(Restrictions.ge("TradeDate", param.getEndDate()));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total=new RefUtil();
		
		List<Portfolio> portfolios = portfolioService.Portfolios(criteria, param.getPageSize(), param.getPageIndex(), total,
				param.getSortBy(), param.getOrderBy());
		
		ActionResult<List<Portfolio>> tempVar = new ActionResult<List<Portfolio>>();
		tempVar.setData(portfolios);
		tempVar.setTotal(total.getTotal());
		tempVar.setSuccess(true);
		return tempVar;
	}

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Portfolios")
	@ResponseBody
	public ActionResult<List<Portfolio>> Portfolios(HttpServletRequest request) {
		List<Portfolio> portfolios = portfolioService.Portfolios();
		ActionResult<List<Portfolio>> tempVar = new ActionResult<List<Portfolio>>();
		tempVar.setData(portfolios);
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
	public ActionResult<Portfolio> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return portfolioService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Portfolio> tempVar = new ActionResult<Portfolio>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param portfolio
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult Save(HttpServletRequest request, @RequestBody Portfolio portfolio) {
		try {
			if (portfolio.getId()!=null) {
				portfolio.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				portfolio.setUpdatedAt(new Date());
			} else {
				portfolio.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				portfolio.setCreatedAt(new Date());
			}
			return portfolioService.Save(portfolio);
		} catch (RuntimeException ex) {

			ActionResult<Portfolio> tempVar = new ActionResult<Portfolio>();
			tempVar.setSuccess(false);
			tempVar.setData(portfolio);
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
			return portfolioService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}