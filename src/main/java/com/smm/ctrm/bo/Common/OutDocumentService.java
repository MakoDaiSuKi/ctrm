package com.smm.ctrm.bo.Common;

import com.smm.ctrm.bo.impl.Basis.ExportDoc;
import com.smm.ctrm.domain.apiClient.OutDocumentFile;

public interface OutDocumentService {

	OutDocumentFile CreateContractPDF(OutDocumentFile odf, String contractId);

	OutDocumentFile CreateShipDoc(OutDocumentFile odf, String receiptShipId, ExportDoc docInfo);

	OutDocumentFile CreatePaymentApprovePDF(OutDocumentFile odf, String fundId);

	OutDocumentFile CreateInvoiceDoc(OutDocumentFile odf, String invoiceId);

	OutDocumentFile CreateContractDoc(OutDocumentFile odf, String contractId);

	ExportDoc buildExportDocByDictionaryId(String dictionaryId);

	/**
	 * @param docName
	 * @return null 没有配置 ， true 删除旧文件，false 不删除
	 */
	Boolean IsReplaceOldDoc(String docName);

	OutDocumentFile CreatePricingDoc(OutDocumentFile odf, String id, ExportDoc docInfo);
	
	OutDocumentFile CreatePositionDoc(OutDocumentFile odf, String id, ExportDoc docInfo);
	
}
