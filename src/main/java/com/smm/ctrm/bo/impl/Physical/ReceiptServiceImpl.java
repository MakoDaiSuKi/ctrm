package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.LotCommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.ReceiptParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ReceiptShipInit;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.PhysicalContants;
import com.smm.ctrm.util.PhysicalMsg;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

@Service
public class ReceiptServiceImpl implements ReceiptService {

	@Autowired
	LotCommonService lotCommonService;

	@Autowired
	HibernateRepository<ReceiptShip> receiptRepo;

	@Autowired
	HibernateRepository<Commodity> commodityRepo;

	@Autowired
	HibernateRepository<Spec> specRepo;

	@Autowired
	HibernateRepository<Brand> brandRepo;

	@Autowired
	HibernateRepository<Storage> storageRepo;

	@Autowired
	HibernateRepository<Lot> lotRepo;

	@Autowired
	HibernateRepository<Contract> contractRepo;
	
	@Autowired
	ReceiptService receiptService;
	
	@Autowired
	HibernateRepository<User> userRepository;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	FundService fundService;

	private static final AtomicLong nextSerialNum = new AtomicLong();

	@Override
	public ActionResult<ReceiptShipInit> Init(String lotId, String Flag) {
		ReceiptShipInit init = new ReceiptShipInit();
		String no = getNoRepeatReceiptNo(Flag);
		if (StringUtils.isNotBlank(lotId)) {
			BigDecimal quantityDelivered = lotCommonService.getLotDeliveredQuantity(lotId, null, Boolean.TRUE, Boolean.TRUE);
			init.setWeight(quantityDelivered);
		}
		init.setNo(no);

		return new ActionResult<>(Boolean.TRUE, "", init);
	}

	/**
	 * 生成收货单号
	 * 
	 * @param prefix
	 *            SH收货
	 * @return
	 */
	public String getNoRepeatReceiptNo(String Flag) {
		String prefix = "";
		if (ReceiptShip.RECEIPT.equals(Flag)) {
			prefix = PhysicalContants.PREFIX_RECEIPT;
		} else if (ReceiptShip.SHIP.equals(Flag)) {
			prefix = PhysicalContants.PREFIX_SHIP;
		} else if (ReceiptShip.OUT.equals(Flag)) {
			prefix = PhysicalContants.PREFIX_OUT_WAREHOUSE;
		}
		nextSerialNum.set(getMaxReceiptNo(prefix));
		while (true) {
			String no = prefix + DateUtil.doFormatDate(new Date(), "yyyyMM")
					+ String.format("%04d", nextSerialNum.incrementAndGet());
			if (!existReceiptNo(no)) {
				return no;
			}
		}
	}

	/**
	 * 从数据库读取当前收发、货单号最大值
	 * 
	 * @param no
	 * @return
	 */
	private int getMaxReceiptNo(String prefix) {
		String no = (String) receiptRepo.CreateCriteria(ReceiptShip.class)
				.setProjection(Projections.max("ReceiptShipNo")).add(Restrictions.like("ReceiptShipNo", prefix + "%"))
				.uniqueResult();
		return StringUtils.isBlank(no) ? 0 : Integer.parseInt(no.substring(no.length() - 4, no.length()));
	}

	/**
	 * 判定收货单号是否已存在
	 * 
	 * @param no
	 * @return
	 */
	private boolean existReceiptNo(String no) {
		return (Long) receiptRepo.CreateCriteria(ReceiptShip.class).setProjection(Projections.rowCount())
				.add(Restrictions.eq("ReceiptShipNo", no)).uniqueResult() > 0;
	}

