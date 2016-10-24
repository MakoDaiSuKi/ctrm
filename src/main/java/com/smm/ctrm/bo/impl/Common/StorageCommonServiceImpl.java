package com.smm.ctrm.bo.impl.Common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.StorageCommonService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.StorageSimple;

@Service
public class StorageCommonServiceImpl implements StorageCommonService {

	@Resource
	HibernateRepository<StorageSimple> storageSimpleRepo;
	
	@Resource
	private CommonService commonService;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private ConcurrentHashMap<Integer, List<Storage>> concurrentStorageMap;
	private static AtomicInteger threadCount = new AtomicInteger(0);
	private static int maxNumNoSplit = 25;

	@Override
	public List<Storage> makeStorageListSimple(List<Storage> list) throws InterruptedException {
		List<Storage> storageList = new ArrayList<>();
		concurrentStorageMap = new ConcurrentHashMap<>();
		if (list.size() > maxNumNoSplit) {
			List<List<Storage>> splitList = splitListForThead(list);
			threadCount.set(0);
			for (int i = 0; i < splitList.size(); i++) {
				ExecutorService executor = Executors.newSingleThreadExecutor();
				executor.execute(new SimpleStorageRunnable(i, splitList.get(i)));
			}
			while (true) {
				if (threadCount.get() == splitList.size()) {
					for (int i = 0; i < threadCount.get(); i++) {
						storageList.addAll(concurrentStorageMap.get(i));
					}
					break;
				}
				TimeUnit.SECONDS.sleep(1);
			}
		} else {
			storageList = commonService.SimplifyDataStorageList(list);
		}
		return storageList;
	}

	private List<List<Storage>> splitListForThead(List<Storage> list) {
		List<List<Storage>> splitList = new ArrayList<>();
		int processorsNum = Runtime.getRuntime().availableProcessors();
		int singleSize = list.size() / processorsNum;
		for (int i = 0; i < processorsNum; i++) {
			int toIndex;
			if (i == processorsNum - 1) {
				toIndex = list.size();
			} else {
				toIndex = (i + 1) * singleSize;
			}
			splitList.add(list.subList(i * singleSize, toIndex));
		}
		return splitList;
	}

	private class SimpleStorageRunnable implements Runnable {
		private List<Storage> storages;
		private int order;

		public SimpleStorageRunnable(int order, List<Storage> storages) {
			this.storages = storages;
			this.order = order;
		}

		@Override
		public void run() {
			concurrentStorageMap.put(order, commonService.SimplifyDataStorageList(storages));
			threadCount.incrementAndGet();
		}
	}
	@Override
	public void filterCxStatus(List<Storage> storages, String cxStatus) {
		if (cxStatus.equals("未冲销")) { // 未冲销

			List<Storage> tempStorages = storages.stream()
					.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(x.getQuantity()) == 0)
					.collect(Collectors.toList());
			storages.removeIf(x -> x.getIsNoticed());
			storages.addAll(tempStorages);
		} else if (cxStatus.equals("已冲销未结束")) { // 已冲销未结束
			List<Storage> tempStorages1 = storages.stream()
					.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(BigDecimal.ZERO) != 0
							&& x.getUnCxQuantity().compareTo(x.getQuantity()) != 0)
					.collect(Collectors.toList());
			storages.removeIf(x -> x.getIsNoticed());
			storages.addAll(tempStorages1);

		} else if (cxStatus.equals("已冲销结束")) { // 已冲销结束
			List<Storage> tempStorages2 = storages.stream()
					.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(BigDecimal.ZERO) == 0)
					.collect(Collectors.toList());
			storages.removeIf(x -> x.getIsNoticed());
			storages.addAll(tempStorages2);
		}
	}
}
