



package com.smm.ctrm.api.Basis;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.RemarkService;
import com.smm.ctrm.domain.Basis.Remark;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Basis/Remark/")
public class RemarkApiController {
	
	@Resource
	private RemarkService remarkService;

	/**
	 * 不含分页的列表
	 * 
	 * @return
	 */
	@RequestMapping("Remarks")
	@ResponseBody
	public ActionResult<List<Remark>> Remarks(HttpServletRequest request) {
		return remarkService.Remarks();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackRemarks")
	@ResponseBody
	public ActionResult<List<Remark>> BackRemarks(HttpServletRequest request) {
		return remarkService.BackRemarks();
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
			return remarkService.Delete(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param remark
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Remark> Save(HttpServletRequest request, @RequestBody Remark remark) {
		try {
			if (remark.getId()!=null) {
				remark.setUpdatedAt(new Date());
				remark.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				remark.setCreatedAt(new Date());
				remark.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return remarkService.Save(remark);
		} catch (RuntimeException ex) {
			ActionResult<Remark> tempVar = new ActionResult<Remark>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Remark> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			
			return remarkService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Remark> tempVar = new ActionResult<Remark>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}