package com.smm.ctrm.bo.impl.Physical;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smm.ctrm.bo.Basis.ProductService;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Finance.FundService;
import com.smm.ctrm.bo.Finance.InvoiceService;
import com.smm.ctrm.bo.Physical.ContractService;
import com.smm.ctrm.bo.Physical.LotService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.bo.Physical.StorageService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfoToken;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Bvi2Sm;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Product;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.Warehouse;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Grade;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.apiClient.ContractParams;
import com.smm.ctrm.domain.apiClient.CpContract;
import com.smm.ctrm.domain.apiClient.ImportContractParams;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.LoginHelper;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.StringUtil;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PremiumType;
import com.smm.ctrm.util.Result.SpotType;
import com.smm.ctrm.util.Result.Status;

/**
 * Created by hao.zheng on 2016/5/3.
 *
 */
@Service
@Transactional
public class ContractServiceImpl implements ContractService {

	private Logger logger = Logger.getLogger(this.getClass());

	@Autowired
	private HibernateRepository<Contract> repository;

	@Autowired
	private HibernateRepository<Lot> lotRepository;

	@Autowired
	private HibernateRepository<Grade> gradeRepository;

	@Autowired
	private HibernateRepository<Commodity> commodityRepository;

	@Autowired
	private HibernateRepository<Legal> legalRepository;

	@Autowired
	private HibernateRepository<Bvi2Sm> bvi2SmRepository;

	@Autowired
	private HibernateRepository<Contract> contractRepository;

	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleRepository;

	@Autowired
	private HibernateRepository<Brand> brandRepository;

	@Autowired
	private HibernateRepository<User> userRepository;
	@Autowired
	private HibernateRepository<PricingRecord> pricingRecordRepo;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepo;

	@Autowired
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	private HibernateRepository<Fund> fundRepository;

	@Autowired
	private HibernateRepository<Storage> storageRepository;
	
	@Autowired
	private HibernateRepository<Warehouse> warehouseRepository;
	

	@Autowired
	private CommonService commonService;

	@Autowired
	private ProductService productService;

	@Autowired
	private LotService lotService;

	@Autowired
	private StorageService storageService;

	@Autowired
	private ReceiptService receiptService;
	
	@Autowired
	private FundService fundService;
	
	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private HibernateRepository<CustomerTitle> ctrepository;

	private final static String contractNo_Repeat_Msg = "合同编号重复，请修改后重试。";

	@Override
	public Criteria GetCriteria() {

		return this.repository.CreateCriteria(Contract.class);

	}

	@Override
	public ActionResult<String> GetMaxSerialNo(String legalId, String spotDirection, Date tradeDate,
			String commodityId) {
		String no = MaxContractSerialNo(tradeDate, legalId, spotDirection, commodityId);
		ActionResult<String> tempVar = new ActionResult<String>();
		tempVar.setSuccess(true);
		tempVar.setData(no);
		tempVar.setTotal(Integer.parseInt(no));
		return tempVar;
	}

	@Override
	public ActionResult<String> Delete(String contractId, String userId) {

		// 取出自己
		Contract contract = this.repository.getOneById(contractId, Contract.class);
		String counterpartId = contract.getCounterpartId();
		// 先取出有多少个批次
		Criteria criteria = this.lotRepository.CreateCriteria(Lot.class);
		criteria.add(Restrictions.eq("ContractId", contractId));
		List<Lot> lots = this.lotRepository.GetList(criteria);

		if (!userId.equalsIgnoreCase(contract.getCreatedId())) {
			return new ActionResult<>(false, "不可以删除他人创建的记录。");
		}

		if (contract.getRivalOrderID() != null) {
			// 判断当前合同的批次是否关联了 /票/资/保/拆/ 点/货
			boolean curIsUpdate = false;
			for (Lot item : lots) {
				curIsUpdate = commonService.IsUpdateByLot(item.getId());
			}
			// 判断对手订单的批次是否关联了 /票/资/保/拆/ 点/货
			Contract contractR = repository.getOneById(contract.getRivalOrderID(), Contract.class);
			boolean rivalIsUpdate = false;
			List<Lot> contractRLots = lotRepository.GetQueryable(Lot.class)
					.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("ContractId", contractR.getId())))
					.toList();
			for (Lot item : contractRLots) {
				rivalIsUpdate = commonService.IsUpdateByLot(item.getId());
			}
			if (curIsUpdate || rivalIsUpdate) {
				return new ActionResult<>(false, "当前订单或对手订单的批次已关联相关业务（票/资/保/拆），不能删除！");
			}
			// 1、将销售订单相关货物的标记还原（a、来源的来源IsBorrow=true b、来源的IsBorrow=true
			// IsIn=false c、当前明细删除）
			// 2、将采购订单相关货物的标记还原（a、来源的countpartid2=null b、当前明细删除）
			ActionResult<String> contractResult = commonService.ResetStorageByContract(contract);
			if (!contractResult.isSuccess()) {
				logger.info(contractResult.getMessage());
				throw new RuntimeException(contractResult.getMessage());
			}
			ActionResult<String> contractRResult = commonService.ResetStorageByContract(contractR);

			if (!contractRResult.isSuccess()) {
				logger.info(contractRResult.getMessage());
				throw new RuntimeException(contractRResult.getMessage());
			}
			// 3、删除批次/删除订单（a、删除销售订单的批次 b、删除销售订单 c、删除采购订单的批次 d、删除采购订单）
			// 删除下属的批次
			for (Lot lot : lots) {
				lotRepository.PhysicsDelete(lot.getId(), Lot.class);
			}
			repository.PhysicsDelete(contractId, Contract.class);

			for (Lot lot : contractRLots) {
				lotRepository.PhysicsDelete(lot.getId(), Lot.class);
			}
			repository.PhysicsDelete(contractR.getId(), Contract.class);

