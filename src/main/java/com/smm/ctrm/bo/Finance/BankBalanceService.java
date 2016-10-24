package com.smm.ctrm.bo.Finance;

import java.util.Date;
import java.util.List;

import com.smm.ctrm.domain.Physical.BankBalance;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

import org.hibernate.Criteria;

public interface BankBalanceService {
	Criteria GetCriteria();
	ActionResult<BankBalance> Save(List<BankBalance> bankBalances);
	ActionResult<String> Delete(String id);
	ActionResult<BankBalance> GetById(String id);
	ActionResult<List<BankBalance>> GetBankBalancesByTradeDate(Date dt);
	List<BankBalance> BankBalances();

    List<BankBalance> BankBalances(Criteria param, Integer pageSize, Integer pageIndex, RefUtil total, String orderBy, String orderSort);



}