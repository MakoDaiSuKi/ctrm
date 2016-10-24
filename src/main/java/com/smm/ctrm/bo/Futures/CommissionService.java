package com.smm.ctrm.bo.Futures;

import java.util.List;

import com.smm.ctrm.domain.Physical.Commission;
import com.smm.ctrm.dto.res.ActionResult;

public interface CommissionService {
	ActionResult<Commission> Save(Commission commission);

	ActionResult<String> Delete(String id);

	ActionResult<Commission> GetById(String id);

	ActionResult<List<Commission>> Commissions();

}