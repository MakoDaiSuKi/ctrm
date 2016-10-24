package com.smm.ctrm.bo.impl.Finance;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.FundType;
import com.smm.ctrm.domain.Basis.BankReceiptStatus;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Physical.BankReceipt;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.VFund;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.DC;
import com.smm.ctrm.util.Result.FeeCode;
import com.smm.ctrm.util.Result.InvoiceType;
import com.smm.ctrm.util.Result.MT4Invoice;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/4/28.
 *
 */
@Service
@Transactional
public class FundServiceImpl implements FundService {

	private static final Logger logger = Logger.getLogger(FundServiceImpl.class);

	@Autowired
	private HibernateRepository<Invoice> invoiceHibernateRepository;

	@Autowired
	private HibernateRepository<Fund> fundRepo;

	@Autowired
	private HibernateRepository<Contract> contractHibernateRepository;

	@Autowired
	private CommonService commonService;

	@Autowired
	private HibernateRepository<VFund> vfundRepo;

	@Autowired
	private HibernateRepository<Legal> legalRepo;

	@Autowired
	private HibernateRepository<Invoice> InvoiceRepo;

	@Autowired
	private HibernateRepository<Commodity> commodityRepo;

	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleRepo;

	@Autowired
	private HibernateRepository<Customer> customerRepo;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<BankReceipt> bankReceiptRepo;

	@Autowired
	private HibernateRepository<CustomerBalance> cbRepository;

	@Override
	public Criteria GetCriteria() {

		return this.fundRepo.CreateCriteria(Fund.class);
	}

