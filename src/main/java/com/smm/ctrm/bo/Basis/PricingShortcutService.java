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
public interface PricingShortcutService {
	ActionResult<List<PricingShortcut>> PricingShortcuts();
	ActionResult<List<PricingShortcut>> BackPricingShortcuts();
	ActionResult<PricingShortcut> Save(PricingShortcut org);
	ActionResult<String> Delete(String id);
	ActionResult<PricingShortcut> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);

}