



package com.smm.ctrm.api.Basis;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.PricingTypeService;
import com.smm.ctrm.domain.Basis.PricingShortcut;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Basis/PricingType/")
public class PricingTypeApiController {
	
	@Resource
	private PricingTypeService pricingTypeService;

	@RequestMapping("PricingTypes")
	@ResponseBody
	public ActionResult<List<PricingShortcut>> PricingTypes(HttpServletRequest request) {
		return pricingTypeService.PricingTypes();
	}

	@RequestMapping("BackPricingTypes")
	@ResponseBody
	public ActionResult<List<PricingShortcut>> BackPricingTypes(HttpServletRequest request) {
		return pricingTypeService.BackPricingTypes();
	}
}