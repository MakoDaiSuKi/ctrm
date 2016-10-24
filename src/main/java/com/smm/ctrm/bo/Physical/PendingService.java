package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface PendingService {
	ActionResult<List<Pending>> PendingsByCustomerId(String customerId);

	boolean CancelByCustomer(Customer impCustomer, String userId, StringBuilder message);

	ActionResult<String> CancelByCustomerId(String customerId, String userId);

	boolean CancelByContract(Contract impContract, String userId, StringBuilder message);

	ActionResult<String> CancelByContractId(String contractId, String userId);

	boolean CancelByFund(Fund impFund, String userId, StringBuilder message);

	ActionResult<String> CancelByFundId(String fundId, String userId);

	ActionResult<List<Pending>> PendingsByContractId(String contractId);

	ActionResult<List<Pending>> PendingsByFundId(String fundId);

	Criteria GetCriteria();

	ActionResult<List<Pending>> ApprovePendings();

	ActionResult<Pending> Ask4Approve(Pending pending, String userId);

	ActionResult<Pending> GetById(String pendingId);

	ActionResult<Customer> CustomerByPendingId(String pendingId);

	ActionResult<Contract> ContractByPendingId(String pendingId);

	ActionResult<Fund> FundByPendingId(String pendingId);

	List<Pending> PendingPager(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil refTotal);

	ActionResult<String> CancelByInvoice(Invoice invoice, String userId, StringBuilder message);

	ActionResult<String> CancelByShip(ReceiptShip invoice, String userId);

	ActionResult<List<Pending>> PendingsByInvoiceId(String invoiceId);
	
	ActionResult<List<Pending>> PendingsByShipId(String shipId);

}