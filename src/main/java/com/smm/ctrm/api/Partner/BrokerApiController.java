package com.smm.ctrm.api.Partner;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.bo.Basis.BrokerService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;

@Controller
@RequestMapping("api/Partner/Broker/")
public class BrokerApiController {

	@Resource
	private BrokerService brokerService;

	@Resource
	private CheckService checkService;

	/**
	 * 根据id获取实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Broker> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return brokerService.GetById(id);
		} catch (RuntimeException ex) {
			ActionResult<Broker> tempVar = new ActionResult<Broker>();
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
	@RequestMapping("Brokers")
	@ResponseBody
	public ActionResult<List<Broker>> Brokers(HttpServletRequest request) {
		return brokerService.Brokers();
	}

	/**
	 * 不带分页列表
	 * 
	 * @return
	 */
	@RequestMapping("BackBrokers")
	@ResponseBody
	public ActionResult<List<Broker>> BackBrokers(HttpServletRequest request) {
		return brokerService.BackBrokers();
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("Delete")
	@ResponseBody
	public ActionResult<String> Delete(HttpServletRequest request, @RequestBody String id) {
		try {

			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.Position);
			ActionResult<String> checkResult = checkService.deletable(id, "BrokerId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			return brokerService.Delete(id);
		} catch (RuntimeException e) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setStatus(ActionStatus.ERROR);
			tempVar.setMessage("Delete Fail");
			return tempVar;
		}
	}

	/**
	 * 保存
	 * 
	 * @param broker
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Broker> Save(HttpServletRequest request, @RequestBody Broker broker) {
		if (broker.getId() != null) {
			broker.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
		} else {
			broker.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
		}
		return brokerService.Save(broker);
	}
}