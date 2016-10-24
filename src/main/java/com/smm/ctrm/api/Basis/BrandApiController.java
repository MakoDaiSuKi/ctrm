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

import com.smm.ctrm.bo.Basis.BrandService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;

@Controller
@RequestMapping("api/Basis/Brand/")
public class BrandApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	@Resource
	private BrandService brandService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Brands")
	@ResponseBody
	public ActionResult<List<Brand>> Brands(HttpServletRequest request) {
		return brandService.Brands();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackBrands")
	@ResponseBody
	public ActionResult<List<Brand>> BackBrands(HttpServletRequest request) {
		return brandService.BackBrands();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Brand> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return brandService.GetById(id);
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
	public ActionResult<Brand> Save(HttpServletRequest request, @RequestBody Brand brand) {
		return brandService.Save(brand);
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
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.LotBrand);
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult = checkService.deletable(id, "BrandId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			return brandService.Delete(id);
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
	@RequestMapping("BrandsByCommodityId")
	@ResponseBody
	public ActionResult<List<Brand>> BrandsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return brandService.BrandsByCommodityId(id);
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
	@RequestMapping("BackBrandsByCommodityId")
	@ResponseBody
	public ActionResult<List<Brand>> BackBrandsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return brandService.BackBrandsByCommodityId(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}