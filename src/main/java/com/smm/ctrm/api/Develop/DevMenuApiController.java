
package com.smm.ctrm.api.Develop;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Maintain.DevelopService;
import com.smm.ctrm.domain.Basis.Button;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Develop/DevMenu/")
public class DevMenuApiController {

	@Autowired
	private DevelopService developService;

	@RequestMapping("MenuById")
	@ResponseBody
	public ActionResult<Menu> MenuById(HttpServletRequest request, @RequestBody String id) {
		try {
			return developService.MenuById(id);
		} catch (RuntimeException ex) {
			ActionResult<Menu> tempVar = new ActionResult<Menu>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			ex.printStackTrace();
			return tempVar;
		}
	}

	@RequestMapping("ButtonById")
	@ResponseBody
	public ActionResult<Button> ButtonById(HttpServletRequest request, @RequestBody String id) {
		try {
			// implicit typing in Java:
			Button btn = developService.ButtonById(id);

			ActionResult<Button> tempVar = new ActionResult<Button>();
			tempVar.setSuccess(true);
			tempVar.setData(btn);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<Button> tempVar2 = new ActionResult<Button>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("Menus")
	@ResponseBody
	public ActionResult<List<Menu>> Menus(HttpServletRequest request) {
		return developService.Menus();
	}

	@RequestMapping("Resources")
	@ResponseBody
	public ActionResult<List<Resource>> Resources(HttpServletRequest request) {
		try {

			List<Resource> res = developService.Resources();
			ActionResult<List<Resource>> tempVar = new ActionResult<>();
			tempVar.setSuccess(true);
			tempVar.setData(res);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<List<Resource>> tempVar2 = new ActionResult<List<Resource>>();
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("DeleteMenu")
	@ResponseBody
	public ActionResult<String> DeleteMenu(HttpServletRequest request, @RequestBody String id) {
		try {
			return developService.DeleteMenu(id);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("DeleteMenus")
	@ResponseBody
	public ActionResult<String> DeleteMenus(HttpServletRequest request, @RequestBody List<Resource> resources) {
		try {
			return developService.DeleteResources(resources);
		} catch (RuntimeException ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("SaveButton")
	@ResponseBody
	public ActionResult<Button> SaveButton(HttpServletRequest request, @RequestBody Button button) {
		try {
			button.setResourceType("Button");

			if (button.getId() != null) {
				button.setUpdatedAt(new Date());
				button.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				button.setCreatedAt(new Date());
				button.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}

			Button btn = developService.SaveButton(button);

			ActionResult<Button> tempVar = new ActionResult<Button>();
			tempVar.setSuccess(true);
			tempVar.setMessage("save success");
			tempVar.setData(btn);
			return tempVar;
		} catch (RuntimeException ex) {
			ActionResult<Button> tempVar2 = new ActionResult<Button>();
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
	}

	@RequestMapping("SaveMenu")
	@ResponseBody
	public ActionResult<Menu> SaveMenu(HttpServletRequest request, @RequestBody Menu menu) {
		try {
			menu.setResourceType("Menu");
			 menu.setLang("zh-CN");
			if (menu.getId() != null) {
				menu.setUpdatedAt(new Date());
				menu.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				menu.setCreatedAt(new Date());
				menu.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}

			return developService.SaveMenu(menu);
		} catch (RuntimeException ex) {
			ActionResult<Menu> tempVar = new ActionResult<Menu>();
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}
}