	@Override
	public ActionResult<ReceiptShip> Save(ReceiptShip receiptShip) {

		if(receiptShip.getStorages() == null || receiptShip.getStorages().size() == 0) {
			return new ActionResult<>(Boolean.FALSE, "商品明细不能为空", receiptShip);
		}
		if (StringUtils.isBlank(receiptShip.getId()) && existReceiptNo(receiptShip.getReceiptShipNo())) {
			String no = getNoRepeatReceiptNo(receiptShip.getFlag());
			receiptShip.setReceiptShipNo(no);
			return new ActionResult<>(Boolean.FALSE, "当前的收发货单号已存在！", receiptShip);
		}
		
		if (receiptShip.getId() == null) {
			receiptShip.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
		} else {
			receiptShip.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
		}
		
		if (receiptShip.getIsIniatiated()) {
			receiptShip.setIsApproved(true);
			receiptShip.setStatus(Status.Agreed);
		}
		
		// 发货单
		if (ReceiptShip.SHIP.equals(receiptShip.getFlag())) {
			
			ActionResult<Boolean> existShip= existPendingShip(receiptShip.getLotId(), receiptShip.getId());
			if(existShip.getData()) 
				return new ActionResult<>(Boolean.FALSE, "存在未审核的发货单");
			
			ReceiptShip rs= null;
			//如果包含初始化业务
			if (receiptShip.getIsIniatiated()) {
				Lot lot = lotRepo.getOneById(receiptShip.getLotId(), Lot.class);
				boolean containCurrReceiptShip = Boolean.TRUE;
				if(StringUtils.isNotBlank(receiptShip.getId())) {
					containCurrReceiptShip = Boolean.FALSE;
				}
				int compareResult = lotCommonService.isLotDeliveredFinishedNormally(lot, receiptShip, Boolean.TRUE, containCurrReceiptShip);
				if (compareResult == 0) {
					lot.setIsDelivered(Boolean.TRUE);
					lotRepo.SaveOrUpdate(lot);
				} else if (compareResult > 0) {
					return new ActionResult<>(Boolean.FALSE, "当前发货单数量超重！");
				}
								
				rs = saveShip(receiptShip).getData();
				ActionResult<String> result=this.initVerify(rs);
				if(!result.isSuccess()){
					return new ActionResult<>(Boolean.FALSE, result.getMessage(), receiptShip);
				}
			}
			else{
				rs = saveShip(receiptShip).getData();
			}
			return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess, receiptShip);
		}
		// 收货单
		else if (ReceiptShip.RECEIPT.equals(receiptShip.getFlag())) {
			if(receiptShip.getStorages() != null) {
				for(Storage storage : receiptShip.getStorages()) {
					if(storageService.countStorageByProjectName(storage.getProjectName(), storage.getId()) > 0) {
						return new ActionResult<>(false, MessageCtrm.DuplicateProjectName);
					}
				}
			}
			return saveReceipt(receiptShip);
		} else {
			return new ActionResult<>(Boolean.FALSE, "noknown Flag" + receiptShip.getFlag());
		}

	}

	/**
	 * 保存收货单
	 * 
	 * @param receiptShip
	 * @return
	 */
	private ActionResult<ReceiptShip> saveReceipt(ReceiptShip receipt) {
		Lot lot = lotRepo.getOneById(receipt.getLotId(), Lot.class);
		boolean containCurrReceiptShip = Boolean.TRUE;
		if(StringUtils.isNotBlank(receipt.getId())) {
			containCurrReceiptShip = Boolean.FALSE;
		}
		int compareResult = lotCommonService.isLotDeliveredFinishedNormally(lot, receipt, Boolean.FALSE, containCurrReceiptShip);
		if (compareResult == 0) {
			lot.setIsDelivered(Boolean.TRUE);
			lotRepo.SaveOrUpdate(lot);
		} else if (compareResult > 0) {
			return new ActionResult<>(Boolean.FALSE, "当前收货单数量超重！");
		}

		receiptRepo.SaveOrUpdateRetrunId(receipt);

		updateStoragesForReceipt(receipt);

		updateQuantityForLot(receipt);

		return new ActionResult<>(true, MessageCtrm.SaveSuccess, receipt);

	}

	/**
	 * 保存发货单
	 * 
	 * @param receiptShip
	 * @return
	 */
	private ActionResult<ReceiptShip> saveShip(ReceiptShip ship) {

		String id=receiptRepo.SaveOrUpdateRetrunId(ship);

		updateStoragesForShip(ship);
		
		ship.setId(id);

		return new ActionResult<>(Boolean.TRUE, MessageCtrm.SaveSuccess, ship);
	}

	/**
	 * 更新已收/发货数量
	 * 
	 * @param receiptShip
	 */
	private void updateQuantityForLot(ReceiptShip receiptShip) {
		Lot lot = lotRepo.getOneById(receiptShip.getLotId(), Lot.class);
		boolean isNeedView = ReceiptShip.SHIP.equalsIgnoreCase(receiptShip.getFlag());
		lot.setQuantityDelivered(lotCommonService.getLotDeliveredQuantity(lot.getId(), receiptShip.getId(), isNeedView, Boolean.TRUE));
		if (lotCommonService.isLotDeliveredFinishedNormally(lot, null, isNeedView, Boolean.TRUE) == 0) {
			lot.setIsDelivered(Boolean.TRUE);
			fundService.updateFundOfLotByStorage(lot, new Fund());
		} else {
			lot.setIsDelivered(Boolean.FALSE);
		}
		lotRepo.SaveOrUpdate(lot);
	}

	/**
	 * 收货单保存、更新商品明细
	 * 
	 * @param receipt
	 */
	private void updateStoragesForReceipt(ReceiptShip receipt) {
		
		List<Storage> databaseList = getStorageByReceiptShipId(receipt.getId());
		
		removeNotExistStorageFromReceipt(receipt, databaseList);

		saveOrUpdateStorageForReceipt(receipt, databaseList);

	}

	/**
	 * 移出已经不存在于当前产成品中的商品明细，并更改其状态
	 * 
	 * @param receipt
	 */
	private void removeNotExistStorageFromReceipt(ReceiptShip receipt, List<Storage> databaseList) {
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		Map<String, Storage> toDelMap = new HashMap<>();

		for (Storage storage : databaseList) {
			toDelMap.put(storage.getId().toLowerCase(), storage);
		}

		for (Storage storage : receipt.getStorages()) {
			if (storage.getId() != null && toDelMap.containsKey(storage.getId().toLowerCase())) {
				toDelMap.remove(storage.getId().toLowerCase());
			}
		}
		
		for (Storage storage : toDelMap.values()) {
			if (storage.getIsOut()) {
				throw new RuntimeException(PhysicalMsg.RECEIPTSHIP_STORAGE_IS_OUT_ERROR);
			}
			storage.setUpdatedAt(new Date());
			storage.setUpdatedId(userInfo.getUserId());
			storage.setIsIn(Boolean.FALSE);
			storage.setLotId(null);
			storage.setLegalId(null);
			storage.setSpecId(null);
			storage.setCustomerId(null);
			storage.setProduct(null);
			storage.setRefId(null);
			storage.setRefName(null);
			storageRepo.SaveOrUpdate(storage);
		}
		for(int i = databaseList.size() - 1; i >=0;i--) {
			if(toDelMap.containsKey(databaseList.get(i).getId().toLowerCase())) {
				databaseList.remove(i);
			}
		}
	}

	/**
	 * 保存或更新传入的商品明细
	 * 
	 * @param receipt
	 */
	private void saveOrUpdateStorageForReceipt(ReceiptShip receipt, List<Storage> databaseList) {
		Map<String, Storage> toAddMap = new HashMap<>();
		List<Storage> currentList = receipt.getStorages();
		for(Storage storage : currentList) {
			String key = storage.getId();
			if(StringUtils.isBlank(key)) {
				key = UUID.randomUUID().toString();
			}
			toAddMap.put(key, storage);
		}
		for(Storage storage : databaseList) {
			if(toAddMap.containsKey(storage.getId().toLowerCase())) {
				toAddMap.remove(storage.getId().toLowerCase());
			}
		}
		Lot lot = lotRepo.getOneById(receipt.getLotId(), Lot.class);
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		if (toAddMap.values() != null && toAddMap.size() > 0) {
			for (Storage s : toAddMap.values()) {
				if (s.getId() == null) {
					s.setCreatedId(userInfo.getUserId());
				} else {
					s.setUpdatedId(userInfo.getUserId());
				}
				s.setIsIn(Boolean.TRUE);
				s.setIsBorrow(Boolean.FALSE);
				s.setLotId(receipt.getLotId());
				s.setLegalId(lot.getLegalId());
				s.setSpecId(lot.getSpecId());
				s.setCustomerId(getCustomerIdByLotId(receipt.getLotId()));
				s.setProduct(lot.getProduct());
				s.setRefId(receipt.getId());
				s.setRefName(ReceiptShip.class.getSimpleName());
				storageRepo.SaveOrUpdate(s);
			}
		}
	}

	private String getCustomerIdByLotId(String lotId) {
		if (lotId == null)
			return null;
		Lot lot = lotRepo.getOneById(lotId, Lot.class);
		if (lot == null || lot.getContractId() == null)
			return null;
		Contract contract = contractRepo.getOneById(lot.getContractId(), Contract.class);
		if (contract == null)
			return null;
		return contract.getCustomerId();
	}

	@Override
	public List<Storage> getStorageByReceiptShipId(String receiptShipId) {
		return storageRepo.GetList(storageRepo.CreateCriteria(Storage.class)
				.add(Restrictions.eq("RefName", ReceiptShip.class.getSimpleName()))
				.add(Restrictions.eq("RefId", receiptShipId)));
	}

	/**
	 * 保存更新商品明细
	 * 
	 * @param receiptShip
	 */
	private void updateStoragesForShip(ReceiptShip ship) {
		List<Storage> databaseList = getStorageByReceiptShipId(ship.getId());
		
		removeNotExistStorageFromShip(ship, databaseList);

		saveOrUpdateStorageForShip(ship, databaseList);

	}

	/**
	 * 移出已经不存在于发货单中的商品明细
	 * 
	 * @param ship
	 */
	private void removeNotExistStorageFromShip(ReceiptShip ship, List<Storage> databaseList) {
		
		List<Storage> currentList = ship.getStorages();
		
		Map<String, Storage> toDelMap = new HashMap<>();

		for (Storage storage : databaseList) {
			toDelMap.put(storage.getId().toLowerCase(), storage);
		}

		for (Storage storage : currentList) {
			if (storage.getId() != null && toDelMap.containsKey(storage.getId().toLowerCase())) {
				toDelMap.remove(storage.getId().toLowerCase());
			}
		}
		restoreReceiptUsedByShip(new ArrayList<>(toDelMap.keySet()));
		for(String storageId : toDelMap.keySet()) {
			storageRepo.PhysicsDelete(storageId, Storage.class);
		}
		for(int i = databaseList.size() - 1; i >= 0; i--) {
			if(toDelMap.containsKey(databaseList.get(i).getId().toLowerCase())) {
				databaseList.remove(i);
			}
		}
	}

	/**
	 * 发货单保存、更新商品明细
	 * 
	 * @param ship
	 */
	private void saveOrUpdateStorageForShip(ReceiptShip ship, List<Storage> databaseList) {
		
		Map<String, Storage> toAddMap = new HashMap<>();
		for(Storage storage : ship.getStorages()) {
			toAddMap.put(storage.getId().toLowerCase(), storage);
		}
		for(Storage storage : databaseList) {
			if(toAddMap.containsKey(storage.getId().toLowerCase())) {
				toAddMap.remove(storage.getId().toLowerCase());
			}
		}
		
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		Lot lot = lotRepo.getOneById(ship.getLotId(), Lot.class);

		List<String> storageIdList = new ArrayList<>();
		if (toAddMap.values() != null && toAddMap.values().size() > 0) {
			Map<String, String> idMap = new HashMap<>();
			List<Storage> newStorageList = new ArrayList<>();
			for (Storage storage : toAddMap.values()) {
				Storage newStorage = new Storage();
				BeanUtils.copyProperties(storage, newStorage);
				newStorageList.add(newStorage);
			}

			for (Storage s : newStorageList) {
				String oldStorageId = s.getId();
				s.setCreatedId(userInfo.getUserId());
				s.setIsOut(Boolean.TRUE);
				s.setIsIn(Boolean.FALSE);
				s.setCounterpartyId3(s.getId());
				s.setCustomerId(lot.getCustomerId());
				s.setBviSourceId(s.getId());
				s.setLotId(ship.getLotId());
				s.setIsBorrow(!s.getLegalId().equalsIgnoreCase(lot.getLegalId()));
				s.setLegalId(lot.getLegalId());
				s.setSpecId(lot.getSpecId());
				s.setContractId(lot.getContractId());
				s.setMT("M");
				s.setIsInvoiced(Boolean.FALSE);
				s.setProduct(lot.getProduct());
				s.setId(null);
				s.setRefId(ship.getId());
				s.setRefName(ReceiptShip.class.getSimpleName());
				String newStorageId = storageRepo.SaveOrUpdateRetrunId(s);
				storageIdList.add(newStorageId);
				idMap.put(oldStorageId, newStorageId);
			}

			for (Storage s : toAddMap.values()) {
				s.setIsOut(Boolean.TRUE);
				s.setCounterpartyId2(idMap.get(s.getId()));
				s.setBviSourceId(s.getId());
				s.setIsBorrow(!s.getLegalId().equalsIgnoreCase(lot.getLegalId()));
				storageRepo.SaveOrUpdateRetrunId(s);
			}
		}
	}

	@Override
	public ActionResult<String> Delete(String id) {
		ActionResult<String> result = new ActionResult<>();

		List<Storage> storageList = getStorageByReceiptShipId(id);
		ReceiptShip receiptShip = receiptRepo.getOneById(id, ReceiptShip.class);

		if (ReceiptShip.RECEIPT.equalsIgnoreCase(receiptShip.getFlag())) {
			for (Storage storage : storageList) {
				if (storage.getIsOut()) {
					return new ActionResult<>(Boolean.FALSE, PhysicalMsg.RECEIPTSHIP_STORAGE_IS_OUT_ERROR);
				}
				storage.setIsIn(Boolean.FALSE);
				storage.setIsBorrow(Boolean.FALSE);
				storage.setLotId(null);
				storage.setCustomerId(null);
				storage.setRefId(null);
				storage.setRefName(null);
				storageRepo.SaveOrUpdate(storage);
			}
			
			updateQuantityForLot(receiptShip);
			
		} else if (ReceiptShip.SHIP.equalsIgnoreCase(receiptShip.getFlag())) {

			if (receiptShip.getStatus() == Status.Agreed) {
				throw new RuntimeException(PhysicalMsg.RECEIPTSHIP_APPROVED);
			} else if (receiptShip.getStatus() == Status.Pending) {
				throw new RuntimeException(PhysicalMsg.RECEIPTSHIP_PENDING);
			}
			
			List<String> list = new ArrayList<>();
			for (Storage storage : storageList) {
				list.add(storage.getId());
				storageRepo.PhysicsDelete(storage.getId(), Storage.class);
			}
			restoreReceiptUsedByShip(list);
		}

		this.receiptRepo.PhysicsDelete(id, ReceiptShip.class);

		result.setSuccess(true);
		result.setMessage(MessageCtrm.DeleteSuccess);

		return result;
	}
	
	/** 
	 * 复原生成发货单对收货明细做的
	 * @param counterpartyId2List
	 */
	private void restoreReceiptUsedByShip(List<String> counterpartyId2List){
		if (counterpartyId2List.size() > 0) {
			List<Storage> oldStorageList = storageRepo.GetQueryable(Storage.class)
					.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.in("CounterpartyId2", counterpartyId2List)))
					.toList();
			for (Storage storage : oldStorageList) {
				storage.setIsOut(Boolean.FALSE);
				storage.setIsIn(Boolean.TRUE);
				storage.setIsBorrow(Boolean.FALSE);
				storage.setCounterpartyId2(null);
				storage.setBviSource(null);
				storageRepo.SaveOrUpdate(storage);
			}
		}
	}

	@Override
	public ActionResult<ReceiptShip> GetById(String id) {
		ActionResult<ReceiptShip> result = new ActionResult<>();

		result.setSuccess(true);
		ReceiptShip receipt = this.receiptRepo.getOneById(id, ReceiptShip.class);
		addObjData(receipt, Boolean.FALSE);
		result.setData(receipt);
		return result;
	}

	@Override
	public ActionResult<List<ReceiptShip>> Pager(Criteria criteria, ReceiptParams receiptParams, RefUtil total) {
		criteria.add(Restrictions.eq("Flag", receiptParams.getFlag()));
		ActionResult<List<ReceiptShip>> result = this.receiptRepo.GetPage(criteria, receiptParams.getPageSize(),
				receiptParams.getPageIndex(), receiptParams.getSortBy(), receiptParams.getOrderBy(), total);
		result.setTotal(total.getTotal());
		return result;
	}

	/**
	 * 将实体关联的实体写入
	 * 
	 * @param receipt
	 * @param isArray
	 *            如果是List中的实体，则不将部分实体装入，提高接口访问速度
	 */
	private void addObjData(ReceiptShip receipt, boolean isArray) {
		if (receipt == null)
			return;
		if (!isArray) {
			List<Storage> list = getStorageByReceiptShipId(receipt.getId());
			for (Storage s : list) {
				if (s.getCommodity() != null) {
					s.setCommodityName(s.getCommodity().getName());
				}
				if (s.getBrand() != null) {
					s.setBrandName(s.getBrand().getName());
				}
				if (s.getSpec() != null) {
					s.setSpecName(s.getSpec().getName());
				}
				s.setBviSource(null);
			}
			receipt.setStorages(list);
		}
	}

	@Override
	public Criteria CreateCriteria() {
		return this.receiptRepo.CreateCriteria(ReceiptShip.class);
	}

	@Override
	public ActionResult<Boolean> existPendingShip(String lotId, String shipId) {

		List<ReceiptShip> list = receiptRepo.GetQueryable(ReceiptShip.class)
				.where(DetachedCriteria.forClass(ReceiptShip.class).add(Restrictions.eq("LotId", lotId))
						.add(Restrictions.ne("Status", Status.Agreed))
						.add(Restrictions.neOrIsNotNull("Id", shipId)))
						
				.toList();

		return new ActionResult<Boolean>(Boolean.TRUE, "", list.size() > 0);
	}

	@Override
	public ActionResult<String> SaveTest(String id) {
		ReceiptShip obj = receiptRepo.getOneById("9B186FC3-E110-4871-924F-047564D6F88B", ReceiptShip.class);
		obj.setDeliveryMan("我是来捣蛋的");
		receiptRepo.SaveOrUpdate(obj);
		if (1 == 1) {
			throw new RuntimeException("测试事务");
		}
		return new ActionResult<>(Boolean.TRUE, "");
	}
	
	
	public ActionResult<String> initVerify(ReceiptShip receiptShip) {
		ActionResult<String> tempVar5 = new ActionResult<String>();
		tempVar5.setSuccess(false);
		try {
			/* 方法提到上一级
			List<Storage> haveReceievedList = receiptService.getStorageByReceiptShipId(receiptShip.getId());
			receiptShip.setStorages(haveReceievedList);	
			Lot lot = lotRepo.getOneById(receiptShip.getLotId(), Lot.class);
			int compareResult = lotCommonService.isLotDeliveredFinishedNormally(lot, receiptShip, Boolean.TRUE, Boolean.FALSE);
			if (compareResult > 0) {
				return new ActionResult<>(Boolean.FALSE, "发货数量过高！");
			} else if (compareResult == 0) {
				lot.setIsDelivered(Boolean.TRUE);
				lotRepo.SaveOrUpdate(lot);
			}
			 */
			Lot lot = lotRepo.getOneById(receiptShip.getLotId(), Lot.class);
			lot.setQuantityDelivered(lotCommonService.getLotDeliveredQuantity(lot.getId(), receiptShip.getId(), Boolean.TRUE, Boolean.TRUE));
			fundService.updateFundOfLotByStorage(lot, new Fund());
			lotRepo.SaveOrUpdate(lot);
			tempVar5.setSuccess(true);
		} catch (Exception e) {
			e.printStackTrace();
			tempVar5.setMessage("修改批次标记失败.");
		}
		return tempVar5;
	}
}
