



package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.OrgService;
import com.smm.ctrm.domain.Basis.Org;
import com.smm.ctrm.domain.apiClient.OrgParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

@Controller
@RequestMapping("api/Auth/Org/")
public class OrgApiController{

	@Resource
	private OrgService orgService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Org> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return orgService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Org> tempVar = new ActionResult<Org>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}

	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("Orgs")
	@ResponseBody
	public ActionResult<List<Org>> Orgs(HttpServletRequest request) {
		return orgService.Orgs();
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	public ActionResult<List<Org>> Pager(HttpServletRequest request, @RequestBody OrgParams param) {
		
		List<Org> orgs;
		int total=0;
		if (param != null) {
			Criteria criteria = orgService.GetCriteria();
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.add(Restrictions.or(Restrictions.like("Name", "%" + param.getKeyword() + "%"),
						Restrictions.like("Address", "%" + param.getKeyword() + "%")));
			}
			RefUtil refTotal=new RefUtil();
			orgs = orgService.Orgs(criteria, param.getPageSize(), param.getPageIndex(), refTotal, param.getSortBy(),
					param.getOrderBy());
		} else {
			orgs = orgService.Orgs().getData();
			total = orgs.size();
		}

		ActionResult<List<Org>> tempVar = new ActionResult<List<Org>>();
		tempVar.setData(orgs);
		tempVar.setTotal(total);
		tempVar.setSuccess(true);
		return tempVar;
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
			return orgService.Delete(id);
		} catch (Exception e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("delete fail");
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param org
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Org> Save(HttpServletRequest request, @RequestBody Org org) {
		try {
			if (org.getId() != null) {
				org.setUpdatedAt(new Date());
				org.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				org.setCreatedAt(new Date());
				org.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return orgService.Save(org);
		} catch (Exception e) {
			ActionResult<Org> tempVar = new ActionResult<Org>();
			tempVar.setSuccess(false);
			tempVar.setData(null);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(MessageCtrm.SaveFaile);
			return tempVar;
		}
	}

}