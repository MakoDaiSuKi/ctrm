package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Physical.SpotPriceEstimateService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.CustomerBalance_new;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.SpotPriceEstimate;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.SpotType;

/**
 * 
 * @author zengshihua
 *
 */
@Service
public class SpotPriceEstimateServiceImpl implements SpotPriceEstimateService {

	@Autowired
	private HibernateRepository<Lot> lotHibRepository;

	@Autowired
	private HibernateRepository<Pricing> pricingRepository;
	
	@Autowired
	private HibernateRepository<SpotPriceEstimate> speRepository;

	/**
	 * 现货暂估价
	 */
	@Override
	public void everyDaySpotPrice() {
		Map<String, Record> result = new HashMap<>();

		Date today = DateUtil.doSFormatDate(new Date(), "yyyy-MM-dd");
		/**
		 * 获取今天发生的订单、并且点价方式是固定价格
		 */
		DetachedCriteria todayLot = DetachedCriteria.forClass(Lot.class);
		todayLot.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayLot.add(Restrictions.le("CreatedAt", DateUtil.endOfTodDay(today)));
		todayLot.add(Restrictions.eq("MajorType", MajorType.Fix));
		List<Lot> lots = this.lotHibRepository.GetQueryable(Lot.class).where(todayLot).toList();
		for (Lot lot : lots) {
			calculate(lot, result);
		}
		/**
		 * 当天点价记录
		 */
		DetachedCriteria todayPricing = DetachedCriteria.forClass(Pricing.class);
		todayPricing.add(Restrictions.ge("CreatedAt", DateUtil.startOfTodDay(today)));
		todayPricing.add(Restrictions.le("CreatedAt", DateUtil.endOfTodDay(today)));
		List<Pricing> pricings = this.pricingRepository.GetQueryable(Pricing.class).where(todayPricing).toList();
		for (Pricing pricing : pricings) {
			calculatePricing(pricing,result);
		}
		
		for (Entry<String, Record> map : result.entrySet()) {
			Record record=map.getValue();
			SpotPriceEstimate spe=new SpotPriceEstimate();
			spe.setEstimateDate(new Date());
			spe.setCommodityId(record.getCommodityId());
			spe.setCurrency(record.getCurrency());
			if(record.getSumBuyQuantity().compareTo(BigDecimal.ZERO)>0){
				spe.setBuyPrice(DecimalUtil.divideForPrice(record.getSumBuyPrice(), record.getSumBuyQuantity()));
			}
			
			if(record.getSumSellQuantity().compareTo(BigDecimal.ZERO)>0){
				spe.setSellPrice(DecimalUtil.divideForPrice(record.getSumSellPrice(), record.getSumSellQuantity()));
			}
			
			this.speRepository.SaveForJob(spe);
		}
		
	}

	protected void calculatePricing(Pricing pricing,Map<String, Record> result) {

		Lot lot=pricing.getLot();
		
		String key = lot.getCommodityId() + lot.getCurrency();

		if (result.containsKey(key)) {
			Record r = result.get(key);
			if (lot.getSpotDirection().equals(SpotType.Sell)) {
				r.setSumSellPrice(r.getSumSellPrice().add(pricing.getPrice().multiply(pricing.getQuantity())));
				r.setSumSellQuantity(r.getSumSellQuantity().add(pricing.getQuantity()));
			} else {
				r.setSumBuyPrice(r.getSumBuyPrice().add(pricing.getPrice().multiply(pricing.getQuantity())));
				r.setSumBuyQuantity(r.getSumBuyQuantity().add(pricing.getQuantity()));
			}
			result.put(key, r);
		} else {
			Record r = new Record();
			r.setCommodityId(lot.getCommodityId());
			r.setCurrency(lot.getCurrency());

			if (lot.getSpotDirection().equals(SpotType.Sell)) {
				r.setSumSellPrice(pricing.getPrice().multiply(pricing.getQuantity()));
				r.setSumSellQuantity(pricing.getQuantity());
				r.setSumBuyPrice(BigDecimal.ZERO);
				r.setSumBuyQuantity(BigDecimal.ZERO);
			} else {
				r.setSumSellPrice(BigDecimal.ZERO);
				r.setSumSellQuantity(BigDecimal.ZERO);
				r.setSumBuyPrice(pricing.getPrice().multiply(pricing.getQuantity()));
				r.setSumBuyQuantity(pricing.getQuantity());
			}
			result.put(key, r);
		}
	}

	protected void calculate(Lot lot, Map<String, Record> result) {

		String key = lot.getCommodityId() + lot.getCurrency();

		if (result.containsKey(key)) {
			Record r = result.get(key);
			if (lot.getSpotDirection().equals(SpotType.Sell)) {
				r.setSumSellPrice(r.getSumSellPrice().add(lot.getPrice().multiply(lot.getQuantityPriced())));
				r.setSumSellQuantity(r.getSumSellQuantity().add(lot.getQuantityPriced()));
			} else {
				r.setSumBuyPrice(r.getSumBuyPrice().add(lot.getPrice().multiply(lot.getQuantityPriced())));
				r.setSumBuyQuantity(r.getSumBuyQuantity().add(lot.getQuantityPriced()));
			}
			result.put(key, r);
		} else {
			Record r = new Record();
			r.setCommodityId(lot.getCommodityId());
			r.setCurrency(lot.getCurrency());

			if (lot.getSpotDirection().equals(SpotType.Sell)) {
				r.setSumSellPrice(lot.getPrice().multiply(lot.getQuantityPriced()));
				r.setSumSellQuantity(lot.getQuantityPriced());
				r.setSumBuyPrice(BigDecimal.ZERO);
				r.setSumBuyQuantity(BigDecimal.ZERO);
			} else {
				r.setSumSellPrice(BigDecimal.ZERO);
				r.setSumSellQuantity(BigDecimal.ZERO);
				r.setSumBuyPrice(lot.getPrice().multiply(lot.getQuantityPriced()));
				r.setSumBuyQuantity(lot.getQuantityPriced());
			}
			result.put(key, r);
		}
	}

	class Record {

		private BigDecimal sumBuyPrice;

		private BigDecimal sumBuyQuantity;

		private BigDecimal sumSellPrice;

		private BigDecimal sumSellQuantity;

		private String currency;

		private String CommodityId;

		public BigDecimal getSumBuyPrice() {
			return sumBuyPrice;
		}

		public void setSumBuyPrice(BigDecimal sumBuyPrice) {
			this.sumBuyPrice = sumBuyPrice;
		}

		public BigDecimal getSumBuyQuantity() {
			return sumBuyQuantity;
		}

		public void setSumBuyQuantity(BigDecimal sumBuyQuantity) {
			this.sumBuyQuantity = sumBuyQuantity;
		}

		public BigDecimal getSumSellPrice() {
			return sumSellPrice;
		}

		public void setSumSellPrice(BigDecimal sumSellPrice) {
			this.sumSellPrice = sumSellPrice;
		}

		public BigDecimal getSumSellQuantity() {
			return sumSellQuantity;
		}

		public void setSumSellQuantity(BigDecimal sumSellQuantity) {
			this.sumSellQuantity = sumSellQuantity;
		}

		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		public String getCommodityId() {
			return CommodityId;
		}

		public void setCommodityId(String commodityId) {
			CommodityId = commodityId;
		}

	}

	@Override
	public void clearSession() {
		this.speRepository.Clear();
	}
}
