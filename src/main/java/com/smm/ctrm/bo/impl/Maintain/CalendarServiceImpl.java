package com.smm.ctrm.bo.impl.Maintain;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Maintain.CalendarService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.Maintain.Calendar;
import com.smm.ctrm.domain.apiClient.WebApiClient;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CalendarServiceImpl implements CalendarService {

	@Autowired
	private HibernateRepository<Calendar> repository;

	@Autowired
	private CommonService commonService;

	@Override
	public ActionResult<String> Sync() {
		List<Calendar> calendars = GetCalendars4Sync();
		if (calendars == null || calendars.size() == 0)
			throw new RuntimeException("无法同步");

		// 批量保存
		calendars.forEach(c -> {

			DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
			where.add(Restrictions.eq("MarketId", c.getMarketId()));
			where.add(Restrictions.eq("TradeDate", c.getTradeDate()));
			where.add(Restrictions.ne("Id", c.getId()));

			List<Calendar> list = this.repository.GetQueryable(Calendar.class).where(where).toList();

			if (list == null || list.size() == 0) {

				c.setId(null);
				c.setVersion(0);
				this.repository.SaveOrUpdate(c);
			}

		});
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);

	}

	@Override
	public ActionResult<String> Save(Calendar calendar) {
		// 检查重复记录
		DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
		where.add(Restrictions.eq("MarketId", calendar.getMarketId()));
		where.add(Restrictions.eq("TradeDate", calendar.getTradeDate()));
		where.add(Restrictions.neOrIsNotNull("Id", calendar.getId()));
		List<Calendar> list = this.repository.GetQueryable(Calendar.class).where(where).toList();
		if (list != null && list.size() > 0)
			throw new RuntimeException(MessageCtrm.DuplicatedName);
		this.repository.SaveOrUpdate(calendar);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> Delete(String id) {
		this.repository.PhysicsDelete(id, Calendar.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<Calendar> GetById(String id) {

		ActionResult<Calendar> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.repository.getOneById(id, Calendar.class));

		return result;
	}

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Calendar.class);

	}

	@Override
	public List<Calendar> Calendars() {

		DetachedCriteria where = DetachedCriteria.forClass(Calendar.class);
		where.add(Restrictions.eq("IsHidden", false));

		return this.repository.GetQueryable(Calendar.class).where(where).toList();
	}

	private List<Calendar> GetCalendars4Sync() {

		LoginInfo syncLoginInfo = this.commonService.GetSyncLoginInfo();

		if (syncLoginInfo == null)
			return null;

		List<Calendar> calendars = null;

		// #region 获取数据

		Date targetDate = DateUtil.getDiffDate(new Date(), -7);

		HashMap<String, String> param = new HashMap<>();

		param.put("StartDate", targetDate.toString());

		WebApiClient webApiClient = new WebApiClient(CommonService.SyncUrl, syncLoginInfo.toString());

		String result = webApiClient.Post("api/Maintain/Calendar/Pager", param);

		if (!StringUtils.isEmpty(result)) {

			@SuppressWarnings("unchecked")
			ActionResult<List<Calendar>> resultObj = JSON.parseObject(result, ActionResult.class);

			if (resultObj.isSuccess()) {

				calendars = resultObj.getData();
			}

		}
		return calendars;
	}

	@Override
	public ActionResult<List<Calendar>> GetPager(Criteria criteria, int pageSize, int pageIndex, String sortBy,
			String orderBy, RefUtil total) {
		return repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total);
	}

}
