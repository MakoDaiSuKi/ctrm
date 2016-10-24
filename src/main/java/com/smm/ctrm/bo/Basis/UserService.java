package com.smm.ctrm.bo.Basis;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Resource;
import com.smm.ctrm.domain.Basis.Role;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface UserService {
	
	ActionResult<List<Role>> GetRolesByUserId(String userId);

	ActionResult<List<User>> GetUsersByRoleId(String roleId);
	
	Criteria GetCriteria();

	ActionResult<List<Resource>> GetMenusByUserId(String userId);

	ActionResult<List<User>> Users();

	ActionResult<List<User>> Traders();

	ActionResult<List<User>> ApproversOfContract();

	ActionResult<List<User>> ApproversOfPayment();

	ActionResult<List<User>> ApproversOfCustomer();

	ActionResult<List<User>> AccountsToBeAudit4App();

	ActionResult<User> GetById(String userId);

	ActionResult<User> Save(User user);

	ActionResult<String> Delete(String userId);

	ActionResult<String> ChangePassword(String newPwd, String oldPwd, String userId);

	ActionResult<String> ResetPassword(String newpwd, String userId, String account);

	ActionResult<String> AuditAccount4App(String accountId, String isAudited);

	ActionResult<List<User>> ApproversOfShip();

	ActionResult<List<User>> ApproversOfInvoice();

	List<User> Pager(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy, String orderBy);

}