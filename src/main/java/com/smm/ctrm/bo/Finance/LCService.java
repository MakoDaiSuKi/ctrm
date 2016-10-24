package com.smm.ctrm.bo.Finance;

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
public interface LCService {
	Criteria GetCriteria();
	ActionResult<LC> Save(LC lc);
	ActionResult<String> Import1By1(LC lc);
	ActionResult<String> Delete(String id);
	ActionResult<LC> GetById(String id);
	List<LC> LCs();


    List<LC> LCs(Criteria param, int pageSize, int pageIndex, RefUtil total, String orderBy,
                  String orderSort);

}