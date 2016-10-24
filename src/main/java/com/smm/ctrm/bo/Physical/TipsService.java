package com.smm.ctrm.bo.Physical;

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
public interface TipsService {
	void GetCriteria();
	ActionResult<Tip> Save(Tip tip);
	ActionResult<String> Delete(String id);
	ActionResult<Tip> GetById(String id);
	ActionResult<List<Tip>> TipsByLotId(String lotId);
    List<Tip> Tips();

}