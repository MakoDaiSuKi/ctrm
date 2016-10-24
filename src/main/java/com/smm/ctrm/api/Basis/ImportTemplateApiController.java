package com.smm.ctrm.api.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.ImportTemplateService;
import com.smm.ctrm.domain.Basis.ImportTemplate;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Basis/ImportTemplate/")
public class ImportTemplateApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private ImportTemplateService importTemplateService;

	@RequestMapping("GetByImportTableName")
	@ResponseBody
	public ActionResult<List<ImportTemplate>> GetByImportTableName(@RequestBody String ImportTableName) {
		try {
			return importTemplateService.GetByImportTableName(ImportTableName);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	@RequestMapping("SaveList")
	@ResponseBody
	public ActionResult<String> SaveList(@RequestBody List<ImportTemplate> list) {
		try {
			return importTemplateService.SaveList(list);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

}
