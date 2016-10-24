package com.smm.ctrm.bo.Futures;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.CpSplitPricing;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
public interface PricingService {
	void ScheduledUpdateAveragePricing();
	Criteria GetCriteria();
	ActionResult<List<Pricing>> PricingByContractId(String contractId);
	ActionResult<List<Pricing>> PricingByLotId(String lotId);
	ActionResult<List<PricingRecord>> PricingRecordsByContractId(String contractId);
	ActionResult<List<PricingRecord>> PricingRecordsByLotId(String lotId);
	ActionResult<Pricing> GetById(String pricingId);
	ActionResult<String> SavePricingScheduled(Pricing pricing);
	ActionResult<String> SavePricingExtended(Pricing pricing);
	ActionResult<String> Delete(String pricingId);
	ActionResult<String> SplitPricing(CpSplitPricing cpSplitPricing, String userId);
	void UpdateAveragePriceById(String pricingId);
	void UpdateAveragePrice(Pricing pricing);
	void UpdateMajorOnAverage(Pricing pricing);
	void UpdatePremiumOnAverage(Pricing pricing);
	List<Pricing> Pricings();
	List<Pricing> Pricings(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);


}