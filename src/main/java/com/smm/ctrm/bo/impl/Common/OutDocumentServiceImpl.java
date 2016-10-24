package com.smm.ctrm.bo.impl.Common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.IRunBody;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.OutDocumentService;
import com.smm.ctrm.bo.impl.Basis.ExportDoc;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBank;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Physical.Attachment;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.StorageOutBill4SM;
import com.smm.ctrm.domain.apiClient.InvoiceParams;
import com.smm.ctrm.domain.apiClient.OutDocument;
import com.smm.ctrm.domain.apiClient.OutDocumentFile;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.FileUtil;
import com.smm.ctrm.util.XWPFUtil;
import com.smm.ctrm.util.Result.AttachType;
import com.smm.ctrm.util.Result.SpotType;

@Service
public class OutDocumentServiceImpl implements OutDocumentService {

	private static Logger logger = Logger.getLogger(OutDocumentServiceImpl.class);

	@Resource
	private CommonService commService;

	@Resource
	HibernateRepository<Brand> brandRepo;

	@Resource
	HibernateRepository<Origin> originRepo;

	@Resource
	HibernateRepository<Product> productRepo;

	@Resource
	HibernateRepository<Invoice> invoiceRepo;

	@Resource
	HibernateRepository<Contract> contractRepo;

	@Resource
	HibernateRepository<CustomerBank> customerBankRepo;

	@Resource
	HibernateRepository<Lot> lotRepo;

	@Resource
	HibernateRepository<Pending> pendingRepo;

	@Resource
	HibernateRepository<Fund> fundRepo;

	@Resource
	HibernateRepository<Storage> storageRepo;

	@Resource
	HibernateRepository<ReceiptShip> receiptShipRepo;

	@Resource
	HibernateRepository<Commodity> commodityRepo;

	@Resource
	HibernateRepository<Warehouse> warehouseRepo;

	@Resource
	HibernateRepository<Legal> legalRepo;

	@Resource
	HibernateRepository<Spec> specRepo;

	@Resource
	HibernateRepository<Customer> customerRepo;

	@Resource
	HibernateRepository<Dictionary> dictionaryRepo;

	@Resource
	HibernateRepository<Pricing> pricingRepo;

	@Resource
	HibernateRepository<Position> positionRepo;

	@Override
	public OutDocumentFile CreateInvoiceDoc(OutDocumentFile odf, String invoiceId) {
		if (invoiceId == null)
			return new OutDocumentFile(false, "当前发票信息不存在！");
		Invoice curInvoice = invoiceRepo.getOneById(invoiceId, Invoice.class);
		if (curInvoice == null)
			return new OutDocumentFile(false, "当前发票信息不存在！");
		if (curInvoice.getLot() == null && curInvoice.getLotId() != null) {
			curInvoice.setLot(lotRepo.getOneById(curInvoice.getLotId(), Lot.class));
		}
		Storage storage = null;
		if (curInvoice.getStorages() != null && curInvoice.getStorages().size() > 0) {
			storage = curInvoice.getStorages().get(0);
		}
		if (curInvoice.getLot() != null && curInvoice.getLot().getOrigin() == null
				&& curInvoice.getLot().getOriginId() != null) {
			curInvoice.getLot().setOrigin(originRepo.getOneById(curInvoice.getLot().getOriginId(), Origin.class));
		}
		Storage bviSource = null;
		if (storage != null && storage.getBviSourceId() != null) {
			bviSource = storageRepo.getOneById(storage.getBviSourceId(), Storage.class);
		} else {
			bviSource = storage;
		}

		OutDocumentFile returnMsg = OutputCheckFile(odf, curInvoice.getInvoiceNo());
		if (!returnMsg.isSuccess())
			return returnMsg;
		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocToPDFExt);
		try {
			String filePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			OutDocumentFile returnCreate = OutputDocument(returnMsg.getTempPath(), filePath, curInvoice.getLot(),
					storage, curInvoice, curInvoice.getLot().getOrigin(), bviSource);
			if (!returnCreate.isSuccess())
				return returnCreate;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	@Override
	public ExportDoc buildExportDocByDictionaryId(String dictionaryId) {
		if (dictionaryId == null)
			return new ExportDoc();
		Dictionary dictionary = dictionaryRepo.getOneById(dictionaryId, Dictionary.class);
		ExportDoc docInfo = new ExportDoc();
		docInfo.setTemplateName(dictionary.getValue());
		List<Dictionary> propertyDictionaryList = dictionaryRepo.GetQueryable(Dictionary.class)
				.where(DetachedCriteria.forClass(Dictionary.class).add(Restrictions.eq("Name", dictionary.getName())))
				.toList();
		for (Dictionary property : propertyDictionaryList) {
			if (property.getCode().equals(Dictionary.REPLACE_OLD_DOC) && property.getValue().equalsIgnoreCase("Y")) {
				docInfo.setIsOverwrite(true);
			}
		}
		return docInfo;
	}

	@Override
	public Boolean IsReplaceOldDoc(String docName) {
		List<Dictionary> propertyDictionaryList = dictionaryRepo.GetQueryable(Dictionary.class)
				.where(DetachedCriteria.forClass(Dictionary.class).add(Restrictions.eq("Name", docName))).toList();
		for (Dictionary property : propertyDictionaryList) {
			if (property.getCode().equals(Dictionary.REPLACE_OLD_DOC) && property.getValue().equals("Y")) {
				return true;
			} else {
				return false;
			}
		}
		return null;
	}

	@Override
	public OutDocumentFile CreateShipDoc(OutDocumentFile odf, String receiptShipId, ExportDoc docInfo) {
		if (receiptShipId == null) {
			return new OutDocumentFile(false, "当前提货单信息不存在！");
		}
		ReceiptShip curReceiptShip = receiptShipRepo.getOneById(receiptShipId, ReceiptShip.class);
		if (curReceiptShip == null)
			return new OutDocumentFile(false, "当前提货单信息不存在！");
		if (curReceiptShip.getStorages() == null || curReceiptShip.getStorages().size() == 0) {
			curReceiptShip.setStorages(storageRepo.GetQueryable(Storage.class).where(
					DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("RefId", curReceiptShip.getId())))
					.toList());
		}
		OutDocumentFile returnMsg = OutputCheckFile(odf, curReceiptShip.getReceiptShipNo());
		if (!returnMsg.isSuccess())
			return returnMsg;

		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocTemplateExt);

