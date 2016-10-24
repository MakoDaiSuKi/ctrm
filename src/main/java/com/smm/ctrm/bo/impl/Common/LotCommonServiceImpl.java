package com.smm.ctrm.bo.impl.Common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.LotCommonService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.util.PhysicalContants;
import com.smm.ctrm.util.Result.Status;

@Service
public class LotCommonServiceImpl implements LotCommonService {

	@Autowired
	HibernateRepository<Storage> storageRepo;

	@Autowired
	HibernateRepository<Lot> lotRepo;

	@Autowired
	HibernateRepository<ReceiptShip> receiptShipRepo;

	@Override
	public int isLotDeliveredFinishedNormally(Lot lot, ReceiptShip receiptShip, boolean isNeedReview, boolean containCurrReceiptShip) {
		List<Storage> storages = new ArrayList<>();
		String receiptShipId = null;
		if(receiptShip != null) {
			storages = receiptShip.getStorages();
			receiptShipId = receiptShip.getId();
		}
		if (lot.getMoreOrLessBasis() == null) {
			return 0;
		}
		BigDecimal addQuantity = BigDecimal.ZERO;
		for (Storage storage : storages) {
			addQuantity = addQuantity.add(storage.getQuantity());
		}

		BigDecimal quantityDelivered = getLotDeliveredQuantity(lot.getId(), receiptShipId, isNeedReview, containCurrReceiptShip).add(addQuantity);

		BigDecimal min = BigDecimal.ZERO;
		BigDecimal max = BigDecimal.ZERO;
		if (lot.getMoreOrLessBasis().equalsIgnoreCase(PhysicalContants.LOT_MOREORLESS_ON_PERCENTAGE)) {
			min = lot.getQuantity().multiply(BigDecimal.ONE.subtract(lot.getMoreOrLess().divide(new BigDecimal(100))));
			max = lot.getQuantity().multiply(BigDecimal.ONE.add(lot.getMoreOrLess().divide(new BigDecimal(100))));
		} else if (lot.getMoreOrLessBasis().equalsIgnoreCase(PhysicalContants.LOT_MOREORLESS_ON_QUANTITY)) {
			min = lot.getQuantity().subtract(lot.getMoreOrLess());
			max = lot.getQuantity().add(lot.getMoreOrLess());
		}
		if (quantityDelivered.compareTo(min) < 0) {
			return -1;
		} else if (quantityDelivered.compareTo(max) > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public BigDecimal getLotDeliveredQuantity(String lotId, String receiptShipId, boolean isNeedReview, boolean containCurrReceiptShip) {
		BigDecimal quantityDelivered = BigDecimal.ZERO;
		DetachedCriteria dc = DetachedCriteria.forClass(ReceiptShip.class).add(Restrictions.eq("LotId", lotId));
		if(!containCurrReceiptShip) {
			dc.add(Restrictions.neOrIsNotNull("Id", receiptShipId));
		}
		if (isNeedReview) {
			dc.add(Restrictions.eq("Status", Status.Agreed));
		}
		List<ReceiptShip> receiptShips = receiptShipRepo.GetQueryable(ReceiptShip.class).where(dc).toList();
		for (ReceiptShip receiptShip : receiptShips) {
			
			List<Storage> storageList = getStorageByReceiptShipId(receiptShip.getId());
			for (Storage storage : storageList) {
				quantityDelivered = quantityDelivered.add(storage.getQuantity());
			}
		}
		return quantityDelivered;
	}
	
	
	/**
	 * 根据收发货单Id，获取收发货和商品明细的关联关系列表
	 * 
	 * @param receiptShipId
	 * @return
	 */
	private List<Storage> getStorageByReceiptShipId(String receiptShipId) {
		return storageRepo.GetList(storageRepo.CreateCriteria(Storage.class)
				.add(Restrictions.eq("RefName", ReceiptShip.class.getSimpleName()))
				.add(Restrictions.eq("RefId", receiptShipId)));
	}

}
