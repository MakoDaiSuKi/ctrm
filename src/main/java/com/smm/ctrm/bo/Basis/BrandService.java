package com.smm.ctrm.bo.Basis;

import java.util.List;

import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.dto.res.ActionResult;

public interface BrandService {
	ActionResult<List<Brand>> BrandsByCommodityId(String commodityId);

	ActionResult<List<Brand>> BackBrandsByCommodityId(String commodityId);

	ActionResult<List<Brand>> Brands();

	ActionResult<List<Brand>> BackBrands();

	ActionResult<Brand> Save(Brand brand);

	ActionResult<String> Delete(String id);

	ActionResult<Brand> GetById(String id);

}