package com.smm.ctrm.bo.impl.Report;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.SpotReportService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Physical.CLot;
import com.smm.ctrm.domain.Physical.CLot2;
import com.smm.ctrm.domain.Physical.CStorage;
import com.smm.ctrm.domain.Physical.CarryPosition;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.DailyPosition;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Lot4MTM3;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.Square4Broker;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Report.EstUnHedge;
import com.smm.ctrm.domain.Report.Lot4MTM;
import com.smm.ctrm.domain.Report.LotPnl;
import com.smm.ctrm.domain.Report.ModelStorage;
import com.smm.ctrm.domain.Report.PositionApprove;
import com.smm.ctrm.domain.Report.PositionDetail;
import com.smm.ctrm.domain.Report.R1;
import com.smm.ctrm.domain.Report.R11;
import com.smm.ctrm.domain.Report.R12;
import com.smm.ctrm.domain.Report.R13;
import com.smm.ctrm.domain.Report.R14;
import com.smm.ctrm.domain.Report.R2;
import com.smm.ctrm.domain.Report.R3;
import com.smm.ctrm.domain.Report.R5;
import com.smm.ctrm.domain.Report.R7;
import com.smm.ctrm.domain.Report.R8;
import com.smm.ctrm.domain.Report.SettleDaily;
import com.smm.ctrm.domain.Report.TradeReport;
import com.smm.ctrm.domain.Report.TradeReportDetailBuy;
import com.smm.ctrm.domain.Report.TradeReportDetailSale;
import com.smm.ctrm.domain.Report.TradeReportDetailSaleAndBuy;
import com.smm.ctrm.domain.apiClient.LotParams;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.domain.apiClient.StorageParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.KeyObj;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MT4Storage;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.SpotType;

/**
 * Created by zhenghao on 2016/4/21.
 *
 */
@Service
public class SpotReportServiceImpl implements SpotReportService {

	private static Logger logger = Logger.getLogger(SpotReportServiceImpl.class);

	@Autowired
	private HibernateRepository<Position> positionRepository;
	
	@Autowired
	private HibernateRepository<R3> r3Repository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;
	
	@Autowired
	private HibernateRepository<CLot> clotRepository;
	
	@Autowired
	private HibernateRepository<R5> r5Repository;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepository;
	
	@Autowired
	private HibernateRepository<R7> r7Repository;

	@Autowired
	private HibernateRepository<Position4Broker> position4BrokerRepository;

	@Autowired
	private HibernateRepository<Square4Broker> square4BrokerRepository;

	@Autowired
	private HibernateRepository<Reuter> reuterRepository;

	@Autowired
	private HibernateRepository<ModelStorage> modelStorageRepository;

	@Autowired
	private HibernateRepository<Storage> storageRepository;
	@Autowired
	private HibernateRepository<CStorage> cstorageRepository;
	@Autowired
	private HibernateRepository<Legal> legalRepository;

	@Autowired
	private HibernateRepository<Brand> brandRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityRepository;

	@Autowired
	private HibernateRepository<DailyPosition> dailyPositionvRepository;

	@Autowired
	private HibernateRepository<SettleDaily> settleDailyRepository;

	@Autowired
	private HibernateRepository<Broker> brokerRepository;
	
	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private CommonService commonService;

	private HibernateRepository<Pricing> pricingRepository;	