			return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
		}

		// 删除对手合同 bvi -sm
		if (counterpartId != null) {
			Contract couterpart = this.repository.getOneById(counterpartId, Contract.class);

			Criteria criteriaLots = this.lotRepository.CreateCriteria(Lot.class);
			criteria.add(Restrictions.eq("ContractId", counterpartId));
			List<Lot> counterLots = this.lotRepository.GetList(criteriaLots);

			for (Lot lot : counterLots) {
				this.lotRepository.PhysicsDelete(lot.getId(), Lot.class);
			}
			this.repository.PhysicsDelete(couterpart.getId(), Contract.class);
		}

		// 删除下属的批次
		for (Lot lot : lots) {
			this.lotRepository.PhysicsDelete(lot.getId(), Lot.class);
		}
		this.repository.PhysicsDelete(contractId, Contract.class);
		return new ActionResult<>(true, MessageCtrm.DeleteSuccess);
	}

	/**
	 * 根据Id取得单个实体
	 * 
	 * @param contractId
	 * @return
	 */
	@Override
	public ActionResult<Contract> GetById(String contractId) {

		Contract contract = this.repository.getOneById(contractId, Contract.class);

		Contract obj = null;
		if (contract != null) {
			obj = commonService.SimplifyData(contract);

			Criteria criteria = this.lotRepository.CreateCriteria(Lot.class);
			criteria.add(Restrictions.eq("ContractId", contractId));
			List<Lot> lots = this.lotRepository.GetList(criteria);
			obj.setLots(commonService.SimplifyDataLotList(lots));

			Criteria criteriaGrade = this.gradeRepository.CreateCriteria(Grade.class);
			criteria.add(Restrictions.eq("ContractId", contractId));
			List<Grade> grades = this.gradeRepository.GetList(criteriaGrade);

			obj.setGrades(commonService.SimplifyDataGradeList(grades));
		}
		/**
		 * 判断是否已经审核和已经发生后续业务
		 */
		ActionResult<Contract> tempVar = new ActionResult<Contract>();
		tempVar.setSuccess(true);
		tempVar.setData(obj);
		return tempVar;
	}

	/**
	 * 根据Id取得单个实体
	 * 
	 * @param id
	 * @return
	 */
	@Override
	public ActionResult<Contract> ContractViewById(String id) {

		Contract contract = this.repository.getOneById(id, Contract.class);
		Contract obj = new Contract();
		if (contract != null) {
			obj = com.smm.ctrm.util.BeanUtils.copy(contract);

			obj.setLots(commonService.SimplifyDataLotList(getDataByContractId(Lot.class, contract.getId())));

			obj.setFunds(getDataByContractId(Fund.class, contract.getId()));

			obj.setStorages(
					commonService.SimplifyDataStorageList(getDataByContractId(Storage.class, contract.getId())));

			obj.setInvoices(
					commonService.SimplifyDataInvoiceList(getDataByContractId(Invoice.class, contract.getId())));

			obj.setPricingRecords(commonService
					.SimplifyDataPricingRecordList(getDataByContractId(PricingRecord.class, contract.getId())));

			obj.setPositions(
					commonService.SimplifyDataPositionList(getDataByContractId(Position.class, contract.getId())));
		}
		return new ActionResult<>(true, "", obj);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getDataByContractId(Class<T> clazz, String contractId) {
		List<T> list = (List<T>) repository.getHibernateTemplate()
				.findByCriteria(DetachedCriteria.forClass(clazz).add(Restrictions.eq("ContractId", contractId)));
		return list;
	}

	/**
	 * 获取指定客户的全部合同
	 * 
	 * @param customerId
	 * @return
	 */
	@Override
	public ActionResult<List<Contract>> ContractsByCustomerId(String customerId) {

		Criteria criteriaPr = this.repository.CreateCriteria(Contract.class);
		criteriaPr.add(Restrictions.and(Restrictions.eq("CustomerId", customerId), Restrictions.eq("IsHidden", false)));
		List<Contract> obj = this.repository.GetList(criteriaPr);

		ActionResult<List<Contract>> tempVar = new ActionResult<List<Contract>>();
		tempVar.setSuccess(true);
		tempVar.setData(obj);
		return tempVar;
	}

	@Override
	public ActionResult<Contract> SaveHeadOfContractRegular(Contract contract) {

		List<Lot> lots = new ArrayList<>();
		Contract oldContract = null;
		if (contract.getId() != null) {
			lots = this.lotRepository.GetList(
					lotRepository.CreateCriteria(Lot.class).add(Restrictions.eq("ContractId", contract.getId())));
			oldContract = this.contractRepository.getOneById(contract.getId(), Contract.class);
		}

		ActionResult<BigDecimal> isValidResult = isValidContract(contract, lots);
		if (!isValidResult.isSuccess()) {
			return new ActionResult<>(false, isValidResult.getMessage());
		}
		contract.setQuantityOfLots(isValidResult.getData());
		if (StringUtils.isBlank(contract.getSerialNo())) {
			// 给合同编号
			SetContractNo(contract);
		}

		// 增加产品名称到字典中，供下次使用
		addProductForContract(contract);

		// 原始创建的合同，都改属于"Draft"状态
		if (contract.getId() == null) {
			contract.setStatus(Status.Draft);
		}

		if (contract.getIsIniatiated()) {
			contract.setStatus(Status.Agreed);
		}

		this.repository.SaveOrUpdate(contract);

		/**
		 * 如果合同的某些要素修改，且这些要素是Lot中冗余的，则必须修改Lot的这些冗余字段的值
		 */
		for (Lot lot : lots) {
			/**
			 * 作为非常重要的冗余,不可删除
			 */
			lot.setLegalId(contract.getLegalId());
			lot.setCustomerId(contract.getCustomerId());
			lot.setCurrency(contract.getCurrency());
			lot.setHeadNo(contract.getHeadNo());
			lot.setSerialNo(contract.getSerialNo());
			lot.setPrefixNo(contract.getPrefix());
			lot.setStatus(contract.getStatus());
			lot.setSpotDirection(contract.getSpotDirection());
			lot.setCommodityId(contract.getCommodityId());
			lot.setProduct(contract.getProduct());
			lot.setIsReBuy(contract.getIsReBuy());
			this.lotRepository.SaveOrUpdate(lot);

			/**
			 * 修改商品名称， 不让改 by zengshihua 2016/09/13
			 */
			/*
			 * if(contract.getId()!=null){
			 * if(!contract.getProduct().equals(oldContract.getProduct())){
			 * DetachedCriteria dc=DetachedCriteria.forClass(Storage.class);
			 * dc.add(Restrictions.eq("LotId", lot.getId())); List<Storage>
			 * storages=this.storageRepository.GetQueryable(Storage.class).where
			 * (dc).toList(); for (Storage s : storages) {
			 * s.setProduct(contract.getProduct());
			 * this.storageRepository.SaveOrUpdate(s); } } }
			 */
		}

		// 重新取值
		contract = this.repository.getOneById(contract.getId(), Contract.class);
		if (contract == null) {
			return new ActionResult<>(false, "数据获取失败，请重新保存！");
		}
		// 如果是初始订单，不用审批，修正直接生效
		if (contract.getIsIniatiated()) {
			contract.setIsApproved(contract.getIsApproved());
			contract.setStatus(contract.getStatus());

			/**
			 * 增加了关于"订单修正"的逻辑(withhold = true即修正客户,则不执行覆盖和隐藏动作）
			 */
			if (contract.getContractAmendId() != null && contract.getWithHold() == false) {
				// 如果订单修正获批，新订单的条款赋值给原订单
				Contract contractAmended = this.repository.getOneById(contract.getContractAmendId(), Contract.class);
				if (contractAmended != null) {
					contractAmended.setQuantity(contract.getQuantity());
					contractAmended.setDeliveryTerm(contract.getDeliveryTerm());
					contractAmended.setPaymentTerm(contract.getPaymentTerm());
					contractAmended.setDocumentNo(contract.getDocumentNo());
					contractAmended.setProduct(contract.getProduct());

					contract.setIsHidden(true); // 修订的合同隐藏掉

					if (!contractAmended.getHeadNo()
							.substring(contractAmended.getHeadNo().length() - 1, contractAmended.getHeadNo().length())
							.equals("A")) {
						contractAmended.setHeadNo(contractAmended.getHeadNo() + "A");
					}

					// 请用户确认，是否还有可能"修正"的成员
					this.repository.SaveOrUpdate(contractAmended);
				}
			}
			this.repository.SaveOrUpdate(contract);
		}
		/**
		 * 如果是审核通过的
		 */
		return new ActionResult<>(true, MessageCtrm.SaveSuccess, contract);
	}

	/**
	 * 数据有效性检查
	 * 
	 * @param contract
	 * @param lots
	 * @return
	 */
	private ActionResult<BigDecimal> isValidContract(Contract contract, List<Lot> lots) {
		if (contract == null) {
			logger.info("Param is null: Contract");
			return new ActionResult<>(false, "Param is null: Contract");
		}

		if (contract.getStatus().equals(Status.Pending)) {
			logger.info("不可修改处于待审状态的订单。");
			return new ActionResult<>(false, "不可修改处于待审状态的订单。");
		}
		// zengshihua/2016/09/19
		// if (contract.getIsIniatiated() == false &&
		// contract.getStatus().equals(Status.Agreed)) {
		// logger.info("不可修改已经审核同意的订单。");
		// return new ActionResult<>(false, "不可修改已经审核同意的订单。");
		// }

		/**
		 * 检查重号
		 */
		if (StringUtils.isNotBlank(contract.getSerialNo())) {
			Criteria checkCriteria = this.repository.CreateCriteria(Contract.class);
			checkCriteria.add(Restrictions.eq("SerialNo", contract.getSerialNo()));
			checkCriteria.add(Restrictions.eq("Prefix", contract.getPrefix()));
			checkCriteria.add(Restrictions.eq("SpotDirection", contract.getSpotDirection()));
			checkCriteria.add(Restrictions.eq("LegalId", contract.getLegalId()));
			checkCriteria.add(Restrictions.eq("CommodityId", contract.getCommodityId()));
			checkCriteria.add(Restrictions.neOrIsNotNull("Id", contract.getId()));
			List<Contract> listContract = this.repository.GetList(checkCriteria);
			if (listContract != null && listContract.size() > 0) {
				logger.info(contractNo_Repeat_Msg);
				return new ActionResult<>(false, contractNo_Repeat_Msg);
			}
		}

		BigDecimal sum = new BigDecimal(0);
		for (Lot lot : lots) {
			sum = sum.add(lot.getQuantity());
		}
		if (contract.getQuantity().compareTo(sum) < 0) {
			logger.info("批次数量之和不可以大于订单数量。");
			return new ActionResult<>(false, "批次数量之和不可以大于订单数量。");
		}

		return new ActionResult<>(true, "", sum);
	}

	/**
	 * 增加产品名称到字典中，供下次使用
	 * 
	 * @param contract
	 */
	private void addProductForContract(Contract contract) {
		Product product = new Product();
		product.setCommodityId(contract.getCommodityId());
		product.setName(contract.getProduct());
		product.setIsHidden(false);
		productService.AddProduct(product);
	}

	/**
	 * 特殊的业务，同时创建Bvi的销售合同和Sm的采购合同
	 */
	@Override
	public ActionResult<Contract> SaveHead4BviToSm(Contract contract) {
		/**
		 * 数据有效性检查
		 */
		if (contract == null) {
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(false);
			tempVar.setMessage("Param is null: Contract");
			return tempVar;
		}

		if (contract.getStatus().equals(Status.Pending)) {
			ActionResult<Contract> tempVar2 = new ActionResult<Contract>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("不可修改处于待审状态的订单。");
			return tempVar2;
		}

		if (contract.getIsIniatiated() == false && contract.getStatus().equals(Status.Agreed)) {
			ActionResult<Contract> tempVar3 = new ActionResult<Contract>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("不可修改已经审核同意的订单。");
			return tempVar3;
		}

		if (contract.getId() != null) {
			Criteria lotCriteria = this.lotRepository.CreateCriteria(Lot.class);
			lotCriteria.add(Restrictions.eq("ContractId", contract.getId()));
			List<Lot> lots = this.lotRepository.GetList(lotCriteria);
			BigDecimal b = new BigDecimal(0);
			for (Lot lot : lots) {
				b = b.add(lot.getQuantity());
			}
			if (contract.getQuantity().compareTo(b) == -1) {
				ActionResult<Contract> tempVar4 = new ActionResult<Contract>();
				tempVar4.setSuccess(false);
				tempVar4.setMessage("批次数量之和不可以大于订单数量。");
				return tempVar4;
			}
		}

		// 给合同编号
		if (StringUtils.isBlank(contract.getSerialNo())) {
			SetContractNo(contract);
		} else {
			/**
			 * 检查重号
			 */
			Criteria checkCriteria = this.repository.CreateCriteria(Contract.class);
			checkCriteria.add(Restrictions.and(Restrictions.eq("SerialNo", contract.getSerialNo()),
					Restrictions.eq("Prefix", contract.getPrefix()),
					Restrictions.eq("SpotDirection", contract.getSpotDirection()),
					Restrictions.eq("LegalId", contract.getLegalId()),
					Restrictions.eq("CommodityId", contract.getCommodityId()), Restrictions.ne("Id", contract.getId())

			));
			List<Contract> duplicateSerialNo = this.repository.GetList(checkCriteria);
			if (duplicateSerialNo != null && duplicateSerialNo.size() > 0) {
				ActionResult<Contract> tempVar5 = new ActionResult<Contract>();
				tempVar5.setSuccess(false);
				tempVar5.setMessage(contractNo_Repeat_Msg);
				return tempVar5;
			}

		}

		/**
		 * 先取得处理特别业务所需要的2个内部台头的数据
		 */
		// var bvi2Sm = getRepository().<Bvi2Sm>GetList().FirstOrDefault();

		List<Bvi2Sm> bvi2Sms = this.bvi2SmRepository.GetList(Bvi2Sm.class);
		Bvi2Sm bvi2Sm = null;
		if (bvi2Sms != null && bvi2Sms.size() > 0) {
			bvi2Sm = bvi2Sms.get(0);
		}

		if (bvi2Sm == null) {
			ActionResult<Contract> tempVar6 = new ActionResult<Contract>();
			tempVar6.setSuccess(false);
			tempVar6.setMessage("bvi2sm is null");
			return tempVar6;
		}

		contract.setCommodity(this.commodityRepository.getOneById(contract.getCommodityId(), Commodity.class));
		;
		/**
		 * 这是新增的情况
		 */
		if (contract.getId() == null) {
			// 保存bvi销售合同之前，赋默认的值给这个合同记录
			contract.setLegalId(bvi2Sm.getBviLegalId());
			contract.setCustomerId(bvi2Sm.getSmCustomerId());
			contract.setCustomerTitleId(bvi2Sm.getSmTitleId());
			contract.setSpotDirection(SpotType.Sell);
			contract.setStatus(Status.Draft);
			if (contract.getIsIniatiated()) {
				contract.setStatus(Status.Agreed);
			}
			contract.setIsInternal(true);

			// 保存销售合同并返回SellId
			this.repository.SaveOrUpdateRetrunId(contract);

			String yy = contract.getPrefix().substring(5, 7);

			Contract purchaseContract = new Contract();
			BeanUtils.copyProperties(contract, purchaseContract);
			purchaseContract.setId(null);
			purchaseContract.setLegalId(bvi2Sm.getSmLegalId());
			purchaseContract.setCustomerId(bvi2Sm.getBviCustomerId());
			purchaseContract.setCustomerTitleId(bvi2Sm.getBviTitleId());
			purchaseContract.setSpotDirection(SpotType.Purchase);
			purchaseContract.setCounterpartId(contract.getId());
			purchaseContract.setIsIniatiated(contract.getIsIniatiated());
			purchaseContract.setStatus(Status.Draft);
			if (purchaseContract.getIsIniatiated()) {
				purchaseContract.setStatus(Status.Agreed);
			}
			purchaseContract.setCommodity(contract.getCommodity());
			purchaseContract.setPrefix(
					SpotType.Purchase + purchaseContract.getCommodity().getCode() + bvi2Sm.getSmLegalCode() + yy);
			purchaseContract.setHeadNo(purchaseContract.getPrefix() + purchaseContract.getSerialNo());

			/**
			 * 检查采购合同编号是重复
			 */
			// var duplicateSerialNo1 =
			// getRepository().<Contract>GetList().FirstOrDefault(x =>
			// x.SerialNo == purchaseContract.SerialNo
			// && x.SpotDirection == purchaseContract.SpotDirection &&
			// x.LegalId == purchaseContract.LegalId
			// && x.CommodityId == purchaseContract.CommodityId && x.Id !=
			// purchaseContract.Id);
			Criteria purchaseCriteria = this.repository.CreateCriteria(Contract.class);
			purchaseCriteria.add(Restrictions.and(Restrictions.eq("SerialNo", purchaseContract.getSerialNo()),
					Restrictions.eq("SpotDirection", purchaseContract.getSpotDirection()),
					Restrictions.eq("LegalId", purchaseContract.getLegalId()),
					Restrictions.eq("CommodityId", purchaseContract.getCommodityId()),
					Restrictions.ne("Id", purchaseContract.getId())

			));

			List<Contract> duplicateSerialNo1 = this.repository.GetList(purchaseCriteria);

			if (duplicateSerialNo1 != null && duplicateSerialNo1.size() > 0) {
				SetContractNo(purchaseContract);
			}

			// 保存采购合同
			this.repository.SaveOrUpdateRetrunId(purchaseContract);

			// 保存销售合同的CounterpartId
			contract = this.repository.getOneById(contract.getId(), Contract.class);
			contract.setCounterpartId(purchaseContract.getId());
			this.repository.SaveOrUpdate(contract);
		} else // 这是修改的情况
		{
			contract.setIsInternal(true);
			contract.setStatus(Status.Draft);
			this.repository.SaveOrUpdate(contract);

			// 不修改对应的"商贸采购合同"
		}

		/**
		 * 如果合同的某些要素修改，且这些要素是Lot中冗余的，则必须修改Lot的这些冗余字段的值
		 */
		Criteria lotsCriteria = this.lotRepository.CreateCriteria(Lot.class);
		lotsCriteria.add(Restrictions.eq("ContractId", contract.getId()));
		List<Lot> lots2 = this.lotRepository.GetList(lotsCriteria);
		for (Lot lot : lots2) {
			/**
			 * 作为非常重要的冗余,不可删除
			 */
			lot.setLegalId(contract.getLegalId());
			lot.setCustomerId(contract.getCustomerId());
			lot.setCurrency(contract.getCurrency());
			lot.setHeadNo(contract.getHeadNo());
			lot.setSerialNo(contract.getSerialNo());
			lot.setPrefixNo(contract.getPrefix());
			lot.setStatus(contract.getStatus());
			lot.setSpotDirection(contract.getSpotDirection());
			lot.setCommodityId(contract.getCommodityId());
			lot.setProduct(contract.getProduct());
			this.lotRepository.SaveOrUpdate(lot);
		}

		// 重新取值
		contract = this.repository.getOneById(contract.getId(), Contract.class);
		ActionResult<Contract> tempVar7 = new ActionResult<Contract>();
		tempVar7.setSuccess(true);
		tempVar7.setData(contract);
		tempVar7.setMessage(MessageCtrm.SaveSuccess);
		return tempVar7;
	}

	/**
	 * /修改商贸采购合同时，不需要任何特别的业务处理
	 */
	@Override
	public ActionResult<Contract> SaveHead4SmToBvi(Contract contract) {
		if (contract == null) {
			ActionResult<Contract> tempVar = new ActionResult<Contract>();
			tempVar.setSuccess(false);
			tempVar.setMessage("Param is null: Contract");
			return tempVar;
		}

		if (contract.getStatus().equals(Status.Pending)) {
			ActionResult<Contract> tempVar2 = new ActionResult<Contract>();
			tempVar2.setSuccess(false);
			tempVar2.setMessage("不可修改处于待审状态的订单。");
			return tempVar2;
		}

		if (contract.getIsIniatiated() == false && contract.getStatus().equals(Status.Agreed)) {
			ActionResult<Contract> tempVar3 = new ActionResult<Contract>();
			tempVar3.setSuccess(false);
			tempVar3.setMessage("不可修改已经审核同意的订单。");
			return tempVar3;
		}

		if (contract.getId() != null) {

			Criteria criteria = this.lotRepository.CreateCriteria(Lot.class);
			criteria.add(Restrictions.eq("ContractId", contract.getId()));
			List<Lot> lots = this.lotRepository.GetList(criteria);
			BigDecimal b = new BigDecimal(0);
			for (Lot lot : lots) {
				b = b.add(lot.getQuantity());
			}

			if (contract.getQuantity().compareTo(b) == -1) {
				ActionResult<Contract> tempVar4 = new ActionResult<Contract>();
				tempVar4.setSuccess(false);
				tempVar4.setMessage("批次数量之和不可以大于订单数量。");
				return tempVar4;
			}
		}

		// 给合同编号
		if (StringUtils.isBlank(contract.getSerialNo())) {
			SetContractNo(contract);
		} else {
			/**
			 * 检查重号
			 */
			Criteria checkCriteria = this.repository.CreateCriteria(Contract.class);
			checkCriteria.add(Restrictions.and(Restrictions.eq("SerialNo", contract.getSerialNo()),
					Restrictions.eq("SpotDirection", contract.getSpotDirection()),
					Restrictions.eq("LegalId", contract.getLegalId()),
					Restrictions.eq("CommodityId", contract.getCommodityId()), Restrictions.ne("Id", contract.getId())

			));
			List<Contract> duplicateSerialNo = this.repository.GetList(checkCriteria);
			if (duplicateSerialNo != null && duplicateSerialNo.size() > 0) {
				ActionResult<Contract> tempVar5 = new ActionResult<Contract>();
				tempVar5.setSuccess(false);
				tempVar5.setMessage(contractNo_Repeat_Msg);
				return tempVar5;
			}
		}

		contract.setIsInternal(true);
		if (contract.getIsIniatiated()) {
			contract.setStatus(Status.Agreed);
		}

		this.repository.SaveOrUpdate(contract);

		/**
		 * 如果合同的某些要素修改，且这些要素是Lot中冗余的，则必须修改Lot的这些冗余字段的值
		 */
		Criteria lots2Criteria = this.lotRepository.CreateCriteria(Lot.class);
		lots2Criteria.add(Restrictions.eq("ContractId", contract.getId()));
		List<Lot> lots2 = this.lotRepository.GetList(lots2Criteria);
		for (Lot lot : lots2) {
			/**
			 * 作为非常重要的冗余,不可删除
			 */
			lot.setLegalId(contract.getLegalId());
			lot.setCustomerId(contract.getCustomerId());
			lot.setCurrency(contract.getCurrency());
			lot.setHeadNo(contract.getHeadNo());
			lot.setSerialNo(contract.getSerialNo());
			lot.setPrefixNo(contract.getPrefix());
			lot.setStatus(contract.getStatus());
			lot.setSpotDirection(contract.getSpotDirection());
			lot.setCommodityId(contract.getCommodityId());
			lot.setProduct(contract.getProduct());
			this.lotRepository.SaveOrUpdate(lot);
		}

		ActionResult<Contract> tempVar6 = new ActionResult<Contract>();
		tempVar6.setSuccess(true);
		tempVar6.setData(contract);
		tempVar6.setMessage(MessageCtrm.SaveSuccess);
		return tempVar6;
	}

	/**
	 * 分页查询
	 */
	@Override
	public List<Contract> Contracts(Criteria criteria, int pageSize, int pageIndex, String sortBy, String orderBy,
			RefUtil total) {

		return this.repository.GetPage(criteria, pageSize, pageIndex, sortBy, orderBy, total).getData();
	}

	@Override
	public List<Lot> LotsByContractId(List<String> collect) {
		List<Lot> lots = new ArrayList<>();
		if (collect != null && collect.size() > 0) {
			Criteria criteria = this.lotRepository.CreateCriteria(Lot.class);
			criteria.add(Restrictions.in("ContractId", collect));
			lots = this.lotRepository.GetList(criteria);
		}

		// lots = commonService.SimplifyDataLotList(lots);
		return lots;
	}

	/**
	 * 给合同编号
	 * 
	 * @param contract
	 */
	private void SetContractNo(Contract contract) {
		if (contract.getId() != null)
			return;
		String yy = null;
		GlobalSet globalSet = commonService.DefaultGlobalSet();
		if (contract.getTradeDate() != null) {
			switch (globalSet.getPrefix4Contract()) {
			case "yyNo":
				yy = DateUtil.doFormatDate(contract.getTradeDate(), "yy");
				break;
			case "yyMMddNo":
				yy = DateUtil.doFormatDate(contract.getTradeDate(), "yyyyMMdd");
				break;
			}
		}
		Commodity commodity = this.commodityRepository.getOneById(contract.getCommodityId(), Commodity.class);
		Legal legal = this.legalRepository.getOneById(contract.getLegalId(), Legal.class);
		String prefix = String.format("%s%s%s%s", contract.getSpotDirection(), commodity.getCode(), legal.getCode(),
				yy);

		contract.setPrefix(prefix); // 赋值给对象，保存到数据库

		contract.setSerialNo(MaxContractSerialNo(contract.getTradeDate(), contract.getLegalId(),
				contract.getSpotDirection(), contract.getCommodityId()));

		contract.setSuffix(contract.getContractAmendId() != null ? "A" : "");

		// 合并成为完整的编号
		contract.setHeadNo(String.format("%s%s%s", contract.getPrefix(), contract.getSerialNo(), contract.getSuffix()));
	}

	private String MaxContractSerialNo(Date tradeDate, String legalId, String spotDirection, String commodityId) {
		String yearOrDay = "year";
		int len = 4;
		GlobalSet globalSet = commonService.DefaultGlobalSet();
		if (globalSet != null) {
			yearOrDay = StringUtils.isBlank(globalSet.getSerial4Contract()) ? "year"
					: globalSet.getSerial4Contract().toLowerCase();
			len = globalSet.getLen4ContractSerialNo() == 0 ? 4 : globalSet.getLen4ContractSerialNo();
		}

		if (globalSet == null || StringUtils.isBlank(globalSet.getSerial4Contract())
				|| (!globalSet.getSerial4Contract().toLowerCase().equals("year")
						&& !globalSet.getSerial4Contract().toLowerCase().equals("day"))) {
			return "0000";
		}

		Contract contract = null;

		/**
		 * 表示修正的单据，不参与编号计算
		 */
		if (yearOrDay.toLowerCase().equals("year")) {

			DetachedCriteria criteria = DetachedCriteria.forClass(Contract.class);
			criteria.addOrder(Order.desc("SerialNo"));

			criteria.add(Restrictions.eq("SpotDirection", spotDirection));
			criteria.add(Restrictions.eq("LegalId", legalId));
			criteria.add(Restrictions.eq("CommodityId", commodityId));
			criteria.add(Restrictions.isNull("ContractAmendId"));

			contract = this.repository.GetQueryable(Contract.class).where(criteria).firstOrDefault();

		} else if (yearOrDay.toLowerCase().equals("day")) {

			// 加一天
			Calendar c = Calendar.getInstance();
			c.setTime(tradeDate);
			c.add(Calendar.DATE, 1);

			DetachedCriteria criteria = DetachedCriteria.forClass(Contract.class);
			criteria.addOrder(Order.desc("SerialNo"));
			criteria.add(Restrictions.eq("SpotDirection", spotDirection));
			criteria.add(Restrictions.eq("LegalId", legalId));
			criteria.add(Restrictions.eq("CommodityId", commodityId));
			criteria.add(Restrictions.ge("TradeDate", tradeDate));
			criteria.add(Restrictions.le("TradeDate", c.getTime()));
			criteria.add(Restrictions.isNull("ContractAmendId"));

			List<Contract> contracts = this.repository.GetQueryable(Contract.class).where(criteria).toList();

			if (contracts != null && contracts.size() > 0) {
				contract = contracts.get(0);
			}

		}

		if (contract == null) {
			return StringUtil.padLeft("1", globalSet.getLen4InvoiceSerialNo(), '0');
		}
		String maxSerialNo = contract.getSerialNo();
		if (StringUtils.isNotBlank(maxSerialNo)) {
			StringBuilder sb = new StringBuilder();
			for (char ch : maxSerialNo.toCharArray()) {
				int numericValue = Character.getNumericValue(ch);
				if (numericValue >= 0 && numericValue <= 9) {
					sb.append(ch);
				}
			}
			if (StringUtils.isNotBlank(sb.toString())) {
				maxSerialNo = sb.toString();
			}
		}

		return StringUtil.padLeft(String.valueOf((Integer.parseInt(maxSerialNo) + 1)), len, '0');
	}

	@Override
	public ActionResult<String> txSaveContractAndRivalOrder(Contract contract) {
		// 保存销售订单

		ActionResult<Contract> saleResult = SaveHeadOfContractRegular(com.smm.ctrm.util.BeanUtils.copy(contract));
		if (!saleResult.isSuccess()) {
			throw new RuntimeException(saleResult.getMessage());
		}
		Contract saleContract = saleResult.getData();
		// 保存销售订单的批次
		List<Lot> lotList = contract.getLots();
		if (lotList != null) {
			for (Lot lot : lotList) {
				// 更新批次所带的货物信息的状态比如isin isout 等
				lot.setContractId(saleContract.getId());
				lot.setIsDelivered(true);
				lot.setQuantityDelivered(lot.getQuantity());
				lot.setContract(saleContract);
				ActionResult<Lot> saleLotResult = lotService.SaveLotOfContractRegular_New(lot);
				if (!saleLotResult.isSuccess()) {
					// 这里应该回滚，并抛出错误
					throw new RuntimeException("保存销售订单批次时失败");
				}
				saleLotResult.getData().setIsDelivered(true);
				lotRepository.SaveOrUpdate(saleLotResult.getData());
				if (saleContract.getLots() == null)
					saleContract.setLots(new ArrayList<>());
				saleContract.getLots().add(saleLotResult.getData());

				List<Storage> curSaleStorages = lot.getStorages();
				lot.setStorages(new ArrayList<>());
				ActionResult<List<Storage>> resultSaleStorages = storageService
						.GoodsDeliver(saleLotResult.getData().getId(), curSaleStorages);
				if (!resultSaleStorages.isSuccess()) {
					return new ActionResult<>(false, "保存采购订单批次时失败：销售订单发货数据创建失败");
				}
				saleContract.getLots().get(0).setStorages(resultSaleStorages.getData());
			}
		}
		// 保存采购订单
		// 1、将传入订单的相关属性变为采购并保存采购订单
		Contract buyContract = com.smm.ctrm.util.BeanUtils.copy(contract);
		Legal legalOne = legalRepository.GetQueryable(Legal.class).where(
				DetachedCriteria.forClass(Legal.class).add(Restrictions.eq("CustomerId", saleContract.getCustomerId())))
				.firstOrDefault();
		if (legalOne != null) {
			buyContract.setLegalId(legalOne.getId()); // 销售订单的客户所在的Legal
		}

		Legal legalTwo = legalRepository.GetQueryable(Legal.class)
				.where(DetachedCriteria.forClass(Legal.class).add(Restrictions.eq("Id", saleContract.getLegalId())))
				.firstOrDefault();
		if (legalTwo != null) {
			buyContract.setCustomerId(legalTwo.getCustomerId()); // 销售订单抬头所关联的CustomerId
		}

		CustomerTitle customerTitle = ctrepository.GetQueryable(CustomerTitle.class).where(DetachedCriteria
				.forClass(CustomerTitle.class).add(Restrictions.eq("CustomerId", buyContract.getCustomerId())))
				.firstOrDefault();
		if (customerTitle != null) {
			buyContract.setCustomerTitleId(customerTitle.getId()); // 销售订单抬头关联的CustomerId的抬头
		}

		buyContract.setId(null);
		Legal legal = legalRepository.getOneById(buyContract.getLegalId(), Legal.class);
		Commodity commodity = commodityRepository.getOneById(buyContract.getCommodityId(), Commodity.class);
		String prefix = DateUtil.doFormatDate(new Date(), "yy");
		buyContract.setPrefix("B" + commodity.getCode() + legal.getCode() + prefix);
		buyContract.setSerialNo(saleContract.getSerialNo());
		buyContract.setHeadNo(buyContract.getPrefix() + buyContract.getSerialNo());
		buyContract.setSpotDirection("B");
		buyContract.setRivalOrderID(saleContract.getId());
		buyContract.setVersion(buyContract.getVersion() + 1);

		int b = 0;
		ActionResult<Contract> buyResult = new ActionResult<Contract>();
		buyResult.setSuccess(false);
		while (b < 1000 && !buyResult.isSuccess()) {
			buyResult = SaveHeadOfContractRegular(buyContract);
			if (!buyResult.isSuccess()) {
				if (buyResult.getMessage().equals(contractNo_Repeat_Msg)) {
					b++;
					String serialNo = String.valueOf(Integer.parseInt(buyContract.getSerialNo()) + b);
					int len = serialNo.length();
					for (int i = 0; i < 4 - len; i++) {
						serialNo = "0" + serialNo;
					}
					buyContract.setHeadNo(buyContract.getPrefix() + buyContract.getSerialNo());
				} else {
					throw new RuntimeException("保存采购订单时失败" + buyResult.getMessage());
				}
			}
		}
		if (!buyResult.isSuccess()) {
			return new ActionResult<>(false, "保存采购订单时失败" + buyResult.getMessage());
		}

		buyContract = buyResult.getData();

		// 2、保存采购订单的批次
		Lot buyLot = saleContract.getLots().get(0);
		buyLot.setId(null);
		buyLot.setContractId(buyContract.getId());
		buyLot.setContract(buyContract);
		buyLot.setIsDelivered(true);
		ActionResult<Lot> buyLotResult = lotService.SaveLotOfContractRegular_New(buyLot);
		if (!buyLotResult.isSuccess()) {
			throw new RuntimeException("保存采购订单批次时失败");
		}
		buyLot = buyLotResult.getData();
		buyLot.setIsDelivered(true);
		lotRepository.SaveOrUpdate(buyLot);
		// 1、新增明细
		List<Storage> sourceStorages = saleContract.getLots().get(0).getStorages();
		List<Storage> newStorages = new ArrayList<>();
		for (Storage item : sourceStorages) {
			// 新增明细
			Storage newStorage = com.smm.ctrm.util.BeanUtils.copy(item);
			newStorage.setCounterpartyId3(item.getId());
			newStorage.setId(null);
			newStorage.setIsInvoiced(false);
			newStorage.setRefId(null);
			newStorage.setRefName(null);
			storageService.Save(newStorage);
			newStorages.add(newStorage);

			// 更新来源
			item.setCounterpartyId2(newStorage.getId());
			storageService.Save(item);
		}
		// 收货（更新新增明细的LotId）

		ActionResult<List<Storage>> resultBuyStorages = storageService.GoodsReceipt(buyLot.getId(), newStorages);
		if (!resultBuyStorages.isSuccess()) {
			return new ActionResult<>(false, "保存采购订单批次时失败：采购订单收货数据创建失败");
		}
		// 更新销售订单的对手订单Id
		saleContract.setRivalOrderID(buyContract.getId());
		saleContract.setVersion(saleContract.getVersion() + 1);
		ActionResult<Contract> updateRivalContractResult = SaveHeadOfContractRegular(saleContract);
		if (!updateRivalContractResult.isSuccess()) {
			throw new RuntimeException("更新采购订单的对手订单ID失败");
		}
		return new ActionResult<>(true, "创建订单及对手订单成功");
	}

	/**
	 * 单批次合同导入
	 */
	@Override
	public ActionResult<String> importContract(ImportContractParams icp) {

		/**
		 * 判断文档编号是否重复
		 */
		if (StringUtils.isNoneBlank(icp.getDocumentNo())) {
			DetachedCriteria dc = DetachedCriteria.forClass(Contract.class);
			dc.add(Restrictions.eq("DocumentNo", icp.getDocumentNo()));
			dc.setProjection(Projections.rowCount());
			long checkNoCount = (Long) this.contractRepository.getHibernateTemplate().findByCriteria(dc).get(0);
			if (checkNoCount > 0) {
				return new ActionResult<>(false, "文档号重复");
			}
		}
		String msg = checkField(icp);

		if (msg != null) {
			return new ActionResult<>(false, msg);
		}
		/**
		 * 构建合同
		 */

		List<CustomerTitle> ct = this.customerTitleRepository.GetQueryable(CustomerTitle.class).where(
				DetachedCriteria.forClass(CustomerTitle.class).add(Restrictions.eq("CustomerId", icp.getCustomerId())))
				.toCacheList();

		Contract contract = new Contract();
		Lot lot = new Lot();

		// 点价部分
		if (icp.getMajorType().equals(MajorType.Fix)) {
			lot.setMajor(icp.getMajor());
			lot.setIsPriced(true);
			lot.setQuantityPriced(icp.getQuantity());
		} else {
			lot.setMajorMarketId(icp.getMajorMarketId());
			lot.setMajorBasis(icp.getMajorBasis() != null ? icp.getMajorBasis() : null);
			lot.setMajorStartDate(icp.getMajorStartDate());
			lot.setMajorEndDate(icp.getMajorEndDate());
			lot.setIsEtaPricing(icp.getIsEtaPricing() != null ? icp.getIsEtaPricing() : false);
			lot.setEtaDuaration(icp.getEtaDuaration() != null ? icp.getEtaDuaration() : null);
		}
		// 升贴水部分
		if (icp.getPremiumType().equals(PremiumType.Fix)) {
			lot.setPremium(icp.getPremium());
		} else {
			lot.setPremiumBasis(icp.getPremiumBasis());
			lot.setPremiumMarketId(icp.getPremiumMarketId());
			lot.setPremiumStartDate(icp.getPremiumStartDate());
			lot.setPremiumEndDate(icp.getPremiumEndDate());
		}

		// 默认审核通过
		contract.setDocumentNo(icp.getDocumentNo());
		contract.setIsApproved(true);
		contract.setStatus(Status.Agreed);
		contract.setQuantity(icp.getQuantity());
		contract.setCreatedId(LoginHelper.GetLoginInfo().UserId);
		contract.setCustomerId(icp.getCustomerId());
		contract.setLegalId(icp.getLegalId());
		contract.setSpotDirection(icp.getSpotDirection());
		contract.setProduct(icp.getProduct());
		contract.setCommodityId(icp.getCommodityId());
		contract.setComments(icp.getComments());
		contract.setQuantity(icp.getQuantity());
		contract.setQuantityOfLots(icp.getQuantity());
		contract.setIsProvisional(false);
		contract.setPricer("us");
		contract.setCustomerTitleId(ct.size() > 0 ? ct.get(0).getId() : "");
		contract.setCustomerTitleName(ct.size() > 0 ? ct.get(0).getName() : "");
		contract.setBrandIds(icp.getBrandIds());
		contract.setBrandNames(icp.getBrandNames());
		

		// 默认参数
		contract.setSpotType("内贸");
		contract.setCurrency("CNY");
		contract.setTradeDate(icp.getTradeDate() != null ? icp.getTradeDate() : new Date());
		contract.setTraderId(LoginHelper.GetLoginInfo().UserId);
		// 合同编号,系统生成
		String serialNo = MaxContractSerialNo(contract.getTradeDate(), icp.getLegalId(), icp.getSpotDirection(),
				icp.getCommodityId());
		contract.setSerialNo(serialNo);
		contract.setPrefix(icp.getContractPrefix());
		contract.setHeadNo(icp.getContractPrefix() + serialNo);
		contract.setHedgeRatio(new BigDecimal(80));

		String cId = this.contractRepository.SaveOrUpdateRetrunId(contract);

		DetachedCriteria dc = DetachedCriteria.forClass(Brand.class);
		dc.add(Restrictions.in("Id", icp.getBrandIds()));
		List<Brand> brands = this.brandRepository.GetQueryable(Brand.class).where(dc).toCacheList();

		/**
		 * 构建批次
		 */
		// 批次号,系统生成
		lot.setFullNo(icp.getContractPrefix() + serialNo + "/10");
		lot.setHeadNo(icp.getContractPrefix() + serialNo);
		lot.setPrefixNo(icp.getContractPrefix());
		lot.setSerialNo(serialNo);
		lot.setLotNo(10);
		lot.setContractId(cId);
		lot.setQuantity(icp.getQuantity());
		lot.setDocumentNo(icp.getDocumentNo());
		lot.setBrandIds(icp.getBrandIds());
		lot.setBrandNames(icp.getBrandNames());
		lot.setIsOriginalLot(false);
		lot.setIsSplitLot(false);
		lot.setIsFunded(false);
		lot.setIsDelivered(false);
		lot.setIsInvoiced(false);
		lot.setIsHedged(false);;
		lot.setQuantityLess(icp.getQuantity().multiply(new BigDecimal(0.98).setScale(4, BigDecimal.ROUND_HALF_UP)));
		lot.setQuantityMore(icp.getQuantity().multiply(new BigDecimal(1.02).setScale(4, BigDecimal.ROUND_HALF_UP)));
		lot.setQuantityDelivered(BigDecimal.ZERO);
		lot.setPremiumType(icp.getPremiumType());
		lot.setProduct(icp.getProduct());
		lot.setTraderName("现货");
		lot.setBrands(brands);
		lot.setSpecId(icp.getSpecId() != null ? icp.getSpecId() : null);
		lot.setMajorType(icp.getMajorType());
		lot.setMajor(icp.getMajor());
		lot.setMoreOrLess(new BigDecimal(2));
		lot.setMoreOrLessBasis("OnPercentage");
		lot.setCommodityId(icp.getCommodityId());
		lot.setStatus(Status.Agreed);
		lot.setSpotDirection(icp.getSpotDirection());
		lot.setCustomerId(icp.getCustomerId());
		lot.setLegalId(icp.getLegalId());
		lot.setQuantityOriginal(icp.getQuantity());

		// String lotId=this.lotRepository.SaveOrUpdateRetrunId(lot);

		// Lot newLot=this.lotRepository.getOneById(lotId, Lot.class);
		Contract con = this.contractRepository.getOneById(cId, Contract.class);
		lot.setContract(con);
		lot.setIsInvoiced(false);
		ActionResult<Lot> returnLot = lotService.SaveLotOfContractRegular_New(lot);
		if (returnLot.isSuccess()) {
			lotService.GenerateFees_New(returnLot.getData());
			// 执行更新价格与余额
			returnLot.setData(commonService.UpdateLotPriceByLotId_New(returnLot.getData().getId()));
		}
		return new ActionResult<>(true, "导入成功.");
	}

	/**
	 * 单批次合同检查必须项
	 */
	@SuppressWarnings("unused")
	private String checkField(ImportContractParams contract) {

		if (StringUtils.isBlank(contract.getCustomerId())) {
			return "CustomerId为空";
		}
		if (StringUtils.isBlank(contract.getLegalId())) {
			return "LegalId为空";
		}
		if (StringUtils.isBlank(contract.getSpotDirection())) {
			return "SpotDirection为空";
		}
		if (StringUtils.isBlank(contract.getCommodityId())) {
			return "CommodityId为空";
		}
		if (contract.getQuantity() == null) {
			return "Quantity为空";
		}
		if (StringUtils.isBlank(contract.getBrandIds())) {
			return "BrandIds为空";
		}
		/*
		 * if(StringUtils.isBlank(contract.getSpecId())){ return "SpecId为空"; }
		 */
		// 如果是固定价
		if (contract.getMajorType().equals(MajorType.Fix)) {
			if (contract.getMajor() == null) {
				return "Major为空";
			}
		} else {
			// 点价市场
			if (StringUtils.isBlank(contract.getMajorMarketId())) {
				return "MajorMarketId为空";
			}

			if (!contract.getMajorMarketName().equals("有色网") && !contract.getMajorMarketName().equals("长江金属")
					&& StringUtils.isBlank(contract.getMajorBasis())) {
				return "MajorBasis为空";
			}
			if (contract.getMajorStartDate() == null) {
				return "MajorStartDate为空";
			}
			if (contract.getMajorEndDate() == null) {
				return "MajorEndDate为空";
			}
		}

		if (contract.getPremiumType().equals(PremiumType.Fix)) {
			if (contract.getPremium() == null) {
				return "Premium为空";
			}
		} else {

			if (StringUtils.isBlank(contract.getPremiumBasis())) {
				return "PremiumBasis为空";
			}
			if (StringUtils.isBlank(contract.getPremiumMarketId())) {
				return "PremiumMarketId为空";
			}
			if (contract.getPremiumStartDate() == null) {
				return "PremiumStartDate为空";
			}
			if (contract.getPremiumEndDate() == null) {
				return "PremiumEndDate为空";
			}
		}

		return null;
	}

	/**
	 * 检查合同订单是否发生后续业务
	 * 
	 */
	public boolean verify(Contract contract) {
		boolean flag = false;
		// 检查订单所有批次是否已经发生业务
		List<Lot> lots = this.lotRepository.GetQueryable(Lot.class)
				.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("ContractId", contract.getId())))
				.toList();
		if (lots != null && lots.size() > 0) {

			List<String> ids = new ArrayList<>();
			for (Lot l : lots) {
				ids.add(l.getId());
			}

			// 点价记录PricingRecord
			DetachedCriteria dc1 = DetachedCriteria.forClass(PricingRecord.class);
			dc1.add(Restrictions.in("LotId", ids));
			dc1.setProjection(Projections.rowCount());
			long prCount = (Long) this.pricingRecordRepo.getHibernateTemplate().findByCriteria(dc1).get(0);

			// 发票记录Invoice
			DetachedCriteria dc2 = DetachedCriteria.forClass(Invoice.class);
			dc2.add(Restrictions.in("LotId", ids));
			dc2.setProjection(Projections.rowCount());
			long iCount = (Long) this.invoiceRepo.getHibernateTemplate().findByCriteria(dc2).get(0);

			// 保值记录Position
			DetachedCriteria dc3 = DetachedCriteria.forClass(Position.class);
			dc3.add(Restrictions.in("LotId", ids));
			dc3.setProjection(Projections.rowCount());
			long pCount = (Long) this.invoiceRepo.getHibernateTemplate().findByCriteria(dc3).get(0);

			// 收/发货记录ReceiptShip
			DetachedCriteria dc4 = DetachedCriteria.forClass(ReceiptShip.class);
			dc4.add(Restrictions.in("LotId", ids));
			dc4.setProjection(Projections.rowCount());
			long rCount = (Long) this.receiptShipRepo.getHibernateTemplate().findByCriteria(dc4).get(0);

			// 付款记录Fund
			DetachedCriteria dc5 = DetachedCriteria.forClass(Fund.class);
			dc5.add(Restrictions.in("LotId", ids));
			dc5.setProjection(Projections.rowCount());
			long fCount = (Long) this.fundRepository.getHibernateTemplate().findByCriteria(dc5).get(0);

			if (prCount > 0 || iCount > 0 || pCount > 0 || rCount > 0 || fCount > 0) {
				flag = true;
			}
		}
		return flag;
	}

	@Override
	public ActionResult<Boolean> verify(String contractId) {
		Contract contract = this.contractRepository.getOneById(contractId, Contract.class);
		boolean flag = verify(contract);
		if (flag) {
			return new ActionResult<>(true, "已经发生后续业务.", true, "");
		} else {
			return new ActionResult<>(true, "还没有发生后续业务", false, "");
		}
	}

	/**
	 * 快捷订单
	 * @throws Exception 
	 */
	@Transactional(readOnly = false)
	public ActionResult<CpContract> fastContract(CpContract contract) throws Exception {

		Contract curContract = contract.getCurContract();

		Lot curLot = contract.getCurLot();

		Storage curStorage = contract.getCurStorage();

		if(curLot.getPrice().compareTo(BigDecimal.ZERO)<=0){
			return new ActionResult<>(false, "价格不能为少于等于零",contract);
		}
		List<Storage> storags=new ArrayList<>();
		if(curContract.getSpotDirection().equals(SpotType.Sell)){
			DetachedCriteria dc=DetachedCriteria.forClass(Storage.class);
			dc.add(Restrictions.eq("WarehouseId", curStorage.getWarehouseId()));
			dc.add(Restrictions.eq("CommodityId", curStorage.getCommodityId()));
			dc.add(Restrictions.eq("BrandId", curStorage.getBrandId()));
			dc.add(Restrictions.eq("SpecId", curStorage.getSpecId()));
			dc.add(Restrictions.eqOrIsNull("GradeSetId", curStorage.getGradeSetId()));
			dc.add(Restrictions.eq("MT", "T"));
			dc.add(Restrictions.eq("IsOut", false));
			dc.add(Restrictions.eq("IsIn", true));
			dc.addOrder(Order.desc("CreatedAt"));
			storags=this.storageRepository.GetQueryable(Storage.class).where(dc).toList();
			if(storags==null||storags.size()==0){
				return new ActionResult<>(false, "没有库存不能做销售订单。",contract);
			}
		}
		Warehouse wh=this.warehouseRepository.getOneById(curStorage.getWarehouseId(), Warehouse.class);
		/**
		 * 保存合同
		 */
		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		curContract.setCreatedId(loginInfo.getUserId());
		curContract.setIsApproved(true);
		curContract.setStatus(1);
		curContract.setRuleWareHouseIds(curStorage.getWarehouseId());
		curContract.setRuleWareHouseNames(wh.getName());
		String contractId=this.contractRepository.SaveOrUpdateRetrunId(curContract);
		curContract.setId(contractId);
		curLot.setContractId(contractId);
		curLot.setCreatedId(loginInfo.getUserId());

		curLot.setQuantityDelivered(curStorage.getQuantity());
		curLot.setQuantityInvoiced(curStorage.getQuantity());
		curLot.setQuantityPriced(curLot.getQuantity());
		// curLot.setMajor(curLot.getPrice());
		curLot.setAmountFunded(curStorage.getQuantity().multiply(curLot.getPrice()));
		curLot.setAmountFundedDraft(curStorage.getQuantity().multiply(curLot.getPrice()));
		curLot.setQuantityFunded(curStorage.getQuantity());
		curLot.setIsInvoiced(curLot.getQuantity().compareTo(curStorage.getQuantity()) > 0 ? false : true);
		curLot.setIsPriced(true);
		if(curContract.getHedgeRatio() != null && curContract.getHedgeRatio().compareTo(BigDecimal.ZERO) == 0 )
			curLot.setIsHedged(true);
		
		if(curStorage.getQuantity().compareTo(curLot.getQuantityMore())<=0&&curStorage.getQuantity().compareTo(curLot.getQuantityLess())>=0){
			curLot.setIsDelivered(true);
			curLot.setIsFunded(true);
		} else {
			curLot.setIsDelivered(false);
			curLot.setIsFunded(false);
		}
		curLot.setCurrency("CNY");
		curLot.setIsInvoiced(true);
		String lotId = this.lotRepository.SaveOrUpdateRetrunId(curLot);
		curLot.setId(lotId);
		Invoice invoice = new Invoice();
		List<Storage> subStorage = new ArrayList<>();
		if (curContract.getSpotDirection().equals(SpotType.Sell)) {
			/**
			 * 保存发货
			 */
			BigDecimal total=BigDecimal.ZERO;
			for (int i=0;i<storags.size();i++) {
				total=total.add(storags.get(i).getQuantity());
				if(total.compareTo(curStorage.getQuantity())==0){
					int j=i;
					subStorage=storags.subList(0, j+1);
					saveSellStorage(subStorage,curContract,curLot,curStorage);
					break;
				} else if (total.compareTo(curStorage.getQuantity()) > 0) {

					int j = i;
					subStorage = storags.subList(0, j + 1);
					Storage maxStorage = storags.subList(0, j + 1).stream()
							.max(Comparator.comparing(s -> s.getQuantity())).get();
					// 需要拆分数量
					Storage copyStorage = new Storage();

					copyStorage = com.smm.ctrm.util.BeanUtils.copy(maxStorage);
					BigDecimal subQuantity = total.subtract(curStorage.getQuantity());
					copyStorage.setId(null);
					copyStorage.setQuantity(maxStorage.getQuantity().subtract(subQuantity));
					copyStorage.setIsBorrow(false);
					// 项目名称
					copyStorage.setProjectName(createProjectName(maxStorage.getProjectName()));
					copyStorage.setIsSplitted(true);
					copyStorage.setGross(maxStorage.getQuantity());
					copyStorage.setGrossAtFactory(maxStorage.getQuantity());
					copyStorage.setQuantityAtWarehouse(maxStorage.getQuantity());
					copyStorage.setSourceId(maxStorage.getId());
					copyStorage.setAmount(maxStorage.getQuantity().multiply(curLot.getPrice()));
					copyStorage.setMajor(curLot.getMajor());
					copyStorage.setPremium(curLot.getPremium());

					String nsId = this.storageRepository.SaveOrUpdateRetrunId(copyStorage);
					copyStorage.setId(nsId);

					maxStorage.setQuantity(subQuantity);
					maxStorage.setGross(subQuantity);
					maxStorage.setGrossAtFactory(subQuantity);
					maxStorage.setQuantityAtWarehouse(subQuantity);

					this.storageRepository.SaveOrUpdateRetrunId(maxStorage);

					subStorage.remove(maxStorage);
					subStorage.add(copyStorage);
					saveSellStorage(subStorage, curContract, curLot, curStorage);
					break;
				} else {
					if (i == storags.size() - 1) {
						throw new Exception("库存不足。");
						//return new ActionResult<>(false, "库存不足。");
					}
				}
			}
			invoice.setStorages(subStorage);
		} else {
			/**
			 * 保存收货
			 */
			ReceiptShip rs = new ReceiptShip();
			rs.setCommodityId(curLot.getCommodityId());
			rs.setContractId(curContract.getId());
			rs.setCustomerId(curLot.getCustomerId());
			rs.setCustomerName(curLot.getCustomerName());
			rs.setFlag(curContract.getSpotDirection().equals(SpotType.Sell) ? "S" : "R");
			rs.setIsApproved(true);
			rs.setLotId(curLot.getId());
			rs.setReceiptShipDate(new Date());
			String no = this.receiptService.getNoRepeatReceiptNo(rs.getFlag());
			rs.setReceiptShipNo(no);
			rs.setSpecId(curLot.getSpecId());
			rs.setWeight(curLot.getQuantity());
			rs.setWhId(curContract.getRuleWareHouseIds());
			rs.setWhName(curContract.getRuleWareHouseNames());
			rs.setWhOutEntryDate(new Date());
			rs.setCreatedId(loginInfo.getUserId());
			rs.setCustomerId(curStorage.getCustomerId());
			rs.setWhId(curStorage.getWarehouseId());
			rs.setWhName(wh.getName());
			
			rs.setCustomerName(curStorage.getCustomerName());
			
			String rsId=this.receiptShipRepo.SaveOrUpdateRetrunId(rs);
			curStorage.setRefId(rsId);
			curStorage.setRefName(ReceiptShip.class.getSimpleName());
			curStorage.setCreatedId(loginInfo.getUserId());
			curStorage.setLotId(lotId);
			curStorage.setIsIn(true);
			curStorage.setTradeDate(new Date());
			curStorage.setMT("T");
			curStorage.setTransitStatus("NA");
			curStorage.setIsInvoiced(true);
			curStorage.setContractId(contractId);
			curStorage.setQuantityInvoiced(curLot.getQuantity());
			curStorage.setAmount(curStorage.getQuantity().multiply(curLot.getPrice()));
			curStorage.setIsBorrow(false);
			curStorage.setCreatedId(loginInfo.getUserId());
			this.storageRepository.SaveOrUpdateRetrunId(curStorage);
			subStorage.clear();
			subStorage.add(curStorage);
			invoice.setStorages(subStorage);
		}

		/**
		 * 完成收付款
		 */
		Fund fund = new Fund();
		fund.setDC(curContract.getSpotDirection().equals(SpotType.Sell) ? Fund.RECEIPT : Fund.PAYMENT);
		fund.setIsIniatiated(true);
		fund.setExecuteDate(new Date());
		fund.setLastExecuteDate(new Date());
		fund.setApproveDate(new Date());
		fund.setAskDate(new Date());
		fund.setInitDate(new Date());
		fund.setQuantity(curStorage.getQuantity());
		fund.setPrice(curLot.getPrice());
		fund.setAmount(curLot.getPrice().multiply(curStorage.getQuantity()));
		fund.setCurrency("CNY");
		fund.setIsExecuted(true);
		fund.setFundType(1);
		fund.setLotId(lotId);
		fund.setLegalId(curLot.getLegalId());
		fund.setCustomerTitleId(curContract.getCustomerTitleId());
		fund.setCustomerId(curContract.getCustomerId());
		fund.setCommodityId(curLot.getCommodityId());
		fund.setCreatedId(loginInfo.getUserId());
		fund.setStatus(1);
		fund.setTradeDate(new Date());

		/**
		 * 获取发票号
		 */
		String preix = curContract.getSpotDirection().equals(SpotType.Sell)
				? "SI" + curLot.getLegalCode() + DateUtil.doFormatDate(new Date(), "yy")
				: "PI" + curLot.getLegalCode() + DateUtil.doFormatDate(new Date(), "yy");
		Integer serilno = commonService.GetSequenceIndex(preix, true);

		/**
		 * 保存发票
		 */

		invoice.setIsIniatiated(true);
		invoice.setIsExecuted(true);
		invoice.setUInvoiceCode("类别一");
		invoice.setFeeCode("9");
		invoice.setCurrency("CNY");
		invoice.setPFA("F");
		String leftSerilno = StringUtil.padLeft(serilno + "", 4, '0');
		invoice.setDocumentNo(preix + leftSerilno + "F");
		invoice.setInvoiceNo(preix + leftSerilno + "F");
		invoice.setSerialNo(leftSerilno + "");
		invoice.setPrefix(preix);
		invoice.setSuffix("F");
		invoice.setTradeDate(new Date());
		invoice.setMT(curContract.getSpotDirection().equals(SpotType.Sell) ? "M" : "T");
		invoice.setQuantity(curStorage.getQuantity());
		invoice.setPrice(curLot.getPrice());
		invoice.setMajor(curLot.getMajor());
		invoice.setIsWareHouseQuantity(false);
		invoice.setIsUserChangedQuantity(false);
		invoice.setPremium(curLot.getPremium());
		invoice.setAmountNotional(curLot.getPrice().multiply(curStorage.getQuantity()));
		invoice.setAmount(curLot.getPrice().multiply(curStorage.getQuantity()));
		invoice.setDueRate(new BigDecimal(100));
		invoice.setAmountDrafted(curLot.getPrice().multiply(curStorage.getQuantity()));
		invoice.setDueDate(new Date());
		invoice.setInvoiceType(0);
		invoice.setDueAmount(curLot.getPrice().multiply(curStorage.getQuantity()));
		invoice.setLegalId(curLot.getLegalId());
		invoice.setContractId(contractId);
		invoice.setLotId(lotId);
		invoice.setCommodityId(curLot.getCommodityId());
		invoice.setCustomerId(curContract.getCustomerId());
		invoice.setCustomerTitleId(curContract.getCustomerTitleId());
		invoice.setStatus(1);
		invoice.setIsApproved(true);
		invoice.setCreatedId(loginInfo.getUserId());
		invoice.setTaxRate(new BigDecimal(17));
		invoice.setTax(invoice.getAmount().divide(new BigDecimal(1.17), 4, BigDecimal.ROUND_HALF_UP)
				.multiply(new BigDecimal(0.17)).setScale(2, BigDecimal.ROUND_HALF_UP));
		invoice.setExcludingTaxAmount(invoice.getAmount().subtract(invoice.getTax()));
		this.fundService.SaveReceive(fund);
		this.invoiceService.Save(invoice);
		return new ActionResult<>(true, "保存成功.", contract);
	}

	/**
	 * 保存发货记录
	 */
	private void saveSellStorage(List<Storage> subStorage, Contract contract, Lot lot, Storage curStorage) {
		LoginInfoToken loginInfo = LoginHelper.GetLoginInfo();
		Warehouse wh=this.warehouseRepository.getOneById(curStorage.getWarehouseId(), Warehouse.class);
		for (Storage storage : subStorage) {
			ReceiptShip rs = new ReceiptShip();

			rs.setCommodityId(lot.getCommodityId());
			rs.setContractId(contract.getId());
			rs.setCustomerId(lot.getCustomerId());
			rs.setCustomerName(lot.getCustomerName());
			rs.setFlag(contract.getSpotDirection().equals(SpotType.Sell) ? "S" : "R");
			String no = this.receiptService.getNoRepeatReceiptNo(rs.getFlag());
			rs.setReceiptShipNo(no);
			rs.setIsApproved(true);
			rs.setLotId(lot.getId());
			rs.setReceiptShipDate(new Date());
			rs.setSpecId(lot.getSpecId());
			rs.setWeight(lot.getQuantity());
			rs.setWhId(contract.getRuleWareHouseIds());
			rs.setWhName(contract.getRuleWareHouseNames());
			rs.setWhOutEntryDate(new Date());
			rs.setCreatedId(loginInfo.getUserId());
			rs.setCustomerId(curStorage.getCustomerId());
			rs.setWhId(curStorage.getWarehouseId());
			rs.setWhName(wh.getName());
			rs.setCustomerName(curStorage.getCustomerName());
			String rsId=this.receiptShipRepo.SaveOrUpdateRetrunId(rs);
			
			
			Storage copyStorage = new Storage();

			copyStorage = com.smm.ctrm.util.BeanUtils.copy(storage);
			copyStorage.setRefId(rsId);
			curStorage.setRefName(ReceiptShip.class.getSimpleName());
			copyStorage.setLotId(lot.getId());
			copyStorage.setMT("M");
			copyStorage.setId(null);
			copyStorage.setTransitStatus("NA");
			copyStorage.setIsInvoiced(true);
			copyStorage.setCounterpartyId3(storage.getId());
			copyStorage.setIsBorrow(false);
			copyStorage.setPrice(lot.getPrice());
			copyStorage.setMajor(lot.getMajor());
			copyStorage.setPremium(lot.getPremium());
			copyStorage.setCardNo(curStorage.getCardNo());
			copyStorage.setTruckNo(curStorage.getTruckNo());
			copyStorage.setTrailer(curStorage.getTrailer());
			String sId = this.storageRepository.SaveOrUpdateRetrunId(copyStorage);
			storage.setIsBorrow(false);
			storage.setCounterpartyId2(sId);
			storage.setIsOut(true);
			this.storageRepository.SaveOrUpdateRetrunId(storage);
		}
	}

	@SuppressWarnings("unused")
	private String createProjectName(String projectName) {
		Integer no = commonService.GetSequenceIndex("Split_Storage_" + projectName);
		if (no == null) {
			no = 1;
		}
		return "* " + projectName + "(" + (no) + ")";
	}

	@Override
	public ActionResult<String> getSanKey(ContractParams param) {
		String path = System.getProperty("smm.ctrm") + "/WEB-INF/classes/Images/" + "矩形树图1.txt";
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(path));
			StringBuilder sb = new StringBuilder();
			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			return new ActionResult<>(true, "", sb.toString(), "");
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return new ActionResult<>(true, "");
	}
}
