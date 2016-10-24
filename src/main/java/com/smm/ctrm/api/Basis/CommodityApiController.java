
package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.CommodityService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Commodity/")
public class CommodityApiController {

	@Resource
	private CommodityService commodityService;
	
	@Resource
	private CheckService checkService;
	
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Commodity> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return commodityService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Commodity> tempVar = new ActionResult<Commodity>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
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
			
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Contract);
			tableList.add(TableNameConst.Position);
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			tableList.add(TableNameConst.Product);
			tableList.add(TableNameConst.Brand);
			tableList.add(TableNameConst.Spec);
			tableList.add(TableNameConst.Origin);
			ActionResult<String> checkResult = checkService.deletable(id, "CommodityId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			return commodityService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param commodity
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Commodity> Save(HttpServletRequest request, @RequestBody Commodity commodity) {
		// 对于任何一个Save，必须加下面的4行代码
		if (commodity.getId() != null) {
			commodity.setUpdatedAt(new Date());
			commodity.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			commodity.setCreatedAt(new Date());
			commodity.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}

		// 开始保存
		return commodityService.Save(commodity);
	}

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Commodities")
	@ResponseBody
	public ActionResult<List<Commodity>> Commodities(HttpServletRequest request) {
		try {
			return commodityService.Commodities();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);;
			return new ActionResult<List<Commodity>>(false, e.getMessage());
		}

	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackCommodities")
	@ResponseBody
	public ActionResult<List<Commodity>> BackCommodities(HttpServletRequest request) {
		return commodityService.BackCommodities();
	}
}