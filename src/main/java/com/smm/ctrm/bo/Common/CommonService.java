package com.smm.ctrm.bo.Common;

import com.smm.ctrm.domain.Basis.*;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Physical.*;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;

public interface CommonService {

	String SyncUrl = "http://trade.api.hedgestudio.com:9730";

	String SyncLoginApi = "api/Basis/Login/Login4Winform";

	LoginInfo GetSyncLoginInfo();

	GlobalSet DefaultGlobalSet();

	String GetServerUploadPath();

	String GetServerOutDocmentPath();

	String GetServerOutDocTemplateFilePath();

	ActionResult<String> SaveAttachment(Attachment attachment);

	ActionResult<List<Attachment>> GetAttachments(String attachType, Boolean isOutDoc, String id);

	ActionResult<Attachment> GetAttachmentById(String id);
	
	ActionResult<vAttachment> GetAttachmentById_New(String id);

	ActionResult<String> DeleteAttachmentById(String id);

	ActionResult<String> DeleteAttachmentByBillId(String attachType, Boolean isOutDoc, String id);

	Position GetPositionSplitted(Position original, BigDecimal quantitySplitted);

	ActionResult<String> SquareWithTransaction(List<Position> longs, List<Position> shorts);

	ActionResult<String> Square(List<Position> longsParam, List<Position> shortsParam);

	ActionResult<String> Square4Broker(List<Position4Broker> longsParam, List<Position4Broker> shortsParam);

	GlobalSet GetGlobalSet();

	String FormatSortBy(String sortBy);

	void SetStorage(Lot lot, Storage storage);

	/**
	 * 更新批次及拆分批次的点价/保值数量及状态
	 * 
	 * @param LotId
	 * @return
	 */
	boolean UpdatePriceAndHedgeFlag4Lot(String LotId);

	/**
	 * 更新批次及拆分批次的点价/保值数量及状态
	 * 
	 * @param lot
	 * @return
	 */
	boolean UpdatePriceAndHedgeFlag4Lot2(Lot lot);

	Customer SimplifyData(Customer customer);

	CustomerTitle SimplifyData(CustomerTitle customerTitle);

	CustomerBalance SimplifyData(CustomerBalance customerBalance);

	Contract SimplifyData(Contract contract);

	Lot SimplifyData(Lot lot);

	Fund SimplifyData(Fund fund);

	Fee SimplifyData(Fee fee);

	Invoice SimplifyData(Invoice invoice);

	Storage SimplifyData(Storage storage);

	Position SimplifyData(Position position);

	Square SimplifyData(Square square);

	Pricing SimplifyData(Pricing pricing);

	PricingRecord SimplifyData(PricingRecord record);

	Pending SimplifyData(Pending pending);

	Tip SimplifyData(Tip note);

	Grade SimplifyData(Grade grade);

	DischargingPriceDiff SimplifyData(DischargingPriceDiff pricediff);

	List<DischargingPriceDiff> SimplifyData(List<DischargingPriceDiff> pricediff);

	InvoiceGrade SimplifyData(InvoiceGrade invoiceGrade);

	Instrument SimplifyData(Instrument instrument);

	Menu SimplifyData(Menu menu);

	LC SimplifyData(LC menu);

	MarginFlow SimplifyData(MarginFlow marginFlow);

	Position4Broker SimplifyData(Position4Broker position);

	Square4Broker SimplifyData(Square4Broker square);

	boolean CheckHasVerifyData(String userId);

	String GetVerifyMessages(String userId);

	void UpdateLotPriceByLotId(String lotId);

	void UpdateLotPriceByLotId(Lot lot);

	void UpdateLotFees(Lot lot);

	boolean UpdateLotFeesByLotId(String lotid);

	Integer GetSequenceIndex(String code);

	Integer GetSequenceIndex(String code, Boolean UseConfirm);

	void ConfirmSequenceIndex(SysSequence sequence);

	boolean UpdateFuturesSpread(String LotId);

	QuantityMaL getQuantityMoreorLess(String MoreOrLessBasis, BigDecimal _Quantity, BigDecimal _MoreOrLess);

	void UpdateDeliveryStatus(List<Lot> lots);

	void UpdateInvoiceStatus(List<Lot> lots);

	QuantityMaL CalculateQuantityOfLotDeliveryed(Lot lot);

	Invoice CalculateQuantityOfInvoice(Lot lot);

	void GenerateBrokerPosition();

	void Square();

	BigDecimal FormatQuantity(BigDecimal Quantity, Commodity commodity, String commodityId);

	BigDecimal FormatQuantity(BigDecimal Quantity);

	BigDecimal FormatPrice(BigDecimal Price, Commodity commodity, String commodityId);

	BigDecimal FormatPrice(BigDecimal Price);

	ActionResult<String> UpdateLotIsFunded(String lotid);

