package com.smm.ctrm.bo.Common;

import com.smm.ctrm.domain.Physical.Storage;

/**
 * @author zhaoyutao
 * 集团货物红鹭销售、红鹭自有库存销售、红鹭交割库存销售、集团库存自己销售
 * 
 */
public interface StorageCirculationService {

	/**
	 * 产品发货到销售点
	 */
	Storage productToPointOfSale(Storage modifyStorage);
	
	/**
	 * 销售点确认收货
	 */
	Storage receiptConfirmedByPointOfSale(Storage modifyStorage);
	
	/**
	 * 销售到外部（红鹭）
	 */
	Storage saleToOutBySubsidiary(Storage modifyStorage, String subsidiaryLegalId);
	
}
