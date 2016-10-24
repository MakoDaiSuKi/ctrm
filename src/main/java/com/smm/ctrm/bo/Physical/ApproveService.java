package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface ApproveService {
	Criteria GetCriteria();

	ActionResult<List<Approve>> Approves();

	ActionResult<Approve> GetById(String id);

	ActionResult<List<Approve>> ApproveHistoryByCustomerId(String customerId);

	ActionResult<List<Approve>> ApproveHistoryByContractId(String contractId);

	ActionResult<List<Approve>> ApproveHistoryByFundId(String fundId);

	ActionResult<String> ApproveCustomer4Winform(Approve approve);

	ActionResult<String> ApproveContract4Winform(Approve approve);

	ActionResult<String> ApprovePayment4Winform(Approve approve);

	List<Approve> Approves(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String orderSort);

	ActionResult<String> ApproveMakeShip4Winform(Approve approve);

	ActionResult<List<Approve>> ApproveHistoryByReceiptShipId(String customerId);

	ActionResult<String> ApproveInvoice4Winform(Approve approve);

	ActionResult<List<Approve>> ApproveHistoryByInvoiceId(String invoiceId);


}