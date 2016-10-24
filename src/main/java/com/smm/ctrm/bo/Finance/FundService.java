package com.smm.ctrm.bo.Finance;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
public interface FundService {
	Criteria GetCriteria();
	ActionResult<Fund> PaymentConfirmed(Fund fund);
	ActionResult<String> SaveReceive(Fund fund);
	ActionResult<String> Payment(Fund fund);
	ActionResult<String> Delete(String id, String userId);
	ActionResult<List<Fund>> FundsByContractId(String contractId);
	ActionResult<List<Fund>> FundsByLotId(String lotId);
	ActionResult<List<Fund>> FundsByInvoiceId(String InvoiceId);
	ActionResult<Fund> GetById(String fundId);
	ActionResult<Fund> CreatePaymentDraft(Fund fund);
	List<Fund> Funds(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy, String orderBy);
	ActionResult<Fund> CreatePaymentDraft4Lot(Fund fund);
	void updateFundOfLotByStorage(Lot lot, Fund fund);
	void updateFundOfLotByInvoice(Lot lot, Fund fund);

}