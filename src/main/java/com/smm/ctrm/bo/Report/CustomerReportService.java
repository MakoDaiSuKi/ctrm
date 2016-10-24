package com.smm.ctrm.bo.Report;

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
public interface CustomerReportService {
	ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4MultiCustomers(List<Customer> customers);
	ActionResult<List<ModelCustomerBalanceL1>> PagerCustomerBalance4OneCustomer(String customerId);

}