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
public interface InvoiceGradeService {
	void GetCriteria();
	ActionResult<String> Save(InvoiceGrade invoiceGrade);
	ActionResult<String> Delete(String invoiceGradeId);
	ActionResult<InvoiceGrade> GetById(String invoiceGradeId);
	ActionResult<List<InvoiceGrade>> GetByInvoiceId(String invoiceId);

}