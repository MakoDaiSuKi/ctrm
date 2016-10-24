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
public interface OriginService {
	ActionResult<List<Origin>> OriginsByCommodityId(String commodityId);
	ActionResult<List<Origin>> Origins();
	ActionResult<List<Origin>> BackOrigins();
	ActionResult<Origin> Save(Origin origin);
	ActionResult<String> Delete(String id);
	ActionResult<Origin> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);

}