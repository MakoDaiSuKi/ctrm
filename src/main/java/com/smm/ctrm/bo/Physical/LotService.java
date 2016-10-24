package com.smm.ctrm.bo.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.smm.ctrm.domain.CpPosition4AllocateToMultiLot;
import com.smm.ctrm.domain.Physical.*;
import org.hibernate.Criteria;

import com.smm.ctrm.domain.Report.Lot4FeesOverview;
import com.smm.ctrm.domain.Report.Lot4MTM;
import com.smm.ctrm.domain.apiClient.MtmParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

public interface LotService {
	ActionResult<String> ConfirmLotQuantityDelivered(Lot lot);

	ActionResult<String> UpdateInvoicFlagOfLots(List<Lot> lots);

	void UpdateLotBalance(Lot lot);

	void CalPnLBySell(Lot lot);

	void CalPnLByBuy(Lot lot);

	ActionResult<String> ModifyExecuted4Initiated(Lot lot);

	ActionResult<String> MarkColor(Lot lot);

	ActionResult<String> ClearColor(Lot lot);

	Criteria GetCriteria();

	ActionResult<Lot> SaveLot(Lot lot);

	ActionResult<Lot> SaveContractProvisional(Lot lot);

	ActionResult<Lot> SaveLotOfContractRegular(Lot lot);

	ActionResult<Lot> SaveLot4BviToSm(Lot sellLot);

	ActionResult<String> DeleteContractProvisional(String lotId, String userId);

	ActionResult<String> DeleteLotOfContractRegular(String id);

	Contract GetContractById(String contractId);

	ActionResult<Lot> GetById(String id);
	
	List<Lot> GetListById(Criteria criteria);

	ActionResult<Lot> LotViewById(String id);

	ActionResult<List<Lot>> GetLotsByLotId(String id);

	ActionResult<List<Lot>> GetLotsByContractId(String contractId);

	ActionResult<List<Lot>> LotsByCustomerId(String customerId);

	ActionResult<Lot> GetFirstLotByContractId(String contractId);

	ActionResult<String> SaveStorageOuts(Lot lot);

	void UpdateLotQuantity(String lotId);

	ActionResult<String> AllocatePricing(Lot lot);

	ActionResult<String> RemovePricing(Lot lot);

	ActionResult<String> AllocatePosition(CpPosition4AllocateToLot cpPosition4AllocateToLot);

	ActionResult<String> RemovePosition(CpPosition4RemoveFromLot cpPosition4RemoveFromLot);

	ActionResult<String> SplitLot(String userId, CpSplitLot cpSplitLot);

	ActionResult<String> DeleteFeesByLotId(String lotId);

	ActionResult<String> GenerateFees(Lot curLot);

	ActionResult<String> SplitLotQuantity(String lotId, BigDecimal spDecimal);

	ActionResult<LotPnL> LotSettleTrial(Param4LotPnL param4LotPnL);

	ActionResult<LotPnL> LotSettleTrial00(Param4LotPnL param4LotPnL);

	ActionResult<LotPnL> LotSettleOfficial(Param4LotPnL param4LotPnL);

	ActionResult<LotPnL> LotPnLById(String lotId);

	ActionResult<String> UpdateLotHedgedPrce(String lotid);

	ActionResult<List<Lot>> LotsById(String lotId);

	ActionResult<QPRecord> SaveQPRecord(QPRecord curQPRecord);

	ActionResult<List<QPRecord>> PagerQPRecord(String lotId);

	ActionResult<String> DeleteQPRcord(QPRecord qPRecord, String userId);

	ActionResult<List<VmContractLot4Combox>> LotsByKeywords(String keyword);

	ActionResult<List<Lot4MTM>> Lot4MTMQuery(Date dtStart, Date dtEnd, Boolean bSquared);

	ActionResult<List<Lot4MTM>> Lot4MTMQueryNew(Date date, Date date2, String spotType, String currency, String string,
			String commodityId, String string2, String status, String legalIds);

	ActionResult<List<Lot4MTM>> Lot4MTMQueryNew2(Date dtStart, Date dtEnd, Date invoiceStartDate, Date invoiceEndDate,
			String keyword, String currency, String spotType, String status, String legalId, String legalIds,
			String commodityId, String createdId, String invoiceStatus, String accountYear, String accountMonth);

	ActionResult<List<Lot4FeesOverview>> Lot4Fees(Date date, Date date2, String spotType, String currency,
			String string, String commodityId, String string2, String status, String legalIds,int pageSize, int pageIndex);

	//ActionResult<String> CalcEstimateFees1(Lot4FeesOverview LotFee);

	ActionResult<String> CalcEstimateFees(Lot4FeesOverview LotFee);

	void CalcRealFee(Lot lot, Lot4FeesOverview LotFee);

	ActionResult<List<Lot4MTM>> Lot4MTMQuery3(Date date, Date date2, String spotType, String currency,
			String spotDirection, String string, String commodityId, String string2, String status, String legalIds);

	ActionResult<String> SaveAccountDateByLot(Lot lot);

	List<Lot> Lots(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy, RefUtil total);

	List<Lot> Lots();
	
	List<Lot> Lots4Hedged(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy, RefUtil total);

	ActionResult<String> SplitLot(CpSplitLot cpSplitLot, String userId);

	List<Lot4MTM3> Lot4MTM3Query(MtmParams mtmParams, RefUtil total);

	List<Lot4Unpriced> UnpricedLotRisk(MtmParams mtmParams, RefUtil total);

    ActionResult<Lot> SaveLotOfContractRegular_New(Lot lot);

    ActionResult<String> GenerateFees_New(Lot curLot);

	ActionResult<LotPnL> NewLoadLotSettle(Param4LotPnL param4LotPnL);

	ActionResult<String> txSaveNewLotSplit(LotSplit lotSplit);

	ActionResult<LotPnL> NewLotSettleTrial(Param4LotPnL param4LotPnL);

	ActionResult<LotPnL> NewLotSettleOfficial(Param4LotPnL param4LotPnL);

	ActionResult<String> AllocatePositionMulti(CpPosition4AllocateToMultiLot allocateToMultiLot);

}