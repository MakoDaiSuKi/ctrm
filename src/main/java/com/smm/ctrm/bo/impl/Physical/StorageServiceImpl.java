package com.smm.ctrm.bo.impl.Physical;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smm.ctrm.api.Inventory.TestTime;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.StorageCommonService;
import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.NumTypes;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Maintain.DSME;
import com.smm.ctrm.domain.Physical.C2Storage;
import com.smm.ctrm.domain.Physical.CLot;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpLotStorages;
import com.smm.ctrm.domain.Physical.CpMergeStorages;
import com.smm.ctrm.domain.Physical.CpSplitStorage;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.MoveOrder;
import com.smm.ctrm.domain.Physical.MoveOrderParam;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.SpotPriceEstimate;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.StorageFee;
import com.smm.ctrm.domain.Physical.StorageFeeDetail;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.domain.apiClient.StorageFeeParams;
import com.smm.ctrm.domain.apiClient.StorageParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ReceiptShipInit;
import com.smm.ctrm.hibernate.DataSource.CTRMOrg;
import com.smm.ctrm.hibernate.DataSource.DataSourceContextHolder;
import com.smm.ctrm.hibernate.objectMapper.HibernateAwareObjectMapper;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.PageModel;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MT4Storage;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/5/3.
 */
@Service
public class StorageServiceImpl implements StorageService {

	private static final Logger logger = Logger.getLogger(StorageServiceImpl.class);

	@Autowired
	private HibernateRepository<Storage> repository;

	@Autowired
	private HibernateRepository<C2Storage> c2repository;
	
	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;
	
	@Autowired
	private HibernateRepository<SpotPriceEstimate> spotPriceEstimateRepo;

	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	private HibernateRepository<Brand> brandRepo;

	@Autowired
	private HibernateRepository<CLot> clotRepository;

	@Autowired
	private HibernateRepository<SummaryFees> summaryFeesRepository;

	@Autowired
	private HibernateRepository<Bvi2Sm> bvi2SmHibernateRepository;

	@Autowired
	private HibernateRepository<Storage> storageRepo;
	
	@Autowired
	private HibernateRepository<StorageFee> storageFeeRepo;
	
	@Autowired
	private HibernateRepository<StorageFeeDetail> storageFeeDetailRepo;

	@Autowired
	private HibernateRepository<User> userHibernateRepository;

	@Autowired
	private HibernateRepository<Invoice> invoiceHibernateRepository;

	@Autowired
	private HibernateRepository<Warehouse> warehouseHibernateRepository;

	@Autowired
	private HibernateRepository<Legal> legalHibernateRepository;

	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleRepo;

	@Autowired
	private HibernateRepository<Commodity> commoditylHRepos;
	
	@Autowired
	private HibernateRepository<MoveOrder> moveOrderHibernateRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private LotService lotService;
	
	@Autowired
	private HibernateRepository<DSME> dsmeRepo;

	@Autowired
	private StorageCommonService storageCommonService;

	@Autowired
	private ReceiptService receiptService;

	@Autowired
	private ContractService contractService;

	private final static String CODE_1001 = "1001";
	private final static String CODE_1002 = "1002";
	private final static String CODE_1003 = "1003";
	private final static String CODE_1004 = "1002";
	private final static String CODE_1005 = "1002";

	@Override
	public Criteria GetCriteria() {

		return this.repository.CreateCriteria(Storage.class);
	}

	@Override
	public ActionResult<List<Storage>> Pager(StorageParams param) throws InterruptedException {
		TestTime.start();
		if (param == null) {
			param = new StorageParams();
		}
		Criteria criteria = storageRepo.CreateCriteria(Storage.class);

		if (param.getBrands() != null) {
			List<String> brandIds = new ArrayList<>();
			for (Brand brand : param.getBrands()) {
				brandIds.add(brand.getId());
			}
			criteria.add(Restrictions.in("BrandId", brandIds));
		}
		if (param.getCommodityIdList() != null) {
			criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
		}
		// 权限
		String userid = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userid, criteria, "CreatedId");
		criteria.setFetchMode("Lot", FetchMode.JOIN);

