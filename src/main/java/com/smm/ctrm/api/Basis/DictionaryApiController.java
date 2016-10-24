
package com.smm.ctrm.api.Basis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.DictionaryService;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;

@Controller
@RequestMapping("api/Basis/Dictionary/")
public class DictionaryApiController {

	@Resource
	private DictionaryService dictionaryService;

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Dictionary> GetById(@RequestBody String id) {
		try {

			return dictionaryService.GetById(id);
		} catch (Exception ex) {
			ActionResult<Dictionary> tempVar = new ActionResult<Dictionary>();
			tempVar.setSuccess(false);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param dictionary
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Dictionary> Save(@RequestBody Dictionary dictionary) {
		return dictionaryService.Save(dictionary);
	}

	/**
	 * 根据Id删除单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(@RequestBody String id) {
		try {
			return dictionaryService.Delete(id);
		} catch (Exception ex) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage(ex.getMessage());
			return tempVar;
		}
	}

	@RequestMapping("MoveUp")
	@ResponseBody
	public ActionResult<String> MoveUp(@RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			dictionaryService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setStatus(ActionStatus.ERROR);
			tempVar3.setMessage(ex.getMessage());
			return tempVar3;
		}
	}

	@RequestMapping("MoveDown")
	@ResponseBody
	public ActionResult<String> MoveDown(@RequestBody String id) {
		if (StringUtils.isBlank(id)) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("id can not be empty");
			return tempVar;
		}

		try {
			dictionaryService.MoveUp(id);
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(true);
			tempVar2.setStatus(ActionStatus.SUCCESS);
			return tempVar2;
		} catch (Exception ex) {
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
	@RequestMapping("Dictionaries")
	@ResponseBody
	public ActionResult<List<Dictionary>> Dictionaries() {
		return dictionaryService.Dictionaries();
	}

	/**
	 * 不含分页的列表(显示/隐藏)
	 * 
	 * @return
	 */
	@RequestMapping("BackDictionaries")
	@ResponseBody
	public ActionResult<List<Dictionary>> BackDictionaries() {
		return dictionaryService.BackDictionaries();
	}

	// 不要删除以下的方法，不清楚用在什么场合
	@RequestMapping("AllDictValue")
	@ResponseBody
	public Map<String, List<Dictionary>> AllDictValue() {

		List<Dictionary> values = dictionaryService.ItemsVisibleOnly();
		List<String> codes = values.stream().map(s -> s.getCode()).distinct().collect(Collectors.toList());
		Map<String, List<Dictionary>> dict = new HashMap<>();

		for (String code : codes) {
			List<Dictionary> thisValues = values.stream().filter(s -> s.getCode() == code).collect(Collectors.toList());
			dict.put(code, thisValues);
		}
		return dict;
	}

	@RequestMapping("Cats")
	@ResponseBody
	public ActionResult<List<Dictionary>> Cats() {
		return dictionaryService.Cats();
	}

	@RequestMapping("ItemsByCatId")
	@ResponseBody
	public ActionResult<List<Dictionary>> ItemsByCatId(@RequestBody String catId) {
		List<Dictionary> item = dictionaryService.ItemByCatId(catId);
		ActionResult<List<Dictionary>> tempVar = new ActionResult<List<Dictionary>>();
		tempVar.setSuccess(true);
		tempVar.setData(item);
		return tempVar;
	}

	@RequestMapping("SaveCat")
	@ResponseBody
	public ActionResult<Dictionary> SaveCat(@RequestBody Dictionary dictionary) {
		dictionaryService.Save(dictionary);
		ActionResult<Dictionary> tempVar = new ActionResult<Dictionary>();
		tempVar.setSuccess(true);
		return tempVar;
	}

	@RequestMapping("SaveItem")
	@ResponseBody
	public ActionResult<Dictionary> SaveItem(@RequestBody Dictionary dictionary) {
		return dictionaryService.Save(dictionary);
	}

	@RequestMapping("DeleteCat")
	@ResponseBody
	public ActionResult<String> DeleteCat(@RequestBody String id) {
		return dictionaryService.DeleteCat(id);
	}

	@RequestMapping("DeleteItem")
	@ResponseBody
	public ActionResult<String> DeleteItem(@RequestBody String id) {
		return dictionaryService.DeleteItem(id);
	}

}