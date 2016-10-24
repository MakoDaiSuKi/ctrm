package com.smm.ctrm.bo.impl.Physical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.PendingService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Division;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Physical.Approve;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
public class PendingServiceImpl implements PendingService {

	private static final Logger logger = Logger.getLogger(PendingServiceImpl.class);

	@Autowired
	private HibernateRepository<Pending> repository;

	@Autowired
	private HibernateRepository<Customer> customerHibernateRepository;

	@Autowired
	private HibernateRepository<Pending> pendingHibernateRepository;

	@Autowired
	private HibernateRepository<Contract> contractHibernateRepository;

	@Autowired
	private HibernateRepository<Fund> fundHibernateRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepo;

	@Autowired
	private HibernateRepository<User> userRepo;

	@Autowired
	private HibernateRepository<Legal> legalRepo;

	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleRepo;

	@Autowired
	private HibernateRepository<Commodity> commodityRepo;

	@Autowired
	private HibernateRepository<Division> divisionRepo;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<Approve> approveRepo;
	
	@Autowired
	private HibernateRepository<Storage> storageRepo;
	
	@Autowired
	private HibernateRepository<PricingRecord> pricingRecordRepo;
	

	@Override
	public ActionResult<List<Pending>> PendingsByCustomerId(String customerId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CustomerId", customerId));

		List<Pending> list = this.repository.GetQueryable(Pending.class).where(where).toList();

		list = this.commonService.SimplifyDataPendingList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public boolean CancelByCustomer(Customer impCustomer, String userId, StringBuilder message) {

		Customer customer = this.customerHibernateRepository.getOneById(impCustomer.getId(), Customer.class);

		if (customer == null) {
			message.append("业务对象已经不存在");
			return false;
		}

		if (!customer.getCreatedId().equalsIgnoreCase(userId)) {
			message.append("不可以撤销他人的申请业务");
			return false;
		}

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CustomerId", impCustomer.getId()));
		List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

		if (impCustomer.getPendings() == null || impCustomer.getPendings().size() == 0) {
			
			
			Pending obj = pendings.stream().filter(p->p.getIsDone()).findFirst().orElse(null);
			if (obj != null) {
				message.append("禁止撤销，因为已经存在审核记录。");
				return false;
			}

			pendings.forEach(pendingHibernateRepository::PhysicsDelete);

			customer.setStatus(Status.Draft);

			customerHibernateRepository.SaveOrUpdate(customer);
		} else {
			boolean hasRemain = false;

			for (Pending pending : pendings) {
				long number = impCustomer.getPendings().stream().filter(p -> p.getId().equals(pending.getId())).count();

				if (!pending.getIsDone() && number > 0) {
					pendingHibernateRepository.PhysicsDelete(pending);
				} else {
					hasRemain = true;
				}
			}

			if (!hasRemain) {
				customer.setStatus(Status.Draft);
			}
			customerHibernateRepository.SaveOrUpdate(customer);
		}

		message.append("撤销成功。");

		return true;
	}

	@Override
	public ActionResult<String> CancelByCustomerId(String customerId, String userId) {

		Customer customer = customerHibernateRepository.getOneById(customerId, Customer.class);

		if (customer == null)
			return new ActionResult<>(false, "业务对象已经不存在");

		if (!customer.getCreatedId().equalsIgnoreCase(userId))
			return new ActionResult<>(false, "不可以撤销他人的申请业务");

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("CustomerId", customerId));
		where.add(Restrictions.eq("IsDone", true));
		List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

		Pending obj = null;

		if (pendings.size() > 0) {
			obj = pendings.get(0);
		}

		if (obj != null)
			return new ActionResult<>(false, "禁止撤销，因为已经存在审核记录");

		pendings.forEach(pendingHibernateRepository::PhysicsDelete);

		customer.setStatus(Status.Draft);
		customerHibernateRepository.SaveOrUpdate(customer);

		return new ActionResult<>(true, "撤销成功");
	}