		try {
			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));
			OutDocumentFile returnCreate = OutputDocumentShipDoc(returnMsg.getTemporarilyFilePath(), destFilePath,
					curReceiptShip, docInfo);
			if (returnCreate.isSuccess()) {
				if (docInfo.getIsOverwrite()) {
					commService.DeleteAttachmentByBillId(ActionStatus.AttachType_ReceiptShip, true, receiptShipId);
				}
			} else {
				return returnCreate;
			}
		} catch (Exception ex) {
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	@Override
	public OutDocumentFile CreateContractDoc(OutDocumentFile odf, String contractId) {
		if (contractId == null) {
			return new OutDocumentFile(false, "当前合同信息不存在！");
		}
		Contract curContract = contractRepo.getOneById(contractId, Contract.class);
		if (curContract == null) {
			return new OutDocumentFile(false, "当前合同信息不存在！");
		}
		if (curContract.getLots() == null) {
			curContract.setLots(lotRepo.GetQueryable(Lot.class)
					.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("ContractId", curContract.getId())))
					.toList());
		}

		OutDocumentFile returnMsg = OutputCheckFile(odf, curContract.getHeadNo());
		if (!returnMsg.isSuccess()) {
			return returnMsg;
		}

		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocTemplateExt);

		try {
			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));
			OutDocumentFile returnCreate = OutputDocumentContractDoc(returnMsg.getTemporarilyFilePath(), destFilePath,
					curContract);
			Boolean isReplace = IsReplaceOldDoc("合同");
			if (returnCreate.isSuccess() && (isReplace == null || isReplace)) {
				commService.DeleteAttachmentByBillId(ActionStatus.AttachType_ContractDoc, true, contractId);
			} else {
				return returnCreate;
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	@Override
	public OutDocumentFile CreateContractPDF(OutDocumentFile odf, String contractId) {
		Contract curContract = contractRepo.getOneById(contractId, Contract.class);
		if (curContract == null) {
			return new OutDocumentFile(false, "当前合同信息不存在！");
		}
		curContract = commService.SimplifyData(curContract);

		OutDocumentFile returnMsg = OutputCheckFile(odf, curContract.getHeadNo());
		if (!returnMsg.isSuccess())
			return returnMsg;

		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocToPDFExt);

		try {
			String filePDFPath = returnMsg.getFileFolder() + "/" + returnMsg.getFilePDFFullName();

			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));
			OutDocumentFile returnCreate = OutputContractDocToPDF(returnMsg.getTemporarilyFilePath(), destFilePath,
					returnMsg.getFilePDFFullName(), curContract);
			if (!returnCreate.isSuccess())
				return returnCreate;

			FileUtil.wordConvertToPdf(destFilePath, filePDFPath);
			new File(destFilePath).delete();
		} catch (Exception ex) {
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}

		return returnMsg;
	}

	/**
	 * 生成合同PDF 合同审批生成PDF
	 * 
	 * @param odf
	 * @param contractId
	 * @return
	 */
	@Override
	public OutDocumentFile CreatePaymentApprovePDF(OutDocumentFile odf, String fundId) {
		if (fundId == null)
			return new OutDocumentFile(false, "当前付款信息不存在！");
		Fund curFund = fundRepo.getOneById(fundId, Fund.class);
		if (curFund == null) {
			return new OutDocumentFile(false, "当前付款信息不存在！");
		}
		curFund = commService.SimplifyData(curFund);
		if (curFund.getCustomerTitleId() != null && curFund.getCustomerBank() == null) {
			curFund.setCustomerBank(
					customerBankRepo.GetQueryable(CustomerBank.class)
							.where(DetachedCriteria.forClass(CustomerBank.class)
									.add(Restrictions.eq("CustomerTitleId", curFund.getCustomerTitleId())))
							.firstOrDefault());
		}

		String headNo = curFund.getFullNo().replace("/", "_");
		OutDocumentFile returnMsg = OutputCheckFile(odf, headNo);
		if (!returnMsg.isSuccess())
			return returnMsg;
		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocToPDFExt);
		try {

			String filePDFPath = returnMsg.getFileFolder() + "/" + returnMsg.getFilePDFFullName();
			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));
			OutDocumentFile returnCreate = OutPutPaymentDocToPDF(returnMsg.getTemporarilyFilePath(), destFilePath,
					returnMsg.getFilePDFFullName(), curFund);
			if (!returnCreate.isSuccess())
				return returnCreate;

			FileUtil.wordConvertToPdf(destFilePath, filePDFPath);
			new File(destFilePath).delete();
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	@Override
	public OutDocumentFile CreatePricingDoc(OutDocumentFile odf, String pricingId, ExportDoc docInfo) {

		if (StringUtils.isBlank(pricingId)) {
			return new OutDocumentFile(false, "当前点价信息不存在！");
		}
		Pricing curPricing = pricingRepo.getOneById(pricingId, Pricing.class);
		if (curPricing == null)
			return new OutDocumentFile(false, "当前点价信息不存在！");
		String headNo = curPricing.getLot().getFullNo().replace("/", "_");
		OutDocumentFile returnMsg = OutputCheckFile(odf, headNo);
		if (!returnMsg.isSuccess())
			return returnMsg;

		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocTemplateExt);

		try {
			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));
			OutDocumentFile returnCreate = OutputDocumentPricingDoc(returnMsg.getTemporarilyFilePath(), destFilePath,
					curPricing, docInfo);
			if (returnCreate.isSuccess()) {
				if (docInfo.getIsOverwrite()) {
					commService.DeleteAttachmentByBillId(ActionStatus.AttachType_Pricing, true, pricingId);
				}
			} else {
				return returnCreate;
			}
		} catch (Exception ex) {
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	@Override
	public OutDocumentFile CreatePositionDoc(OutDocumentFile odf, String positionId, ExportDoc docInfo) {

		if (StringUtils.isBlank(positionId)) {
			return new OutDocumentFile(false, "当前头寸信息不存在！");
		}
		Position curPosition = positionRepo.getOneById(positionId, Position.class);

		if (curPosition == null)
			return new OutDocumentFile(false, "当前头寸信息不存在！");

		String headNo = curPosition.getFullNo() == null ? "" : curPosition.getFullNo().replace("/", "_");

		OutDocumentFile returnMsg = OutputCheckFile(odf, headNo);
		if (!returnMsg.isSuccess())
			return returnMsg;

		returnMsg.setFilePDFFullName(returnMsg.getFileName() + OutDocument.OutDocTemplateExt);

		try {
			String destFilePath = returnMsg.getFileFolder() + "/" + returnMsg.getFileFullName();
			// 模版文件拷贝到新文件
			FileUtils.copyFile(new File(returnMsg.getTempPath()), new File(returnMsg.getTemporarilyFilePath()));

			OutDocumentFile returnCreate = OutputDocumentPositionDoc(returnMsg.getTemporarilyFilePath(), destFilePath,
					curPosition, docInfo);
			if (returnCreate.isSuccess()) {
				if (docInfo.getIsOverwrite()) {
					commService.DeleteAttachmentByBillId(ActionStatus.AttachType_Pricing, true, positionId);
				}
			} else {
				return returnCreate;
			}
		} catch (Exception ex) {
			return new OutDocumentFile(false, "生成文件失败:" + ex.getMessage());
		}
		return returnMsg;
	}

	private OutDocumentFile OutputDocumentPricingDoc(String temporarilyFilePath, String destFilePath,
			Pricing curPricing, ExportDoc docInfo) {
		if (curPricing == null) {
			return new OutDocumentFile(false, "当前点价信息为空，无法生成");
		}
		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			Map<String, String> replaceTextMap = getReplaceKeyValueForPricingDocx(curPricing);
			GetBookMarkStarts(docx, replaceTextMap);
			docx.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败" + ex.getMessage());
		} finally {
			try {
				if (docx != null)
					docx.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}
		return new OutDocumentFile(true, "");
	}

	private OutDocumentFile OutputDocumentPositionDoc(String temporarilyFilePath, String destFilePath,
			Position curPosition, ExportDoc docInfo) {
		if (curPosition == null) {
			return new OutDocumentFile(false, "当前头寸信息为空，无法生成");
		}
		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			Map<String, String> replaceTextMap = getReplaceKeyValueForPositionDocx(curPosition);
			GetBookMarkStarts(docx, replaceTextMap);
			docx.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败" + ex.getMessage());
		} finally {
			try {
				if (docx != null)
					docx.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}
		return new OutDocumentFile(true, "");
	}

	private Map<String, String> getReplaceKeyValueForPricingDocx(Pricing curPricing) {
		Map<String, String> map = new HashMap<>();
		if (curPricing.getCustomer() != null) {
			Customer customer = curPricing.getCustomer();
			map.put("CustomerName", customer.getName());
			map.put("ENCustomerName", customer.getEnName());
			map.put("SignerENCustomer",
					StringUtils.isNotBlank(customer.getEnName()) ? customer.getName() : customer.getEnName());
			map.put("CNCustomerName",
					StringUtils.isNoneBlank(customer.getCnName()) ? customer.getName() : customer.getCnName());
			map.put("SignerCNCustomer",
					StringUtils.isNoneBlank(customer.getCnName()) ? customer.getName() : customer.getCnName());
		}
		if (curPricing.getLegal() != null && curPricing.getLegal().getCustomerId() != null) {
			Customer legalCustomer = customerRepo.getOneById(curPricing.getLegal().getCustomerId(), Customer.class);
			map.put("SignerENLegalName", StringUtils.isNotBlank(legalCustomer.getEnName()) ? legalCustomer.getName()
					: legalCustomer.getEnName());
			map.put("SignerCNLegalName", StringUtils.isNotBlank(legalCustomer.getCnName()) ? legalCustomer.getName()
					: legalCustomer.getCnName());
		}
		map.put("CNTradeDate", DateFormatUtils.format(curPricing.getTradeDate(), "yyyy-MM-dd"));
		map.put("ENTradeDate", DateFormatUtils.format(curPricing.getTradeDate(), "yyyy-MM-dd"));
		Contract contract = curPricing.getContract();
		if (contract != null) {
			map.put("CNHeadNo", contract.getHeadNo());
			map.put("ENHeadNo", contract.getHeadNo());
			map.put("CNHeadNo2", contract.getHeadNo());
			map.put("ENHeadNo2", contract.getHeadNo());
		}
		map.put("ENQuantity", curPricing.getQuantity().setScale(4, RoundingMode.HALF_UP).toPlainString());
		map.put("CNQuantity", curPricing.getQuantity().setScale(4, RoundingMode.HALF_UP).toPlainString());
		map.put("Today", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		map.put("LsTradeDate", DateFormatUtils.format(curPricing.getTradeDate(), "yyyy-MM-dd"));
		map.put("LsQuantity", curPricing.getQuantity().setScale(4, RoundingMode.HALF_UP).toPlainString());
		map.put("LsPrice", DecimalUtil.nullToZero(curPricing.getMajor())
				.add(DecimalUtil.nullToZero(curPricing.getFee())).setScale(2, RoundingMode.HALF_UP).toPlainString());
		map.put("LsMajor",
				DecimalUtil.nullToZero(curPricing.getMajor()).setScale(2, RoundingMode.HALF_UP).toPlainString());
		map.put("LsFee", DecimalUtil.nullToZero(curPricing.getFee()).setScale(2, RoundingMode.HALF_UP).toPlainString());
		map.put("ENPricer",
				curPricing.getPricer() == null ? ""
						: (curPricing.getPricer().equalsIgnoreCase("US") ? "mine"
								: (curPricing.getPricer().equalsIgnoreCase("COUNTERPARTY") ? "your" : "")));
		map.put("CNPricer",
				curPricing.getPricer() == null ? ""
						: (curPricing.getPricer().equalsIgnoreCase("US") ? "我方"
								: (curPricing.getPricer().equalsIgnoreCase("COUNTERPARTY") ? "你方" : "")));
		return map;
	}

	private Map<String, String> getReplaceKeyValueForPositionDocx(Position curPosition) {
		Map<String, String> map = new HashMap<>();
		map.put("CustomerName", curPosition.getLot() == null ? "" : curPosition.getLot().getCustomerName());
		String customerCNName = curPosition.getLot() == null ? ""
				: curPosition.getLot().getCustomer() == null ? "" : curPosition.getLot().getCustomer().getCnName();
		map.put("CustomerCNName", customerCNName);
		map.put("LegalName", curPosition.getLegal() == null ? "" : curPosition.getLegal().getName());
		String tradeDate = DateFormatUtils.format(curPosition.getTradeDate(), "yyyy-MM-dd");
		map.put("TradeDate", tradeDate);

		map.put("TradeDate1", tradeDate);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(curPosition.getPromptDate());
		String tempDayOfWeek = DateUtil.getEngWeek(calendar.get(Calendar.DAY_OF_WEEK));
		String[] wedToSat = new String[] { "Wednesday", "Thursday", "Friday", "Saturday" };
		String[] monToTue = new String[] { "Monday", "Tuesday" };

		if (Arrays.asList(wedToSat).contains(tempDayOfWeek)) {
			calendar.add(Calendar.DAY_OF_MONTH, -2);
			map.put("TradeDate2", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
		} else if (Arrays.asList(monToTue).contains(tempDayOfWeek)) {
			calendar.add(Calendar.DAY_OF_MONTH, -4);
			map.put("TradeDate2", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
		} else // if(tempDayOfWeek == "Sunday")
		{
			calendar.add(Calendar.DAY_OF_MONTH, -3);
			map.put("TradeDate2", DateFormatUtils.format(calendar.getTime(), "yyyy-MM-dd"));
		}
		map.put("TradeDate3", tradeDate);
		map.put("TradeDate4", tradeDate);
		map.put("FullNo", curPosition == null ? "" : curPosition.getFullNo());
		map.put("Today", DateFormatUtils.format(new Date(), "yyyy-MM-dd"));
		map.put("Quantity", curPosition.getQuantity().abs().setScale(2, RoundingMode.HALF_UP).toPlainString());
		map.put("AbsQuantity", curPosition.getQuantity().abs().setScale(2, RoundingMode.HALF_UP).toPlainString());
		map.put("Direction", curPosition.getQuantity().compareTo(BigDecimal.ZERO) < 0 ? "卖出" : "买入");
		map.put("Currency", curPosition.getCurrency());
		map.put("Price", curPosition.getOurPrice() == null ? ""
				: DecimalUtil.getThousandsSeparator(curPosition.getOurPrice(), 2));
		map.put("PromptDate", DateFormatUtils.format(curPosition.getPromptDate(), "yyyy-MM-dd"));
		return map;
	}

	/**
	 * 根据路径文件路径获取所有的书签
	 * 
	 * @param path
	 * @param docx
	 * @return
	 */
	public static void GetBookMarkStarts(XWPFDocument docx, Map<String, String> keyValueMap) {
		try {
			for (XWPFTable table : docx.getTables()) {
				for (XWPFTableRow row : table.getRows()) {
					for (XWPFTableCell cell : row.getTableCells()) {
						handleXWPFParagraph(cell.getParagraphs(), keyValueMap);
					}
				}
			}
			handleXWPFParagraph(docx.getParagraphs(), keyValueMap);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private static void handleXWPFParagraph(List<XWPFParagraph> paras, Map<String, String> keyValueMap) {
		for (XWPFParagraph para : paras) {
			for (CTBookmark bookmark : para.getCTP().getBookmarkStartList()) {
				String bmName = bookmark.getName();
				// 格式：z_唯一占位符__实际书签名称 ，例如：z_woshitest__CustomerName
				// 目的是为了解决同一个文档，输出同一内容在文档不同位置
				if (bmName.startsWith("z_")) {
					bmName = bmName.substring(bmName.indexOf("__") + 2);
				}
				int runNum = 0;
				Node nextNode = bookmark.getDomNode().getNextSibling();
				while (nextNode != null) {
					if (nextNode.getNodeName().equals("w:r")) {
						runNum++;
					}
					nextNode = nextNode.getNextSibling();
				}
				XWPFRun run = para.insertNewRun(para.getRuns().size() - runNum);
				String text = keyValueMap.get(bmName);
				if (StringUtils.isNotBlank(text)) {
					run.setText(text);
					CTR ctr = run.getCTR();
					if (!ctr.isSetRPr()) {
						ctr.addNewRPr();
					}
					if (para.getCTP().isSetPPr()) {
						CTPPr ppr = para.getCTP().getPPr();
						if (ppr.isSetRPr()) {
							if (ppr.getRPr().isSetFitText()) {
								ctr.getRPr().setRFonts(ppr.getRPr().getRFonts());
							}
							if (ppr.getRPr().isSetB()) {
								ctr.getRPr().setB(ppr.getRPr().getB());
							}
							if (ppr.getRPr().isSetBCs()) {
								ctr.getRPr().setBCs(ppr.getRPr().getBCs());
							}
							if (ppr.getRPr().isSetSz()) {
								ctr.getRPr().setSz(ppr.getRPr().getSz());
							}
							if (ppr.getRPr().isSetSzCs()) {
								ctr.getRPr().setSzCs(ppr.getRPr().getSzCs());
							}
						}
					}
				}
				// Node nextNode = bookmark.getDomNode().getNextSibling();
				// while (nextNode != null &&
				// !(nextNode.getNodeName().contains("bookmarkEnd"))) {
				// para.getCTP().getDomNode().removeChild(nextNode);
				// nextNode = bookmark.getDomNode().getNextSibling();
				// }
				// if(nextNode == null) continue;
				// para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(),
				// nextNode);
				// para.getCTP().getDomNode().appendChild(run.getCTR().getDomNode());
			}
		}
	}

	public OutDocumentFile OutPutPaymentDocToPDF(String temporarilyFilePath, String destFilePath, String filePDFName,
			Fund curFund) {
		// 按照模版格式，生成临时文件
		XWPFDocument xmlDoc = null;
		try {
			xmlDoc = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			List<Pending> curPendings = pendingRepo.GetQueryable(Pending.class)
					.where(DetachedCriteria.forClass(Pending.class).add(Restrictions.eq("FundId", curFund.getId())))
					.toList();

			curPendings = commService.SimplifyDataPendingList(curPendings);
			Pending curPending = null;
			if (curPendings != null && curPendings.size() > 0) {
				curPending = curPendings.get(0);
			}

			if (curPending == null) {
				return new OutDocumentFile(false, "没有找到匹配的审核记录");
			}
			Map<String, String> replaceTextMap = getReplaceKeyValueForFundDocx(curFund, curPending, filePDFName);
			GetBookMarkStarts(xmlDoc, replaceTextMap);
			xmlDoc.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败");
		} finally {
			try {
				if (xmlDoc != null)
					xmlDoc.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}

		return new OutDocumentFile(true, "");
	}

	private Map<String, String> getReplaceKeyValueForFundDocx(Fund curFund, Pending curPending, String filePDFName) {
		Map<String, String> map = new HashMap<>();
		Invoice invoiceStorage = null;
		if (curFund.getInvoiceId() != null) {
			invoiceStorage = invoiceRepo.getOneById(curFund.getInvoiceId(), Invoice.class);
		}
		map.put("FullNo", curFund.getFullNo());
		map.put("HeadNo", curFund.getContract() == null ? "" : curFund.getContract().getHeadNo());
		map.put("DivisionName", curPending.getAskerDivisionName()); // 送审部门
		map.put("PenderName", curPending.getAskerName()); // 送审人
		map.put("TradeDate", DateUtil.doFormatDate(curPending.getTradeDate(), "yyyy年MM月dd日"));// 送审日期
		map.put("LastExecuteDate", curFund.getLastExecuteDate() != null
				? DateUtil.doFormatDate(curFund.getLastExecuteDate(), "yyyy年MM月dd日") : "");// 最迟付款日期
		map.put("CustomerName", curFund.getCustomerName()); // 客户名称
		map.put("CustomerBank", curFund.getCustomerBank() != null ? curFund.getCustomerBank().getBankNote() : "");
		if (curFund.getInvoiceId() != null) {
			CustomerBank bank = customerBankRepo.getOneById(invoiceStorage.getCustomerBankId(), CustomerBank.class);
			if (bank != null) {
				map.put("InvoiceBank", bank.getBankNote());
			}
		}

		map.put("Amount", DecimalUtil.getThousandsSeparator(curFund.getAmount(), 2));
		map.put("AmountCapital", commService.MoneyLowerToLarge(curFund.getAmount()));
		map.put("Comments", curFund.getComments());
		map.put("Description", FundStorageDescription(invoiceStorage));
		map.put("Asker", curPending.getAskerName());
		map.put("FileName", filePDFName);
		map.put("LegalName", curFund.getLegalName());
		return map;
	}

	private OutDocumentFile OutputContractDocToPDF(String temporarilyFilePath, String destFilePath,
			String filePDFFullName, Contract curContract) {

		XWPFDocument xmlDoc = null;
		try {
			xmlDoc = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			List<Pending> curPendings = pendingRepo.GetQueryable(Pending.class).where(
					DetachedCriteria.forClass(Pending.class).add(Restrictions.eq("ContractId", curContract.getId())))
					.toList();

			curPendings = commService.SimplifyDataPendingList(curPendings);

			Map<String, String> replaceTextMap = getReplaceKeyValueForContractPDF(curContract, curPendings,
					filePDFFullName);
			GetBookMarkStarts(xmlDoc, replaceTextMap);
			AddPendings(xmlDoc, curPendings);
			xmlDoc.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败");
		} finally {
			try {
				if (xmlDoc != null)
					xmlDoc.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}
		return new OutDocumentFile(true, "");
	}

	private void AddPendings(XWPFDocument xmlDoc, List<Pending> pengdings) {
		if (pengdings.size() == 0)
			return;
		XWPFTable table = GetTableByIndex(xmlDoc, 0);
		Map<String, String> map = new HashMap<>();
		for (Pending pending : pengdings) {
			if (map.containsKey(pending.getApproverDivisionId()))
				continue;
			map.put(pending.getApproverDivisionId(), pending.getApproverDivisionName());
		}

		String[] tableColumns = { "姓名", "时间", "结论" };

		int cloneRowNum = table.getNumberOfRows() - 1;
		int from = 0;
		int to = 0;
		for (Entry<String, String> entry : map.entrySet()) {
			if (entry.getKey() == null)
				continue;
			String tmpDivision = entry.getValue() + "/评审意见";
			from = table.getNumberOfRows();
			XWPFTableRow addRowHead = table.insertNewTableRow(table.getNumberOfRows());
			XWPFUtil.CopytTableRow(table.getRow(cloneRowNum), addRowHead);
			List<XWPFTableCell> headCells = addRowHead.getTableCells();
			SetCellText(headCells.get(0), tmpDivision);
			SetCellText(headCells.get(1), tableColumns[0]);
			SetCellText(headCells.get(2), tableColumns[1]);
			SetCellText(headCells.get(3), tableColumns[2]);

			// 增加审批行
			for (Pending pending : pengdings) {
				if (pending.getApproverDivisionId().equalsIgnoreCase(entry.getKey())) {
					to = table.getNumberOfRows();
					XWPFTableRow addRowBody = table.insertNewTableRow(table.getNumberOfRows());
					XWPFUtil.CopytTableRow(table.getRow(cloneRowNum), addRowBody);
					List<XWPFTableCell> bodyCells = addRowBody.getTableCells();
					SetCellText(bodyCells.get(0), "");
					SetCellText(bodyCells.get(1), pending.getApproverName());
					SetCellText(bodyCells.get(2), pending.getApproveTradeDate() != null
							? DateUtil.doFormatDate(pending.getApproveTradeDate(), "yyyy年MM月dd日") : "");
					SetCellText(bodyCells.get(3), pending.getIsApproved() ? "通过"
							: (pendingRepo.getOneById(pending.getId(), Pending.class) == null ? "" : "不通过"));

				}
			}
			if (to > from) {
				mergeCellsVertically(table, 0, from, to);
			}
		}
		table.removeRow(cloneRowNum);

	}

	private Map<String, String> getReplaceKeyValueForContractPDF(Contract curContract, List<Pending> curPendings,
			String filePDFName) {

		Pending curPending = null;
		if (curPendings != null && curPendings.size() > 0) {
			curPending = curPendings.get(0);
		}

		List<Attachment> attachments = null;
		ActionResult<List<Attachment>> listAttr = commService.GetAttachments(AttachType.Fund, true,
				curContract.getId());
		if (listAttr.isSuccess())
			attachments = listAttr.getData();
		Map<String, String> map = new HashMap<>();
		map.put("HeadNo", curContract.getHeadNo());
		map.put("DivisionName", curPending == null ? "" : curPending.getAskerDivisionName()); // 送审部门
		map.put("PenderName", curPending == null ? "" : curPending.getAskerName());// 送审人
		map.put("TradeDate", curPending == null ? "" : DateUtil.doFormatDate(curPending.getTradeDate(), "yyyy年MM月dd日"));// 送审日期
		map.put("ContractName", curContract.getHeadNo()); // 合同名称
		map.put("CustomerTitle", curContract.getCustomerTitleName()); // 对方当事人
		map.put("TraderName", curContract.getTraderName());// 拟签署人
		map.put("Comments", curContract.getComments());
		map.put("ContractContent", ContractContent(curContract, attachments));
		if (attachments != null) {
			String attString = "";
			for (Attachment attachment : attachments) {
				if (!StringUtils.isNotBlank(attString)) {
					attString += ",";
				}
				attString += attachment.getFileName();
			}
			map.put("Attachments", attString);
		}

		map.put("FileName", filePDFName);
		return map;

	}

	private String ContractContent(Contract contract, List<Attachment> attachment) {
		StringBuilder Content = new StringBuilder();

		if (contract != null) {
			if (contract.getCommodity() == null && contract.getCommodityId() != null)
				contract.setCommodity(commodityRepo.getOneById(contract.getCommodityId(), Commodity.class));

			Content.append("合同主要内容如下：\r\n");
			Content.append("商品品种:" + (contract.getCommodity() == null ? "" : contract.getCommodity().getName())
					+ " , 交付条款:" + contract.getDeliveryTerm() + "\r\n");
			Content.append("商品名称:" + contract.getProduct() + " , 签订日期:"
					+ DateUtil.shortDateString(contract.getTradeDate()) + "\r\n");
			Content.append("币种:" + contract.getCurrency() + " , 付款方式:" + contract.getPaymentTerm() + "\r\n");
			Content.append("数量:" + contract.getQuantity().toString() + " , 检验机构:" + contract.getTestOrg() + "\r\n");
			Content.append("称重:" + contract.getNaQuantity() + " , 贸易类型:" + contract.getSpotType() + "\r\n");
			Content.append("交货期始:"
					+ (contract.getDeliveryStartDate() == null ? ""
							: DateUtil.shortDateString(contract.getDeliveryStartDate()))
					+ " , 交货期止:" + (contract.getDeliveryEndDate() == null ? ""
							: DateUtil.shortDateString(contract.getDeliveryEndDate()))
					+ "\r\n");
			Content.append("销售类型:" + (contract.getSpotDirection().toUpperCase() == "S" ? "销售" : "采购") + " , 赊账天数:"
					+ contract.getDueDays() + "\r\n");
			Content.append("点价方:" + (contract.getPricer().toUpperCase() == "US" ? "我方" : "对方") + "\r\n \r\n");
		}

		if (attachment != null && attachment.size() > 0) {
			Content.append("附件内容如下：\r\n");
			for (Attachment attachment1 : attachment) {
				Content.append(attachment1.getFileName() + "\r\n");
			}
		}
		return Content.toString();
	}

	public OutDocumentFile OutputDocumentShipDoc(String temporarilyFilePath, String destFilePath, ReceiptShip curShip,
			ExportDoc docInfo) {
		if (curShip == null) {
			return new OutDocumentFile(false, "当前提货单为空，无法生成");
		}
		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			Map<String, String> replaceTextMap = getReplaceKeyValueForReceiptShipDocx(curShip);
			GetBookMarkStarts(docx, replaceTextMap);
			if (docInfo.getTemplateName().equals("ReceiptShip_jituan")) {
				AddStorageToShipDocForBaiyin(docx, curShip, curShip.getStorages());
			} else if (docInfo.getTemplateName().equals("ReceiptShip_honglu")) {
				AddStorageToShipDoc(docx, curShip, curShip.getStorages());
			} else {
				addTitleForShipDoc(curShip, docx);
				AddStorageToShipDoc(docx, curShip, curShip.getStorages());
			}
			docx.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败" + ex.getMessage());
		} finally {
			try {
				if (docx != null)
					docx.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}
		return new OutDocumentFile(true, "");
	}

	private void addTitleForShipDoc(ReceiptShip ship, XWPFDocument docx) {
		String legalCustomerId = ship.getLot().getLegal().getCustomerId();
		if (StringUtils.isNotBlank(legalCustomerId)) {
			Customer legalCustomer = customerRepo.getOneById(legalCustomerId, Customer.class);
			XWPFTableCell headCell = docx.getTables().get(0).getRow(0).getCell(1);
			XWPFRun topLineRun = headCell.getParagraphs().get(0).createRun();
			topLineRun.setText(legalCustomer.getName());
			topLineRun.setBold(true);
			topLineRun.setFontSize(22);
			XWPFRun bottomLineRun = headCell.addParagraph().createRun();
			String bottomLineStr = "";
			bottomLineStr += "地址："
					+ (StringUtils.isBlank(legalCustomer.getAddress()) ? "" : legalCustomer.getAddress());
			bottomLineStr += " 电话：" + (StringUtils.isBlank(legalCustomer.getTel()) ? "" : legalCustomer.getTel());
			bottomLineStr += " 传真：" + (StringUtils.isBlank(legalCustomer.getFax()) ? "" : legalCustomer.getFax());
			bottomLineRun.setFontSize(9);
			bottomLineRun.setText(bottomLineStr);
			List<XWPFParagraph> headCellParas = headCell.getParagraphs();
			for (XWPFParagraph para : headCellParas) {
				para.setAlignment(ParagraphAlignment.CENTER);
			}
		}
	}

	private void addNewPageForShip(XWPFDocument docx) {

		XWPFTable newHeadTable = docx.createTable();
		XWPFTable tableCopy = docx.getTables().get(0);
		XWPFUtil.CloneTable(tableCopy, newHeadTable);
		docx.createParagraph();
		XWPFTable bodyTable = docx.createTable();
		tableCopy = docx.getTables().get(1);
		XWPFUtil.CloneTable(tableCopy, bodyTable);
		XWPFParagraph newPara = docx.createParagraph();
		XWPFUtil.copyParagraph(docx.getParagraphs().get(1), newPara);
		docx.createParagraph();
		docx.createParagraph();
	}
	
	/**
	 * 将货物明细添加到提货单
	 * 
	 * @param docx
	 * @param bs
	 * @param storages
	 */
	private void AddStorageToShipDoc(XWPFDocument docx, ReceiptShip ship, List<Storage> storages) {
		if (storages == null || storages.size() == 0)
			return;

		boolean isQrCodeShow = Boolean.TRUE;
		XWPFParagraph qrPara = docx.getTables().get(0).getRow(0).getCell(2).getParagraphs().get(0);
		if (qrPara.getRuns().size() > 0) {
			String qrText = qrPara.getRuns().get(0).text();
			if (StringUtils.isNotBlank(qrText) && qrText.equalsIgnoreCase("n")) {
				isQrCodeShow = Boolean.FALSE;
				qrPara.removeRun(0);
			}
		} else {
			qrPara.createRun();
		}

		String quantityZero = "";
		for (int i = 0; i < ship.getCommodity().getDigits(); i++) {
			quantityZero += "0";
		}
		List<String> propertyNameList = getPropertyNameFromTableHeadForShip(docx, 1, 3);
		int pageNum = storages.size() / 5 + (storages.size() % 5 == 0 ? 0 : 1);
		for (int pageIndex = 0; pageIndex < pageNum - 1; pageIndex++) {
			addNewPageForShip(docx);
		}

		for (int pageIndex = 0; pageIndex < pageNum; pageIndex++) {
			XWPFTable table = docx.getTables().get(1 + pageIndex * 2);
			int curRowCursor = 0;
			BigDecimal sumQuantity = BigDecimal.ZERO;
			int sumBundles = 0;
			List<Storage> curStorages = new ArrayList<>();
			for (int storageIndex = pageIndex * 5; storageIndex < (pageIndex + 1) * 5
					&& storageIndex < storages.size(); storageIndex++) {
				Storage storage = storages.get(storageIndex);
				curStorages.add(storage);
				XWPFTableRow curRow = table.getRow(3 + curRowCursor);
				for (int propertyIndex = 0; propertyIndex < propertyNameList.size(); propertyIndex++) {
					SetCellText(curRow.getCell(propertyIndex), getMyPropety(ship, propertyNameList.get(propertyIndex),
							storageIndex, quantityZero.length(), 0, "-"));
				}
				sumQuantity = sumQuantity.add(storage.getQuantity());
				sumBundles = sumBundles + storage.getBundles();
				curRowCursor++;
			}
			if (isQrCodeShow) {
				generateShipQrCode(ship, curStorages,
						docx.getTables().get(pageIndex * 2).getRow(0).getCell(2).getParagraphs().get(0).createRun(),
						quantityZero);
			}
			int supposeDeliver = 0;
			int indeedDeliverd = 0;
			int supposeBundles = 0;
			int cellIndex = 0;
			for(XWPFTableCell cell : table.getRow(8).getTableCells()) {
				for(CTBookmark bookmark : cell.getParagraphs().get(0).getCTP().getBookmarkStartArray()){
					if(bookmark.getName().equals("合计应发数量")) {
						supposeDeliver = cellIndex;
					} else if(bookmark.getName().equals("合计实发数量")) {
						indeedDeliverd = cellIndex;
					} else if(bookmark.getName().equals("合计应发件数")) {
						supposeBundles = cellIndex;
					}
					
				}
				cellIndex++;
			}
			if(supposeDeliver > 0) {
				SetCellText(table.getRow(8).getCell(supposeDeliver), sumQuantity.compareTo(BigDecimal.ZERO) == 0 ? ""
						: sumQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString());
			}
			if(indeedDeliverd > 0) {
				SetCellText(table.getRow(8).getCell(indeedDeliverd), sumQuantity.compareTo(BigDecimal.ZERO) == 0 ? ""
						: sumQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString());
			}
			if(supposeBundles > 0) {
				SetCellText(table.getRow(8).getCell(supposeBundles), sumBundles == 0 ? "" : Integer.toString(sumBundles));
			}
		}
	}

	private void AddStorageToShipDocForBaiyin(XWPFDocument docx, ReceiptShip ship, List<Storage> storages) {

		if (storages == null || storages.size() == 0)
			return;

		int pageNum = storages.size() / 5 + (storages.size() % 5 == 0 ? 0 : 1);
		List<String> propertyNameList = getPropertyNameFromTableHeadForShip(docx, 1, 3);
		for (int pageIndex = 0; pageIndex < pageNum - 1; pageIndex++) {
			addNewPageForShip(docx);
		}
		String quantityZero = "";
		for (int i = 0; i < ship.getCommodity().getDigits(); i++) {
			quantityZero += "0";
		}
		for (int pageIndex = 0; pageIndex < pageNum; pageIndex++) {
			XWPFTable table = docx.getTables().get(1 + pageIndex * 2);
			int curRowCursor = 0;
			BigDecimal sumQuantity = BigDecimal.ZERO;

			for (int storageIndex = pageIndex * 5; storageIndex < (pageIndex + 1) * 5
					&& storageIndex < storages.size(); storageIndex++) {
				Storage storage = storages.get(storageIndex);
				XWPFTableRow curRow = table.getRow(3 + curRowCursor);
				for (int propertyIndex = 0; propertyIndex < propertyNameList.size(); propertyIndex++) {
					SetCellText(curRow.getCell(propertyIndex), getMyPropety(ship, propertyNameList.get(propertyIndex),
							storageIndex, quantityZero.length(), 0, "-"));
				}
				curRowCursor++;
				sumQuantity = sumQuantity.add(storage.getQuantity());
			}
			SetCellText(table.getRow(8).getCell(5), sumQuantity.compareTo(BigDecimal.ZERO) == 0 ? ""
					: sumQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString());
			SetCellText(table.getRow(8).getCell(6), sumQuantity.compareTo(BigDecimal.ZERO) == 0 ? ""
					: sumQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString());
		}

	}

	/**
	 * @param docx
	 * @param tableIndex
	 * @param rowIndex
	 * @return
	 */
	private List<String> getPropertyNameFromTableHeadForShip(XWPFDocument docx, int tableIndex, int rowIndex) {
		List<String> headPropertyList = new ArrayList<>();
		XWPFTable table = docx.getTables().get(tableIndex);
		XWPFTableRow headRow = table.getRow(rowIndex);
		for (XWPFTableCell cell : headRow.getTableCells()) {
			headPropertyList.add(cell.getText());
		}
		table.removeRow(rowIndex);
		return headPropertyList;
	}

	private final static Pattern digitPattern = Pattern.compile("\\$([0-9]+)");

	public Integer getDigitFromProperty(String property) {
		Matcher m = digitPattern.matcher(property);
		if (m.find()) {
			return Integer.parseInt(m.group(1));
		}
		return null;
	}

	/**
	 * @param obj
	 * @param property
	 *            字段名
	 * @param index
	 *            数组元素的下标
	 * @param quantityScale
	 *            数量精度
	 * @param priceScale
	 *            价格精度
	 * @param separate
	 *            分隔符
	 * @return
	 */
	private String getMyPropety(Object obj, String property, int index, int quantityScale, int priceScale,
			String separate) {
		if (obj == null || StringUtils.isBlank(property)) {
			return "";
		}
		try {
			property = property.trim();
			// 获取小数位配置
			Integer digitConf = getDigitFromProperty(property);
			// 移出已使用的小数位配置
			if (digitConf != null) {
				int digitConfStartPosition = property.indexOf("$" + digitConf);
				property = property.substring(0, digitConfStartPosition);
			}
			// 分隔多字段
			String[] singlePropertyArr = property.split("---");
			String result = "";
			for (String singleProperty : singlePropertyArr) {
				// 处理乘法
				if (singleProperty.contains("*")) {
					if (StringUtils.isNotBlank(result)) {
						result += separate;
					}
					String[] multiplyArr = singleProperty.split("\\*");
					BigDecimal xB = DecimalUtil
							.nullToZero((BigDecimal) getSinglePropertyValue(obj, multiplyArr[0], index));
					BigDecimal yB = DecimalUtil
							.nullToZero((BigDecimal) getSinglePropertyValue(obj, multiplyArr[1], index));
					if (digitConf == null) {
						digitConf = 2; // 乘法得出的结果，默认取两位小数
					}
					result += convertDecimalWithDigit(property, xB.multiply(yB), digitConf, priceScale, quantityScale);
				} else {
					Object value = getSinglePropertyValue(obj, singleProperty, index);
					if (value != null) {
						if (StringUtils.isNotBlank(result)) {
							result += separate;
						}
						if (value instanceof BigDecimal) {
							result += convertDecimalWithDigit(property, (BigDecimal) value, digitConf, priceScale,
									quantityScale);
						} else {
							result += value;
						}
					}
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * @param property
	 * @param value
	 * @param digitConf
	 * @param priceScale
	 * @param quantityScale
	 * @return
	 */
	private String convertDecimalWithDigit(String property, BigDecimal value, Integer digitConf, int priceScale,
			int quantityScale) {
		if (value.compareTo(BigDecimal.ZERO) == 0) {
			return "";
		}
		if (digitConf != null) {
			return DecimalUtil.Round4s5r(value, digitConf).toPlainString();
		} else if (property.contains("Quantity")) {
			return DecimalUtil.Round4s5r((BigDecimal) value, quantityScale).toPlainString();
		} else {
			return DecimalUtil.Round4s5r((BigDecimal) value, priceScale).toPlainString();
		}
	}

	private Object getSinglePropertyValue(Object obj, String property, int index) {
		try {
			String[] propertyArr = property.split("\\.");
			for (String propertyName : propertyArr) {
				if (obj == null) {
					return null;
				}
				if (propertyName.endsWith("[]")) {
					propertyName = propertyName.substring(0, propertyName.length() - 2);
					List<?> list = (List<?>) obj.getClass().getDeclaredMethod("get" + propertyName).invoke(obj);
					obj = list.get(index);
				} else {
					obj = obj.getClass().getDeclaredMethod("get" + propertyName.trim()).invoke(obj);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 添加表头二维码
	 * 
	 * @param curShip
	 * @param curStorages
	 * @param run
	 * @param quantityZero
	 */
	public void generateShipQrCode(ReceiptShip curShip, List<Storage> curStorages, XWPFRun run, String quantityZero) {

		String code = "";
		// 开头 提货单号
		code += curShip.getReceiptShipNo() + ";";
		// 循环 卡号1，应发重量1；卡号1，应发重量1；
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage item : curStorages) {
			code += item.getCardNo() + ","
					+ item.getQuantity().setScale(quantityZero.length(), RoundingMode.HALF_UP).toString() + ";";
			sumQuantity = sumQuantity.add(item.getQuantity());
		}
		// 结尾
		code += "total:" + sumQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString();
		String qrCodePath = commService.CreateCode_Simple(code);
		if (StringUtils.isNotBlank(qrCodePath)) {
			try {
				FileInputStream is = new FileInputStream(qrCodePath);
				run.addPicture(is, XWPFDocument.PICTURE_TYPE_JPEG, qrCodePath, Units.toEMU(50), Units.toEMU(50));
				is.close();
			} catch (InvalidFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private Map<String, String> getReplaceKeyValueForReceiptShipDocx(ReceiptShip curShip) {
		Map<String, String> map = new HashMap<>();
		// 书签写内容
		Customer customer = curShip.getCustomer();
		if (customer == null) {
			map.put("提货单位", "");
		} else {
			map.put("提货单位", customer.getName());
		}

		map.put("发货单号", curShip.getReceiptShipNo());
		map.put("业务日期",
				curShip.getReceiptShipDate() == null ? "" : DateUtil.shortDateString(curShip.getReceiptShipDate()));
		if (StringUtils.isBlank(curShip.getWhId())) {
			map.put("提货仓库", "");
			map.put("仓库地址", "");
		} else {
			if (curShip.getWarehouse() != null) {
				map.put("提货仓库", curShip.getWarehouse().getName());
				map.put("仓库地址", curShip.getWarehouse().getAddress());
			} else {
				map.put("提货仓库", "");
				map.put("仓库地址", "");
			}
		}
		map.put("联系电话", curShip.getDeliveryTelePhone());
		map.put("提货人姓名", curShip.getDeliveryMan());
		map.put("身份证号", curShip.getDeliveryManIDCard());
		map.put("Card", curShip.getDeliveryManIDCard());
		if (StringUtils.isNotBlank(curShip.getDeliveryManIDCard())) {
			for (int i = 0; i < curShip.getDeliveryManIDCard().length(); i++) {
				map.put("Card" + String.valueOf(i + 1), String.valueOf(curShip.getDeliveryManIDCard().charAt(i)));
			}
		}
		map.put("车辆编号", curShip.getTruckNo());

		map.put("备注", curShip.getRemark());
		return map;
	}

	public OutDocumentFile OutputDocumentContractDoc(String temporarilyFilePath, String destFilePath,
			Contract curContract) {
		if (curContract == null) {
			return new OutDocumentFile(false, "当前合同为空，无法生成");
		}
		// 按照模版格式，生成临时文件
		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(temporarilyFilePath));
			Map<String, String> replaceTextMap = getReplaceKeyValueForContractDocx(curContract);
			GetBookMarkStarts(docx, replaceTextMap);

			String priceZero = "";
			for (int i = 0; i < curContract.getCommodity().getDigits4Price(); i++) {
				priceZero += "0";
			}
			String quantityZero = "";
			for (int i = 0; i < curContract.getCommodity().getDigits(); i++) {
				quantityZero += "0";
			}
			AddLotToContractDoc(docx, null, curContract, priceZero, quantityZero);
			docx.write(new FileOutputStream(new File(destFilePath)));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "生成文件失败");
		} finally {
			try {
				if (docx != null)
					docx.close();
				new File(temporarilyFilePath).delete();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				return new OutDocumentFile(false, "保存文件失败");
			}
		}

		return new OutDocumentFile(true, "");
	}

	private Map<String, String> getReplaceKeyValueForContractDocx(Contract curContract) {
		Map<String, String> map = new HashMap<String, String>();
		String partA = "";
		// 判断购销类型 如果是采购，我方为乙方；如果是销售，客户为乙方 此处取客户名称
		Customer legalCustomer = null;
		if (curContract.getLegal() != null) {
			legalCustomer = customerRepo.getOneById(curContract.getLegal().getCustomerId(), Customer.class);
		}
		if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			if (legalCustomer != null) {
				partA = legalCustomer.getName();
			}
		} else if (curContract.getSpotDirection().equals(SpotType.Purchase)) {
			if (curContract.getCustomer() != null) {
				partA = curContract.getCustomer().getName();
			}
		}
		if (legalCustomer != null) {
			map.put("内部抬头对应的客户名称", legalCustomer.getName());
			map.put("内部抬头对应的客户传真", legalCustomer.getFax());
		}
		if (curContract.getRuleWareHouseNames() != null) {
			map.put("约定仓库", curContract.getRuleWareHouseNames());
		}
		if (curContract.getCustomer() != null) {
			map.put("客户的传真", curContract.getCustomer().getFax());
		}
		Dictionary dic = dictionaryRepo.GetQueryable(Dictionary.class)
				.where(DetachedCriteria.forClass(Dictionary.class).add(Restrictions.eq("Code", "PaymentTerm"))
						.add(Restrictions.eq("Value", curContract.getPaymentTerm())))
				.firstOrDefault();
		if(dic != null && StringUtils.isNotBlank(dic.getName())) {
			map.put("付款方式", dic.getName());
		}
		map.put("运输方式", curContract.getDeliveryTerm());
		map.put("甲方", partA); // 书签写内容

		String partB = "";
		// 判断购销类型 如果是采购，客户为甲方；如果是销售，我方为甲方 此处取客户名称
		if (curContract.getSpotDirection().equals(SpotType.Purchase)) {
			if (legalCustomer != null) {
				partB = legalCustomer.getName();
			}
		} else if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			if (curContract.getCustomer() != null) {
				partB = curContract.getCustomer().getName();
			}
		}
		map.put("乙方", partB);
		String partASignature = "";
		// 判断购销类型 如果是采购，我方为乙方；如果是销售，客户为乙方 此处取客户名称
		if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			if (legalCustomer != null) {
				partASignature = legalCustomer.getName();
			}
		} else if (curContract.getSpotDirection().equals(SpotType.Purchase)) {
			if (curContract.getCustomer() != null) {
				partASignature = curContract.getCustomer().getName();
			}
		}
		map.put("甲方签字", partASignature);
		String partBSignature = "";
		// 判断购销类型 如果是采购，客户为甲方；如果是销售，我方为甲方 此处取客户名称
		if (curContract.getSpotDirection().equals(SpotType.Purchase)) {
			if (legalCustomer != null) {
				partBSignature = legalCustomer.getName();
			}
		} else if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			if (curContract.getCustomer() != null) {
				partBSignature = curContract.getCustomer().getName();
			}
		}
		map.put("乙方签字", partBSignature);
		String spotTypeName = "";
		if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			spotTypeName = "销";
		} else if (curContract.getSpotDirection().equals(SpotType.Purchase)) {
			spotTypeName = "购";
		}
		map.put("购销类型", spotTypeName);
		map.put("编号", curContract.getHeadNo());
		map.put("日期", DateUtil.shortDateString(curContract.getTradeDate()));
		BigDecimal totalQuantity = BigDecimal.ZERO;
		List<Lot> lots = curContract.getLots();
		if (lots != null) {
			for (Lot lot : lots) {
				totalQuantity = totalQuantity.add(DecimalUtil.nullToZero(lot.getQuantity()));
			}
		}
		String quantityZero = "";
		for (int i = 0; i < curContract.getCommodity().getDigits(); i++) {
			quantityZero += "0";
		}
		map.put("总数量", totalQuantity.compareTo(BigDecimal.ZERO) == 0 ? ""
				: totalQuantity.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString()); // 品种设定

		BigDecimal totalRealNumber = BigDecimal.ZERO;
		if (lots != null) {
			for (Lot lot : lots) {
				totalRealNumber = totalRealNumber.add(DecimalUtil.nullToZero(lot.getQuantityDelivered()));
			}
		}
		map.put("总实数", totalRealNumber.compareTo(BigDecimal.ZERO) == 0 ? ""
				: totalRealNumber.setScale(quantityZero.length(), RoundingMode.HALF_UP).toString()); // 数量，按品种设定
		BigDecimal sumLotMoney = BigDecimal.ZERO;
		for (Lot lot : lots) {
			sumLotMoney = sumLotMoney.add(DecimalUtil.nullToZero(lot.getFinal()).multiply(lot.getQuantity()));
		}

		map.put("总金额", sumLotMoney == null ? "" : sumLotMoney.setScale(2, RoundingMode.HALF_UP).toString());// 金额，固定两位
		map.put("大写总金额", commService.MoneyLowerToLarge(sumLotMoney.setScale(2, RoundingMode.HALF_UP)));
		return map;
	}

	public OutDocumentFile OutputDocument(String templatePath, String filePath, Lot curLot, Storage storage,
			Invoice curInvoice, Origin origin, Storage bviSource) {

		// if (customer == null)
		// return;
		// 按照模版格式，生成临时文件

		XWPFDocument docx = null;
		try {
			docx = new XWPFDocument(OPCPackage.open(templatePath));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new OutDocumentFile(false, "无法打开目标文件进行后续操作");
		}
		Map<String, String> replaceTextMap = getReplaceKeyValueForInvoiceDocx(curInvoice, curLot, storage, origin,
				bviSource);
		GetBookMarkStarts(docx, replaceTextMap);
		AddPackingList(docx, null, curInvoice);// 生成PACKING LIST
		AddCertificateofWeight(docx, null, curInvoice);// 重量证明
		AddCommercialInvoice(docx, null, curInvoice);
		AddCommercialInvoice(docx, null, curInvoice);
		AddInWarehouse4BVI(docx, null, curInvoice);
		AddOutWarehouse4SM(docx, null, curInvoice);
		AddFinalInvoice(docx, null, curInvoice);

		try {
			docx.write(new FileOutputStream(new File(filePath)));
			docx.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
			return new OutDocumentFile(false, "生成文件失败");
		}
		return new OutDocumentFile(true, "");

	}

	private Map<String, String> getReplaceKeyValueForInvoiceDocx(Invoice curInvoice, Lot curLot, Storage storage,
			Origin origin, Storage bviSource) {

		if (curLot != null) {
			if (curLot.getCustomer() == null && curLot.getCustomerId() != null)
				curLot.setCustomer(customerRepo.getOneById(curLot.getCustomerId(), Customer.class));
			if (curLot.getContract() == null && curLot.getContractId() != null)
				curLot.setContract(contractRepo.getOneById(curLot.getContractId(), Contract.class));
		}
		Customer customer = null;
		if (curInvoice.getCustomerId() != null)
			customer = customerRepo.getOneById(curInvoice.getCustomerId(), Customer.class);
		else if (curLot != null && curLot.getCustomerId() != null)
			customer = customerRepo.getOneById(curLot.getCustomerId(), Customer.class);
		Customer legalCustomer = null;
		if (curInvoice.getLegalId() != null) {
			Legal legal1 = null;
			if (curInvoice.getLegal() == null && curInvoice.getLegalId() != null) {
				legal1 = legalRepo.getOneById(curInvoice.getLegalId(), Legal.class);
				curInvoice.setLegal(legal1);
			}

			if (legal1 != null && legal1.getCustomerId() != null)
				legalCustomer = customerRepo.getOneById(legal1.getCustomerId(), Customer.class);
		}
		List<Product> listProducts = new ArrayList<Product>();
		Map<String, String> map = new HashMap<>();
		InvoiceParams param4out = new InvoiceParams();
		// 书签写内容
		if (customer != null) {
			map.put("CustomerName", customer.getName());
			map.put("CustomerNameCN", customer.getCnName());
			map.put("CustomerNameEN", customer.getEnName());
			map.put("CustomerNameAndAddress", customer.getName() + customer.getAddress());
			map.put("CustomerNameAndAddressEN", customer.getEnName() + customer.getAddress());
			String customerAllSuffix = "\r\n" + customer.getAddress()
					+ (StringUtils.isBlank(customer.getZip()) ? "" : "," + customer.getZip()) + "\r\nTel:"
					+ customer.getTel() + "; Fax:" + customer.getFax();
			map.put("CustomerAll", customer.getName() + customerAllSuffix);
			map.put("CustomerAllEN", customer.getEnName() + customerAllSuffix);
			map.put("CustomerAddress", customer.getAddress());
			map.put("CustomerContact",
					customer.getAddress() + "\r\nTel:" + customer.getTel() + "; Fax:" + customer.getFax());
			map.put("CustomerTel", customer.getTel());
			map.put("CustomerFax", customer.getFax());
			map.put("ENCustomerName", customer.getEnName());
		} else {
			map.put("CustomerName", "");
			map.put("CustomerNameCN", "");
			map.put("CustomerNameEN", "");
			map.put("CustomerNameAndAddress", "");
			map.put("CustomerNameAndAddressEN", "");
			map.put("CustomerAll", "");
			map.put("CustomerAllEN", "");
			map.put("CustomerAddress", "");
			map.put("CustomerContact", "");
			map.put("CustomerTel", "");
			map.put("CustomerFax", "");
			map.put("ENCustomerName", "");
		}
		map.put("SupplierName", "");
		String brand = "";
		if (curLot.getBrandNames().indexOf(",") > -1)
			brand = storage == null ? "" : storage.getBrandName();
		else
			brand = curLot.getBrandNames();
		if (storage != null) {
			map.put("Vessel", storage.getVessel());
			map.put("Intermediate", storage.getIntermediate());
			map.put("ProductStorage", storage.getProduct());
			map.put("ProductStorageEN", ProductEn(storage.getProduct(), listProducts));
			map.put("Commodity", storage.getCommodityName());

		}
		map.put("Factory", BrandFactory(brand));
		map.put("DeliveryTerm", curLot.getDeliveryTerm());

		map.put("Loading", curLot.getLoading());
		map.put("Discharging", curLot == null ? "" : curLot.getDischarging());
		map.put("TradeDate", DateUtil.doFormatDate(curInvoice.getTradeDate(), "yyyy-MM-dd"));
		map.put("InvoiceNo", curInvoice.getInvoiceNo());
		map.put("HeadNo", curLot.getContract() == null ? "" : curLot.getContract().getHeadNo());
		map.put("FullNo", curLot.getFullNo());
		map.put("DueDate",
				curInvoice.getDueDate() == null ? "" : DateUtil.doFormatDate(curInvoice.getDueDate(), "yyyy-MM-dd"));
		map.put("Product", curLot == null ? "" : curLot.getProduct());
		if (listProducts.size() == 0)
			listProducts = productRepo.GetList(Product.class);
		map.put("ProductEN", ProductEn(curLot.getProduct(), listProducts));
		map.put("OriginName", origin == null ? "" : origin.getCode());
		// 发票NW = 工厂nw，则gross = 出厂gross，否则取仓库gross
		BigDecimal gross = BigDecimal.ZERO;
		BigDecimal grossAtFactory = BigDecimal.ZERO;
		BigDecimal netWeight1 = BigDecimal.ZERO;
		BigDecimal sumBundles = BigDecimal.ZERO;
		if (curInvoice.getStorages() != null) {
			for (Storage t : curInvoice.getStorages()) {
				gross = gross.add(t.getGross());
				grossAtFactory = grossAtFactory.add(t.getGrossAtFactory());
				netWeight1 = netWeight1.add(t.getQuantity());
				sumBundles = sumBundles.add(new BigDecimal(t.getBundles()));
			}
		}
		String gross1 = "";
		if (curInvoice.getQuantity().compareTo(netWeight1) == 0) {
			gross1 = grossAtFactory.setScale(3, RoundingMode.HALF_UP).toString();
		} else {
			gross1 = gross.setScale(3, RoundingMode.HALF_UP).toString();
		}
		map.put("Gross", gross1);
		map.put("NetWeight", netWeight1.setScale(3, RoundingMode.HALF_UP).toString());
		map.put("Provisional",
				curInvoice.getPrice().subtract(curLot.getPremium() == null ? BigDecimal.ZERO : curLot.getPremium())
						.setScale(2, RoundingMode.HALF_UP).toString());
		String currencyStr = curInvoice.getCurrency().equals("USD") ? "$" : "￥";
		map.put("Provisional2", currencyStr + DecimalUtil.getThousandsSeparator(curInvoice.getMajor(), 2));
		map.put("Premium",
				curLot.getPremium() == null ? "" : curLot.getPremium().setScale(2, RoundingMode.HALF_UP).toString());
		map.put("Premium2", currencyStr + DecimalUtil.getThousandsSeparator(curInvoice.getPremium(), 2));
		map.put("Premium4AdjustAndProvision", currencyStr + DecimalUtil.getThousandsSeparator(
				curInvoice.getPrice().subtract(DecimalUtil.nullToZero(curInvoice.getMajor())), 2));
		map.put("Price", currencyStr + DecimalUtil.getThousandsSeparator(curInvoice.getPrice(), 2));
		BigDecimal sumInvoiceProvisionalAmount = BigDecimal.ZERO;
		if (curInvoice.getProvisionals() != null && curInvoice.getProvisionals().size() > 0) {
			Invoice f1 = curInvoice.getProvisionals().get(0);
			map.put("ProvisionalPrice", currencyStr + DecimalUtil.getThousandsSeparator(f1.getPrice(), 2));
			map.put("ProvisionalPremium",
					f1.getPremium() != null ? f1.getPremium().setScale(2, RoundingMode.HALF_UP).toString() : "");
			map.put("ProvisionalMajor",
					f1.getMajor() != null ? f1.getMajor().setScale(2, RoundingMode.HALF_UP).toString() : "");
			for (Invoice i : curInvoice.getProvisionals()) {
				sumInvoiceProvisionalAmount.add(i.getAmount());
			}
			map.put("ProvisionalAmount",
					currencyStr + DecimalUtil.getThousandsSeparator(sumInvoiceProvisionalAmount, 2));
			map.put("ProvisionalQuantity", f1.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());
			BigDecimal grossPro = BigDecimal.ZERO;
			BigDecimal grossAtFactoryPro = BigDecimal.ZERO;
			BigDecimal netWeightPro = BigDecimal.ZERO;
			String gross2 = "";
			if (f1.getStorages() != null && f1.getStorages().size() > 0) {
				for (Storage s : f1.getStorages()) {
					grossPro = grossPro.add(s.getGross());
					grossAtFactoryPro = grossAtFactoryPro.add(s.getGrossAtFactory());
					netWeightPro = netWeightPro.add(s.getQuantity());
				}
				if (curInvoice.getQuantity().compareTo(netWeightPro) == 0) {
					gross2 = grossAtFactoryPro.setScale(3, RoundingMode.HALF_UP).toString();
				} else {
					gross2 = grossPro.setScale(3, RoundingMode.HALF_UP).toString();
				}
			}
			map.put("ProvisionalGross", gross2);
			map.put("ProvisionalInvoiceNo", f1.getInvoiceNo());
		}
		map.put("Amount", currencyStr + DecimalUtil.getThousandsSeparator(curInvoice.getAmount(), 2));
		map.put("AmountNotional", currencyStr
				+ DecimalUtil.getThousandsSeparator(DecimalUtil.nullToZero(curInvoice.getAmountNotional()), 2));
		map.put("Bundles", sumBundles.toString());
		map.put("BrandName", curLot.getBrandNames());
		map.put("Quantity", curInvoice.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());
		map.put("Unit", curInvoice.getUnit());

		map.put("Comments", curInvoice.getComments());
		map.put("NA", "N/A");
		@SuppressWarnings("deprecation")
		String chineseMonth = DateUtil.getChineseMonth(curLot.getETD().getMonth());
		map.put("QuoteMonth", curLot == null ? "" : (curLot.getETD() == null ? "" : chineseMonth));

		map.put("LegalName", curInvoice.getLegalName());
		map.put("LegalCode", curInvoice.getLegal().getCode());
		if (legalCustomer != null) {
			map.put("LegalCustomerName", legalCustomer.getName());
			map.put("LegalCustomerNameCN", legalCustomer.getCnName());
			map.put("LegalCustomerNameEN", legalCustomer.getEnName());
			String legalCustomerAllSuffix = "\r\n" + legalCustomer.getAddress()
					+ (StringUtils.isBlank(legalCustomer.getZip()) ? "" : "," + legalCustomer.getZip()) + "\r\nTel:"
					+ legalCustomer.getTel() + "; Fax:" + legalCustomer.getFax();
			map.put("LegalCustomerAll", legalCustomer.getName() + legalCustomerAllSuffix);
			map.put("LegalCustomerAllEN", legalCustomer.getEnName() + legalCustomerAllSuffix);
			map.put("LegalCustomerAddressZipTelFaxEN", legalCustomer.getAddress() + legalCustomerAllSuffix);
			map.put("LegalCustomerAddress", legalCustomer.getAddress());
		}
		if (curInvoice.getLegalBank() != null) {
			map.put("BankNoteOfNormal", curInvoice.getLegalBank().getBankNote());
		}
		if (curInvoice.getAmount().compareTo(BigDecimal.ZERO) > 0 && curInvoice.getLegalBank() != null)
			map.put("BankNoteOfFinalisation", curInvoice.getLegalBank().getBankNote());

		if (curInvoice.getAmount().compareTo(BigDecimal.ZERO) >= 0 && curInvoice.getLegalBank() != null)
			map.put("BankeNoteOfBviToSmFinal", curInvoice.getLegalBank().getBankNote());
		else if (curInvoice.getAmount().compareTo(BigDecimal.ZERO) < 0 && curInvoice.getCustomerBank() != null)
			map.put("BankeNoteOfBviToSmFinal", curInvoice.getCustomerBank().getBankNote());
		map.put("CreatedBy", curInvoice.getCreatedBy());

		map.put("TradeDate4Bill", DateUtil.shortDateString(curInvoice.getTradeDate()));// 开票日期
		if (param4out != null && param4out.getStartDate() != null && param4out.getEndDate() != null) {
			map.put("TradeDate4BillOut", DateUtil.shortDateString(param4out.getStartDate()) + "--"
					+ DateUtil.shortDateString(param4out.getEndDate()));
		}
		map.put("Currency", curInvoice.getCurrency());

		return map;
	}

	public OutDocumentFile OutputCheckFile(OutDocumentFile odf, String No) {
		// 模板保存在公共服务器上，指定目录中
		File tempFolderFile = new File(odf.getTempFolder());
		if (!tempFolderFile.exists()) {
			tempFolderFile.mkdirs();
		}
		File tempPathFile = new File(odf.getTempPath());
		if (!tempPathFile.exists()) {
			return new OutDocumentFile(false, "模板文件不存在，请检查！");
		}

		if (odf.getFileFolder() == null) {
			return new OutDocumentFile(false, "生成文件对应的公共文件目录不存在，请先设置！");
		}
		File fileFolderFile = new File(odf.getFileFolder());
		if (!fileFolderFile.exists()) {
			fileFolderFile.mkdirs();
		}
		odf.setFileName(No + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss"));
		odf.setFileFullName(odf.getFileName() + OutDocument.OutDocTemplateExt);

		odf.setFilePath(odf.getFileFolder() + "/" + odf.getFileFullName());
		// 生成的具有模版样式的新文件
		// 如果重名,则删除原来已经存在的文件
		File filePathFile = new File(odf.getFilePath());
		if (filePathFile.exists()) {
			try {
				filePathFile.delete();
			} catch (Exception ex) {
				return new OutDocumentFile(false, "请先关闭已经存在且被打开的目标文件！");
			}
		}
		odf.setTemporarilyFilePath(odf.getFileFolder() + "/" + odf.getFileFullName() + "temp");
		odf.setSuccess(true);
		return odf;
	}

	/**
	 * 模板中Table 业务数据替换 添加装箱明细
	 * 
	 * @param docx
	 * @param bs
	 * @param curInvoice
	 */
	private void AddPackingList(XWPFDocument docx, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice == null || curInvoice.getStorages() == null || curInvoice.getStorages().size() == 0)
			return;

		List<Storage> packList = curInvoice.getStorages();
		XWPFTable table = docx.getTables().get(0);
		if (table == null)
			return;

		BigDecimal sumBundles = BigDecimal.ZERO;
		BigDecimal sumQuantity = BigDecimal.ZERO;
		BigDecimal sumGross = BigDecimal.ZERO;
		int rowIndex = 2;
		for (Storage storage : packList) {
			XWPFTableRow addRow = table.insertNewTableRow(1);
			XWPFUtil.CopytTableRow(table.getRow(table.getNumberOfRows() - 1), addRow);
			sumBundles = sumBundles.add(new BigDecimal(storage.getBundles()));
			sumQuantity = sumQuantity.add(storage.getQuantity());
			sumGross = sumGross.add(DecimalUtil.nullToZero(storage.getGrossAtFactory()));
			SetCellText(addRow.getCell(0), String.valueOf(rowIndex - 1));
			SetCellText(addRow.getCell(1), storage.getBrandName());
			SetCellText(addRow.getCell(2), storage.getProjectName());
			SetCellText(addRow.getCell(3), String.valueOf(storage.getBundles()));
			SetCellText(addRow.getCell(4), storage.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());
			SetCellText(addRow.getCell(5),
					DecimalUtil.nullToZero(storage.getGrossAtFactory()).setScale(3, RoundingMode.HALF_UP).toString());
			rowIndex++;
		}

		XWPFTableRow latRow = table.getRow(table.getNumberOfRows() - 1);
		SetCellText(latRow.getCell(4), sumBundles.toString());
		SetCellText(latRow.getCell(5), sumQuantity.setScale(3, RoundingMode.HALF_UP).toString());
		SetCellText(latRow.getCell(6), sumGross.setScale(3, RoundingMode.HALF_UP).toString());
	}

	/**
	 * Certificate of weight
	 * 
	 * @param xmlDoc
	 * @param bs
	 * @param curInvoice
	 */
	private void AddCertificateofWeight(XWPFDocument docx, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice == null || curInvoice.getStorages() == null || curInvoice.getStorages().size() == 0)
			return;

		XWPFTable table = docx.getTables().get(0);
		if (table == null)
			return;
		List<Storage> packList = curInvoice.getStorages();
		BigDecimal netWeight1 = BigDecimal.ZERO;
		if (curInvoice.getStorages() != null) {
			for (Storage s : curInvoice.getStorages()) {
				netWeight1 = netWeight1.add(s.getQuantity());
			}
		}
		BigDecimal sumGross = BigDecimal.ZERO;
		BigDecimal sumBundles = BigDecimal.ZERO;
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage storage : packList) {
			XWPFTableRow addRow = table.insertNewTableRow(1);
			XWPFUtil.CopytTableRow(table.getRow(table.getNumberOfRows() - 1), addRow);
			String gross = "";
			if (curInvoice.getQuantity().compareTo(netWeight1) == 0) {
				gross = DecimalUtil.nullToZero(storage.getGrossAtFactory()).setScale(3, RoundingMode.HALF_UP)
						.toString();
				sumGross = sumGross.add(DecimalUtil.nullToZero(storage.getGrossAtFactory()));
			} else {
				gross = DecimalUtil.nullToZero(storage.getGross()).setScale(3, RoundingMode.HALF_UP).toString();
				sumGross = sumGross.add(DecimalUtil.nullToZero(storage.getGross()));
			}

			SetCellText(addRow.getCell(0), "N/A");
			SetCellText(addRow.getCell(1), "copper cathodes");
			SetCellText(addRow.getCell(2), "7403.1101");
			SetCellText(addRow.getCell(3), String.valueOf(storage.getBundles()));
			SetCellText(addRow.getCell(4), storage.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());
			SetCellText(addRow.getCell(5), gross);
			SetCellText(addRow.getCell(6),
					new BigDecimal(0.726 * storage.getBundles()).setScale(3, RoundingMode.HALF_UP).toString());
			sumBundles = sumBundles.add(new BigDecimal(storage.getBundles()));
			sumQuantity = sumQuantity.add(storage.getQuantity());
		}

		XWPFTableRow addRow = table.insertNewTableRow(table.getNumberOfRows());
		XWPFUtil.CopytTableRow(table.getRow(table.getNumberOfRows() - 1), addRow);
		SetCellText(addRow.getCell(0), "Total");
		SetCellText(addRow.getCell(1), "");
		SetCellText(addRow.getCell(2), "");
		SetCellText(addRow.getCell(3), sumBundles.toString());
		SetCellText(addRow.getCell(4), sumQuantity.setScale(3, RoundingMode.HALF_UP).toString());
		SetCellText(addRow.getCell(5), sumGross.setScale(3, RoundingMode.HALF_UP).toString());
		SetCellText(addRow.getCell(6), "");

	}

	/**
	 * @param docx
	 * @param bs
	 * @param lots
	 * @param priceZero
	 * @param quantityZero
	 */
	private void AddLotToContractDoc(XWPFDocument docx, CTBookmark bs, Contract curContract, String priceZero,
			String quantityZero) {
		List<Lot> lots = curContract.getLots();
		List<String> propertyNameList = getPropertyNameFromTableHeadForShip(docx, 1, 1);
		if (lots == null || lots.size() == 0)
			return;
		XWPFTable table = docx.getTables().get(1);
		if (table == null)
			return;
		for (int lotIndex = 0; lotIndex < lots.size(); lotIndex++) {
			XWPFTableRow curRow = table.insertNewTableRow(2);
			XWPFUtil.CopytTableRow(table.getRow(1), curRow);
			for (int propertyIndex = 0; propertyIndex < propertyNameList.size(); propertyIndex++) {
				SetCellText(curRow.getCell(propertyIndex), getMyPropety(curContract,
						propertyNameList.get(propertyIndex), lotIndex, quantityZero.length(), priceZero.length(), "-"));
			}
		}
		table.removeRow(1);
	}

	/**
	 * Commercial Invoice
	 * 
	 * @param docx
	 * @param bs
	 * @param curInvoice
	 */
	private void AddCommercialInvoice(XWPFDocument docx, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice == null || curInvoice.getStorages() == null || curInvoice.getStorages().size() == 0)
			return;

		XWPFTable table = docx.getTables().get(0);
		if (table == null)
			return;

		List<Storage> packList = curInvoice.getStorages();
		List<XWPFTableRow> rows = table.getRows();
		if (rows.size() < 7)
			return;
		List<XWPFTableRow> cloneRows = new ArrayList<>();
		for (int i = 0; i < rows.size(); i++) {
			cloneRows.add(table.insertNewTableRow(1));
		}
		BigDecimal netWeight1 = BigDecimal.ZERO;
		if (curInvoice.getStorages() != null) {
			for (Storage s : curInvoice.getStorages()) {
				netWeight1 = netWeight1.add(s.getQuantity());
			}
		}
		int index = 1;
		List<Origin> Origins = originRepo.GetList(Origin.class);
		for (Storage storage : packList) {
			// 初始化数据
			String gross = "";
			if (curInvoice.getQuantity().compareTo(netWeight1) == 0) {
				gross = DecimalUtil.nullToZero(storage.getGrossAtFactory()).setScale(3, RoundingMode.HALF_UP)
						.toString();
			} else {
				gross = DecimalUtil.nullToZero(storage.getGross()).setScale(3, RoundingMode.HALF_UP).toString();
			}
			Lot curLot1 = null;
			if (storage.getLotId() != null) {
				curLot1 = lotRepo.getOneById(storage.getLotId(), Lot.class);
			}
			Origin origin = null;
			if (curLot1 != null && curLot1.getOriginId() != null) {
				for (Origin o : Origins) {
					if (o.getId().equals(curLot1.getOriginId())) {
						origin = o;
						break;
					}
				}
			}

			if (index == 1) {
				CommercialInvoiceSetCellText(rows, storage, curInvoice, origin, curLot1, gross);
				index = 7;
			} else {
				List<XWPFTableRow> addRows = new ArrayList<>();
				for (int i = 0; i < rows.size(); i++) {
					table.insertNewTableRow(index);
					index++;
				}
				CommercialInvoiceSetCellText(addRows, storage, curInvoice, origin, curLot1, gross);
			}
		}
	}

	/**
	 * 批量设置单元格值
	 * 
	 * @param rows
	 * @param storage
	 * @param curInvoice
	 * @param origin
	 * @param curLot1
	 * @param gross
	 */
	private void CommercialInvoiceSetCellText(List<XWPFTableRow> rows, Storage storage, Invoice curInvoice,
			Origin origin, Lot curLot1, String gross) {
		List<XWPFTableCell> cells = rows.get(0).getTableCells();
		SetCellText(cells.get(1), storage.getProduct());
		SetCellText(cells.get(3), String.valueOf(storage.getBundles()));

		cells = rows.get(1).getTableCells();
		SetCellText(cells.get(1), origin == null ? "" : origin.getCode());
		SetCellText(cells.get(3), storage.getBrandName());

		cells = rows.get(2).getTableCells();
		SetCellText(cells.get(1), gross);
		SetCellText(cells.get(3), storage.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());

		cells = rows.get(3).getTableCells();
		SetCellText(cells.get(1), curLot1 != null ? (curLot1.getPremium() != null
				? curInvoice.getPrice().subtract(curLot1.getPremium()).setScale(2, RoundingMode.HALF_UP).toString()
				: "") : "");

		cells = rows.get(4).getTableCells();
		SetCellText(cells.get(1), curLot1 != null ? (curLot1.getPremium() != null
				? curLot1.getPremium().setScale(2, RoundingMode.HALF_UP).toString() : "") : "");

		cells = rows.get(5).getTableCells();
		SetCellText(cells.get(1), curLot1 != null
				? (curLot1.getPrice() != null ? curLot1.getPrice().setScale(2, RoundingMode.HALF_UP).toString() : "")
				: "");

		cells = rows.get(6).getTableCells();
		SetCellText(cells.get(1), curLot1 != null ? (curLot1.getPrice() != null
				? (storage.getQuantity().multiply(curLot1.getPrice())).setScale(2, RoundingMode.HALF_UP).toString()
				: "") : "");
	}

	/**
	 * @param docx
	 * @param bs
	 * @param curInvoice
	 */
	private void AddInWarehouse4BVI(XWPFDocument docx, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice == null)
			return;
		Lot curlot = curInvoice.getLot();
		if (curlot == null)
			curlot = lotRepo.getOneById(curInvoice.getLotId(), Lot.class);

		XWPFTable table = docx.getTables().get(0);
		if (table == null)
			return;

		BigDecimal sumQuantity = BigDecimal.ZERO;
		BigDecimal sumAmount = BigDecimal.ZERO;
		List<Invoice> lstInvoice = new ArrayList<>();
		lstInvoice.add(curInvoice);
		Storage storage = null;
		if (curInvoice.getStorages() != null && curInvoice.getStorages().size() > 0) {
			storage = curInvoice.getStorages().get(0);
		}
		if (storage == null)
			return;

		for (Invoice invoice : lstInvoice) {
			XWPFTableRow addRow = table.createRow();
			sumQuantity = sumQuantity.add(invoice.getQuantity());
			BigDecimal amount1 = DecimalUtil.Round4s5r(invoice.getQuantity().multiply(invoice.getPrice()), 2);
			sumAmount = sumAmount.add(amount1);

			List<XWPFTableCell> cells = addRow.getTableCells();
			SetCellText(cells.get(0),
					invoice.getCustomer() != null ? invoice.getCustomer().getName() : invoice.getCustomerName());// BrandFactory(storage.BrandName);//供货单位
			SetCellText(cells.get(1), (storage.getBrandName() + storage.getProduct()));// 品名
			SetCellText(cells.get(2), curlot == null ? "" : curlot.getFullNo());// 采购批次号
			SetCellText(cells.get(3),
					StringUtils.isBlank(invoice.getDocumentNo()) ? invoice.getInvoiceNo() : invoice.getDocumentNo());// 采购发票号
			SetCellText(cells.get(4), invoice.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());// 数量
			SetCellText(cells.get(5), "吨");// 单位
			SetCellText(cells.get(6), invoice.getPrice().setScale(2, RoundingMode.HALF_UP).toString());// 不含税单价
			SetCellText(cells.get(7), amount1.setScale(2, RoundingMode.HALF_UP).toString()); // 成本价格
		}

		XWPFTableRow lastRow = GetTableLastRow(table);
		List<XWPFTableCell> lastCells = lastRow.getTableCells();
		SetCellText(lastCells.get(4), sumQuantity.setScale(3, RoundingMode.HALF_UP).toString());
		SetCellText(lastCells.get(7), sumAmount.setScale(2, RoundingMode.HALF_UP).toString());
	}

	private void AddOutWarehouse4SM(XWPFDocument xmlDoc, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice == null || curInvoice.getStorages() == null || curInvoice.getStorages().size() == 0)
			return;
		List<Storage> packList = curInvoice.getStorages();

		XWPFTable table = GetTableByIndex(xmlDoc, 1);
		if (table == null)
			return;

		BigDecimal sumQuantity = BigDecimal.ZERO;
		BigDecimal sumAmount = BigDecimal.ZERO;
		// 得到采购信息

		List<StorageOutBill4SM> outBill4SMs = new ArrayList<>();
		for (Storage storage : packList) {
			// 对应的采购发票和批次
			Invoice invoiceIn = null;

			List<Invoice> invoiceIns = Invoices4Sql(storage.getCounterpartyId());
			if (invoiceIns != null) {
				for (Invoice i : invoiceIns) {
					if (i.getPFA().equals("P") && i.getAdjustId() != null) {
						invoiceIn = i;
					}
				}
			}
			StorageOutBill4SM outbill = new StorageOutBill4SM();
			outbill.setCustomerName(curInvoice.getCustomerName());
			outbill.setBrandName(storage.getBrandName());
			outbill.setProduct(storage.getProduct());
			outbill.setInvoiceNo4Sale(curInvoice.getInvoiceNo());
			outbill.setInvoiceNo4Buy(invoiceIn != null ? (StringUtils.isBlank(invoiceIn.getDocumentNo())
					? invoiceIn.getInvoiceNo() : invoiceIn.getDocumentNo()) : "");
			outbill.setQuantity(storage.getQuantity());
			outbill.setUnit("吨");
			outbill.setPrice(invoiceIn != null ? invoiceIn.getPrice() : BigDecimal.ZERO);
			outBill4SMs.add(outbill);
		}
		Map<String, StorageOutBill4SM> map = new HashMap<>();
		for (StorageOutBill4SM sob : outBill4SMs) {
			String key = sob.getCustomerName() + "|" + sob.getBrandName() + "|" + sob.getProduct() + "|"
					+ sob.getInvoiceNo4Buy() + "|" + sob.getInvoiceNo4Sale() + "|" + sob.getPrice() + "|"
					+ sob.getUnit();
			if (map.containsKey(key)) {
				StorageOutBill4SM value = map.get(key);
				value.setQuantity(value.getQuantity().add(sob.getQuantity()));
			} else {
				map.put(key, sob);
			}
		}

		int rowIndex = 3;
		for (StorageOutBill4SM storage : map.values()) {
			XWPFTableRow addRow = table.insertNewTableRow(table.getNumberOfRows());
			XWPFUtil.CopytTableRow(GetTableRow(table, 1), addRow);
			sumQuantity = sumQuantity.add(storage.getQuantity());
			BigDecimal amount1 = DecimalUtil.Round4s5r(storage.getQuantity().multiply(storage.getPrice()), 2);
			sumAmount = sumAmount.add(amount1);

			List<XWPFTableCell> cells = addRow.getTableCells();
			SetCellText(cells.get(0), storage.getCustomerName());// BrandFactory(storage.BrandName);//客户
			SetCellText(cells.get(1), storage.getBrandName() + storage.getProduct());// 品名
			SetCellText(cells.get(2), storage.getInvoiceNo4Sale());// 销售发票号
			SetCellText(cells.get(3), storage.getInvoiceNo4Buy());// 采购发票号
			SetCellText(cells.get(4), storage.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());// 数量
			SetCellText(cells.get(5), "吨");// 单位
			SetCellText(cells.get(6), storage.getPrice().setScale(2, RoundingMode.HALF_UP).toString());// 不含税单价
			SetCellText(cells.get(7), amount1.setScale(2, RoundingMode.HALF_UP).toString()); // 成本价格
			SetCellText(cells.get(8), curInvoice.getComments()); // 备注
			table.addRow(addRow, rowIndex);
			rowIndex++;
		}

		XWPFTableRow latRow = GetTableLastRow(table);
		List<XWPFTableCell> lastCells = latRow.getTableCells();
		SetCellText(lastCells.get(4), sumQuantity.setScale(3, RoundingMode.HALF_UP).toString());
		SetCellText(lastCells.get(7), sumAmount.setScale(2, RoundingMode.HALF_UP).toString());
	}

	private void AddFinalInvoice(XWPFDocument xmlDoc, CTBookmark bs, Invoice curInvoice) {
		if (curInvoice.getProvisionals() == null || curInvoice.getProvisionals().size() == 0)
			return;
		String currencyStr = curInvoice.getCurrency().equals("USD") ? "$" : "￥";
		XWPFTable table = GetTableByIndex(xmlDoc, 3);
		if (table == null)
			return;

		int rowIndex = 3;
		for (Invoice invoice : curInvoice.getProvisionals()) {
			XWPFTableRow addRow = table.insertNewTableRow(table.getNumberOfRows());
			XWPFUtil.CopytTableRow(GetTableRow(table, 1), addRow);
			BigDecimal gross = BigDecimal.ZERO;
			BigDecimal grossAtFactory = BigDecimal.ZERO;
			BigDecimal netWeight1 = BigDecimal.ZERO;
			if (invoice.getStorages() != null && invoice.getStorages().size() > 0) {
				for (Storage s : invoice.getStorages()) {
					gross = gross.add(s.getGross());
					grossAtFactory = grossAtFactory.add(s.getGrossAtFactory());
					netWeight1 = netWeight1.add(s.getQuantity());
				}
			}
			String gross1 = "";
			if (invoice.getQuantity().compareTo(netWeight1) == 0)
				gross1 = grossAtFactory.setScale(3, RoundingMode.HALF_UP).toString();
			else {
				gross1 = gross.setScale(3, RoundingMode.HALF_UP).toString();
			}

			List<XWPFTableCell> cells = addRow.getTableCells();
			SetCellText(cells.get(1), invoice.getInvoiceNo());// 发票号
			SetCellText(cells.get(2), invoice.getQuantity().setScale(3, RoundingMode.HALF_UP).toString());// 发票净重
			SetCellText(cells.get(3), (gross1 == "0.000" ? "" : gross1));// 采购毛重
			SetCellText(cells.get(4), currencyStr
					+ (invoice.getMajor() != null ? DecimalUtil.getThousandsSeparator(invoice.getMajor(), 2) : ""));// 发票的市价格
			SetCellText(cells.get(5), currencyStr
					+ (invoice.getPremium() != null ? DecimalUtil.getThousandsSeparator(invoice.getPremium(), 2) : ""));// 发票升贴水
			SetCellText(cells.get(6), currencyStr + DecimalUtil.getThousandsSeparator(invoice.getAmount(), 2));// 发票金额
			table.addRow(addRow, rowIndex);
			rowIndex++;
		}

		XWPFTableRow addLastRow = table.insertNewTableRow(table.getNumberOfRows());
		XWPFUtil.CopytTableRow(GetTableRow(table, 1), addLastRow);
		List<XWPFTableCell> lastCells = addLastRow.getTableCells();
		SetCellText(lastCells.get(0), "Balance Payment");
		SetCellText(lastCells.get(5), currencyStr + DecimalUtil.getThousandsSeparator(curInvoice.getAmount(), 2));
		table.addRow(addLastRow, rowIndex + 1);
	}

	public XWPFTable GetTableByIndex(XWPFDocument docx, int index) {
		return docx.getTables().get(index);
	}

	public XWPFParagraph GetTableFirstParagraph(XWPFTableCell cell) {
		return cell.getParagraphs().get(0);
	}

	public XWPFTableRow GetTableRow(XWPFTable table, int rowIndex) {
		return table.getRow(rowIndex);
	}

	/**
	 * @Description: 跨列合并
	 */
	public void mergeCellsHorizontal(XWPFTable table, int row, int fromCell, int toCell) {
		for (int cellIndex = fromCell; cellIndex <= toCell; cellIndex++) {
			XWPFTableCell cell = table.getRow(row).getCell(cellIndex);
			if (cellIndex == fromCell) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.CONTINUE);
			}
		}
	}

	/**
	 * @Description: 跨行合并
	 * @see http://stackoverflow.com/questions/24907541/row-span-with-xwpftable
	 */
	public void mergeCellsVertically(XWPFTable table, int col, int fromRow, int toRow) {
		for (int rowIndex = fromRow; rowIndex <= toRow; rowIndex++) {
			XWPFTableCell cell = table.getRow(rowIndex).getCell(col);
			if (rowIndex == fromRow) {
				// The first merged cell is set with RESTART merge value
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
			} else {
				// Cells which join (merge) the first one, are set with CONTINUE
				cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
			}
			cell.getCTTc().getTcPr().addNewVAlign().setVal(STVerticalJc.CENTER);
		}
	}

	public void setCellText(XWPFTableCell cell, String text, String bgcolor, int width) {
		CTTc cttc = cell.getCTTc();
		CTTcPr cellPr = cttc.addNewTcPr();
		cellPr.addNewTcW().setW(BigInteger.valueOf(width));
		CTTcPr ctPr = cttc.addNewTcPr();
		CTShd ctshd = ctPr.addNewShd();
		ctshd.setFill(bgcolor);
		ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);
		cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);
		cell.setText(text);
	}

	public XWPFTableRow GetTableLastRow(XWPFTable table) {
		return table.getRow(table.getNumberOfRows() - 1);
	}

	public void SetCellText(XWPFTableCell cell, String value) {
		XWPFParagraph para = GetTableFirstParagraph(cell);
		if (para == null)
			return;
		XWPFRun run = para.createRun();
		run.setText(value);
		para.addRun(run);
	}

	private String FundStorageDescription(Invoice invoice) {
		if (invoice == null || invoice.getStorages() == null || invoice.getStorages().size() == 0)
			return "";
		StringBuilder StorageDescription = new StringBuilder();
		Storage storage = invoice.getStorages().get(0);
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage s : invoice.getStorages()) {
			sumQuantity = sumQuantity.add(s.getQuantity());
		}

		// 一个发票只有一个品牌和升贴水
		StorageDescription.append(invoice.getQuantity().setScale(3, RoundingMode.HALF_UP).toString() + "MT/"
				+ storage.getBrandName() + "/" + invoice.getPrice().toString() + "("
				+ (invoice.getPremium() != null ? invoice.getPremium().toString() : "") + ")" + "\r\n");

		return StorageDescription.toString();
	}

	public List<Invoice> Invoices4Sql(String id) {

		String sql = " select distinct Invoice.* " + " from [Physical].[Invoice] Invoice inner join "
				+ " [Physical].[InvoiceStorage] InvoiceStorage on Invoice.id = InvoiceStorage.InvoiceId "
				+ " where InvoiceStorage.StorageId ='" + id + "' ";
		List<Invoice> invoices = invoiceRepo.ExecuteSql(sql, Invoice.class);
		for (Invoice invoice : invoices) {

			if (invoice == null || invoice.getId() == null)
				continue;
			if (invoice.getLot() != null)
				invoice.setFullNo(invoice.getLot().getFullNo());
			if (invoice.getCustomer() != null)
				invoice.setCustomerName(invoice.getCustomer().getName());
			if (invoice.getLegal() != null)
				invoice.setLegalName(invoice.getLegal().getName());
		}
		return invoices;
	}

	/**
	 * 处理中英文/品牌对应工厂等转换，后期需要整理成为数据库字段
	 * 
	 * @param BrandName
	 * @return
	 */
	private static String BrandFactory(String BrandName) {
		String FactoryNameEN = "";
		switch (BrandName.toUpperCase()) {
		case "MKM":
			FactoryNameEN = "MINIERE DE KALUMBWE-MYUNGA (MKM) S.A .S";
			break;
		case "COMILU":
			FactoryNameEN = "LA COMPAGNIE MINIERE DE LUISHA (COMILU) SAS";
			break;
		case "CIMCO":
			FactoryNameEN = "CONGO INTERNATIONAL MINING CORPORATION CIMCO SAS/LUISHA";
			break;
		default:
			FactoryNameEN = "";
			break;

		}
		return FactoryNameEN;
	}

	private static String ProductEn(String product, List<Product> listProducts) {
		for (Product p : listProducts) {
			if (p.getName().equals(product)) {
				return p.getNameEN(); // 使用备注保存
			}
		}
		switch (product) {
		case "电解铜":
			return "copper cathodes";
		case "铜杆":
		case "阴阳铜":
		case "1#锌":
		case "0#锌":
		case "铝镁门":
		case "氢氧化钴":
			return product;
		}
		return "";
	}

}