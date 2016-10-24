package com.smm.ctrm.bo.impl.Futures;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.PositionService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Mainmeta;
import com.smm.ctrm.domain.MainmetaResult;
import com.smm.ctrm.domain.NumTypes;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpSplitPosition;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.PositionLot;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.PositionParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.HttpClientUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.PropertiesUtil;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.ForwardType;
import com.smm.ctrm.util.Result.LS;
import com.smm.ctrm.util.Result.MT4Storage;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class PositionServiceImpl implements PositionService {

	private static final Logger logger = Logger.getLogger(PositionServiceImpl.class);

	@Autowired
	private HibernateRepository<Position> repository;

	@Autowired
	private HibernateRepository<Calendar> calendarRepository;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepository;

	@Autowired
	private HibernateRepository<Storage> storageRepository;

	@Autowired
	private HibernateRepository<Position4Broker> position4BrokerRepository;

	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<Square> squareRepository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;

	@Autowired
	private HibernateRepository<SFE> sfeRepository;

	@Autowired
	private HibernateRepository<LME> lmeRepository;

	@Autowired
	private HibernateRepository<Broker> brokerRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityRepository;

	@Autowired
	private HibernateRepository<Reuter> reuterRepository;

	@Autowired
	private HibernateRepository<Customer> customerRepository;

	@Autowired
	private HibernateRepository<Legal> legalRepository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;

	@Autowired
	private HibernateRepository<Dictionary> dictionaryRepository;
	
	@Autowired
	private HibernateRepository<PositionLot> positionLotRepo;

	@Autowired
	private CommonService commonService;

	private final static String CODE = "0";

	@Override
	public List<Position> getPositionListByPosition4BrokerId(List<String> position4BrokerIdList) {
		if(position4BrokerIdList==null || position4BrokerIdList.size() <1)
			return new ArrayList<>();
		
		return repository.GetQueryable(Position.class).where(DetachedCriteria.forClass(Position.class)
				.add(Restrictions.in("Position4BrokerId", position4BrokerIdList))).toList();
	}

	@Override
	public void ScheduledUpdateAveragePosition() {

		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.eq("ForwardType", ActionStatus.Average));
		where.add(Restrictions.eq("IsPriced", false));

		List<Position> list = this.repository.GetQueryable(Position.class).where(where).toList();

		if (list != null && list.size() > 0) {

			list.forEach(this::UpdateQuantityPerDay);
		}

	}

	private void UpdateQuantityPerDay(Position position) {

		if (position == null || !position.getForwardType().equals(ActionStatus.Average))
			return;

		position = this.repository.getOneById(position.getId(), Position.class);

		if (position.getAverageStartDate() != null && position.getAverageStartDate() != new Date()
				&& position.getAverageEndDate() != null && position.getAverageEndDate() != new Date()) {
			Date startDate = position.getAverageStartDate();
			Date endDate = position.getAverageEndDate();

			DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
			where.add(Restrictions.eq("IsTrade", true));
			where.add(Restrictions.ge("TradeDate", startDate));
			where.add(Restrictions.le("TradeDate", endDate));
			where.add(Restrictions.eq("MarketId", position.getMarketId()));

			List<Calendar> calendars = this.calendarRepository.GetQueryable(Calendar.class).where(where).toList();

			if (calendars.size() == 0)
				return;

			position.setPricingDays(calendars.size());
			position.setQtyPerPricingDay(position.getQuantity().divide(new BigDecimal(position.getPricingDays())));
		}

		this.repository.SaveOrUpdate(position);

	}

	@Override
	public void UpdateAveragePositionById(String positionId) {

		try {
			if (positionId == null)
				return;
			Position position = this.repository.getOneById(positionId, Position.class);

			if (position == null)
				return;
			UpdateQuantityPerDay(position);
			UpdateAveragePosition(position);
		} catch (Exception e) {

			logger.error(e.getMessage(), e);
		}
	}

	@Override
	public void UpdateAveragePosition(Position position) {

		if (position == null)
			return;

		if (position.getForwardType().equals(ActionStatus.Average) && position.getMarket().getCode().equals("SFE")) {
			UpdateSfeAveragePosition(position);
		} else if (position.getForwardType().equals(ActionStatus.Average)
				&& position.getMarket().getCode().equals("LME")) {
			UpdateLmeAveragePosition(position);
		}

	}

	@Override
	public void UpdateSfeAveragePositionById(String positionId) {

		if (positionId == null)
			return;
		Position position = this.repository.getOneById(positionId, Position.class);
		if (position == null)
			return;
		UpdateSfeAveragePosition(position);
	}

	@Override
	public void UpdateSfeAveragePosition(Position position) {

		position = repository.getOneById(position.getId(), Position.class);

		BigDecimal ourPrice = BigDecimal.ZERO;
		if (!position.getForwardType().equals(ForwardType.Average) || !position.getMarket().getCode().equals("SFE")) {
			return;
		}

		// 取交易日历，进而算出头寸的均价天数，进而算同每天的数量=头寸数量/均价天数
		List<Calendar> calendars = calendarRepository.GetQueryable(Calendar.class).where(
				DetachedCriteria.forClass(Calendar.class).add(Restrictions.eq("MarketId", position.getMarketId())))
				.toList();
		if (calendars.size() == 0) {
			return;
		}
		List<SFE> sfes = sfeRepository.GetQueryable(SFE.class)
				.where(DetachedCriteria.forClass(SFE.class)
						.add(Restrictions.eq("CommodityId", position.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", position.getAverageStartDate())))
						.add(Restrictions.or(Restrictions.le("TradeDate", position.getAverageEndDate()))))
				.toList();
		if (sfes.size() == 0) {
			return;
		}

		if (position.getPromptBasis().toUpperCase().equals("SETTLE")) {
			BigDecimal size = new BigDecimal(sfes.size());
			for (SFE sfe : sfes) {
				ourPrice.add(((sfe.getPriceSettle() != null) ? sfe.getPriceSettle() : BigDecimal.ZERO).divide(size));
			}
		} else if (position.getPromptBasis().toUpperCase().equals("WEIGHTED")) {
			BigDecimal size = new BigDecimal(sfes.size());
			for (SFE sfe : sfes) {
				ourPrice.add(
						((sfe.getPriceWeighted() != null) ? sfe.getPriceWeighted() : BigDecimal.ZERO).divide(size));
			}
		}
		position.setOurPrice(ourPrice.add(((position.getSpread() != null) ? position.getSpread() : BigDecimal.ZERO)));
		position.setIsPriced(sfes.size() == position.getPricingDays() && position.getPricingDays() > 0);
		repository.Save(position);
	}

	@Override
	public void UpdateLmeAveragePositionById(String positionId) {

		if (positionId == null)
			return;
		Position position = this.repository.getOneById(positionId, Position.class);
		if (position == null)
			return;
		UpdateLmeAveragePosition(position);

	}

	@Override
	public void UpdateLmeAveragePosition(Position position) {
		position = repository.getOneById(position.getId(), Position.class);
		BigDecimal ourPrice = BigDecimal.ZERO;
		if (!position.getForwardType().equals(ForwardType.Average) || !position.getMarket().getCode().equals("LME")) {
			return;
		}

		// 取交易日历，进而算出头寸的均价天数，进而算同每天的数量=头寸数量/均价天数
		List<Calendar> calendars = calendarRepository.GetQueryable(Calendar.class).where(
				DetachedCriteria.forClass(Calendar.class).add(Restrictions.eq("MarketId", position.getMarketId())))
				.toList();

		if (calendars.size() == 0) {
			return;
		}
		List<LME> lmes = lmeRepository.GetQueryable(LME.class)
				.where(DetachedCriteria.forClass(LME.class)
						.add(Restrictions.eq("CommodityId", position.getCommodityId()))
						.add(Restrictions.or(Restrictions.ge("TradeDate", position.getAverageStartDate())))
						.add(Restrictions.or(Restrictions.le("TradeDate", position.getAverageEndDate()))))
				.toList();
		if (lmes.size() == 0) {
			return;
		}

		if (position.getPromptBasis().toUpperCase().equals("CASH")) {
			BigDecimal size = new BigDecimal(lmes.size());
			for (LME lme : lmes) {
				ourPrice.add(((lme.getCashSell() != null) ? lme.getCashSell() : BigDecimal.ZERO).divide(size));
			}
		} else if (position.getPromptBasis().toUpperCase().equals("3M")) {
			BigDecimal size = new BigDecimal(lmes.size());
			for (LME lme : lmes) {
				ourPrice.add(((lme.getM3Sell() != null) ? lme.getM3Sell() : BigDecimal.ZERO).divide(size));
			}
		}

		position.setOurPrice(ourPrice.add(((position.getSpread() != null) ? position.getSpread() : BigDecimal.ZERO)));

		position.setIsPriced(lmes.size() == position.getPricingDays() && position.getPricingDays() > 0);

		repository.SaveOrUpdate(position);
	}

	@Override
	public ActionResult<String> Square(List<Position> positions) {

		List<Position> longs = new ArrayList<>();

		List<Position> shorts = new ArrayList<>();

		positions.forEach(position -> {

			if (position.getLS().equals(ActionStatus.LS_LONG))
				longs.add(position);

			if (position.getLS().equals(ActionStatus.LS_SHORT))
				shorts.add(position);
		});

		return this.commonService.SquareWithTransaction(longs, shorts);
	}

	@Override
	public ActionResult<String> ImportLMEsViaList(List<Position> positions, String userName, String userId) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void ImportLMEsViaStream(InputStream stream) throws Exception {
		try {
			Workbook workbook = WorkbookFactory.create(stream);
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			rows.next();
			rows.next();
			while (rows.hasNext()) {
				Row row = rows.next();
				Broker broker = brokerRepository.GetQueryable(Broker.class).where(DetachedCriteria
						.forClass(Broker.class).add(Restrictions.eq("Code", row.getCell(1).getStringCellValue())))
						.firstOrDefault();
				Commodity commodity = commodityRepository.GetQueryable(Commodity.class)
						.where(DetachedCriteria.forClass(Commodity.class)
								.add(Restrictions.eq("Code", row.getCell(2).getStringCellValue().toUpperCase())))
						.firstOrDefault();

				Date promptDate = new Date();
				if (row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING) {
					String promptDateStr = row.getCell(6).getStringCellValue().toUpperCase();
					if (promptDateStr.equals("3M")) {
						promptDate.setMonth(promptDate.getMonth() + 3);
					}
					if (promptDateStr.equals("CASH")) {
						promptDate.setDate(promptDate.getDate() + 2);
					}
				} else {
					promptDate = row.getCell(6).getDateCellValue();
				}
				if (broker == null || commodity == null) {
					continue;
				}
				Position position = new Position();
				position.setTradeDate(row.getCell(0).getDateCellValue());
				position.setHands(new BigDecimal(row.getCell(3).getNumericCellValue()));
				position.setLS(row.getCell(4).getStringCellValue().toUpperCase());
				position.setOurPrice(new BigDecimal(row.getCell(5).getNumericCellValue()));
				position.setPromptDate(promptDate);
				position.setCreatedAt(new Date());
				position.setQuantity(((position.getHands() != null) ? position.getHands() : BigDecimal.ZERO)
						.multiply(new BigDecimal(commodity.getQuantityPerLot())));
				repository.Save(position);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public ActionResult<List<Position>> PositionsByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Position> list = this.repository.GetQueryable(Position.class).where(where).toList();

		list = this.commonService.SimplifyDataPositionList(list);

		ActionResult<List<Position>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Position>> PositionsByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<Position> list = this.repository.GetQueryable(Position.class).where(where).toList();

		list = this.commonService.SimplifyDataPositionList(list);

		ActionResult<List<Position>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Position>> PositionsOpening(String invoiceId) {

		@SuppressWarnings("unchecked")
		List<Map<String, Object>> mapList = (List<Map<String, Object>>) repository.getHibernateTemplate()
				.findByCriteria(
						DetachedCriteria.forClass(Position.class).add(Restrictions.eq("IsSquared", false))
								.add(Restrictions.eq("IsAccounted", false))
								.setProjection(Projections.projectionList()
										.add(Projections.groupProperty("MarketId"), "MarketId")
										.add(Projections.groupProperty("CommodityId"), "CommodityId")
										.add(Projections.groupProperty("PromptDate"), "PromptDate")
										.add(Projections.sum("Quantity"), "Quantity"))
								.setResultTransformer(Criteria.ALIAS_TO_ENTITY_MAP));

		List<Position> list = new ArrayList<>();
		for (Map<String, Object> map : mapList) {
			Position p = new Position();
			p.setMarketId((String) map.get("MarketId"));
			p.setCommodityId((String) map.get("CommodityId"));
			p.setPromptDate((Date) map.get("PromptDate"));
			p.setQuantity((BigDecimal) map.get("Quantity"));
			list.add(p);
		}
		return new ActionResult<>(true, null, commonService.SimplifyDataPositionList(list));
	}

	@Override
	public ActionResult<List<Position>> PositionsBothWayByInvoiceId(String invoiceId) {

		List<Position> rs = new ArrayList<>();
		Invoice invoice = invoiceRepository.getOneById(invoiceId, Invoice.class);
		rs.addAll(repository.GetQueryable(Position.class)
				.where(DetachedCriteria.forClass(Position.class).add(Property.forName("LotId").eq(invoice.getLotId())))
				.toList());

		if (invoice.getStorages() != null && invoice.getStorages().size() > 0) {
			for (Storage storage : invoice.getStorages()) {
				Storage counterparty = storageRepository.getOneById(storage.getCounterpartyId(), Storage.class);
				if (counterparty != null) {
					List<Position> positionList = repository.GetQueryable(Position.class)
							.where(DetachedCriteria.forClass(Position.class)
									.add(Property.forName("LotId").eq(counterparty.getLotId()))
									.add(Property.forName("IsAccounted").eq(Boolean.FALSE)))
							.toList();

					// 避免重复合并到rs中
					HashSet<String> positionIdSet = new HashSet<>();
					for (Position p : rs) {
						positionIdSet.add(p.getId());
					}
					for (Position position : positionList) {
						if (positionIdSet.contains(position.getId())) {
							continue;
						} else {
							rs.add(position);
						}
					}

				}
			}
		}

		for (Position position : rs) {
			if (position.getMarket() != null)
				position.getMarketName().equals(position.getMarket().getName());
			if (position.getCommodityId() != null) {
				position.setCommodityCode(position.getCommodity().getCode());
				position.setCommodityName(position.getCommodity().getName());
			}
			if (position.getLot() != null)
				position.setFullNo(position.getLot().getFullNo());
		}
		List<Position> rsTemp = new ArrayList<>();
		for (Position p : rs) {
			rsTemp.add(commonService.SimplifyData(p));
		}
		return new ActionResult<>(Boolean.TRUE, "", rsTemp);
	}

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Position.class);
	}

	@Override
	public ActionResult<String> SavePosition2Broker(Position position) {

		if (position == null) {
			return new ActionResult<>(Boolean.FALSE, "当前头寸为空，无法生成broker头寸");
		}

		// if ((position.getLS().equals(LS.LONG) && position.getIsSplitted()) ||
		// position.getIsVirtual())
		// return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
		if (position.getPosition4BrokerId() != null) {
			updateQuantityOfPosition4Broker(position);
			// List<Position4Broker> exists =
			// position4BrokerRepository.GetQueryable(Position4Broker.class)
			// .where(DetachedCriteria.forClass(Position4Broker.class)
			// .add(Property.forName("OriginalId").eq(position.getId())))
			// .toList();
			//
			// if (exists != null && exists.size() > 0) {
			// for (Position4Broker broker : exists) {
			// if (broker.getIsSquared()) {
			// // broker已经对齐
			// return new ActionResult<>(Boolean.FALSE,
			// "Broker头寸明细已经按照Broker对齐,当前头寸修改不会同步到Broker头寸明细");
			// }
			// }
			// for (Position4Broker broker : exists) {
			// position4BrokerRepository.PhysicsDelete(broker);
			// }
			// }
		} else {
			Position4Broker position4Broker = new Position4Broker();
			if(position.getBeforeChanged() != null && position.getBeforeChanged().getPurpose() != null) {
				position4Broker.setPurpose(position.getBeforeChanged().getPurpose());
			}
			BeanUtils.copyProperties(position, position4Broker);
			position4Broker.setId(null);
			position4Broker.setQuantity(
					position.getLS().equals(LS.SHORT) ? position.getQuantity() : position.getQuantityOriginal()); // 得到头寸的原始数量
			position4Broker.setQuantityUnSquared(position.getLS().equals(LS.SHORT) ? position.getQuantityUnSquared()
					: position.getQuantityOriginal());
			position4Broker.setIsSquared(Boolean.FALSE);
			position4Broker.setOriginalId(position.getId());
			position4BrokerRepository.SaveOrUpdate(position4Broker);
			position.setPosition4BrokerId(position4Broker.getId());
		}
		repository.SaveOrUpdate(position);
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
	}

	void updateQuantityOfPosition4Broker(Position position) {
		BigDecimal currQuantity = BigDecimal.ZERO;
		if (StringUtils.isNotBlank(position.getPosition4BrokerId())) {
			List<Position> otherPositions = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.ne("Id", position.getId()))
							.add(Restrictions.eqOrIsNull("Position4BrokerId", position.getPosition4BrokerId())))
					.toList();
			for (Position p : otherPositions) {
				currQuantity = currQuantity.add(p.getQuantity());
			}
			currQuantity = currQuantity.add(position.getQuantity());
		} else {
			currQuantity = position.getQuantity();
		}

		Position4Broker currBroker = position4BrokerRepository.GetQueryable(Position4Broker.class)
				.where(DetachedCriteria.forClass(Position4Broker.class)
						.add(Property.forName("Id").eq(position.getPosition4BrokerId())))
				.firstOrDefault();
		if (currBroker != null) {
			if(position.getBeforeChanged() != null && position.getBeforeChanged().getPurpose() != null) {
				currBroker.setPurpose(position.getBeforeChanged().getPurpose());
			}
			currBroker.setQuantity(currQuantity);
			position4BrokerRepository.SaveOrUpdate(currBroker);
		}
	}

	@Override
	public ActionResult<Position> Save(Position position) {
		if (position.getMarketId() == null) {
			return new ActionResult<>(Boolean.FALSE, "Param is missing: MarketId",null);
		}

		if (StringUtils.isNotBlank(position.getId()) && StringUtils.isNotBlank(position.getLotId())) {
			return new ActionResult<>(Boolean.FALSE, "已关联到批次，不允许修改",null);
		}

		Market market = marketRepository.getOneById(position.getMarketId(), Market.class);
		position.setCurrency(market.getCurrency());
		try {
			List<Square> squareList = null;
			if (StringUtils.isNotBlank(position.getId())) {
				squareList = squareRepository.GetQueryable(Square.class)
						.where(DetachedCriteria.forClass(Square.class)
								.add(Restrictions.disjunction().add(Property.forName("LongId").eq(position.getId()))
										.add(Property.forName("ShortId").eq(position.getId()))))
						.toList();
				if (squareList != null && squareList.size() > 0) {
					return new ActionResult<>(Boolean.FALSE, "该头寸全部或者部分已经对齐，不允许修改。",null);
				}

				// 数量处理，判断是否数量变更
				if (position.getQuantity().compareTo(position.getQuantityBeforeChanged()) != 0) {
					BigDecimal diff = position.getQuantity().subtract(position.getQuantityBeforeChanged());
					position.setQuantityOriginal(position.getQuantityOriginal().add(diff));

					position.setQuantityUnSquared(
							position.getQuantity().abs().subtract(getSquareSumQuantity(squareList))
									.multiply(new BigDecimal(position.getQuantity().compareTo(BigDecimal.ZERO))));
					// position 为被拆分的头寸
					List<Position> splitsList = repository.GetQueryable(Position.class)
							.where(DetachedCriteria.forClass(Position.class)
									.add(Property.forName("SourceId").eq(position.getId()))
									.add(Property.forName("IsSplitted").eq(Boolean.TRUE)))
							.toList();
					for (Position split : splitsList) {
						List<Square> sumList = squareRepository.GetQueryable(Square.class)
								.where(DetachedCriteria.forClass(Square.class)
										.add(Restrictions.disjunction()
												.add(Property.forName("LongId").eq(split.getId())).add(
														Property.forName("ShortId").eq(split.getId()))))
								.toList();
						split.setQuantityUnSquared(split.getQuantity().abs().subtract(getSquareSumQuantity(sumList))
								.multiply(new BigDecimal(split.getQuantity().compareTo(BigDecimal.ZERO))));
						split.setQuantityOriginal(position.getQuantityOriginal());
						repository.SaveOrUpdate(split);
					}
					// positon 为拆分出来的头寸
					Position source = repository.GetQueryable(Position.class).where(DetachedCriteria
							.forClass(Position.class).add(Property.forName("Id").eq(position.getSourceId())))
							.firstOrDefault();
					if (source != null) {
						List<Square> sourceSquareList = squareRepository.GetQueryable(Square.class)
								.where(DetachedCriteria.forClass(Square.class)
										.add(Restrictions.disjunction()
												.add(Property.forName("LongId").eq(source.getId())).add(
														Property.forName("ShortId").eq(source.getId()))))
								.toList();
						source.setQuantityUnSquared(
								source.getQuantity().abs().subtract(getSquareSumQuantity(sourceSquareList))
										.multiply(new BigDecimal(source.getQuantity().compareTo(BigDecimal.ZERO))));
						source.setQuantityOriginal(position.getQuantityOriginal());
						repository.SaveOrUpdate(source);
					}
				}
			} else {
				position.setQuantityUnSquared(position.getQuantity());
				position.setQuantityOriginal(position.getQuantity());
			}

			if (position.getIsVirtual() && StringUtils.isBlank(position.getId())) { // 虚拟头寸交易编号(修改时不需要重复增加）
				if (position.getIsCarry()) {
					Position carry = repository.GetQueryable(Position.class).where(DetachedCriteria
							.forClass(Position.class).add(Property.forName("Id").eq(position.getCarryPositionId())))
							.firstOrDefault();
					position.setOurRef((carry != null ? carry.getOurRef() : position.getOurRef()) + "(vc"
							+ position.getLS().toLowerCase() + ")");
				} else {
					position.setOurRef(position.getOurRef() + "(v)"); // 普通虚拟头寸
				}
			} else {
				// 普通调期头寸
				if (position.getIsCarry() && StringUtils.isBlank(position.getId())) {
					Position carry = repository.GetQueryable(Position.class).where(DetachedCriteria
							.forClass(Position.class).add(Property.forName("Id").eq(position.getCarryPositionId())))
							.firstOrDefault();
					position.setOurRef((carry != null ? carry.getOurRef() : position.getOurRef()) + "(c"
							+ position.getLS().toLowerCase() + ")");
				}
			}
			// 交易编号重复性判断
			if (position.getIsSourceOfCarry()) {
				// 被调期头寸
				Position ref = repository.GetQueryable(Position.class).where(DetachedCriteria.forClass(Position.class)
						.add(Restrictions.conjunction(Property.forName("Id").neOrIsNotNull(position.getId()),
								Restrictions.disjunction(Property.forName("OurRef").eq(position.getOurRef()),
										Restrictions.conjunction(Property.forName("CarryRef").eq(position.getOurRef()),
												Property.forName("CarryPositionId").neOrIsNotNull(position.getId()))))))
						.firstOrDefault();
				if (ref != null) {
					return new ActionResult<>(Boolean.FALSE, "交易编号已经存在2");
				}
			} else {
				// (排除被调期，因为调期来源OurRef肯定已经在Carryref中存在）
				Position ref = repository.GetQueryable(Position.class)
						.where(DetachedCriteria.forClass(Position.class)
								.add(Restrictions.conjunction(Property.forName("Id").neOrIsNotNull(position.getId()),
										Restrictions.disjunction(Property.forName("OurRef").eq(position.getOurRef()),
												Property.forName("CarryRef").eq(position.getOurRef())))))
						.firstOrDefault();
				if (ref != null) {
					return new ActionResult<>(Boolean.FALSE, "交易编号已经存在1");
				}
			}
			if(StringUtils.isNotBlank(position.getId())) {
				Position positionBeforeChange = new Position();
				positionBeforeChange.setPurpose(position.getPurpose());
				position.setPurpose("S");
			}
			repository.SaveOrUpdate(position);
			ActionResult<String> brokerOk = SavePosition2Broker(position);// 同步生成Broker头寸明细
			if (!brokerOk.isSuccess()) {
				return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess + "\r\n提示：" + brokerOk.getMessage(),position);
			}
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess,position);
		} catch (Exception e) {
			logger.error(e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage(),null);
		}
	}
	
	private BigDecimal getSquareSumQuantity(List<Square> list) {
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Square square : list) {
			sumQuantity.add(square.getQuantity());
		}
		return sumQuantity;
	}

	@Override
	public ActionResult<String> SaveCarryPositions(List<Position> CarryPositions) {

		for (Position position : CarryPositions) {
			Save(position);
		}
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> Position2Lot(List<Position> positions) {

		for (Position position : positions) {
			Position2Lot(position);
		}

		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
	}

	// 头寸关联批次
	@Override
	public ActionResult<String> Position2Lot(Position position) {

		logger.info("------------------position id:" + position.getId());

		logger.info("------------------position lotId:" + position.getLotId());

		if (StringUtils.isBlank(position.getLotId())) {
			return new ActionResult<>(Boolean.FALSE, "未选择批次，不可分配");
		}

		// 重新从数据库中读取批次
		Lot lot = lotRepository.getOneById(position.getLotId(), Lot.class);

		logger.info("---------------lot :" + JSON.toJSONString(position.getLot()));

		if (lot == null) {
			return new ActionResult<>(Boolean.FALSE, "批次不存在");
		}

		// //获取该批次所有头寸
		// List<Position> positionList = repository.GetQueryable(Position.class)
		// .where(DetachedCriteria.forClass(Position.class).add(Property.forName("LotId").eq(lot.getId())))
		// .toList(); // 该批次已保值不包含本次
		//
		//
		//// Lot lot = lotRepository.GetQueryable(Lot.class)
		//// .where(DetachedCriteria.forClass(Lot.class).add(Property.forName("Id").eq(position.getLotId())))
		//// .firstOrDefault();
		//
		// BigDecimal dcSum = BigDecimal.ZERO;
		//
		// //循环所有头寸，累计总数量
		// for (Position p : positionList) {
		// dcSum = dcSum.add(p.getQuantity());
		// }
		//
		// dcSum = dcSum.add(position.getQuantity()); // 保值总数量包含本次

		repository.SaveOrUpdate(position);

		// commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());

		commonService.UpdatePriceAndHedgeFlag4Lot2(lot);

		commonService.UpdateFuturesSpread(lot.getId()); // 重新计算Spread

		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> UnPosition2Lot(Position position) {

		try {

			if (position == null || position.getLotId() == null) {
				return new ActionResult<>(Boolean.FALSE, "未分配头寸，无需取消分配");
			}

			// 获取对应批次
			Lot lot = lotRepository.getOneById(position.getLotId(), Lot.class);

			if (lot == null) {
				return new ActionResult<>(Boolean.FALSE, "批次不存在");
			}

			if (position.getIsCarry() != null && position.getIsCarry()) {

				// 获取头寸列表
				List<Position> carrys = repository.GetQueryable(Position.class)
						.where(DetachedCriteria.forClass(Position.class)
								.add(Property.forName("CarryCounterpart").eq(position.getCarryCounterpart()))
								.add(Property.forName("IsCarry").eq(Boolean.TRUE)))
						.toList();

				// 取消头寸与批次的相关关系
				for (Position carry : carrys) {
					carry.setLotId(null);
					repository.SaveOrUpdate(carry);
				}
			} else {
				position.setLotId(null);
				repository.SaveOrUpdate(position);
			}

			commonService.UpdatePriceAndHedgeFlag4Lot(lot.getId());
			commonService.UpdateFuturesSpread(lot.getId());

		} catch (Exception e) {

			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
		return new ActionResult<>(Boolean.TRUE, "取消成功");
	}

	@Override
	public ActionResult<String> Delete(String id) {

		Position position = repository.getOneById(id, Position.class);

		if (StringUtils.isNotBlank(position.getLotId())) {
			return new ActionResult<>(Boolean.FALSE, "已经用于保值，不允许删除。");
		}
		if (position.getIsSplitted() && StringUtils.isNotBlank(position.getSourceId())) {
			Position sourcePosition = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("Id", position.getSourceId())))
					.firstOrDefault();
			if (StringUtils.isNotBlank(sourcePosition.getLotId())) {
				return new ActionResult<>(Boolean.FALSE, "原始头寸已经用于保值，不允许删除。");
			}
		}
		if (StringUtils.isNotBlank(position.getPosition4BrokerId())) {
			DetachedCriteria dcOtherPosition = DetachedCriteria.forClass(Position.class);
			if (StringUtils.isNotBlank(position.getSourceId())) {
				dcOtherPosition.add(Restrictions.ne("Id", position.getSourceId()));
			}
			dcOtherPosition.add(Restrictions.ne("Id", id));
			dcOtherPosition.add(Restrictions.eq("Position4BrokerId", position.getPosition4BrokerId()));
			List<Position> positionList = repository.GetQueryable(Position.class).where(dcOtherPosition).toList();
			for (Position p : positionList) {
				if (StringUtils.isNotBlank(p.getLotId())) {
					return new ActionResult<>(false, "原始头寸拆分出来的其他头寸已关联批次，不允许删除。");
				}
			}
		}
		if (position.getIsSourceOfVirtual()) {
			return new ActionResult<>(Boolean.FALSE, "存在据此生成的虚拟头寸，不允许删除。请先删除相应的虚拟头寸。");
		}
		// 检查是否存在拆分出来的记录
		Position split = repository.GetQueryable(Position.class)
				.where(DetachedCriteria.forClass(Position.class).add(Property.forName("SourceId").eq(position.getId())))
				.firstOrDefault();
		if (split != null) {
			return new ActionResult<>(Boolean.FALSE, "请先删除被拆分出来的记录。");
		}
		Square positionSquared = squareRepository.GetQueryable(Square.class)
				.where(DetachedCriteria.forClass(Square.class)
						.add(Restrictions.disjunction().add(Property.forName("LongId").eq(position.getId()))
								.add(Property.forName("ShortId").eq(position.getId()))))
				.firstOrDefault();
		if (positionSquared != null) {
			return new ActionResult<>(Boolean.FALSE, "该头寸全部或者部分已经对齐，不允许修改。");
		}

		Position virtualSource = null;
		if (position.getVirtualId() != null) {
			virtualSource = repository.getOneById(position.getVirtualId(), Position.class);
			Position children = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class)
							.add(Property.forName("VirtualId").eq(virtualSource.getId()))
							.add(Property.forName("LotId").isNotNull()).add(Property.forName("LotId").isNotEmpty()))
					.firstOrDefault();
			if (children != null) {
				return new ActionResult<>(Boolean.FALSE, "虚拟头寸的一个或者一对已被用于保值，不允许删除。");
			}
		}
		// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去

		Position source = null;
		if (position.getSourceId() != null) {
			source = repository.getOneById(position.getSourceId(), Position.class);
			// if (!position.getIsCarry()) {
			// source.setQuantity(source.getQuantity().add(position.getQuantity()));
			// repository.SaveOrUpdate(source);
			// }

		}

		// 如果据此复制出了虚拟头寸，则先删除虚拟头寸
		if (position.getIsSourceOfVirtual()) {
			if (virtualSource != null) {
				List<Position> children = repository.GetQueryable(Position.class).where(DetachedCriteria
						.forClass(Position.class).add(Property.forName("VirtualId").eq(virtualSource.getId())))
						.toList();
				for (Position child : children) {
					repository.PhysicsDelete(child.getId(), Position.class);
					updatePositionQuantityOrDeleteBroker(child);
				}
			}
		}

		// 如果删除的是虚拟头寸，并且允许删除，则必须成对删除(非调期,，调期头寸统一在后面处理)
		if (StringUtils.isNotBlank(position.getVirtualId()) && !position.getIsCarry()) {
			// step 1: 找到原生的头寸是哪一个
			virtualSource = repository.getOneById(position.getVirtualId(), Position.class);
			// step 2: 删除自身、和另一个对手头寸，都是虚拟的头寸
			String sql = String.format("Delete from [Physical].Position where VirtualId = '%s'", virtualSource.getId());
			repository.ExecuteNonQuery(sql);
			// step 3: 同时更新原生头寸的标志
			virtualSource.setIsSourceOfVirtual(Boolean.FALSE);
			repository.SaveOrUpdate(virtualSource);
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
		}
		// 如果删除的是被调期头寸，必须先删除原调期头寸
		if (repository.GetQueryable(Position.class)
				.where(DetachedCriteria.forClass(Position.class).add(Property.forName("CarryPositionId").eq(id)))
				.firstOrDefault() != null) {
			return new ActionResult<>(Boolean.FALSE, "该头寸已经被调期，请先删除调期头寸");
		}
		// 如果删除的是调期头寸，并且允许删除，则必须成对删除
		if (position.getIsCarry()) {
			if (position.getIsSplitted()) {
				// 分拆的虚拟头寸
				Position carryCounter = repository.GetQueryable(Position.class)
						.where(DetachedCriteria.forClass(Position.class)
								.add(Property.forName("Id").neOrIsNotNull(position.getId()))
								.add(Property.forName("CarryCounterpart").eq(position.getCarryCounterpart())))
						.firstOrDefault();
				if (carryCounter != null) {
					Position sourceCounter = repository.GetQueryable(Position.class).where(DetachedCriteria
							.forClass(Position.class).add(Property.forName("Id").eq(carryCounter.getSourceId())))
							.firstOrDefault();
					if (source != null && sourceCounter != null) {
						if (!source.getId().equals(sourceCounter.getId())) {
							source.setQuantity(source.getQuantity().add(position.getQuantity()));
							repository.SaveOrUpdate(source);
							sourceCounter.setQuantity(sourceCounter.getQuantity().add(carryCounter.getQuantity()));
							repository.SaveOrUpdate(sourceCounter);
						} else { // 非调期分拆头寸，作为普通头寸处理
							if (position.getIsSourceOfCarry() || carryCounter.getIsSourceOfCarry()) {
								return new ActionResult<>(Boolean.FALSE, "该头寸已经被调期，请先删除调期头寸_");
							}
							source.setIsSourceOfCarry(Boolean.FALSE);
							repository.SaveOrUpdate(source);
						}
					}
					repository.PhysicsDelete(position.getId(), Position.class);
					updatePositionQuantityOrDeleteBroker(position);
					repository.PhysicsDelete(carryCounter.getId(), Position.class);
					updatePositionQuantityOrDeleteBroker(carryCounter);
				}
				return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
			} else { // 非分拆头寸,正常调期的一对头寸
				// step 1: 找到来源的头寸是哪一个
				Position carrySource = null;
				if (StringUtils.isNotBlank(position.getCarryPositionId())) {
					carrySource = repository.getOneById(position.getCarryPositionId(), Position.class);
				}
				Position carryWasCarry = repository.GetQueryable(Position.class)
						.where(DetachedCriteria.forClass(Position.class)
								.add(Property.forName("CarryCounterpart").eq(position.getCarryCounterpart()))
								.add(Property.forName("IsSourceOfCarry").eq(Boolean.TRUE)))
						.firstOrDefault();
				if (carryWasCarry != null) {
					return new ActionResult<>(Boolean.FALSE, "该头寸已经被调期，请先删除调期头寸1");
				}
				// step 2: 删除自身、和另一个对手头寸，都是调期的头寸
				List<Position> virtualPostiionList = repository.GetQueryable(Position.class)
						.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("IsCarry", Boolean.TRUE))
								.add(Restrictions.eq("CarryCounterpart", position.getCarryCounterpart())))
						.toList();
				for (Position virtualPosition : virtualPostiionList) {
					updatePositionQuantityOrDeleteBroker(virtualPosition);
					repository.PhysicsDelete(virtualPosition.getId(), Position.class);
				}
				// String sql = String.format(
				// "Delete from [Physical].Position where IsCarry =1 and
				// CarryCounterPart = '%s'",
				// position.getCarryCounterpart());
				// repository.ExecuteNonQuery(sql);
				// step 3: 同时更新来源头寸的标志
				if (carrySource != null) {
					carrySource.setIsSourceOfCarry(Boolean.FALSE);
					repository.SaveOrUpdate(carrySource);
				}

				return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
			}
		}

		// 同步删除broker头寸明细
		// List<Position4Broker> exists =
		// position4BrokerRepository.GetQueryable(Position4Broker.class)
		// .where(DetachedCriteria.forClass(Position4Broker.class)
		// .add(Property.forName("OriginalId").eq(position.getId())))
		// .toList();
		// if (exists != null && exists.size() > 0) {
		// boolean isSquared = Boolean.FALSE;
		// for (Position4Broker p : exists) {
		// if (p.getIsSquared()) {
		// isSquared = Boolean.TRUE;
		// break;
		// }
		// }
		// if (isSquared) {
		// return new ActionResult<>(Boolean.FALSE,
		// "Broker头寸明细已经按照Broker对齐,当前头寸不可删除，请先处理Broker头寸明细");
		// } else {
		// boolean bSouSquared = Boolean.FALSE;
		// if (position.getIsSplitted()) {
		// Position sou =
		// repository.GetQueryable(Position.class).where(DetachedCriteria
		// .forClass(Position.class).add(Property.forName("Id").eq(position.getSourceId())))
		// .firstOrDefault();
		// if (sou != null) {
		// Position4Broker souExists =
		// position4BrokerRepository.GetQueryable(Position4Broker.class)
		// .where(DetachedCriteria.forClass(Position4Broker.class)
		// .add(Property.forName("Id").eq(sou.getPosition4BrokerId())))
		// .firstOrDefault();
		// if (souExists != null) {
		// bSouSquared = souExists.getIsSquared();
		// if (!souExists.getIsSquared()) {
		// souExists.setQuantity(sou.getQuantity());
		// position4BrokerRepository.SaveOrUpdate(souExists);
		// }
		// }
		// }
		// }
		// if (!bSouSquared) {// 如果拆分批次的原始批次已经对齐，那么此时就不能删除broker拆分头寸了
		// for (Position4Broker broker : exists) {
		// position4BrokerRepository.PhysicsDelete(broker);
		// }
		// }
		// }
		// }
		updatePositionQuantityOrDeleteBroker(position);
		repository.PhysicsDelete(id, Position.class);
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
	}

	void updatePositionQuantityOrDeleteBroker(Position position) {

		if (position.getIsSplitted()) {
			Position sou = repository.GetQueryable(Position.class).where(
					DetachedCriteria.forClass(Position.class).add(Property.forName("Id").eq(position.getSourceId())))
					.firstOrDefault();
			sou.setQuantity(sou.getQuantity().add(position.getQuantity()));
			repository.Save(sou);
		} else {
			if (StringUtils.isNotBlank(position.getPosition4BrokerId())) {
				position4BrokerRepository.PhysicsDelete(position.getPosition4BrokerId(), Position4Broker.class);
			}
		}
	}

	@Override
	public ActionResult<String> SplitPosition(CpSplitPosition cpSplitPosition, String userId) {
		// User user = userRepository.getOneById(userId, User.class);
		BigDecimal qtySplit = cpSplitPosition.getQuantitySplitted();
		Position position = cpSplitPosition.getOriginalPosition();
		if (position == null || position.getIsSquared() || position.getIsAccounted()) {
			return new ActionResult<>(Boolean.FALSE, "不符合拆分的条件：记录为空、或已结算、或已会计。");
		}
		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(BigDecimal.ZERO) < 0)
				&& qtySplit.compareTo(BigDecimal.ZERO) > 0) {
			return new ActionResult<>(Boolean.FALSE, "拆分的数量与原数量的符号应保持相同。");
		}
		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(position.getQuantity()) >= 0)
				|| (position.getQuantity().compareTo(BigDecimal.ZERO) < 0
						&& qtySplit.compareTo(position.getQuantity()) <= 0)) {
			return new ActionResult<>(Boolean.FALSE, "拆分的数量必须小于原数量。");
		}
		position.setQuantity(position.getQuantity().subtract(qtySplit));
		position.setQuantityUnSquared(position.getQuantityUnSquared().subtract(qtySplit));
		repository.SaveOrUpdate(position);
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
		String splitId = repository.SaveOrUpdateRetrunId(theSplit);
		// 如果拆分的是非虚拟的卖出头寸需要同步拆分头寸明细按Broker（头寸明细按Broker必须未结算对齐）
		if (!position.getIsVirtual() && position.getLS().equals(LS.SHORT)) {
			Position split = repository.GetQueryable(Position.class)
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
					SavePosition2Broker(split);
				}
			} else {
				SavePosition2Broker(position);
				SavePosition2Broker(split);
			}
		}

		if (position.getIsCarry()) {
			BigDecimal qtySplit2 = BigDecimal.ZERO.subtract(qtySplit);
			// 调期头寸需要同步拆分对手头寸
			Position counterPart = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.ne("Id", position.getId()))
							.add(Restrictions.eq("CarryCounterpart", position.getCarryCounterpart())))
					.firstOrDefault();
			if (counterPart != null) {
				counterPart.setQuantity(counterPart.getQuantity().subtract(qtySplit2));
				counterPart.setQuantityUnSquared(counterPart.getQuantityUnSquared().subtract(qtySplit2));
				repository.SaveOrUpdate(counterPart);
				Position theSplit1 = new Position();
				BeanUtils.copyProperties(counterPart, theSplit1);
				theSplit1.setId(null);
				theSplit1.setQuantity(qtySplit2);
				theSplit1.setQuantityUnSquared(qtySplit2);
				theSplit1.setIsSplitted(Boolean.TRUE);
				int iNo1 = commonService.GetSequenceIndex(NumTypes.Split_Storage + counterPart.getOurRef()).intValue();// 生成唯一序号
				theSplit1.setOurRef(counterPart.getOurRef() + ("(s" + iNo1 + ")"));
				theSplit1.setSourceId(counterPart.getId());
				theSplit1.setCarryCounterpart(carryCounterPart);
				theSplit1.setCarryRef("");
				String splitId1 = repository.SaveOrUpdateRetrunId(theSplit1);
				// 如果拆分的是非虚拟的卖出头寸需要同步拆分头寸明细按Broker（头寸明细按Broker必须未结算对齐）
				if (!counterPart.getIsVirtual() && counterPart.getLS().equals(LS.SHORT)) {
					Position split1 = repository.GetQueryable(Position.class).where(
							DetachedCriteria.forClass(Position.class).add(Restrictions.eqOrIsNull("Id", splitId1)))
							.firstOrDefault();
					Position4Broker brokerPosition = position4BrokerRepository.GetQueryable(Position4Broker.class)
							.where(DetachedCriteria.forClass(Position4Broker.class)
									.add(Restrictions.eq("OriginalId", counterPart.getId())))
							.firstOrDefault();
					if (brokerPosition != null) {
						if (!brokerPosition.getIsSquared()) {
							brokerPosition.setQuantity(counterPart.getQuantity());
							brokerPosition.setQuantityUnSquared(counterPart.getQuantityUnSquared());
							position4BrokerRepository.SaveOrUpdate(brokerPosition);

							SavePosition2Broker(split1);
						}
					} else {
						SavePosition2Broker(counterPart);
						SavePosition2Broker(split1);
					}
				}
			}
		}
		return new ActionResult<>(Boolean.TRUE, "拆分成功。");
	}

	@Override
	public ActionResult<Position> GetById(String positionId) {

		Position position = repository.GetQueryable(Position.class)
				.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eqOrIsNull("Id", positionId)))
				.firstOrDefault();
		if (position != null) {
			position = commonService.SimplifyData(position);
		}
		return new ActionResult<>(Boolean.TRUE, "", position);
	}

	@Override
	public ActionResult<Reuter> GetReuterPrice(String CommodityId, Date TradeDate, Date PromptDate) {
		// 3个月以内取到期日的，超过三个月的取到期日所处月第三周周三的单价
		Reuter lme = reuterRepository.GetQueryable(Reuter.class)
				.where(DetachedCriteria.forClass(Reuter.class).add(Restrictions.eq("CommodityId", CommodityId))
						.add(Restrictions.eq("PromptDate", DateUtil.doSFormatDate(PromptDate, "yyyy-MM-dd 00:00:00")))
						.add(Restrictions.eq("TradeDate", DateUtil.doSFormatDate(TradeDate, "yyyy-MM-dd 00:00:00"))))
				.firstOrDefault();
		return new ActionResult<>(Boolean.TRUE, "", lme);
	}

	@Override
	public ActionResult<String> CreateVirtual(String positionId) {

		Position position = repository.getOneById(positionId, Position.class);
		if (position == null || position.getLot() == null) {
			return new ActionResult<>(Boolean.FALSE, "只有分配给BVI的保值头寸才允许复制虚拟头寸，且只能复制一次。");
		}
		if (position.getLot().getLegal().getCode() != "SB" || position.getIsSourceOfVirtual()) {
			return new ActionResult<>(Boolean.FALSE, "只有分配给BVI的保值头寸才允许复制虚拟头寸，且只能复制一次。");
		}
		try {
			Position longPosition = new Position();
			BeanUtils.copyProperties(position, longPosition);
			longPosition.setId(null);
			longPosition.setLS(position.getLS().equals(LS.LONG) ? LS.SHORT : LS.LONG);
			longPosition.setQuantity(position.getQuantity().negate());
			longPosition.setIsVirtual(Boolean.TRUE);
			longPosition.setOurRef(position.getOurRef() + "(v)");
			longPosition.setVirtualId(position.getId());
			repository.SaveOrUpdate(longPosition);
			Position shortPosition = new Position();
			BeanUtils.copyProperties(position, shortPosition);
			shortPosition.setId(null);
			shortPosition.setLotId(null);
			shortPosition.setIsVirtual(Boolean.TRUE);
			shortPosition.setOurRef(position.getOurRef() + "(v)");
			shortPosition.setVirtualId(position.getId());
			repository.SaveOrUpdate(shortPosition);

			position.setIsSourceOfVirtual(Boolean.TRUE);
			repository.SaveOrUpdate(position);
			return new ActionResult<>(Boolean.TRUE, "成功复制了虚拟头寸。");
		} catch (Exception ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@Override
	public ActionResult<String> CreateVirtual(Position position) {
		if (position.getId() == null) {
			return new ActionResult<>(Boolean.FALSE, "当前选的头寸不存在");
		}
		// position信息可能部分在前台发生变化，比如价格可以使用用户新价格然后传到后台
		// 数据有效性检查
		// 原始，来源头寸，用于更新状态，新生成的头寸数据都是有position
		Position positionSource = repository.getOneById(position.getId(), Position.class);

		if (positionSource == null || positionSource.getLot() == null) {
			return new ActionResult<>(Boolean.FALSE, "只有已经分配给BVI的保值头寸才允许复制虚拟头寸，且只能复制一次。");
		}
		if (!positionSource.getLot().getLegal().getCode().equals("SB") || positionSource.getIsSourceOfVirtual()) {
			return new ActionResult<>(Boolean.FALSE, "只有分配给BVI的保值头寸才允许复制虚拟头寸，且只能复制一次。");
		}
		try {
			List<Lot> smLots = getSmLots(positionSource.getLotId());
			// 先检查bvi采购-->Bvi销售--sm采购是否一一对应（不存在bvi采购分拆多次销售）
			if (smLots != null && smLots.size() == 1) {
				Position longPosition = new Position();
				BeanUtils.copyProperties(position, longPosition);// 分配给bvi采购，用于对齐bvi采购保值头寸
				longPosition.setId(null);
				longPosition.setLS(position.getLS().equals(LS.LONG) ? LS.SHORT : LS.LONG); // LS方向必须取反
				longPosition.setQuantity(position.getQuantity().negate()); // 数量方向必须取反
				longPosition.setIsVirtual(true);
				longPosition.setOurRef(position.getOurRef() + "(v)");
				longPosition.setVirtualId(position.getId());
				longPosition.setLotId(positionSource.getLotId()); // 不可修改批次
				repository.SaveOrUpdate(longPosition);

				Position shortPosition = new Position();
				BeanUtils.copyProperties(position, shortPosition);// 分配给SM采购
				shortPosition.setId(null);
				shortPosition.setLotId(smLots.get(0).getId()); // 可以用于其它现货的保值,找到对应的sm采购批次
				shortPosition.setIsVirtual(true);
				shortPosition.setOurRef(position.getOurRef() + "(v)");
				longPosition.setVirtualId(position.getId());
				repository.SaveOrUpdate(shortPosition);

				// 修改原头寸状态
				positionSource.setIsSourceOfVirtual(true);
				repository.SaveOrUpdate(positionSource);

				// 更新头寸对应的批次的标记及信息
				Lot bviBuy = lotRepository.GetQueryable(Lot.class).where(
						DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("Id", positionSource.getLotId())))
						.firstOrDefault();
				Lot smBuy = smLots.get(0);
				smBuy.setIsHedged(bviBuy.getIsHedged());
				lotRepository.SaveOrUpdate(smBuy);

				if (StringUtils.isNotBlank(smBuy.getCounterpartId())) {
					Lot bviSale = lotRepository.GetQueryable(Lot.class).where(
							DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("Id", smBuy.getCounterpartId())))
							.firstOrDefault();
					bviSale.setIsHedged(bviBuy.getIsHedged());
					lotRepository.SaveOrUpdate(bviSale);
				}
				return new ActionResult<String>(Boolean.TRUE, "成功复制了虚拟头寸。");
			} else {
				return new ActionResult<String>(Boolean.FALSE, "BVI采购-->BVI销售-->SM采购不是一一对应,不能复制虚拟头寸");
			}
		} catch (RuntimeException ex) {
			return new ActionResult<String>(Boolean.FALSE, ex.getMessage());
		}
	}

	@Override
	public ActionResult<Position> GetCounterPartById(String positionId) {

		// Position cur = repository.GetQueryable(Position.class)
		// .where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("Id",
		// positionId)))
		// .firstOrDefault();// 当前头寸

		Position cur = repository.getOneById(positionId, Position.class);

		if (cur != null) {

			Position positions = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("IsCarry", Boolean.TRUE))
							.add(Restrictions.eq("CarryCounterpart", cur.getCarryCounterpart()))
							.add(Restrictions.eq("CarryPositionId", cur.getCarryPositionId()))
							.add(Restrictions.ne("Id", positionId)))
					.firstOrDefault();

			return new ActionResult<>(Boolean.TRUE, "", positions);
		} else {
			return new ActionResult<>(Boolean.FALSE, "编号为：" + positionId + " 的头寸信息不存在");
		}
	}

	@Override
	public ActionResult<List<Position>> GetCarryPostionsById(String positionId) {

		Position cur = repository.getOneById(positionId, Position.class);

		if (cur != null && cur.getIsCarry()) {

			// 获取所有相关position
			List<Position> positions = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("IsCarry", Boolean.TRUE))
							.add(Restrictions.eq("CarryCounterpart", cur.getCarryCounterpart())))
					.toList();

			// 格式化数据
			positions = commonService.SimplifyDataPositionList(positions);

			return new ActionResult<>(Boolean.TRUE, "", positions);
		} else {
			return new ActionResult<>(Boolean.FALSE, "当前头寸信息错误");
		}
	}

	@Override
	public List<Position> GetVirtualSwapPositionByCounterpartId(String counterpartId) {
		List<Position> list = repository.GetQueryable(Position.class).where(DetachedCriteria.forClass(Position.class)
				.add(Restrictions.eq("IsVirtual", Boolean.TRUE)).add(Restrictions.eq("IsCarry", Boolean.TRUE))
				.add(Restrictions.eq("CarryCounterpart", counterpartId)).add(Restrictions.isNull("CarryPositionId")))
				.toList();
		return commonService.SimplifyDataPositionList(list);
	}

	@Override
	public ActionResult<String> SaveVirtualSwapPosition(List<Position> positions) throws Exception {

		if (positions == null || positions.size() != 2) {
			return new ActionResult<>(Boolean.FALSE, "提交的虚拟调期头寸数量不正确，请重试！");
		}

		if (!positions.get(0).getLS().equals("L")) {
			return new ActionResult<>(Boolean.FALSE, "提交的第一条记录必须为多头，请检查！");
		}

		if (!positions.get(1).getLS().equals("S")) {
			return new ActionResult<>(Boolean.FALSE, "提交的第二条记录必须为空头，请检查！");
		}
		for (Position position : positions) {
			Market market = marketRepository.getOneById(position.getMarketId(), Market.class);
			position.setCurrency(market.getCurrency());
			if (StringUtils.isNotBlank(position.getId())) {
				Square stemp = squareRepository.GetQueryable(Square.class)
						.where(DetachedCriteria.forClass(Square.class)
								.add(Restrictions.or(Restrictions.eq("LongId", position.getId()),
										Restrictions.eq("ShortId", position.getId()))))
						.firstOrDefault();

				if (stemp != null) {
					throw new Exception("存在头寸全部或者部分已经对齐，不允许修改");
				}
			}

			if (position.getIsVirtual() && StringUtils.isBlank(position.getId())) // 虚拟头寸交易编号(修改的时候不需要重复增加）
			{
				if (position.getIsCarry()) {
					position.setOurRef(
							(StringUtils.isBlank(position.getOurRef()) ? position.getCarryRef() : position.getOurRef())
									+ "(vc" + position.getLS().toLowerCase() + ")");
				} else {
					position.setOurRef(position.getOurRef() + "(v)");
				}
			} else {
				// 普通调期头寸
				if (position.getIsCarry() && StringUtils.isBlank(position.getId())) {
					position.setOurRef(
							(StringUtils.isBlank(position.getOurRef()) ? position.getCarryRef() : position.getOurRef())
									+ "(c" + position.getLS().toLowerCase() + ")");
				}
			}
			/// #region 检查交易编号
			Position refExist = repository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class)
							.add(Restrictions.and(Restrictions.eq("Id", position.getId()),
									Restrictions.or(Restrictions.eq("OurRef", position.getOurRef())),
									Restrictions.eq("CarryRef", position.getOurRef()),
									Restrictions.eq("OurRef", position.getCarryRef()))))
					.firstOrDefault();
			if (refExist != null) {
				throw new Exception("交易编号已经存在");
			}
			position.setQuantityUnSquared(position.getQuantity());
			repository.SaveOrUpdate(position);
			SavePosition2Broker(position);
		}
		return new ActionResult<>(Boolean.TRUE, "虚拟调期头寸保存成功！");
	}

	@Override
	public List<Position> Positions(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {
		List<Position> list = repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
		return assemblingBeanList(list);
	}

	/**
	 * 根据BVI采购找到对应的SM采购
	 * 
	 * @return
	 */
	private List<Lot> getSmLots(String BviLotId) {
		if (BviLotId == null) {
			return null;
		}
		Lot bviLot = lotRepository.GetQueryable(Lot.class)
				.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eqOrIsNull("Id", BviLotId)))
				.firstOrDefault();
		if (bviLot == null) {
			return null;
		}
		List<Storage> storageins = storageRepository.GetQueryable(Storage.class)
				.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", BviLotId))
						.add(Restrictions.eq("MT", MT4Storage.Take)))
				.toList();// bvi收货
		List<Lot> SmLots = new ArrayList<Lot>();
		HashSet<String> smLotsSet = new HashSet<>();
		for (Storage storagein : storageins) {
			List<Storage> storageSmIns = storageRepository.GetQueryable(Storage.class)
					.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.isNotNull("BviSourceId"))
							.add(Restrictions.eq("BviSourceId", storagein.getId()))
							.add(Restrictions.eq("MT", MT4Storage.Take)))
					.toList();
			for (Storage storageSmIn : storageSmIns) {
				if (smLotsSet.contains(storageSmIn.getLotId())) {
					continue;
				}
				Lot smlot = lotRepository.GetQueryable(Lot.class)
						.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("Id", storageSmIn.getLotId())))
						.firstOrDefault();
				SmLots.add(smlot);
				smLotsSet.add(smlot.getId());
			}
		}
		return SmLots;
	}

	/**
	 * 以下是打掉关系
	 */
	public List<Position> assemblingBeanList(List<Position> ct) {
		if (ct.size() == 0)
			return null;

		List<String> marketIds = new ArrayList<>();
		List<String> commodityIds = new ArrayList<>();
		// List<String> brokerIds=new ArrayList<>();
		// List<String> customerIds=new ArrayList<>();
		List<String> lotIds = new ArrayList<>();
		ct.forEach(pt -> {
			if (pt.getMarketId() != null) {
				marketIds.add(pt.getMarketId());
			}
			if (pt.getCommodityId() != null) {
				commodityIds.add(pt.getCommodityId());
			}
			// if(pt.getBrokerId()!=null){
			// brokerIds.add(pt.getBrokerId());
			// }
			// if(pt.getCustomerId()!=null){
			// customerIds.add(pt.getCustomerId());
			// }
			if (pt.getLotId() != null) {
				lotIds.add(pt.getLotId());
			}
		});
		List<Market> markets = new ArrayList<>();
		if (marketIds.size() > 0) {
			DetachedCriteria dc1 = DetachedCriteria.forClass(Market.class);
			dc1.add(Restrictions.in("Id", marketIds));
			markets = this.marketRepository.GetQueryable(Market.class).where(dc1).toList();
		}
		List<Commodity> commoditys = new ArrayList<>();
		if (commodityIds.size() > 0) {
			DetachedCriteria dc2 = DetachedCriteria.forClass(Commodity.class);
			dc2.add(Restrictions.in("Id", commodityIds));
			commoditys = this.commodityRepository.GetQueryable(Commodity.class).where(dc2).toList();
		}
		// List<Broker> brokers=new ArrayList<>();
		// if(brokerIds.size()>0){
		// DetachedCriteria dc3=DetachedCriteria.forClass(Broker.class);
		// dc3.add(Restrictions.in("Id", brokerIds));
		// brokers=this.brokerRepository.GetQueryable(Broker.class).where(dc3).toList();
		// }
		// List<Customer> customers=new ArrayList<>();
		// if(customerIds.size()>0){
		// DetachedCriteria dc4=DetachedCriteria.forClass(Customer.class);
		// dc4.add(Restrictions.in("Id", customerIds));
		// customers=this.customerRepository.GetQueryable(Customer.class).where(dc4).toList();
		// }
		List<Lot> lots = new ArrayList<>();
		if (lotIds.size() > 0) {
			DetachedCriteria dc5 = DetachedCriteria.forClass(Lot.class);
			dc5.add(Restrictions.in("Id", lotIds));
			lots = this.lotRepository.GetQueryable(Lot.class).where(dc5).toList();
		}
		for (Position pt : ct) {
			if (markets.size() > 0) {
				markets.forEach(m -> {
					if (m.getId().equals(pt.getMarketId())) {
						pt.setMarket(m);
					}
				});
			}
			if (commoditys.size() > 0) {
				commoditys.forEach(m -> {
					if (m.getId().equals(pt.getCommodityId())) {
						pt.setCommodity(m);
					}
				});
			}
			// if(brokers.size()>0){
			// brokers.forEach(m->{
			// if(m.getId().equals(pt.getBrokerId())){
			// pt.setBroker(m);
			// }
			// });
			// }
			// if(customers.size()>0){
			// customers.forEach(m->{
			// if(m.getId().equals(pt.getCustomerId())){
			// pt.setCustomer(m);
			// }
			// });
			// }
			if (lots.size() > 0) {
				lots.forEach(m -> {
					if (m.getId().equals(pt.getLotId())) {
						pt.setLot(m);
					}
				});
			}
		}

		List<String> contractIds = new ArrayList<>();
		ct.forEach(pt -> {
			if (pt.getLot() != null && pt.getLot().getContractId() != null) {
				contractIds.add(pt.getLot().getContractId());
			}
		});

		List<Contract> contracts = new ArrayList<>();
		if (contractIds.size() > 0) {
			DetachedCriteria dc6 = DetachedCriteria.forClass(Contract.class);
			dc6.add(Restrictions.in("Id", contractIds));
			contracts = this.contractRepository.GetQueryable(Contract.class).where(dc6).toList();
		}
		for (Position pt : ct) {
			if (contracts.size() > 0) {
				contracts.forEach(m -> {
					if (pt.getLot() != null && m.getId().equals(pt.getLot().getContractId())) {
						pt.getLot().setContract(m);
					}
				});
			}
		}

		List<String> legalIds = new ArrayList<>();
		ct.forEach(pt -> {
			if (pt.getLot() != null && pt.getLot().getContract() != null
					&& pt.getLot().getContract().getLegalId() != null) {
				legalIds.add(pt.getLot().getContract().getLegalId());
			}
		});

		List<Legal> legals = new ArrayList<>();
		if (contractIds.size() > 0) {
			DetachedCriteria dc7 = DetachedCriteria.forClass(Legal.class);
			dc7.add(Restrictions.in("Id", contractIds));
			legals = this.legalRepository.GetQueryable(Legal.class).where(dc7).toList();
		}
		for (Position pt : ct) {
			if (legals.size() > 0) {
				legals.forEach(m -> {
					if (pt.getLot() != null && pt.getLot().getContract() != null
							&& m.getId().equals(pt.getLot().getContract().getLegalId())) {
						pt.getLot().getContract().setLegal(m);
					}
				});
			}
		}
		return ct;
	}

	public void assemblingBean(Position pt) {
		if (pt != null) {
			// if(pt.getTraderId()!=null){
			// User trader=this.userRepository.getOneById(pt.getTraderId(),
			// User.class);
			// pt.setTrader(trader);
			// }

			if (pt.getMarketId() != null) {
				Market market = this.marketRepository.getOneById(pt.getMarketId(), Market.class);
				pt.setMarket(market);
			}
			if (pt.getCommodityId() != null) {
				Commodity commodity = this.commodityRepository.getOneById(pt.getCommodityId(), Commodity.class);
				pt.setCommodity(commodity);
			}
			if (pt.getBrokerId() != null) {
				Broker broker = this.brokerRepository.getOneById(pt.getBrokerId(), Broker.class);
				pt.setBroker(broker);
			}
			if (pt.getCustomerId() != null) {
				Customer customer = this.customerRepository.getOneById(pt.getCustomerId(), Customer.class);
				pt.setCustomer(customer);
			}
			// if(pt.getSourceId()!=null){
			// Position source=this.repository.getOneById(pt.getSourceId(),
			// Position.class);
			// pt.setSource(source);
			// }
			// if(pt.getVirtualId()!=null){
			// Position virtual=this.repository.getOneById(pt.getVirtualId(),
			// Position.class);
			// pt.setVirtual(virtual);
			// }
			// if(pt.getLegalId()!=null){
			// Legal legal=this.legalRepository.getOneById(pt.getLegalId(),
			// Legal.class);
			// pt.setLegal(legal);
			// }
			// if(pt.getContractId()!=null){
			// Contract
			// contract=this.contractRepository.getOneById(pt.getContractId(),
			// Contract.class);
			// pt.setContract(contract);
			// }
			if (pt.getLotId() != null) {
				Lot lot = this.lotRepository.getOneById(pt.getLotId(), Lot.class);
				if (lot != null && lot.getContractId() != null) {
					Contract contract = this.contractRepository.getOneById(lot.getContractId(), Contract.class);
					if (contract != null && contract.getLegalId() != null) {
						Legal legal = this.legalRepository.getOneById(contract.getLegalId(), Legal.class);
						contract.setLegal(legal);
					}
					lot.setContract(contract);
				}
				pt.setLot(lot);
			}
			// if(pt.getInvoiceId()!=null){
			// Invoice
			// invoice=this.invoiceRepository.getOneById(pt.getInvoiceId(),
			// Invoice.class);
			// pt.setInvoice(invoice);
			// }
			// if(pt.getCreatedId()!=null){
			// User created=this.userRepository.getOneById(pt.getCreatedId(),
			// User.class);
			// pt.setCreated(created);
			// }
			// if(pt.getUpdatedId()!=null){
			// User updated=this.userRepository.getOneById(pt.getUpdatedId(),
			// User.class);
			// pt.setUpdated(updated);
			// }

		}
	}

	/**
	 * 投机头寸风险警示
	 */
	@Override
	public ActionResult<List<Position>> UndistributePositionRisk(PositionParams param) {
		/**
		 * 取上期所价格
		 */
		String[] commodityName = new String[] { "CU", "AL", "PB", "ZN", "NI", "SN" };
		Map<String, BigDecimal> sfePrice = quotationPrice();
		/**
		 * 获取风险范围百分比
		 */
		DetachedCriteria riskScale = DetachedCriteria.forClass(Dictionary.class);
		riskScale.add(Restrictions.eq("Name", "投机头寸风险警示比例"));
		riskScale.add(Restrictions.eq("Code", "PriceRisk"));
		List<Dictionary> comms = this.dictionaryRepository.GetQueryable(Dictionary.class).where(riskScale).toList();
		BigDecimal scale = new BigDecimal(5);
		if (comms != null && comms.size() > 0) {
			scale = new BigDecimal(comms.get(0).getValue());
		}
		BigDecimal max = new BigDecimal(1).add(scale.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
		BigDecimal min = new BigDecimal(1).subtract(scale.divide(new BigDecimal(100), 4, BigDecimal.ROUND_HALF_UP));
		if (sfePrice != null) {
			Criteria dc = this.GetCriteria();
			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				dc.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
				dc.createAlias("Broker", "broker", JoinType.LEFT_OUTER_JOIN);

				Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
						Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));

				Criterion c = Restrictions.and(Restrictions.isNotNull("BrokerId"),
						Restrictions.like("broker.Name", "%" + param.getKeyword() + "%"));

				Criterion d = Restrictions.like("OurRef", "%" + param.getKeyword() + "%");
				Criterion e = Restrictions.like("Comments", "%" + param.getKeyword() + "%");

				Disjunction disjunction = Restrictions.disjunction();

				disjunction.add(b);
				disjunction.add(c);
				disjunction.add(d);
				disjunction.add(e);
				dc.add(disjunction);
			}

			dc.createAlias("Market", "market", JoinType.LEFT_OUTER_JOIN);
			dc.add(Restrictions.eq("market.Code", "SFE"));
			dc.add(Restrictions.isNull("LotId"));
			dc.createAlias("Commodity", "commodity", JoinType.LEFT_OUTER_JOIN);
			Disjunction disjunction = Restrictions.disjunction();

			Criterion cu = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[0]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[0]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[0]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));

			Criterion al = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[1]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[1]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[1]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));

			Criterion pb = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[2]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[2]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[2]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));

			Criterion zn = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[3]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[3]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[3]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));

			Criterion ni = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[4]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[4]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[4]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));

			Criterion sn = Restrictions.and(Restrictions.eq("commodity.Code", commodityName[5]),
					Restrictions.or(
							Restrictions.lt("OurPrice",
									sfePrice.get(commodityName[5]).divide(max, 4, BigDecimal.ROUND_HALF_UP)),
							Restrictions.gt("OurPrice",
									sfePrice.get(commodityName[5]).divide(min, 4, BigDecimal.ROUND_HALF_UP))));
			disjunction.add(cu);
			disjunction.add(al);
			disjunction.add(pb);
			disjunction.add(zn);
			disjunction.add(ni);
			disjunction.add(sn);
			dc.add(disjunction);
			RefUtil total = new RefUtil();
			List<Position> resultList = this.repository.GetPage(dc, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total).getData();
			if (resultList != null && resultList.size() > 0) {
				for (Position position : resultList) {
					position.setPositionProfit(sfePrice.get(position.getCommodity().getCode())
							.subtract(position.getOurPrice()).multiply(position.getQuantity()));
					position.setM2MPrice(sfePrice.get(position.getCommodity().getCode()));
					position.setMarketName(position.getMarket().getName());
					position.setCommodityName(position.getCommodity().getName());
					position.setCustomer(null);
					position.setMarket(null);
					position.setCommodity(null);
					position.setBroker(null);
				}

				resultList = resultList.stream()
						.sorted((p1, p2) -> p1.getPositionProfit().compareTo(p2.getPositionProfit()))
						.collect(Collectors.toList());
				ActionResult<List<Position>> result = new ActionResult<>();
				result.setData(resultList);
				result.setSuccess(true);
				result.setTotal(total.getTotal());
				return result;

			}
		}
		ActionResult<List<Position>> result = new ActionResult<>();
		result.setData(new ArrayList<>());
		result.setSuccess(true);
		result.setTotal(0);
		return result;
	}

	public Map<String, BigDecimal> quotationPrice() {
		/**
		 * 取上期所价格
		 */
		Map<String, BigDecimal> priceMap = new HashMap<>();
		String[] commodityName = new String[] { "CU", "AL", "PB", "ZN", "NI", "SN" };
		Map<String, String> mainId = new HashMap<>();
		String mainmetaIds = "";
		for (String code : commodityName) {
			String mainmetalUrl = PropertiesUtil.getString("futures.quotation.mainmetal").replace("{0}", code);
			String mJson = HttpClientUtil.requestByGetMethod(mainmetalUrl);
			if (StringUtils.isNotBlank(mJson)) {
				Map<String, Object> map = JSONUtil.doConvertJson2Map(mJson);
				if (map.containsKey("code") && String.valueOf(map.get("code")).equals(CODE)) {
					String mainmeta = (String) map.get("data");
					mainId.put(mainmeta, code);
					mainmetaIds += mainmeta + ",";
				} else {
					logger.info("没有获取到主力金属.");
				}
			}
		}

		if (StringUtils.isNoneBlank(mainmetaIds)) {
			String current = PropertiesUtil.getString("futures.quotation.current").replace("{0}",
					mainmetaIds.substring(0, mainmetaIds.length() - 1));
			String cJson = HttpClientUtil.requestByGetMethod(current);
			if (StringUtils.isNotBlank(cJson)) {
				try {
					MainmetaResult mainmetaResult = (MainmetaResult) JSONUtil.doConvertStringToBean(cJson,
							MainmetaResult.class);
					if (mainmetaResult != null && mainmetaResult.getMainmeta().size() > 0) {
						for (Mainmeta m : mainmetaResult.getMainmeta()) {
							priceMap.put(mainId.get(m.getInstrumentID()), m.getLastPrice());
						}
					}
				} catch (IOException e) {
					logger.error("获取期货行情出错:", e);
				}
			} else {
				logger.info("没有获取到行情数据.");
			}
		} else {
			priceMap = null;
		}
		return priceMap;
	}

	@Override
	public ActionResult<List<Position>> Arbitrage(Position position) {
		
		Position splitPosition = position;
		BigDecimal arbitrageQuantity = splitPosition.getQuantity();
		if(arbitrageQuantity.compareTo(BigDecimal.ZERO) == 0) {
			return new ActionResult<>(false, "套利数量不能为空");
		}

		if(!splitPosition.getOCS().equalsIgnoreCase("O")) {
			return new ActionResult<>(false, "请选择非平仓头寸");
		}
		String promptDateStr = DateFormatUtils.format(splitPosition.getPromptDate(), "yyyyMMdd");
		String todayStr = DateFormatUtils.format(new Date(), "yyyyMMdd");
		if(promptDateStr.compareTo(todayStr) <= 0) {
			return new ActionResult<>(false, "头寸已到期");
		}
		if(position.getLots() != null) {
			List<Lot> lots = position.getLots();
			if(lots.size() == 0) {
				return new ActionResult<>(false, "选择的批次数量不能为0");
			}
			for(Lot lot : lots) {
				if(lot.getQuantityHedged() != null && lot.getQuantityHedged().compareTo(BigDecimal.ZERO) > 0) {
					return new ActionResult<>(false, "所选批次必须未套保");
				}
				if(!lot.getIsPriced()) {
					return new ActionResult<>(false, "必须是完成点价的批次");
				}
			}
		} else if (position.getPositions() != null) {
			List<Position> positions = position.getPositions();
			if(positions.size() == 0) {
				return new ActionResult<>(false, "选择的跨期头寸不能为空");
			}
		} else {
			return new ActionResult<>(false, "所选批次和头寸数量为空");
		}
		
		
		
		Position parentPosition = repository.getOneById(position.getId(), Position.class);
		parentPosition.setQuantity(parentPosition.getQuantity().subtract(splitPosition.getQuantity()));
		repository.SaveOrUpdate(parentPosition);
		splitPosition.setId(null);
		splitPosition.setPurpose("A");
		repository.SaveOrUpdate(splitPosition);
		if(position.getLots() != null) {
			List<Lot> lots = position.getLots();
			for(Lot lot : lots) {
				PositionLot positionLot = new PositionLot();
				positionLot.setArbitrageType(splitPosition.getArbitrageType());
				positionLot.setArbitrageRefId(lot.getId());
				positionLot.setPositionId(splitPosition.getId());
				positionLotRepo.SaveOrUpdate(positionLot);
				lot.setIsArbitrage(true);
				lotRepository.SaveOrUpdate(lot);
			}
		} else if (position.getPositions() != null) {
			for(Position p : position.getPositions()) {
				PositionLot positionLot = new PositionLot();
				positionLot.setArbitrageType(splitPosition.getArbitrageType());
				positionLot.setArbitrageRefId(p.getId());
				positionLot.setPositionId(splitPosition.getId());
				positionLotRepo.SaveOrUpdate(positionLot);
				p.setIsArbitrage(true);
				repository.SaveOrUpdate(p);
			}
		}
		
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}
}
