package com.smm.ctrm.bo.Finance;

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
public interface FeeService {
	void GetCriteria();
	ActionResult<String> Save(Fee fee);
	ActionResult<String> Delete(String feeId);
	ActionResult<Fee> GetById(String feeId);
	ActionResult<List<Fee>> FeesByLotId(String lotId);

}