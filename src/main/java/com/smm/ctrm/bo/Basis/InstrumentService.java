package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface InstrumentService {
	Criteria GetCriteria();
	ActionResult<List<Instrument>> Instruments();
	ActionResult<List<Instrument>> BackInstruments();
	ActionResult<String> Save(Instrument instrument);
	ActionResult<String> Delete(String instrumentId);
	ActionResult<Instrument> GetById(String instrumentId);
	List<Instrument> Instruments(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);

}