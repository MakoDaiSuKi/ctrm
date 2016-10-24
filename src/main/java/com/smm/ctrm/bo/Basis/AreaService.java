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
public interface AreaService {
	ActionResult<List<Area>> Areas();
	ActionResult<List<Area>> BackAreas();
	ActionResult<Area> Save(Area area);
	ActionResult<String> Delete(String id);
	ActionResult<Area> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);

}