package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.GradeSet;
import com.smm.ctrm.dto.res.ActionResult;

public interface GradeSetService {
	ActionResult<List<GradeSet>> GradeSetsByCommodityId(String commodityId);

	ActionResult<List<GradeSet>> BackGradeSetsByCommodityId(String commodityId);

	ActionResult<List<GradeSet>> GradeSets();

	ActionResult<List<GradeSet>> BackGradeSets();

	ActionResult<GradeSet> Save(GradeSet gradeSet);

	ActionResult<String> Delete(String id);

	ActionResult<GradeSet> GetById(String id);

}