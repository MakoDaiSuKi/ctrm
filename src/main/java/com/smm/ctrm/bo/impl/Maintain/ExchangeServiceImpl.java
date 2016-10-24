package com.smm.ctrm.bo.impl.Maintain;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Maintain.ExchangeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Maintain.Exchange;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class ExchangeServiceImpl implements ExchangeService {

	@Autowired
	private HibernateRepository<Exchange> repository;

	@Override
	public ActionResult<String> Save(Exchange exchange) {
		this.repository.SaveOrUpdate(exchange);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> Delete(String id) {
		this.repository.PhysicsDelete(id, Exchange.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<Exchange> GetById(String id) {
		return new ActionResult<>(true, "", repository.getOneById(id, Exchange.class));
	}

	@Override
	public ActionResult<List<Exchange>> Exchanges() {
		List<Exchange> list = this.repository.GetQueryable(Exchange.class).where(DetachedCriteria.forClass(Exchange.class)
				.add(Restrictions.eq("IsHidden", false))).toList();
		return new ActionResult<>(true, "", list);
	}

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Exchange.class);
	}

	@Override
	public List<Exchange> PagerExchanges(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String orderSort) {
		return this.repository.GetPage(criteria, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}
}
