package com.smm.ctrm.bo.Finance;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.CInvoice;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.InvoicePnL;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Param4InvoicePnL;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;


public interface InvoiceService {
	Criteria GetCriteria();

	ActionResult<Invoice> Save(Invoice invoice);

	ActionResult<Invoice> Save4SummaryNoteFee(Invoice invoice);

	ActionResult<Invoice> Save4MultiLots(Invoice invoice);

	ActionResult<Invoice> GetById(String invoiceId);

	ActionResult<InvoicePnL> InvoicePnLById(String invoiceId);

	ActionResult<List<Invoice>> InvoicesByContractId(String contractId);

	ActionResult<List<Invoice>> InvoicesByLotId(String lotId);

	ActionResult<List<Invoice>> InvoicesByCustomerId(String customerId);

	ActionResult<Boolean> IsInvoiceNoDuplicate(Invoice CurInvocie);

	ActionResult<InvoicePnL> InvoiceSettleTrial(Param4InvoicePnL param4InvoicePnL);

	ActionResult<InvoicePnL> InvoiceSettleOfficial(Param4InvoicePnL param4InvoicePnL);

	ActionResult<Invoice> InvalidInvoiceById(Invoice invoice);

	List<Invoice> Invoices4Sql(String filter);

	List<Lot> LotsById(String lotId);

	List<Invoice> Invoices(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total);

	List<Storage> getStorageListByInvoiceId(String invoiceId);

	Criteria GetCriteria2();

	List<CInvoice> Invoices2(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total);

	ActionResult<List<Invoice>> StorageToInvoiceSave(List<Invoice> invoices);

	ActionResult<List<Invoice>> StoragesInvoiceById(String invoiceId);

	ActionResult<String> DeleteForStoragesInvoice(List<Invoice> invoices);

	ActionResult<String> Delete(Invoice invoice);


}