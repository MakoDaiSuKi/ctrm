package com.smm.ctrm.bo.impl.Finance;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.bo.Finance.InvoiceService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.dao.SimpleDao;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Basis.Area;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.SysSequence;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.domain.Physical.CInvoice;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Invoice2Storage;
import com.smm.ctrm.domain.Physical.InvoicePnL;
import com.smm.ctrm.domain.Physical.InvoiceStorage;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Param4InvoicePnL;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.domain.Physical.VFund;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.FeeCode;
import com.smm.ctrm.util.Result.InvoiceType;
import com.smm.ctrm.util.Result.LS;
import com.smm.ctrm.util.Result.MT4Invoice;
import com.smm.ctrm.util.Result.MT4Storage;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

	private static final Logger logger = Logger.getLogger(InvoiceServiceImpl.class);

	@Autowired
	private HibernateRepository<Invoice> invoiceRepo;

	@Autowired
	private HibernateRepository<CInvoice> cInvoiceRepo;

	@Autowired
	private HibernateRepository<Pending> pendingRepository;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<Storage> storageRepo;

	@Autowired
	private HibernateRepository<Fund> fundRepo;

	@Autowired
	private HibernateRepository<Invoice2Storage> invoice2StorageRepo;

	@Autowired
	private HibernateRepository<Contract> contractRepo;

	@Autowired
	private HibernateRepository<InvoicePnL> invoicePnLRepo;

	@Autowired
	private HibernateRepository<Square> squareRepo;

	@Autowired
	private HibernateRepository<SummaryFees> summaryFeesRepo;

	@Autowired
	private HibernateRepository<GlobalSet> globalSetRepo;

	@Autowired
	private HibernateRepository<Market> marketRepo;

	@Autowired
	private HibernateRepository<Position> positionRepo;

	@Autowired
	private HibernateRepository<Customer> customerRepo;

	@Autowired
	private HibernateRepository<Legal> legalRepo;

	@Autowired
	private CommonService commonService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private SimpleDao<InvoiceStorage> invoiceStorageDao;

	@Autowired
	private HibernateRepository<VFund> vFundRepo;

	@Autowired
	private FundService fundService;

	@Override
	public Criteria GetCriteria() {

		return this.invoiceRepo.CreateCriteria(Invoice.class);
	}

	@Override
	public Criteria GetCriteria2() {

		return this.cInvoiceRepo.CreateCriteria(CInvoice.class);
	}

	@Override
	public List<Storage> getStorageListByInvoiceId(String invoiceId) {
		List<InvoiceStorage> invoiceStorageList = invoiceStorageDao
				.getList(DetachedCriteria.forClass(InvoiceStorage.class).add(Restrictions.eq("InvoiceId", invoiceId)));
		List<String> storageIdList = new ArrayList<>();
		for (InvoiceStorage invoiceStorage : invoiceStorageList) {
			storageIdList.add(invoiceStorage.getStorageId());
		}
		if (storageIdList == null || storageIdList.size() == 0) {
			return new ArrayList<>();
		}
		return storageRepo.GetQueryable(Storage.class)
				.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.in("Id", storageIdList))).toList();
	}

	/**
	 * 逻辑说明：首先按发票类型进行不同的处理 1.临时发票：逻辑最简单，直接保存即可 2.正式发票： 先检查是否已经点价， 先更新合同标识，最后保存
	 * 3.临时发票： 需要更新映射表， 根据调整的数量之和，是否等于合同数量，决定合同的开票标识
	 * 如果是更新，同样需要计算被调整的数量之和，是否与合同数量相等
	 */
	@Override
	public ActionResult<Invoice> Save(Invoice invoice) {

		/*
		 * 逻辑说明：首先按发票类型进行不同的处理 1.临时发票：逻辑最简单，直接保存即可 2.正式发票： 先检查是否已经点价，
		 * 先更新合同标识，最后保存 3.临时发票： 需要更新映射表， 根据调整的数量之和，是否等于合同数量，决定合同的开票标识
		 * 如果是更新，同样需要计算被调整的数量之和，是否与合同数量相等
		 */

		if (invoice == null || invoice.getLotId() == null || invoice.getPFA() == null) {

			return new ActionResult<>(false, "发票类型没或者批次未选择");
		}

		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("InvoiceNo", invoice.getInvoiceNo()));
		where.add(Restrictions.neOrIsNotNull("id", invoice.getId()));
		List<Invoice> existInvoice = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		if (existInvoice != null && existInvoice.size() > 0)
			return new ActionResult<>(false, "发票号重复");

		Lot lot = lotRepo.getOneById(invoice.getLotId(), Lot.class);

		if (lot == null)
			return new ActionResult<>(false, "lot is null");

		if (invoice.getPFA().equals("P") && invoice.getAdjustId() != null) {

			return new ActionResult<>(false, "该临时发票已经被调整过，不能修改发票信息!");
		}

		String[] tmpArr = new String[] { "P", "F", "A", "L" };

		List<Invoice> invoices = null;

		if (Arrays.asList(tmpArr).contains(invoice.getPFA())) {
			// #region 发票数量不能大于批次最大数量
			// add by chenyj 2015.07.10
			// 临时发票需要排除被调整的部分,若是调整发票修改，需要排除对应的临时发票
			where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("LotId", invoice.getLotId()));
			where.add(Restrictions.eq("Is4MultiLots", invoice.getIs4MultiLots()));
			where.add(Restrictions.ne("Id", invoice.getId()));
			invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			List<String> contains = new ArrayList<>();

			if (invoice.getProvisionals() != null) {
				contains = invoice.getProvisionals().stream().map(x -> x.getId()).collect(Collectors.toList());
			}

			final List<String> finalContains = contains;
			double temp_sum = invoices.stream().filter(
					x -> x.getPFA().equals("P") && x.getAdjustId() == null && !finalContains.contains(x.getId()))
					.mapToDouble(x -> x.getQuantity().doubleValue()).sum();

			BigDecimal pSummary = new BigDecimal(temp_sum);

			BigDecimal fASummary = new BigDecimal(
					invoices.stream().filter(x -> x.getPFA().equals("F") || x.getPFA().equals("A"))
							.mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			BigDecimal tmpSum = invoice.getQuantity().add(pSummary).add(fASummary);

			QuantityMaL quantityMaL = commonService.CalculateQuantityOfLotDeliveryed_New(lot);

			// if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) {
			// if (tmpSum.compareTo(quantityMaL.getQuantityMore()) > 0) {
			// return new ActionResult<>(false, "发票数量不能大于批次最大数量（溢短装最大范围）！");
			// }
			// } else if (tmpSum.compareTo(lot.getQuantityMore()) > 0) {
			// return new ActionResult<>(false, "发票数量不能大于批次最大数量（溢短装最大范围）！");
			// }

			if (tmpSum.compareTo(lot.getQuantityMore()) > 0) {
				return new ActionResult<>(false, "发票数量不能大于批次最大数量（溢短装最大范围）！");
			}

			// #region 发票批次对应的所有发票交付明细（通知货量） <=批次溢短装（最大）,by harry 20150721

			final List<String> finalContains1 = contains;
			List<Invoice> storages1 = invoices.stream().filter(
					x -> x.getPFA().equals("P") && x.getAdjustId() == null && !finalContains1.contains(x.getId()))
					.collect(Collectors.toList());

			List<Invoice> storages2 = invoices.stream().filter(x -> x.getPFA().equals("F") || x.getPFA().equals("A"))
					.collect(Collectors.toList());

			List<Storage> lstStorages = new ArrayList<>();
			List<Storage> lstNotices = new ArrayList<>();

			for (Invoice invoice1 : storages1) {
				lstStorages.addAll(invoice1.getStorages());
				lstNotices.addAll(invoice1.getNotices());
			}
			for (Invoice invoice2 : storages2) {
				lstStorages.addAll(invoice2.getStorages());
				lstNotices.addAll(invoice2.getNotices());
			}
			BigDecimal StorageSummary1 = new BigDecimal(0);
			for (Storage x : lstStorages) {
				StorageSummary1 = StorageSummary1.add(x.getQuantity());
			}
			BigDecimal NoticeSummary1 = new BigDecimal(0);
			for (Storage x : lstNotices) {
				NoticeSummary1 = NoticeSummary1.add(x.getQuantity());
			}
			BigDecimal tmpSum1 = StorageSummary1;

			for (Storage x : invoice.getStorages()) {
				tmpSum1 = tmpSum1.add(x.getQuantity());
			}
			BigDecimal tmpSum2 = NoticeSummary1;
			for (Storage x : invoice.getNotices()) {
				tmpSum2 = tmpSum2.add(x.getQuantity());
			}

			BigDecimal tmpSum00 = tmpSum1.compareTo(tmpSum2) > 0 ? tmpSum1 : tmpSum2;

			/*
			 * if (lot.getIsSplitted() || lot.getIsSourceOfSplitted()) { if
			 * (tmpSum00.compareTo(quantityMaL.getQuantityMore()) > 0) { return
			 * new ActionResult<>(false, "发票交付明细数量（通知货量）不能大于批次最大数量（溢短装最大范围）！");
			 * } } else if (tmpSum00.compareTo(lot.getQuantityMore()) > 0) {
			 * return new ActionResult<>(false,
			 * "发票交付明细数量（通知货量）不能大于批次最大数量（溢短装最大范围）！"); }
			 */
			if (tmpSum00.compareTo(quantityMaL.getQuantityMore()) > 0) {
				return new ActionResult<>(false, "发票交付明细数量（通知货量）不能大于批次最大数量（溢短装最大范围）！");
			}
		}

		// ---------

		if (StringUtils.isNotBlank(invoice.getId())) {
			where = DetachedCriteria.forClass(VFund.class);
			where.add(Restrictions.eq("InvoiceId", invoice.getId()));
			List<VFund> fund = this.vFundRepo.GetQueryable(VFund.class).where(where).toList();

			if (fund != null && fund.size() > 0) {
				return new ActionResult<>(false, "该发票已经被用于申请付款或资金收付，不允许删除！");
			}
		}

		// -------------
		invoice.setLotId(lot.getId());
		invoice.setContractId(lot.getContractId());
		invoice.setLegalId(lot.getLegalId());
		invoice.setCommodityId(lot.getCommodityId());

		// 如果是修改的状态，先恢复原来的商品明细的IsInvoiced的标志
		if (!StringUtils.isEmpty(invoice.getId())) {
			Invoice old = invoiceRepo.getOneById(invoice.getId(), Invoice.class);

			for (Storage storage : old.getStorages()) {
				if (storage == null)
					continue;
				storage.setIsInvoiced(false);
				storageRepo.SaveOrUpdate(storage);
			}
			for (Storage storage : old.getNotices()) {
				if (storage == null)
					continue;
				storage.setIsInvoiced(false);
				storageRepo.SaveOrUpdate(storage);
			}
		} else {
			// #region yunsq 2016年6月7日 18:59:52 发票保存默认草稿状态
			if (invoice.getPFA().equals(InvoiceType.Misc) || invoice.getMT().equals(MT4Storage.Take)
					|| invoice.getPFA().equals(InvoiceType.Note))
				invoice.setStatus(Status.Agreed);
			else
				invoice.setStatus(Status.Draft);
		}

		List<Storage> ss = new ArrayList<>();

		if (invoice.getStorages() != null) {
			for (Storage storage : invoice.getStorages()) {
				if (storage == null)
					continue;
				Storage t = storageRepo.getOneById(storage.getId(), Storage.class);
				if (t == null)
					continue;

				if (invoice.getPFA().equals(InvoiceType.Provisional)) {
					t.setPriceProvisional(storage.getPrice());
				}
				t.setIsInvoiced(true);
				storageRepo.SaveOrUpdate(t);
				ss.add(t);
			}
		}

		invoice.setStorages(ss);

		// --------
		List<Storage> ss1 = new ArrayList<>();

		if (invoice.getNotices() != null) {
			for (Storage storage : invoice.getNotices()) {
				if (storage == null)
					continue;
				Storage t = storageRepo.getOneById(storage.getId(), Storage.class);
				if (t == null)
					continue;

				if (invoice.getPFA().equals(InvoiceType.Provisional)) {
					t.setPriceProvisional(storage.getPrice());
				}
				t.setIsInvoiced(true);
				storageRepo.SaveOrUpdate(t);
				ss1.add(t);
			}
		}

		invoice.setNotices(ss1);

		// -------------------
		// 如果是调整发票并且金额为0，则自动将IsExecuted标识置1
		if (invoice.getPFA().equals(InvoiceType.Adjust)) {
			where = DetachedCriteria.forClass(Fund.class);
			where.add(Restrictions.eq("InvoiceId", invoice.getId()));
			List<Fund> founds = fundRepo.GetQueryable(Fund.class).where(where).toList();

			if (founds.size() == 0) {

				if (invoice.getAmount().compareTo(BigDecimal.ZERO) == 0) {
					invoice.setIsExecuted(true);
					// 同步更新批次状态
					// CommonService.UpdateLotIsFunded(invoice.LotId);
					lot.setIsFunded(commonService.CanLotIsFunded_New(invoice.getLotId()));
				} else
					invoice.setIsExecuted(false);
			}
		}
		// step 1: 保存发票
		String invoiceId = invoiceRepo.SaveOrUpdateRetrunId(invoice);
		// 确认发票编号
		SysSequence sequence = new SysSequence();
		BigDecimal tmpd = new BigDecimal(invoice.getSerialNo());
		sequence.setIdentityId(tmpd);
		sequence.setCode("invoiceno_" + invoice.getPrefix());
		commonService.ConfirmSequenceIndex(sequence);

		// step 3: 恢复该发票项下的原来的调整发票
		where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("AdjustId", invoiceId));
		List<Invoice> provisionals = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		for (Invoice invoice1 : provisionals) {
			Invoice t = invoiceRepo.getOneById(invoice1.getId(), Invoice.class);
			t.setAdjustId(null);
			invoiceRepo.SaveOrUpdate(t);
		}

		// step 5: 更新发票项下的临时发票的adjustId
		if (invoice.getProvisionals() != null && invoice.getProvisionals().size() > 0) {

			for (Invoice inv : invoice.getProvisionals()) {
				Invoice t = invoiceRepo.getOneById(inv.getId(), Invoice.class);
				// 不允许一个临时发票、被用于2次调整。即，只能被“调整”1次。
				if (inv.getId().equals(t.getAdjustId())) {

					return new ActionResult<>(false,
							String.format("暂定价发票%s已经调整，发生在调整发票%s", t.getInvoiceNo(), inv.getInvoiceNo()));
				}

				t.setAdjustId(invoiceId);
				invoiceRepo.SaveOrUpdate(t);
			}

		}

		// 更新批次的（开或收）发票数量和标志

		where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", invoice.getLotId()));
		where.add(
				Restrictions.or(Restrictions.eq("PFA", InvoiceType.Final), Restrictions.eq("PFA", InvoiceType.Adjust)));
		invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		// yunsq 2016年6月27日 21:07:03 普通发票保存后如果数量已开完状态打勾
		if ((invoice.getPFA().equals(InvoiceType.Final) || invoice.getPFA().equals(InvoiceType.Adjust))
				&& invoice.getMT().equals(MT4Invoice.Take)) {
			List<Storage> storages4ThisInvoice = new ArrayList<Storage>();
			for (Invoice invoice1 : invoices) {
				if (invoice1 != null && invoice1.getStorages() != null)
					storages4ThisInvoice.addAll(invoice1.getStorages());
			}
			BigDecimal sumQuantity = new BigDecimal(0);
			for (Storage storage : storages4ThisInvoice) {
				sumQuantity = sumQuantity.add(storage.getQuantity());
			}
			lot.setQuantityInvoiced(sumQuantity);
			lot.setIsInvoiced((lot.getQuantityInvoiced().compareTo(lot.getQuantityLess()) >= 0
					&& lot.getQuantityInvoiced().compareTo(lot.getQuantityMore()) <= 0));
		}
		// 开票数量取发票，发票可能没有明细，by caojun
		lot.setQuantityInvoiced(new BigDecimal(invoices.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum())
				.setScale(3, RoundingMode.HALF_UP));

		// zengshihua 2016/08/29属于初始化业务，不需要审核
		if (invoice.getIsIniatiated()) {
			invoice.setIsApproved(true);
			invoice.setStatus(Status.Agreed);
		}

		this.invoiceRepo.SaveOrUpdate(invoice);
		List<Lot> lots = commonService.setInvoice4Lot_NewInvoice(lot, invoice);

		// add by zhu yixin on 2015-1-14，尽管CRRC没有提出，考虑到可能需要，先加上了。

		where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", invoice.getLotId()));
		where.add(Restrictions.eq("FeeCode", FeeCode.Goods));
		List<Invoice> temp_list = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		BigDecimal b = new BigDecimal(0);
		for (Invoice x : temp_list) {
			b = b.add(x.getDueAmount());
		}
		lot.setAmountInvoiced(b);

		// 2016-5-16 Shencl 如果开票标识为true，则判断收付款标识
		if (invoice.getMT().equals(MT4Invoice.Take)) {
			if (lot.getIsInvoiced()) {
				if (lot.getAmountFunded() != null) {
					// 如果付款金额大于发票金额
					if (lot.getAmountFunded().compareTo(lot.getAmountInvoiced()) >= 0) {
						lot.setIsFunded(true);
					}
				}
			}
		}
		logger.info("Lot:......" + lot);
		/* lot.setVersion(lot.getVersion()+1); */
		lotRepo.SaveOrUpdate(lot);

		commonService.UpdateLotFeesByLotId_New(lot.getId()); // 更新不是SummaryNote的发票对应的批次的费用

		// commonService.UpdateInvoiceStatus(lots);

		// yunsq 2016年6月27日 21:10:58 收票不需要审核
		if (invoice.getMT().equals(MT4Invoice.Take)) {
			commonService.UpdateInvoiceStatus_NewInvoice(lots);
		}

		// 重新取值
		invoice = invoiceRepo.getOneById(invoice.getId(), Invoice.class);

		// Invoice vinvoice=this.invoiceRepo.getOneById(invoice.getId(),
		// Invoice.class);
		// List<Invoice> listvInvoice = new ArrayList<Invoice>();
		// listvInvoice.add(vinvoice);
		//
		// List<Invoice> listInvoice = ListBindSubClass(listvInvoice, new
		// HashMap<>());
		// if (listInvoice.size() > 0)
		// invoice = listInvoice.get(0);

		// Shencl 2016年6月29日 21:10:58 更新客户资金余额
		commonService.SyncCustomerBalance(invoice.getCustomerId(), invoice.getLegalId(), invoice.getCommodityId());

		if (invoice.getIsIniatiated()) {
			this.initVerify(invoice);
		}
		/**
		 * 断掉 Storages>BviSource关系,临时解决JSON序列化循环引用
		 */
		if (invoice.getStorages() != null) {
			List<Storage> Storages = invoice.getStorages();
			for (Storage s : Storages) {
				s.setBviSource(null);
			}
			invoice.getStorages().clear();
			invoice.setStorages(Storages);// 装回去
		}
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, invoice);
	}

	@Override
	public ActionResult<Invoice> Save4SummaryNoteFee(Invoice invoice) {

		ActionResult<Invoice> result = new ActionResult<>();

		try {

			// 检查数据有效性
			if (!StringUtils.isEmpty(invoice.getId())) {

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("InvoiceId", invoice.getId()));
				where.add(Restrictions.ne("Status", -1));

				List<Pending> list = this.pendingRepository.GetQueryable(Pending.class).where(where).toList();

				if (list != null && list.size() > 0) {

					throw new Exception("发票已经申请付款，不允许修改。");
				}

			}

			this.invoiceRepo.SaveOrUpdate(invoice);

			// 确认发票编号
			SysSequence sequence = new SysSequence();

			sequence.setIdentityId(new BigDecimal(invoice.getSerialNo()));
			sequence.setCode("invoiceno_" + invoice.getPrefix());

			this.commonService.ConfirmSequenceIndex(sequence);

			result.setSuccess(true);
			result.setData(invoice);

		} catch (Exception e) {

			logger.error(e);

			result.setSuccess(false);
			result.setMessage(e.getMessage());

		}

		return result;
	}

	@Override
	public ActionResult<Invoice> Save4MultiLots(Invoice invoice) {

		try {

			// #region 数据有效性检查
			if (invoice == null || (invoice.getLotId() == null && invoice.getContractId() == null)
					|| invoice.getPFA() == null || ((invoice.getStorages() == null || invoice.getStorages().size() == 0)
							&& (invoice.getNotices() == null || invoice.getNotices().size() == 0))) {

				return new ActionResult<>(false, "发票类型，批次或者商品货物明细未选择");
			}

			Contract contract = contractRepo.getOneById(invoice.getContractId(), Contract.class);

			if (contract == null)
				return new ActionResult<>(false, "contract is null");

			DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
			where.add(Restrictions.eq("ContractId", contract.getId()));
			List<Lot> temp_lot_list = lotRepo.GetQueryable(Lot.class).where(where).toList();
			BigDecimal sumLotQuantity = new BigDecimal(
					temp_lot_list.stream().mapToDouble(x -> x.getQuantityMore().doubleValue()).sum());

			// #region 发票数量不能大于批次最大数量
			// add by chenyj 2015.07.10

			where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("ContractId", invoice.getContractId()));
			where.add(Restrictions.eq("Is4MultiLots", invoice.getIs4MultiLots()));
			where.add(Restrictions.ne("Id", invoice.getId()));
			List<Invoice> temp_Invoice_list = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			BigDecimal summary = new BigDecimal(
					temp_Invoice_list.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			BigDecimal tmpSum = invoice.getQuantity().add(summary);

			if (tmpSum.compareTo(sumLotQuantity) > 0) {
				return new ActionResult<>(false, "发票数量不能大于批次最大数量（溢短装最大范围）！");
			}

			// ---------------

			Lot lot = lotRepo.getOneById(invoice.getLotId(), Lot.class);

			if (!StringUtils.isEmpty(invoice.getId())) {

				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("InvoiceId", invoice.getId()));
				where.add(Restrictions.ne("Status", Status.Deny));
				Pending pending = pendingRepository.GetQueryable(Pending.class).where(where).firstOrDefault();
				if (pending != null) {
					return new ActionResult<>(false, "发票已经申请付款，不允许修改。");
				}
			}

			invoice.setLegalId(contract.getLegalId());
			invoice.setCommodityId(contract.getCommodityId());
			if (lot != null)
				invoice.setLotId(lot.getId());

			// 如果是修改的状态，先恢复原来的商品明细的IsInvoiced的标志
			if (!StringUtils.isEmpty(invoice.getId())) {
				Invoice old = invoiceRepo.getOneById(invoice.getId(), Invoice.class);
				for (Storage storage : old.getStorages()) {
					if (storage == null)
						continue;
					storage.setIsInvoiced(false);
					storageRepo.SaveOrUpdate(storage);
				}
			}

			// --------
			// step 2: 更新storages
			List<Storage> ss = new ArrayList<>();

			if (invoice.getStorages() != null) {
				for (Storage storage : invoice.getStorages()) {
					if (storage == null)
						continue;
					Storage t = storageRepo.getOneById(storage.getId(), Storage.class);
					if (t == null)
						continue;
					t.setPremium(storage.getPremium());
					t.setMajor(storage.getMajor());
					if (invoice.getPFA().equals(InvoiceType.Provisional))
						t.setPriceProvisional(storage.getMajor() == null ? BigDecimal.ZERO : storage.getMajor());
					t.setPrice(storage.getPrice());
					t.setQuantity(storage.getQuantity());
					t.setQuantityInvoiced(storage.getQuantityInvoiced());
					t.setAmount(storage.getAmount());
					t.setIsInvoiced(true);
					storageRepo.SaveOrUpdate(t);
					ss.add(t);
				}
			}
			invoice.setStorages(ss);

			// step 1: 保存发票
			// var invoiceId = Repository.SaveOrUpdateRetrunId(invoice);
			invoiceRepo.SaveOrUpdate(invoice);
			// 确认发票编号
			SysSequence sequence = new SysSequence();
			sequence.setIdentityId(new BigDecimal(invoice.getSerialNo()));
			sequence.setCode("invoiceno_" + invoice.getPrefix());
			commonService.ConfirmSequenceIndex(sequence);

			// 更新发票和交付明细中间表的关联日期
			if (invoice.getStorages() != null) {
				List<String> StorageIds = invoice.getStorages().stream().map(x -> x.getId())
						.collect(Collectors.toList());

				where = DetachedCriteria.forClass(Invoice2Storage.class);
				where.add(Restrictions.eq("InvoiceId", invoice.getId()));
				List<Invoice2Storage> invoice2Storges = invoice2StorageRepo.GetQueryable(Invoice2Storage.class)
						.where(where).toList();

				final Invoice finalInvoice = invoice;
				List<Invoice2Storage> invoice2StorgeDelete = invoice2Storges.stream().filter(
						x -> x.getInvoiceId().equals(finalInvoice.getId()) && !StorageIds.contains(x.getStorageId()))
						.collect(Collectors.toList());

				for (Invoice2Storage invoice2Storge : invoice2StorgeDelete) {
					invoice2StorageRepo.PhysicsDelete(invoice2Storge);
				}
				for (Storage storage : invoice.getStorages()) {
					Invoice2Storage exist = invoice2Storges.stream()
							.filter(x -> x.getStorageId().equals(storage.getId())).collect(Collectors.toList()).get(0);

					if (exist != null) {
						if (exist.getTradeDate() == null) {
							exist.setTradeDate(new Date());
							invoice2StorageRepo.SaveOrUpdate(exist);
						}
					} else {
						Invoice2Storage newinvoice2Storge = new Invoice2Storage();
						newinvoice2Storge.setInvoiceId(invoice.getId());
						newinvoice2Storge.setStorageId(storage.getId());
						newinvoice2Storge.setTradeDate(new Date());

						invoice2StorageRepo.SaveOrUpdate(newinvoice2Storge);
					}
				}

			}

			// step 3: 恢复该发票项下的原来的调整发票
			where = DetachedCriteria.forClass(Approve.class);
			where.add(Restrictions.eq("AdjustId", invoice.getId()));
			List<Invoice> provisionals = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			for (Invoice invoice1 : provisionals) {
				Invoice t = invoiceRepo.getOneById(invoice1.getId(), Invoice.class);
				t.setAdjustId(null);
				invoiceRepo.SaveOrUpdate(t);
			}

			// step 5: 更新发票项下的临时发票的adjustId
			if (invoice.getProvisionals() != null && invoice.getProvisionals().size() > 0) {

				for (Invoice inv : invoice.getProvisionals()) {
					Invoice t = invoiceRepo.getOneById(inv.getId(), Invoice.class);
					// 不允许一个临时发票、被用于2次调整。即，只能被“调整”1次。
					if (t.getAdjustId().equals(inv.getId())) {

						return new ActionResult<>(false, "暂定价发票{0}已经调整，发生在调整发票{1}");
					}

					t.setAdjustId(invoice.getId());
					invoiceRepo.SaveOrUpdate(t);
				}
			}

			// to-do:作为较为特殊的发票类型，无法统计每个批次的发票数量
			commonService.UpdateLotPriceByLotId(invoice.getLotId());

			// -----------------

			// 更新批次的（开或收）发票数量和标志
			where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("ContractId", invoice.getContractId()));
			where.add(Restrictions.or(Restrictions.eq("PFA", InvoiceType.Final),
					Restrictions.eq("PFA", InvoiceType.Adjust)));
			List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			List<Storage> storages4ThisInvoice = new ArrayList<>();
			for (Invoice invoice1 : invoices) {
				if (invoice1 != null && invoice1.getStorages() != null)
					storages4ThisInvoice.addAll(invoice1.getStorages());
			}

			Map<String, List<Storage>> temp_map = storages4ThisInvoice.stream()
					.collect(Collectors.groupingBy(x -> x.getLotId()));

			for (String LotId : temp_map.keySet()) {

				BigDecimal Quantity = new BigDecimal(
						temp_map.get(LotId).stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

				Lot tmpLot = lotRepo.getOneById(LotId, Lot.class);
				if (tmpLot == null)
					continue;

				tmpLot.setQuantityInvoiced(Quantity);
				List<Lot> lots = commonService.setInvoice4Lot(tmpLot);
				lotRepo.SaveOrUpdate(tmpLot);

				commonService.UpdateInvoiceStatus(lots);
			}

			// 重新取值
			invoice = commonService.SimplifyData(invoiceRepo.getOneById(invoice.getId(), Invoice.class));

			return new ActionResult<>(true, MessageCtrm.SaveSuccess, invoice);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}
	
	@Override
	@Transactional
	public ActionResult<String> DeleteForStoragesInvoice(List<Invoice> invoices){
		// #region 有效性和合法的检查
		for(Invoice invoice : invoices) {
			Delete(invoice);
		}
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	@Override
	public ActionResult<String> Delete(Invoice invoice) {
		
		// #region 有效性和合法的检查
		if (invoice == null)
			return new ActionResult<>(false, "发票已删除，请刷新结果");

		if (invoice.getIsAccounted())
			return new ActionResult<>(false, "已经过账，不允许删除");

		if (StringUtils.isNotBlank(invoice.getAdjustId())) {
			Invoice adjust = invoiceRepo.getOneById(invoice.getAdjustId(), Invoice.class);

			if (adjust != null)
				return new ActionResult<>(false, "已经开具调整发票，不允许删除该临时发票");
		}

		// DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		// where.add(Restrictions.eq("InvoiceId", invoiceId));
		// Pending pending =
		// pendingRepository.GetQueryable(Pending.class).where(where).toList().get(0);

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("InvoiceId", invoice.getId()));
		Fund funds = fundRepo.GetQueryable(Fund.class).where(where).firstOrDefault();

		if (funds != null)
			return new ActionResult<>(false, "该发票已经被用于申请付款或资金收付，不允许删除");

		DetachedCriteria where2 = DetachedCriteria.forClass(Invoice.class);
		where2.add(Restrictions.eq("AdjustId", invoice.getId()));
		List<Invoice> temp_Invoice_list = invoiceRepo.GetQueryable(Invoice.class).where(where2).toList();

		invoice.setProvisionals(temp_Invoice_list);

		// --------------------

		try {

			// 如果是调整发票，删除以前，先恢复临时发票的AdjustId的值
			if (invoice.getProvisionals() != null && invoice.getProvisionals().size() > 0) {

				for (Invoice inv : invoice.getProvisionals()) {
					Invoice t = invoiceRepo.getOneById(inv.getId(), Invoice.class);
					t.setAdjustId(null);
					invoiceRepo.SaveOrUpdate(t);
				}

			}

			// 删除发票以前，先恢复商品明细的IsInvoiced的标志
			if (invoice.getStorages() != null) {

				for (Storage v : invoice.getStorages()) {
					if (v == null)
						continue;

					Storage t = storageRepo.getOneById(v.getId(), Storage.class);
					t.setIsInvoiced(false);
					storageRepo.SaveOrUpdate(t);

					if (v.getMergeId() != null) // 此条记录被合并
					{
						Storage merges = storageRepo.getOneById(v.getMergeId(), Storage.class);

						if (merges != null) {
							merges.setIsInvoiced(false);
							storageRepo.SaveOrUpdate(merges);
						}
					}

				}
			}

			// 删除发票以前，先恢复通知货量的IsInvoiced的标志
			if (invoice.getNotices() != null) {
				for (Storage v : invoice.getNotices()) {
					if (v == null)
						continue;
					Storage t = storageRepo.getOneById(v.getId(), Storage.class);
					t.setIsInvoiced(false);
					storageRepo.SaveOrUpdate(t);
				}
			}

			// 删除发票自身
			invoiceRepo.PhysicsDelete(invoice);

			// 删除发票和交付明细的中间表Invoice2Storage中的数据
			/*
			 * where = DetachedCriteria.forClass(Invoice2Storage.class);
			 * where.add(Restrictions.eq("InvoiceId", invoiceId));
			 * List<Invoice2Storage> invoice2Storages =
			 * invoice2StorageRepo.GetQueryable(Invoice2Storage.class)
			 * .where(where).toList();
			 * 
			 * for (Invoice2Storage invoice2Storage : invoice2Storages) {
			 * invoice2StorageRepo.PhysicsDelete(invoice2Storage); }
			 */

			// 更新批次的（开或收）发票数量和标志
			DetachedCriteria where3 = DetachedCriteria.forClass(Invoice.class);
			where3.add(Restrictions.eq("LotId", invoice.getLotId()));
			where3.add(Restrictions.or(Restrictions.eq("PFA", InvoiceType.Final),
					Restrictions.eq("PFA", InvoiceType.Adjust)));
			List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(where3).toList();

			// -----
			Lot lot = lotRepo.getOneById(invoice.getLotId(), Lot.class);

			if (lot != null) {
				lot.setQuantityInvoiced(
						new BigDecimal(invoices.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
				// lot.IsInvoiced = (lot.QuantityInvoiced >= lot.QuantityLess &&
				// lot.QuantityInvoiced <= lot.QuantityMore);
				List<Lot> lots = commonService.setInvoice4Lot(lot);
				lotRepo.SaveOrUpdate(lot);
				commonService.UpdateInvoiceStatus(lots);
			}

			DetachedCriteria where4 = DetachedCriteria.forClass(SummaryFees.class);
			where4.add(Restrictions.eq("InvoiceId", invoice.getId()));
			List<SummaryFees> summaryFees = summaryFeesRepo.GetQueryable(SummaryFees.class).where(where4).toList();

			summaryFees.forEach(summaryFeesRepo::PhysicsDelete);

			// 更新SummaryNote发票对应批次的费用
			if (invoice.getPFA().equals(InvoiceType.SummaryNote) && invoice.getStorages() != null
					&& invoice.getStorages().size() > 0) {

				Map<String, List<Storage>> temp_map = invoice.getStorages().stream()
						.collect(Collectors.groupingBy(x -> x.getLotId()));

				for (String lotId : temp_map.keySet()) {

					commonService.UpdateLotFeesByLotId(lotId);
				}
			} else if (!invoice.getPFA().equals(InvoiceType.SummaryNote)
					&& !invoice.getFeeCode().equals(FeeCode.Goods)) {

				// 更新不是SummaryNote的费用发票对应批次的费用
				commonService.UpdateLotFeesByLotId(invoice.getLotId());
			}

			return new ActionResult<>(true, "删除成功");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<Invoice> GetById(String invoiceId) {

		Invoice invoice = invoiceRepo.getOneById(invoiceId, Invoice.class);

		Invoice obj = null;

		if (invoice != null) {
			obj = commonService.SimplifyData(invoice);

			DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("AdjustId", invoice.getId()));
			List<Invoice> invoiceList = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			invoice.setProvisionals(invoiceList);

			if (invoice.getProvisionals() != null && invoice.getProvisionals().size() > 0)
				obj.setProvisionals(commonService.SimplifyDataInvoiceList(invoice.getProvisionals()));

			obj.setLegalBank(invoice.getLegalBank());
			obj.setCustomerBank(invoice.getCustomerBank());
		}

		return new ActionResult<>(true, null, obj);
	}

	@Override
	public ActionResult<InvoicePnL> InvoicePnLById(String invoiceId) {

		InvoicePnL invoice = invoicePnLRepo.getOneById(invoiceId, InvoicePnL.class);

		DetachedCriteria where = DetachedCriteria.forClass(Square.class);
		where.add(Restrictions.eq("InvoicePnLId", invoice.getId()));
		List<Square> Square_list = squareRepo.GetQueryable(Square.class).where(where).toList();

		invoice.setSquares(Square_list);

		// ------
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("PnLId", invoice.getId()));
		where.add(Restrictions.eq("MT", "T"));
		List<Storage> Storage_list_1 = storageRepo.GetQueryable(Storage.class).where(where).toList();
		invoice.setStorageIns(Storage_list_1);

		// ------
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("PnLId", invoice.getId()));
		where.add(Restrictions.eq("MT", "M"));
		List<Storage> Storage_list_2 = storageRepo.GetQueryable(Storage.class).where(where).toList();
		invoice.setStorageOuts(Storage_list_2);

		return new ActionResult<>(true, null, invoice);
	}

	@Override
	public ActionResult<List<Invoice>> InvoicesByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));
		List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		invoices = commonService.SimplifyDataInvoiceList(invoices);

		return new ActionResult<>(true, null, invoices);
	}

	@Override
	public ActionResult<List<Invoice>> InvoicesByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));
		List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		invoices = commonService.SimplifyDataInvoiceList(invoices);

		return new ActionResult<>(true, null, invoices);
	}

	@Override
	public ActionResult<List<Invoice>> InvoicesByCustomerId(String customerId) {

		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CustomerId", customerId));
		List<Invoice> invoices = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		invoices = commonService.SimplifyDataInvoiceList(invoices);

		return new ActionResult<>(true, null, invoices);
	}

	@Override
	public ActionResult<Boolean> IsInvoiceNoDuplicate(Invoice CurInvocie) {

		try {

			if (CurInvocie == null)
				throw new Exception("CurInvocie is null");

			DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("InvoiceNo", CurInvocie.getInvoiceNo()));
			where.add(Restrictions.neOrIsNotNull("Id", CurInvocie.getId()));
			List<Invoice> temp_Invoice_list = invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			boolean isDuplicate = false;
			if (temp_Invoice_list != null && temp_Invoice_list.size() > 0)
				isDuplicate = true;

			if (isDuplicate) // 如果该发票号重复，则将该发票号置为使用状态
			{
				// 确认发票编号
				SysSequence sequence = new SysSequence();
				sequence.setIdentityId(new BigDecimal(CurInvocie.getSerialNo()));
				sequence.setCode("invoiceno_" + CurInvocie.getPrefix());
				commonService.ConfirmSequenceIndex(sequence);
				isDuplicate = true;
			}

			return new ActionResult<>(true, null, isDuplicate);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public ActionResult<InvoicePnL> InvoiceSettleTrial(Param4InvoicePnL param4InvoicePnL) {

		// #region 有效性检查
		GlobalSet globalSet = globalSetRepo.GetList(GlobalSet.class).get(0);
		if (globalSet == null)
			return new ActionResult<>(false, "缺少GlobalSet设置");

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		if (param4InvoicePnL == null || param4InvoicePnL.getInvoiceId() == null) {

			return new ActionResult<>(false, "Params is unavailable: InvoiceId");
		}

		Invoice invoice = invoiceRepo.getOneById(param4InvoicePnL.getInvoiceId(), Invoice.class);
		if (invoice == null)
			return new ActionResult<>(false, "The invoice is not existed already");

		if (invoice.getPFA().equals(InvoiceType.Provisional)) {

			return new ActionResult<>(false, "不需要对临时发票进行盈亏结算");
		}

		if (invoice.getIsSettled())
			return new ActionResult<>(false, "The invoice is settled already");

		if (invoice.getIsAccounted())
			return new ActionResult<>(false, "The invoice is accounted already.");

		// ------------
		// #region 构建:vmPnL

		InvoicePnL vmPnL = new InvoicePnL();
		vmPnL.setLotId(invoice.getLotId());
		vmPnL.setInvoiceId(invoice.getId());
		vmPnL.setContractFullNo(invoice.getLot().getFullNo());
		vmPnL.setCustomerName(invoice.getLot().getCustomer().getName());
		vmPnL.setPremium(invoice.getPremium() == null ? BigDecimal.ZERO : invoice.getPremium());

		// #region 销售一侧的商品明细
		vmPnL.setStorageOuts(invoice.getStorages());
		vmPnL.setQuantitySell(invoice.getQuantity());

		// 销售金额
		vmPnL.setAmountSell(BigDecimal.ZERO);

		// 销售发生的fee的汇总金额
		BigDecimal fee4Sell = BigDecimal.ZERO;// vmPnL.StorageOuts.Sum(so =>
												// so.Quantity * (so.Fee ?? 0));

		for (Storage so : vmPnL.getStorageOuts()) {
			if (so.getCurrency().equals(currency)) {
				vmPnL.setAmountSell(vmPnL.getAmountSell().add(so.getQuantity().multiply(so.getPrice())));
				fee4Sell = fee4Sell.add(so.getQuantity().multiply(so.getFee()));
			} else {
				if (so.getCurrency().equals("CNY")) {
					vmPnL.setAmountSell(
							vmPnL.getAmountSell().add(so.getQuantity().multiply(so.getPrice()).divide(rate)));
					fee4Sell = fee4Sell.add(so.getQuantity().multiply(so.getFee()).divide(rate));
				} else {
					vmPnL.setAmountSell(
							vmPnL.getAmountSell().add(so.getQuantity().multiply(so.getPrice()).multiply(rate)));
					fee4Sell = fee4Sell.add(so.getQuantity().multiply(so.getFee()).multiply(rate));
				}
			}
		}

		// 销售均价
		if (vmPnL.getQuantitySell().compareTo(BigDecimal.ZERO) != 0) {
			vmPnL.setPriceSell(vmPnL.getAmountSell().divide(vmPnL.getQuantitySell()));
		}

		vmPnL.setStorageOuts(commonService.SimplifyDataStorageList(vmPnL.getStorageOuts()));

		// #region 采购一侧的商品明细
		vmPnL.setStorageIns(new ArrayList<>());

		for (Storage v : vmPnL.getStorageOuts()) {
			// 取得对手方的交付记录，并且加入到List中
			if (v.getCounterpartyId() != null) {

				vmPnL.getStorageIns().add(storageRepo.getOneById(v.getCounterpartyId(), Storage.class));
			}
		}

		// 采购数量
		vmPnL.setQuantityPurchase(
				new BigDecimal(vmPnL.getStorageIns().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));

		// 采购成本
		vmPnL.setAmountPurchase(BigDecimal.ZERO);

		// 采购发生的fee的汇总金额
		BigDecimal fee4Purchase = BigDecimal.ZERO;

		for (Storage si : vmPnL.getStorageIns()) {
			if (si.getCurrency().equals(currency)) {
				vmPnL.setAmountPurchase(vmPnL.getAmountPurchase().add(si.getQuantity().multiply(si.getPrice())));
				fee4Purchase = fee4Purchase.add(si.getQuantity().multiply(si.getFee()));
			} else {
				if (si.getCurrency().equals("CNY")) {
					vmPnL.setAmountPurchase(
							vmPnL.getAmountPurchase().add(si.getQuantity().multiply(si.getPrice()).divide(rate)));
					fee4Purchase = fee4Purchase.add(si.getQuantity().multiply(si.getFee()).divide(rate));
				} else {
					vmPnL.setAmountPurchase(
							vmPnL.getAmountPurchase().add(si.getQuantity().multiply(si.getPrice()).multiply(rate)));
					fee4Purchase = fee4Purchase.add(si.getQuantity().multiply(si.getFee()).multiply(rate));
				}
			}
		}

		// 采购价格
		if (vmPnL.getQuantityPurchase().compareTo(BigDecimal.ZERO) != 0) {

			vmPnL.setPricePurchase(vmPnL.getAmountPurchase().divide(vmPnL.getAmountPurchase()));

		}

		vmPnL.setFee(fee4Sell.add(fee4Purchase));

		vmPnL.setPnL4Spot(vmPnL.getAmountSell().subtract(vmPnL.getAmountPurchase()));

		vmPnL.setStorageIns(commonService.SimplifyDataStorageList(vmPnL.getStorageIns()));

		// #region 构造: pl.Hedges

		List<Position> lstPosition = param4InvoicePnL.getPositions();

		if (lstPosition != null) {
			if (lstPosition.stream().filter(Position::getIsSquared).collect(Collectors.toList()).size() > 0) {

				return new ActionResult<>(false, "存在已结算的头寸");
			}

			List<Market> markets = marketRepo.GetList(Market.class);

			List<Square> squares = new ArrayList<>();

			for (Market market : markets)// 根据不同市对齐头寸
			{
				List<Position> lstPosition4Market = lstPosition.stream()
						.filter(x -> x.getMarketId().equals(market.getId())).collect(Collectors.toList());

				if (lstPosition4Market.size() > 0) {
					ActionResult<InvoicePnL> pnlbyMarket = Position4Market(vmPnL, lstPosition4Market, market);

					if (pnlbyMarket.isSuccess() && pnlbyMarket.getData() != null) {
						squares.addAll(pnlbyMarket.getData().getSquares());

						// 调期费用
						vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().add(pnlbyMarket.getData().getPnL4Carry()));
					}
				}

			}
			if (squares.size() > 0)
				vmPnL.setPnL4Hedge(new BigDecimal(squares.stream().mapToDouble(x -> x.getPnL().doubleValue()).sum()));

			vmPnL.setSquares(
					squares.stream().sorted(Comparator.comparing(Square::getPromptDate)).collect(Collectors.toList()));
		}

		// 合计盈亏= 现货盈亏+ 期货盈亏
		vmPnL.setPnLTotal(vmPnL.getPnL4Spot().add(vmPnL.getPnL4Hedge()));
		vmPnL.setCurrency(currency);

		return new ActionResult<>(true, "试算成功", vmPnL);

	}

	private ActionResult<InvoicePnL> Position4Market(InvoicePnL vmPnlParam, List<Position> positions, Market market) {
		switch (market.getCode().toUpperCase()) {
		case "LME":
			return PositionTrial4LME(vmPnlParam, positions);
		case "SFE":
			return PositionTrial4LME(vmPnlParam, positions);
		case "SGX":
			return PositionTrial4LME(vmPnlParam, positions);
		}

		return new ActionResult<>(false, "不支持的市场");
	}

	private ActionResult<InvoicePnL> PositionTrial4LME(InvoicePnL vmPnlParam, List<Position> positions) {
		GlobalSet globalSet = this.globalSetRepo.GetList(GlobalSet.class).get(0);

		if (globalSet == null)
			return new ActionResult<>(false, "缺少GlobalSet设置");

		String currency = globalSet.getDefaultCurrency();
		BigDecimal rate = globalSet.getCurrencyRate();

		// #region 构建:vmPnL
		InvoicePnL vmPnL = new InvoicePnL();
		vmPnL.setLotId(vmPnlParam.getLotId());
		vmPnL.setInvoiceId(vmPnlParam.getId());
		vmPnL.setContractFullNo(vmPnlParam.getContractFullNo());
		vmPnL.setCustomerName(vmPnlParam.getCustomerName());
		vmPnL.setPremium(vmPnlParam.getPremium());

		// #region 构造: pl.Hedges

		List<Position> lstPosition = positions;
		// 因为数据库里是带有正负号的，原来的算法要求没有正负号，暂时作这样的处理
		if (lstPosition != null) {
			// #region 数据预处理
			List<Position> lstL = lstPosition.stream().filter(x -> x.getLS().equals(LS.LONG)).sorted(Comparator
					.comparing(Position::getPromptDate).thenComparing(Comparator.comparing(Position::getQuantity)))
					.collect(Collectors.toList());

			List<Position> lstS = lstPosition.stream().filter(x -> x.getLS().equals(LS.SHORT)).sorted(Comparator
					.comparing(Position::getPromptDate).thenComparing(Comparator.comparing(Position::getQuantity)))
					.collect(Collectors.toList());

			List<Square> squares = new ArrayList<>();
			do {
				// #region 先取2侧
				Position l = null;
				Position s = null;

				if (lstL.size() > 0) {
					l = lstL.get(0);
					final Position finalL = l;
					s = lstS.stream()
							.filter(x -> finalL != null && x.getPromptDate() == finalL.getPromptDate()
									&& x.getMarketId().equals(finalL.getMarketId())
									&& x.getCommodityId().equals(finalL.getCommodityId()))
							.collect(Collectors.toList()).get(0);
				} else {
					if (lstS.size() > 0)
						s = lstS.get(0);
				}

				// #region LS两边都不为空

				if (l != null && s != null) {
					BigDecimal qtyOfLong = l.getQuantity();
					BigDecimal qtyOfShort = s.getQuantity();

					if (qtyOfLong.compareTo(qtyOfShort.abs()) == 0) {

						Square t = new Square();

						t.setPromptDate(l.getPromptDate());
						t.setQuantity(l.getQuantity());
						t.setRefLong(l.getOurRef());
						t.setPriceLong(l.getOurPrice());
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(s.getOurPrice());
						t.setPromptDateShort(s.getPromptDate());
						t.setCurrency(s.getCurrency());

						t.setLongId(l.getId());
						t.setShortId(s.getId());
						t.setQuantityLong(l.getQuantity());
						t.setQuantityShort(s.getQuantity());
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());

						squares.add(t);
						lstL.remove(l);
						lstS.remove(s);

					} else if (qtyOfLong.compareTo(qtyOfShort.abs()) > 0) {
						// 扣减原数量
						l.setQuantity(l.getQuantity().subtract(s.getQuantity().abs()));

						Square t = new Square();
						t.setPromptDate(l.getPromptDate());
						t.setQuantity(s.getQuantity().abs());
						t.setRefLong(l.getOurRef());
						t.setPriceLong(l.getOurPrice());
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(s.getOurPrice());
						t.setPromptDateShort(s.getPromptDate());
						t.setTradeDateShort(s.getTradeDate());
						t.setCurrency(s.getCurrency());

						t.setShortId(s.getId());
						t.setQuantityLong(s.getQuantity().abs());
						t.setQuantityShort(s.getQuantity());
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());

						Position LongSplitted = new Position();

						try {
							BeanUtils.copyProperties(LongSplitted, l);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}

						LongSplitted.setId(null);
						LongSplitted.setQuantity(s.getQuantity().abs());
						LongSplitted.setIsSplitted(true);
						LongSplitted.setOurRef(l.getOurRef() + ("(s)"));
						LongSplitted.setSourceId(l.getSourceId() == null ? l.getId() : l.getSourceId());
						LongSplitted.setIsSquared(true);

						t.setSplitSquarePosition(LongSplitted);

						squares.add(t);
						lstS.remove(s);
					} else if (qtyOfLong.compareTo(qtyOfShort.abs()) < 0) {
						s.setQuantity(s.getQuantity().add(l.getQuantity()));

						Square t = new Square();
						t.setPromptDate(l.getPromptDate());
						t.setQuantity(qtyOfLong);
						t.setRefLong(l.getOurRef());
						t.setPriceLong(l.getOurPrice());
						t.setPromptDateLong(l.getPromptDate());
						t.setTradeDateLong(l.getTradeDate());

						t.setRefShort(s.getOurRef());
						t.setPriceShort(s.getOurPrice());
						t.setPromptDateShort(s.getPromptDate());
						t.setTradeDateShort(s.getTradeDate());
						t.setCurrency(s.getCurrency());

						t.setLongId(l.getId());
						t.setQuantityLong(qtyOfLong);
						t.setQuantityShort(BigDecimal.ZERO.subtract(qtyOfLong));
						t.setMarketId(l.getMarketId());
						t.setCommodityId(l.getCommodityId());

						Position ShortSplitted = new Position();

						try {
							BeanUtils.copyProperties(ShortSplitted, s);
						} catch (IllegalAccessException | InvocationTargetException e) {
							e.printStackTrace();
						}

						ShortSplitted.setId(null);
						ShortSplitted.setQuantity(BigDecimal.ZERO.subtract(l.getQuantity().abs()));
						ShortSplitted.setIsSplitted(true);
						ShortSplitted.setOurRef(s.getOurRef() + "(s)");
						ShortSplitted.setSourceId(s.getSourceId() == null ? s.getId() : s.getSourceId());
						ShortSplitted.setIsSquared(true);

						t.setSplitSquarePosition(ShortSplitted);

						squares.add(t);
						lstL.remove(l);
					}
				}

				// #region 一边为空
				if (l == null && s != null) {
					Square t = new Square();
					t.setQuantity(s.getQuantity().abs());
					t.setRefShort(s.getOurRef());
					t.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());
					t.setPromptDateLong(null);
					t.setPromptDateShort(s.getPromptDate());
					t.setTradeDateLong(null);
					t.setTradeDateShort(s.getTradeDate());

					t.setCurrency(s.getCurrency());
					t.setPromptDate(null);

					t.setShortId(s.getId());
					t.setQuantityShort(s.getQuantity());
					t.setMarketId(s.getMarketId());
					t.setCommodityId(s.getCommodityId());

					squares.add(t);
					lstS.remove(s);
				}
				if (l != null && s == null) {
					Square t = new Square();
					t.setQuantity(l.getQuantity());
					t.setRefLong(l.getOurRef());
					t.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
					t.setPromptDateShort(null);
					t.setPromptDateLong(l.getPromptDate());

					t.setTradeDateLong(l.getTradeDate());
					t.setTradeDateShort(null);

					t.setCurrency(l.getCurrency());
					t.setPromptDate(null);

					t.setLongId(l.getId());
					t.setQuantityLong(l.getQuantity());
					t.setMarketId(l.getMarketId());
					t.setCommodityId(l.getCommodityId());

					squares.add(t);
					lstL.remove(l);
				}

			} while (lstL.size() > 0 || lstS.size() > 0);

			for (Square v : squares) {
				if (v.getCurrency().equals(currency))
					v.setPnL(v.getPriceShort().subtract(v.getPriceLong()).multiply(v.getQuantity().abs()));
				else {
					if (v.getCurrency().equals("CNY"))
						v.setPnL(v.getPriceShort().subtract(v.getPriceLong()).multiply(v.getQuantity().abs())
								.divide(rate));
					else
						v.setPnL(v.getPriceShort().subtract(v.getPriceLong()).multiply(v.getQuantity().abs())
								.multiply(rate));
				}
			}

			vmPnL.setSquares(
					squares.stream().sorted(Comparator.comparing(x -> x.getPromptDate())).collect(Collectors.toList()));
		}

		// 单独计算调期的损益
		if (vmPnL.getSquares() != null) {
			// var q = (from p in vmPnL.Squares.Where(x => x.PromptDate !=
			// DateTime.MinValue).ToList()
			// group p by p.PromptDate
			// into g
			// select new
			// {
			// promptDate = g.Key
			// }).OrderBy(x => x.promptDate).ToList();

			Map<Date, List<Square>> temp_map = vmPnL.getSquares().stream().filter(x -> x.getPromptDate() != null)
					.collect(Collectors.groupingBy(Square::getPromptDate));

			List<Date> q = new ArrayList<>();

			for (Date d : temp_map.keySet()) {

				q.add(d);
			}

			if (q.size() == 1) {
				// 没有调期
				vmPnL.setPnL4Carry(BigDecimal.ZERO);
			} else if (q.size() > 1) {
				// 取得最后的平仓日期和次最后日期
				Date maxPromptDate = vmPnL.getSquares().stream().max(Comparator.comparing(Square::getPromptDate)).get()
						.getPromptDate();

				List<Square> carries = vmPnL.getSquares().stream().filter(x -> x.getPromptDate().equals(maxPromptDate))
						.collect(Collectors.toList());

				Square carry = carries.get(0);

				if (carry != null) {
					String carryCurrency = carry.getCurrency();
					vmPnL.setPnL4Carry(new BigDecimal(carries.stream().mapToDouble(
							x -> x.getPriceShort().subtract(x.getPriceLong()).multiply(x.getQuantity()).doubleValue())
							.sum()));

					if (!carryCurrency.equals(currency)) {
						if (carryCurrency.equals("CNY"))
							vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().divide(rate));
						else
							vmPnL.setPnL4Carry(vmPnL.getPnL4Carry().multiply(rate));
					}
				}
			}
		}

		return new ActionResult<>(true, "试算成功", vmPnL);

	}

	@Override
	public ActionResult<InvoicePnL> InvoiceSettleOfficial(Param4InvoicePnL param4InvoicePnL) {

		ActionResult<InvoicePnL> actionResult = InvoiceSettleTrial(param4InvoicePnL);
		InvoicePnL obj = actionResult.getData();

		if (actionResult.isSuccess() == false || obj == null) {

			return new ActionResult<>(false, "试算失败，请检查");
		}

		List<Square> squares = obj.getSquares();

		if (squares != null) {

			if (squares.stream().filter(x -> x.getPromptDate() == null).collect(Collectors.toList()).size() > 0) {

				return new ActionResult<>(false, "头寸到期日错误，不符合正式结算的要求");
			}

			List<String> idList = squares.stream().map(Square::getId).collect(Collectors.toList());

			DetachedCriteria where = DetachedCriteria.forClass(Square.class);
			where.add(Restrictions.in("Id", idList));

			ProjectionList pList = Projections.projectionList();
			pList.add(Projections.groupProperty("MarketId"));
			pList.add(Projections.groupProperty("CommodityId"));
			pList.add(Projections.groupProperty("PromptDate"));
			pList.add(Projections.sum("QuantityLong"), "QuantityLong");
			pList.add(Projections.sum("QuantityShort"), "QuantityShort");
			where.setProjection(pList);

			List<Map<String, Object>> mapList = (List<Map<String, Object>>) squareRepo.getHibernateTemplate()
					.findByCriteria(where);

			List<Square> t = new ArrayList<>();

			mapList.forEach(map -> {

				Square s = new Square();
				s.setMarketId((String) map.get("MarketId"));
				s.setCommodityId((String) map.get("CommodityId"));
				s.setPromptDate((Date) map.get("PromptDate"));
				BigDecimal sum = ((BigDecimal) map.get("QuantityLong")).add((BigDecimal) map.get("QuantityShort"));
				s.setQuantity(sum);

				t.add(s);
			});

			if (t.stream().filter(x -> x.getQuantity().compareTo(BigDecimal.ZERO) != 0).toArray().length > 0) {

				return new ActionResult<>(false, "头寸没有全部对齐，不符合正式结算的要求");
			}
		}

		// ---------------------

		obj.setSquares(null);// 清空关联的明细，否则保存会失败

		String pnLId = invoicePnLRepo.SaveOrUpdateRetrunId(obj);

		if (squares != null) {

			for (Square invoicePnL4Position : squares) {
				Position split = invoicePnL4Position.getSplitSquarePosition(); // 临时存放的拆分头寸，必须自Square之前保存

				if (split != null) {
					split.setIsSquared(true);
					String splitId = positionRepo.SaveOrUpdateRetrunId(split);
					if (split.getLS().equals(LS.LONG))
						invoicePnL4Position.setLongId(splitId);
					else
						invoicePnL4Position.setShortId(splitId);

					invoicePnL4Position.setSplitSquarePosition(null);
				}
				invoicePnL4Position.setCreatedAt(new Date());
				invoicePnL4Position.setInvoicePnLId(pnLId);
				invoicePnL4Position.setSquareDate(new Date());

				squareRepo.SaveOrUpdate(invoicePnL4Position);
			}

		}

		// #region 如果结算成功，需要将有关的业务数据的IsSettled标志置为True
		for (Position position : param4InvoicePnL.getPositions()) {

			Position p = positionRepo.getOneById(position.getId(), Position.class);
			p.setQuantity(position.getQuantity());
			// p.IsSettled = true;
			p.setIsSquared(true);
			positionRepo.SaveOrUpdate(p);
		}

		obj.getStorageIns().forEach(s -> {

			Storage storage = storageRepo.getOneById(s.getId(), Storage.class);
			storage.setIsSettled(true);
			storage.setPnLId(pnLId);
			storageRepo.SaveOrUpdate(storage);

		});

		obj.getStorageOuts().forEach(s -> {

			Storage storage = storageRepo.getOneById(s.getId(), Storage.class);
			storage.setIsSettled(true);
			storage.setPnLId(pnLId);
			storageRepo.SaveOrUpdate(storage);

		});

		Invoice invoice = invoiceRepo.getOneById(obj.getInvoiceId(), Invoice.class);
		invoice.setIsSettled(true);
		invoiceRepo.SaveOrUpdate(invoice);

		return new ActionResult<>(true, "结算成功");
	}

	@Override
	public ActionResult<Invoice> InvalidInvoiceById(Invoice invoice) {

		try {

			Invoice inv = invoiceRepo.getOneById(invoice.getId(), Invoice.class);

			inv.setInvalidId(invoice.getInvalidId());
			inv.setInvalidBy(invoice.getInvalidBy());
			inv.setInvalidAt(invoice.getInvalidAt());
			inv.setInvoiceType(invoice.getInvoiceType());
			inv.setPFA(invoice.getPFA());
			invoiceRepo.SaveOrUpdate(inv);

			return new ActionResult<>(true, null, invoice);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public List<Invoice> Invoices4Sql(String sql) {

		List<Invoice> invoices = invoiceRepo.ExecuteCorrectlySql(sql, Invoice.class);

		for (Invoice invoice : invoices) {
			if (invoice == null || invoice.getId() == null)
				continue;
			if (invoice.getLot() != null) {
				invoice.setFullNo(invoice.getLot().getFullNo());
			} else if (invoice.getLotId() != null) {
				Lot lot = lotRepo.getOneById(invoice.getLotId(), Lot.class);
				if (lot != null) {
					invoice.setFullNo(lot.getFullNo());
				}
			}

			if (invoice.getCustomer() != null) {
				invoice.setCustomerName(invoice.getCustomer().getName());
			} else if (invoice.getCustomerId() != null) {
				Customer customer = customerRepo.getOneById(invoice.getCustomerId(), Customer.class);
				if (customer != null) {
					invoice.setCustomerName(customer.getName());
				}
			}
			if (invoice.getLegal() != null) {
				invoice.setLegalName(invoice.getLegal().getName());
			} else if (invoice.getLegalId() != null) {
				Legal legal = legalRepo.getOneById(invoice.getLegalId(), Legal.class);
				if (legal != null) {
					invoice.setLegalName(legal.getName());
				}
			}

		}
		return invoices;
	}

	@Override
	public List<Lot> LotsById(String lotId) {

		return storageService.LotsById(lotId);
	}

	@Override
	public List<Invoice> Invoices(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {
		return invoiceRepo.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	@Override
	public List<CInvoice> Invoices2(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {
		return cInvoiceRepo.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	public ActionResult<String> initVerify(Invoice invoice) {

		if (!invoice.getPFA().equalsIgnoreCase("P")) {

			Lot lot = lotRepo.getOneById(invoice.getLotId(), Lot.class);

			DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);

			Disjunction djc = Restrictions.disjunction();
			djc.add(Restrictions.eq("LotId", invoice.getLotId()));
			where.add(djc);
			where.add(Restrictions.or(Restrictions.eq("PFA", ActionStatus.InvoiceType_Final),
					Restrictions.eq("PFA", ActionStatus.InvoiceType_Adjust)));

			List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			BigDecimal sumQuantity = BigDecimal.ZERO;
			for (Invoice in : invoices) {
				sumQuantity = sumQuantity.add(in.getQuantity());
			}

			BigDecimal _Quantity = lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal();

			QuantityMaL lotQuantity = commonService.getQuantityMoreorLess(lot.getMoreOrLessBasis(), _Quantity,
					lot.getMoreOrLess());

			if (DecimalUtil.nullToZero(lot.getAmountFunded())
					.compareTo(DecimalUtil.nullToZero(lot.getAmountInvoiced())) >= 0) {
				lot.setIsFunded(Boolean.TRUE);
			}

			boolean isInvoiced = lotQuantity.getQuantityLess().compareTo(sumQuantity.abs()) <= 0
					&& sumQuantity.abs().compareTo(lotQuantity.getQuantityMore()) <= 0;
			lot.setIsInvoiced(isInvoiced);

			fundService.updateFundOfLotByInvoice(lot, new Fund());
			lotRepo.SaveOrUpdate(lot);
		}
		return new ActionResult<>(true, "发票审核成功");
	}

	@Override
	@Transactional
	public ActionResult<List<Invoice>> StorageToInvoiceSave(List<Invoice> invoices) {
		LoginInfoToken userInfo = LoginHelper.GetLoginInfo();
		for (Invoice invoice : invoices) {
			if (invoice.getId() != null) {
				invoice.setUpdatedId(userInfo.getUserId());
			} else {
				invoice.setCreatedId(userInfo.getUserId());
			}
			ActionResult<Invoice> result = Save(invoice);
			if (!result.isSuccess()) {
				throw new RuntimeException(result.getMessage());
			}

		}
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, invoices);
	}

	@Override
	public ActionResult<List<Invoice>> StoragesInvoiceById(String invoiceId) {
		Invoice invoice = invoiceRepo.getOneById(invoiceId, Invoice.class);
		String invoiceNo = invoice.getInvoiceNo();
		int separateIndex = invoiceNo.indexOf("/");
		if (separateIndex == -1) {
			return new ActionResult<>(false, "参数有误!");
		}
		List<Invoice> list = invoiceRepo.GetQueryable(Invoice.class).where(DetachedCriteria.forClass(Invoice.class)
				.add(Restrictions.like("InvoiceNo", invoiceNo.substring(0, separateIndex) + "%"))).toList();

		return new ActionResult<>(true, "", list);
	}
}
