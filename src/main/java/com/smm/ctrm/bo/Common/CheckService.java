package com.smm.ctrm.bo.Common;

import java.util.List;

import com.smm.ctrm.dto.res.ActionResult;

public interface CheckService {
	
	public ActionResult<String> deletable(String value, String column, List<String> tableList);

}
