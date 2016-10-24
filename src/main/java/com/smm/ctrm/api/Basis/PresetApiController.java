



package com.smm.ctrm.api.Basis;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.PresetService;
import com.smm.ctrm.domain.Basis.Preset;
import com.smm.ctrm.dto.res.ActionResult;

@Controller
@RequestMapping("api/Basis/Preset/")
public class PresetApiController {
	
	@Resource
	private PresetService presetService;

	

	@RequestMapping("Presets")
	@ResponseBody
	public ActionResult<List<Preset>> Presets(HttpServletRequest request) {
		return presetService.Presets();
	}

	@RequestMapping("BackPresets")
	@ResponseBody
	public ActionResult<List<Preset>> BackPresets(HttpServletRequest request) {
		return presetService.BackPresets();
	}

	@RequestMapping("SavePreset")
	@ResponseBody
	public ActionResult<Preset> SavePreset(HttpServletRequest request, @RequestBody Preset preset) {
		return presetService.SavePreset(preset);
	}

	@RequestMapping("DeletePreset")
	@ResponseBody
	public ActionResult<Preset> DeletePreset(HttpServletRequest request, @RequestBody String id)
	{
		try
		{
			return presetService.DeletePreset(id);
			/*ActionResult<Preset> actionResult=new ActionResult<>();
			actionResult.setSuccess(true);
			return actionResult;*/
		}
		catch (Exception e)
		{
			ActionResult<Preset> actionResult=new ActionResult<>();
			actionResult.setSuccess(false);
			actionResult.setMessage(e.getMessage());
			return actionResult;
		}
	}
}