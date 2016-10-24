package com.smm.ctrm.api.Physical;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.BaseApiController;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Physical.FinishedProductService;
import com.smm.ctrm.domain.Physical.FinishedProduct;
import com.smm.ctrm.domain.apiClient.FinishedProductParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.FinishedProductInit;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.RefUtil;

/**
 * @author zhaoyutao
 *
 */
@Controller
@RequestMapping("api/Physical/FinishedProduct/")
public class FinishedProductApiController extends BaseApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	FinishedProductService finishedProductService;
	
	@Resource
	private CheckService checkService ;

	@RequestMapping("Init")
	@ResponseBody
	public ActionResult<String> Init(@RequestBody(required = false) FinishedProductInit params) {
		try {
			return finishedProductService.Init();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<FinishedProduct> Save(@RequestBody FinishedProduct finishedProduct) {
		try {
			return finishedProductService.Save(finishedProduct);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(@RequestBody String id) {
		try {
			return finishedProductService.Delete(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<FinishedProduct> GetById(@RequestBody String id) {
		try {
			return finishedProductService.GetById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<FinishedProduct>> Pager(@RequestBody FinishedProductParams receiptParams) {
		try {
			RefUtil total = new RefUtil();
			Criteria criteria = finishedProductService.CreateCriteria();
			criteria.createAlias("Spec", "spec", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("WareHouse", "wareHouse", JoinType.LEFT_OUTER_JOIN);
			if(!StringUtils.isBlank(receiptParams.getKeyword()))
			{
				criteria.add(
						Restrictions.or(
								Restrictions.or(
										Restrictions.like("No", "%" + receiptParams.getKeyword() + "%"),
										Restrictions.like("spec.Name", "%" + receiptParams.getKeyword() + "%")
								),
								Restrictions.like("wareHouse.Name", "%" + receiptParams.getKeyword() + "%")
						));
			}
			
			// 品种标识
			if (StringUtils.isNotBlank(receiptParams.getCommodityId())) {
				criteria.add(Restrictions.eq("CommodityId", receiptParams.getCommodityId()));
			}

			// 内部台头的标识
			if (StringUtils.isNotBlank(receiptParams.getLegalId())) {
				criteria.add(Restrictions.eq("LegalId", receiptParams.getLegalId()));
			}
			
			// 创建人员：多选
			if (StringUtils.isNotBlank(receiptParams.getCreatedBys())) {
				String[] strings = receiptParams.getCreatedBys().split(",");
				List<String> list = new ArrayList<>();
				for (String str : strings) {
					list.add(str.trim());
				}
				if(list.size()>0){
					criteria.add(Restrictions.in("CreatedId", list));
				}

			}
			
			// 状态：多选
			if (receiptParams.getStatus()!=null) {
				criteria.add(Restrictions.eq("IsEnd", receiptParams.getStatus()));
			}
			
			// 业务日期：开始日期
			if (receiptParams.getStartDate() != null) {
				criteria.add(Restrictions.ge("TradeDate", receiptParams.getStartDate()));
			}
			// 业务日期：结束日期
			if (receiptParams.getEndDate() != null) {
				criteria.add(Restrictions.le("TradeDate", receiptParams.getEndDate()));
			}
			
			return finishedProductService.Pager(criteria, receiptParams, total);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

}
