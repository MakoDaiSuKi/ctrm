package com.smm.ctrm.bo.Basis;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Date;
import org.hibernate.Criteria;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.Maintain.*;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.domain.Report.*;
public interface AccountService {
	ActionResult<List<Account>> Accounts();
	ActionResult<List<Account>> BackAccounts();
	ActionResult<Account> Save(Account account);
	ActionResult<String> Delete(String id);
	ActionResult<Account> GetById(String id);
	void MoveUp(String id);
	void MoveDown(String id);

}