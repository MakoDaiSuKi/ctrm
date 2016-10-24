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
public interface SFEService {
	ActionResult<String> Sync();
	ActionResult<SFE> Save(SFE sfe);
	ActionResult<String> Delete(String id);
	ActionResult<SFE> GetById(String id);
	Criteria GetCriteria();
	List<SFE> SFEs();

    List<SFE> SFEs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy, String orderSort);
    
    /**
     * 接入上期所数据
     */
    void fetchQuote();
}