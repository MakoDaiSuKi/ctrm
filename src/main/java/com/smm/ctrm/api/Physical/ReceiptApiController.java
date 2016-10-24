package com.smm.ctrm.api.Physical;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.BaseApiController;
import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.apiClient.ReceiptParams;
import com.smm.ctrm.domain.apiClient.ReceiptShipInitParam;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ReceiptShipInit;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Physical/Receipt/")
public class ReceiptApiController extends BaseApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	ReceiptService receiptService;

	@RequestMapping("Init")
	@ResponseBody
	public ActionResult<ReceiptShipInit> Init(@RequestBody ReceiptShipInitParam params) {
		try {
			return receiptService.Init(params.getLotId(), params.getFlag());
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<ReceiptShip> Save(@RequestBody ReceiptShip receipt) {
		try {
			return receiptService.Save(receipt);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("SaveTest")
	@ResponseBody
	public ActionResult<String> test(@RequestBody String id) {
		try {
			return receiptService.SaveTest(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}

	}

	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(@RequestBody String id) {
		try {
			return receiptService.Delete(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<ReceiptShip> GetById(@RequestBody String id) {
		try {
			return receiptService.GetById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<ReceiptShip>> Pager(@RequestBody ReceiptParams receiptParams) {

		try {
			TestTime.start();
			RefUtil total = new RefUtil();
			Criteria criteria = receiptService.CreateCriteria();
			criteria.setFetchMode("Lot", FetchMode.JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			
			// 关键字
			if (!StringUtils.isBlank(receiptParams.getKeyword())) {
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Warehouse", "warehouse", JoinType.LEFT_OUTER_JOIN);
				if(receiptParams.getFlag().equalsIgnoreCase(ReceiptShip.RECEIPT)){
					
					Disjunction disjunction=Restrictions.disjunction();
					disjunction.add(Restrictions.like("ReceiptShipNo", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("warehouse.Name", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("lot.FullNo", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("customer.Name", "%" + receiptParams.getKeyword() + "%"));
					criteria.add(disjunction);
				}
				else if(receiptParams.getFlag().equalsIgnoreCase(ReceiptShip.SHIP)){
					
					Disjunction disjunction=Restrictions.disjunction();
					disjunction.add(Restrictions.like("ReceiptShipNo", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("warehouse.Name", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("lot.FullNo", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("customer.Name", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("DeliveryMan","%" + receiptParams.getKeyword() + "%" ));
					disjunction.add(Restrictions.like("DeliveryManIDCard", "%" + receiptParams.getKeyword() + "%"));
					disjunction.add(Restrictions.like("DeliveryUnit", "%" + receiptParams.getKeyword() + "%"));
					criteria.add(disjunction);
				}
			}
			
			// 品种标识
			if (StringUtils.isNotBlank(receiptParams.getCommodityId())) {
				criteria.add(Restrictions.eq("CommodityId", receiptParams.getCommodityId()));
			}

			// 内部台头的标识
			if (StringUtils.isNotBlank(receiptParams.getLegalId())) {
				criteria.add(Restrictions.eq("lot.LegalId", receiptParams.getLegalId()));
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
			if (StringUtils.isNotBlank(receiptParams.getStatus())) {
				String[] strings = receiptParams.getStatus().split(",");
				List<Integer> list = new ArrayList<>();
				for (String str : strings) {
					list.add(Integer.parseInt(str.trim()));
				}
				if(list.size()>0){
					criteria.add(Restrictions.in("Status", list));
				}

			}
			
			// 业务日期：开始日期
			if (receiptParams.getStartDate() != null) {
				criteria.add(Restrictions.ge("ReceiptShipDate", receiptParams.getStartDate()));
			}
			// 业务日期：结束日期
			if (receiptParams.getEndDate() != null) {
				criteria.add(Restrictions.le("ReceiptShipDate", receiptParams.getEndDate()));
			}

			TestTime.addMilestone("参数构造完成");
			ActionResult<List<ReceiptShip>> list =  receiptService.Pager(criteria, receiptParams, total);
			TestTime.addMilestone("查询完成");
			logger.info(TestTime.result());
			return list;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	@RequestMapping("existPendingShip")
	@ResponseBody
	public ActionResult<Boolean> existPendingShip(@RequestBody String lotId) {

		try {
			return receiptService.existPendingShip(lotId, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

}
