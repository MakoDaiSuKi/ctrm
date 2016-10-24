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
public interface LMEService {
	ActionResult<String> Sync();
	ActionResult<String> SaveLmes(List<LME> lmes);
	ActionResult<String> Save(LME lme);
	ActionResult<String> Delete(String id);
	ActionResult<String> DeleteLmes(List<LME> lmes);
	ActionResult<LME> GetById(String id);
	Criteria GetCriteria();
	List<LME> LMEs();

    List<LME> LMEs(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,RefUtil total);

}