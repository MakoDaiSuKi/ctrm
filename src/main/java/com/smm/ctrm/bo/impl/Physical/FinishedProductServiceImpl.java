package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Physical.FinishedProductService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.FeeSetup;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Physical.FinishedProduct;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.FinishedProductParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.PhysicalContants;
import com.smm.ctrm.util.PhysicalMsg;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

@Service
public class FinishedProductServiceImpl implements FinishedProductService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	HibernateRepository<FinishedProduct> finishedProductRepo;

	@Autowired
	HibernateRepository<Storage> storageRepo;

	@Autowired
	HibernateRepository<Legal> legalRepo;

	@Autowired
	HibernateRepository<Commodity> commodityRepo;

	@Autowired
	HibernateRepository<Product> productRepo;

	@Autowired
	HibernateRepository<Brand> brandRepo;

	@Autowired
	HibernateRepository<Spec> specRepo;

	@Autowired
	HibernateRepository<FeeSetup> feeSetupRepo;

	@Autowired
	HibernateRepository<User> userRepo;

	@Autowired
	HibernateRepository<Customer> customerRepo;

	@Autowired
	HibernateRepository<Warehouse> warehouseRepo;
	
	@Autowired
	StorageService storageService;

	@Override
	public Criteria CreateCriteria() {
		return finishedProductRepo.CreateCriteria(FinishedProduct.class);
	}

	@Override
	public ActionResult<String> Init() {
		ActionResult<String> result = new ActionResult<>();
		String prefix = PhysicalContants.FINISHED_PRODUCT_NO_PREFIX;
		String no = getNoRepeatReceiptNo(prefix);
		result.setSuccess(Boolean.TRUE);
		result.setData(no);
		return result;
	}

	private static final AtomicLong nextSerialNum = new AtomicLong();

	/**
	 * 生成收货单号
	 * 
	 * @param prefix
	 *            SH收货
	 * @return
	 */
	private String getNoRepeatReceiptNo(String prefix) {
		nextSerialNum.set(getMaxFinishedProductNo(prefix));
		while (true) {
			String no = prefix + DateUtil.doFormatDate(new Date(), "yyyyMM")
					+ String.format("%04d", nextSerialNum.incrementAndGet());
			if (!existReceiptNo(no)) {
				return no;
			}
		}
	}

	/**
	 * 判定收货单号是否已存在
	 * 
	 * @param no
	 * @return
	 */
	private int getMaxFinishedProductNo(String prefix) {
		String no = (String) finishedProductRepo.CreateCriteria(FinishedProduct.class)
				.setProjection(Projections.max("No")).add(Restrictions.like("No", prefix + "%")).uniqueResult();
		return StringUtils.isBlank(no) ? 0 : Integer.parseInt(no.substring(no.length() - 4, no.length()));
	}

	/**
	 * 判定收货单号是否已存在
	 * 
	 * @param no
	 * @return
	 */
	private boolean existReceiptNo(String no) {
		return (Long) finishedProductRepo.CreateCriteria(FinishedProduct.class).setProjection(Projections.rowCount())
				.add(Restrictions.eq("No", no)).uniqueResult() > 0 ? Boolean.TRUE : Boolean.FALSE;
	}

	@Override
	public ActionResult<List<FinishedProduct>> Pager(Criteria criteria, FinishedProductParams params, RefUtil total) {
		ActionResult<List<FinishedProduct>> result = this.finishedProductRepo.GetPage(criteria, params.getPageSize(),
				params.getPageIndex(), params.getSortBy(), params.getOrderBy(), total);
		for (FinishedProduct obj : result.getData()) {
			addObjData(obj, Boolean.TRUE);
		}
		logger.info(TestTime.result());
		return result;

	}

	/**
	 * 填充引用实体数据
	 * 
	 * @param fp
	 * @param isArray
	 */
	private void addObjData(FinishedProduct fp, Boolean isArray) {
		if (fp.getLegalId() != null) {
			fp.setLegal(legalRepo.getOneById(fp.getLegalId(), Legal.class));
		}
		if (fp.getCommodityId() != null) {
			fp.setCommodity(commodityRepo.getOneById(fp.getCommodityId(), Commodity.class));
		}

		if (fp.getBrandId() != null) {
			fp.setBrand(brandRepo.getOneById(fp.getBrandId(), Brand.class));
		}
		if (fp.getUserId() != null) {
			fp.setUserInfo(userRepo.getOneById(fp.getUserId(), User.class));
		}
		if (fp.getCustomerId() != null) {
			fp.setCustomer(customerRepo.getOneById(fp.getCustomerId(), Customer.class));
		}

		if (isArray) {
			BigDecimal sumQuantity = getSumQuantityByFinishedProductId(fp.getId());
			fp.setQuantityDelivered(sumQuantity);
		} else {
			List<Storage> list = getStoragesByFinishedProductId(fp.getId());
			List<String> storageIdlist = new ArrayList<>();
			for (Storage storage : list) {
				storageIdlist.add(storage.getId());
			}
			List<Storage> storageList = new ArrayList<>();
			if (storageIdlist.size() > 0) {
				storageList = storageRepo.GetQueryable(Storage.class)
						.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.in("id", storageIdlist)))
						.toList();
				for (Storage s : storageList) {
					if (s.getCommodityId() != null) {
						s.setCommodityName(commodityRepo.getOneById(s.getCommodityId(), Commodity.class).getName());
					}
					if (s.getBrandId() != null) {
						s.setBrandName(brandRepo.getOneById(s.getBrandId(), Brand.class).getName());
					}
					if (s.getSpecId() != null) {
						s.setSpecName(specRepo.getOneById(s.getSpecId(), Spec.class).getName());
					}
				}
			}

			fp.setStorages(storageList);
		}
	}

	@Override
	public ActionResult<FinishedProduct> GetById(String id) {
		FinishedProduct fp = finishedProductRepo.getOneById(id, FinishedProduct.class);
		addObjData(fp, Boolean.FALSE);
		return new ActionResult<>(Boolean.TRUE, "", fp);

	}

	@Override
	public ActionResult<String> Delete(String id) {
		List<Storage> finishedProductStorageList = getStoragesByFinishedProductId(id);
		
		boolean b = finishedProductStorageList.stream().anyMatch(x->x.getIsOut());
		if(b){
			return new ActionResult<>(Boolean.FALSE, PhysicalMsg.FINISHEDPRODUCT_STORAGE_IS_OUT_ERROR);
		}
		for (Storage storage : finishedProductStorageList) {
			
			storage.setIsIn(Boolean.FALSE);
			storage.setRefId(null);
			storage.setRefName(null);
			storageRepo.SaveOrUpdate(storage);
		}

		this.finishedProductRepo.PhysicsDelete(id, FinishedProduct.class);

		return new ActionResult<>(Boolean.TRUE, MessageCtrm.DeleteSuccess);

	}

	@Override
	public ActionResult<FinishedProduct> Save(FinishedProduct finishedProduct) throws Exception {

		ActionResult<FinishedProduct> result = new ActionResult<>();
		if (StringUtils.isBlank(finishedProduct.getId()) && existReceiptNo(finishedProduct.getNo())) {
			return new ActionResult<>(Boolean.FALSE, "当前的收货单号已存在！");
		}
		if (finishedProduct.getId() != null) {
			finishedProduct.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
		} else {
			finishedProduct.setStatus(Status.Pending);
			finishedProduct.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
		}
		if (finishedProduct.getStorages() != null) {
			BigDecimal sumQuantity = BigDecimal.ZERO;
			for (Storage storage : finishedProduct.getStorages()) {
				if(storageService.countStorageByProjectName(storage.getProjectName(), storage.getId()) > 0) {
					return new ActionResult<>(false, MessageCtrm.DuplicateProjectName);
				}
				sumQuantity = sumQuantity.add(storage.getQuantity());
			}
			BigDecimal moreQuantity = finishedProduct.getQuantity()
					.multiply(BigDecimal.ONE.add(finishedProduct.getMoreOrLess().divide(new BigDecimal(100))));
			BigDecimal lessQuantity = finishedProduct.getQuantity()
					.multiply(BigDecimal.ONE.subtract(finishedProduct.getMoreOrLess().divide(new BigDecimal(100))));
			if (moreQuantity.compareTo(sumQuantity) > -1 && lessQuantity.compareTo(sumQuantity) < 1) {
				finishedProduct.setIsEnd(Boolean.TRUE);
			} else if (moreQuantity.compareTo(sumQuantity) < 0) {
				throw new Exception(PhysicalMsg.FINISHEDPRODUCT_OVERWEIGHT);
			} else {
				finishedProduct.setIsEnd(Boolean.FALSE);
			}
		} else {
			finishedProduct.setIsEnd(Boolean.FALSE);
		}

		if (finishedProduct.getId() == null) {
			finishedProduct.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
		}
		String finishedProductId = finishedProductRepo.SaveOrUpdateRetrunId(finishedProduct);
		finishedProduct.setId(finishedProductId);

		saveStoragesForFinishedProduct(finishedProduct);

		result.setSuccess(true);
		result.setData(finishedProduct);

		return result;
	}

	/**
	 * 保存更新商品明细
	 * 
	 * @param finishedProduct
	 * @throws Exception
	 */
	private void saveStoragesForFinishedProduct(FinishedProduct finishedProduct) throws Exception {

		removeNotExistStorageFromFinishedProduct(finishedProduct);

		saveOrUpdateStorage(finishedProduct);
	}

	/**
	 * 移出已经不存在于当前产成品中的运输明细，并更改其状态
	 * 
	 * @param finishedProduct
	 * @throws Exception
	 */
	private void removeNotExistStorageFromFinishedProduct(FinishedProduct finishedProduct) throws Exception {

		List<Storage> toDelList = getStoragesByFinishedProductId(finishedProduct.getId());
		Map<String, Storage> toDelMap = new HashMap<>();

		for (Storage storage : toDelList) {
			toDelMap.put(storage.getId().toLowerCase(), storage);
		}

		for (Storage storage : finishedProduct.getStorages()) {
			if (storage.getId() != null && toDelMap.containsKey(storage.getId().toLowerCase())) {
				toDelMap.remove(storage.getId().toLowerCase());
			}
		}

		// 删除已经不存在与该产成品中的运输明细，如果该明细已经标记为出库，这抛出不允许删除的异常
		for (Storage storage : toDelMap.values()) {

			if (storage.getIsOut()) {
				throw new Exception(PhysicalMsg.FINISHEDPRODUCT_STORAGE_IS_OUT_ERROR);
			}
			storage.setIsIn(Boolean.FALSE);
			storage.setRefId(null);
			storage.setRefName(null);
			storageRepo.SaveOrUpdate(storage);
		}
	}

	/**
	 * 根据finishedProductId获取FinishedProduct和Storage的Id关联关系列表
	 * 
	 * @param finishedProductId
	 * @return
	 */
	private List<Storage> getStoragesByFinishedProductId(String finishedProductId) {
		return storageRepo.GetList(storageRepo.CreateCriteria(Storage.class)
				.add(Restrictions.eq("RefName", FinishedProduct.class.getSimpleName()))
				.add(Restrictions.eq("RefId", finishedProductId)));
	}
	
	private BigDecimal getSumQuantityByFinishedProductId(String finishedProductId) {
		return (BigDecimal) storageRepo.getHibernateTemplate().findByCriteria(DetachedCriteria.forClass(Storage.class)
				.add(Restrictions.eq("RefId", finishedProductId))
				.add(Restrictions.eq("RefName", FinishedProduct.class.getSimpleName()))
				.setProjection(Projections.sum("Quantity"))).get(0);
	}

	/**
	 * 保存或更新传入的运输明细
	 * 
	 * @param finishedProduct
	 */
	private void saveOrUpdateStorage(FinishedProduct finishedProduct) {
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		if (finishedProduct.getStorages() != null) {
			for (Storage s : finishedProduct.getStorages()) {
				if (s.getId() == null) {
					s.setCreatedId(userInfo.getUserId());
				}
				s.setIsIn(Boolean.TRUE);
				s.setIsOut(Boolean.FALSE);
				s.setIsBorrow(Boolean.FALSE);
				s.setProduct(finishedProduct.getProductName());
				s.setRefId(finishedProduct.getId());
				s.setRefName(FinishedProduct.class.getSimpleName());
				storageRepo.SaveOrUpdate(s);
			}
		}
	}
}
