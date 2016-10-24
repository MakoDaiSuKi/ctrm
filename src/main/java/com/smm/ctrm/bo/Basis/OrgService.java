package com.smm.ctrm.bo.Basis;

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
public interface OrgService {
	Criteria GetCriteria();
	ActionResult<List<Org>> Orgs();
	ActionResult<Org> Save(Org org);
	ActionResult<String> Delete(String id);
	ActionResult<Org> GetById(String id);
	List<Org> Orgs(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy, String orderBy);

}