
package com.smm.ctrm.api.Inventory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smm.ctrm.api.BaseApiController;
import com.smm.ctrm.bo.Basis.CustomerService;
import com.smm.ctrm.bo.Common.CheckService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.StorageCommonService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Physical.C2Storage;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.CpLotStorages;
import com.smm.ctrm.domain.Physical.CpMergeStorages;
import com.smm.ctrm.domain.Physical.CpSplitStorage;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.MoveOrderParam;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.StorageFee;
import com.smm.ctrm.domain.Physical.StorageFeeDetail;
import com.smm.ctrm.domain.apiClient.StorageFeeParams;
import com.smm.ctrm.domain.apiClient.StorageParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.MT4Storage;

@Controller
@RequestMapping("api/Inventory/Storage/")
public class StorageApiController extends BaseApiController {

	private Logger logger = Logger.getLogger(this.getClass());

	
	@Resource
	private StorageService storageService;

	@Resource
	private LotService lotService;

	@Resource
	private CommonService commonService;

	@Resource
	private CustomerService customerService;

	@Resource
	StorageCommonService storageCommonService;
	
	@Resource
	private CheckService checkService;

	@Resource
	HibernateRepository<Storage> storageRepo;

	/**
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Holding")
	@ResponseBody
	public ActionResult<List<C2Storage>> Holding(@RequestBody StorageParams param) {
		try {
			if(param.getHistoryDate() != null && !DateUtil.getFormatDateToShort(param.getHistoryDate()).equals(DateUtil.getFormatDateToShort(new Date()))) {
				return storageService.getDailyStoragesHoldingFromJson(param);
			}
			return storageService.StoragesHolding(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 待确认入库的Bvi商品明细
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerFactories")
	@ResponseBody
	public ActionResult<List<Storage>> PagerFactories(@RequestBody(required = false) StorageParams param) {
		try {
			if (param == null) {
				param = new StorageParams();
			}

			Criteria criteria = storageService.GetCriteria();

			// 查询权限
			String userId = LoginHelper.GetLoginInfo().UserId;
			criteria = commonService.AddPermission(userId, criteria, "CreatedId");

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
				Criterion d = Restrictions.or(Restrictions.like("TestNo", "%" + param.getKeyword() + "%"),
						Restrictions.like("NoticeBillNo", "%" + param.getKeyword() + "%"));

				criteria.add(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d));
			}

			if (param.getBrands() != null) {

				List<String> brandIds = param.getBrands().stream().map(s -> s.getId()).collect(Collectors.toList());

				criteria.add(Restrictions.in("BrandId", brandIds));
			}
			if (param.getIsReturn() != null) {
				if (!param.getIsReturn()) {
					criteria.add(Restrictions.or(Restrictions.eq("IsReturn", param.getIsReturn()),
							Restrictions.isNull("IsReturn")));
				} else {
					criteria.add(Restrictions.eq("IsReturn", param.getIsReturn()));
				}
			}
			if (param.getBrandId() != null) {
				criteria.add(Restrictions.eq("BrandId", param.getBrandId()));
			}
			if (param.getHasNoticeId() != null) {
				if (param.getHasNoticeId()) {
					criteria.add(Restrictions.isNotNull("NoticeStorageId"));
				} else {
					criteria.add(Restrictions.isNull("NoticeStorageId"));
				}

			}
			if (param.getIsNoticed() != null) {
				criteria.add(Restrictions.eq("IsNoticed", param.getIsNoticed()));
			}
			if (!StringUtils.isBlank(param.getLegalIds())) {
				/*
				 * var split = param.LegalIds.split("[,]", -1); var arrs =
				 * split.Select(s => new Guid(s)).ToList();
				 */
				String[] splis = param.getLegalIds().split(",");
				criteria.add(Restrictions.in("LegalId", Arrays.asList(splis)));

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

