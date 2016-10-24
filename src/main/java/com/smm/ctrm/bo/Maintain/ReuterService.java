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
public interface ReuterService {
    Criteria GetCriteria();
	ActionResult<String> Import1By1(Reuter reuter, String userName, String userId);

    List<Reuter> Storages(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy,String orderSort);

}