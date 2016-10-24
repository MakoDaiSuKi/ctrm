package com.smm.ctrm.bo.Physical;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.smm.ctrm.util.RefUtil;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface DischargingPriceDiffService {
	void GetCriteria();
	ActionResult<DischargingPriceDiff> Save(DischargingPriceDiff pricediff);
	ActionResult<String> Delete(String id);
	ActionResult<DischargingPriceDiff> GetById(String id);
	ActionResult<List<DischargingPriceDiff>> PriceDiffsByContractId(String contractId);
	List<DischargingPriceDiff> PriceDiffs();

    List<DischargingPriceDiff> PriceDiffs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);


}