package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface CustomerService {
	Criteria GetCriteria();
	ActionResult<Customer> Save(Customer customer);
	ActionResult<String> Delete(String customerId, String userId);
	ActionResult<Customer> GetById(String id);

    List<Customer> getListByName(String name);
	List<Customer> Top10CustomerByProfit();
	List<Customer> GetByCustomerName(String customerName);
	List<Customer> CustomersCustomersIncInternals();
	List<Customer> CustomersCustomersExcInternals();
	List<Customer> InternalCustomersOnly();
	List<Customer> ImportCustomers();
	List<Customer> Top10CustomerByTurnover();
	List<Customer> Customers(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);
}