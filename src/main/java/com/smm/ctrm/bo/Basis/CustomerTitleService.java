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
public interface CustomerTitleService {
	ActionResult<CustomerTitle> Save(CustomerTitle customerTitle);
	ActionResult<String> Delete(String id);
	ActionResult<List<CustomerTitle>> CustomerTitles();
	ActionResult<CustomerTitle> GetById(String id);
	ActionResult<List<CustomerTitle>> GetTitlesByCustomerId(String customerId);

}