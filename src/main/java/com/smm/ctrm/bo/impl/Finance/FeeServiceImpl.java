package com.smm.ctrm.bo.impl.Finance;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Finance.FeeService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fee;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
public class FeeServiceImpl implements FeeService {

	private static final Logger logger = Logger.getLogger(FeeServiceImpl.class);

	@Autowired
	private HibernateRepository<Fee> repository;

	@Autowired
	private HibernateRepository<Contract> repositoryContract;

	@Autowired
	private HibernateRepository<Lot> repositoryLot;

	@Override
	public void GetCriteria() {

		this.repository.CreateCriteria(Fee.class);
	}

	@Override
	public ActionResult<String> Save(Fee fee) {

		// 检查重复
		DetachedCriteria where = DetachedCriteria.forClass(Fee.class);
		where.add(Restrictions.eq("LotId", fee.getLotId()));
		where.add(Restrictions.eq("FeeCode", fee.getFeeCode()));
		where.add(Restrictions.neOrIsNotNull("Id", fee.getId()));

		List<Fee> list = this.repository.GetQueryable(Fee.class).where(where).toList();

		if (list != null && list.size() > 0)
			throw new RuntimeException("针对同一批次，只能设置唯一一次相同杂费Code的记录。");
		this.repository.SaveOrUpdate(fee);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);

	}

	@Override
	public ActionResult<String> Delete(String feeId) {

		ActionResult<String> result = new ActionResult<>();

		try {

			Fee fee = this.repository.getOneById(feeId, Fee.class);

			if (fee == null)
				throw new Exception("已经过账，不允许删除");

			this.repository.PhysicsDelete(feeId, Fee.class);

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
	public ActionResult<Fee> GetById(String feeId) {

		ActionResult<Fee> result = new ActionResult<>();
		Fee fee = this.repository.getOneById(feeId, Fee.class);
		if (fee.getContractId() != null) {
			fee.setContract(this.repositoryContract.getOneById(fee.getContractId(), Contract.class));
		}
		if (fee.getLotId() != null) {
			fee.setLot(this.repositoryLot.getOneById(fee.getLotId(), Lot.class));
		}
		result.setSuccess(true);
		result.setData(fee);

		return result;
	}

	@Override
	public ActionResult<List<Fee>> FeesByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Fee.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<Fee> list = this.repository.GetQueryable(Fee.class).where(where).toList();

		ActionResult<List<Fee>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}
}
