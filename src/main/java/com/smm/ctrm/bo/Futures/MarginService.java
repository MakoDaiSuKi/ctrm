package com.smm.ctrm.bo.Futures;

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
public interface MarginService {
	ActionResult<String> Save(Margin margin);
	ActionResult<String> Delete(String id);
	ActionResult<Margin> GetById(String id);
	ActionResult<List<Margin>> Margins();

}