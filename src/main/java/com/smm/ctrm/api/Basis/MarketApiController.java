
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

import com.smm.ctrm.bo.Basis.MarketService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.TableNameConst;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;

@Controller
@RequestMapping("api/Basis/Market/")
public class MarketApiController {

	@Resource
	private MarketService marketService;

	@Resource
	private CheckService checkService;

	/**
	 * 不含分页的列表，仅包括：现货市场
	 * 
	 * @return
	 */
	@RequestMapping("SpotMarkets")
	@ResponseBody
	public ActionResult<List<Market>> SpotMarkets(HttpServletRequest request) {
		return marketService.SpotMarkets();
	}

	/**
	 * 不含分页的列表(显示/隐藏)，仅包括：现货市场
	 * 
	 * @return
	 */
	@RequestMapping("BackSpotMarkets")
	@ResponseBody
	public ActionResult<List<Market>> BackSpotMarkets(HttpServletRequest request) {
		return marketService.BackSpotMarkets();
	}

	/**
	 * 不含分页的列表，仅包括：期货市场
	 * 
	 * @return
	 */
	@RequestMapping("FuturesMarkets")
	@ResponseBody
	public ActionResult<List<Market>> FuturesMarkets(HttpServletRequest request) {
		return marketService.FuturesMarkets();
	}

	/**
	 * 不含分页的列表(显示/隐藏)，仅包括：期货市场
	 * 
	 * @return
	 */
	@RequestMapping("BackFuturesMarkets")
	@ResponseBody
	public ActionResult<List<Market>> BackFuturesMarkets(HttpServletRequest request) {
		return marketService.BackFuturesMarkets();
	}

	/**
	 * 不含分页的列表，包括全部市场
	 * 
	 * @return
	 */
	@RequestMapping("Markets")
	@ResponseBody
	public ActionResult<List<Market>> Markets(HttpServletRequest request) {
		return marketService.Markets();
	}

	/**
	 * 不含分页的列表(显示/隐藏)，包括全部市场
	 * 
	 * @return
	 */
	@RequestMapping("BackMarkets")
	@ResponseBody
	public ActionResult<List<Market>> BackMarkets(HttpServletRequest request) {
		return marketService.BackMarkets();
	}

	/**
	 * 根据Id获得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Market> GetById(HttpServletRequest request, @RequestBody String id) {
		try {
			return marketService.GetById(id);
		} catch (RuntimeException ex) {
			return new ActionResult<>(false, ex.getMessage());
		}
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
			tableList.add(TableNameConst.Pricing);
			ActionResult<String> checkResult = checkService.deletable(id, "MajorMarketId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			checkResult = checkService.deletable(id, "PremiumMarketId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			tableList = new ArrayList<>();
			tableList.add(TableNameConst.Position);
			checkResult = checkService.deletable(id, "MarketId", tableList);
			if (!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}

			marketService.Delete(id);
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
	 * @param market
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<Market> Save(HttpServletRequest request, @RequestBody Market market) {
		try {
			if (market.getId() != null) {
				market.setUpdatedAt(new Date());
				market.setUpdatedBy(LoginHelper.GetLoginInfo(request).getName());
			} else {
				market.setCreatedAt(new Date());
				market.setCreatedBy(LoginHelper.GetLoginInfo(request).getName());
			}
			return marketService.Save(market);
		} catch (RuntimeException ex) {
			ActionResult<Market> tempVar2 = new ActionResult<Market>();
			tempVar2.setSuccess(false);
			tempVar2.setData(market);
			tempVar2.setStatus(ActionStatus.ERROR);
			tempVar2.setMessage(ex.getMessage());
			return tempVar2;
		}
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
			marketService.MoveUp(id);
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
			marketService.MoveUp(id);
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
}