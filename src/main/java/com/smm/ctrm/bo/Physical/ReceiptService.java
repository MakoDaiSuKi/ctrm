package com.smm.ctrm.bo.Physical;

import java.util.List;

import org.hibernate.Criteria;

import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.ReceiptParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ReceiptShipInit;
import com.smm.ctrm.util.RefUtil;

/**
 * @author zhaoyutao
 * 
 */
public interface ReceiptService {
	
	/**
	 * @param receipt
	 * @return
	 */
	ActionResult<ReceiptShip> Save(ReceiptShip receipt);
	
	/**
	 * @param id
	 * @return
	 */
	ActionResult<String> Delete(String id);
	
	/**
	 * @param id
	 * @return
	 */
	ActionResult<ReceiptShip> GetById(String id);
	/**
	 * @param criteria
	 * @param pageSize
	 * @param pageIndex
	 * @param total
	 * @param sortBy
	 * @param orderBy
	 * @return
	 */
	ActionResult<List<ReceiptShip>> Pager(Criteria criteria, ReceiptParams receiptParams, RefUtil total);
	
	/**
	 * @return
	 */
	Criteria CreateCriteria();
	
	/** 返回收货单号和已收货重量
	 * @param lotId
	 * @param Flag
	 * @return
	 */
	ActionResult<ReceiptShipInit> Init(String lotId, String Flag);

	ActionResult<String> SaveTest(String id);

	/**
	 * 根据收发货单Id，获取和收、发货单关联的商品明细列表
	 * 
	 * @param receiptShipId
	 * @return
	 */
	List<Storage> getStorageByReceiptShipId(String receiptShipId);

	ActionResult<Boolean> existPendingShip(String lotId, String shipId);
	
	/**
	 * 收发货单号
	 * @param Flag
	 * @return
	 */
	String getNoRepeatReceiptNo(String Flag);
}
