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
import com.smm.ctrm.domain.apiClient.CustomerBalanceDetailParams;
public interface CustomerBalanceService {
	Criteria GetCriteria();
	ActionResult<String> AmendInitBalance(CustomerBalance customerBalance);
	ActionResult<String> Delete(String id);
	ActionResult<String> DeleteCustomerBalances(List<CustomerBalance> customerBalances);
	ActionResult<List<CustomerBalance>> CustomerBalances();
	ActionResult<CustomerBalance> GetById(String id);
	void ScheduledUpdateCustomerBalance();
	void UpdateCustomerBalance(String legalId, String customerId, String commodityId);
	ActionResult<String> ImportInitCustomerBalance(List<CustomerBalance> customerBalances);
	ActionResult<String> Reconciliation();
	CustomerBalance2 PagerDetail2(CustomerBalanceDetailParams param);
	List<CustomerBalance> PagerDetail(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,RefUtil total);
	CustomerBalance assemblingBean(CustomerBalance data);
	List<String> GetCustomersIdByCreatedId(List<String> usersid);
	ActionResult<List<CustomerBalance_new>> getCustomerBalance(CustomerBalanceDetailParams param);
	void getCustomerBalanceForJob(CustomerBalanceDetailParams param);
	void clearSession();
	ActionResult<CustomerBalance_new> Save(CustomerBalance_new customerBalance);
}