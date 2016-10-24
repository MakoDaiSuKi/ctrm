package com.smm.ctrm.bo.impl.Futures;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.PricingService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.Maintain.DSME;
import com.smm.ctrm.domain.Maintain.LMB;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpSplitPricing;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PriceTiming;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class PricingServiceImpl implements PricingService {

	private static final Logger logger = Logger.getLogger(PricingServiceImpl.class);

	@Autowired
	private HibernateRepository<Pricing> pricingRepository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;

	@Autowired
	private HibernateRepository<PricingRecord> pricingRecordRepository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;

	@Autowired
	private HibernateRepository<User> userRepository;

	@Autowired
	private HibernateRepository<Calendar> calendarRepository;

	@Autowired
	private HibernateRepository<SFE> sfeRepository;

	@Autowired
	private HibernateRepository<LME> lmeRepository;

	@Autowired
	private HibernateRepository<LMB> lmbRepository;

	@Autowired
	private HibernateRepository<DSME> dsmeRepository;

	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<Legal> legalRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private CheckService checkService;

	@Override
	public void ScheduledUpdateAveragePricing() {

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("MajorType", ActionStatus.MajorType_Average));
		where.add(Restrictions.eq("PremiumType", ActionStatus.PremiumType_Average));

		List<Pricing> list = this.pricingRepository.GetQueryable(Pricing.class).where(where).toList();

		if (list != null && list.size() > 0)
			list.forEach(this::UpdateAveragePrice);

	}

	@Override
	public ActionResult<List<Pricing>> PricingByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Pricing> list = this.pricingRepository.GetQueryable(Pricing.class).where(where).toList();

		list = this.commonService.SimplifyDataPricingList(list);

		ActionResult<List<Pricing>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Pricing>> PricingByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<Pricing> list = this.pricingRepository.GetQueryable(Pricing.class).where(where).toList();

		list = this.commonService.SimplifyDataPricingList(list);
		if (list == null) {
			list = new ArrayList<Pricing>();
		}
		ActionResult<List<Pricing>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<PricingRecord>> PricingRecordsByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<PricingRecord> list = this.pricingRecordRepository.GetQueryable(PricingRecord.class).where(where).toList();

		list = this.commonService.SimplifyDataPricingRecordList(list);

		ActionResult<List<PricingRecord>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<PricingRecord>> PricingRecordsByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<PricingRecord> list = this.pricingRecordRepository.GetQueryable(PricingRecord.class).where(where).toList();

		list = this.commonService.SimplifyDataPricingRecordList(list);

		ActionResult<List<PricingRecord>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<Pricing> GetById(String pricingId) {

		ActionResult<Pricing> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(commonService.SimplifyData(this.pricingRepository.getOneById(pricingId, Pricing.class)));

		return result;
	}

	/**
	 * 格式化日期为yyyyMMdd
	 * 
	 * @param date
	 * @return 发生异常时，将返回传入日期
	 */
	private Date FormatDateAsYymmdd000000(Date date) {

		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sdf.parse(sdf.format(date));
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return date;
	}

	@Override
	public ActionResult<String> SavePricingScheduled(Pricing pricing) {
		// try {
		// 格式化均价点介的日期、格式化lot.PricingType
		pricing.setMajorStartDate(FormatDateAsYymmdd000000(pricing.getMajorStartDate()));
		pricing.setMajorEndDate(FormatDateAsYymmdd000000(pricing.getMajorEndDate()));
		pricing.setPremiumStartDate(FormatDateAsYymmdd000000(pricing.getPremiumStartDate()));
		pricing.setPremiumEndDate(FormatDateAsYymmdd000000(pricing.getPremiumEndDate()));
		pricing.setPricingType(String.format("%s+%s", pricing.getMajorType(), pricing.getPremiumType()));

		Contract contract = contractRepository.getOneById(pricing.getContractId(), Contract.class);

		Lot lot = lotRepository.getOneById(pricing.getLotId(), Lot.class);
		if (contract == null) {
			return new ActionResult<String>(Boolean.FALSE, MessageCtrm.ParamError + ": ContractId");
		}

		// 检查数量关系是否合法。只有在"正常点价"时，才需要执行此项检查。
		if (pricing.getPriceTiming().equals(PriceTiming.Onschedule)) {
			// 检查：累计点价数量 是否大于 批次数量
			if (lot != null) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> mapList = (List<Map<String, Object>>) pricingRepository.getHibernateTemplate()
						.findByCriteria(
								DetachedCriteria.forClass(Pricing.class)
										.add(Restrictions.eq("LotId", pricing.getLotId()))
										.add(Restrictions.ne("Id", pricing.getId())).setProjection(Projections
												.projectionList().add(Projections.sum("Quantity"), "quantity"))
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
				BigDecimal quantityPriced4Lot = BigDecimal.ZERO;
				if (mapList.size() > 0) {
					Map<String, Object> map = mapList.get(0);
					BigDecimal bg = (BigDecimal) map.get("Quantity");
					if (bg == null) {
						bg = new BigDecimal(0);
					}
					quantityPriced4Lot.add(bg).add(pricing.getQuantity());
				}

				BigDecimal more = new BigDecimal(0), less = new BigDecimal(0);
				if (lot.getMoreOrLessBasis().equals("OnQuantity")) {
					more = ((lot.getQuantityOriginal() != null) ? lot.getQuantityOriginal() : BigDecimal.ZERO)
							.add(lot.getMoreOrLess());
					less = ((lot.getQuantityOriginal() != null) ? lot.getQuantityOriginal() : BigDecimal.ZERO)
							.subtract(lot.getMoreOrLess());
				} else {
					more = ((lot.getQuantityOriginal() != null) ? lot.getQuantityOriginal() : BigDecimal.ZERO)
							.multiply((BigDecimal.ONE.add(lot.getMoreOrLess().divide(new BigDecimal(100)))));
					less = ((lot.getQuantityOriginal() != null) ? lot.getQuantityOriginal() : BigDecimal.ZERO)
							.multiply((BigDecimal.ONE.subtract(lot.getMoreOrLess().divide(new BigDecimal(100)))));
				}
				if (quantityPriced4Lot.compareTo(more) > 0) {
					return new ActionResult<>(Boolean.FALSE, "点价数量不可以大于批次数量(含溢短装）");
				}
			}
		}

		// pricingRepository.BeginTransaction();

		// 一定要先保存点价的结果，再统计
		pricing.setCustomerId(contract.getCustomerId());
		pricing.setCommodityId(contract.getCommodityId());
		pricing.setLegalId(contract.getLegalId());
		pricingRepository.SaveOrUpdate(pricing);

		// 一定要重新获取
		pricing = pricingRepository.getOneById(pricing.getId(), Pricing.class);

		if (pricing.getMajorType().equals(MajorType.Pricing)) {
			// 先删除原记录

			List<PricingRecord> records = pricingRecordRepository.GetQueryable(PricingRecord.class).where(
					DetachedCriteria.forClass(PricingRecord.class).add(Restrictions.eq("PricingId", pricing.getId())))
					.toList();

			for (PricingRecord record : records) {
				pricingRecordRepository.PhysicsDelete(record);
			}

			// 写入点价记录的明细表
			PricingRecord pricingRecord = new PricingRecord();

			pricingRecord.setCommodityId(pricing.getCommodityId());
			pricingRecord.setLegalId(pricing.getLegalId());
			pricingRecord.setPriceTiming(PriceTiming.Onschedule);
			pricingRecord.setLotId(pricing.getLotId());
			pricingRecord.setCustomerId(pricing.getCustomerId());
			pricingRecord.setMajorMarketId(pricing.getMajorMarketId());
			if (pricing.getMajorMarket() != null) {
				pricingRecord.setCurrency(pricing.getMajorMarket().getCurrency());
			}
			pricingRecord.setMajor(pricing.getMajor());
			pricingRecord.setPricingId(pricing.getId());
			pricingRecord.setMajorMarketId(pricing.getMajorMarketId());
			pricingRecord.setTradeDate(pricing.getTradeDate());
			pricingRecord.setPremium((pricing.getPremium() != null) ? pricing.getPremium() : BigDecimal.ZERO);
			pricingRecord.setQuantity(pricing.getQuantity());
			pricingRecord.setPremium(pricing.getPremium());
			pricingRecord.setPrice(pricing.getPrice());
			pricingRecord.setFee(pricing.getFee());
			pricingRecord.setPricingType(pricing.getPricingType());
			pricingRecord.setPricer("None");
			pricingRecord.setCreatedBy("Sys");
			pricingRecordRepository.SaveOrUpdate(pricingRecord);
		}

		// 更新批次的点价数量和点价标志
		if (lot != null) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> pricings = (List<Map<String, Object>>) pricingRepository.getHibernateTemplate()
					.findByCriteria(DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("LotId", lot.getId()))
							.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule))
							.setProjection(Projections.projectionList().add(Projections.sum("Quantity"), "Quantity"))
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
			BigDecimal bg = BigDecimal.ZERO;
			if (pricings.size() > 0) {
				bg.add((BigDecimal) pricings.get(0).get("Quantity"));
			}
			lot.setQuantityPriced(bg);
			lot.setIsPriced(commonService.IsPriced4Lot(lot));
			lotRepository.SaveOrUpdate(lot);
			// 批次定价方式为点价时，原来的逻辑是点价完成才更新价格，现在改为：无论点价完成与否都更新价格 2015-11-9
			commonService.UpdateLotPriceByLotId(lot);
		}

		// 更新合同的点价数量和点价标志
		contract = contractRepository.getOneById(contract.getId(), Contract.class);
		if (contract != null) {
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> pricings = (List<Map<String, Object>>) pricingRepository.getHibernateTemplate()
					.findByCriteria(DetachedCriteria.forClass(Pricing.class)
							.add(Restrictions.eq("ContractId", contract.getId()))
							.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule))
							.setProjection(Projections.projectionList().add(Projections.sum("Quantity"), "Quantity"))
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
			BigDecimal bg = BigDecimal.ZERO;
			if (pricings.size() > 0) {
				bg.add((BigDecimal) pricings.get(0).get("Quantity"));
			}
			contract.setQuantityPriced(bg);
			contract.setIsPriced(contract.getQuantity().compareTo(contract.getQuantityPriced()) == 0);
			contractRepository.SaveOrUpdate(contract);
		}

		UpdateQuantityPerDay(pricing);

		// pricingRepository.CommitTransaction();
		commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
		// } catch (RuntimeException ex) {
		// pricingRepository.RollbackTransaction();
		// return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		// }
	}

	private void UpdateQuantityPerDay(Pricing pricing) {
		if (pricing == null) {
			return;
		}

		java.util.Date startDate = new java.util.Date(0);
		java.util.Date endDate = new java.util.Date(0);

		pricing = pricingRepository.getOneById(pricing.getId(), Pricing.class);
		if (pricing.getMajorStartDate() != null && pricing.getMajorEndDate() != null) {
			startDate = pricing.getMajorStartDate();
			endDate = pricing.getMajorEndDate();
			List<Calendar> calendars = calendarRepository.GetQueryable(Calendar.class)
					.where(DetachedCriteria.forClass(Calendar.class).add(Restrictions.eq("IsTrade", Boolean.TRUE))
							.add(Restrictions.ge("TradeDate", startDate)).add(Restrictions.le("TradeDate", endDate))
							.add(Restrictions.eq("MarketId", pricing.getMajorMarketId())))
					.toList();
			if (calendars.size() == 0) {
				return;
			}
			pricing.setMajorDays(calendars.size());
			pricing.setQtyPerMainDay(
					pricing.getQuantity().divide(new BigDecimal(pricing.getMajorDays()), 5, RoundingMode.HALF_EVEN));
		}
		if (pricing.getPremiumStartDate() != null && pricing.getPremiumEndDate() != null) {
			startDate = pricing.getPremiumStartDate();
			endDate = pricing.getPremiumEndDate();
			List<Calendar> calendars = calendarRepository.GetQueryable(Calendar.class)
					.where(DetachedCriteria.forClass(Calendar.class).add(Restrictions.eq("IsTrade", Boolean.TRUE))
							.add(Restrictions.ge("TradeDate", startDate)).add(Restrictions.le("TradeDate", endDate))
							.add(Restrictions.eq("MarketId", pricing.getMajorMarketId())))
					.toList();
			if (calendars.size() == 0) {
				return;
			}
			pricing.setPremiumDays(calendars.size());
			pricing.setQtyPerPremiumDay(
					pricing.getQuantity().divide(new BigDecimal(pricing.getPremiumDays()), 5, RoundingMode.HALF_EVEN));
		}

		if ("0".equals(pricing.getEtaPrice())) {
			pricing.setMajorDays(5);
			pricing.setQtyPerMainDay(
					pricing.getQuantity().divide(new BigDecimal(pricing.getMajorDays()), 5, RoundingMode.HALF_EVEN));
		}

		pricingRepository.SaveOrUpdate(pricing);
	}

	@Override
	public ActionResult<String> SavePricingExtended(Pricing pricing) {
		try {
			Lot lot = lotRepository.getOneById(pricing.getId(), Lot.class);
			if (lot == null || !pricing.getPriceTiming().equals(PriceTiming.Extension)) {
				return new ActionResult<>(Boolean.FALSE, MessageCtrm.ParamError + ": lot / not extension");
			}

			// 表单上送上来的ContractId可能不正确，因此，在此处重新赋值
			pricing.setContractId(lot.getContractId());
			// 检查延期的数量 是否 大于未点的数量
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> pricings = (List<Map<String, Object>>) pricingRepository.getHibernateTemplate()
					.findByCriteria(DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("LotId", lot.getId()))
							.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule))
							.setProjection(Projections.projectionList().add(Projections.sum("Quantity"), "Quantity"))
							.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
			BigDecimal bg = BigDecimal.ZERO;
			if (pricings.size() > 0) {
				bg.add((BigDecimal) pricings.get(0).get("Quantity"));
			}

			if (pricing.getQuantity().compareTo(lot.getQuantity().subtract(bg)) > 0) {
				return new ActionResult<>(Boolean.FALSE, "改期数量超过了未点的数量，不允许改期操作");
			}
			pricingRepository.SaveOrUpdate(pricing);
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
		} catch (RuntimeException ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@Override
	public ActionResult<String> Delete(String pricingId) {

		Pricing pricing = pricingRepository.GetQueryable(Pricing.class)
				.where(DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("Id", pricingId))).firstOrDefault();
		if (pricing == null) {
			return new ActionResult<>(Boolean.FALSE, MessageCtrm.NoResultFound);
		}

		// 检查关联的批次是否开过发票，如果开过发票，则不允许删除
		List<String> tableList = new ArrayList<>();
		tableList.add(TableNameConst.Invoice);
		ActionResult<String> checkResult = checkService.deletable(pricing.getLotId(), "LotId", tableList);
		if (!checkResult.isSuccess()) {
			return new ActionResult<>(false, checkResult.getMessage());
		}

		// 检查是否存在拆分出来的记录
		Pricing split = pricingRepository.GetQueryable(Pricing.class)
				.where(DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("SourceId", pricing.getId())))
				.firstOrDefault();
		if (split != null) {
			return new ActionResult<>(Boolean.FALSE, "请先删除被拆分出来的记录。");
		}

		// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
		if (StringUtils.isNotBlank(pricing.getSourceId())) {

			Pricing source = pricingRepository.getOneById(pricing.getSourceId(), Pricing.class);
			if (source != null) {
				source.setQuantity(source.getQuantity().add(pricing.getQuantity()));
				pricingRepository.SaveOrUpdate(source);
			}
		}

		// 先删除PricingRecord中的明细
		String sql = String.format("Delete from [Physical].PricingRecord where PricingId = '%s'", pricingId);
		pricingRecordRepository.ExecuteNonQuery(sql);

		pricingRepository.PhysicsDelete(pricingId, Pricing.class);

		// 更新批次的点价数量和点价标志
		Lot lot = lotRepository.getOneById(pricing.getLotId(), Lot.class);
		List<Pricing> pricings = pricingRepository.GetQueryable(Pricing.class)
				.where(DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("LotId", lot.getId()))
						.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule)))
				.toList();
		if (lot != null) {
			commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());
			commonService.UpdateLotPriceByLotId(lot.getId());
		}

		// 更新合同的点价数量和点价标志
		Contract contract = contractRepository.getOneById(pricing.getContractId(), Contract.class);
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) pricingRepository.getHibernateTemplate()
				.findByCriteria(
						DetachedCriteria.forClass(Pricing.class).add(Restrictions.eq("ContractId", contract.getId()))
								.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule))
								.setProjection(
										Projections.projectionList().add(Projections.sum("Quantity"), "Quantity"))
						.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));
		BigDecimal bg = BigDecimal.ZERO;
		if (pricings.size() > 0) {
			bg = bg.add((BigDecimal) mapList.get(0).get("Quantity"));
		}
		if (contract != null) {
			contract.setQuantityPriced(bg);
			contract.setIsPriced(contract.getQuantity().compareTo(contract.getQuantityPriced()) == 0);
			contractRepository.SaveOrUpdate(contract);
		}
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<String> SplitPricing(CpSplitPricing cpSplitPricing, String userId) {
		// 数据格式检查
		User user = userRepository.getOneById(userId, User.class);
		Pricing pricing = cpSplitPricing.getOriginalPricing();

		if (pricing == null || pricing.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
			return new ActionResult<>(Boolean.FALSE, "不符合拆分的条件。");
		}

		if (pricing.getLotId() != null && !"".equals(pricing.getLotId())) {
			Lot lot = lotRepository.getOneById(pricing.getLotId(), Lot.class);
			return new ActionResult<>(Boolean.FALSE,
					String.format("该点价已经被确定给了订单号为: %s，不允许拆分。", lot != null ? lot.getFullNo() : ""));
		}

		if (cpSplitPricing.getQuantitySplitted().compareTo(pricing.getQuantity()) >= 0) {
			return new ActionResult<>(Boolean.FALSE, "拆分的数量必须小于原数量。");
		}

		pricing.setQuantity(pricing.getQuantity().subtract(cpSplitPricing.getQuantitySplitted()));
		pricingRepository.SaveOrUpdate(pricing);
		pricing = pricingRepository.getOneById(pricing.getId(), Pricing.class);
		// 构建新的拆分出来的库存记录，并保存
		Pricing theSplit = null;
		try {
			theSplit = (Pricing) BeanUtils.cloneBean(pricing);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		theSplit.setId(null);
		theSplit.setIsSplitted(true);
		theSplit.setSourceId((pricing.getSourceId() != null) ? pricing.getSourceId() : pricing.getId());
		theSplit.setQuantity(cpSplitPricing.getQuantitySplitted()); // 非常重要
		theSplit.setCreatedAt(new java.util.Date());
		theSplit.setCreatedBy(user.getName());
		pricingRepository.SaveOrUpdate(theSplit);
		return new ActionResult<>(true, "拆分成功。");
	}

	@Override
	public void UpdateAveragePriceById(String pricingId) {

		Pricing pricing = this.pricingRepository.getOneById(pricingId, Pricing.class);

		if (pricing == null)
			return;

		UpdateMajorOnAverage(pricing); // 计算主体价格部分
		UpdatePremiumOnAverage(pricing); // 计算升贴水部分
		this.commonService.UpdateLotPriceByLotId(pricing.getLot());
		commonService.UpdatePriceAndHedgeFlag4Lot(pricing.getLotId());

	}

	@Override
	public void UpdateAveragePrice(Pricing pricing) {

		if (pricing == null)
			return;
		UpdateMajorOnAverage(pricing); // 计算主体价格部分
		UpdatePremiumOnAverage(pricing); // 计算升贴水部分
	}

	@Override
	public void UpdateMajorOnAverage(Pricing pricing) {
		if (!pricing.getMajorType().equals(MajorType.Average)) {
			return;
		}

		if (pricing.getMajorStartDate() == null || pricing.getMajorEndDate() == null) {
			return;
		}

		pricing = pricingRepository.getOneById(pricing.getId(), Pricing.class);
		pricing.setMajorDays(0);
		
		DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
		where.add(Restrictions.eq("MarketId", pricing.getMajorMarketId()))
		.add(Restrictions.or(Restrictions.ge("TradeDate", pricing.getMajorStartDate())))
		.add(Restrictions.or(Restrictions.le("TradeDate", pricing.getMajorEndDate())));	

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.groupProperty("TradeDate"));
		where.setProjection(pList);

		List<Map<String, Object>> mapList = (List<Map<String, Object>>) calendarRepository.getHibernateTemplate()
				.findByCriteria(where);
		
		if(mapList!=null && mapList.size() > 0)
		{
			pricing.setMajorDays(mapList.size());
		}
		
		//calendarRepository.
		
		Date startDate = pricing.getMajorStartDate();
		Date endDate = pricing.getMajorEndDate();

		// 取得需要的市场价格，并且判断点价天数是否足够、设置批次的点价标志
		String majorMarketUpperCase = pricing.getMajorMarket().getCode().toUpperCase();
		if (majorMarketUpperCase.equals("LME")) {
			if (pricing.getIsEtaPricing()) {
				if (pricing.getEtaPrice().equals("0")) //// LME特殊的月均价：最低的连续5天均价
				{
					UpdateLmeEtaAverage(pricing, startDate, endDate);
				} else if (pricing.getEtaPrice().equals("1")) // LME常规的月均价，Basis必须是CASH
				{
					UpdateLmeNormalAverage(pricing, startDate, endDate);

				}
			} else { // LME常规的月均价，Basis必须是CASH
				UpdateLmeNormalAverage(pricing, startDate, endDate);
			}
		} else if (majorMarketUpperCase.equals("SFE")) // SFE均价合同
		{
			UpdateSfeAverage(pricing, startDate, endDate);
		} else if (majorMarketUpperCase.equals("LMB")) {
			UpdateLmbAverage(pricing, startDate, endDate);
		} else if (majorMarketUpperCase.equals("PLATTS")) {
			UpdatePlattsAverage(pricing, startDate, endDate);
		} else if (majorMarketUpperCase.equals("CME")) {
		} else // 国内市场价格
		{
			UpdateDsmeAverage(pricing, startDate, endDate);
		}

		// 计算最终价格
		
		BigDecimal pPrice = ((pricing.getMajor() != null) ? pricing.getMajor() : BigDecimal.ZERO)
				.add(((pricing.getPremium() != null) ? pricing.getPremium() : BigDecimal.ZERO));
		pricing.setPrice(commonService.FormatPrice(pPrice,pricing.getCommodity(),pricing.getCommodityId()));

		pricingRepository.SaveOrUpdate(pricing);

	}

	private void UpdateLmeNormalAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {
		// LME常规的月均价
		List<LME> lmes = lmeRepository.GetQueryable(LME.class)
				.where(DetachedCriteria.forClass(LME.class)
						.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
						.add(Restrictions.or(Restrictions.le("TradeDate", endDate))))
				.toList();
		if (lmes.size() == 0) {
			return;
		}

		// 先删除PricingRecord中的明细
		String sql = String.format("Delete from [Physical].PricingRecord where PricingId = '%s'", pricing.getId());
		pricingRecordRepository.ExecuteNonQuery(sql);

		if (pricing.getMajorBasis().toUpperCase().equals("CASH")) {

			BigDecimal bg = BigDecimal.ZERO;
			for (LME lme : lmes) {
				bg = bg.add(lme.getCashSell() == null ? BigDecimal.ZERO : lme.getCashSell());
			}
			pricing.setMajor(bg.divide(new BigDecimal(lmes.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LME price : lmes) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getCashSell() != null) ? price.getCashSell() : BigDecimal.ZERO, "Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("3M")) {
			BigDecimal bg = BigDecimal.ZERO;
			for (LME lme : lmes) {
				bg = bg.add(lme.getM3Sell() == null ? BigDecimal.ZERO : lme.getM3Sell());
			}
			pricing.setMajor(bg.divide(new BigDecimal(lmes.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LME price : lmes) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getM3Sell() != null) ? price.getM3Sell() : BigDecimal.ZERO, "Main");
			}
		}
		pricing.setIsPriced((lmes.size() == pricing.getMajorDays()) && (pricing.getMajorDays() > 0));
	}

	// 计算各个市场的均价:(1) 按ETA月均价，且属于特殊的月均价
	private void UpdateLmeEtaAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {
		try {
			List<LME> lmes = lmeRepository.GetQueryable(LME.class)
					.where(DetachedCriteria.forClass(LME.class)
							.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
							.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
							.add(Restrictions.or(Restrictions.le("TradeDate", endDate)))
							.addOrder(Order.asc("TradeDate")))
					.toList();
			if (lmes.size() == 0 || lmes.size() < 5) {
				return;
			}
			pricing.setIsPriced(true); // lmes.Count == pricing.MajorDays &&
										// pricing.MajorDays > 0;
			int i = lmes.size() - 4;
			java.math.BigDecimal[] decimals = new java.math.BigDecimal[i];

			BigDecimal ret = new BigDecimal(Integer.MAX_VALUE);
			int retInt = 0;
			BigDecimal min = new BigDecimal(Integer.MIN_VALUE);
			for (int j = 0; j < i; j++) {
				int end = j + 5 > lmes.size() ? lmes.size() : j + 5;
				List<LME> subList = lmes.subList(j, end);
				BigDecimal t = BigDecimal.ZERO;
				for (LME lme : subList) {
					t = t.add(lme.getCashSell() == null ? BigDecimal.ZERO : lme.getCashSell());
				}
				decimals[j] = t;
				if (ret.compareTo(t) > 0) {
					ret = t;
					retInt = j;
				}
				if (t.compareTo(min) < 0) {
					min = t;
				}
			}

			// pricing.setMajor(min.divide(new BigDecimal(5)));
			pricing.setMajor(commonService.FormatPrice(min.divide(new BigDecimal(5)), pricing.getCommodity(),
					pricing.getCommodityId()));

			// 先删除PricingRecord中的明细
			String sql = String.format("Delete from [Physical].PricingRecord where PricingId = '%s'", pricing.getId());
			pricingRecordRepository.ExecuteNonQuery(sql);

			int end = retInt + 5 > lmes.size() ? lmes.size() : retInt + 5;
			List<LME> subLMEList = lmes.subList(retInt, end);

			for (LME lme : subLMEList) {
				// 写入点价记录的明细表
				PricingRecord pricingRecord = new PricingRecord();
				pricingRecord.setPriceTiming(PriceTiming.Onschedule);
				pricingRecord.setLotId(pricing.getLotId());
				pricingRecord.setMajorMarketId(pricing.getMajorMarketId());
				pricingRecord.setCurrency(pricing.getMajorMarket().getCurrency());
				// pricingRecord.setMajor(lme.getCashSell());

				pricingRecord.setMajor(
						commonService.FormatPrice(lme.getCashSell(), pricing.getCommodity(), pricing.getCommodityId()));
				pricingRecord.setPricingId(pricing.getId());
				pricingRecord.setMajorMarketId(pricing.getMajorMarketId());
				pricingRecord.setTradeDate(lme.getTradeDate());
				BigDecimal prPremium = (pricing.getPremium() != null) ? pricing.getPremium() : BigDecimal.ZERO;
				pricingRecord.setPremium(
						commonService.FormatPrice(prPremium, pricing.getCommodity(), pricing.getCommodityId()));
				BigDecimal prQuantity = (pricing.getQtyPerMainDay() != null) ? pricing.getQtyPerMainDay()
						: BigDecimal.ZERO;
				pricingRecord.setQuantity(
						commonService.FormatQuantity(prQuantity, pricing.getCommodity(), pricing.getCommodityId()));
				// pricingRecord.setPremium(pricing.getPremium());
				BigDecimal prPrice = ((pricingRecord.getMajor() != null) ? pricingRecord.getMajor() : BigDecimal.ZERO)
						.add((pricingRecord.getPremium() != null) ? pricingRecord.getPremium() : BigDecimal.ZERO);
				pricingRecord
						.setPrice(commonService.FormatPrice(prPrice, pricing.getCommodity(), pricing.getCommodityId()));
				pricingRecord.setPricingType(pricing.getPricingType());
				pricingRecord.setPricer("None");
				pricingRecord.setCreatedBy("Sys");
				pricingRecordRepository.SaveOrUpdate(pricingRecord);
			}
		} catch (RuntimeException ex) {
			throw ex;
		}

	}

	/// #region 计算各个市场的均价:(3) 更新SFE的均价，不一定是1个月的均价。均价期间由用户任意指定。
	private void UpdateSfeAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {

		/*
		 * DetachedCriteria sfeDc = DetachedCriteria.forClass(SFE.class)
		 * .add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
		 * .add(Restrictions.or(Restrictions.le("TradeDate", endDate)));
		 * if(pricing.getContract() != null)
		 */

		List<SFE> sfes = sfeRepository.GetQueryable(SFE.class)
				.where(DetachedCriteria.forClass(SFE.class)
						.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
						.add(Restrictions.or(Restrictions.le("TradeDate", endDate))))
				.toList();
		if (sfes.size() == 0) {
			return;
		}
		// 先删除PricingRecord中的明细
		String sql = String.format("Delete from [Physical].PricingRecord where PricingId = '%s'", pricing.getId());
		pricingRecordRepository.ExecuteNonQuery(sql);

		if (pricing.getMajorBasis().toUpperCase().equals("SETTLE")) {

			BigDecimal sum = BigDecimal.ZERO;
			for (SFE sfe : sfes) {
				sum = sum.add(sfe.getPriceSettle() == null ? BigDecimal.ZERO : sfe.getPriceSettle());
			}
			pricing.setMajor(sum.divide(new BigDecimal(sfes.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (SFE price : sfes) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getPriceSettle() != null) ? price.getPriceSettle() : BigDecimal.ZERO, "Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("WEIGHTED")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (SFE sfe : sfes) {
				sum = sum.add(sfe.getPriceWeighted() == null ? BigDecimal.ZERO : sfe.getPriceWeighted());
			}
			pricing.setMajor(sum.divide(new BigDecimal(sfes.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (SFE price : sfes) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getPriceWeighted() != null) ? price.getPriceWeighted() : BigDecimal.ZERO, "Main");
			}
		}
		pricing.setIsPriced((sfes.size() == pricing.getMajorDays()) && (pricing.getMajorDays() > 0));
	}

	// 计算各个市场的均价:(5) 更新LMB的均价，不一定是1个月的均价。均价期间由用户任意指定。
	private void UpdateLmbAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {
		List<LMB> prices = lmbRepository.GetQueryable(LMB.class)
				.where(DetachedCriteria.forClass(LMB.class)
						.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
						.add(Restrictions.or(Restrictions.le("TradeDate", endDate)))
						.add(Restrictions.eq("MarketId", pricing.getMajorMarketId()))
						.add(Restrictions.eq("SpecId", pricing.getLot().getSpecId())))
				.toList();
		if (prices.size() == 0) {
			return;
		}

		// 一定要删除该Pricing的记录，避免重复。例如，用户修改了点价日期的区间。
		List<PricingRecord> pricingRecords = pricingRecordRepository.GetQueryable(PricingRecord.class).where(
				DetachedCriteria.forClass(PricingRecord.class).add(Restrictions.eq("PricingId", pricing.getId())))
				.toList();
		for (PricingRecord pricingRecord : pricingRecords) {
			pricingRecordRepository.PhysicsDelete(pricingRecord.getId(), PricingRecord.class);
		}
		BigDecimal discount = (pricing.getLot().getDiscount() != null) ? pricing.getLot().getDiscount()
				: BigDecimal.ZERO;
		if (discount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if (pricing.getMajorBasis().toUpperCase().equals("LOW")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceLow() == null ? BigDecimal.ZERO : lmb.getPriceLow());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceLow() != null) ? price.getPriceLow() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("HIGH")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceHigh() == null ? BigDecimal.ZERO : lmb.getPriceHigh());
			}
			pricing.setMajor(sum.subtract(new BigDecimal(prices.size())));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceHigh() != null) ? price.getPriceHigh() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("AVERAGE")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceAverage() == null ? BigDecimal.ZERO : lmb.getPriceAverage());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceAverage() != null) ? price.getPriceAverage() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		}

		pricing.setIsPriced((prices.size() == pricing.getMajorDays()) && (pricing.getMajorDays() > 0));
	}

	// 计算各个市场的均价:(6) 更新PLATTS的均价，不一定是1个月的均价。均价期间由用户任意指定。
	private void UpdatePlattsAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {
		List<LMB> prices = lmbRepository.GetQueryable(LMB.class)
				.where(DetachedCriteria.forClass(LMB.class)
						.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
						.add(Restrictions.or(Restrictions.le("TradeDate", endDate)))
						.add(Restrictions.eq("MarketId", pricing.getMajorMarketId()))
						.add(Restrictions.eq("SpecId", pricing.getLot().getSpecId())))
				.toList();
		if (prices.size() == 0) {
			return;
		}

		// 一定要删除该Pricing的记录，避免重复。例如，用户修改了点价日期的区间。
		List<PricingRecord> pricingRecords = pricingRecordRepository.GetQueryable(PricingRecord.class).where(
				DetachedCriteria.forClass(PricingRecord.class).add(Restrictions.eq("PricingId", pricing.getId())))
				.toList();
		for (PricingRecord pricingRecord : pricingRecords) {
			pricingRecordRepository.PhysicsDelete(pricingRecord.getId(), PricingRecord.class);
		}
		BigDecimal discount = (pricing.getLot().getDiscount() != null) ? pricing.getLot().getDiscount()
				: BigDecimal.ZERO;
		if (discount.compareTo(BigDecimal.ZERO) == 0) {
			return;
		}
		if (pricing.getMajorBasis().toUpperCase().equals("LOW")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceLow() == null ? BigDecimal.ZERO : lmb.getPriceLow());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceLow() != null) ? price.getPriceLow() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("HIGH")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceHigh() == null ? BigDecimal.ZERO : lmb.getPriceHigh());
			}
			pricing.setMajor(sum.subtract(new BigDecimal(prices.size())));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceHigh() != null) ? price.getPriceHigh() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("AVERAGE")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (LMB lmb : prices) {
				sum = sum.add(lmb.getPriceAverage() == null ? BigDecimal.ZERO : lmb.getPriceAverage());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (LMB price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						((price.getPriceAverage() != null) ? price.getPriceAverage() : BigDecimal.ZERO)
								.multiply(discount.divide(new BigDecimal(100))).setScale(2, RoundingMode.HALF_EVEN),
						"Main");
			}
		}

		pricing.setIsPriced((prices.size() == pricing.getMajorDays()) && (pricing.getMajorDays() > 0));
	}

	// 计算各个市场的均价:(4) 更新DSME的均价，不一定是1个月的均价。均价期间由用户任意指定。
	private void UpdateDsmeAverage(Pricing pricing, java.util.Date startDate, java.util.Date endDate) {

		List<DSME> prices = dsmeRepository.GetQueryable(DSME.class)
				.where(DetachedCriteria.forClass(DSME.class)
						.add(Restrictions.eq("CommodityId",
								pricing.getCommodityId() != null ? pricing.getCommodityId() : null))
				.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
				.add(Restrictions.or(Restrictions.le("TradeDate", endDate)))
				.add(Restrictions.eq("MarketId", pricing.getMajorMarketId()))).toList();
		if (prices.size() == 0) {
			return;
		}

		// 一定要删除该Pricing的记录，避免重复。例如，用户修改了点价日期的区间。
		List<PricingRecord> pricingRecords = pricingRecordRepository.GetQueryable(PricingRecord.class).where(
				DetachedCriteria.forClass(PricingRecord.class).add(Restrictions.eq("PricingId", pricing.getId())))
				.toList();
		for (PricingRecord pricingRecord : pricingRecords) {
			pricingRecordRepository.PhysicsDelete(pricingRecord.getId(), PricingRecord.class);
		}

		if (pricing.getMajorBasis().toUpperCase().equals("LOW")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPriceLow() == null ? BigDecimal.ZERO : dsme.getPriceLow());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (DSME price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getPriceLow() != null) ? price.getPriceLow() : BigDecimal.ZERO, "Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("HIGH")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPriceHigh() == null ? BigDecimal.ZERO : dsme.getPriceHigh());
			}
			pricing.setMajor(sum.subtract(new BigDecimal(prices.size())));
			for (DSME price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getPriceHigh() != null) ? price.getPriceHigh() : BigDecimal.ZERO, "Main");
			}
		} else if (pricing.getMajorBasis().toUpperCase().equals("AVERAGE")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPriceAverage() == null ? BigDecimal.ZERO : dsme.getPriceAverage());
			}
			pricing.setMajor(sum.divide(new BigDecimal(prices.size()), 5, BigDecimal.ROUND_HALF_UP));
			for (DSME price : prices) {
				CreatePricingRecord(pricing, price.getTradeDate(),
						(price.getPriceAverage() != null) ? price.getPriceAverage() : BigDecimal.ZERO, "Main");
			}
		}

		pricing.setIsPriced((prices.size() == pricing.getMajorDays()) && (pricing.getMajorDays() > 0));
	}

	// 生成均价点价的结果明细，写入表PricingRecord
	private void CreatePricingRecord(Pricing pricing, java.util.Date pricingDate, java.math.BigDecimal price,
			String mainOrPremium) {
		PricingRecord pricingRecord = new PricingRecord();
		pricingRecord.setCommodityId(pricing.getCommodityId());
		pricingRecord.setPriceTiming(PriceTiming.Onschedule);
		pricingRecord.setCurrency(pricing.getMajorMarket().getCurrency());
		pricingRecord.setPricingId(pricing.getId());
		pricingRecord.setQuantity(
				commonService.FormatPrice(pricing.getQuantity(), pricing.getCommodity(), pricing.getCommodityId()));
		pricingRecord.setContractId(pricing.getContractId());
		pricingRecord.setLotId(pricing.getLotId());
		pricingRecord.setMajor(commonService.FormatPrice(price, pricing.getCommodity(), pricing.getCommodityId()));
		pricingRecord.setMajorMarketId(pricing.getMajorMarketId());
		pricingRecord.setTradeDate(pricingDate);
		BigDecimal prPremium = (pricing.getPremium() != null) ? pricing.getPremium() : BigDecimal.ZERO;
		pricingRecord
				.setPremium(commonService.FormatPrice(prPremium, pricing.getCommodity(), pricing.getCommodityId()));
		if (mainOrPremium.equals("Main")) {
			// pricingRecord
			// .setQuantity((pricing.getQtyPerMainDay() != null) ?
			// pricing.getQtyPerMainDay() : BigDecimal.ZERO);

			BigDecimal prQuantity = (pricing.getQtyPerMainDay() != null) ? pricing.getQtyPerMainDay() : BigDecimal.ZERO;
			pricingRecord.setQuantity(
					commonService.FormatQuantity(prQuantity, pricing.getCommodity(), pricing.getCommodityId()));

			pricingRecord.setMajor(price);
		} else if (mainOrPremium.equals("Premium")) {
			if (pricing.getMajorType().equals(MajorType.Average)) {
				// pricingRecord.setQuantity(
				// (pricing.getQtyPerPremiumDay() != null) ?
				// pricing.getQtyPerPremiumDay() : BigDecimal.ZERO);
				BigDecimal prQuantity = (pricing.getQtyPerPremiumDay() != null) ? pricing.getQtyPerPremiumDay()
						: BigDecimal.ZERO;
				pricingRecord.setQuantity(
						commonService.FormatQuantity(prQuantity, pricing.getCommodity(), pricing.getCommodityId()));

				pricingRecord.setPremium(price);
			} else if (pricing.getMajorType().equals(MajorType.Fix)) {
				// pricingRecord.setQuantity(
				// (pricing.getQtyPerPremiumDay() != null) ?
				// pricing.getQtyPerPremiumDay() : BigDecimal.ZERO);

				BigDecimal prQuantity = (pricing.getQtyPerPremiumDay() != null) ? pricing.getQtyPerPremiumDay()
						: BigDecimal.ZERO;
				pricingRecord.setQuantity(
						commonService.FormatQuantity(prQuantity, pricing.getCommodity(), pricing.getCommodityId()));

				pricingRecord.setMajor((pricing.getMajor() != null) ? pricing.getMajor() : BigDecimal.ZERO);
				pricingRecord.setPremium(pricing.getPremium());
			}
		}
		BigDecimal prPrice = ((pricingRecord.getMajor() != null) ? pricingRecord.getMajor() : BigDecimal.ZERO)
				.add((pricingRecord.getPremium() != null) ? pricingRecord.getPremium() : BigDecimal.ZERO);
		pricingRecord.setPremium(commonService.FormatPrice(prPrice, pricing.getCommodity(), pricing.getCommodityId()));

		pricingRecord.setPrice(prPrice);
		pricingRecord.setPricingType(pricing.getPricingType());
		pricingRecord.setPricer("None");
		pricingRecord.setCreatedBy("Sys");
		pricingRecordRepository.SaveOrUpdate(pricingRecord);
	}

	@Override
	public void UpdatePremiumOnAverage(Pricing pricing) {
		if (pricing.getPremiumStartDate() == null || pricing.getPremiumEndDate() == null) {
			return;
		}
		pricing = pricingRepository.getOneById(pricing.getId(), Pricing.class);
		String majorMarketCode = pricing.getMajorMarket().getCode().toUpperCase();
		String premiumMarketCode = pricing.getPremiumMarket().getCode().toUpperCase();

		if (majorMarketCode.equals("LME") || majorMarketCode.equals("SFE") || premiumMarketCode.equals("LME")
				|| premiumMarketCode.equals("SFE")) {
			return;
		}
		Date startDate = pricing.getPremiumStartDate();
		Date endDate = pricing.getPremiumEndDate();
		// 先删除已经存在的点价记录子项
		// 取得需要的市场价格，并且判断点价天数是否足够、设置批次的点价标志
		List<DSME> prices = dsmeRepository.GetQueryable(DSME.class)
				.where(DetachedCriteria.forClass(DSME.class)
						.add(Restrictions.eq("CommodityId", pricing.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", startDate)))
						.add(Restrictions.or(Restrictions.le("TradeDate", endDate)))
						.add(Restrictions.eq("MarketId", pricing.getPremiumMarketId())))
				.toList();

		if (prices.size() == 0) {
			return;
		}

		if (pricing.getPremiumBasis().toUpperCase().equals("LOW")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPremiumLow() == null ? BigDecimal.ZERO : dsme.getPremiumLow());
			}
			pricing.setPremium(sum.divide(new BigDecimal(prices.size())));
			if (pricing.getMajorType().equals(MajorType.Average) || pricing.getMajorType().equals(MajorType.Fix)) {
				for (DSME price : prices) {
					CreatePricingRecord(pricing, price.getTradeDate(),
							(price.getPremiumLow() != null) ? price.getPremiumLow() : BigDecimal.ZERO, "Main");
				}
			}

		} else if (pricing.getPremiumBasis().toUpperCase().equals("HIGH")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPremiumHigh() == null ? BigDecimal.ZERO : dsme.getPremiumHigh());
			}
			pricing.setPremium(sum.subtract(new BigDecimal(prices.size())));
			if (pricing.getMajorType().equals(MajorType.Average) || pricing.getMajorType().equals(MajorType.Fix)) {
				for (DSME price : prices) {
					CreatePricingRecord(pricing, price.getTradeDate(),
							(price.getPremiumHigh() != null) ? price.getPremiumHigh() : BigDecimal.ZERO, "Main");
				}
			}
		} else if (pricing.getPremiumBasis().toUpperCase().equals("AVERAGE")) {
			BigDecimal sum = BigDecimal.ZERO;
			for (DSME dsme : prices) {
				sum = sum.add(dsme.getPremiumAverage() == null ? BigDecimal.ZERO : dsme.getPremiumAverage());
			}
			pricing.setPremium(sum.divide(new BigDecimal(prices.size())));
			if (pricing.getMajorType().equals(MajorType.Average) || pricing.getMajorType().equals(MajorType.Fix)) {
				for (DSME price : prices) {
					CreatePricingRecord(pricing, price.getTradeDate(),
							(price.getPremiumAverage() != null) ? price.getPremiumAverage() : BigDecimal.ZERO, "Main");
				}
			}
		}

		pricing.setIsPriced(prices.size() == pricing.getPremiumDays() && pricing.getPremiumDays() > 0);

		// 计算最终价格
		BigDecimal pPrice = ((pricing.getMajor() != null) ? pricing.getMajor() : BigDecimal.ZERO)
				.add((pricing.getPremium() != null) ? pricing.getPremium() : BigDecimal.ZERO);
		pricing.setPrice(commonService.FormatPrice(pPrice, pricing.getCommodity(), pricing.getCommodityId()));

		pricingRepository.SaveOrUpdate(pricing);
	}

	@Override
	public List<Pricing> Pricings() {

		return this.pricingRepository.GetList(Pricing.class);
	}

	@Override
	public Criteria GetCriteria() {
		return this.pricingRepository.CreateCriteria(Pricing.class);
	}

	@Override
	public List<Pricing> Pricings(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
		return assemble(pricingRepository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData());
	}

	public List<Pricing> assemble(List<Pricing> lps) {
		if (lps == null || lps.size() == 0)
			return new ArrayList<>();

		List<String> premiumMarketIds = new ArrayList<>();
		List<String> contractIds = new ArrayList<>();
		List<String> legalIds = new ArrayList<>();
		List<String> commodityIds = new ArrayList<>();

		lps.forEach(p -> {
			if (p.getLegalId() != null) {
				legalIds.add(p.getLegalId());
			}
			if (p.getContractId() != null) {
				contractIds.add(p.getContractId());
			}
			if (p.getPremiumMarketId() != null) {
				premiumMarketIds.add(p.getPremiumMarketId());
			}
			if (p.getCommodityId() != null) {
				commodityIds.add(p.getCommodityId());
			}
		});
		List<Commodity> commodity = new ArrayList<>();
		if (commodityIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Commodity.class);
			dc.add(Restrictions.in("Id", commodityIds));
			commodity = this.commodityRepository.GetQueryable(Commodity.class).where(dc).toList();
		}

		List<Market> markets = new ArrayList<>();
		if (premiumMarketIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Market.class);
			dc.add(Restrictions.in("Id", premiumMarketIds));
			markets = this.marketRepository.GetQueryable(Market.class).where(dc).toList();
		}
		List<Contract> contracts = new ArrayList<>();
		if (contractIds.size() > 0) {
			DetachedCriteria dc2 = DetachedCriteria.forClass(Contract.class);
			dc2.add(Restrictions.in("Id", contractIds));
			contracts = this.contractRepository.GetQueryable(Contract.class).where(dc2).toList();
		}
		List<Legal> legals = new ArrayList<>();
		if (legalIds.size() > 0) {
			DetachedCriteria dc3 = DetachedCriteria.forClass(Legal.class);
			dc3.add(Restrictions.in("Id", legalIds));
			legals = this.legalRepository.GetQueryable(Legal.class).where(dc3).toList();
		}
		for (Pricing pricing : lps) {
			markets.forEach(m -> {
				if (m.getId().equals(pricing.getPremiumMarketId())) {
					pricing.setPremiumMarketName(m.getName());
				}
			});
			contracts.forEach(c -> {
				if (c.getId().equals(pricing.getContractId())) {
					pricing.setHeadNo(c.getHeadNo());
					pricing.setQuantityOfContract(c.getQuantity());
					pricing.setQuantityPriciedOfContract(c.getQuantityPriced());
				}
			});
			legals.forEach(l -> {
				if (l.getId().equals(pricing.getLegalId())) {
					pricing.setLegalName(l.getName());
				}
			});
			commodity.forEach(c -> {
				if (c.getId().equals(pricing.getCommodityId())) {
					pricing.setCommodity(c);
				}
			});
		}
		return lps;
	}

}
