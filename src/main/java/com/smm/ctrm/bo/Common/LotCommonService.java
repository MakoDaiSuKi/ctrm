package com.smm.ctrm.bo.Common;

import java.math.BigDecimal;

import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.ReceiptShip;

public interface LotCommonService {

	/** 
	 * @param lot
	 * @param receiptShip
	 * @param isNeedReview 
	 * @return
	 */
	/** 当前批次是否已收/发货完成
	 * @param lot
	 * @param currReceiptShip
	 * @param isNeedReview 是否需要审核
	 * @param containCurrReceiptShip 是否包含当前收发货单
	 * @return -1 少了 0 正好 1 多了
	 */
	int isLotDeliveredFinishedNormally(Lot lot, ReceiptShip currReceiptShip, boolean isNeedReview,
			boolean containCurrReceiptShip);
	
	/** 根据lotId获取已经收/发货数量
	 * @param lotId
	 * @param currReceiptShip
	 * @param isNeedReview
	 * @param containCurrReceiptShip 是否包含当前收发货单
	 * @return
	 */
	BigDecimal getLotDeliveredQuantity(String lotId, String currReceiptShip, boolean isNeedReview,
			boolean containCurrReceiptShip);

	

	
	
}
