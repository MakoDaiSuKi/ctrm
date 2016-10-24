



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.PeriodService;
import com.smm.ctrm.domain.Basis.Period;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Period/")
public class PeriodApiController {
	
	@Resource
	private PeriodService periodService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Periods")
	@ResponseBody
	public ActionResult<List<Period>> Periods(HttpServletRequest request) {
		return periodService.Periods();

	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackPeriods")
	@ResponseBody
	public ActionResult<List<Period>> BackPeriods(HttpServletRequest request) {
		return periodService.BackPeriods();

	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Period> GetById(HttpServletRequest request, @RequestBody String id) {
		try {

			return periodService.GetById(id);
		} catch (RuntimeException ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 根据Id删除单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return periodService.Delete(id);
		} catch (RuntimeException ex) {
			return new ActionResult<>(false, ex.getMessage());
		}

	}

	/**
	 * 保存
	 * 
	 * @param period
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Period> Save(HttpServletRequest request, @RequestBody Period period) {
		if (period.getId()!=null) {
			period.setUpdatedAt(new Date());
			period.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			period.setCreatedAt(new Date());
			period.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}

		return periodService.Save(period);
	}

	@RequestMapping("Set2Current")
	@ResponseBody
	public ActionResult<List<Period>> Set2Current(HttpServletRequest request, @RequestBody Period period) {
		return periodService.Set2Current(period);
	}
}