package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.PositionService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.CpPosition4AllocateToMultiLot;
import com.smm.ctrm.domain.MultiLot4Postion;
import com.smm.ctrm.domain.NumTypes;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.domain.Basis.FeeSetup;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpPosition4AllocateToLot;
import com.smm.ctrm.domain.Physical.CpPosition4RemoveFromLot;
import com.smm.ctrm.domain.Physical.CpSplitLot;
import com.smm.ctrm.domain.Physical.CpSplitPosition;
import com.smm.ctrm.domain.Physical.Fee;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.LC;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Lot4MTM3;
import com.smm.ctrm.domain.Physical.Lot4Unpriced;
import com.smm.ctrm.domain.Physical.LotPnL;
import com.smm.ctrm.domain.Physical.LotSplit;
import com.smm.ctrm.domain.Physical.Param4LotPnL;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.Physical.QPRecord;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.domain.Physical.VmContractLot4Combox;
import com.smm.ctrm.domain.Physical.vLot;
import com.smm.ctrm.domain.Report.Lot4FeesOverview;
import com.smm.ctrm.domain.Report.Lot4MTM;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.KeyObj;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.FeeCode;
import com.smm.ctrm.util.Result.InvoiceType;
import com.smm.ctrm.util.Result.LS;
import com.smm.ctrm.util.Result.LeagalnFeeType;
import com.smm.ctrm.util.Result.MT4Storage;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PremiumType;
import com.smm.ctrm.util.Result.PriceTiming;
import com.smm.ctrm.util.Result.SpotType;
import com.smm.ctrm.util.Result.Status;
import com.sun.star.bridge.oleautomation.Decimal;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
@Transactional
public class LotServiceImpl implements LotService {

	private static final Logger logger = Logger.getLogger(LotServiceImpl.class);

	@Autowired
	private HibernateRepository<Lot> repository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private PositionService positionService;

	@Autowired
	private HibernateRepository<Storage> storageRepository;

	@Autowired
	private HibernateRepository<Calendar> calendarRepository;

	@Autowired
	private HibernateRepository<PricingRecord> pricingRecordRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private HibernateRepository<Brand> brandRepository;

	@Autowired
	private HibernateRepository<Bvi2Sm> bvi2SmRepository;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepository;

	@Autowired
	private HibernateRepository<Position> positionRepository;

	@Autowired
	private HibernateRepository<Fund> fundRepository;

	@Autowired
	private HibernateRepository<Pricing> pricingRepository;

	@Autowired
	private HibernateRepository<Legal> legalRepository;

	@Autowired
	private HibernateRepository<Fee> feeRepository;

	@Autowired
	private HibernateRepository<FeeSetup> feeSetupRepository;

	@Autowired
	private HibernateRepository<Reuter> reuterRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityRepository;

	@Autowired
	private HibernateRepository<GlobalSet> globalSetRepository;

	@Autowired
	private HibernateRepository<LotPnL> lotPnLSetRepository;

	@Autowired
	private HibernateRepository<Square> squareRepository;

	@Autowired
	private HibernateRepository<QPRecord> qrRecordRepository;

	@Autowired
	private HibernateRepository<LC> lcRepository;

	@Autowired
	private HibernateRepository<Lot4MTM3> lot4mtm3Repository;

	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<Customer> customerRepository;

	@Autowired
	private HibernateRepository<Lot4Unpriced> lot4UnpricedRepository;

	@Autowired
	private HibernateRepository<SFE> sfeRepository;

	@Autowired
	private HibernateRepository<Dictionary> dictionaryRepository;

	@Autowired
	private HibernateRepository<vLot> vlotRepo;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	private HibernateRepository<Pending> pendingRepo;

	@Autowired
	private HibernateRepository<Position4Broker> position4BrokerRepository;

	@Override
	public ActionResult<String> ConfirmLotQuantityDelivered(Lot lot) {
		lot.setIsQuantityConfirmed(true);
		this.repository.SaveOrUpdate(lot);
		return new ActionResult<>(true, "成功确认实数");
	}

	@Override
	public ActionResult<String> UpdateInvoicFlagOfLots(List<Lot> lots) {

		if (lots != null && lots.size() > 0) {

			lots.forEach(lot -> {
				lot.setIsInvoiced(true);
				this.repository.SaveOrUpdate(lot);
			});
		}

		ActionResult<String> result = new ActionResult<>();

		result.setSuccess(true);
		result.setMessage("开票标志已经完成");

		return result;
	}

	@Override
	public void UpdateLotBalance(Lot lot) {
		// 需要重新取一次
		lot = this.repository.getOneById(lot.getId(), Lot.class);

		int flag = (lot.getSpotDirection().toUpperCase().equals(SpotType.Purchase)) ? -1 : 1;
		BigDecimal actualQuantity = (lot.getIsDelivered())
				? (lot.getQuantityDelivered() != null) ? lot.getQuantityDelivered() : BigDecimal.ZERO
				: lot.getQuantity();

		// 按照小王2013-11-1的要求，如果是均价合同（客户称之为长单合同），那么价格用2位小数、再计算账面金额
		// 但是，如果不是长单合同，则保留原来的做法、就可以了
		BigDecimal price = (lot.getPrice() != null ? lot.getPrice() : BigDecimal.ZERO).setScale(4,
				BigDecimal.ROUND_HALF_UP);

		if (lot.getMajorType() != null
				&& (lot.getPremiumType() != null && (lot.getMajorType().toUpperCase().equals(MajorType.Average)
						|| lot.getPremiumType() == PremiumType.Average))) {
			price = (lot.getPrice() != null ? lot.getPrice() : BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		/**
		 * 已全部点价
		 */
		if (lot.getIsPriced()) {
			lot.setDueBalance(actualQuantity.multiply(price).multiply(new BigDecimal(flag)));

			lot.setLastBalance(lot.getDueBalance()
					.subtract(lot.getPaidBalance() != null ? lot.getPaidBalance() : BigDecimal.ZERO));
			this.repository.SaveOrUpdate(lot);
			return;
		}

		/**
		 * 部分或者全部未点 取得最新的市场价格
		 */

		if (lot.getMajorMarket() == null) {
			return;
		}

		BigDecimal currentMarketPrice = commonService.GetM2MPrice(lot.getMajorMarketId(),
				lot.getMajorMarket().getCode(), lot.getCommodityId(), lot.getSpecId());

		// 现在是在不清楚批次最终价格的情况下
		// 1.全部未点价 已点价数量=0，未点价数量=批次数量
		if (lot.getQuantityPriced() == null || lot.getQuantityPriced().compareTo(BigDecimal.ZERO) == 0) {
			lot.setDueBalance(actualQuantity.multiply(currentMarketPrice).multiply(new BigDecimal(flag)));
			lot.setLastBalance(
					lot.getDueBalance().subtract(lot.getDueBalance() != null ? lot.getDueBalance() : BigDecimal.ZERO));
		} else {

			// 2.部分点价
			BigDecimal a = BigDecimal.ZERO;
			BigDecimal b = BigDecimal.ZERO;
			a = lot.getQuantityPriced().multiply(price).multiply(new BigDecimal(flag));
			b = actualQuantity.subtract(lot.getQuantityPriced() != null ? lot.getQuantityPriced() : BigDecimal.ZERO);
			b = b.multiply(currentMarketPrice).multiply(new BigDecimal(flag));
			lot.setDueBalance(a.add(b));

			lot.setLastBalance(lot.getDueBalance()
					.multiply(lot.getPaidBalance() != null ? lot.getPaidBalance() : BigDecimal.ZERO));
		}
		this.repository.SaveOrUpdate(lot);
	}

	/**
	 * 计算销售合同的盈亏
	 * 
	 * @param lot
	 */
	@Override
	public void CalPnLBySell(Lot lot) {

		if (lot.getSpotDirection() == SpotType.Purchase) {
			return;
		}

		// 1.先再次计算销售合同的单价
		// 才能获得最新销售金额 amountSell
		// BigDecimal amountSell = lot.getPrice()
		// .multiply(lot.getIsDelivered()
		// ? (lot.getQuantityDelivered() != null ? lot.getQuantityDelivered() :
		// BigDecimal.ZERO)
		// : lot.getQuantity());

		// 2.计算采购成本
		BigDecimal amountBuy = BigDecimal.ZERO;
		// 关键点：找到销售出库的商品，是来自哪几个合同。因为来自不同的采购合同，其采购的成本价格是不相同的
		// 2.1 同一个销售合同，可以有多次发货
		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> storages = this.storageRepository.GetQueryable(Storage.class).where(where).toList();
		// amountBuy = storages.Sum(x => (((x.Price != null) ? x.Price :
		// 0)*x.Quantity));
		BigDecimal s = BigDecimal.ZERO;
		for (Storage storage : storages) {
			s = (storage.getPrice() != null ? storage.getPrice() : BigDecimal.ZERO).multiply(storage.getQuantity());
		}
		// 得到PnL
		lot.setPnL(s.subtract(amountBuy));

		// 保存结果
		this.repository.SaveOrUpdate(lot);
	}

	@Override
	public void CalPnLByBuy(Lot lot) {

		if (lot == null)
			return;

		if (lot.getSpotDirection().equals(ActionStatus.SpotType_Sell))
			return;

		// 1.先再次计算采购合同的单价和成本
		BigDecimal amountBuy = BigDecimal.ZERO;
		// 2.再计算销售收入
		BigDecimal amountSell = BigDecimal.ZERO;

		// 得到PnL
		lot.setPnL(amountBuy.subtract(amountSell));

		// 保存结果
		this.repository.SaveOrUpdate(lot);

	}

	@Override
	public ActionResult<String> ModifyExecuted4Initiated(Lot lot) {

		this.repository.SaveOrUpdate(lot);

		return new ActionResult<>(true, "修改成功");
	}

	@Override
	public ActionResult<String> MarkColor(Lot lot) {

		this.repository.SaveOrUpdate(lot);

		return new ActionResult<>(true, "成功加上颜色标注");
	}

	@Override
	public ActionResult<String> ClearColor(Lot lot) {

		if (lot == null)
			return new ActionResult<>(false, "lot is null");

		lot.setMarkColor(0);

		this.repository.SaveOrUpdate(lot);

		return new ActionResult<>(true, "成功加上颜色标注");
	}

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Lot.class);
	}

	/**
	 * 通用：保存批次，没有特殊处理
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public ActionResult<Lot> SaveLot(Lot lot) {

		try {
			/**
			 * 格式化均价点介的日期、格式化
			 */
			lot.setMajorStartDate(DateUtil.doSFormatDate(lot.getMajorStartDate(), "yyyyMMdd"));
			lot.setMajorEndDate(DateUtil.doSFormatDate(lot.getMajorEndDate(), "yyyyMMdd"));
			lot.setPremiumStartDate(DateUtil.doSFormatDate(lot.getPremiumStartDate(), "yyyyMMdd"));
			lot.setPremiumEndDate(DateUtil.doSFormatDate(lot.getPremiumEndDate(), "yyyyMMdd"));
			lot.setPricingType(String.format("%s+%s", lot.getMajorType(), lot.getPremiumType()));

