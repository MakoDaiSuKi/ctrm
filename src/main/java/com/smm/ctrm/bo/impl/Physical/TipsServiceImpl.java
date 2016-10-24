package com.smm.ctrm.bo.impl.Physical;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.TipsService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Tip;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class TipsServiceImpl implements TipsService {

	private static final Logger logger = Logger.getLogger(TipsServiceImpl.class);

	@Autowired
	private HibernateRepository<Tip> repository;

	@Autowired
	private CommonService commonService;

	@Override
	public void GetCriteria() {

		this.repository.CreateCriteria(Tip.class);

	}

	@Override
	public ActionResult<Tip> Save(Tip tip) {
		this.repository.SaveOrUpdate(tip);
		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess, tip);
	}

	@Override
	public ActionResult<String> Delete(String id) {

		ActionResult<String> result = new ActionResult<>();

		try {

			this.repository.PhysicsDelete(id, Tip.class);

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
	public ActionResult<Tip> GetById(String id) {

		ActionResult<Tip> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.repository.getOneById(id, Tip.class));

		return result;
	}

	@Override
	public ActionResult<List<Tip>> TipsByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Tip.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<Tip> list = this.repository.GetQueryable(Tip.class).where(where).toList();

		list = this.commonService.SimplifyDataTipList(list);

		ActionResult<List<Tip>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public List<Tip> Tips() {

		return this.repository.GetList(Tip.class);
	}
}
