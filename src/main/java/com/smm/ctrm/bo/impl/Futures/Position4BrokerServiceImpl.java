package com.smm.ctrm.bo.impl.Futures;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Futures.Position4BrokerService;
import com.smm.ctrm.bo.Futures.PositionService;
import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpSplitPosition4Broker;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.PositionDelivery;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.domain.Physical.Square4Broker;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.Position4BrokerParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PremiumType;
import com.smm.ctrm.util.Result.SpotType;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class Position4BrokerServiceImpl implements Position4BrokerService {

	private static final Logger logger = Logger.getLogger(Position4BrokerServiceImpl.class);

	@Autowired
	private HibernateRepository<Position4Broker> position4BrokerRepo;

	@Autowired
	private HibernateRepository<Position> positionRepository;

	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<Square> squareRepository;

	@Autowired
	private HibernateRepository<Square4Broker> Square4BrokerRepo;
	
	@Autowired
	private HibernateRepository<Storage> storageRepository;
	
	@Autowired
	private HibernateRepository<Lot> lotRepository;
	
	@Autowired
	private HibernateRepository<Contract> contractRepository;
	
	@Autowired
	private HibernateRepository<PositionDelivery> posDeliveryRepository;
	
	@Autowired
	private HibernateRepository<Legal> legalRepository;
	
	@Autowired
	private HibernateRepository<Commodity> commoditylHRepos;
	
	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipHRepos;
	
	@Autowired
	private HibernateRepository<Warehouse> warehouseHRepos;
	
	@Autowired
	private HibernateRepository<Customer> customerHRepos;
	

	@Autowired
	private PositionService positionService;

	@Autowired
	private CommonService commonService;
	
	@Autowired
	private ReceiptService receiptService;
	
	@Autowired
	private ContractService contractService;
	
	private final static String S="卖";

	@Override
	public ActionResult<String> Square() {

		ActionResult<String> result = new ActionResult<>();

		this.commonService.Square();

		result.setSuccess(true);
		result.setMessage("对齐完成");

		return result;
	}

	@Override
	public Criteria GetCriteria() {

		return this.position4BrokerRepo.CreateCriteria(Position4Broker.class);
	}

	public ActionResult<String> Save2(Position4Broker position) {

		if (position == null || StringUtils.isEmpty(position.getMarketId()))
			throw new RuntimeException(" position is null");
		Market market = this.marketRepository.getOneById(position.getMarketId(), Market.class);
		if (market == null)
			throw new RuntimeException("market is null where id:" + position.getMarketId());

		position.setCurrency(market.getCurrency());

		if (!StringUtils.isEmpty(position.getId())) {
			DetachedCriteria where = DetachedCriteria.forClass(Square4Broker.class);
			where.add(Restrictions.or(Restrictions.eq("LongId", position.getId()),
					Restrictions.eq("ShortId", position.getId())));
			List<Square4Broker> temp_list = this.Square4BrokerRepo.GetQueryable(Square4Broker.class).where(where)
					.toList();

			if (temp_list != null && temp_list.size() > 0) {
				return new ActionResult<>(false, "该头寸全部或者部分已经对齐，不允许修改");
			}
		} else {
			position.setQuantityUnSquared(position.getQuantity());
		}
		position.setOurRef(position.getIsVirtual() ? position.getOurRef() + "(v)" : position.getOurRef());
		this.position4BrokerRepo.SaveOrUpdate(position);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> Save(Position4Broker position) {
		if (position.getMarketId() == null) {
			return new ActionResult<>(Boolean.FALSE, "Param is missing: MarketId");
		}

		List<Position> positionList = positionRepository.GetQueryable(Position.class).where(
				DetachedCriteria.forClass(Position.class).add(Restrictions.eq("Position4BrokerId", position.getId())))
				.toList();

		for (Position p : positionList) {
			if (StringUtils.isNotBlank(p.getId()) && StringUtils.isNotBlank(p.getLotId())) {
				return new ActionResult<>(Boolean.FALSE, "已关联到批次，不允许修改");
			}
		}

		if (StringUtils.isNotBlank(position.getId()) && positionList.size() > 1) {
			return new ActionResult<>(Boolean.FALSE, "已关联多个头寸，不允许修改！");
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
					return new ActionResult<>(Boolean.FALSE, "该头寸全部或者部分已经对齐，不允许修改。");
				}

				// 数量处理，判断是否数量变更
				if (position.getQuantity().compareTo(position.getQuantityBeforeChanged()) != 0) {
					BigDecimal diff = position.getQuantity().subtract(position.getQuantityBeforeChanged());
					position.setQuantityOriginal(position.getQuantityOriginal().add(diff));

					position.setQuantityUnSquared(
							position.getQuantity().abs().subtract(getSquareSumQuantity(squareList))
									.multiply(new BigDecimal(position.getQuantity().compareTo(BigDecimal.ZERO))));
					// position 为被拆分的头寸
					List<Position4Broker> splitsList = position4BrokerRepo.GetQueryable(Position4Broker.class)
							.where(DetachedCriteria.forClass(Position4Broker.class)
									.add(Property.forName("SourceId").eq(position.getId()))
									.add(Property.forName("IsSplitted").eq(Boolean.TRUE)))
							.toList();
					for (Position4Broker split : splitsList) {
						List<Square> sumList = squareRepository.GetQueryable(Square.class)
								.where(DetachedCriteria.forClass(Square.class)
										.add(Restrictions.disjunction()
												.add(Property.forName("LongId").eq(split.getId())).add(
														Property.forName("ShortId").eq(split.getId()))))
								.toList();
						split.setQuantityUnSquared(split.getQuantity().abs().subtract(getSquareSumQuantity(sumList))
								.multiply(new BigDecimal(split.getQuantity().compareTo(BigDecimal.ZERO))));
						split.setQuantityOriginal(position.getQuantityOriginal());
						position4BrokerRepo.SaveOrUpdate(split);
					}
					// positon 为拆分出来的头寸
					Position4Broker source = position4BrokerRepo.GetQueryable(Position4Broker.class)
							.where(DetachedCriteria.forClass(Position4Broker.class)
									.add(Property.forName("Id").eq(position.getSourceId())))
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
						position4BrokerRepo.SaveOrUpdate(source);
					}
				}
			} else {
				position.setQuantityUnSquared(position.getQuantity());
				position.setQuantityOriginal(position.getQuantity());
			}

			if (position.getIsVirtual() && StringUtils.isBlank(position.getId())) { // 虚拟头寸交易编号(修改时不需要重复增加）
				if (position.getIsCarry()) {
					Position4Broker carry = position4BrokerRepo.GetQueryable(Position4Broker.class)
							.where(DetachedCriteria.forClass(Position4Broker.class)
									.add(Property.forName("Id").eq(position.getCarryPositionId())))
							.firstOrDefault();
					position.setOurRef((carry != null ? carry.getOurRef() : position.getOurRef()) + "(vc"
							+ position.getLS().toLowerCase() + ")");
				} else {
					position.setOurRef(position.getOurRef() + "(v)"); // 普通虚拟头寸
				}
			} else {
				// 普通调期头寸
				if (position.getIsCarry() && StringUtils.isBlank(position.getId())) {
					Position4Broker carry = position4BrokerRepo.GetQueryable(Position4Broker.class)
							.where(DetachedCriteria.forClass(Position4Broker.class)
									.add(Property.forName("Id").eq(position.getCarryPositionId())))
							.firstOrDefault();
					position.setOurRef((carry != null ? carry.getOurRef() : position.getOurRef()) + "(c"
							+ position.getLS().toLowerCase() + ")");
				}
			}
			// 交易编号重复性判断
			if (position.getIsSourceOfCarry()) {
				// 被调期头寸
				Position4Broker ref = position4BrokerRepo.GetQueryable(Position4Broker.class).where(DetachedCriteria
						.forClass(Position4Broker.class)
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
				Position4Broker ref = position4BrokerRepo.GetQueryable(Position4Broker.class)
						.where(DetachedCriteria.forClass(Position4Broker.class)
								.add(Restrictions.conjunction(Property.forName("Id").neOrIsNotNull(position.getId()),
										Restrictions.disjunction(Property.forName("OurRef").eq(position.getOurRef()),
												Property.forName("CarryRef").eq(position.getOurRef())))))
						.firstOrDefault();
				if (ref != null) {
					return new ActionResult<>(Boolean.FALSE, "交易编号已经存在1");
				}
			}
			position4BrokerRepo.SaveOrUpdate(position);
			if (positionList.size() > 0) {
				copy4BrokerToPosition(position, positionList.get(0));
			}

			// ActionResult<String> brokerOk = SavePosition2Broker(position);//
			// 同步生成Broker头寸明细
			// if (!brokerOk.isSuccess()) {
			// return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess +
			// "\r\n提示：" + brokerOk.getMessage());
			// }
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
		} catch (Exception e) {
			logger.error(e);
			return new ActionResult<>(Boolean.FALSE, e.getMessage());
		}
	}

	private void copy4BrokerToPosition(Position4Broker broker, Position position) {
		position.setIsVirtual(broker.getIsVirtual());
		position.setOurRef(broker.getOurRef());
		position.setTradeDate(broker.getTradeDate());
		position.setTraderId(broker.getTraderId());
		position.setBrokerId(broker.getBrokerId());
		position.setMarketId(broker.getMarketId());
		position.setCommodityId(broker.getCommodityId());
		position.setForwardType(broker.getForwardType());
		position.setQuantity(broker.getQuantity());
		position.setPurpose(broker.getPurpose());
		position.setLS(broker.getLS());
		position.setOurPrice(broker.getOurPrice());
		position.setTradePrice(broker.getTradePrice());
		position.setCarryDiffPrice(broker.getCarryDiffPrice());
		position.setTradeFee(broker.getTradeFee());
		position.setPromptDate(broker.getPromptDate());
		position.setComments(broker.getComments());
		position.setInstrumentId(broker.getInstrumentId());
		position.setOCS(broker.getOCS());
		position.setLegalId(broker.getLegalId());
		position.setPositionType(broker.getPositionType());
		position.setHands(broker.getHands());
		positionRepository.SaveOrUpdate(position);
	}

	private BigDecimal getSquareSumQuantity(List<Square> list) {
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Square square : list) {
			sumQuantity.add(square.getQuantity());
		}
		return sumQuantity;
	}

	@Override
	@Transactional
	public ActionResult<String> Delete(String id) {

		Position4Broker broker = this.position4BrokerRepo.getOneById(id, Position4Broker.class);
		List<Position> positionList = positionRepository.GetQueryable(Position.class).where(
				DetachedCriteria.forClass(Position.class).add(Restrictions.eq("Position4BrokerId", broker.getId())))
				.toList();
		if (positionList.size() > 1) {
			return new ActionResult<>(false, "已拆分的头寸，不允许删除。");
		}
		for (Position p : positionList) {
			if (StringUtils.isNotBlank(p.getLotId())) {
				return new ActionResult<>(false, "已关联批次，不允许删除。");
			}
		}

		DetachedCriteria where = DetachedCriteria.forClass(Position4Broker.class);
		// where.add(Restrictions.eq("SourceId", broker.getId()));
		// List<Position4Broker> temp_list =
		// this.position4BrokerRepo.GetQueryable(Position4Broker.class).where(where)
		// .toList();
		//
		// if (temp_list != null && temp_list.size() > 0)
		// throw new Exception("请先删除被拆分出来的记录");

		where = DetachedCriteria.forClass(Square4Broker.class);
		where.add(
				Restrictions.or(Restrictions.eq("LongId", broker.getId()), Restrictions.eq("ShortId", broker.getId())));

		List<Square4Broker> temp_list_2 = this.Square4BrokerRepo.GetQueryable(Square4Broker.class).where(where)
				.toList();

		if (temp_list_2 != null && temp_list_2.size() > 0)
			throw new RuntimeException("该头寸全部或者部分已经对齐，不允许修改");

		// -------------

		// logger.info("-------------------sourceid:"+
		// position.getSourceId());

		Position4Broker source = null;

		if (StringUtils.isNotBlank(broker.getSourceId())) {
			source = this.position4BrokerRepo.getOneById(broker.getSourceId(), Position4Broker.class);
			if (source != null) {
				source.setQuantity(source.getQuantity().subtract(broker.getQuantity()));
				this.position4BrokerRepo.SaveOrUpdate(source);
			}
		}

		// 如果删除的是被调期头寸，必须先删除原调期头寸
		where = DetachedCriteria.forClass(Position4Broker.class);
		where.add(Restrictions.eq("CarryPositionId", id));
		// List<Position4Broker> tempList =
		// this.position4BrokerRepo.GetList(Position4Broker.class);

		List<Position4Broker> tempList = this.position4BrokerRepo.GetQueryable(Position4Broker.class).where(where)
				.toList();

		if (tempList != null && tempList.size() > 0) {

			// if (tempList.stream().filter(p ->
			// p.getCarryPositionId().equals(id)).collect(Collectors.toList()).size()
			// > 0) {
			//
			// return new ActionResult<>(false, "该头寸已经被调期，请先删除调期头寸");
			// }

			return new ActionResult<>(false, "该头寸已经被调期，请先删除调期头寸");
		}

		// 如果删除的是调期头寸，并且允许删除，则必须成对删除
		/*
		 * if (broker.getIsCarry()) { // step 1: 找到来源的头寸是哪一个
		 * 
		 * Position4Broker CarrySource =
		 * this.position4BrokerRepo.getOneById(broker.getCarryPositionId(),
		 * Position4Broker.class);
		 * 
		 * where = DetachedCriteria.forClass(Position4Broker.class);
		 * where.add(Restrictions.eq("CarryCounterpart",
		 * broker.getCarryCounterpart()));
		 * where.add(Restrictions.eq("IsSourceOfCarry", true));
		 * List<Position4Broker> tempList2 =
		 * this.position4BrokerRepo.GetQueryable(Position4Broker.class)
		 * .where(where).toList();
		 * 
		 * if (tempList2 != null && tempList2.size() > 0) { return new
		 * ActionResult<>(false, "该头寸已经被调期，请先删除调期头寸1");
		 * 
		 * }
		 * 
		 * // step 2: 删除自身、和另一个对手头寸，都是调期的头寸 // String sql = String.format( //
		 * "Delete from [Physical].Position where IsCarry =1 and //
		 * CarryCounterPart = '{0}'", // position.getCarryCounterpart());
		 * 
		 * String sql =
		 * "Delete from [Physical].Position where IsCarry =1 and CarryCounterPart = '"
		 * + broker.getCarryCounterpart() + "'";
		 * 
		 * this.position4BrokerRepo.ExecuteNonQuery(sql);
		 * 
		 * // step 3: 同时更新来源头寸的标志 CarrySource.setIsSourceOfCarry(false);
		 * 
		 * this.position4BrokerRepo.SaveOrUpdate(CarrySource);
		 * 
		 * return new ActionResult<>(true, "删除成功"); }
		 */

		// this.position4BrokerRepo.PhysicsDelete(id, Position4Broker.class);
		if (positionList.size() > 0) {
			positionService.Delete(positionList.get(0).getId());
		}
		return new ActionResult<>(true, "删除成功");
	}

	/**
	 * 逻辑说明： 1. 拆分出来的数量必须小于原数量 2. 如果头寸已经被用于保值，则不允许拆分
	 * 
	 * @param cpSplitPosition
	 * @param userId
	 */
	@Override
	public final ActionResult<String> SplitPosition(CpSplitPosition4Broker cpSplitPosition, String userId) {
		// User user = getRepository().<User>Load(userId);
		BigDecimal qtySplit = cpSplitPosition.getQuantitySplitted();
		Position4Broker position = cpSplitPosition.getOriginalPosition();

		// 数据格式检查
		if (position == null || position.getIsSquared() || position.getIsAccounted()) {
			return new ActionResult<>(Boolean.FALSE, "不符合拆分的条件：记录为空、或已结算、或已会计。");
		}

		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(BigDecimal.ZERO) < 0)
				|| (position.getQuantity().compareTo(BigDecimal.ZERO) < 0 && qtySplit.compareTo(BigDecimal.ZERO) > 0)) {
			return new ActionResult<>(false, "拆分的数量与原数量的符号应保持相同。");
		}

		if ((position.getQuantity().compareTo(BigDecimal.ZERO) > 0 && qtySplit.compareTo(position.getQuantity()) >= 0)
				|| (position.getQuantity().compareTo(BigDecimal.ZERO) < 0
						&& qtySplit.compareTo(position.getQuantity()) <= 0)) {
			return new ActionResult<>(false, "拆分的数量必须小于原数量。");
		}
		try {
			position.setQuantity(position.getQuantity().subtract(qtySplit));
			position4BrokerRepo.SaveOrUpdate(position);

			// 构建拆分后的记录，并保存
			// 即使是已经用于保值的头寸，也是允许拆分的
			Position4Broker theSplit = new Position4Broker();
			BeanUtils.copyProperties(position, theSplit);
			theSplit.setId(null);
			theSplit.setQuantity(qtySplit);
			theSplit.setIsSplitted(true);
			theSplit.setOurRef(position.getOurRef() + ("(s)"));
			theSplit.setSourceId((position.getSourceId() != null) ? position.getSourceId() : position.getId());
			position4BrokerRepo.SaveOrUpdate(theSplit);
		} catch (RuntimeException ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
		return new ActionResult<>(true, "拆分成功。");
	}

	@Override
	public ActionResult<Position4Broker> GetById(String positionId) {

		ActionResult<Position4Broker> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.position4BrokerRepo.getOneById(positionId, Position4Broker.class));

		return result;
	}

	@Override
	public ActionResult<String> GenerateBrokerPosition() {

		ActionResult<String> result = new ActionResult<>();

		this.commonService.GenerateBrokerPosition();

		result.setSuccess(true);
		result.setMessage("生成成功");

		return result;
	}

	@Override
	public List<Position4Broker> Positions(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {

		return (List<Position4Broker>) this.position4BrokerRepo
				.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	@Override
	public ActionResult<String> posDelivery(Position4BrokerParams pos4bParams) {
		
		
		
		Position4Broker pBroker=pos4bParams.getPosition4Broker();
		
		PositionDelivery pDelivery=pos4bParams.getPositionDelivery();
		
		LoginInfoToken userInfo=LoginHelper.GetLoginInfo();
		
		/**
		 * 根据交割价和数量生成对应平仓头寸
		 */
		Position position=new Position();
		position.setTradeDate(new Date());
		position.setQuantity(pDelivery.getDeliveryQuantity());
		position.setQuantityOriginal(pDelivery.getDeliveryQuantity());
		position.setPremium(pDelivery.getPremium());
		position.setLS(pBroker.getLS());
		position.setHands(pBroker.getHands());
		position.setOurPrice(pDelivery.getDeliveryPrice());
		position.setTradePrice(pDelivery.getDeliveryPrice());
		position.setCurrency(pBroker.getCurrency());
		position.setOCS("S");
		position.setTraderId(userInfo.getUserId());
		position.setMarketId(pBroker.getMarketId());
		position.setMarketCode(pBroker.getMarketCode());
		position.setCommodityId(pBroker.getCommodityId());
		position.setBrokerId(pBroker.getBrokerId());
		position.setLegalId(pBroker.getLegalId());
		String pId=this.positionRepository.SaveOrUpdateRetrunId(position);
		String lotId="";
		String headNo="";
		//卖头寸
		if(pBroker.getLS().equals(S)){
			
			List<Storage> storageList=pos4bParams.getStorageList();
			/**
			 * 以此客户、交割价、升贴水和数量生成固定价销售订单
			 */
			Contract contract=new Contract();
			contract.setPrice(pDelivery.getDeliveryPrice());
			contract.setPremium(pDelivery.getPremium());
			contract.setPremiumType(PremiumType.Fix);
			contract.setMajorType(MajorType.Fix);
			contract.setMajorMarketId(pBroker.getMarketId());
			//contract.setGradeSetIds();品级ID
			//contract.setBrandIds();
			contract.setBrandNames(pBroker.getBrandNames());
			contract.setLegalId(pBroker.getLegalId());
			contract.setLegalCode(pBroker.getLegalCode());
			contract.setIsPriced(true);
			contract.setIsIniatiated(true);
			//设置合同号
			String maxSerialNo = contractService
					.GetMaxSerialNo(pBroker.getLegalId(), SpotType.Purchase, new Date(), pBroker.getCommodityId()).getData();
			Legal legal = this.legalRepository.getOneById(pBroker.getLegalId(), Legal.class);
			Commodity commodity = this.commoditylHRepos.getOneById(pBroker.getCommodityId(), Commodity.class);
			String prefix = DateUtil.doFormatDate(new Date(), "yy");
			contract.setPrefix(SpotType.Purchase + commodity.getCode() + legal.getCode() + prefix);
			contract.setSerialNo(String.valueOf(maxSerialNo));
			contract.setHeadNo(contract.getPrefix() + maxSerialNo);
			headNo=contract.getHeadNo();
			contract.setQuantity(pDelivery.getDeliveryQuantity());
			contract.setCurrency(pBroker.getCurrency());
			contract.setSpotDirection("S");
			contract.setTradeDate(new Date());
			contract.setStatus(1);
			contract.setIsApproved(true);
			contract.setTraderId(userInfo.getUserId());
			contract.setCustomerId(pBroker.getCustomerId());
			contract.setCommodityId(pBroker.getCommodityId());
			contract.setCreatedId(userInfo.getUserId());
			
			String contractId=this.contractRepository.SaveOrUpdateRetrunId(contract);
			
			Lot lot=new Lot();
			lot.setPrice(pDelivery.getDeliveryPrice());
			lot.setPremium(pDelivery.getPremium());
			lot.setPremiumType(PremiumType.Fix);
			lot.setMajorType(MajorType.Fix);
			lot.setMajorMarketId(pBroker.getMarketId());
			lot.setQuantity(pDelivery.getDeliveryQuantity());
			lot.setCurrency(pBroker.getCurrency());
			lot.setSpotDirection("S");
			lot.setTradeDate(new Date());
			lot.setStatus(1);
			lot.setBrandNames(pBroker.getBrandNames());
			lot.setLegalId(pBroker.getLegalId());
			lot.setLegalCode(pBroker.getLegalCode());
			lot.setIsPriced(true);
			lot.setQuantityDelivered(pDelivery.getDeliveryQuantity());
			lot.setQuantityPriced(pDelivery.getDeliveryQuantity());
			lot.setQuantityHedged(pDelivery.getDeliveryQuantity());
			lot.setHedgedPrice(pDelivery.getDeliveryPrice());
			lot.setIsDelivered(true);
			lot.setIsHedged(true);
			lot.setIsPriced(true);
			lot.setCustomerId(pBroker.getCustomerId());
			lot.setCommodityId(pBroker.getCommodityId());
			lot.setCreatedId(userInfo.getUserId());
			lot.setIsSplitLot(false);
			lot.setIsOriginalLot(false);
			lot.setIsFunded(false);
			lot.setIsInvoiced(false);
			
			lotId=this.lotRepository.SaveOrUpdateRetrunId(lot);
			
			/**
			 * 使用上述生成的平仓头寸进行保值
			 */
			Position p=this.positionRepository.getOneById(pId, Position.class);
			p.setLotId(lotId);
			p.setContractId(contractId);
			this.positionRepository.SaveOrUpdate(p);
			Customer customer=this.customerHRepos.getOneById(pDelivery.getCustomerId(), Customer.class);
			for (Storage sl : storageList) {
				ReceiptShip rs=new ReceiptShip();
				rs.setCommodityId(pBroker.getCommodityId());
				rs.setContractId(contractId);
				rs.setCustomerId(pDelivery.getCustomerId());
				rs.setCustomerName(customer.getName());
				rs.setFlag(SpotType.Sell);
				rs.setIsApproved(true);
				rs.setLotId(lotId);
				rs.setReceiptShipDate(new Date());
				String no = this.receiptService.getNoRepeatReceiptNo(rs.getFlag());
				rs.setReceiptShipNo(no);
				rs.setSpecId(sl.getSpecId());
				rs.setWeight(pDelivery.getDeliveryQuantity());
				rs.setWhId(sl.getWarehouseId());
				Warehouse w=this.warehouseHRepos.getOneById(sl.getWarehouseId(), Warehouse.class);
				rs.setWhName(w.getName());
				rs.setWhOutEntryDate(new Date());
				rs.setCreatedId(userInfo.getUserId());
				String rsId=this.receiptShipHRepos.SaveOrUpdateRetrunId(rs);
				/**
				 * 同时发货
				 */
				Storage storage=new Storage();
				storage=com.smm.ctrm.util.BeanUtils.copy(sl);
				storage.setRefId(rsId);
				storage.setCreatedId(userInfo.getUserId());
				storage.setLotId(lotId);
				storage.setIsIn(true);
				storage.setTradeDate(new Date());
				storage.setMT("M");
				storage.setTransitStatus("NA");
				storage.setIsInvoiced(true);
				storage.setContractId(contractId);
				storage.setQuantityInvoiced(pDelivery.getDeliveryQuantity());
				storage.setAmount(pDelivery.getDeliveryQuantity().multiply(pDelivery.getDeliveryPrice()));
				storage.setIsBorrow(false);
				storage.setIsOut(true);
				storage.setCounterpartyId3(sl.getId());	
				String sId = this.storageRepository.SaveOrUpdateRetrunId(storage);
				sl.setCounterpartyId2(sId);
				sl.setIsOut(true);
				this.storageRepository.SaveOrUpdateRetrunId(sl);
				
			}
			
		}else{//买头寸
			/**
			 * 以此客户、交割价、升贴水和数量生成固定价采购订单
			 */
			Contract contract=new Contract();
			contract.setPrice(pDelivery.getDeliveryPrice());
			contract.setPremium(pDelivery.getPremium());
			contract.setPremiumType(PremiumType.Fix);
			contract.setMajorType(MajorType.Fix);
			contract.setMajorMarketId(pBroker.getMarketId());
			//contract.setGradeSetIds();品级ID
			//contract.setBrandIds();
			contract.setBrandNames(pBroker.getBrandNames());
			contract.setLegalId(pBroker.getLegalId());
			contract.setLegalCode(pBroker.getLegalCode());
			contract.setIsPriced(true);
			contract.setIsIniatiated(true);
			//设置合同号
			String maxSerialNo = contractService
					.GetMaxSerialNo(pBroker.getLegalId(), SpotType.Purchase, new Date(), pBroker.getCommodityId()).getData();
			Legal legal = this.legalRepository.getOneById(pBroker.getLegalId(), Legal.class);
			Commodity commodity = this.commoditylHRepos.getOneById(pBroker.getCommodityId(), Commodity.class);
			String prefix = DateUtil.doFormatDate(new Date(), "yy");
			contract.setPrefix(SpotType.Purchase + commodity.getCode() + legal.getCode() + prefix);
			contract.setSerialNo(String.valueOf(maxSerialNo));
			contract.setHeadNo(contract.getPrefix() + maxSerialNo);
			
			contract.setQuantity(pDelivery.getDeliveryQuantity());
			contract.setCurrency(pBroker.getCurrency());
			contract.setSpotDirection(SpotType.Purchase);
			contract.setTradeDate(new Date());
			contract.setStatus(1);
			contract.setIsApproved(true);
			contract.setTraderId(userInfo.getUserId());
			contract.setCustomerId(pBroker.getCustomerId());
			contract.setCommodityId(pBroker.getCommodityId());
			contract.setCreatedId(userInfo.getUserId());
			
			String contractId=this.contractRepository.SaveOrUpdateRetrunId(contract);
			
			Lot lot=new Lot();
			lot.setPrice(pDelivery.getDeliveryPrice());
			lot.setPremium(pDelivery.getPremium());
			lot.setPremiumType(PremiumType.Fix);
			lot.setMajorType(MajorType.Fix);
			lot.setMajorMarketId(pBroker.getMarketId());
			lot.setQuantity(pDelivery.getDeliveryQuantity());
			lot.setCurrency(pBroker.getCurrency());
			lot.setSpotDirection("B");
			lot.setTradeDate(new Date());
			lot.setStatus(1);
			lot.setBrandNames(pBroker.getBrandNames());
			lot.setLegalId(pBroker.getLegalId());
			lot.setLegalCode(pBroker.getLegalCode());
			lot.setIsPriced(true);
			lot.setQuantityDelivered(pDelivery.getDeliveryQuantity());
			lot.setQuantityPriced(pDelivery.getDeliveryQuantity());
			lot.setQuantityHedged(pDelivery.getDeliveryQuantity());
			lot.setHedgedPrice(pDelivery.getDeliveryPrice());
			lot.setIsDelivered(true);
			lot.setIsHedged(true);
			lot.setIsPriced(true);
			lot.setCustomerId(pBroker.getCustomerId());
			lot.setCommodityId(pBroker.getCommodityId());
			lot.setCreatedId(userInfo.getUserId());
			lot.setContractId(contractId);
			lot.setIsSplitLot(false);
			lot.setIsOriginalLot(false);
			lot.setIsFunded(false);
			lot.setIsInvoiced(false);
			lotId=this.lotRepository.SaveOrUpdateRetrunId(lot);
			
			/**
			 * 收货、同时生成交付明细（仓库使用输入信息）
			 */
			ReceiptShip rs=new ReceiptShip();
			rs.setCommodityId(pBroker.getCommodityId());
			rs.setContractId(contractId);
			rs.setCustomerId(pDelivery.getCustomerId());
			Customer customer=this.customerHRepos.getOneById(pDelivery.getCustomerId(), Customer.class);
			rs.setCustomerName(customer.getName());
			rs.setFlag(SpotType.Sell);
			rs.setIsApproved(true);
			rs.setLotId(lotId);
			rs.setReceiptShipDate(new Date());
			String no = this.receiptService.getNoRepeatReceiptNo(rs.getFlag());
			rs.setReceiptShipNo(no);
			//rs.setSpecId(curLot.getSpecId());
			rs.setWeight(pDelivery.getDeliveryQuantity());
			rs.setWhId(pDelivery.getWareHouseId());
			Warehouse w=this.warehouseHRepos.getOneById(pDelivery.getWareHouseId(), Warehouse.class);
			rs.setWhName(w.getName());
			rs.setWhOutEntryDate(new Date());
			rs.setCreatedId(userInfo.getUserId());
			String rsId=this.receiptShipHRepos.SaveOrUpdateRetrunId(rs);
			
			
			Storage storage=new Storage();
			storage.setRefId(rsId);
			storage.setCreatedId(userInfo.getUserId());
			storage.setLotId(lotId);
			storage.setIsIn(true);
			storage.setTradeDate(new Date());
			storage.setMT("M");
			storage.setTransitStatus("NA");
			storage.setIsInvoiced(true);
			storage.setContractId(contractId);
			storage.setQuantityInvoiced(pDelivery.getDeliveryQuantity());
			storage.setAmount(pDelivery.getDeliveryQuantity().multiply(pDelivery.getDeliveryPrice()));
			storage.setIsBorrow(false);
			storage.setIsOut(true);
			String sId = this.storageRepository.SaveOrUpdateRetrunId(storage);
			
		}
		
		/**
		 * 交割价、数量、时间汇总到头寸相应字段中
		 */
		
		DetachedCriteria dc=DetachedCriteria.forClass(PositionDelivery.class);
		dc.add(Restrictions.eq("PositionId", pBroker.getId()));
		List<PositionDelivery> pdLists=this.posDeliveryRepository.GetQueryable(PositionDelivery.class).where(dc).toList();
		
		if(pdLists!=null&&pdLists.size()>0){
			//交割价格加权平均
			BigDecimal total=BigDecimal.ZERO;
			for (PositionDelivery p : pdLists) {
				total=total.add(p.getDeliveryPrice());
			}
			total=total.add(pDelivery.getDeliveryPrice());
			pBroker.setDeliveryPrice(DecimalUtil.divideForPrice(total,new BigDecimal(pdLists.size()+1)));
		}else{
			pBroker.setDeliveryPrice(pDelivery.getDeliveryPrice());
		}
		
		pBroker.setDeliveryQuantity(DecimalUtil.nullToZero(pBroker.getDeliveryQuantity()).add(pDelivery.getDeliveryQuantity()));
		pBroker.setLastDeliveryDate(new Date());
		
		this.position4BrokerRepo.SaveOrUpdate(pBroker);
		
		/**
		 * 头寸交割表中建立相应记录。
		 */
		
		pDelivery.setPosition4BrokerNo(pBroker.getOurRef());
		pDelivery.setPosition4BrokerId(pBroker.getId());
		pDelivery.setPositionId(pId);
		pDelivery.setLotId(lotId);
		pDelivery.setLotNo(headNo+"/10");
		pDelivery.setCreatedId(userInfo.getUserId());
		
		this.posDeliveryRepository.SaveOrUpdate(pDelivery);
		
		return new ActionResult<>(true,"头寸交割成功.");
	}
}
