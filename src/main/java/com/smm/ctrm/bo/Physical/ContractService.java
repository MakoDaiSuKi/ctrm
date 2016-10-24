package com.smm.ctrm.bo.Physical;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.apiClient.ContractParams;
import com.smm.ctrm.domain.apiClient.CpContract;
import com.smm.ctrm.domain.apiClient.ImportContractParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface ContractService {
	Criteria GetCriteria();

	ActionResult<String> GetMaxSerialNo(String legalId, String spotDirection, Date tradeDate, String commodityId);

	ActionResult<String> Delete(String contractId, String userId);

	ActionResult<Contract> GetById(String contractId);

	ActionResult<Contract> ContractViewById(String id);

	ActionResult<List<Contract>> ContractsByCustomerId(String customerId);

	ActionResult<Contract> SaveHeadOfContractRegular(Contract contract);

	ActionResult<Contract> SaveHead4BviToSm(Contract contract);

	ActionResult<Contract> SaveHead4SmToBvi(Contract contract);

	List<Contract> Contracts(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total);

	List<Lot> LotsByContractId(List<String> collect);

	ActionResult<String> txSaveContractAndRivalOrder(Contract contract);

	ActionResult<String> importContract(ImportContractParams contract);

	ActionResult<Boolean> verify(String contractId);

	ActionResult<CpContract> fastContract(CpContract contract);

	ActionResult<String> getSanKey(ContractParams param);

}