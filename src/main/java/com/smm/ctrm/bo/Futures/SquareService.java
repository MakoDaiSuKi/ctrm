package com.smm.ctrm.bo.Futures;

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
public interface SquareService {
	Criteria GetCriteria();
	ActionResult<Square> GetById(String id);
	ActionResult<String> Save(Square position);
	ActionResult<String> Delete(String id);
	ActionResult<String> SqaureMannually(List<Position> longs, List<Position> shorts);
	ActionResult<String> SqaureAutomatically();
	List<Square> PositionsSquared(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy);

}