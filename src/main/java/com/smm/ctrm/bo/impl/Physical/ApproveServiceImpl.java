package com.smm.ctrm.bo.impl.Physical;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Common.LotCommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.bo.Physical.ApproveService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.PendingType;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class ApproveServiceImpl implements ApproveService {

	@Autowired
	private HibernateRepository<Approve> repository;

	@Autowired
	private HibernateRepository<Customer> customerRepository;

	@Autowired
	private HibernateRepository<Pending> pendingRepository;

	@Autowired
	private HibernateRepository<User> userRepository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<Fund> fundRepo;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepository;

	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	LotCommonService lotCommonService;

	@Autowired
	CommonService commonService;

	@Autowired
	ReceiptService receiptService;

	@Autowired
	FundService fundService;

	@Override
	public Criteria GetCriteria() {
		return this.repository.CreateCriteria(Approve.class);

	}

	@Override
	public ActionResult<List<Approve>> Approves() {

		ActionResult<List<Approve>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.repository.GetList(Approve.class));

		return result;
	}

	@Override
	public ActionResult<Approve> GetById(String id) {

		ActionResult<Approve> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(this.repository.getOneById(id, Approve.class));

		return result;
	}

	@Override
	public ActionResult<List<Approve>> ApproveHistoryByCustomerId(String customerId) {

		DetachedCriteria where = DetachedCriteria.forClass(Approve.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CustomerId", customerId));

		List<Approve> list = this.repository.GetQueryable(Approve.class).where(where).toList();

		ActionResult<List<Approve>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(assembleList(list));

		return result;
	}

	@Override
	public ActionResult<List<Approve>> ApproveHistoryByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Approve.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Approve> list = this.repository.GetQueryable(Approve.class).where(where).toList();

		ActionResult<List<Approve>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(assembleList(list));

		return result;
	}

	@Override
	public ActionResult<List<Approve>> ApproveHistoryByFundId(String fundId) {

		DetachedCriteria where = DetachedCriteria.forClass(Approve.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("FundId", fundId));

		List<Approve> list = this.repository.GetQueryable(Approve.class).where(where).toList();

		ActionResult<List<Approve>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(assembleList(list));

		return result;
	}

	@Override
	public ActionResult<String> ApproveCustomer4Winform(Approve approve) {
		String approverId = LoginHelper.GetLoginInfo().getUserId();
		updateCreatedIdOrUpdatedId(approve);
		if (approve == null || StringUtils.isBlank(approve.getPendingId())) {
			return new ActionResult<String>(false, "参数不全: approve.PendingId");
		}

		Pending pending = this.pendingRepository.getOneById(approve.getPendingId(), Pending.class);
		if (pending.getCustomerId() == null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("参数不全: pending.CustomerId");
			return tempVar2;
		}

		Customer customer = this.customerRepository.getOneById(pending.getCustomerId(), Customer.class);
		if (customer == null) {
			this.pendingRepository.PhysicsDelete(approve.getPendingId(), Pending.class);
			return new ActionResult<String>(Boolean.FALSE, "该客户已经被删除");
		}

		if (customer.getStatus() - Status.Pending != 0) {
			pending.setIsDone(true);
			this.pendingRepository.SaveOrUpdate(pending);
			return new ActionResult<String>(false, String.format("客户: %s已经审核，不可以重复审核。", customer.getName()));
		}

		User approver = userRepository.getOneById(approverId, User.class);
		if (approver.getIsCustomerTerminator()) {
			customer.setIsApproved(approve.getIsApproved());
			customer.setStatus(approve.getIsApproved() ? Status.Agreed : Status.Deny);
			this.customerRepository.SaveOrUpdate(customer);
		} else {
			if (!approve.getIsApproved()) {
				customer.setIsApproved(false);
				customer.setStatus(Status.Deny);
				this.customerRepository.SaveOrUpdate(customer);
			}
		}

		if (!approve.getIsApproved()) {
			RemoveCustomerPending4Others(customer.getId(), pending.getId());
		}

		approve.setCustomerId(pending.getCustomerId());
		approve.setApproverId(approverId);
		System.out.println("");
		repository.SaveOrUpdateRetrunId(approve);

		updatePendingByApprove(approve, pending);

		return new ActionResult<String>(true, "客户审核成功");
	}

	private void updateCreatedIdOrUpdatedId(Approve approve) {
		if (StringUtils.isBlank(approve.getId())) {
			approve.setCreatedId(LoginHelper.GetLoginInfo().getUserId());
		} else {
			approve.setUpdatedId(LoginHelper.GetLoginInfo().getUserId());
		}

	}

	/**
	 * 订单审批
	 * 
	 * @param approve
	 * @param approverId
	 * @return
	 */
	@Override
	public ActionResult<String> ApproveContract4Winform(Approve approve) {
		String approverId = LoginHelper.GetLoginInfo().getUserId();
		updateCreatedIdOrUpdatedId(approve);

		if (approve == null || approve.getPendingId() == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("参数不全: approve.PendingId");
			return tempVar;
		}

		Pending pending = this.pendingRepository.getOneById(approve.getPendingId(), Pending.class);
		if (pending.getContractId() == null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("参数不全: pending.ContractId");
			return tempVar2;
		}

		Contract contract = this.contractRepository.getOneById(pending.getContractId(), Contract.class);

		if (contract == null) {
			this.pendingRepository.PhysicsDelete(approve.getPendingId(), Pending.class);
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("该合同已经被删除");
			return tempVar3;
		}

		if (contract.getStatus() != Status.Pending) {
			pending.setIsDone(true);
			this.pendingRepository.SaveOrUpdate(pending);

			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(String.format("合同%s已经审核，不可以重复审核。", contract.getHeadNo()));
			return tempVar4;
		}

		User approver = this.userRepository.getOneById(approverId, User.class);
		if (approver.getIsContractTerminator()) {
			contract.setIsApproved(approve.getIsApproved());
			contract.setStatus(approve.getIsApproved() ? Status.Agreed : Status.Deny);

			/**
			 * 增加了关于"订单修正"的逻辑(withhold = true即修正客户,则不执行覆盖和隐藏动作）
			 */

			if (approve.getIsApproved()) {
				if (contract.getContractAmendId() != null && !contract.getWithHold()) {
					/**
					 * 如果订单修正获批，新订单的条款赋值给原订单
					 */
					Contract contractAmended = this.contractRepository.getOneById(contract.getContractAmendId(),
							Contract.class);
					if (contractAmended != null) {
						contractAmended.setQuantity(contract.getQuantity());
						contractAmended.setDeliveryTerm(contract.getDeliveryTerm());
						contractAmended.setPaymentTerm(contract.getPaymentTerm());
						contractAmended.setDocumentNo(contract.getDocumentNo());
						contractAmended.setProduct(contract.getProduct());

						contract.setIsHidden(true); // 修订的合同隐藏掉

						if (!contractAmended.getHeadNo().substring(contractAmended.getHeadNo().length() - 1,
								contractAmended.getHeadNo().length()).equals("A")) {
							contractAmended.setHeadNo(contractAmended.getHeadNo() + "A");
						}

						this.contractRepository.SaveOrUpdate(contractAmended);
					}
				}
			}
			this.contractRepository.SaveOrUpdate(contract);
		} else {
			if (!approve.getIsApproved()) {
				contract.setIsApproved(false);
				contract.setStatus(Status.Deny);
				this.contractRepository.SaveOrUpdate(contract);
			}
		}

		if (!approve.getIsApproved()) // approver.IsPaymentTerminator
										// || 2015-06-03 终审后仍然保留原记录
		{
			RemoveContractPending4Others(contract.getId(), pending.getId());
		}

		// 因为增加了Lot.Status，更新这个值
		Criteria criteria = this.lotRepo.CreateCriteria(Lot.class);
		criteria.add(Restrictions.eq("ContractId", contract.getId()));
		List<Lot> lots = this.lotRepo.GetList(criteria);

		for (Lot lot : lots) {
			lot.setStatus(contract.getStatus());
			this.lotRepo.SaveOrUpdate(lot);
		}

		approve.setContractId(pending.getContractId());
		approve.setApproverId(approverId);
		this.repository.SaveOrUpdateRetrunId(approve);

		updatePendingByApprove(approve, pending);

		ActionResult<String> tempVar5 = new ActionResult<String>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage("订单审核成功");
		return tempVar5;
	}

	/**
	 * 更新 IsDone为true，ApproveTradeDate为当前时间，IsApproved为审批结果，ApproveComments为审批意见
	 * 
	 * @param approve
	 * @param pending
	 */
	private void updatePendingByApprove(Approve approve, Pending pending) {
		// 将原来的pending的IsDone 置为true
		pending.setIsDone(true);
		pending.setApproveId(approve.getId());
		pending.setIsApproved(approve.getIsApproved());
		this.pendingRepository.SaveOrUpdate(pending);
	}

	/**
	 * 付款审批
	 * 
	 * @param approve
	 * @param approverId
	 * @return
	 */
	@Override
	public ActionResult<String> ApprovePayment4Winform(Approve approve) {
		String approverId = LoginHelper.GetLoginInfo().getUserId();
		updateCreatedIdOrUpdatedId(approve);
		if (approve == null || approve.getPendingId() == null) {
			return new ActionResult<>(false, "参数不全: approve.PendingId。");
		}

		Pending pending = this.pendingRepository.getOneById(approve.getPendingId(), Pending.class);
		if (pending.getFundId() == null) {
			return new ActionResult<>(false, "参数不全: pending.FundId。");
		}

		Fund fund = this.fundRepo.getOneById(pending.getFundId(), Fund.class);
		if (fund == null) {
			this.pendingRepository.PhysicsDelete(approve.getFundId(), Pending.class);
			return new ActionResult<>(false, "该付款已经被删除。");
		}

		if (fund.getStatus() == Status.Agreed || fund.getStatus() == Status.Deny) {
			pending.setIsDone(true);
			this.pendingRepository.SaveOrUpdate(pending);
			return new ActionResult<>(false, "该付款已经审核，不需要重复审核。");
		}

		User approver = this.userRepository.getOneById(approverId, User.class);
		if (approver.getIsPaymentTerminator()) {
			fund.setIsApproved(approve.getIsApproved());
			fund.setStatus(approve.getIsApproved() ? Status.Agreed : Status.Deny);
			this.fundRepo.SaveOrUpdate(fund);
		} else {
			if (!approve.getIsApproved()) {
				fund.setIsApproved(false);
				fund.setStatus(Status.Deny);
				this.fundRepo.SaveOrUpdate(fund);
			}
		}
		/**
		 * approver.IsPaymentTerminator 2015-06-03 终审后仍然保留原记录
		 */
		if (!approve.getIsApproved()) {
			/**
			 * 同步更新发票的累计申请付款金额
			 */
			if (pending.getPendingType() == PendingType.Pay4Invoice) {
				Criteria criteria = this.fundRepo.CreateCriteria(Fund.class);
				criteria.add(Restrictions.and(Restrictions.eq("InvoiceId", fund.getInvoiceId()),
						Restrictions.ne("Status", Status.Deny)));
				List<Fund> funds = this.fundRepo.GetList(criteria);
				Invoice invoice = this.invoiceRepository.getOneById(fund.getInvoiceId(), Invoice.class);
				if (invoice != null) {

					BigDecimal amountDrafted = new BigDecimal(0);
					BigDecimal quantityDrafted = new BigDecimal(0);
					for (Fund cal : funds) {
						amountDrafted = amountDrafted.add(cal.getAmount());
						quantityDrafted = quantityDrafted.add(cal.getQuantity());
					}
					invoice.setAmountDrafted(amountDrafted);
					invoice.setQuantityDrafted(quantityDrafted);
					this.invoiceRepository.SaveOrUpdate(invoice);
				}
			} else if (pending.getPendingType() == PendingType.Pay4Lot) {

				Lot lot = lotRepo.GetQueryable(Lot.class)
						.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("Id", fund.getLotId())))
						.firstOrDefault();
				if (lot == null) {
					return new ActionResult<>(false, "获取数据失败！");
				}
				if (!approve.getIsApproved()) {
					RemovePaymentPending4Others(fund.getId(), pending.getId());
				}
			}
		}

		approve.setFundId(pending.getFundId());
		approve.setApproverId(approverId);
		this.repository.SaveOrUpdateRetrunId(approve);

		updatePendingByApprove(approve, pending);

		return new ActionResult<>(true, "付款审核成功。");

	}

	@Override
	public List<Approve> Approves(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String orderBy,
			String orderSort) {

		return this.repository.GetPage(criteria, pageSize, pageIndex, orderBy, orderSort, total).getData();
	}

	private void RemoveCustomerPending4Others(String customerId, String pendingId) {
		if (customerId == null)
			return;

		Criteria criteria = this.pendingRepository.CreateCriteria(Pending.class);
		criteria.add(Restrictions.and(Restrictions.eq("CustomerId", customerId), Restrictions.ne("Id", pendingId)));
		List<Pending> pendings = this.pendingRepository.GetList(criteria);

		for (Pending pending : pendings) {
			this.pendingRepository.PhysicsDelete(pending.getId(), Pending.class);
		}

	}

	private void RemoveContractPending4Others(String contractId, String pendingId) {
		if (contractId == null)
			return;
		Criteria criteria = this.pendingRepository.CreateCriteria(Pending.class);
		criteria.add(Restrictions.and(Restrictions.eq("ContractId", contractId), Restrictions.ne("Id", pendingId)));
		List<Pending> pendings = this.pendingRepository.GetList(criteria);

		for (Pending pending : pendings) {
			this.pendingRepository.PhysicsDelete(pending.getId(), Pending.class);
		}
	}

	private void RemovePaymentPending4Others(String fundId, String pendingId) {
		if (fundId == null) {
			return;
		}
		Criteria criteria = this.pendingRepository.CreateCriteria(Pending.class);
		criteria.add(Restrictions.and(Restrictions.eq("FundId", fundId), Restrictions.ne("Id", pendingId)));
		List<Pending> pendings = this.pendingRepository.GetList(criteria);
		for (Pending pending : pendings) {
			this.pendingRepository.PhysicsDelete(pending.getId(), Pending.class);
		}
	}

	private void RemoveInvoicePending4Others(String invoiceId, String pendingId) {
		if (invoiceId == null) {
			return;
		}
		Criteria criteria = this.pendingRepository.CreateCriteria(Pending.class);
		criteria.add(Restrictions.and(Restrictions.eq("InvoiceId", invoiceId), Restrictions.ne("Id", pendingId)));
		List<Pending> pendings = this.pendingRepository.GetList(criteria);
		for (Pending pending : pendings) {
			this.pendingRepository.PhysicsDelete(pending.getId(), Pending.class);
		}
	}

	private void RemoveReceiptShipPending4Others(String invoiceId, String pendingId) {
		if (invoiceId == null) {
			return;
		}
		Criteria criteria = this.pendingRepository.CreateCriteria(Pending.class);
		criteria.add(Restrictions.and(Restrictions.eq("ReceiptShipId", invoiceId), Restrictions.ne("Id", pendingId)));
		List<Pending> pendings = this.pendingRepository.GetList(criteria);
		for (Pending pending : pendings) {
			this.pendingRepository.PhysicsDelete(pending.getId(), Pending.class);
		}
	}

	@Override
	public ActionResult<String> ApproveMakeShip4Winform(Approve approve) {
		String approverId = LoginHelper.GetLoginInfo().getUserId();
		updateCreatedIdOrUpdatedId(approve);

		if (approve == null || approve.getPendingId() == null) {
			ActionResult<String> tempVar = new ActionResult<String>();
			tempVar.setSuccess(false);
			tempVar.setMessage("参数不全: approve.PendingId");
			return tempVar;
		}

		Pending pending = this.pendingRepository.getOneById(approve.getPendingId(), Pending.class);
		if (pending.getReceiptShipId() == null) {
			ActionResult<String> tempVar2 = new ActionResult<String>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("参数不全: pending.ReceiptShipId");
			return tempVar2;
		}

		ReceiptShip receiptShip = this.receiptShipRepo.getOneById(pending.getReceiptShipId(), ReceiptShip.class);

		if (receiptShip == null) {
			this.pendingRepository.PhysicsDelete(approve.getPendingId(), Pending.class);
			ActionResult<String> tempVar3 = new ActionResult<String>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("该发货单已经被删除");
			return tempVar3;
		}

		if (receiptShip.getStatus() != Status.Pending) {
			pending.setIsDone(true);
			this.pendingRepository.SaveOrUpdate(pending);

			ActionResult<String> tempVar4 = new ActionResult<String>();
			tempVar4.setSuccess(false);
			tempVar4.setMessage(String.format("发货单%s已经审核，不可以重复审核。", receiptShip.getReceiptShipNo()));
			return tempVar4;
		}

		List<Storage> haveReceievedList = receiptService.getStorageByReceiptShipId(receiptShip.getId());
		receiptShip.setStorages(haveReceievedList);
		Lot lot = lotRepo.getOneById(receiptShip.getLotId(), Lot.class);
		int compareResult = lotCommonService.isLotDeliveredFinishedNormally(lot, receiptShip, Boolean.TRUE,
				Boolean.TRUE);
		User approver = this.userRepository.getOneById(approverId, User.class);
		if (compareResult > 0 && approve.getIsApproved()) {

			return new ActionResult<>(Boolean.FALSE, "收货数量过高！");
		} else if (compareResult == 0 && approver.getIsReceiptShipTerminator() && approve.getIsApproved()) {
			lot.setIsDelivered(Boolean.TRUE);
			lotRepo.SaveOrUpdate(lot);
		}

		if (approver.getIsReceiptShipTerminator()) {
			receiptShip.setIsApproved(approve.getIsApproved());
			receiptShip.setStatus(approve.getIsApproved() ? Status.Agreed : Status.Deny);
			this.receiptShipRepo.SaveOrUpdate(receiptShip);
			lot.setQuantityDelivered(lotCommonService.getLotDeliveredQuantity(lot.getId(), receiptShip.getId(),
					Boolean.TRUE, Boolean.TRUE));
			fundService.updateFundOfLotByStorage(lot, new Fund());
			lotRepo.SaveOrUpdate(lot);
		} else {
			if (!approve.getIsApproved()) {
				receiptShip.setIsApproved(false);
				receiptShip.setStatus(Status.Deny);
				this.receiptShipRepo.SaveOrUpdate(receiptShip);
			}
		}

		approve.setReceiptShipId(pending.getReceiptShipId());
		approve.setApproverId(approverId);
		this.repository.SaveOrUpdateRetrunId(approve);

		updatePendingByApprove(approve, pending);

		if (!approve.getIsApproved()) {
			RemoveReceiptShipPending4Others(receiptShip.getId(), pending.getId());
		}

		ActionResult<String> tempVar5 = new ActionResult<String>();
		tempVar5.setSuccess(true);
		tempVar5.setMessage("发货单审核成功");
		return tempVar5;
	}

	@Override
	public ActionResult<List<Approve>> ApproveHistoryByReceiptShipId(String receiptShipId) {
		DetachedCriteria where = DetachedCriteria.forClass(Approve.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ReceiptShipId", receiptShipId));

		List<Approve> list = this.repository.GetQueryable(Approve.class).where(where).toList();

		ActionResult<List<Approve>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	@Transactional
	public ActionResult<String> ApproveInvoice4Winform(Approve approve) {
		String approverId = LoginHelper.GetLoginInfo().getUserId();
		updateCreatedIdOrUpdatedId(approve);
		if (approve == null || approve.getPendingId() == null) {
			return new ActionResult<>(false, "参数不全: approve.PendingId");
		}
		Pending pending = this.pendingRepository.getOneById(approve.getPendingId(), Pending.class);
		if (pending.getInvoiceId() == null) {
			return new ActionResult<>(false, "参数不全: pending.InvoiceId");
		}
		Invoice invoice = invoiceRepository.getOneById(pending.getInvoiceId(), Invoice.class);
		
		if (invoice == null) {
			this.pendingRepository.PhysicsDelete(approve.getPendingId(), Pending.class);
			return new ActionResult<>(false, "该发票已经被删除");
		}

		if (invoice.getStatus() != Status.Pending) {
			pending.setIsDone(true);
			this.pendingRepository.SaveOrUpdate(pending);
			return new ActionResult<>(false, String.format("发票%s已经审核，不可以重复审核。", invoice.getInvoiceNo()));
		}
		
		String invoiceNo = invoice.getInvoiceNo();
		int separateIndex = invoiceNo.indexOf("/");
		List<Invoice> invoiceList = new ArrayList<>();
		if(separateIndex > 0) {
			invoiceList = invoiceRepository.GetQueryable(Invoice.class).where(DetachedCriteria.forClass(Invoice.class)
					.add(Restrictions.like("InvoiceNo", invoiceNo.substring(0, separateIndex) + "%"))).toList();
		} else {
			invoiceList.add(invoice);
		}
		User approver = this.userRepository.getOneById(approverId, User.class);
		if (approver.getIsInvoiceTerminator()) {

			for(Invoice inv : invoiceList) {
				if (!inv.getPFA().equalsIgnoreCase("P")) {

					Lot lot = lotRepo.getOneById(inv.getLotId(), Lot.class);

					DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);

					Disjunction djc = Restrictions.disjunction();
					djc.add(Restrictions.eq("LotId", inv.getLotId()));
					where.add(djc);
					where.add(Restrictions.or(Restrictions.eq("PFA", ActionStatus.InvoiceType_Final),
							Restrictions.eq("PFA", ActionStatus.InvoiceType_Adjust)));

					List<Invoice> invoices = this.invoiceRepository.GetQueryable(Invoice.class).where(where).toList();

					BigDecimal sumQuantity = BigDecimal.ZERO;
					for (Invoice in : invoices) {
						sumQuantity = sumQuantity.add(in.getQuantity());
					}

					BigDecimal _Quantity = lot.getQuantityOriginal() == null ? lot.getQuantity()
							: lot.getQuantityOriginal();

					QuantityMaL lotQuantity = commonService.getQuantityMoreorLess(lot.getMoreOrLessBasis(), _Quantity,
							lot.getMoreOrLess());

					if (DecimalUtil.nullToZero(lot.getAmountFunded())
							.compareTo(DecimalUtil.nullToZero(lot.getAmountInvoiced())) >= 0) {
						lot.setIsFunded(Boolean.TRUE);
					}

					boolean isInvoiced = lotQuantity.getQuantityLess().compareTo(sumQuantity.abs()) <= 0
							&& sumQuantity.abs().compareTo(lotQuantity.getQuantityMore()) <= 0;
					lot.setIsInvoiced(isInvoiced);

					lotRepo.SaveOrUpdate(lot);
				}
			}

			if (!approve.getIsApproved()) {
				for(Invoice inv : invoiceList) {
					RemoveInvoicePending4Others(inv.getId(), pending.getId());
				}
			}
			for(Invoice inv : invoiceList) {
				inv.setIsApproved(approve.getIsApproved());
				inv.setStatus(approve.getIsApproved() ? Status.Agreed : Status.Deny);
				this.invoiceRepository.SaveOrUpdate(inv);
			}
		} else {
			if (!approve.getIsApproved()) {
				for(Invoice inv : invoiceList) {
					inv.setIsApproved(false);
					inv.setStatus(Status.Deny);
					this.invoiceRepository.SaveOrUpdate(inv);
				}
			}
		}

		if (!approve.getIsApproved()) {
			for(Invoice inv : invoiceList) {
				RemoveInvoicePending4Others(inv.getId(), pending.getId());
			}
		}

		approve.setInvoiceId(pending.getInvoiceId());
		approve.setApproverId(approverId);
		this.repository.SaveOrUpdateRetrunId(approve);

		updatePendingByApprove(approve, pending);
		return new ActionResult<>(true, "开票申请审核完成");
	}

	public List<Approve> assembleList(List<Approve> list) {

		if (list == null || list.size() == 0)
			return new ArrayList<>();

		List<String> pendingIds = new ArrayList<>();
		List<String> askerIds = new ArrayList<>();
		list.forEach(a -> {
			if (a.getPendingId() != null) {
				pendingIds.add(a.getPendingId());
			}
		});
		if (pendingIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Pending.class);
			dc.add(Restrictions.in("Id", pendingIds));
			List<Pending> pendings = this.pendingRepository.GetQueryable(Pending.class).where(dc).toList();
			if (pendings != null && pendings.size() > 0) {
				list.forEach(a -> {
					pendings.forEach(p -> {
						if (p.getId().equals(a.getPendingId())) {
							a.setPending(p);
							if (p.getAskerId() != null) {
								askerIds.add(p.getAskerId());
							}
						}
					});
				});
			}
		}
		if (askerIds.size() > 0) {
			DetachedCriteria dc2 = DetachedCriteria.forClass(User.class);
			dc2.add(Restrictions.in("Id", askerIds));
			List<User> users = this.userRepository.GetQueryable(User.class).where(dc2).toList();
			if (users != null && users.size() > 0) {
				list.forEach(a -> {
					users.forEach(u -> {
						if (a.getPending() != null && u.getId().equals(a.getPending().getAskerId())) {
							a.getPending().setAsker(u);
						}
					});
				});
			}
		}
		return list;
	}

	@Override
	public ActionResult<List<Approve>> ApproveHistoryByInvoiceId(String invoiceId) {
		Invoice invoice = invoiceRepository.getOneById(invoiceId, Invoice.class);
		String invoiceNo = invoice.getInvoiceNo();
		List<String> invoiceIdList = new ArrayList<>();
		int separateIndex = invoiceNo.indexOf("/");
		
		if(separateIndex > -1) {
			List<Invoice> list = invoiceRepository.GetQueryable(Invoice.class)
					.where(DetachedCriteria.forClass(Invoice.class)
							.add(Restrictions.like("InvoiceNo", invoiceNo.substring(0, separateIndex) + "%")))
					.toList();
			for (Invoice inv : list) {
				invoiceIdList.add(inv.getId());
			}
		} else {
			invoiceIdList.add(invoiceId);
		}
		
		List<Approve> approveList = repository.GetQueryable(Approve.class)
				.where(DetachedCriteria.forClass(Approve.class).add(Restrictions.in("InvoiceId", invoiceIdList)))
				.toList();

		return new ActionResult<>(true, "", approveList);
	}

}
