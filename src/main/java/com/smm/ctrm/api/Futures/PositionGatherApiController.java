package com.smm.ctrm.api.Futures;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Futures.PositionGatherService;
import com.smm.ctrm.domain.Physical.PositionGather;
import com.smm.ctrm.domain.apiClient.PositionGatherParams;
import com.smm.ctrm.dto.res.ActionResult;

/**
 * @author zhaoyutao
 *
 */
@Controller
@RequestMapping("api/Futures/PositionGather/")
public class PositionGatherApiController {

	private static Logger logger = Logger.getLogger(PositionGatherApiController.class);

	@Resource
	private PositionGatherService positionGatherService;

	/**
	 * 保存
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(HttpServletRequest request, @RequestBody PositionGather positionGather) {
		try {
			return positionGatherService.Save(positionGather);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<PositionGather>> Pager(HttpServletRequest request,
			@RequestBody PositionGatherParams param) {
		try {
			return positionGatherService.Pager(param);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
	
	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request,
			@RequestBody String id) {
		try {
			return positionGatherService.Delete(id);
		} catch (RuntimeException ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}
}