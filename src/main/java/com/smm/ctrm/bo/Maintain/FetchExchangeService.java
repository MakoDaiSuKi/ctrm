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
public interface FetchExchangeService {
	void GetCriteria();
	ActionResult<String> Save(FetchExchange fetchExchange);
	ActionResult<String> Delete(String id);
	ActionResult<FetchExchange> GetById(String id);


    List<FetchExchange> FetchExchanges(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);

}