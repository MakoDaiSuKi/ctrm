package com.smm.ctrm.bo.Maintain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.smm.ctrm.util.RefUtil;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface LMBService {
	Criteria GetCriteria();
	ActionResult<LMB> Save(LMB lmb);
	ActionResult<String> Delete(String id);
	ActionResult<LMB> GetById(String id);
	List<LMB> LMBs();
    List<LMB> LMBs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);

}