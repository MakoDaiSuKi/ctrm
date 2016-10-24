package com.smm.ctrm.bo.Basis;

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
public interface LegalService {
	ActionResult<List<Legal>> Pager(RefUtil total, String orderSort, Criteria param, Integer pageIndex, Integer pageSize, String orderBy);
	ActionResult<List<Legal>> Legals();
	ActionResult<List<Legal>> Legals(String orgId);
	ActionResult<Legal> Save(Legal legal);
	ActionResult<String> Delete(String legalId);
	ActionResult<Legal> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);
	void ModifyDefaultLegal(String id);

}