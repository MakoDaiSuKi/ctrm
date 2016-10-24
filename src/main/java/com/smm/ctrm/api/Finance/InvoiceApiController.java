
package com.smm.ctrm.api.Finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.InvoiceService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Physical.CInvoice;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.InvoicePnL;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Param4InvoicePnL;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.InvoiceParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.FeeCode;
import com.smm.ctrm.util.Result.InvoiceType;
import com.smm.ctrm.util.Result.MT4Invoice;

@Controller
@RequestMapping("api/Finance/Invoice/")
public class InvoiceApiController {

	private static Logger logger = Logger.getLogger(InvoiceApiController.class);

	@Resource
	private InvoiceService invoiceService;

	@Resource
	private CommonService commonService;

	/**
	 * 根据Sql，返回需要的数据集合
	 * 
	 * @param sql
	 * @return
	 */
	@RequestMapping("PagerViaSql")
	@ResponseBody
	public ActionResult<List<Invoice>> PagerViaSql(HttpServletRequest request, @RequestBody String sql) {
		try {
			//
			String filter = "from Invoice";
			if (!StringUtils.isBlank(sql)) {
				filter += " where " + sql;
			}
			List<Invoice> invoices = invoiceService.Invoices4Sql(filter);
			if (invoices != null && invoices.size() > 0) {
				List<Invoice> t = new ArrayList<Invoice>();
				for (Invoice invoice : invoices) {

					Invoice n = new Invoice();
					BeanUtils.copyProperties(invoice, n);

					if (invoice.getLot() != null) {
						n.setFullNo(invoice.getLot().getFullNo());
					}
					if (invoice.getCustomer() != null) {
						n.setCustomerName(invoice.getCustomer().getName());
					}
					if (invoice.getLegal() != null) {
						n.setLegalName(invoice.getLegal().getName());
					}
					n.setStorages(invoice.getStorages());
					t.add(n);
				}
				ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
				tempVar.setSuccess(true);
				tempVar.setTotal(t.size());
				tempVar.setData(t);
				return tempVar;
			}

			ActionResult<List<Invoice>> tempVar2 = new ActionResult<List<Invoice>>();
			tempVar2.setSuccess(true);
			tempVar2.setTotal(0);
			tempVar2.setData(null);
			return tempVar2;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<CInvoice>> Pager(HttpServletRequest request, @RequestBody InvoiceParams param) {
		TestTime.start();
		if (param == null) {
			param = new InvoiceParams();
		}

		Criteria criteria = invoiceService.GetCriteria2();
		// 加入权限过滤参数
		String userId = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userId, criteria, "CreatedId");

		// 关键字
		if (StringUtils.isNotBlank(param.getKeyword())) {
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			/*
			 * criteria.createAlias("Contract", "contract",
			 * JoinType.LEFT_OUTER_JOIN);
			 */
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);

			Criterion a = Restrictions.like("InvoiceNo", "%" + param.getKeyword() + "%");
			Criterion b = Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%");
			/*
			 * Criterion c = Restrictions.like("contract.HeadNo", "%" +
			 * param.getKeyword() + "%");
			 */
			Criterion d = Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%");
			Criterion e = Restrictions.like("customer.Name", "%" + param.getKeyword() + "%");
			Criterion f = Restrictions.like("Comments", "%" + param.getKeyword() + "%");
			Disjunction disjunction = Restrictions.disjunction();

			disjunction.add(a);
			disjunction.add(b);
			/* disjunction.add(c); */
			disjunction.add(d);
			disjunction.add(e);
			disjunction.add(f);
			criteria.add(disjunction);
		}

		// 发票类型
		if (StringUtils.isNotBlank(param.getPFA())) {
			criteria.add(Restrictions.eq("PFA", param.getPFA()));
		}
		// 费用类型
		if (StringUtils.isNotBlank(param.getFeeCode())) {
			criteria.add(Restrictions.eq("FeeCode", param.getFeeCode()));
		}

		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}
		if (param.getLotId() != null) {
			if (param.getIsContainSplitLotInvoice() != null) {// 获取批次信息
				List<Lot> lots = invoiceService.LotsById(param.getLotId());
				List<String> l1 = lots.stream().map(s -> s.getId()).collect(Collectors.toList());
				criteria.add(Restrictions.in("LotId", l1));
			} else {
				criteria.add(Restrictions.eq("LotId", param.getLotId()));
			}
		}
		if (param.getContractId() != null) {
			criteria.add(Restrictions.eq("ContractId", param.getContractId()));
		}
		// M = 开票, T = 收票
		if (StringUtils.isNotBlank(param.getMT())) {
			criteria.add(Restrictions.eq("MT", param.getMT()));
		}
		// 开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		if (param.getAdjustId() != null && (param.getIsAdjust() != null ? param.getIsAdjust() : false)) {
			criteria.add(
					Restrictions.or(Restrictions.eq("AdjustId", param.getAdjustId()), Restrictions.isNull("AdjustId")));
		} else if (param.getAdjustId() != null) {
			criteria.add(Restrictions.eq("AdjustId", param.getAdjustId()));
		} else if (param.getIsAdjust() != null) {
			criteria.add(Restrictions.isNull("AdjustId"));
		}
		if (param.getIsAccounted() != null) {
			criteria.add(Restrictions.eq("IsAccounted", param.getIsAccounted()));
		}
		if (param.getIsSettled() != null) {
			criteria.add(Restrictions.eq("IsSettled", param.getIsSettled()));
		}
		if (param.getIsExecuted() != null) {
			criteria.add(Restrictions.eq("IsExecuted", param.getIsExecuted()));
		}
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		RefUtil total = new RefUtil();
		TestTime.addMilestone("参数构造完成");
		List<CInvoice> invoices = invoiceService.Invoices2(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total);

		TestTime.addMilestone("查询完成");
		invoices = commonService.SimplifyDataInvoicePager(invoices);
		TestTime.addMilestone("格式化完成");
		logger.info(TestTime.result());
		return new ActionResult<>(true, "", invoices, total);
	}

	/**
	 * 交易报告
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("InvoiceTradeReport")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoiceTradeReport(HttpServletRequest request,
			@RequestBody InvoiceParams param) {
		if (param == null) {
			param = new InvoiceParams();
		}

		Criteria criteria = invoiceService.GetCriteria();
		criteria.add(Restrictions.eq("MT", MT4Invoice.Make));
		criteria.add(Restrictions.eq("FeeCode", FeeCode.Goods));

		// 品种标识
		if (param.getCommodityId() != null) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}

		// 品种标识 - 多选
		if (!StringUtils.isBlank(param.getCommodityIds())) {
			String[] split = param.getCommodityIds().split(",");
			criteria.add(Restrictions.in("CommodityId", Arrays.asList(split)));
		}

		// M = 开票, T = 收票
		if (!StringUtils.isBlank(param.getMT())) {
			criteria.add(Restrictions.eq("MT", param.getMT()));
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
		RefUtil total = new RefUtil();

		List<Invoice> invoices = invoiceService.Invoices(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total);

		List<Invoice> result = new ArrayList<Invoice>();

		for (Invoice invoice : invoices) {
			if (invoice == null) {
				continue;
			}
			Invoice newInvoice = new Invoice();
			BeanUtils.copyProperties(invoices, newInvoice);

			if (invoice.getCustomer() != null) {
				Customer newCustomer = new Customer();
				BeanUtils.copyProperties(invoice.getCustomer(), newCustomer);
				newInvoice.setCustomer(newCustomer);
			}
			if (invoice.getLot() != null) {
				Lot newLot = new Lot();
				BeanUtils.copyProperties(invoice.getLot(), newLot);

				newInvoice.setLot(newLot);
				newInvoice.setFullNo(invoice.getLot().getFullNo());
			}

			if (invoice.getStorages() != null) {
				newInvoice.setStorages(new ArrayList<Storage>());

				for (Storage stor : invoice.getStorages()) {
					if (stor == null || stor.getCounterparty() == null) {
						continue;
					}

					/**
					 * 注意这里要取counterParty的对象
					 */

					Storage newStor = new Storage();
					BeanUtils.copyProperties(stor.getCounterparty(), newStor);
					if (stor.getCounterparty().getLot() != null) {
						Lot newLot = new Lot();
						BeanUtils.copyProperties(stor.getCounterparty().getLot(), newLot);
						newStor.setLot(newLot);
					}

					newInvoice.getStorages().add(newStor);
				}
			}

			/**
			 * BigDecimal转 double有可能精度丢失 后续有关GrossProfitAmountbug，应该注意这里
			 */
			double cal = newInvoice.getStorages().stream().mapToDouble(x -> x.getQuantity()
					.multiply(x.getPrice() != null ? x.getPrice() : new BigDecimal(0)).doubleValue()).sum();
			BigDecimal sum = new BigDecimal(cal);
			newInvoice.setGrossProfitAmount(newInvoice.getAmount().subtract(sum));
			if (!newInvoice.getAmount().equals(BigDecimal.ZERO)) {
				newInvoice.setGrossProfitRate(
						((newInvoice.getGrossProfitAmount() != null ? newInvoice.getGrossProfitAmount()
								: new BigDecimal(0)).divide(newInvoice.getAmount())).multiply(new BigDecimal(100)));
			}
			result.add(newInvoice);
		}

		ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
		tempVar.setData(result);
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
	public ActionResult<Invoice> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return invoiceService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Invoice> tempVar = new ActionResult<Invoice>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}

	}

	/**
	 * 得到最大序号
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetMaxSerilNo")
	@ResponseBody
	public ActionResult<Integer> GetMaxSerilNo(HttpServletRequest request, @RequestBody String Preix) {
		try {
			Integer serilno = commonService.GetSequenceIndex(Preix, true);
			ActionResult<Integer> tempVar = new ActionResult<>();
			tempVar.setSuccess(true);
			tempVar.setData(serilno);
			return tempVar;
		} catch (Exception ex) {
			ActionResult<Integer> tempVar2 = new ActionResult<>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar2;
		}

	}

	/**
	 * 判断发票号是否重复
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("IsInvoiceNoDuplicate")
	@ResponseBody
	public ActionResult<Boolean> IsInvoiceNoDuplicate(HttpServletRequest request, @RequestBody Invoice invoice) {
		try {
			return invoiceService.IsInvoiceNoDuplicate(invoice);
		} catch (Exception ex) {
			ActionResult<Boolean> tempVar = new ActionResult<Boolean>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			tempVar.setData(false);
			ex.printStackTrace();
			return tempVar;
		}

	}

	@RequestMapping("InvoicePnLById")
	@ResponseBody
	public ActionResult<InvoicePnL> InvoicePnLById(HttpServletRequest request, @RequestBody String id) {
		try {

			return invoiceService.InvoicePnLById(id);
		} catch (Exception ex) {
			ActionResult<InvoicePnL> tempVar = new ActionResult<InvoicePnL>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
	
	@Resource
	HibernateRepository<Invoice> invoiceRepo;

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
			Invoice invoice = invoiceRepo.getOneById(id, Invoice.class);
			String invoiceNo = invoice.getInvoiceNo();
			int separateIndex = invoiceNo.indexOf("/");
			if(separateIndex > 0) {
				List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(DetachedCriteria.forClass(Invoice.class)
						.add(Restrictions.like("InvoiceNo", invoice.getInvoiceNo().substring(0, separateIndex) + "%"))).toList();
				return invoiceService.DeleteForStoragesInvoice(invoices);
			} else {
				return invoiceService.Delete(invoice);
			}
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存: 标准发票
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Invoice> Save(HttpServletRequest request, @RequestBody Invoice invoice) {
		try {
			if (invoice.getId() != null) {
				invoice.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				invoice.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			return invoiceService.Save(invoice);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}
	
	/**
	 * 采购收票功能
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("StorageToInvoiceSave")
	@ResponseBody
	public ActionResult<List<Invoice>> StorageToInvoiceSave(HttpServletRequest request, @RequestBody List<Invoice> invoices) {
		try {
			return invoiceService.StorageToInvoiceSave(invoices);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	/**
	 * 保存: 非标准发票，同时包括多个批次
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("Save4MultiLots")
	@ResponseBody
	public ActionResult<Invoice> Save4MultiLots(HttpServletRequest request, @RequestBody Invoice invoice) {
		if (invoice.getId() != null) {
			invoice.setUpdatedAt(new Date());
			invoice.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			invoice.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			invoice.setCreatedAt(new Date());
			invoice.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			invoice.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		return invoiceService.Save4MultiLots(invoice);
	}

	/**
	 * 保存，Note合计费用
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("Save4SummaryNoteFee")
	@ResponseBody
	public ActionResult<Invoice> Save4SummaryNoteFee(HttpServletRequest request, @RequestBody Invoice invoice) {
		if (invoice.getId() != null) {
			invoice.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
		} else {
			invoice.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
		}
		return invoiceService.Save4SummaryNoteFee(invoice);
	}

	/**
	 * 获取指定订单的全部发票
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("InvoicesByContractId")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoicesByContractId(HttpServletRequest request,
			@RequestBody String contractId) {
		try {
			return invoiceService.InvoicesByContractId(contractId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 获取指定批次的全部发票
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("InvoicesByLotId")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoicesByLotId(HttpServletRequest request, @RequestBody String lotId) {
		try {
			return invoiceService.InvoicesByLotId(lotId);
		} catch (Exception ex) {
			ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("InvoicesByStorageId")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoicesByStorageId(HttpServletRequest request, @RequestBody String id) {
		try {
			String sql = " select distinct Invoice.* " + " from Physical.Invoice Invoice inner join "
					+ " Physical.InvoiceStorage InvoiceStorage on Invoice.id = InvoiceStorage.InvoiceId "
					+ " where Invoice.Is4MultiLots = 1 and InvoiceStorage.StorageId ='" + id + "' ";
			List<Invoice> invoices = invoiceService.Invoices4Sql(sql);
			ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
			tempVar.setSuccess(true);
			tempVar.setData(invoices);
			return tempVar;

		} catch (Exception ex) {
			ActionResult<List<Invoice>> tempVar2 = new ActionResult<List<Invoice>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("InvoicesByStorageId2")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoicesByStorageId2(HttpServletRequest request, @RequestBody String id) {
		try {
			String sql = " select distinct Invoice.* " + " from Physical.Invoice Invoice inner join "
					+ " Physical.InvoiceStorage InvoiceStorage on Invoice.id = InvoiceStorage.InvoiceId "
					+ " where InvoiceStorage.StorageId ='" + id + "' ";

			List<Invoice> invoices = invoiceService.Invoices4Sql(sql);
			ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
			tempVar.setSuccess(true);
			tempVar.setData(invoices);
			return tempVar;

		} catch (Exception ex) {
			ActionResult<List<Invoice>> tempVar2 = new ActionResult<List<Invoice>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 获取指定客户的全部合同
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("InvoicesByCustomerId")
	@ResponseBody
	public ActionResult<List<Invoice>> InvoicesByCustomerId(HttpServletRequest request, @RequestBody String lotId) {
		try {
			return invoiceService.InvoicesByCustomerId(lotId);
		} catch (Exception ex) {
			ActionResult<List<Invoice>> tempVar = new ActionResult<List<Invoice>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 发票盈亏结算 - 试算
	 * 
	 * @param param4InvoicePnL
	 * @return
	 */
	@RequestMapping("InvoiceSettleTrial")
	@ResponseBody
	public ActionResult<InvoicePnL> InvoiceSettleTrial(HttpServletRequest request,
			@RequestBody Param4InvoicePnL param4InvoicePnL) {
		return invoiceService.InvoiceSettleTrial(param4InvoicePnL);
	}

	/**
	 * 发票盈亏结算 - 正式
	 * 
	 * @param param4InvoicePnL
	 * @return
	 */
	@RequestMapping("InvoiceSettleOfficial")
	@ResponseBody
	public ActionResult<InvoicePnL> InvoiceSettleOfficial(HttpServletRequest request,
			@RequestBody Param4InvoicePnL param4InvoicePnL) {
		return invoiceService.InvoiceSettleOfficial(param4InvoicePnL);
	}

	/**
	 * 计算父子批次总共的发票数量
	 * 
	 * @param lot
	 * @return
	 */
	@RequestMapping("CalculateQuantityOfInvoice")
	@ResponseBody
	public ActionResult<Invoice> CalculateQuantityOfInvoice(HttpServletRequest request, @RequestBody Lot lot) {
		try {
			if (lot == null) {
				ActionResult<Invoice> tempVar = new ActionResult<Invoice>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数错误");
				return tempVar;
			}
			// implicit typing in Java:
			Invoice invoice = commonService.CalculateQuantityOfInvoice(lot);
			ActionResult<Invoice> tempVar2 = new ActionResult<Invoice>();
			tempVar2.setSuccess(true);
			tempVar2.setData(invoice);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<Invoice> tempVar3 = new ActionResult<Invoice>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 获取拆分批次父子批次的发票
	 * 
	 * @param lotId
	 * @return
	 */
	@RequestMapping("InvoicesOfSplittedLotByLotId")
	@ResponseBody
	public ActionResult<Boolean> InvoicesOfSplittedLotByLotId(HttpServletRequest request, @RequestBody String lotId) {
		try {
			List<Invoice> listInvoice = commonService.InvoicesOfSplittedLotByLotId(lotId);
			boolean existInvoiceOfAorF = listInvoice.stream()
					.anyMatch(x -> x.getPFA().equals(InvoiceType.Adjust) || x.getPFA().equals(InvoiceType.Final));
			ActionResult<Boolean> tempVar = new ActionResult<Boolean>();
			tempVar.setSuccess(true);
			tempVar.setData(existInvoiceOfAorF);
			return tempVar;
		} catch (Exception ex) {
			ActionResult<Boolean> tempVar2 = new ActionResult<Boolean>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 作废调整发票
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("InvalidInvoiceById")
	@ResponseBody
	public ActionResult<Invoice> InvalidInvoiceById(HttpServletRequest request, @RequestBody Invoice invoice) {
		invoice.setInvalidAt(new Date());
		invoice.setInvalidBy(LoginHelper.GetLoginInfo(request).getName());
		invoice.setInvalidId(LoginHelper.GetLoginInfo(request).getUserId());
		return invoiceService.InvalidInvoiceById(invoice);
	}
	
	/**
	 * 获收货生成发票列表
	 * 
	 * @param invoice
	 * @return
	 */
	@RequestMapping("StoragesInvoiceById")
	@ResponseBody
	public ActionResult<List<Invoice>> InvalidInvoiceById(HttpServletRequest request, @RequestBody String invoiceId) {
		return invoiceService.StoragesInvoiceById(invoiceId);
	}
	
}