	@Override
	public ActionResult<List<R1>> R1Report() {

		List<Lot> lots = lotRepository.GetQueryable(Lot.class)
				.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("SpotDirection", SpotType.Purchase))
						.add(Restrictions.eq("IsPriced", Boolean.TRUE))
						.add(Restrictions.eq("IsInvoiced", Boolean.FALSE)))
				.toList();

		List<Market> markets = new ArrayList<Market>();
		//去除重复市场编码，提供一次性查处市场
		List<String> marketCodes= new ArrayList<String>();
		//存放市场对应的品种集合
		Map<String,List<String>> market2Comm =new HashMap<String,List<String>>();
		
		//抽取批次用到的市场 和 品种
		for(Lot lot:lots){
			String marketCode = (lot.getCurrency().equals("USD") ? "LME" : "SFE").toLowerCase();
			 List<String> collectMarketCode= marketCodes.stream().filter(c->c.equalsIgnoreCase(marketCode)).collect(Collectors.toList());
			 if(collectMarketCode.size() > 0){
				 List<String> listMap=market2Comm.get(marketCode);
				 List<String> collectListMap =listMap.stream().filter(c->c.equalsIgnoreCase(lot.getCommodityId())).collect(Collectors.toList());
				 if(collectListMap == null || collectListMap.size() < 1)
					 market2Comm.get(marketCode).add(lot.getCommodityId());
				 continue;
			 } 
			 marketCodes.add(marketCode);
			 
			 List<String> listComm =new ArrayList<String>();
			 listComm.add(lot.getCommodityId());
			 market2Comm.put(marketCode,listComm);
		}
		
		//查询所有市场
		Criteria criteria = this.marketRepository.CreateCriteria(Market.class);
		criteria.add(Restrictions.in("Code", marketCodes));
		List<Market> ms = this.marketRepository.GetList(criteria);
		if(ms!=null && ms.size() > 0)
			markets.addAll(ms);
		
		//所有市场的价格数据
		Map<String,List<?>> lot2Prices =commonService.GetLotM2mPrice(market2Comm);
		
		for (Lot lot : lots) {
			String marketCode = lot.getCurrency().equals("USD") ? "LME" : "SFE";
			lot.setQuantityUnInvoiced((lot.getIsDelivered()
					? ((lot.getQuantityDelivered() != null) ? lot.getQuantityDelivered() : BigDecimal.ZERO)
					: lot.getQuantity()).subtract(
							(lot.getQuantityInvoiced() != null) ? lot.getQuantityInvoiced() : BigDecimal.ZERO));
			lot.setPrice(
					((lot.getPrice() != null) ? lot.getPrice() : BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN));
			lot.setM2MPrice(commonService.GetM2MPrice2(marketCode, lot.getCommodityId(), lot.getSpecId(),markets,lot2Prices));
			lot.setPriceDiff((lot.getPrice().subtract(lot.getM2MPrice()).setScale(2, RoundingMode.HALF_EVEN)));
			lot.setExposure(lot.getQuantityUnInvoiced().multiply(lot.getPriceDiff()).negate().setScale(2,
					RoundingMode.HALF_EVEN));
			if (lot.getCustomer() != null) {
				lot.setCustomerName(lot.getCustomer().getName());
			} else {
				lot.setCustomerName("test");
			}
		}
		Map<String, R1> map = new HashMap<>();
		for (Lot lot : lots) {
			String comeStr = lot.getCustomerName() + "," + lot.getCustomerId();
			if (map.keySet().contains(comeStr)) {
				map.get(comeStr).setExposure(map.get(comeStr).getExposure()
						.add(lot.getExposure() == null ? BigDecimal.ZERO : lot.getExposure()));
			} else {
				R1 r1 = new R1();
				r1.setCustomerId(lot.getCustomerId());
				r1.setCustomerName(lot.getCustomerName());
				r1.setExposure(lot.getExposure() == null ? BigDecimal.ZERO : lot.getExposure());
				map.put(comeStr, r1);
			}
		}
		List<R1> r1S = new ArrayList<>();
		for (Entry<String, R1> entry : map.entrySet()) {
			R1 r1 = entry.getValue();
			r1.setLots(lots.stream().filter(w -> w.getCustomerId().equals(r1.getCustomerId()))
					.collect(Collectors.toList()));
			r1S.add(r1);
		}
		return new ActionResult<>(Boolean.TRUE, "", r1S);
	}

	@Override
	public ActionResult<java.util.List<R2>> R2Report() {
		return null;
	}

	@Override
	public ActionResult<List<R3>> R3Report() {
		List<R3> positions = this.r3Repository.GetList(R3.class);

		for (R3 position : positions) {

			if (position.getBroker() != null) {
				position.setBrokerName(position.getBroker().getName());
			}

			if (position.getCommodity() != null) {
				position.setCommodityName(position.getCommodity().getName());
				position.setCommodityUnit(position.getCommodity().getUnit());
			}

			if (position.getMarket() != null) {
				position.setMarketName(position.getMarket().getName());
			}
		}
		return new ActionResult<>(Boolean.TRUE, "", positions);
	}

	public ActionResult<List<R3>> R4Report() {
		List<R3> positions = this.r3Repository.GetList(R3.class);

		for (R3 position : positions) {

			if (position.getContract() != null) {
				position.setContractHeadNo(position.getContract().getHeadNo());
				position.setContract(null);
			} else if (position.getLot() != null) {
				position.setContractHeadNo(position.getLot().getFullNo());
				position.setLot(null);
			}

			if (position.getBroker() != null) {
				position.setBrokerName(position.getBroker().getName());
				position.setBroker(null);
			}

			if (position.getCommodity() != null) {
				position.setCommodityName(position.getCommodity().getName());
				position.setCommodityUnit(position.getCommodity().getUnit());
				position.setCommodity(null);
			}

			if (position.getMarket() != null) {
				position.setMarketName(position.getMarket().getName());
				position.setMarket(null);
			}
		}
		return new ActionResult<>(Boolean.TRUE, "", positions);
	}

	@Override
	public ActionResult<List<R5>> R5Report() {		
		DetachedCriteria dc = DetachedCriteria.forClass(R5.class);
		dc.add(Restrictions.eq("MajorType", MajorType.Average));
		dc.add(Restrictions.eq("IsHidden",false));
		List<R5> lots = this.r5Repository.GetQueryable(R5.class).where(dc).toList();
		for (R5 lot : lots) {
			
			if (lot.getCommodity() != null) {
				lot.setCommodityCode(lot.getCommodity().getCode());
				lot.setCommodity(null);
			}

			if (lot.getContract() != null) {
				Contract contract = lot.getContract();
				lot.setContractHeadNo(contract.getHeadNo());
				if (contract.getCustomer() != null) {
					lot.setCustomerName(contract.getCustomer().getName());
					lot.setContract(null);
				}
			}
		}
		return new ActionResult<>(Boolean.TRUE, "", lots);
	}

	@Override
	public ActionResult<List<R5>> R6Report() {
		DetachedCriteria dc=DetachedCriteria.forClass(R5.class);
		dc.add(Restrictions.eq("IsPriced", false));
		List<R5> lots = this.r5Repository.GetQueryable(R5.class).where(dc).toList();
		for (R5 lot : lots) {

			if (lot.getCommodity() != null) {
				lot.setCommodityCode(lot.getCommodity().getCode());
				lot.setCommodity(null);
			}

			if (lot.getContract() != null) {
				Contract contract = lot.getContract();
				lot.setContractHeadNo(contract.getHeadNo());
				if (contract.getCustomer() != null) {
					lot.setCustomerName(contract.getCustomer().getName());
					lot.setContract(null);
				}
			}
		}
		return new ActionResult<>(Boolean.TRUE, "", lots);
	}

	@Override
	public ActionResult<List<R7>> R7Report() {
		List<R7> invoices = this.r7Repository.GetList(R7.class);
		for (R7 invoice : invoices) {
			if (invoice.getLot() != null) {
				invoice.setLotFullNo(invoice.getLot().getFullNo());
				invoice.setLot(null);
			}
		}
		return new ActionResult<>(Boolean.TRUE, "", invoices);
	}

	public ActionResult<List<R8>> R8Report() {
		List<R7> invoices = this.r7Repository.GetList(R7.class);
		
		List<R8> r8list = new  ArrayList<>();
		for (R7 invoice : invoices) {
			R8 r8=new R8();
			if (invoice.getLot() != null) {
				invoice.setLotFullNo(invoice.getLot().getFullNo());
				invoice.setLotQuantity(invoice.getLot().getQuantity());
				invoice.setLotQP(invoice.getLot().getQP());
				invoice.setLotQuantityPriced(invoice.getLot().getQuantityPriced());
				invoice.setLot(null);
			}
			if (invoice.getCustomer() != null) {
				invoice.setCustomerName(invoice.getCustomer().getName());
				invoice.setCustomer(null);
			}
			r8.setLotFullNo(invoice.getLotFullNo());
			r8.setLotQuantity(invoice.getLotQuantity());
			r8.setLotQuantityPriced(invoice.getLotQuantityPriced());
			r8.setLotQP(invoice.getLotQP());
			r8.setCustomerName(invoice.getCustomerName());
			
			if(r8list.size()==0){
				r8list.add(r8);
			}else{
				boolean flag=true;
				for (R8 r : r8list) {
					if(r.getLotFullNo()!=null&&r8.getLotFullNo()!=null&&r.getLotFullNo().equalsIgnoreCase(r8.getLotFullNo())){
						flag=false;
					}
				}
				if(flag){
					r8list.add(r8);	
				}
			}
		}
		for (R8 v : r8list) {
			v.setR7s(invoices.stream().filter(w ->w.getLotFullNo()!=null&&v.getLotFullNo()!=null&&w.getLotFullNo().equals(v.getLotFullNo())).collect(Collectors.toList()));
		}
		
		return new ActionResult<>(Boolean.TRUE, "", r8list);
	}

	/**
	 * 返回存在点价或保值敞口的现货列表
	 * 
	 * @return
	 */
	@Override
	public List<R11> R11Report() {
		List<String> arrsB = new ArrayList<>();
		arrsB.add("B");
		List<String> arrsS = new ArrayList<>();
		arrsS.add("S");
		DetachedCriteria dc=DetachedCriteria.forClass(CLot.class);
		dc.addOrder(Order.desc("CreatedAt"));
		List<CLot> wholelots = this.clotRepository.GetQueryable(CLot.class).where(dc).toList();
		
		/*List<CLot> splitlots = wholelots.stream().filter(x -> x.getIsSplitted() == Boolean.TRUE)
				.collect(Collectors.toList());*/
		
		List<CLot> fatherlots =wholelots;
				//wholelots.stream().filter(x -> x.getIsSplitted() == Boolean.FALSE)
				//.collect(Collectors.toList());
		
		/*for (CLot lot1 : fatherlots) {
			for (CLot lot : splitlots) {
				if (lot1.getId().equals(lot.getSplitFromId())) {
					lot1.setQuantityDelivered(lot1.getQuantityDelivered().add(lot.getQuantityDelivered()));
					lot1.setQuantity(
							(lot1.getQuantityOriginal() != null) ? lot1.getQuantityOriginal() : lot1.getQuantity());
					if (lot1.getIsDelivered() && lot.getIsDelivered()) {
						lot1.setIsDelivered(true);
					} else {
						lot1.setIsDelivered(false);
					}

				}
			}

		}*/

		List<R11> tmpList = new ArrayList<>();

		for (CLot lot : fatherlots) {
			
			if (lot.getQP() != null && arrsS.contains(lot.getSpotDirection()) && lot.getIsDelivered()== true
					&& (DecimalUtil.nullToZero(lot.getQuantityDelivered()).compareTo(DecimalUtil.nullToZero(lot.getQuantityPriced())) != 0
							|| DecimalUtil.nullToZero(lot.getQuantityDelivered()).compareTo(DecimalUtil.nullToZero(lot.getQuantityHedged())) != 0)) {
				R11 r = getR11(lot, subtract(lot.getQuantityDelivered(), lot.getQuantityPriced()),
						subtract(lot.getQuantityDelivered(), lot.getQuantityHedged()));
				tmpList.add(r);
			}

			if (lot.getQP() != null && arrsB.contains(lot.getSpotDirection()) && lot.getIsDelivered()== true
					&& (DecimalUtil.nullToZero(lot.getQuantityDelivered()).compareTo(DecimalUtil.nullToZero(lot.getQuantityPriced())) != 0
							|| DecimalUtil.nullToZero(lot.getQuantityDelivered()).compareTo(DecimalUtil.nullToZero(lot.getQuantityHedged()).negate()) != 0)) {
				R11 r = getR11(lot, subtract(lot.getQuantityDelivered(), lot.getQuantityPriced()),
						add(lot.getQuantityDelivered(), lot.getQuantityHedged()));
				tmpList.add(r);
			}
			if (lot.getQP() != null && arrsS.contains(lot.getSpotDirection()) && lot.getIsDelivered()== false
					&& (DecimalUtil.nullToZero(lot.getQuantity()).compareTo(DecimalUtil.nullToZero(lot.getQuantityPriced())) != 0
							|| DecimalUtil.nullToZero(lot.getQuantity()).compareTo(DecimalUtil.nullToZero(lot.getQuantityHedged())) != 0)) {
				R11 r = getR11(lot, subtract(lot.getQuantity(), lot.getQuantityPriced()),
						subtract(lot.getQuantity(), lot.getQuantityHedged()));
				tmpList.add(r);
			}

			if (lot.getQP() != null && arrsB.contains(lot.getSpotDirection()) && lot.getIsDelivered()== false
					&& (DecimalUtil.nullToZero(lot.getQuantity()).compareTo(DecimalUtil.nullToZero(lot.getQuantityPriced())) != 0
							|| DecimalUtil.nullToZero(lot.getQuantity()).compareTo(DecimalUtil.nullToZero(lot.getQuantityHedged()).negate()) != 0)) {
				R11 r = getR11(lot, add(lot.getQuantity(), lot.getQuantityPriced()),
						subtract(lot.getQuantity(), lot.getQuantityHedged()));
				tmpList.add(r);
			}
		}
		/*tmpList.sort(new Comparator<R11>() {
			public int compare(R11 o1, R11 o2) {
				return o1.getFullNo().compareTo(o2.getFullNo());
			};
		});*/
		return tmpList;
	}

	private BigDecimal add(BigDecimal left, BigDecimal right) {
		return (left == null ? BigDecimal.ZERO : left).add(right == null ? BigDecimal.ZERO : right);
	}

	private BigDecimal subtract(BigDecimal left, BigDecimal right) {
		return (left == null ? BigDecimal.ZERO : left).subtract(right == null ? BigDecimal.ZERO : right);
	}

	private R11 getR11(CLot lot, BigDecimal QuantityPricedDiff, BigDecimal QuantityHedgedDiff) {

		R11 r = new R11();
		r.setFullNo(lot.getFullNo());
		r.setQuantity(DecimalUtil.nullToZero(lot.getQuantity()).setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityDelivered(DecimalUtil.nullToZero(lot.getQuantityDelivered()).setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityPriced(DecimalUtil.nullToZero(lot.getQuantityPriced()).setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityPricedDiff(QuantityPricedDiff.setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityHedged(DecimalUtil.nullToZero(lot.getQuantityHedged()).setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityHedgedDiff(QuantityHedgedDiff.setScale(3, RoundingMode.HALF_EVEN));
		r.setQuantityDiff(DecimalUtil.nullToZero(lot.getQuantity()).subtract(DecimalUtil.nullToZero(lot.getQuantityDelivered())));
		r.setCreatedAt(lot.getCreatedAt());
		return r;
	}

	/**
	 * 报表：保值日期与预计销售日期不一致的批次列表
	 * 
	 * @return
	 */
	public List<R13> R13Report() {
		List<Lot> lots = lotRepository.GetQueryable(Lot.class)
				.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("SpotDirection", "B")))
				.OrderBy(Order.asc("FullNo")).toList();
		List<R13> result = new ArrayList<R13>();

		for (Lot lot : lots) {

			R13 r = new R13();
			r.setFullNo(lot.getFullNo());
			r.setQuantity(lot.getQuantity());
			r.setEstimateSaleDate(lot.getEstimateSaleDate());
			r.setQP(lot.getQP());

			// #region 净头寸
			DetachedCriteria where = DetachedCriteria.forClass(Position.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<Position> positions = positionRepository.GetQueryable(Position.class).where(where).toList();

			Map<List<Object>, List<Position>> groupMap = positions.stream().collect(Collectors
					.groupingBy(p -> new KeyObj(p.getMarketId(), p.getCommodityId(), p.getPromptDate()).getKeys()));

			List<Position> query = new ArrayList<>();

			for (List<Object> key : groupMap.keySet()) {

				Position p = new Position();
				p.setMarketId((String) key.get(0));
				p.setCommodityId((String) key.get(1));
				p.setPromptDate((Date) key.get(2));
				p.setQuantity(new BigDecimal(
						groupMap.get(key).stream().mapToDouble(t -> t.getQuantity().doubleValue()).sum()));

				query.add(p);
			}

			// #region 各种情况判断

			if (lot.getEstimateSaleDate() == null) {
				List<Date> tmpList = query.stream().filter(p -> !p.getQuantity().equals(BigDecimal.ZERO))
						.map(Position::getPromptDate).distinct().collect(Collectors.toList());

				for (Date p : tmpList) {
					r.setPromptDate(r.getPromptDate() + DateUtil.doFormatDate(p, "yyyy/MM/dd"));
				}
				result.add(r);
				continue;
			}

			List<Date> pList = query.stream()
					.filter(p -> !p.getPromptDate().equals(lot.getEstimateSaleDate())
							&& !p.getQuantity().equals(BigDecimal.ZERO))
					.map(Position::getPromptDate).distinct().collect(Collectors.toList());

			if (pList.size() == 0) {
				result.add(r);
				continue;
			}

			for (Date p : pList) {
				r.setPromptDate(r.getPromptDate() + DateUtil.doFormatDate(p, "yyyy/MM/dd"));
			}
			result.add(r);
		}

		return result;
	}

	@Override
	public List<R14> R14Report() {
		List<Position> positions = positionRepository.GetList(Position.class);

		// 分组
		Map<List<Object>, List<Position>> groupMap = positions.stream().collect(
				Collectors.groupingBy(p -> new KeyObj(p.getMarketId(), p.getMarket().getName(), p.getCommodityId(),
						p.getCommodity().getName(), DateUtil.doFormatDate(p.getPromptDate(), "yyyy-MM-dd")).getKeys()));

		// 创建新集合
		List<R14> query = new ArrayList<>();

		for (List<Object> key : groupMap.keySet()) {
			Boolean b =((String) key.get(4)).equalsIgnoreCase("2016-05-17");
			if(b)
			{
				String s ="s";
				String a = s;
			}
			R14 r = new R14();
			r.setMarketId((String) key.get(0));
			r.setMarketName((String) key.get(1));
			r.setCommodityId((String) key.get(2));
			r.setCommodityCode((String) key.get(3));
			r.setPromptDate((String) key.get(4));

			r.setSP(BigDecimal.ZERO.subtract(new BigDecimal(groupMap.get(key).stream()
					.filter(xr -> !StringUtils.isEmpty(xr.getLotId()) && xr.getIsVirtual() && xr.getIsCarry())
					.mapToDouble(xr -> xr.getQuantity().doubleValue()).sum())));
			
			r.setMG(new BigDecimal(groupMap.get(key).stream().filter(xr -> xr.getLotId() == null)
					.mapToDouble(xr -> xr.getQuantity().doubleValue()).sum()));
			r.setHG(new BigDecimal(groupMap.get(key).stream().filter(xr -> xr.getLotId()!=null)
					.mapToDouble(xr -> xr.getQuantity().doubleValue()).sum()));

			/*
			r.setMG(new BigDecimal(groupMap.get(key).stream().filter(xr -> StringUtils.isAllLowerCase(xr.getLotId()))
					.mapToDouble(xr -> xr.getQuantity().doubleValue()).sum()));
			r.setHG(new BigDecimal(groupMap.get(key).stream().filter(xr -> !StringUtils.isAllLowerCase(xr.getLotId()))
					.mapToDouble(xr -> xr.getQuantity().doubleValue()).sum()));
					*/

			query.add(r);
		}

		List<R14> list = query
				.stream().sorted(
						Comparator.comparing(R14::getMarketId)
								.thenComparing(Comparator.comparing(R14::getCommodityId)
										.thenComparing(Comparator.comparing(R14::getPromptDate))))
				.collect(Collectors.toList());

		return list;
	}

	public List<R12> R12Report() {

		String[] arrs = new String[] { "B", "S" };

		// 批次信息
		DetachedCriteria where = DetachedCriteria.forClass(CLot.class);
		where.add(Restrictions.isNotNull("QP"));
		where.add(Restrictions.in("SpotDirection", arrs));
		where.add(Restrictions.or(Restrictions.eq("IsPriced", false), Restrictions.eq("IsHedged", false)));
		List<CLot> lots = this.clotRepository.GetQueryable(CLot.class).where(where).toList();

		// 净头寸，未关联批次
		//先不考虑没有关联批次的头寸   20160628
		/*where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.isNull("LotId"));
		List<Position> positions = positionRepository.GetQueryable(Position.class).where(where).toList();*/

		List<R12> r12List = new ArrayList<>();

		// #region 添加到期日
		Map<Date, List<CLot>> groupMap = lots.stream().collect(Collectors.groupingBy(CLot::getQP));
		List<Date> qpLots = new ArrayList<>();
		groupMap.keySet().forEach(qpLots::add);

		/*Map<Date, List<Position>> groupMap_temp = positions.stream()
				.collect(Collectors.groupingBy(Position::getPromptDate));
		List<Date> qpPositions = new ArrayList<>();
		groupMap_temp.keySet().forEach(qpPositions::add);*/

		for (Date qp : qpLots) {
			if (qp == null)
				continue;
			Date qp_format=DateUtil.doSFormatDate(qp, "yyyy-MM-dd");
			if (r12List.stream().allMatch(d -> !qp_format.equals(d.getQP()))) {
				R12 obj = new R12();
				obj.setQP(qp);
				r12List.add(obj);
			}
		}

		/*for (Date qp : qpPositions) {
			if (r12List.stream().filter(d -> d.getQP().equals(qp)).count() == 0) {
				R12 obj = new R12();
				obj.setQP(qp);
				r12List.add(obj);
			}
		}*/
		for (R12 r12 : r12List) {
			/**
			 * 计算各个字段数值
			 */
			if (r12 == null || !(r12.getQP()!=null)) continue;
			
            List<CLot> percLots=lots.stream().filter(x -> "B".equals(x.getSpotDirection()) && x.getQP()!=null && DateUtil.doFormatDate(x.getQP(), "yyyy-MM-dd").compareTo(DateUtil.doFormatDate(r12.getQP(),"yyyy-MM-dd"))==0).collect(Collectors.toList());
            
            List<CLot> sellLots=lots.stream().filter(x -> "S".equals(x.getSpotDirection()) && x.getQP()!=null && DateUtil.doFormatDate(x.getQP(), "yyyy-MM-dd").compareTo(DateUtil.doFormatDate(r12.getQP(),"yyyy-MM-dd"))==0).collect(Collectors.toList());

            BigDecimal p_sum_Quantity=BigDecimal.ZERO;
            BigDecimal p_sum_QuantityPriced=BigDecimal.ZERO;
            BigDecimal p_sum_QuantityHedged=BigDecimal.ZERO;
            
            
            for (CLot cLot : percLots) {
            	p_sum_Quantity=p_sum_Quantity.add(DecimalUtil.nullToZero(cLot.getQuantity()));
            	p_sum_QuantityPriced=p_sum_QuantityPriced.add(DecimalUtil.nullToZero(cLot.getQuantityPriced()));
            	p_sum_QuantityHedged=p_sum_QuantityHedged.add(DecimalUtil.nullToZero(cLot.getQuantityHedged()));
			}
            r12.setP_Quantity(p_sum_Quantity);
            r12.setP_Quantity(r12.getP_Quantity().setScale(2,BigDecimal.ROUND_HALF_UP));
            r12.setP_QuantityPriced(p_sum_QuantityPriced);
            r12.setP_QuantityPriced(DecimalUtil.nullToZero(r12.getP_QuantityPriced()).setScale(2,BigDecimal.ROUND_HALF_UP));
            r12.setP_QuantityHedged(p_sum_QuantityHedged);
            r12.setP_QuantityHedged(DecimalUtil.nullToZero(r12.getP_QuantityHedged()).setScale(2,BigDecimal.ROUND_HALF_UP));

            
            
            BigDecimal s_sum_Quantity=BigDecimal.ZERO;
            BigDecimal s_sum_QuantityPriced=BigDecimal.ZERO;
            BigDecimal s_sum_QuantityHedged=BigDecimal.ZERO;
            for (CLot x : sellLots) {
            	s_sum_Quantity=s_sum_Quantity.add(DecimalUtil.nullToZero(x.getQuantity()));
            	s_sum_QuantityPriced=s_sum_QuantityPriced.add(DecimalUtil.nullToZero(x.getQuantityPriced()));
            	s_sum_QuantityHedged=s_sum_QuantityHedged.add(DecimalUtil.nullToZero(x.getQuantityHedged()));
			}
            r12.setS_Quantity(s_sum_Quantity.multiply(new BigDecimal(-1)));
            r12.setS_Quantity(DecimalUtil.nullToZero(r12.getS_Quantity()).setScale(2,BigDecimal.ROUND_HALF_UP));
            r12.setS_QuantityPriced(s_sum_QuantityPriced.multiply(new BigDecimal(-1)));
            r12.setS_QuantityPriced(DecimalUtil.nullToZero(r12.getS_QuantityPriced()).setScale(2,BigDecimal.ROUND_HALF_UP));
            r12.setS_QuantityHedged(s_sum_QuantityHedged.multiply(new BigDecimal(-1)));
            r12.setS_QuantityHedged(DecimalUtil.nullToZero(r12.getS_QuantityHedged()).setScale(2,BigDecimal.ROUND_HALF_UP));

            r12.setP_DiffHedged(r12.getP_Quantity().add(DecimalUtil.nullToZero(r12.getP_QuantityHedged())));
            r12.setP_DiffPriced(r12.getP_Quantity().subtract(DecimalUtil.nullToZero(r12.getP_QuantityPriced())));

            r12.setS_DiffHedged(r12.getS_Quantity().add(DecimalUtil.nullToZero(r12.getS_QuantityHedged())).multiply(new BigDecimal(-1)));
            r12.setS_DiffPriced(r12.getS_Quantity().subtract(DecimalUtil.nullToZero(r12.getS_QuantityPriced())).multiply(new BigDecimal(-1)));

            r12.setCleanHedged(DecimalUtil.nullToZero(r12.getP_QuantityHedged()).add(DecimalUtil.nullToZero(r12.getS_QuantityHedged())));
            r12.setQuantityPriced(r12.getP_QuantityPriced().add(r12.getS_QuantityPriced())); //点价数量
            r12.setExposureQuantity(r12.getQuantityPriced().add(r12.getCleanHedged())); //敞口数
		}
		
		return r12List.stream().sorted((p1,p2)->p2.getQP().compareTo(p1.getQP())).collect(Collectors.toList());
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<PositionApprove> PositionApproveReport(java.util.Date dtEnd, String brokerIds) {
		
		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.isNotNull("LotId"))
		.add(Restrictions.eq("IsVirtual", Boolean.FALSE))
		.add(Restrictions.eq("TradeDate", dtEnd));
		if(brokerIds != null){
			where.add(Restrictions.eq("BrokerId", brokerIds));
		}
		where.addOrder(Order.asc("PromptDate"));
		
		List<Position> positions = positionRepository.GetQueryable(Position.class)
				.where(where).toList();

		List<PositionApprove> positionApprove = new ArrayList<>();
		for (Position x : positions) {
			PositionApprove pa = new PositionApprove();
			pa.setOurRef(x.getOurRef());
			pa.setETDMonth(x.getLot().getETD() == null ? null : (x.getLot().getETD().getMonth() + 1) + "月");
			pa.setBrandNames(x.getLot().getBrandNames());
			pa.setLotQuantity(x.getLot().getQuantityOriginal() == null ? x.getLot().getQuantity()
					: x.getLot().getQuantityOriginal());
			pa.setQuantity(x.getQuantity().abs());
			pa.setPlanPositionQuantity(x.getQuantity().abs());
			pa.setPricePositionQuantity(x.getQuantity().abs());
			pa.setPositionQuantity(x.getQuantity().abs());
			pa.setPromptDate(DateUtil.doFormatDate(x.getPromptDate(), "yyyy-MM-dd"));
			pa.setFullNoOfLot(x.getLot().getFullNo());
			pa.setFullNoOfCounterparty(x.getLot().getCustomer().getName());
			pa.setBrokerNames(x.getBroker().getName());
			pa.setSpotDirection(x.getLot().getSpotDirection());
			pa.setIsComplete("√");
			positionApprove.add(pa);
		}
		for (PositionApprove approve : positionApprove) {
			if (approve.getSpotDirection().contains("S")) {
				approve.setPlanPositionQuantity(null);
			} else {
				approve.setPricePositionQuantity(null);
				approve.setPositionQuantity(null);
			}
			approve.setQuantity(converToThreeHalfUpDecimal(approve.getQuantity()));
			approve.setLotQuantity(converToThreeHalfUpDecimal(approve.getLotQuantity()));
			if (approve.getPlanPositionQuantity() != null) {
				approve.setPlanPositionQuantity(converToThreeHalfUpDecimal(approve.getPlanPositionQuantity()));
			}
			if (approve.getPricePositionQuantity() != null) {
				approve.setPricePositionQuantity(converToThreeHalfUpDecimal(approve.getPricePositionQuantity()));
			}
			if (approve.getPositionQuantity() != null) {
				approve.setPositionQuantity(converToThreeHalfUpDecimal(approve.getPositionQuantity()));
			}

		}
		return positionApprove;
	}

	private BigDecimal converToThreeHalfUpDecimal(BigDecimal bg) {
		return (bg == null ? BigDecimal.ZERO : bg).setScale(3, RoundingMode.HALF_EVEN);
	}

	private BigDecimal converToTwoHalfUpDecimal(BigDecimal bg) {
		return (bg == null ? BigDecimal.ZERO : bg).setScale(2, RoundingMode.HALF_EVEN);
	}

	@Override
	public List<PositionDetail> PositionDetailReport(java.util.Date dtEnd, String brokerIds) {

		Criterion se = null;
		if (brokerIds == null) {
			se = Restrictions.isNotNull("Id");
		} else {
			se = Restrictions.isNull("Id");
		}
		List<Position4Broker> position4Broker = position4BrokerRepository.GetQueryable(Position4Broker.class)
				.where(DetachedCriteria.forClass(Position4Broker.class).add(Restrictions.eq("IsVirtual", Boolean.FALSE))
						.add(Restrictions.eq("TradeDate", dtEnd))
						.add(Restrictions.or(Restrictions.eq("BrokerId", brokerIds), se))
						.addOrder(Order.asc("PromptDate")))
				.toList();

		List<Position> positionsSplite = positionRepository.GetQueryable(Position.class)
				.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("IsVirtual", Boolean.FALSE))
						.add(Restrictions.isNotNull("LotId")).add(Restrictions.eq("IsSplitted", Boolean.TRUE)))
				.toList();

		for (Position4Broker p4b : position4Broker) {
			p4b.setBrandNames("");
			if (p4b.getLot() != null) {
				p4b.setBrandNames(p4b.getLot().getBrandNames());
			}
			List<Position> tempPositons = positionsSplite.stream()
					.filter(x -> x.getSourceId().equals(p4b.getOriginalId())).collect(Collectors.toList());
			if (tempPositons != null) {
				for (Position t : tempPositons) {
					String[] names = t.getLot().getBrandNames().split("[,]", -1);
					for (String name : names) {
						if (!p4b.getBrandNames().contains(name)) {
							if (StringUtils.isBlank(p4b.getBrandNames())) {
								p4b.setBrandNames(name);
							} else {
								p4b.setBrandNames(p4b.getBrandNames() + "," + name);
							}
						}
					}
				}
			}
		}

		List<Square4Broker> square4BrokerList = square4BrokerRepository.GetQueryable(Square4Broker.class)
				.OrderBy(Order.asc("PromptDate")).toList();
		List<PositionDetail> positionDetailList = new ArrayList<>();
		for (Position4Broker p : position4Broker) {
			PositionDetail pd = new PositionDetail();
			pd.setId(p.getId());
			pd.setPromptDate(DateUtil.doFormatDate(p.getPromptDate(), "yyyy-MM-dd"));
			pd.setLS(p.getLS());
			pd.setPositionQuantity(p.getQuantity().abs());
			pd.setTradePrice(p.getTradePrice());
			pd.setCarryDiffPrice(p.getCarryDiffPrice());
			pd.setTradeFee(p.getTradeFee());
			pd.setOurPirce(p.getOurPrice());
			pd.setBrandNames(p.getBrandNames());
			pd.setOurRef(p.getOurRef());
			pd.setBrokerNames(p.getBroker().getName());
			positionDetailList.add(pd);
		}

		for (PositionDetail detail : positionDetailList) {

			if (detail.getLS().contains("L")) {
				detail.setLS("买入");
				detail.setWareouseStatus("平仓");
				detail.setTakePositionRatio(null);
				detail.setDiffOfPriceAndPosition("0");
				Square4Broker square = square4BrokerList.stream().filter(x -> x.getLongId().equals(detail.getId())).findFirst().orElse(null);
				if (square != null) {
					detail.setPnL(square.getPnL());
				}
			} else {
				detail.setLS("卖出");
				detail.setWareouseStatus("开仓");
				detail.setTakePositionRatio("100%");
				detail.setDiffOfPriceAndPosition(null);
				detail.setPnL(null);
			}
			if (detail.getPositionQuantity() != null) {
				detail.setPositionQuantity(converToThreeHalfUpDecimal(detail.getPositionQuantity()));
			}
			if (detail.getTradePrice() != null) {
				detail.setTradePrice(converToTwoHalfUpDecimal(detail.getTradePrice()));
			}
			if (detail.getCarryDiffPrice() != null) {
				detail.setCarryDiffPrice(converToTwoHalfUpDecimal(detail.getCarryDiffPrice()));
			}
			if (detail.getTradeFee() != null) {
				detail.setTradeFee(converToTwoHalfUpDecimal(detail.getTradeFee()));
			}
			if (detail.getPnL() != null) {
				detail.setPnL(converToTwoHalfUpDecimal(detail.getPnL()));
			}
		}
		return positionDetailList;
	}

	@Override
	public List<DailyPosition> DailyPositionReportSearch(Date dtEnd, String brokerIds) {

		//List<Reuter> reuter = reuterRepository.GetList(Reuter.class);
		//List<Reuter> reuter = reuterRepository.GetQueryable(Reuter.class).toList();
		
		DetachedCriteria where = DetachedCriteria.forClass(Position4Broker.class);
		where.add(Restrictions.eq("IsVirtual", false));
		if(brokerIds!=null)
			where.add(Restrictions.eq("BrokerId", brokerIds));
		List<Position4Broker> position4Broker = position4BrokerRepository.GetQueryable(Position4Broker.class)
				.where(where).toList();

		// ---------------------
		
		Map<List<Object>, List<Position4Broker>> groupMap = position4Broker.stream()
				.filter(x -> x.getTradeDate().compareTo(dtEnd)<=0 && !x.getQuantity().equals(BigDecimal.ZERO))
				.collect(Collectors.groupingBy(
						x -> new KeyObj(x.getMarketId(), x.getCommodityId(), x.getBrokerId(),
								x.getMarket()==null ? null :x.getMarket().getName(),
								x.getCommodity()==null?null:x.getCommodity().getName(),
								x.getPromptDate() == null?null:DateUtil.doSFormatDate(x.getPromptDate(),"yyyy-MM-dd"), 
								x.getBroker()==null?null:x.getBroker().getName()).getKeys()));
		
		
		/*
		Map<List<Object>, List<Position4Broker>> groupMap = position4Broker.stream()
				.filter(x -> x.getTradeDate().before(dtEnd) && !x.getQuantity().equals(BigDecimal.ZERO))
				.collect(Collectors.groupingBy(
						x -> new KeyObj(x.getMarketId(), x.getCommodityId(), x.getBrokerId(), x.getMarket().getName(),
								x.getCommodity().getName(), x.getPromptDate(), x.getBroker().getName()).getKeys()));
		*/
		
		List<DailyPosition> daily = new ArrayList<>();
		for (List<Object> key : groupMap.keySet()) {

			DailyPosition d = new DailyPosition();

			d.setMarketId((String) key.get(0));
			d.setCommodityId((String) key.get(1));
			d.setBrokerId((String) key.get(2));
			d.setMarketName((String) key.get(3));
			d.setCommodityName((String) key.get(4));

			d.setPromptDate((Date) key.get(5));
			d.setBrokerNames((String) key.get(6));

			d.setEndLineDate(dtEnd==null?new Date():dtEnd);
			d.setConsultQuantity(
					new BigDecimal(groupMap.get(key).stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			d.setPositionPrice(new BigDecimal(groupMap.get(key).stream()
					.mapToDouble(x -> x.getQuantity().multiply(x.getOurPrice()).doubleValue()).sum()));

			daily.add(d);
		}
		
		daily =daily.stream()
				.sorted(Comparator.comparing(DailyPosition::getBrokerNames)
						.thenComparing(Comparator.comparing(DailyPosition::getPromptDate)))
				.collect(Collectors.toList());
		
		List<DailyPosition> daily1 = new ArrayList<>();
		//daily.OrderBy(c)
		for (DailyPosition d : daily) {

			if (!d.getConsultQuantity().equals(BigDecimal.ZERO) && d.getPositionPrice() != null){
				d.setPositionPrice(DecimalUtil.divideForPrice(d.getPositionPrice(), d.getConsultQuantity()));
				//d.setPositionPrice(d.getPositionPrice().divide(d.getConsultQuantity()));
			}	
			else {
				d.setPositionPrice(null);
			}

			DetachedCriteria reuterWhere = DetachedCriteria.forClass(Reuter.class);
			reuterWhere.add(Restrictions.eq("TradeDate", d.getEndLineDate()));
			reuterWhere.add(Restrictions.eq("PromptDate", d.getPromptDate()));
			Reuter reuter1 = reuterRepository.GetQueryable(Reuter.class).where(reuterWhere).firstOrDefault();
			
			/**
			Reuter reuter1 = reuter.stream().filter(
					x -> x.getTradeDate().equals(d.getEndLineDate()) && x.getPromptDate().equals(d.getPromptDate()))
					.findFirst().get();
					**/

			if (reuter1 != null) {

				d.setBalancePrice(reuter1.getPrice());
			}

			BigDecimal absOfPositionPrice = DecimalUtil.nullToZero(d.getPositionPrice()).abs();
			if(DecimalUtil.nullToZero(d.getBalancePrice()).compareTo(BigDecimal.ZERO) != 0)
				d.setFloatPnl(absOfPositionPrice.subtract(DecimalUtil.nullToZero(d.getBalancePrice())).multiply(d.getConsultQuantity().abs()));
			d.setSEndLineDate(DateUtil.doFormatDate(d.getEndLineDate(), "yyyy-MM-dd"));
			d.setSPromptDate(DateUtil.doFormatDate(d.getPromptDate(), "yyyy-MM-dd"));

			if (DecimalUtil.nullToZero(d.getPositionPrice()).compareTo(BigDecimal.ZERO) != 0 
					&& DecimalUtil.nullToZero(d.getBalancePrice()).compareTo(BigDecimal.ZERO) != 0 ) {
				d.setFloatPnlRatio(
						DecimalUtil.divideForPrice(
								absOfPositionPrice.subtract(
										DecimalUtil.nullToZero(d.getBalancePrice())
								),DecimalUtil.nullToZero(absOfPositionPrice))
						.multiply(new BigDecimal(100)));
				/* d.setFloatPnlRatio(absOfPositionPrice.subtract(d.getBalancePrice()).divide(absOfPositionPrice)
						.multiply(new BigDecimal(100))); */
			} else {

				d.setFloatPnlRatio(null);
			}

			if (d.getPositionPrice() != null)
				d.setPositionPrice(DecimalUtil.nullToZero(d.getPositionPrice()).setScale(2, RoundingMode.HALF_EVEN));
			if (d.getBalancePrice() != null)
				d.setBalancePrice(DecimalUtil.nullToZero(d.getBalancePrice()).setScale(2, RoundingMode.HALF_EVEN));
			if (d.getFloatPnl() != null)
				d.setFloatPnl(DecimalUtil.nullToZero(d.getFloatPnl()).setScale(2, RoundingMode.HALF_EVEN));
			if (d.getFloatPnlRatio() != null)
				d.setFloatPnlRatio(DecimalUtil.nullToZero(d.getFloatPnlRatio()).setScale(2, RoundingMode.HALF_EVEN));
			if (!d.getConsultQuantity().equals(BigDecimal.ZERO))
				daily1.add(d);

		}

		// 按类型进行合计计算
		List<Broker> brokers = brokerRepository.GetList(Broker.class);
		List<DailyPosition> consults = new ArrayList<>();

		for (Broker b : brokers) {
			DailyPosition consult = new DailyPosition();
			List<DailyPosition> temp = daily1.stream().filter(x -> x.getBrokerId().equals(b.getId()))
					.collect(Collectors.toList());

			if (temp.size() > 0) {
				consult.setBrokerNames(b.getName() + "合计");
				consult.setConsultQuantity(
						new BigDecimal(temp.stream().mapToDouble(x -> x.getConsultQuantity().doubleValue()).sum()));
				if (consult.getConsultQuantity().equals(BigDecimal.ZERO))
					consult.setPositionPrice(null);
				else {
					BigDecimal temp_sum = new BigDecimal(temp.stream()
							.mapToDouble(x -> x.getConsultQuantity().multiply(x.getPositionPrice()).doubleValue())
							.sum());
					
					consult.setPositionPrice(DecimalUtil.divideForQuantity(temp_sum, consult.getConsultQuantity()).abs());
				}
				List<DailyPosition> floatPnlNull= temp.stream().filter(c->c.getFloatPnl() == null).collect(Collectors.toList());
				if(floatPnlNull!=null && floatPnlNull.size() == 0){
					consult.setFloatPnl(
							new BigDecimal(temp.stream().mapToDouble(x -> x.getFloatPnl().doubleValue()).sum()));
				}
				else
					consult.setFloatPnl(null);

				consults.add(consult);
			}
		}

		daily1.addAll(consults);
		// 此处的排序会影响合计的出现位置，轻易不要修改
		List<DailyPosition> daily2 = daily1.stream()
				.sorted(Comparator.comparing(DailyPosition::getBrokerNames)
						.thenComparing(Comparator.comparing(DailyPosition::getPromptDate)))
				.collect(Collectors.toList());

		return daily2;

	}

	public ActionResult<List<DailyPosition>> DailyPositionReportSave(List<DailyPosition> daily) {
		try {
			DailyPosition tempdaily = daily.get(0);
			if (daily.size() != 0) {
				List<Position> dailyPosition = positionRepository.GetQueryable(Position.class).where(DetachedCriteria
						.forClass(Position.class).add(Restrictions.eq("EndLineDate", tempdaily.getEndLineDate())))
						.toList();
				for (Position d : dailyPosition) {
					positionRepository.PhysicsDelete(d);
				}
				for (DailyPosition d : daily) {
					dailyPositionvRepository.SaveOrUpdate(d);

				}
			}
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);

		} catch (RuntimeException ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	public List<EstUnHedge> EstUnHedgeReport(java.util.Date startTime, java.util.Date endTime) {
		List<EstUnHedge> result = new ArrayList<>();
		Legal sm = legalRepository.GetQueryable(Legal.class)
				.where(DetachedCriteria.forClass(Legal.class).add(Restrictions.eq("Code", "SM"))).firstOrDefault();
		DetachedCriteria dca = DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("IsSplitted", Boolean.FALSE))
				.add(Restrictions.eq("SpotDirection", "B")).add(Restrictions.eq("LegalId", sm.getId()));
		if (startTime != null) {
			dca.add(Restrictions.or(Restrictions.gt("QP", startTime)));
		}
		if (endTime != null) {
			dca.add(Restrictions.lt("QP", endTime));
		}
		List<Lot> lots = lotRepository.GetQueryable(Lot.class).where(dca).toList();
		if (lots.size() == 0) {
			return null;
		}
		for (Lot lot : lots) {

			List<Storage> storages = storageRepository.GetQueryable(Storage.class)
					.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId())))
					.toList();

			List<Position> positions = positionRepository.GetQueryable(Position.class)
					.where(DetachedCriteria.forClass(Position.class).add(Restrictions.eq("LotId", lot.getId())))
					.toList();
			if (storages.size() > 0) {

				List<Position> carryPositions = positions.stream().filter(x -> x.getIsCarry() == Boolean.TRUE)
						.collect(Collectors.toList());

				List<CarryPosition> lstCarry = new ArrayList<>();
				if (carryPositions.size() > 0) {
					lstCarry = commonService.GetCarryPositions(carryPositions);
				}
				// 根据交付明细找到对手方，并记录跟交明细相的采批和销售批次的信息
				for (Storage storage : storages) {
					EstUnHedge estUnHedge = new EstUnHedge();
					if (storage.getCounterpartyId() != null) {
						// 得到对手方
						Storage counterStorage = storageRepository.GetQueryable(Storage.class).where(DetachedCriteria
								.forClass(Storage.class).add(Restrictions.eq("Id", storage.getCounterpartyId())))
								.firstOrDefault();
						if (counterStorage != null && counterStorage.getLotId() != null
								&& counterStorage.getContractId() != null) {
							Lot counterLot = lotRepository.GetQueryable(Lot.class).where(DetachedCriteria
									.forClass(Lot.class).add(Restrictions.eq("Id", counterStorage.getLotId())))
									.firstOrDefault();
							// 记录销售的保值信息
							if (counterLot != null) {

								CarryPosition lotCarry = lstCarry.stream()
										.filter(x -> x.getPromptDate4Long() == lot.getEstimateSaleDate()
												&& x.getPromptDate4Short() == counterLot.getQP()
												&& x.getQuantity().compareTo(storage.getQuantity()) == 0)
										.findAny().get();
								if (lotCarry == null) {
									lotCarry = lstCarry.stream()
											.filter(x -> x.getPromptDate4Long() == lot.getEstimateSaleDate()
													&& x.getPromptDate4Short() == counterLot.getQP())
											.findAny().get();
								}
								if (lotCarry != null) {
									lstCarry.remove(lotCarry);
									estUnHedge.setPositionQuantity((lotCarry.getQuantity() != null)
											? lotCarry.getQuantity() : BigDecimal.ZERO);
								}
								estUnHedge.setSellFullNo(counterLot.getFullNo());
								estUnHedge.setBuyFullNo(lot.getFullNo());
								if (counterLot.getQP() != null) {
									estUnHedge.setRealSellDate(DateUtil.doFormatDate(counterLot.getQP(), "yyyy-MM-dd"));
								}
								if (lot.getEstimateSaleDate() != null) {
									estUnHedge.setEstSellDate(
											DateUtil.doFormatDate(counterLot.getEstimateSaleDate(), "yyyy-MM-dd"));
								}
								estUnHedge.setLotQuantity(counterLot.getQuantity());
								estUnHedge.setStorageQuantity(storage.getQuantity());
								if (estUnHedge.getStorageQuantity().compareTo(estUnHedge.getPositionQuantity()) == 0) {
									estUnHedge.setIsHedged(Boolean.TRUE);
								}
								result.add(estUnHedge);
							}
						}
					}
				}
			}
		}
		Map<ArrayList<Object>, List<EstUnHedge>> groupMap = result.stream()
				.filter(x -> x.getIsHedged() == Boolean.FALSE)
				.sorted((x1, x2) -> x1.getBuyFullNo().compareTo(x2.getBuyFullNo()))
				.collect(Collectors.groupingBy(x -> new KeyObj(x.getBuyFullNo(), x.getSellFullNo(), x.getEstSellDate(),
						x.getRealSellDate(), x.getLotQuantity(), x.getIsHedged()).getKeys()));

		List<EstUnHedge> result1 = new ArrayList<>();

		for (List<Object> keyList : groupMap.keySet()) {
			EstUnHedge obj = new EstUnHedge();
			obj.setBuyFullNo((String) keyList.get(0));
			obj.setSellFullNo((String) keyList.get(1));
			obj.setEstSellDate((String) keyList.get(2));
			obj.setRealSellDate((String) keyList.get(3));
			obj.setLotQuantity((BigDecimal) keyList.get(4));
			obj.setIsHedged((Boolean) keyList.get(5));
			obj.setStorageQuantity(new BigDecimal(
					groupMap.get(keyList).stream().mapToDouble(x -> x.getStorageQuantity().doubleValue()).sum()));
			obj.setPositionQuantity(new BigDecimal(
					groupMap.get(keyList).stream().mapToDouble(x -> x.getPositionQuantity().doubleValue()).sum()));
			result1.add(obj);
		}

		for (EstUnHedge r : result1) {
			if (r.getStorageQuantity().compareTo(r.getPositionQuantity()) == 0) {
				r.setIsHedged(true);
			}
			r.setStorageQuantity(converToTwoHalfUpDecimal(r.getStorageQuantity()));
		}
		return result1.stream().filter(x -> x.getIsHedged() == false).collect(Collectors.toList());
	}

	/**
	 * 交易报告
	 * 
	 */
	@SuppressWarnings("deprecation")
	public TradeReport TradeReport(Date dtStart, Date dtEnd, String BrandId, String LegalId, String CommodityId,
			String ProductName) {
		if (BrandId == null || LegalId == null || CommodityId == null || dtStart == null || dtEnd == null) {
			return null;
		}

		TradeReport report = new TradeReport();

		Brand brand = brandRepository.GetQueryable(Brand.class)
				.where(DetachedCriteria.forClass(Brand.class).add(Restrictions.eq("Id", BrandId))).firstOrDefault();
		Legal legal = legalRepository.GetQueryable(Legal.class)
				.where(DetachedCriteria.forClass(Legal.class).add(Restrictions.eq("Id", LegalId))).firstOrDefault();
		Commodity commodity = commodityRepository.GetQueryable(Commodity.class)
				.where(DetachedCriteria.forClass(Commodity.class).add(Restrictions.eq("Id", CommodityId)))
				.firstOrDefault();

		// 得到前期库存
		Date LastDate = new Date(dtStart.getTime());
		LastDate.setDate(LastDate.getDate() - 1);
		SettleDaily Daily = settleDailyRepository.GetQueryable(SettleDaily.class)
				.where(DetachedCriteria.forClass(SettleDaily.class).add(Restrictions.eq("TradeDate", LastDate))
						.add(Restrictions.eq("BrandId", BrandId)).add(Restrictions.eq("LegalId", LegalId))
						.add(Restrictions.eq("CommodityId", CommodityId)))
				.firstOrDefault();
		if (Daily == null) {
			// 期初库存(重新计算）
			Daily = GetSettleDailyInit(new Date(dtStart.getTime()), new Date(dtEnd.getTime()), BrandId, legal,
					CommodityId);
		}
		report.setSettleDailyInit(Daily);
		// 得到本期出入库发生
		report.setCommodityName(commodity == null ? "" : commodity.getName());
		report.setStartDate(dtStart);
		report.setEndDate(dtEnd);
		report.setBrandName(brand == null ? "" : brand.getName());
		report.setLegalName(legal == null ? "" : legal.getName());
		report.setProductName(ProductName);

		List<TradeReportDetailBuy> storagesIn = GetWarehouse4InLists(dtStart, dtEnd, BrandId, legal, CommodityId);
		List<TradeReportDetailBuy> storagesOut = GetWarehouse4OutLists(dtStart, dtEnd, BrandId, legal, CommodityId);
		List<TradeReportDetailSale> invoice4Sales = GetInvoice4SaleLists(dtStart, dtEnd, BrandId, legal, CommodityId);
		List<TradeReportDetailSaleAndBuy> invoiceContainBuyAndSale = GetInvoice4SnBLists(dtStart, dtEnd, BrandId, legal,
				CommodityId);

		List<TradeReportDetailBuy> StoragesIn1 = storagesIn != null ? storagesIn : null;
		List<TradeReportDetailBuy> StoragesOut1 = storagesOut != null ? storagesOut : null;
		List<TradeReportDetailSale> Invoice4Sales1 = invoice4Sales != null ? invoice4Sales : null;
		List<TradeReportDetailSaleAndBuy> InvoiceContainBuyAndSale1 = invoiceContainBuyAndSale != null
				? invoiceContainBuyAndSale : null;

		report.setStoragesIn((StoragesIn1 != null && StoragesIn1.size() > 0) ? StoragesIn1 : null);
		report.setStoragesOut((StoragesOut1 != null && StoragesOut1.size() > 0) ? StoragesOut1 : null);
		report.setInvoice4Sales((Invoice4Sales1 != null && Invoice4Sales1.size() > 0) ? Invoice4Sales1 : null);
		report.setInvoiceContainBuyAndSale((InvoiceContainBuyAndSale1 != null && InvoiceContainBuyAndSale1.size() > 0)
				? InvoiceContainBuyAndSale1 : null);

		BigDecimal quantity4Invoice = BigDecimal.ZERO;
		BigDecimal quantity4Ware = BigDecimal.ZERO;
		for (TradeReportDetailSale t : invoice4Sales) {
			quantity4Invoice=quantity4Invoice.add(t.getQuantity());
			quantity4Ware=quantity4Ware.add(t.getQuantity4Ware());
		}

		BigDecimal quantityInSum = BigDecimal.ZERO;
		BigDecimal amountInSum = BigDecimal.ZERO;

		for (TradeReportDetailBuy t : storagesIn) {
			quantityInSum=quantityInSum.add(t.getQuantity());
			amountInSum=amountInSum.add(t.getAmount());
		}

		BigDecimal quantityOutSum = BigDecimal.ZERO;
		BigDecimal amountOutSum = BigDecimal.ZERO;

		for (TradeReportDetailBuy t : storagesOut) {
			quantityOutSum=quantityOutSum.add(DecimalUtil.nullToZero(t.getQuantity()));
			amountOutSum=amountOutSum.add(DecimalUtil.nullToZero(t.getAmount()));
		}

		report.setSaleQuantityDiff(((quantity4Invoice != null) ? quantity4Invoice : BigDecimal.ZERO)
				.subtract(((quantity4Ware != null) ? quantity4Ware : BigDecimal.ZERO)));
		report.setQuantity4Stock((Daily == null ? BigDecimal.ZERO
				: ((Daily.getStorageQuantity() != null) ? Daily.getStorageQuantity() : BigDecimal.ZERO))
						.add(((quantityInSum != null) ? quantityInSum : BigDecimal.ZERO)
								.subtract(((quantityOutSum != null) ? quantityOutSum : BigDecimal.ZERO)))); // 库存数量
		report.setAmount4Stock((Daily == null ? BigDecimal.ZERO
				: ((Daily.getStorageAmount() != null) ? Daily.getStorageAmount() : BigDecimal.ZERO))
						.add(((amountInSum != null) ? amountInSum : BigDecimal.ZERO)
								.subtract(((amountOutSum != null) ? amountOutSum : BigDecimal.ZERO)))); // 库存金额

		SettleDaily settle = new SettleDaily();
		settle.setStorageAmount(report.getAmount4Stock());
		settle.setStorageQuantity(report.getQuantity4Stock());
		settle.setBrandId(BrandId);
		settle.setLegalId(LegalId);
		settle.setCommodityId(CommodityId);
		settle.setTradeDate(dtEnd);
		report.setSettleDaily(settle);
		return report;
	}

	/**
	 * 期初库存
	 * 
	 * @return
	 */
	private SettleDaily GetSettleDailyInit(Date dtStart, Date dtEnd, String BrandId, Legal Legal, String CommodityId) {

		String procName = "[dbo].[proc_TradeReport_SettleDaily_init]";

		if (Legal.getCode().equals("SM")) {
			procName = "[dbo].[proc_TradeReport_SettleDaily_init4SM]";
		}

		Map<String, Object> parameters = new LinkedHashMap<>();

		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("LegalId", Legal.getId());
		parameters.put("BrandId", BrandId);
		parameters.put("CommodityId", CommodityId);

		List<Object[]> snBRecords = reuterRepository.ExecuteProcedureSql(procName, parameters);

		SettleDaily snb = new SettleDaily();

		for (Object[] snBRecord : snBRecords) {
			//String[] rec = (String[]) snBRecord;
			Object b=snBRecord[0];
			Object c=snBRecord[1];
			snb.setStorageQuantity(new BigDecimal(String.valueOf(b)));
			snb.setStorageAmount(new BigDecimal(String.valueOf(c)));
			snb.setLegalId(Legal.getId());
			snb.setBrandId(BrandId);
			snb.setCommodityId(CommodityId);
			snb.setTradeDate(DateUtil.getDiffDate(dtStart, -1));
		}

		return snb;
	}

	/**
	 * 入库记录
	 * 
	 * @return
	 */
	private List<TradeReportDetailBuy> GetWarehouse4InLists(Date dtStart, Date dtEnd, String BrandId, Legal legal,
			String CommodityId) {

		String procName = "proc_TradeReport_BuyIn";

		if (legal.getCode().equals("SM")) {
			procName = "proc_TradeReport_BuyIn4SM";
		}

		Map<String, Object> parameters = new LinkedHashMap<>();

		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("LegalId", legal.getId());
		parameters.put("BrandId", BrandId);
		parameters.put("CommodityId", CommodityId);

		List<Object[]> snBRecords = reuterRepository.ExecuteProcedureSql(procName, parameters);

		List<TradeReportDetailBuy> lst = new ArrayList<>();

		for (Object[] rec : snBRecords) {
			TradeReportDetailBuy snb = new TradeReportDetailBuy();
			//String[] rec = (String[]) snBRecord;
			snb.setOType(String.valueOf(rec[0]));
			snb.setInvoiceNo(rec[1]!=null?String.valueOf(rec[1]):null);
			snb.setPrice(new BigDecimal(String.valueOf(rec[2])));
			snb.setQuantity(new BigDecimal(String.valueOf(rec[3])));
			snb.setAmount(new BigDecimal(String.valueOf(rec[4])));

			lst.add(snb);
		}

		return lst;
	}

	/**
	 * 出库记录（成本信息）
	 * 
	 * @return
	 */
	private List<TradeReportDetailBuy> GetWarehouse4OutLists(Date dtStart, Date dtEnd, String BrandId, Legal legal,
			String CommodityId) {

		String procName = "proc_TradeReport_BuyOut";

		if (legal.getCode().equals("SM")) {
			procName = "proc_TradeReport_BuyOut4SM";
		}

		Map<String, Object> parameters = new LinkedHashMap<>();

		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("LegalId", legal.getId());
		parameters.put("BrandId", BrandId);
		parameters.put("CommodityId", CommodityId);

		List<Object[]> snBRecords = reuterRepository.ExecuteProcedureSql(procName, parameters);

		List<TradeReportDetailBuy> lst = new ArrayList<>();

		for (Object[] rec : snBRecords) {
			TradeReportDetailBuy snb = new TradeReportDetailBuy();

			snb.setOType(String.valueOf(rec[0]));
			snb.setInvoiceNo(rec[1]!=null?String.valueOf(rec[1]):null);
			snb.setPrice(rec[2]!=null?new BigDecimal(String.valueOf(rec[2])):BigDecimal.ZERO);
			snb.setQuantity(rec[3]!=null?new BigDecimal(String.valueOf(rec[3])):BigDecimal.ZERO);
			snb.setAmount(rec[4]!=null?new BigDecimal(String.valueOf(rec[4])):BigDecimal.ZERO);

			lst.add(snb);
		}

		return lst;

	}

	/**
	 * 销售发票
	 * 
	 * @return
	 */
	private List<TradeReportDetailSale> GetInvoice4SaleLists(Date dtStart, Date dtEnd, String BrandId, Legal legal,
			String CommodityId) {

		String procName = "proc_TradeReport_Sales";

		if (legal.getCode().equals("SM")) {
			procName = "proc_TradeReport_Sales4SM";
		}

		Map<String, Object> parameters = new LinkedHashMap<>();

		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("LegalId", legal.getId());
		parameters.put("BrandId", BrandId);
		parameters.put("CommodityId", CommodityId);

		List<Object[]> snBRecords = reuterRepository.ExecuteProcedureSql(procName, parameters);

		List<TradeReportDetailSale> lst = new ArrayList<>();

		for (Object[] rec : snBRecords) {
			TradeReportDetailSale snb = new TradeReportDetailSale();

			snb.setCustomerName(String.valueOf(rec[1]));
			snb.setInvoiceNo(rec[2]!=null?String.valueOf(rec[2]):null);
			snb.setQuantity(rec[3]!=null?new BigDecimal(String.valueOf(rec[3])):BigDecimal.ZERO);
			snb.setPrice(rec[4]!=null?new BigDecimal(String.valueOf(rec[4])):BigDecimal.ZERO);
			snb.setAmount(rec[5]!=null?new BigDecimal(String.valueOf(rec[5])):BigDecimal.ZERO);
			snb.setQuantity4Ware(rec[6]!=null?new BigDecimal(String.valueOf(rec[6])):BigDecimal.ZERO);

			lst.add(snb);
		}

		return lst;

	}

	/**
	 * 销售发票+采购发票 （1-n)
	 * 
	 * @return
	 */
	private List<TradeReportDetailSaleAndBuy> GetInvoice4SnBLists(Date dtStart, Date dtEnd, String BrandId, Legal legal,
			String CommodityId) {
		String procName = "proc_TradeReport_SaleAndBuy";

		if (legal.getCode().equals("SM")) {
			procName = "proc_TradeReport_SaleAndBuy";
		}

		Map<String, Object> parameters = new LinkedHashMap<>();

		parameters.put("dBegin", dtStart);
		parameters.put("dEnd", dtEnd);
		parameters.put("LegalId", legal.getId());
		parameters.put("BrandId", BrandId);
		parameters.put("CommodityId", CommodityId);

		List<Object[]> snBRecords = reuterRepository.ExecuteProcedureSql(procName, parameters);

		List<TradeReportDetailSaleAndBuy> lst = new ArrayList<>();

		for (Object[] rec : snBRecords) {
			TradeReportDetailSaleAndBuy snb = new TradeReportDetailSaleAndBuy();

			snb.setInvoiceNo(rec[0]!=null?String.valueOf(rec[0]):null);
			snb.setDocumentNo(String.valueOf(rec[1]));
			snb.setQuantity(rec[2]!=null?new BigDecimal(String.valueOf(rec[2])):BigDecimal.ZERO);
			snb.setQuantityIn(rec[3]!=null?new BigDecimal(String.valueOf(rec[3])):BigDecimal.ZERO);
			lst.add(snb);
		}

		return lst;
	}

	// 更新库存统计表
	@Override
	public void UpdateModelStorage(String corporationId, String commodityId, String brandId, String warehouseId,
			int storageType) {
		ModelStorage modelStorage = new ModelStorage();
		modelStorage.setLegalId(corporationId);
		modelStorage.setCommodityId(commodityId);
		modelStorage.setBrandId(brandId);
		modelStorage.setWarehouseId(warehouseId);
		switch (storageType) {
		case -2:
			// 重新汇总几个数量 - 批次表 - 1. 取得名义上的数量汇总

			List<Lot> lots = lotRepository.GetQueryable(Lot.class)
					.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("IsDelivered", Boolean.FALSE)))
					.toList();
			BigDecimal qtyNotionalIn = BigDecimal.ZERO;
			BigDecimal qtyNotionalOut = BigDecimal.ZERO;
			for (Lot lot : lots) {
				String spotDirection = lot.getContract().getSpotDirection();
				if (spotDirection.equals(SpotType.Purchase)) {
					qtyNotionalIn.add((lot.getIsDelivered() ? lot.getQuantity()
							: subtract(lot.getQuantity(), lot.getQuantityDelivered())));
				} else if (spotDirection.equals(SpotType.Sell)) {
					qtyNotionalOut.add((lot.getIsDelivered() ? lot.getQuantity()
							: subtract(lot.getQuantity(), lot.getQuantityDelivered())));
				}
			}
			modelStorage.setQtyNotionalIn(qtyNotionalIn);
			modelStorage.setQtyNotionalOut(qtyNotionalOut);
			modelStorage.setQtyNotionalBalance(qtyNotionalIn.subtract(qtyNotionalOut));
			Write2ModelStorageNotional(modelStorage);
			break;
		case -1:
			// 重新汇总几个数量 - 批次表 - 2. 取得在途的数量汇总
			// todo:根据在途仓库的名称，统计在途数量

			break;
		default:

			// 重新汇总几个数量 - 批次表 - 3. 取得实际的数量汇总
			// 仅计算现有的实际库存数量，卖出去的已被扣减

			List<Storage> tmp = storageRepository.GetQueryable(Storage.class)
					.where(DetachedCriteria.forClass(Storage.class)
							.add(Restrictions.or(
									Restrictions.and(Restrictions.eq("MT", MT4Storage.Take),
											Restrictions.eq("IsInitiated", false), Restrictions.gt("Quantity", 0)),
									Restrictions.eq("IsInitiated", Boolean.TRUE))

							).add(Restrictions.eq("WarehouseId", warehouseId)).add(Restrictions.eq("BrandId", brandId))).toList();
			List<Storage> storages = new ArrayList<>();
			BigDecimal qtyStorageBalance = BigDecimal.ZERO;
			for (Storage storage : tmp) {
				if (storage.getLot().getCommodity().equals(commodityId)) {
					storages.add(storage);
					if (!storage.getIsInitiated() && (storage.getQuantity().compareTo(BigDecimal.ZERO) > 0)) {
						qtyStorageBalance.add(storage.getQuantity());
					}
					if (storage.getIsInitiated()) {
						if (storage.getMT().equals(MT4Storage.Take)) {
							qtyStorageBalance.add(storage.getQuantity());
						} else if (storage.getMT().equals(MT4Storage.Make)) {
							qtyStorageBalance.subtract(storage.getQuantity());
						}
					}

				}
			}
			modelStorage.setQtyStorageBalance(qtyStorageBalance);
			Write2ModelStorageActual(modelStorage);
			break;
		}
	}

	private void Write2ModelStorageNotional(ModelStorage modelStorage) {

		DetachedCriteria where = DetachedCriteria.forClass(ModelStorage.class);
		where.add(Restrictions.eq("LegalId", modelStorage.getLegalId()));
		where.add(Restrictions.eq("CommodityId", modelStorage.getCommodityId()));
		where.add(Restrictions.eq("BrandId", modelStorage.getBrandId()));
		where.add(Restrictions.eq("WarehouseId", modelStorage.getWarehouseId()));

		ModelStorage obj = modelStorageRepository.GetQueryable(ModelStorage.class).where(where).firstOrDefault();

		if (obj != null) {
			obj.setQtyNotionalIn(modelStorage.getQtyNotionalIn());
			obj.setQtyNotionalOut(modelStorage.getQtyNotionalOut());
			obj.setQtyNotionalBalance(modelStorage.getQtyNotionalBalance());
			obj.setQtyTotal(
					obj.getQtyNotionalBalance().add(obj.getQtyTransitBalance()).add(obj.getQtyStorageBalance()));

			modelStorageRepository.SaveOrUpdate(obj);
		} else {
			modelStorage.setQtyTotal(modelStorage.getQtyNotionalBalance().add(modelStorage.getQtyTransitBalance())
					.add(modelStorage.getQtyStorageBalance()));
			modelStorageRepository.SaveOrUpdate(modelStorage);
		}
	}

	private void Write2ModelStorageTransit(ModelStorage modelStorage) {

		DetachedCriteria where = DetachedCriteria.forClass(ModelStorage.class);
		where.add(Restrictions.eq("LegalId", modelStorage.getLegalId()));
		where.add(Restrictions.eq("CommodityId", modelStorage.getCommodityId()));
		where.add(Restrictions.eq("BrandId", modelStorage.getBrandId()));
		where.add(Restrictions.eq("WarehouseId", modelStorage.getWarehouseId()));

		ModelStorage obj = modelStorageRepository.GetQueryable(ModelStorage.class).where(where).firstOrDefault();

		if (obj != null) {
			obj.setQtyTransitIn(modelStorage.getQtyTransitIn());
			obj.setQtyTransitOut(modelStorage.getQtyTransitOut());
			obj.setQtyTransitBalance(modelStorage.getQtyTransitBalance());
			obj.setQtyTotal(
					obj.getQtyNotionalBalance().add(obj.getQtyTransitBalance()).add(obj.getQtyStorageBalance()));

			modelStorageRepository.SaveOrUpdate(obj);
		} else {
			modelStorage.setQtyTotal(modelStorage.getQtyNotionalBalance().add(modelStorage.getQtyTransitBalance())
					.add(modelStorage.getQtyStorageBalance()));
			modelStorageRepository.SaveOrUpdate(modelStorage);
		}
	}

	private void Write2ModelStorageActual(ModelStorage modelStorage) {

		DetachedCriteria where = DetachedCriteria.forClass(ModelStorage.class);
		where.add(Restrictions.eq("LegalId", modelStorage.getLegalId()));
		where.add(Restrictions.eq("CommodityId", modelStorage.getCommodityId()));
		where.add(Restrictions.eq("BrandId", modelStorage.getBrandId()));
		where.add(Restrictions.eq("WarehouseId", modelStorage.getWarehouseId()));

		ModelStorage obj = modelStorageRepository.GetQueryable(ModelStorage.class).where(where).firstOrDefault();
		if (obj != null) {
			obj.setQtyStorageIn(modelStorage.getQtyStorageIn());
			obj.setQtyStorageOut(modelStorage.getQtyStorageOut());
			obj.setQtyStorageBalance(modelStorage.getQtyStorageBalance());
			obj.setQtyTotal(
					obj.getQtyNotionalBalance().add(obj.getQtyTransitBalance()).add(obj.getQtyStorageBalance()));

			modelStorageRepository.SaveOrUpdate(obj);
		} else {
			modelStorage.setQtyTotal(modelStorage.getQtyNotionalBalance().add(modelStorage.getQtyTransitBalance())
					.add(modelStorage.getQtyStorageBalance()));
			modelStorageRepository.SaveOrUpdate(modelStorage);
		}

	}

	/**
	 * 保存
	 * 
	 * @param modelStorage
	 */
	public ActionResult<ModelStorage> Save(ModelStorage modelStorage) {
		try {
			modelStorageRepository.SaveOrUpdate(modelStorage);
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess);
		} catch (RuntimeException ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 */
	public ActionResult<String> Delete(String id) {
		try {
			modelStorageRepository.PhysicsDelete(id, ModelStorage.class);
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);
		} catch (RuntimeException ex) {
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 不带分页
	 * 
	 * @return
	 */
	public ActionResult<List<ModelStorage>> ModelStorages() {
		return new ActionResult<>(Boolean.TRUE, "",
				modelStorageRepository.GetQueryable(ModelStorage.class).where(
						DetachedCriteria.forClass(ModelStorage.class).add(Restrictions.eq("IsHidden", Boolean.FALSE)))
						.toList());
	}

	/**
	 * 根据Id取得单个实体
	 * 
	 * @param id
	 * @return
	 */
	public ActionResult<ModelStorage> GetById(String id) {
		return new ActionResult<>(Boolean.TRUE, "", modelStorageRepository.GetQueryable(ModelStorage.class)
				.where(DetachedCriteria.forClass(ModelStorage.class).add(Restrictions.eq("Id", id))).firstOrDefault());
	}
	public ActionResult<List<LotPnl>> LotPnlQuery(MtmParams params){
		if(params == null)
			params = new MtmParams();
		//获取销售批次（批次发货数量大于0）
		Criteria lotdc4S = this.lotRepository.CreateCriteria(Lot.class);
		lotdc4S.createAlias("Contract", "contract", JoinType.LEFT_OUTER_JOIN);
		lotdc4S.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
		// 业务状态
		if (!StringUtils.isBlank(params.getKeyword())) {

			Criterion a = Restrictions.like("FullNo", "%" + params.getKeyword() + "%");
			Criterion b = Restrictions.like("contract.DocumentNo", "%" + params.getKeyword() + "%");			
			Criterion c = Restrictions.like("customer.Name", "%" + params.getKeyword() + "%");
			Criterion d = Restrictions.like("DocumentNo", "%" + params.getKeyword() + "%");
			Disjunction disjunction = Restrictions.disjunction();
			disjunction.add(a);
			disjunction.add(b);
			disjunction.add(c);
			disjunction.add(d);		
			lotdc4S.add(disjunction);
		}
		if(!StringUtils.isBlank(params.getLegalIds())){

			String[] split = params.getLegalIds().split(",");

			Stream<String> stream = Arrays.stream(split);
			List<String> arrs = stream.map(s -> String.format("%s", s.trim())).collect(Collectors.toList());
			if (arrs.size() > 0) {
				lotdc4S.add(Restrictions.in("LegalId", arrs));
			}
		}
		if(!StringUtils.isBlank(params.getSpotType())){			
			lotdc4S.add(Restrictions.eq("contract.SpotType", params.getSpotType()));			
		}
		if(!StringUtils.isBlank(params.getCurrency())){			
			lotdc4S.add(Restrictions.eq("Currency", params.getCurrency()));			
		}
		if(params.getStartDate() != null){			
			lotdc4S.add(Restrictions.ge("contract.TradeDate", params.getStartDate()));			
		}
		if(params.getEndDate() != null){			
			lotdc4S.add(Restrictions.le("contract.TradeDate", params.getEndDate()));			
		}
		lotdc4S.add(Restrictions.eq("SpotDirection",SpotType.Sell));
		lotdc4S.add(Restrictions.gt("QuantityDelivered",BigDecimal.ZERO));
		RefUtil total = new RefUtil();
//		List<Lot> lots4S = this.lotRepository.GetQueryable(Lot.class).where(lotdc4S).toList();
		List<Lot> lots4S = this.lotRepository.GetPage(lotdc4S, params.getPageSize(), params.getPageIndex(), params.getSortBy(), params.getOrderBy(), total).getData();
		if(lots4S == null || lots4S.size() == 0){
			List<LotPnl> list = new ArrayList<>();
			ActionResult<List<LotPnl>> tempVar1 = new ActionResult<List<LotPnl>>();
			tempVar1.setSuccess(true);
			tempVar1.setData(list);
			return tempVar1;
		}
		//获取销售交付明细
		DetachedCriteria storagedc4M = DetachedCriteria.forClass(CStorage.class);		
		List<String> lotIds4S = lots4S.stream().map(s -> s.getId()).collect(Collectors.toList());
		storagedc4M.add(Restrictions.in("LotId",lotIds4S));
		
		List<CStorage> storages4S = this.cstorageRepository.GetQueryable(CStorage.class).where(storagedc4M).toList();
		if(storages4S == null || storages4S.size() == 0){
			List<LotPnl> list = new ArrayList<>();
			ActionResult<List<LotPnl>> tempVar2 = new ActionResult<List<LotPnl>>();
			tempVar2.setSuccess(true);
			tempVar2.setData(list);			
			return tempVar2;
		}
		//获取采购交付明细
		//List<CStorage> storageIds4T = storages4S.stream().map(s -> s.getCounterparty3()).collect(Collectors.toList());
		List<String> counterpartyId3s = storages4S.stream().map(x -> x.getCounterpartyId3()).distinct().collect(Collectors.toList());
		DetachedCriteria storages4Bdc = DetachedCriteria.forClass(CStorage.class);
		storages4Bdc.add(Restrictions.in("Id",counterpartyId3s));		
		List<CStorage> storages4B = this.cstorageRepository.GetQueryable(CStorage.class).where(storages4Bdc).toList();
		if(storages4B == null || storages4B.size() == 0){
			List<LotPnl> list = new ArrayList<>();
			ActionResult<List<LotPnl>> tempVar3 = new ActionResult<List<LotPnl>>();
			tempVar3.setSuccess(true);
			tempVar3.setData(list);			
			return tempVar3;
		}
		//获取采购批次
		//List<Lot> Lots4B = storages4S.stream().map(s -> s.getLot()).collect(Collectors.toList());		
		List<String> lotIds4B = storages4B.stream().map(x -> x.getLotId()).collect(Collectors.toList());
		DetachedCriteria lotId4Bdc = DetachedCriteria.forClass(CLot.class);
		lotId4Bdc.add(Restrictions.in("Id",lotIds4B));		
		List<CLot> Lots4B = this.clotRepository.GetQueryable(CLot.class).where(lotId4Bdc).toList();
		
//		//获取销售点价记录		
//		DetachedCriteria pricingdc4S = DetachedCriteria.forClass(Pricing.class);
//		pricingdc4S.add(Restrictions.in("LotId", lotIds4S));
//		List<Pricing> pricings4S = this.pricingRepository.GetQueryable(Pricing.class).where(pricingdc4S).toList();
//		//获取采购点价记录
//		List<String> lotIds4B = storageIds4T.stream().map(s -> s.getLotId()).collect(Collectors.toList());
//		DetachedCriteria pricingdc4 = DetachedCriteria.forClass(Pricing.class);
//		pricingdc4S.add(Restrictions.in("LotId", lotIds4B));
//		List<Pricing> pricings4 = this.pricingRepository.GetQueryable(Pricing.class).where(pricingdc4S).toList();
		
		//获取销售发票
		DetachedCriteria invoice4Sdc = DetachedCriteria.forClass(Invoice.class);
		invoice4Sdc.add(Restrictions.in("LotId", lotIds4S));
		List<Invoice> invoices4S = this.invoiceRepository.GetQueryable(Invoice.class).where(invoice4Sdc).toList();
		
		//获取销售保值头寸
		DetachedCriteria positions4Sdc = DetachedCriteria.forClass(Position.class);
		positions4Sdc.add(Restrictions.in("LotId", lotIds4S));
		List<Position> positions4S = this.positionRepository.GetQueryable(Position.class).where(positions4Sdc).toList();
		
		//获取销售保值头寸
		DetachedCriteria positions4Bdc = DetachedCriteria.forClass(Position.class);
		positions4Bdc.add(Restrictions.in("LotId", lotIds4B));
		List<Position> positions4B = this.positionRepository.GetQueryable(Position.class).where(positions4Bdc).toList();
				
		
		List<LotPnl> lotPnlList = new ArrayList<>();
		for (Lot lot : lots4S){
			if(lot.getFullNo().equals("SCUSB169053/10"))
				lot.setFullNo(lot.getFullNo());
			LotPnl lotPnl = new LotPnl();
			lotPnl.setFullNo4S(lot.getFullNo());
			lotPnl.setBrandNames4S(lot.getBrandNames());
			lotPnl.setCustomerName4S(lot.getCustomerName());
			lotPnl.setCustomerId(lot.getCustomerId());
			
			List<Invoice> invoices4SOfLot = invoices4S.stream().filter(x -> x.getLotId().equals(lot.getId())).collect(Collectors.toList());
			String invoiceNos = "";
			if(invoices4SOfLot != null && invoices4SOfLot.size() > 0){
				for(Invoice invoice : invoices4SOfLot){
					if(StringUtils.isBlank(invoiceNos)){
						invoiceNos = invoice.getInvoiceNo();
					}
					else
						invoiceNos = invoiceNos + "," + invoice.getInvoiceNo();
				}
			}				
			lotPnl.setInvoiceNo4S(invoiceNos);
			lotPnl.setQuantity4S(lot.getQuantityDelivered());
			lotPnl.setPrice4S(lot.getPrice());
			
			List<CStorage> storage4SOfLot = storages4S.stream().filter(x -> x.getLotId().equals(lot.getId())).collect(Collectors.toList());			
			BigDecimal realFee4S = BigDecimal.ZERO;
			if(storage4SOfLot != null && storage4SOfLot.size() > 0){
				for(CStorage sto  : storage4SOfLot){
					realFee4S = realFee4S.add(DecimalUtil.nullToZero(sto.getRealFee()));
				}
			}
				
			
			lotPnl.setRealFee4S(realFee4S);
			lotPnl.setQuantityHedged4S(lot.getQuantityHedged());
			
			BigDecimal sumAmountOfHedged4S = BigDecimal.ZERO;
			BigDecimal sumQuantityOfHedged4S = BigDecimal.ZERO;
			BigDecimal priceOfHedged4s = BigDecimal.ZERO;
			List<Position> positions4SOfLot = positions4S.stream().filter(x -> x.getLotId().equals(lot.getId())).collect(Collectors.toList());
			if(positions4SOfLot != null && positions4SOfLot.size() > 0){
				for(Position position : positions4SOfLot){
					sumQuantityOfHedged4S = sumQuantityOfHedged4S.add(DecimalUtil.nullToZero(position.getQuantity()));
					sumAmountOfHedged4S = sumAmountOfHedged4S.add(DecimalUtil.nullToZero(position.getOurPrice()).multiply(DecimalUtil.nullToZero(position.getQuantity())));
				}
			}
			if(sumQuantityOfHedged4S.compareTo(BigDecimal.ZERO) != 0)
				priceOfHedged4s = sumAmountOfHedged4S.divide(sumQuantityOfHedged4S,2, RoundingMode.HALF_UP);
			
			lotPnl.setPriceHedged4S(priceOfHedged4s);
			
			BigDecimal profit4S = DecimalUtil.nullToZero(lot.getQuantityDelivered()).multiply(DecimalUtil.nullToZero(lot.getPrice()));
			
			lotPnl.setProfit4S(profit4S);			
			
			List<String> counterpartyId3sOfLot = storage4SOfLot.stream().map(x -> x.getCounterpartyId3()).collect(Collectors.toList());
			List<CStorage> storages4BOfLot = storages4B.stream().filter(x -> counterpartyId3sOfLot.contains(x.getId())).collect(Collectors.toList());
			
			BigDecimal Cost4B = BigDecimal.ZERO;
			BigDecimal realFee4B = BigDecimal.ZERO;
			BigDecimal Price4B = BigDecimal.ZERO;
			BigDecimal sumAmountOfHedged4B = BigDecimal.ZERO;
			BigDecimal PriceHedged4B = BigDecimal.ZERO;
			BigDecimal quantityHedged4B = BigDecimal.ZERO;
			List<String> lotIdHasAccessed = new ArrayList<>();
			if(storages4BOfLot != null && storages4BOfLot.size() > 0){
				for(CStorage sto : storages4BOfLot){
					realFee4B = realFee4B.add(DecimalUtil.nullToZero(sto.getRealFee()).multiply(DecimalUtil.nullToZero(sto.getQuantity())));
					
					
					CLot lot4B = Lots4B.stream().filter(x -> x.getId().equals(sto.getLotId())).findFirst().orElse(null);	
					if(lot4B == null || lotIdHasAccessed.contains(sto.getLotId()))
						continue;
					lotIdHasAccessed.add(sto.getLotId());
					Cost4B = Cost4B.add(DecimalUtil.nullToZero(sto.getQuantity()).multiply(DecimalUtil.nullToZero(lot4B.getPrice())));
					List<Position> positions = positions4B.stream().filter(x -> x.getLotId().equals(sto.getLotId())).collect(Collectors.toList());
					BigDecimal quantityOfHedged = BigDecimal.ZERO;
					if(positions != null && positions.size() > 0){						
						for(Position position : positions){
							sumAmountOfHedged4B = sumAmountOfHedged4B.add(DecimalUtil.nullToZero(position.getOurPrice()).multiply(DecimalUtil.nullToZero(position.getQuantity())));
							quantityOfHedged = quantityOfHedged.add(DecimalUtil.nullToZero(position.getQuantity()));
						}					
						
					}
					if(lot4B.getIsDelivered())
						quantityHedged4B = quantityHedged4B.add(quantityOfHedged.multiply(sto.getQuantity()).divide(lot4B.getQuantityDelivered(),2,RoundingMode.HALF_UP));
					else
						quantityHedged4B = quantityHedged4B.add(quantityOfHedged.multiply(sto.getQuantity()).divide(lot4B.getQuantity(),2,RoundingMode.HALF_UP));
					if(quantityHedged4B.compareTo(BigDecimal.ZERO) != 0)
						PriceHedged4B = sumAmountOfHedged4B.divide(quantityHedged4B,2,RoundingMode.HALF_UP);
				
				}				
			}
			Price4B = Cost4B.divide(lot.getQuantityDelivered(),2,RoundingMode.HALF_UP);				
			lotPnl.setPrice4B(Price4B);
			lotPnl.setPriceHedged4B(PriceHedged4B);
			lotPnl.setRealFee4B(realFee4B);
			lotPnl.setQuantityHedged4B(quantityHedged4B);
			lotPnl.setPriceHedged4B(PriceHedged4B);
				
			lotPnl.setCost4B(Cost4B);
			
			lotPnl.setSumProfit4Goods(lotPnl.getProfit4S().subtract(realFee4S).subtract(lotPnl.getCost4B()).subtract(realFee4B));
			lotPnl.setSumProfit4Hedged(sumAmountOfHedged4B.add(sumAmountOfHedged4S).negate());			
			lotPnl.setSumProfit4All(lotPnl.getSumProfit4Goods().add(lotPnl.getSumProfit4Hedged()));
			lotPnlList.add(lotPnl);
		}
		ActionResult<List<LotPnl>> tempVar3 = new ActionResult<List<LotPnl>>();
		tempVar3.setSuccess(true);
		tempVar3.setData(lotPnlList);
		tempVar3.setTotal(total);
		return tempVar3;
	};
}
