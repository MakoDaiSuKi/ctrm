



package com.smm.ctrm.api.Basis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.OriginService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Basis/Origin/")
public class OriginApiController {
	
	@Resource
	private OriginService originService;
	
	@Resource
	private CheckService checkService;


	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Origins")
	@ResponseBody
	public ActionResult<List<Origin>> Origins(HttpServletRequest request) {
		return originService.Origins();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackOrigins")
	@ResponseBody
	public ActionResult<List<Origin>> BackOrigins(HttpServletRequest request) {
		return originService.BackOrigins();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Origin> GetById(HttpServletRequest request, @RequestBody String id) {
		return originService.GetById(id);
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
			tableList.add(TableNameConst.Lot);
			tableList.add(TableNameConst.Storage);
			ActionResult<String> checkResult = checkService.deletable(id, "OriginId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			
			
			
			originService.Delete(id);
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(true);
			tempVar.setStatus(ActionStatus.SUCCESS);
			tempVar.setMessage(MessageCtrm.DeleteSuccess);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	/**
	 * 保存
	 * 
	 * @param origin
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Origin> Save(HttpServletRequest request, @RequestBody Origin origin) {
		if (origin.getId()!=null) {
			origin.setUpdatedAt(new Date());
			origin.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			origin.setCreatedAt(new Date());
			origin.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return originService.Save(origin);
	}

	/**
	 * 指定控件向上移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			originService.MoveUp(id);
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

	/**
	 * 指定控件向下移动
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(HttpServletRequest request, @RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			originService.MoveUp(id);
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

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("OriginsByCommodityId")
	@ResponseBody
	public ActionResult<List<Origin>> OriginsByCommodityId(HttpServletRequest request, @RequestBody String id) {
		try {
			return originService.OriginsByCommodityId(id);
		} catch (RuntimeException ex) {
			ActionResult<List<Origin>> tempVar = new ActionResult<List<Origin>>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}