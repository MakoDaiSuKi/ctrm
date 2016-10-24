package com.smm.ctrm.bo.Futures;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface MarginFlowService {
	ActionResult<String> Save(MarginFlow marginFlow);
	ActionResult<String> Delete(String id);
	ActionResult<MarginFlow> GetById(String id);
	ActionResult<List<MarginFlow>> MarginFlows();
	Criteria GetCriteria();
	List<MarginFlow> Pager(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String sortBy);

}