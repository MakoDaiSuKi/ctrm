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
public interface RemarkService {
	ActionResult<List<Remark>> Remarks();
	ActionResult<List<Remark>> BackRemarks();
	ActionResult<Remark> Save(Remark remark);
	ActionResult<String> Delete(String id);
	ActionResult<Remark> GetById(String id);

}