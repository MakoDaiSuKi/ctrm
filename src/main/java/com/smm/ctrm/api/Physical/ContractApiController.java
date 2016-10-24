package com.smm.ctrm.api.Physical;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.VmContractLot4Combox;
import com.smm.ctrm.domain.apiClient.ContractParams;
import com.smm.ctrm.domain.apiClient.CpContract;
import com.smm.ctrm.domain.apiClient.ImportContractParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.SpotType;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by zhenghao on 2016/4/21.
 *
 *
 */
@Controller
@RequestMapping("api/Physical/Contract/")
public class ContractApiController {

	private static Logger logger = Logger.getLogger(ContractApiController.class);

	@Resource
	private ContractService contractService;

	@Resource
	private CheckService checkService;

	@Resource
	private ProductService productService;

	@Resource
	private CommonService commonService;

	@Resource
	private LotService lotService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Contract> GetById(HttpServletRequest request, @RequestBody String id) {
		try {			return contractService.GetById(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 用于"合同一LANG表"
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("ContractViewById")
	@ResponseBody
	public ActionResult<Contract> ContractViewById(HttpServletRequest request, @RequestBody String id) {
		try {
			return contractService.ContractViewById(id);
		} catch (RuntimeException ex) {
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
	public ActionResult<?> Pager(HttpServletRequest request, @RequestBody ContractParams param) {
		TestTime.start();
		if (param == null) {
			param = new ContractParams();
		}
		Criteria criteria = contractService.GetCriteria();

		// 加入权限过滤参数
		String userId = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userId, criteria, "CreatedId");
		/*
		 *  criteria.createAlias("Commodity",
		 * "commodity", JoinType.LEFT_OUTER_JOIN);
		 */
		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Legal", "legal", JoinType.LEFT_OUTER_JOIN);
		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			
			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.like("HeadNo", "%" + param.getKeyword() + "%"),
							Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%")),
					Restrictions.or(Restrictions.like("legal.Name", "%" + param.getKeyword() + "%"),
							Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"))));
		}

		// 币种
		if (StringUtils.isNotBlank(param.getCurrency())) {
			criteria.add(Restrictions.eq("Currency", param.getCurrency()));
		}

		// 业务类型
		if (StringUtils.isNotBlank(param.getSpotType())) {
			criteria.add(Restrictions.eq("SpotType", param.getSpotType()));
		}

		// 业务状态：多选
		if (StringUtils.isNotBlank(param.getStatuses())) {
			String[] strings = param.getStatuses().split(",");
			List<Integer> list = new ArrayList<>();
			for (String str : strings) {
				list.add(Integer.parseInt(str.trim()));
			}
			if (list.size() > 0) {
				criteria.add(Restrictions.in("Status", list));
			}

		}
		
		if (StringUtils.isNotBlank(param.getTransactionType())) {
			String[] strings = param.getTransactionType().split(",");
			List<String> list = new ArrayList<>();
			for (String str : strings) {
				list.add(str.trim());
			}
			if (list.size() > 0) {
				criteria.add(Restrictions.in("TransactionType", list));
			}

		}
		// 台头标识：多选
		if (StringUtils.isNotBlank(param.getLegalIds())) {

			String[] split = org.springframework.util.StringUtils.trimAllWhitespace(param.getLegalIds()).split(",");

			criteria.add(Restrictions.in("LegalId", Arrays.asList(split)));
		}

		// 创建者
		if (StringUtils.isNotBlank(param.getCreatedId())) {
			criteria.add(Restrictions.eq("CreatedId", param.getCreatedId()));
		}

		// 品种标识
		if (StringUtils.isNotBlank(param.getCommodityId())) {
			criteria.add(Restrictions.eq("CommodityId", param.getCommodityId()));
		}

		// 内部台头的标识
		if (StringUtils.isNotBlank(param.getLegalId())) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}

		// 全部，采购，销售 {P, S}
		if (StringUtils.isNotBlank(param.getSpotDirection())) {
			criteria.add(Restrictions.eq("SpotDirection", param.getSpotDirection()));
		}

		// 合同日期：开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 合同日期：结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		criteria.add(Restrictions.eq("IsHidden", false));

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();
		TestTime.addMilestone("参数构造完成");
		List<Contract> contracts = contractService.Contracts(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total);
		TestTime.addMilestone("查询完成");
		List<Contract> objs = new ArrayList<>();

		List<Lot> allLots = contractService
				.LotsByContractId(contracts.stream().map(x -> x.getId()).collect(Collectors.toList()));

		// 按合同编号分组
		Map<String, List<Lot>> groupLots = new HashMap<>();

		if (allLots.size() > 0) {
			groupLots = allLots.stream().collect(Collectors.groupingBy(Lot::getContractId));
		}

		if (contracts.size() > 0) {

			// 循环所有合同
			for (Contract contract : contracts) {

				// 格式化合同数据
				// Contract obj = commonService.SimplifyData(contract);
				Contract obj = com.smm.ctrm.util.BeanUtils.copy(contract);

				// 获取合同对应所有批次
				List<Lot> lots = groupLots.get(obj.getId());

				obj.setLots(this.SimplifyDataLotList2(lots));

				// 判断合同是否完成，所有批次完成才算合同完成
				if (param.getIsConfirm() != null) {

					for (int i = obj.getLots().size() - 1; i >= 0; i--) {

						boolean notConfirm = !obj.getLots().get(i).getIsDelivered()
								|| !obj.getLots().get(i).getIsInvoiced() || !obj.getLots().get(i).getIsPriced()
								|| !obj.getLots().get(i).getIsHedged() || !obj.getLots().get(i).getIsFunded();

						if (param.getIsConfirm()) {
							if (notConfirm) {
								obj.getLots().remove(i);
							}
						} else {
							if (!notConfirm) {
								obj.getLots().remove(i);
							}
						}
					}

					// 检查该合同是否已经完成
					// boolean isConfrim = checkIsConfirmForContract(obj);

					// 如果已经完成，保存在返回结果集中
					if (obj.getLots().size() != 0) {
						objs.add(obj);
					}

				} else {
					objs.add(obj);
				}
			}
		}
		logger.info(TestTime.result());
		return new ActionResult<>(true, "", objs, total);
	}

	// 格式化批次数据
	private List<Lot> SimplifyDataLotList(List<Lot> lots) {

		List<Lot> objs = new ArrayList<>();

		if (lots == null || lots.size() < 1)
			return objs;

		for (Lot lot : lots) {

			// 去除对象本身的关联关系
			Lot obj = com.smm.ctrm.util.BeanUtils.copy(lot);

			// 赋值
			obj.setLegal(lot.getLegal());

			obj.setCustomerName(lot.getCustomer() == null ? "" : lot.getCustomer().getName());

			obj.setTraderName(lot.getContract().getTraderName());

			obj.setLegalCode(lot.getLegal() == null ? "" : lot.getLegal().getCode());
			obj.setLegalName(lot.getLegal() == null ? "" : lot.getLegal().getName());

			obj.setMajorMarketName(lot.getMajorMarket() == null ? "" : lot.getMajorMarket().getName());

			obj.setDigits(lot.getCommodity() == null ? 0 : lot.getCommodity().getDigits());
			obj.setUnit(lot.getCommodity() == null ? "" : lot.getCommodity().getUnit());

			// region 格式化数量,单价
			obj.setQuantity(this.formatQuantity(obj.getQuantity(), lot.getCommodity()));

			obj.setPremium(this.formatPrice(obj.getPremium(), lot.getCommodity()));

			obj.setMajor(this.formatPrice(obj.getMajor(), lot.getCommodity()));

			obj.setFinal(this.formatPrice(obj.getFinal(), lot.getCommodity()));

			obj.setBrands(lot.getBrands());

			obj.setQuantityBeforeChanged(lot.getQuantity());

			objs.add(obj);
		}

		return objs;
	}

	// 格式化批次数据
	private List<Lot> SimplifyDataLotList2(List<Lot> lots) {

		List<Lot> objs = new ArrayList<>();

		if (lots == null || lots.size() < 1)
			return objs;

		for (Lot lot : lots) {
			// 去除对象本身的关联关系
			Lot obj = com.smm.ctrm.util.BeanUtils.copy(lot);
			objs.add(obj);
		}

		return objs;
	}

	// 格式化价格
	private BigDecimal formatPrice(BigDecimal premium, Commodity commodity) {

		if (premium == null) {

			return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

		} else {

			int temp = (commodity != null && commodity.getDigits4Price() > 0) ? commodity.getDigits4Price() : 0;

			return premium.setScale(temp, RoundingMode.HALF_EVEN);
		}

	}

	// 格式化数量
	private BigDecimal formatQuantity(BigDecimal quantity, Commodity commodity) {

		if (quantity == null) {

			return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_EVEN);

		} else {

			int temp = (commodity != null && commodity.getDigits() >= 0) ? commodity.getDigits() : 0;

			return quantity.setScale(temp, RoundingMode.HALF_EVEN);
		}
	}

	// 检查合同是否已完成。完成返回true ，否则返回false
	// 判断合同中的所有批次是否已完成
	private boolean checkIsConfirmForContract(Contract obj) {

		List<Lot> lots = obj.getLots();

		if (lots == null || lots.size() < 1)
			return false;
		List<Lot> removeList = new ArrayList<>();
		// 过滤掉未完成的批次
		for (Lot lot : lots) {

			boolean notConfirm = !lot.getIsDelivered() || !lot.getIsInvoiced() || !lot.getIsPriced()
					|| !lot.getIsHedged() || !lot.getIsFunded();

			if (notConfirm)
				removeList.add(lot);
		}
		if (removeList.size() > 0)
			lots.removeAll(removeList);

		// 如果都没有完成，返回false
		if (lots.size() == 0)
			return false;

		// 如果还有完成的批次，保存在合同对象中。
		obj.setLots(lots);

		return true;
	}

	@RequestMapping("Pager4Glue")
	@ResponseBody
	public ActionResult<List<VmContractLot4Combox>> Pager4Glue(HttpServletRequest request,
			@RequestBody(required = false) ContractParams param) {
		if (param == null) {
			param = new ContractParams();
		}
		Criteria criteria = contractService.GetCriteria();

		criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Legal", "legal", JoinType.LEFT_OUTER_JOIN);
		criteria.createAlias("Commodity", "commodity", JoinType.LEFT_OUTER_JOIN);

		// 关键字
		if (StringUtils.isNotEmpty(param.getKeyword()) && !StringUtils.isBlank(param.getKeyword())) {
			criteria.add(Restrictions.or(
					Restrictions.or(Restrictions.like("HeadNo", "%" + param.getKeyword() + "%"),
							Restrictions.like("DocumentNo", "%" + param.getKeyword() + "%")),
					Restrictions.or(Restrictions.like("legal.Name", "%" + param.getKeyword() + "%"),
							Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"))));
		}

		// 业务状态
		if (StringUtils.isNotEmpty(param.getStatuses()) && !StringUtils.isBlank(param.getStatuses())) {

			String[] strings = param.getStatuses().split(",");

			Stream<String> stream = Arrays.stream(strings);
			List<Integer> arrs = stream.map(s -> Integer.valueOf(s)).collect(Collectors.toList());

			criteria.add(Restrictions.in("Status", arrs));

		}

		// 创建者
		if (StringUtils.isNotEmpty(param.getCreatedId())) {
			criteria.add(Restrictions.eq("CreatedId", param.getCreatedId()));
		}

		// 品种标识
		if (StringUtils.isNotEmpty(param.getCommodityId())) {
			criteria.add(Restrictions.like("CommodityId", param.getCommodityId()));
		}

		// 内部台头的标识
		if (StringUtils.isNotEmpty(param.getLegalId())) {
			criteria.add(Restrictions.like("LegalId", param.getLegalId()));
		}

		// 全部，采购，销售 {P, S}
		if (StringUtils.isNotEmpty(param.getSpotDirection()) && !StringUtils.isBlank(param.getSpotDirection())) {
			criteria.add(Restrictions.eq("SpotDirection", param.getSpotDirection()));
		}

		// 合同日期：开始日期
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		// 合同日期：结束日期
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}

		RefUtil total = new RefUtil();
		List<Contract> contracts = contractService.Contracts(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total);

		List<VmContractLot4Combox> ret = new ArrayList<VmContractLot4Combox>();
		contracts.forEach(x -> {
			VmContractLot4Combox tempVar = new VmContractLot4Combox();
			tempVar.setId(x.getId());
			tempVar.setLegalName(x.getLegal().getName());
			tempVar.setHeadNo(x.getHeadNo());
			tempVar.setQuantity(x.getQuantity());
			tempVar.setCustomerName(x.getCustomer().getName());
			tempVar.setCustomerShortName(x.getCustomer().getShortName());
			ret.add(tempVar);
		});

		ActionResult<List<VmContractLot4Combox>> tempVar2 = new ActionResult<List<VmContractLot4Combox>>();
		tempVar2.setSuccess(true);
		tempVar2.setTotal(total.getTotal());
		tempVar2.setData(ret);
		return tempVar2;
	}

	@RequestMapping("GetMaxSerialNo")
	@ResponseBody
	public ActionResult<String> GetMaxSerialNo(HttpServletRequest request, @RequestBody ContractParams param) {
		try {
			if (param == null || param.getLegalId() == null || param.getCommodityId() == null
					|| StringUtils.isBlank(param.getSpotDirection())
					|| (!param.getSpotDirection().equals(SpotType.Purchase)
							&& !param.getSpotDirection().equals(SpotType.Sell))) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("ParamError");
				tempVar.setTotal(1);
				return tempVar;
			}

			Date tradeDate = new Date();

			if (param.getTradeDate() != null) {
				tradeDate = param.getTradeDate();
			}

			return contractService.GetMaxSerialNo(param.getLegalId(), param.getSpotDirection(), tradeDate,
					param.getCommodityId());
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar2;
		}
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
			
			ActionResult<List<Lot>> listLot =lotService.GetLotsByContractId(id);
			if(listLot.getData()!=null && listLot.getData().size() > 0)
			{
				for(Lot lot:listLot.getData())
				{
					List<String> tableList = new ArrayList<>();
					tableList.add(TableNameConst.Position);
					tableList.add(TableNameConst.Pricing);
					tableList.add(TableNameConst.Invoice);
					tableList.add(TableNameConst.ReceiptShip);
					tableList.add(TableNameConst.Fund);
					tableList.add(TableNameConst.Storage);
//					tableList.add(TableNameConst.FinishedProduct);
					
					ActionResult<String> checkResult = checkService.deletable(lot.getId(), "LotId", tableList);
					if(!checkResult.isSuccess()) {
						return new ActionResult<>(false, checkResult.getMessage());
					}
				}
			}
			
			/*
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Lot);
			ActionResult<String> checkResult = checkService.deletable(id, "ContractId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			*/

			String userId = LoginHelper.GetLoginInfo(request).getUserId();

			return contractService.Delete(id, userId);
		} catch (RuntimeException e) {
			logger.error(e.getMessage(), e);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("DeleteFail");
			return tempVar;
		}
	}

	/**
	 * 保存长单的合同的头部
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("SaveHeadOfContractRegular")
	@ResponseBody
	public ActionResult<Contract> SaveHeadOfContractRegular(HttpServletRequest request,
			@RequestBody Contract contract) {
		String userId = LoginHelper.GetLoginInfo(request).getUserId();

		if (contract == null || contract.getIsProvisional()) {
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		try {
			if (contract.getId() != null) {
				contract.setUpdatedAt(new Date());
				contract.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				contract.setStatus(Status.Draft);
				contract.setCreatedAt(new Date());
				contract.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}

			if (!contract.getCreatedId().equalsIgnoreCase(userId)) {
				ActionResult<Contract> tempVar2 = new ActionResult<Contract>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("不可以修改他人的记录。");
				return tempVar2;
			}

			return contractService.SaveHeadOfContractRegular(contract);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Contract> tempVar3 = new ActionResult<Contract>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 获取指定客户的全部合同
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping("ContractsByCustomerId")
	@ResponseBody
	public ActionResult<List<Contract>> ContractsByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		return contractService.ContractsByCustomerId(customerId);
	}

	/**
	 * 特殊的业务，同时创建Bvi的销售合同和Sm的采购合同 特殊的业务， 新增时，同时创建Bvi的销售合同和Sm的采购合同
	 * 修改时，只修改Bvi的销售合同，不修改对应的"商贸采购合同"
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("SaveHead4BviToSm")
	@ResponseBody
	public ActionResult<Contract> SaveHead4BviToSm(HttpServletRequest request, @RequestBody Contract contract) {

		if (contract == null || contract.getIsProvisional()) {
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		try {
			if (contract.getId() != null) {
				contract.setUpdatedAt(new Date());
				contract.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				contract.setStatus(Status.Draft);
				contract.setCreatedAt(new Date());
				contract.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}
			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			if (!contract.getCreatedId().equalsIgnoreCase(userId)) {
				ActionResult<Contract> tempVar2 = new ActionResult<Contract>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("不可以修改他人的记录。");
				return tempVar2;
			}

			return contractService.SaveHead4BviToSm(contract);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Contract> tempVar3 = new ActionResult<Contract>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 特殊的业务，只修改"商贸采购合同"本身，不修改其它的信息
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("SaveHead4SmToBvi")
	@ResponseBody
	public ActionResult<Contract> SaveHead4SmToBvi(HttpServletRequest request, @RequestBody Contract contract) {
		/**
		 * 检查的数据的合法性
		 */
		if (contract == null || contract.getIsProvisional()) {
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		try {
			if (contract.getId() != null) {
				contract.setUpdatedAt(new Date());
				contract.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setUpdatedId(LoginHelper.GetLoginInfo(request).getUserId());
			} else {
				contract.setStatus(Status.Draft);
				contract.setCreatedAt(new Date());
				contract.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
				contract.setCreatedId(LoginHelper.GetLoginInfo(request).getUserId());
			}

			String userId = LoginHelper.GetLoginInfo(request).getUserId();
			if (!contract.getCreatedId().equalsIgnoreCase(userId)) {
				ActionResult<Contract> tempVar2 = new ActionResult<Contract>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("不可以修改他人的记录。");
				return tempVar2;
			}

			return contractService.SaveHead4SmToBvi(contract);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			ActionResult<Contract> tempVar3 = new ActionResult<Contract>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 保存订单及生成对手方订单
	 * 
	 * @param contract
	 * @return
	 */
	@RequestMapping("SaveContractAndRivalOrder")
	@ResponseBody
	public ActionResult<String> SaveContractAndRivalOrder(@RequestBody(required = false) Contract contract) {
		// 检查的数据的合法性
		String userId = LoginHelper.GetLoginInfo().UserId;
		if (contract == null || contract.getIsProvisional()) {
			return new ActionResult<>(false, MessageCtrm.ParamError);
		}
		try {
			if (StringUtils.isNotBlank(contract.getId())) {
				contract.setUpdatedId(userId);
			} else {
				contract.setStatus(Status.Draft);
				contract.setCreatedId(userId);
			}
			if (!userId.equalsIgnoreCase(contract.getCreatedId())) {
				return new ActionResult<>(false, "不可以修改他人的记录。");
			}
			return contractService.txSaveContractAndRivalOrder(contract);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 单批次合同导入
	 */
	@RequestMapping("ImportContract")
	@ResponseBody
	public ActionResult<String> importContract(@RequestBody ImportContractParams contract) {
		try {
			return contractService.importContract(contract);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	/**
	 * 检查合同订单是否发生后续业务
	 */
	@RequestMapping("Verify")
	@ResponseBody
	public ActionResult<Boolean> Verify(@RequestBody String  contractId) {
		try {
			return contractService.verify(contractId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	/**
	 * 快捷订单
	 */
	@RequestMapping("FastContract")
	@ResponseBody
	public ActionResult<CpContract> FastContract(@RequestBody CpContract  contract) {
		try {
			return contractService.fastContract(contract);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	/** 桑基图
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("getSanKey")
	@ResponseBody
	public ActionResult<String> getSanKey(ContractParams param) {
		try {
			return contractService.getSanKey(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}