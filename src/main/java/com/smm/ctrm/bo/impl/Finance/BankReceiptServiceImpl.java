package com.smm.ctrm.bo.impl.Finance;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Finance.BankReceiptService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.BankReceipt;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;

/**
 * 
 * @author tantaigen
 *
 */
@Service
public class BankReceiptServiceImpl implements BankReceiptService {
	private static final Logger logger = Logger.getLogger(BankReceiptServiceImpl.class);

	@Autowired
	private HibernateRepository<BankReceipt> repository;

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(BankReceipt.class);
	}

	/// <summary>
	/// 根据Id取得单个实体
	/// </summary>
	/// <param name="id"></param>
	/// <returns></returns>
	public ActionResult<BankReceipt> GetById(String id) {
		if (id == null || "".equals(id)) {
			logger.error("参数错误");
			return new ActionResult<>(Boolean.FALSE, "参数错误");
		}
		ActionResult<BankReceipt> result = new ActionResult<>();
		result.setSuccess(true);
		result.setData(repository.getOneById(id, BankReceipt.class));

		return result;
	}

	public ActionResult<List<BankReceipt>> GetReceiptListByCustomerId(String customerId) {
		if (customerId == null || "".equals(customerId)) {
			logger.error("参数错误");
			return new ActionResult<>(Boolean.FALSE, "参数错误");
		}
		ActionResult<List<BankReceipt>> result = new ActionResult<>();
		List<BankReceipt> list = repository.GetQueryable(BankReceipt.class)
				.where(DetachedCriteria.forClass(BankReceipt.class).add(Restrictions.in("CustomerId", customerId)))
				.toList();
		result.setSuccess(true);
		result.setData(list);
		return result;
	}

	public List<BankReceipt> BankReceipt(Criteria param, Integer pageSize, Integer pageIndex, RefUtil total,
			String orderBy, String orderSort) {
		return this.repository.GetPage(param, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}

	public ActionResult<BankReceipt> Save(BankReceipt bankReceipt) {
		ActionResult<BankReceipt> actionResult = new ActionResult<>();
		repository.SaveOrUpdate(bankReceipt);
		actionResult.setData(bankReceipt);
		actionResult.setSuccess(true);
		actionResult.setMessage(MessageCtrm.SaveSuccess);
		return actionResult;

	}

	@Override
	public ActionResult<String> Delete(String id) {

		ActionResult<String> result = new ActionResult<>();
		this.repository.PhysicsDelete(id, BankReceipt.class);
		result.setSuccess(true);
		result.setMessage(MessageCtrm.DeleteSuccess);
		return result;
	}
}
