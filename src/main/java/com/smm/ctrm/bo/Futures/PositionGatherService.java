package com.smm.ctrm.bo.Futures;

import java.util.List;

import com.smm.ctrm.domain.Physical.PositionGather;
import com.smm.ctrm.domain.apiClient.PositionGatherParams;
import com.smm.ctrm.dto.res.ActionResult;

public interface PositionGatherService {

	public ActionResult<String> Save(PositionGather positionGather);

	public ActionResult<List<PositionGather>> Pager(PositionGatherParams param);

	public ActionResult<String> Delete(String id);
}