			criteria.add(Restrictions.eq("MT", MT4Storage.Take));
			if (param.getIsIn() != null) {
				if (param.getIsIn() == false) {
					criteria.add(Restrictions.or(Restrictions.eq("IsIn", null), Restrictions.eq("IsIn", false)));
				}
				if (param.getIsIn() == true) {
					criteria.add(Restrictions.eq("IsIn", false));
				}
			}
			if (param.getIsFactory() != null) {
				criteria.add(Restrictions.eq("IsFactory", param.getIsFactory()));
			}
			criteria.add(Restrictions.eq("IsHidden", false));

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);

			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("SourceStoragesById")
	@ResponseBody
	public ActionResult<List<Storage>> SourceStoragesById(@RequestBody String Id) {
		try {
			return storageService.SourceStoragesById(Id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("BviSourceStoragesById")
	@ResponseBody
	public ActionResult<List<Storage>> BviSourceStoragesById(@RequestBody String Id) {
		try {
			return storageService.BviSourceStoragesById(Id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("MergeSourceStoragesById")
	@ResponseBody
	public ActionResult<List<Storage>> MergeSourceStoragesById(@RequestBody String Id) {
		try {
			return storageService.MergeSourceStoragesById(Id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 已经确认入库、可供Bvi出库的商品明细
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerBvi4Out")
	@ResponseBody
	public ActionResult<List<Storage>> PagerBvi4Out(@RequestBody StorageParams param) {
		try {
			if (param == null) {
				param = new StorageParams();
			}
			Criteria criteria = storageService.GetCriteria();

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

				criteria.add(
						Restrictions.or(
								Restrictions.or(
										Restrictions.or(Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"),
												Restrictions.like("ProjectName", "%" + param.getKeyword() + "%")),
										Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
												Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"))),
								Restrictions.like("TestNo", "%" + param.getKeyword() + "%")));
			}

			if (param.getContractId() != null) {
				criteria.add(Restrictions.eq("ContractId", param.getContractId()));
			}

			if (param.getLotId() != null) {
				criteria.add(Restrictions.eq("LotId", param.getLotId()));
			}

			if (param.getBrands() != null) {
				// var brandIds = param.Brands.Select(s => s.Id).ToList();
				List<String> brandIds = param.getBrands().stream().map(s -> s.getId()).collect(Collectors.toList());
				criteria.add(Restrictions.in("BrandId", brandIds));
			}

			if (!StringUtils.isBlank(param.getLegalIds())) {
				String[] split = param.getLegalIds().split(",");
				criteria.add(Restrictions.in("LegalId", Arrays.asList(split)));
			}

			if (param.getLegalId() != null) {
				criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
			}

			if (param.getHasBviSource() != null && !param.getHasBviSource()) {
				criteria.add(Restrictions.isNull("BviSourceId"));
			}

			criteria.add(Restrictions.eq("MT", MT4Storage.Take));
			criteria.add(Restrictions.eq("IsIn", true));
			criteria.add(Restrictions.eq("IsOut", false));
			criteria.add(Restrictions.eq("IsHidden", false));

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);

			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * Bvi已经发给商贸的商品明细
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerBviMaked")
	@ResponseBody
	public ActionResult<List<Storage>> PagerBviMaked(@RequestBody(required = false) StorageParams param) {
		try {
			if (param == null) {
				param = new StorageParams();
			}
			Criteria criteria = storageService.GetCriteria();

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

				criteria.add(
						Restrictions.or(
								Restrictions.or(
										Restrictions.or(Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"),
												Restrictions.like("ProjectName", "%" + param.getKeyword() + "%")),
										Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
												Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"))),
								Restrictions.like("TestNo", "%" + param.getKeyword() + "%")));
			}

			if (param.getBrands() != null) {
				List<String> brandIds = param.getBrands().stream().map(s -> s.getId()).collect(Collectors.toList());
				criteria.add(Restrictions.in("BrandId", brandIds));
			}

			if (param.getContractId() != null) {
				criteria.add(Restrictions.eq("ContractId", param.getContractId()));
			}

			if (param.getLotId() != null) {
				criteria.add(Restrictions.eq("LotId", param.getLotId()));
			}

			if (!StringUtils.isBlank(param.getLegalIds())) {
				String[] split = param.getLegalIds().split(",");
				criteria.add(Restrictions.in("LegalId", Arrays.asList(split)));
			}

			if (param.getLegalIds() != null) {
				criteria.add(Restrictions.eq("LegalId", param.getLegalIds()));
			}

			criteria.add(Restrictions.eq("MT", MT4Storage.Make));
			criteria.add(Restrictions.eq("IsIn", true));
			criteria.add(Restrictions.eq("IsOut", false));
			criteria.add(Restrictions.eq("IsHidden", false));
			if (param.getIsRebuy() != null) {
				criteria.add(Restrictions.eq("IsReBuy", param.getIsRebuy()));
			}

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();

			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);
			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 仍然可以供开票的商品明细，历为商品不可以重复开票
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerAvailable4Invoice")
	@ResponseBody
	public ActionResult<List<Storage>> PagerAvailable4Invoice(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			if (param == null) {
				param = new StorageParams();
			}
			Criteria criteria = storageService.GetCriteria();

			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);

				criteria.add(
						Restrictions.or(
								Restrictions.or(
										Restrictions.or(
												Restrictions.or(
														Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"),
														Restrictions.like("ProjectName",
																"%" + param.getKeyword() + "%")),
												Restrictions.or(
														Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
														Restrictions.like("customer.Name",
																"%" + param.getKeyword() + "%"))),
										Restrictions.like("TestNo", "%" + param.getKeyword() + "%")),
								Restrictions.like("NoticeBillNo", "%" + param.getKeyword() + "%")));
			}

			if (param.getBrands() != null) {
				List<String> brandIds = param.getBrands().stream().map(s -> s.getId()).collect(Collectors.toList());
				criteria.add(Restrictions.in("BrandId", brandIds));
			}

			if (param.getContractId() != null) {
				criteria.add(Restrictions.eq("ContractId", param.getContractId()));
			}

			if (param.getLotId() != null) {
				if (param.getIsContainSplit() != null) // 获取批次信息
				{
					List<Lot> lots = storageService.LotsById(param.getLotId());
					List<String> l1 = lots.stream().map(s -> s.getId()).collect(Collectors.toList());
					criteria.add(Restrictions.in("LotId", l1));
				} else {
					criteria.add(Restrictions.eq("LotId", param.getLotId()));
				}
			}

			if (!StringUtils.isBlank(param.getLegalIds())) {
				String[] split = param.getLegalIds().split(",");
				criteria.add(Restrictions.in("LegalId", Arrays.asList(split)));
			}

			if (param.getLegalId() != null) {
				criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
			}
			// if (param.LotId != null) criteria.add(Restrictions.eq("LotId",
			// param.LotId));

			// 如果是修改发票，则列出已经属于该发票项下的商品、以及还没有invoiceId的其它商品
			if (param.getIsInvoiced() != null) {
				criteria.add(Restrictions.eq("IsInvoiced", param.getIsInvoiced()));
			}
			// 只取通知货量数据
			if (param.getIsNoticed() != null) {
				criteria.add(Restrictions.eq("IsNoticed", param.getIsNoticed()));
			}
			// 强制只列出IsHidden = false的记录。为了配合BVI库存管理的特殊需要。
			// 因为被Merge的记录，其IsHidden被置为false了。
			criteria.add(Restrictions.eq("IsHidden", false));
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);

			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("Pager")
	@ResponseBody
	public ActionResult<List<Storage>> Pager(@RequestBody(required = false) StorageParams param) {
		try {
			return storageService.Pager(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/**
	 * 仓储查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerStorageFee")
	@ResponseBody
	public ActionResult<List<StorageFee>> PagerStorageFee(@RequestBody(required = false) StorageFeeParams param) {
		try {
			return storageService.PagerStorageFee(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("PagerMaked")
	@ResponseBody
	public ActionResult<List<Storage>> PagerMaked(@RequestBody(required = false) StorageParams param) {
		try {
			if (param == null)
				param = new StorageParams();
			Criteria criteria = storageService.GetCriteria();

			if (param.getBrands() != null) {
				List<String> brandIds = new ArrayList<>();
				for (Brand brand : param.getBrands()) {
					brandIds.add(brand.getId());
				}
				criteria.add(Restrictions.in("BrandId", brandIds));
			}
			if (param.getCommodityIdList() != null&&param.getCommodityIdList().size()>0) {
				criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
			}
			criteria.setFetchMode("Lot", FetchMode.JOIN);
			criteria.setFetchMode("Customer", FetchMode.JOIN);
			criteria.setFetchMode("Brand", FetchMode.JOIN);

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

				Criterion e = Restrictions.or(Restrictions.like("TruckNo", "%" + param.getKeyword() + "%"),
						Restrictions.like("CardNo", "%" + param.getKeyword() + "%"));

				criteria.add(Restrictions.or(
						Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d), e),
						Restrictions.like("StorageNo", "%" + param.getKeyword() + "%")));
			}

			if (param.getGoodsSource() != null) {
				if (param.getGoodsSource().trim().equals("")) {
					criteria.add(Restrictions.isNull("GoodsSource"));
				}
			}

			// 强制只列出IsHidden = false的记录。为了配合BVI库存管理的特殊需要。
			// 因为被Merge的记录，其IsHidden被置为false了。
			criteria.add(Restrictions.eq("IsHidden", false));

			if (param.getBrandId() != null)
				criteria.add(Restrictions.eq("BrandId", param.getBrandId()));
			if (param.getIsNoticed() != null) {
				criteria.add(Restrictions.eq("IsNoticed", param.getIsNoticed()));
			}
			if (param.getContractId() != null)
				criteria.add(Restrictions.eq("ContractId", param.getContractId()));

			if (param.getLotId() != null)
				criteria.add(Restrictions.eq("LotId", param.getLotId()));

			if (StringUtils.isNotBlank(param.getLegalIds())) {
				String[] split = param.getLegalIds().split(",");
				List<String> legalIdList = new ArrayList<>();
				for (String id : split) {
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

			if (param.getWarehouseId() != null)
				criteria.add(Restrictions.eq("WarehouseId", param.getWarehouseId()));

			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
			RefUtil total = new RefUtil();
			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);
			if (StringUtils.isNotBlank(param.getCxStatus()) && param.getIsNoticed()) {
				storageCommonService.filterCxStatus(storages, param.getCxStatus());
				total.setTotal(storages.size());
			}
			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 产成品查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("FinishedGoodsPager")
	@ResponseBody
	public ActionResult<List<Storage>> FinishedGoodsPager(HttpServletRequest request,
			@RequestBody StorageParams param) {
		try {
			if (param == null) {
				param = new StorageParams();
			}
			Criteria criteria = storageService.GetCriteria();

			if (param.getBrands() != null) {
				List<String> brandIds = param.getBrands().stream().map(s -> s.getId()).collect(Collectors.toList());
				criteria.add(Restrictions.in("BrandId", brandIds));
			}
			if (param.getCommodityIdList() != null) {
				criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
			}
			// 关键字
			if (!StringUtils.isBlank(param.getKeyword())) {
				criteria.createAlias("Customer", "customer", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Lot", "lot", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Brand", "brand", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("EstCustomer", "esgcustomer", JoinType.LEFT_OUTER_JOIN);
				criteria.createAlias("Lot4EstSale", "lot4estsale", JoinType.LEFT_OUTER_JOIN);

				Criterion a = Restrictions.and(Restrictions.isNotNull("LotId"),
						Restrictions.like("lot.FullNo", "%" + param.getKeyword() + "%"));
				Criterion b = Restrictions.and(Restrictions.isNotNull("CustomerId"),
						Restrictions.like("customer.Name", "%" + param.getKeyword() + "%"));
				Criterion c = Restrictions.or(Restrictions.like("brand.Name", "%" + param.getKeyword() + "%"),
						Restrictions.like("ProjectName", "%" + param.getKeyword() + "%"));
				Criterion d = Restrictions.or(Restrictions.like("TestNo", "%" + param.getKeyword() + "%"),
						Restrictions.like("NoticeBillNo", "%" + param.getKeyword() + "%"));
				Criterion e = Restrictions.and(Restrictions.isNotNull("EstCustomerId"),
						Restrictions.like("esgcustomer.Name", "%" + param.getKeyword() + "%"));
				Criterion f = Restrictions.and(Restrictions.isNotNull("Lot4EstSaleId"),
						Restrictions.like("lot4estsale.FullNo", "%" + param.getKeyword() + "%"));

				criteria.add(Restrictions.or(
						Restrictions.or(
								Restrictions.or(Restrictions.or(Restrictions.or(Restrictions.or(a, b), c), d), e), f),
						Restrictions.like("StorageNo", "%" + param.getKeyword() + "%")));
			}
			if (StringUtils.isNotBlank(param.getGoodsSource())) {
				criteria.add(Restrictions.eq("GoodsSource", param.getGoodsSource()));
			}
			criteria.add(Restrictions.isNull("CounterpartyId3"));

			// 强制只列出IsHidden = false的记录。为了配合BVI库存管理的特殊需要。
			// 因为被Merge的记录，其IsHidden被置为false了。
			criteria.add(Restrictions.eq("IsHidden", false));

			if (param.getIsNoticed() != null) {
				criteria.add(Restrictions.eq("IsNoticed", param.getIsNoticed()));
			}
			if (param.getContractId() != null) {
				criteria.add(Restrictions.eq("ContractId", param.getContractId()));
			}
			if (param.getLotId() != null) {
				criteria.add(Restrictions.eq("LotId", param.getLotId()));
			}
			if (!StringUtils.isBlank(param.getLegalIds())) {
				String[] split = param.getLegalIds().split(",");
				criteria.add(Restrictions.in("LegalId", Arrays.asList(split)));
			}
			if (param.getLegalId() != null) {
				criteria.add(Restrictions.eq("LegalId", param.getLegalId()));
			}
			if (param.getLotId() != null) {
				criteria.add(Restrictions.eq("LotId", param.getLotId()));
			}
			if (!StringUtils.isBlank(param.getTransitStatus())) {
				criteria.add(Restrictions.eq("TransitStatus", param.getTransitStatus()));
			}
			if (!StringUtils.isBlank(param.getMT())) {
				criteria.add(Restrictions.eq("MT", param.getMT()));
			}
			if (param.getIsIn()) {
				criteria.add(Restrictions.eq("IsIn", param.getIsIn()));
			}
			if (param.getIsOut()) {
				criteria.add(Restrictions.eq("IsOut", param.getIsOut()));
			}
			param.setSortBy(commonService.FormatSortBy(param.getSortBy()));

			RefUtil total = new RefUtil();
			List<Storage> storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
					param.getSortBy(), param.getOrderBy(), total);

			if (!param.getCxStatus().equals("") && param.getIsNoticed() == true) {
				storageCommonService.filterCxStatus(storages, param.getCxStatus());
				total.setTotal(storages.size());
			}

			storages = commonService.SimplifyDataStorageList(storages);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setSuccess(true);
			tempVar.setTotal(total.getTotal());
			tempVar.setData(storages);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 基本操作
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("Save")
	@ResponseBody
	public ActionResult<String> Save(@RequestBody Storage storage) {
		try {
			if (storage.getId() != null) {
				storage.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				storage.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			return storageService.Save(storage);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/**
	 * 基本操作
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("SaveStorageFee")
	@ResponseBody
	public ActionResult<String> SaveStorageFee(@RequestBody StorageFee storageFee) {
		try {
			if (storageFee.getId() != null) {
				storageFee.setUpdatedAt(new Date());
				storageFee.setUpdatedBy(LoginHelper.GetLoginInfo().getName());
				storageFee.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				storageFee.setCreatedAt(new Date());
				storageFee.setCreatedBy(LoginHelper.GetLoginInfo().getName());
				storageFee.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			return storageService.SaveStorageFee(storageFee);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("SaveNoticeStorage")
	@ResponseBody
	public ActionResult<Storage> SaveNoticeStorage(@RequestBody Storage storage) {
		try {
			if (storage.getId() != null) {
				storage.setUpdatedAt(new Date());
				storage.setUpdatedBy(LoginHelper.GetLoginInfo().getName());
				storage.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				storage.setCreatedAt(new Date());
				storage.setCreatedBy(LoginHelper.GetLoginInfo().getName());
				storage.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}

			storageService.SaveNoticeStorage(storage);

			ActionResult<Storage> tempVar = new ActionResult<Storage>();
			tempVar.setSuccess(true);
			tempVar.setData(storage);
			tempVar.setMessage(MessageCtrm.SaveSuccess);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	@RequestMapping("SaveFactories")
	@ResponseBody
	public ActionResult<String> SaveFactories(HttpServletRequest request, @RequestBody List<Storage> storages) {
		try {

			return storageService.SaveFactories(storages);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 根据id获取单个实体
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetById")
	@ResponseBody
	public ActionResult<Storage> GetById(@RequestBody String id) {
		try {
			return storageService.GetById(id);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("GetByNoticeStorageId")
	@ResponseBody
	public ActionResult<List<Storage>> GetByNoticeStorageId(@RequestBody String id) {
		try {

			List<Storage> list = storageService.GetByNoticeStorageId(id);
			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setData(list);
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 特殊业务：分拆
	 * 
	 * @param cpSplitStorage
	 * @return
	 */
	@RequestMapping("SplitStorage")
	@ResponseBody
	public ActionResult<String> SplitStorage(@RequestBody CpSplitStorage cpSplitStorage) {
		try {
			String userId = LoginHelper.GetLoginInfo().getUserId();
			return storageService.SplitStorage(cpSplitStorage, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 特殊业务：修改实数
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("ReviseQuantity")
	@ResponseBody
	public ActionResult<String> ReviseQuantity(@RequestBody Storage storage) {
		try {
			return storageService.ReviseQuantity(storage);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/**
	 * 修改实数
	 * 第二种情况，询问用户是否结余库存
	 * 复制一条，重新保存
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("ReviseQuantity2")
	@ResponseBody
	public ActionResult<String> ReviseQuantity2(@RequestBody Storage storage) {
		try {
			return storageService.saveReviseQuantity(storage);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 特殊业务：创建虚拟库存
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("CreateVirtual")
	@ResponseBody
	public ActionResult<String> CreateVirtual(@RequestBody Storage storage) {
		try {
			storageService.CreateVirtual(storage);
			ActionResult<String> tempVar = new ActionResult<>();
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 特殊业务：回补虚拟库存
	 * 
	 * @param targetVirtualStorage
	 * @param storages
	 * @return
	 */
	@RequestMapping("FillBack")
	@ResponseBody
	public ActionResult<String> FillBack(@RequestBody List<Storage> storages,
			@RequestBody Storage targetVirtualStorage) {
		try {
			storageService.FillBack(storages, targetVirtualStorage);
			ActionResult<String> tempVar = new ActionResult<>();
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 导入期初库存
	 * 
	 * @return
	 */
	@RequestMapping("ImportInitStorages")
	@ResponseBody
	public String ImportInitStorages() {
		storageService.ImportInitStorages();
		return null;
	}

	/**
	 * 只返回可用数量大于0的在库列表
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("StoragesAvailable")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesAvailable(@RequestBody StorageParams param) {
		try {
			List<Storage> storages;
			int total = 0;
			if (param != null) {
				Criteria criteria = storageService.GetCriteria();
				criteria.add(Restrictions.eq("MT", MT4Storage.Take));
				criteria.add(Restrictions.eq("IsOut", false));

				RefUtil refUtil = new RefUtil();
				storages = storageService.Storages(criteria, param.getPageSize(), param.getPageIndex(),
						param.getSortBy(), param.getOrderBy(), refUtil);
				total = refUtil.getTotal();
			} else {

				storages = storageService.Storages().getData();
				total = storages.size();
			}

			ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
			tempVar.setData(storages);
			tempVar.setTotal(total);
			tempVar.setSuccess(true);
			return tempVar;
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 返回指定订单的全部交付明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("StoragesByContractId")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesByContractId(@RequestBody String id) {
		return storageService.StoragesByContractId(id);
	}

	/**
	 * 返回指定批次的全部交付明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("StoragesByLotId")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesByLotId(@RequestBody String id) {
		return storageService.StoragesByLotId(id);
	}

	@RequestMapping("StoragesByProjectName")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesByProjectName(HttpServletRequest request,
			@RequestBody String projectName) {
		return storageService.StoragesByProjectName(projectName);
	}

	/**
	 * 获取指定客户的全部出入库明细
	 * 
	 * @param customerId
	 * @return
	 */
	@RequestMapping("StoragesByCustomerId")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesByCustomerId(HttpServletRequest request,
			@RequestBody String customerId) {
		return storageService.StoragesByCustomerId(customerId);
	}

	/**
	 * 获取指定合计费ID的全部出库库明细
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("StoragesBySummaryFeesId")
	@ResponseBody
	public ActionResult<List<Storage>> StoragesBySummaryFeesId(@RequestBody String id) {
		return storageService.StoragesBySummaryFeesId(id);
	}

	/**
	 * 特殊用法：导入Bvi运输动态 --- 批量
	 * 
	 * 客户端根据FullNo 和   仓库名称 分组
	 * @param storage
	 * @return
	 */
	@RequestMapping("ImportFactory")
	@ResponseBody
	public ActionResult<List<Storage>> ImportFactory(@RequestBody List<Storage> storage) {
		try {
			String userId = LoginHelper.GetLoginInfo().getUserId();
			String userName = LoginHelper.GetLoginInfo().getName();
			return storageService.ImportFactory(storage, userName, userId);

		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			return new ActionResult<>(false, e.getMessage());
		}
	}

	/**
	 * 特殊用法：导入Bvi运输动态 --- 批量
	 * 
	 * @param storages
	 * @return
	 */
	@RequestMapping("ImportBvis")
	@ResponseBody
	public ActionResult<String> ImportBvis(@RequestBody List<Storage> storages) {
		String userId = LoginHelper.GetLoginInfo().getUserId();
		String userName = LoginHelper.GetLoginInfo().getName();
		return storageService.ImportBvis(storages, userId, userName);
	}

	/**
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("MergeBvis")
	@ResponseBody
	public ActionResult<String> MergeBvis(@RequestBody CpMergeStorages storage) {
		try {
			if (storage.getMerge().getId() != null) {
				storage.getMerge().setUpdatedAt(new Date());
				storage.getMerge().setUpdatedBy(LoginHelper.GetLoginInfo().getName());
				storage.getMerge().setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
			} else {
				storage.getMerge().setCreatedAt(new Date());
				storage.getMerge().setCreatedBy(LoginHelper.GetLoginInfo().getAccount());
				storage.getMerge().setCreatedId(LoginHelper.GetLoginInfo().getUserId());
			}
			String userId = LoginHelper.GetLoginInfo().getUserId();
			return storageService.MergeBvis(storage, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("MergeCancel")
	@ResponseBody
	public ActionResult<String> MergeCancel(@RequestBody String id) {
		try {
			String userId = LoginHelper.GetLoginInfo().getUserId();
			return storageService.MergeCancel(id, userId);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}

	/**
	 * 特殊用法：SM或BVI从工厂购入，从工厂的商品运输明细中选择收到的货物
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("TakeStoragesFromFactory")
	@ResponseBody
	public ActionResult<String> TakeStoragesFromFactory(HttpServletRequest request,
			@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		return storageService.TakeStoragesFromFactory(cpLotStorages);
	}

	/**
	 * 特殊用于商贸：从BVI发货给商贸的记录中,批量地拷贝,成为商贸的收货记录
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("CopyStorageInsFromBviOuts")
	@ResponseBody
	public ActionResult<String> CopyStorageInsFromBviOuts(HttpServletRequest request,
			@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		return storageService.CopyStorageInsFromBviOuts(cpLotStorages);
	}

	/**
	 * 回购收货
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("CopyStorageInsFromOuts")
	@ResponseBody
	public ActionResult<String> CopyStorageInsFromOuts(HttpServletRequest request,
			@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null || cpLotStorages.getLotId() == null || cpLotStorages.getStorages() == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}

		return storageService.CopyStorageInsFromOuts(cpLotStorages);
	}

	/**
	 * 特殊用于工厂：批量删除来自工厂的入库记录
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("DeleteFactoryIns")
	@ResponseBody
	public ActionResult<String> DeleteFactoryIns(@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(MessageCtrm.ParamError);
			return tempVar;
		}
		return storageService.DeleteFactoryIns(cpLotStorages);
	}

	/**
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("RemoveFactoryIns")
	@ResponseBody
	public ActionResult<String> RemoveFactoryIns(@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}
		return storageService.RemoveFactoryIns(cpLotStorages);
	}

	/**
	 * 标准方法：单个地收货或发货
	 * 
	 * @param storage
	 * @return
	 */
	@RequestMapping("CreateStorageInAndOut")
	@ResponseBody
	public ActionResult<String> CreateStorageInAndOut(@RequestBody Storage storage) {
		if (storage == null || (!storage.getMT().equals(MT4Storage.Take) && !storage.getMT().equals(MT4Storage.Make))) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage(MessageCtrm.ParamError);
			return tempVar;
		}

		if (storage.getId() != null) {
			storage.setUpdatedAt(new Date());
			storage.setUpdatedBy(LoginHelper.GetLoginInfo().getName());
			storage.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
		} else {
			storage.setCreatedAt(new Date());
			storage.setCreatedBy(LoginHelper.GetLoginInfo().getName());
			storage.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
		}

		return storageService.CreateStorageInAndOut(storage);
	}

	/**
	 * 标准方法：删除单个的收货或者发货
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("DeleteStorageById")
	@ResponseBody
	public ActionResult<String> DeleteStorageById(@RequestBody String id) {
		try {
			
			//以下校验在前端和后台已校验过
			/*//判断是否已开票
			List<String> tableList = new ArrayList<>();
			tableList.add(TableNameConst.InvoiceStorage);
			ActionResult<String> checkResult = checkService.deletable(id, "StorageId", tableList);
			if(!checkResult.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}
			//判断是否关联收发货单与产成品
			List<String> tableList2 = new ArrayList<>();
			tableList2.add(TableNameConst.ReceiptShip);
			tableList2.add(TableNameConst.FinishedProduct);
			ActionResult<String> checkResult2 = checkService.deletable(id, "RefId", tableList2);
			if(!checkResult2.isSuccess()) {
				return new ActionResult<>(false, checkResult.getMessage());
			}*/
			
			return storageService.DeleteStorageById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}
	
	@RequestMapping("DeleteStorageFeeById")
	@ResponseBody
	public ActionResult<String> DeleteStorageFeeById(@RequestBody String id) {
		try {
			if(id == null)
				return new ActionResult<>(false, "参数有误");
			
			return storageService.DeleteStorageById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}
	
	@RequestMapping("GetStorageFeeDetailById")
	@ResponseBody
	public ActionResult<List<StorageFeeDetail>> GetStorageFeeDetailById(@RequestBody String id) {
		try {
			if(id == null)
				return new ActionResult<>(false, "参数有误");
			
			return storageService.GetStorageFeeDetailById(id);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}
	
	
	

	@RequestMapping("DeleteStorage")
	@ResponseBody
	public ActionResult<String> DeleteStorage(@RequestBody Storage storage) {
		return storageService.DeleteStorage(storage);
	}

	/**
	 * 标准方法：保存批量地发货、出库 --- BVI销售给SM、以及SM销售给下游，共用这个方法
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("CreateStorageOuts")
	@ResponseBody
	public ActionResult<String> CreateStorageOuts(HttpServletRequest request,
			@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null || cpLotStorages.getStorages() == null || cpLotStorages.getStorages().size() == 0) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("ParamError");
			return tempVar;
		}
		String userId = LoginHelper.GetLoginInfo().getUserId();
		return storageService.CreateStorageOuts(cpLotStorages, userId);
	}

	/**
	 * 标准方法：批量删除收货记录
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("DeleteStorageIns")
	@ResponseBody
	public ActionResult<String> DeleteStorageIns(@RequestBody CpLotStorages cpLotStorages) {
		try {
			if (cpLotStorages == null) {
				ActionResult<String> tempVar = new ActionResult<String>();
				tempVar.setSuccess(false);
				tempVar.setMessage("cpLotStorages参数为空.");
				return tempVar;
			}

			return storageService.DeleteStorageIns(cpLotStorages);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false,e.getMessage());
		}
	}

	/**
	 * 标准方法：批量删除发货记录
	 * 
	 * @param cpLotStorages
	 * @return
	 */
	@RequestMapping("DeleteStorageOuts")
	@ResponseBody
	public ActionResult<String> DeleteStorageOuts(HttpServletRequest request,
			@RequestBody CpLotStorages cpLotStorages) {
		if (cpLotStorages == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("cpLotStorages参数为空.");
			return tempVar;
		}

		return storageService.DeleteStorageOuts(cpLotStorages);
	}

	/**
	 * 批量删除收发货记录
	 * 
	 * @param storageList
	 * @return
	 */
	@RequestMapping("DeleteMultiStorages")
	@ResponseBody
	public ActionResult<String> DeleteMultiStorages(HttpServletRequest request,
			@RequestBody List<Storage> storageList) {
		return storageService.DeleteMultiStorages(storageList);
	}

	/**
	 * 退货
	 * 
	 * @param storageList
	 * @return
	 */
	@RequestMapping("ReturnStorage")
	@ResponseBody
	public ActionResult<String> ReturnStorage(HttpServletRequest request,
			@RequestBody java.util.ArrayList<Storage> storageList) {
		return storageService.RetrnStorages(storageList);
	}

	/**
	 * 修改运输状态信息
	 * 
	 * @param storages
	 * @return
	 */
	@RequestMapping("AmendNonKeyInfo")
	@ResponseBody
	public ActionResult<String> AmendNonKeyInfo(HttpServletRequest request,
			@RequestBody java.util.ArrayList<Storage> storages) {
		try {
			return storageService.AmendNonKeyInfo(storages);
		} catch (Exception e) {
			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}
	
	/**
	 * 修改运输状态信息
	 * 
	 * @param storages
	 * @return
	 */
	@RequestMapping("ChangeWarehouse")
	@ResponseBody
	public ActionResult<String> ChangeWarehouse(HttpServletRequest request,
			@RequestBody java.util.ArrayList<MoveOrderParam> moveOrderParams) {
		try {
			return storageService.ChangeWarehouse(moveOrderParams);
		} catch (Exception e) {
			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("PagerInSide")
	@ResponseBody
	public ActionResult<List<Storage>> PagerInSide(@RequestBody(required = false) StorageParams param) {
		if (param == null) {
			param = new StorageParams();
		}
		Criteria criteria = storageRepo.CreateCriteria(Storage.class);

		if (param.getBrands() != null) {
			List<String> brandIds = new ArrayList<>();
			for (Brand brand : param.getBrands()) {
				brandIds.add(brand.getId());
			}
			if (brandIds.size() > 0) {
				criteria.add(Restrictions.in("BrandId", brandIds));
			}
		}
		if (param.getCommodityIdList() != null && param.getCommodityIdList().size() > 0) {
			criteria.add(Restrictions.in("CommodityId", param.getCommodityIdList()));
		}
		// 权限
		String userid = LoginHelper.GetLoginInfo().UserId;
		criteria = commonService.AddPermission(userid, criteria, "CreatedId");

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
		if (param.getIsBorrow() != null)
			criteria.add(Restrictions.eq("IsBorrow", param.getIsBorrow()));
		param.setSortBy(commonService.FormatSortBy(param.getSortBy()));
		TestTime.addMilestone("参数构造完成。");
		RefUtil ref = new RefUtil();

		List<Storage> storages = storageRepo.GetPage(criteria, param.getPageSize(), param.getPageIndex(),
				param.getSortBy(), param.getOrderBy(), ref).getData();
		int total = ref.getTotal();
		TestTime.addMilestone("数据库查询完成。");
		if (StringUtils.isNotBlank(param.getCxStatus()) && param.getIsNoticed() == true) {
			switch (param.getCxStatus()) {
			case "未冲销": // 未冲销
				List<Storage> tempStorages = storages.stream()
						.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(x.getQuantity()) == 0)
						.collect(Collectors.toList());
				List<Storage> remove = storages.stream().filter(x -> x.getIsNoticed()).collect(Collectors.toList());
				storages.removeAll(remove);
				storages.addAll(tempStorages);
				break;
			case "已冲销未结束": // 已冲销未结束
				List<Storage> tempStorages1 = storages.stream()
						.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(BigDecimal.ZERO) != 0
								&& x.getUnCxQuantity().compareTo(x.getQuantity()) != 0)
						.collect(Collectors.toList());
				List<Storage> remove1 = storages.stream().filter(x -> x.getIsNoticed()).collect(Collectors.toList());
				storages.removeAll(remove1);
				storages.addAll(tempStorages1);
				break;
			case "已冲销结束": // 已冲销结束
				List<Storage> tempStorages2 = storages.stream()
						.filter(x -> x.getIsNoticed() && x.getUnCxQuantity().compareTo(BigDecimal.ZERO) == 0)
						.collect(Collectors.toList());
				List<Storage> remove2 = storages.stream().filter(x -> x.getIsNoticed()).collect(Collectors.toList());
				storages.removeAll(remove2);
				storages.addAll(tempStorages2);
				break;
			}
			total = storages.size();
		}
		if (StringUtils.isNotBlank(param.getCxStatus()) && param.getIsNoticed()) {
			storageCommonService.filterCxStatus(storages, param.getCxStatus());
			total = storages.size();
		}

		TestTime.addMilestone("数据简化之前。");
		storages = commonService.SimplifyDataStorageList(storages);
		TestTime.addMilestone("数据简化完成。");
		ActionResult<List<Storage>> tempVar = new ActionResult<List<Storage>>();
		tempVar.setSuccess(true);
		tempVar.setTotal(total);
		tempVar.setData(storages);
		String testResultStr = TestTime.result();
		logger.info(testResultStr);
		return tempVar;
	}

	/**
	 * 换货库存查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("ExchangeStorage")
	@ResponseBody
	public ActionResult<List<C2Storage>> ExchangeStorage(@RequestBody StorageParams param) {
		try {
			return storageService.ExchangeStorages(param);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	/**
	 * 换货库存查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("SpotSaleList")
	@ResponseBody
	public ActionResult<List<Storage>> SpotSaleList(@RequestBody String contractId) {
		try {
			return storageService.SpotSaleList(contractId);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/**
	 * 换货库存查询
	 * 
	 * @param param
	 * @return
	 */
	@RequestMapping("SpotSaleListToCustomer")
	@ResponseBody
	public ActionResult<List<Storage>> SpotSaleListToCustomer(@RequestBody StorageParams params) {
		try {
			return storageService.SpotSaleListToCustomer(params);

		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/** 
	 * 收货单直接生成合同订单
	 * @param storage
	 * @return
	 */
	@RequestMapping("ReceiptWithContractAndLot")
	@ResponseBody
	public ActionResult<String> ReceiptWithContractAndLot(@RequestBody Storage storage) {
		try {
			List<Storage> storageList = new ArrayList<>();
			storageList.add(storage);
			return storageService.ReceiptWithContractAndLot(storageList);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
	
	/** 
	 * 发货单直接生成合同订单
	 * @param storage
	 * @return
	 */
	@RequestMapping("ShipWithContractAndLot")
	@ResponseBody
	public ActionResult<String> ShipWithContractAndLot(@RequestBody Contract contract) {
		try {
			return storageService.ShipWithContractAndLot(contract);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(Boolean.FALSE, ex.getMessage());
		}
	}
}