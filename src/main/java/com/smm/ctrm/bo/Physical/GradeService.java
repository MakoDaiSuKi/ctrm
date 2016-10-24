package com.smm.ctrm.bo.Physical;

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
public interface GradeService {
	void GetCriteria();
	ActionResult<Grade> Save(Grade grade);
	ActionResult<String> Delete(String id);
	ActionResult<Grade> GetById(String id);
	ActionResult<List<Grade>> GradesByContractId(String contractId);
	ActionResult<List<Grade>> GradesByInvoiceId(String invoiceId);
	List<Grade> Grades();

}