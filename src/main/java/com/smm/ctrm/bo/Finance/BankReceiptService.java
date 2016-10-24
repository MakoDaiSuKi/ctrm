package com.smm.ctrm.bo.Finance;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.BankReceipt;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface BankReceiptService {
	Criteria GetCriteria();

	/**
	 * 根据Id取得单个实体
	 * 
	 * @param id
	 * @return
	 */
	public ActionResult<BankReceipt> GetById(String id);

	/**
	 * 查客户名称下的所有水单列表
	 * 
	 * @param customerId
	 * @return
	 */
	public ActionResult<List<BankReceipt>> GetReceiptListByCustomerId(String customerId);

	List<BankReceipt> BankReceipt(Criteria param, Integer pageSize, Integer pageIndex, RefUtil total, String orderBy,
			String orderSort);
	public ActionResult<BankReceipt> Save(BankReceipt bankReceipt);
	
	public ActionResult<String> Delete(String id);
}
