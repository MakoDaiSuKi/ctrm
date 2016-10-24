package com.smm.ctrm.bo.impl.Basis;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Basis.CommodityService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CommodityServiceImpl implements CommodityService {

	private static final Logger logger = Logger.getLogger(CommodityServiceImpl.class);

	@Autowired
	private HibernateRepository<Commodity> repository;

	@Override
	public ActionResult<Commodity> Save(Commodity commodity) {

		ActionResult<Commodity> result = new ActionResult<>();

		try {

			DetachedCriteria dc = DetachedCriteria.forClass(Commodity.class);
			if (commodity.getId() != null) {
				dc.add(Restrictions.ne("Id", commodity.getId()));
			}
			dc.add(Restrictions.or(Restrictions.eq("Name", commodity.getName()),
					Restrictions.eq("Code", commodity.getCode())));
			List<Commodity> obj = this.repository.GetQueryable(Commodity.class).where(dc).toList();
			if (obj != null && obj.size() > 0) {
				return new ActionResult<>(false, MessageCtrm.DuplicatedName);
			}

			this.repository.SaveOrUpdate(commodity);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<String> Delete(String id) {
		ActionResult<String> result = new ActionResult<>();

		try {

			this.repository.PhysicsDelete(id, Commodity.class);

			result.setSuccess(true);
			result.setMessage(MessageCtrm.DeleteSuccess);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<List<Commodity>> Commodities() {

		DetachedCriteria where = DetachedCriteria.forClass(Commodity.class);
		where.add(Restrictions.eq("IsHidden", false));

		List<Commodity> list = this.repository.GetQueryable(Commodity.class).where(where).toCacheList();

		ActionResult<List<Commodity>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Commodity>> BackCommodities() {

		List<Commodity> list = this.repository.GetQueryable(Commodity.class).OrderBy(Order.desc("OrderIndex")).toList();

		ActionResult<List<Commodity>> result = new ActionResult<>();
		result.setSuccess(true);
		result.setData(list);
		return result;
	}

	@Override
	public ActionResult<Commodity> GetById(String id) {

		Commodity commodity = repository.getOneById(id, Commodity.class);
		return new ActionResult<>(true, "", commodity);
	}
}
