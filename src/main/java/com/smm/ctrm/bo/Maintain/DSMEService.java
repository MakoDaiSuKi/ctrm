package com.smm.ctrm.bo.Maintain;

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
public interface DSMEService {
	ActionResult<String> Sync();
	Criteria GetCriteria();
	ActionResult<DSME> Save(DSME dsme) throws Exception;
	ActionResult<String> Delete(String id);
	ActionResult<DSME> GetById(String id);
	List<DSME> Dsmes();
	List<DSME> Dsmes(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy, String orderBy);
	/**
     * 接入SMM现货数据
     */
    void fetchQuote();
}