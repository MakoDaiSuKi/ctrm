package com.smm.ctrm.bo.Maintain;

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
public interface FetchSFEService {
	void GetCriteria();
	ActionResult<String> Save(FetchSFE fetchSFE);
	ActionResult<String> Delete(String id);
	ActionResult<FetchSFE> GetById(String id);

    List<FetchSFE> FetchSFEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);

}