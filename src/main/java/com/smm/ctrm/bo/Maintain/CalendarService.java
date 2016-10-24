package com.smm.ctrm.bo.Maintain;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
public interface CalendarService {
	ActionResult<String> Sync();
	ActionResult<String> Save(Calendar calendar);
	ActionResult<String> Delete(String id);
	ActionResult<Calendar> GetById(String id);
	Criteria GetCriteria();
	List<Calendar> Calendars();

	ActionResult<List<Calendar>> GetPager(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total);


}