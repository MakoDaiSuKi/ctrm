



package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.WarehouseService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Warehouse/")
public class WarehouseApiController {
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Resource
	private WarehouseService warehouseService;
	
	@Resource
	private CheckService checkService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Warehouses")
	@ResponseBody
	public ActionResult<List<Warehouse>> Warehouses(HttpServletRequest request) {
		return warehouseService.Warehouses();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackWarehouses")
	@ResponseBody
	public ActionResult<List<Warehouse>> BackWarehouses(HttpServletRequest request) {
		return warehouseService.BackWarehouses();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Warehouse> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return warehouseService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Warehouse> tempVar = new ActionResult<Warehouse>();
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
			tableList.add(TableNameConst.Storage);
			tableList.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult = checkService.deletable(id, "WarehouseId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			tableList.clear();
			tableList.add(TableNameConst.ReceiptShip);
			checkResult = checkService.deletable(id, "WhId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			return warehouseService.Delete(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 保存
	 * 
	 * @param warehouse
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Warehouse> Save(HttpServletRequest request, @RequestBody Warehouse warehouse) {
		if (warehouse.getId()!=null) {
			warehouse.setUpdatedAt(new Date());
			warehouse.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			warehouse.setCreatedAt(new Date());
			warehouse.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}

		return warehouseService.Save(warehouse);
	}

	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			warehouseService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("is can not be empty");
			return tempVar;
		}

		try {
			warehouseService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}
}