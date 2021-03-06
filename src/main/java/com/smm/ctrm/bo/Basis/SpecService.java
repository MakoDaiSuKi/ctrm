package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface SpecService {
	ActionResult<List<Spec>> Specs();
	ActionResult<List<Spec>> BackSpecs();
	ActionResult<List<Spec>> SpecsByCommodityId(String commodityId);
	ActionResult<Spec> Save(Spec spec);
	ActionResult<String> Delete(String id);
	ActionResult<Spec> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);

}