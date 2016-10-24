package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface LegalBankService {
	ActionResult<List<LegalBank>> LegalBanks();
	ActionResult<List<LegalBank>> LegalBanksByLegalId(String legalId);
	ActionResult<List<LegalBank>> BackLegalBanks();
	ActionResult<LegalBank> Save(LegalBank legalBank);
	ActionResult<String> Delete(String id);
	LegalBank GetById(String id);


}