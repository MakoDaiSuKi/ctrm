package com.smm.ctrm.bo.impl.Common;

import javax.annotation.Resource;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.StorageCirculationService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Physical.Storage;

@Service
public class StorageCirculationServiceImpl implements StorageCirculationService{
	
	@Resource
	HibernateRepository<Storage> storageRepo;

	@Override
	public Storage productToPointOfSale(Storage modifyStorage) {
		Storage newStorage = new Storage();
		BeanUtils.copyProperties(modifyStorage, newStorage);
		newStorage.setIsIn(Boolean.FALSE);
		newStorage.setIsOut(Boolean.TRUE);
		newStorage.setCounterpartyId3(modifyStorage.getId());
		newStorage.setBviSourceId(modifyStorage.getId());
		newStorage.setId(null);
		String newStorageId = storageRepo.SaveOrUpdateRetrunId(newStorage);
		
		modifyStorage.setIsOut(Boolean.TRUE);
		modifyStorage.setCounterpartyId2(newStorageId);
		storageRepo.SaveOrUpdate(modifyStorage);
		
		return newStorage;
	}

	@Override
	public Storage receiptConfirmedByPointOfSale(Storage modifyStorage) {
		Storage newStorage = new Storage();
		BeanUtils.copyProperties(modifyStorage, newStorage);
		newStorage.setIsIn(Boolean.TRUE);
		newStorage.setIsOut(Boolean.FALSE);
		newStorage.setCounterpartyId3(modifyStorage.getId());
		newStorage.setId(null);
		String newStorageId = storageRepo.SaveOrUpdateRetrunId(newStorage);
		
		modifyStorage.setIsIn(Boolean.TRUE);
		modifyStorage.setCounterpartyId2(newStorageId);
		storageRepo.SaveOrUpdate(modifyStorage);
		
		return newStorage;
	}

	@Override
	public Storage saleToOutBySubsidiary(Storage modifyStorage, String subsidiaryLegalId) {
		
		Storage newStorage = new Storage();
		BeanUtils.copyProperties(modifyStorage, newStorage);
		newStorage.setId(null);
		newStorage.setLegalId(subsidiaryLegalId);
		newStorage.setIsIn(Boolean.FALSE);
		newStorage.setIsOut(Boolean.TRUE);
		
		
		return null;
	}

}