		// 关键字
		if (StringUtils.isNotBlank(param.getKeyword())) {
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);

			Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
					Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
			Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));
			Criterion c = Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
					Restrictions.like("ProjectName", "%" + param.getKeyword() + "%"));
			Criterion d = Restrictions.or(Restrictions.like("TestNo", "%" + param.getKeyword() + "%"),
					Restrictions.like("NoticeBillNo", "%" + param.getKeyword() + "%"));

			criteria.add(Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d),
					Restrictions.like("StorageNo", "%" + param.getKeyword() + "%")));
		}

		// 强制只列出IsHidden = false的记录。为了配合BVI库存管理的特殊需要。
		// 因为被Merge的记录，其IsHidden被置为false了。
		criteria.add(Restrictions.eq("IsHidden", false));

		if (param.getIsNoticed() != null) {
			criteria.add(Restrictions.eq("IsNoticed", param.getIsNoticed()));
		}
		if (StringUtils.isNotBlank(param.getWarehouseId())) {
			criteria.add(Restrictions.eq("WarehouseId", param.getWarehouseId()));
		}
		if (param.getContractId() != null) {
			criteria.add(Restrictions.eq("ContractId", param.getContractId()));
		}
		if (param.getLotId() != null) {
			criteria.add(Restrictions.eq("LotId", param.getLotId()));
		}

		if (StringUtils.isNotBlank(param.getLegalIds())) {
			String[] split = param.getLegalIds().split(",");
			List<String> legalIdList = new ArrayList<>();
			for (String id : split) {
				if(id.trim()!="")
					legalIdList.add(id.trim());
			}
			criteria.add(Restrictions.in("LegalId", legalIdList));
		}
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}
		if (!StringUtils.isBlank(param.getTransitStatus())) {
			criteria.add(Restrictions.eq("TransitStatus", param.getTransitStatus()));
		}
		if (!StringUtils.isBlank(param.getMT())) {
			criteria.add(Restrictions.eq("MT", param.getMT()));
		}
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		if (param.getIsIn() != null) {
			criteria.add(Restrictions.eq("IsIn", param.getIsIn()));
		}
		if (param.getIsOut() != null) {
			criteria.add(Restrictions.eq("IsOut", param.getIsOut()));
		}
		if (param.getIsInvoiced() != null) {
			criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
		}
		if (param.getLoadDateEnd() != null) {
			criteria.add(Restrictions.le("LoadDate", param.getLoadDateEnd()));
		}
		if (param.getLoadDateStart() != null) {
			criteria.add(Restrictions.ge("LoadDate", param.getLoadDateStart()));
		}
		if(param.getCustomerId() != null){
			criteria.add(Restrictions.eq("CustomerId", param.getCustomerId()));
		}


		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		TestTime.addMilestone("参数构造完成。");
		RefUtil total = new RefUtil();

		List<Storage> storages = storageRepo.GetPage(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total).getData();
		TestTime.addMilestone("数据库查询完成。");

		if (StringUtils.isNotBlank(param.getCxStatus()) && param.getIsNoticed()) {
			storageCommonService.filterCxStatus(storages, param.getCxStatus());
			total.setTotal(storages.size());
		}

		TestTime.addMilestone("数据简化之前。");
		storages = commonService.SimplifyDataStorageList(storages);
		TestTime.addMilestone("数据简化完成。");
		String testResultStr = TestTime.result();
		logger.info(testResultStr);
		return new ActionResult<>(true, "", storages, total);
	}

	@Override
	public ActionResult<List<Storage>> BviStorageHolding(String bviLegalId) {

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LegalId", bviLegalId));
		where.add(Restrictions.eq("IsIn", true));
		where.add(Restrictions.eq("IsOut", false));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}
	
	@Override
	public ActionResult<List<StorageFee>> PagerStorageFee(StorageFeeParams param)
			throws InterruptedException {
		
		TestTime.start();
		if (param == null) {
			param = new StorageFeeParams();
		}
		Criteria criteria = storageFeeRepo.CreateCriteria(StorageFee.class);

		
		if(param.getLegalIds() != null){
			List<String> listLegalIds =new ArrayList<>();
			String[] strLegalIds = param.getLegalIds().split(",");
			for(String legalId:strLegalIds)
			{
				if(legalId.trim()!="")
					listLegalIds.add(legalId.trim());
			}
			if(listLegalIds.size() > 0)
				criteria.add(Restrictions.in("LegalId", listLegalIds));
		}
		
		if(param.getWarehouseIds()!= null){
			List<String> listWhIds =new ArrayList<>();
			String[] strWhIds = param.getWarehouseIds().split(",");
			for(String WhId:strWhIds)
			{
				if(WhId.trim()!="")
					listWhIds.add(WhId.trim());
			}
			
			if(listWhIds.size() > 0)
				criteria.add(Restrictions.in("WarehouseId", listWhIds));
		}

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		TestTime.addMilestone("参数构造完成。");
		RefUtil total = new RefUtil();

		List<StorageFee> storageFees = storageFeeRepo.GetPage(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total).getData();
		TestTime.addMilestone("数据库查询完成。");

		
		TestTime.addMilestone("数据简化之前。");
		//storages = commonService.SimplifyDataStorageList(storages);
		TestTime.addMilestone("数据简化完成。");
		String testResultStr = TestTime.result();
		logger.info(testResultStr);
		return new ActionResult<>(true, "", storageFees, total);
	}


	@Override
	public ActionResult<List<Storage>> SmStorageHolding(String smLegalId) {

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LegalId", smLegalId));
		where.add(Restrictions.eq("IsIn", true));
		where.add(Restrictions.eq("IsOut", false));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> StorageHolding() {

		Bvi2Sm a = bvi2SmHibernateRepository.GetList(Bvi2Sm.class).get(0);

		if (a == null)
			return new ActionResult<>(false, "Param: Bvi2Sm is null");

		String bviLegalId = a.getBviLegalId();
		String smLegalId = a.getSmLegalId();

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LegalId", bviLegalId));
		where.add(Restrictions.eq("IsIn", true));
		where.add(Restrictions.eq("IsOut", false));
		List<Storage> bviStorages = storageRepo.GetQueryable(Storage.class).where(where).toList();

		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LegalId", smLegalId));
		where.add(Restrictions.eq("IsIn", true));
		where.add(Restrictions.eq("IsOut", false));
		List<Storage> smStorages = storageRepo.GetQueryable(Storage.class).where(where).toList();

		List<Storage> obj = new ArrayList<>();
		obj.addAll(bviStorages);
		obj.addAll(smStorages);

		return new ActionResult<>(true, null, obj);
	}

	@Override
	public ActionResult<String> Save(Storage storage) {
		if (countStorageByProjectName(storage.getProjectName(), storage.getId()) > 0) {
			return new ActionResult<>(false, MessageCtrm.DuplicateProjectName);
		}
		this.repository.SaveOrUpdate(storage);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}
	
	@Override
	public ActionResult<String> SaveStorageFee(StorageFee storageFee) {
		if(storageFee.getId() == null)
		{
			DetachedCriteria where = DetachedCriteria.forClass(StorageFee.class);
			where.add(Restrictions.eq("FeeCode", storageFee.getFeeCode()));
			List<StorageFee> oldList = storageFeeRepo.GetQueryable(StorageFee.class).where(where).toList();
			
			if(oldList!=null && oldList.size() > 0)
				return new ActionResult<>(false, MessageCtrm.DuplicateFeeCode);
		}
		storageFeeRepo.SaveOrUpdate(storageFee);
		
		for(StorageFeeDetail storageFeeDetail : storageFee.getStorageFeeDetail())
		{
			storageFeeDetail.setStorageFeeId(storageFee.getId());
			
			storageFeeDetailRepo.SaveOrUpdate(storageFeeDetail);
		}
		
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public void SaveNoticeStorage(Storage storage) {

		storageRepo.SaveOrUpdate(storage);

		if (storage.getNoticedStorages() == null)
			storage.setNoticedStorages(new ArrayList<>());

		// 未选择的记录去除关联关系，去除单号
		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("NoticeStorageId", storage.getId()));
		List<Storage> oldList = storageRepo.GetQueryable(Storage.class).where(where).toList();

		for (Storage storage1 : oldList) {
			if (storage.getNoticedStorages().stream().filter(x -> x.getId().equals(storage1.getId())).count() == 0) {
				storage1.setNoticeStorageId(null);
				storage1.setNoticeBillNo(null);
				storage1.setIsReversed(null);
				storageRepo.SaveOrUpdate(storage1);
			}
		}

		// 开始处理通知货量关联的交付明细，加上关联关系并设置单号
		for (Storage storage1 : storage.getNoticedStorages()) {
			Storage old = storageRepo.getOneById(storage1.getId(), Storage.class);

			old.setNoticeBillNo(storage.getNoticeBillNo());
			old.setNoticeStorageId(storage.getId());

			old.setIsReversed(storage.getUnCxQuantity().equals(BigDecimal.ZERO));

			storageRepo.SaveOrUpdate(old);
		}
	}

	@Override
	public ActionResult<String> SaveFactories(List<Storage> storages) {

		try {

			if (storages == null || storages.size() == 0)
				throw new Exception("storages is null");
			// 取出所有的 lotid ，并排除重复
			List<String> lotIdList = storages.stream().map(Storage::getLotId).distinct().collect(Collectors.toList());
			// 批量更新
			for (Storage storage : storages) {
				this.repository.SaveOrUpdate(storage);
			}
			if (lotIdList != null) {
				for (String s : lotIdList) {
					this.commonService.UpdateLotPriceByLotId(s);
				}
			}
			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<Storage> GetById(String storageId) {

		Storage storage = this.repository.getOneById(storageId, Storage.class);

		Storage obj = null;

		if (storage != null) {

			obj = this.commonService.SimplifyData(storage);

			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("MergeId", storage.getId()));

			List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

			obj.setMergedStorages(this.commonService.SimplifyDataStorageList(list));
		}

		return new ActionResult<>(true, null, obj);
	}

	@Override
	public ActionResult<List<Storage>> Storages() {

		return new ActionResult<>(true, null, this.repository.GetList(Storage.class));
	}

	/// <summary>
	/// 拆分提单
	/// 逻辑说明：1.只有“方向=入库，IO=I”且“可用数量 > 0 即 QuantityAvailable > 0”的记录才可以拆分
	/// 2. 拆分时，需要拆出多少数量，由系统自动算出一个近似的名义数量 QuantityNotional，也要写入数据库的
	/// 3. 结果就是：一条库存记录，变成了2条
	/// </summary>
	/// <param name="cpSplitStorage"></param>
	/// <param name="userId"></param>
	@Override
	public ActionResult<String> SplitStorage(CpSplitStorage cpSplitStorage, String userId) {

		User user = userHibernateRepository.getOneById(userId, User.class);

		Storage storage = cpSplitStorage.getOriginalStorage();

		if (storage == null || storage.getQuantity().compareTo(BigDecimal.ZERO) <= 0
				|| storage.getMT().equals(MT4Storage.Make)) {

			return new ActionResult<>(false, "参数异常 : MT");
		}

		if (storage.getIsOut())
			return new ActionResult<>(false, "已出库，不允许进行拆分");

		if (cpSplitStorage.getQuantitySplitted().compareTo(storage.getQuantity()) >= 0) {

			return new ActionResult<>(false, "拆分的数量必须小于原数量");
		}

		storage.setBundles(storage.getBundles() - cpSplitStorage.getBundlesSplitted());
		storage.setQuantity(storage.getQuantity().subtract(cpSplitStorage.getQuantitySplitted()));
		storage.setQuantityInvoiced(
				storage.getQuantityInvoiced().subtract(cpSplitStorage.getQuantityInvoicedSplitted()));

		storage.setGross(DecimalUtil.nullToZero(storage.getGross()).subtract(cpSplitStorage.getGrossSplitted()));
		storage.setGrossAtFactory(storage.getGrossAtFactory().subtract(cpSplitStorage.getGrossAtFactorySplitted()));
		if(storage.getQuantityAtWarehouse() != null) {
			storage.setQuantityAtWarehouse(storage.getQuantityAtWarehouse().subtract(cpSplitStorage.getQuantityAtWarehouseSplitted()));
		}
		// storage.SplitNo =(storage.SplitNo==null||storage.SplitNo <= 0) ? 1 :
		// storage.SplitNo + 1;
		storageRepo.SaveOrUpdate(storage);

		storage = storageRepo.getOneById(storage.getId(), Storage.class);// 原记录

		Integer iNo = commonService.GetSequenceIndex(NumTypes.Split_Storage + storage.getProjectName());
		// 本条交付明细已被分拆过的数据
		// var spliteds = Repository.GetQueryable<Storage>().Where(x =>
		// x.SourceId != null && x.SourceId == storage.Id);
		String splitStr = "(" + iNo.toString() + ")";

		// #region 构建新的拆分出来的库存记录，并保存
		Storage theSplit = new Storage();

		// BeanUtils.copyProperties(theSplit, storage);
		theSplit = com.smm.ctrm.util.BeanUtils.copy(storage);
		theSplit.setId(null);
		theSplit.setIsSplitted(true);
		theSplit.setSourceId(storage.getSourceId() == null ? storage.getId() : storage.getSourceId());
		theSplit.setIsMerged(false);
		theSplit.setMergeId(storage.getMergeId());
		theSplit.setBundles(cpSplitStorage.getBundlesSplitted()); // 非常重要
		theSplit.setQuantityInvoiced(cpSplitStorage.getQuantityInvoicedSplitted());
		theSplit.setQuantity(cpSplitStorage.getQuantitySplitted());
		theSplit.setGross(cpSplitStorage.getGrossSplitted());
		theSplit.setGrossAtFactory(cpSplitStorage.getGrossAtFactorySplitted());
		theSplit.setQuantityAtWarehouse(cpSplitStorage.getQuantityAtWarehouseSplitted());
		theSplit.setCreatedAt(new Date());
		theSplit.setCreatedBy(user.getName());
		theSplit.setProjectName("* " + storage.getProjectName() + splitStr);
		theSplit.setIsInvoiced(false);

		String SplitId = storageRepo.SaveOrUpdateRetrunId(theSplit);

		// #region 交付明细已经存在发票的处理

		if (storage.getIsInvoiced()) {
			List<Invoice> invoices = InvoicesByStorageId(storage.getId());
			if (invoices != null && invoices.size() > 0) {
				/*--- 拆分后，新拆出来的货物，也要对应增加所有发票 by lihuan 2016-07-19 ---*/
				DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
				dc.add(Restrictions.eq("Id", SplitId));
				Storage split = this.repository.GetQueryable(Storage.class).where(dc).firstOrDefault();
				for (Invoice invoice : invoices) {
					if (split != null && invoice.getStorages() != null) {
						invoice.getStorages().add(split);
						this.invoiceHibernateRepository.SaveOrUpdate(invoice);
					}
				}
			}
		}

		return new ActionResult<>(true, "成功拆分");
	}

	public List<Invoice> InvoicesByStorageId(String id) {
		if (StringUtils.isBlank(id)) {
			return new ArrayList<>();
		}
		String sql = " select invoice.* " + " from Physical.Invoice invoice inner join "
				+ " Physical.InvoiceStorage ist on invoice.Id = ist.InvoiceId " + " where ist.StorageId ='" + id + "' ";
		return invoiceHibernateRepository.ExecuteCorrectlySql(sql, Invoice.class);

	}

	protected void subStock(List<Storage> notOutStorages, Storage splittSource, BigDecimal total) {
		if (total.compareTo(BigDecimal.ZERO) > 0) {
			if (notOutStorages.size() == 0 && splittSource!=null&&!splittSource.getIsOut()) {
				// 已经没有拆分出来的库存，直接修改原库存记录
				splittSource.setQuantity(splittSource.getQuantity().subtract(total));
				this.storageRepo.SaveOrUpdate(splittSource);
			} else {
				for (int i = notOutStorages.size() - 1; i >= 0; i--) {
					Storage storage = notOutStorages.get(i);
					if (storage.getQuantity().compareTo(total) > 0) {
						storage.setQuantity(storage.getQuantity().subtract(total));
						this.storageRepo.SaveOrUpdate(storage);
					} else {
						total = total.subtract(storage.getQuantity());
						storage.setIsHidden(true);
						storage.setIsDeleted(true);
						this.storageRepo.SaveOrUpdate(storage);// 逻辑删除
						notOutStorages.remove(i);
						subStock(notOutStorages, splittSource, total);
					}
				}
			}
		}
	}

	@Override
	public ActionResult<String> ReviseQuantity(Storage storage) {

		if (storage == null)
			return new ActionResult<>(Boolean.FALSE, "storage is null");
		
		Storage queryStorage = this.repository.getOneById(storage.getId(), Storage.class);
		
		if(storage.getQuantity().compareTo(queryStorage.getQuantity())==0){
			return new ActionResult<>(Boolean.FALSE, "原有数量和实际数量相等");
		}
		//判断数量是否超过溢短装
		if(queryStorage.getLotId()!=null){
			Lot lot=this.lotRepository.getOneById(queryStorage.getLotId(), Lot.class);
			DetachedCriteria dc=DetachedCriteria.forClass(Storage.class);
			dc.add(Restrictions.eq("LotId", queryStorage.getLotId()));
			dc.add(Restrictions.ne("Id", storage.getId()));
			dc.setProjection(Projections.sum("Quantity"));
			BigDecimal count=(BigDecimal) this.storageRepo.getHibernateTemplate().findByCriteria(dc).get(0);
			BigDecimal promise=count!=null?count.add(storage.getQuantity()):storage.getQuantity();
			if(promise.compareTo(lot.getQuantityLess())<0||promise.compareTo(lot.getQuantityMore())>0){
				return new ActionResult<>(Boolean.FALSE, "修改实数超过关联订单溢短装范围。溢短装范围："
												+lot.getQuantityLess().setScale(3, BigDecimal.ROUND_HALF_UP)
												+"-"+
												lot.getQuantityMore().setScale(3, BigDecimal.ROUND_HALF_UP)
									);
			}
		}
		
		/**
		 * 修改发货单
		 */
		if (queryStorage.getMT().equals("M")) {
			// 根据发货单CounterpartyId3 找到 采购单
			Storage buySource = this.repository.getOneById(queryStorage.getCounterpartyId3(), Storage.class);
			BigDecimal total = queryStorage.getQuantity().subtract(storage.getQuantity());//本次修改变化数
			
			if (buySource.getMT().equals("T")) {
				
				if (buySource.getIsSplitted()) {

					// 根据SourceId取来源记录，
					Storage splittSource = this.repository.getOneById(buySource.getSourceId(), Storage.class);
					/**
					 * 获取全部拆分的收货单,判断是否已经全部出货
					 */
					DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
					dc.add(Restrictions.eq("MT", "T"));
					dc.add(Restrictions.eq("SourceId", buySource.getSourceId()));
					List<Storage> sourceStorages = this.storageRepo.GetQueryable(Storage.class).where(dc).toList();
					List<Storage> notOutStorages = sourceStorages.stream().filter(s -> s.getIsOut() == false)
							.collect(Collectors.toList());

					boolean flag = sourceStorages.stream().anyMatch(s -> s.getIsOut() == false);
					BigDecimal surplus = BigDecimal.ZERO;// 剩余库存
					for (Storage s : sourceStorages) {
						if (!s.getIsOut()) {
							surplus = surplus.add(s.getQuantity());
						}
					}

					

					if ((!splittSource.getIsOut()) || flag) {
						/**
						 * 收货记录未全部发货。如果来源没有发货，修改拆分来源。否则修改任意一条拆分的记录
						 */
						if (!splittSource.getIsOut()) {
							surplus = surplus.add(splittSource.getQuantity());
							if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {
								// 实数减少的，增加剩余库存
								splittSource.setQuantity(splittSource.getQuantity().add(total));
								this.storageRepo.SaveOrUpdate(splittSource);
								queryStorage.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(queryStorage);
								buySource.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(buySource);
							} else {
								// 实数增加的，判断库存是否足够，减少库存
								if (surplus.compareTo(total.abs()) >= 0) {
									/**
									 * 先从拆分出来的库存减少
									 */
									subStock(notOutStorages, splittSource, total.abs());
									queryStorage.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(queryStorage);
									buySource.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(buySource);
								} else {
									return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
								}
							}

						} else {
							/**
							 * 如果来源已经发货
							 */
							if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {
								Storage fristsourceStorage = sourceStorages.stream().filter(s -> !s.getIsOut())
										.findFirst().orElse(null);
								if (fristsourceStorage != null) {
									fristsourceStorage.setQuantity(fristsourceStorage.getQuantity().add(total));
									this.storageRepo.SaveOrUpdate(fristsourceStorage);
									queryStorage.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(queryStorage);
									buySource.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(buySource);
								}
							} else {
								// 实数增加的，判断库存是否足够，减少库存
								if (surplus.compareTo(total) >= 0) {
									/**
									 * 先从拆分出来的库存减少
									 */
									subStock(notOutStorages, splittSource, total.abs());
									queryStorage.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(queryStorage);
									buySource.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(buySource);
								} else {
									return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
								}
							}
						}
						// 修改发货单本次数量
						ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(), ReceiptShip.class);
						rs.setWeight(storage.getQuantity());
						this.receiptShipRepo.SaveOrUpdate(rs);

					} else {
						// 全部发货
						// 修改多了提示库存不足，修改少了生成新的库存
						if (storage.getQuantity().compareTo(queryStorage.getQuantity()) > 0) {
							return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
						} else {
							// 少发货，还是赋值一条新记录
							return new ActionResult<String>(true, "原卡号已经发货，修改实数后将产生新的库存，是否继续？", null, CODE_1002);
						}
					} 
				} else {
					/**
					 *  判断收货单是否是拆分，并且没有拆分记录
					 *  
					 */
					DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
					dc.add(Restrictions.eq("MT", "T"));
					dc.add(Restrictions.eq("SourceId", buySource.getId()));
					List<Storage> sourceStorages = this.storageRepo.GetQueryable(Storage.class).where(dc).toList();
					if(sourceStorages.size()>0){
						List<Storage> notOutStorages = sourceStorages.stream().filter(s -> s.getIsOut() == false)
								.collect(Collectors.toList());
						boolean flag = sourceStorages.stream().anyMatch(s -> s.getIsOut() == false);
						if(flag){
							BigDecimal surplus = BigDecimal.ZERO;// 剩余库存
							for (Storage s : sourceStorages) {
								if (!s.getIsOut()) {
									surplus = surplus.add(s.getQuantity());
								}
							}
							if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {
								// 实数减少的，增加剩余库存，取第一条拆分记录
								Storage frist=sourceStorages.get(sourceStorages.size()-1);
								frist.setQuantity(frist.getQuantity().add(total));
								this.storageRepo.SaveOrUpdate(frist);
								queryStorage.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(queryStorage);
								buySource.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(buySource);
								if(queryStorage.getIsOut()){
									// 修改发货单本次数量
									ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(), ReceiptShip.class);
									rs.setWeight(storage.getQuantity());
									this.receiptShipRepo.SaveOrUpdate(rs);
								}
								
							} else {
								// 实数增加的，判断库存是否足够，减少库存
								if (surplus.compareTo(total.abs()) >= 0) {
									/**
									 * 先从拆分出来的库存减少
									 */
									subStock(notOutStorages, null, total.abs());
									queryStorage.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(queryStorage);
									buySource.setQuantity(storage.getQuantity());
									this.repository.SaveOrUpdate(buySource);
									
									if(queryStorage.getIsOut()){
										// 修改发货单本次数量
										ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(), ReceiptShip.class);
										rs.setWeight(storage.getQuantity());
										this.receiptShipRepo.SaveOrUpdate(rs);
									}
									
								} else {
									return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
								}
							}
							
						}else{
							if (storage.getQuantity().compareTo(queryStorage.getQuantity()) > 0) {
								return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
							} else {
								// 少发货，还是赋值一条新记录
								return new ActionResult<String>(true, "原卡号已经发货，修改实数后将产生新的库存，是否继续？", null, CODE_1002);
							}
						}
					}else{
						// 发货对应的收货记录没有拆分
						return new ActionResult<String>(true, "本次修改将同步修改收货重量，是否继续？", null, CODE_1005);
					}
				}
			}
		} else {
			/**
			 * 修改收货单
			 */

			if (queryStorage.getIsOut()) {
				// (2)已经发货
				if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {
					return new ActionResult<String>(false, "修改后的重量不能小于已发货重量。", null, CODE_1003);
				} else {
					return new ActionResult<String>(true, "修改后重量大于发货重量,将同步更新发货重量。", null, CODE_1004);
				}
			} else {
				// (1)收货记录未发货，修改实数字段（出厂净重）
				queryStorage.setQuantity(storage.getQuantity());
				this.repository.SaveOrUpdate(queryStorage);
			}
		}

		if (storage.getLotId() != null) {
			Lot lot = this.lotRepository.getOneById(storage.getLotId(), Lot.class);

			if (lot != null) {

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.eq("IsHidden", false));

				List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

				BigDecimal allQuantity = BigDecimal.ZERO;
				if (list != null) {
					for (Storage s : list) {
						allQuantity = allQuantity.add(s.getQuantity());
					}
				}

				lot.setQuantityDelivered(allQuantity);

				this.lotRepository.SaveOrUpdate(lot);

				this.commonService.UpdateDeliveryStatus(this.commonService.setDelivery4Lot(lot));

			}
		}

		return new ActionResult<String>(true, "成功修改实数");

	}

	/**
	 * 
	 * CounterpartyId2记录去向
	 * 
	 * CounterpartyId3记录来源
	 * 
	 */
	@Override
	public ActionResult<String> saveReviseQuantity(Storage storage) {

		if (storage == null)
			new ActionResult<>(Boolean.FALSE, "storage is null");

		Storage queryStorage = this.repository.getOneById(storage.getId(), Storage.class);
		BigDecimal total = queryStorage.getQuantity().subtract(storage.getQuantity());
		if (queryStorage.getMT().equals("M")) {
			// 根据发货单CounterpartyId3 找到 采购单
			Storage buySource = this.repository.getOneById(queryStorage.getCounterpartyId3(), Storage.class);
			if (buySource.getMT().equals("T")) {
				// 判断收货单是否是拆分，取来SourceId
				if (buySource.getIsSplitted()) {
					// 根据SourceId取来源记录，
					Storage splittSource = this.repository.getOneById(buySource.getSourceId(), Storage.class);

					/**
					 * 获取全部拆分的收货单,判断是否已经全部出货
					 */
					DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
					dc.add(Restrictions.eq("MT", "T"));
					dc.add(Restrictions.eq("SourceId", buySource.getSourceId()));
					List<Storage> sourceStorages = this.storageRepo.GetQueryable(Storage.class).where(dc).toList();

					boolean flag = sourceStorages.stream().anyMatch(s -> !s.getIsOut());
					BigDecimal surplus = BigDecimal.ZERO;// 剩余库存
					for (Storage s : sourceStorages) {
						if (!s.getIsOut()) {
							surplus = surplus.add(s.getQuantity());
						}
					}
					if (flag) {
						// 收货记录未全部发货。如果来源没有发货，修改拆分来源。否则修改任意一条拆分的记录
						if (!splittSource.getIsOut()) {
							surplus = surplus.add(splittSource.getQuantity());
							if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {

							} else {
								// 实数增加的，判断库存是否足够，减少库存
								if (surplus.compareTo(total) > 0) {

								} else {

								}
							}

						} else {
							/**
							 * 如果来源已经发货
							 */
							if (queryStorage.getQuantity().compareTo(storage.getQuantity()) > 0) {

							} else {

							}
						}

					} else {
						// 修改多了提示库存不足，修改少了生成新的库存
						if (storage.getQuantity().compareTo(queryStorage.getQuantity()) > 0) {
							return new ActionResult<String>(false, "没有足够库存。", null, CODE_1001);
						} else {
							// 少发货，赋值一条新记录
							Storage copyStorage = new Storage();
							try {
								ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
								ConvertUtils.register(new SqlDateConverter(null), java.math.BigDecimal.class);
								BeanUtils.copyProperties(copyStorage, splittSource);
								// 修改实数
								queryStorage.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(queryStorage);
								buySource.setQuantity(storage.getQuantity());
								this.repository.SaveOrUpdate(buySource);
								// 复制一条记录
								copyStorage.setIsSplitted(true);
								copyStorage.setLotId(buySource.getLotId());
								copyStorage.setQuantity(total);
								copyStorage.setGross(total);
								copyStorage.setGrossAtFactory(total);
								copyStorage.setId(null);
								copyStorage.setIsOut(false);
								copyStorage.setCounterparty2(null);
								copyStorage.setSourceId(buySource.getSourceId()!=null?buySource.getSourceId():buySource.getId());
								copyStorage.setMT("T");
								copyStorage.setProjectName(
										"* " + splittSource.getProjectName() + "(" + (sourceStorages.size() + 1) + ")");
								this.repository.SaveOrUpdate(copyStorage);

								
								// 修改发货单本次数量
								if(queryStorage.getRefId()!=null){
									ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(),ReceiptShip.class);
									rs.setWeight(storage.getQuantity());
									this.receiptShipRepo.SaveOrUpdate(rs);
								}

							} catch (Exception e) {
								e.printStackTrace();
								return new ActionResult<>(false, "修改实数失败");
							}
						}
					}
				} else {
					/**
					 *  判断收货单没有拆分记录
					 *  
					 */
					DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
					dc.add(Restrictions.eq("MT", "T"));
					dc.add(Restrictions.eq("SourceId", buySource.getId()));
					List<Storage> sourceStorages = this.storageRepo.GetQueryable(Storage.class).where(dc).toList();
					if(sourceStorages.size()>0){
						// 少发货，赋值一条新记录
						Storage copyStorage = new Storage();
						try {
							ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
							ConvertUtils.register(new SqlDateConverter(null), java.math.BigDecimal.class);
							BeanUtils.copyProperties(copyStorage, buySource);
							// 修改实数
							queryStorage.setQuantity(storage.getQuantity());
							this.repository.SaveOrUpdate(queryStorage);
							buySource.setQuantity(storage.getQuantity());
							this.repository.SaveOrUpdate(buySource);
							// 复制一条记录
							copyStorage.setIsSplitted(true);
							copyStorage.setLotId(buySource.getLotId());
							copyStorage.setQuantity(total);
							copyStorage.setGross(total);
							copyStorage.setGrossAtFactory(total);
							copyStorage.setId(null);
							copyStorage.setIsOut(false);
							copyStorage.setCounterparty2(null);
							copyStorage.setSourceId(buySource.getSourceId()!=null?buySource.getSourceId():buySource.getId());
							copyStorage.setMT("T");
							copyStorage.setProjectName(
									"* " + buySource.getProjectName() + "(" + (sourceStorages.size() + 1) + ")");
							this.repository.SaveOrUpdate(copyStorage);

							// 修改发货单本次数量
							if(queryStorage.getRefId()!=null){
								ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(),
										ReceiptShip.class);
								rs.setWeight(storage.getQuantity());
								this.receiptShipRepo.SaveOrUpdate(rs);
							}

						} catch (Exception e) {
							e.printStackTrace();
							return new ActionResult<>(false, "修改实数失败");
						}
					}else{
						// 修改收货重量
						buySource.setQuantity(storage.getQuantity());
						this.repository.SaveOrUpdate(buySource);
						// 修改发货重量
						queryStorage.setQuantity(storage.getQuantity());
						this.repository.SaveOrUpdate(queryStorage);

						// 修改发货单本次数量
						if(queryStorage.getRefId()!=null){
							ReceiptShip rs = this.receiptShipRepo.getOneById(queryStorage.getRefId(), ReceiptShip.class);
							rs.setWeight(storage.getQuantity());
							this.receiptShipRepo.SaveOrUpdate(rs);
						}
					}
				}
			}
		} else {
			/**
			 * 修改收货记录，并且同步更新发货记录
			 */
			Storage sellStorage = this.storageRepo.getOneById(queryStorage.getCounterpartyId2(), Storage.class);
			// 修改发货记录
			sellStorage.setQuantity(storage.getQuantity());
			this.repository.SaveOrUpdate(sellStorage);
			// 修改收货记录
			queryStorage.setQuantity(storage.getQuantity());
			this.repository.SaveOrUpdate(queryStorage);

			// 修改发货单本次数量(直接生成销售订单是没有refId的)
			if(sellStorage.getRefId()!=null){
				ReceiptShip rs = this.receiptShipRepo.getOneById(sellStorage.getRefId(), ReceiptShip.class);
				rs.setWeight(storage.getQuantity());
				this.receiptShipRepo.SaveOrUpdate(rs);
			}
		}

		if (storage.getLotId() != null) {
			Lot lot = this.lotRepository.getOneById(storage.getLotId(), Lot.class);

			if (lot != null) {

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.eq("IsHidden", false));

				List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

				BigDecimal allQuantity = BigDecimal.ZERO;
				if (list != null) {
					for (Storage s : list) {
						allQuantity = allQuantity.add(s.getQuantity());
					}
				}

				lot.setQuantityDelivered(allQuantity);

				this.lotRepository.SaveOrUpdate(lot);

				this.commonService.UpdateDeliveryStatus(this.commonService.setDelivery4Lot(lot));

			}
		}

		return new ActionResult<>(true, "成功修改实数");

	}

	@Override
	public ActionResult<Storage> CreateVirtual(Storage storage) {

		try {

			if (!storage.getIsVirtual())
				return new ActionResult<>(false, "参数错误");

			this.repository.SaveOrUpdate(storage);

			return new ActionResult<>(true, "更新成功", storage);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> FillBack(List<Storage> storages, Storage targetVirtualStorage) {

		return new ActionResult<>(false, "待实现");
	}

	@Override
	public void ImportInitStorages() {

	}

	@Override
	public ActionResult<List<Storage>> StoragesByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("ContractId", contractId));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> StoragesByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", lotId));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> StoragesByProjectName(String projectName) {

		if (StringUtils.isEmpty(projectName))
			return new ActionResult<>(false, "projectName is null");

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("ProjectName", projectName));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> SourceStoragesById(String storageId) {

		if (StringUtils.isEmpty(storageId))
			return new ActionResult<>(false, "storageId is null");

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("SourceId", storageId));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> BviSourceStoragesById(String storageId) {

		if (StringUtils.isEmpty(storageId))
			return new ActionResult<>(false, "storageId is null");

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("BviSourceId", storageId));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> MergeSourceStoragesById(String storageId) {

		if (StringUtils.isEmpty(storageId))
			return new ActionResult<>(false, "storageId is null");

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("MergeId", storageId));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> StoragesByCustomerId(String customerId) {

		if (StringUtils.isEmpty(customerId))
			return new ActionResult<>(false, "customerId is null");

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("CustomerId", customerId));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> list = this.repository.GetQueryable(Storage.class).where(where).toList();

		list = this.commonService.SimplifyDataStorageList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Storage>> StoragesBySummaryFeesId(String summaryFeeId) {

		SummaryFees summaryFees = this.summaryFeesRepository.getOneById(summaryFeeId, SummaryFees.class);

		if (summaryFees == null)
			return new ActionResult<>(false, "summaryFees is null");

		return new ActionResult<>(true, null, summaryFees.getStorages());

	}

	/**
	 * 检查批次信息
	 * 
	 * @param storage
	 */
	public ActionResult<Lot> checkLot(List<Storage> storage) {

		/**
		 * 检查批次号是否存在
		 */
		DetachedCriteria dc = DetachedCriteria.forClass(Lot.class);
		dc.add(Restrictions.eq("FullNo", storage.get(0).getFullNo()));
		Lot lot = this.lotRepository.GetQueryable(Lot.class).where(dc).firstOrDefault();
		if (lot == null) {
			return new ActionResult<>(false, "订单批次号不存在，不可收货", "1002");
		}

		BigDecimal sum = BigDecimal.ZERO;
		for (Storage s : storage) {
			sum = sum.add(s.getQuantity());
		}

		/**
		 * 批次已经收货完成，提示该批次已经超过批次溢短装数量最大值，不可收货
		 */

		if (lot.getIsDelivered()) {
			return new ActionResult<>(false, "批次已经超过批次溢短装数量最大值，不可收货", "1003");
		}
		/**
		 * 本次导入到批次数量汇总超过批次数量溢短装范围最大值，不可收货
		 * 
		 * 本次收货数量(Quantity)+实际的交付数量(QuantityDelivered)<=约定交付的最多数量(QuantityMore)
		 */
		if (sum.add(lot.getQuantityDelivered()).compareTo(lot.getQuantityMore()) > 0) {
			return new ActionResult<>(false, "本次导入到批次数量汇总超过批次数量溢短装范围最大值，不可收货", "1004");
		}

		return new ActionResult<>(true, "检查通过.", lot);
	}

	@Override
	public ActionResult<List<Storage>> ImportFactory(List<Storage> storage, String userName, String userId) {
		List<Storage> resultList = new ArrayList<>();
		if (storage == null || storage.size() == 0)
			return new ActionResult<>(false, "不存在需要导入的记录");

		if (StringUtils.isNotBlank(storage.get(0).getFullNo())) {
			ActionResult<Lot> result = checkLot(storage);

			if (!result.isSuccess()) {
				return new ActionResult<>(false, result.getMessage(), storage);
			}
			/**
			 * 自动将交付明细生成收货单并收货到批次
			 */
			if (StringUtils.isNotBlank(storage.get(0).getFullNo()) && result.isSuccess()) {
				// 根据仓库名称查仓库ID
				Warehouse wh = this.warehouseHibernateRepository.getOneById(storage.get(0).getWarehouseId(),
						Warehouse.class);
				if (wh == null) {
					return new ActionResult<>(false, "仓库不存在.");
				}
				Lot lot = result.getData();
				ReceiptShip rs = new ReceiptShip();
				rs.setFlag(ReceiptShip.RECEIPT);// 收货
				rs.setLotId(lot.getId());
				rs.setContractId(lot.getContractId());
				rs.setCommodityId(lot.getCommodityId());
				rs.setCustomerId(lot.getCustomerId());
				rs.setCustomerName(lot.getCustomer().getName());
				rs.setBrandId(storage.get(0).getBrandId());
				rs.setSpecId(lot.getSpecId());
				rs.setWhId(wh.getId());
				rs.setWhName(wh.getName());
				rs.setReceiptShipDate(new Date());
				rs.setWhOutEntryDate(new Date());
				rs.setStorages(storage);
				/**
				 * 重量
				 */
				BigDecimal sum = BigDecimal.ZERO;
				for (Storage s : storage) {
					sum = sum.add(s.getQuantity());
				}
				rs.setWeight(sum);
				/**
				 * 发货单号
				 */
				ActionResult<ReceiptShipInit> init = receiptService.Init(null, ReceiptShip.RECEIPT);
				rs.setReceiptShipNo(init.getData().getNo());
				// 创建收货单
				receiptService.Save(rs);
			}
		} else {
			for (Storage s : storage) {
				s.setIsFactory(true);
				s.setCreatedAt(new Date());
				s.setCreatedBy(userName);
				s.setCreatedId(userId);

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("ProjectName", s.getProjectName()));
				List<Storage> storageList = storageRepo.GetQueryable(Storage.class).where(where).toList();
				if (storageList != null && storageList.size() > 0) {
					resultList.add(s);
				} else {
					storageRepo.SaveOrUpdate(s);
				}
			}
		}
		if (resultList.size() == 0) {
			return new ActionResult<>(true, MessageCtrm.SaveSuccess);
		} else {
			return new ActionResult<>(false, "项目名称重复", resultList);
		}
	}

	@Override
	public ActionResult<String> ImportBvis(List<Storage> storages, String userName, String userId) {

		try {

			if (storages == null || storages.size() == 0)
				return new ActionResult<>(false, "不存在需要导入的记录");

			int i = 0;
			for (Storage v : storages) {
				v.setCreatedAt(new Date());
				v.setCreatedBy(userName);
				v.setCreatedId(userId);

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("ProjectName", v.getProjectName()));
				Storage exist = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

				if (exist == null) {
					storageRepo.SaveOrUpdate(v);
					i++;
				} else {
					// 如果已经出库，则不再覆盖处理
					if (exist.getIsIn() || exist.getIsOut())
						continue;

					// 如果没有出库，则允许修改（即覆盖处理）
					exist.setMT(MT4Storage.Take);
					exist.setTradeDate(new Date());
					exist.setUpdatedAt(new Date());
					exist.setUpdatedBy(userName);
					exist.setUpdatedId(userId);
					exist.setBrandId(v.getBrandId());
					exist.setLegalId(v.getLegalId());
					exist.setCommodityId(v.getCommodityId());
					exist.setBundles(v.getBundles());
					exist.setDepartureDate(v.getDepartureDate());
					exist.setDiff(v.getDiff());
					exist.setDiffRate(v.getDiffRate());
					exist.setGrade(v.getGrade());
					exist.setGrossAtFactory(v.getGrossAtFactory());
					exist.setGross(v.getGross());
					exist.setLoadDate(v.getLoadDate());
					exist.setLocationStatus(v.getLocationStatus());
					exist.setQuantity(v.getQuantity());
					exist.setNo(v.getNo());
					exist.setProduct(v.getProduct());
					// exist.TestDate = v.TestDate;
					// exist.TestNo = v.TestNo;
					exist.setTrailer(v.getTrailer());
					exist.setTruckNo(v.getTruckNo());
					storageRepo.SaveOrUpdate(exist);
					i++;
				}
			}

			return new ActionResult<>(true, "本次共导入了 " + i + " 条记录。");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> MergeBvis(CpMergeStorages cpMergeStorages, String userId) {

		User user = userHibernateRepository.getOneById(userId, User.class);

		// #region 数据格式检查
		if (cpMergeStorages == null || cpMergeStorages.getMerge() == null || cpMergeStorages.getMergedStorages() == null
				|| cpMergeStorages.getMergedStorages().size() == 0 || user == null) {

			return new ActionResult<>(false, "参数错误");
		}

		if (cpMergeStorages.getMergedStorages().stream().anyMatch(Storage::getIsOut)) {

			return new ActionResult<>(false, "部分被合并对象已经出库，不允许此项操作。");
		}
		List<String> obj = cpMergeStorages.getMergedStorages().stream().map(Storage::getProjectName)
				.collect(Collectors.toList());

		String[] nameArray = obj != null & obj.size() > 0 ? obj.toArray(new String[obj.size()]) : null;

		cpMergeStorages.getMerge().setProjectName(StringUtils.join(nameArray, "/"));

		String storageId = storageRepo.SaveOrUpdateRetrunId(cpMergeStorages.getMerge());
		List<Storage> ts = new ArrayList<>();

		for (Storage s : cpMergeStorages.getMergedStorages()) {

			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("MergeId", s.getId()));
			List<Storage> mergedBofore = storageRepo.GetQueryable(Storage.class).where(where).toList();

			if (mergedBofore.size() > 0) {
				ts.addAll(mergedBofore);
				storageRepo.PhysicsDelete(s.getId(), Storage.class);
			} else
				ts.add(s);
		}

		if (ts != null) {
			for (Storage s : ts) {
				Storage t = storageRepo.getOneById(s.getId(), Storage.class);
				t.setMergeId(storageId);
				t.setIsHidden(true);
				storageRepo.SaveOrUpdate(t);
			}
		}
		return new ActionResult<>(true, "合并成功");

	}

	@Override
	public ActionResult<String> MergeCancel(String userId, String storageId) {

		Storage storage = storageRepo.getOneById(storageId, Storage.class);

		User user = userHibernateRepository.getOneById(userId, User.class);

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("MergeId", storageId));
		List<Storage> storagesMerged = storageRepo.GetQueryable(Storage.class).where(where).toList();

		// #region 数据格式检查
		if (storage == null || user == null || storagesMerged.size() == 0) {

			return new ActionResult<>(false, "参数错误");
		}

		if (storage.getIsOut())
			return new ActionResult<>(false, "已经出库，不允许撤消合并");

		// 20150714 追加逻辑判断，如果当前合并的lotid
		// 与被合并的lotid不相同，不允许取消合并（因为该收货动作是合并后处理的，必须先取消收货）
		if (!StringUtils.isEmpty(storage.getLotId())
				&& storagesMerged.stream().filter(x -> !x.getLotId().equals(storage.getId())).count() > 0) {
			return new ActionResult<>(false, "该条记录已入库，不允许撤消合并，请先删除对应批次中的该明细");
		}

		try {

			storagesMerged.forEach(sm -> {

				sm.setMergeId(null);
				sm.setIsHidden(false);
				storageRepo.SaveOrUpdate(sm);
			});

			storageRepo.PhysicsDelete(storage);

			return new ActionResult<>(true, "撤消成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> TakeStoragesFromFactory(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {

			return new ActionResult<>(false, "参数为空");
		}

		Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

		if (lot == null)
			return new ActionResult<>(false, "lot is null");

		try {

			// 检查：累计的交付数量，是否大于批次的QuantityMore。超过时，则提示用户修改、不予处理、直接返回。
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", cpLotStorages.getLotId()));
			List<Storage> list = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal temp_sum = new BigDecimal(
					cpLotStorages.getStorages().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			BigDecimal list_temp_sum = new BigDecimal(
					list.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
				QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed(lot);

				if (quantityMaL != null && quantityMaL.getQuantityMore()
						.compareTo(quantityMaL.getQuantityDeliveryed().add(temp_sum)) < 0) {

					return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");
				}
			} else if (lot.getQuantityMore().compareTo(list_temp_sum.add(temp_sum)) < 0) {
				return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");
			}

			// 确认已经收货入库，只会对BVI采购自工厂的合同的收货
			for (Storage storage : cpLotStorages.getStorages()) {
				if (!StringUtils.isEmpty(storage.getId())) {
					storage.setUpdatedAt(new Date());
				} else {
					storage.setCreatedAt(new Date());
				}

				storage.setLotId(lot.getId());
				storage.setContractId(lot.getContractId());
				storage.setIsIn(true);
				storage.setCustomerId(lot.getCustomerId());
				storage.setTradeDate(new Date());
				storage.setIsReBuy(lot.getIsReBuy());

				if (lot.getLotNo() >= 10 && lot.getLotNo() < 130)
					storage.setPurchaseMonth(lot.getLotNo() / 10);
				else {
					storage.setPurchaseMonth(null);
				}

				// 如果收货时，lot已经结束点价，则在此处即更新价格有关的成员
				commonService.SetStorage(lot, storage);

				storageRepo.SaveOrUpdate(storage);
			}

			// 更新批次的IsDelivered标志
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
			// lot.QuantityDelivered <= lot.QuantityMore);
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);

			// 更新批次价格及余额
			commonService.UpdateLotPriceByLotId(lot.getId());
			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> DeleteFactoryIns(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getStorages() == null || cpLotStorages.getStorages().size() <= 0) {

			return new ActionResult<>(false, "lot or lot.Storages is null 或者不是一个子记录。");
		}

		try {

			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

			for (Storage storage : cpLotStorages.getStorages()) {

				if (storage == null)
					continue;

				if (storage.getIsOut())
					return new ActionResult<>(false, "已出库，禁止删除");

				if (storage.getIsInvoiced())
					return new ActionResult<>(false, "已经收/开票，禁止删除");

				// 检查是否存在拆分出来的记录
				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("SourceId", storage.getId()));
				Storage childrenSplited = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

				if (childrenSplited != null)
					return new ActionResult<>(false, "请先删除被拆分出来的记录");

				// 检查是否是合并的记录，如果是，返回到“明细”-“撤消合并”
				where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("MergeId", storage.getId()));
				List<Storage> a = storageRepo.GetQueryable(Storage.class).where(where).toList();

				if (a.size() > 0)
					return new ActionResult<>(false, "请返回到查看合并的界面，先撤消合并");

				if (storage.getIsSplitted()) {

					// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
					Storage source = storageRepo.getOneById(storage.getSourceId(), Storage.class);

					if (source != null) {
						if (source.getIsOut()) {
							return new ActionResult<>(false, "原始库存已发货！");
						}
						if (!source.getLotId().equalsIgnoreCase(storage.getLotId())) {
							return new ActionResult<>(false, "拆分库存与原始库存不属于同一订单，无法删除！");
						}
						source.setBundles(source.getBundles() + storage.getBundles());
						source.setQuantityInvoiced(source.getQuantityInvoiced().add(storage.getQuantityInvoiced()));
						source.setQuantity(source.getQuantity().add(storage.getQuantity()));
						source.setGross(source.getGross().add(storage.getGross()));
						source.setGrossAtFactory(source.getGrossAtFactory().add(storage.getGrossAtFactory()));
						source.setQuantityAtWarehouse(source.getQuantityAtWarehouse().add(storage.getQuantityAtWarehouse()));
						storageRepo.SaveOrUpdate(source);
					}
					storageRepo.PhysicsDelete(storage);
				} else {
					if (storage.getIsFactory() && lot != null) // (1)不需要真正删除工厂的商品运输明细，只需重置几个标志值
					{
						storage.setIsIn(false);
						storage.setLotId(null);
						storage.setCustomerId(null);
						storage.setTradeDate(null);
						storage.setFullNo(null);
						storageRepo.SaveOrUpdate(storage);
						if (storage.getIsMerged()) {
							// 如果是合并记录，则需要将合并记录的原始记录取消关联
							where = DetachedCriteria.forClass(Storage.class);
							where.add(Restrictions.eq("MergeId", storage.getId()));
							List<Storage> merges = storageRepo.GetQueryable(Storage.class).where(where).toList();

							for (Storage merge : merges) {
								merge.setIsIn(false);
								merge.setLotId(null);
								merge.setCustomerId(null);
								merge.setTradeDate(null);
								merge.setFullNo(null);
								storageRepo.SaveOrUpdate(merge);
							}

						}
					} else {
						storageRepo.PhysicsDelete(storage); // (2)物理删除
					}
				}

			}

			// 更新批次的QuantityStoraged + IsDelivered标志
			if (lot != null && !StringUtils.isEmpty(lot.getId())) {

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.eq("IsHidden", false));
				List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();
				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				List<Lot> lots = commonService.setDelivery4Lot(lot);

				lotRepository.SaveOrUpdate(lot);

				commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
			}

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<String> RemoveFactoryIns(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getStorages() == null || cpLotStorages.getStorages().size() <= 0) {

			return new ActionResult<>(false, "lot or lot.Storages is null 或者不是一个子记录。");
		}

		try {

			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

			for (Storage storage : cpLotStorages.getStorages()) {

				if (storage == null)
					continue;

				if (storage.getIsOut())
					return new ActionResult<>(false, "已出库，禁止删除");

				if (storage.getIsInvoiced())
					return new ActionResult<>(false, "已经收/开票，禁止删除");

				if (lot != null) {

					if (lot.getIsReBuy()) {
						// 更新入库记录的状态
						Storage theIn = storageRepo.getOneById(storage.getCounterpartyId(), Storage.class);

						if (theIn != null) {
							theIn.setCounterpartyId(null);
							theIn.setIsOut(false);
							storageRepo.SaveOrUpdate(theIn);
						}

						// 回购订单，取消回购需要删除记录
						storageRepo.PhysicsDelete(storage);
					} else {
						storage.setIsIn(false);
						storage.setLotId(null);
						storage.setCustomerId(null);
						storage.setTradeDate(null);
						storage.setFullNo(null);
						storageRepo.SaveOrUpdate(storage);

						if (storage.getIsMerged()) {
							// 如果是合并记录，则需要将合并记录的原始记录取消关联
							DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
							where.add(Restrictions.eq("MergeId", storage.getId()));
							List<Storage> merges = storageRepo.GetQueryable(Storage.class).where(where).toList();

							for (Storage merge : merges) {
								merge.setIsIn(false);
								merge.setLotId(null);
								merge.setCustomerId(null);
								merge.setTradeDate(null);
								merge.setFullNo(null);
								storageRepo.SaveOrUpdate(merge);
							}

						}
					}
				}

			}

			// 更新批次的QuantityStoraged + IsDelivered标志
			if (lot != null && !StringUtils.isEmpty(lot.getId())) {

				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.eq("IsHidden", false));
				List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess
				// &&
				// lot.QuantityDelivered <= lot.QuantityMore);
				List<Lot> lots = commonService.setDelivery4Lot(lot);
				lotRepository.SaveOrUpdate(lot);
				commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
			}

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.toString());
		}
	}

	@Override
	public ActionResult<String> DeleteMultiStorages(List<Storage> storageList) {

		if (storageList == null || storageList.size() == 0)
			return new ActionResult<>(false, "参数为空");

		try {

			storageRepo.BeginTransaction();

			for (Storage storage : storageList) {

				if (storage.getIsInvoiced())
					throw new Exception("删除的记录中，存在已经收/开票记录，禁止删除！");
				Lot lot = lotRepository.getOneById(storage.getLotId(), Lot.class);

				Storage tmpStorage = storageRepo.getOneById(storage.getId(), Storage.class);
				if (tmpStorage == null)
					throw new Exception("删除的记录中，有记录已不存在！");

				List<Storage> storages = null;

				if (tmpStorage.getIsNoticed()) {

					if (tmpStorage.getMT().equals(MT4Storage.Take) && tmpStorage.getIsOut()) {
						throw new Exception("删除的记录中，存在已经出库的记录，禁止删除！");
					}

					DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
					where.add(Restrictions.eq("NoticeStorageId", tmpStorage.getId()));
					List<Storage> noticeList = storageRepo.GetQueryable(Storage.class).where(where).toList();

					for (Storage storage1 : noticeList) {
						storage1.setNoticeBillNo(null);
						storage1.setNoticeStorageId(null);
						storageRepo.SaveOrUpdate(storage1);
					}

					// #region 如果删除的是bvi的收货

					if (tmpStorage.getLegal().getCode().equals("SB") && tmpStorage.getMT().equals(MT4Storage.Take)
							&& tmpStorage.getIsIn()) {
						// 更新其IsIn的标志
						tmpStorage.setIsIn(false);
						tmpStorage.setLotId(null);
						// 状态被重新设置
						storageRepo.SaveOrUpdate(tmpStorage);

						if (lot != null) {
							where = DetachedCriteria.forClass(Storage.class);
							where.add(Restrictions.eq("LotId", lot.getId()));
							storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

							lot.setQuantityDelivered(new BigDecimal(
									storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
							// lot.IsDelivered = (lot.QuantityDelivered >=
							// lot.QuantityLess &&
							// lot.QuantityDelivered <= lot.QuantityMore);
							List<Lot> lots = commonService.setDelivery4Lot(lot);
							lotRepository.SaveOrUpdate(lot);

							commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
						}
						continue;
					}

					if (tmpStorage.getIsSettled() || tmpStorage.getIsAccounted())
						throw new Exception("删除的记录中，存在已经结算、或者过账的交付记录！");

					// 检查是否存在拆分出来的记录
					where = DetachedCriteria.forClass(Storage.class);
					where.add(Restrictions.eq("SourceId", tmpStorage.getId()));
					Storage split = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

					if (split != null)
						throw new Exception("删除的记录中，存在被拆分出来的记录，请先删除被拆分出来的记录！");

					// 如果已经出库，则禁止删除
					if (tmpStorage.getMT().equals(MT4Storage.Take) && tmpStorage.getIsOut())
						throw new Exception("删除的记录中，存在已经出库的记录，禁止删除！");

					// 如果是合并的记录
					if (tmpStorage.getIsMerged()) {
						where = DetachedCriteria.forClass(Storage.class);
						where.add(Restrictions.eq("MergeId", tmpStorage.getId()));
						List<Storage> merged = storageRepo.GetQueryable(Storage.class).where(where).toList();

						// 更新被合并的那些记录的状态，以后不再显示，并保存其SourceId
						for (Storage v : merged) {
							v.setIsHidden(false);
							v.setMergeId(null);
							storageRepo.SaveOrUpdate(v);
						}
						storageRepo.PhysicsDelete(tmpStorage);

						if (lot != null) {
							where = DetachedCriteria.forClass(Storage.class);
							where.add(Restrictions.eq("LotId", tmpStorage.getLotId()));
							where.add(Restrictions.eq("IsHidden", false));
							storages = storageRepo.GetQueryable(Storage.class).where(where).toList();
							// 更新批次的出入库数量和标志
							lot.setQuantityDelivered(new BigDecimal(
									storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
							// lot.IsDelivered = lot.QuantityDelivered >=
							// lot.QuantityLess &&
							// lot.QuantityDelivered <= lot.QuantityMore;
							List<Lot> lots = commonService.setDelivery4Lot(lot);
							lotRepository.SaveOrUpdate(lot);

							commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
						}

						continue;
					}

					// 根据删除的是入库、还是出库记录
					switch (tmpStorage.getMT()) {
					case MT4Storage.Make:
						Storage counterParty = storageRepo.getOneById(tmpStorage.getCounterpartyId(), Storage.class);
						counterParty.setCounterpartyId(null);
						counterParty.setIsOut(false);
						storageRepo.SaveOrUpdate(counterParty);
						storageRepo.PhysicsDelete(tmpStorage);
						break;
					case MT4Storage.Take:

						// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
						Storage source = storageRepo.getOneById(tmpStorage.getSourceId(), Storage.class);
						if (source != null) {
							source.setBundles(source.getBundles() + tmpStorage.getBundles());
							source.setGrossAtFactory(source.getGrossAtFactory().add(tmpStorage.getGrossAtFactory()));
							source.setQuantityInvoiced(
									source.getQuantityInvoiced().add(tmpStorage.getQuantityInvoiced()));
							source.setQuantity(source.getQuantity().add(tmpStorage.getQuantity()));
							source.setGross(source.getGross().add(tmpStorage.getGross()));
							storageRepo.SaveOrUpdate(source);
						}
						storageRepo.PhysicsDelete(tmpStorage);
						break;
					}

					if (lot != null) {
						where = DetachedCriteria.forClass(Storage.class);
						where.add(Restrictions.eq("IsHidden", false));
						where.add(Restrictions.eq("LotId", tmpStorage.getLotId()));
						storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

						// 更新批次的出入库数量和标志
						lot.setQuantityDelivered(new BigDecimal(
								storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
						// lot.IsDelivered = lot.QuantityDelivered >=
						// lot.QuantityLess &&
						// lot.QuantityDelivered <= lot.QuantityMore;

						List<Lot> lots = commonService.setDelivery4Lot(lot);
						lotRepository.SaveOrUpdate(lot);
						commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
					}
					continue;

				}

				if (tmpStorage.getMT().equals(MT4Storage.Make)) {
					// 更新入库记录的状态
					Storage theIn = storageRepo.getOneById(tmpStorage.getCounterpartyId(), Storage.class);

					if (theIn != null) {
						theIn.setCounterpartyId(null);
						theIn.setIsOut(false);
						storageRepo.SaveOrUpdate(theIn);
					}

					// 删除出库记录
					storageRepo.PhysicsDelete(tmpStorage);

				} else if (tmpStorage.getMT().equals(MT4Storage.Take)) {

					if (tmpStorage.getIsOut())
						throw new Exception("删除的记录中，存在已经出库的记录，禁止删除！");

					// 检查是否存在拆分出来的记录
					DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
					where.add(Restrictions.eq("SourceId", tmpStorage.getId()));
					Storage childrenSplited = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

					if (childrenSplited != null)
						throw new Exception("删除的记录中，存在被拆分出来的记录，请先删除被拆分出来的记录！");

					// 检查是否是合并的记录，如果是，返回到“明细”-“撤消合并”
					where = DetachedCriteria.forClass(Storage.class);
					where.add(Restrictions.eq("MergeId", tmpStorage.getId()));
					List<Storage> a = storageRepo.GetQueryable(Storage.class).where(where).toList();
					if (a.size() > 0)
						throw new Exception("删除的记录中，存在合并的记录，请返回到查看合并的界面，先撤消合并！");

					if (tmpStorage.getIsSplitted()) {
						// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
						Storage source = storageRepo.getOneById(tmpStorage.getSourceId(), Storage.class);
						if (source != null) {
							source.setBundles(source.getBundles() + tmpStorage.getBundles());
							source.setQuantityInvoiced(
									source.getQuantityInvoiced().add(tmpStorage.getQuantityInvoiced()));
							source.setQuantity(source.getQuantity().add(tmpStorage.getQuantity()));
							source.setGross(source.getGross().add(tmpStorage.getGross()));
							source.setGrossAtFactory(source.getGrossAtFactory().add(tmpStorage.getGrossAtFactory()));
							storageRepo.SaveOrUpdate(source);
						}
						storageRepo.PhysicsDelete(tmpStorage);
					} else {
						if (tmpStorage.getIsFactory() && lot != null) // (1)不需要真正删除工厂的商品运输明细，只需重置几个标志值
						{
							tmpStorage.setIsIn(false);
							tmpStorage.setLotId(null);
							tmpStorage.setCustomerId(null);
							tmpStorage.setTradeDate(null);
							tmpStorage.setFullNo(null);
							storageRepo.SaveOrUpdate(tmpStorage);
						} else {
							storageRepo.PhysicsDelete(tmpStorage); // (2)物理删除
						}
					}
				}

				if (lot == null)
					continue;
				// 更新批次的QuantityStoraged + IsDelivered标志
				DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				where.add(Restrictions.eq("IsHidden", false));
				storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess
				// &&
				// lot.QuantityDelivered <= lot.QuantityMore);
				List<Lot> lots1 = commonService.setDelivery4Lot(lot);
				lotRepository.SaveOrUpdate(lot);
				commonService.UpdateDeliveryStatus(lots1);// 更新拆分批次的交货状态
			}

			storageRepo.CommitTransaction();

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> RetrnStorages(List<Storage> storageList) {

		if (storageList == null || storageList.size() == 0)
			return new ActionResult<>(false, "参数为空");

		for (Storage item : storageList) {

			// 商贸收货数据
			Storage smInStorage = storageRepo.getOneById(item.getId(), Storage.class);

			if (smInStorage == null) {
				storageRepo.RollbackTransaction();

				return new ActionResult<>(false, "交付明细不存在");
			}
			smInStorage.setIsDeleted(true);
			smInStorage.setIsReturn(true);
			storageRepo.SaveOrUpdate(smInStorage);

			// BVI出库数据
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.or(Restrictions.eq("Id", smInStorage.getCounterpartyId3()),
					Restrictions.eq("Id", smInStorage.getCounterpartyId2())));
			Storage bviOutStroage = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

			if (bviOutStroage == null) {
				storageRepo.RollbackTransaction();

				return new ActionResult<>(false, "对手发货交付明细不存在");
			}
			bviOutStroage.setIsDeleted(true);
			bviOutStroage.setIsReturn(true);
			storageRepo.SaveOrUpdate(bviOutStroage);

			// BVI入库数据
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("Id", bviOutStroage.getBviSourceId()));
			Storage bvInStroage = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();
			if (bvInStroage == null) {
				storageRepo.RollbackTransaction();

				return new ActionResult<>(false, "BVI收货明细不存在未被退货");
			}
			bvInStroage.setIsOut(false);
			storageRepo.SaveOrUpdate(bvInStroage);
		}

		return new ActionResult<>(true, "退货已完成");

	}

	@Override
	public ActionResult<String> DeleteStorageIns(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getStorages() == null || cpLotStorages.getStorages().size() <= 0) {

			return new ActionResult<>(false, "lot or lot.Storages is null 或者不是一个子记录");
		}

		Lot lot = null;
		if (cpLotStorages.getLotId() != null) {
			lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);
		}
		for (Storage storage : cpLotStorages.getStorages()) {

			if (storage == null)
				continue;
			if (storage.getIsSettled() || storage.getIsAccounted())
				return new ActionResult<>(false, "不允许删除已经结算、或者过账的交付记录。");
			if (storage.getIsInvoiced())
				return new ActionResult<>(false, "已经收/开票，禁止删除");

			// 检查是否是合并的记录，如果是，返回到“明细”-“撤消合并”
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("MergeId", storage.getId()));
			List<Storage> a = storageRepo.GetQueryable(Storage.class).where(where).toList();
			if (a.size() > 0)
				return new ActionResult<>(false, "请返回到查看合并的界面，先撤消合并。");

			// 检查是否存在拆分出来的记录
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("SourceId", storage.getId()));
			List<Storage> childrenSplited = storageRepo.GetQueryable(Storage.class).where(where).toList();
			if (childrenSplited != null && childrenSplited.size() > 0)
				return new ActionResult<>(false, "请先删除被拆分出来的记录");

			// 如果已经出库，则禁止删除
			if (storage.getMT().equals(MT4Storage.Take) && storage.getIsOut())
				return new ActionResult<>(false, "已经出库，禁止删除。");

			if (storage.getIsSplitted()) {
				// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
				Storage source = storageRepo.getOneById(storage.getSourceId(), Storage.class);
				// 如果已经出库，则禁止删除
				if (source.getMT().equals(MT4Storage.Take) && source.getIsOut())
					return new ActionResult<>(false, "拆分源已经出库，禁止删除。");
				if (source != null) {
					source.setBundles(source.getBundles() + storage.getBundles());
					source.setGrossAtFactory(source.getGrossAtFactory().add(storage.getGrossAtFactory()));
					source.setQuantityInvoiced(source.getQuantityInvoiced().add(storage.getQuantityInvoiced()));
					source.setQuantity(source.getQuantity().add(storage.getQuantity()));
					source.setGross(source.getGross().add(storage.getGross()));
					source.setQuantityAtWarehouse(source.getQuantityAtWarehouse().add(storage.getQuantityAtWarehouse()));
					storageRepo.SaveOrUpdate(source);
				}
				storageRepo.PhysicsDelete(storage);
			} else {
				// add by zhu yixin
				// 因为SM有3种类型的收货，区别是否来自工厂的商品运输明细
				if (storage.getIsFactory() && lot != null) // (1)不需要真正删除工厂的商品运输明细，只需重置几个标志值
				{
					storage.setIsIn(false);
					storage.setLotId(null);
					storage.setCustomerId(null);
					storage.setTradeDate(null);
					storage.setFullNo(null);
					storageRepo.SaveOrUpdate(storage);
				} else {
					// 更新来源记录的状态(sm采购bvi的）
					if (storage.getBviSourceId() != null) {
						// 找到sm采购的对应BVI销售出库记录
						where = DetachedCriteria.forClass(Storage.class);
						where.add(Restrictions.eq("BviSourceId", storage.getBviSourceId()));
						where.add(Restrictions.eq("MT", MT4Storage.Make));
						where.add(Restrictions.eq("IsOut", true));
						where.add(Restrictions.eq("CounterpartyId", storage.getBviSourceId()));
						Storage counterparty = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

						if (counterparty != null) {
							counterparty.setIsOut(false);
							storageRepo.SaveOrUpdate(counterparty);
						}
					}
					storageRepo.PhysicsDelete(storage); // (2)物理删除
				}
			}

		}

		// 更新批次的QuantityStoraged + IsDelivered标志
		if (lot != null && !StringUtils.isEmpty(lot.getId())) {

			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
			// lot.QuantityDelivered <= lot.QuantityMore);
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);

			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());

			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
		}

		return new ActionResult<>(true, "删除成功");

	}

	@Override
	public ActionResult<String> CreateStorageOuts(CpLotStorages cpLotStorages, String userId) {

		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null
				|| cpLotStorages.getStorages().size() == 0) {

			return new ActionResult<>(false, "cpLotStorages or cpLotStorages.Storages is null");

		}

		try {

			User user = userHibernateRepository.getOneById(userId, User.class);
			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

			// 检查：累计的交付数量，是否大于批次的QuantityMore。超过时，则提示用户修改、不予处理、直接返回。
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", cpLotStorages.getLotId()));
			List<Storage> list = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal temp_sum = new BigDecimal(
					cpLotStorages.getStorages().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			BigDecimal temp_list_sum = new BigDecimal(
					list.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
				QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed(lot);

				if (quantityMaL != null && quantityMaL.getQuantityMore()
						.compareTo(quantityMaL.getQuantityDeliveryed().add(temp_sum)) < 0) {

					return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");
				}

			} else if (lot.getQuantityMore().compareTo(temp_list_sum.add(temp_sum)) < 0) {
				return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择");
			}

			// 生成BVI仓库的出库记录
			for (Storage s : cpLotStorages.getStorages()) {

				Storage storage = storageRepo.getOneById(s.getId(), Storage.class);

				if (!StringUtils.isEmpty(storage.getId())) {
					storage.setUpdatedAt(new Date());
				} else {
					storage.setCreatedAt(new Date());
				}

				storage.setIsOut(true);

				// #region 构建相应的出库记录
				Storage theOut = new Storage();

				BeanUtils.copyProperties(theOut, storage);

				theOut.setId(null);
				theOut.setBviSourceId(storage.getBviSourceId() == null ? storage.getId() : storage.getBviSourceId());
				theOut.setVersion(0);
				theOut.setMT(MT4Storage.Make);
				theOut.setCounterpartyId(storage.getId());
				theOut.setCounterpartyId3(storage.getId());
				theOut.setLegalId(lot.getLegalId());
				theOut.setContractId(lot.getContractId());
				theOut.setLotId(lot.getId());
				theOut.setCustomerId(lot.getCustomerId());
				theOut.setIsInvoiced(false);
				theOut.setIsOut(false);
				theOut.setPrice(lot.getPrice());
				theOut.setCurrency(lot.getCurrency());
				theOut.setCreatedAt(new Date());
				theOut.setCreatedBy(user.getName());
				theOut.setIsSplitted(false);
				theOut.setSourceId(null);
				theOut.setIsMerged(false);
				theOut.setIsReBuy(lot.getIsReBuy());
				theOut.setTradeDate(new Date());
				theOut.setNoticeStorageId(null);

				commonService.SetStorage(lot, theOut);

				String outid = storageRepo.SaveOrUpdateRetrunId(theOut);

				storage.setCounterpartyId2(outid);
				storageRepo.SaveOrUpdate(storage);// 更新上游信息

			}

			// 重新获取该批次的全部出库记录
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", cpLotStorages.getLotId()));
			List<Storage> storages1 = storageRepo.GetQueryable(Storage.class).where(where).toList();

			// 重新统计批次的“已交付数量 + 交付标志”
			lot.setQuantityDelivered(
					new BigDecimal(storages1.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
			// lot.QuantityDelivered <= lot.QuantityMore);
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);

			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());

			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			// 更新“入库记录”的对手Id
			for (Storage storage : storages1) {
				if (storage.getCounterpartyId() != null) {
					Storage storageIn = storageRepo.getOneById(storage.getCounterpartyId(), Storage.class);
					storageIn.setCounterpartyId(storage.getId());
					storageRepo.SaveOrUpdate(storageIn);
				}
			}

			return new ActionResult<>(true, null);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.toString());
		}

	}

	@Override
	public ActionResult<String> DeleteStorageOuts(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getStorages() == null || cpLotStorages.getStorages().size() <= 0
				|| cpLotStorages.getStorages().get(0) == null) {

			return new ActionResult<>(false, "参数为空");
		}

		try {

			for (Storage theOut : cpLotStorages.getStorages()) {
				// 删除以前，首先更新对应的入库记录的状态
				if (theOut == null)
					continue;

				if (theOut.getIsInvoiced())
					return new ActionResult<>(false, "已经收/开票，禁止删除");

				// 更新入库记录的状态
				Storage theIn = storageRepo.getOneById(theOut.getCounterpartyId(), Storage.class);
				if (theIn != null) {
					theIn.setCounterpartyId(null);
					theIn.setIsOut(false);
					storageRepo.SaveOrUpdate(theIn);
				}

				// 删除出库记录
				storageRepo.PhysicsDelete(theOut);

			}

			// 更新批次的QuantityStoraged + IsDelivered标志
			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			where.add(Restrictions.eq("IsHidden", false));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);
			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());

			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> CopyStorageInsFromBviOuts(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {

			return new ActionResult<>(false, "参数为空");
		}

		try {

			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

			// 检查：累计的交付数量，是否大于批次的QuantityMore。超过时，则提示用户修改、不予处理、直接返回。
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", cpLotStorages.getLotId()));
			List<Storage> list = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal temp_sum = new BigDecimal(
					cpLotStorages.getStorages().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
			BigDecimal list_temp_sum = new BigDecimal(
					list.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
				QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed(lot);

				if (quantityMaL != null && quantityMaL.getQuantityMore()
						.compareTo(quantityMaL.getQuantityDeliveryed().add(temp_sum)) < 0) {

					return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");

				}

			} else if (lot.getQuantityMore().compareTo(list_temp_sum.add(temp_sum)) < 0) {

				return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");
			}

			// 确认已经收货入库，只会对BVI采购自工厂的合同的收货
			for (Storage storage : cpLotStorages.getStorages()) {
				Storage storage1 = storageRepo.getOneById(storage.getId(), Storage.class);
				storage1.setIsOut(true);

				Storage storageIn = new Storage();
				BeanUtils.copyProperties(storageIn, storage);

				storageIn.setMT(MT4Storage.Take);
				storageIn.setId(null);
				storageIn.setCounterpartyId(null);
				storageIn.setCounterpartyId2(null);
				// storageIn.CounterpartyId2 = storage.Id; //收货时记录来源
				storageIn.setCounterpartyId3(storage.getId());
				storageIn.setProjectName2(storage.getProjectName());
				storageIn.setBviSourceId(storage.getBviSourceId());
				storageIn.setLotId(lot.getId());
				storageIn.setContractId(lot.getContractId());
				storageIn.setLegalId(lot.getLegalId());
				storageIn.setIsInvoiced(false);
				storageIn.setIsIn(true);
				storageIn.setIsOut(false);
				storageIn.setCustomerId(lot.getCustomerId());
				storageIn.setTradeDate(new Date());

				commonService.SetStorage(lot, storageIn);

				String InId = storageRepo.SaveOrUpdateRetrunId(storageIn);

				storage1.setCounterpartyId2(InId);
				storageRepo.SaveOrUpdate(storage1); // 更新bvi出库记录状态为已使用

			}

			// 更新批次的IsDelivered标志
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			/*
			 * lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
			 * lot.QuantityDelivered <= lot.QuantityMore);
			 */
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);

			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());
			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> CopyStorageInsFromOuts(CpLotStorages cpLotStorages) {

		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {

			return new ActionResult<>(false, "参数为空");
		}

		try {

			Lot lot = lotRepository.getOneById(cpLotStorages.getLotId(), Lot.class);

			// 检查：累计的交付数量，是否大于批次的QuantityMore。超过时，则提示用户修改、不予处理、直接返回。
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", cpLotStorages.getLotId()));
			List<Storage> list = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal temp_sum = new BigDecimal(
					cpLotStorages.getStorages().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			BigDecimal list_temp_sum = new BigDecimal(
					list.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
				QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed(lot);

				if (quantityMaL != null && quantityMaL.getQuantityMore()
						.compareTo(quantityMaL.getQuantityDeliveryed().add(temp_sum)) < 0) {

					return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择");
				}

			} else if (lot.getQuantityMore().compareTo(list_temp_sum.add(temp_sum)) < 0) {
				return new ActionResult<>(false, "交付数量超过合同数量，请重新输入或选择。");
			}

			// 确认已经收货入库，只会对BVI采购自工厂的合同的收货
			for (Storage storage : cpLotStorages.getStorages()) {
				Storage storage1 = storageRepo.getOneById(storage.getId(), Storage.class);
				storage1.setIsOut(true);

				Storage storageIn = new Storage();

				BeanUtils.copyProperties(storageIn, storage);
				storageIn.setMT(MT4Storage.Take);
				storageIn.setId(null);
				storageIn.setCounterpartyId(storage1.getId());
				storageIn.setCounterpartyId2(null);
				// storageIn.CounterpartyId2 = storage.Id; //收货时记录来源
				storageIn.setCounterpartyId3(storage.getId());
				storageIn.setProjectName2(storage.getProjectName());
				storageIn.setBviSourceId(storage.getBviSourceId());
				storageIn.setLotId(lot.getId());
				storageIn.setContractId(lot.getContractId());
				storageIn.setLegalId(lot.getLegalId());
				storageIn.setIsInvoiced(false);
				storageIn.setIsIn(true);
				storageIn.setIsOut(false);
				storageIn.setIsReBuy(storage.getIsReBuy());
				storageIn.setCustomerId(lot.getCustomerId());
				storageIn.setTradeDate(new Date());

				commonService.SetStorage(lot, storageIn);

				String InId = storageRepo.SaveOrUpdateRetrunId(storageIn);
				storage1.setCounterpartyId2(InId);
				storageRepo.SaveOrUpdate(storage1); // 更新bvi出库记录状态为已使用

			}

			// 更新批次的IsDelivered标志
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess &&
			// lot.QuantityDelivered <= lot.QuantityMore);
			List<Lot> lots = commonService.setDelivery4Lot(lot);
			lotRepository.SaveOrUpdate(lot);

			// 更新批次价格及余额
			commonService.UpdateLotPriceByLotId(lot.getId());
			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> CreateStorageInAndOut(Storage storage) {

		if (storage == null || storage.getQuantity().compareTo(BigDecimal.ZERO) <= 0
				|| (!storage.getMT().equals(MT4Storage.Make) && !storage.getMT().equals(MT4Storage.Take))) {

			return new ActionResult<>(false, "参数为空");
		}

		if (storage.getMT().equals(MT4Storage.Take))
			return CreateStorageIn(storage);

		return CreateStorageOut(storage);
	}

	private ActionResult<String> CreateStorageOut(Storage storage) {
		// #region 检查数据有效性

		if (storage == null || storage.getLotId() == null || storage.getQuantity().compareTo(BigDecimal.ZERO) <= 0
				|| !storage.getMT().equals(MT4Storage.Make) || storage.getCounterpartyId3() == null) {

			return new ActionResult<>(false, "参数为空");
		}

		Lot lot = lotRepository.getOneById(storage.getLotId(), Lot.class);

		if (lot == null)
			return new ActionResult<>(false, "Lot is null");

		if (!StringUtils.isEmpty(storage.getId())) {
			Storage exist = storageRepo.getOneById(storage.getId(), Storage.class);
			if (exist == null)
				return new ActionResult<>(false, "storage is null");
		}

		// 检查：累计的交付数量，是否，大于，合同数量
		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", storage.getLotId()));
		where.add(Restrictions.neOrIsNotNull("Id", storage.getId()));

		List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

		BigDecimal sumQuantityNotionalStoraged = storage.getQuantity()
				.add(new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));

		// if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
		// QuantityMaL quantityMaL =
		// commonService.CalculateQuantityOfLotDeliveryed(lot);
		//
		// if (!StringUtils.isEmpty(storage.getId())) {
		// Storage oldStorage = storageRepo.getOneById(storage.getId(),
		// Storage.class);
		// quantityMaL.setQuantityDeliveryed(quantityMaL.getQuantityDeliveryed().subtract(oldStorage.getQuantity()));
		// }
		//
		// if (quantityMaL != null &&
		// quantityMaL.getQuantityMore().compareTo(storage.getQuantity().add(quantityMaL.getQuantityDeliveryed()))
		// < 0) {
		//
		// return new ActionResult<>(false, "交付数量超过合同数量，请重新输入。");
		// }
		//
		// } else {
		if (storage.getLot().getQuantityMore().compareTo(sumQuantityNotionalStoraged) < 0) {

			return new ActionResult<>(false, "交付数量超过合同数量，请重新输入。");
		}
		// }

		try {
			Storage counterparty = storageRepo.getOneById(storage.getCounterpartyId3(), Storage.class);

			if (counterparty != null) {
				counterparty.setIsOut(false);
				counterparty.setCounterpartyId2(storage.getId());
				storageRepo.SaveOrUpdate(counterparty);
			}

			// 更新批次的出入库数量和标志
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", storage.getLotId()));
			storages = storageRepo.GetQueryable(Storage.class).where(where).toList();
			lot.setQuantityDelivered(
					new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
			// lot.IsDelivered = sumQuantityNotionalStoraged >= lot.QuantityLess
			// &&
			// sumQuantityNotionalStoraged <= lot.QuantityMore;
			List<Lot> lots = commonService.setDelivery4Lot(lot);

			lotRepository.SaveOrUpdate(lot);

			commonService.SetStorage(lot, storage);

			storageRepo.SaveOrUpdate(storage);

			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());
			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);
		} catch (Exception ex) {
			logger.error(ex);

			return new ActionResult<>(false, ex.getMessage());
		}

	}

	private ActionResult<String> CreateStorageIn(Storage storage) {
		try {
			// #region 检查数据有效性
			if (storage == null) {
				return new ActionResult<>(false, "参数错误：: storage");
			}
			if (storage.getQuantity().compareTo(BigDecimal.ZERO) <= 0 || !storage.getMT().equals(MT4Storage.Take)) {
				return new ActionResult<>(false, "参数错误");
			}

			Lot lot = lotRepository.getOneById(storage.getLotId(), Lot.class);
			if (lot == null) {
				return new ActionResult<>(false, "参数错误：lot");
			}
			if (!StringUtils.isEmpty(storage.getId())) {
				Storage exist = storageRepo.getOneById(storage.getId(), Storage.class);
				if (exist == null) {
					return new ActionResult<>(false, "storage is null where id=" + storage.getId());
				}
			}

			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", storage.getLotId()));
			where.add(Restrictions.neOrIsNotNull("Id", storage.getId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal sumQuantityNotionalStoraged = storage.getQuantity()
					.add(new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));

			// if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
			// QuantityMaL quantityMaL =
			// commonService.CalculateQuantityOfLotDeliveryed(lot);
			// if (!StringUtils.isEmpty(storage.getId())) {
			// Storage oldStorage = storageRepo.getOneById(storage.getId(),
			// Storage.class);
			// quantityMaL.setQuantityDeliveryed(quantityMaL.getQuantityDeliveryed().subtract(oldStorage.getQuantity()));
			// }
			//
			//
			// if (quantityMaL != null &&
			// quantityMaL.getQuantityMore().compareTo(quantityMaL.getQuantityDeliveryed().add(storage.getQuantity()))
			// < 0) {
			//
			// return new ActionResult<>(false, "交付数量超过合同数量，请重新输入。");
			// }
			//
			// } else {
			// 检查：累计的交付数量，是否，大于，合同数量
			if (lot.getQuantityMore().compareTo(sumQuantityNotionalStoraged) < 0) {

				return new ActionResult<>(false, "交付数量超过合同数量，请重新输入。");
			}
			// }

			if (StringUtils.isEmpty(storage.getId())) {
				storage.setIsIn(true);
				storage.setIsOut(false);
				storage.setCounterpartyId(null);
			}

			commonService.SetStorage(lot, storage);

			storageRepo.SaveOrUpdate(storage);

			// 更新批次的出入库数量和标志
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", storage.getLotId()));
			storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			BigDecimal quantityDelivered  = BigDecimal.ZERO;
			for(Storage tStorage : storages)
			{
				quantityDelivered = quantityDelivered.add(tStorage.getQuantity());
			}
			
			
			lot.setQuantityDelivered(quantityDelivered);
			/*
			 * lot.IsDelivered = sumQuantityNotionalStoraged >= lot.QuantityLess
			 * && sumQuantityNotionalStoraged <= lot.QuantityMore;
			 */
			List<Lot> lots = commonService.setDelivery4Lot(lot);

			lotRepository.SaveOrUpdate(lot);

			// 更新价格
			commonService.UpdateLotPriceByLotId(lot.getId());
			commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, ex.getMessage());
		}
	}

	@Override
	public ActionResult<String> DeleteStorageById(String storageId) {

		Storage storage = storageRepo.getOneById(storageId, Storage.class);

		if (storage == null)
			throw new RuntimeException("当前记录已经不存在");

		if (storage.getMT().equals(MT4Storage.Take) && storage.getIsOut()) {
			throw new RuntimeException("已经出库，禁止删除");
		}

		if (storage.getIsInvoiced())
			throw new RuntimeException("已经收/开票，禁止删除");

		return DeleteStorage(storage);

	}
	
	@Override
	public ActionResult<String> DeleteStorageFeeById(String storageFeeId)
	{
		StorageFee storageFee = storageFeeRepo.getOneById(storageFeeId, StorageFee.class);
		if (storageFee == null)
			throw new RuntimeException("当前记录已经不存在");
		
		DetachedCriteria where = DetachedCriteria.forClass(StorageFeeDetail.class);
		where.add(Restrictions.eq("StorageFeeId", storageFee.getId()));
		List<StorageFeeDetail> storageFeeDetails = storageFeeDetailRepo.GetQueryable(StorageFeeDetail.class).where(where).toList();
		
		storageFeeDetails.forEach(storageFeeDetailRepo::PhysicsDelete);
		
		storageFeeRepo.PhysicsDelete(storageFee);

		return new ActionResult<>(true, "删除成功");
	}
	
	@Override
	public ActionResult<List<StorageFeeDetail>> GetStorageFeeDetailById(String storageFeeId)
	{
		DetachedCriteria where = DetachedCriteria.forClass(StorageFeeDetail.class);
		where.add(Restrictions.eq("StorageFeeId", storageFeeId));
		List<StorageFeeDetail> storageFeeDetails = storageFeeDetailRepo.GetQueryable(StorageFeeDetail.class).where(where).toList();
		
		for(StorageFeeDetail storageFeeDetail:storageFeeDetails)
		{
			storageFeeDetail.getStorage().setCommodityName(storageFeeDetail.getStorage().getCommodity().getName());
		}
		return new ActionResult<>(true,"",storageFeeDetails);
	}

	@Override
	public ActionResult<String> DeleteStorage(Storage storage) {

		storage = storageRepo.getOneById(storage.getId(), Storage.class);

		if (storage == null)
			throw new RuntimeException("storage is null");

		if (storage.getIsInvoiced())
			throw new RuntimeException("已经收/开票，禁止删除");

		// #region 删除通知货量对应的交付明细
		Hibernate.initialize(storage.getLegal());
		Hibernate.initialize(storage.getMT());
		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("NoticeStorageId", storage.getId()));
		List<Storage> noticeList = storageRepo.GetQueryable(Storage.class).where(where).toList();

		for (Storage storage1 : noticeList) {
			storage1.setNoticeBillNo(null);
			storage1.setNoticeStorageId(null);
			storageRepo.SaveOrUpdate(storage1);
		}
		Lot lot = null;
		if (storage != null && storage.getLotId() != null) {
			lot = lotRepository.getOneById(storage.getLotId(), Lot.class);
		}

		// #region 如果删除的是bvi的收货
		if (storage.getLegal().getCode().equals("SB") && storage.getMT().equals(MT4Storage.Take)
				&& storage.getIsIn() == true) {
			// 更新其IsIn的标志
			storage.setIsIn(false);
			storage.setLotId(null);
			// 状态被重新设置
			storageRepo.SaveOrUpdate(storage);

			// 更新批次的状态
			if (storage != null && storage.getLotId() != null) {
				lot = lotRepository.getOneById(lot.getId(), Lot.class);

				where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("LotId", lot.getId()));
				List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess
				// &&
				// lot.QuantityDelivered <= lot.QuantityMore);
				List<Lot> lots = commonService.setDelivery4Lot(lot);
				lotRepository.SaveOrUpdate(lot);
				commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
			}
			return new ActionResult<>(true, "删除成功");

		}

		if (storage.getIsSettled() || storage.getIsAccounted())
			throw new RuntimeException("不允许删除已经结算、或者过账的交付记录");

		// 检查是否存在拆分出来的记录
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("SourceId", storage.getId()));
		Storage split = storageRepo.GetQueryable(Storage.class).where(where).firstOrDefault();

		if (split != null)
			throw new RuntimeException("请先删除被拆分出来的记录");

		// 如果已经出库，则禁止删除
		if (storage.getMT().equals(MT4Storage.Take) && storage.getIsOut())
			throw new RuntimeException("已经出库，禁止删除");

		// 如果是合并的记录
		if (storage.getIsMerged()) {
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("MergeId", storage.getId()));
			List<Storage> merged = storageRepo.GetQueryable(Storage.class).where(where).toList();

			// 更新被合并的那些记录的状态，以后不再显示，并保存其SourceId
			for (Storage v : merged) {
				v.setIsHidden(false);
				v.setMergeId(null);
				storageRepo.SaveOrUpdate(v);
			}
			storageRepo.PhysicsDelete(storage);

			if (lot != null) {
				where = DetachedCriteria.forClass(Storage.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("LotId", storage.getLotId()));
				List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

				// 更新批次的出入库数量和标志
				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsDelivered = lot.QuantityDelivered >= lot.QuantityLess
				// &&
				// lot.QuantityDelivered <= lot.QuantityMore;
				List<Lot> lots = commonService.setDelivery4Lot(lot);
				lotRepository.SaveOrUpdate(lot);

				commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
			}

			return new ActionResult<>(true, "删除成功");
		}
		// 根据删除的是入库、还是出库记录
		switch (storage.getMT()) {
		case MT4Storage.Make:
			Storage counterParty = storageRepo.getOneById(storage.getCounterpartyId(), Storage.class);
			counterParty.setCounterpartyId(null);
			counterParty.setIsOut(false);
			storageRepo.SaveOrUpdate(counterParty);
			storageRepo.PhysicsDelete(storage);
			break;
		case MT4Storage.Take:

			// 检查是否是拆分出来的记录，如果是，则将数量、合并到原来的记录中去
			if (storage != null && storage.getSourceId() != null) {
				Storage source = storageRepo.getOneById(storage.getSourceId(), Storage.class);
				if (source != null) {
					source.setBundles(source.getBundles() + storage.getBundles());
					source.setGrossAtFactory(source.getGrossAtFactory().add(storage.getGrossAtFactory()));
					source.setQuantityInvoiced(source.getQuantityInvoiced().add(storage.getQuantityInvoiced()));
					source.setQuantity(source.getQuantity().add(storage.getQuantity()));
					source.setGross(source.getGross().add(storage.getGross()));
					storageRepo.SaveOrUpdate(source);
				}
			}
			storageRepo.PhysicsDelete(storage);
			break;
		}
		if (storage != null && storage.getLotId() != null) {
			lot = lotRepository.getOneById(storage.getLotId(), Lot.class);
		}
		if (lot != null) {
			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("LotId", storage.getLotId()));
			List<Storage> storages = storageRepo.GetQueryable(Storage.class).where(where).toList();

			// 更新批次的出入库数量和标志
			if (lot != null) {
				lot.setQuantityDelivered(
						new BigDecimal(storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsDelivered = lot.QuantityDelivered >= lot.QuantityLess
				// &&
				// lot.QuantityDelivered <= lot.QuantityMore;
				List<Lot> lots = commonService.setDelivery4Lot(lot);

				commonService.UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态
			}
		}

		return new ActionResult<>(true, "删除成功");

	}

	@Override
	public ActionResult<String> AmendNonKeyInfo(List<Storage> storages) {

		if (storages == null || storages.size() == 0)
			return new ActionResult<>(false, "参数为空");		
		
		storages.forEach(storageRepo::SaveOrUpdate);

		return new ActionResult<>(true, "修改成功");

	}
	
	@Override
	public ActionResult<String> ChangeWarehouse(List<MoveOrderParam> moveOrderParams) {

		if (moveOrderParams == null || moveOrderParams.size() == 0)
			return new ActionResult<>(false, "参数为空");		
		
		for(MoveOrderParam moveOrderParam:moveOrderParams)
		{
			moveOrderHibernateRepository.SaveOrUpdate(moveOrderParam.getMoveOrder());
			storageRepo.SaveOrUpdate(moveOrderParam.getStorage());
		}

		return new ActionResult<>(true, "修改成功");

	}
	
	@Override
	public List<Storage> GetByNoticeStorageId(String noticeId) {

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("NoticeStorageId", noticeId));

		return storageRepo.GetQueryable(Storage.class).where(where).toList();

	}

	@Override
	public List<Storage> Storages(Criteria param, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {

		return this.repository.GetPage(param, pageSize, pageIndex, sortBy, orderBy, total).getData();

	}

	@Override
	public List<Lot> LotsById(String lotId) {

		return lotService.LotsById(lotId).getData();
	}

	/**
	 * 发货
	 * 
	 * @param lotId
	 *            关联的发货的批次Id
	 * @param storages
	 *            要发货的运输明细
	 * @return 新生成的运输明细
	 */
	@Override
	public ActionResult<List<Storage>> GoodsDeliver(String lotId, List<Storage> storages) {
		List<Storage> returnStorages = new ArrayList<>();
		for (Storage storage : storages) {
			// 1、更新来源的Isborrow(是否借用)为false
			if (storage.getCounterpartyId3() == null) {
				return new ActionResult<>(false, "当前商品明细无来源ID" + storage.getId());
			}
			Storage sourceStorage = GetById(storage.getCounterpartyId3()).getData();
			if (sourceStorage == null) {
				return new ActionResult<>(false, "获取销售订单关联货物的来源时失败");
			}
			sourceStorage.setIsBorrow(false);
			Save(sourceStorage);
			// 2、新增明细，来源为当前明细Id，批次为当前批次，其他与上一条相同
			Storage newStorage = com.smm.ctrm.util.BeanUtils.copy(storage);
			newStorage.setId(null);
			newStorage.setLotId(lotId);
			newStorage.setCounterpartyId3(storage.getId());
			newStorage.setRefId(null);
			newStorage.setRefName(null);
			newStorage.setIsIn(true);
			newStorage.setIsBorrow(false);
			newStorage.setIsInvoiced(false);
			String newStorageId = Save(newStorage).getData();
			// 3、更新当前的IsBorrow为false，IsIn为true，newStorageId
			storage.setIsIn(true);
			storage.setIsBorrow(false);
			storage.setCounterpartyId2(newStorage.getId());
			Save(storage);
			returnStorages.add(newStorage);
		}
		// 因为发货需要审批，故这里不需要更新批次的IsDeliver
		return new ActionResult<>(true, "", returnStorages);
	}

	/**
	 * 收货
	 * 
	 * @param lotId
	 * @param storages
	 * @return
	 */
	@Override
	public ActionResult<List<Storage>> GoodsReceipt(String lotId, List<Storage> storages) {
		List<Storage> returnStorages = new ArrayList<>();
		// 收货
		for (Storage item : storages) {
			item.setMT(MT4Storage.Take);
			item.setLotId(lotId);
			Save(item);
			returnStorages.add(item);
		}
		// 更新批次发货是否完成 IsDeliver
		// 更新批次的收货状态
		Lot lot = lotRepository.getOneById(lotId, Lot.class);
		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage storage : storages) {
			sumQuantity = sumQuantity.add(storage.getQuantity());
		}
		QuantityMaL lotQuantity = commonService.getQuantityMoreorLess(lot.getMoreOrLessBasis(),
				(lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal()),
				lot.getMoreOrLess());
		boolean IsDeliveryed = DecimalUtil.nullToZero(lotQuantity.getQuantityLess()).compareTo(sumQuantity.abs()) <= 0
				&& sumQuantity.abs().compareTo(DecimalUtil.nullToZero(lotQuantity.getQuantityMore())) <= 0;
		lot.setIsDelivered(IsDeliveryed);
		lotRepository.SaveOrUpdate(lot);

		return new ActionResult<>(true, "", returnStorages);
	}

	@Override
	public Criteria GetCriteria2() {
		return this.c2repository.CreateCriteria(C2Storage.class);
	}

	@Override
	public void DailyStoragesHoldingToJson() {
		try {
			StorageParams param = new StorageParams();
			param.setOrderBy("Ascending");
			param.setSortBy("ProjectName");

			List<C2Storage> exchangeStorages = StoragesHolding(param).getData();
			String jsonPath = getStorageHoldingDailyJsonFolderPath(new Date());
			// 验证指定文件夹是否存在
			File file = new File(jsonPath);
			if (!file.exists() && !file.isDirectory()) {
				file.mkdirs();
			}
			File jsonFile = new File(jsonPath + "/" + getStorageHoldingDailyJsonFileName(new Date()));
			if (!jsonFile.exists()) {
				jsonFile.createNewFile();
			}
			StringBuffer sb = new StringBuffer();
			FileOutputStream out = new FileOutputStream(jsonFile, false);
			sb.append(JSONUtil.doConvertObjToString(exchangeStorages));
			out.write(sb.toString().getBytes("utf-8"));// 注意需要转换对应的字符集]
			out.close();
		} catch (IOException e) {
			logger.error("生成库存统计文件失败，" + e.getMessage(), e);
		}
	}

	@Override
	public Long countStorageByProjectName(String ProjectName, String currStorageId) {

		if (org.apache.commons.lang3.StringUtils.isBlank(ProjectName)) {
			return 0L;
		}

		return (Long) this.storageRepo.getHibernateTemplate()
				.findByCriteria(DetachedCriteria.forClass(Storage.class)
						.add(Restrictions.eq("ProjectName", ProjectName))
						.add(Restrictions.neOrIsNotNull("Id", currStorageId)).setProjection(Projections.rowCount()))
				.get(0);
	}

	private String getStorageHoldingDailyJsonFolderPath(Date date) {
		return CTRMOrg.currentOrg().getTemplateFilePath() + "/StoragesHolding/"
				+ DateUtil.getFormatDateToYearMonth(date);
	}

	private String getStorageHoldingDailyJsonFileName(Date date) {
		return DataSourceContextHolder.getDataSourceName() + "_" + DateUtil.getFormatDateToShort(date) + ".json";
	}

	@Override
	public ActionResult<List<C2Storage>> getDailyStoragesHoldingFromJson(StorageParams param) {
		try {

			String jsonPath = getStorageHoldingDailyJsonFolderPath(param.getHistoryDate());
			File jsonFile = new File(jsonPath + "/" + getStorageHoldingDailyJsonFileName(param.getHistoryDate()));
			if (!jsonFile.exists()) {
				return new ActionResult<>(true, "没有查到历史文件数据", new ArrayList<>());
			}
			HibernateAwareObjectMapper mapper = new HibernateAwareObjectMapper();
			List<C2Storage> list = mapper.readValue(new FileInputStream(jsonFile),
					new TypeReference<ArrayList<C2Storage>>() {
					});
			return new ActionResult<>(true, "", list);
		} catch (IOException e) {
			logger.error("获取库存统计文件失败，" + e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<List<C2Storage>> StoragesHolding(StorageParams param) {
		if (param == null) {
			param = new StorageParams();
		}
		Criteria criteria = GetCriteria2();
		/**
		 * 作为当前的库存，默认的参数条件有
		 */
		criteria.add(Restrictions.eq("IsIn", true));
		criteria.add(Restrictions.or(Restrictions.eq("IsOut", null), Restrictions.eq("IsOut", false)));
		criteria.add(Restrictions.eq("MT", MT4Storage.Take));
		if (param.getCommodityIdList() != null && param.getCommodityIdList().size() > 0) {
			criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
		}
		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {

			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);

			Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
					Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
			Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));
			Criterion c = Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
					Restrictions.like("ProjectName", "%" + param.getKeyword() + "%"));
			Criterion d = Restrictions.like("TestNo", "%" + param.getKeyword() + "%");
			criteria.add(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d));
		}

		if (param.getContractId() != null) {
			criteria.add(Restrictions.eq("ContractId", param.getContractId()));
		}
		if (param.getLotId() != null) {
			criteria.add(Restrictions.eq("LotId", param.getLotId()));
		}
		if (param.getLegalId() != null) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}
		if (param.getWarehouseId() != null) {
			criteria.add(Restrictions.eq("WarehouseId", param.getWarehouseId()));
		}
		if (param.getBrandId() != null) {
			criteria.add(Restrictions.eq("BrandId", param.getBrandId()));
		}
		if (!StringUtils.isBlank(param.getTransitStatus())) {
			criteria.add(Restrictions.eq("TransitStatus", param.getTransitStatus()));
		}
		criteria.add(Restrictions.eq("IsHidden", false));
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		RefUtil total = new RefUtil();
		List<C2Storage> storages = this.c2repository.GetPage(criteria, param.getPageSize(),
				param.getPageIndex(), param.getSortBy(), param.getOrderBy(), total).getData();
		List<C2Storage> objs = new ArrayList<C2Storage>();
		if (storages != null && storages.size() > 0) {
			List<DSME> dsmeList = getSMMDsmeList();
			List<SpotPriceEstimate> spotPriceEstimateList = getSpotPriceEstimateList();
			storages = commonService.SimplifyDataStorageHolding(storages, false, spotPriceEstimateList, dsmeList);
			objs.addAll(storages);
		}
		return new ActionResult<>(true, "", objs, total);
	}
	
	private List<SpotPriceEstimate> getSpotPriceEstimateList(){
		Criteria criteria = spotPriceEstimateRepo.CreateCriteria(SpotPriceEstimate.class)
				.addOrder(Order.asc("CommodityId"))
				.addOrder(Order.desc("EstimateDate"));
		return spotPriceEstimateRepo.GetList(criteria);
	}
	
	private List<DSME> getSMMDsmeList(){
		List<Market> marketList = marketRepository.GetList(Market.class);
		String marketId = "";
		for(Market market : marketList) {
			if(market.getCode().equalsIgnoreCase("SMM")) {
				marketId = market.getId();
			}
		}
		if(StringUtils.isNotBlank(marketId)) {
			Criteria criteria = dsmeRepo.CreateCriteria(DSME.class)
					.add(Restrictions.eq("MarketId", marketId))
					.addOrder(Order.asc("CommodityId"))
					.addOrder(Order.desc("TradeDate"));
			return dsmeRepo.GetList(criteria);
		}
		return new ArrayList<>();
	}

	@Override
	public ActionResult<List<C2Storage>> ExchangeStorages(StorageParams param) {

		if (param == null) {
			param = new StorageParams();
		}
		Criteria criteria = GetCriteria2();
		/**
		 * 作为当前的库存，默认的参数条件有
		 */
		criteria.add(Restrictions.eq("IsBorrow", param.getIsBorrow()));
		criteria.add(Restrictions.eq("MT", param.getMT()));

		if (param.getCommodityIdList() != null) {
			criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
		}
		// 货物来源的抬头不等于当前记录的抬头
		// 暂时忽略这个条件
		if (StringUtils.isNotBlank(param.getLegalIds())) {
			String[] ids = param.getLegalIds().trim().split(",");
			criteria.add(Restrictions.in("LegalId", Arrays.asList(ids)));
		}
		// 产品名称
		if (StringUtils.isNotBlank(param.getProductName())) {
			criteria.add(Restrictions.eq("Product", param.getProductName()));
		}
		if (StringUtils.isNotBlank(param.getLegalId())) {
			criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
		}
		if (StringUtils.isNotBlank(param.getSpecId())) {
			criteria.add(Restrictions.eq("SpecId", param.getSpecId()));
		}
		if (param.getCommodityIdList() != null && !param.getCommodityIdList().isEmpty()) {
			criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
		}
		// 关键字
		if (!StringUtils.isBlank(param.getKeyword())) {
			criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
			criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);

			Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
					Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
			Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
					Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));
			Criterion c = Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
					Restrictions.like("ProjectName", "%" + param.getKeyword() + "%"));
			Criterion d = Restrictions.like("TestNo", "%" + param.getKeyword() + "%");

			criteria.add(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d));

		}
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		if (param.getLoadDateEnd() != null) {
			criteria.add(Restrictions.le("LoadDate", param.getLoadDateEnd()));
		}
		if (param.getLoadDateStart() != null) {
			criteria.add(Restrictions.ge("LoadDate", param.getLoadDateStart()));
		}
		criteria.add(Restrictions.eq("IsHidden", false));

		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

		RefUtil total = new RefUtil();

		List<C2Storage> stos = new ArrayList<>();
		List<C2Storage> storages = c2repository.GetPage(criteria, 0, 0, param.getSortBy(), param.getOrderBy(), total)
				.getData();
		List<Legal> legal = this.legalHibernateRepository.GetQueryable(Legal.class).toList();
		//assembly(storages);// 重新组装关联对象
		for (C2Storage item : storages) {
			if (org.apache.commons.lang3.StringUtils.isBlank(item.getCounterpartyId3())) {
				continue;
			}
			// 获取来源
			C2Storage counterParty3Storage = this.c2repository.getOneById(item.getCounterpartyId3(), C2Storage.class);
			if (counterParty3Storage == null) {
				continue;
			}
			// 验证来源的LegalId
			if (org.apache.commons.lang3.StringUtils.isNotBlank(param.getSourceLegalId())) {
				if (!counterParty3Storage.getLegalId().equals(param.getSourceLegalId())) {
					continue;
				}
			}
			// 如果来源的抬头与当前抬头一样则不符合条件
			if (item.getLegalId().equals(counterParty3Storage.getLegalId())) {
				continue;
			}
			// 获取批次
			CLot lot = this.clotRepository.getOneById(item.getLotId(), CLot.class);
			// 如果当前所在的批次的客户是内部客户，则不符合条件
			if (lot == null || (lot.getCustomerId() != null
					&& legal.stream().anyMatch(p -> lot.getCustomerId().equals(p.getCustomerId())))) {
				continue;
			}
			if (item.getRefName().equals(ReceiptShip.class.getSimpleName())
					&& StringUtils.isNotBlank(item.getRefId())) {
				ReceiptShip receiptShip = receiptShipRepo.GetQueryable(ReceiptShip.class)
						.where(DetachedCriteria.forClass(ReceiptShip.class).add(Restrictions.eq("Id", item.getRefId()))
								.add(Restrictions.eq("Flag", ReceiptShip.SHIP))
								.add(Restrictions.ne("Status", Status.Agreed)))
						.firstOrDefault();
				if (receiptShip != null) {
					continue;
				}
			}

			stos.add(item);
		}
		PageModel pageModel = new PageModel(stos, param.getPageSize());

		storages = pageModel.getObjects(param.getPageIndex());

		List<C2Storage> objs = new ArrayList<C2Storage>();
		if (storages != null) {
			storages = commonService.SimplifyDataStorageHolding(storages, false, new ArrayList<>(), new ArrayList<>());
			objs.addAll(storages);
		}

		ActionResult<List<C2Storage>> tempVar = new ActionResult<List<C2Storage>>();
		tempVar.setSuccess(true);
		tempVar.setTotal(objs.size());
		tempVar.setData(objs);
		return tempVar;
	}

	@Override
	public ActionResult<List<Storage>> SpotSaleList(String contractId) {
		List<Storage> storages = new ArrayList<>();
		try {
			DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("ContractId", contractId));
			where.add(Restrictions.eq("IsOut", true));
			where.add(Restrictions.eq("IsInvoiced", false));

			storages = this.repository.GetQueryable(Storage.class).where(where).toList();
			// 获取当前订单下已发货、未开发票的商品明细
			for (Storage item : storages) {
				// 如果目标Id是空则不作处理
				if (item.getCounterpartyId2() == null || item.getCounterpartyId2() == "") {
					break;
				}
				// 如果目标Storage不为空则相关字段赋值
				Storage storage = storageRepo.getOneById(item.getCounterpartyId2(), Storage.class);
				if (storage != null) {
					Lot lot = lotRepository.getOneById(storage.getLotId(), Lot.class);
					if (lot != null) {
						item.setPrice(lot.getPrice());
					}
					Contract contract = contractRepository.getOneById(storage.getContractId(), Contract.class);
					if (contract != null) {
						item.setTradeDate(contract.getTradeDate());
					}
					item.setSaleDate(storage.getTradeDate());
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
		return new ActionResult<>(true, "", storages);
	}

	@Override
	public void clearSession() {
		storageRepo.Clear();
	}

	@Override
	public ActionResult<List<Storage>> SpotSaleListToCustomer(StorageParams param) {
		Criteria criteria = repository.CreateCriteria(Storage.class);
		criteria.add(Restrictions.eq("IsOut", true));
		if (StringUtils.isNotBlank(param.getCustomerId())) {
			criteria.add(Restrictions.eq("CustomerId", param.getCustomerId()));
		}
		if (param.getStartDate() != null) {
			criteria.add(Restrictions.ge("TradeDate", param.getStartDate()));
		}
		if (param.getEndDate() != null) {
			criteria.add(Restrictions.le("TradeDate", param.getEndDate()));
		}
		criteria.add(Restrictions.eq("IsInvoiced", false));
		criteria.add(Restrictions.eq("MT", "T"));
		RefUtil total = new RefUtil();
		List<Storage> storages = this.repository.GetPage(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), total).getData();
		// 获取当前订单下已发货、未开发票的商品明细
		for (Storage item : storages) {
			// 如果目标Id是空则不作处理
			if (item.getCounterpartyId2() == null || item.getCounterpartyId2() == "") {
				break;
			}
			// 如果目标Storage不为空则相关字段赋值
			Storage storage = storageRepo.getOneById(item.getCounterpartyId2(), Storage.class);
			if (storage != null) {
				Lot lot = lotRepository.getOneById(storage.getLotId(), Lot.class);
				if (lot != null) {
					item.setPrice(lot.getPrice());
				}
				Contract contract = contractRepository.getOneById(storage.getContractId(), Contract.class);
				if (contract != null) {
					item.setTradeDate(contract.getTradeDate());
				}
				item.setSaleDate(storage.getTradeDate());
			}
		}
		return new ActionResult<>(true, "", storages, total);
	}

	@Override
	public ActionResult<String> ReceiptWithContractAndLot(List<Storage> storageList) {
		for (Storage storage : storageList) {
			if (countStorageByProjectName(storage.getProjectName(), storage.getId()) > 0) {
				return new ActionResult<>(false, MessageCtrm.DuplicateProjectName);
			}
		}
		Contract contract = generateContractFromStorage(storageList, "B", storageList.get(0).getCustomerId(), null)
				.getData();
		Lot lot = generateLotFromStorage(storageList, contract, storageList.get(0).getPrice());
		for (Storage storage : storageList) {
			storage.setLotId(lot.getId());
			if (storage.getId() != null) {
				storage.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				storage.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			storage.setIsIn(true);
			storageRepo.Save(storage);
		}
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	@Override
	public ActionResult<String> ShipWithContractAndLot(Contract contract) {
		List<Storage> storageList = contract.getLots().get(0).getStorages();
		String customerId = contract.getCustomerId();
		BigDecimal price = contract.getLots().get(0).getPrice();
		for (Storage storage : storageList) {
			if (countStorageByProjectName(storage.getProjectName(), storage.getId()) > 0) {
				return new ActionResult<>(false, MessageCtrm.DuplicateProjectName);
			}
		}

		contract = generateContractFromStorage(storageList, "S", customerId, contract).getData();
		Lot lot = generateLotFromStorage(storageList, contract, price);
		saveStorageForShip(storageList, lot);
		return new ActionResult<>(true, MessageCtrm.SaveSuccess);
	}

	private void saveStorageForShip(List<Storage> storageList, Lot lot) {
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		Map<String, String> idMap = new HashMap<>();
		List<Storage> newStorageList = new ArrayList<>();
		for (Storage storage : storageList) {
			Storage newStorage = new Storage();
			org.springframework.beans.BeanUtils.copyProperties(storage, newStorage);
			newStorageList.add(newStorage);
		}

		for (Storage s : newStorageList) {
			String oldStorageId = s.getId();
			s.setCreatedId(userInfo.getUserId());
			s.setIsOut(Boolean.TRUE);
			s.setIsIn(Boolean.FALSE);
			s.setCounterpartyId3(s.getId());
			s.setBviSourceId(s.getId());
			s.setLotId(lot.getId());
			s.setIsBorrow(!s.getLegalId().equalsIgnoreCase(lot.getLegalId()));
			s.setLegalId(lot.getLegalId());
			s.setSpecId(lot.getSpecId());
			s.setContractId(lot.getContractId());
			s.setMT("M");
			s.setIsInvoiced(Boolean.FALSE);
			s.setProduct(lot.getProduct());
			s.setId(null);
			String newStorageId = storageRepo.SaveOrUpdateRetrunId(s);
			idMap.put(oldStorageId, newStorageId);
		}

		for (Storage s : storageList) {
			s.setIsOut(Boolean.TRUE);
			s.setCounterpartyId2(idMap.get(s.getId()));
			s.setBviSourceId(s.getId());
			s.setIsBorrow(!s.getLegalId().equalsIgnoreCase(lot.getLegalId()));
			storageRepo.SaveOrUpdateRetrunId(s);
		}
	}

	private Lot generateLotFromStorage(List<Storage> storageList, Contract contract, BigDecimal price) {
		BigDecimal totalQuantity = BigDecimal.ZERO;
		List<Brand> brands = new ArrayList<>();
		String brandIds = "";
		String brandNames = "";
		for (Storage storage : storageList) {
			Brand brand = brandRepo.getOneById(storage.getBrandId(), Brand.class);
			if (brand != null) {
				brands.add(brand);
				if (StringUtils.isBlank(brandIds)) {
					brandIds += ",";
				}
				brandIds += brand.getId();
				if (StringUtils.isNotBlank(brandNames)) {
					brandNames += ",";
				}
				brandNames += brand.getName();
			}
			totalQuantity = totalQuantity.add(storage.getQuantity());
		}
		Storage storage = storageList.get(0);
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();

		// Commodity commodity =
		// commoditylHRepos.getOneById(storage.getCommodityId(),
		// Commodity.class);
		// Customer customer = customerRepo.getOneById(storage.getCustomerId(),
		// Customer.class);
		// Legal legal =
		// legalHibernateRepository.getOneById(storage.getLegalId(),
		// Legal.class);
		Lot lot = null;
		if(contract == null || contract.getLots() == null || contract.getLots().get(0) == null) {
			lot = new Lot();
		} else {
			lot = contract.getLots().get(0);
		}
		
		lot.setQuantityBeforeChanged(storage.getQuantity());
		lot.setIsSplitLot(false);
		lot.setIsOriginalLot(false);
		lot.setGrade(BigDecimal.ZERO);
		lot.setDiscount(BigDecimal.ZERO);
		lot.setIsReBuy(false);
		lot.setQuantityOriginal(storage.getQuantity());
		lot.setMoreOrLessBasis("OnPercentage");
		lot.setMoreOrLess(new BigDecimal(2));
		lot.setIsQuantityConfirmed(false);
		lot.setMarkColor(0);
		lot.setIsAllowOverrideContractNo(false);
		lot.setFullNo(contract.getHeadNo() + "10");
		lot.setHeadNo(contract.getHeadNo());
		lot.setLotNo(10);
		lot.setQuantity(totalQuantity);
		QuantityMaL quantityMal = commonService.getQuantityMoreorLess("OnPercentage", totalQuantity, new BigDecimal(2));
		lot.setQuantityLess(quantityMal.getQuantityLess());
		lot.setQuantityMore(quantityMal.getQuantityMore());
		lot.setQuantityDelivered(storage.getQuantity());
		lot.setBrandIds(brandIds);
		lot.setBrandNames(brandNames);
		lot.setSpotDirection(contract.getSpotDirection());
		lot.setStatus(Status.Agreed);
		lot.setIsDelivered(true);
		lot.setIsPriced(true);
		lot.setIsInvoiced(false);
		lot.setIsFeeEliminated(true);
		lot.setMajor(price);
		lot.setPremium(BigDecimal.ZERO);
		lot.setIsEtaPricing(false);
		lot.setMajorType("F");
		lot.setMajorDays(0);
		lot.setPremiumDays(0);
		lot.setPremiumType("F");
		lot.setPremiumBasis("A");
		lot.setBrands(brands);
		lot.setContractId(contract.getId());
		lot.setContract(contract);
		lot.setLegalId(storage.getLegalId());
		lot.setCustomerId(storage.getCustomerId());
		lot.setSpecId(storage.getSpecId());
		lot.setCreatedId(userInfo.getUserId());
		lot.setPrice(lot.getMajor());//固定价，没有升贴水
		lotService.SaveLotOfContractRegular_New(lot);
		return lot;
	}

	private ActionResult<Contract> generateContractFromStorage(List<Storage> storageList, String SpotDirection,
			String customerId, Contract contract) {
		BigDecimal totalQuantity = BigDecimal.ZERO;
		for (Storage storage : storageList) {
			totalQuantity = totalQuantity.add(storage.getQuantity());
		}
		Storage storage = storageList.get(0);
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		if (contract == null) {
			contract = new Contract();
		}
		contract.setIsPriced(Boolean.FALSE);
		contract.setIsInternal(false);
		contract.setIsReBuy(false);
		contract.setIsIniatiated(true);
		contract.setTransactionType("现货");
		contract.setWithHold(false);

		String maxSerialNo = contractService
				.GetMaxSerialNo(storage.getLegalId(), SpotDirection, new Date(), storage.getCommodityId()).getData();

		Legal legal = legalHibernateRepository.getOneById(storage.getLegalId(), Legal.class);
		Commodity commodity = commoditylHRepos.getOneById(storage.getCommodityId(), Commodity.class);
		String prefix = DateUtil.doFormatDate(new Date(), "yy");
		contract.setPrefix(SpotDirection + commodity.getCode() + legal.getCode() + prefix);
		contract.setSerialNo(String.valueOf(maxSerialNo));
		contract.setHeadNo(contract.getPrefix() + maxSerialNo);
		contract.setSpotDirection(SpotDirection);

		contract.setProduct(storage.getProduct());
		contract.setQuantity(totalQuantity);
		contract.setCurrency("CNY");
		contract.setSpotDirection(SpotDirection);
		contract.setSpotType("内贸");
		contract.setDueDays(0);
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		contract.setTradeDate(calendar.getTime());
		contract.setNaQuantity("出厂净重为最终重量");
		contract.setTestOrg("双方协商");
		contract.setPricer("us");
		contract.setIsProvisional(false);
		contract.setStatus(Status.Agreed);
		contract.setIsApproved(false);
		contract.setTraderId(userInfo.getUserId());
		contract.setLegalId(storage.getLegalId());
		contract.setCustomerId(customerId);
		CustomerTitle customerTitle = customerTitleRepo.GetQueryable(CustomerTitle.class).where(DetachedCriteria
				.forClass(CustomerTitle.class).add(Restrictions.eq("CustomerId", storage.getCustomerId())))
				.firstOrDefault();
		if (customerTitle != null) {
			contract.setCustomerTitleId(customerTitle.getId());
		}
		contract.setCommodityId(storage.getCommodityId());
		contract.setCreatedId(userInfo.getUserId());
		contract.setHedgeRatio(new BigDecimal(80));
		return contractService.SaveHeadOfContractRegular(contract);
	}

	

}
