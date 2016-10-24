package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.ImportTemplate;
import com.smm.ctrm.dto.res.ActionResult;

public interface ImportTemplateService {
	public ActionResult<List<ImportTemplate>> GetByImportTableName(String importTableName);

	public ActionResult<String> SaveList(List<ImportTemplate> list);
}
