package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.FinishedProduct;
import com.smm.ctrm.domain.apiClient.FinishedProductParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface FinishedProductService {

	Criteria CreateCriteria();

	ActionResult<List<FinishedProduct>> Pager(Criteria criteria, FinishedProductParams receiptParams, RefUtil total);

	ActionResult<FinishedProduct> GetById(String id);

	ActionResult<FinishedProduct> Save(FinishedProduct finishedProduct) throws Exception;

	ActionResult<String> Delete(String id);

	ActionResult<String> Init();

}
