package com.smm.ctrm.bo.Maintain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;

import com.smm.ctrm.util.RefUtil;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface FetchCalendarService {
	ActionResult<String> GenerateLmbCalendar(Date dtDateStartTime, Date dtDateEndTime);
	ActionResult<String> GenerateLmeCalendar(Date dtDateStartTime, Date dtDateEndTime);
	ActionResult<String> GenerateSfeCalendar(Date dtDateStartTime, Date dtDateEndTime);
	ActionResult<String> GenerateDsmeCalendar(Date dtDateStartTime, Date dtDateEndTime);
	ActionResult<String> SaveVm(VmFetchCalendar vm);
	ActionResult<String> Save(FetchCalendar fetchCalendar);
	ActionResult<String> Delete(String id);
	ActionResult<VmFetchCalendar> GetVmById(Date tradeDate);
	ActionResult<FetchCalendar> GetById(String id);
    Criteria GetCriteria();

    List<FetchCalendar> FetchCalendars(Criteria param, int pageSize, int pageIndex, RefUtil total,String orderBy, String orderSort);

}