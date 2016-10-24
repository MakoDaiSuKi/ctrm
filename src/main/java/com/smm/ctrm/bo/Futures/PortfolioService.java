package com.smm.ctrm.bo.Futures;

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
public interface PortfolioService {
	Criteria GetCriteria();
	ActionResult<String> Save(Portfolio portfolio);
	ActionResult<String> Delete(String id);
	ActionResult<Portfolio> GetById(String id);
	List<Portfolio> Portfolios();


    List<Portfolio> Portfolios(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);

}