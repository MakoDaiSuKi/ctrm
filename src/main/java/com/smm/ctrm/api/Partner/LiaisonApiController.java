



package com.smm.ctrm.api.Partner;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.LiaisonService;
import com.smm.ctrm.domain.Basis.Liaison;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Partner/Liaison/")
public class LiaisonApiController {
	
	@Resource
	private LiaisonService liaisonService;


	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Liaison> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return liaisonService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Liaison> tempVar = new ActionResult<Liaison>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 获取Customer下的联系人
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetLiaisonsByCustomerId")
	@ResponseBody
	public ActionResult<List<Liaison>> GetLiaisonsByCustomerId(HttpServletRequest request, @RequestBody String id) {
		return liaisonService.Liaisons(id);
	}

	/**
	 * 不带分页列表
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Liaisons")
	@ResponseBody
	public List<Liaison> Liaisons() {
		return liaisonService.Liaisons();
	}

	/**
	 * 根据id删除实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {
			return liaisonService.Delete(id);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("DeleteFail");
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param liaison
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Liaison> Save(HttpServletRequest request, @RequestBody Liaison liaison) {
		try {
			if (liaison.getId()!=null) {
				liaison.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				liaison.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return liaisonService.Save(liaison);
		} catch (RuntimeException e) {
			ActionResult<Liaison> tempVar = new ActionResult<Liaison>();
			tempVar.setSuccess(false);
			tempVar.setData(null);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("SaveFail");
			return tempVar;
		}
	}
}