			Contract contract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);
			if (contract != null) {
				/**
				 * 作为非常重要的冗余,不可删除
				 */
				lot.setLegalId(contract.getLegalId());
				lot.setCustomerId(contract.getCustomerId());
				lot.setCurrency(contract.getCurrency());
				lot.setHeadNo(contract.getHeadNo());
				lot.setSerialNo(contract.getSerialNo());
				lot.setPrefixNo(contract.getPrefix());
				lot.setStatus(contract.getStatus());
				lot.setSpotDirection(contract.getSpotDirection());
				lot.setCommodityId(contract.getCommodityId());
				lot.setProduct(contract.getProduct());
			}

			this.repository.SaveOrUpdate(lot);
			ActionResult<Lot> tempVar = new ActionResult<Lot>();
			tempVar.setSuccess(true);
			tempVar.setMessage(MessageCtrm.SaveSuccess);
			tempVar.setData(lot);
			return tempVar;
		} catch (Exception ex) {
			ActionResult<Lot> tempVar2 = new ActionResult<Lot>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 保存：临单的合同头 + 批次
	 */
	@Override
	public ActionResult<Lot> SaveContractProvisional(Lot lot) {

		/**
		 * 检查lot.Contract不可为空，且类型必须是临单
		 * 
		 */
		if (lot.getContract() == null || lot.getContract().getIsProvisional() == false) {
			ActionResult<Lot> tempVar = new ActionResult<Lot>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		/**
		 * 格式化均价点介的日期、格式化lot.PricingType
		 */

		lot.setMajorStartDate(DateUtil.doSFormatDate(lot.getMajorStartDate(), "yyyyMMdd"));
		lot.setMajorEndDate(DateUtil.doSFormatDate(lot.getMajorEndDate(), "yyyyMMdd"));
		lot.setPremiumStartDate(DateUtil.doSFormatDate(lot.getPremiumStartDate(), "yyyyMMdd"));
		lot.setPremiumEndDate(DateUtil.doSFormatDate(lot.getPremiumEndDate(), "yyyyMMdd"));
		lot.setPricingType(String.format("%s+%s", lot.getMajorType(), lot.getPremiumType()));

		try {
			/**
			 * 检查重名。合同编号不可重复。
			 */

			DetachedCriteria dc = DetachedCriteria.forClass(Contract.class);
			dc.add(Restrictions.and(Restrictions.eq("Id", lot.getContractId()),
					Restrictions.eq("HeadNo", lot.getContract().getHeadNo())));
			Contract existContract = this.contractRepository.GetQueryable(Contract.class).where(dc).firstOrDefault();
			if (existContract != null) {
				ActionResult<Lot> tempVar2 = new ActionResult<Lot>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("合同编号重复");
				return tempVar2;
			}

			/**
			 * 保存合同和批次
			 */

			if (lot.getContractId() == null) {
				lot.getContract().setStatus(Status.Draft);
				lot.getContract().setCreatedAt(new Date());
				lot.getContract().setCreatedBy(lot.getCreatedBy());
				lot.getContract().setCreatedId(lot.getCreatedId());

				/**
				 * 如果是新增，则首先新增合同头，并且返回ContractId
				 */
				// String contractId =
				// this.contractRepository.SaveOrUpdateRetrunId(lot.getContract());
				/**
				 * 作为非常重要的冗余,不可删除
				 */
				lot.setLegalId(lot.getContract().getLegalId());
				lot.setCustomerId(lot.getContract().getCustomerId());
				lot.setCurrency(lot.getContract().getCurrency());
				lot.setHeadNo(lot.getContract().getHeadNo());
				lot.setSerialNo(lot.getContract().getSerialNo());
				lot.setPrefixNo(lot.getContract().getPrefix());
				lot.setStatus(lot.getContract().getStatus());
				lot.setSpotDirection(lot.getContract().getSpotDirection());
				lot.setCommodityId(lot.getContract().getCommodityId());
				lot.setProduct(lot.getContract().getProduct());
				lot.setContractId(lot.getContract().getId());
				this.repository.SaveOrUpdate(lot);
			} else {
				lot.getContract().setUpdatedAt(new Date());
				lot.getContract().setUpdatedBy(lot.getCreatedBy());
				lot.getContract().setUpdatedId(lot.getCreatedId());
				this.contractRepository.SaveOrUpdate(lot.getContract());

				/**
				 * 作为非常重要的冗余,不可删除
				 */
				lot.setLegalId(lot.getContract().getLegalId());
				lot.setCustomerId(lot.getContract().getCustomerId());
				lot.setCurrency(lot.getContract().getCurrency());
				lot.setHeadNo(lot.getContract().getHeadNo());
				lot.setStatus(lot.getContract().getStatus());
				lot.setSpotDirection(lot.getContract().getSpotDirection());
				lot.setCommodityId(lot.getContract().getCommodityId());
				lot.setProduct(lot.getContract().getProduct());
				lot.setContractId(lot.getContract().getId());
				this.repository.SaveOrUpdate(lot);
			}

			// 预先处理点价天数，每天点价数量
			if (lot.getMajorType().equals(MajorType.Average) || lot.getPremiumType().equals(PremiumType.Average)) {
				UpdateQuantityPerDay(lot);
				this.repository.SaveOrUpdate(lot);
			}

			// 按CRRC兼顾标准版产品的要求，同时生成点价记录
			if (lot.getMajorType() == MajorType.Fix) {

				DetachedCriteria where = DetachedCriteria.forClass(PricingRecord.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				PricingRecord pr = this.pricingRecordRepository.GetQueryable(PricingRecord.class).where(where)
						.firstOrDefault();

				PricingRecord pricingRecord = pr != null ? pr : new PricingRecord();
				pricingRecord.setPriceTiming(PriceTiming.Onschedule);
				pricingRecord.setLotId(lot.getId());
				pricingRecord.setContractId(lot.getContractId());
				pricingRecord.setMajorType(lot.getMajorType());
				pricingRecord.setQuantity(lot.getQuantity());
				pricingRecord.setTradeDate(lot.getContract().getTradeDate());
				pricingRecord.setMajorMarketId(null);
				pricingRecord.setMajorBasis(lot.getMajorBasis());
				pricingRecord.setMajorDays(lot.getMajorDays());
				pricingRecord.setMajorEndDate(lot.getMajorEndDate());
				pricingRecord.setMajorStartDate(lot.getMajorStartDate());
				pricingRecord.setMajorType(lot.getMajorBasis());
				pricingRecord.setPremium(lot.getPremium());
				pricingRecord.setPremiumBasis(lot.getPremiumBasis());
				pricingRecord.setPremiumDays(lot.getPremiumDays());
				pricingRecord.setPremiumEndDate(lot.getPremiumEndDate());
				pricingRecord.setPremiumMarketId(lot.getPremiumMarketId());
				pricingRecord.setPremiumStartDate(lot.getPremiumStartDate());
				pricingRecord.setPremiumType(lot.getPremiumType());
				pricingRecord.setQtyPerPremiumDay(lot.getQtyPerPremiumDay());
				this.pricingRecordRepository.SaveOrUpdate(pricingRecord);
			}

			// 不要删除以下的代码
			// UpdateLotPrice(lot);
			// UpdateLotBalance(lot);
			// CustomerBalanceService.UpdateCustomerBalance(lot.Contract.CustomerId,
			// lot.Contract.LegalId, lot.Contract.CommodityId);

			// 先保存新的"产品名称"
			Product product = new Product();
			product.setCommodityId(lot.getCommodityId());
			product.setName(lot.getProduct());
			product.setIsHidden(false);
			productService.AddProduct(product);

			// 重新取值，保证Version是最新的
			DetachedCriteria where2 = DetachedCriteria.forClass(Lot.class);
			where2.add(Restrictions.eq("Id", lot.getId()));
			lot = this.repository.GetQueryable(Lot.class).where(where2).firstOrDefault();
		} catch (Exception ex) {
			ActionResult<Lot> tempVar4 = new ActionResult<Lot>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
		ActionResult<Lot> tempVar5 = new ActionResult<Lot>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage(MessageCtrm.SaveSuccess);
		tempVar5.setData(lot);
		return tempVar5;
	}

	/**
	 * 保存：长单的批次
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public ActionResult<Lot> SaveLotOfContractRegular(Lot lot) {

		/**
		 * 检查lot.Contract不可为空，且类型必须是长单
		 */
		if (lot.getContract() == null || lot.getContract().getIsProvisional() == true) {
			ActionResult<Lot> tempVar = new ActionResult<Lot>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		/**
		 * 格式化均价点介的日期、格式化lot.PricingType
		 */
		lot.setMajorStartDate(DateUtil.doSFormatDate(lot.getMajorStartDate(), "yyyyMMdd"));
		lot.setMajorEndDate(DateUtil.doSFormatDate(lot.getMajorEndDate(), "yyyyMMdd"));
		lot.setPremiumStartDate(DateUtil.doSFormatDate(lot.getPremiumStartDate(), "yyyyMMdd"));
		lot.setPremiumEndDate(DateUtil.doSFormatDate(lot.getPremiumEndDate(), "yyyyMMdd"));
		lot.setPricingType(String.format("%s+%s", lot.getMajorType(), lot.getPremiumType()));

		try {
			/**
			 * 只需要检查批次号是否存在重复 不需要在此处检查合同号是否存在重复，因为保存合同头的方法是在ContractService
			 * 检查批次号是否存在重复
			 */

			/**
			 * 检查重名。合同编号不可重复。
			 */

			DetachedCriteria dc = DetachedCriteria.forClass(Lot.class);
			dc.add(Restrictions.and(Restrictions.eq("Id", lot.getId()),
					Restrictions.eq("ContractId", lot.getContractId()), Restrictions.eq("LotNo", lot.getLotNo())

			));
			Lot existLot = this.repository.GetQueryable(Lot.class).where(dc).firstOrDefault();
			if (existLot != null) {
				ActionResult<Lot> tempVar2 = new ActionResult<Lot>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("批次编号重复");
				return tempVar2;
			}

			/**
			 * 检查数量关系是否合法
			 */
			// var lotQuantitySum = (from l in
			// getRepository().<Lot>GetQueryable() where l.ContractId ==
			// lot.ContractId && l.Id != lot.Id select l.Quantity).Sum() +
			// lot.Quantity;

			DetachedCriteria dc2 = DetachedCriteria.forClass(Lot.class);
			dc2.add(Restrictions.and(Restrictions.eq("Id", lot.getId()),
					Restrictions.eq("ContractId", lot.getContractId())

			));
			List<Lot> lots = this.repository.GetQueryable(Lot.class).where(dc2).toList();
			BigDecimal lotQuantitySum = BigDecimal.ZERO;
			for (Lot lot2 : lots) {
				lotQuantitySum = lotQuantitySum.add(lot2.getQuantity());
			}
			lotQuantitySum = lotQuantitySum.add(lot.getQuantity());

			if (lotQuantitySum.compareTo(lot.getContract().getQuantity()) == 1) {
				ActionResult<Lot> tempVar3 = new ActionResult<Lot>();
				tempVar3.setSuccess(false);
				tempVar3.setMessage("批次数量之和不可以大于合同数量");
				return tempVar3;
			}

			// 先保存新的"产品名称"
			Product product = new Product();
			product.setCommodityId(lot.getContract().getCommodityId());
			product.setName(lot.getContract().getProduct());
			productService.Save(product);

			/**
			 * 早先bvi - sm的同步生成批次时，存在lot.FullNo编号中的买卖BS方向错误的问题
			 */
			lot.setFullNo(String.format("%s/%s", lot.getContract().getHeadNo(), lot.getLotNo()));
			lot.setIsIniatiated(lot.getContract().getIsIniatiated());

			/**
			 * 作为非常重要的冗余,不可删除
			 */
			lot.setLegalId(lot.getContract().getLegalId());
			lot.setCustomerId(lot.getContract().getCustomerId());
			lot.setCurrency(lot.getContract().getCurrency());
			lot.setHeadNo(lot.getContract().getHeadNo());
			lot.setSerialNo(lot.getContract().getSerialNo());
			lot.setStatus(lot.getContract().getStatus());
			lot.setSpotDirection(lot.getContract().getSpotDirection());
			lot.setCommodityId(lot.getContract().getCommodityId());
			lot.setProduct(lot.getContract().getProduct());

			/**
			 * 批次原始数量
			 */

			ReCalculateLotMarks(lot);

			// 预先处理点价天数，每天点价数量
			if (lot.getMajorType() == MajorType.Average || lot.getPremiumType() == PremiumType.Average) {
				UpdateQuantityPerDay(lot);
			}

			this.repository.SaveOrUpdate(lot); // 保存批次

			UpdateQuantityOriginal(lot);

			/**
			 * 更新合同中的数量汇总
			 */
			Contract existContract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);

			DetachedCriteria dc3 = DetachedCriteria.forClass(Lot.class);
			dc3.add(Restrictions.eq("ContractId", existContract.getId()));

			List<Lot> lots2 = this.repository.GetQueryable(Lot.class).where(dc3).toList();
			BigDecimal sum = BigDecimal.ZERO;
			for (Lot lot3 : lots2) {
				sum = sum.add(lot3.getQuantity());
			}
			existContract.setQuantityOfLots(sum);
			this.contractRepository.SaveOrUpdate(existContract);

		} catch (Exception ex) {
			ActionResult<Lot> tempVar4 = new ActionResult<Lot>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}

		ActionResult<Lot> tempVar5 = new ActionResult<Lot>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage(MessageCtrm.SaveSuccess);
		tempVar5.setData(lot);
		return tempVar5;
	}

	/**
	 * 以下是针对BVI的销售和采购的特殊业务的处理
	 * 
	 * @param sellLot
	 * @return
	 */
	@Override
	public ActionResult<Lot> SaveLot4BviToSm(Lot sellLot) {

		try {
			/**
			 * 检查批次号是否存在重复
			 */
			DetachedCriteria lotWhere = DetachedCriteria.forClass(Lot.class);
			lotWhere.add(Restrictions.eq("Id", sellLot.getId()));
			lotWhere.add(Restrictions.eq("ContractId", sellLot.getContractId()));
			lotWhere.add(Restrictions.eq("LotNo", sellLot.getLotNo()));
			Lot existLot = this.repository.GetQueryable(Lot.class).where(lotWhere).firstOrDefault();
			if (existLot != null) {
				ActionResult<Lot> tempVar = new ActionResult<Lot>();
				tempVar.setSuccess(false);
				tempVar.setMessage("批次编号重复");
				return tempVar;
			}

			/**
			 * BVI-SM属特殊业务，依赖于已经存在下方成的设置
			 */
			Bvi2Sm bvi2Sm = this.bvi2SmRepository.GetQueryable(Bvi2Sm.class).firstOrDefault();

			if (bvi2Sm == null) {
				ActionResult<Lot> tempVar2 = new ActionResult<Lot>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("bvi2sm is null");
				return tempVar2;
			}

			/**
			 * 格式化均价点价的日期、格式化
			 */
			sellLot.setMajorStartDate(DateUtil.doSFormatDate(sellLot.getMajorStartDate(), "yyyyMMdd"));
			sellLot.setMajorEndDate(DateUtil.doSFormatDate(sellLot.getMajorEndDate(), "yyyyMMdd"));
			sellLot.setPremiumStartDate(DateUtil.doSFormatDate(sellLot.getPremiumStartDate(), "yyyyMMdd"));
			sellLot.setPremiumEndDate(DateUtil.doSFormatDate(sellLot.getPremiumEndDate(), "yyyyMMdd"));
			sellLot.setPricingType(String.format("%s+%s", sellLot.getMajorType(), sellLot.getPremiumType()));

			/**
			 * 检查数量关系是否合法
			 */
			// var lotQuantitySum = (from l in
			// getRepository().<Lot>GetQueryable() where l.ContractId ==
			// sellLot.ContractId && l.Id != sellLot.Id select l.Quantity).Sum()
			// + sellLot.Quantity;
			DetachedCriteria lot2Where = DetachedCriteria.forClass(Lot.class);
			lot2Where.add(Restrictions.eq("Id", sellLot.getId()));
			lot2Where.add(Restrictions.eq("ContractId", sellLot.getContractId()));
			List<Lot> Lots = this.repository.GetQueryable(Lot.class).where(lot2Where).toList();
			BigDecimal lotQuantitySum = BigDecimal.ZERO;
			for (Lot lot : Lots) {
				lotQuantitySum = lotQuantitySum.add(lot.getQuantity());
			}
			lotQuantitySum = lotQuantitySum.add(sellLot.getQuantity());

			if (lotQuantitySum.compareTo(sellLot.getContract().getQuantity()) == 1) {
				ActionResult<Lot> tempVar3 = new ActionResult<Lot>();
				tempVar3.setSuccess(false);
				tempVar3.setMessage("批次数量之和不可以大于合同数量");
				return tempVar3;
			}

			Contract sellContract = this.contractRepository.getOneById(sellLot.getContractId(), Contract.class);

			/// #region 作为非常重要的冗余,不可删除
			sellLot.setLegalId(sellContract.getLegalId());
			sellLot.setCustomerId(sellContract.getCustomerId());
			sellLot.setCurrency(sellContract.getCurrency());
			sellLot.setHeadNo(sellContract.getHeadNo());
			sellLot.setHeadNo(sellContract.getHeadNo());
			sellLot.setSerialNo(sellContract.getSerialNo());
			sellLot.setStatus(sellContract.getStatus());
			sellLot.setSpotDirection(sellContract.getSpotDirection());
			sellLot.setCommodityId(sellContract.getCommodityId());
			sellLot.setProduct(sellContract.getProduct());
			sellLot.setFullNo(String.format("%s/$s", sellLot.getHeadNo(), sellLot.getLotNo()));
			sellLot.setIsIniatiated(sellContract.getIsIniatiated());

			ReCalculateLotMarks(sellLot);

			if (sellLot.getId() == null) // 这是新增的情况
			{
				// 保存销售合同并返回SellId
				String sellId = this.repository.SaveOrUpdateRetrunId(sellLot);
				// 预先处理点价天数，每天点价数量
				if (sellLot.getMajorType().equals(MajorType.Average)
						|| sellLot.getPremiumType().equals(PremiumType.Average)) {
					UpdateQuantityPerDay(sellLot);
					this.repository.SaveOrUpdate(sellLot);
				}

				/// #region 如果是BVI-SM的销售合同，则创建对应的SM采购批次
				if (sellContract.getIsInternal() && sellContract.getLegalId() == bvi2Sm.getBviLegalId()) {
					Contract purchaseContract = this.contractRepository.getOneById(sellContract.getCounterpartId(),
							Contract.class);

					if (purchaseContract == null) {
						ActionResult<Lot> tempVar4 = new ActionResult<Lot>();
						tempVar4.setSuccess(false);
						tempVar4.setMessage("CounterpartId is null");
						return tempVar4;
					}

					Lot purchaseLot = new Lot();

					BeanUtils.copyProperties(sellLot, purchaseLot);
					/**
					 * setBrands 和C#写法不一样，需要注意
					 */
					purchaseLot.setBrands(sellLot.getBrands());
					purchaseLot.setId(null);
					purchaseLot.setContractId(purchaseContract.getId());
					purchaseLot.setCounterpartId(sellId); // 注意这个赋值
					purchaseLot.setLegalId(purchaseContract.getLegalId());
					purchaseLot.setCustomerId(purchaseContract.getCustomerId());
					purchaseLot.setCurrency(purchaseContract.getCurrency());
					purchaseLot.setHeadNo(purchaseContract.getHeadNo());
					purchaseLot.setHeadNo(purchaseContract.getHeadNo());
					purchaseLot.setSerialNo(purchaseContract.getSerialNo());
					purchaseLot.setStatus(purchaseContract.getStatus());
					purchaseLot.setSpotDirection(purchaseContract.getSpotDirection());
					purchaseLot.setCommodityId(purchaseContract.getCommodityId());
					purchaseLot.setProduct(purchaseContract.getProduct());
					purchaseLot.setFullNo(String.format("%/%s", purchaseLot.getHeadNo(), purchaseLot.getLotNo()));
					purchaseLot.setIsIniatiated(purchaseContract.getIsIniatiated());

					// 保存采购合同并返回PurchaseId
					String purchaseLotId = this.repository.SaveOrUpdateRetrunId(purchaseLot);

					// 更新采购合同中的数量汇总(QuantityOfLot)
					// 需要再次获取
					purchaseContract = this.contractRepository.getOneById(purchaseLot.getContractId(), Contract.class);

					// var purchaseLots = getRepository().<Lot>GetList().Where(x
					// => x.ContractId == purchaseContract.Id).ToList();

					DetachedCriteria lotDc = DetachedCriteria.forClass(Lot.class);
					lotDc.add(Restrictions.eq("ContractId", purchaseContract.getId()));
					/**
					 * 取一个list然后统计数量。优化：直接在数据库统计
					 */
					List<Lot> purchaseLots = this.repository.GetQueryable(Lot.class).where(lotDc).toList();
					BigDecimal x = BigDecimal.ZERO;
					for (Lot lot : purchaseLots) {
						x = x.add(lot.getQuantity());
					}

					purchaseContract.setQuantityOfLots(x);

					this.contractRepository.SaveOrUpdate(purchaseContract);

					// 保存销售批次的CounterpartId
					sellLot = this.repository.getOneById(sellId, Lot.class);
					sellLot.setCounterpartId(purchaseLotId);
					this.repository.SaveOrUpdate(sellLot);
				}
			} else // 这是修改的情况
			{
				this.repository.SaveOrUpdate(sellLot);
				// 预先处理点价天数，每天点价数量
				if (sellLot.getMajorType().equals(MajorType.Average)
						|| sellLot.getPremiumType().equals(PremiumType.Average)) {
					UpdateQuantityPerDay(sellLot);
					this.repository.SaveOrUpdate(sellLot);
				}
			}
			UpdateQuantityOriginal(sellLot);
			// 更新销售合同中的数量汇总(QuantityOfLot)
			// var sellLots = getRepository().<Lot>GetList().Where(x =>
			// x.ContractId == sellContract.Id).ToList();
			// sellContract.QuantityOfLots = sellLots.Sum(x => x.Quantity);
			DetachedCriteria uplotDc = DetachedCriteria.forClass(Lot.class);
			uplotDc.add(Restrictions.eq("ContractId", sellContract.getId()));
			/**
			 * 取一个list然后统计数量。优化：直接在数据库统计
			 */
			List<Lot> sellLots = this.repository.GetQueryable(Lot.class).where(uplotDc).toList();
			BigDecimal x = BigDecimal.ZERO;
			for (Lot lot : sellLots) {
				x = x.add(lot.getQuantity());
			}

			this.contractRepository.SaveOrUpdate(sellContract);

			DetachedCriteria querylotDc = DetachedCriteria.forClass(Lot.class);
			querylotDc.add(Restrictions.eq("Id", sellLot.getId()));

			// 重新取值，保证Version是最新的
			sellLot = this.repository.GetQueryable(Lot.class).where(querylotDc).firstOrDefault();
			ActionResult<Lot> tempVar5 = new ActionResult<Lot>();
			tempVar5.setSuccess(true);
			tempVar5.setMessage(MessageCtrm.SaveSuccess);
			tempVar5.setData(sellLot);
			return tempVar5;
		} catch (Exception ex) {
			ActionResult<Lot> tempVar6 = new ActionResult<Lot>();
			tempVar6.setSuccess(false);
			tempVar6.setMessage(ex.getMessage());
			return tempVar6;
		}
	}

	@Override
	public ActionResult<String> DeleteContractProvisional(String lotId, String userId) {

		try {

			Lot lot = this.repository.getOneById(lotId, Lot.class);

			if (lot == null)
				throw new Exception("lot is null");

			if (!lot.getCreatedId().equals(userId))
				throw new Exception("不可以删除他人创建的记录");

			this.repository.PhysicsDelete(lotId, Lot.class);
			this.contractRepository.PhysicsDelete(lot.getContractId(), Contract.class);

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}

	}

	/**
	 * 删除长单合同的某个批次
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public ActionResult<String> DeleteLotOfContractRegular(String id) {

		// 更新批次
		Lot lot = this.repository.getOneById(id, Lot.class);
		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		Invoice existInvoice = this.invoiceRepository.GetQueryable(Invoice.class).where(where).firstOrDefault();

		if (existInvoice != null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("该批次已经存在发票，不允许删除！");
			return tempVar;
		}

		DetachedCriteria whereStorage = DetachedCriteria.forClass(Storage.class);
		whereStorage.add(Restrictions.eq("LotId", lot.getId()));
		whereStorage.add(Restrictions.eq("IsOut", true));
		Storage storageIsOut = this.storageRepository.GetQueryable(Storage.class).where(whereStorage).firstOrDefault();

		if (storageIsOut != null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("该批次的交付明细已经被下一个流程接收，不允许删除！");
			return tempVar2;
		}
		// if (lot.getIsSplitted() && lot.getSplitFromId() != null) {
		// // 如果删除的是拆出来的批次
		// Lot originalLot = this.repository.getOneById(lot.getSplitFromId(),
		// Lot.class);
		// originalLot.setQuantity(originalLot.getQuantity().add(lot.getQuantity()));
		// ReCalculateLotMarks(originalLot);
		// DetachedCriteria whereLot = DetachedCriteria.forClass(Lot.class);
		// whereLot.add(Restrictions.eq("Id", lot.getId()));
		// whereLot.add(Restrictions.eq("SplitFromId", lot.getSplitFromId()));
		//
		// Lot existOtherSplit =
		// this.repository.GetQueryable(Lot.class).where(whereLot).firstOrDefault();
		//
		// if (existOtherSplit == null) {
		// originalLot.setIsSourceOfSplitted(false);
		// }
		// this.repository.SaveOrUpdate(originalLot);
		// } else {
		//
		// DetachedCriteria whereLot = DetachedCriteria.forClass(Lot.class);
		// whereLot.add(Restrictions.eq("SplitFromId", id));
		//
		// Lot isSpanOff =
		// this.repository.GetQueryable(Lot.class).where(whereLot).firstOrDefault();
		//
		// if (isSpanOff != null) {
		// ActionResult<String> tempVar3 = new ActionResult<String>();
		// tempVar3.setSuccess(false);
		// tempVar3.setMessage("该批次已被分拆，请先删除拆出的批次！");
		// return tempVar3;
		// }
		// }

		// 删除相关的业务数据

		DeleteDataLotRelated(lot.getId());
		this.repository.PhysicsDelete(id, Lot.class);

		// 更新合同中的数量汇总
		Contract existContract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);

		// existContract.QuantityOfLots = (from l in
		// getRepository().<Lot>GetQueryable() where l.ContractId ==
		// existContract.Id select l.Quantity).Sum();
		Criteria sumLot = this.repository.CreateCriteria(Lot.class);
		sumLot.add(Restrictions.eq("ContractId", existContract.getId()));
		sumLot.setProjection(Projections.sum("Quantity"));
		Object obj = sumLot.uniqueResult();
		if (obj == null) {
			existContract.setQuantityOfLots(new BigDecimal("0"));
		} else {
			existContract.setQuantityOfLots(new BigDecimal(obj.toString()));
		}
		this.contractRepository.SaveOrUpdate(existContract); 
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public Contract GetContractById(String contractId) {

		return this.contractRepository.getOneById(contractId, Contract.class);
	}

	@Override
	public ActionResult<Lot> GetById(String id) {

		Lot lot = this.repository.getOneById(id, Lot.class);
		if (lot != null) {
			lot = commonService.SimplifyData(lot);
		}
		return new ActionResult<>(true, "success", lot);
	}

	@Override
	public List<Lot> GetListById(Criteria criteria) {

		List<Lot> listLot = this.repository.GetList(criteria);
		return listLot;
	}

	@Override
	public ActionResult<Lot> LotViewById(String id) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("Id", id));
		Lot obj = this.repository.GetQueryable(Lot.class).where(where).firstOrDefault();

		if (obj != null) {
			// obj.setSpecName(obj.getSpec().getName());
			// obj.setOriginName(obj.getOrigin().getName());
			obj.setUnit(obj.getCommodity().getUnit());
			/**
			 * warning
			 * 
			 * obj = obj.<Lot>Copy(); C#是为了清理对象
			 */
			DetachedCriteria dcFund = DetachedCriteria.forClass(Fund.class);
			dcFund.add(Restrictions.eq("LotId", obj.getId()));
			List<Fund> fundList = this.fundRepository.GetQueryable(Fund.class).where(dcFund).toList();
			obj.setFunds(commonService.SimplifyDataFundList(fundList));

			DetachedCriteria dcStorage = DetachedCriteria.forClass(Storage.class);
			dcStorage.add(Restrictions.eq("LotId", obj.getId()));
			dcStorage.add(Restrictions.eq("IsHidden", false));
			List<Storage> storageList = this.storageRepository.GetQueryable(Storage.class).where(dcStorage).toList();
			obj.setStorages(commonService.SimplifyDataStorageList(storageList));

			DetachedCriteria dcInvoice = DetachedCriteria.forClass(Invoice.class);
			dcInvoice.add(Restrictions.eq("LotId", obj.getId()));
			List<Invoice> invoiceList = this.invoiceRepository.GetQueryable(Invoice.class).where(dcInvoice).toList();
			obj.setInvoices(commonService.SimplifyDataInvoiceList(invoiceList));

			DetachedCriteria dcPricingRecord = DetachedCriteria.forClass(PricingRecord.class);
			dcPricingRecord.add(Restrictions.eq("LotId", obj.getId()));
			List<PricingRecord> prList = this.pricingRecordRepository.GetQueryable(PricingRecord.class)
					.where(dcPricingRecord).toList();
			obj.setPricingRecords(commonService.SimplifyDataPricingRecordList(prList));

			DetachedCriteria dcPosition = DetachedCriteria.forClass(Position.class);
			dcPosition.add(Restrictions.eq("LotId", obj.getId()));
			List<Position> positionList = this.positionRepository.GetQueryable(Position.class).where(dcPosition)
					.toList();
			obj.setPositions(commonService.SimplifyDataPositionList(positionList));

			DetachedCriteria dcPricings = DetachedCriteria.forClass(Pricing.class);
			dcPricings.add(Restrictions.eq("LotId", obj.getId()));
			List<Pricing> pricingList = this.pricingRepository.GetQueryable(Pricing.class).where(dcPricings).toList();
			obj.setPricings(commonService.SimplifyDataPricingList(pricingList));
			List<Position> positions = obj.getPositions();
			// if (obj.getIsSplitted()) // 拆分批次使用原始批次头寸计算
			// {
			// DetachedCriteria dclotOrginal =
			// DetachedCriteria.forClass(Lot.class);
			// dclotOrginal.add(Restrictions.eq("Id", obj.getSplitFromId()));
			// Lot lotOrginal =
			// this.repository.GetQueryable(Lot.class).where(dclotOrginal).firstOrDefault();
			// if (lotOrginal != null) {
			// obj.setQuantityHedged(lotOrginal.getQuantityHedged());
			// obj.setQuantityPriced(lotOrginal.getQuantityPriced());
			// }
			// if (lotOrginal != null) {
			// DetachedCriteria dcPosition2 =
			// DetachedCriteria.forClass(Position.class);
			// dcPosition2.add(Restrictions.eq("LotId", lotOrginal.getId()));
			// List<Position> positionList2 =
			// this.positionRepository.GetQueryable(Position.class)
			// .where(dcPosition2).toList();
			// positions =
			// commonService.SimplifyDataPositionList(positionList2);
			// }
			// }
			obj.setStorages(commonService.FuturesSpread(obj, obj.getStorages(), positions)); // 计算调期成本（如右键功能增加更新价格此处可不需要）
			//去掉死循环
			if(obj.getPricingRecords() != null){
				for (PricingRecord pr : obj.getPricingRecords()) {
					pr.setLot(null);
					if(pr.getPricing()!=null) pr.getPricing().setLot(null);
				} 
			}
			if(obj.getFunds() != null) {
				for(Fund fund : obj.getFunds()) {
					if(fund.getLot() != null) {
						fund.setLot(null);
					}
				}
			}
		}
		ActionResult<Lot> tempVar = new ActionResult<Lot>();
		tempVar.setSuccess(true);
		tempVar.setData(obj);
		return tempVar;
	}

	@Override
	public ActionResult<List<Lot>> GetLotsByLotId(String id) {

		Lot lot = this.repository.getOneById(id, Lot.class);

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("ContractId", lot.getContractId()));

		List<Lot> lotList = this.repository.GetQueryable(Lot.class).where(where).toList();

		lotList = this.commonService.SimplifyDataLotList(lotList);

		return new ActionResult<>(true, "success", lotList);
	}

	@Override
	public ActionResult<List<Lot>> GetLotsByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("ContractId", contractId));
		where.add(Restrictions.eq("IsHidden", false));

		List<Lot> lotList = this.repository.GetQueryable(Lot.class).where(where).toList();

		lotList = this.commonService.SimplifyDataLotList(lotList);

		return new ActionResult<>(true, "success", lotList);
	}

	@Override
	public ActionResult<List<Lot>> LotsByCustomerId(String customerId) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("ContractId.CustomerId", customerId));
		where.add(Restrictions.eq("IsHidden", false));

		List<Lot> lotList = this.repository.GetQueryable(Lot.class).where(where).toList();

		lotList = this.commonService.SimplifyDataLotList(lotList);

		return new ActionResult<>(true, "success", lotList);
	}

	@Override
	public ActionResult<Lot> GetFirstLotByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("ContractId.Id", contractId));
		where.add(Restrictions.eq("IsHidden", false));

		List<Lot> lotList = this.repository.GetQueryable(Lot.class).where(where).toList();

		Lot lot = null;

		if (lotList != null && lotList.size() > 0)
			lot = lotList.get(0);

		return new ActionResult<>(true, "success", lot);
	}

	/**
	 * 保存批量地发货、出库
	 */
	@Override
	public ActionResult<String> SaveStorageOuts(Lot lot) {

		if (lot == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}
		try {
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", lot.getId()));

			List<Storage> exists = this.storageRepository.GetQueryable(Storage.class).where(where).toList();

			for (Storage storage : exists) {
				Storage counterParty = this.storageRepository.getOneById(storage.getCounterpartyId(), Storage.class);
				counterParty.setCounterpartyId(null);
				this.storageRepository.SaveOrUpdate(counterParty);
				this.storageRepository.PhysicsDelete(storage.getId(), Storage.class);
			}

			for (Storage storageIn : lot.getStorages()) {
				String counterpartyId = storageIn.getId();
				// 更新入库明细的业务标志
				storageIn.setIsOut(true);
				this.storageRepository.SaveOrUpdate(storageIn);

				// 构建从属的出库商品明细记录
				Storage storageOut = storageIn;
				storageOut.setMT(MT4Storage.Make);
				storageOut.setId(null);
				storageOut.setCounterpartyId(counterpartyId);
				storageOut.setLotId(lot.getId());
				storageOut.setCreatedAt(new Date());
				storageOut.setCreatedBy(lot.getUpdatedBy());
				this.storageRepository.SaveOrUpdate(storageOut);
			}

			// var storages = getRepository().<Storage>GetList().Where(x =>
			// x.LotId == lot.Id&&x.IsHidden==false).ToList();

			Criteria where2 = this.storageRepository.CreateCriteria(Storage.class);
			where2.add(Restrictions.eq("LotId", lot.getId()));
			where2.add(Restrictions.eq("IsHidden", false));
			where2.setProjection(Projections.sum("Quantity"));
			// List<Storage>
			// storages=this.storageRepository.GetQueryable(Storage.class).where(where2).toList();

			lot.setQuantityDelivered(new BigDecimal(where2.uniqueResult().toString()));

			List<Lot> lots = commonService.setDelivery4Lot(lot);
			this.repository.SaveOrUpdate(lot);
			commonService.UpdateDeliveryStatus(lots); // 更新拆分批次的交货状态
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(MessageCtrm.SaveSuccess);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 更新批次的已出入库数量、出入库完成标志
	 * 
	 * @param lotId
	 */
	@Override
	public void UpdateLotQuantity(String lotId) {

		Lot lot = this.repository.getOneById(lotId, Lot.class);

		// 更新已出入库的数量和标志
		Criteria where2 = this.storageRepository.CreateCriteria(Storage.class);
		where2.add(Restrictions.eq("LotId", lot.getId()));
		where2.add(Restrictions.eq("IsHidden", false));
		where2.setProjection(Projections.sum("Quantity"));
		lot.setQuantityDelivered(new BigDecimal(where2.uniqueResult().toString()));

		List<Lot> lots = commonService.setDelivery4Lot(lot);

		// 更新已点价的数量和标志
		Criteria where3 = this.pricingRepository.CreateCriteria(Pricing.class);
		where3.add(Restrictions.eq("LotId", lot.getId()));
		where3.setProjection(Projections.sum("Quantity"));
		lot.setQuantityPriced(new BigDecimal(where3.uniqueResult().toString()));

		lot.setIsPriced(commonService.IsPriced4Lot(lot));

		this.repository.SaveOrUpdate(lot);
		commonService.UpdateDeliveryStatus(lots); // 更新拆分批次的交货状态
	}

	/**
	 * 保值：分配头寸
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public ActionResult<String> AllocatePricing(Lot lot) {

		/// #region 参数检查
		if (lot == null || lot.getPricings() == null || lot.getPricings().size() == 0) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("参数错误。");
			return tempVar;
		}
		// 参照c#linq
		List<Pricing> pricings = new ArrayList<>();
		List<Pricing> pricings_f = new ArrayList<>();
		for (Pricing p : lot.getPricings()) {
			Pricing pricing = this.pricingRepository.getOneById(p.getId(), Pricing.class);
			pricings.add(pricing);
		}
		pricings_f = pricings.stream().filter(p -> p.getLotId() != null).collect(Collectors.toList());

		StringBuffer err = new StringBuffer();
		for (Pricing pricing : pricings_f) {
			err.append(pricing.getTradeDate() + " ");
		}
		if (StringUtils.isNotBlank(err.toString())) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(String.format("以下点价已被使用：%s", err));
			return tempVar2;
		}

		try {
			for (Pricing pricing : lot.getPricings()) {
				pricing.setLotId(lot.getId());
				this.pricingRepository.SaveOrUpdate(pricing);
			}

			// 更新批次的点价数量和点价标志
			lot = this.repository.getOneById(lot.getId(), Lot.class);
			Criteria where = this.pricingRepository.CreateCriteria(Pricing.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			where.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule));
			where.setProjection(Projections.sum("Quantity"));
			lot.setQuantityPriced(new BigDecimal(where.uniqueResult().toString()));

			lot.setIsPriced(commonService.IsPriced4Lot(lot));
			this.repository.SaveOrUpdate(lot);

			// 更新合同的点价数量和点价标志
			Contract contract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);
			if (contract != null) {

				Criteria where2 = this.pricingRepository.CreateCriteria(Pricing.class);
				where2.add(Restrictions.eq("ContractId", contract.getId()));
				where2.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule));
				where2.setProjection(Projections.sum("Quantity"));

				contract.setQuantityPriced(new BigDecimal(where2.uniqueResult().toString()));

				contract.setIsPriced(contract.getQuantity() == contract.getQuantityPriced());
				this.contractRepository.SaveOrUpdate(contract);
			}

			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(true);
			tempVar3.setMessage("点价分配成功。");
			return tempVar3;
		} catch (Exception ex) {
			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 保值：移除头寸
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public ActionResult<String> RemovePricing(Lot lot) {

		if (lot == null || lot.getPricings() == null || lot.getPricings().size() == 0) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("参数错误。");
			return tempVar;
		}

		try {
			for (Pricing pricing : lot.getPricings()) {
				pricing.setLotId(null);
				this.pricingRepository.SaveOrUpdate(pricing);
			}

			// 更新批次的点价数量和点价标志
			lot = this.repository.getOneById(lot.getId(), Lot.class);

			Criteria where = this.pricingRepository.CreateCriteria(Pricing.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			where.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule));
			where.setProjection(Projections.sum("Quantity"));
			lot.setQuantityPriced(new BigDecimal(where.uniqueResult().toString()));

			lot.setIsPriced(commonService.IsPriced4Lot(lot));
			this.repository.SaveOrUpdate(lot);

			// 更新合同的点价数量和点价标志
			Contract contract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);
			if (contract != null) {
				Criteria where2 = this.pricingRepository.CreateCriteria(Pricing.class);
				where2.add(Restrictions.eq("ContractId", contract.getId()));
				where2.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule));
				where2.setProjection(Projections.sum("Quantity"));

				contract.setQuantityPriced(new BigDecimal(where2.uniqueResult().toString()));

				contract.setIsPriced(contract.getQuantity() == contract.getQuantityPriced());
				this.contractRepository.SaveOrUpdate(contract);
			}

			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage("移除成功。");
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 保值：分配头寸
	 * 
	 * @param cpPosition4AllocateToLot
	 * @return
	 */
	@Override
	public ActionResult<String> AllocatePosition(CpPosition4AllocateToLot cpPosition4AllocateToLot) {

		if (cpPosition4AllocateToLot == null || cpPosition4AllocateToLot.getLotId() == null
				|| cpPosition4AllocateToLot.getPositions4Allocate() == null
				|| cpPosition4AllocateToLot.getPositions4Allocate().size() == 0) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("参数错误。");
			return tempVar;
		}

		List<Position> positions = new ArrayList<>();
		List<Position> positions_f = new ArrayList<>();
		for (Position p : cpPosition4AllocateToLot.getPositions4Allocate()) {
			Position position = this.positionRepository.getOneById(p.getId(), Position.class);
			positions.add(position);
		}
		positions_f = positions.stream().filter(exist -> exist.getLotId() != null).collect(Collectors.toList());
		StringBuffer err = new StringBuffer();
		for (Position position : positions_f) {
			err.append(position.getOurRef());
		}

		if (StringUtils.isNotBlank(err.toString())) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(String.format("以下头寸已被用于其它的保值：%s", err));
			return tempVar2;
		}

		try {
			Lot lot = this.repository.getOneById(cpPosition4AllocateToLot.getLotId(), Lot.class);

			for (Position position : cpPosition4AllocateToLot.getPositions4Allocate()) {
				Position obj = this.positionRepository.getOneById(position.getId(), Position.class);
				if (obj.getIsCarry()) {
					DetachedCriteria dcCarrys = DetachedCriteria.forClass(Position.class);
					dcCarrys.add(Restrictions.eq("CarryCounterpart", obj.getCarryCounterpart()));
					dcCarrys.add(Restrictions.eq("IsCarry", true));

					List<Position> carrys = this.positionRepository.GetQueryable(Position.class).where(dcCarrys)
							.toList();

					for (Position carry : carrys) {
						carry.setLotId(cpPosition4AllocateToLot.getLotId());
						this.positionRepository.SaveOrUpdate(carry);
					}
				} else {
					obj.setLotId(cpPosition4AllocateToLot.getLotId());
					this.positionRepository.SaveOrUpdate(obj);
				}
			}

			// 更新合同的点价数量和点价标志

			commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());
			commonService.UpdateFuturesSpread(lot.getId()); // 重新计算批次Spread
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(true);
			tempVar3.setMessage("头寸分配成功。");
			return tempVar3;
		} catch (Exception ex) {
			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 保值：移除头寸
	 * 
	 * @param cpPosition4RemoveFromLot
	 * @return
	 */
	@Override
	public ActionResult<String> RemovePosition(CpPosition4RemoveFromLot cpPosition4RemoveFromLot) {
		if (cpPosition4RemoveFromLot == null || cpPosition4RemoveFromLot.getPositions4Remove() == null
				|| cpPosition4RemoveFromLot.getPositions4Remove().size() == 0) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setMessage("参数错误。");
			return tempVar;
		}
		try {
			Lot lot = this.repository.getOneById(cpPosition4RemoveFromLot.getLotId(), Lot.class);

			for (Position position : cpPosition4RemoveFromLot.getPositions4Remove()) {
				Position t = this.positionRepository.getOneById(position.getId(), Position.class);
				if (t.getIsCarry()) {
					DetachedCriteria dcCarrys = DetachedCriteria.forClass(Position.class);
					dcCarrys.add(Restrictions.eq("CarryCounterpart", t.getCarryCounterpart()));
					dcCarrys.add(Restrictions.eq("IsCarry", true));
					List<Position> carrys = this.positionRepository.GetQueryable(Position.class).where(dcCarrys)
							.toList();
					for (Position carry : carrys) {
						carry.setLotId(null);
						this.positionRepository.SaveOrUpdate(carry);
					}
				} else {
					t.setLotId(null);
					this.positionRepository.SaveOrUpdate(t);
				}
			}

			// 更新合同的点价数量和点价标志
			commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());

			commonService.UpdateFuturesSpread(lot.getId()); // 重新计算批次Spread
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage("移除成功。");
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(true);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 批次分拆
	 * 
	 * @param userId
	 * @param cpSplitLot
	 * @return
	 */
	@Override
	public ActionResult<String> SplitLot(String userId, CpSplitLot cpSplitLot) {

		BigDecimal qtySplit = cpSplitLot.getQuantitySplitted();
		Lot originalLot = cpSplitLot.getOriginalLot();

		/**
		 * 数据格式检查
		 */
		if (originalLot == null || originalLot.getIsSettled() || originalLot.getIsAccounted()) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("不符合拆分的条件：记录为空、或已结算、或已会计。");
			return tempVar;
		}

		if (qtySplit.compareTo(BigDecimal.ZERO) == 0 || qtySplit.compareTo(BigDecimal.ZERO) == -1) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("批次拆分的数量必须大于0。");
			return tempVar2;
		}

		if (qtySplit.compareTo(originalLot.getQuantity()) == 0 || qtySplit.compareTo(originalLot.getQuantity()) == 1) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("拆分的数量必须小于原数量。");
			return tempVar3;
		}

		/**
		 * 检查批次号是否存在重复
		 */
		DetachedCriteria dcLot = DetachedCriteria.forClass(Lot.class);
		dcLot.add(Restrictions.eq("ContractId", cpSplitLot.getOriginalLot().getContractId()));
		dcLot.add(Restrictions.eq("LotNo", cpSplitLot.getLotNo()));

		Lot existLot = this.repository.GetQueryable(Lot.class).where(dcLot).firstOrDefault();

		if (existLot != null) {
			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage("批次编号重复");
			return tempVar4;
		}

		try {
			originalLot.setQuantity(originalLot.getQuantity().subtract(qtySplit));
			this.repository.SaveOrUpdate(originalLot);

			Lot theSplit = new Lot();
			BeanUtils.copyProperties(originalLot, theSplit);

			theSplit.setId(null);
			theSplit.setQuantity(qtySplit);
			theSplit.setLotNo(cpSplitLot.getLotNo());
			theSplit.setDocumentNo(cpSplitLot.getDocumentNo());
			this.repository.SaveOrUpdate(theSplit);
		} catch (Exception ex) {
			ActionResult<String> tempVar5 = new ActionResult<String>();
			tempVar5.setSuccess(false);
			tempVar5.setMessage(ex.getMessage());
			return tempVar5;
		}

		ActionResult<String> tempVar6 = new ActionResult<String>();
		tempVar6.setSuccess(true);
		tempVar6.setMessage("拆分成功。");
		return tempVar6;
	}

	@Override
	public ActionResult<String> DeleteFeesByLotId(String lotId) {

		if (StringUtils.isEmpty(lotId))
			return new ActionResult<>(false, "删除相关预估费用信息失败");

		String sql = String.format("Delete from [Physical].Fee where LotId = '{0}' and IsEliminated =0 ", lotId);
		sql = sql + "and not(FeeCode = '10' and AmountDone Is Not Null)";

		this.repository.ExecuteNonQuery(sql);

		return new ActionResult<>(true, "删除相关预估费用信息成功");
	}

	@Override
	public ActionResult<String> GenerateFees(Lot curLot) {

		DetachedCriteria dcLegal = DetachedCriteria.forClass(Legal.class);
		dcLegal.add(Restrictions.eq("Id", curLot.getLegalId()));
		Legal legal = this.legalRepository.GetQueryable(Legal.class).where(dcLegal).firstOrDefault();
		if (legal == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("该批次无抬头信息");
			return tempVar;
		}
		DetachedCriteria dcContract = DetachedCriteria.forClass(Contract.class);
		dcContract.add(Restrictions.eq("Id", curLot.getContractId()));
		Contract curContract = this.contractRepository.GetQueryable(Contract.class).where(dcContract).firstOrDefault();
		if (curContract == null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("该批次订单信息不存在");
			return tempVar2;
		}

		List<String> feeTypes = LeagalnFeeType.getSmSFeeType(); // 不再通过类型区分，只要存在相关的费率配置就进行计算
		int iSerial = 0;
		for (String feeType : feeTypes) {
			iSerial++;
			String type = feeType;

			DetachedCriteria dcFee = DetachedCriteria.forClass(Fee.class);
			dcFee.add(Restrictions.eq("LotId", curLot.getId()));
			dcFee.add(Restrictions.eq("FeeCode", type));
			Fee exist = this.feeRepository.GetQueryable(Fee.class).where(dcFee).firstOrDefault();
			StringBuilder rate = new StringBuilder();
			List<FeeSetup> feeSetups;
			if (type.equals(FeeCode.Transportation) || type.equals(FeeCode.Other)) // 运输费不需要区分抬头和订单类型
			{
				DetachedCriteria dcFeeSetup = DetachedCriteria.forClass(FeeSetup.class);
				dcFeeSetup.add(Restrictions.eq("FeeType", type));
				feeSetups = this.feeSetupRepository.GetQueryable(FeeSetup.class).where(dcFeeSetup).toList();
			} else {
				DetachedCriteria dcFeeSetup = DetachedCriteria.forClass(FeeSetup.class);
				dcFeeSetup.add(Restrictions.eq("FeeType", type));
				dcFeeSetup.add(Restrictions.eq("LegalId", curLot.getLegalId()));
				dcFeeSetup.add(Restrictions.eq("SpotDirection", curContract.getSpotDirection()));
				feeSetups = this.feeSetupRepository.GetQueryable(FeeSetup.class).where(dcFeeSetup).toList();
			}
			if (exist != null) {

				if (feeSetups.size() <= 0) // 没有费率设置
				{
					exist.setAmountEstimated(BigDecimal.ZERO);
					exist.setRate(BigDecimal.ZERO);
					this.feeRepository.SaveOrUpdate(exist);
				} else {

					BigDecimal amount = CalcAmount(curLot, feeSetups, type, rate);
					exist.setRate(new BigDecimal(rate.toString()));
					exist.setAmountEstimated(amount);
					exist.setTradeDate(curLot.getQP());
					exist.setUpdatedBy(curLot.getCreatedBy());
					exist.setUpdatedAt(new java.util.Date());
					exist.setSerialNo(iSerial);
					this.feeRepository.SaveOrUpdate(exist);
				}
			} else if (feeSetups.size() > 0) {
				/**
				 * 取费用费率数据
				 */

				BigDecimal amount = CalcAmount(curLot, feeSetups, type, rate);

				Fee tempVar3 = new Fee();
				tempVar3.setLotId(curLot.getId());
				tempVar3.setFeeCode(type);
				tempVar3.setFeeBasis(GetFeeBasis(type));
				tempVar3.setCurrency(curLot.getCurrency());
				tempVar3.setTradeDate(curLot.getQP());
				tempVar3.setAmountEstimated(amount);
				tempVar3.setRate(new BigDecimal(rate.toString()));
				tempVar3.setIsEliminated(false);
				tempVar3.setCreatedBy(curLot.getCreatedBy());
				tempVar3.setCreatedAt(new Date());
				tempVar3.setSerialNo(iSerial);
				Fee curFee = tempVar3;
				// 保存费用
				this.feeRepository.SaveOrUpdate(curFee);
			} else if (feeSetups.size() <= 0) // 费用费率未设置自动添加空数据(有预估费用，但是金额为0）
			{
				/**
				 * 取费用费率数据
				 */
				Fee tempVar4 = new Fee();
				tempVar4.setLotId(curLot.getId());
				tempVar4.setFeeCode(type);
				tempVar4.setFeeBasis(GetFeeBasis(type));
				tempVar4.setCurrency(curLot.getCurrency());
				tempVar4.setTradeDate(curLot.getQP());
				tempVar4.setAmountEstimated(BigDecimal.ZERO);
				tempVar4.setAmountDone(BigDecimal.ZERO);
				tempVar4.setRate(BigDecimal.ZERO);
				tempVar4.setIsEliminated(false);
				tempVar4.setCreatedBy(curLot.getCreatedBy());
				tempVar4.setCreatedAt(new Date());
				tempVar4.setSerialNo(iSerial);
				Fee curFee = tempVar4;
				// 保存费用
				this.feeRepository.SaveOrUpdate(curFee);
			}

		}

		ActionResult<String> tempVar5 = new ActionResult<String>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage("成功生成费用信息");
		return tempVar5;
	}

	/**
	 * 批次分拆
	 */
	@Override
	public ActionResult<String> SplitLotQuantity(String lotId, BigDecimal splitQuantity) {

		Lot oldLot = this.repository.getOneById(lotId, Lot.class);

		/**
		 * 数据校验
		 */
		if (oldLot == null) {
			return new ActionResult<>(Boolean.FALSE, "不能找到原批次信息！");
		}

		if (oldLot.getLotNo() % 10 != 0) {
			return new ActionResult<>(Boolean.FALSE, "原批次批次号不符合规范！");
		}

		/**
		 * 计算批次号
		 */

		Integer lotNo = oldLot.getLotNo();

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.isNotNull("SplitFromId"));
		where.add(Restrictions.eq("SplitFromId", oldLot.getId()));
		where.add(Restrictions.isNotNull("IsSplitted"));
		where.add(Restrictions.eq("IsSplitted", true));
		List<Lot> lots = this.repository.GetQueryable(Lot.class).where(where).toList();
		List<Integer> lotNos = lots.stream().map(s -> s.getLotNo()).collect(Collectors.toList());

		Integer splits;
		if (lotNos == null || lotNos.size() == 0) {
			splits = -1;
		} else {
			splits = lotNos.stream().reduce(0, Integer::max);
		}

		if (splits == -1) {
			// 说明不存在
			lotNo = lotNo * 10 + 1;
		} else {
			lotNo = ++splits;
		}

		oldLot.setQuantity(oldLot.getQuantity().subtract(splitQuantity));
		ReCalculateLotMarks(oldLot);
		oldLot.setIsSourceOfSplitted(true);
		this.repository.SaveOrUpdate(oldLot); // 保存原批次信息

		Lot newLot = new Lot();
		BeanUtils.copyProperties(oldLot, newLot);
		newLot.setId(null);
		newLot.setQuantity(splitQuantity);
		newLot.setSplitFromId(oldLot.getId());
		newLot.setLotNo(lotNo);
		newLot.setFullNo(newLot.getHeadNo() + "/" + newLot.getLotNo());
		newLot.setVersion(1);
		newLot.setQuantityDelivered(BigDecimal.ZERO);
		newLot.setQuantityHedged(BigDecimal.ZERO);
		newLot.setQuantityInvoiced(BigDecimal.ZERO);
		if (oldLot.getMajorType().equals(MajorType.Fix)) {
			newLot.setQuantityPriced(oldLot.getQuantityOriginal());
		} else {
			newLot.setQuantityPriced(BigDecimal.ZERO);
		}
		newLot.setIsSourceOfSplitted(false);

		if (oldLot.getBrands() != null && oldLot.getBrands().size() > 0) {
			newLot.setBrands(new ArrayList<Brand>());
			for (Brand brand : oldLot.getBrands()) {
				Brand newb = new Brand();
				BeanUtils.copyProperties(brand, newb);
				newLot.getBrands().add(newb);
			}
		}
		/**
		 * 溢短装
		 */
		if (newLot.getMoreOrLessBasis().equals("OnQuantity")) {
			newLot.setQuantityLess(newLot.getQuantity().multiply(newLot.getMoreOrLess()));
			newLot.setQuantityMore(newLot.getQuantity().add(newLot.getMoreOrLess()));

		} else if (newLot.getMoreOrLessBasis().equals("OnPercentage")) {
			newLot.setQuantityLess(newLot.getQuantity()
					.multiply(new BigDecimal(1).subtract(oldLot.getMoreOrLess().divide(new BigDecimal(100)))));
			newLot.setQuantityMore(newLot.getQuantity()
					.multiply(new BigDecimal(1).add(oldLot.getMoreOrLess().divide(new BigDecimal(100)))));
		}

		newLot.setIsSplitted(true);
		newLot.setIsInvoiced(false);
		newLot.setIsFunded(false);
		newLot.setIsDelivered(false); // 新批次肯定是未交货部分

		// 新批次的点价/保值信息与原批次保持一致
		newLot.setIsPriced(oldLot.getIsPriced());
		newLot.setIsHedged(oldLot.getIsHedged());

		this.repository.SaveOrUpdate(newLot); // 保存原批次信息

		return new ActionResult<>(Boolean.TRUE, "批次拆分成功！");
	}

	/**
	 * 批次盈亏结算 - 试算
	 * 
	 * @param param4LotPnL
	 * @return
	 */
	@Override
	public ActionResult<LotPnL> LotSettleTrial(Param4LotPnL param4LotPnL) {
		/**
		 * 有效性检查
		 */
		GlobalSet globalSet = this.globalSetRepository.GetQueryable(GlobalSet.class).firstOrDefault();

		if (globalSet == null) {
			ActionResult<LotPnL> tempVar = new ActionResult<LotPnL>();
			tempVar.setSuccess(false);
			tempVar.setMessage("缺少GlobalSet设置。");
			return tempVar;
		}

		String currency = globalSet.getDefaultCurrency();

		if (param4LotPnL.getLotId() == null) {
			ActionResult<LotPnL> tempVar2 = new ActionResult<LotPnL>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("Params is unavailable: LotId");
			return tempVar2;
		}
		// 结算之前先更新保值头寸价格
		UpdateLotHedgedPrce(param4LotPnL.getLotId());

		Lot lot = this.repository.getOneById(param4LotPnL.getLotId(), Lot.class);

		if (lot == null) {
			ActionResult<LotPnL> tempVar3 = new ActionResult<LotPnL>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("The Lot is not existed already.");
			return tempVar3;
		}

		if (lot.getIsSettled()) {
			ActionResult<LotPnL> tempVar4 = new ActionResult<LotPnL>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage("The Lot is settled already.");
			return tempVar4;
		}

		if (lot.getIsAccounted()) {
			ActionResult<LotPnL> tempVar5 = new ActionResult<LotPnL>();
			tempVar5.setSuccess(false);
			tempVar5.setMessage("The Lot is accounted already.");
			return tempVar5;
		}

		/**
		 * 构建:vmPnL
		 */

		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(lot.getId());
		vmPnL.setContractFullNo(lot.getFullNo());
		vmPnL.setCustomerName(lot.getCustomer().getName());
		vmPnL.setPremium((lot.getPremium() != null ? lot.getPremium() : BigDecimal.ZERO));

		/**
		 * 销售一侧的商品明细
		 */

		DetachedCriteria whereStorage = DetachedCriteria.forClass(Storage.class);
		whereStorage.add(Restrictions.eq("LotId", lot.getId()));
		vmPnL.setStorageOuts(this.storageRepository.GetQueryable(Storage.class).toList());

		vmPnL.setQuantitySell(lot.getQuantity());

		// 销售金额
		vmPnL.setAmountSell(BigDecimal.ZERO);
		for (Storage so : vmPnL.getStorageOuts()) {
			Storage sms = so;
			String lotid1 = sms.getLotId();
			if (lot.getIsSplitted()!=null&&lot.getIsSplitted()) // 拆分批次使用原批次保值均价
			{
				lotid1 = lot.getSplitFromId();
			}
			so.setFuture2(PositionAverageByLot(lotid1)); // 保值均价,
			// 对应采购（SM)
			DetachedCriteria whereStorage2 = DetachedCriteria.forClass(Storage.class);
			whereStorage2.add(Restrictions.eq("Id", sms.getCounterpartyId()));
			Storage smb = this.storageRepository.GetQueryable(Storage.class).where(whereStorage2).firstOrDefault();
			if (smb != null && smb.getContractId() != null && smb.getLotId() != null) {
				so.setFuture3(PositionAverageByLot(smb.getLotId()).add(smb.getSpread4Lot()));
				so.setSpotPrice3(smb.getMajor());
				so.setPremium3(smb.getPremium());
				so.setRealFee3(smb.getRealFee());

				/**
				 * 得到上游信息
				 */
				DetachedCriteria whereContract = DetachedCriteria.forClass(Contract.class);
				whereContract.add(Restrictions.eq("Id", smb.getContractId()));
				Contract contract4Smb = this.contractRepository.GetQueryable(Contract.class).where(whereContract)
						.firstOrDefault();

				if (contract4Smb != null) {
					/**
					 * 内部，来自bvi
					 */
					if (contract4Smb.getIsInternal()) {
						// 对应BVI销售
						DetachedCriteria whereStorage4 = DetachedCriteria.forClass(Storage.class);
						whereStorage4.add(Restrictions.eq("Id", smb.getCounterpartyId3()));
						Storage bvis = this.storageRepository.GetQueryable(Storage.class).where(whereStorage4)
								.firstOrDefault();
						if (bvis != null) {
							so.setSpotPrice4(bvis.getMajor());
							so.setPremium4(bvis.getPremium());
							so.setRealFee4(bvis.getRealFee());
							// 对应BVI采购
							DetachedCriteria whereStorage5 = DetachedCriteria.forClass(Storage.class);
							whereStorage5.add(Restrictions.eq("Id", bvis.getCounterpartyId()));
							Storage bvib = this.storageRepository.GetQueryable(Storage.class).where(whereStorage5)
									.firstOrDefault();
							if (bvib != null) {
								so.setFuture5(PositionAverageByLot(bvib.getLotId()));
								so.setSpotPrice5(bvib.getMajor());
								so.setPremium5(bvib.getPremium());
								so.setRealFee5(bvib.getRealFee5());
							}
						}
					} else {
						// 普通销售
					}

				}
			}

			BigDecimal b = BigDecimal.ZERO;
			BigDecimal future5 = so.getFuture5() != null ? so.getFuture5() : BigDecimal.ZERO;
			BigDecimal spotPrice5 = so.getSpotPrice5() != null ? so.getSpotPrice5() : BigDecimal.ZERO;
			BigDecimal premium5 = so.getPremium5() != null ? so.getPremium5() : BigDecimal.ZERO;
			BigDecimal realFee5 = so.getRealFee5() != null ? so.getRealFee5() : BigDecimal.ZERO;
			BigDecimal spotPrice4 = so.getSpotPrice4() != null ? so.getSpotPrice4() : BigDecimal.ZERO;
			BigDecimal premium4 = so.getPremium4() != null ? so.getPremium4() : BigDecimal.ZERO;
			BigDecimal realFee4 = so.getRealFee4() != null ? so.getRealFee4() : BigDecimal.ZERO;
			BigDecimal future3 = so.getFuture3() != null ? so.getFuture3() : BigDecimal.ZERO;
			BigDecimal spotPrice3 = so.getSpotPrice3() != null ? so.getSpotPrice3() : BigDecimal.ZERO;
			BigDecimal premium3 = so.getPremium3() != null ? so.getPremium3() : BigDecimal.ZERO;
			BigDecimal realFee3 = so.getRealFee3() != null ? so.getRealFee3() : BigDecimal.ZERO;
			BigDecimal major = so.getMajor() != null ? so.getMajor() : BigDecimal.ZERO;
			BigDecimal premium = so.getPremium() != null ? so.getPremium() : BigDecimal.ZERO;
			BigDecimal future2 = so.getFuture2() != null ? so.getFuture2() : BigDecimal.ZERO;
			BigDecimal realFee = so.getRealFee() != null ? so.getRealFee() : BigDecimal.ZERO;

			b = future5.subtract(spotPrice5).subtract(premium5).subtract(realFee5).subtract(spotPrice4).add(premium4)
					.subtract(realFee4).add(future3).subtract(spotPrice3).subtract(premium3).subtract(realFee3)
					.add(major).add(premium).subtract(future2).subtract(realFee);

			so.setPnLUnit(b);
			so.setPnL4Storage(so.getPnLUnit().multiply(so.getQuantity()));

			so.setPnL4Storage(((so.getPnL4Storage() != null) ? so.getPnL4Storage().setScale(2, BigDecimal.ROUND_HALF_UP)
					: BigDecimal.ZERO));
		}

		vmPnL.setStorageOuts(commonService.SimplifyDataStorageList(vmPnL.getStorageOuts()));

		BigDecimal t = BigDecimal.ZERO;
		for (Storage storage : vmPnL.getStorageOuts()) {
			BigDecimal s = storage.getPnL4Storage() != null ? storage.getPnL4Storage() : BigDecimal.ZERO;
			t = t.add(s);
		}
		vmPnL.setPnLTotal(t);
		// 合计盈亏= 现货盈亏+ 期货盈亏
		vmPnL.setCurrency(currency);

		ActionResult<LotPnL> tempVar7 = new ActionResult<LotPnL>();
		tempVar7.setSuccess(true);
		tempVar7.setMessage("试算成功。");
		tempVar7.setData(vmPnL);
		return tempVar7;
	}

	/**
	 * 批次盈亏结算 - 试算
	 * 
	 * @param param4LotPnL
	 * @return
	 */
	@Override
	public ActionResult<LotPnL> LotSettleTrial00(Param4LotPnL param4LotPnL) {

		DetachedCriteria where = DetachedCriteria.forClass(GlobalSet.class);
		GlobalSet globalSet = this.globalSetRepository.GetQueryable(GlobalSet.class).where(where).firstOrDefault();

		if (globalSet == null) {
			ActionResult<LotPnL> tempVar = new ActionResult<LotPnL>();
			tempVar.setSuccess(false);
			tempVar.setMessage("缺少GlobalSet设置。");
			return tempVar;
		}

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		if (param4LotPnL.getLotId() == null) {
			ActionResult<LotPnL> tempVar2 = new ActionResult<LotPnL>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("Params is unavailable: LotId");
			return tempVar2;
		}

		// 结算之前先更新保值头寸价格
		UpdateLotHedgedPrce(param4LotPnL.getLotId());

		Lot lot = this.repository.getOneById(param4LotPnL.getLotId(), Lot.class);
		if (lot == null) {
			ActionResult<LotPnL> tempVar3 = new ActionResult<LotPnL>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("The Lot is not existed already.");
			return tempVar3;
		}

		if (lot.getIsSettled()) {
			ActionResult<LotPnL> tempVar4 = new ActionResult<LotPnL>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage("The Lot is settled already.");
			return tempVar4;
		}

		if (lot.getIsAccounted()) {
			ActionResult<LotPnL> tempVar5 = new ActionResult<LotPnL>();
			tempVar5.setSuccess(false);
			tempVar5.setMessage("The Lot is accounted already.");
			return tempVar5;
		}

		/**
		 * 构建:vmPnL
		 */
		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(lot.getId());
		vmPnL.setContractFullNo(lot.getFullNo());
		vmPnL.setCustomerName(lot.getCustomer().getName());
		vmPnL.setPremium((lot.getPremium() != null ? lot.getPremium() : BigDecimal.ZERO));

		/**
		 * 销售一侧的商品明细
		 */

		DetachedCriteria whereStorage = DetachedCriteria.forClass(Storage.class);
		whereStorage.add(Restrictions.eq("LotId", lot.getId()));
		vmPnL.setStorageOuts(this.storageRepository.GetQueryable(Storage.class).toList());

		vmPnL.setQuantitySell(lot.getQuantity());

		// 销售金额
		vmPnL.setAmountSell(BigDecimal.ZERO);

		// 销售发生的fee的汇总金额
		BigDecimal fee4Sell = BigDecimal.ZERO;

		for (Storage so : vmPnL.getStorageOuts()) {
			if (currency.equals(so.getCurrency())) {
				vmPnL.setAmountSell(vmPnL.getAmountSell()
						.add(so.getQuantity().multiply(so.getPrice() != null ? so.getPrice() : BigDecimal.ZERO)));

				fee4Sell = fee4Sell.add(so.getQuantity().multiply(so.getFee() != null ? so.getFee() : BigDecimal.ZERO));
			} else {
				if (so.getCurrency().equals("CNY")) {
					vmPnL.setAmountSell(vmPnL.getAmountSell().add(so.getQuantity()
							.multiply(so.getPrice() != null ? so.getPrice() : BigDecimal.ZERO).divide(rate)));
					fee4Sell = fee4Sell.add(so.getQuantity()
							.multiply(so.getFee() != null ? so.getFee() : BigDecimal.ZERO).divide(rate));
				} else {
					vmPnL.setAmountSell(vmPnL.getAmountSell().add(so.getQuantity()
							.multiply(so.getPrice() != null ? so.getPrice() : BigDecimal.ZERO).multiply(rate)));
					fee4Sell = fee4Sell.add(so.getQuantity()
							.multiply(so.getFee() != null ? so.getFee() : BigDecimal.ZERO).multiply(rate));
				}
			}
		}

		// 销售均价
		if (vmPnL.getQuantitySell().compareTo(BigDecimal.ZERO) != 0) {
			vmPnL.setPriceSell(vmPnL.getAmountSell().divide(vmPnL.getQuantitySell()));
		}

		vmPnL.setStorageOuts(commonService.SimplifyDataStorageList(vmPnL.getStorageOuts()));

		/**
		 * 采购一侧的商品明细
		 */

		vmPnL.setStorageIns(new ArrayList<Storage>());
		for (Storage v : vmPnL.getStorageOuts()) {
			// 取得对手方的交付记录，并且加入到List中
			if (v.getCounterpartyId() != null) {
				Storage storage = this.storageRepository.getOneById(v.getCounterpartyId(), Storage.class);
				vmPnL.getStorageIns().add(storage);
			}
		}

		BigDecimal b = BigDecimal.ZERO;
		for (Storage storage : vmPnL.getStorageIns()) {
			b = b.add(storage.getQuantity());
		}
		// 采购数量
		vmPnL.setQuantityPurchase(b);

		// 采购成本
		vmPnL.setAmountPurchase(BigDecimal.ZERO);
		;

		// 采购发生的fee的汇总金额
		BigDecimal fee4Purchase = BigDecimal.ZERO;

		List<String> lstBuyLot = new ArrayList<String>();
		for (Storage si : vmPnL.getStorageIns()) {
			// 结算之前先更新保值头寸价格
			if (si.getLotId() != null && !lstBuyLot.contains(si.getLotId())) {
				UpdateLotHedgedPrce(si.getLotId());
			}

			if (currency.equals(si.getCurrency())) {
				vmPnL.setAmountPurchase(vmPnL.getAmountPurchase()
						.add(si.getQuantity().multiply(si.getPrice() != null ? si.getPrice() : BigDecimal.ZERO)));

				fee4Purchase = fee4Purchase
						.add(si.getQuantity().multiply(si.getFee() != null ? si.getFee() : BigDecimal.ZERO));
			} else {
				if (si.getCurrency().equals("CNY")) {
					vmPnL.setAmountPurchase(vmPnL.getAmountPurchase().add(si.getQuantity()
							.multiply(si.getPrice() != null ? si.getPrice() : BigDecimal.ZERO).divide(rate)));
					fee4Purchase = fee4Purchase.add(si.getQuantity()
							.multiply(si.getFee() != null ? si.getFee() : BigDecimal.ZERO).divide(rate));
				}

				else {
					vmPnL.setAmountPurchase(vmPnL.getAmountPurchase().add(si.getQuantity()
							.multiply(si.getPrice() != null ? si.getPrice() : BigDecimal.ZERO).multiply(rate)));
					fee4Purchase = fee4Purchase.add(si.getQuantity()
							.multiply(si.getFee() != null ? si.getFee() : BigDecimal.ZERO).multiply(rate));
				}
			}
			if (si.getLotId() != null && !lstBuyLot.contains(si.getLotId())) {
				lstBuyLot.add(si.getLotId());
			}
		}

		// 采购价格
		if (vmPnL.getQuantityPurchase().compareTo(BigDecimal.ZERO) != 0) {
			vmPnL.setPricePurchase(vmPnL.getAmountPurchase().divide(vmPnL.getQuantityPurchase()));
		}

		vmPnL.setFee(fee4Sell.add(fee4Purchase));

		vmPnL.setPnL4Spot(vmPnL.getAmountSell() != null ? vmPnL.getAmountSell()
				: BigDecimal.ZERO
						.subtract(vmPnL.getAmountPurchase() != null ? vmPnL.getAmountPurchase() : BigDecimal.ZERO));

		vmPnL.setStorageIns(commonService.SimplifyDataStorageList(vmPnL.getStorageIns()));

		if (param4LotPnL.getIsPositionSquare()) {

		} else {
			/**
			 * 期货盈亏(如果头寸需要结算，此段代码需关闭)
			 */
			BigDecimal Hedgeforbuy = BigDecimal.ZERO;
			for (String buylotId1 : lstBuyLot) {
				DetachedCriteria dcbuylot = DetachedCriteria.forClass(Lot.class);
				dcbuylot.add(Restrictions.eq("Id", buylotId1));
				Lot buylot = this.repository.GetQueryable(Lot.class).where(dcbuylot).firstOrDefault();

				if (buylot != null) {
					Hedgeforbuy = Hedgeforbuy.add(buylot.getQuantity()
							.multiply(buylot.getHedgedPrice() != null ? buylot.getHedgedPrice() : BigDecimal.ZERO));
				}
			}
			/**
			 * 采购对应卖出头寸，销售对应的是买入头寸
			 */
			vmPnL.setPnLTotal(
					Hedgeforbuy.subtract((lot.getHedgedPrice() != null ? lot.getHedgedPrice() : BigDecimal.ZERO)
							.multiply(vmPnL.getQuantitySell() != null ? vmPnL.getQuantitySell() : BigDecimal.ZERO)));

		}
		vmPnL.setPnLTotal(vmPnL.getPnL4Spot().add(vmPnL.getPnL4Hedge())); // 合计盈亏=
																			// 现货盈亏+
																			// 期货盈亏
		vmPnL.setCurrency(currency);

		ActionResult<LotPnL> tempVar7 = new ActionResult<LotPnL>();
		tempVar7.setSuccess(true);
		tempVar7.setMessage("试算成功。");
		tempVar7.setData(vmPnL);
		return tempVar7;
	}

	/**
	 * 批次盈亏结算 - 正式
	 * 
	 * @param param4LotPnL
	 * @return
	 */
	@Override
	public ActionResult<LotPnL> LotSettleOfficial(Param4LotPnL param4LotPnL) {

		ActionResult<LotPnL> actionResult = LotSettleTrial(param4LotPnL);
		LotPnL obj = actionResult.getData();
		if (actionResult.isSuccess() == false || obj == null) {
			ActionResult<LotPnL> tempVar = new ActionResult<LotPnL>();
			tempVar.setSuccess(false);
			tempVar.setMessage("试算失败，请检查。");
			return tempVar;
		}
		List<Square> squares = obj.getSquares();

		if (squares != null) {

			boolean flag = true;
			for (Square square : squares) {
				if (square.getPromptDate() == null) {
					flag = false;
					break;
				}
			}

			if (flag) {
				ActionResult<LotPnL> tempVar2 = new ActionResult<LotPnL>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("头寸到期日错误，不符合正式结算的要求。");
				return tempVar2;
			}

			Map<Object, List<Square>> group_quares = squares.stream().collect(Collectors
					.groupingBy(p -> new KeyObj(p.getMarketId(), p.getCommodityId(), p.getPromptDate()).getKeys()));
			Iterator<Entry<Object, List<Square>>> iter = group_quares.entrySet().iterator();
			List<Square> listSquare = new ArrayList<>();
			while (iter.hasNext()) {
				@SuppressWarnings("rawtypes")
				Entry e = (Entry) iter.next();
				@SuppressWarnings("unchecked")
				List<String> key = (List<String>) e.getKey();
				@SuppressWarnings("unchecked")
				List<Square> val = (List<Square>) e.getValue();
				Square s = new Square();
				s.setMarketId(key.get(0));
				s.setCommodityId(key.get(1));
				s.setPromptDate(DateUtil.doSFormatDate(key.get(0), null));
				BigDecimal b = BigDecimal.ZERO;
				for (Square square : val) {
					b = b.add(square.getQuantityLong().add(square.getQuantityShort()));
				}
				s.setQuantity(b);
				listSquare.add(s);
			}

			if (listSquare.stream().anyMatch(p -> p.getQuantity().compareTo(BigDecimal.ZERO) != 0)) {
				ActionResult<LotPnL> tempVar4 = new ActionResult<LotPnL>();
				tempVar4.setSuccess(false);
				tempVar4.setMessage("头寸没有全部对齐，不符合正式结算的要求。");
				return tempVar4;
			}
		}
		obj.setSquares(null); // 清空关联的明细，否则保存会失败
		String pnLId = this.lotPnLSetRepository.SaveOrUpdateRetrunId(obj);
		// 保存结算明细
		if (squares != null) {
			for (Square PnL4Position : squares) {
				Position split = PnL4Position.getSplitSquarePosition(); // 临时存放的拆分头寸，必须自Square之前保存
				if (split != null) {
					split.setIsSquared(true);
					split.setLotPnLId(pnLId);
					String splitId = this.positionRepository.SaveOrUpdateRetrunId(split);
					if (split.getLS().equals(LS.LONG)) {
						PnL4Position.setLongId(splitId);
					} else {
						PnL4Position.setShortId(splitId);
					}

					PnL4Position.setSplitSquarePosition(null);
				}
				PnL4Position.setCreatedAt(new Date());
				PnL4Position.setLotPnLId(pnLId);
				PnL4Position.setSquareDate(new Date());

				this.squareRepository.SaveOrUpdate(PnL4Position);
			}
		}
		if (obj.getPositions() != null) {
			for (Position position : obj.getPositions()) {
				Position p = this.positionRepository.getOneById(position.getId(), Position.class);
				p.setQuantity(position.getQuantity());
				p.setLotPnLId(pnLId);
				p.setIsSquared(true);
				this.positionRepository.SaveOrUpdate(p);
			}
		}

		// 需要优化
		List<Storage> listStorage = new ArrayList<>();
		for (Storage s : obj.getStorageIns()) {
			Storage storage = this.storageRepository.getOneById(s.getId(), Storage.class);
			if (storage != null) {
				listStorage.add(storage);
			}

		}

		for (Storage storage : listStorage) {
			storage.setIsSettled(true);
			this.storageRepository.SaveOrUpdate(storage);
		}

		List<Storage> listStorageOuts = new ArrayList<>();
		for (Storage s2 : obj.getStorageOuts()) {
			Storage storage2 = this.storageRepository.getOneById(s2.getId(), Storage.class);
			if (storage2 != null) {
				listStorageOuts.add(storage2);
			}

		}

		for (Storage so : listStorageOuts) {
			so.setIsSettled(true);
			this.storageRepository.SaveOrUpdate(so);
		}

		Lot lot = this.repository.getOneById(obj.getLotId(), Lot.class);

		lot.setIsSettled(true);
		this.repository.SaveOrUpdate(lot);

		ActionResult<LotPnL> tempVar5 = new ActionResult<LotPnL>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage("结算成功。");
		tempVar5.setData(null);
		return tempVar5;
	}

	@Override
	public ActionResult<LotPnL> LotPnLById(String lotId) {

		DetachedCriteria where1 = DetachedCriteria.forClass(LotPnL.class);
		where1.add(Restrictions.eq("LotId", lotId));
		LotPnL lotPnL = this.lotPnLSetRepository.GetQueryable(LotPnL.class).where(where1).firstOrDefault();

		DetachedCriteria where2 = DetachedCriteria.forClass(Square.class);
		where2.add(Restrictions.eq("LotId", lotId));
		List<Square> squares = this.squareRepository.GetQueryable(Square.class).where(where2).toList();
		lotPnL.setSquares(squares);

		DetachedCriteria where3 = DetachedCriteria.forClass(Storage.class);
		where3.add(Restrictions.eq("LotId", lotPnL.getId()));
		where3.add(Restrictions.eq("MT", "T"));
		List<Storage> storageIns = this.storageRepository.GetQueryable(Storage.class).where(where3).toList();
		lotPnL.setStorageIns(storageIns);

		DetachedCriteria where4 = DetachedCriteria.forClass(Storage.class);
		where4.add(Restrictions.eq("LotId", lotPnL.getId()));
		where4.add(Restrictions.eq("MT", "M"));
		List<Storage> StorageOuts = this.storageRepository.GetQueryable(Storage.class).where(where4).toList();
		lotPnL.setStorageOuts(StorageOuts);

		ActionResult<LotPnL> tempVar = new ActionResult<LotPnL>();
		tempVar.setSuccess(true);
		tempVar.setData(lotPnL);
		return tempVar;
	}

	/**
	 * 更新批次头寸价格
	 * 
	 * @param lotid
	 * @return
	 */
	@Override
	public ActionResult<String> UpdateLotHedgedPrce(String lotid) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("Id", lotid));
		Lot curLot = this.repository.GetQueryable(Lot.class).where(where).firstOrDefault();

		if (curLot == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("未得到批次信息");
			return tempVar;
		}

		// 得到批次的保值信息
		DetachedCriteria where2 = DetachedCriteria.forClass(Position.class);
		where2.add(Restrictions.eq("LotId", lotid));
		where2.add(Restrictions.isNotNull("LotId"));
		List<Position> lstPositon = this.positionRepository.GetQueryable(Position.class).where(where2).toList();

		List<Position> lstPhysicalPosition = lstPositon.stream().filter(x -> x.getIsCarry() == false)
				.collect(Collectors.toList());

		List<Position> lstCarryPosition = lstPositon.stream().filter(x -> x.getIsCarry() == true)
				.collect(Collectors.toList());

		BigDecimal amount = BigDecimal.ZERO;
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Position x : lstPhysicalPosition) {
			amount = amount.add(x.getOurPrice().multiply(x.getQuantity()));
			sumQuantity = sumQuantity.add(x.getQuantity());
		}
		BigDecimal average = BigDecimal.ZERO;
		if (sumQuantity.compareTo(BigDecimal.ZERO) != 0) {
			average = DecimalUtil.divideForQuantity(amount, sumQuantity);
		}

		BigDecimal sumSpread = BigDecimal.ZERO;
		for (Position position : lstCarryPosition) {
			sumSpread = sumSpread.add(position.getSpread());
		}
		// 计算保值均价 = 非调期头寸的加权平均价格 + 调期Spread；
		curLot.setHedgedPrice(average != null ? average : sumSpread);

		this.repository.SaveOrUpdate(curLot);
		ActionResult<String> tempVar2 = new ActionResult<String>();
		tempVar2.setSuccess(true);
		tempVar2.setMessage("更新完成");
		return tempVar2;
	}

	/**
	 * 根据批次id得到该批次以及该批次的拆分批次
	 * 
	 * @param lotId
	 * @return
	 */
	@Override
	public ActionResult<List<Lot>> LotsById(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.or(Restrictions.eqOrIsNull("Id", lotId), Restrictions.eq("Id", lotId)));
		where.add(Restrictions.eq("IsHidden", false));

		List<Lot> lots = this.repository.GetQueryable(Lot.class).where(where).toList();
		lots = commonService.SimplifyDataLotList(lots);

		ActionResult<List<Lot>> tempVar = new ActionResult<List<Lot>>();
		tempVar.setSuccess(true);
		tempVar.setData(lots);
		return tempVar;
	}

	/**
	 * 保存修改后的QP
	 * 
	 * @param curQPRecord
	 * @return
	 */
	@Override
	public ActionResult<QPRecord> SaveQPRecord(QPRecord curQPRecord) {

		try {
			if (curQPRecord.getLotId() != null) {
				Lot lot = this.repository.getOneById(curQPRecord.getLotId(), Lot.class);
				lot.setQP(curQPRecord.getRevisedQP());
				this.repository.SaveOrUpdate(lot);
				String recordId = this.qrRecordRepository.SaveOrUpdateRetrunId(curQPRecord);
				DetachedCriteria where = DetachedCriteria.forClass(QPRecord.class);
				where.add(Restrictions.eq("Id", recordId));
				QPRecord record = this.qrRecordRepository.GetQueryable(QPRecord.class).where(where).firstOrDefault();

				ActionResult<QPRecord> tempVar = new ActionResult<QPRecord>();
				tempVar.setSuccess(true);
				tempVar.setMessage(MessageCtrm.SaveSuccess);
				tempVar.setData(record);
				return tempVar;
			}
			ActionResult<QPRecord> tempVar2 = new ActionResult<QPRecord>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("请先保存批次");
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<QPRecord> tempVar3 = new ActionResult<QPRecord>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;

		}
	}

	/**
	 * 查询QP修改历史
	 * 
	 * @param lotId
	 * @return
	 */
	@Override
	public ActionResult<List<QPRecord>> PagerQPRecord(String lotId) {
		try {
			if (lotId == null) {
				ActionResult<List<QPRecord>> tempVar = new ActionResult<List<QPRecord>>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数错误");
				return tempVar;
			}
			DetachedCriteria where = DetachedCriteria.forClass(QPRecord.class);
			where.add(Restrictions.eq("LotId", lotId));
			List<QPRecord> QPRecords = this.qrRecordRepository.GetQueryable(QPRecord.class).where(where).toList();

			ActionResult<List<QPRecord>> tempVar2 = new ActionResult<List<QPRecord>>();
			tempVar2.setSuccess(true);
			tempVar2.setData(assemblingBeanList3(QPRecords));
			return tempVar2;

		} catch (Exception ex) {
			ActionResult<List<QPRecord>> tempVar3 = new ActionResult<List<QPRecord>>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * 删除QP历史记录
	 * 
	 * @param qPRecord
	 * @param userId
	 * @return
	 */
	@Override
	public ActionResult<String> DeleteQPRcord(QPRecord qPRecord, String userId) {
		try {
			QPRecord record = this.qrRecordRepository.getOneById(qPRecord.getId(), QPRecord.class);
			if (!userId.equalsIgnoreCase(record.getCreatedId())) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("不可已删除他人创建的记录");
				return tempVar;
			}
			this.qrRecordRepository.PhysicsDelete(qPRecord.getId(), QPRecord.class);
			Lot lot = this.repository.getOneById(qPRecord.getLotId(), Lot.class);
			lot.setQP(qPRecord.getCurrentQP());
			this.repository.SaveOrUpdate(lot);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setMessage(MessageCtrm.DeleteSuccess);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	/**
	 * LotsByKeywords
	 *
	 * @param keyword
	 * @return
	 */
	@Override
	public ActionResult<List<VmContractLot4Combox>> LotsByKeywords(String keyword) {
		Criteria criteria = vlotRepo.CreateCriteria(vLot.class);
		if (keyword != null && !"".equals(keyword)) {
			criteria.add(Restrictions.like("FullNo", "%" + keyword + "%"));
		}
		criteria.setMaxResults(20);
		@SuppressWarnings("unchecked")
		List<vLot> vLots = criteria.list();

		final List<VmContractLot4Combox> vmContractLot4ComboxList = new ArrayList<>();

		if (vLots != null) {
			vLots.forEach(x -> {
				VmContractLot4Combox vmContractLot4Combox = new VmContractLot4Combox();
				vmContractLot4Combox.setId(x.getId());
				vmContractLot4Combox.setLegalName(x.getLegalName() == null ? "" : x.getLegalName());
				vmContractLot4Combox.setHeadNo(x.getHeadNo());
				vmContractLot4Combox.setFullNo(x.getFullNo());

				// 保留几位小数
				if (x.getQuantity() == null) {
					x.setQuantity(BigDecimal.ZERO);
				}
				// 计算需要保留几位
				int digits = 3;
				// 保留3位小数
				vmContractLot4Combox.setQuantity(x.getQuantity().setScale(digits, RoundingMode.HALF_EVEN));

				vmContractLot4Combox.setCustomerName(x.getCustomerName() == null ? "" : x.getCustomerName());
				vmContractLot4Combox.setCustomerShortName(x.getCustomerShortName());

				vmContractLot4ComboxList.add(vmContractLot4Combox);
			});
		}

		ActionResult<List<VmContractLot4Combox>> tempVar5 = new ActionResult<List<VmContractLot4Combox>>();
		tempVar5.setSuccess(true);
		tempVar5.setData(vmContractLot4ComboxList);
		return tempVar5;
	}

	/**
	 * MTM 报表
	 * 
	 * @param dtStart
	 * @param dtEnd
	 * @param bSquared
	 * @return
	 */
	@Override
	public ActionResult<List<Lot4MTM>> Lot4MTMQuery(Date dtStart, Date dtEnd, Boolean bSquared) {

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("FullNo4Bvi", "");
		parameters.put("bSquared", bSquared ? 1 : 0);
		try {
			String proc = "proc_Lot4MTMQuery";

			List<Object[]> snBRecords = this.repository.ExecuteProcedureSql(proc, parameters);

			List<Lot4MTM> lot4mtms = new ArrayList<Lot4MTM>();

			int iStart = 0;
			for (Object[] snBRecord : snBRecords) {
				Lot4MTM lot4mtm = new Lot4MTM();
				List<Object> rec = Arrays.asList(snBRecord);

				lot4mtm.setFullNo4Bvi(String.valueOf(rec.get(iStart)));
				lot4mtm.setQuantity4Bvi((StringUtils.isBlank(String.valueOf(rec.get(iStart + 1))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 1)))));
				lot4mtm.setPricingQuantity4Bvi((StringUtils.isBlank(String.valueOf(rec.get(iStart + 2)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 2)))));
				lot4mtm.setPrice4PositionBviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 3)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 3)))));
				lot4mtm.setPrice4PricingBviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 4))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 4)))));
				lot4mtm.setProfit4BviBFuture((StringUtils.isBlank(String.valueOf(rec.get(iStart + 5))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 5)))));
				lot4mtm.setPremium4BviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 6))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 6)))));
				lot4mtm.setEstimateFee4BviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 7)))));
				lot4mtm.setSpread4InitialBviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 8)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 8)))));
				lot4mtm.setSpread4QpBviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 9))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 9)))));
				lot4mtm.setSpread4LotBviB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 10))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 10)))));
				lot4mtm.setLoading(String.valueOf(rec.get(iStart + 11)));
				lot4mtm.setDeliveryTerm4Bvi((String) rec.get(iStart + 12));

				lot4mtm.setPrice4PricingBviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 13)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 13)))));
				lot4mtm.setPremium4BviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 14))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 14)))));
				lot4mtm.setEstimateFee4BviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 15))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 15)))));
				lot4mtm.setSpread4InitialBviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 16)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 16)))));
				lot4mtm.setSpread4QpBviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 17))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 17)))));
				lot4mtm.setSpread4LotBviS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 18))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 18)))));

				lot4mtm.setPrice4PricingSmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 19))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 19)))));
				lot4mtm.setPremium4SmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 20))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 20)))));
				lot4mtm.setEstimateFee4SmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 21))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 21)))));
				lot4mtm.setSpread4InitialSmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 22)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 22)))));
				lot4mtm.setSpread4QpSmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 23))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 23)))));
				lot4mtm.setSpread4LotSmB((StringUtils.isBlank(String.valueOf(rec.get(iStart + 24))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 24)))));

				lot4mtm.setPricingQuantity4Sm((StringUtils.isBlank(String.valueOf(rec.get(iStart + 25)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 25)))));
				lot4mtm.setPrice4PositionSmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 26)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 26)))));
				lot4mtm.setPrice4PricingSmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 27))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 27)))));
				lot4mtm.setProfit4SmSFuture((StringUtils.isBlank(String.valueOf(rec.get(iStart + 28))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 2)))));
				lot4mtm.setPremium4SmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 29))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 29)))));
				lot4mtm.setEstimateFee4SmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 30))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 30)))));
				lot4mtm.setSpread4InitialSmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 31)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 31)))));
				lot4mtm.setSpread4QpSmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 32))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 32)))));
				lot4mtm.setSpread4LotSmS((StringUtils.isBlank(String.valueOf(rec.get(iStart + 33))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 33)))));
				lot4mtm.setDeliveryTerm4Sm((String) rec.get(iStart + 34));
				lot4mtm.setFullNo4Sm((String) rec.get(35));
				lot4mtm.setQuantity4Sm((StringUtils.isBlank(String.valueOf(rec.get(iStart + 36))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 36)))));
				lot4mtm.setProfit((StringUtils.isBlank(String.valueOf(rec.get(iStart + 37))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 37)))));

				lot4mtms.add(lot4mtm);
			}
			ActionResult<List<Lot4MTM>> tempVar5 = new ActionResult<List<Lot4MTM>>();
			tempVar5.setSuccess(true);
			tempVar5.setTotal(lot4mtms.size());
			tempVar5.setData(lot4mtms);
			return tempVar5;

		} catch (Exception ex) {
			ActionResult<List<Lot4MTM>> tempVar6 = new ActionResult<List<Lot4MTM>>();
			tempVar6.setSuccess(false);
			tempVar6.setMessage(ex.getMessage());
			return tempVar6;
		}
	}

	/**
	 * MTM 盈亏结算表
	 * 
	 * @param date
	 * @param date2
	 * @param spotType
	 * @param currency
	 * @param string
	 * @param commodityId
	 * @param string2
	 * @param status
	 * @param legalIds
	 * @return
	 */
	@Override
	public ActionResult<List<Lot4MTM>> Lot4MTMQueryNew(Date dtStart, Date dtEnd, String keyWork, String currency,
			String spotType, String status, String legalId, String legalIds, String commodityId) {

		Map<String, Object> param = new HashMap<>();

		param.put("dBegin", dtStart);
		param.put("dEnd", dtEnd);
		param.put("keyWork", keyWork);
		param.put("currency", currency);
		param.put("spotType", spotType);
		param.put("status", status);
		param.put("legalId", legalId);
		param.put("legalIds", legalIds);
		param.put("commodityId", commodityId);

		try {
			String proc = "proc_Lot4MTMQuery_Profit";

			List<Object[]> snBRecords = this.repository.ExecuteProcedureSql(proc, param);

			java.util.ArrayList<Lot4MTM> lot4mtms = new java.util.ArrayList<Lot4MTM>();
			int iStart = 0, iStartBviB = 5, iStartBviS = 22, iStartSmB = 34, iStartSmS = 45;
			for (Object[] snBRecord : snBRecords) {
				Lot4MTM lot4mtm = new Lot4MTM();
				List<Object> rec = Arrays.asList(snBRecord);

				/**
				 * bvi 采购
				 */
				if (!StringUtils.isBlank(String.valueOf(rec.get(iStartBviB)))) {
					lot4mtm.setLotId4BviB(String.valueOf(rec.get(iStartBviB)));
				}
				lot4mtm.setFullNo4Bvi(String.valueOf(rec.get(iStartBviB + 1)));
				lot4mtm.setCustomerName4BviB((String) rec.get(iStartBviB + 2));
				lot4mtm.setInvoiceNo4BviB((String) rec.get(iStartBviB + 3));
				lot4mtm.setPrice4PositionBviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 4)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 4))));
				lot4mtm.setPrice4PricingBviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 5)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 5))));
				lot4mtm.setPricingQuantity4Bvi(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 6)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 6))));
				lot4mtm.setProfit4BviBFuture(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 7)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 7))));
				lot4mtm.setPremium4BviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 8))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviB + 8))));
				lot4mtm.setEstimateFee4BviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 9)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 9))));
				lot4mtm.setRealFee4BviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 10))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviB + 10))));
				lot4mtm.setSpread4InitialBviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 11)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 11))));
				lot4mtm.setSpread4QpBviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 12))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviB + 12))));
				lot4mtm.setSpread4LotBviB(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 13)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 13))));
				lot4mtm.setLoading(String.valueOf(rec.get(iStartBviB + 14)));
				lot4mtm.setDeliveryTerm4Bvi((String) rec.get(iStartBviB + 15));
				lot4mtm.setProfit4BviBSpot(StringUtils.isBlank(String.valueOf(rec.get(iStartBviB + 16)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviB + 16))));

				/**
				 * bvi销售
				 */
				if (!StringUtils.isBlank(String.valueOf(rec.get(iStartBviS)))) {
					lot4mtm.setLotId4BviS(String.valueOf(rec.get(iStartBviS)));
				}
				lot4mtm.setFullNo4BviS((String) rec.get(iStartBviS + 1));
				lot4mtm.setCustomerName4BviS((String) rec.get(iStartBviS + 2));
				lot4mtm.setInvoiceNo4BviS((String) rec.get(iStartBviS + 3));
				lot4mtm.setProfit4BviSFuture(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 4)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviS + 4))));
				lot4mtm.setPremium4BviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 5))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviS + 5))));
				lot4mtm.setEstimateFee4BviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 6)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviS + 6))));
				lot4mtm.setRealFee4BviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviS + 7))));
				lot4mtm.setSpread4InitialBviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 8)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviS + 8))));
				lot4mtm.setSpread4QpBviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 9))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartBviS + 9))));
				lot4mtm.setSpread4LotBviS(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 10)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviS + 10))));
				lot4mtm.setProfit4BviSSpot(StringUtils.isBlank(String.valueOf(rec.get(iStartBviS + 11)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartBviS + 11))));
				/**
				 * sm 采购
				 */
				if (!StringUtils.isBlank(String.valueOf(rec.get(iStartSmB)))) {
					lot4mtm.setLotId4SmB(String.valueOf(rec.get(iStartSmB)));
				}
				lot4mtm.setFullNo4SmB((String) rec.get(iStartSmB + 1));
				lot4mtm.setCustomerName4SmB((String) rec.get(iStartSmB + 2));
				lot4mtm.setInvoiceNo4SmB((String) rec.get(iStartSmB + 3));
				lot4mtm.setProfit4SmBFuture(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 4)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartSmB + 4))));
				lot4mtm.setPremium4SmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 5))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 5))));
				lot4mtm.setEstimateFee4SmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 6))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 6))));
				lot4mtm.setRealFee4SmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 7))));
				lot4mtm.setSpread4InitialSmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 8)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartSmB + 8))));
				lot4mtm.setSpread4QpSmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 9))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 9))));
				lot4mtm.setSpread4LotSmB(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 10))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 10))));
				lot4mtm.setProfit4SmBSpot(StringUtils.isBlank(String.valueOf(rec.get(iStartSmB + 11))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmB + 11))));
				/**
				 * sm销售
				 */
				if (!StringUtils.isBlank(String.valueOf(rec.get(iStart)))) {
					lot4mtm.setLotId4SmS(String.valueOf(rec.get(iStart)));
				}
				lot4mtm.setFullNo4Sm((String) rec.get(iStart + 1));
				lot4mtm.setQuantity4Sm(StringUtils.isBlank(String.valueOf(rec.get(iStart + 2))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 2))));
				lot4mtm.setCustomerName4SmS((String) rec.get(iStart + 3));
				lot4mtm.setInvoiceNo4SmS((String) rec.get(iStart + 4));

				lot4mtm.setProfit4SmSFuture(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 1)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartSmS + 1))));
				lot4mtm.setPremium4SmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 2))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 2))));
				lot4mtm.setEstimateFee4SmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 3))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 3))));
				lot4mtm.setRealFee4SmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 4))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 4))));
				lot4mtm.setSpread4InitialSmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 5)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStartSmS + 5))));
				lot4mtm.setSpread4QpSmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 6))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 6))));
				lot4mtm.setSpread4LotSmS(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 7))));
				lot4mtm.setProfit4SmSSpot(StringUtils.isBlank(String.valueOf(rec.get(iStartSmS + 8))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStartSmS + 8))));
				lot4mtm.setProfit4Futre(StringUtils.isBlank(String.valueOf(rec.get(54))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(54))));
				lot4mtm.setProfit4Spot(StringUtils.isBlank(String.valueOf(rec.get(55))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(55))));
				lot4mtm.setProfit(StringUtils.isBlank(String.valueOf(rec.get(56))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(56))));
				lot4mtm.setLotId(lot4mtm.getLotId4SmS());
				/**
				 * 各利润单价的计算
				 */
				if (lot4mtm.getQuantity4Sm().compareTo(BigDecimal.ZERO) != 0) {
					lot4mtm.setPriceOfProfit4BviBFuture(
							lot4mtm.getProfit4BviBFuture().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4BviSFuture(
							lot4mtm.getProfit4BviSFuture().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4SmBFuture(lot4mtm.getProfit4SmBFuture().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4SmSFuture(lot4mtm.getProfit4SmSFuture().divide(lot4mtm.getQuantity4Sm()));

					lot4mtm.setPriceOfProfit4BviBSpot(lot4mtm.getProfit4BviBSpot().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4BviSSpot(lot4mtm.getProfit4BviSSpot().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4SmBSpot(lot4mtm.getProfit4SmBSpot().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4SmSSpot(lot4mtm.getProfit4SmSSpot().divide(lot4mtm.getQuantity4Sm()));

					lot4mtm.setPriceOfProfit4Futre(lot4mtm.getProfit4Futre().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit4Spot(lot4mtm.getProfit4Spot().divide(lot4mtm.getQuantity4Sm()));
					lot4mtm.setPriceOfProfit(lot4mtm.getProfit().divide(lot4mtm.getQuantity4Sm()));
				} else {
					lot4mtm.setPriceOfProfit4BviBFuture(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4BviSFuture(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4SmBFuture(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4SmSFuture(BigDecimal.ZERO);

					lot4mtm.setPriceOfProfit4BviBSpot(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4BviSSpot(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4SmBSpot(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4SmSSpot(BigDecimal.ZERO);

					lot4mtm.setPriceOfProfit4Futre(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit4Spot(BigDecimal.ZERO);
					lot4mtm.setPriceOfProfit(BigDecimal.ZERO);
				}
				lot4mtms.add(lot4mtm);
			}
			ActionResult<java.util.List<Lot4MTM>> tempVar10 = new ActionResult<java.util.List<Lot4MTM>>();
			tempVar10.setSuccess(true);
			tempVar10.setTotal(lot4mtms.size());
			tempVar10.setData(lot4mtms);
			return tempVar10;

		} catch (Exception ex) {
			ActionResult<java.util.List<Lot4MTM>> tempVar11 = new ActionResult<java.util.List<Lot4MTM>>();
			tempVar11.setSuccess(false);
			tempVar11.setMessage(ex.getMessage());
			return tempVar11;
		}
	}

	/**
	 * 批次盈亏结算表含回购
	 * 
	 * @param dtStart
	 * @param dtEnd
	 * @param invoiceStartDate
	 * @param invoiceEndDate
	 * @param keyword
	 * @param currency
	 * @param spotType
	 * @param status
	 * @param legalId
	 * @param legalIds
	 * @param commodityId
	 * @param createdId
	 * @param invoiceStatus
	 * @param accountYear
	 * @param accountMonth
	 * @return
	 */
	@Override
	public ActionResult<List<Lot4MTM>> Lot4MTMQueryNew2(Date dtStart, Date dtEnd, Date invoiceStartDate,
			Date invoiceEndDate, String keyword, String currency, String spotType, String status, String legalId,
			String legalIds, String commodityId, String createdId, String invoiceStatus, String accountYear,
			String accountMonth) {

		Map<String, Object> param = new LinkedHashMap<>();
		param.put("dBegin", dtStart);
		param.put("dEnd", dtEnd);
		param.put("keyWork", keyword);
		param.put("currency", currency);
		param.put("spotType", spotType);
		param.put("status", status);
		param.put("legalId", legalId);
		param.put("legalIds", legalIds);
		param.put("commodityId", commodityId);
		param.put("dInvoiceBegin", invoiceStartDate);
		param.put("dInvoiceEnd", invoiceEndDate);

		String proc = "proc_Lot4MTMQuery_Profit2";
		List<Object[]> snBRecords = this.repository.ExecuteProcedureSql(proc, param);

		List<Lot4MTM> lot4mtms = new ArrayList<Lot4MTM>();
		int iStart = 0, iStartBviB = 5, iStartBviS = 22, iStartSmB = 34, iStartSmS = 45;
		int iStartRebuyS = 54, iStartRebuyB = 66;
		for (Object[] snBRecord : snBRecords) {
			Lot4MTM lot4mtm = new Lot4MTM();
			List<Object> rec = Arrays.asList(snBRecord);

			/**
			 * bvi 采购
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStartBviB))) {
				lot4mtm.setLotId4BviB((String) rec.get(iStartBviB));
			}
			lot4mtm.setFullNo4Bvi(String.valueOf(rec.get(iStartBviB + 1)));
			lot4mtm.setCustomerName4BviB((String) rec.get(iStartBviB + 2));
			lot4mtm.setInvoiceNo4BviB((String) rec.get(iStartBviB + 3));
			lot4mtm.setPrice4PositionBviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 4)));
			lot4mtm.setPrice4PricingBviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 5)));
			lot4mtm.setPricingQuantity4Bvi(DecimalUtil.getProcedureData(rec.get(iStartBviB + 6)));
			lot4mtm.setProfit4BviBFuture(DecimalUtil.getProcedureData(rec.get(iStartBviB + 7)));
			lot4mtm.setPremium4BviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 8)));
			lot4mtm.setEstimateFee4BviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 9)));
			lot4mtm.setRealFee4BviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 10)));
			lot4mtm.setSpread4InitialBviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 11)));
			lot4mtm.setSpread4QpBviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 12)));
			lot4mtm.setSpread4LotBviB(DecimalUtil.getProcedureData(rec.get(iStartBviB + 13)));
			lot4mtm.setLoading((String) rec.get(iStartBviB + 14));
			lot4mtm.setDeliveryTerm4Bvi((String) rec.get(iStartBviB + 15));
			lot4mtm.setProfit4BviBSpot(DecimalUtil.getProcedureData(rec.get(iStartBviB + 16)));

			/**
			 * bvi销售
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStartBviS))) {
				lot4mtm.setLotId4BviS((String) rec.get(iStartBviS));
			}
			lot4mtm.setFullNo4BviS((String) rec.get(iStartBviS + 1));
			lot4mtm.setCustomerName4BviS((String) rec.get(iStartBviS + 2));
			lot4mtm.setInvoiceNo4BviS((String) rec.get(iStartBviS + 3));
			lot4mtm.setProfit4BviSFuture(DecimalUtil.getProcedureData(rec.get(iStartBviS + 4)));
			lot4mtm.setPremium4BviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 5)));
			lot4mtm.setEstimateFee4BviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 6)));
			lot4mtm.setRealFee4BviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 7)));
			lot4mtm.setSpread4InitialBviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 8)));
			lot4mtm.setSpread4QpBviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 9)));
			lot4mtm.setSpread4LotBviS(DecimalUtil.getProcedureData(rec.get(iStartBviS + 10)));
			lot4mtm.setProfit4BviSSpot(DecimalUtil.getProcedureData(rec.get(iStartBviS + 11)));

			/**
			 * 采购
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStartSmB))) {
				lot4mtm.setLotId4SmB((String) rec.get(iStartSmB));
			}
			lot4mtm.setFullNo4SmB((String) rec.get(iStartSmB + 1));
			lot4mtm.setCustomerName4SmB((String) rec.get(iStartSmB + 2));
			lot4mtm.setInvoiceNo4SmB((String) rec.get(iStartSmB + 3));
			lot4mtm.setProfit4SmBFuture(DecimalUtil.getProcedureData(rec.get(iStartSmB + 4)));
			lot4mtm.setPremium4SmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 5)));
			lot4mtm.setEstimateFee4SmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 6)));
			lot4mtm.setRealFee4SmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 7)));
			lot4mtm.setSpread4InitialSmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 8)));
			lot4mtm.setSpread4QpSmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 9)));
			lot4mtm.setSpread4LotSmB(DecimalUtil.getProcedureData(rec.get(iStartSmB + 10)));
			lot4mtm.setProfit4SmBSpot(DecimalUtil.getProcedureData(rec.get(iStartSmB + 11)));

			/**
			 * sm销售
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStart))) {
				lot4mtm.setLotId4SmS((String) rec.get(iStart));
			}
			lot4mtm.setFullNo4Sm((String) rec.get(iStart + 1));
			lot4mtm.setQuantity4Sm(DecimalUtil.getProcedureData(rec.get(iStart + 2)));
			lot4mtm.setCustomerName4SmS((String) (rec.get(iStart + 3)));
			lot4mtm.setInvoiceNo4SmS((String) rec.get(iStart + 4));

			lot4mtm.setProfit4SmSFuture(DecimalUtil.getProcedureData(rec.get(iStartSmS + 1)));
			lot4mtm.setPremium4SmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 2)));
			lot4mtm.setEstimateFee4SmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 3)));
			lot4mtm.setRealFee4SmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 4)));
			lot4mtm.setSpread4InitialSmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 5)));
			lot4mtm.setSpread4QpSmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 6)));
			lot4mtm.setSpread4LotSmS(DecimalUtil.getProcedureData(rec.get(iStartSmS + 7)));
			lot4mtm.setProfit4SmSSpot(DecimalUtil.getProcedureData(rec.get(iStartSmS + 8)));

			/**
			 * 回购销售
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStartRebuyS))) {
				lot4mtm.setLotId4ReBuyS((String) rec.get(iStartRebuyS));
			}
			lot4mtm.setFullNo4ReBuyS((String) rec.get(iStartRebuyS + 1));
			lot4mtm.setCustomerName4ReBuyS((String) (rec.get(iStartRebuyS + 2)));
			lot4mtm.setInvoiceNo4ReBuyS((String) rec.get(iStartRebuyS + 3));
			lot4mtm.setProfit4ReBuySFuture(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 4)));
			lot4mtm.setPremium4ReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 5)));
			lot4mtm.setEstimateFee4ReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 6)));
			lot4mtm.setRealFee4ReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 7)));
			lot4mtm.setSpread4InitialReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 8)));
			lot4mtm.setSpread4QpReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 9)));
			lot4mtm.setSpread4LotReBuyS(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 10)));
			lot4mtm.setProfit4ReBuySSpot(DecimalUtil.getProcedureData(rec.get(iStartRebuyS + 11)));

			/**
			 * 回购采购
			 */
			if (StringUtils.isNotBlank((String) rec.get(iStartRebuyB))) {
				lot4mtm.setLotId4ReBuyB((String) rec.get(iStartRebuyB));
			}
			lot4mtm.setFullNo4ReBuyB((String) rec.get(iStartRebuyB + 1));
			lot4mtm.setCustomerName4ReBuyB((String) (rec.get(iStartRebuyB + 2)));

			lot4mtm.setInvoiceNo4ReBuyB((String) rec.get(iStartRebuyB + 3));
			lot4mtm.setProfit4ReBuyBFuture(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 4)));
			lot4mtm.setPremium4ReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 5)));
			lot4mtm.setEstimateFee4ReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 6)));
			lot4mtm.setRealFee4ReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 7)));
			lot4mtm.setSpread4InitialReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 8)));
			lot4mtm.setSpread4QpReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 9)));
			lot4mtm.setSpread4LotReBuyB(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 10)));
			lot4mtm.setProfit4ReBuyBSpot(DecimalUtil.getProcedureData(rec.get(iStartRebuyB + 11)));
			lot4mtm.setProfit4Futre(DecimalUtil.getProcedureData(rec.get(78)));
			lot4mtm.setProfit4Spot(DecimalUtil.getProcedureData(rec.get(79)));
			lot4mtm.setProfit(DecimalUtil.getProcedureData(rec.get(80)));
			lot4mtm.setLotId(lot4mtm.getLotId4SmS());

			lot4mtm.setBrandNames4Sms(String.valueOf(rec.get(81)));
			lot4mtm.setIsInvoiced((boolean) rec.get(82));

			/*
			 * lot4mtm.setDischarging(String.valueOf(rec.get(83)));
			 * lot4mtm.setAccountYear(String.valueOf(rec.get(84)));
			 * lot4mtm.setAccountMonth(String.valueOf(rec.get(85))); if
			 * (!StringUtils.isBlank(String.valueOf(rec.get(84))) &&
			 * !StringUtils.isBlank(String.valueOf(rec.get(85)))) {
			 * lot4mtm.setAccountDate(String.valueOf(rec.get(84)) + "-" +
			 * (String.valueOf(rec.get(85)).length() == 1 ? ("0" +
			 * String.valueOf(rec.get(85))) : String.valueOf(rec.get(85)))); }
			 * 
			 * lot4mtm.setInvoiceCreatedId(String.valueOf(rec.get(86)));
			 * lot4mtm.setInvoiceCreaterName(String.valueOf(rec.get(87)));
			 */

			/**
			 * 各利润单价的计算
			 */
			if (lot4mtm.getQuantity4Sm().compareTo(BigDecimal.ZERO) != 0) {
				lot4mtm.setPriceOfProfit4BviBFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4BviBFuture(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4BviSFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4BviSFuture(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4SmBFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4SmBFuture(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4SmSFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4SmSFuture(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4ReBuyBFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4ReBuyBFuture(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4ReBuySFuture(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4ReBuySFuture(), lot4mtm.getQuantity4Sm()));

				lot4mtm.setPriceOfProfit4BviBSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4BviBSpot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4BviSSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4BviSSpot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4SmBSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4SmBSpot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4SmSSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4SmSSpot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4ReBuyBSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4ReBuyBSpot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4ReBuySSpot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4ReBuySSpot(), lot4mtm.getQuantity4Sm()));

				lot4mtm.setPriceOfProfit4Futre(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4Futre(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit4Spot(
						DecimalUtil.divideForPrice(lot4mtm.getProfit4Spot(), lot4mtm.getQuantity4Sm()));
				lot4mtm.setPriceOfProfit(DecimalUtil.divideForPrice(lot4mtm.getProfit(), lot4mtm.getQuantity4Sm()));
			} else {
				lot4mtm.setPriceOfProfit4BviBFuture(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4BviSFuture(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4SmBFuture(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4SmSFuture(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4ReBuyBFuture(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4ReBuySFuture(BigDecimal.ZERO);

				lot4mtm.setPriceOfProfit4BviBSpot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4BviSSpot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4SmBSpot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4SmSSpot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4ReBuyBSpot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4ReBuySSpot(BigDecimal.ZERO);

				lot4mtm.setPriceOfProfit4Futre(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit4Spot(BigDecimal.ZERO);
				lot4mtm.setPriceOfProfit(BigDecimal.ZERO);
			}
			lot4mtms.add(lot4mtm);
		}
		/*
		 * // 结算 if (invoiceStatus.equals("Y")) { lot4mtms =
		 * lot4mtms.stream().filter(s -> s.getIsInvoiced() ==
		 * true).collect(Collectors.toList());
		 * 
		 * } else if (invoiceStatus.equals("N")) { lot4mtms =
		 * lot4mtms.stream().filter(s -> s.getIsInvoiced() ==
		 * false).collect(Collectors.toList()); } // 创建者 if (createdId != null)
		 * { lot4mtms = lot4mtms.stream().filter(s ->
		 * s.getInvoiceCreatedId().equals(createdId))
		 * .collect(Collectors.toList()); } // 入账年 if
		 * (!StringUtils.isBlank(accountYear)) { lot4mtms =
		 * lot4mtms.stream().filter(s -> s.getAccountYear().equals(accountYear))
		 * .collect(Collectors.toList()); } // 入账月 if
		 * (!StringUtils.isBlank(accountMonth)) { lot4mtms =
		 * lot4mtms.stream().filter(s ->
		 * s.getAccountMonth().equals(accountMonth))
		 * .collect(Collectors.toList()); }
		 */

		return new ActionResult<>(true, "", lot4mtms, new RefUtil(lot4mtms.size()));
	}

	/**
	 * 批次费用一览
	 * 
	 * @param date
	 * @param date2
	 * @param spotType
	 * @param currency
	 * @param string
	 * @param commodityId
	 * @param string2
	 * @param status
	 * @param legalIds
	 * @return
	 */
	@Override
	public ActionResult<List<Lot4FeesOverview>> Lot4Fees(Date dtStart, Date dtEnd, String keyWork, String currency,
			String spotType, String status, String legalId, String legalIds, String commodityId, int pageSize,
			int pageIndex) {
		try {
			List<Lot4FeesOverview> lstFees = new ArrayList<Lot4FeesOverview>();
			// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
			Criteria where = this.repository.CreateCriteria(Lot.class);
			where.add(Restrictions.ge("CreatedAt", dtStart));
			where.add(Restrictions.le("CreatedAt", dtEnd));
			if (!StringUtils.isBlank(keyWork)) {
				where.add(Restrictions.eq("FullNo", keyWork));
			}
			if (!StringUtils.isBlank(currency)) {
				where.add(Restrictions.eq("Currency", currency));
			}
			if (!StringUtils.isBlank(spotType)) {
				where.add(Restrictions.eq("SpotDirection", spotType));
			}
			// this.repository.GetPage(criteria, pageSize, pageIndex, sortBy,
			// orderBy, total
			RefUtil total = new RefUtil();
			List<Lot> lots = this.repository.GetPage(where, pageSize, pageIndex, null, null, total).getData();
			// List<Lot> lots =
			// this.repository.GetQueryable(Lot.class).where(where).toList();
			if (lots == null || lots.size() == 0) {
				return new ActionResult<>(false, "没有符合条件的批次.");
			}
			String lotIds = "";
			List<String> ids = new ArrayList<>();
			for (Lot lot : lots) {
				lotIds += ",'" + lot.getId() + "'";
				ids.add(lot.getId());
			}
			lotIds = lotIds.substring(1).trim();
			// 取费用所有数据
			DetachedCriteria dc = DetachedCriteria.forClass(Fee.class);
			dc.add(Restrictions.in("LotId", ids));
			List<Fee> fees = this.feeRepository.GetQueryable(Fee.class).where(dc).toList();

			for (Lot lot : lots) {
				Lot4FeesOverview lotfee = new Lot4FeesOverview();
				lotfee.setLotId(lot.getId());
				lotfee.setFullNo(lot.getFullNo());
				lotfee.setQuantity4Lot(lot.getQuantity());
				lotfee.setQuantity4Delivery(lot.getQuantityDelivered());
				CalcEstimateFees1(lotfee, fees);
				// lotIds += ",'" + lot.getId() + "' ";
				lstFees.add(lotfee);
			}

			Map<String, Object> param = new LinkedHashMap<>();
			param.put("begin", DateUtil.doSFormatDate("1901-01-01", "yyyy-MM-dd"));
			param.put("end", DateUtil.doSFormatDate("2798-01-01", "yyyy-MM-dd"));
			param.put("KeyWord", "");
			param.put("LegalIds", "");
			param.put("SpotType", "");
			param.put("currency", "");
			param.put("LotIds", lotIds);

			String proc = "proc_LotRealFees_Overview";
			List<Object[]> lotReals = this.repository.ExecuteProcedureSql(proc, param);
			for (Object[] lotReal : lotReals) {
				List<Object> rec = Arrays.asList(lotReal);

				if (StringUtils.isBlank(String.valueOf(rec.get(0)))) {
					continue;
				}
				Lot4FeesOverview lotfee = null;
				for (Lot4FeesOverview lo : lstFees) {
					if (lo.getLotId().equals(String.valueOf(rec.get(0)))) {
						lotfee = lo;
						continue;
					}
				}

				lotfee.setLotId(String.valueOf(rec.get(0)));
				lotfee.setFullNo(String.valueOf(rec.get(1)));
				lotfee.setQuantity4Lot((StringUtils.isBlank(String.valueOf(rec.get(2))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(2)))));
				lotfee.setQuantity4Delivery((StringUtils.isBlank(String.valueOf(rec.get(3))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(3)))));
				lotfee.setRealAmount4Trans((StringUtils.isBlank(String.valueOf(rec.get(4))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(4)))));
				lotfee.setRealAmount4Test((StringUtils.isBlank(String.valueOf(rec.get(5))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(5)))));
				lotfee.setRealAmount4Insu((StringUtils.isBlank(String.valueOf(rec.get(6))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(6)))));
				lotfee.setRealAmount4Cost((StringUtils.isBlank(String.valueOf(rec.get(7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(7)))));
				lotfee.setRealAmount4Bank((StringUtils.isBlank(String.valueOf(rec.get(8))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(8)))));
				lotfee.setRealAmount4Buy((StringUtils.isBlank(String.valueOf(rec.get(9))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(9)))));
				lotfee.setRealAmount4Fine((StringUtils.isBlank(String.valueOf(rec.get(10))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(10)))));
				lotfee.setRealAmount4Hedge((StringUtils.isBlank(String.valueOf(rec.get(11))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(11)))));
				lotfee.setRealAmount4Other((StringUtils.isBlank(String.valueOf(rec.get(12))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(12)))));
			}
			ActionResult<List<Lot4FeesOverview>> tempVar2 = new ActionResult<List<Lot4FeesOverview>>();
			tempVar2.setSuccess(true);
			tempVar2.setTotal(total.getTotal());
			tempVar2.setData(lstFees);
			return tempVar2;

		} catch (Exception ex) {
			ActionResult<List<Lot4FeesOverview>> tempVar3 = new ActionResult<List<Lot4FeesOverview>>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar3;
		}
	}

	public ActionResult<String> CalcEstimateFees1(Lot4FeesOverview LotFee, List<Fee> fees) {
		/**
		 * 不再通过类型区分，只要存在相关的费率配置就进行计算
		 */
		List<String> feeTypes = LeagalnFeeType.getSmSFeeType();
		for (String feeType : feeTypes) {
			/*
			 * String type = feeType; DetachedCriteria where =
			 * DetachedCriteria.forClass(Fee.class);
			 * where.add(Restrictions.eq("LotId", LotFee.getLotId()));
			 * where.add(Restrictions.eq("FeeCode", feeType));
			 * 
			 * Fee fee =
			 * this.feeRepository.GetQueryable(Fee.class).where(where).
			 * firstOrDefault();
			 */

			Fee fee = fees.stream().filter(
					f -> f.getLotId().equalsIgnoreCase(LotFee.getLotId()) && f.getFeeCode().equalsIgnoreCase(feeType))
					.findFirst().orElse(null);

			if (fee != null) {
				setEstimateFeeByType(feeType, LotFee, fee.getAmountEstimated(), fee.getRate(), GetFeeBasis(feeType));
			} else {
				setEstimateFeeByType(feeType, LotFee, BigDecimal.ZERO, BigDecimal.ZERO, GetFeeBasis(feeType));
			}
		}

		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setMessage("成功生成费用信息");
		return tempVar;
	}

	@Override
	public ActionResult<String> CalcEstimateFees(Lot4FeesOverview LotFee) {

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("Id", LotFee.getLotId()));
		Lot curLot = this.repository.GetQueryable(Lot.class).where(where).firstOrDefault();

		if (curLot == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("该批次无抬头信息");
			return tempVar;
		}

		DetachedCriteria where2 = DetachedCriteria.forClass(Legal.class);
		where2.add(Restrictions.eq("Id", curLot.getLegalId()));
		Legal legal = this.legalRepository.GetQueryable(Legal.class).where(where2).firstOrDefault();

		if (legal == null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("该批次无抬头信息");
			return tempVar2;
		}

		DetachedCriteria where3 = DetachedCriteria.forClass(Contract.class);
		where3.add(Restrictions.eq("Id", curLot.getContractId()));
		Contract curContract = this.contractRepository.GetQueryable(Contract.class).where(where3).firstOrDefault();
		if (curContract == null) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("该批次订单信息不存在");
			return tempVar3;
		}
		// 不再通过类型区分，只要存在相关的费率配置就进行计算
		List<String> feeTypes = LeagalnFeeType.getSmSFeeType();
		for (String feeType : feeTypes) {
			String type = feeType;
			List<FeeSetup> feeSetups;
			if (type.equals(FeeCode.Transportation) || type.equals(FeeCode.Other)) // 运输费不需要区分抬头和订单类型
			{
				DetachedCriteria where4 = DetachedCriteria.forClass(FeeSetup.class);
				where4.add(Restrictions.eq("FeeType", type));
				feeSetups = this.feeSetupRepository.GetQueryable(FeeSetup.class).where(where4).toList();
			} else {
				DetachedCriteria where5 = DetachedCriteria.forClass(FeeSetup.class);
				where5.add(Restrictions.eq("FeeType", type));
				where5.add(Restrictions.eq("LegalId", curLot.getLegalId()));
				where5.add(Restrictions.eq("SpotDirection", curContract.getSpotDirection()));
				feeSetups = this.feeSetupRepository.GetQueryable(FeeSetup.class).where(where5).toList();
			}

			if (feeSetups.size() > 0) {
				/**
				 * 取费用费率数据
				 */
				StringBuilder rate = new StringBuilder();

				BigDecimal amount = CalcAmount(curLot, feeSetups, type, rate);

				setEstimateFeeByType(type, LotFee, amount, new BigDecimal(rate.toString()), GetFeeBasis(type));
			}
			if (feeSetups.size() <= 0) {
				setEstimateFeeByType(type, LotFee, BigDecimal.ZERO, BigDecimal.ZERO, "");

			}
		}

		ActionResult<String> tempVar4 = new ActionResult<String>();
		tempVar4.setSuccess(true);
		tempVar4.setMessage("成功生成费用信息");
		return tempVar4;
	}

	@Override
	public void CalcRealFee(Lot lot, Lot4FeesOverview LotFee) {

		if (lot == null) {
			return;
		}

		/**
		 * 费用计算
		 */
		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		where.add(Restrictions.eq("PFA", InvoiceType.Final));
		where.add(Restrictions.eq("PFA", InvoiceType.Adjust));
		where.add(Restrictions.eq("PFA", InvoiceType.Provisional));
		where.add(Restrictions.eq("PFA", InvoiceType.MultiLots));
		where.add(Restrictions.eq("PFA", InvoiceType.SummaryNote));
		List<Invoice> funds = this.invoiceRepository.GetQueryable(Invoice.class).where(where).toList();

		// 按检验批次登记的实际费用
		DetachedCriteria where2 = DetachedCriteria.forClass(Storage.class);
		where2.add(Restrictions.isNotNull("LotId"));
		where2.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> noteByTest = this.storageRepository.GetQueryable(Storage.class).where(where2).toList();

		// 来自信用证的费用
		List<LC> lcs = this.lcRepository.GetQueryable(LC.class).toList();
		List<LC> Lcs = lcs.stream().filter(x -> x.getInvoices().size() > 0).collect(Collectors.toList());

		for (LC lc : Lcs) {
			List<Invoice> invoices = lc.getInvoices();
			for (Invoice y : invoices) {
				if (!y.getLotId().equalsIgnoreCase(lot.getId())) {
					Lcs.remove(lc);
					continue;
				}
			}
		}

		List<String> feeCodeType = LeagalnFeeType.getSmSFeeType();
		List<Invoice> temp = new ArrayList<Invoice>();
		for (Invoice f : funds) {
			if (!feeCodeType.contains(f.getFeeCode())) {
				temp.add(f);
			}
		}

		BigDecimal amountDone4Other = BigDecimal.ZERO;
		for (Invoice invoice : temp) {
			amountDone4Other = amountDone4Other.add(invoice.getAmount());
		}

		/**
		 * 信证中的费用
		 */
		BigDecimal lcfee = BigDecimal.ZERO;
		DetachedCriteria where4 = DetachedCriteria.forClass(Legal.class);
		where4.add(Restrictions.eq("Code", "SM"));
		Legal sm = this.legalRepository.GetQueryable(Legal.class).where(where4).firstOrDefault();

		for (LC l : Lcs) {
			// 计算出该信用证中SM的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfSM = l.getInvoices().stream().filter(x -> x.getLegalId().equals(sm.getId()))
					.collect(Collectors.toList());

			List<Invoice> invoicesSMOfA = invoicesOfSM.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesSMOfA.size() > 0) {
				for (Invoice x : invoicesOfSM) {
					for (Invoice i : invoicesSMOfA) {
						if (x.getAdjustId().equals(i.getId())) {
							invoicesOfSM.remove(x);
						}
					}
				}
			}
			// 计算出该信用证中BVI的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfBVI = l.getInvoices().stream().filter(x -> x.getLegalId().equals(sm.getId()))
					.collect(Collectors.toList());

			List<Invoice> invoicesBVIOfA = invoicesOfBVI.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesBVIOfA.size() > 0) {
				for (Invoice x : invoicesOfBVI) {
					for (Invoice i : invoicesBVIOfA) {
						if (x.getAdjustId().equals(i.getId())) {
							invoicesOfBVI.remove(x);
						}
					}
				}
			}
			// 计算出信用证中该批次的发票

			List<Invoice> invoicesOfLot = l.getInvoices().stream().filter(x -> x.getLotId().equals(lot.getId()))
					.collect(Collectors.toList());

			List<Invoice> invoicesLotOfA = invoicesOfLot.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesLotOfA.size() > 0) {
				for (Invoice x : invoicesOfLot) {
					for (Invoice i : invoicesLotOfA) {
						if (x.getAdjustId().equals(i.getId())) {
							invoicesOfLot.remove(x);
						}

					}
				}

			}
			BigDecimal invoicesOfLotSum = BigDecimal.ZERO;
			for (Invoice iol : invoicesOfLot) {
				invoicesOfLotSum = invoicesOfLotSum.add(iol.getQuantity());
			}
			BigDecimal invoicesOfSMSum = BigDecimal.ZERO;
			for (Invoice ios : invoicesOfSM) {
				invoicesOfSMSum = invoicesOfSMSum.add(ios.getQuantity());
			}
			BigDecimal invoicesOfBVISum = BigDecimal.ZERO;
			for (Invoice iom : invoicesOfBVI) {
				invoicesOfBVISum = invoicesOfBVISum.add(iom.getQuantity());
			}

			// 商贸
			if (lot.getLegalId().equals(sm.getId())) {
				/**
				 * 开证费用+承兑费
				 */
				lcfee = lcfee.add(l.getKzAmount().multiply(invoicesOfLotSum).divide(invoicesOfSMSum)
						.add(l.getCdAmount().multiply(invoicesOfLotSum).divide(invoicesOfSMSum)));
			} else {
				/**
				 * 贴现费 + 议付费
				 */
				lcfee = lcfee.add(l.getTxAmount().multiply(invoicesOfLotSum).divide(invoicesOfBVISum)
						.add(l.getYfAmount().multiply(invoicesOfLotSum).divide(invoicesOfBVISum)));

			}
		}

		/**
		 * 按批次检验费用,如果预估费用中没有该类型的检验费，则费用归集到其它类型
		 */
		BigDecimal sumAmount = BigDecimal.ZERO;
		BigDecimal sumTransAmount = BigDecimal.ZERO;
		BigDecimal sumInsuAmount = BigDecimal.ZERO;
		BigDecimal sumCost = BigDecimal.ZERO;
		BigDecimal sumOhters = BigDecimal.ZERO;
		BigDecimal sumBankDocFee = BigDecimal.ZERO;
		BigDecimal sumBuyDocFee = BigDecimal.ZERO;
		BigDecimal sumDisputeFine = BigDecimal.ZERO;
		BigDecimal sumHedgeFee = BigDecimal.ZERO;
		for (Storage notefee : noteByTest) {
			for (SummaryFees summaryFee : notefee.getSummaryFeesList()) {

				DetachedCriteria where5 = DetachedCriteria.forClass(Invoice.class);
				where5.add(Restrictions.eq("Id", summaryFee.getInvoiceId()));
				where5.add(Restrictions.eq("PFA", InvoiceType.SummaryNote));
				Invoice invoice = this.invoiceRepository.GetQueryable(Invoice.class).where(where5).firstOrDefault();

				if (invoice != null) {
					switch (invoice.getFeeCode()) {
					case FeeCode.Test: // 检验费

						sumAmount = sumAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));

						if (!feeCodeType.contains(FeeCode.Test)) {
							amountDone4Other = amountDone4Other.add(sumAmount != null ? sumAmount : BigDecimal.ZERO);
						}
						break;
					case FeeCode.Transportation: // 运输费
						sumTransAmount = sumTransAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.Transportation)) {
							amountDone4Other = amountDone4Other
									.add(sumTransAmount != null ? sumTransAmount : BigDecimal.ZERO);
						}
						break;
					case FeeCode.Insurance: // 保险费
						sumInsuAmount = sumInsuAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.Insurance)) {
							amountDone4Other = amountDone4Other
									.add(sumInsuAmount != null ? sumInsuAmount : BigDecimal.ZERO);
						}
						break;
					case FeeCode.Cost: // 资金成本
						sumCost = sumCost.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.Cost)) {
							amountDone4Other = amountDone4Other.add(sumCost != null ? sumCost : BigDecimal.ZERO);
						}
						break;
					case FeeCode.BankDocumentsFee: // 银行文件费
						sumBankDocFee = sumBankDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.BankDocumentsFee)) {
							amountDone4Other = amountDone4Other
									.add(sumBankDocFee != null ? sumBankDocFee : BigDecimal.ZERO);
						}
						break;
					case FeeCode.BuyDocumentsFee: // 采购文件费
						sumBuyDocFee = sumBuyDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.BuyDocumentsFee)) {
							amountDone4Other = amountDone4Other
									.add(sumBuyDocFee != null ? sumBuyDocFee : BigDecimal.ZERO);
						}
						break;
					case FeeCode.DisputeFine: // 争议罚款费
						sumDisputeFine = sumDisputeFine.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.DisputeFine)) {
							amountDone4Other = amountDone4Other
									.add(sumDisputeFine != null ? sumDisputeFine : BigDecimal.ZERO);
						}
						break;
					case FeeCode.HedgeFee: // 套保费
						sumHedgeFee = sumHedgeFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (!feeCodeType.contains(FeeCode.HedgeFee)) {
							amountDone4Other = amountDone4Other
									.add(sumHedgeFee != null ? sumHedgeFee : BigDecimal.ZERO);
						}
						break;
					default: // 其他所有费用
						sumOhters = sumOhters.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						break;
					}

				}
			}
		}

		for (String fee : feeCodeType) {

			List<Invoice> f_funds = funds.stream().filter(x -> x.getFeeCode().equals(fee)).collect(Collectors.toList());
			BigDecimal sumAmt = BigDecimal.ZERO;
			for (Invoice invoice : f_funds) {
				sumAmt = sumAmt.add(invoice.getAmount());
			}

			if (fee == FeeCode.Cost) // 资金成本（实际发生来自信用证）
			{
				LotFee.setRealAmount4Cost(sumAmt.add(lcfee).add(sumCost));
			} else if (fee == FeeCode.Test) // 检验费时，实际费需加上按检验批次录入的检验费
			{
				LotFee.setRealAmount4Test(sumAmt.add(sumAmount));
			} else if (fee == FeeCode.Other) // 其他费用
			{
				LotFee.setRealAmount4Other(sumAmt.add(amountDone4Other).add(sumOhters));
			} else if (fee == FeeCode.Transportation) // 运输费用
			{
				LotFee.setRealAmount4Trans(sumAmt.add(sumTransAmount));
			} else if (fee == FeeCode.Insurance) // 保险费
			{
				LotFee.setRealAmount4Insu(sumAmt.add(sumInsuAmount));
			} else if (fee == FeeCode.BankDocumentsFee) // 银行文件费
			{
				LotFee.setRealAmount4Bank(sumAmt.add(sumBankDocFee));
			} else if (fee == FeeCode.BuyDocumentsFee) // 采购文件费
			{
				LotFee.setRealAmount4Buy(sumAmt.add(sumBuyDocFee));
			} else if (fee == FeeCode.DisputeFine) // 争议罚款费
			{
				LotFee.setRealAmount4Fine(sumAmt.add(sumDisputeFine));
			} else if (fee == FeeCode.HedgeFee) // 套保费
			{
				LotFee.setRealAmount4Hedge(sumAmt.add(sumHedgeFee));
			}
		}

	}

	/**
	 * 实时MTM报表
	 * 
	 * @param date
	 * @param date2
	 * @param spotType
	 * @param currency
	 * @param spotDirection
	 * @param string
	 * @param commodityId
	 * @param string2
	 * @param status
	 * @param legalIds
	 * @return
	 */
	@Override
	public ActionResult<List<Lot4MTM>> Lot4MTMQuery3(Date dtStart, Date dtEnd, String keyWork, String currency,
			String spotType, String status, String legalId, String legalIds, String commodityId, String spotDirection) {

		Map<String, Object> param = new LinkedHashMap<>();
		param.put("dBegin", dtStart);
		param.put("dEnd", dtEnd);
		param.put("keyWork", keyWork);
		param.put("currency", currency);
		param.put("spotType", spotType);
		param.put("status", status);
		param.put("legalId", legalId);
		param.put("legalIds", legalIds);
		param.put("commodityId", commodityId);
		param.put("spotDirection", spotDirection);
		param.put("commodityId", commodityId);
		param.put("spotDirection", spotDirection);

		try {
			String proc = "proc_Lot4MTMQuery_LotByLot";
			List<Object[]> snBRecords = this.repository.ExecuteProcedureSql(proc, param);

			java.util.ArrayList<Lot4MTM> lot4mtms = new java.util.ArrayList<Lot4MTM>();
			int iStart = 2;
			for (Object[] snBRecord : snBRecords) {
				Lot4MTM lot4mtm = new Lot4MTM();

				List<Object> rec = Arrays.asList(snBRecord);

				/**
				 * bvi 采购
				 */
				lot4mtm.setFullNo4Bvi(String.valueOf(rec.get(iStart)));
				lot4mtm.setQuantity4Bvi(StringUtils.isBlank(String.valueOf(rec.get(iStart + 1))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 1))));
				lot4mtm.setCustomerName4BviB((String) rec.get(iStart + 2));
				lot4mtm.setPricingQuantity4Bvi(StringUtils.isBlank(String.valueOf(rec.get(iStart + 3)))
						? BigDecimal.ZERO : new BigDecimal(String.valueOf(rec.get(iStart + 3))));
				lot4mtm.setPrice4PositionBviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 4))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 4))));
				lot4mtm.setPrice4PricingBviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 5))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 5))));
				lot4mtm.setProfit4BviBFuture(StringUtils.isBlank(String.valueOf(rec.get(iStart + 6))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 6))));
				lot4mtm.setPriceOfProfit4BviBFuture(lot4mtm.getQuantity4Bvi().compareTo(BigDecimal.ZERO) == 0
						? BigDecimal.ZERO : lot4mtm.getProfit4BviBFuture().divide(lot4mtm.getQuantity4Bvi()));
				lot4mtm.setPremium4BviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 7))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 7))));
				lot4mtm.setEstimateFee4BviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 8))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 8))));
				lot4mtm.setSpread4InitialBviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 9))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 9))));
				lot4mtm.setSpread4QpBviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 10))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 10))));
				lot4mtm.setSpread4LotBviB(StringUtils.isBlank(String.valueOf(rec.get(iStart + 11))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 11))));
				lot4mtm.setProfit4BviBSpot(StringUtils.isBlank(String.valueOf(rec.get(iStart + 12))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 12))));
				lot4mtm.setPriceOfProfit4BviBSpot(lot4mtm.getQuantity4Bvi().compareTo(BigDecimal.ZERO) == 0
						? BigDecimal.ZERO : lot4mtm.getProfit4BviBSpot().divide(lot4mtm.getQuantity4Bvi()));
				lot4mtm.setProfit(StringUtils.isBlank(String.valueOf(rec.get(iStart + 13))) ? BigDecimal.ZERO
						: new BigDecimal(String.valueOf(rec.get(iStart + 13))));
				lot4mtm.setPriceOfProfit(lot4mtm.getQuantity4Bvi().compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
						: lot4mtm.getProfit().divide(lot4mtm.getQuantity4Bvi()));

				if (!StringUtils.isBlank(String.valueOf(rec.get(iStart + 14)))) {
					lot4mtm.setLotId(String.valueOf(rec.get(iStart + 14)));
				}

				if (!StringUtils.isBlank(String.valueOf(rec.get(iStart + 15)))) {
					lot4mtm.setIsInvoiced(Boolean.valueOf((String.valueOf(rec.get(iStart + 15)))));
				}

				if (!StringUtils.isBlank(String.valueOf(rec.get(iStart + 16)))) {
					lot4mtm.setIsCustInvoiced(Boolean.valueOf((String.valueOf(rec.get(iStart + 16)))));
				}

				lot4mtms.add(lot4mtm);
			}
			if (!StringUtils.isBlank(spotType)) {
				DetachedCriteria where = DetachedCriteria.forClass(Contract.class);
				where.add(Restrictions.eq("SpotType", spotType));
				List<Contract> contracts = this.contractRepository.GetQueryable(Contract.class).where(where).toList();
				List<String> contract = contracts.stream().map(s -> s.getHeadNo()).collect(Collectors.toList());
				for (Lot4MTM mtm : lot4mtms) {
					if (!contract.contains(mtm.getFullNo4Bvi().split("/")[0])) {
						lot4mtms.remove(mtm);
					}
				}
			}
			ActionResult<List<Lot4MTM>> tempVar11 = new ActionResult<List<Lot4MTM>>();
			tempVar11.setSuccess(true);
			tempVar11.setTotal(lot4mtms.size());
			tempVar11.setData(lot4mtms);
			return tempVar11;

		} catch (Exception ex) {
			ActionResult<List<Lot4MTM>> tempVar12 = new ActionResult<List<Lot4MTM>>();
			tempVar12.setSuccess(false);
			tempVar12.setMessage(ex.getMessage());
			return tempVar12;
		}
	}

	/**
	 * 保存入帐年月
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public ActionResult<String> SaveAccountDateByLot(Lot lot) {
		try {
			if (lot == null || lot.getId() == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("参数错误！");
				return tempVar;
			}
			DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
			where.add(Restrictions.eq("Id", lot.getId()));
			Lot lot1 = this.repository.GetQueryable(Lot.class).where(where).firstOrDefault();

			if (lot1 == null) {
				ActionResult<String> tempVar2 = new ActionResult<String>();
				tempVar2.setSuccess(false);
				tempVar2.setMessage("数据错误！");
				return tempVar2;
			}
			lot1.setAccountYear(lot.getAccountYear());
			lot1.setAccountMonth(lot.getAccountMonth());
			lot.setUpdatedAt(lot.getUpdatedAt());
			lot1.setUpdatedBy(lot.getUpdatedBy());
			lot1.setUpdatedId(lot.getUpdatedId());
			this.repository.SaveOrUpdate(lot1);
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(true);
			tempVar3.setMessage("保存成功！");
			return tempVar3;
		} catch (Exception ex) {
			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(ex.getMessage());
			return tempVar4;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param criteria
	 * @param pageSize
	 * @param pageIndex
	 * @param sortBy
	 * @param orderBy
	 * @param total
	 * @return
	 */
	@Override
	public List<Lot> Lots(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {
		return this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	/**
	 * 分页查询
	 * 
	 * @param criteria
	 * @param pageSize
	 * @param pageIndex
	 * @param sortBy
	 * @param orderBy
	 * @param total
	 * @return
	 */
	@Override
	public List<Lot> Lots4Hedged(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {
		List<Lot> lots = this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
		if (lots != null && lots.size() > 0) {
			List<String> lotIds = lots.stream().map(x -> x.getId()).collect(Collectors.toList());
			DetachedCriteria hedgeddc = DetachedCriteria.forClass(Position.class);
			hedgeddc.add(Restrictions.in("LotId", lotIds));
			List<Position> positions = this.positionRepository.GetQueryable(Position.class).where(hedgeddc).toList();
			for (Lot lot : lots) {
				if (lot.getContract() != null) {
					lot.setHedgeRadioView(lot.getContract().getHedgeRatio());
				}
				List<Position> positions4Lot = positions.stream().filter(x -> x.getLotId().equals(lot.getId()))
						.collect(Collectors.toList());
				if (positions4Lot != null && positions4Lot.size() > 0) {
					BigDecimal amount4PositionsOfLot = new BigDecimal(
							positions4Lot.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
					lot.setAvgPrice4Hedged(
							amount4PositionsOfLot.divide(lot.getQuantityHedged(), 5, RoundingMode.HALF_UP));
				} else {
					lot.setAvgPrice4Hedged(BigDecimal.ZERO);
				}
			}
		}
		return lots;
	}

	/**
	 * 不带分页
	 * 
	 * @return
	 */
	@Override
	public List<Lot> Lots() {
		return this.repository.GetList(Lot.class);
	}

	@Override
	public ActionResult<String> SplitLot(CpSplitLot cpSplitLot, String userId) {
		return SplitLot(userId, cpSplitLot);
	}

	public void UpdateQuantityPerDay(Lot lot) {
		if (lot == null) {
			return;
		}

		Date startDate = new Date();
		Date endDate = new Date();

		if (lot.getMajorStartDate() != null && lot.getMajorEndDate() != null) {
			startDate = lot.getMajorStartDate();
			endDate = lot.getMajorEndDate();

			// 计算交易天数
			DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
			where.add(Restrictions.eq("IsTrade", true));
			where.add(Restrictions.ge("TradeDate", startDate));
			where.add(Restrictions.le("TradeDate", endDate));
			where.add(Restrictions.eq("MarketId", lot.getMajorMarketId()));
			List<Calendar> calendars = this.calendarRepository.GetQueryable(Calendar.class).where(where).toList();

			// 如果交易日历中没有记录，直接返回
			if (calendars == null || calendars.size() == 0) {
				return;
			}

			lot.setMajorDays(calendars.size());

			// 主价点价的每天数量
			lot.setQtyPerMainDay(
					lot.getQuantity().divide(new BigDecimal(lot.getMajorDays()), BigDecimal.ROUND_HALF_EVEN));

		}

		if (lot.getPremiumStartDate() != null && lot.getPremiumEndDate() != null) {
			startDate = lot.getPremiumStartDate();
			endDate = lot.getPremiumEndDate();
			DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
			where.add(Restrictions.eq("IsTrade", true));
			where.add(Restrictions.ge("TradeDate", startDate));
			where.add(Restrictions.le("TradeDate", endDate));
			where.add(Restrictions.eq("MarketId", lot.getMajorMarketId()));
			List<Calendar> calendars = this.calendarRepository.GetQueryable(Calendar.class).where(where).toList();

			if (calendars == null || calendars.size() == 0) {
				return;
			}
			lot.setPremiumDays(calendars.size());
			lot.setQtyPerPremiumDay(
					lot.getQuantity().divide(new BigDecimal(lot.getPremiumDays()), BigDecimal.ROUND_HALF_EVEN));
		}
	}

	/**
	 * 重新计算批次相关标记
	 * 
	 * @param lot
	 */
	private void ReCalculateLotMarks(Lot lot) {
		/**
		 * 溢短装
		 */
		if (lot.getMoreOrLessBasis().equals("OnQuantity")) {
			lot.setQuantityLess(lot.getQuantity().subtract(lot.getMoreOrLess()));
			lot.setQuantityMore(lot.getQuantity().add(lot.getMoreOrLess()));
		} else if (lot.getMoreOrLessBasis().equals("OnPercentage")) {
			lot.setQuantityLess(lot.getQuantity()
					.multiply(new BigDecimal(1).subtract(lot.getMoreOrLess().divide(new BigDecimal(100)))));
			lot.setQuantityMore(
					lot.getQuantity().multiply(new BigDecimal(1).add(lot.getMoreOrLess().divide(new BigDecimal(100)))));
		}
		/**
		 * 更新批次是否首发货完成
		 */
		List<Lot> lots = commonService.setDelivery4Lot(lot);
		List<Lot> lots1 = commonService.setInvoice4Lot(lot);
		lot.setIsPriced(commonService.IsPriced4Lot(lot));
		lot.setIsHedged(commonService.IsHedged4Lot(lot));

		commonService.UpdateDeliveryStatus(lots); // 更新拆分批次的交货状态
		commonService.UpdateInvoiceStatus(lots1); // 更新拆分批次的开票状态

		if (lot.getBrands() != null && lot.getBrands().size() > 0) {
			List<Brand> tempBrands = new ArrayList<Brand>();
			for (Brand brand : lot.getBrands()) {
				DetachedCriteria where = DetachedCriteria.forClass(Brand.class);
				where.add(Restrictions.eq("Id", brand.getId()));
				Brand temp = this.brandRepository.GetQueryable(Brand.class).where(where).firstOrDefault();

				if (temp != null) {
					tempBrands.add(temp);
				} else {
					tempBrands.add(brand);
				}
			}
			lot.setBrands(tempBrands);
		}

	}

	/**
	 * 更新批次及拆出批次原数量
	 * 
	 * @param lot
	 */
	private void UpdateQuantityOriginal(Lot lot) {
		if (lot.getId() == null) {
			return;
		}

		/*
		 * if (lot.getIsSplitted() != null && lot.getIsSplitted()) { //
		 * 拆出来的记录，修改了批次数量，需要更新原批次的原始数量,如定价方式是固定价，则点价数量等于原始数量 DetachedCriteria
		 * where = DetachedCriteria.forClass(Lot.class);
		 * where.add(Restrictions.eq("SplitFromId", lot.getSplitFromId()));
		 * where.add(Restrictions.ne("Id", lot.getId())); List<Lot> splits =
		 * this.repository.GetQueryable(Lot.class).where(where).toList(); for
		 * (Lot split : splits) { if
		 * (split.getMajorType().equals(MajorType.Fix)) {
		 * split.setQuantityPriced(lot.getQuantityOriginal()); }
		 * split.setQuantityOriginal(lot.getQuantityOriginal());
		 * this.repository.SaveOrUpdate(split); }
		 * 
		 * DetachedCriteria where2 = DetachedCriteria.forClass(Lot.class);
		 * where2.add(Restrictions.eq("Id", lot.getId()));
		 * where2.add(Restrictions.ne("Id", lot.getSplitFromId())); Lot source =
		 * this.repository.GetQueryable(Lot.class).where(where2).firstOrDefault(
		 * );
		 * 
		 * if (source != null) { if
		 * (source.getMajorType().equals(MajorType.Fix)) {
		 * source.setQuantityPriced(lot.getQuantityOriginal()); }
		 * source.setQuantityOriginal(lot.getQuantityOriginal());
		 * this.repository.SaveOrUpdate(source); } } else if
		 * (lot.getIsSourceOfSplitted() != null && lot.getIsSourceOfSplitted())
		 * { DetachedCriteria where3 = DetachedCriteria.forClass(Lot.class);
		 * where3.add(Restrictions.eq("SplitFromId", lot.getId())); List<Lot>
		 * splits =
		 * this.repository.GetQueryable(Lot.class).where(where3).toList(); for
		 * (Lot split : splits) { if
		 * (split.getMajorType().equals(MajorType.Fix)) {
		 * split.setQuantityPriced(lot.getQuantityOriginal()); }
		 * split.setQuantityOriginal(lot.getQuantityOriginal());
		 * this.repository.SaveOrUpdate(split); } }
		 */

	}

	/**
	 * 删除：临单的批次，同时删除整个合同
	 * 
	 * @param lotId
	 */
	private void DeleteDataLotRelated(String lotId) {
		/**
		 * 删除该批次有关的全部业务记录
		 */
		if (lotId == null) {
			return;
		}
		// String sql = String.format("Delete Physical.Note where LotId = '%s'",
		// lotId);
		// 数据中没有这个表
		// this.repository.ExecuteNonQuery(sql);

		// 财务有关
		String sql = String.format("Delete from Physical.Invoice where LotId = '%s'", lotId);
		this.repository.ExecuteNonQuery(sql);
		/*
		 * sql = String.format("Delete from Physical.LC where LotId = '%s'",
		 * lotId); this.repository.ExecuteNonQuery(sql);
		 */

		// 报表有关 数据中没有这个表
		/*
		 * sql = String.format("Delete from Report.ModelPnL where LotId = '%s'",
		 * lotId); this.repository.ExecuteNonQuery(sql);
		 */
		// 品牌 相关 数据中没有这个表
		sql = String.format("Delete from Physical.LotBrand where LotId = '%s'", lotId);
		this.repository.ExecuteNonQuery(sql);

		// 期货有关
		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.eq("LotId", lotId));
		List<Position> positionsOfLot = this.positionRepository.GetQueryable(Position.class).where(where).toList();

		for (Position p : positionsOfLot) {
			p.setLotId(null);
			this.positionRepository.SaveOrUpdate(p);
		}
		sql = String.format("Delete from Physical.Pricing where LotId = '%s'", lotId);
		this.repository.ExecuteNonQuery(sql);
		sql = String.format("Delete from Physical.PricingRecord where LotId = '%s'", lotId);
		this.repository.ExecuteNonQuery(sql);

		// 数据中没有这个表
		/*
		 * sql = String.format(
		 * "Delete from Physical.PorfolioLot where LotId = '%s'", lotId);
		 * this.repository.ExecuteNonQuery(sql);
		 */

		Lot lot = this.repository.getOneById(lotId, Lot.class);

		/**
		 * 重置批次上一流程的交付明细的IsOut标记
		 */
		if (lot != null) {
			DetachedCriteria where3 = DetachedCriteria.forClass(Storage.class);
			where3.add(Restrictions.eq("LotId", lot.getId()));
			List<Storage> storagesOfLot = this.storageRepository.GetQueryable(Storage.class).where(where3).toList();

			if (lot.getSpotDirection().equals("S")) {
				for (Storage s : storagesOfLot) {
					Storage temp = this.storageRepository.GetQueryable(Storage.class).where(
							DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("CounterpartyId3", s.getId())))
							.firstOrDefault();
					if (temp != null) {
						temp.setIsOut(false);
						this.storageRepository.SaveOrUpdate(temp);
					}
				}
			} else if (lot.getLegal() != null && !lot.getLegal().getCode().equals("SB")
					&& lot.getSpotDirection().equals("B")) {
				for (Storage s : storagesOfLot) {
					Storage temp = this.storageRepository.GetQueryable(Storage.class).where(
							DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("CounterpartyId3", s.getId())))
							.firstOrDefault();
					if (temp != null) {
						temp.setIsOut(false);
						this.storageRepository.SaveOrUpdate(temp);
					}
				}
			}
		}
		sql = String.format("Delete from Physical.Storage where LotId = '%s'", lotId);
		this.repository.ExecuteNonQuery(sql);
	}

	public String GetFeeBasis(String feeType) {

		String feeBasis = "";
		switch (feeType) {
		case FeeCode.Transportation:
		case FeeCode.Test:
			feeBasis = "OnQuantity";
			break;
		case FeeCode.Insurance:
		case FeeCode.Cost:
		case FeeCode.Other:
			feeBasis = "OnAmount";
			break;
		default:
			feeBasis = "OnQuantity";
			break;
		}
		return feeBasis;
	}

	public BigDecimal CalcAmount(Lot curLot, List<FeeSetup> feeSetups, String feeType, StringBuilder rate) {
		BigDecimal amount = BigDecimal.ZERO;
		rate.append("0");
		switch (feeType) {
		case FeeCode.Transportation:
			FeeSetup find = feeSetups.stream().filter(x -> x.getLoading().equals(curLot.getLoading())
					&& x.getDischarging().equals(curLot.getDischarging())).findFirst().orElse(null);
			if (find != null) {
				rate.setLength(0);
				rate.append(find.getPrice());
				amount = curLot.getQuantity().multiply(find.getPrice());
			}
			break;
		case FeeCode.Test:
			BigDecimal price0 = feeSetups.get(0).getPrice();
			if (price0 != null) {
				amount = curLot.getQuantity().multiply(price0);
				rate.setLength(0);
				rate.append(price0);
			}
			break;
		case FeeCode.Insurance: // 待调整，取发票金额计算（改成取合同签订日期现货价格）//20150709
								// 改成取ruter的行情价格

			BigDecimal price = feeSetups.get(0).getPrice();

			DetachedCriteria where = DetachedCriteria.forClass(Contract.class);
			where.add(Restrictions.eq("Id", curLot.getContractId()));
			Contract curContract = this.contractRepository.GetQueryable(Contract.class).where(where).firstOrDefault();

			if (price != null) {
				rate.setLength(0);
				rate.append(price);
			}
			if (curContract != null) {
				// 由于crrc时2015-07-28初始化的，这之前没有reuter行情数据
				if (curContract.getTradeDate().before(DateUtil.doSFormatDate("2015-07-29", "yyyy-MM-dd"))
						|| curContract.getTradeDate().equals(DateUtil.doSFormatDate("2015-07-29", "yyyy-MM-dd"))) {
					BigDecimal M2MPrice = new BigDecimal(5500);
					amount = M2MPrice.multiply(curLot.getQuantity()).multiply(price);

				} else {

					java.util.Calendar c = java.util.Calendar.getInstance();
					c.setTime(curContract.getTradeDate());
					c.add(java.util.Calendar.DATE, -1);

					java.util.Calendar t = java.util.Calendar.getInstance();
					t.setTime(curContract.getTradeDate());
					t.add(java.util.Calendar.DATE, 2);

					DetachedCriteria whereReuter = DetachedCriteria.forClass(Reuter.class);
					whereReuter.add(Restrictions.eq("TradeDate", c.getTime()));
					whereReuter.add(Restrictions.eq("CommodityId", curLot.getCommodityId()));
					whereReuter.add(Restrictions.eq("PromptDate", t.getTime()));

					Reuter ruter = this.reuterRepository.GetQueryable(Reuter.class).where(whereReuter).firstOrDefault();
					if (price != null && ruter != null && curLot.getQP() != null && ruter.getPrice() != null) {
						BigDecimal M2MPrice = ruter.getPrice();
						amount = M2MPrice.multiply(curLot.getQuantity()).multiply(price);
					}
				}
			}
			break;

		case FeeCode.Cost: // 待调整取铜价计算,铜价=（根据批次的QP得到的MtM行情的单价*批次数量 ）,ruter行情
			DetachedCriteria whereCommodity = DetachedCriteria.forClass(Commodity.class);
			whereCommodity.add(Restrictions.eq("Code", "CU"));
			Commodity cu = this.commodityRepository.GetQueryable(Commodity.class).where(whereCommodity)
					.firstOrDefault();

			DetachedCriteria whereContract = DetachedCriteria.forClass(Contract.class);
			whereContract.add(Restrictions.eq("Id", curLot.getContractId()));
			Contract curContract1 = this.contractRepository.GetQueryable(Contract.class).where(whereContract)
					.firstOrDefault();

			BigDecimal price1 = feeSetups.get(0).getPrice(); // 费用单价
			if (price1 != null) {
				rate.setLength(0);
				rate.append(price1); // 铜行情单价
			}
			if (cu != null || curContract1 != null) {
				if (curContract1.getTradeDate().before(DateUtil.doSFormatDate("2015-07-29", "yyyy-MM-dd"))
						|| curContract1.getTradeDate().equals(DateUtil.doSFormatDate("2015-07-29", "yyyy-MM-dd"))) // 由于crrc时2015-07-28初始化的，这之前没有reuter行情数据
				{
					amount = new BigDecimal(5500).multiply(curLot.getQuantity()).multiply(price1);
				} else {

					java.util.Calendar c = java.util.Calendar.getInstance();
					c.setTime(curContract1.getTradeDate());
					c.add(java.util.Calendar.DATE, -1);

					java.util.Calendar t = java.util.Calendar.getInstance();
					t.setTime(curContract1.getTradeDate());
					t.add(java.util.Calendar.DATE, 2);

					DetachedCriteria whereReuter = DetachedCriteria.forClass(Reuter.class);
					whereReuter.add(Restrictions.eq("TradeDate", c.getTime()));
					whereReuter.add(Restrictions.eq("CommodityId", curLot.getCommodityId()));
					whereReuter.add(Restrictions.eq("PromptDate", t.getTime()));

					Reuter ruter = this.reuterRepository.GetQueryable(Reuter.class).where(whereReuter).firstOrDefault();

					if (price1 != null && ruter != null && ruter.getPrice() != null) {
						amount = ruter.getPrice().multiply(curLot.getQuantity()).multiply(price1);
					}
				}
			}

			break;
		case FeeCode.BankDocumentsFee: // 银行文件费
		case FeeCode.BuyDocumentsFee: // 采购文件费
		case FeeCode.DisputeFine: // 争议罚款费
		case FeeCode.HedgeFee:// 套保费
			BigDecimal pricex = feeSetups.get(0).getPrice();
			if (pricex != null) {
				amount = curLot.getQuantity().multiply(pricex);
				rate.setLength(0);
				rate.append(pricex);
			}
			break;
		case FeeCode.Other:
			BigDecimal price2 = feeSetups.get(0).getPrice();
			if (price2 != null) {
				rate.setLength(0);
				rate.append(price2);
			}
			amount = BigDecimal.ZERO;
			break;
		}
		return amount;
	}

	/**
	 * 得到批次保值的加权平均价格
	 * 
	 * @param lotId
	 * @return
	 */
	private BigDecimal PositionAverageByLot(String lotId) {
		if (lotId == null) {
			return BigDecimal.ZERO;
		}
		DetachedCriteria wherePosition = DetachedCriteria.forClass(Position.class);
		wherePosition.add(Restrictions.eq("LotId", lotId));
		List<Position> positions = this.positionRepository.GetQueryable(Position.class).where(wherePosition).toList();

		BigDecimal sumMoney = BigDecimal.ZERO;
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Position position : positions) {
			sumMoney = sumMoney.add(position.getOurPrice().multiply(position.getQuantity()));
			sumQuantity = sumQuantity.add(position.getQuantity());
		}

		BigDecimal ourPrice = (sumQuantity.compareTo(BigDecimal.ZERO) == 0 || sumMoney == null) ? BigDecimal.ZERO
				: sumMoney.divide(sumQuantity,2, BigDecimal.ROUND_HALF_UP);

		return ourPrice;
	}

	public void setEstimateFeeByType(String FeeType, Lot4FeesOverview lotFee, BigDecimal fee, BigDecimal feeRate,
			String feeBasis) {
		switch (FeeType) {
		case FeeCode.Transportation:
			lotFee.setEstimateAmount4Trans(fee);
			lotFee.setEstimateBasis4Trans(feeBasis);
			lotFee.setEstimateRate4Trans(feeRate);
			break;
		case FeeCode.Test:
			lotFee.setEstimateAmount4Test(fee);
			lotFee.setEstimateBasis4Test(feeBasis);
			lotFee.setEstimateRate4Test(feeRate);
			break;
		case FeeCode.Insurance:
			lotFee.setEstimateAmount4Insu(fee);
			lotFee.setEstimateBasis4Insu(feeBasis);
			lotFee.setEstimateRate4Insu(feeRate);
			break;
		case FeeCode.Cost:
			lotFee.setEstimateAmount4Cost(fee);
			lotFee.setEstimateBasis4Cost(feeBasis);
			lotFee.setEstimateRate4Cost(feeRate);
			break;
		case FeeCode.BankDocumentsFee:
			lotFee.setEstimateAmount4Bank(fee);
			lotFee.setEstimateBasis4Bank(feeBasis);
			lotFee.setEstimateRate4Bank(feeRate);
			break;
		case FeeCode.BuyDocumentsFee:
			lotFee.setEstimateAmount4Buy(fee);
			lotFee.setEstimateBasis4Buy(feeBasis);
			lotFee.setEstimateRate4Buy(feeRate);
			break;
		case FeeCode.DisputeFine:
			lotFee.setEstimateAmount4Fine(fee);
			lotFee.setEstimateBasis4Fine(feeBasis);
			lotFee.setEstimateRate4Fine(feeRate);
			break;
		case FeeCode.HedgeFee:
			lotFee.setEstimateAmount4Hedge(fee);
			lotFee.setEstimateBasis4Hedge(feeBasis);
			lotFee.setEstimateRate4Hedge(feeRate);
			break;
		case FeeCode.Other:
			lotFee.setEstimateAmount4Other(fee);
			lotFee.setEstimateBasis4Other(feeBasis);
			lotFee.setEstimateRate4Other(feeRate);
			break;
		}
	}

	@Override
	public List<Lot4MTM3> Lot4MTM3Query(MtmParams mtmParams, RefUtil total) {

		Criteria dc = this.lot4mtm3Repository.CreateCriteria(Lot4MTM3.class);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(mtmParams.getKeyword())) {
			dc.add(Restrictions.like("FullNo", "%" + mtmParams.getKeyword() + "%"));
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(mtmParams.getLegalIds())) {
			Object[] legalStr = (Object[]) mtmParams.getLegalIds().split(",");

			for (int i = 0; i < legalStr.length; i++) {
				System.out.println("|" + ((String) legalStr[i]).trim() + "|");
				legalStr[i] = ((String) legalStr[i]).trim();
			}
			dc.add(Restrictions.in("LegalId", legalStr));
		}

		// 销售需要添加条件，必须是已经发货的
		// 采购，必须已经点价

		// 只计算SFE市场
		dc.createAlias("MajorMarket", "majorMarket", JoinType.LEFT_OUTER_JOIN);
		dc.add(Restrictions.eq("majorMarket.Code", "SFE"));
		// 保值未完成的
		dc.add(Restrictions.eq("IsHedged", false));
		// 必须是点价
		dc.add(Restrictions.eq("MajorType", Lot4MTM3.MajorType_P));

		dc.add(Restrictions.or(Restrictions.and(Restrictions.eq("SpotDirection", Lot4MTM3.sdType_S),
				Restrictions.gt("QuantityDelivered", BigDecimal.ZERO)// 交付明细数量大于0
		), Restrictions.and(Restrictions.eq("SpotDirection", Lot4MTM3.sdType_B),
				Restrictions.gt("QuantityPriced", BigDecimal.ZERO)// 点价数量大于0
		)));

		List<Lot4MTM3> list = assemblingBeanList(this.lot4mtm3Repository.GetPage(dc, mtmParams.getPageSize(),
				mtmParams.getPageIndex(), mtmParams.getSortBy(), mtmParams.getOrderBy(), total).getData());
		// List<Lot4MTM3>
		// list=assemblingBeanList(this.lot4mtm3Repository.GetQueryable(Lot4MTM3.class).where(dc).toList());
		/**
		 * 判断属于那个市场
		 */
		if (list != null && list.size() > 0) {
			Map<String, BigDecimal> sfePrice = new HashMap<>();
			for (Lot4MTM3 m2m : list) {

				// 根据市场，品种，到期日得到M2MPrice
				// if
				// (m2m.getMajorMarket().getCode().toUpperCase().equals("LME"))
				// {
				// Reuter reuterPrice =
				// positionService.GetReuterPrice(m2m.getCommodityId(),
				// m2m.getContract().getTradeDate(),
				// m2m.getQP()).getData();
				// if (reuterPrice != null) {
				// m2m.setM2MPrice(reuterPrice.getPrice());
				// m2m.setM2MProfit(totalProfit(reuterPrice.getPrice(),m2m));
				//
				// }
				// } else

				if (m2m.getMajorMarket().getCode().toUpperCase().equals("SFE")) {

					/**
					 * 获取期货行情
					 */
					if (!sfePrice.containsKey(m2m.getCommodity().getCode())) {
						commonService.quotationPrice(m2m.getCommodity().getCode(), sfePrice);
					}
					BigDecimal lastPrice = sfePrice.get(m2m.getCommodity().getCode());
					lastPrice = DecimalUtil.nullToZero(lastPrice);
					m2m.setM2MPrice(lastPrice);
					m2m.setM2MProfit(totalProfit(lastPrice, m2m));
				}
				m2m.setMajorMarket(null);
				m2m.setCommodity(null);
				m2m.setContract(null);
			}
		} else {
			list = new ArrayList<>();
		}
		return list;
	}

	/**
	 * 计算实时利润
	 * 
	 */

	public BigDecimal totalProfit(BigDecimal price, Lot4MTM3 m2m) {
		BigDecimal profit = BigDecimal.ZERO;

		if (m2m.getSpotDirection().equals(Lot4MTM3.sdType_B) && m2m.getAvgPrice4Pricing() != null) {
			/**
			 * 采购
			 */
			BigDecimal xPrice = price.subtract(m2m.getAvgMajor4Pricing()).multiply(m2m.getQuantityPriced());
			BigDecimal qPrice = BigDecimal.ZERO;
			if (m2m.getAvgPrice4Position() != null && m2m.getAvgPrice4Position().compareTo(BigDecimal.ZERO) > 0) {
				qPrice = m2m.getAvgPrice4Position().subtract(price).multiply(m2m.getQuantityHedged());
			}
			profit = xPrice.subtract(qPrice);

		} else if (m2m.getSpotDirection().equals(Lot4MTM3.sdType_S) && m2m.getAvgPrice4Pricing() != null) {
			/**
			 * 销售
			 */
			// 获取套保比例
			Contract contract = this.contractRepository.getOneById(m2m.getContractId(), Contract.class);
			if (contract == null || contract.getHedgeRatio() == null) {
				contract.setHedgeRatio(BigDecimal.ZERO);
			}
			// 销售：（实时价格+销售升贴水Premium - 对应货物的采购点价价格+采购升贴水price）*（销售批次数量-销售点价数量）+
			// （销售点价价格+销售升贴水 - 对应货物的采购点价价格+采购升贴水price）*销售点价数量+
			// （对应货物的保值价格-实时价格））*（销售批次数量*套保比例-销售套保数量）+
			// （对应货物的保值价格-销售保值价格）*销售套保数量
			BigDecimal a = price.add(m2m.getPremium()).subtract(m2m.getAvgPrice4Pricing_S())
					.multiply(m2m.getQuantity().subtract(m2m.getQuantityPriced()));

			BigDecimal b = (m2m.getAvgMajor4Pricing().add(m2m.getPremium()).subtract(m2m.getAvgPrice4Pricing_S()))
					.multiply(m2m.getQuantityPriced());
			// HedgeRatio 套保比例
			BigDecimal c = (m2m.getAvgPrice4Position_S().subtract(price))
					.multiply(
							m2m.getQuantity()
									.multiply((contract.getHedgeRatio() != null ? contract.getHedgeRatio()
											: BigDecimal.ZERO).divide(new BigDecimal(100)))
									.subtract(m2m.getQuantityHedged()));

			BigDecimal d = (m2m.getAvgPrice4Position_S().subtract(m2m.getAvgPrice4Position()))
					.multiply(m2m.getQuantityHedged());

			profit = a.add(b).add(c).add(d);
		}
		return profit;
	}

	/**
	 * 以下是打掉关系
	 */
	public List<Lot4MTM3> assemblingBeanList(List<Lot4MTM3> ct) {
		if (ct == null || ct.size() == 0)
			return null;
		List<String> mIds = new ArrayList<>();
		List<String> commIds = new ArrayList<>();
		List<String> contIds = new ArrayList<>();
		List<String> custIds = new ArrayList<>();
		List<String> lotIds = new ArrayList<>();

		for (Lot4MTM3 lt : ct) {
			if (lt != null) {
				if (lt.getMajorMarketId() != null) {
					mIds.add(lt.getMajorMarketId());
				}
				if (lt.getCommodityId() != null) {
					commIds.add(lt.getCommodityId());
				}
				if (lt.getContractId() != null) {
					contIds.add(lt.getContractId());
				}
				if (lt.getCustomerId() != null) {
					custIds.add(lt.getCustomerId());
				}
				lotIds.add(lt.getId());
			}
		}
		List<Market> mList = new ArrayList<>();
		if (mIds.size() > 0) {
			DetachedCriteria mdc = DetachedCriteria.forClass(Market.class);
			mdc.add(Restrictions.in("Id", mIds));
			mList = this.marketRepository.GetQueryable(Market.class).where(mdc).toList();
		}

		List<Commodity> commList = new ArrayList<>();
		if (commIds.size() > 0) {
			DetachedCriteria commdc = DetachedCriteria.forClass(Commodity.class);
			commdc.add(Restrictions.in("Id", commIds));
			commList = this.commodityRepository.GetQueryable(Commodity.class).where(commdc).toList();
		}

		List<Contract> contList = new ArrayList<>();
		if (contIds.size() > 0) {
			DetachedCriteria contdc = DetachedCriteria.forClass(Contract.class);
			contdc.add(Restrictions.in("Id", contIds));
			contList = this.contractRepository.GetQueryable(Contract.class).where(contdc).toList();
		}

		List<Customer> custList = new ArrayList<>();
		if (custIds.size() > 0) {
			DetachedCriteria contdc = DetachedCriteria.forClass(Customer.class);
			contdc.add(Restrictions.in("Id", custIds));
			custList = this.customerRepository.GetQueryable(Customer.class).where(contdc).toList();
		}

		for (Lot4MTM3 lot4mtm3 : ct) {
			if (mList.size() > 0) {
				for (Market market : mList) {
					if (lot4mtm3.getMajorMarketId().equals(market.getId())) {
						lot4mtm3.setMajorMarket(market);
						break;
					}
				}
			}

			if (commList.size() > 0) {
				for (Commodity commodity : commList) {
					if (lot4mtm3.getCommodityId().equals(commodity.getId())) {
						lot4mtm3.setCommodity(commodity);
						break;
					}
				}
			}

			if (contList.size() > 0) {
				for (Contract contract : contList) {
					if (lot4mtm3.getContractId().equals(contract.getId())) {
						lot4mtm3.setContract(contract);
						break;
					}
				}
			}

			if (custList.size() > 0) {
				for (Customer customer : custList) {
					if (lot4mtm3.getCustomerId().equals(customer.getId())) {
						lot4mtm3.setCustomerName(customer.getName());
						break;
					}
				}
			}
		}
		DetachedCriteria pdc = DetachedCriteria.forClass(Pricing.class);
		pdc.add(Restrictions.in("LotId", lotIds));
		List<Pricing> priList = this.pricingRepository.GetQueryable(Pricing.class).where(pdc).toList();

		DetachedCriteria posdc = DetachedCriteria.forClass(Position.class);
		posdc.add(Restrictions.in("LotId", lotIds));
		List<Position> positionList = this.positionRepository.GetQueryable(Position.class).where(posdc).toList();
		List<Pricing> rlist = new ArrayList<>();
		List<Position> pforlist = new ArrayList<>();
		for (Lot4MTM3 lt2 : ct) {
			// ------------点价加权平均------------------
			rlist.clear();
			for (Pricing pr : priList) {
				if (pr.getLotId().equals(lt2.getId())) {
					rlist.add(pr);
				}
			}
			if (rlist != null && rlist.size() > 0) {

				BigDecimal totalQuantity = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				BigDecimal totalM = BigDecimal.ZERO;
				for (Pricing pricing : rlist) {
					total = total.add(pricing.getQuantity().multiply(pricing.getPrice()));
					totalM = totalM.add(pricing.getQuantity().multiply(pricing.getMajor()));
					totalQuantity = totalQuantity.add(pricing.getQuantity());
				}
				if (totalQuantity.compareTo(BigDecimal.ZERO) != 0) {
					lt2.setAvgPrice4Pricing(total.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));
					lt2.setAvgMajor4Pricing(totalM.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));

				}
			} else {
				lt2.setAvgPrice4Pricing(BigDecimal.ZERO);
			}
			// --------------保值加权平均-------------------------------
			pforlist.clear();
			for (Position position : positionList) {
				if (position.getLotId().equals(lt2.getId())) {
					pforlist.add(position);
				}
			}
			if (pforlist != null && pforlist.size() > 0) {
				BigDecimal totalQuantity = BigDecimal.ZERO;
				BigDecimal total = BigDecimal.ZERO;
				for (Position position : pforlist) {
					total = total.add(position.getQuantity().multiply(position.getOurPrice()));
					totalQuantity = totalQuantity.add(position.getQuantity());
				}
				if (totalQuantity.compareTo(BigDecimal.ZERO) != 0) {
					lt2.setAvgPrice4Position(total.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));
				}
			} else {
				lt2.setAvgPrice4Position(BigDecimal.ZERO);
			}

		}

		for (Lot4MTM3 lt : ct) {

			if (lt != null) {
				/**
				 * 销售
				 */
				if (lt.getSpotDirection().equals(Lot4MTM3.sdType_S)) {

					// ----------------------采购来源点价记录----------------------------------//
					/**
					 * 根据LotId追踪货物表Storage找到来源Storage.Id上游
					 */
					DetachedCriteria storageDc = DetachedCriteria.forClass(Storage.class);
					storageDc.add(Restrictions.eq("LotId", lt.getId()));
					storageDc.add(Restrictions.isNotNull("CounterpartyId3"));
					storageDc.setProjection(Projections.distinct(Projections.property("CounterpartyId3")));
					List<String> idList = this.storageRepository.GetQueryable(Storage.class).where(storageDc).toStr();

					if (idList.size() > 0) {
						// 取上游记录的LotId
						DetachedCriteria pStorageDc = DetachedCriteria.forClass(Storage.class);
						pStorageDc.add(Restrictions.in("Id", idList));
						pStorageDc.setProjection(Projections.distinct(Projections.property("LotId")));

						List<String> lotIdList = this.storageRepository.GetQueryable(Storage.class).where(pStorageDc)
								.toStr();

						if (lotIdList.size() > 0) {

							/**
							 * 处理点价(点价价格+升贴水 加权平均)
							 */
							DetachedCriteria dcp = DetachedCriteria.forClass(Pricing.class);
							dcp.add(Restrictions.in("LotId", lotIdList));
							dcp.add(Restrictions.isNotNull("LotId"));
							List<Pricing> pList = this.pricingRepository.GetQueryable(Pricing.class).where(dcp)
									.toList();
							if (pList != null && pList.size() > 0) {
								BigDecimal totalQuantity = BigDecimal.ZERO;
								BigDecimal total = BigDecimal.ZERO;
								for (Pricing pricing : pList) {
									total = total.add(pricing.getQuantity().multiply(pricing.getPrice()));
									totalQuantity = totalQuantity.add(pricing.getQuantity());
								}
								if (totalQuantity.compareTo(BigDecimal.ZERO) != 0) {
									lt.setAvgPrice4Pricing_S(total.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));
								}
							}

							/**
							 * 处理保值价格
							 */
							DetachedCriteria dct = DetachedCriteria.forClass(Position.class);
							dct.add(Restrictions.in("LotId", lotIdList));
							List<Position> posList = this.positionRepository.GetQueryable(Position.class).where(dct)
									.toList();
							if (posList != null && posList.size() > 0) {
								BigDecimal totalQuantity = BigDecimal.ZERO;
								BigDecimal total = BigDecimal.ZERO;
								for (Position position : posList) {
									total = total.add(position.getQuantity().multiply(position.getOurPrice()));
									totalQuantity = totalQuantity.add(position.getQuantity());
								}
								if (totalQuantity.compareTo(BigDecimal.ZERO) != 0) {
									lt.setAvgPrice4Position_S(total.divide(totalQuantity, 2, BigDecimal.ROUND_HALF_UP));
								}
							}
						}
					} else {

					}
				}
			}
		}
		return ct;
	}

	@Override
	public List<Lot4Unpriced> UnpricedLotRisk(MtmParams mtmParams, RefUtil total) {
		DetachedCriteria dc = DetachedCriteria.forClass(Lot4Unpriced.class);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(mtmParams.getKeyword())) {
			dc.add(Restrictions.like("FullNo", "%" + mtmParams.getKeyword() + "%"));
		}
		if (org.apache.commons.lang3.StringUtils.isNotBlank(mtmParams.getLegalIds())) {
			Object[] s = (Object[]) mtmParams.getLegalIds().split(",");
			dc.add(Restrictions.in("LegalId", s));
		}
		// 只计算SFE市场
		dc.createAlias("MajorMarket", "majorMarket", JoinType.LEFT_OUTER_JOIN);
		dc.add(Restrictions.eq("majorMarket.Code", "SFE"));
		// 不是固定价
		dc.add(Restrictions.ne("MajorType", Lot4MTM3.MajorType_F));
		dc.add(Restrictions.or(Restrictions.isNull("QuantityPriced"),
				Restrictions.le("QuantityPriced", BigDecimal.ZERO)));

		List<Lot4Unpriced> resultList = this.lot4UnpricedRepository.GetQueryable(Lot4Unpriced.class).where(dc).toList();
		resultList = assemblingBeanList2(resultList);

		DetachedCriteria riskScale = DetachedCriteria.forClass(Dictionary.class);
		riskScale.add(Restrictions.eq("Name", "未点价批次风险警示比例"));
		riskScale.add(Restrictions.eq("Code", "PriceRisk"));
		List<Dictionary> comms = this.dictionaryRepository.GetQueryable(Dictionary.class).where(riskScale).toList();

		BigDecimal scale = new BigDecimal(5);
		if (comms != null && comms.size() > 0) {
			scale = new BigDecimal(comms.get(0).getValue());
		}
		BigDecimal max = new BigDecimal(1).add(scale.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
		BigDecimal min = new BigDecimal(1).subtract(scale.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));

		/**
		 * 实时价格>价格*(1+15%)或者实时价格<价格*(1-15%) 如果不在这个范围内，删除不显示
		 */
		Map<String, BigDecimal> sfePrice = new HashMap<>();
		List<Lot4Unpriced> removelist = new ArrayList<>();
		resultList.forEach(s -> {
			if (!sfePrice.containsKey(s.getCommodity().getCode())) {
				commonService.quotationPrice(s.getCommodity().getCode(), sfePrice);
			}
			BigDecimal lastPrice = sfePrice.get(s.getCommodity().getCode());
			if (s.getTradeDatePrice() != null) {
				if (lastPrice.compareTo(s.getTradeDatePrice().multiply(max)) > 0
						|| lastPrice.compareTo(s.getTradeDatePrice().multiply(min)) < 0) {
					s.setM2MPrice(lastPrice);
					s.setM2MProfit(lastPrice.subtract(s.getTradeDatePrice()).multiply(s.getQuantity()));
					s.setCommodity(null);
					s.setMajorMarket(null);
					s.setContract(null);
					s.setCustomer(null);
				} else {
					removelist.add(s);
				}
			} else {
				removelist.add(s);
			}
		});
		resultList.removeAll(removelist);
		resultList = resultList.stream().sorted((p1, p2) -> p1.getM2MProfit().compareTo(p2.getM2MProfit()))
				.collect(Collectors.toList());
		return resultList;
	}

	@Override
	public ActionResult<Lot> SaveLotOfContractRegular_New(Lot lot) {

		// 检查lot.Contract不可为空，且类型必须是长单
		if (lot.getContract() == null || lot.getContract().getIsProvisional()) {
			return new ActionResult<>(false, MessageCtrm.ParamError);
		}

		// 格式化均价点介的日期、格式化lot.PricingType
		lot.setMajorStartDate(commonService.FormatDateAsYymmdd000000(lot.getMajorStartDate()));
		lot.setMajorEndDate(commonService.FormatDateAsYymmdd000000(lot.getMajorEndDate()));
		lot.setPremiumStartDate(commonService.FormatDateAsYymmdd000000(lot.getPremiumStartDate()));
		lot.setPremiumEndDate(commonService.FormatDateAsYymmdd000000(lot.getPremiumEndDate()));
		lot.setPricingType(String.format("%s+%s", lot.getMajorType(), lot.getPremiumType()));

		// 检查批次号是否重复
		DetachedCriteria dc = DetachedCriteria.forClass(Lot.class);
		dc.add(Restrictions.neOrIsNotNull("Id", lot.getId()));
		dc.add(Restrictions.eq("ContractId", lot.getContractId()));
		dc.add(Restrictions.eq("LotNo", lot.getLotNo()));

		Lot existLot = repository.GetQueryable(Lot.class).where(dc).firstOrDefault();

		if (existLot != null)
			throw new RuntimeException("批次号重复。");

		// 检查数量关系是否合法
		dc = DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("ContractId", lot.getContractId()))
				.add(Restrictions.neOrIsNotNull("Id", lot.getId())).setProjection(Projections.sum("Quantity"));
		BigDecimal lotQuantitySum = DecimalUtil
				.nullToZero((BigDecimal) repository.getHibernateTemplate().findByCriteria(dc).get(0))
				.add(lot.getQuantity());

		// 检查批次总数量是否大于合同总数量
		if (lotQuantitySum.compareTo(lot.getContract().getQuantity()) > 0)
			throw new RuntimeException("批次数量之和不可以大于合同数量");

		// 先保存产品名称
		Product product = new Product();
		product.setCommodityId(lot.getContract().getCommodityId());
		product.setName(lot.getContract().getProduct());
		productService.Save(product);

		// as a workround: 早先bvi - sm的同步生成批次时，存在lot.FullNo编号中的买卖BS方向错误的问题
		lot.setFullNo(String.format("%s/%s", lot.getContract().getHeadNo(), lot.getLotNo()));

		lot.setIsIniatiated(lot.getContract().getIsIniatiated());

		// 作为非常重要的冗余,不可删除
		Contract contract = lot.getContract();

		lot.setLegalId(contract.getLegalId());
		lot.setCustomerId(contract.getCustomerId());
		lot.setCurrency(contract.getCurrency());
		lot.setHeadNo(contract.getHeadNo());
		lot.setSerialNo(contract.getSerialNo());
		lot.setStatus(contract.getStatus());
		lot.setSpotDirection(contract.getSpotDirection());
		lot.setCommodityId(contract.getCommodityId());
		lot.setProduct(contract.getProduct());

		// 重新计算批次相关标记
		ReCalculateLotMarks_New(lot);

		// 预先处理点价天数，每天点价数量
		if (lot.getMajorType().equals(MajorType.Average) || lot.getPremiumType().equals(PremiumType.Average)) {
			UpdateQuantityPerDay(lot);
		}
		this.repository.SaveOrUpdate(lot); // 保存批次

		UpdateQuantityOriginal(lot);

		// 更新合同中的数量汇总
		Contract existContract = contractRepository.getOneById(lot.getContractId(), Contract.class);

		BigDecimal sumContractLots = DecimalUtil
				.nullToZero((BigDecimal) lotRepo.getHibernateTemplate()
						.findByCriteria(DetachedCriteria.forClass(Lot.class)
								.add(Restrictions.eq("ContractId", existContract.getId()))
								.setProjection(Projections.sum("Quantity")))
						.get(0));
		existContract.setQuantityOfLots(sumContractLots);
		contractRepository.SaveOrUpdate(existContract);
		return new ActionResult<>(true, "保存成功", lot);
	}

	/**
	 * 重新计算批次相关标记
	 * 
	 * @param lot
	 */
	private void ReCalculateLotMarks_New(Lot lot) {

		// 溢短装
		if (lot.getMoreOrLessBasis().equals("OnQuantity")) {

			lot.setQuantityLess(lot.getQuantity().subtract(lot.getMoreOrLess()));
			lot.setQuantityMore(lot.getQuantity().add(lot.getMoreOrLess()));

		} else if (lot.getMoreOrLessBasis().equals("OnPercentage")) {
			lot.setQuantityLess(lot.getQuantity()
					.multiply(BigDecimal.ONE.subtract(lot.getMoreOrLess().divide(new BigDecimal(100)))));
			lot.setQuantityMore(
					lot.getQuantity().multiply(BigDecimal.ONE.add(lot.getMoreOrLess().divide(new BigDecimal(100)))));
		}

		// 更新批次是否首发货完成
		// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
		// lot.QuantityDelivered <= lot.QuantityMore);
		List<Lot> lots = commonService.setDelivery4Lot_New(lot);
		// lot.IsInvoiced = (lot.QuantityInvoiced >= lot.QuantityLess &&
		// lot.QuantityInvoiced <= lot.QuantityMore);
		List<Lot> lots1 = commonService.setInvoice4Lot_NewContract(lot);
		// lot.IsPriced = (lot.QuantityOriginal??0) == lot.QuantityPriced;
		// lot.IsHedged = lot.Quantity == Math.Abs(lot.QuantityHedged??0);
		lot.setIsPriced(commonService.IsPriced4Lot_New(lot));
		lot.setIsHedged(commonService.IsHedged4Lot_New(lot));

		commonService.UpdateDeliveryStatus_New(lots);// 更新拆分批次的交货状态
		commonService.UpdateInvoiceStatus_NewContract(lots1);// 更新拆分批次的开票状态

		if (lot.getBrands() != null && lot.getBrands().size() > 0) {
			List<String> ids = new ArrayList<>();
			for (Brand brand : lot.getBrands()) {
				ids.add(brand.getId());
			}
			lot.setBrands(brandRepository.GetQueryable(Brand.class)
					.where(DetachedCriteria.forClass(Brand.class).add(Restrictions.in("Id", ids))).toList());
		}

	}

	@Override
	public ActionResult<String> GenerateFees_New(Lot curLot) {

		if (curLot == null)
			return new ActionResult<>(false, "lot is null");

		Legal legal = legalRepository.getOneById(curLot.getLegalId(), Legal.class);

		if (legal == null)
			return new ActionResult<>(false, "该批次无抬头信息");

		Contract contract = contractRepository.getOneById(curLot.getContractId(), Contract.class);

		if (contract == null)
			return new ActionResult<>(false, "该批次订单信息不存在");

		// 不再通过类型区分，只要存在相关的费率配置就进行计算
		List<String> feeTypes = LeagalnFeeType.getSmSFeeType();

		int iSerial = 0;

		for (String feeType : feeTypes) {
			iSerial++;
			String type = feeType;
			String rate = "";
			List<FeeSetup> feeSetups;
			if (type.equals(FeeCode.Transportation) || type.equals(FeeCode.Other)) // 运输费不需要区分抬头和订单类型
			{
				feeSetups = feeSetupRepository.GetQueryable(FeeSetup.class)
						.where(DetachedCriteria.forClass(FeeSetup.class).add(Restrictions.eq("FeeType", type)))
						.toList();
			} else {

				DetachedCriteria dc = DetachedCriteria.forClass(FeeSetup.class);
				dc.add(Restrictions.eq("FeeType", type));
				dc.add(Restrictions.eq("LegalId", curLot.getLegalId()));
				dc.add(Restrictions.eq("SpotDirection", curLot.getSpotDirection()));
				feeSetups = feeSetupRepository.GetQueryable(FeeSetup.class).where(dc).toList();
			}

			Fee exist = feeRepository
					.GetQueryable(Fee.class).where(DetachedCriteria.forClass(Fee.class)
							.add(Restrictions.eq("LotId", curLot.getId())).add(Restrictions.eq("FeeCode", type)))
					.firstOrDefault();
			if (exist != null) {
				if (feeSetups.size() == 0) // 没有费率设置
				{
					exist.setAmountEstimated(BigDecimal.ZERO);
					exist.setRate(BigDecimal.ZERO);
					feeRepository.SaveOrUpdate(exist);
				} else {
					Map<String, String> calcAmountMap = CalcAmount_New(curLot, feeSetups, type);
					String amount = calcAmountMap.get(this.CalcAmount_Key_Amount);
					rate = calcAmountMap.get(this.CalcAmount_Key_Rate);
					exist.setRate(new BigDecimal(rate));
					exist.setAmountEstimated(new BigDecimal(amount));
					exist.setTradeDate(curLot.getQP());
					exist.setUpdatedBy(curLot.getCreatedBy());
					exist.setUpdatedAt(new Date());
					exist.setSerialNo(iSerial);

					feeRepository.SaveOrUpdate(exist);
				}
			} else if (feeSetups.size() > 0) {
				// #region 取费用费率数据
				Map<String, String> calcAmountMap = CalcAmount_New(curLot, feeSetups, type);
				String amount = calcAmountMap.get(this.CalcAmount_Key_Amount);
				rate = calcAmountMap.get(this.CalcAmount_Key_Rate);
				Fee curFee = new Fee();
				curFee.setLotId(curLot.getId());
				curFee.setFeeCode(type);
				curFee.setFeeBasis(GetFeeBasis(type));
				curFee.setCurrency(curLot.getCurrency());
				curFee.setTradeDate(curLot.getQP());
				curFee.setAmountEstimated(new BigDecimal(amount));
				curFee.setRate(new BigDecimal(rate));
				curFee.setIsEliminated(false);
				curFee.setCreatedBy(curLot.getCreatedBy());
				curFee.setCreatedAt(new Date());
				curFee.setSerialNo(iSerial);

				// 保存费用
				feeRepository.SaveOrUpdate(curFee);
			} else if (feeSetups.size() <= 0) // 费用费率未设置自动添加空数据(有预估费用，但是金额为0）
			{
				// #region 取费用费率数据
				Fee curFee = new Fee();

				curFee.setLotId(curLot.getId());
				curFee.setFeeCode(type);
				curFee.setFeeBasis(GetFeeBasis(type));
				curFee.setCurrency(curLot.getCurrency());
				curFee.setTradeDate(curLot.getQP());
				curFee.setAmountEstimated(BigDecimal.ZERO);
				curFee.setAmountDone(BigDecimal.ZERO);
				curFee.setRate(BigDecimal.ZERO);
				curFee.setIsEliminated(false);
				curFee.setCreatedBy(curLot.getCreatedBy());
				curFee.setCreatedAt(new Date());
				curFee.setSerialNo(iSerial);

				// 保存费用
				feeRepository.SaveOrUpdate(curFee);
			}

		}

		return new ActionResult<>(true, "成功生成费用信息");
	}

	private final String CalcAmount_Key_Amount = "amount";

	private final String CalcAmount_Key_Rate = "rate";

	/**
	 * @param curLot
	 * @param feeSetups
	 * @param feeType
	 * @param rate
	 * @return map key: [CalcAmount_Key_Amount-> amount, CalcAmount_Key_Rate->
	 *         rate]
	 */
	private Map<String, String> CalcAmount_New(Lot curLot, List<FeeSetup> feeSetups, String feeType) {

		String amount = "0";
		String rate = "0";

		switch (feeType) {

		case FeeCode.Transportation:

			FeeSetup find = null;
			List<FeeSetup> feeSetupList = feeSetups.stream().filter(x -> x.getLoading().equals(curLot.getLoading())
					&& x.getDischarging().equals(curLot.getDischarging())).collect(Collectors.toList());

			if (feeSetupList != null && feeSetupList.size() > 0)
				find = feeSetupList.get(0);

			if (find != null)// && curLot.QP != null)
			{
				rate = find.getPrice().toString();
				amount = (curLot.getQuantity().multiply(find.getPrice())).toString();
			}
			break;
		case FeeCode.Test:
			BigDecimal price0 = feeSetups.get(0).getPrice();
			if (price0 != null)// && curLot.QP != null)
			{
				amount = (curLot.getQuantity().multiply(price0)).toString();
				rate = price0.toString();
			}
			break;
		case FeeCode.Insurance: // 待调整，取发票金额计算（改成取合同签订日期现货价格）//20150709
								// 改成取ruter的行情价格
			BigDecimal price = feeSetups.get(0).getPrice();
			Contract curContract = contractRepository.getOneById(curLot.getContractId(), Contract.class);

			if (price != null)
				rate = price.toString();
			if (curContract != null) {
				if (curContract.getTradeDate().before(DateUtil.doSFormatDate("2015/07/29", "yyyy/MM/dd"))) // 由于crrc时2015-07-28初始化的，这之前没有reuter行情数据
				{
					BigDecimal M2MPrice = new BigDecimal(5500);
					amount = (M2MPrice.multiply(curLot.getQuantity().multiply(price))).toString();

				} else {
					// 取cash价格，20160120
					DetachedCriteria dc = DetachedCriteria.forClass(Reuter.class);
					dc.add(Restrictions.eq("CommodityId", curLot.getCommodityId()));
					dc.add(Restrictions.eq("PromptDate", DateUtil.getDiffDate(curContract.getTradeDate(), 2)));
					dc.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(curContract.getTradeDate(), -1)));

					Reuter ruter = reuterRepository.GetQueryable(Reuter.class).where(dc).firstOrDefault();
					if (price != null && ruter != null && curLot.getQP() != null && ruter.getPrice() != null) {
						BigDecimal M2MPrice = ruter.getPrice();
						amount = (M2MPrice.multiply(curLot.getQuantity().multiply(price))).toString();
					}
				}

			}
			break;
		case FeeCode.Cost: // 待调整取铜价计算,铜价=（根据批次的QP得到的MtM行情的单价*批次数量 ）,ruter行情

			Commodity cu = commodityRepository.GetQueryable(Commodity.class)
					.where(DetachedCriteria.forClass(Commodity.class).add(Restrictions.eq("Code", "CU")))
					.firstOrDefault();

			Contract curContract1 = contractRepository.getOneById(curLot.getContractId(), Contract.class);

			BigDecimal price1 = feeSetups.get(0).getPrice(); // 费用单价

			if (price1 != null)
				rate = price1.toString(); // 铜行情单价

			if (cu != null || curContract1 != null) {
				if (curContract1.getTradeDate().before(DateUtil.doSFormatDate("2015/07/29", "yyyy/MM/dd"))) // 由于crrc时2015-07-28初始化的，这之前没有reuter行情数据
				{
					amount = (curLot.getQuantity().multiply(price1).multiply(new BigDecimal(5500))).toString();
				} else {
					// 取cash价格，20160120
					Reuter ruter = reuterRepository.GetQueryable(Reuter.class).where(DetachedCriteria
							.forClass(Reuter.class).add(Restrictions.eq("CommodityId", cu.getId()))
							.add(Restrictions.eq("PromptDate", DateUtil.getDiffDate(curContract1.getTradeDate(), 2)))
							.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(curContract1.getTradeDate(), -1))))
							.firstOrDefault();
					if (price1 != null && ruter != null && ruter.getPrice() != null) {
						amount = (ruter.getPrice().multiply(curLot.getQuantity().multiply(price1))).toString();
					}
				}
			}
			break;
		case FeeCode.BankDocumentsFee: // 银行文件费
		case FeeCode.BuyDocumentsFee: // 采购文件费
		case FeeCode.DisputeFine: // 争议罚款费
		case FeeCode.HedgeFee:// 套保费
			BigDecimal pricex = feeSetups.get(0).getPrice(); // 费用单价
			if (pricex != null)// && curLot.QP != null)
			{
				amount = (curLot.getQuantity().multiply(pricex)).toString();
				rate = pricex.toString();
			}
			break;
		case FeeCode.Other:
			BigDecimal price2 = feeSetups.get(0).getPrice(); // 费用单价
			if (price2 != null)
				rate = price2.toString();
			amount = "0";
			break;
		}
		Map<String, String> map = new HashMap<>();
		map.put(CalcAmount_Key_Amount, amount);
		map.put(CalcAmount_Key_Rate, rate);
		return map;
	}

	public List<Lot4Unpriced> assemblingBeanList2(List<Lot4Unpriced> ct) {

		List<String> commodityIds = new ArrayList<>();
		List<String> customerIds = new ArrayList<>();
		List<String> contractIds = new ArrayList<>();

		ct.forEach(s -> {
			if (s.getCommodityId() != null) {
				commodityIds.add(s.getCommodityId());
			}
			if (s.getCustomerId() != null) {
				customerIds.add(s.getCustomerId());
			}
			if (s.getContractId() != null) {
				contractIds.add(s.getContractId());
			}

		});
		if (commodityIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Commodity.class);
			dc.add(Restrictions.in("Id", commodityIds));
			List<Commodity> listComm = this.commodityRepository.GetQueryable(Commodity.class).where(dc).toList();

			if (listComm != null && listComm.size() > 0) {
				ct.forEach(s -> {
					listComm.forEach(c -> {
						if (s.getCommodityId().equals(c.getId())) {
							s.setCommodity(c);
						}
					});

				});
			}
		}

		if (customerIds.size() > 0) {
			DetachedCriteria dc2 = DetachedCriteria.forClass(Customer.class);
			dc2.add(Restrictions.in("Id", customerIds));

			List<Customer> listCustomer = this.customerRepository.GetQueryable(Customer.class).where(dc2).toList();
			if (listCustomer != null && listCustomer.size() > 0) {
				ct.forEach(s -> {
					listCustomer.forEach(c -> {
						if (s.getCustomerId().equals(c.getId())) {
							s.setCustomerName(c.getName());
						}
					});

				});
			}
		}

		if (contractIds.size() > 0) {
			DetachedCriteria dc3 = DetachedCriteria.forClass(Contract.class);
			dc3.add(Restrictions.in("Id", contractIds));

			List<Contract> listContract = this.contractRepository.GetQueryable(Contract.class).where(dc3).toList();
			if (listContract != null && listContract.size() > 0) {
				ct.forEach(s -> {
					listContract.forEach(c -> {
						if (s.getContractId().equals(c.getId())) {
							s.setContract(c);
						}
					});

				});
			}
		}

		/**
		 * 获取合同签订日的价格
		 */
		List<Date> listDate = new ArrayList<>();
		List<Lot4Unpriced> removelist = new ArrayList<>();
		ct.forEach(s -> {
			if (s.getContract() != null) {
				if (s.getContract().getTradeDate() != null) {
					if (s.getContract().getTradeDate()
							.compareTo(DateUtil.doSFormatDate(new Date(), "yyyy-MM-dd 00:00:00.000")) == 0) {
						removelist.add(s);
					} else if (!listDate.contains(s.getContract().getTradeDate())) {
						listDate.add(DateUtil.doSFormatDate(s.getContract().getTradeDate(), "yyyy-MM-dd 00:00:00.000"));
					}
				}
			}
		});
		ct.removeAll(removelist);// 如果是今天签订的合同不显示

		if (listDate.size() > 0) {
			DetachedCriteria dc4 = DetachedCriteria.forClass(SFE.class);
			dc4.add(Restrictions.in("TradeDate", listDate));
			List<SFE> listSfe = this.sfeRepository.GetQueryable(SFE.class).where(dc4).toList();
			if (listSfe != null && listSfe.size() > 0) {
				ct.forEach(s -> {
					listSfe.forEach(x -> {
						if (s.getContract().getTradeDate().compareTo(x.getTradeDate()) == 0
								&& s.getContract().getCommodityId().equals(x.getCommodityId())) {
							s.setTradeDatePrice(x.getPriceSettle());
						}
					});
				});
			}
		}
		return ct;
	}

	public List<QPRecord> assemblingBeanList3(List<QPRecord> ct) {

		List<String> lotIds = new ArrayList<>();

		ct.forEach(s -> {
			if (s.getLotId() != null) {
				lotIds.add(s.getLotId());
			}
		});
		if (lotIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Lot.class);
			dc.add(Restrictions.in("Id", lotIds));
			List<Lot> listLot = this.repository.GetQueryable(Lot.class).where(dc).toList();

			if (listLot != null && listLot.size() > 0) {
				ct.forEach(s -> {
					listLot.forEach(c -> {
						if (c.getId().equals(s.getLotId())) {
							s.setLot(c);
						}
					});

				});
			}
		}
		return ct;
	}


	@Override
	public ActionResult<LotPnL> NewLoadLotSettle(Param4LotPnL param4LotPnL) {

		// 有效性检查
		GlobalSet globalSet = globalSetRepository.GetQueryable(GlobalSet.class).firstOrDefault();
		if (globalSet == null)
			return new ActionResult<>(false, "缺少GlobalSet设置。");

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		if (param4LotPnL.getLotId() == null) {
			logger.info("Params is unavailable: LotId");
			return new ActionResult<>(false, "Params is unavailable: LotId");
		}
		Lot lot = lotRepo.getOneById(param4LotPnL.getLotId(), Lot.class);

		// 构建:vmPnL
		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(lot.getId());
		vmPnL.setContractFullNo(lot.getFullNo());
		vmPnL.setCustomerName(lot.getCustomer().getName());
		vmPnL.setPremium(DecimalUtil.nullToZero(lot.getPremium()));

		boolean isHasReivalOrder = false;
		Contract contract = contractRepository.getOneById(lot.getContractId(), Contract.class);
		if (org.apache.commons.lang3.StringUtils.isNotBlank(contract.getRivalOrderID())) // 对手订单
			isHasReivalOrder = true;
		else
			isHasReivalOrder = false;

		// 销售一侧的商品明细
		String sql = "";
		vmPnL.setStorageOuts(storageRepository.GetQueryable(Storage.class)
				.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId()))).toList());
		for (Storage outs : vmPnL.getStorageOuts()) {
			outs.setIsSelect(true);
			sql = "select inv.InvoiceNo as InvoiceNo from Physical.Storage ps "
					+ "left join Physical.InvoiceStorage ins on ps.Id = ins.StorageId "
					+ "left join Physical.Invoice inv on inv.Id = ins.InvoiceId " + "where ps.Id = '" + outs.getId()
					+ "'";

			List<String> invoiceNos = this.storageRepository.ExecuteCorrectlySql4(sql, "InvoiceNo");
			if (invoiceNos.size() > 1) {
				for (String no : invoiceNos) {
					if (outs.getInvoiceNo() == null)
						outs.setInvoiceNo(no);
					else
						outs.setInvoiceNo(no != null ? outs.getInvoiceNo() + "," + no : outs.getInvoiceNo());
				}
			} else if (invoiceNos.size() == 1) {
				outs.setInvoiceNo(invoiceNos.get(0));
			}

			outs.setPrice(lot.getMajor());
			outs.setFee(DecimalUtil.nullToZero(lot.getFee()));
			outs.setPremium(lot.getPremium());
			outs.setCurrency(lot.getCurrency());
		}

		BigDecimal sumStorageOuts = BigDecimal.ZERO;
		for (Storage storage : vmPnL.getStorageOuts()) {
			sumStorageOuts = sumStorageOuts.add(storage.getQuantity());
		}
		vmPnL.setQuantitySell(sumStorageOuts);

		// 销售金额
		vmPnL.setAmountSell(BigDecimal.ZERO);

		// 销售发生的fee的汇总金额
		BigDecimal fee4Sell = BigDecimal.ZERO;

		for (Storage so : vmPnL.getStorageOuts()) {
			if (so.getCurrency().equals(currency)) {
				vmPnL.setAmountSell(
						vmPnL.getAmountSell().add(so.getQuantity().multiply(DecimalUtil.nullToZero(so.getPrice()))));
			} else {
				if (so.getCurrency().equals("CNY")) {
					vmPnL.setAmountSell(vmPnL.getAmountSell().add(so.getQuantity()
							.multiply(DecimalUtil.nullToZero(so.getPrice())).divide(rate, 5, RoundingMode.HALF_EVEN)));
				} else {
					vmPnL.setAmountSell(vmPnL.getAmountSell()
							.add(so.getQuantity().multiply(DecimalUtil.nullToZero(so.getPrice())).multiply(rate)));
				}
			}
		}

		// 销售均价
		if (vmPnL.getQuantitySell().compareTo(BigDecimal.ZERO) != 0) {
			vmPnL.setPriceSell(vmPnL.getAmountSell().divide(vmPnL.getQuantitySell()));
		}

		vmPnL.setStorageOuts(commonService.SimplifyDataStorageList(vmPnL.getStorageOuts()));
		fee4Sell = lot.getQuantity().multiply(lot.getRealFee());

		// 采购一侧的商品明细

		vmPnL.setStorageIns(new ArrayList<>());
		for (Storage v : vmPnL.getStorageOuts()) {
			if (!isHasReivalOrder) {
				// 取得对手方的交付记录，并且加入到List中
				if (v.getCounterpartyId3() != null) {
					vmPnL.getStorageIns().add(storageRepository.getOneById(v.getCounterpartyId3(), Storage.class));
				}
			} else {
				if (v.getCounterpartyId2() != null)
					vmPnL.getStorageIns().add(storageRepository.getOneById(v.getCounterpartyId2(), Storage.class));
			}
		}

		// 采购数量
		BigDecimal sumStorageIns = BigDecimal.ZERO;
		for (Storage storage : vmPnL.getStorageIns()) {
			sumStorageIns = sumStorageIns.add(storage.getQuantity());
		}
		vmPnL.setQuantityPurchase(sumStorageIns);

		// 采购成本
		vmPnL.setAmountPurchase(BigDecimal.ZERO);

		// 采购发生的fee的汇总金额
		BigDecimal fee4Purchase = BigDecimal.ZERO;

		List<String> lstBuyLot = new ArrayList<>();
		for (Storage si : vmPnL.getStorageIns()) {
			si.setIsSelect(true);
			sql = "select inv.InvoiceNo as InvoiceNo from Physical.Storage ps "
					+ "left join Physical.InvoiceStorage ins on ps.Id = ins.StorageId "
					+ "left join Physical.Invoice inv on inv.Id = ins.InvoiceId " + "where ps.Id = '" + si.getId()
					+ "'";

			List<String> invoiceNos = this.storageRepository.ExecuteCorrectlySql4(sql, "InvoiceNo");
			if (invoiceNos.size() > 1) {
				for (String no : invoiceNos) {
					if (si.getInvoiceNo() == null)
						si.setInvoiceNo(no);
					else
						si.setInvoiceNo(no != null ? si.getInvoiceNo() + "," + no : si.getInvoiceNo());
				}
			} else if (invoiceNos.size() == 1) {
				si.setInvoiceNo(invoiceNos.get(0));
			}
			// 供应商
			sql = "select c.Name from Basis.Customer c " + "right join Physical.Storage ps on c.Id = ps.CustomerId "
					+ "where ps.Id = '" + si.getId() + "'";

			List<String> names = this.customerRepository.ExecuteCorrectlySql4(sql, "Name");

			if (names != null && names.size() > 0)
				si.setCustomerName(names.get(0));

			// 点价，升贴水，费用
			Lot lotin = lotRepo.getOneById(si.getLotId(), Lot.class);
			if (lotin != null) {
				si.setPrice(lotin.getMajor());
				si.setPremium(lotin.getPremium());
				si.setFee(DecimalUtil.nullToZero(lotin.getFee()));
				si.setCurrency(lotin.getCurrency());
				fee4Purchase = fee4Purchase.add(DecimalUtil.nullToZero(lotin.getQuantity())
						.multiply(DecimalUtil.nullToZero(lotin.getRealFee())));
			}

			if (si.getCurrency().equals(currency)) {
				vmPnL.setAmountPurchase(vmPnL.getAmountPurchase()
						.add(si.getQuantity().multiply(DecimalUtil.nullToZero(si.getPrice()))));
			} else {
				if (si.getCurrency().equals("CNY")) {
					vmPnL.setAmountPurchase(vmPnL.getAmountPurchase().add(si.getQuantity()
							.multiply(DecimalUtil.nullToZero(si.getPrice())).divide(rate, 5, RoundingMode.HALF_EVEN)));
				} else {
					vmPnL.setAmountPurchase(vmPnL.getAmountPurchase()
							.add(si.getQuantity().multiply(DecimalUtil.nullToZero(si.getPrice())).multiply(rate)));
				}
			}
			if (si.getLotId() != null && !lstBuyLot.contains(si.getLotId()))
				lstBuyLot.add(si.getLotId());
		}

		// 采购价格
		if (vmPnL.getQuantityPurchase().compareTo(BigDecimal.ZERO) != 0)
			vmPnL.setPricePurchase(vmPnL.getAmountPurchase().divide(vmPnL.getQuantityPurchase()));
		vmPnL.setFee(fee4Sell.add(fee4Purchase));
		vmPnL.setPnL4Spot(DecimalUtil.nullToZero(vmPnL.getAmountSell())
				.subtract(DecimalUtil.nullToZero(vmPnL.getAmountPurchase())));
		vmPnL.setStorageIns(commonService.SimplifyDataStorageList(vmPnL.getStorageIns()));
		// 期货盈亏计算（暂时不删除，如果需要头寸结算的话，只需将此段代码打开）
		List<Square> squares = squareRepository.GetQueryable(Square.class)
				.where(DetachedCriteria.forClass(Square.class).add(Restrictions.eq("LotId", lot.getId()))).toList();
		if (squares.size() > 0) {
			BigDecimal sumSquares = BigDecimal.ZERO;
			for (Square s : squares) {
				sumSquares = sumSquares.add(s.getPnL());
			}
			vmPnL.setPnL4Hedge(sumSquares);
		}

		for (Square s : squares) {
			if (s.getPromptDateLong() == null)
				s.setPromptDateLong(s.getPromptDate());

			if (s.getPromptDateShort() == null)
				s.setPromptDateShort(s.getPromptDate());
		}
		squares.sort((p1, p2) -> p1.getPromptDate().compareTo(p2.getPromptDate()));
		vmPnL.setSquares(squares);
		vmPnL.setPnLTotal(vmPnL.getPnL4Spot().add(vmPnL.getPnL4Hedge())); // 合计盈亏=
																			// 现货盈亏+
																			// 期货盈亏
		vmPnL.setCurrency(currency);

		if (vmPnL != null) {
			if (vmPnL.getSquares() != null && vmPnL.getSquares().size() > 0) {
				for (Square s : vmPnL.getSquares()) {
					s.setLotPnL(null);
				}
			}
			if (vmPnL.getStorageIns() != null && vmPnL.getStorageIns().size() > 0) {
				for (Storage s : vmPnL.getStorageIns()) {
					s.setBviSource(null);
				}
			}
		}

		return new ActionResult<>(true, "试算成功。", vmPnL);
	}

	@Override
	public ActionResult<String> txSaveNewLotSplit(LotSplit lotSplit) {
		BigDecimal index;// 标识
		boolean isNew; // 新增
		Lot new_lot = new Lot();
		Lot old_lot = new Lot();
		int i;
		String sql;
		/**
		 * 拆分合同批次
		 */
		for (Lot lot : lotSplit.getLots()) {
			if (org.apache.commons.lang3.StringUtils.isNotBlank(lot.getId())) {
				// 重新计算溢短装
				QuantityMaL mal_old = commonService.getQuantityMoreorLess(lot.getMoreOrLessBasis(), lot.getQuantity(),
						lot.getMoreOrLess());
				lot.setQuantityMore(DecimalUtil.nullToZero(mal_old.getQuantityMore()));
				lot.setQuantityLess(DecimalUtil.nullToZero(mal_old.getQuantityLess()));
				this.lotRepo.SaveOrUpdate(lot);
				old_lot = com.smm.ctrm.util.BeanUtils.copy(lot);
			} else {
				// 重新计算溢短装
				QuantityMaL mal_new = commonService.getQuantityMoreorLess(lot.getMoreOrLessBasis(), lot.getQuantity(),
						lot.getMoreOrLess());
				lot.setQuantityMore(DecimalUtil.nullToZero(mal_new.getQuantityMore()));
				lot.setQuantityLess(DecimalUtil.nullToZero(mal_new.getQuantityLess()));
				lot.setId(this.lotRepo.SaveOrUpdateRetrunId(lot));
				new_lot = com.smm.ctrm.util.BeanUtils.copy(lot);
			}
		}

		/**
		 * 拆分货物
		 */
		if (lotSplit.Split_Storage != null && lotSplit.Split_Storage.size() > 0) {
			new_lot.setQuantityDelivered(BigDecimal.ZERO);
			old_lot.setQuantityDelivered(BigDecimal.ZERO);
		} else {
			new_lot.setQuantityDelivered(BigDecimal.ZERO);
			new_lot.setIsDelivered(false);
			this.lotRepo.SaveOrUpdate(new_lot);
		}
		for (i = 0; i < lotSplit.Split_Storage.size();) {
			Storage old_storage = lotSplit.Split_Storage.get(i);
			Storage new_storage = lotSplit.Split_Storage.get(i + 1);

			if (old_storage.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
				old_storage.setLotId(new_lot.getId());
				old_storage.setQuantity(new_storage.getQuantity());
				old_storage.setAmount(old_storage.getQuantity().multiply(old_storage.getPrice()));
				old_storage.setStorageQuantity(old_storage.getQuantity());
				this.storageRepository.SaveOrUpdate(old_storage);

				// 更新批次中的交货数量
				new_lot.setQuantityDelivered(new_lot.getQuantityDelivered().add(old_storage.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);

				this.lotRepo.SaveOrUpdate(old_lot);

				String tableName;
				ReceiptShip receipt_ship = new ReceiptShip();
				Pending pending = new Pending();
				switch (old_lot.getSpotDirection()) {
				case SpotType.Purchase: // 采购
					tableName = old_storage.getRefName();
					if (tableName.equals("ReceiptShip")) {
						// --- 收货单挪到新批次 ---
						DetachedCriteria dc = DetachedCriteria.forClass(ReceiptShip.class);
						dc.add(Restrictions.eq("Id", old_storage.getRefId()));
						receipt_ship = this.receiptShipRepo.GetQueryable(ReceiptShip.class).where(dc).firstOrDefault();
						receipt_ship.setLotId(new_lot.getId());
						this.receiptShipRepo.SaveOrUpdate(receipt_ship);
					}
					break;
				case SpotType.Sell:// 销售
					tableName = old_storage.getRefName();
					if (tableName.equals("ReceiptShip")) {
						// ---- 发货单挪到新批次 ---
						DetachedCriteria dc2 = DetachedCriteria.forClass(ReceiptShip.class);
						dc2.add(Restrictions.eq("Id", old_storage.getRefId()));
						receipt_ship = this.receiptShipRepo.GetQueryable(ReceiptShip.class).where(dc2).firstOrDefault();
						receipt_ship.setLotId(new_lot.getId());
						this.receiptShipRepo.SaveOrUpdate(receipt_ship);
					}
					break;
				default:
					break;
				}
			} else {

				// 新增货物
				new_storage.setLotId(new_lot.getId());
				new_storage.setGross(new_storage.getQuantity());
				new_storage.setGrossAtFactory(new_storage.getQuantity());
				new_storage.setStorageQuantity(new_storage.getQuantity());
				new_storage.setId(this.storageRepository.SaveOrUpdateRetrunId(new_storage));

				// 更新批次中的交货数量
				new_lot.setQuantityDelivered(new_lot.getQuantityDelivered().add(new_storage.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);

				// 更新交付货物
				old_storage.setGross(old_storage.getQuantity());
				old_storage.setGrossAtFactory(old_storage.getQuantity());
				old_storage.setStorageQuantity(old_storage.getQuantity());
				this.storageRepository.SaveOrUpdate(old_storage);

				// 更新批次中的交货数量
				old_lot.setQuantityDelivered(old_lot.getQuantityDelivered().add(old_storage.getQuantity()));
				this.lotRepo.SaveOrUpdate(old_lot);

				String tableName;
				ReceiptShip old_receipt_ship = new ReceiptShip();
				Pending old_pending = new Pending();
				switch (old_lot.getSpotDirection()) {
				case SpotType.Purchase: // 采购单
					tableName = old_storage.getRefName();
					if (tableName.equals("ReceiptShip")) {
						// --- 旧收货单数量更新 ---
						DetachedCriteria dc3 = DetachedCriteria.forClass(ReceiptShip.class);
						dc3.add(Restrictions.eq("Id", old_storage.getRefId()));
						old_receipt_ship = this.receiptShipRepo.GetQueryable(ReceiptShip.class).where(dc3)
								.firstOrDefault();
						old_receipt_ship.setWeight(old_storage.getQuantity());
						this.receiptShipRepo.SaveOrUpdate(old_receipt_ship);

						// --- 新收货单数量、批次更新---
						ReceiptShip new_receipt_ship = com.smm.ctrm.util.BeanUtils.copy(old_receipt_ship);
						new_receipt_ship.setId(null);
						new_receipt_ship.setLotId(new_lot.getId());
						new_receipt_ship.setWeight(new_storage.getQuantity());
						new_storage.setRefId(this.receiptShipRepo.SaveOrUpdateRetrunId(new_receipt_ship));
						new_storage.setRefName("ReceiptShip");
						this.storageRepository.SaveOrUpdate(new_storage);
					}
					break;
				case SpotType.Sell:// 销售单
					tableName = old_storage.getRefName();
					if (tableName == "ReceiptShip") {
						// --- 旧发货单数量更新 ---
						DetachedCriteria dc4 = DetachedCriteria.forClass(ReceiptShip.class);
						dc4.add(Restrictions.eq("Id", old_storage.getRefId()));
						old_receipt_ship = this.receiptShipRepo.GetQueryable(ReceiptShip.class).where(dc4)
								.firstOrDefault();
						old_receipt_ship.setWeight(old_storage.getQuantity());
						this.receiptShipRepo.SaveOrUpdate(old_receipt_ship);

						// --- 新发货单数量、批次更新---
						ReceiptShip new_receipt_ship = com.smm.ctrm.util.BeanUtils.copy(old_receipt_ship);
						new_receipt_ship.setId(null);
						new_receipt_ship.setLotId(new_lot.getId());
						new_receipt_ship.setWeight(new_storage.getQuantity());
						new_storage.setRefId(this.receiptShipRepo.SaveOrUpdateRetrunId(new_receipt_ship));
						new_storage.setRefName("ReceiptShip");
						this.storageRepository.SaveOrUpdate(new_storage);

						// --- 新增发货单，对应生成新的审核单 ---
						// old_pending =
						// Repository.GetQueryable<Pending>().Where(p =>
						// p.ReceiptShipId ==
						// old_storage.RefId).FirstOrDefault();
						// var new_pending = old_pending.Copy<Pending>();
						// new_pending.Id = null;
						// new_pending.ReceiptShipId = new_receipt_ship.Id;
						// Repository.SaveOrUpdate(new_pending);

						DetachedCriteria dc5 = DetachedCriteria.forClass(Pending.class);
						dc5.add(Restrictions.eq("ReceiptShipId", old_storage.getRefId()));
						old_pending = this.pendingRepo.GetQueryable(Pending.class).where(dc5).firstOrDefault();
						Pending new_pending = com.smm.ctrm.util.BeanUtils.copy(old_pending);
						new_pending.setId(null);
						new_pending.setReceiptShipId(new_receipt_ship.getId());
						this.pendingRepo.SaveOrUpdate(new_pending);
					}

					// --- 拆对手采购方的货 ---
					boolean isHasReivalOrder = false;
					String CounterpartyId;
					Contract contract = this.contractRepository.getOneById(old_lot.getContractId(), Contract.class);
					if (contract.getRivalOrderID() != null) // 对手订单
						isHasReivalOrder = true;
					else
						isHasReivalOrder = false;

					if (isHasReivalOrder) // 是不是换货库存订单
						CounterpartyId = old_storage.getCounterpartyId2();
					else
						CounterpartyId = old_storage.getCounterpartyId3();

					Storage old_storageIn = new Storage();
					if (CounterpartyId != null) {
						old_storageIn = this.storageRepository.getOneById(CounterpartyId, Storage.class);

						// 拆采购方的货
						Storage new_storageIn = com.smm.ctrm.util.BeanUtils.copy(old_storageIn);
						new_storageIn.setId(null);
						new_storageIn.setQuantity(new_storage.getQuantity());
						new_storageIn.setGross(new_storage.getQuantity());
						new_storageIn.setGrossAtFactory(new_storage.getQuantity());
						new_storageIn.setStorageQuantity(new_storageIn.getQuantity());

						new_storage.setCounterpartyId3(this.storageRepository.SaveOrUpdateRetrunId(new_storageIn));
						this.storageRepository.SaveOrUpdate(new_storage);

						old_storageIn.setQuantity(old_storage.getQuantity());
						old_storageIn.setStorageQuantity(old_storage.getQuantity());
						old_storageIn.setGross(old_storage.getQuantity());
						old_storageIn.setGrossAtFactory(old_storage.getQuantity());
						this.storageRepository.SaveOrUpdate(old_storageIn);

						// 新增采购方货对应发票中的货
						sql = String.format("select InvoiceId from Physical.InvoiceStorage where StorageId = '%s'",
								old_storageIn.getId());
						List<String> lstInvoiceId = this.storageRepository.ExecuteCorrectlySql2(sql);
						if (lstInvoiceId != null && lstInvoiceId.size() > 0) {
							// 有发票才插入
							for (String inv : lstInvoiceId) {
								sql = String.format(
										"insert into Physical.InvoiceStorage(InvoiceId, StorageId) Values('%s','%s')",
										inv.toString(), new_storageIn.getId());
								this.storageRepository.ExecuteCorrectlySql3(sql);
							}
						}
					}

					break;
				}
			}

			i += 2;
		}
		/**
		 * 拆分资金
		 */
		if (lotSplit.getSplit_Fund() == null || lotSplit.getSplit_Fund().size() == 0) {
			new_lot.setQuantityFunded(BigDecimal.ZERO);
			new_lot.setAmountFunded(BigDecimal.ZERO);
			new_lot.setAmountFundedDraft(BigDecimal.ZERO);
			new_lot.setIsFunded(false);
			this.lotRepo.SaveOrUpdate(new_lot);
		} else {
			old_lot.setQuantityFunded(BigDecimal.ZERO);
			old_lot.setAmountFunded(BigDecimal.ZERO);
			old_lot.setAmountFundedDraft(BigDecimal.ZERO);

			new_lot.setQuantityFunded(BigDecimal.ZERO);
			new_lot.setAmountFunded(BigDecimal.ZERO);
			new_lot.setAmountFundedDraft(BigDecimal.ZERO);
		}

		for (i = 0; i < lotSplit.Split_Fund.size();) {
			Fund old_fund = lotSplit.getSplit_Fund().get(i);
			Fund new_fund = lotSplit.getSplit_Fund().get(i + 1);

			if (old_fund.getAmount().compareTo(BigDecimal.ZERO) == 0) {
				old_fund.setLotId(new_lot.getId());
				old_fund.setQuantity(new_fund.getQuantity());
				old_fund.setAmount(new_fund.getAmount());
				this.fundRepository.SaveOrUpdate(old_fund);

				new_lot.setAmountFunded(new_lot.getAmountFunded().add(new_fund.getAmount()));
				new_lot.setQuantityFunded(new_lot.getQuantityFunded().add(new_fund.getAmount()));
				new_lot.setAmountFundedDraft(new_lot.getAmountFundedDraft().add(new_fund.getAmount()));
				this.lotRepo.SaveOrUpdate(new_lot);
				this.lotRepo.SaveOrUpdate(old_lot);

			} else {

				// 新增资金
				new_fund.setLotId(new_lot.getId());
				new_fund.setId(this.fundRepository.SaveOrUpdateRetrunId(new_fund));
				new_lot.setAmountFunded(new_lot.getAmountFunded().add(new_fund.getAmount()));
				new_lot.setQuantityFunded(new_lot.getQuantityFunded().add(new_fund.getAmount()));
				new_lot.setAmountFundedDraft(new_lot.getAmountFundedDraft().add(new_fund.getAmount()));
				this.lotRepo.SaveOrUpdate(new_lot);

				old_lot.setAmountFundedDraft(old_lot.getAmountFundedDraft().add(old_fund.getAmount()));
				old_lot.setAmountFunded(old_lot.getAmountFunded().add(old_fund.getAmount()));
				old_lot.setQuantityFunded(old_lot.getQuantityFunded().add(old_fund.getAmount()));
				// 更新资金
				this.fundRepository.SaveOrUpdate(old_fund);

				this.lotRepo.SaveOrUpdate(old_lot);

				Pending old_pending = new Pending();
				switch (old_lot.getSpotDirection()) {
				case SpotType.Purchase: // 采购单

					// --- 付款资金拆分后，对应付款审核单新增对应记录 ---
					DetachedCriteria dc6 = DetachedCriteria.forClass(Pending.class);
					dc6.add(Restrictions.eq("FundId", old_fund.getId()));
					old_pending = this.pendingRepo.GetQueryable(Pending.class).where(dc6).firstOrDefault();
					if (old_pending != null) {
						Pending new_pending = com.smm.ctrm.util.BeanUtils.copy(old_pending);
						new_pending.setId(null);
						new_pending.setFundId(new_fund.getId());
						this.pendingRepo.SaveOrUpdate(new_pending);
					}
					break;
				case SpotType.Sell:
					break;
				default:
					break;
				}

			}

			i += 2;
		}
		/**
		 * 拆分发票
		 */
		if (lotSplit.getSplit_Invoice() != null && lotSplit.getSplit_Invoice().size() > 0) {
			new_lot.setQuantityInvoiced(BigDecimal.ZERO);
			new_lot.setAmountInvoiced(BigDecimal.ZERO);

			old_lot.setQuantityInvoiced(BigDecimal.ZERO);
			old_lot.setAmountInvoiced(BigDecimal.ZERO);
		} else {
			new_lot.setQuantityInvoiced(BigDecimal.ZERO);
			new_lot.setAmountInvoiced(BigDecimal.ZERO);
			new_lot.setIsInvoiced(false);
			new_lot.setStorages(null);
			this.lotRepo.SaveOrUpdate(new_lot);
		}

		for (i = 0; i < lotSplit.getSplit_Invoice().size();) {
			Invoice old_invoice = lotSplit.getSplit_Invoice().get(i);
			Invoice new_invoice = lotSplit.getSplit_Invoice().get(i + 1);

			if (old_invoice.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
				old_invoice.setLotId(new_lot.getId());
				old_invoice.setQuantity(new_invoice.getQuantity());
				old_invoice.setAmount(old_invoice.getQuantity().multiply(old_invoice.getPrice()));
				sql = String.format(
						"select * from Physical.Storage ps right join Physical.InvoiceStorage pi on ps.Id = pi.StorageId where pi.InvoiceId = '%s'",
						old_invoice.getId());
				old_invoice.setStorages(this.storageRepository.ExecuteCorrectlySql(sql, Storage.class));
				this.invoiceRepository.SaveOrUpdate(old_invoice);

				// 更新批次中的发票数量
				new_lot.setAmountInvoiced(new_lot.getAmountInvoiced().add(old_invoice.getAmount()));
				this.lotRepo.SaveOrUpdate(new_lot);
				this.lotRepo.SaveOrUpdate(old_lot);

			} else {

				// 新增发票
				index = new BigDecimal(new_invoice.getSplitAmount());
				new_invoice.setLotId(new_lot.getId());
				new_invoice.setInvoiceNo(new_invoice.getInvoiceNo() + "/1");
				List<Storage> sList = new ArrayList<>();
				for (Storage s : lotSplit.getSplit_Storage()) {
					if (s.getSplitCount() != null && s.getSplitCount().compareTo(index) == 0) {
						sList.add(s);
					}
				}
				new_invoice.setStorages(sList);
				new_invoice.setId(this.invoiceRepository.SaveOrUpdateRetrunId(new_invoice));

				// 更新批次中的发票数量
				new_lot.setAmountInvoiced(new_lot.getAmountInvoiced().add(new_invoice.getAmount()));
				this.lotRepo.SaveOrUpdate(new_lot);

				// 更新发票
				List<String> storage_ids = old_invoice.getStorages().stream().map(s -> s.getId())
						.collect(Collectors.toList());
				List<Storage> invoice_storage_list = new ArrayList<Storage>();
				for (String id : storage_ids) {
					List<Storage> list = lotSplit.Split_Storage.stream().filter(s -> s.getId().equals(id))
							.collect(Collectors.toList());
					invoice_storage_list.addAll(list);
				}
				old_invoice.setStorages(invoice_storage_list);
				this.invoiceRepository.SaveOrUpdate(old_invoice);

				// 更新批次中的发票数量
				old_lot.setAmountInvoiced(old_lot.getAmountInvoiced().add(old_invoice.getAmount()));
				this.lotRepo.SaveOrUpdate(old_lot);

				Pending old_pending = new Pending();
				switch (old_lot.getSpotDirection()) {
				case SpotType.Purchase: // 采购单
					break;
				case SpotType.Sell: // 销售单

					// --- 发票拆分后，对应的发票审核单新增记录 ---
					DetachedCriteria dc7 = DetachedCriteria.forClass(Pending.class);
					dc7.add(Restrictions.eq("InvoiceId", old_invoice.getId()));
					old_pending = this.pendingRepo.GetQueryable(Pending.class).where(dc7).firstOrDefault();
					Pending new_pending = com.smm.ctrm.util.BeanUtils.copy(old_pending);
					new_pending.setId(null);
					new_pending.setInvoiceId(new_invoice.getId());
					this.pendingRepo.SaveOrUpdate(new_pending);
					break;
				default:
					break;
				}

			}
			i += 2;
		}
		/**
		 * 拆分费用
		 */
		for (i = 0; i < lotSplit.getSplit_Fee().size();) {
			Invoice old_fee = lotSplit.getSplit_Fee().get(i);
			Invoice new_fee = lotSplit.getSplit_Fee().get(i + 1);

			if (old_fee.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
				old_fee.setLotId(new_lot.getId());
				old_fee.setAmount(new_fee.getAmount());
				this.invoiceRepository.SaveOrUpdate(old_fee);
			} else {
				// 新增费用发票
				new_fee.setLotId(new_lot.getId());
				new_fee.setInvoiceNo(new_fee.getInvoiceNo() + "/1");
				new_fee.setId(this.invoiceRepository.SaveOrUpdateRetrunId(new_fee));
				// 更新费用发票
				this.invoiceRepository.SaveOrUpdate(old_fee);
			}
			i += 2;
		}
		/**
		 * 拆分点价
		 */
		if (lotSplit.getSplit_Price() != null && lotSplit.getSplit_Price().size() > 0) {
			new_lot.setQuantityPriced(BigDecimal.ZERO);
			old_lot.setQuantityPriced(BigDecimal.ZERO);

			new_lot.setQuantityUnPriced(BigDecimal.ZERO);
			new_lot.setQuantityUnPriced(BigDecimal.ZERO);

			new_lot.setIsPriced(old_lot.getIsPriced());
		} else {
			new_lot.setQuantityPriced(BigDecimal.ZERO);
			new_lot.setQuantityUnPriced(BigDecimal.ZERO);

			if (new_lot.getMajorType().equals(MajorType.Fix)) {
				new_lot.setIsPriced(true);
				new_lot.setQuantityPriced(new_lot.getQuantity());
				old_lot.setQuantityPriced(old_lot.getQuantity());
				this.lotRepo.SaveOrUpdate(old_lot);
			} else {
				new_lot.setIsPriced(false);

			}
			this.lotRepo.SaveOrUpdate(new_lot);
		}

		for (i = 0; i < lotSplit.getSplit_Price().size();) {
			Pricing old_price = lotSplit.getSplit_Price().get(i);
			Pricing new_price = lotSplit.getSplit_Price().get(i + 1);

			if (old_price.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
				old_price.setLotId(new_lot.getId());
				old_price.setQuantity(new_price.getQuantity());
				this.pricingRepository.SaveOrUpdate(old_price);

				// 更新批次中的已点价数量
				new_lot.setQuantityPriced(new_lot.getQuantityPriced().add(old_price.getQuantity()));
				new_lot.setQuantityUnPriced(
						new_lot.getQuantityUnPriced().add(new_lot.getQuantity()).subtract(old_price.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);
				this.lotRepo.SaveOrUpdate(old_lot);
				List<PricingRecord> pricingIds = lotSplit.getSplit_PriceRecord().stream()
						.filter(r -> r.getPricingId().equals(old_price.getId())).collect(Collectors.toList());
				for (PricingRecord record : pricingIds) {
					record.setLotId(new_lot.getId());
					record.setQuantity(old_price.getQuantity());
					this.pricingRecordRepository.SaveOrUpdate(record);
				}
			} else {

				// 新增Pricing
				index = new_price.getSplitCount();
				new_price.setSplitCount(BigDecimal.ZERO);
				new_price.setLotId(new_lot.getId());
				new_price.setIsSplitted(true);
				new_price.setId(this.pricingRepository.SaveOrUpdateRetrunId(new_price));

				// 更新批次中的已点价数量
				new_lot.setQuantityPriced(new_lot.getQuantityPriced().add(new_price.getQuantity()));
				new_lot.setQuantityUnPriced(
						new_lot.getQuantityUnPriced().add(new_lot.getQuantity()).subtract(new_price.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);
				List<PricingRecord> prs = new ArrayList<>();
				for (PricingRecord s : lotSplit.getSplit_PriceRecord()) {
					if (s.getSplitCount() != null && s.getSplitCount().compareTo(index) == 0) {
						prs.add(s);
					}
				}
				for (PricingRecord record : prs) {
					record.setLotId(new_lot.getId());
					record.setSplitCount(null);
					record.setPricingId(new_price.getId());
					this.pricingRecordRepository.SaveOrUpdate(record);
				}

				// 更新Pricing
				old_price.setLot(old_lot);
				old_price.setLotNo(old_lot.getLotNo());
				this.pricingRepository.SaveOrUpdate(old_price);
				// 更新批次中的已点价数量
				old_lot.setQuantityPriced(old_lot.getQuantityPriced().add(old_price.getQuantity()));
				old_lot.setQuantityUnPriced(DecimalUtil.nullToZero(old_lot.getQuantityUnPriced())
						.add(DecimalUtil.nullToZero(old_lot.getQuantity()))
						.subtract(DecimalUtil.nullToZero(old_price.getQuantity())));
				this.lotRepo.SaveOrUpdate(old_lot);
				List<PricingRecord> prs2 = lotSplit.getSplit_PriceRecord().stream()
						.filter(r -> r.getPricingId().equals(old_price.getId())).collect(Collectors.toList());
				for (PricingRecord record : prs2) {
					record.setSplitCount(null);
					record.setPricingId(old_price.getId());
					this.pricingRecordRepository.SaveOrUpdate(record);
				}
			}
			i += 2;
		}
		/**
		 * 拆分套保
		 */
		if (lotSplit.getSplit_Position() != null && lotSplit.getSplit_Position().size() > 0) {
			old_lot.setQuantityHedged(BigDecimal.ZERO);
			new_lot.setQuantityHedged(BigDecimal.ZERO);
		} else {
			new_lot.setQuantityHedged(BigDecimal.ZERO);
			new_lot.setIsHedged(false);
			this.lotRepo.SaveOrUpdate(new_lot);
		}

		for (i = 0; i < lotSplit.getSplit_Position().size();) {
			Position old_position = lotSplit.getSplit_Position().get(i);
			Position new_position = lotSplit.getSplit_Position().get(i + 1);

			if (old_position.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
				old_position.setLotId(new_lot.getId());
				old_position.setQuantity(new_position.getQuantity());
				this.positionRepository.SaveOrUpdate(old_position);

				// 更新批次中的保值数量
				new_lot.setQuantityHedged(new_lot.getQuantityHedged().add(old_position.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);
				this.lotRepo.SaveOrUpdate(old_lot);
			} else {

				// 新增Position
				new_position.setLotId(new_lot.getId());
				new_position.setId(this.positionRepository.SaveOrUpdateRetrunId(new_position));

				// 更新批次中的保值数量
				new_lot.setQuantityHedged(new_lot.getQuantityHedged().add(new_position.getQuantity()));
				this.lotRepo.SaveOrUpdate(new_lot);

				// 更新Position
				this.positionRepository.SaveOrUpdate(old_position);

				// 更新批次中的保值数量
				old_lot.setQuantityHedged(old_lot.getQuantityHedged().add(old_position.getQuantity()));
				this.lotRepo.SaveOrUpdate(old_lot);

			}

			i += 2;
		}
		/**
		 * 这段代码是为了级联保存品牌
		 */
		if (lotSplit.getLots() != null && lotSplit.getLots().size() > 0 && lotSplit.getLots().get(0).getBrands() != null
				&& lotSplit.getLots().get(0).getBrands().size() > 0) {

			List<String> ids = new ArrayList<>();
			Lot lot = lotSplit.getLots().get(0);
			for (Brand brand : lot.getBrands()) {
				ids.add(brand.getId());
			}
			List<Brand> brands = brandRepository.GetQueryable(Brand.class)
					.where(DetachedCriteria.forClass(Brand.class).add(Restrictions.in("Id", ids))).toList();
			new_lot.setBrands(brands);
			old_lot.setBrands(brands);
			this.lotRepo.SaveOrUpdate(new_lot);
			this.lotRepo.SaveOrUpdate(old_lot);
		}

		ActionResult<String> result = new ActionResult<String>();
		result.setData(old_lot.getId() + "," + new_lot.getId());
		result.setMessage("拆分成功");
		result.setSuccess(true);
		return result;
	}

	@Override
	public ActionResult<LotPnL> NewLotSettleTrial(Param4LotPnL param4LotPnL) {
		/**
		 * 有效性检查
		 */
		GlobalSet globalSet = this.globalSetRepository.GetQueryable(GlobalSet.class).firstOrDefault();
		if (globalSet == null)
			return new ActionResult<LotPnL>(false, "缺少GlobalSet设置。");

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		if (param4LotPnL.getLotId() == null)
			return new ActionResult<LotPnL>(false, "Params is unavailable: LotId");
		// 结算之前先更新保值头寸价格
		UpdateLotHedgedPrce(param4LotPnL.getLotId());

		Lot lot = this.lotRepo.getOneById(param4LotPnL.getLotId(), Lot.class);
		if (lot == null)
			return new ActionResult<LotPnL>(false, "The Lot is not existed already.");

		if (lot.getIsSettled())
			return new ActionResult<LotPnL>(false, "The invoice is settled already.");

		if (lot.getIsAccounted())
			return new ActionResult<LotPnL>(false, "The invoice is accounted already.");

		/**
		 * 构建:vmPnL
		 */

		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(lot.getId());
		vmPnL.setContractFullNo(lot.getFullNo());
		vmPnL.setCustomerName(lot.getCustomer().getName());
		vmPnL.setPremium(lot.getPremium() != null ? lot.getPremium() : BigDecimal.ZERO);

		// 销售发生的fee的汇总金额
		BigDecimal fee4Sell = BigDecimal.ZERO;

		// 采购发生的fee的汇总金额
		BigDecimal fee4Purchase = BigDecimal.ZERO;

		boolean isHasReivalOrder = false;

		Contract contract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);
		if (contract.getRivalOrderID() != null) // 对手订单
			isHasReivalOrder = true;
		else
			isHasReivalOrder = false;

		List<String> lstBuyLot = new ArrayList<String>();

		Lot lotin = new Lot();
		/**
		 * 销售一侧的商品明细
		 */
		String sql = "";

		DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> storageOuts = this.storageRepository.GetQueryable(Storage.class).where(dc).toList();
		vmPnL.setStorageOuts(storageOuts);
		for (Storage outs : vmPnL.getStorageOuts()) {
			outs.setIsSelect(true);
			sql = String.format(
					"select inv.InvoiceNo as InvoiceNo from Physical.Storage ps inner "
							+ "join Physical.InvoiceStorage ins on ps.Id = ins.StorageId "
							+ "inner join Physical.Invoice inv on inv.Id = ins.InvoiceId  where ps.Id = '%s'",
					outs.getId());

			List<String> invoiceNos = this.storageRepository.ExecuteCorrectlySql4(sql, "InvoiceNo");
			if (invoiceNos != null && invoiceNos.size() > 0) {
				if (invoiceNos.size() > 1) {
					for (String no : invoiceNos) {
						if (outs.getInvoiceNo() == null)
							outs.setInvoiceNo(no != null ? no : null);
						else
							outs.setInvoiceNo(no != null ? outs.getInvoiceNo() + "," + no : outs.getInvoiceNo());
					}
				} else {
					outs.setInvoiceNo(invoiceNos.get(0) != null ? invoiceNos.get(0) : null);
				}
			}

			outs.setPrice(lot.getMajor());
			outs.setFee(lot.getFee() == null ? BigDecimal.ZERO : lot.getFee());
			outs.setPremium(lot.getPremium());
			outs.setCurrency(lot.getCurrency());

		}

		if (vmPnL.getStorageOuts() != null) {
			// vmPnL.QuantitySell = lot.Quantity;
			BigDecimal pnSum = BigDecimal.ZERO;
			for (Storage pn : vmPnL.getStorageOuts()) {
				pnSum = pnSum.add(pn.getQuantity());
			}
			vmPnL.setQuantitySell(pnSum);

			// 销售金额
			vmPnL.setAmountSell(BigDecimal.ZERO);

			for (Storage so : vmPnL.getStorageOuts()) {
				if (so.getCurrency().equals(currency)) {
					vmPnL.setAmountSell(vmPnL.getAmountSell()
							.add(so.getQuantity().multiply(DecimalUtil.nullToZero(so.getPrice()))));
				} else {
					if ("CNY".equals(so.getCurrency())) {
						vmPnL.setAmountSell(vmPnL.getAmountSell()
								.add(so.getQuantity().multiply(DecimalUtil.nullToZero(so.getPrice())).divide(rate, 5,
										RoundingMode.HALF_EVEN)));
					} else {
						vmPnL.setAmountSell(vmPnL.getAmountSell()
								.add(so.getQuantity().multiply(DecimalUtil.nullToZero(so.getPrice())).multiply(rate)));
					}
				}
			}

			// 销售均价
			if (vmPnL.getQuantitySell().compareTo(BigDecimal.ZERO) != 0)
				vmPnL.setPriceSell(DecimalUtil.divideForQuantity(vmPnL.getAmountSell(), vmPnL.getQuantitySell())
						.setScale(4, RoundingMode.HALF_EVEN));

			vmPnL.setStorageOuts(commonService.SimplifyDataStorageList(vmPnL.getStorageOuts()));
		}
		fee4Sell = lot.getQuantity().multiply(DecimalUtil.nullToZero(lot.getRealFee()));

		/**
		 * 采购一侧的商品明细
		 */
		vmPnL.setStorageIns(new ArrayList<Storage>());
		for (Storage v : vmPnL.getStorageOuts()) {
			if (!isHasReivalOrder) {
				// 取得对手方的交付记录，并且加入到List中
				if (v.getCounterpartyId3() != null)
					vmPnL.getStorageIns().add(this.storageRepository.getOneById(v.getCounterpartyId3(), Storage.class));
			} else {
				if (v.getCounterpartyId2() != null)
					vmPnL.getStorageIns().add(this.storageRepository.getOneById(v.getCounterpartyId2(), Storage.class));
			}
		}

		if (vmPnL.getStorageIns() != null) {
			BigDecimal pnSum = BigDecimal.ZERO;
			for (Storage pn : vmPnL.getStorageIns()) {
				pnSum = pnSum.add(pn.getQuantity());
			}
			// 采购数量
			vmPnL.setQuantityPurchase(pnSum);

			// 采购成本
			vmPnL.setAmountPurchase(BigDecimal.ZERO);

			for (Storage si : vmPnL.getStorageIns()) {
				si.setIsSelect(true);
				sql = String.format(
						"select inv.InvoiceNo as InvoiceNo from Physical.Storage ps "
								+ "left join Physical.InvoiceStorage ins on ps.Id = ins.StorageId "
								+ "left join Physical.Invoice inv on inv.Id = ins.InvoiceId  where ps.Id ='%s'",
						si.getId());

				List<String> invoiceNos = this.storageRepository.ExecuteCorrectlySql4(sql, "InvoiceNo");
				if (invoiceNos != null && invoiceNos.size() > 0) {
					if (invoiceNos.size() > 1) {
						for (String no : invoiceNos) {
							if (si.getInvoiceNo() == null)
								si.setInvoiceNo(no != null ? no : null);
							else
								si.setInvoiceNo(no != null ? si.getInvoiceNo() + "," + no : si.getInvoiceNo());
						}
					} else {
						si.setInvoiceNo(invoiceNos.get(0) != null ? invoiceNos.get(0) : null);
					}
				}

				// 供应商
				sql = String.format("select c.Name from Basis.Customer c  right join "
						+ "Physical.Storage ps on c.Id = ps.CustomerId  where ps.Id = '%s'", si.getId());
				List<String> name = this.customerRepository.ExecuteCorrectlySql4(sql, "Name");

				if (name != null && name.size() > 0)
					si.setCustomerName(name.get(0));

				// 点价，升贴水，费用
				if (StringUtils.isNotBlank(si.getLotId())) {
					lotin = this.lotRepo.getOneById(si.getLotId(), Lot.class);
				}
				if (lotin != null) {
					si.setPrice(lotin.getMajor());
					si.setPremium(lotin.getPremium());
					si.setFee(lotin.getFee() == null ? BigDecimal.ZERO : lotin.getFee());
					si.setCurrency(lotin.getCurrency());
					fee4Purchase = fee4Purchase.add(DecimalUtil.nullToZero(lotin.getQuantity())
							.multiply(DecimalUtil.nullToZero(lotin.getRealFee())));
				}

				// 结算之前先更新保值头寸价格
				if (si.getLotId() != null && !lstBuyLot.contains(si.getLotId()))
					UpdateLotHedgedPrce(si.getLotId());

				if (currency.equals(si.getCurrency())) {
					vmPnL.setAmountPurchase(DecimalUtil.nullToZero(vmPnL.getAmountPurchase()).add(
							DecimalUtil.nullToZero(si.getQuantity()).multiply(DecimalUtil.nullToZero(si.getPrice()))));
				} else {
					if ("CNY".equals(si.getCurrency())) {
						vmPnL.setAmountPurchase(DecimalUtil.nullToZero(vmPnL.getAmountPurchase())
								.add(DecimalUtil.nullToZero(si.getQuantity())
										.multiply(DecimalUtil.nullToZero(si.getPrice()))
										.divide(rate, 5, RoundingMode.HALF_EVEN)));
					} else {
						vmPnL.setAmountPurchase(DecimalUtil.nullToZero(vmPnL.getAmountPurchase())
								.add(DecimalUtil.nullToZero(si.getQuantity())
										.multiply(DecimalUtil.nullToZero(si.getPrice())).multiply(rate)));
					}
				}
				if (si.getLotId() != null && !lstBuyLot.contains(si.getLotId()))
					lstBuyLot.add(si.getLotId());

			}

			vmPnL.setStorageIns(commonService.SimplifyDataStorageList(vmPnL.getStorageIns()));

		}

		// 采购价格
		if (vmPnL.getQuantityPurchase().compareTo(BigDecimal.ZERO) != 0)
			vmPnL.setPricePurchase(
					DecimalUtil.divideForQuantity(vmPnL.getAmountPurchase(), vmPnL.getQuantityPurchase()));

		vmPnL.setFee(fee4Sell.add(fee4Purchase));

		vmPnL.setPnL4Spot(DecimalUtil.nullToZero(vmPnL.getAmountSell())
				.subtract(DecimalUtil.nullToZero(vmPnL.getAmountPurchase())));

		/**
		 * 期货盈亏计算（暂时不删除，如果需要头寸结算的话，只需将此段代码打开
		 */
		DetachedCriteria dc2 = DetachedCriteria.forClass(Position.class);
		dc2.add(Restrictions.isNotNull("LotId"));
		dc2.add(Restrictions.eq("LotId", lot.getId()));
		List<Position> positionsOut = this.positionRepository.GetQueryable(Position.class).where(dc2).toList();
		List<Position> positionsIn = new ArrayList<Position>();
		for (String buylot : lstBuyLot) {
			DetachedCriteria dc3 = DetachedCriteria.forClass(Position.class);
			dc3.add(Restrictions.isNotNull("LotId"));
			dc3.add(Restrictions.eq("LotId", buylot));
			List<Position> buyPosition = this.positionRepository.GetQueryable(Position.class).where(dc3).toList();
			if (buyPosition != null && buyPosition.size() > 0) {
				positionsIn.addAll(buyPosition);
			}
		}

		List<Position> positions = new ArrayList<Position>();
		positions.addAll(positionsOut);
		positions.addAll(positionsIn);
		vmPnL.setPositions(positions);
		if (positions.size() > 0) {

			List<Position> lstPosition = positions;
			if (lstPosition.stream().anyMatch(x -> x.getIsSquared())) {
				return new ActionResult<LotPnL>(false, "存在已结算的头寸！");
			}

			List<Market> markets = this.marketRepository.GetQueryable(Market.class).toList();
			List<Square> squares = new ArrayList<Square>();
			// 根据不同市对齐头寸
			for (Market market : markets) {
				List<Position> lstPosition4Market = lstPosition.stream()
						.filter(x -> x.getMarketId().equals(market.getId())).collect(Collectors.toList());
				if (lstPosition4Market.size() > 0) {
					ActionResult<LotPnL> pnlbyMarket = Position4Market(vmPnL, lstPosition4Market, market);
					if (pnlbyMarket.isSuccess() && pnlbyMarket.getData() != null) {
						squares.addAll(pnlbyMarket.getData().getSquares());
						vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().add(pnlbyMarket.getData().getPnL4Carry())); // 调期费用
					}
				}

			}
			if (squares.size() > 0) {
				BigDecimal pnSum = BigDecimal.ZERO;
				for (Square pn : squares) {
					pnSum = pnSum.add(pn.getPnL());
				}
				vmPnL.setPnL4Hedge(pnSum);
			}
			vmPnL.setSquares(squares.stream().sorted((p1, p2) -> p1.getPromptDate().compareTo(p2.getPromptDate()))
					.collect(Collectors.toList()));
		}

		vmPnL.setPnLTotal(
				DecimalUtil.nullToZero(vmPnL.getPnL4Spot()).add(DecimalUtil.nullToZero(vmPnL.getPnL4Hedge()))); // 合计盈亏=
																												// 现货盈亏+
																												// 期货盈亏
		vmPnL.setCurrency(currency);

		ActionResult<LotPnL> result = new ActionResult<>();
		result.setSuccess(true);
		result.setMessage("试算成功。");
		result.setData(vmPnL);
		return result;
	}

	/**
	 * 根据市场进行不同的头寸结算
	 * 
	 * @param vmPnlParam
	 * @param positions
	 * @param market
	 * @return
	 */
	private ActionResult<LotPnL> Position4Market(LotPnL vmPnlParam, List<Position> positions, Market market) {
		switch (market.getCode().toUpperCase()) {
		case "LME":
			return PositionTrial4LME(vmPnlParam, positions);
		case "SFE":
			return PositionTrial4LME(vmPnlParam, positions);
		case "SGX":
			return PositionTrial4LME(vmPnlParam, positions);
		}
		return new ActionResult<LotPnL>(false, "不支持的市场");
	}

	private ActionResult<LotPnL> PositionTrial4LME(LotPnL vmPnlParam, List<Position> positions) {

		/**
		 * 有效性检查
		 */
		GlobalSet globalSet = this.globalSetRepository.GetQueryable(GlobalSet.class).firstOrDefault();
		if (globalSet == null)
			return new ActionResult<LotPnL>(false, "缺少GlobalSet设置。");

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		/**
		 * 构建:vmPnL
		 */
		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(vmPnlParam.getLotId());
		vmPnL.setContractFullNo(vmPnlParam.getContractFullNo());
		vmPnL.setCustomerName(vmPnlParam.getCustomerName());
		vmPnL.setPremium(vmPnlParam.getPremium());

		List<Position> lstPosition = positions;
		// 因为数据库里是带有正负号的，原来的算法要求没有正负号，暂时作这样的处理
		if (lstPosition != null) {
			/**
			 * 数据预处理
			 */

			/*
			 * var lstL = lstPosition.Where(pn => pn.LS == LS.LONG) .OrderBy(pn
			 * => pn.PromptDate) .ThenBy(pn => pn.Quantity) .ToList();
			 * 
			 * var lstS = lstPosition.Where(pn => pn.LS == LS.SHORT) .OrderBy(pn
			 * => pn.PromptDate) .ThenBy(pn => pn.Quantity) .ToList();
			 */

			List<Position> lstL = lstPosition.stream().filter(pn -> pn.getLS().equals(LS.LONG))
					.sorted((p1, p2) -> p1.getPromptDate().compareTo(p2.getPromptDate()))
					.sorted((p1, p2) -> p1.getQuantity().compareTo(p2.getQuantity())).collect(Collectors.toList());

			List<Position> lstS = lstPosition.stream().filter(pn -> pn.getLS().equals(LS.SHORT))
					.sorted((p1, p2) -> p1.getPromptDate().compareTo(p2.getPromptDate()))
					.sorted((p1, p2) -> p1.getQuantity().compareTo(p2.getQuantity())).collect(Collectors.toList());

			List<Square> squares = new ArrayList<Square>();
			do {
				/**
				 * 先取2侧
				 */
				Position l = null;
				Position s = null;
				if (lstL.size() > 0) {
					l = lstL.get(0);
					if (l != null) {
						// 根据市场品种到期日 日期判断可能会有问题
						for (Position pn : lstS) {
							if (pn.getPromptDate().equals(l.getPromptDate()) && pn.getMarketId().equals(l.getMarketId())
									&& pn.getCommodityId().equals(l.getCommodityId())) {
								s = pn;
								break;
							}
						}
					}
				} else {
					if (lstS.size() > 0)
						s = lstS.get(0);
				}

				/**
				 * LS两边都不为空
				 */

				if (l != null && s != null) {
					BigDecimal qtyOfLong = l.getQuantity();
					BigDecimal qtyOfShort = s.getQuantity();
					if (qtyOfLong.compareTo(qtyOfShort.abs()) == 0) {
						Square t = new Square();
						// 作为冗余
						t.setPromptDate(l.getPromptDate());
						t.setQuantity(l.getQuantity());
						t.setRefLong(l.getOurRef());
						t.setPriceLong(DecimalUtil.nullToZero(l.getOurPrice()));
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(DecimalUtil.nullToZero(s.getOurPrice()));
						t.setPromptDateShort(s.getPromptDate());
						t.setTradeDateShort(s.getTradeDate());
						t.setCurrency(s.getCurrency());

						t.setLongId(l.getId());
						t.setShortId(s.getId());
						t.setQuantityLong(l.getQuantity());
						t.setQuantityShort(s.getQuantity());
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());
						t.setLotId(vmPnlParam.getLotId());

						squares.add(t);
						lstL.remove(l);
						lstS.remove(s);
					} else if (qtyOfLong.compareTo(qtyOfShort.abs()) > 0) {
						l.setQuantity(l.getQuantity().subtract(s.getQuantity().abs())); // 扣减原数量
						Square t = new Square();

						t.setPromptDate(l.getPromptDate());
						t.setQuantity(s.getQuantity().abs());
						t.setRefLong(l.getOurRef());
						t.setPriceLong(DecimalUtil.nullToZero(l.getOurPrice()));
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(DecimalUtil.nullToZero(s.getOurPrice()));
						t.setPromptDateShort(s.getPromptDate());
						t.setTradeDateShort(s.getTradeDate());
						t.setCurrency(s.getCurrency());

						// LongId = l.Id;
						t.setShortId(s.getId());
						t.setQuantityLong(s.getQuantity().abs());
						t.setQuantityShort(s.getQuantity());
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());
						t.setLotId(vmPnlParam.getLotId());

						Position LongSplitted = com.smm.ctrm.util.BeanUtils.copy(l);
						LongSplitted.setId(null);
						LongSplitted.setQuantity(s.getQuantity().abs());
						LongSplitted.setIsSplitted(true);
						LongSplitted.setOurRef(l.getOurRef() + ("(s)"));
						LongSplitted.setSourceId(l.getSourceId() != null ? l.getSourceId() : l.getId());
						LongSplitted.setIsSquared(true);

						t.setSplitSquarePosition(LongSplitted);

						squares.add(t);
						lstS.remove(s);
					} else if (qtyOfLong.compareTo(qtyOfShort.abs()) < 0) {
						s.setQuantity(s.getQuantity().add(l.getQuantity()));
						Square t = new Square();
						t.setPromptDate(l.getPromptDate()); // 作为冗余
						t.setQuantity(qtyOfLong);
						t.setRefLong(l.getOurRef());
						t.setPriceLong(DecimalUtil.nullToZero(l.getOurPrice()));
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(DecimalUtil.nullToZero(s.getOurPrice()));
						t.setPromptDateShort(s.getPromptDate());
						t.setTradeDateShort(s.getTradeDate());
						t.setCurrency(s.getCurrency());

						t.setLongId(l.getId());
						t.setQuantityLong(qtyOfLong);
						t.setQuantityShort(BigDecimal.ZERO.subtract(qtyOfLong));
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());
						t.setLotId(vmPnlParam.getLotId());

						Position ShortSplitted = com.smm.ctrm.util.BeanUtils.copy(s);
						ShortSplitted.setId(null);
						ShortSplitted.setQuantity(l.getQuantity().abs().negate());
						ShortSplitted.setIsSplitted(true);
						ShortSplitted.setOurRef(s.getOurRef() + ("(s)"));
						ShortSplitted.setSourceId(s.getSourceId() != null ? s.getSourceId() : s.getId());
						ShortSplitted.setIsSquared(true);

						t.setSplitSquarePosition(ShortSplitted);

						squares.add(t);
						lstL.remove(l);
					}
				}

				/**
				 * 一边为空
				 */
				if (l == null && s != null) {
					Square t = new Square();
					t.setQuantity(s.getQuantity().abs());
					t.setRefShort(s.getOurRef());
					t.setPriceShort(DecimalUtil.nullToZero(s.getOurPrice()));
					t.setPromptDateLong(null);
					t.setPromptDateShort(s.getPromptDate());
					t.setTradeDateLong(null);
					t.setTradeDateShort(s.getTradeDate());
					t.setCurrency(s.getCurrency());
					t.setPromptDate(null);
					t.setLS(s.getLS());
					t.setShortId(s.getId());
					t.setQuantityShort(s.getQuantity());
					t.setMarketId(s.getMarketId());
					t.setCommodityId(s.getCommodityId());
					t.setLotId(vmPnlParam.getLotId());
					squares.add(t);
					lstS.remove(s);
				}
				if (l != null && s == null) {
					Square t = new Square();
					t.setQuantity(l.getQuantity());
					t.setRefLong(l.getOurRef());
					t.setPriceLong(DecimalUtil.nullToZero(l.getOurPrice()));
					t.setPromptDateLong(l.getPromptDate());
					t.setPromptDateShort(null);
					t.setTradeDateLong(l.getTradeDate());
					t.setTradeDateShort(null);
					t.setCurrency(l.getCurrency());
					t.setPromptDate(null);
					t.setLS(l.getLS());
					t.setLongId(l.getId());
					t.setQuantityLong(l.getQuantity());
					t.setMarketId(l.getMarketId());
					t.setCommodityId(l.getCommodityId());
					t.setLotId(vmPnlParam.getLotId());
					squares.add(t);
					lstL.remove(l);
				}

			} while (lstL.size() > 0 || lstS.size() > 0);

			for (Square v : squares) {
				if (v.getCurrency().equals(currency))
					v.setPnL((DecimalUtil.nullToZero(v.getPriceShort()).subtract(v.getPriceLong()))
							.multiply(v.getQuantity().abs()));
				else {
					if ("CNY".equals(v.getCurrency()))
						v.setPnL((DecimalUtil.nullToZero(v.getPriceShort())
								.subtract(DecimalUtil.nullToZero(v.getPriceLong())))
										.multiply(DecimalUtil.nullToZero(v.getQuantity()).abs())
										.divide(rate, 5, RoundingMode.HALF_EVEN));
					else
						v.setPnL((DecimalUtil.nullToZero(v.getPriceShort())
								.subtract(DecimalUtil.nullToZero(v.getPriceLong())))
										.multiply(DecimalUtil.nullToZero(v.getQuantity()).abs()).multiply(rate));
				}
			}
			vmPnL.setSquares(squares.stream().sorted((p1, p2) -> p1.getPromptDate().compareTo(p2.getPromptDate()))
					.collect(Collectors.toList()));
		}
		// 单独计算调期的损益
		if (vmPnL.getSquares() != null) {
			/*
			 * var q = (from p in vmPnL.Squares.Where(x => x.PromptDate !=
			 * DateTime.MinValue).ToList() group p by p.PromptDate into g select
			 * new { promptDate = g.Key }).OrderBy(x => x.promptDate).ToList();
			 */

			List<Square> squares = vmPnL.getSquares().stream().filter(x -> x.getPromptDate() != null)
					.collect(Collectors.toList());
			List<Date> q = new ArrayList<>();
			if (!squares.isEmpty()) {
				q = squares.stream().map(p -> p.getPromptDate()).collect(Collectors.toList());
				// 去重
				HashSet<Date> hs = new HashSet<>(q);
				q.clear();
				q.addAll(hs);
				q = q.stream().sorted((p1, p2) -> p1.compareTo(p2)).collect(Collectors.toList());
			}

			if (q.size() == 1) {
				// 没有调期
				vmPnL.setPnL4Carry(BigDecimal.ZERO);
			} else if (q.size() > 1) {
				// 取得最后的平仓日期和次最后日期
				// Date maxPromptDate = vmPnL.Squares.Max(x => x.PromptDate);

				if (vmPnL.getSquares() != null && vmPnL.getSquares().size() > 0) {
					Date maxPromptDate = vmPnL.getSquares().get(0).getPromptDate();
					for (Square s : vmPnL.getSquares()) {
						if (s.getPromptDate().after(maxPromptDate)) {
							maxPromptDate = s.getPromptDate();
						}
					}

					// var carries = vmPnL.Squares.Where(x => x.PromptDate ==
					// maxPromptDate).ToList();

					List<Square> carries = new ArrayList<>();
					for (Square x : vmPnL.getSquares()) {
						if (x.getPromptDate().compareTo(maxPromptDate) == 0) {
							carries.add(x);
						}
					}
					Square carry = carries.stream().findFirst().orElse(null);
					if (carry != null) {
						String carryCurrency = carry.getCurrency();
						BigDecimal sum = BigDecimal.ZERO;
						for (Square x : carries) {
							sum = sum.add(
									(x.getPriceShort().subtract(x.getPriceLong())).multiply(x.getQuantity().abs()));
						}
						vmPnL.setPnL4Carry(sum);

						if (!carryCurrency.equals(currency)) {
							if ("CNY".equals(carryCurrency))
								vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().divide(rate, 5, RoundingMode.HALF_EVEN));
							else
								vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().multiply(rate));
						}
					}
				}
			}
		}
		ActionResult<LotPnL> result = new ActionResult<>();
		result.setSuccess(true);
		result.setMessage("试算成功。");
		result.setData(vmPnL);
		return result;
	}

	/**
	 * 批次盈亏结算 - 正式
	 * 
	 * @param param4LotPnL
	 * @return
	 */
	@Override
	public ActionResult<LotPnL> NewLotSettleOfficial(Param4LotPnL param4LotPnL) {
		// 检查结算条件
		Lot lot = lotRepo.getOneById(param4LotPnL.getLotId(), Lot.class);
		if (!lot.getIsDelivered()) {
			return new ActionResult<>(false, "销售批次未发货完毕，不符合正式结算的要求");
		}

		if (!lot.getIsInvoiced()) {
			return new ActionResult<>(false, "销售批次未开票完毕，不符合正式结算的要求");
		}

		if (!lot.getIsPriced()) {
			return new ActionResult<>(false, "销售批次未点价完毕，不符合正式结算的要求");
		}

		LotPnL vmPnL = new LotPnL();
		vmPnL.setLotId(lot.getId());
		vmPnL.setContractFullNo(lot.getFullNo());
		vmPnL.setCustomerName(lot.getCustomer().getName());
		vmPnL.setPremium(DecimalUtil.nullToZero(lot.getPremium()));
		vmPnL.setStorageOuts(storageRepository.GetQueryable(Storage.class)
				.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId()))).toList());
		for (Storage so : vmPnL.getStorageOuts()) {
			String lot_id = so.getLotId();
			Lot so_lot = lotRepo.getOneById(lot_id, Lot.class);
			if (!so_lot.getIsDelivered()) {
				return new ActionResult<>(false, "采购批次未发货完毕，不符合正式结算的要求");
			}
			if (!so_lot.getIsInvoiced()) {
				return new ActionResult<>(false, "采购批次未开票完毕，不符合正式结算的要求");
			}

			if (!so_lot.getIsPriced()) {
				return new ActionResult<>(false, "采购批次未点价完毕，不符合正式结算的要求");
			}

		}

		ActionResult<LotPnL> actionResult = NewLotSettleTrial(param4LotPnL);
		if (!actionResult.isSuccess() || actionResult.getData() == null)
			return new ActionResult<>(false, "试算失败，请检查。");
		LotPnL obj = actionResult.getData();
		List<Square> squares = obj.getSquares();

		if (squares != null) {
			Map<String, Square> mySquaresMap = new HashMap<>();
			for (Square square : squares) {
				if (square.getPromptDate() == null) {
					return new ActionResult<>(false, "头寸到期日错误，不符合正式结算的要求。");
				}
				String key = square.getMarketId() + "," + square.getCommodityId() + "," + square.getPromptDate();
				if (mySquaresMap.keySet().contains(key)) {
					Square valueSquare = mySquaresMap.get(key);
					valueSquare.setQuantity(
							valueSquare.getQuantity().add(square.getQuantityLong().add(square.getQuantityShort())));
				} else {
					Square valueSquare = new Square();
					valueSquare.setMarketId(square.getMarketId());
					valueSquare.setCommodityId(square.getCommodityId());
					valueSquare.setPromptDate(square.getPromptDate());
					valueSquare.setQuantity(square.getQuantity());
					mySquaresMap.put(key, valueSquare);
				}

			}
			for (Square s : mySquaresMap.values()) {
				if (s.getQuantity().compareTo(BigDecimal.ZERO) != 0) {
					return new ActionResult<>(false, "头寸没有全部对齐，不符合正式结算的要求。");
				}
			}
		}
		obj.setSquares(null);// 清空关联的明细，否则保存会失败
		String pnLId = lotPnLSetRepository.SaveOrUpdateRetrunId(obj);
		// 保存结算明细
		if (squares != null) {
			for (Square PnL4Position : squares) {
				Position split = PnL4Position.getSplitSquarePosition(); // 临时存放的拆分头寸，必须自Square之前保存
				if (split != null) {
					split.setIsSquared(true);
					split.setLotPnLId(pnLId);
					String splitId = positionRepository.SaveOrUpdateRetrunId(split);
					if (split.getLS().equals(LS.LONG))
						PnL4Position.setLongId(splitId);
					else
						PnL4Position.setShortId(splitId);

					PnL4Position.setSplitSquarePosition(null);
				}
				PnL4Position.setLotPnLId(pnLId);
				PnL4Position.setSquareDate(new Date());
				squareRepository.SaveOrUpdate(PnL4Position);
			}
		}
		if (obj.getPositions() != null) {
			for (Position position : obj.getPositions()) {
				Position p = positionRepository.getOneById(position.getId(), Position.class);
				p.setQuantity(position.getQuantity());
				p.setLotPnLId(pnLId);
				p.setIsSquared(true);
				positionRepository.SaveOrUpdate(p);
			}
		}
		List<String> storageIds = new ArrayList<>();

		if (obj.getStorageIns() != null) {
			for (Storage ste : obj.getStorageIns()) {
				storageIds.add(ste.getId());
			}
		}
		if (obj.getStorageOuts() != null) {
			for (Storage ste : obj.getStorageOuts()) {
				storageIds.add(ste.getId());
			}
		}
		if (storageIds.size() > 0) {
			storageRepository.getCurrentSession()
					.createQuery("update Storage t set t.IsSettled = :IsSettled where Id in :idList")
					.setParameter("IsSettled", true).setParameter("idList", storageIds).executeUpdate();
		}
		lot = lotRepo.getOneById(obj.getLotId(), Lot.class);
		lot.setIsSettled(true);
		lotRepo.SaveOrUpdate(lot);
		return new ActionResult<LotPnL>(true, "结算成功。");
	}

	/**
	 * 批量保值-头寸分配
	 */
	@Override
	@Transactional
	public ActionResult<String> AllocatePositionMulti(CpPosition4AllocateToMultiLot allocateToMultiLot) {

		if (allocateToMultiLot == null || allocateToMultiLot.getLots() == null
				|| allocateToMultiLot.getLots().size() == 0 || allocateToMultiLot.getPositions4Allocate() == null
				|| allocateToMultiLot.getPositions4Allocate().size() == 0) {

			return new ActionResult<>(false, "参数有误.");
		}

		/**
		 * 先匹配不用拆分的头寸
		 */
		List<MultiLot4Postion> mp = allocateToMultiLot.getLots();
		List<Position> ps = allocateToMultiLot.getPositions4Allocate();
		List<Position> lp = new ArrayList<>();
		for (int i = mp.size() - 1; i >= 0; i--) {
			lp.clear();
			for (int p = ps.size() - 1; p >= 0; p--) {
				if (mp.get(i).getQuantityHedge().compareTo(ps.get(p).getQuantity()) == 0) {
					CpPosition4AllocateToLot cat = new CpPosition4AllocateToLot();
					cat.setLotId(mp.get(i).getLotId());
					lp.add(ps.get(p));
					cat.setPositions4Allocate(lp);
					this.AllocatePosition(cat);
					ps.remove(p);// 删除
					mp.remove(i);// 删除
					break;
				}
			}
		}
		/**
		 * 需要拆分的
		 */
		for (int i = mp.size() - 1; i >= 0; i--) {
			lp.clear();
			MultiLot4Postion lot = mp.get(i);
			BigDecimal quantity = BigDecimal.ZERO;
			for (int p = ps.size() - 1; p >= 0; p--) {
				lp.add(ps.get(p));
				quantity = quantity.add(ps.get(p).getQuantity());
				if (quantity.compareTo(mp.get(i).getQuantityHedge()) == 0) {
					CpPosition4AllocateToLot cat = new CpPosition4AllocateToLot();
					cat.setLotId(lot.getLotId());
					cat.setPositions4Allocate(lp);
					this.AllocatePosition(cat);
					ps.removeAll(lp);// 删除
					mp.remove(i);
					break;
				} else if (quantity.abs().compareTo(mp.get(i).getQuantityHedge().abs()) > 0) {
					// 拆多少
					BigDecimal splitQuantity = quantity.subtract(mp.get(i).getQuantityHedge());

					for (int k = 0; k < lp.size(); k++) {
						if (lp.get(k).getQuantity().abs().compareTo(splitQuantity.abs()) > 0) {// 取一个大的来拆
							CpSplitPosition cpSplitPosition = new CpSplitPosition();
							cpSplitPosition.setQuantitySplitted(splitQuantity);
							cpSplitPosition.setOriginalPosition(lp.get(k));
							String pId = this.SplitPosition(cpSplitPosition);
							Position sourdPosition = this.positionRepository.getOneById(lp.get(k).getId(),
									Position.class);
							Position addPosition = this.positionRepository.getOneById(pId, Position.class);
							ps.add(addPosition);
							lp.set(k, sourdPosition);
							break;
						}
					}

					CpPosition4AllocateToLot cat = new CpPosition4AllocateToLot();
					cat.setLotId(lot.getLotId());
					cat.setPositions4Allocate(lp);
					this.AllocatePosition(cat);
					ps.removeAll(lp);// 删除
					mp.remove(i);
					break;
				}
			}
		}

		return new ActionResult<>(true, "保值成功.");
	}

	public String SplitPosition(CpSplitPosition cpSplitPosition) {

		BigDecimal qtySplit = cpSplitPosition.getQuantitySplitted();
		Position position = cpSplitPosition.getOriginalPosition();
		if (position == null || position.getIsSquared() || position.getIsAccounted()) {
			return "不符合拆分的条件：记录为空、或已结算、或已会计。";
		}
		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(BigDecimal.ZERO) < 0)
				&& qtySplit.compareTo(BigDecimal.ZERO) > 0) {
			return "拆分的数量与原数量的符号应保持相同。";
		}
		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(position.getQuantity()) >= 0)
				|| (position.getQuantity().compareTo(BigDecimal.ZERO) < 0
						&& qtySplit.compareTo(position.getQuantity()) <= 0)) {
			return "拆分的数量必须小于原数量。";
		}
		position.setQuantity(position.getQuantity().subtract(qtySplit));
		position.setQuantityUnSquared(position.getQuantityUnSquared().subtract(qtySplit));
		this.positionRepository.SaveOrUpdate(position);
		// 构建拆分后的记录，并保存
		// 即使是已经用于保值的头寸，也是允许拆分的
		String carryCounterPart = String.valueOf(new Date().getTime());
		Position theSplit = new Position();
		BeanUtils.copyProperties(position, theSplit);
		theSplit.setId(null);
		theSplit.setQuantity(qtySplit);
		theSplit.setQuantityUnSquared(qtySplit);
		theSplit.setIsSplitted(Boolean.TRUE);
		int iNo = commonService.GetSequenceIndex(NumTypes.Split_Storage + position.getOurRef()).intValue();
		theSplit.setOurRef(position.getOurRef() + ("(s" + iNo + ")"));
		theSplit.setSourceId(position.getSourceId() == null ? position.getId() : position.getSourceId());
		if (position.getIsCarry()) {// 调期头寸拆分
			theSplit.setCarryCounterpart(carryCounterPart);
			theSplit.setCarryRef("");
			theSplit.setSourceId(position.getId());
		}
		String splitId = this.positionRepository.SaveOrUpdateRetrunId(theSplit);
		// 如果拆分的是非虚拟的卖出头寸需要同步拆分头寸明细按Broker（头寸明细按Broker必须未结算对齐）
		if (!position.getIsVirtual() && position.getLS().equals(LS.SHORT)) {
			Position split = this.positionRepository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("Id", splitId)))
					.firstOrDefault();
			Position4Broker brokerPosition = position4BrokerRepository.GetQueryable(Position4Broker.class)
					.where(DetachedCriteria.forClass(Position4Broker.class)
							.add(Restrictions.eqOrIsNull("OriginalId", position.getId())))
					.firstOrDefault();
			if (brokerPosition != null) {
				if (!brokerPosition.getIsSquared()) {
					brokerPosition.setQuantity(position.getQuantity());
					brokerPosition.setQuantityUnSquared(position.getQuantityUnSquared());
					position4BrokerRepository.SaveOrUpdate(brokerPosition);
					this.positionService.SavePosition2Broker(split);
				}
			} else {
				this.positionService.SavePosition2Broker(position);
				this.positionService.SavePosition2Broker(split);
			}
		}
		return splitId;
	}
}
