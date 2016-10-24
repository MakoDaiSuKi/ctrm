
package com.smm.ctrm.api.Basis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.BasisService;
import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.OutDocumentService;
import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.bo.impl.Basis.ExportDoc;
import com.smm.ctrm.domain.AttachType;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.StorageTemplate;
import com.smm.ctrm.domain.Physical.Attachment;
import com.smm.ctrm.domain.Physical.vAttachment;
import com.smm.ctrm.domain.apiClient.OutDocument;
import com.smm.ctrm.domain.apiClient.OutDocumentFile;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.DataSource.CTRMOrg;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Common/")
public class CommonApiController {

	@Resource
	private CustomerService customerService;

	@Resource
	private ContractService contractService;

	@Resource
	private LotService lotService;

	@Resource
	private OutDocumentService outDocumentService;

	@Resource
	private BasisService basisService;
	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private CommonService commonService;

	@RequestMapping("UploadFile")
	@ResponseBody
	public ActionResult<String> UploadFile(HttpServletRequest request, HttpServletResponse response, String attachType,
			String id, String fileName) {
		try {

			String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
			String path = commonService.GetServerUploadPath();
			int pointIndex = fileName.lastIndexOf(".");
			if (pointIndex > 0) {
				fileName = fileName.substring(0, pointIndex) + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss")+ fileName.substring(pointIndex);
			} else {
				fileName = fileName + "_" + DateFormatUtils.format(new Date(), "yyyyMMddHHmmss");
			}
			String pathAndName = fileDir + path + "/" + fileName;
			File directory = new File(fileDir + path);
			if (!directory.exists()) {
				directory.mkdirs();
			}
			File file = new File(pathAndName);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			
			int length = 0;
			byte[] byteArr = new byte[1024];
			
			while ((length = request.getInputStream().read(byteArr, 0, 1024)) != -1) {
				fos.write(byteArr,0, length);
			}
			fos.close();
			Attachment attachment = new Attachment();
			attachment.setTradeDate(new Date());
			switch (attachType) {
			case AttachType.Customer:
				attachment.setCustomerId(id);
				break;
			case AttachType.Contract:
				attachment.setContractId(id);
				break;
			case AttachType.Invoice:
				attachment.setInvoiceId(id);
				break;
			case AttachType.Fund:
				attachment.setFundId(id);
				break;
			case AttachType.Storage:
				attachment.setStorageId(id);
				break;
			default:
				break;
			}
			attachment.setIsOutDocument(false);
			attachment.setFileUrl(fileDir);
			attachment.setFileName(fileName);
			attachment.setCreatedBy(LoginHelper.GetLoginInfo().Name);
			attachment.setCreatedId(LoginHelper.GetLoginInfo().UserId);
			commonService.SaveAttachment(attachment);
			return new ActionResult<>(true, "文件生成成功!", null);
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage(), ex);
			ex.getMessage();
			return new ActionResult<>(false, ex.getMessage(), null);
		}
	}

	@RequestMapping("ReplaceFile")
	@ResponseBody
	public ActionResult<String> ReplaceFile(HttpServletRequest request, String guid) {
		try {
			String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
			// 产生文件保存路径
			ActionResult<vAttachment> result = commonService.GetAttachmentById_New(guid);

			if (!result.isSuccess()) {
				return new ActionResult<>(false, "未找到要替换的文件！");
			}

			vAttachment attachment = result.getData();

			if (attachment == null) {
				return new ActionResult<>(false, "未找到要替换的文件！");
			}
			String path = attachment.getIsOutDocument() ? commonService.GetServerOutDocmentPath()
					: commonService.GetServerUploadPath();
			String pathAndName = fileDir + path + "/" + attachment.getFileName();

			// 先删除原文件
			File temp = new File(pathAndName);
			if (temp.exists()) {
				temp.delete();
			}

			File file = new File(pathAndName);

			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			byte[] byteArr = new byte[1024];
			while (request.getInputStream().read(byteArr, 0, 1024) != -1) {
				fos.write(byteArr);
			}
			fos.close();

			return new ActionResult<>(true, "文件替换成功!");
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据模板生成对应的doc文件
	 * 
	 * @param attachType
	 * @param id
	 * @param targetDir
	 * @param fileName
	 * @return
	 */
	@RequestMapping("OutDocumentAddAttachment")
	@ResponseBody
	public ActionResult<String> OutDocumentAddAttachment(String attachType, String id, String targetDir,
			String fileName) {
		try {
			Attachment attachment = new Attachment();
			switch (attachType) {
			case AttachType.Contract:
				attachment.setContractId(id);
				break;
			case AttachType.Invoice:
				attachment.setInvoiceId(id);
				break;
			case AttachType.Pricing:
				attachment.setPricingId(id);
				break;
			case AttachType.Position:
				attachment.setPositionId(id);
				break;
			default:
				break;
			}

			attachment.setFileUrl(targetDir);
			attachment.setFileName(fileName);
			attachment.setCreatedBy(LoginHelper.GetLoginInfo().Name);
			attachment.setCreatedId(LoginHelper.GetLoginInfo().UserId);
			attachment.setIsOutDocument(true);
			commonService.SaveAttachment(attachment);

			return new ActionResult<>(true, "文件生成成功!", null);

		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("CreateOutDocument")
	@ResponseBody
	public ActionResult<String> CreateOutDocument(String attachType, String id, String templateDoc, String dictionaryId)
			throws Exception {
		try {
			ExportDoc docInfo = outDocumentService.buildExportDocByDictionaryId(dictionaryId);
			if (StringUtils.isBlank(templateDoc) && docInfo.getTemplateName() != null) {
				templateDoc = docInfo.getTemplateName();
			}
			String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
			// 获取外部文档模板文件夹路径
			String path = commonService.GetServerOutDocTemplateFilePath();
			String sourceDir = fileDir + path;

			// 获取外部文档模板文件路径
			File sourceDirectory = new File(sourceDir);
			if (!sourceDirectory.exists()) {
				sourceDirectory.mkdirs();
			}

			String sourceFile = sourceDir + "/" + templateDoc + OutDocument.OutDocTemplateExt;

			// 外部文档保存路径
			path = commonService.GetServerOutDocmentPath();
			String targetDir = fileDir + path;
			File targetDirectory = new File(targetDir);
			if (!targetDirectory.exists())
				targetDirectory.mkdirs();

			String fileName = "";
			Attachment attachment = new Attachment();
			attachment.setTradeDate(new Date());

			OutDocumentFile odf = new OutDocumentFile();
			odf.setTempPath(sourceFile);
			odf.setTempFolder(sourceDir);
			odf.setTempFileName(templateDoc);
			odf.setTempFileFullName(templateDoc + OutDocument.OutDocTemplateExt);
			odf.setFileFolder(targetDir);
			odf.setFileRelFolder(path);

			switch (attachType) {
			case AttachType.Contract:
				attachment.setContractId(id);
				odf = outDocumentService.CreateContractPDF(odf, id);
				fileName = odf.getFilePDFFullName();
				break;
			case AttachType.Fund:
				attachment.setFundId(id);
				odf = outDocumentService.CreatePaymentApprovePDF(odf, id);
				fileName = odf.getFilePDFFullName();

				break;
			case AttachType.Invoice:
				attachment.setInvoiceId(id);
				odf = outDocumentService.CreateInvoiceDoc(odf, id);
				fileName = odf.getFileFullName();
				break;
			case AttachType.ContractDoc:
				attachment.setContractId(id);
				odf = outDocumentService.CreateContractDoc(odf, id);
				fileName = odf.getFileFullName();
				break;
			case AttachType.ReceiptShip:
				attachment.setReceiptShipId(id);
				odf = outDocumentService.CreateShipDoc(odf, id, docInfo);
				fileName = odf.getFileFullName();
				break;
			case AttachType.Pricing:
				attachment.setPricingId(id);
				odf = outDocumentService.CreatePricingDoc(odf, id, docInfo);
				fileName = odf.getFileFullName();
				break;
			case AttachType.Position:
				attachment.setPositionId(id);
				odf = outDocumentService.CreatePositionDoc(odf, id, docInfo);
				fileName = odf.getFileFullName();
				break;
			default:
				break;
			}

			if (!odf.isSuccess()) {
				return new ActionResult<>(false, odf.getMessage());
			}

			attachment.setFileUrl(path);
			attachment.setFileName(fileName);
			attachment.setCreatedAt(new Date());
			attachment.setCreatedBy(LoginHelper.GetLoginInfo().Name);
			attachment.setCreatedId(LoginHelper.GetLoginInfo().UserId);
			attachment.setIsOutDocument(true);
			commonService.SaveAttachment(attachment);

			return new ActionResult<>(true, "文件生成成功!");
		} catch (Exception ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("GetAttachments")
	@ResponseBody
	public ActionResult<List<Attachment>> GetAttachments(HttpServletRequest request,
			@RequestBody Map<String, String> dicts) {
		try {
			boolean isOut = false;
			if (StringUtils.isNotBlank(dicts.get("isOutDoc"))) {
				isOut = Boolean.valueOf(dicts.get("isOutDoc"));
			}
			if (StringUtils.isBlank(dicts.get("id"))) {
				return new ActionResult<>(true, "", new ArrayList<>());
			}
			return commonService.GetAttachments(dicts.get("attachType"), isOut, dicts.get("id"));
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@RequestMapping("DeleteAttachmentById")
	@ResponseBody
	public ActionResult<String> DeleteAttachmentById(HttpServletRequest request, @RequestBody String id) {
		try {

			return commonService.DeleteAttachmentById(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("DeleteAttachmentByBillId")
	@ResponseBody
	public ActionResult<String> DeleteAttachmentByBillId(@RequestBody Map<String, String> dicts) {
		try {
			String attachType = null;
			String id = null;
			String isOutDoc = null;
			attachType = dicts.get("attachType");
			id = dicts.get("id");
			isOutDoc = dicts.get("isOutDoc");

			boolean isOut = false;
			if (!StringUtils.isBlank(isOutDoc)) {
				isOut = Boolean.valueOf(isOutDoc);
			}

			return commonService.DeleteAttachmentByBillId(attachType, isOut, id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 
	 * @param guid
	 * @return
	 */
	@RequestMapping("DownloadFileById")
	public void DownloadFileById(HttpServletRequest request, HttpServletResponse response, String guid) {

		ActionResult<Attachment> result = commonService.GetAttachmentById(guid);
		String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
		if (!result.isSuccess()) {
			return;
		} else {
			Attachment attachment = result.getData();
			String path = attachment.getIsOutDocument() ? commonService.GetServerOutDocmentPath()
					: commonService.GetServerUploadPath();
			String pathAndName = fileDir + path + "/" + attachment.getFileName();
			OutputStream out = null;
			try {
				response.reset();
				response.setHeader("Content-Disposition", "attachment; filename=" + attachment.getFileName());
				response.setContentType("application/octet-stream");
				out = response.getOutputStream();
				out.write(FileUtils.readFileToByteArray(new File(pathAndName)));
				out.flush();
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			} finally {
				try {
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
			}
		}

	}

	@RequestMapping("Bvi2Sm")
	@ResponseBody
	public ActionResult<Bvi2Sm> Bvi2Sm(HttpServletRequest request) {
		return basisService.Bvi2Sm();
	}

	@RequestMapping("CheckHasVerifyData")
	@ResponseBody
	public ActionResult<Boolean> CheckHasVerifyData(HttpServletRequest request, @RequestBody String id) {
		try {

			boolean b = commonService.CheckHasVerifyData(id);

			ActionResult<Boolean> tempVar = new ActionResult<Boolean>();
			tempVar.setSuccess(true);
			tempVar.setData(b);
			return tempVar;
		} catch (Exception ex) {
			ActionResult<Boolean> tempVar2 = new ActionResult<Boolean>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 获取审核状态，返回字符串
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetVerifyMessages")
	@ResponseBody
	public ActionResult<String> GetVerifyMessages(HttpServletRequest request, @RequestBody String id) {
		try {

			String str = commonService.GetVerifyMessages(id);

			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setData(str);
			return tempVar;
		} catch (Exception ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 通过字段查询数据ID
	 * 
	 * @param dicts
	 * @return
	 */
	@RequestMapping("GetObjectByName")
	@ResponseBody
	public ActionResult<String> GetObjectByName(HttpServletRequest request, @RequestBody Map<String, String> dicts) {
		try {
			return basisService.GetObjectByName(dicts);
		} catch (Exception ex) {
			ex.printStackTrace();
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 获取产品入库字段模板
	 * 
	 * @return
	 */
	@RequestMapping("LoadStorageTemplateData")
	@ResponseBody
	public ActionResult<List<StorageTemplate>> LoadStorageTemplateData() {
		try {
			return basisService.LoadStorageTemplateData();
		} catch (Exception ex) {
			ActionResult<List<StorageTemplate>> tempVar2 = new ActionResult<List<StorageTemplate>>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 创建产品入库模板字段数据
	 * 
	 * @param request
	 * @param storageTemplateData
	 * @return
	 */
	@RequestMapping("CrerateStorageTemplateData")
	@ResponseBody
	public ActionResult<String> CrerateStorageTemplateData(HttpServletRequest request,
			@RequestBody List<StorageTemplate> storageTemplateData) {
		try {
			return basisService.CrerateStorageTemplateData(storageTemplateData);
		} catch (Exception ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("DownloadTemplateFile")
	@ResponseBody
	public ActionResult<String> DownloadTemplateFile(HttpServletResponse response, String fileName) {
		try {
			String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
			String path = commonService.GetServerOutDocTemplateFilePath();
			String pathAndName = fileDir + path + "/" + fileName;
			File fi = new File(pathAndName);
			byte[] byteArr = null;

			if (fi.exists()) {
				byteArr = FileUtils.readFileToByteArray(fi);
			} else {
				byteArr = "not found!".getBytes();
			}
			response.setContentType("application/octet-stream; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + pathAndName);
			OutputStream out = response.getOutputStream();
			out.write(byteArr);
			out.flush();
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ActionResult<>(false, ex.getMessage());
		}

	}

}