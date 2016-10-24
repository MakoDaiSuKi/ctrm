package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.ImportTemplateService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.ImportTemplate;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

@Service
public class ImportTemplateServiceImpl implements ImportTemplateService {

	@Resource
	HibernateRepository<ImportTemplate> importTemplateRepo;

	@Override
	public ActionResult<List<ImportTemplate>> GetByImportTableName(String importTableName) {
		List<ImportTemplate> list = importTemplateRepo.GetQueryable(ImportTemplate.class).where(DetachedCriteria
				.forClass(ImportTemplate.class).add(Restrictions.eqOrIsNull("ImportTableName", importTableName)))
				.toList();
		return new ActionResult<>(true, "", list);
	}

	@Override
	public ActionResult<String> SaveList(List<ImportTemplate> list) {
		for (ImportTemplate template : list) {
			importTemplateRepo.Save(template);
		}
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

}