	@Override
	public ActionResult<Fund> PaymentConfirmed(Fund fund) {

		if (fund == null)
			throw new RuntimeException("fund is null");

		this.fundRepo.SaveOrUpdate(fund);

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		// 更新发票的收付款标志
		if (fund.FundType.equals(FundType.Fund4Invoice)) {

			Invoice invoice = this.invoiceHibernateRepository.getOneById(fund.getInvoiceId(), Invoice.class);
			where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
			List<Fund> funds1 = this.fundRepo.GetQueryable(Fund.class).where(where).toList();

			BigDecimal Amount_sum = BigDecimal.ZERO;

			for (Fund f : funds1) {
				if (f.getStatus() == Status.Agreed) {
					Amount_sum = Amount_sum.add(f.getAmount());
				}
			}

			invoice.setIsExecuted(invoice.getAmount().abs().compareTo(Amount_sum) == 0);

			this.invoiceHibernateRepository.SaveOrUpdate(invoice);
		}

		// to-do:以下的代码处理，基于重要的假设前提：美元合同只能用美元收付款，人民币合同只能用人民币收付款。
		// to-do:如果不是币种统一，则需要换算。
		// 如果需要换算，则指定一个汇率，在sum以前，先判断币种、换算、最后汇总

		// 更新批次“已发生金额”和“收付款标志”
		Lot lot = this.lotRepo.getOneById(fund.getLotId(), Lot.class);

		where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		where.add(Restrictions.eq("IsExecuted", true));
		List<Fund> funds = this.fundRepo.GetQueryable(Fund.class).where(where).toList();
		BigDecimal paidBalanceSum = BigDecimal.ZERO;
		for (Fund f : funds) {
			paidBalanceSum = paidBalanceSum.add(f.getAmount());
		}
		lot.setPaidBalance(paidBalanceSum);

		/**
		 * 更新批次的收付款
		 */
		List<VFund> query4Draft = this.vfundRepo.GetQueryable(VFund.class)
				.where(DetachedCriteria.forClass(VFund.class).add(Restrictions.eq("LotId", fund.getLotId()))
						.add(Restrictions.eq("FundType", FundType.Fund4Lot))
						.add(Restrictions.ne("Status", Status.Deny)))
				.toList();

		List<VFund> query4Amount = this.vfundRepo.GetQueryable(VFund.class)
				.where(DetachedCriteria.forClass(VFund.class).add(Restrictions.eq("LotId", fund.getLotId()))
						.add(Restrictions.eq("FundType", FundType.Fund4Lot))
						.add(Restrictions.eq("Status", Status.Agreed)).add(Restrictions.eq("IsExecuted", true)))
				.toList();
		BigDecimal amountfunds = BigDecimal.ZERO;
		if (query4Amount != null && query4Amount.size() > 0) {
			for (VFund p : query4Amount) {
				amountfunds = amountfunds.add(p.getAmount());
			}
		}
		if (lot.getIsDelivered()) {
			if (amountfunds.compareTo(lot.getQuantityDelivered().multiply(lot.getPrice())) >= 0) {
				lot.setIsFunded(true);
			}
		}
		/**
		 * 更新累计付款金额
		 */
		lot.setAmountFunded(DecimalUtil.nullToZero(lot.getAmountFunded()).add(fund.getAmount()));
		BigDecimal quantity = BigDecimal.ZERO;
		BigDecimal draftAmount = BigDecimal.ZERO;
		if (query4Draft != null && query4Draft.size() > 0) {
			for (VFund p : query4Draft) {
				quantity = quantity.add(p.getQuantity());
				draftAmount = draftAmount.add(p.getAmount());
			}
		}
		lot.setQuantityFunded(commonService.FormatQuantity(quantity, fund.getCommodity(), fund.getCommodityId()));
		lot.setAmountFundedDraft(commonService.FormatQuantity(draftAmount, fund.getCommodity(), fund.getCommodityId()));
		lot.setAmountFunded(commonService.FormatQuantity(amountfunds, fund.getCommodity(), fund.getCommodityId()));
		this.lotRepo.SaveOrUpdate(lot);

		this.commonService.SyncCustomerBalance(lot.getCustomerId(), lot.getLegalId(), lot.getCommodityId());
		// this.commonService.UpdateLotIsFunded(fund.getLotId());
		// 更新客户的资金余额（分别按台头、客户、品种、币种，进行汇总）
		// this.customerBalanceService.UpdateCustomerBalance(lot.getCustomerId(),
		// lot.getLegalId(), lot.getCommodityId());
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, fund);
	}

	@Override
	public ActionResult<String> SaveReceive(Fund fund) {

		// 检查有效性，以及补上LotId的值
		if (!fund.getDC().equals(DC.Credit)) {
			return new ActionResult<>(false, "Credit/Debit is error");
		}

		Lot lot = this.lotRepo.getOneById(fund.getLotId(), Lot.class);

		if (lot.getIsPriced() && lot.getIsDelivered()) {
			if (lot.getAmountFunded().add(fund.getAmount())
					.compareTo(lot.getPrice().multiply(lot.getQuantityDelivered())) > 0) {
				return new ActionResult<>(false, "收款金额不能大于批次金额.");
			}
		}

		// 初始化需要的值
		fund.setStatus(Status.Agreed);
		fund.setIsExecuted(true);
		fund.setCommodityId(lot.getCommodityId());

		try {
			BankReceipt old_receipt = null;
			BankReceipt receipt = null;

			if (!StringUtils.isEmpty(fund.getId())) {
				Fund old = this.fundRepo.getOneById(fund.getId(), Fund.class);
				// this.fundRepo.SaveOrUpdate(fund);

				if (old.getBankReceiptId().equals(fund.getBankReceiptId())) {
					/*--- 水单号不变 ---*/
					/* BankReceipt */ old_receipt = bankReceiptRepo.getOneById(fund.getBankReceiptId(),
							BankReceipt.class);

					// 先还原历史收款金额
					old_receipt.setConfirmedAmount(old_receipt.getConfirmedAmount().subtract(old.getAmount()));
					old_receipt.setUnConfrimAmount(old_receipt.getUnConfrimAmount().add(old.getAmount()));

					// 更新当前收款金额
					old_receipt.setConfirmedAmount(old_receipt.getConfirmedAmount().add(fund.getAmount()));
					old_receipt.setUnConfrimAmount(old_receipt.getUnConfrimAmount().subtract(fund.getAmount()));

					/*--- 更新水单状态  ---*/
					if (old_receipt.getConfirmedAmount().compareTo(old_receipt.getAmount()) == 0)
						old_receipt.setStatus(BankReceiptStatus.ConfirmedFinish);
					else if (old_receipt.getConfirmedAmount().compareTo(BigDecimal.ZERO) == 1
							&& old_receipt.getConfirmedAmount().compareTo(old_receipt.getAmount()) == -1)
						old_receipt.setStatus(BankReceiptStatus.Confirmed);
					else
						old_receipt.setStatus(BankReceiptStatus.Registered);

					// bankReceiptRepo.SaveOrUpdate(old_receipt);

				} else {
					/*--- 水单号不变 ---*/
					/* BankReceipt */ old_receipt = bankReceiptRepo.getOneById(old.getBankReceiptId(),
							BankReceipt.class);

					// 先还原历史收款金额
					old_receipt.setConfirmedAmount(old_receipt.getConfirmedAmount().subtract(old.getAmount()));
					old_receipt.setUnConfrimAmount(old_receipt.getUnConfrimAmount().add(old.getAmount()));

					/*--- 更新水单状态  ---*/
					if (old_receipt.getConfirmedAmount().compareTo(old_receipt.getAmount()) == 0)
						old_receipt.setStatus(BankReceiptStatus.ConfirmedFinish);
					else if (old_receipt.getConfirmedAmount().compareTo(BigDecimal.ZERO) == 1
							&& old_receipt.getConfirmedAmount().compareTo(old_receipt.getAmount()) == -1)
						old_receipt.setStatus(BankReceiptStatus.Confirmed);
					else
						old_receipt.setStatus(BankReceiptStatus.Registered);

					// bankReceiptRepo.SaveOrUpdate(old_receipt);

					// 新增收款，更新水单认领金额
					/* BankReceipt */ receipt = bankReceiptRepo.getOneById(fund.getBankReceiptId(), BankReceipt.class);
					receipt.setConfirmedAmount(old_receipt.getConfirmedAmount().add(fund.getAmount()));
					receipt.setUnConfrimAmount(old_receipt.getUnConfrimAmount().subtract(fund.getAmount()));

					/*--- 更新水单状态 ---*/
					if (receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == 0)
						receipt.setStatus(BankReceiptStatus.ConfirmedFinish);
					else if (receipt.getConfirmedAmount().compareTo(BigDecimal.ZERO) == 1
							&& receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == -1)
						receipt.setStatus(BankReceiptStatus.Confirmed);
					else
						receipt.setStatus(BankReceiptStatus.Registered);

					// bankReceiptRepo.SaveOrUpdate(receipt);
				}

			} else {

				/*--- 新增收款，更新水单认领金额 ---*/
				/* BankReceipt */ receipt = bankReceiptRepo.getOneById(fund.getBankReceiptId(), BankReceipt.class);

				// 先还原历史收款金额
				receipt.setConfirmedAmount(receipt.getConfirmedAmount().add(fund.getAmount()));
				receipt.setUnConfrimAmount(receipt.getUnConfrimAmount().subtract(fund.getAmount()));

				/*--- 更新水单状态  ---*/
				if (receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == 0)
					receipt.setStatus(BankReceiptStatus.ConfirmedFinish);
				else if (receipt.getConfirmedAmount().compareTo(BigDecimal.ZERO) == 1
						&& receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == -1)
					receipt.setStatus(BankReceiptStatus.Confirmed);
				else
					receipt.setStatus(BankReceiptStatus.Registered);

				// bankReceiptRepo.SaveOrUpdate(receipt);
			}

			// 更新客户的资金余额（分别按台头、客户、品种、币种，进行汇总）
			List<CustomerBalance> cbs = new ArrayList<>();
			if (lot != null) {
				cbs = commonService.txSyncCustomerBalance(lot.getCustomerId(), lot.getLegalId(), lot.getCommodityId());
			}
			updateFundOfLotByStorage(lot, fund);
			txSaveReceive(fund, old_receipt, receipt, lot, cbs);
			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	@Override
	public void updateFundOfLotByStorage(Lot lot, Fund fund) {
		DetachedCriteria dc1 = DetachedCriteria.forClass(VFund.class);
		dc1.add(Restrictions.eq("LotId", lot.getId()));
		dc1.add(Restrictions.eq("FundType", FundType.Fund4Lot));
		dc1.add(Restrictions.ne("Status", Status.Deny));
		List<VFund> query4Draft = this.vfundRepo.GetQueryable(VFund.class).where(dc1).toList();

		DetachedCriteria dc2 = DetachedCriteria.forClass(VFund.class);
		dc2.add(Restrictions.eq("LotId", lot.getId()));
		dc2.add(Restrictions.eq("FundType", FundType.Fund4Lot));
		dc2.add(Restrictions.ne("Status", Status.Deny));
		dc2.add(Restrictions.eq("IsExecuted", true));
		List<VFund> query4Amount = this.vfundRepo.GetQueryable(VFund.class).where(dc2).toList();
		if (lot != null) {
			// if(lot.getIsInvoiced()){
			// DetachedCriteria dc3=DetachedCriteria.forClass(Invoice.class);
			// dc3.add(Restrictions.eq("LotId", lot.getId()));
			// dc3.add(Restrictions.eq("FeeCode", FeeCode.Goods));//by
			// zengshihua at 2016-08-08
			// List<Invoice>
			// invoices=this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(dc3).toList();
			// if(invoices!=null&&invoices.size()>0){
			// BigDecimal amountInvoices=BigDecimal.ZERO;
			// for (Invoice p : invoices) {
			// amountInvoices=amountInvoices.add(p.getDueAmount());
			// }
			BigDecimal amount = BigDecimal.ZERO;
			if (query4Amount != null && query4Amount.size() > 0) {
				BigDecimal diff = BigDecimal.ZERO;
				for (VFund p : query4Amount) {
					if (fund.getId() != null && fund.getId().equalsIgnoreCase(p.getId())) {
						diff = fund.getAmount().subtract(p.getAmount());// 修改
					}
					amount = amount.add(p.getAmount()).add(diff);
				}
				amount = amount.add(diff);
			}

			BigDecimal quantity = BigDecimal.ZERO;
			BigDecimal draftAmount = BigDecimal.ZERO;

			if (query4Draft != null && query4Draft.size() > 0) {
				BigDecimal diff = BigDecimal.ZERO;
				BigDecimal qDiff = BigDecimal.ZERO;
				for (VFund p : query4Draft) {

					if (fund.getId() != null && fund.getId().equalsIgnoreCase(p.getId())) {
						diff = fund.getAmount().subtract(p.getAmount());
						qDiff = fund.getQuantity().subtract(DecimalUtil.nullToZero(p.getQuantity()));
					}
					quantity = quantity.add(DecimalUtil.nullToZero(p.getQuantity()));
					draftAmount = draftAmount.add(p.getAmount());
				}
				quantity = quantity.add(qDiff);
				draftAmount = draftAmount.add(diff);

			}

			if (fund.getId() == null) {
				amount = amount.add(DecimalUtil.nullToZero(fund.getAmount()));// 新增
				draftAmount = draftAmount.add(DecimalUtil.nullToZero(fund.getAmount()));// 新增
				quantity = quantity.add(DecimalUtil.nullToZero(fund.getQuantity()));
			}

			if (lot.getIsDelivered() && (lot.getPrice() != null && lot.getPrice().compareTo(BigDecimal.ZERO) != 0)
					&& amount.compareTo(lot.getQuantityDelivered().multiply(lot.getPrice())) >= 0) {
				lot.setIsFunded(true);
			} else {
				lot.setIsFunded(false);
			}
			lot.setQuantityFunded(quantity);
			lot.setAmountFundedDraft(draftAmount);
			lot.setAmountFunded(amount);
			// }
			// }
		}
	}

	@Override
	public void updateFundOfLotByInvoice(Lot lot, Fund fund) {
		DetachedCriteria dc1 = DetachedCriteria.forClass(VFund.class);
		dc1.add(Restrictions.eq("LotId", lot.getId()));
		dc1.add(Restrictions.eq("FundType", FundType.Fund4Lot));
		dc1.add(Restrictions.ne("Status", Status.Deny));
		List<VFund> query4Draft = this.vfundRepo.GetQueryable(VFund.class).where(dc1).toList();

		DetachedCriteria dc2 = DetachedCriteria.forClass(VFund.class);
		dc2.add(Restrictions.eq("LotId", lot.getId()));
		dc2.add(Restrictions.eq("FundType", FundType.Fund4Lot));
		dc2.add(Restrictions.ne("Status", Status.Deny));
		dc2.add(Restrictions.eq("IsExecuted", true));
		List<VFund> query4Amount = this.vfundRepo.GetQueryable(VFund.class).where(dc2).toList();
		if (lot != null) {
			if (lot.getIsInvoiced()) {
				DetachedCriteria dc3 = DetachedCriteria.forClass(Invoice.class);
				dc3.add(Restrictions.eq("LotId", lot.getId()));
				dc3.add(Restrictions.eq("FeeCode", FeeCode.Goods));// by
																	// zengshihua
																	// at
																	// 2016-08-08
				List<Invoice> invoices = this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(dc3)
						.toList();
				if (invoices != null && invoices.size() > 0) {
					BigDecimal amountInvoices = BigDecimal.ZERO;
					for (Invoice p : invoices) {
						amountInvoices = amountInvoices.add(p.getDueAmount());
					}
					BigDecimal amount = BigDecimal.ZERO;
					if (query4Amount != null && query4Amount.size() > 0) {
						BigDecimal diff = BigDecimal.ZERO;
						for (VFund p : query4Amount) {
							if (fund.getId() != null && fund.getId().equalsIgnoreCase(p.getId())) {
								diff = fund.getAmount().subtract(p.getAmount());// 修改
							}
							amount = amount.add(p.getAmount()).add(diff);
						}
						amount = amount.add(diff);
					}

					BigDecimal quantity = BigDecimal.ZERO;
					BigDecimal draftAmount = BigDecimal.ZERO;

					if (query4Draft != null && query4Draft.size() > 0) {
						BigDecimal diff = BigDecimal.ZERO;
						BigDecimal qDiff = BigDecimal.ZERO;
						for (VFund p : query4Draft) {

							if (fund.getId() != null && fund.getId().equalsIgnoreCase(p.getId())) {
								diff = fund.getAmount().subtract(p.getAmount());
								qDiff = fund.getQuantity().subtract(DecimalUtil.nullToZero(p.getQuantity()));
							}
							quantity = quantity.add(DecimalUtil.nullToZero(p.getQuantity()));
							draftAmount = draftAmount.add(p.getAmount());
						}
						quantity = quantity.add(qDiff);
						draftAmount = draftAmount.add(diff);

					}

					if (fund.getId() == null) {
						amount = amount.add(DecimalUtil.nullToZero(fund.getAmount()));// 新增
						draftAmount = draftAmount.add(DecimalUtil.nullToZero(fund.getAmount()));// 新增
						quantity = quantity.add(DecimalUtil.nullToZero(fund.getQuantity()));
					}

					if (amount.compareTo(amountInvoices) >= 0) {
						lot.setIsFunded(true);
					} else {
						lot.setIsFunded(false);
					}
					lot.setQuantityFunded(quantity);
					lot.setAmountFundedDraft(draftAmount);
					lot.setAmountFunded(amount);
				}
			}
		}
	}

	private void txSaveReceive(Fund fund, BankReceipt old_receipt, BankReceipt receipt, Lot lot,
			List<CustomerBalance> cbs) {

		this.fundRepo.SaveOrUpdate(fund);
		if (old_receipt != null)
			bankReceiptRepo.SaveOrUpdate(old_receipt);
		if (receipt != null)
			bankReceiptRepo.SaveOrUpdate(receipt);

		this.lotRepo.SaveOrUpdate(lot);

		if (cbs.size() > 0) {
			for (CustomerBalance customerBalance : cbs) {
				this.cbRepository.SaveOrUpdate(customerBalance);
			}
		}
	}

	@Override
	public ActionResult<String> Payment(Fund fund) {

		// 检查有效性，以及补上LotId的值

		if (!fund.getDC().equals(DC.Debit)) {
			return new ActionResult<>(false, "Credit/Debit is error");
		}

		Invoice invoice = invoiceHibernateRepository.getOneById(fund.getInvoiceId(), Invoice.class);

		if (invoice == null)
			return new ActionResult<>(false, "fund.InvoiceId is null");

		String lot_id = StringUtils.isAllLowerCase(invoice.getLotId()) ? fund.getLotId() : invoice.getLotId();

		Lot lot = lotRepo.getOneById(lot_id, Lot.class);

		if (lot == null)
			return new ActionResult<>(false, "lot is null");

		// 检查前台是否送了这个参数。如果确认已经送了，可以删除下面的一行代码
		fund.setContractId(invoice.getContractId());
		fund.setLotId(invoice.getLotId());
		fund.setLegalId(invoice.getLegalId());
		fund.setCommodityId(invoice.getCommodityId());
		fund.setCustomerId(invoice.getCustomerId());
		fund.setCustomerTitleId(invoice.getCustomerTitleId());

		// 检查收款的金额、是否超过了发票的金额
		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
		where.add(Restrictions.ne("Id", fund.getId()));
		List<Fund> funds = this.fundRepo.GetQueryable(Fund.class).where(where).toList();

		BigDecimal amount_sum = new BigDecimal(funds.stream().mapToDouble(x -> x.getAmount().doubleValue()).sum());

		if (fund.getAmount().add(amount_sum).compareTo(invoice.getAmount()) > 0) {

			return new ActionResult<>(false, "付款金额不允许超过发票金额");
		}

		try {

			if (!StringUtils.isEmpty(fund.getId())) {
				Fund old = this.fundRepo.getOneById(fund.getId(), Fund.class);
				this.fundRepo.SaveOrUpdate(old);
			} else {
				// PartnerBalanceService.UpdateCustomerBalance(fund.PartnerId,
				// fund.legalId, fund.commodityId)
				fundRepo.SaveOrUpdate(fund);
			}

			// 更新发票的AmountDrafted, IsExecuted标志
			where = DetachedCriteria.forClass(Fund.class);
			where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
			funds = fundRepo.GetQueryable(Fund.class).where(where).toList();

			double AmountDrafted_sum = funds.stream().filter(x -> x.getStatus().equals(Status.Draft)
					|| x.getStatus().equals(Status.Pending) || x.getStatus().equals(Status.Agreed))
					.mapToDouble(x -> x.getAmount().doubleValue()).sum();

			double QuantityDrafted_sum = funds.stream()
					.filter(x -> x.getStatus().equals(Status.Draft) || x.getStatus().equals(Status.Pending)
							|| x.getStatus().equals(Status.Agreed))
					.mapToDouble(x -> x.getQuantity().doubleValue()).sum();

			double Amount_sum = funds.stream().filter(x -> x.getStatus().equals(Status.Agreed))
					.mapToDouble(x -> x.getAmount().doubleValue()).sum();

			invoice.setAmountDrafted(new BigDecimal(AmountDrafted_sum));
			invoice.setQuantityDrafted(new BigDecimal(QuantityDrafted_sum));
			invoice.setIsExecuted(invoice.getAmount().compareTo(new BigDecimal(Amount_sum)) == 0);

			invoiceHibernateRepository.SaveOrUpdate(invoice);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<String> Delete(String id, String userId) {

		Fund fund = fundRepo.getOneById(id, Fund.class);
		if (fund == null)
			return new ActionResult<>(false, "fund is null");

		if (!fund.getCreatedId().toLowerCase().equals(userId.toLowerCase()))
			return new ActionResult<>(false, "不可以删除他人创建的记录");

		// Invoice invoice1 =
		// invoiceHibernateRepository.getOneById(fund.getInvoiceId(),Invoice.class);

		// if (invoice1 == null) return new ActionResult<>(false,"fund.InvoiceId
		// is null");

		String lot_id = fund.getLotId();

		// if(!StringUtils.isEmpty(invoice1.getLotId())) lot_id=
		// invoice1.getLotId();

		Lot lot = lotRepo.getOneById(lot_id, Lot.class);

		// 多批次发票为空
		if (lot == null)
			return new ActionResult<>(false, "lot is null");

		try {

			// 更新lot的已申请数量
			lot.setQuantityFunded(DecimalUtil.nullToZero(lot.getQuantityFunded())
					.subtract(DecimalUtil.nullToZero(fund.getQuantity())));
			// 更新lot的已申请金额
			lot.setAmountFundedDraft(DecimalUtil.nullToZero(lot.getAmountFundedDraft())
					.subtract(DecimalUtil.nullToZero(fund.getAmount())));
			if (fund.getIsExecuted()) {
				// 如果是Done的，更新lot的实际交付数量
				// lot. = lot.AmountFunded - fund.Amount;
				// 如果是Done的，更新lot的付款金额
				lot.setAmountFunded(DecimalUtil.nullToZero(lot.getAmountFunded())
						.subtract(DecimalUtil.nullToZero(fund.getAmount())));
			}

			if (org.apache.commons.lang3.StringUtils.isNotBlank(fund.getLotId())
					&& fund.getFundType().equals(FundType.Fund4Lot)) {
				DetachedCriteria dc3 = DetachedCriteria.forClass(Invoice.class);
				dc3.add(Restrictions.eq("LotId", fund.getLotId()));
				dc3.add(Restrictions.eq("FeeCode", FeeCode.Goods));// by
																	// zengshihua
																	// at
																	// 2016-08-08
				List<Invoice> invoices = this.invoiceHibernateRepository.GetQueryable(Invoice.class).where(dc3)
						.toList();
				BigDecimal amountInvoices = BigDecimal.ZERO;
				if (invoices != null && invoices.size() > 0) {
					for (Invoice p : invoices) {
						amountInvoices = amountInvoices.add(p.getDueAmount());
					}
				}
				if (lot.getAmountFunded() != null && lot.getAmountFunded().compareTo(amountInvoices) >= 0) {
					lot.setIsFunded(true);
				} else {
					lot.setIsFunded(false);
				}

			}
			Invoice invoice = null;
			ActionResult<Lot> uLotIsFundResult = null;
			if (StringUtils.isNotBlank(fund.getInvoiceId())) {
				invoice = invoiceHibernateRepository.getOneById(fund.getInvoiceId(), Invoice.class);

				// 2015-1-8 将发票金额的汇总逻辑改与收款的一致
				DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
				where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
				List<Fund> funds = fundRepo.GetQueryable(Fund.class).where(where).toList();

				BigDecimal AmountDrafted_sum = BigDecimal.ZERO;
				for (Fund x : funds) {
					if (x.getStatus().equals(Status.Draft) || x.getStatus().equals(Status.Pending)
							|| x.getStatus().equals(Status.Agreed)) {
						AmountDrafted_sum = AmountDrafted_sum.add(x.getAmount());
					}
				}
				BigDecimal QuantityDrafted_sum = BigDecimal.ZERO;
				for (Fund x : funds) {
					if (x.getStatus().equals(Status.Draft) || x.getStatus().equals(Status.Pending)
							|| x.getStatus().equals(Status.Agreed)) {
						QuantityDrafted_sum = QuantityDrafted_sum.add(x.getQuantity());
					}
				}
				invoice.setAmountDrafted(AmountDrafted_sum);
				invoice.setQuantityDrafted(QuantityDrafted_sum);
				invoice.setIsExecuted(invoice.getAmount().abs().compareTo(invoice.getAmountDrafted()) == 0);

				uLotIsFundResult = commonService.txUpdateLotIsFunded(invoice.getLotId()); // 更新资金标记
				// 原来的逻辑
			}

			// 更新客户的资金余额（分别按台头、客户、品种、币种，进行汇总） 原来的逻辑
			// CustomerBalanceService.UpdateCustomerBalance(fund.CustomerId,
			// fund.LegalId, fund.CommodityId);
			// 更新客户的资金余额（分别按台头、客户、品种、币种，进行汇总） 2016-1-8 与收款的更新客户的资金余额逻辑保持一致
			List<CustomerBalance> cbs = new ArrayList<>();
			if (lot != null) {
				cbs = commonService.txSyncCustomerBalance(lot.getCustomerId(), lot.getLegalId(), lot.getCommodityId());
			}

			// yunsq 2016年7月26日 17:09:02 收款，更新水单金额
			BankReceipt receipt = null;
			if (fund.getDC().equals("C") && StringUtils.isNotBlank(fund.getBankReceiptId())) {
				receipt = bankReceiptRepo.getOneById(fund.getBankReceiptId().toString(), BankReceipt.class);
				if (receipt != null) {
					receipt.setConfirmedAmount(receipt.getConfirmedAmount().subtract(fund.getAmount()));
					receipt.setUnConfrimAmount(receipt.getUnConfrimAmount().subtract(fund.getAmount()));

					if (receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == 0)
						receipt.setStatus(BankReceiptStatus.ConfirmedFinish);
					else if (receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == 1
							&& receipt.getConfirmedAmount().compareTo(receipt.getAmount()) == -1)
						receipt.setStatus(BankReceiptStatus.Confirmed);
					else
						receipt.setStatus(BankReceiptStatus.Registered);
				}

			}
			txDelete(lot, fund, invoice, uLotIsFundResult, cbs, receipt);

			return new ActionResult<>(true, MessageCtrm.DeleteSuccess);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ActionResult<>(false, e.getMessage());
		}
	}

	private void txDelete(Lot lot, Fund fund, Invoice invoice, ActionResult<Lot> uLotIsFundResult,
			List<CustomerBalance> cbs, BankReceipt receipt) {
		this.lotRepo.SaveOrUpdate(lot);
		this.fundRepo.PhysicsDelete(fund);
		if (invoice != null)
			this.invoiceHibernateRepository.SaveOrUpdate(invoice);
		if (uLotIsFundResult != null && uLotIsFundResult.getData() != null) {
			Lot curLot = uLotIsFundResult.getData();
			this.lotRepo.SaveOrUpdate(curLot);
		}
		if (cbs.size() > 0) {
			for (CustomerBalance customerBalance : cbs) {
				this.cbRepository.SaveOrUpdate(customerBalance);
			}
		}
		if (receipt != null)
			this.bankReceiptRepo.SaveOrUpdate(receipt);

	}

	@Override
	public ActionResult<List<Fund>> FundsByContractId(String contractId) {

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("ContractId", contractId));

		List<Fund> list = this.fundRepo.GetQueryable(Fund.class).where(where).toList();

		list = this.commonService.SimplifyDataFundList(list);

		ActionResult<List<Fund>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;

	}

	@Override
	public ActionResult<List<Fund>> FundsByLotId(String lotId) {

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("LotId", lotId));

		List<Fund> list = this.fundRepo.GetQueryable(Fund.class).where(where).toList();

		list = this.commonService.SimplifyDataFundList(list);

		ActionResult<List<Fund>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<List<Fund>> FundsByInvoiceId(String InvoiceId) {

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("IsHidden", false));
		where.add(Restrictions.eq("InvoiceId", InvoiceId));

		List<Fund> list = this.fundRepo.GetQueryable(Fund.class).where(where).toList();

		list = this.commonService.SimplifyDataFundList(list);

		ActionResult<List<Fund>> result = new ActionResult<>();

		result.setSuccess(true);
		result.setData(list);

		return result;
	}

	@Override
	public ActionResult<Fund> GetById(String fundId) {

		ActionResult<Fund> result = new ActionResult<>();
		Fund fund = this.fundRepo.getOneById(fundId, Fund.class);
		if (org.apache.commons.lang3.StringUtils.isNoneBlank(fund.getCustomerTitleId())) {
			fund.setCustomerTitle(this.customerTitleRepo.getOneById(fund.getCustomerTitleId(), CustomerTitle.class));
		}
		if (StringUtils.isNotBlank(fund.getLegalId())) {
			fund.setLegal(this.legalRepo.getOneById(fund.getLegalId(), Legal.class));
		}
		if (StringUtils.isNotBlank(fund.getInvoiceId())) {
			fund.setInvoice(this.InvoiceRepo.getOneById(fund.getInvoiceId(), Invoice.class));
		}
		if (StringUtils.isNotBlank(fund.getCommodityId())) {
			fund.setCommodity(this.commodityRepo.getOneById(fund.getCommodityId(), Commodity.class));
		}
		if (StringUtils.isNotBlank(fund.getCustomerId())) {
			fund.setCustomer(this.customerRepo.getOneById(fund.getCustomerId(), Customer.class));
		}
		if (StringUtils.isNotBlank(fund.getLotId())) {
			fund.setLot(this.lotRepo.getOneById(fund.getLotId(), Lot.class));
		}
		if (StringUtils.isNotBlank(fund.getBankReceiptId())) {
			fund.setBankReceipt(this.bankReceiptRepo.getOneById(fund.getBankReceiptId(), BankReceipt.class));
		}
		result.setSuccess(true);
		result.setData(this.commonService.SimplifyDataFund(fund));
		return result;
	}

	@Override
	public ActionResult<Fund> CreatePaymentDraft(Fund fund) {

		Invoice invoice = invoiceHibernateRepository.getOneById(fund.getInvoiceId(), Invoice.class);

		if (invoice == null)
			return new ActionResult<>(false, "fund.InvoiceId is null");

		Contract contract = contractHibernateRepository.getOneById(invoice.getContractId(), Contract.class);
		if (contract == null)
			return new ActionResult<>(false, "Invoice.ContractId is null");

		// -------------------

		if (!StringUtils.isEmpty(fund.getLotId())) // 得到该批次项下所有付款
		{
			if (invoice.getPFA().equals(InvoiceType.Note)) {
				if (StringUtils.isEmpty(fund.getId())) {
					fund.setStatus(Status.Agreed);
					fund.setIsApproved(true);
				}

				fundRepo.SaveOrUpdate(fund);

				return new ActionResult<>(true, "成功创建付款的草稿记录", fund);
			}

			// #region
			Lot lot = lotRepo.getOneById(fund.getLotId(), Lot.class);
			if (lot != null) {
				// 检查前台是否送了这个参数。如果确认已经送了，可以删除下面的一行代码
				fund.setContractId(lot.getContractId());

				DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
				where.add(Restrictions.eq("LotId", fund.getLotId()));
				where.add(Restrictions.ne("Id", fund.getId()));
				List<Fund> fundsbylot = fundRepo.GetQueryable(Fund.class).where(where).toList();

				// 该批次全部付款
				BigDecimal sumFundQuantityByLot = new BigDecimal(
						fundsbylot.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum())
								.add(fund.getQuantity());

				if (sumFundQuantityByLot.compareTo(lot.getQuantityMore()) > 0) {

					return new ActionResult<>(false, "该批次所有付款数量超过批次数量（含溢短装）支付范围\"");
				}

				if (contract.getIsInternal() && invoice.getPFA().equals("P")) {
					// 内部的临时发票不需要判断开票数量和交付明细数量
				} else {
					// #region 判断全部数量与交付明数量

					where = DetachedCriteria.forClass(Fund.class);
					where.add(Restrictions.eq("LotId", fund.getLotId()));
					List<Invoice> invoicesByLot = invoiceHibernateRepository.GetQueryable(Invoice.class).where(where)
							.toList();

					List<Invoice> storages1 = invoicesByLot.stream()
							.filter(x -> x.getPFA().equals("P") && x.getAdjustId() == null)
							.collect(Collectors.toList());
					List<Invoice> storages2 = invoicesByLot.stream()
							.filter(x -> x.getPFA().equals("F") || x.getPFA().equals("A")).collect(Collectors.toList());

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

					BigDecimal StorageSummary1 = new BigDecimal(
							lstStorages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
					BigDecimal NoticeSummary1 = new BigDecimal(
							lstNotices.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
					BigDecimal tmpSum00 = StorageSummary1.compareTo(NoticeSummary1) > 0 ? StorageSummary1
							: NoticeSummary1;

					if (sumFundQuantityByLot.compareTo(tmpSum00) > 0) {
						return new ActionResult<>(false, "该批次所有发票付款数量必须小于发票交付明细（或通知货量）的数量！");
					}
				}
			}
		}

		DetachedCriteria where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
		where.add(Restrictions.ne("Id", fund.getId()));
		List<Fund> funds = fundRepo.GetQueryable(Fund.class).where(where).toList();

		if (contract.getIsInternal() && invoice.getPFA().equals("P")) {
			// 内部的临时发票不需要判断开票数量和交付明细数量
		} else {
			BigDecimal stroageQuantity = BigDecimal.ZERO;
			BigDecimal noticeQuantity = BigDecimal.ZERO;

			if (invoice.getStorages() != null && invoice.getStorages().size() > 0) {
				stroageQuantity = new BigDecimal(
						invoice.getStorages().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
			}

			if (invoice.getNotices() != null && invoice.getNotices().size() > 0) {
				noticeQuantity = new BigDecimal(
						invoice.getNotices().stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
			}

			BigDecimal sumQuantity = new BigDecimal(
					Math.max(stroageQuantity.doubleValue(), noticeQuantity.doubleValue()));

			BigDecimal quantity_sum = new BigDecimal(
					funds.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

			if (fund.getQuantity().add(quantity_sum).compareTo(sumQuantity) > 0)
				return new ActionResult<>(false, "累计付款数量不能大于对应发票关联货的最大值！");

		}

		BigDecimal InvoiceAmount = invoice.getAmount();

		if (invoice.getMT().equals(MT4Invoice.Make)) {

			InvoiceAmount = invoice.getAmount().abs();
		}

		BigDecimal amount_sum = new BigDecimal(funds.stream().mapToDouble(x -> x.getAmount().doubleValue()).sum());

		if (fund.getAmount().add(amount_sum).compareTo(InvoiceAmount) > 0)
			return new ActionResult<>(false, "付款金额不允许超过发票金额");

		if (StringUtils.isEmpty(fund.getId())) {
			fund.setStatus(Status.Agreed);
			fund.setIsApproved(true);
		}

		fundRepo.SaveOrUpdate(fund);

		// 更新发票的IsExecuted标志

		where = DetachedCriteria.forClass(Fund.class);
		where.add(Restrictions.eq("InvoiceId", fund.getInvoiceId()));
		where.add(Restrictions.ne("Status", Status.Deny));
		funds = fundRepo.GetQueryable(Fund.class).where(where).toList();

		invoice.setAmountDrafted(new BigDecimal(funds.stream().mapToDouble(x -> x.getAmount().doubleValue()).sum()));
		invoice.setQuantityDrafted(
				new BigDecimal(funds.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum()));
		invoiceHibernateRepository.SaveOrUpdate(invoice);

		return new ActionResult<>(true, "成功创建付款的草稿记录");

	}

	@Override
	public List<Fund> Funds(Criteria criteria, int pageSize, int pageIndex, RefUtil total, String sortBy,
			String orderBy) {

		return (List<Fund>) this.fundRepo.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	@Override
	public ActionResult<Fund> CreatePaymentDraft4Lot(Fund fund) {

		if (fund == null)
			return new ActionResult<>(false, MessageCtrm.ParamError);
		Lot lot = this.lotRepo.getOneById(fund.getLotId(), Lot.class);
		if (lot == null)
			return new ActionResult<>(false, "没有找到对应批次。");
		if (fund.getQuantity().compareTo(lot.getQuantityMore()) > 0)
			return new ActionResult<>(false, "付款数量不可以大于批次的溢短装！");

		DetachedCriteria dc1 = DetachedCriteria.forClass(VFund.class);
		dc1.add(Restrictions.eq("LotId", lot.getId()));
		dc1.add(Restrictions.eq("FundType", FundType.Fund4Lot));
		dc1.add(Restrictions.ne("Status", Status.Deny));
		dc1.add(Restrictions.neOrIsNotNull("Id", fund.getId()));
		List<VFund> vFundList = this.vfundRepo.GetQueryable(VFund.class).where(dc1).toList();

		BigDecimal quantityFundedSum = BigDecimal.ZERO;
		BigDecimal amountFundedDraftSum = BigDecimal.ZERO;
		for (VFund vf : vFundList) {
			quantityFundedSum = quantityFundedSum.add(vf.getQuantity());
			amountFundedDraftSum = amountFundedDraftSum.add(vf.getAmount());
		}
		quantityFundedSum = quantityFundedSum.add(fund.getQuantity());
		amountFundedDraftSum = amountFundedDraftSum.add(fund.getAmount());

		lot.setQuantityFunded(quantityFundedSum);
		lot.setAmountFundedDraft(amountFundedDraftSum);
		this.lotRepo.SaveOrUpdate(lot);

		String id = this.fundRepo.SaveOrUpdateRetrunId(fund);
		fund.setId(id);
		String message = "成功创建付款的草稿记录。";
		if (fund.getIsIniatiated()) {
			message = "成功创建付款记录";
		}
		return new ActionResult<>(true, message, fund);
	}
}
