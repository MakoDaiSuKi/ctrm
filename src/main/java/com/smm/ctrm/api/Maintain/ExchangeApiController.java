
package com.smm.ctrm.api.Maintain;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.ExchangeService;
import com.smm.ctrm.domain.Maintain.Exchange;
import com.smm.ctrm.domain.apiClient.ExchangeParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Maintain/Exchange/")
public class ExchangeApiController {
	private Logger logger = Logger.getLogger(this.getClass());
	@Resource
	private ExchangeService exchangeService;

	@Resource
	private CommonService privateCommonService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Exchanges")
	@ResponseBody
	public ActionResult<List<Exchange>> Exchanges(HttpServletRequest request) {
		return exchangeService.Exchanges();
	}

	/**
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Exchange>> Pager(HttpServletRequest request, @RequestBody ExchangeParams param) {
		try {
			if (param == null) {
				param = new ExchangeParams();
			}
			Criteria criteria = exchangeService.GetCriteria();
			if (StringUtils.isNotBlank((param.getCurrency()))) {
				criteria.add(Restrictions.like("Currency", "%" + param.getCurrency() + "%"));
			}
			if (param.getTradeDate() != null) {
				criteria.add(Restrictions.eq("TradeDate", param.getTradeDate()));
			}
			RefUtil total = new RefUtil();
			List<Exchange> exchanges = exchangeService.PagerExchanges(criteria, param.getPageSize(),
					param.getPageIndex(), total, param.getOrderBy(), param.getSortBy());
			return new ActionResult<>(true, "", exchanges, total);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
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
	public ActionResult<Exchange> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return exchangeService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param exchange
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody Exchange exchange) {
		try {
			return exchangeService.Save(exchange);
		} catch (RuntimeException ex) {
			return new ActionResult<>(false, ex.getMessage());
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
			return exchangeService.Delete(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}