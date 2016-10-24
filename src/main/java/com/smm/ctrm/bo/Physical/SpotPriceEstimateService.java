package com.smm.ctrm.bo.Physical;

public interface SpotPriceEstimateService {

	/**
	 * 现货暂估价
	 */
	void everyDaySpotPrice();

	void clearSession();
}