	boolean CanLotIsFunded(String lotid);

	List<Lot> setDelivery4Lot(Lot lot);

	List<Customer> SimplifyDataCustomerList(List<Customer> customer);

	List<Invoice> SimplifyDataInvoiceList(List<Invoice> invoices);

	List<InvoiceGrade> SimplifyDataInvoiceGradeList(List<InvoiceGrade> invoices);

	List<Fee> SimplifyDataFeeList(List<Fee> fees);

	List<Storage> SimplifyDataStorageList(List<Storage> storages);

	List<Position> SimplifyDataPositionList(List<Position> positions);

	List<Square> SimplifyDataSquareList(List<Square> squares);

	List<Pricing> SimplifyDataPricingList(List<Pricing> pricings);

	List<PricingRecord> SimplifyDataPricingRecordList(List<PricingRecord> pricingRecords);

	List<CustomerTitle> SimplifyDataCustomerTitleList(List<CustomerTitle> list);

	List<Fund> SimplifyDataFundList(List<Fund> list);

	List<Grade> SimplifyDataGradeList(List<Grade> list);

	List<Lot> SimplifyDataLotList(List<Lot> lotList);

	List<Pending> SimplifyDataPendingList(List<Pending> list);

	List<Tip> SimplifyDataTipList(List<Tip> list);

	List<Invoice> InvoicesOfSplittedLotByLotId(String lotId);

	List<CustomerBalance> SimplifyDataCustomerBalanceList(List<CustomerBalance> customerBalances);

	List<Square> SimplifySquareData(List<Square> squares);

	List<Square4Broker> SimplifyDataSquare4BrokerList(List<Square4Broker> squares);

	List<MarginFlow> SimplifyDataMarginFlowList(List<MarginFlow> exchanges);

	List<LC> SimplifyDataLCList(List<LC> lcs);

	List<Instrument> SimplifyDataInstrumentList(List<Instrument> lots);

	List<Position4Broker> SimplifyDataPosition4BrokerList(List<Position4Broker> positions);

	BigDecimal GetM2MPrice(String majorMarketId, String code, String commodityId, String specId);

	List<Lot> setInvoice4Lot(Lot lot);

	Boolean IsPriced4Lot(Lot lot);

	boolean IsPriced4Lot(Lot lot, RefUtil ref);

	Boolean IsHedged4Lot(Lot lot);

	List<Storage> FuturesSpread(Lot obj, List<Storage> storages, List<Position> positions);

	List<CarryPosition> GetCarryPositions(List<Position> carryPositions);

	BigDecimal GetM2MPrice(String marketCode, String commodityId, String specId);

	List<String> GetUserPermissionByUser(String userid);

	void quotationPrice(String commodityName, Map<String, BigDecimal> priceMap);

	Criteria AddPermission(String userId, Criteria criteria, String fieldName);

	List<String> GetSquare4BrokenIdByUsersId(List<String> createdId);

	List<String> GetSquareIdByUsersId(List<String> createdId);

	Lot UpdateLotPriceByLotId_New(String id);

	Date FormatDateAsYymmdd000000(Date majorStartDate);

	List<Lot> setDelivery4Lot_New(Lot lot);

	List<Lot> setInvoice4Lot_NewContract(Lot lot);

	Boolean IsPriced4Lot_New(Lot lot);

	Boolean IsHedged4Lot_New(Lot lot);

	void UpdateDeliveryStatus_New(List<Lot> lots);

	void UpdateInvoiceStatus_NewContract(List<Lot> lots1);

	void SyncCustomerBalance(String customerId, String legalId, String commodityId);

	Fund SimplifyDataFund(Fund fund);

	boolean IsUpdateByLot(String lotId);

	ActionResult<String> ResetStorageByContract(Contract contract);

	Boolean CanLotIsFunded_New(String lotId);

	List<Lot> setInvoice4Lot_NewInvoice(Lot lot, Invoice invoice);

	boolean UpdateLotFeesByLotId_New(String id);

	QuantityMaL CalculateQuantityOfLotDeliveryed_New(Lot lot);

	void UpdateInvoiceStatus_NewInvoice(List<Lot> lots);

	String MoneyLowerToLarge(BigDecimal num);
	
	List<CInvoice> SimplifyDataInvoicePager(List<CInvoice> invoices);

	List<C2Storage> SimplifyDataStorageHolding(List<C2Storage> storages,boolean mark);

	String CreateCode_Simple(String nr);

	List<CustomerBalance> txSyncCustomerBalance(String customerId, String legalId, String commodityId);

	ActionResult<Lot> txUpdateLotIsFunded(String lotid);
	
	BigDecimal GetM2MPrice2(String marketCode, String commodityId, String specId,List<Market> markets,Map<String,List<?>> map);
	
	Map<String,List<?>> GetLotM2mPrice(Map<String,List<String>> lot2Commditys);
}