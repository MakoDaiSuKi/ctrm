



package com.smm.ctrm.api.Auth;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Controller;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.MenuService;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Auth/Menu/")
public class MenuApiController {

	@Resource
	private MenuService menuService;

	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Menu> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return menuService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Menu> tempVar = new ActionResult<Menu>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 只用于开发人员的、返回全部（包括隐藏）的菜单列表
	 * 
	 * @return
	 */
	@RequestMapping("Menus")
	@ResponseBody
	public ActionResult<List<Menu>> Menus(HttpServletRequest request) {
		String account = LoginHelper.GetLoginInfo(request).Account;
		return menuService.Menus(account);
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
			return menuService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("DeleteMenus")
	@ResponseBody
	public ActionResult<String> DeleteMenus(HttpServletRequest request, @RequestBody List<Menu> menus) {
		try {
			return menuService.DeleteMenus(menus);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param menu
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Menu> Save(HttpServletRequest request, @RequestBody Menu menu) {
		try {
			menu.setResourceType("Menu");

			/**
			 * 因为按照客户端的赋值，进行保存。不必按服务端的语言类别赋值。
			 */
			 menu.setLang(Locale.getDefault().getLanguage());//获取系统语言

			if (menu.getId() != null) {
				menu.setUpdatedAt(new Date());
				menu.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				menu.setCreatedAt(new Date());
				menu.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}

			return menuService.Save(menu);
		} catch (Exception ex) {
			ActionResult<Menu> tempVar = new ActionResult<Menu>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}