	@Override
	public boolean CancelByContract(Contract impContract, String userId, StringBuilder message) {

		

		Contract contract = this.contractHibernateRepository.getOneById(impContract.getId(), Contract.class);

		if (contract == null) {
			message.append("业务对象已经不存在");
			return false;
		}

		//判段操作人是否是管理员 by zengshihua 2016/09/06
		User user=this.userRepo.getOneById(userId,User.class);
		if(user.getIsSysAdmin()){
			//检查订单所有批次是否已经发生业务
			List<Lot> lots=this.lotRepo.GetQueryable(Lot.class)
					.where(DetachedCriteria.forClass(Lot.class)
							.add(Restrictions.eq("ContractId", contract.getId())))
					.toList();
			
			boolean flag=false;
			if(lots!=null&&lots.size()>0){
				
				List<String> ids=new ArrayList<>();
				for (Lot l : lots) {
					ids.add(l.getId());
				}
				
				//点价记录PricingRecord
				DetachedCriteria dc1=DetachedCriteria.forClass(PricingRecord.class);
				dc1.add(Restrictions.in("LotId", ids));
				dc1.setProjection(Projections.rowCount());
				long prCount=(Long) this.pricingRecordRepo.getHibernateTemplate().findByCriteria(dc1).get(0);
				
				//发票记录Invoice
				DetachedCriteria dc2=DetachedCriteria.forClass(Invoice.class);
				dc2.add(Restrictions.in("LotId", ids));
				dc2.setProjection(Projections.rowCount());
				long iCount=(Long) this.invoiceRepo.getHibernateTemplate().findByCriteria(dc2).get(0);
				
				//保值记录Position
				DetachedCriteria dc3=DetachedCriteria.forClass(Position.class);
				dc3.add(Restrictions.in("LotId", ids));
				dc3.setProjection(Projections.rowCount());
				long pCount=(Long) this.invoiceRepo.getHibernateTemplate().findByCriteria(dc3).get(0);
				
				//收/发货记录ReceiptShip
				DetachedCriteria dc4=DetachedCriteria.forClass(ReceiptShip.class);
				dc4.add(Restrictions.in("LotId", ids));
				dc4.setProjection(Projections.rowCount());
				long rCount=(Long) this.receiptShipRepo.getHibernateTemplate().findByCriteria(dc4).get(0);
				
				//付款记录Fund
				DetachedCriteria dc5=DetachedCriteria.forClass(Fund.class);
				dc5.add(Restrictions.in("LotId", ids));
				dc5.setProjection(Projections.rowCount());
				long fCount=(Long) this.fundHibernateRepository.getHibernateTemplate().findByCriteria(dc5).get(0);
				
				if(prCount>0||iCount>0||pCount>0||rCount>0||fCount>0){
					flag=true;
				}
			}
			
			if(flag){
				message.append("已经发生业务，不能撤销.");
				return false;
			}else{
				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("ContractId", impContract.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
				}
				contract.setStatus(Status.Draft);
				contract.setIsApproved(false);
				contractHibernateRepository.SaveOrUpdate(contract);
			}
			
		}else{
			
			if (!contract.getCreatedId().equalsIgnoreCase(userId)) {
				message.append("不可以撤销他人的申请业务");
				return false;
			}

			if (impContract.getPendings() == null || impContract.getPendings().size() == 0) {

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("ContractId", impContract.getId()));
				//where.add(Restrictions.eq("IsDone", true));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();
				Pending obj = pendings.stream().filter(p->p.getIsDone()).findFirst().orElse(null);
				if (obj != null) {
					message.append("禁止撤销，因为已经存在审核记录。");
					return false;
				}

				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);

				}
				contract.setStatus(Status.Draft);
				contractHibernateRepository.SaveOrUpdate(contract);
			} else {
				boolean hasRemain = false;

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("ContractId", impContract.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					if (!pending.getIsDone() && impContract.getPendings().stream().anyMatch(x -> x.getId().equalsIgnoreCase(pending.getId()))) {
						pendingHibernateRepository.PhysicsDelete(pending);
					} else {
						hasRemain = true;
					}
				}

				if (!hasRemain) {
					contract.setStatus(Status.Draft);
					contractHibernateRepository.SaveOrUpdate(contract);
				}
			}
		}

		message.append("撤销成功");
		return true;
	}

	@Override
	public ActionResult<String> CancelByContractId(String contractId, String userId) {

		Contract contract = this.contractHibernateRepository.getOneById(contractId, Contract.class);

		if (contract == null)
			return new ActionResult<>(false, "业务对象已经不存在");

		if (!contract.getCreatedId().equalsIgnoreCase(userId))
			return new ActionResult<>(false, "不可以撤销他人的申请业务");

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));
		List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

		Pending obj = pendings.stream().filter(Pending::getIsDone).collect(Collectors.toList()).get(0);

		if (obj != null)
			return new ActionResult<>(false, "禁止撤销，因为已经存在审核记录");

		pendings.forEach(pendingHibernateRepository::PhysicsDelete);

		contract.setStatus(Status.Draft);
		contractHibernateRepository.SaveOrUpdate(contract);

		return new ActionResult<>(true, "撤销成功");
	}

	@Override
	public boolean CancelByFund(Fund impFund, String userId, StringBuilder message) {

		Fund fund = fundHibernateRepository.getOneById(impFund.getId(), Fund.class);

		if (fund == null) {
			message.append("业务对象已经不存在。");
			return false;
		}
		//判段操作人是否是管理员 by zengshihua 2016/09/06
		User user=this.userRepo.getOneById(userId,User.class);
		if(user.getIsSysAdmin()){
			//如果是管理员，并且没有确认付款。
			if(fund.getIsExecuted()){
				message.append("已经确认付款，不能撤销。");
				return false;
			}
			DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
			where.add(Restrictions.eq("IsHidden", false));
			where.add(Restrictions.eq("FundId", impFund.getId()));
			List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();
			pendings.forEach(pendingHibernateRepository::PhysicsDelete);
			fund.setStatus(Status.Draft);
			fundHibernateRepository.SaveOrUpdate(fund);
			
		}else{
			if (!fund.getCreatedId().equalsIgnoreCase(userId)) {
				message.append("不可以撤销他人的申请业务。");
				return false;
			}

			if (impFund.getPendings() == null || impFund.getPendings().size() == 0) {

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("FundId", impFund.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
				}

				fund.setStatus(Status.Draft);
				fund.setIsApproved(false);
				fundHibernateRepository.SaveOrUpdate(fund);
			} else {
				boolean hasRemain = false;

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("FundId", impFund.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					if (!pending.getIsDone()
							&& impFund.getPendings().stream().anyMatch(x -> x.getId().equals(pending.getId()))) {
						pendingHibernateRepository.PhysicsDelete(pending);
					} else {
						hasRemain = true;
					}
				}

				if (!hasRemain) {
					fund.setStatus(Status.Draft);
					fundHibernateRepository.SaveOrUpdate(fund);
				}
			}
		}
		message.append("撤销成功。");
		return true;
	}

	@Override
	public ActionResult<String> CancelByFundId(String fundId, String userId) {

		Fund fund = fundHibernateRepository.getOneById(fundId, Fund.class);

		if (fund == null)
			return new ActionResult<>(false, "业务对象已经不存在");

		if (!fund.getCreatedId().equalsIgnoreCase(userId))
			return new ActionResult<>(false, "不可以撤销他人的申请业务");

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("FundId", fundId));
		List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

		Pending obj = pendings.stream().filter(Pending::getIsDone).collect(Collectors.toList()).get(0);

		if (obj != null)
			return new ActionResult<>(false, "禁止撤销，因为已经存在审核记录");

		pendings.forEach(pendingHibernateRepository::PhysicsDelete);

		fund.setStatus(Status.Draft);
		fundHibernateRepository.SaveOrUpdate(fund);

		return new ActionResult<>(true, "撤销成功");
	}

	@Override
	public ActionResult<List<Pending>> PendingsByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Pending> list = this.repository.GetQueryable(Pending.class).where(where).toList();

		list = this.commonService.SimplifyDataPendingList(list);

		return new ActionResult<>(true, null, list);

	}

	@Override
	public ActionResult<List<Pending>> PendingsByFundId(String fundId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("FundId", fundId));

		List<Pending> list = this.repository.GetQueryable(Pending.class).where(where).toList();

		list = this.commonService.SimplifyDataPendingList(list);

		return new ActionResult<>(true, null, list);
	}

	@Override
	public ActionResult<List<Pending>> PendingsByInvoiceId(String invoiceId) {
		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("InvoiceId", invoiceId));

		List<Pending> list = this.repository.GetQueryable(Pending.class).where(where).toList();

		addObjData(list);

		ActionResult<List<Pending>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	private void addObjData(List<Pending> pendings) {
		for (Pending pending : pendings) {
			if (pending.getApprover() != null) {
				pending.setApproverName(pending.getApprover().getName());
			}
			if (pending.getAsker() != null) {
				pending.setAskerName(pending.getAsker().getName());
			}
			Approve approve = approveRepo.GetQueryable(Approve.class)
					.where(DetachedCriteria.forClass(Approve.class).add(Restrictions.eq("PendingId", pending.getId())))
					.firstOrDefault();
			if (approve != null) {
				pending.setApproveComments(approve.getComments());
				pending.setApproveTradeDate(approve.getTradeDate());
				
			}
			if(pending.getInvoice()!=null){
				pending.getInvoice().setStorages(null);//为了打断关系
			}
			
		}
	}

	@Override
	public Criteria GetCriteria() {

		return this.repository.CreateCriteria(Pending.class);
	}

	@Override
	public ActionResult<List<Pending>> ApprovePendings() {

		List<Pending> pendings = this.repository.GetList(Pending.class);

		pendings = this.commonService.SimplifyDataPendingList(pendings);

		return new ActionResult<>(true, null, pendings);
	}

	@Override
	public ActionResult<Pending> Ask4Approve(Pending pending, String userId) {

		try {
			int initStatus = Status.Draft;

			// 更新客户的状态
			if (pending.getCustomerId() != null) {
				
				//验证提交的审核人员中是否有终审账号
				if(!CheckApproveHasTerminator("IsCustomerTerminator",pending.getApproverIds()))
					return new ActionResult<>(false,"提交审核的人员中没有终审账号！");
				
				Customer customer = customerHibernateRepository.getOneById(pending.getCustomerId(), Customer.class);

				if (!customer.getCreatedId().equalsIgnoreCase(userId))
					return new ActionResult<>(false, "不可以对他人的记录提出审核的申请");

				if (customer.getStatus().equals(Status.Agreed)) {
					String msg = "";
					if (customer.getStatus().equals(Status.Agreed))
						msg = "同意";
					if (customer.getStatus().equals(Status.Deny))
						msg = "否决";

					return new ActionResult<>(false, String.format("该客户已经审核，结论是：{%s}。", msg));
				}
				initStatus = customer.getStatus();
				customer.setStatus(Status.Pending);
				customerHibernateRepository.SaveOrUpdate(customer);
			}

			// 更新合同的状态
			if (pending.getContractId() != null) {
				
				//验证提交的审核人员中是否有终审账号
				if(!CheckApproveHasTerminator("IsContractTerminator",pending.getApproverIds()))
					return new ActionResult<>(false,"提交审核的人员中没有终审账号！");
				
				Contract contract = contractHibernateRepository.getOneById(pending.getContractId(), Contract.class);

				if (!contract.getCreatedId().equalsIgnoreCase(userId))
					return new ActionResult<>(false, "不可以对他人的记录提出审核的申请");

				if (contract.getStatus().equals(Status.Agreed)) {
					String msg = "";
					if (contract.getStatus().equals(Status.Agreed))
						msg = "同意";
					if (contract.getStatus().equals(Status.Deny))
						msg = "否决";

					return new ActionResult<>(false, String.format("该合同已经审核，结论是：{%s}。", msg));
				}
				initStatus = contract.getStatus();
				contract.setStatus(Status.Pending);
				contractHibernateRepository.SaveOrUpdate(contract);
			}

			// 更新付款的状态
			if (pending.getFundId() != null) {
				
				//验证提交的审核人员中是否有终审账号
				if(!CheckApproveHasTerminator("IsPaymentTerminator",pending.getApproverIds()))
					return new ActionResult<>(false,"提交审核的人员中没有终审账号！");
				
				Fund fund = fundHibernateRepository.getOneById(pending.getFundId(), Fund.class);

				if (!fund.getCreatedId().equalsIgnoreCase(userId))
					return new ActionResult<>(false, "不可以对他人的记录提出审核的申请");

				if (fund.getStatus().equals(Status.Agreed)) {
					String msg = "";
					if (fund.getStatus().equals(Status.Agreed))
						msg = "同意";
					if (fund.getStatus().equals(Status.Deny))
						msg = "否决";

					return new ActionResult<>(false, String.format("该付款已经审核，结论是：{%s}。", msg));
				}

				initStatus = fund.getStatus();
				fund.setStatus(Status.Pending);
				fundHibernateRepository.SaveOrUpdate(fund);
			}

			// 更新收货单的状态
			if (pending.getReceiptShipId() != null) {
				
				//验证提交的审核人员中是否有终审账号
				if(!CheckApproveHasTerminator("IsReceiptShipTerminator",pending.getApproverIds()))
					return new ActionResult<>(false,"提交审核的人员中没有终审账号！");
				
				ReceiptShip receiptShip = receiptShipRepo.getOneById(pending.getReceiptShipId(), ReceiptShip.class);

				if (!receiptShip.getCreatedId().equalsIgnoreCase(userId))
					return new ActionResult<>(false, "不可以对他人的记录提出审核的申请");

				if (receiptShip.getStatus().equals(Status.Agreed)) {
					String msg = "";
					if (receiptShip.getStatus().equals(Status.Agreed))
						msg = "同意";
					if (receiptShip.getStatus().equals(Status.Deny))
						msg = "否决";

					return new ActionResult<>(false, String.format("该收货单已经审核，结论是：{%s}。", msg));
				}
				initStatus = receiptShip.getStatus();
				receiptShip.setStatus(Status.Pending);
				receiptShipRepo.SaveOrUpdate(receiptShip);
			}

			// 更新发票的状态
			if (pending.getInvoiceId() != null) {
				
				//验证提交的审核人员中是否有终审账号
				if(!CheckApproveHasTerminator("IsInvoiceTerminator",pending.getApproverIds()))
					return new ActionResult<>(false,"提交审核的人员中没有终审账号！");

				Invoice invoice = invoiceRepo.getOneById(pending.getInvoiceId(), Invoice.class);

				if (!invoice.getCreatedId().equalsIgnoreCase(userId))
					return new ActionResult<>(false, "不可以对他人的记录提出审核的申请");

				if (invoice.getStatus().equals(Status.Agreed)) {
					String msg = "";
					if (invoice.getStatus().equals(Status.Agreed))
						msg = "同意";
					if (invoice.getStatus().equals(Status.Deny))
						msg = "否决";

					return new ActionResult<>(false, String.format("该发票已经审核，结论是：{%s}。", msg));
				}
				
				String invoiceNo = invoice.getInvoiceNo();
				int separateIndex = invoiceNo.indexOf("/");
				if(separateIndex > 0) {
					List<Invoice> list = invoiceRepo.GetQueryable(Invoice.class).where(DetachedCriteria.forClass(Invoice.class)
							.add(Restrictions.like("InvoiceNo", invoiceNo.substring(0, separateIndex) + "%"))).toList();
					for(Invoice inv : list) {
						initStatus = inv.getStatus();
						inv.setStatus(Status.Pending);
						invoiceRepo.SaveOrUpdate(inv);
					}
				} else {
					initStatus = invoice.getStatus();
					invoice.setStatus(Status.Pending);
					invoiceRepo.SaveOrUpdate(invoice);
				}
				
			}

			// #region 通过foreach，保存多个申请审核的记录

			String[] ids = pending.getApproverIds().split(",");

			for (int i = 0; i < ids.length; i++) {
				ids[i] = ids[i].trim();
			}

			DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
			handleIsNullRestriction(pending.getCustomerId(), "CustomerId", where);
			handleIsNullRestriction(pending.getContractId(), "ContractId", where);
			handleIsNullRestriction(pending.getFundId(), "FundId", where);
			handleIsNullRestriction(pending.getReceiptShipId(), "ReceiptShipId", where);
			handleIsNullRestriction(pending.getInvoiceId(), "InvoiceId", where);

			where.add(Restrictions.eq("IsHidden", Boolean.FALSE));
			List<Pending> exists = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

			if (initStatus == Status.Deny) {

				DetachedCriteria approveWhere = DetachedCriteria.forClass(Approve.class);
				handleIsNullRestriction(pending.getCustomerId(), "CustomerId", approveWhere);
				handleIsNullRestriction(pending.getContractId(), "ContractId", approveWhere);
				handleIsNullRestriction(pending.getFundId(), "FundId", approveWhere);
				handleIsNullRestriction(pending.getReceiptShipId(), "ReceiptShipId", approveWhere);
				handleIsNullRestriction(pending.getInvoiceId(), "InvoiceId", approveWhere);
				approveWhere.add(Restrictions.eq("IsHidden", Boolean.FALSE));
				List<Approve> approveList = approveRepo.GetQueryable(Approve.class).where(approveWhere).toList();
				for (Approve approve : approveList) {
					approve.setIsHidden(Boolean.TRUE);
					approveRepo.SaveOrUpdate(approve);
				}

				for (Pending exist : exists) {
					exist.setIsHidden(Boolean.TRUE);
					pendingHibernateRepository.SaveOrUpdate(exist);
					// if (exist.getId().equals(pending.getId()))
					// continue;
					//
					// if (Arrays.asList(ids).contains(exist.getApproverId())) {
					// return new ActionResult<>(false, "对于相同业务和审核人员，不需要重复提交。");
					// }
				}
			}

			for (String id : ids) {

				pending.setApproverId(id);
				pending.setId(null);
				pendingHibernateRepository.SaveOrUpdate(pending);
			}

			return new ActionResult<>(true, "处理成功", pending);

		} catch (Exception e) {

			logger.error(e.getMessage(), e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	private Boolean CheckApproveHasTerminator(String field,String userId)
	{
		String[] strUsers = userId.split(",");
		List<String> listUser = new ArrayList<String>();
		for(String user:strUsers){
			listUser.add(user.trim());
		}
		
		//验证提交的审核人员中是否有终审账号
		DetachedCriteria where = DetachedCriteria.forClass(User.class);
		where.add(Restrictions.eq(field,true));
		where.add(Restrictions.in("Id",listUser));
		List<User> users = userRepo.GetQueryable(User.class).where(where).toList();
		if(users==null || users.size() == 0)
			return false;
		else 
			return true;
	}
	
	private void handleIsNullRestriction(String value, String propertyName, DetachedCriteria where) {
		where.add(Restrictions.eqOrIsNull(propertyName, value));
	}

	@Override
	public ActionResult<Pending> GetById(String pendingId) {

		Pending pending = this.repository.getOneById(pendingId, Pending.class);
		Fund fund = pending.getFund();
		pending = this.commonService.SimplifyData(pending);
		pending.setFund(fund);
		return new ActionResult<>(true, null, pending);
	}

	@Override
	public ActionResult<Customer> CustomerByPendingId(String pendingId) {

		Pending pending = this.repository.getOneById(pendingId, Pending.class);

		if (pending == null)
			return new ActionResult<>(false, "pending is null");

		Customer custormer = pending.getCustomer();

		if (custormer == null)
			return new ActionResult<>(false, "customer is null");

		custormer.setPendings(this.PendingsByCustomerId(custormer.getId()).getData());

		return new ActionResult<>(true, null, custormer);
	}

	@Override
	public ActionResult<Contract> ContractByPendingId(String pendingId) {

		Pending pending = this.repository.getOneById(pendingId, Pending.class);

		if (pending == null)
			return new ActionResult<>(false, "pending is null");

		Contract contract = pending.getContract();

		if (contract == null)
			return new ActionResult<>(false, "contract is null");

		contract.setPendings(this.PendingsByContractId(contract.getId()).getData());

		return new ActionResult<>(true, null, contract);
	}

	@Override
	public ActionResult<Fund> FundByPendingId(String pendingId) {

		Pending pending = this.repository.getOneById(pendingId, Pending.class);

		if (pending == null)
			return new ActionResult<>(false, "pending is null");

		Fund fund = pending.getFund();

		if (fund == null)
			return new ActionResult<>(false, "Fund is null");

		fund.setPendings(this.PendingsByFundId(fund.getId()).getData());

		return new ActionResult<>(true, null, fund);
	}

	@Override
	public List<Pending> PendingPager(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil refTotal) {

		return (List<Pending>) this.repository
				.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, refTotal).getData();
	}

	@Override
	public ActionResult<String> CancelByInvoice(Invoice invoice, String userId, StringBuilder message) {


		invoice = this.invoiceRepo.getOneById(invoice.getId(), Invoice.class);

		if (invoice == null) {
			return new ActionResult<>(Boolean.FALSE, "业务对象已经不存在");
		}
		String invoiceNo = invoice.getInvoiceNo();
		int separateIndex = invoiceNo.indexOf("/");
		List<Invoice> invoiceList = new ArrayList<>();
		if(separateIndex > 0) {
			invoiceList = invoiceRepo.GetQueryable(Invoice.class).where(DetachedCriteria.forClass(Invoice.class)
					.add(Restrictions.like("InvoiceNo", invoiceNo.substring(0, separateIndex) + "%"))).toList();
		} else {
			invoiceList.add(invoice);
		}
		List<String> invoiceIdList = new ArrayList<>();
		for(Invoice in : invoiceList) {
			invoiceIdList.add(in.getId());
		}

		//判段操作人是否是管理员
		User user=this.userRepo.getOneById(userId,User.class);
		if(user.getIsSysAdmin()){
			// 如果是管理员，直接撤销发票 by zengshihua 2016/09/06
			DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
			where = DetachedCriteria.forClass(Pending.class);
			where.add(Restrictions.eq("IsHidden", false));
			
			where.add(Restrictions.in("InvoiceId", invoiceIdList));
			List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();
			for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
			}
			for(Invoice in : invoiceList) {
				in.setStatus(Status.Draft);
				in.setIsApproved(false);
				invoiceRepo.SaveOrUpdate(in);
			}
			
		}else{
			if (!invoice.getCreatedId().equalsIgnoreCase(userId)) {
				return new ActionResult<>(Boolean.FALSE, "不可以撤销他人的申请业务");
			}
			/**
			 * 没有存在审核记录
			 */
			if (invoice.getPendings() == null || invoice.getPendings().size() == 0) {
				
				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.in("InvoiceId", invoiceIdList));
				where.add(Restrictions.eq("IsDone", true));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				if (pendings.size() > 0) {
					return new ActionResult<>(Boolean.FALSE, "禁止撤销，因为已经存在审核记录。");
				}
				
				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
				}
				for(Invoice in : invoiceList) {
					in.setStatus(Status.Draft);
					invoiceRepo.SaveOrUpdate(in);
				}
			} else {
				boolean hasRemain = false;

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.in("InvoiceId", invoiceIdList));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					if (!pending.getIsDone()
							&& invoice.getPendings().stream().anyMatch(x -> x.getId().equals(pending.getId()))) {
						pendingHibernateRepository.PhysicsDelete(pending);
					} else {
						hasRemain = true;
					}
				}

				if (!hasRemain) {
					for(Invoice in : invoiceList) {
						in.setStatus(Status.Draft);
						invoiceRepo.SaveOrUpdate(in);
					}
				}
			}
		}
		
		return new ActionResult<>(Boolean.TRUE, "撤销成功");
	}

	@Override
	public ActionResult<String> CancelByShip(ReceiptShip ship, String userId) {

		ship = this.receiptShipRepo.getOneById(ship.getId(), ReceiptShip.class);

		if (ship == null) {
			return new ActionResult<>(Boolean.FALSE, "业务对象已经不存在");
		}
		//判段操作人是否是管理员
		User user=this.userRepo.getOneById(userId,User.class);
		if(user.getIsSysAdmin()){
			/**
			 * 判断是否有对应发票,如果有对应发票，不能撤销
			 */
			//发货单->批次->发票->货
			Lot lot=this.lotRepo.getOneById(ship.getLotId(), Lot.class);
			List<Invoice> invoices=this.invoiceRepo.GetQueryable(Invoice.class)
					.where(DetachedCriteria.forClass(Invoice.class).add(Restrictions.eq("LotId", lot.getId())))
					.toList();
			List<String> sId=new ArrayList<>();
			if(invoices!=null&&invoices.size()>0){
				for (Invoice invoice : invoices) {
					for (Storage s : invoice.getStorages()) {
						if(!sId.contains(s.getId())){
							sId.add(s.getId());
						}
					}
				}
			}
			//发货单->货
			List<Storage> storage=this.storageRepo.GetQueryable(Storage.class)
					.where(DetachedCriteria.forClass(Storage.class)
							.add(Restrictions.eq("RefId", ship.getId()))
							.add(Restrictions.eq("RefName", "ReceiptShip"))
							)
					.toList();
			
			List<String> refSId=new ArrayList<>();
			for (Storage s : storage) {
				if(!refSId.contains(s.getId())){
					refSId.add(s.getId());
				}
			}
			//如果两个集合有重复值，则说明已经有对应发票
			if(sId.size()==0||!Collections.disjoint(sId, refSId)){
				//还原批次发货标记和交付数量。
				lot.setIsDelivered(false);
				lot.setQuantityDelivered(lot.getQuantityDelivered().subtract(ship.getWeight()));
				this.lotRepo.SaveOrUpdate(lot);

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("InvoiceId", ship.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();
				//删除审核记录
				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
				}
				
				ship.setStatus(Status.Draft);
				ship.setIsApproved(false);
				receiptShipRepo.SaveOrUpdate(ship);
				
			}else{
				return new ActionResult<>(Boolean.FALSE, "发货单有对应发票，不能撤销");
			}
			
		}else{
			if (!ship.getCreatedId().equalsIgnoreCase(userId)) {
				return new ActionResult<>(Boolean.FALSE, "不可以撤销他人的申请业务");
			}

			if (ship.getPendings() == null || ship.getPendings().size() == 0) {

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("InvoiceId", ship.getId()));
				where.add(Restrictions.eq("IsDone", true));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				if (pendings.size() > 0) {
					return new ActionResult<>(Boolean.FALSE, "禁止撤销，因为已经存在审核记录。");
				}

				for (Pending pending : pendings) {
					pendingHibernateRepository.PhysicsDelete(pending);
				}
				ship.setStatus(Status.Draft);
				receiptShipRepo.SaveOrUpdate(ship);
			} else {
				boolean hasRemain = false;

				DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
				where = DetachedCriteria.forClass(Pending.class);
				where.add(Restrictions.eq("IsHidden", false));
				where.add(Restrictions.eq("InvoiceId", ship.getId()));
				List<Pending> pendings = pendingHibernateRepository.GetQueryable(Pending.class).where(where).toList();

				for (Pending pending : pendings) {
					if (!pending.getIsDone()
							&& ship.getPendings().stream().filter(x -> x.getId().equals(pending.getId())).count() > 0) {
						pendingHibernateRepository.PhysicsDelete(pending);
					} else {
						hasRemain = true;
					}
				}

				if (!hasRemain) {
					ship.setStatus(Status.Draft);
					receiptShipRepo.SaveOrUpdate(ship);
				}
			}
		}
		
		return new ActionResult<>(Boolean.TRUE, "撤销成功");
	}

	@Override
	public ActionResult<List<Pending>> PendingsByShipId(String shipId) {
		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ReceiptShipId", shipId));

		List<Pending> list = this.repository.GetQueryable(Pending.class).where(where).toList();

		addObjData(list);
		ActionResult<List<Pending>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	/**
	 * 打掉关系
	 */
	public List<Pending> assemble_notUsed(List<Pending> list) {
		if (list == null || list.size() == 0)
			return null;

		List<String> contractIds = new ArrayList<>();
		List<String> invoiceIds = new ArrayList<>();
		List<String> askerIds = new ArrayList<>();
		List<String> legalIds = new ArrayList<>();
		List<String> customerIds_fun = new ArrayList<>();
		List<String> customerIds = new ArrayList<>();
		List<String> customerTitleIds = new ArrayList<>();
		List<String> lotIds = new ArrayList<>();
		List<String> approveIds = new ArrayList<>();
		list.forEach(p -> {
			if (p.getContractId() != null) {
				contractIds.add(p.getContractId());
			}
			if (p.getFund() != null && p.getFund().getInvoiceId() != null) {
				invoiceIds.add(p.getFund().getInvoiceId());
			}
			if (p.getAskerId() != null) {
				askerIds.add(p.getAskerId());
			}
			if (p.getFund() != null && p.getFund().getLegalId() != null) {
				legalIds.add(p.getFund().getLegalId());
			}
			if (p.getFund() != null && p.getFund().getCustomerId() != null) {
				customerIds_fun.add(p.getFund().getCustomerId());
			}
			if (p.getFund() != null && p.getFund().getCustomerTitleId() != null) {
				customerTitleIds.add(p.getFund().getCustomerTitleId());
			}
			if (p.getCustomerId() != null) {
				customerIds.add(p.getCustomerId());
			}
			if (p.getFund() != null && p.getFund().getLotId() != null) {
				lotIds.add(p.getFund().getLotId());
			}
			if (p.getApproveId()!=null) {
				approveIds.add(p.getApproveId());
			}
		});
		List<Approve> approveList=new ArrayList<>();
		if(approveIds.size()>0){
			DetachedCriteria dc16 = DetachedCriteria.forClass(Approve.class);
			dc16.add(Restrictions.in("Id", approveIds));
			approveList = this.approveRepo.GetQueryable(Approve.class).where(dc16).toList();
		}
		// in参数个数有限制
		List<Contract> contlist = new ArrayList<>();
		if (contractIds.size() > 0) {
			DetachedCriteria dc = DetachedCriteria.forClass(Contract.class);
			dc.add(Restrictions.in("Id", contractIds));
			contlist = this.contractHibernateRepository.GetQueryable(Contract.class).where(dc).toList();
		}

		List<Invoice> invoicelist = new ArrayList<>();
		if (invoiceIds.size() > 0) {
			DetachedCriteria dc2 = DetachedCriteria.forClass(Invoice.class);
			dc2.add(Restrictions.in("Id", invoiceIds));
			invoicelist = this.invoiceRepo.GetQueryable(Invoice.class).where(dc2).toList();
		}

		List<User> askerlist = new ArrayList<>();
		if (askerIds.size() > 0) {
			DetachedCriteria dc3 = DetachedCriteria.forClass(User.class);
			dc3.add(Restrictions.in("Id", askerIds));
			askerlist = this.userRepo.GetQueryable(User.class).where(dc3).toList();
		}

		List<Legal> legallist = new ArrayList<>();
		if (legalIds.size() > 0) {
			DetachedCriteria dc4 = DetachedCriteria.forClass(Legal.class);
			dc4.add(Restrictions.in("Id", legalIds));
			legallist = this.legalRepo.GetQueryable(Legal.class).where(dc4).toList();
		}

		List<Customer> customerlist = new ArrayList<>();
		if (customerIds_fun.size() > 0) {
			DetachedCriteria dc5 = DetachedCriteria.forClass(Customer.class);
			dc5.add(Restrictions.in("Id", customerIds_fun));
			customerlist = this.customerHibernateRepository.GetQueryable(Customer.class).where(dc5).toList();
		}

		List<CustomerTitle> customerTitlelist = new ArrayList<>();
		if (customerTitleIds.size() > 0) {
			DetachedCriteria dc6 = DetachedCriteria.forClass(CustomerTitle.class);
			dc6.add(Restrictions.in("Id", customerTitleIds));
			customerTitlelist = this.customerTitleRepo.GetQueryable(CustomerTitle.class).where(dc6).toList();
		}

		List<Customer> customerList3 = new ArrayList<>();
		if (customerIds.size() > 0) {
			DetachedCriteria dc13 = DetachedCriteria.forClass(Customer.class);
			dc13.add(Restrictions.in("Id", customerIds));
			customerList3 = this.customerHibernateRepository.GetQueryable(Customer.class).where(dc13).toList();
		}

		List<Lot> lotList = new ArrayList<>();
		if (lotIds.size() > 0) {
			DetachedCriteria dc14 = DetachedCriteria.forClass(Lot.class);
			dc14.add(Restrictions.in("Id", lotIds));
			lotList = this.lotRepo.GetQueryable(Lot.class).where(dc14).toList();
		}

		for (Pending p : list) {
			if(approveList!=null&&approveList.size()>0){
				approveList.forEach(c->{
					if(c.getId().equals(p.getApproveId())){
						p.setApprove(c);
					}
				});
			}
			if (customerList3.size() > 0) {
				customerList3.forEach(c -> {
					if (c.getId().equals(p.getCustomerId())) {
						p.setCustomer(c);
					}
				});
			}
			if (contlist != null && contlist.size() > 0) {
				contlist.forEach(c -> {
					if (c.getId().equals(p.getContractId())) {
						p.setContract(c);
					}
				});
			}
			if (invoicelist != null && invoicelist.size() > 0) {
				invoicelist.forEach(c -> {
					if (p.getFund() != null && c.getId().equals(p.getFund().getInvoiceId())) {
						p.getFund().setInvoice(c);
					}
				});
			}

			if (askerlist != null && askerlist.size() > 0) {
				askerlist.forEach(c -> {
					if (p.getAskerId().equals(c.getId())) {
						p.setAsker(c);
					}
				});
			}

			if (legallist != null && legallist.size() > 0) {
				legallist.forEach(c -> {
					if (p.getFund() != null && c.getId().equals(p.getFund().getLegalId())) {
						p.getFund().setLegal(c);
					}
				});
			}
			if (customerlist != null && customerlist.size() > 0) {
				customerlist.forEach(c -> {
					if (p.getFund() != null && c.getId().equals(p.getFund().getCustomerId())) {
						p.getFund().setCustomer(c);
					}
				});
			}
			if (customerTitlelist != null && customerTitlelist.size() > 0) {
				customerTitlelist.forEach(c -> {
					if (p.getFund() != null && c.getId().equals(p.getFund().getCustomerTitleId())) {
						p.getFund().setCustomerTitle(c);
					}
				});
			}
			if (lotList != null && lotList.size() > 0) {
				lotList.forEach(c -> {
					if (p.getFund() != null && c.getId().equals(p.getFund().getLotId())) {
						p.getFund().setLot(c);
					}
				});
			}
		}

		legalIds.clear();
		customerIds_fun.clear();
		customerTitleIds.clear();
		List<String> commodityIds = new ArrayList<>();
		List<String> divisionIds = new ArrayList<>();
		List<String> traderIdIds = new ArrayList<>();
		List<String> approvesId = new ArrayList<>();

		list.forEach(p -> {
			if(p.getApprove()!=null&&p.getApprove().getApproverId()!=null){
				approvesId.add(p.getApprove().getApproverId());
			}
			if (p.getContract() != null && p.getContract().getLegalId() != null) {
				legalIds.add(p.getContract().getLegalId());
			}
			if (p.getContract() != null && p.getContract().getCustomerId() != null) {
				customerIds_fun.add(p.getContract().getCustomerId());
			}
			if (p.getContract() != null && p.getContract().getCustomerTitleId() != null) {
				customerTitleIds.add(p.getContract().getCustomerTitleId());
			}
			if (p.getContract() != null && p.getContract().getCommodityId() != null) {
				commodityIds.add(p.getContract().getCommodityId());
			}
			if (p.getAsker() != null && p.getAsker().getDivisionId() != null) {
				divisionIds.add(p.getAsker().getDivisionId());
			}
			if (p.getContract() != null && p.getContract().getTraderId() != null) {
				traderIdIds.add(p.getContract().getTraderId());
			}

		});
		List<User> userApprove=new ArrayList<>();
		if(approvesId.size()>0){
			DetachedCriteria dc17 = DetachedCriteria.forClass(User.class);
			dc17.add(Restrictions.in("Id", approvesId));
			userApprove = this.userRepo.GetQueryable(User.class).where(dc17).toList();
		}
		
		List<Customer> customerlist2 = new ArrayList<>();
		if (customerIds_fun.size() > 0) {
			DetachedCriteria dc7 = DetachedCriteria.forClass(Customer.class);
			dc7.add(Restrictions.in("Id", customerIds_fun));
			customerlist2 = this.customerHibernateRepository.GetQueryable(Customer.class).where(dc7).toList();
		}
		List<CustomerTitle> customerTitlelist2 = new ArrayList<>();
		if (customerTitleIds.size() > 0) {
			DetachedCriteria dc8 = DetachedCriteria.forClass(CustomerTitle.class);
			dc8.add(Restrictions.in("Id", customerTitleIds));
			customerTitlelist2 = this.customerTitleRepo.GetQueryable(CustomerTitle.class).where(dc8).toList();
		}
		List<Legal> legallist2 = new ArrayList<>();
		if (legalIds.size() > 0) {
			DetachedCriteria dc9 = DetachedCriteria.forClass(Legal.class);
			dc9.add(Restrictions.in("Id", legalIds));
			legallist2 = this.legalRepo.GetQueryable(Legal.class).where(dc9).toList();
		}

		List<Commodity> commoditylist = new ArrayList<>();
		if (commodityIds.size() > 0) {
			DetachedCriteria dc10 = DetachedCriteria.forClass(Commodity.class);
			dc10.add(Restrictions.in("Id", commodityIds));
			commoditylist = this.commodityRepo.GetQueryable(Commodity.class).where(dc10).toList();
		}
		List<Division> divisionlist = new ArrayList<>();
		if (divisionIds.size() > 0) {
			DetachedCriteria dc11 = DetachedCriteria.forClass(Division.class);
			dc11.add(Restrictions.in("Id", divisionIds));
			divisionlist = this.divisionRepo.GetQueryable(Division.class).where(dc11).toList();
		}
		List<User> traderlist = new ArrayList<>();
		if (traderIdIds.size() > 0) {
			DetachedCriteria dc15 = DetachedCriteria.forClass(User.class);
			dc15.add(Restrictions.in("Id", traderIdIds));
			traderlist = this.userRepo.GetQueryable(User.class).where(dc15).toList();
		}

		for (Pending p : list) {
			if(userApprove!=null&&userApprove.size()>0){
				userApprove.forEach(c->{
					if(p.getApprove()!=null&&p.getApprove().getApproverId()!=null&&c.getId().equals(p.getApprove().getApproverId())){
						p.getApprove().setApprover(c);
					}
				});
			}
			if (legallist2 != null && legallist2.size() > 0) {
				legallist2.forEach(c -> {
					if (c.getId().equals(p.getContract().getLegalId())) {
						p.getContract().setLegal(c);
					}
				});
			}
			if (customerlist2 != null && customerlist2.size() > 0) {
				customerlist2.forEach(c -> {
					if (p.getContract() != null && c.getId().equals(p.getContract().getCustomerId())) {
						p.getContract().setCustomer(c);
					}
				});
			}
			if (customerTitlelist2 != null && customerTitlelist2.size() > 0) {
				customerTitlelist2.forEach(c -> {
					if (p.getContract() != null && c.getId().equals(p.getContract().getCustomerTitleId())) {
						p.getContract().setCustomerTitle(c);
					}
				});
			}
			if (legallist2 != null && legallist2.size() > 0) {
				legallist2.forEach(c -> {
					if (p.getContract() != null && c.getId().equals(p.getContract().getLegalId())) {
						p.getContract().setLegal(c);
					}
				});
			}
			if (commoditylist != null && commoditylist.size() > 0) {
				commoditylist.forEach(c -> {
					if (p.getContract() != null && c.getId().equals(p.getContract().getCommodityId())) {
						p.getContract().setCommodity(c);
					}
				});
			}
			if (divisionlist != null && divisionlist.size() > 0) {
				divisionlist.forEach(c -> {
					if (p.getAsker() != null) {
						p.getAsker().setDivision(c);
					}
				});
			}
			if (traderlist != null && traderlist.size() > 0) {
				traderlist.forEach(c -> {
					if (p.getContract() != null && c.getId().equals(p.getContract().getTraderId())) {
						p.getContract().setTrader(c);
						;
					}
				});
			}
		}
		return list;
	}
}
