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
public interface DivisionService {
	ActionResult<List<Division>> Divisions();
	ActionResult<List<Division>> GetDivisionsByOrgId(String orgId);
	ActionResult<Division> Save(Division division);
	ActionResult<String> Delete(String id);
	ActionResult<Division> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);
	ActionResult<List<Division>> Pager(RefUtil total, String orderSort, String orderBy, Criteria param,
			Integer pageSize, Integer pageIndex);

}