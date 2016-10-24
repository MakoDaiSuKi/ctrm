package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.C2Storage;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpLotStorages;
import com.smm.ctrm.domain.Physical.CpMergeStorages;
import com.smm.ctrm.domain.Physical.CpSplitStorage;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.MoveOrderParam;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.StorageFee;
import com.smm.ctrm.domain.Physical.StorageFeeDetail;
import com.smm.ctrm.domain.apiClient.StorageFeeParams;
import com.smm.ctrm.domain.apiClient.StorageParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;


public interface StorageService {
	
	Criteria GetCriteria();

	ActionResult<List<Storage>> BviStorageHolding(String bviLegalId);

	ActionResult<List<Storage>> SmStorageHolding(String smLegalId);

	ActionResult<List<Storage>> StorageHolding();

	ActionResult<String> Save(Storage storage);
	
	ActionResult<String> SaveStorageFee(StorageFee storageFee);

	void SaveNoticeStorage(Storage storage);

	ActionResult<String> SaveFactories(List<Storage> storages);

	ActionResult<Storage> GetById(String storageId);

	ActionResult<List<Storage>> Storages();

	ActionResult<String> SplitStorage(CpSplitStorage cpSplitStorage, String userId);

	ActionResult<String> ReviseQuantity(Storage storage);

	ActionResult<Storage> CreateVirtual(Storage storage);

	ActionResult<String> FillBack(List<Storage> storages, Storage targetVirtualStorage);

	void ImportInitStorages();

	ActionResult<List<Storage>> StoragesByContractId(String contractId);

	ActionResult<List<Storage>> StoragesByLotId(String lotId);

	ActionResult<List<Storage>> StoragesByProjectName(String projectName);

	ActionResult<List<Storage>> SourceStoragesById(String storageId);

	ActionResult<List<Storage>> BviSourceStoragesById(String storageId);

	ActionResult<List<Storage>> MergeSourceStoragesById(String storageId);

	ActionResult<List<Storage>> StoragesByCustomerId(String customerId);

	ActionResult<List<Storage>> StoragesBySummaryFeesId(String summaryFeeId);

	ActionResult<List<Storage>> ImportFactory(List<Storage> storage, String userName, String userId);

	ActionResult<String> ImportBvis(List<Storage> storages, String userName, String userId);

	ActionResult<String> MergeBvis(CpMergeStorages cpMergeStorages, String userId);

	ActionResult<String> MergeCancel(String userId, String storageId);

	ActionResult<String> TakeStoragesFromFactory(CpLotStorages cpLotStorages);

	ActionResult<String> DeleteFactoryIns(CpLotStorages cpLotStorages);

	ActionResult<String> RemoveFactoryIns(CpLotStorages cpLotStorages);

	ActionResult<String> DeleteMultiStorages(List<Storage> storageList);

	ActionResult<String> RetrnStorages(List<Storage> storageList);

	ActionResult<String> DeleteStorageIns(CpLotStorages cpLotStorages);

	ActionResult<String> CreateStorageOuts(CpLotStorages cpLotStorages, String userId);

	ActionResult<String> DeleteStorageOuts(CpLotStorages cpLotStorages);

	ActionResult<String> CopyStorageInsFromBviOuts(CpLotStorages cpLotStorages);

	ActionResult<String> CopyStorageInsFromOuts(CpLotStorages cpLotStorages);

	ActionResult<String> CreateStorageInAndOut(Storage storage);

	ActionResult<String> DeleteStorageById(String storageId);
	
	ActionResult<String> DeleteStorageFeeById(String storageFeeId);
	
	ActionResult<List<StorageFeeDetail>> GetStorageFeeDetailById(String storageFeeId);

	ActionResult<String> DeleteStorage(Storage storage);

	ActionResult<String> AmendNonKeyInfo(List<Storage> storages);
	
	ActionResult<String> ChangeWarehouse(List<MoveOrderParam> moveOrderParams);

	List<Storage> GetByNoticeStorageId(String id);

	List<Storage> Storages(Criteria param, int pageSize, int pageIndex, String string, String orderBy, RefUtil total);

	List<Lot> LotsById(String lotId);

	ActionResult<List<Storage>> Pager(StorageParams param) throws InterruptedException;
	
	ActionResult<List<StorageFee>> PagerStorageFee(StorageFeeParams param) throws InterruptedException;

	ActionResult<List<Storage>> GoodsDeliver(String lotId, List<Storage> storages);

	ActionResult<List<Storage>> GoodsReceipt(String lotId, List<Storage> storages);

	Criteria GetCriteria2();

	ActionResult<List<Storage>> SpotSaleList(String contractId);

	ActionResult<String> saveReviseQuantity(Storage storage);

	ActionResult<List<C2Storage>> ExchangeStorages(StorageParams param);

	ActionResult<List<C2Storage>> StoragesHolding(StorageParams param);

	void DailyStoragesHoldingToJson();

	ActionResult<List<C2Storage>> getDailyStoragesHoldingFromJson(StorageParams param);

	Long countStorageByProjectName(String ProjectName, String currStorageId);

	void clearSession();

	ActionResult<List<Storage>> SpotSaleListToCustomer(StorageParams params);

	ActionResult<String> ReceiptWithContractAndLot(List<Storage> storages);

	ActionResult<String> ShipWithContractAndLot(Contract contract);

}