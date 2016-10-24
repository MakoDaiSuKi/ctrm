package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.HedgeNumber;
import com.smm.ctrm.domain.apiClient.HedgeNumberParams;
import com.smm.ctrm.dto.res.ActionResult;

public interface HedgeNumberService {
	Criteria GetCriteria();

	ActionResult<HedgeNumber> Save(HedgeNumber hedgeNumber);

	ActionResult<String> GetNo(String type);

	ActionResult<HedgeNumber> GetHedgeNumberById(String hNId);

	ActionResult<List<HedgeNumber>> GetHedgeNumbers(HedgeNumberParams hnParams);

	ActionResult<String> Delete(String hnId);

}