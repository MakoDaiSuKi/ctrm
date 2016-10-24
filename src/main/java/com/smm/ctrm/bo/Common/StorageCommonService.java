package com.smm.ctrm.bo.Common;

import java.util.List;

import com.smm.ctrm.domain.Physical.Storage;

public interface StorageCommonService {

	/** 使用线程处理，加快速度
	 * @param list
	 * @return
	 * @throws InterruptedException
	 */
	List<Storage> makeStorageListSimple(List<Storage> list) throws InterruptedException;

	/** 冲销状态过滤
	 * @param storages
	 * @param cxStatus
	 */
	void filterCxStatus(List<Storage> storages, String cxStatus);

}
