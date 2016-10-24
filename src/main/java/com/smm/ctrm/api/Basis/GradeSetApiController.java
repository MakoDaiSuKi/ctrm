package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.GradeSetService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.GradeSet;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;

@Controller
@RequestMapping("api/Basis/GradeSet/")
public class GradeSetApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private GradeSetService gradeSetService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("GradeSets")
	@ResponseBody
	public ActionResult<List<GradeSet>> GradeSets(HttpServletRequest request) {
		return gradeSetService.GradeSets();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackGradeSets")
	@ResponseBody
	public ActionResult<List<GradeSet>> BackGradeSets(HttpServletRequest request) {
		return gradeSetService.BackGradeSets();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<GradeSet> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return gradeSetService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param brand
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<GradeSet> Save(HttpServletRequest request, @RequestBody GradeSet gradeSet) {
		return gradeSetService.Save(gradeSet);
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
			/*List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.LotBrand);
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult = checkService.deletable(id, "BrandId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}*/
			return gradeSetService.Delete(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 不含分页，返回指定品种的品牌的列表
	 * 
	 * @return
	 */
	@RequestMapping("GradeSetsByCommodityId")
	@ResponseBody
	public ActionResult<List<GradeSet>> GradeSetsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return gradeSetService.GradeSetsByCommodityId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 不含分页，返回指定品种的品牌的列表
	 * 
	 * @return
	 */
	@RequestMapping("BackGradeSetsByCommodityId")
	@ResponseBody
	public ActionResult<List<GradeSet>> BackGradeSetsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return gradeSetService.BackGradeSetsByCommodityId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}