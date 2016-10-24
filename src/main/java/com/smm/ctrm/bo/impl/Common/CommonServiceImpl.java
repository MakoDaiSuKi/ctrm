package com.smm.ctrm.bo.impl.Common;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.beanutils.converters.SqlDateConverter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.smm.ctrm.bo.Common.CommonService;
import com.smm.ctrm.bo.Physical.ReceiptService;
import com.smm.ctrm.dao.HibernateRepository;
import com.smm.ctrm.domain.LoginInfo;
import com.smm.ctrm.domain.Mainmeta;
import com.smm.ctrm.domain.MainmetaResult;
import com.smm.ctrm.domain.QuantityMaL;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Broker;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.CustomerBalance;
import com.smm.ctrm.domain.Basis.CustomerTitle;
import com.smm.ctrm.domain.Basis.Dictionary;
import com.smm.ctrm.domain.Basis.EstPremiumSetup;
import com.smm.ctrm.domain.Basis.GlobalSet;
import com.smm.ctrm.domain.Basis.Instrument;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Market;
import com.smm.ctrm.domain.Basis.Menu;
import com.smm.ctrm.domain.Basis.RolePermission;
import com.smm.ctrm.domain.Basis.SysSequence;
import com.smm.ctrm.domain.Basis.SysSequenceResult;
import com.smm.ctrm.domain.Basis.User;
import com.smm.ctrm.domain.Basis.UserRole;
import com.smm.ctrm.domain.Maintain.DSME;
import com.smm.ctrm.domain.Maintain.LME;
import com.smm.ctrm.domain.Maintain.Reuter;
import com.smm.ctrm.domain.Maintain.SFE;
import com.smm.ctrm.domain.Physical.Attachment;
import com.smm.ctrm.domain.Physical.C2Storage;
import com.smm.ctrm.domain.Physical.CInvoice;
import com.smm.ctrm.domain.Physical.CStorage;
import com.smm.ctrm.domain.Physical.CarryPosition;
import com.smm.ctrm.domain.Physical.Contract;
import com.smm.ctrm.domain.Physical.DischargingPriceDiff;
import com.smm.ctrm.domain.Physical.Fee;
import com.smm.ctrm.domain.Physical.Fund;
import com.smm.ctrm.domain.Physical.Grade;
import com.smm.ctrm.domain.Physical.Invoice;
import com.smm.ctrm.domain.Physical.InvoiceGrade;
import com.smm.ctrm.domain.Physical.LC;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.MarginFlow;
import com.smm.ctrm.domain.Physical.Pending;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.domain.Physical.Position4Broker;
import com.smm.ctrm.domain.Physical.Pricing;
import com.smm.ctrm.domain.Physical.PricingRecord;
import com.smm.ctrm.domain.Physical.QPRecord;
import com.smm.ctrm.domain.Physical.ReceiptShip;
import com.smm.ctrm.domain.Physical.Square;
import com.smm.ctrm.domain.Physical.Square4Broker;
import com.smm.ctrm.domain.Physical.Storage;
import com.smm.ctrm.domain.Physical.SummaryFees;
import com.smm.ctrm.domain.Physical.Tip;
import com.smm.ctrm.domain.Physical.vAttachment;
import com.smm.ctrm.domain.Physical.vLot;
import com.smm.ctrm.domain.apiClient.WebApiClient;
import com.smm.ctrm.dto.res.ActionResult;
import com.smm.ctrm.dto.res.ActionStatus;
import com.smm.ctrm.hibernate.DataSource.CTRMOrg;
import com.smm.ctrm.util.DateUtil;
import com.smm.ctrm.util.DecimalUtil;
import com.smm.ctrm.util.HttpClientUtil;
import com.smm.ctrm.util.JSONUtil;
import com.smm.ctrm.util.MessageCtrm;
import com.smm.ctrm.util.ParameterType;
import com.smm.ctrm.util.PropertiesUtil;
import com.smm.ctrm.util.QrCodePrintUtil;
import com.smm.ctrm.util.RefUtil;
import com.smm.ctrm.util.Result.FeeCode;
import com.smm.ctrm.util.Result.InvoiceType;
import com.smm.ctrm.util.Result.LS;
import com.smm.ctrm.util.Result.MT4Invoice;
import com.smm.ctrm.util.Result.MajorType;
import com.smm.ctrm.util.Result.PriceTiming;
import com.smm.ctrm.util.Result.SpotType;

/**
 * Created by hao.zheng on 2016/4/25.
 *
 */
@Service
public class CommonServiceImpl implements CommonService {

	public static final String SyncUrl = "http://trade.api.hedgestudio.com:9730";

	public static final String SyncLoginApi = "api/Basis/Login/Login4Winform";

	private static final Logger logger = Logger.getLogger(CommonServiceImpl.class);

	private final static String CODE = "0";

	private final Calendar calendar = Calendar.getInstance();

	@Autowired
	private HibernateRepository<GlobalSet> globalSetRepo;

	@Autowired
	private HibernateRepository<Attachment> attachmentRepo;

	@Autowired
	private HibernateRepository<vAttachment> vattachmentRepo;

	@Autowired
	private HibernateRepository<Pending> pendingRepo;
	
	@Resource
	private HibernateRepository<ReceiptShip> receiptShipRepo;

	@Autowired
	private HibernateRepository<Lot> lotRepo;

	@Autowired
	private HibernateRepository<Storage> storageRepo;

	@Autowired
	private HibernateRepository<Position> positionRepo;

	@Autowired
	private HibernateRepository<Invoice> invoiceRepo;

	@Autowired
	private HibernateRepository<Position4Broker> position4BrokerRepo;

	@Autowired
	private HibernateRepository<Broker> brokerRepo;

	@Autowired
	private HibernateRepository<Commodity> commodityRepo;

	@Autowired
	private HibernateRepository<SFE> sfeRepo;

	@Autowired
	private HibernateRepository<Market> marketRepository;

	@Autowired
	private HibernateRepository<LME> lmeRepo;

	@Autowired
	private HibernateRepository<DSME> dsmeRepo;

	@Autowired
	private HibernateRepository<Square> squareRepo;

	@Autowired
	private HibernateRepository<Square4Broker> square4BrokerRepo;

	@Autowired
	private HibernateRepository<Brand> brandRepo;

	@Autowired
	private HibernateRepository<Fee> feeRepo;

	@Autowired
	private HibernateRepository<Pricing> pricingRepo;

	@Autowired
	private HibernateRepository<Grade> gradeRepo;

	@Autowired
	private HibernateRepository<DischargingPriceDiff> dischargingPriceDiffRepo;

	@Autowired
	private HibernateRepository<EstPremiumSetup> estPremiumSetupRepo;

	@Autowired
	private HibernateRepository<LC> lcRepo;

	@Autowired
	private HibernateRepository<Legal> legalRepo;

	@Autowired
	private HibernateRepository<Contract> contractRepo;

	@Autowired
	private HibernateRepository<QPRecord> qpRecordRepo;

	@Autowired
	private HibernateRepository<Reuter> reuterRepo;

	@Autowired
	private HibernateRepository<User> userRepo;

	@Autowired
	private HibernateRepository<UserRole> userRoleRepo;

	@Autowired
	private HibernateRepository<RolePermission> rolePermissionRepo;

	@Autowired
	private HibernateRepository<Customer> customerRepo;

	@Autowired
	private HibernateRepository<CustomerTitle> customerTitleRepo;

	@Autowired
	private HibernateRepository<vLot> vLotHibernateRepository;

	@Autowired
	private HibernateRepository<Dictionary> dicRepo;

	@Autowired
	private HibernateRepository<Fund> funRepo;
	
	@Autowired
	private HibernateRepository<Instrument> instrumentRepo;

	@Autowired
	private HibernateRepository<CustomerBalance> customerBalanceRepo;

	@Override
	public LoginInfo GetSyncLoginInfo() {

		LoginInfo syncLoginInfo = null;

		HashMap<String, String> param = new HashMap<>();

		param.put("OrgCode", "CRRC");
		param.put("Account", "CRRC");
		param.put("Password", "sg1234");

		WebApiClient webApiClient = new WebApiClient(CommonService.SyncUrl, syncLoginInfo.toString());

		String result = webApiClient.Post(SyncLoginApi, param);

		if (!StringUtils.isEmpty(result)) {

			ActionResult<LoginInfo> resultObj = JSON.parseObject(result, ActionResult.class);

			if (resultObj.isSuccess()) {

				syncLoginInfo = resultObj.getData();
			}
		}

		return syncLoginInfo;
	}

	@Override
	public GlobalSet DefaultGlobalSet() {
		return this.globalSetRepo.GetQueryable(GlobalSet.class).firstOrDefault();
	}

	@Override
	public String GetServerUploadPath() {

		GlobalSet globalSet = this.DefaultGlobalSet();

		if (globalSet != null)
			return globalSet.getServerUploadPath();

		return null;

	}

	@Override
	public String GetServerOutDocmentPath() {

		GlobalSet globalSet = this.DefaultGlobalSet();

		if (globalSet != null)
			return globalSet.getServerOutDocumentPath();

		return null;
	}

	@Override
	public String GetServerOutDocTemplateFilePath() {

		GlobalSet globalSet = this.DefaultGlobalSet();

		if (globalSet != null)
			return globalSet.getServerOutDocTemplateFilePath();

		return null;

	}

	@Override
	public ActionResult<String> SaveAttachment(Attachment attachment) {

		try {

			this.attachmentRepo.SaveOrUpdate(attachment);

			return new ActionResult<>(true, MessageCtrm.SaveSuccess);

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());

		}
	}

	@Override
	public ActionResult<List<Attachment>> GetAttachments(String attachType, Boolean isOutDoc, String id) {
		DetachedCriteria where = DetachedCriteria.forClass(Attachment.class)
				.add(Restrictions.eq("IsOutDocument", isOutDoc))
			.add(Restrictions.eqOrIsNull(attachType + "Id", id));
		List<Attachment> attachments = this.attachmentRepo.GetQueryable(Attachment.class).where(where).OrderBy(Order.desc("TradeDate")).toList();
		return new ActionResult<>(true, "", attachments);
	}

	@Override
	public ActionResult<Attachment> GetAttachmentById(String id) {

		return new ActionResult<>(true, null, this.attachmentRepo.getOneById(id, Attachment.class));
	}

	@Override
	public ActionResult<vAttachment> GetAttachmentById_New(String id) {

		return new ActionResult<>(true, null, this.vattachmentRepo.getOneById(id, vAttachment.class));

	}

	@Override
	public ActionResult<String> DeleteAttachmentById(String id) {
		String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
		// step 1: 删除删除磁盘上的文件
		Attachment attachment = this.attachmentRepo.getOneById(id, Attachment.class);
		// 获取到了attachment.file name and its path, and then delete it from
		// hard disk

		// step 2: 删除数据库中的相关记录
		if (attachment != null) {
			String filePath = fileDir + "/" + attachment.getFileUrl() + "\\" + attachment.getFileName();

			File file = new File(filePath);

			if (file.exists()) {
				file.delete();
			}

			this.attachmentRepo.PhysicsDelete(id, Attachment.class);
		}

		return new ActionResult<>(true, "成功删除附件");

	}

	@Override
	public ActionResult<String> DeleteAttachmentByBillId(String attachType, Boolean isOutDoc, String id) {
		String fileDir = CTRMOrg.currentOrg().getTemplateFilePath();
		DetachedCriteria where = DetachedCriteria.forClass(Attachment.class);
		where.add(Restrictions.eq("IsOutDocument", isOutDoc));

		switch (attachType) {
		case ActionStatus.AttachType_Contract:
			where.add(Restrictions.eq("ContractId", id));
			break;
		case ActionStatus.AttachType_ContractDoc:
			where.add(Restrictions.eq("ContractId", id));
			break;
		case ActionStatus.AttachType_Customer:
			where.add(Restrictions.eq("CustomerId", id));
			break;
		case ActionStatus.AttachType_Storage:
			where.add(Restrictions.eq("StorageId", id));
			break;
		case ActionStatus.AttachType_Fund:
			where.add(Restrictions.eq("FundId", id));
			break;
		case ActionStatus.AttachType_Invoice:
			where.add(Restrictions.eq("InvoiceId", id));
			break;
		case ActionStatus.AttachType_Pricing:
			where.add(Restrictions.eq("PricingId", id));
			break;
		case ActionStatus.AttachType_Position:
			where.add(Restrictions.eq("PositionId", id));
			break;
		case ActionStatus.AttachType_ReceiptShip:
			where.add(Restrictions.eq("ReceiptShipId", id));
			break;
		}

		List<Attachment> attachements = this.attachmentRepo.GetQueryable(Attachment.class).where(where).toList();

		if (attachements == null || attachements.size() == 0)
			return new ActionResult<>(false, "该单据无附件信息");

		// 循环删除
		attachements.forEach(attachment -> {

			if (attachment != null) {
				String filePath = fileDir + "/" + attachment.getFileUrl() + "/" + attachment.getFileName();

				File file = new File(filePath);

				if (file.exists()) {
					file.delete();
				}

				this.attachmentRepo.PhysicsDelete(attachment.getId(), Attachment.class);
			}
		});

		return new ActionResult<>(true, "成功删除附件");

	}

	public Position4Broker GetPositionSplitted(Position4Broker original, BigDecimal quantitySplitted) {
		// #region 数据格式检查

		if (original == null || original.getIsSquared() || original.getIsAccounted())
			return null;

		if ((original.getQuantity().compareTo(BigDecimal.ZERO) == 1
				&& quantitySplitted.compareTo(BigDecimal.ZERO) == -1)
				|| (original.getQuantity().compareTo(BigDecimal.ZERO) == -1
						&& quantitySplitted.compareTo(BigDecimal.ZERO) == 1))
			return null;

		if ((original.getQuantity().compareTo(BigDecimal.ZERO) > 0
				&& quantitySplitted.compareTo(original.getQuantity()) >= 0)
				|| (original.getQuantity().compareTo(BigDecimal.ZERO) < 0
						&& quantitySplitted.compareTo(original.getQuantity()) <= 0))
			return null;

		// #endregion

		original.setQuantity(original.getQuantity().subtract(quantitySplitted));
		this.position4BrokerRepo.SaveOrUpdate(original);

		Position4Broker positionSplitted = new Position4Broker();

		try {
			positionSplitted = com.smm.ctrm.util.BeanUtils.copy(original);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		positionSplitted.setId(null);
		positionSplitted.setQuantity(quantitySplitted);
		positionSplitted.setIsSplitted(true);
		positionSplitted.setOurRef(original.getOurRef() + "(s)");
		positionSplitted.setSourceId(original.getSourceId() == null ? original.getSourceId() : original.getSourceId());
		this.position4BrokerRepo.SaveOrUpdate(positionSplitted);
		return positionSplitted;
	}

	@Override
	public Position GetPositionSplitted(Position original, BigDecimal quantitySplitted) {

		// 检查数据格式
		if (original == null || original.getIsSquared() || original.getIsAccounted())
			return null;

		if ((original.getQuantity().compareTo(BigDecimal.ZERO) == 1
				&& quantitySplitted.compareTo(BigDecimal.ZERO) == -1)
				|| (original.getQuantity().compareTo(BigDecimal.ZERO) == -1
						&& quantitySplitted.compareTo(BigDecimal.ZERO) == 1))
			return null;

		if ((original.getQuantity().compareTo(BigDecimal.ZERO) == 1
				&& quantitySplitted.compareTo(original.getQuantity()) >= 0)
				|| (original.getQuantity().compareTo(BigDecimal.ZERO) == -1
						&& quantitySplitted.compareTo(original.getQuantity()) <= 0))
			return null;

		original.setQuantity(original.getQuantity().subtract(quantitySplitted));

		this.positionRepo.SaveOrUpdate(original);

		Position positionSplitted = new Position();

		try {
			BeanUtils.copyProperties(positionSplitted, original);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error(e.getMessage(), e);
		}

		positionSplitted.setId(null);
		positionSplitted.setQuantity(quantitySplitted);
		positionSplitted.setIsSplitted(true);
		positionSplitted.setOurRef(original.getOurRef() + "(s)");
		positionSplitted.setSourceId(original.getSourceId() == null ? original.getId() : original.getSourceId());

		this.positionRepo.SaveOrUpdate(positionSplitted);

		return positionSplitted;
	}

	@Override
	public ActionResult<String> SquareWithTransaction(List<Position> longs, List<Position> shorts) {

		if (longs == null || longs.size() == 0 || shorts == null || shorts.size() == 0)
			return new ActionResult<>(false, "不存在需要结算的头寸");

		// region 检查数据

		if (longs.stream().filter(p -> p.getIsAccounted() || p.getIsSquared()).collect(Collectors.toList())
				.size() > 0) {

			return new ActionResult<>(false, "多头头寸中存在已经对齐（结算/记账）的头寸数据");
		}

		if (longs.stream().filter(p -> p.getQuantityUnSquared().abs().compareTo(BigDecimal.ZERO) == 0)
				.collect(Collectors.toList()).size() > 0) {

			return new ActionResult<>(false, "多头含有已经平仓头寸");
		}

		if (shorts.stream().filter(p -> p.getIsAccounted() || p.getIsSquared()).collect(Collectors.toList())
				.size() > 0) {

			return new ActionResult<>(false, "空头头寸中存在已经对齐（结算/记账）的头寸数据");
		}

		if (shorts.stream().filter(p -> p.getQuantityUnSquared().abs().compareTo(BigDecimal.ZERO) == 0)
				.collect(Collectors.toList()).size() > 0) {

			return new ActionResult<>(false, "空头含有已经平仓头寸");
		}

		// endregion

		this.squareRepo.BeginTransaction();

		try {
			ActionResult<String> result = Square(longs, shorts);
			this.squareRepo.CommitTransaction();
			return result;
		} catch (Exception e) {
			logger.error(e);

			this.squareRepo.RollbackTransaction();

			throw e;
		}

	}

	@Override
	public ActionResult<String> Square(List<Position> longsParam, List<Position> shortsParam) {

		// 数据处理
		List<Position> longs = longsParam.stream()
				.sorted(Comparator.comparing(Position::getPromptDate)
						.thenComparing(Comparator.comparing(Position::getTradeDate)
								.thenComparing(Comparator.comparing(Position::getQuantity))))
				.collect(Collectors.toList());

		List<Position> shorts = shortsParam.stream()
				.sorted(Comparator.comparing(Position::getPromptDate)
						.thenComparing(Comparator.comparing(Position::getTradeDate)
								.thenComparing(Comparator.comparing(Position::getQuantity))))
				.collect(Collectors.toList());

		// ---------------

		List<Square> squareds = new ArrayList<>();

		for (Position l : longs) {

			if (l == null)
				continue;

			Position s = shorts.stream()
					.filter(pn -> pn.getMarketId().equals(l.getMarketId())
							&& pn.getCommodityId().equals(l.getCommodityId())
							&& pn.getPromptDate().equals(l.getPromptDate()))
					.collect(Collectors.toList()).get(0);

			if (s == null) {

				longs.remove(l);

				continue;
			}

			BigDecimal qtyLong = l.getQuantityUnSquared();
			BigDecimal qtyShort = s.getQuantityUnSquared();

			// 多空头数量相同
			if (qtyLong.compareTo(qtyShort.abs()) == 0) {
				Square positionSquared = new Square();
				positionSquared.setSquareDate(new Date());
				positionSquared.setPromptDate(l.getPromptDate());
				positionSquared.setQuantity(l.getQuantity());
				positionSquared.setLongId(l.getId());
				positionSquared.setShortId(s.getId());
				positionSquared.setCurrency(l.getCurrency());
				positionSquared.setPnL(s.getOurPrice().subtract(l.getOurPrice().multiply(qtyLong)));
				positionSquared.setCreatedAt(new Date());
				positionSquared.setRefLong(l.getOurRef());
				positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
				positionSquared.setPromptDateLong(l.getPromptDate());
				positionSquared.setTradeDateLong(l.getTradeDate());

				positionSquared.setRefShort(s.getOurRef());
				positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());
				positionSquared.setPromptDateShort(s.getPromptDate());
				positionSquared.setTradeDateShort(s.getTradeDate());
				positionSquared.setCommodityId(l.getCommodityId());
				positionSquared.setMarketId(l.getMarketId());

				this.squareRepo.SaveOrUpdate(positionSquared);

				l.setQuantityUnSquared(BigDecimal.ZERO);
				l.setIsSquared(true);
				this.positionRepo.SaveOrUpdate(l);

				s.setQuantityUnSquared(BigDecimal.ZERO);
				s.setIsSquared(true);
				this.positionRepo.SaveOrUpdate(s);

				squareds.add(positionSquared);
				longs.remove(l);
				shorts.remove(s);
			} else if (qtyLong.compareTo(qtyShort.abs()) == 1) {

				// 多头数量大于空头数量，即：需要拆分多头头寸

				// 多头中拆分出来的头寸。同样是多头。
				Position longSplitted = GetPositionSplitted(l, qtyShort.abs());

				if (longSplitted == null) {
					longs.remove(l);
					continue;
				}

				// 修正拆出的头寸的对齐状态
				longSplitted = this.positionRepo.getOneById(longSplitted.getId(), Position.class);

				longSplitted.setQuantityUnSquared(BigDecimal.ZERO);
				longSplitted.setIsSquared(true);
				positionRepo.SaveOrUpdate(longSplitted);

				Square positionSquared = new Square();
				positionSquared.setSquareDate(new Date());
				positionSquared.setPromptDate(l.getPromptDate());
				positionSquared.setQuantity(qtyShort.abs());
				positionSquared.setLongId(longSplitted.getId());
				positionSquared.setShortId(s.getId());
				positionSquared.setCurrency(longSplitted.getCurrency());
				positionSquared.setPnL(s.getOurPrice().subtract(longSplitted.getOurPrice().multiply(qtyShort.abs())));
				positionSquared.setCreatedAt(new Date());
				positionSquared.setRefLong(l.getOurRef());
				positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
				positionSquared.setPromptDateLong(l.getPromptDate());
				positionSquared.setTradeDateLong(l.getTradeDate());

				positionSquared.setRefShort(s.getOurRef());
				positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());

				positionSquared.setPromptDateShort(s.getPromptDate());
				positionSquared.setTradeDateShort(s.getTradeDate());
				positionSquared.setCommodityId(l.getCommodityId());
				positionSquared.setMarketId(l.getMarketId());

				this.squareRepo.SaveOrUpdate(positionSquared);

				l.setQuantityUnSquared(l.getQuantityUnSquared().subtract(qtyShort.abs()));
				this.positionRepo.SaveOrUpdate(l);

				s.setQuantityUnSquared(BigDecimal.ZERO);
				s.setIsSquared(true);
				this.positionRepo.SaveOrUpdate(s);

				squareds.add(positionSquared);
				shorts.remove(s);

			} else if (qtyLong.compareTo(qtyShort.abs()) == -1) {
				// 多头数量小于空头数量，即：需要拆分空头头寸

				Position shortSplitted = GetPositionSplitted(s, BigDecimal.ZERO.subtract(qtyLong));

				if (shortSplitted == null) {
					shorts.remove(l);
					continue;
				}

				// 修正拆出的头寸的对齐状态
				shortSplitted = this.positionRepo.getOneById(shortSplitted.getId(), Position.class);
				shortSplitted.setQuantityUnSquared(BigDecimal.ZERO);
				shortSplitted.setIsSquared(true);
				positionRepo.SaveOrUpdate(shortSplitted);

				Square positionSquared = new Square();
				positionSquared.setSquareDate(new Date());
				positionSquared.setPromptDate(l.getPromptDate());
				positionSquared.setQuantity(qtyLong);
				positionSquared.setLongId(l.getId());
				positionSquared.setShortId(shortSplitted.getId());
				positionSquared.setCurrency(l.getCurrency());

				positionSquared.setPnL(shortSplitted.getOurPrice().subtract(l.getOurPrice().multiply(qtyLong)));
				positionSquared.setCreatedAt(new Date());
				positionSquared.setRefLong(l.getOurRef());

				positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
				positionSquared.setPromptDateLong(l.getPromptDate());
				positionSquared.setTradeDateLong(l.getTradeDate());

				positionSquared.setRefShort(s.getOurRef());
				positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());

				positionSquared.setPromptDateShort(s.getPromptDate());
				positionSquared.setTradeDateShort(s.getTradeDate());
				positionSquared.setCommodityId(l.getCommodityId());
				positionSquared.setMarketId(l.getMarketId());

				this.squareRepo.SaveOrUpdate(positionSquared);

				s.setQuantityUnSquared(s.getQuantityUnSquared().add(qtyLong.abs())); // 实际上，qtyLong肯定是正数
				this.positionRepo.SaveOrUpdate(s);

				l.setQuantityUnSquared(BigDecimal.ZERO);
				l.setIsSquared(true);
				positionRepo.SaveOrUpdate(l);

				squareds.add(positionSquared);
				shorts.remove(l);
			}

		}

		// 循环更新
		squareds.forEach(this.squareRepo::SaveOrUpdate);

		return new ActionResult<>(true, "头寸结算成功");
	}

	@Override
	public ActionResult<String> Square4Broker(List<Position4Broker> longsParam, List<Position4Broker> shortsParam) {

		// 数据处理
		List<Position4Broker> longs = longsParam.stream()
				.sorted(Comparator.comparing(Position4Broker::getPromptDate)
						.thenComparing(Comparator.comparing(Position4Broker::getTradeDate)))
				.collect(Collectors.toList());

		List<Position4Broker> shorts = shortsParam.stream()
				.sorted(Comparator.comparing(Position4Broker::getPromptDate)
						.thenComparing(Comparator.comparing(Position4Broker::getTradeDate)))
				.collect(Collectors.toList());

		// ---------------------------

		List<Square4Broker> squareds = new ArrayList<>();

		while (shorts.stream().anyMatch(x -> !x.getIsDel())) {

			try {

				Position4Broker s = shorts.stream().filter(x -> !x.getIsDel()).findFirst().orElse(null);

				if (s == null || s.getIsDel()) {
					continue;
				}

				Position4Broker l = null;
				for (Position4Broker pn : longs) {
					if (pn.getMarketId().equals(s.getMarketId()) && pn.getCommodityId().equals(s.getCommodityId())
							&& DateUtil.doFormatDate(pn.getPromptDate(), "yyyy-MM-dd")
									.compareTo(DateUtil.doFormatDate(s.getPromptDate(), "yyyy-MM-dd")) == 0
							&& !pn.getIsDel()) {
						l = pn;
						break;
					}
				}
				if (l == null) {
					s.setIsDel(true);
					continue;
				}

				BigDecimal qtyLong = l.getQuantityUnSquared();
				BigDecimal qtyShort = s.getQuantityUnSquared();

				// 多空头数量相同
				if (qtyLong.compareTo(qtyShort.abs()) == 0) {
					Square4Broker positionSquared = new Square4Broker();

					positionSquared.setSquareDate(new Date());
					positionSquared.setPromptDate(l.getPromptDate());
					positionSquared.setQuantity(l.getQuantity());
					positionSquared.setLongId(l.getId());
					positionSquared.setShortId(s.getId());
					positionSquared.setCurrency(l.getCurrency());

					positionSquared.setPnL(s.getOurPrice().subtract(l.getOurPrice().multiply(qtyLong)));
					positionSquared.setCreatedAt(new Date());
					positionSquared.setRefLong(l.getOurRef());

					positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
					positionSquared.setPromptDateLong(l.getPromptDate());
					positionSquared.setTradeDateLong(l.getTradeDate());

					positionSquared.setRefShort(s.getOurRef());
					positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());

					positionSquared.setPromptDateShort(s.getPromptDate());
					positionSquared.setTradeDateShort(s.getTradeDate());
					positionSquared.setCommodityId(l.getCommodityId());
					positionSquared.setMarketId(l.getMarketId());

					this.square4BrokerRepo.SaveOrUpdate(positionSquared);

					l.setQuantityUnSquared(BigDecimal.ZERO);
					l.setIsSquared(true);
					this.position4BrokerRepo.SaveOrUpdate(l);

					s.setQuantityUnSquared(BigDecimal.ZERO);
					s.setIsSquared(true);
					this.position4BrokerRepo.SaveOrUpdate(s);

					squareds.add(positionSquared);

					l.setIsDel(true);
					s.setIsDel(true);
					// longs.Remove(l);
					// shorts.Remove(s);
				} else if (qtyLong.compareTo(qtyShort.abs()) == 1) {
					// 多头数量大于空头数量，即：需要拆分多头头寸

					// 多头中拆分出来的头寸。同样是多头。
					Position4Broker longSplitted = GetPositionSplitted(l, qtyShort.abs());

					if (longSplitted == null) {
						// longs.Remove(l);
						l.setIsDel(true);
						continue;
					}

					// 修正拆出的头寸的对齐状态
					longSplitted = this.position4BrokerRepo.getOneById(longSplitted.getId(), Position4Broker.class);
					longSplitted.setQuantityUnSquared(BigDecimal.ZERO);
					longSplitted.setIsSquared(true);
					this.position4BrokerRepo.SaveOrUpdate(longSplitted);

					Square4Broker positionSquared = new Square4Broker();

					positionSquared.setSquareDate(new Date());
					positionSquared.setPromptDate(l.getPromptDate());
					positionSquared.setQuantity(qtyShort.abs());
					positionSquared.setLongId(longSplitted.getId());
					positionSquared.setShortId(s.getId());
					positionSquared.setCurrency(longSplitted.getCurrency());

					positionSquared
							.setPnL(s.getOurPrice().subtract(longSplitted.getOurPrice().multiply(qtyShort.abs())));
					positionSquared.setCreatedAt(new Date());
					positionSquared.setRefLong(l.getOurRef());

					positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
					positionSquared.setPromptDateLong(l.getPromptDate());
					positionSquared.setTradeDateLong(l.getTradeDate());

					positionSquared.setRefShort(s.getOurRef());
					positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());

					positionSquared.setPromptDateShort(s.getPromptDate());
					positionSquared.setTradeDateShort(s.getTradeDate());
					positionSquared.setCommodityId(l.getCommodityId());
					positionSquared.setMarketId(l.getMarketId());

					this.square4BrokerRepo.SaveOrUpdate(positionSquared);

					l.setQuantityUnSquared(l.getQuantityUnSquared().subtract(qtyShort.abs()));
					this.position4BrokerRepo.SaveOrUpdate(l);

					s.setQuantityUnSquared(BigDecimal.ZERO);
					s.setIsSquared(true);
					this.position4BrokerRepo.SaveOrUpdate(s);

					squareds.add(positionSquared);
					s.setIsDel(true);
					// shorts.Remove(s);
				} else if (qtyLong.compareTo(qtyShort.abs()) < 0) {

					// 多头数量小于空头数量，即：需要拆分空头头寸
					Position4Broker shortSplitted = GetPositionSplitted(s, BigDecimal.ZERO.subtract(qtyLong));
					if (shortSplitted == null) {
						// shorts.Remove(l);
						l.setIsDel(true);
						continue;
					}

					// 修正拆出的头寸的对齐状态
					shortSplitted = this.position4BrokerRepo.getOneById(shortSplitted.getId(), Position4Broker.class);
					shortSplitted.setQuantityUnSquared(BigDecimal.ZERO);
					shortSplitted.setIsSquared(true);
					position4BrokerRepo.SaveOrUpdate(shortSplitted);

					Square4Broker positionSquared = new Square4Broker();

					positionSquared.setSquareDate(new Date());
					positionSquared.setPromptDate(l.getPromptDate());
					positionSquared.setQuantity(qtyShort);
					positionSquared.setLongId(l.getId());
					positionSquared.setShortId(shortSplitted.getId());
					positionSquared.setCurrency(l.getCurrency());

					positionSquared.setPnL(shortSplitted.getOurPrice().subtract(l.getOurPrice().multiply(qtyLong)));
					positionSquared.setCreatedAt(new Date());
					positionSquared.setRefLong(l.getOurRef());

					positionSquared.setPriceLong(l.getOurPrice() == null ? BigDecimal.ZERO : l.getOurPrice());
					positionSquared.setPromptDateLong(l.getPromptDate());
					positionSquared.setTradeDateLong(l.getTradeDate());

					positionSquared.setRefShort(s.getOurRef());
					positionSquared.setPriceShort(s.getOurPrice() == null ? BigDecimal.ZERO : s.getOurPrice());

					positionSquared.setPromptDateShort(s.getPromptDate());
					positionSquared.setTradeDateShort(s.getTradeDate());
					positionSquared.setCommodityId(l.getCommodityId());
					positionSquared.setMarketId(l.getMarketId());

					this.square4BrokerRepo.SaveOrUpdate(positionSquared);

					s.setQuantityUnSquared(s.getQuantityUnSquared().add(qtyLong.abs())); // 实际上，qtyLong肯定是正数
					this.position4BrokerRepo.SaveOrUpdate(s);

					l.setQuantityUnSquared(BigDecimal.ZERO);
					l.setIsSquared(true);
					position4BrokerRepo.SaveOrUpdate(l);

					squareds.add(positionSquared);
					// shorts.Remove(l);
					l.setIsDel(true);
				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				return new ActionResult<>(false,e.getMessage());
			}

		}

		squareds.forEach(this.square4BrokerRepo::SaveOrUpdate);

		return new ActionResult<>(true, "头寸结算成功");
	}

	@Override
	public GlobalSet GetGlobalSet() {

		return this.globalSetRepo.GetList(GlobalSet.class).get(0);
	}

	@Override
	public String FormatSortBy(String sortBy) {

		if (StringUtils.isEmpty(sortBy))
			return null;

		sortBy = sortBy.replace("Legal.", "legal.");
		sortBy = sortBy.replace("Contract.", "contract.");
		sortBy = sortBy.replace("Lot.", "lot.");
		sortBy = sortBy.replace("Invoice.", "invoice.");
		sortBy = sortBy.replace("Storage.", "storage.");
		sortBy = sortBy.replace("Fund.", "fund.");
		sortBy = sortBy.replace("Commodity.", "commodity.");
		sortBy = sortBy.replace("Spec.", "spec.");
		sortBy = sortBy.replace("Brand.", "brand.");

		sortBy = sortBy.replace("Customer.", "customer.");
		sortBy = sortBy.replace("TraderName.", "trader.Name");
		sortBy = sortBy.replace("CustomerName.", "customer.Name");

		sortBy = sortBy.equals("MajorMarket.Name") ? "majorMarket.Name" : sortBy;
		sortBy = sortBy.equals("MajorMarket.Code") ? "majorMarket.Code" : sortBy;
		sortBy = sortBy.equals("PremiumMarket.Name") ? "premiunMarket.Name" : sortBy;
		sortBy = sortBy.equals("PremiumMarket.Code") ? "premiumMarket.Code" : sortBy;
		sortBy = sortBy.equals("Market.Name") ? "market.Name" : sortBy;
		sortBy = sortBy.equals("Market.Code") ? "market.Code" : sortBy;
		return sortBy;
	}

	@Override
	public void SetStorage(Lot lot, Storage storage) {

		if (lot == null || lot.getIsPriced() == false || storage == null)
			return;

		storage.setFee(lot.getFee() == null ? BigDecimal.ZERO : lot.getFee());
		storage.setPremium(lot.getPremium() == null ? BigDecimal.ZERO : lot.getPremium());
		storage.setMajor(lot.getMajor() == null ? BigDecimal.ZERO : lot.getMajor());
		storage.setPrice(lot.getPrice() == null ? BigDecimal.ZERO : lot.getPrice());

		storage.setAmount(storage.getQuantity().multiply(storage.getPrice()));
		storage.setCurrency(lot.getCurrency());

	}

	// @Override
	// public List<CustomerTitle> SimplifyData(List<CustomerTitle>
	// customerTitles) {
	// return null;
	// }

	@Override
	public Customer SimplifyData(Customer customer) {

		if (customer == null)
			return null;

		if (customer.getArea() != null) {

			customer.setAreaName(customer.getArea().getName());
		}

		Customer returnCustomer = new Customer();

		try {
			BeanUtils.copyProperties(returnCustomer, customer);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return returnCustomer;

	}

	@Override
	public CustomerTitle SimplifyData(CustomerTitle customerTitle) {

		
		if (customerTitle == null)
			return null;
		
		CustomerTitle obj = com.smm.ctrm.util.BeanUtils.copy(customerTitle);
		if (customerTitle.getCustomer() != null){
			obj.setCustomerName(customerTitle.getCustomer().getName());
			obj.setCustomer(null);
		}
		obj.setCreatedAt(null);
		obj.setCreatedBy(null);
		obj.setDeletedAt(null);
		obj.setDeletedBy(null);
		obj.setIsDeleted(null);
		obj.setIsHidden(null);
		obj.setUpdatedBy(null);
		obj.setUpdatedAt(null);

		return obj;
	}

	@Override
	public CustomerBalance SimplifyData(CustomerBalance customerBalance) {

		if (customerBalance == null)
			return null;

		if (customerBalance.getCustomer() != null) {
			customerBalance.setCustomerName(customerBalance.getCustomer().getName());
			customerBalance.setCustomer(null);
		}
		if (customerBalance.getLegal() != null) {
			customerBalance.setLegalName(customerBalance.getLegal().getName());
			customerBalance.setLegal(null);
		}
		if (customerBalance.getCommodity() != null) {
			customerBalance.setCommodityName(customerBalance.getCommodity().getName());
			customerBalance.setCommodity(null);
		}

		CustomerBalance obj = new CustomerBalance();

		try {
			ConvertUtils.register(new SqlDateConverter(null), java.util.Date.class);
			ConvertUtils.register(new SqlDateConverter(null), java.math.BigDecimal.class);
			BeanUtils.copyProperties(obj, customerBalance);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return obj;
	}

	@Override
	public Contract SimplifyData(Contract contract) {

		if (contract == null)
			return null;

		Contract obj = com.smm.ctrm.util.BeanUtils.copy(contract);
		
		if (contract.getCustomer() != null) {
			obj.setCustomer(contract.getCustomer());
			obj.setCustomerName(contract.getCustomer().getName());
		}
		if (contract.getTrader() != null) {
			obj.setTraderName(contract.getTrader().getName());
		}

		if (contract.getCustomerTitle() != null) {
			obj.setCustomerTitleName(contract.getCustomerTitle().getName());
		}

		if (contract.getLegal() != null) {
			obj.setLegalName(contract.getLegal().getName());
			obj.setLegalCode(contract.getLegal().getCode());
		}
		if (contract.getCommodity() != null) {

			obj.setCommodityCode(contract.getCommodity().getCode());
			obj.setCommodityName(contract.getCommodity().getName());

			/*
			obj.setQuantity(FormatQuantity(obj.getQuantity(), contract.getCommodity(), contract.getCommodityId()));
			obj.setQuantityOfLots(
					FormatQuantity(obj.getQuantityOfLots(), contract.getCommodity(), contract.getCommodityId()));
			*/
		}

		return obj;
	}

	@Override
	public Lot SimplifyData(Lot lot) {

		if (lot == null)
			return null;

		Lot obj = com.smm.ctrm.util.BeanUtils.copy(lot);

		obj.setLegal(lot.getLegal());

		if (lot.getCustomer() != null) {
			obj.setCustomerName(lot.getCustomer().getName());
		}

		if (lot.getContract() != null && lot.getContract().getTrader() != null) {
			// 重新读取交易商名称
			obj.setTraderName(lot.getContract().getTrader().getName());
		}

		if (lot.getLegal() != null) {
			obj.setLegalCode(lot.getLegal().getCode());
			obj.setLegalName(lot.getLegal().getName());
		}

		if (lot.getMajorMarket() != null) {
			obj.setMajorMarketName(lot.getMajorMarket().getName());
		}

		if (lot.getCommodity() != null) {
			obj.setDigits(lot.getCommodity().getDigits());
			obj.setUnit(lot.getCommodity().getUnit());
		}

		/*
		// region 格式化数量,单价
		obj.setQuantity(FormatQuantity(obj.getQuantity(), lot.getCommodity(), lot.getCommodityId()));

		obj.setQuantityOriginal(FormatQuantity(obj.getQuantityOriginal(), lot.getCommodity(), lot.getCommodityId()));
		obj.setQuantityLess(FormatQuantity(obj.getQuantityLess(), lot.getCommodity(), lot.getCommodityId()));
		obj.setQuantityMore(FormatQuantity(obj.getQuantityMore(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQuantityDelivered() != null) {
			obj.setQuantityDelivered(
					FormatQuantity(obj.getQuantityDelivered(), lot.getCommodity(), lot.getCommodityId()));
		}

		if (obj.getQuantityInvoiced() != null)
			obj.setQuantityInvoiced(
					FormatQuantity(obj.getQuantityInvoiced(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQuantityPriced() != null)
			obj.setQuantityPriced(FormatQuantity(obj.getQuantityPriced(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQuantityHedged() != null)
			obj.setQuantityHedged(FormatQuantity(obj.getQuantityHedged(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQuantityUnPriced() != null)
			obj.setQuantityUnPriced(
					FormatQuantity(obj.getQuantityUnPriced(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQuantityUnInvoiced() != null)
			obj.setQuantityUnInvoiced(
					FormatQuantity(obj.getQuantityUnInvoiced(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQtyPerPremiumDay() != null)
			obj.setQtyPerPremiumDay(
					FormatQuantity(obj.getQtyPerPremiumDay(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getQtyPerMainDay() != null)
			obj.setQtyPerMainDay(FormatQuantity(obj.getQtyPerMainDay(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getPremium() != null)
			obj.setPremium(FormatPrice(obj.getPremium(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getMajor() != null)
			obj.setMajor(FormatPrice(obj.getMajor(), lot.getCommodity(), lot.getCommodityId()));

		if (obj.getPrice() != null)
			obj.setPrice(FormatPrice(obj.getPrice(), lot.getCommodity(), lot.getCommodityId()));
		if (obj.getFinal() != null)
			obj.setFinal(FormatPrice(obj.getFinal(), lot.getCommodity(), lot.getCommodityId()));
		if (obj.getFee() != null)
			obj.setFee(FormatPrice(obj.getFee(), lot.getCommodity(), lot.getCommodityId()));
		if (obj.getRealFee() != null)
			obj.setRealFee(FormatPrice(obj.getRealFee(), lot.getCommodity(), lot.getCommodityId()));
		if (obj.getEstimateFee() != null)
			obj.setEstimateFee(FormatPrice(obj.getEstimateFee(), lot.getCommodity(), lot.getCommodityId()));
		// endregion
		*/
		obj.setBrands(lot.getBrands());
		obj.setContract(lot.getContract());
		// obj.setContract(SimplifyData(lot.getContract()));

		obj.setQuantityBeforeChanged(lot.getQuantity());

		return obj;
	}

	@Override
	public Fund SimplifyData(Fund fund) {

		if (fund.getLot() != null) {
			fund.setFullNo(fund.getLot().getFullNo());
		}
		if (fund.getCustomer() != null)
			fund.setCustomerName(fund.getCustomer().getName());
		if (fund.getCustomerTitle() != null)
			fund.setCustomerTitleName(fund.getCustomerTitle().getName());
		if (fund.getLegal() != null)
			fund.setLegalName(fund.getLegal().getName());
		if (fund.getInvoice() != null) {
			fund.setInvoiceQuantity(fund.getInvoice().getQuantity());
			fund.setInvoiceAmount(fund.getInvoice().getAmount());
			fund.setInvoiceDueDate(fund.getInvoice().getDueDate());
			fund.setInvoiceTradeDate(fund.getInvoice().getTradeDate());
			fund.setInvoiceNo(fund.getInvoice().getInvoiceNo());
		}
		if (fund.getCommodity() != null) {
			fund.setCommodityCode(fund.getCommodity().getCode());
			fund.setCommodityName(fund.getCommodity().getName());
			fund.setUnit(fund.getCommodity().getUnit());
		}
		/*
		fund.setQuantity(FormatQuantity(fund.getQuantity(), fund.getCommodity(), fund.getCommodityId()));
		fund.setInvoiceQuantity(FormatQuantity(fund.getInvoiceQuantity(), fund.getCommodity(), fund.getCommodityId()));
	 	*/
		fund.setInvoice(SimplifyData(fund.getInvoice()));

		return fund;
	}

	@Override
	public LC SimplifyData(LC lc) {

		if (lc == null) {
			return lc;
		}

		if (lc.getBenificiaryId() != null) {
			lc.setBenificiaryName(customerRepo.getOneById(lc.getBenificiaryId(), Customer.class).getName());
		}
		if (lc.getIssuerId() != null) {
			lc.setIssuerName(customerRepo.getOneById(lc.getIssuerId(), Customer.class).getName());
		}
		List<Invoice> invoiceList = invoiceRepo.GetQueryable(Invoice.class)
				.where(DetachedCriteria.forClass(Invoice.class).add(Restrictions.eq("LcId", lc.getId()))).toList();
		lc.setInvoices(this.SimplifyDataInvoiceList(invoiceList));

		return lc;
	}

	@Override
	public List<Customer> SimplifyDataCustomerList(List<Customer> customer) {

		if (customer == null || customer.size() == 0)
			return null;

		return customer.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public Fee SimplifyData(Fee fee) {

		if (fee == null)
			return null;
		if (fee.getLot() != null)
			fee.setFullNo(fee.getLot().getFullNo());

		Fee obj = new Fee();

		try {
			BeanUtils.copyProperties(obj, fee);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public Invoice SimplifyData(Invoice invoice) {

		if (invoice == null)
			return null;

		if (invoice.getLot() != null) {
			invoice.setFullNo(invoice.getLot().getFullNo());
		} else {
			// 增加此处的原因是，因为增加了一个发票包含多个批次交付明细的情形
			if (invoice.getContract() != null)
				invoice.setFullNo(invoice.getContract().getHeadNo());
		}

		if (invoice.getCustomer() != null)
			invoice.setCustomerName(invoice.getCustomer().getName());
		;
		if (invoice.getCustomerTitle() != null)
			invoice.setCustomerTitleName(invoice.getCustomerTitle().getName());
		;
		if (invoice.getLegal() != null)
			invoice.setLegalName(invoice.getLegal().getName());

		if (invoice.getCommodity() != null) {
			invoice.setCommodityCode(invoice.getCommodity().getCode());
			invoice.setCommodityName(invoice.getCommodity().getName());
			invoice.setUnit(invoice.getCommodity().getUnit());
		}

		invoice.setQuantity(FormatQuantity(invoice.getQuantity(), invoice.getCommodity(), invoice.getCommodityId()));
		invoice.setQuantityDrafted(
				FormatQuantity(invoice.getQuantityDrafted(), invoice.getCommodity(), invoice.getCommodityId()));
		
		Invoice obj = com.smm.ctrm.util.BeanUtils.copy(invoice);
		
		if (invoice.getInvoiceGrades() != null)
			obj.setInvoiceGrades(SimplifyDataInvoiceGradeList(invoice.getInvoiceGrades()));

		if (invoice.getStorages() != null)
			obj.setStorages(SimplifyDataStorageList(invoice.getStorages()));

		if (invoice.getNotices() != null)
			obj.setNotices(SimplifyDataStorageList(invoice.getNotices()));
		
		return obj;
	}

	@Override
	public Storage SimplifyData(Storage obj) {

		if (obj == null)
			return null;
		Storage storage = com.smm.ctrm.util.BeanUtils.copy(obj);
		storage.setQuantityInvoiced(obj.getQuantity()); // QuantityInvoiced
		if (obj.getPrice() != null && BigDecimal.ZERO.compareTo(obj.getPrice()) != 0) {
			storage.setAmount(storage.getQuantityInvoiced().multiply(obj.getPrice()));
		} else {
			storage.setAmount(storage.getQuantityInvoiced()
					.multiply(obj.getPriceProvisional() != null ? obj.getPriceProvisional() : BigDecimal.ZERO));
		}
		storage.setAmount2((obj.getStorageQuantity() != null ? obj.getStorageQuantity() : BigDecimal.ZERO)
				.multiply(obj.getPrice() != null ? obj.getPrice() : BigDecimal.ZERO));
		if (obj.getBviSource() != null)
			storage.setSourceProjectName(obj.getBviSource().getProjectName());
		if (obj.getLot() != null) {
			storage.setFullNo(obj.getLot().getFullNo());
			storage.setLotDischarging(obj.getLot().getDischarging());
			storage.setLotEstimateSaleDate(obj.getLot().getEstimateSaleDate());
			storage.setDeliveryTermOfLot(obj.getLot().getDeliveryTerm());
		}
		if (obj.getCustomer() != null) {
			storage.setCustomerName(obj.getCustomer().getName());
		}
		if (obj.getEstCustomer() != null) {
			storage.setEstCustomerName(obj.getEstCustomer().getName());
		}
		if (obj.getLegal() != null) {
			storage.setLegalName(obj.getLegal().getName());
		}
		if (obj.getBrand() != null) {
			storage.setBrandName(obj.getBrand().getName());
		}
		if (obj.getSpec() != null) {
			storage.setSpecName(obj.getSpec().getName());
			storage.setGrade(obj.getSpec().getName());
		}
		if (obj.getWarehouse() != null) {
			storage.setWarehouseName(obj.getWarehouse().getName());
		}
		if (obj.getTransitStatus() == null)
			storage.setTransitStatus("");
		if (obj.getCommodity() != null) {
			storage.setQuantityPerLot(obj.getCommodity().getQuantityPerLot());
			storage.setDigits(obj.getCommodity().getDigits());
			storage.setCommodityCode(obj.getCommodity().getCode());
			storage.setCommodityName(obj.getCommodity().getName());
			storage.setUnit(obj.getCommodity().getUnit());
		}
		
		/*
		// 格式化数量
		storage.setQuantity(FormatQuantity(obj.getQuantity(), obj.getCommodity(), obj.getCommodityId()));
		storage.setGross(FormatQuantity(obj.getGross(), obj.getCommodity(), obj.getCommodityId()));
		storage.setUnCxQuantity(FormatQuantity(obj.getUnCxQuantity(), obj.getCommodity(), obj.getCommodityId()));
		storage.setGrossAtFactory(FormatQuantity(obj.getGrossAtFactory(), obj.getCommodity(), obj.getCommodityId()));
		storage.setDiff(FormatQuantity(obj.getDiff(), obj.getCommodity(), obj.getCommodityId()));
		storage.setStorageQuantity(
				FormatQuantity(storage.getGross().subtract(new BigDecimal(obj.getBundles() * 0.003f)),
						obj.getCommodity(), obj.getCommodityId()));
						
		if (obj.getCounterparty() != null) {
			if (obj.getCounterparty().getLot() != null)
				storage.setFullNoOfCounterparty(obj.getCounterparty().getLot().getFullNo());

			if (obj.getCounterparty().getCustomer() != null)
				storage.setCustomerNameOfCounterparty(obj.getCounterparty().getCustomer().getName());
		}*/
		if (obj.getMT().equals("M") && obj.getCounterparty3() != null) {
			if(obj.getCounterparty3().getLot()!= null) {
				storage.setFullNoOfCounterparty(obj.getCounterparty3().getLot().getFullNo());
			}
			if(obj.getCounterparty3().getCustomer() != null) {
				storage.setCustomerNameOfCounterparty(obj.getCounterparty3().getCustomer().getName());
			}
		} else if (obj.getMT().equals("T") && obj.getCounterparty2() != null) {
			if(obj.getCounterparty2().getLot()!= null) {
				storage.setFullNoOfCounterparty(obj.getCounterparty2().getLot().getFullNo());
			}
			if(obj.getCounterparty2().getCustomer() != null) {
				storage.setCustomerNameOfCounterparty(obj.getCounterparty2().getCustomer().getName());
			}
		}
		if (storage.getContract() != null) {
			storage.setContractSpotDirection(
					obj.getContract().getSpotDirection().equals(ActionStatus.SpotType_Purchase) ? "采购" : "销售");
		}
		if (obj.getCustomer() != null)
			storage.setCustomer(com.smm.ctrm.util.BeanUtils.copy(obj.getCustomer()));

		if (obj.getLot() != null)
			storage.setLot(com.smm.ctrm.util.BeanUtils.copy(obj.getLot()));
		if (obj.getSource() != null)
			storage.setSource(com.smm.ctrm.util.BeanUtils.copy(obj.getSource()));
		
		return storage;
	}

	@Override
	public Position SimplifyData(Position position) {

		if (position == null)
			return null;

		if (position.getMarket() != null) {
			position.setMarketCode(position.getMarket().getCode());
			position.setMarketName(position.getMarket().getName());
			position.setMarket(null);

		}
		if (position.getCommodity() != null) {
			position.setCommodityName(position.getCommodity().getName());
			position.setCommodityCode(position.getCommodity().getCode());
			position.setCommodity(null);
		}
		if (position.getLot() != null) {
			position.setLotQuantity(position.getLot().getQuantity());
			position.setLotQuantityHedged(position.getLot().getQuantityHedged());
			position.setLotPrice(position.getLot().getPrice());
			position.setFullNo(position.getLot().getFullNo());
			position.setLot(null);// 把不需要的属性去掉，节省json序列化时间
		}
		if (position.getLegal() != null) {
			position.setLegalCode(position.getLegal().getCode());
			position.setLegalName(position.getLegal().getName());
		}
		if (position.getBroker() != null) {
			position.setBrokerName(position.getBroker().getName());
			position.setBroker(null);
		}
		if(StringUtils.isNotBlank(position.getInstrumentId())) {
			Instrument instrument = instrumentRepo.getOneById(position.getInstrumentId(), Instrument.class);
			if(instrument != null){
				position.setInstrumentName(instrument.getName());
			}
		}
		/*
		position.setQuantity(
				FormatQuantity(position.getQuantity(), position.getCommodity(), position.getCommodityId()));
		position.setQuantityOriginal(
				FormatQuantity(position.getQuantityOriginal(), position.getCommodity(), position.getCommodityId()));
		position.setQuantityUnSquared(
				FormatQuantity(position.getQuantityUnSquared(), position.getCommodity(), position.getCommodityId()));
		*/

		position.setQuantityBeforeChanged(position.getQuantity());

		// Position p = new Position();

		// try {
		// ConvertUtils.register(new SqlDateConverter(null),
		// java.math.BigDecimal.class);
		// ConvertUtils.register(new SqlDateConverter(null),
		// java.util.Date.class);
		// BeanUtils.copyProperties(p, position);
		// } catch (IllegalAccessException | InvocationTargetException e) {
		// e.printStackTrace();
		// }

		// position.setBeforeChanged(p);

		// Position obj = new Position();
		//

		return position;
	}

	@Override
	public Square SimplifyData(Square square) {

		if (square == null || square.getLong() == null || square.getShort() == null)
			return null;

		square.setPromptDate(square.getLong().getPromptDate());
		square.setQuantity(square.getLong().getQuantity());

		square.setMarketCode(square.getLong().getMarket().getCode());
		square.setMarketName(square.getLong().getMarket().getName());

		square.setCommodityCode(square.getLong().getCommodity().getCode());
		square.setCommodityName(square.getLong().getCommodity().getName());

		square.setPriceLong(square.getLong().getOurPrice() != null ? square.getLong().getOurPrice() : BigDecimal.ZERO);
		square.setRefLong(square.getLong().getOurRef());

		square.setPriceShort(
				square.getShort().getOurPrice() != null ? square.getShort().getOurPrice() : BigDecimal.ZERO);
		square.setRefShort(square.getShort().getOurRef());

		/*
		square.setQuantity(FormatQuantity(square.getQuantity(), square.getCommodity(), square.getCommodityId()));
		square.setQuantityLong(
				FormatQuantity(square.getQuantityLong(), square.getCommodity(), square.getCommodityId()));
		square.setQuantityLong(
				FormatQuantity(square.getQuantityShort(), square.getCommodity(), square.getCommodityId()));
		*/

		return com.smm.ctrm.util.BeanUtils.copy(square);
	}

	@Override
	public Pricing SimplifyData(Pricing pricing) {

		if (pricing == null)
			return null;

		// if (pricing.getLegal() != null)
		// pricing.setLegalName(pricing.getLegal().getName());

		if (pricing.getMajorMarket() != null)
			pricing.setMajorMarketName(pricing.getMajorMarket().getName());

		// if (pricing.getPremiumMarket() != null)
		// pricing.setPremiumMarketName(pricing.getPremiumMarket().getName());

		// if (pricing.getContract() != null) {
		// pricing.setHeadNo(pricing.getContract().getHeadNo());
		// pricing.setQuantityOfContract(pricing.getContract().getQuantity());
		// pricing.setQuantityPriciedOfContract(pricing.getContract().getQuantityPriced());
		// }

		if (pricing.getLot() != null) {
			pricing.setQuantityOfLot(pricing.getLot().getQuantity());
			pricing.setQuantityPricedOfLot(pricing.getLot().getQuantityPriced());
			pricing.setFullNo(pricing.getLot().getFullNo());
			pricing.setHeadNo(pricing.getLot().getHeadNo());
			pricing.setLotNo(pricing.getLot().getLotNo());
		}

		if (pricing.getCustomer() != null)
			pricing.setCustomerName(pricing.getCustomer().getName());

		if (pricing.getCommodity() != null) {
			pricing.setQuantityPerLot(pricing.getCommodity().getQuantityPerLot());
			pricing.setDigits(pricing.getCommodity().getDigits());
			pricing.setCommodityCode(pricing.getCommodity().getCode());
			pricing.setCommodityName(pricing.getCommodity().getName());
			pricing.setUnit(pricing.getCommodity().getUnit());
		}
		// pricing.PriceContainFee = pricing.Major ?? 0 + pricing.Fee ?? 0;
		/*
		pricing.setQuantity(FormatQuantity(pricing.getQuantity(), pricing.getCommodity(), pricing.getCommodityId()));
		pricing.setQtyPerMainDay(
				FormatQuantity(pricing.getQtyPerMainDay(), pricing.getCommodity(), pricing.getCommodityId()));
		pricing.setQtyPerPremiumDay(
				FormatQuantity(pricing.getQtyPerPremiumDay(), pricing.getCommodity(), pricing.getCommodityId()));
		pricing.setPriceContainFee(pricing.getMajor().add(pricing.getFee()));
		pricing.setPriceContainFee(
				FormatPrice(pricing.getPriceContainFee(), pricing.getCommodity(), pricing.getCommodityId()));
		*/
		// Pricing obj = new Pricing();
		//

		return com.smm.ctrm.util.BeanUtils.copy(pricing);
	}

	@Override
	public PricingRecord SimplifyData(PricingRecord record) {

		if (record == null)
			return null;

		if (record.getLegal() != null)
			record.setLegalName(record.getLegal().getName());

		if (record.getMajorMarket() != null)
			record.setMajorMarketName(record.getMajorMarket().getName());

		if (record.getPremiumMarket() != null)
			record.setPremiumMarketName(record.getPremiumMarket().getName());

		if (record.getLot() != null) {
			record.setFullNo(record.getLot().getFullNo());
			record.setHeadNo(record.getLot().getHeadNo());
			record.setLotNo(record.getLot().getLotNo());
		}

		if (record.getCommodity() != null) {
			record.setCommodityCode(record.getCommodity().getCode());
			record.setCommodityName(record.getCommodity().getName());
			record.setUnit(record.getCommodity().getUnit());

		}

		if (record.getCustomer() != null)
			record.setCustomerName(record.getCustomer().getName());

		// record.PriceContainFee = record.Major ?? 0 + record.Fee ?? 0;
		/*
		record.setQuantity(FormatQuantity(record.getQuantity(), record.getCommodity(), record.getCommodityId()));
		record.setQtyPerMainDay(
				FormatQuantity(record.getQtyPerMainDay(), record.getCommodity(), record.getCommodityId()));
		record.setQtyPerPremiumDay(
				FormatQuantity(record.getQtyPerPremiumDay(), record.getCommodity(), record.getCommodityId()));
		*/
		PricingRecord obj = new PricingRecord();

		try {
			BeanUtilsBean beanUtilsBean = new BeanUtilsBean();
			beanUtilsBean.getConvertUtils()
					.register(new org.apache.commons.beanutils.converters.BigDecimalConverter(null), BigDecimal.class);
			beanUtilsBean.getConvertUtils().register(new org.apache.commons.beanutils.converters.DateConverter(null),
					java.util.Date.class);
			beanUtilsBean.copyProperties(obj, record);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public Pending SimplifyData(Pending pending) {

		if (pending == null)
			return null;
		User approver = pending.getApprover();
		if (approver != null) {
			pending.setApprover(approver);
			pending.setApproverName(approver.getName());
			pending.setApproveTradeDate(null);
			if (approver.getDivision() != null) {
				pending.setApproverDivisionId(approver.getDivision().getId());
				pending.setApproverDivisionName(approver.getDivision().getName());
			}
		}

		if (pending.getFund() != null && pending.getFund().getInvoice() != null) {
			pending.setDueDate(pending.getFund().getInvoice().getDueDate());
			pending.setFullNo(pending.getFund().getLot() == null ? "" : pending.getFund().getLot().getFullNo());
			pending.setInvoiceId(pending.getFund().getInvoiceId());
			pending.setInvoiceNo(pending.getFund().getInvoice().getInvoiceNo());
			pending.setInvoiceAmount(pending.getFund().getInvoice().getAmount());
			pending.setFundAmount(pending.getFund().getAmount());
			pending.setFundQuantity(pending.getFund().getQuantity());
			pending.setCurrency(pending.getFund().getCurrency());
			pending.setLegalName(pending.getFund().getLegal() == null ? "" : pending.getFund().getLegal().getName());
			pending.setCustomerName(
					pending.getFund().getCustomer() == null ? "" : pending.getFund().getCustomer().getName());
			if (pending.getFund().getCustomerTitle() != null)
				pending.setCustomerTitleName(pending.getFund().getCustomerTitle().getName());
			pending.setStatus(pending.getFund().getStatus());
			pending.setInvoiceTradeDate(pending.getFund().getInvoice().getTradeDate());
			pending.setCreatedName(pending.getFund().getCreatedBy());
			pending.setCreatedTime(pending.getFund().getCreatedAt());
			pending.setUpdatedName(pending.getFund().getUpdatedBy());
			pending.setUpdatedTime(pending.getFund().getUpdatedAt());
		}
		if (pending.getFund() != null && pending.getFund().getLot() != null) {
			pending.setFullNo(pending.getFund().getLot() == null ? "" : pending.getFund().getLot().getFullNo());
			pending.setLotId(pending.getFund().getLotId());
			pending.setFundAmount(pending.getFund().getAmount());
			pending.setFundQuantity(pending.getFund().getQuantity());
			pending.setCurrency(pending.getFund().getCurrency());
			pending.setLegalName(pending.getFund().getLegal() == null ? "" : pending.getFund().getLegal().getName());
			pending.setCustomerName(
					pending.getFund().getCustomer() == null ? "" : pending.getFund().getCustomer().getName());
			if (pending.getFund().getCustomerTitle() != null)
				pending.setCustomerTitleName(pending.getFund().getCustomerTitle().getName());
			pending.setStatus(pending.getFund().getStatus());
			pending.setCreatedName(pending.getFund().getCreatedBy());
			pending.setCreatedTime(pending.getFund().getCreatedAt());
			pending.setUpdatedName(pending.getFund().getUpdatedBy());
			pending.setUpdatedTime(pending.getFund().getUpdatedAt());
		}
		if (pending.getContract() != null) {
			if (pending.getContract().getTrader() != null)
				pending.setTraderName(pending.getContract().getTrader().getName());
			if (pending.getContract().getLegal() != null)
				pending.setLegalName(pending.getContract().getLegal().getName());
			pending.setQuantityOfContract(pending.getContract().getQuantity());
			pending.setQuantityOfLots(pending.getContract().getQuantityOfLots());

			pending.setCommodityName(
					pending.getContract().getCommodity() == null ? "" : pending.getContract().getCommodity().getName());
			pending.setProductName(pending.getContract().getProduct());
			pending.setCustomerName(
					pending.getContract().getCustomer() == null ? "" : pending.getContract().getCustomer().getName());
			if (pending.getContract().getCustomerTitle() != null)
				pending.setCustomerTitleName(pending.getContract().getCustomerTitle().getName());
			pending.setHeadNo(pending.getContract().getHeadNo());
			pending.setStatus(pending.getContract().getStatus());
			pending.setCreatedName(pending.getContract().getCreatedBy());
			pending.setCreatedTime(pending.getContract().getCreatedAt());
			pending.setUpdatedName(pending.getContract().getUpdatedBy());
			pending.setUpdatedTime(pending.getContract().getUpdatedAt());
		}

		if (pending.getCustomer() != null) {
			pending.setCustomerName(pending.getCustomer().getName());
			pending.setAddr(pending.getCustomer().getAddress());
			pending.setStatus(pending.getCustomer().getStatus());
			pending.setCreatedName(pending.getCustomer().getCreatedBy());
			pending.setCreatedTime(pending.getCustomer().getCreatedAt());
			pending.setUpdatedName(pending.getCustomer().getUpdatedBy());
			pending.setUpdatedTime(pending.getCustomer().getUpdatedAt());
		}

		if (pending.getAsker() != null) {
			pending.setAskerName(pending.getAsker().getName());
			// add by chenyj 2014.12.21
			if (pending.getAsker().getDivision() != null) {
				pending.setAskerDivisionName(pending.getAsker().getDivision().getName());
				pending.setAskerDivisionId(pending.getAsker().getDivision().getId());
			}
		}

		if (pending.getApprove() != null) {
			pending.setApproveComments(pending.getApprove().getComments());
			pending.setIsApproved(pending.getApprove().getIsApproved());
			pending.setApproveTradeDate(pending.getApprove().getTradeDate());
			if (pending.getApprove().getApprover() != null)
				pending.setApproverName(pending.getApprove().getApprover().getName());
		}
		
		Pending result = com.smm.ctrm.util.BeanUtils.copy(pending);
		if(pending.getReceiptShip() != null) {
			result.setReceiptShip(pending.getReceiptShip());
		}
		if(pending.getInvoice()!=null) {
			pending.setInvoiceNo(pending.getInvoice().getInvoiceNo());
			result.setInvoice(com.smm.ctrm.util.BeanUtils.copy(pending.getInvoice()));
			result.getInvoice().setCustomerName(pending.getInvoice().getCustomer().getName());
		}
		return result;
	}

	@Override
	public Tip SimplifyData(Tip note) {

		if (note == null)
			return null;
		if (note.getLot() != null) {
			note.setFullNo(note.getLot().getFullNo());
			if (note.getLot().getCustomer() != null)
				note.setCustomerName(note.getLot().getCustomer().getName());
		}

		if (note.getCreated() != null)
			note.setCreatedName(note.getCreated().getName());

		Tip obj = new Tip();

		try {
			BeanUtils.copyProperties(obj, note);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public Grade SimplifyData(Grade grade) {

		if (grade == null)
			return null;
		if (grade.getContract() != null) {
			grade.setContractHeadNo(grade.getContract().getHeadNo());
			if (grade.getContract().getCustomer() != null)
				grade.setCustomerName(grade.getContract().getCustomer().getName());
		}
		if (grade.getSpec() != null)
			grade.setSpecName(grade.getSpec().getName());

		Grade obj = new Grade();

		try {
			PropertyUtils.copyProperties(obj, grade);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return grade;
	}

	@Override
	public DischargingPriceDiff SimplifyData(DischargingPriceDiff pricediff) {

		if (pricediff == null)
			return null;
		if (pricediff.getContract() != null) {
			pricediff.setContractHeadNo(pricediff.getContract().getHeadNo());
		}

		DischargingPriceDiff obj = new DischargingPriceDiff();

		try {
			// BeanUtils.copyProperties(obj, pricediff);
			PropertyUtils.copyProperties(obj, pricediff);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public List<DischargingPriceDiff> SimplifyData(List<DischargingPriceDiff> list) {
		// List<DischargingPriceDiff> list = new ArrayList<>();
		// for(DischargingPriceDiff pri : pricediffs){
		// DischargingPriceDiff obj = this.SimplifyData(pri);
		// list.add(obj);
		// }
		// return list;
		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public InvoiceGrade SimplifyData(InvoiceGrade invoiceGrade) {

		if (invoiceGrade == null)
			return null;
		if (invoiceGrade.getGrade() != null)
			if (invoiceGrade.getGrade().getSpec() != null) {
				invoiceGrade.setSpecName(invoiceGrade.getGrade().getSpec().getName());
				invoiceGrade.setSpecId(invoiceGrade.getGrade().getSpec().getId());
			}

		if (invoiceGrade.getInvoice() != null)
			invoiceGrade.setInvoiceNo(invoiceGrade.getInvoice().getInvoiceNo());

		InvoiceGrade obj = new InvoiceGrade();

		try {
			BeanUtils.copyProperties(obj, invoiceGrade);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public Instrument SimplifyData(Instrument instrument) {

		if (instrument == null)
			return null;
		if (instrument.getMarketId() != null) {
			Market market = marketRepository.getOneById(instrument.getMarketId(), Market.class);
			instrument.setMarketName(market.getName());
			instrument.setMarketCode(market.getCode());
			instrument.setCurrency(market.getCurrency());
		}
		if (instrument.getCommodityId() != null) {
			Commodity commodity = commodityRepo.getOneById(instrument.getCommodityId(), Commodity.class);
			instrument.setCommodityName(commodity.getName());
			instrument.setCommodityCode(commodity.getCode());
		}

		/*
		 * Instrument obj = new Instrument();
		 * 
		 * try { ConvertUtils.register(new SqlDateConverter(null),
		 * java.util.Date.class); BeanUtils.copyProperties(obj, instrument); }
		 * catch (IllegalAccessException | InvocationTargetException e) {
		 * e.printStackTrace(); }
		 */

		return instrument;
	}

	@Override
	public Menu SimplifyData(Menu menu) {

		if (menu == null)
			return null;

		Menu obj = new Menu();

		try {
			BeanUtils.copyProperties(obj, menu);
		} catch (IllegalAccessException | InvocationTargetException e) {
			e.printStackTrace();
		}

		return obj;
	}

	@Override
	public boolean CheckHasVerifyData(String userId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsDone", false));
		where.add(Restrictions.eq("ApproverId", userId));

		List<Pending> list = this.pendingRepo.GetQueryable(Pending.class).where(where).toList();

		return (list != null && list.size() > 0);
	}

	@Override
	public String GetVerifyMessages(String userId) {

		DetachedCriteria where = DetachedCriteria.forClass(Pending.class);
		where.add(Restrictions.eq("IsDone", false));
		where.add(Restrictions.eq("ApproverId", userId));

		List<Pending> list = this.pendingRepo.GetQueryable(Pending.class).where(where).toList();

		if (list == null || list.size() == 0)
			return null;

		String message = String.format("您共有{0}个待审事项：", list.size());

		// 待审订单
		List<Pending> contractPending = list.stream()
				.filter(pending -> StringUtils.isEmpty(pending.getCustomerId())
						&& !StringUtils.isEmpty(pending.getContractId()) && StringUtils.isEmpty(pending.getFundId()))
				.collect(Collectors.toList());

		int cpCount = contractPending.size();

		if (cpCount > 0)
			message += String.format("\r\n\r\n\"待审订单\" {0} 条;", cpCount);

		// 待审付款
		List<Pending> fundPending = list.stream()
				.filter(pending -> StringUtils.isEmpty(pending.getCustomerId())
						&& StringUtils.isEmpty(pending.getContractId()) && StringUtils.isEmpty(pending.getFundId()))
				.collect(Collectors.toList());

		int fpCount = fundPending.size();
		if (fpCount > 0)
			message += String.format("\r\n\r\n\"待审付款\" {0} 条;", fpCount);

		// 待审付款
		List<Pending> customerPending = list.stream()
				.filter(pending -> !StringUtils.isEmpty(pending.getCustomerId())
						&& StringUtils.isEmpty(pending.getContractId()) && StringUtils.isEmpty(pending.getFundId()))
				.collect(Collectors.toList());

		int cmpCount = customerPending.size();
		if (cmpCount > 0)
			message += String.format("\r\n\r\n\"待审客户\" {0} 条;", cmpCount);

		message += "\r\n\r\n\r\n\r\n请及时处理！";

		return message;

	}

	@Override
	public MarginFlow SimplifyData(MarginFlow marginFlow) {
		if (marginFlow == null)
			return marginFlow;
		if (marginFlow.getBrokerId() != null) {
			marginFlow.setBrokerName(brokerRepo.getOneById(marginFlow.getBrokerId(), Broker.class).getName());
		}
		return marginFlow;
	}

	@Override
	public void UpdateLotPriceByLotId(String lotId) {
		if (lotId == null)
			return;
		Lot lot = this.lotRepo.getOneById(lotId, Lot.class);
		if (lot == null)
			return;
		// 在此增加一个逻辑：由于lot数量在收/发货之后修改了，导致isDelivered标志出错
		// 更新批次的IsDelivered标志
		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage storage : storages) {
			sumQuantity = storage.getQuantity().add(sumQuantity);
		}
		lot.setQuantityDelivered(FormatQuantity(sumQuantity, lot.getCommodity(), lot.getCommodityId()));
		/*
		 * 使用新的统一方法 lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess
		 * && lot.QuantityDelivered <= lot.QuantityMore);
		 **/
		List<Lot> lots = setDelivery4Lot(lot);

		this.lotRepo.SaveOrUpdate(lot);

		UpdateFuturesSpread(lotId); // 更新期货调期成本（此成本需要作为最终成本的一部分）
		UpdateLotPriceByLotId(lot);
		UpdatePriceAndHedgeFlag4Lot(lotId);
		UpdateDeliveryStatus(lots);// 更新拆分批次的交货状态

	}

	public boolean UpdatePriceAndHedgeFlag4Lot(String lotId) {

		if (StringUtils.isEmpty(lotId))
			return false;

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("Id", lotId));

		List<Lot> list = this.lotRepo.GetQueryable(Lot.class).where(where).toList();

		Lot lot = null;

		if (list != null && list.size() > 0)
			lot = list.get(0);

		return UpdatePriceAndHedgeFlag4Lot2(lot);
	}

	public boolean UpdatePriceAndHedgeFlag4Lot2(Lot lot) {

		if (lot == null)
			return false;

		RefUtil ref = new RefUtil();

		BigDecimal QuantityPriced = BigDecimal.ZERO;
		BigDecimal QuantityHedged = BigDecimal.ZERO;

		boolean IsPriced;

		if (lot.getMajorType().equals(MajorType.Fix)) {
			IsPriced = true;
			QuantityPriced = lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal(); // ??
		} else {
			IsPriced = IsPriced4Lot(lot, ref);
			QuantityPriced = ref.getQuantityPriced();

		}

		// 是否保值完成
		boolean IsHedged = IsHedged4Lot(lot, ref);
		QuantityHedged = ref.getQuantityHedged();
		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		where.add(Restrictions.eq("MajorType", MajorType.Average));
		where.add(Restrictions.eq("IsPriced", false));

		// 是否点价完成
		List<Pricing> pricingList = pricingRepo.GetQueryable(Pricing.class).where(where).toList();

		if (pricingList != null && pricingList.size() > 0)
			IsPriced = false;

		lot.setIsPriced(IsPriced);
		lot.setIsHedged(IsHedged);
		
		lot.setQuantityHedged(FormatQuantity(QuantityHedged,lot.getCommodity(),lot.getCommodityId()));
		lot.setQuantityPriced(FormatPrice(QuantityPriced,lot.getCommodity(),lot.getCommodityId()));

		lotRepo.SaveOrUpdate(lot);

		// //是否拆分批次
		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		// // 拆分批次使用原批次信息
		// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("Id", lot.getSplitFromId()));
		//
		// List<Lot> list =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList();
		//
		// if (list == null || list.size() == 0)
		// return false;
		//
		// Lot lot1 = list.get(0);
		//
		// BigDecimal QuantityPriced = BigDecimal.ZERO;
		//
		// BigDecimal QuantityHedged = BigDecimal.ZERO;
		//
		// boolean IsPriced =
		// lot1.getMajorType().equals(ActionStatus.MajorType_Fix)
		// || IsPriced4Lot(lot1, QuantityPriced);
		//
		// boolean IsHedged = IsHedged4Lot(lot1, QuantityHedged);
		//
		// lot.setIsPriced(IsPriced);
		// lot.setIsHedged(IsHedged);
		//
		// this.lotRepo.SaveOrUpdate(lot);
		//
		// } else {
		//
		// BigDecimal QuantityPriced = BigDecimal.ZERO;
		//
		// BigDecimal QuantityHedged = BigDecimal.ZERO;
		//
		// boolean IsPriced;
		//
		// if (lot.getMajorType().equals(ActionStatus.MajorType_Fix)) {
		// IsPriced = true;
		// QuantityPriced = lot.getQuantityOriginal() == null ?
		// lot.getQuantity() : lot.getQuantityOriginal();
		// } else {
		// IsPriced = IsPriced4Lot(lot, QuantityPriced);
		//
		// }
		//
		// //是否对冲
		// boolean IsHedged = IsHedged4Lot(lot, QuantityHedged);
		//
		// logger.info("----------------------- is Hedged:"+IsHedged);
		//
		//
		// lot.setIsPriced(IsPriced);
		// lot.setIsHedged(IsHedged);
		// lot.setQuantityHedged(QuantityHedged);
		// lot.setQuantityPriced(QuantityPriced);
		//
		// this.lotRepo.SaveOrUpdate(lot);
		//
		// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("SplitFromId", lot.getId()));
		//
		// List<Lot> lotsChild =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList();
		//
		// for (Lot lot1 : lotsChild) {
		// lot1.setIsPriced(IsPriced);
		// lot1.setIsHedged(IsHedged);
		//
		// lot1.setMajor(lot.getMajor());
		// lot1.setPrice(lot.getPrice());
		// lot1.setQuantityPriced(lot.getQuantityPriced());
		//
		// BigDecimal lotPirce = lot.getPrice() == null ? BigDecimal.ZERO :
		// lot.getPrice();
		// BigDecimal lotFee = lot.getFee() == null ? BigDecimal.ZERO :
		// lot.getFee();
		//
		// if (lot.getSpotDirection().equals(ActionStatus.SpotType_Purchase)) {
		//
		// lot1.setFinal(lotPirce.add(lotFee));
		// } else {
		//
		// lot1.setFinal(lotPirce.subtract(lotFee));
		// }
		//
		// this.lotRepo.SaveOrUpdate(lot1);
		// }
		// }

		return true;
	}

	private boolean IsHedged4Lot(Lot lot, RefUtil ref) {

		// logger.info("-----------------------before quantityHedged:
		// "+quantityHedged);

		ref.setQuantityHedged(BigDecimal.ZERO);

		// logger.info("----------------------- in IsHedged4Lot where lot
		// id="+lot.getId());

		if (lot == null)
			return false;

		// Lot lot1 = lot;
		//
		// logger.info("-----------------lot getIsSplitted
		// :"+lot.getIsSplitted());
		//
		//
		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		//
		// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("Id", lot.getSplitFromId()));
		//
		// List<Lot> list =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList();
		//
		// if (list == null || list.size() == 0)
		// return false;
		//
		// lot1 = list.get(0);
		//
		// }

		DetachedCriteria where = DetachedCriteria.forClass(Position.class);
		where.add(Restrictions.eq("LotId", lot.getId()));

		List<Position> positions = this.positionRepo.GetQueryable(Position.class).where(where).toList();

		// logger.info("--------------positions:"+positions.size());

		for (Position p : positions) {

			ref.setQuantityHedged(ref.getQuantityHedged().add(p.getQuantity()));
		}

		// logger.info("-----------------------after quantityHedged:
		// "+quantityHedged);

		// BigDecimal _Quantity = lot1.getQuantityOriginal() == null ?
		// lot1.getQuantity() : lot1.getQuantityOriginal();

		// QuantityMaL lotQuantity =
		// getQuantityMoreorLess(lot1.getMoreOrLessBasis(), _Quantity,
		// lot1.getMoreOrLess());

		BigDecimal hedgeRatio = BigDecimal.ZERO;

		if (lot.getContract() == null) {

			Contract contract = contractRepo.getOneById(lot.getContractId(), Contract.class);

			if (contract != null) {
				hedgeRatio = contract.getHedgeRatio();
			}

		} else {
			hedgeRatio = lot.getContract().getHedgeRatio();
		}

		// 保值比例 (从合同中获取)
		hedgeRatio = hedgeRatio.divide(new BigDecimal(100));

		// 最小保值数量
		BigDecimal ratioLess = lot.getQuantityLess().multiply(hedgeRatio);

		// 最大保值数量
		BigDecimal ratioMore = lot.getQuantityMore();

		// return ratioLess.compareTo(quantityHedged.abs()) <= 0 &&
		// quantityHedged.abs().compareTo(ratioMore) <= 0;

		// logger.info("-----------------------quantityHedged:
		// "+quantityHedged);
		// logger.info("-------------lot.getQuantityLess()
		// "+lot.getQuantityLess()+"
		// hedgeRatio："+hedgeRatio+"----------ratioLess: "+ratioLess);
		// logger.info("-----------------------ratioMore: "+ratioMore);

		return ref.getQuantityHedged().abs().compareTo(ratioLess) >= 0
				&& ref.getQuantityHedged().abs().compareTo(ratioMore) <= 0;
	}

	@Override
	public boolean IsPriced4Lot(Lot lot, RefUtil ref) {

		if (lot == null)
			return false;

		// Lot lot1 = lot;
		//
		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		//
		// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("Id", lot.getSplitFromId()));
		//
		// List<Lot> list =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList();
		//
		// if (list == null || list.size() == 0)
		// return false;
		//
		// lot1 = list.get(0);
		//
		// }

		DetachedCriteria where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		// where.add(Restrictions.isNotNull("LotId"));

		List<Pricing> pricings = this.pricingRepo.GetQueryable(Pricing.class).where(where).toList();

		for (Pricing p : pricings) {
			ref.setQuantityPriced(ref.getQuantityPriced().add(p.getQuantity()));
		}

		BigDecimal _Quantity = lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal();

		QuantityMaL lotQuantity = getQuantityMoreorLess(lot.getMoreOrLessBasis(), _Quantity, lot.getMoreOrLess());

		BigDecimal _QuantityLess = lotQuantity.getQuantityLess() == null ? BigDecimal.ZERO
				: lotQuantity.getQuantityLess();

		BigDecimal _QuantityMore = lotQuantity.getQuantityMore() == null ? BigDecimal.ZERO
				: lotQuantity.getQuantityMore();

		return (_QuantityLess.compareTo(ref.getQuantityPriced().abs()) <= 0)
				&& (ref.getQuantityPriced().abs().compareTo(_QuantityMore) <= 0);

	}

	@Override
	public void UpdateLotPriceByLotId(Lot lot) {

		/*
		 * 业务说明： 早先的设计，只是更新批次的价格。改进后，同时更新批次的 (1) 杂费的冲销标志。IsFeeEliminated。 (2)
		 * 杂费的单价。
		 */
		if (lot == null)
			return;

		DetachedCriteria where = DetachedCriteria.forClass(Fee.class);
		where.add(Restrictions.eq("LotId", lot.getId()));

		// List<Fee> fees =
		// this.feeRepo.GetQueryable(Fee.class).where(where).toList();

		where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		// List<Invoice> funds =
		// this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();//
		// 来自发票的实际费用

		// 按检验批次登记的实际费用
		UpdateLotFees(lot);

		// ----------------------------------------------

		where = DetachedCriteria.forClass(Pricing.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		List<Pricing> pricings = this.pricingRepo.GetQueryable(Pricing.class).where(where).toList();

		List<Pricing> scheduled = pricings.stream().filter(p -> p.getPriceTiming().equals(PriceTiming.Onschedule))
				.collect(Collectors.toList());

		// int symbol = lot.getSpotDirection().equals(SpotType.Purchase) ? 1 :
		// -1; // 正负号

		BigDecimal quantityPriced = BigDecimal.ZERO; 
		for(Pricing price:scheduled)
		{
			// 全部已点价的数量全部已点价的数量
			quantityPriced = quantityPriced.add(price.getQuantity());
		}
		
		BigDecimal amount4Major = BigDecimal.ZERO; 
		BigDecimal amount4Fee = BigDecimal.ZERO; 
		for(Pricing price:pricings)
		{
			 // 按市场价格计算的批次的金额
			amount4Major = amount4Major.add(price.getQuantity().multiply(DecimalUtil.nullToZero(price.getMajor())));
			// 该批次的全部杂费的金额
			amount4Fee = amount4Fee.add(price.getQuantity().multiply(DecimalUtil.nullToZero(price.getFee())));
		}
		amount4Major = amount4Major.add(amount4Fee);
		amount4Major = FormatPrice(amount4Major, lot.getCommodity(), lot.getCommodityId());
		amount4Fee = FormatPrice(amount4Fee, lot.getCommodity(), lot.getCommodityId());
		
		//double quantityPriced = scheduled.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum();// 全部已点价的数量全部已点价的数量

		
		//double amount4Major = pricings.stream()
		//		.mapToDouble(x -> x.getQuantity().multiply(DecimalUtil.nullToZero(x.getMajor())).doubleValue()).sum();
	 	
		// double amount4Premium = pricings.stream()
		// .mapToDouble(x ->
		// x.getQuantity().multiply(x.getPremium()).doubleValue()).sum();
		
		//double amount4Fee = pricings.stream().mapToDouble(x -> x.getQuantity().multiply(x.getFee()).doubleValue())
		//		.sum(); 
		
		// double amount4Price = pricings.stream()
		// .mapToDouble(x ->
		// x.getQuantity().multiply(x.getMajor()).add(x.getPremium()).doubleValue()).sum();//
		// 该批次的商品价格

		//amount4Major += amount4Fee;
		// double amount4Final = amount4Price + (symbol * amount4Fee); //
		// 该批次的最终金额。如果是采购，加上杂费金额；如果是销售，减去杂费金额。

		// 计算市场价格
		if (!lot.getMajorType().equals(MajorType.Fix)) {

			if (quantityPriced.compareTo(BigDecimal.ZERO) == 0) {
				lot.setMajor(BigDecimal.ZERO);
			} else {
				BigDecimal dMajor = DecimalUtil.divideForPrice(amount4Major, quantityPriced);
				lot.setMajor(FormatPrice(dMajor,lot.getCommodity(),lot.getCommodityId()));
				//lot.setMajor(
				//		new BigDecimal(amount4Major).divide(new BigDecimal(quantityPriced), 5, RoundingMode.HALF_EVEN));
			}
		}

		// -----------
		// 点价价格 ( = 市场价格 + 升贴水)
		lot.setPrice(FormatPrice(lot.getMajor().add(lot.getPremium()),lot.getCommodity(),lot.getCommodityId()));

		// 最终的采购或销售价格 ( = 点价价格 +/- 杂费价格)
		BigDecimal lFinal =lot.getSpotDirection().equals(SpotType.Purchase) ? lot.getPrice().add(lot.getFee())
				: lot.getPrice().subtract(lot.getFee());
		lot.setFinal(FormatPrice(lFinal,lot.getCommodity(),lot.getCommodityId()));
		// if (lot.MajorType != MajorType.Fix)
		
		lot.setQuantityPriced(FormatPrice(quantityPriced,lot.getCommodity(),lot.getCommodityId()));
		// if (lot.MajorType != MajorType.Fix)
		// lot.IsPriced = (lot.QuantityOriginal ?? 0) == lot.QuantityPriced;
		lot.setIsPriced(IsPriced4Lot(lot));

		// 增加更新拆分批次的价格
		boolean flag = lot.getIsSplitted() == null ? false : lot.getIsSplitted();
		if (flag && !StringUtils.isEmpty(lot.getSplitFromId())) {
			Lot splitfrom = this.lotRepo.getOneById(lot.getSplitFromId(), Lot.class);

			if (splitfrom != null) {
				lot.setMajor(FormatPrice(splitfrom.getMajor(),lot.getCommodity(),lot.getCommodityId()));
				lot.setPrice(FormatPrice(splitfrom.getPrice(),lot.getCommodity(),lot.getCommodityId()));
				lFinal = lot.getSpotDirection().equals(SpotType.Purchase) ? lot.getPrice().add(lot.getFee())
						: lot.getPrice().subtract(lot.getFee());
				lot.setFinal(FormatPrice(lFinal,lot.getCommodity(),lot.getCommodityId()));
				lot.setQuantityPriced(FormatQuantity(splitfrom.getQuantityPriced(),lot.getCommodity(),lot.getCommodityId()));
				lot.setIsPriced(splitfrom.getIsPriced());
			}
		}
		this.lotRepo.SaveOrUpdate(lot);

		Contract contract = this.contractRepo.getOneById(lot.getContractId(), Contract.class);
		if (contract != null) {
			where = DetachedCriteria.forClass(Grade.class);
			where.add(Restrictions.eq("ContractId", contract.getId()));
			List<Grade> grades = this.gradeRepo.GetQueryable(Grade.class).where(where).toList();

			contract.setGrades(grades);

			where = DetachedCriteria.forClass(DischargingPriceDiff.class);
			where.add(Restrictions.eq("ContractId", contract.getId()));
			List<DischargingPriceDiff> priceDiffs = this.dischargingPriceDiffRepo
					.GetQueryable(DischargingPriceDiff.class).where(where).toList();

			contract.setPriceDiffs(priceDiffs);
		}

		// 预计销售升贴水
		BigDecimal estPremium = BigDecimal.ZERO;

		if (!StringUtils.isEmpty(lot.getEstDischarging())) {

			where = DetachedCriteria.forClass(EstPremiumSetup.class);
			where.add(Restrictions.eq("EstDischarging", lot.getEstDischarging()));
			EstPremiumSetup estPremiumSetup = null;
			List<EstPremiumSetup> estPremiumSetups = this.estPremiumSetupRepo.GetQueryable(EstPremiumSetup.class)
					.where(where).toList();
			if (estPremiumSetups != null && estPremiumSetups.size() > 0) {
				estPremiumSetup = estPremiumSetups.get(0);
			}

			if (estPremiumSetup != null) {
				estPremium = estPremiumSetup.getEstPremium();
			}
		}

		// ----------
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> noteByTest = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		if (noteByTest != null && noteByTest.size() > 0) {

			for (Storage storage : noteByTest) {

				storage.setFee(lot.getFee());
				// storage.RealFee = lot.RealFee ?? 0;
				storage.setEstimateFee(lot.getEstimateFee());

				if (!storage.getIsMannalPremium()) {
					storage.setPremium(lot.getPremium());

					// 重计算升贴水
					if (contract != null) {
						if (contract.getGrades() != null) {
							Grade g = contract.getGrades().stream()
									.filter(x -> x.getSpecId().equals(storage.getSpecId())).findFirst().orElse(null);
							if (g != null) {
								// #region 等级相关升贴水

								BigDecimal diff = BigDecimal.ZERO;

								switch (lot.getDeliveryTerm().trim().toUpperCase()) {
								case "FOB":
									storage.setPremium(g.getFOB().add(diff));
								case "CIF":
									storage.setPremium(g.getCIF().add(diff));
								case "EXW":
									storage.setPremium(g.getEXW().add(diff));
								case "FCA":
									storage.setPremium(g.getFCA().add(diff));
								default:
									break;
								}

								// #endregion
							}
						}
						if (contract.getPriceDiffs() != null && contract.getPriceDiffs().size() > 0) {

							DischargingPriceDiff pdiff = contract.getPriceDiffs().stream()
									.filter(x -> x.getDischarging().equals(lot.getDischarging())).findFirst()
									.orElse(null);
							if (pdiff != null) {
								storage.setPremium(storage.getPremium().add(pdiff.getPriceDiff()));
							}
						}
					}

				}

				// 预计销售升贴水
				storage.setPremium4EstSale(estPremium);
				storage.setMajor(lot.getMajor());

				BigDecimal price = storage.getMajor().add(DecimalUtil.nullToZero(storage.getPremium()));

				BigDecimal final_1 = lot.getSpotDirection().equals(SpotType.Purchase)
						? price.add(DecimalUtil.nullToZero(storage.getRealFee()))
						: price.subtract(DecimalUtil.nullToZero(storage.getRealFee()));

				if (lot.getSpotDirection().equals(SpotType.Purchase))
					final_1 = final_1.subtract(DecimalUtil.nullToZero(storage.getSpread4Initial())
							.subtract(DecimalUtil.nullToZero(storage.getSpread4Qp()))
							.subtract(DecimalUtil.nullToZero(storage.getSpread4Lot())));
				// 20150715 价格增加调期成本
				else
					final_1 = final_1.add(DecimalUtil.nullToZero(storage.getSpread4Initial())
							.add(DecimalUtil.nullToZero(storage.getSpread4Qp()))
							.add(DecimalUtil.nullToZero(storage.getSpread4Lot())));
				// 20150715 价格增加调期成本

				storage.setPrice(final_1);
				storage.setAmount(storage.getQuantity().multiply(storage.getPrice()));
				storage.setCurrency(lot.getCurrency());
				this.storageRepo.SaveOrUpdate(storage);

			}

		}

	}

	@Override
	public void UpdateLotFees(Lot lot) {
		int decimalAccuracy = 5;
		int decimalType = BigDecimal.ROUND_HALF_EVEN;
		if (lot == null)
			return;
		// 费用计算
		DetachedCriteria where = DetachedCriteria.forClass(Fee.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		List<Fee> fees = this.feeRepo.GetQueryable(Fee.class).where(where).toList();
		where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", lot.getId()));
		where.add(Restrictions.ne("PFA", ActionStatus.InvoiceType_Final));
		where.add(Restrictions.ne("PFA", ActionStatus.InvoiceType_Adjust));
		where.add(Restrictions.ne("PFA", ActionStatus.InvoiceType_Provisional));
		where.add(Restrictions.ne("PFA", ActionStatus.InvoiceType_MultiLots));
		where.add(Restrictions.ne("PFA", ActionStatus.InvoiceType_SummaryNote));
		List<Invoice> funds = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		// ---------------
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.isNotNull("LotId"));
		where.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> noteByTest = this.storageRepo.GetQueryable(Storage.class).where(where).toList();
		for (Storage storage : noteByTest) {
			Hibernate.initialize(storage.getSummaryFeesList());
		}

		where = DetachedCriteria.forClass(LC.class);
		where.createAlias("Invoices", "invoices");
		// where.add(Restrictions.gt("Invoices.Count", 0));
		where.add(Restrictions.eq("invoices.LotId", lot.getId()));
		List<LC> lcs = this.lcRepo.GetQueryable(LC.class).where(where).toList();
		List<LC> tmpLcs = new ArrayList<>();
		for (LC lc : lcs) {
			if (lc.getInvoices() != null && lc.getInvoices().size() > 0) {
				tmpLcs.add(lc);
			}
		}
		lcs = tmpLcs;
		List<String> feeCodeType = fees.stream().map(Fee::getFeeCode).collect(Collectors.toList());

		List<Invoice> temp = new ArrayList<>();

		for (Invoice f : funds) {
			if (!feeCodeType.contains(f.getFeeCode()))
				temp.add(f);
		}

		// 计算价格
		//BigDecimal amountDone4Other = new BigDecimal(temp.stream().mapToDouble(i -> i.getAmount().doubleValue()).sum());
		BigDecimal amountDone4Other =  BigDecimal.ZERO;
		for(Invoice invoice:temp){
			amountDone4Other=amountDone4Other.add(invoice.getAmount());
		}

		// 信证中的费用
		BigDecimal lcfee = BigDecimal.ZERO;

		/*where = DetachedCriteria.forClass(Legal.class);
		where.add(Restrictions.eq("Code", "SM"));
		Legal sm = this.legalRepo.GetQueryable(Legal.class).where(where).firstOrDefault();

		for (LC l : lcs) {
			// 计算出该信用证中SM的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfSM = l.getInvoices().stream().filter(i -> i.getLegalId().equals(sm.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesSMOfA = invoicesOfSM.stream()
					.filter(i -> i.getPFA().equals(ActionStatus.InvoiceType_Adjust)).collect(Collectors.toList());
			List<Invoice> removes = new ArrayList<>();
			if (invoicesSMOfA.size() > 0) {
				invoicesOfSM.forEach(i -> {
					invoicesSMOfA.forEach(ia -> {
						if (ia.getId().equals(i.getAdjustId()))
							removes.add(i);
					});
				});
				invoicesOfSM.removeAll(removes);
			}

			// 计算出该信用证中BVI的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfBVI = l.getInvoices().stream().filter(i -> !i.getLegal().equals(sm.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesBVIOfA = invoicesOfBVI.stream()
					.filter(i -> i.getPFA().equals(ActionStatus.InvoiceType_Adjust)).collect(Collectors.toList());
			removes.clear();
			if (invoicesBVIOfA.size() > 0) {
				invoicesOfBVI.forEach(i -> {

					invoicesBVIOfA.forEach(ia -> {

						if (ia.getId().equals(i.getAdjustId()))
							removes.add(i);
					});

				});
				invoicesOfBVI.removeAll(removes);
			}

			// 计算出信用证中该批次的发票
			List<Invoice> invoicesOfLot = l.getInvoices().stream().filter(i -> i.getLotId().equals(lot.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesLotOfA = invoicesOfLot.stream()
					.filter(i -> i.getPFA().equals(ActionStatus.InvoiceType_Adjust)).collect(Collectors.toList());
			removes.clear();
			if (invoicesLotOfA.size() > 0) {
				invoicesOfLot.forEach(i -> {

					invoicesLotOfA.forEach(ia -> {

						if (ia.getId().equals(i.getAdjustId()))
							removes.add(i);
					});

				});
				invoicesOfLot.removeAll(removes);
			}

			BigDecimal invoicesOfLot_sum = new BigDecimal(
					invoicesOfLot.stream().mapToDouble(i -> i.getQuantity().doubleValue()).sum());

			BigDecimal invoicesOfSM_sum = new BigDecimal(
					invoicesOfSM.stream().mapToDouble(i -> i.getQuantity().doubleValue()).sum());

			BigDecimal invoicesOfBVI_sum = new BigDecimal(
					invoicesOfBVI.stream().mapToDouble(i -> i.getQuantity().doubleValue()).sum());

			if (lot.getLegalId().equals(sm.getId())) // 商贸
			{

				// 开证费用+承兑费
				lcfee = lcfee
						.add(l.getKzAmount().multiply(invoicesOfLot_sum).divide(invoicesOfSM_sum, decimalAccuracy,
								decimalType))
						.add(l.getCdAmount().multiply(invoicesOfLot_sum).divide(invoicesOfSM_sum, decimalAccuracy,
								decimalType));

			} else // bvi
			{
				// 贴现费 + 议付费
				lcfee = lcfee
						.add(l.getTxAmount().multiply(invoicesOfLot_sum).divide(invoicesOfBVI_sum, decimalAccuracy,
								decimalType))
						.add(l.getYfAmount().multiply(invoicesOfLot_sum).divide(invoicesOfBVI_sum, decimalAccuracy,
								decimalType));
			}
		}*/

		// 按批次检验费用,如果预估费用中没有该类型的检验费，则费用归集到其它类型

		BigDecimal sumAmount = BigDecimal.ZERO, sumTransAmount = BigDecimal.ZERO, sumInsuAmount = BigDecimal.ZERO,
				sumCost = BigDecimal.ZERO, sumOhters = BigDecimal.ZERO;
		BigDecimal sumBankDocFee = BigDecimal.ZERO, sumBuyDocFee = BigDecimal.ZERO, sumDisputeFine = BigDecimal.ZERO,
				sumHedgeFee = BigDecimal.ZERO;
		// decimal? sumTransAmountEstimated = 0M; //bvi采购中，运输费
		List<Storage> storages = new ArrayList<>();
		for (Storage notefee : noteByTest) {
			Storage notefeeCopy = noteByTest.stream().filter(x -> x.getId().equals(notefee.getId()))
					.collect(Collectors.toList()).get(0);
			notefeeCopy.setRealFee(BigDecimal.ZERO);
			if (notefee.getSummaryFeesList() != null) {
				for (SummaryFees summaryFee : notefee.getSummaryFeesList()) {

					where = DetachedCriteria.forClass(Invoice.class);
					where.add(Restrictions.eq("Id", summaryFee.getInvoiceId()));
					where.add(Restrictions.eq("PFA", ActionStatus.InvoiceType_SummaryNote));
					Invoice invoice = this.invoiceRepo.GetQueryable(Invoice.class).where(where).firstOrDefault();

					if (invoice != null) {
						switch (invoice.getFeeCode()) {
						case FeeCode.Test: // 检验费
							sumAmount = sumAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.Test)) {

								amountDone4Other = amountDone4Other.add(sumAmount);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.Transportation:// 运输费
							sumTransAmount = sumTransAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.Transportation)) {

								amountDone4Other = amountDone4Other.add(sumTransAmount);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.Insurance:// 保险费
							sumInsuAmount = sumInsuAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.Insurance)) {

								amountDone4Other = amountDone4Other.add(sumInsuAmount);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.Cost:// 资金成本
							sumCost = sumCost.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.Cost)) {

								amountDone4Other = amountDone4Other.add(sumCost);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.BankDocumentsFee:// 银行文件费
							sumBankDocFee = sumBankDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.BankDocumentsFee)) {

								amountDone4Other = amountDone4Other.add(sumBankDocFee);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.BuyDocumentsFee:// 采购文件费
							sumBuyDocFee = sumBuyDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.BuyDocumentsFee)) {

								amountDone4Other = amountDone4Other.add(sumBuyDocFee);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.DisputeFine:// 争议罚款费
							sumDisputeFine = sumDisputeFine.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.DisputeFine)) {

								amountDone4Other = amountDone4Other.add(sumDisputeFine);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						case FeeCode.HedgeFee:// 套保费
							sumHedgeFee = sumHedgeFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							if (!feeCodeType.contains(FeeCode.HedgeFee)) {

								amountDone4Other = amountDone4Other.add(sumHedgeFee);
								notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
							}
						default:// 其他所有费用
							sumOhters = sumOhters.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
							break;
						}

					}
				}
			}

			storages.add(notefeeCopy);
		}

		// --------------
		for (Fee fee : fees) {

			// 计算价格

			BigDecimal funds_sum = new BigDecimal(0);

			for (Invoice x : funds) {
				if (x.getFeeCode().equals(fee.getFeeCode())) {
					funds_sum = funds_sum.add(x.getAmount());
				}
			}
			fee.setAmountDone(funds_sum);

			if (fee.getFeeCode().equals(FeeCode.Cost)) // 资金成本（实际发生来自信用证）
			{
				fee.setAmountDone(fee.getAmountDone().add(lcfee.add(sumCost)));
			} else if (fee.getFeeCode().equals(FeeCode.Test)) // 检验费时，实际费需加上按检验批次录入的检验费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumAmount));
			} else if (fee.getFeeCode().equals(FeeCode.Other)) // 其他费用
			{
				if (fees.stream().filter(x -> x.getFeeCode().equals(FeeCode.Cost)).collect(Collectors.toList())
						.get(0) == null) {

					amountDone4Other = amountDone4Other.add(lcfee);
					fee.setAmountDone(fee.getAmountDone().add(sumAmount.add(amountDone4Other)));
				}
			} else if (fee.getFeeCode().equals(FeeCode.Transportation)) // 运输费用
			{
				fee.setAmountDone(fee.getAmountDone().add(sumTransAmount));
			} else if (fee.getFeeCode().equals(FeeCode.Insurance)) // 保险费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumInsuAmount));
			} else if (fee.getFeeCode().equals(FeeCode.BankDocumentsFee)) // 银行文件费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumBankDocFee));
			} else if (fee.getFeeCode().equals(FeeCode.BuyDocumentsFee)) // 采购文件费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumBuyDocFee));
			} else if (fee.getFeeCode().equals(FeeCode.DisputeFine)) // 争议罚款费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumDisputeFine));
			} else if (fee.getFeeCode().equals(FeeCode.HedgeFee)) // 套保费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumHedgeFee));
			}
			this.feeRepo.SaveOrUpdate(fee);
		}

		// 更批次对应的交付明细的实际费用
		BigDecimal realFeeOfStorage = BigDecimal.ZERO;
		String[] feeCodes = new String[] { FeeCode.Cost, FeeCode.Test, FeeCode.Transportation, FeeCode.Insurance,
				FeeCode.BankDocumentsFee, FeeCode.BuyDocumentsFee, FeeCode.DisputeFine, FeeCode.HedgeFee };
		for (String fee : feeCodes) {
			BigDecimal feeAmountDone = new BigDecimal(funds.stream().filter(x -> x.getFeeCode().equals(fee))
					.mapToDouble(x -> x.getAmount().doubleValue()).sum());

			if (Objects.equals(fee, FeeCode.Cost)) // 资金成本（实际发生来自信用证）
			{
				realFeeOfStorage = realFeeOfStorage
						.add(lcfee.add(feeAmountDone).divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.Test)) // 检验费时，实际费需加上按检验批次录入的检验费
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.Transportation)) // 运输费用
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.Insurance)) // 保险费
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			}
			if (Objects.equals(fee, FeeCode.HedgeFee)) // 套保费用
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.BankDocumentsFee)) // 银行文件费用
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.BuyDocumentsFee)) // 采购文件费用
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			} else if (Objects.equals(fee, FeeCode.DisputeFine)) // 争议罚款费
			{
				realFeeOfStorage = realFeeOfStorage
						.add(feeAmountDone.divide(lot.getQuantity(), decimalAccuracy, decimalType));
			}
		}

		for (Storage s : storages) {
			Storage tempStorage = this.storageRepo.getOneById(s.getId(), Storage.class);
			BigDecimal realFee = s.getRealFee().add(realFeeOfStorage);

			tempStorage.setRealFee(lot.getIsDelivered() == null ? (realFee.multiply(lot.getQuantity())
					.divide(lot.getQuantityDelivered(), decimalAccuracy, decimalType)) : realFee);

			this.storageRepo.SaveOrUpdate(tempStorage);
		}

		// 批次数量具体是多少
		BigDecimal qty = lot.getIsDelivered() == null ? lot.getQuantityDelivered() : lot.getQuantity();

		// 各项杂费是否已经全部冲销，决定批次的IsFeeEliminated标志。IsFeeEliminated为true，是发票可以盈亏结算的前提条件之一。
		lot.setIsFeeEliminated(
				fees.stream().filter(x -> !x.getIsEliminated()).collect(Collectors.toList()).size() == 0);

		// ***********************************
		// 无论IsFeeEliminated是什么，计算出批次的杂费单价，均为/MT
		BigDecimal sumFee = BigDecimal.ZERO;

		for (Fee f : fees) {
			sumFee = sumFee.add(f.getIsEliminated() ? f.getAmountDone() : f.getAmountEstimated());
		}

		// #endregion
		lot.setEstimateFee(new BigDecimal(fees.stream().mapToDouble(x -> x.getAmountEstimated().doubleValue()).sum())
				.divide(qty, decimalAccuracy, decimalType)); // 预估
		lot.setFee(sumFee.divide(qty, decimalAccuracy, decimalType));
		lot.setRealFee(new BigDecimal(fees.stream().mapToDouble(x -> x.getAmountDone().doubleValue()).sum()).divide(qty,
				decimalAccuracy, decimalType)); // 实际发生

	}

	@Override
	public boolean UpdateLotFeesByLotId(String lotid) {

		if (lotid == null)
			return false;

		Lot lot = this.lotRepo.getOneById(lotid, Lot.class);

		UpdateLotFees(lot);

		return true;
	}

	@Override
	public Integer GetSequenceIndex(String code) {

		return GetSequenceIndex(code, false);
	}

	@Override
	public Integer GetSequenceIndex(String code, Boolean UseConfirm) {
		
		if (UseConfirm) {
			return identityGetNewIdWithConfirm(code);
		}else{
			return identityGetNewId(code);
		}

		/*String name = "proc_IdentityGetNewId"; 

		if (UseConfirm) {
			name = "proc_IdentityGetNewId_WithConfirm";
		}
		
		Map<String, Object> pmap = new LinkedHashMap<>();

		pmap.put("pTableName", code);

		Map<String, String> omap = new LinkedHashMap<>();

		omap.put("pIdentityId", ParameterType.DECIMAL.getCode());

		Map<String, Object> map = this.lotRepo.ExecuteProcedureSqlOutMap(name, pmap, omap);

		if (map == null || !map.containsKey("pIdentityId"))
			return -1;

		BigDecimal tmpd = new BigDecimal(-1);

		tmpd = new BigDecimal(String.valueOf(map.get("pIdentityId")));

		return tmpd.intValue();*/
	}
	
	
	public Integer identityGetNewId(String code){
		String sql="select lastid from [Basis].[sys_sequence] where code ='"+code+"'";
		Object obj=lotRepo.ExecuteScalarSql(sql);
		Integer lastid=1;
		if(obj!=null){
			lastid=Integer.valueOf(String.valueOf(obj));
			String sqlUpdate="update [Basis].[sys_sequence] set lastid="+(lastid+1)+" where code='"+code+"'";
			lotRepo.ExecuteCorrectlySql3(sqlUpdate);
		}else{
			String sqlInsert="insert into [Basis].[sys_sequence] (code,lastid) values('"+code+"',2)";
			lotRepo.ExecuteCorrectlySql3(sqlInsert);
		}
		return lastid;
	}
	
	public Integer identityGetNewIdWithConfirm(String code){
		String sql="select lastid from Basis.SYS_SEQUENCE_RESULT where code ='"+code+"' and Confirmed = 0 order by lastid";
		List<String> obj=lotRepo.ExecuteCorrectlySql2(sql);
		if(obj!=null&&obj.size()>0){
			return Integer.valueOf(String.valueOf(obj.get(0)));
		}else{
			String sql2="select lastid from [Basis].[sys_sequence] where code ='"+code+"'";
			Object lastid=lotRepo.ExecuteScalarSql(sql2);
			Integer newLastid=1;
			if(lastid!=null){
				newLastid=Integer.valueOf(String.valueOf(lastid));
				String sqlUpdate="update [Basis].[sys_sequence] set lastid="+(newLastid+1)+" where code='"+code+"'";
				lotRepo.ExecuteCorrectlySql3(sqlUpdate);
			}else{
				String sqlInsert="insert into [Basis].[sys_sequence] (code,lastid) values('"+code+"',2)";
				lotRepo.ExecuteCorrectlySql3(sqlInsert);
			}
			String sqlSysSequenceResult="insert into [Basis].[SYS_SEQUENCE_RESULT] (code,lastid,Confirmed) values('"+code+"',"+newLastid+",0)";
			lotRepo.ExecuteCorrectlySql3(sqlSysSequenceResult);
			return newLastid;
		}
	}

	@Override
	public void ConfirmSequenceIndex(SysSequence sequence) {

		ProcIdentityGetConfirmId(sequence.getIdentityId(),sequence.getCode());
		/*
		String name = "[dbo].[proc_IdentityGetConfirmId]";

		Map<String, Object> pmap = new LinkedHashMap<String, Object>();

		pmap.put("pIdentityId", sequence.getIdentityId());
		pmap.put("pTableName", sequence.getCode());

		this.lotRepo.ExecuteProcedureSql2(name, pmap);
	 	*/
	}
	
	public void ProcIdentityGetConfirmId(BigDecimal identityId,String tableName)
	{
		String hql= "update SysSequenceResult set Confirmed = 1 where code = :tableName and Confirmed = 0 and lastid = :identityId";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("tableName", tableName);
		params.put("identityId", identityId);
		this.lotRepo.ExecuteHql2Update(hql, params);
	}

	@Override
	public boolean UpdateFuturesSpread(String LotId) {

		if (LotId == null)
			return false;

		Lot lot = this.lotRepo.getOneById(LotId, Lot.class);

		if (lot == null)
			return false;

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", LotId));
		List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		if (storages != null && storages.size() > 0) {

			List<Storage> spreadStorages = FuturesSpread2(lot, storages);

			if (spreadStorages != null && spreadStorages.size() > 0)
				spreadStorages.forEach(this.storageRepo::SaveOrUpdate);
		}

		return true;
	}

	/// <summary>
	/// Initial spread:合同创建时QP和预计销售日期的价差。 取行情的日期：合同签订日期
	/// QP spread：合同创建时QP和最终QP的价差。 取行情的日期：QP修改日期的前一天的行情数据。
	/// Assignment spread（Lot Spread)：合同创建时预计销售日和货物装运日期的价差。
	/// 取行情的日期：装运日期录入那天的前一天的行情数据。
	/// </summary>
	/// <param name="lot"></param>
	/// <param name="storages"></param>
	/// <returns></returns>
	private List<Storage> FuturesSpread2(Lot lot, List<Storage> storages) {

		if (lot.getLegalId() != null) {
			lot.setLegal(this.legalRepo.getOneById(lot.getLegalId(), Legal.class));
		}
		if (lot.getContractId() != null) {

			lot.setContract(this.contractRepo.getOneById(lot.getContractId(), Contract.class));
		}

		if (lot.getContract() != null && lot.getLegal() != null && storages != null && storages.size() > 0) {
			// #region 计算Spread

			// var legals = Repository.GetQueryable<Legal>();
			// var Sm = legals.First(x => x.Code == "SM");
			// var bvi = legals.First(x => x.Code == "SB");

			BigDecimal initialSpread = BigDecimal.ZERO, QpSpread = BigDecimal.ZERO;

			// 合同创建时QP
			DetachedCriteria where = DetachedCriteria.forClass(QPRecord.class);
			where.add(Restrictions.eq("LotId", lot.getId()));
			List<QPRecord> QpRecords = this.qpRecordRepo.GetQueryable(QPRecord.class).where(where)
					.OrderBy(Order.desc("TradeDate")).toList();

			// QPRecord Qp4Create = null;
			Date QPDate4Original = lot.getQP();
			QPRecord Qp4Create = null;
			if (QpRecords.size() > 0) {
				Qp4Create = QpRecords.get(0);
			}
			if (Qp4Create != null)
				QPDate4Original = Qp4Create.getCurrentQP();

			// #region InitialSpread
			where = DetachedCriteria.forClass(Reuter.class);
			where.add(Restrictions.eq("PromptDate", QPDate4Original));
			where.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(lot.getContract().getTradeDate(), -1)));
			List<Reuter> ruter4inital_1_list = this.reuterRepo.GetQueryable(Reuter.class).where(where).toList();

			where = DetachedCriteria.forClass(Reuter.class);
			where.add(Restrictions.eq("PromptDate", lot.getEstimateSaleDate()));
			where.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(lot.getContract().getTradeDate(), -1)));

			List<Reuter> ruter4inital_2_list = this.reuterRepo.GetQueryable(Reuter.class).where(where).toList();

			if (ruter4inital_1_list != null && ruter4inital_1_list.size() > 0 && ruter4inital_2_list != null
					&& ruter4inital_2_list.size() > 0) {

				Reuter ruter4inital_1 = ruter4inital_1_list.get(0);
				Reuter ruter4inital_2 = ruter4inital_2_list.get(0);
				initialSpread = ruter4inital_2.getPrice().subtract(ruter4inital_1.getPrice());
			}
			// #region QPSpread

			QpSpread = new BigDecimal(QpRecords.stream().mapToDouble(x -> x.getPriceDiff().doubleValue()).sum());

			// #region Assignment Spread(lot Spread)

			// var storages4lotSpread =
			// Repository.GetQueryable<Storage>().Where(x => x.LotId == lot.Id);
			for (Storage storage : storages) {
				storage.setSpread4Initial(initialSpread);
				storage.setSpread4Qp(QpSpread);
				if (storage.getBLDate4Update() != null) {
					where = DetachedCriteria.forClass(Reuter.class);
					where.add(Restrictions.eq("PromptDate", QPDate4Original));
					where.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(storage.getBLDate4Update(), -1)));
					List<Reuter> ruter4Assignment_1_list = this.reuterRepo.GetQueryable(Reuter.class).where(where)
							.toList();

					where = DetachedCriteria.forClass(Reuter.class);
					where.add(Restrictions.eq("PromptDate", lot.getEstimateSaleDate()));
					where.add(Restrictions.eq("TradeDate", DateUtil.getDiffDate(storage.getBLDate4Update(), -1)));
					List<Reuter> ruter4Assignment_2_list = this.reuterRepo.GetQueryable(Reuter.class).where(where)
							.toList();

					if (ruter4Assignment_1_list != null && ruter4Assignment_1_list.size() > 0
							&& ruter4Assignment_2_list != null && ruter4Assignment_2_list.size() > 0) {
						Reuter ruter4Assignment_1 = ruter4Assignment_1_list.get(0);
						Reuter ruter4Assignment_2 = ruter4Assignment_2_list.get(0);
						storage.setSpread4Lot(ruter4Assignment_2.getPrice().subtract(ruter4Assignment_1.getPrice()));
					}
				}

				// 价格
				if (lot.getContract().getSpotDirection().equals(SpotType.Purchase)) {
					// 点价价格（含升贴水） + 费用 -调期成本
					storage.setPrice(
							DecimalUtil.nullToZero(storage.getMajor()).add(DecimalUtil.nullToZero(storage.getPremium()))
									.add(DecimalUtil.nullToZero(storage.getRealFee()))
									.subtract(DecimalUtil.nullToZero(storage.getSpread4Initial()))
									.subtract(DecimalUtil.nullToZero(storage.getSpread4Qp()))
									.subtract(DecimalUtil.nullToZero(storage.getSpread4Lot())));
				} else if (lot.getContract().getSpotDirection().equals(SpotType.Sell)) {
					// 点价价格（含升贴水） - 费用 +调期成本
					storage.setPrice((storage.getMajor() != null ? storage.getMajor() : BigDecimal.ZERO)
							.add(storage.getPremium() != null ? storage.getPremium() : BigDecimal.ZERO)
							.subtract(storage.getRealFee() != null ? storage.getRealFee() : BigDecimal.ZERO)
							.add(storage.getSpread4Initial() != null ? storage.getSpread4Initial() : BigDecimal.ZERO)
							.add(storage.getSpread4Qp() != null ? storage.getSpread4Qp() : BigDecimal.ZERO)
							.add(storage.getSpread4Lot() != null ? storage.getSpread4Lot() : BigDecimal.ZERO));
				}
			}

		}
		return storages;

	}

	@Override
	public QuantityMaL getQuantityMoreorLess(String MoreOrLessBasis, BigDecimal _Quantity, BigDecimal _MoreOrLess) {

		BigDecimal Quantity = _Quantity == null ? BigDecimal.ZERO : _Quantity;

		BigDecimal MoreOrLess = _MoreOrLess == null ? BigDecimal.ZERO : _MoreOrLess;

		QuantityMaL quantity = new QuantityMaL();
		quantity.setQuantity(_Quantity);
		quantity.setMoreOrLessBasis(MoreOrLessBasis);
		quantity.setMoreOrLess(_MoreOrLess);

		if (MoreOrLessBasis.equals("OnQuantity")) {
			quantity.setQuantityLess(Quantity.subtract(MoreOrLess));
			quantity.setQuantityMore(Quantity.add(MoreOrLess));
		} else if (MoreOrLessBasis.equals("OnPercentage")) {
			quantity.setQuantityLess(
					Quantity.multiply(new BigDecimal(1).subtract(MoreOrLess.divide(new BigDecimal(100)))));
			quantity.setQuantityMore(Quantity.multiply(new BigDecimal(1).add(MoreOrLess.divide(new BigDecimal(100)))));
		}
		return quantity;
	}

	@Override
	public void UpdateDeliveryStatus(List<Lot> lots) {

		if (lots == null)
			return;

		lots.forEach(lot1 -> {

			Lot dbLot = this.lotRepo.getOneById(lot1.getId(), Lot.class);

			if (dbLot != null) {

				lot1.setIsDelivered(dbLot.getIsDelivered());

				this.lotRepo.SaveOrUpdate(lot1);
			}

		});
	}

	@Override
	public void UpdateInvoiceStatus(List<Lot> lots) {

		if (lots == null)
			return;

		lots.stream().distinct().collect(Collectors.toList()).forEach(lot1 -> {

			Lot dbLot = this.lotRepo.getOneById(lot1.getId(), Lot.class);

			if (dbLot != null) {

				lot1.setIsInvoiced(dbLot.getIsInvoiced());

				this.lotRepo.SaveOrUpdate(lot1);
			}

		});

	}

	@Override
	public QuantityMaL CalculateQuantityOfLotDeliveryed(Lot lot) {

		if (lot == null)
			return null;
		Lot originalLot = lot;

		if (lot.getIsSplitted() != null && lot.getIsSplitted()) {
			originalLot = this.lotRepo.getOneById(lot.getSplitFromId(), Lot.class);// 原始批次
		}
		if (originalLot == null)
			return null;

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		List<Lot> lots2 = this.lotRepo.GetQueryable(Lot.class).where(where).toList(); // 拆分批次，//注意包含全部拆分批次

		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;

		storages.forEach(s -> sumQuantity.add(s.getQuantity()));

		for (Lot lot1 : lots2) {

			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", lot1.getId()));
			where.add(Restrictions.eq("IsHidden", false));

			List<Storage> storages1 = this.storageRepo.GetQueryable(Storage.class).where(where).toList();
			storages1.forEach(s1 -> sumQuantity.add(s1.getQuantity()));
		}

		BigDecimal _Quantity = originalLot.getQuantityOriginal() == null ? originalLot.getQuantity()
				: originalLot.getQuantityOriginal();

		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(), _Quantity,
				originalLot.getMoreOrLess());

		lotQuantity.setQuantityDeliveryed(sumQuantity);

		return lotQuantity;
	}

	@Override
	public Invoice CalculateQuantityOfInvoice(Lot lot) {

		if (lot == null || lot.getId() == null)
			return null;

		Lot originalLot = lot;

		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		// originalLot = this.lotRepo.getOneById(lot.getSplitFromId(),
		// Lot.class);// 原始批次
		// }

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		// List<Lot> lots2 =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList(); //
		// 拆分批次，//注意包含全部拆分批次

		// List<String> lots2Ids =
		// lots2.stream().map(Lot::getId).collect(Collectors.toList());

		where = DetachedCriteria.forClass(Invoice.class);

		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.eq("FeeCode", FeeCode.Goods));
		where.add(Restrictions.not(Restrictions.and(Restrictions.eq("PFA", ActionStatus.InvoiceType_Provisional),
				Restrictions.isNotNull("AdjustId"))));
		// where.add(Restrictions.ne("PFA",
		// ActionStatus.InvoiceType_Provisional));
		// where.add(Restrictions.isNull("AdjustId"));

		List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Invoice invoice : invoices) {
			sumQuantity = sumQuantity.add(invoice.getQuantity());
		}
		Invoice allInvoice = new Invoice();
		allInvoice.setQuantity(sumQuantity);
		return allInvoice;

	}

	@Override
	public void GenerateBrokerPosition() {

		/*
		 * // 获取组织编号 List<Position4Broker> position4BrokerList =
		 * this.position4BrokerRepo.GetList(Position4Broker.class);
		 * 
		 * List<String> OriginalIdList = new ArrayList<>();
		 * 
		 * if (position4BrokerList != null && position4BrokerList.size() > 0) {
		 * 
		 * OriginalIdList = position4BrokerList.stream().map(x ->
		 * x.getOriginalId()).collect(Collectors.toList()); }
		 * 
		 * // 按组织编号查询 DetachedCriteria dc =
		 * DetachedCriteria.forClass(Position.class);
		 * dc.add(Restrictions.eq("IsVirtual", false));
		 * dc.add(Restrictions.eq("IsSplitted", false));
		 * dc.add(Restrictions.not(Restrictions.in("Id", OriginalIdList)));
		 * 
		 * List<Position> positions =
		 * this.positionRepo.GetQueryable(Position.class).where(dc).toList();
		 */
		String sqlPb = "Select OriginalId from Physical.Position4Broker";
		List<String> pbIds = this.position4BrokerRepo.ExecuteCorrectlySql2(sqlPb);
		if (pbIds != null && pbIds.size() > 0) {
			String pIds = "";
			for (String s : pbIds) {
				pIds += "'" + s + "',";
			}
			pIds = pIds.substring(0, pIds.length() - 1);

			String sql = "Select * from Physical.Position where IsVirtual = 0 and IsSplitted = 0 and Id not in (" + pIds
					+ ")";
			List<Position> positions = this.positionRepo.ExecuteCorrectlySql(sql, Position.class);

			if (positions != null && positions.size() > 0) {
				for (Position position : positions) {
					Position4Broker position4Broker = new Position4Broker();

					PropertyUtilsBean pub = new PropertyUtilsBean();

					try {
						pub.copyProperties(position4Broker, position);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}

					position4Broker.setId(null);
					position4Broker.setQuantity(position.getQuantityOriginal()); // 得到头寸的原始数量
					position4Broker.setQuantityUnSquared(position.getQuantityOriginal());
					position4Broker.setIsSquared(false);
					position4Broker.setOriginalId(position.getId());

					this.position4BrokerRepo.SaveOrUpdate(position4Broker);
				}
			}

		}

	}

	@Override
	public void Square() {

		GenerateBrokerPosition();// 先生成一次原始头寸数据

		DetachedCriteria where = DetachedCriteria.forClass(Position4Broker.class);
		where.add(Restrictions.eq("IsSquared", false));
		List<Position4Broker> positions = this.position4BrokerRepo.GetQueryable(Position4Broker.class).where(where)
				.toList();

		List<Broker> brokers = this.brokerRepo.GetList(Broker.class);

		for (Broker broker : brokers) {
			List<Position4Broker> longs = positions.stream()
					.filter(pn -> pn.getLS().equals(ActionStatus.LS_LONG) && pn.getBrokerId().equals(broker.getId()))
					.collect(Collectors.toList());

			List<Position4Broker> shorts = positions.stream()
					.filter(pn -> pn.getLS().equals(ActionStatus.LS_SHORT) && pn.getBrokerId().equals(broker.getId()))
					.collect(Collectors.toList());

			Square4Broker(longs, shorts);
		}
	}

	@Override
	public BigDecimal FormatQuantity(BigDecimal Quantity, Commodity commodity, String commodityId) {

		// if (commodity == null && commodityId != null)
		// commodity = this.commodityRepo.getOneById(commodityId,
		// Commodity.class);
		//
		// if (commodity != null && commodity.getDigits() >= 0) {
		// return (Quantity == null ? BigDecimal.ZERO :
		// Quantity).setScale(commodity.getDigits(),RoundingMode.HALF_EVEN);
		// }
		//
		// return FormatQuantity(Quantity); // 默认3位小数

		if (Quantity == null) {

			return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_EVEN);

		} else {

			int temp = (commodity != null && commodity.getDigits() >= 0) ? commodity.getDigits() : 0;

			return Quantity.setScale(temp, RoundingMode.HALF_EVEN);
		}
	}

	@Override
	public BigDecimal FormatQuantity(BigDecimal Quantity) {

		if (Quantity == null)
			return BigDecimal.ZERO.setScale(3, RoundingMode.HALF_EVEN);

		return Quantity.setScale(3, RoundingMode.HALF_EVEN); // 默认3位小数
	}

	@Override
	public BigDecimal FormatPrice(BigDecimal Price, Commodity commodity, String commodityId) {

		if (commodity == null && commodityId != null)
			commodity = this.commodityRepo.getOneById(commodityId, Commodity.class);

		if (Price.compareTo(BigDecimal.ZERO) > 0 && commodity != null && commodity.getDigits4Price() >= 0) {

			return Price.setScale(commodity.getDigits4Price(), RoundingMode.HALF_EVEN); // 按数据库设置，保留几位小数
		}
		return FormatQuantity(Price); // 默认2位小数

	}

	@Override
	public BigDecimal FormatPrice(BigDecimal Price) {

		if (Price == null)
			return BigDecimal.ZERO;

		return Price.setScale(2, RoundingMode.HALF_EVEN); // 默认2位小数
	}

	@Override
	public ActionResult<String> UpdateLotIsFunded(String lotid) {

		try {

			Lot curLot = this.lotRepo.getOneById(lotid, Lot.class);

			if (curLot == null)
				throw new Exception("未得到批次信息");

			if (curLot.getIsInvoiced()) {
				// 单批次发票
				DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
				where.add(Restrictions.eq("LotId", lotid));
				where.add(Restrictions.eq("Is4MultiLots", false));
				where.add(Restrictions.or(Restrictions.eq("PFA", "F"), Restrictions.eq("PFA", "A")));
				List<Invoice> invoiceDoneForSingle = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

				if (invoiceDoneForSingle.size() > 0 && invoiceDoneForSingle.stream().filter(x -> !x.getIsExecuted())
						.collect(Collectors.toList()).size() > 0) {
					curLot.setIsFunded(false);
					this.lotRepo.SaveOrUpdate(curLot);

					return new ActionResult<>(false, "收/付款未完成");

				}
				// 多批次发票
				List<Invoice> invoiceDoneForMulti = GetMultilotInvoicesByLotId(lotid).stream()
						.filter(x -> x.getPFA().equals("F") || x.getPFA().equals("A")).collect(Collectors.toList());

				if (invoiceDoneForMulti.size() > 0 && invoiceDoneForSingle.stream().filter(x -> !x.getIsExecuted())
						.collect(Collectors.toList()).size() > 0) {
					curLot.setIsFunded(false);
					this.lotRepo.SaveOrUpdate(curLot);
					return new ActionResult<>(false, "收/付款未完成");
				}

				if (invoiceDoneForSingle.size() > 0 || invoiceDoneForMulti.size() > 0) {
					curLot.setIsFunded(true);
					this.lotRepo.SaveOrUpdate(curLot);
					return new ActionResult<>(true, null);
				}
			}

			return new ActionResult<>(false, "收/付款未完成");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public ActionResult<Lot> txUpdateLotIsFunded(String lotid) {

		try {

			Lot curLot = this.lotRepo.getOneById(lotid, Lot.class);

			if (curLot == null)
				throw new Exception("未得到批次信息");

			if (curLot.getIsInvoiced()) {
				// 单批次发票
				DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
				where.add(Restrictions.eq("LotId", lotid));
				where.add(Restrictions.eq("Is4MultiLots", false));
				where.add(Restrictions.or(Restrictions.eq("PFA", "F"), Restrictions.eq("PFA", "A")));
				List<Invoice> invoiceDoneForSingle = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

				if (invoiceDoneForSingle.size() > 0 && invoiceDoneForSingle.stream().filter(x -> !x.getIsExecuted())
						.collect(Collectors.toList()).size() > 0) {
					curLot.setIsFunded(false);
					// this.lotRepo.SaveOrUpdate(curLot);

					return new ActionResult<>(false, "收/付款未完成", curLot);

				}
				// 多批次发票
				List<Invoice> invoiceDoneForMulti = GetMultilotInvoicesByLotId(lotid).stream()
						.filter(x -> x.getPFA().equals("F") || x.getPFA().equals("A")).collect(Collectors.toList());

				if (invoiceDoneForMulti.size() > 0 && invoiceDoneForSingle.stream().filter(x -> !x.getIsExecuted())
						.collect(Collectors.toList()).size() > 0) {
					curLot.setIsFunded(false);
					// this.lotRepo.SaveOrUpdate(curLot);
					return new ActionResult<>(false, "收/付款未完成", curLot);
				}

				if (invoiceDoneForSingle.size() > 0 || invoiceDoneForMulti.size() > 0) {
					curLot.setIsFunded(true);
					// this.lotRepo.SaveOrUpdate(curLot);
					return new ActionResult<>(true, null, curLot);
				}
			}

			return new ActionResult<>(false, "收/付款未完成");

		} catch (Exception e) {

			logger.error(e);

			return new ActionResult<>(false, e.getMessage());
		}

	}

	@Override
	public boolean CanLotIsFunded(String lotid) {

		Lot curLot = this.lotRepo.getOneById(lotid, Lot.class);

		if (curLot == null)
			return false;

		if (curLot.getIsInvoiced()) {
			// 单批次发票
			DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("LotId", lotid));
			where.add(Restrictions.eq("Is4MultiLots", false));
			where.add(Restrictions.or(Restrictions.eq("PFA", "F"), Restrictions.eq("PFA", "A")));

			List<Invoice> invoiceDoneForSingle = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			if (invoiceDoneForSingle.size() > 0 && invoiceDoneForSingle.stream().filter(x -> x.getIsExecuted() == false)
					.collect(Collectors.toList()).size() > 0) {

				return false;
			}
			// 多批次发票
			List<Invoice> invoiceDoneForMulti = GetMultilotInvoicesByLotId(lotid).stream()
					.filter(invoice -> invoice.getPFA().equals("F") || invoice.getPFA().equals("A"))
					.collect(Collectors.toList());

			if (invoiceDoneForMulti.size() > 0 && invoiceDoneForMulti.stream().filter(x -> x.getIsExecuted() == false)
					.collect(Collectors.toList()).size() > 0) {

				return false;
			}

			if (invoiceDoneForSingle.size() > 0 || invoiceDoneForMulti.size() > 0) {
				return true;
			}
		}

		return false;
	}

	private List<Invoice> GetMultilotInvoicesByLotId(String lotid) {

		if (lotid == null)
			return null;
		String sql = "Select Distinct a.* " + "  from [Physical].[Invoice] a "
				+ "  inner join [Physical].[InvoiceStorage] b " + "      on a.Id = b.InvoiceId and a.Is4MultiLots =1 "
				+ "  inner join [Physical].[Storage] c on b.StorageId = c.id " + " where c.LotId ='" + lotid + "' ";

		List<Invoice> invoices = this.invoiceRepo.ExecuteCorrectlySql(sql, Invoice.class);

		for (Invoice invoice : invoices) {
			if (invoice == null || invoice.getId() == null)
				continue;

			if (invoice.getLot() != null)
				invoice.setFullNo(invoice.getLot().getFullNo());
			if (invoice.getCustomer() != null)
				invoice.setCustomerName(invoice.getCustomer().getName());
			if (invoice.getLegal() != null)
				invoice.setLegalName(invoice.getLegal().getName());
		}
		return invoices;
	}

	@Override
	public List<Lot> setDelivery4Lot(Lot lot) {

		if (lot == null || lot.getId() == null)
			return null;

		Lot originalLot = lot;

		// lot.IsDelivered = !lot.IsDelivered;//测试

		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		// originalLot = this.lotRepo.getOneById(lot.getSplitFromId(),
		// Lot.class);
		// }

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		// List<Lot> lots2 =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList(); //
		// 拆分批次，//注意包含全部拆分批次

		List<Lot> lots2 = new ArrayList<Lot>();
		where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;

		storages.forEach(s -> sumQuantity.add(s.getQuantity()));

		for (Lot lot1 : lots2) {

			where = DetachedCriteria.forClass(Storage.class);
			where.add(Restrictions.eq("LotId", lot1.getId()));
			where.add(Restrictions.eq("IsHidden", false));

			List<Storage> storages1 = this.storageRepo.GetQueryable(Storage.class).where(where).toList();
			storages1.forEach(s1 -> sumQuantity.add(s1.getQuantity()));
		}

		// BigDecimal _Quantity = originalLot.getQuantityOriginal() == null ?
		// originalLot.getQuantity()
		// : originalLot.getQuantityOriginal();

		// QuantityMaL lotQuantity =
		// getQuantityMoreorLess(originalLot.getMoreOrLessBasis(), _Quantity,
		// originalLot.getMoreOrLess());

		// BigDecimal lotQuantityLess = lotQuantity.getQuantityLess() == null ?
		// BigDecimal.ZERO
		// : lotQuantity.getQuantityLess();

		boolean IsDeliveryed = lot.getQuantityMore().compareTo(lot.getQuantityDelivered()) >= 0
				&& lot.getQuantityDelivered().compareTo(lot.getQuantityLess()) >= 0;

		List<Lot> lots4SplitsWithoutCurret = lots2.stream().filter(x -> !x.getId().equals(lot.getId()))
				.collect(Collectors.toList());

		for (Lot lot4split : lots4SplitsWithoutCurret) {
			lot4split.setIsDelivered(IsDeliveryed);
		}

		List<Lot> lotsToUpdateIsDeliveryed = new ArrayList<>();

		lotsToUpdateIsDeliveryed.addAll(lots4SplitsWithoutCurret);

		// if (lot.getIsSplitted()!=null && lot.getIsSplitted()) {
		// originalLot.setIsDelivered(IsDeliveryed);
		// lotsToUpdateIsDeliveryed.add(originalLot);
		// }

		lot.setIsDelivered(IsDeliveryed);

		return lotsToUpdateIsDeliveryed;
	}

	@Override
	public List<Invoice> SimplifyDataInvoiceList(List<Invoice> invoices) {

		if (invoices == null || invoices.size() == 0)
			return null;

		return invoices.stream().map(this::SimplifyData).collect(Collectors.toList());

	}

	@Override
	public List<InvoiceGrade> SimplifyDataInvoiceGradeList(List<InvoiceGrade> invoices) {

		if (invoices == null || invoices.size() == 0)
			return null;

		return invoices.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Fee> SimplifyDataFeeList(List<Fee> fees) {

		if (fees == null || fees.size() == 0)
			return null;

		return fees.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	// private Map<String, String> contractSpotDirectionMap = new HashMap<>();

	// @Override
	// public List<Storage>
	// SimplifyDataStorageListWithContractSpotDirectionMap(List<Storage>
	// storages,
	// Map<String, String> contractSpotDirectionMap) {
	//
	// if (storages == null || storages.size() == 0)
	// return storages;
	//
	// this.contractSpotDirectionMap = contractSpotDirectionMap;
	//
	// return
	// storages.stream().map(this::SimplifyData).collect(Collectors.toList());
	// }

	@Override
	public Lot UpdateLotPriceByLotId_New(String lotId) {

		if (lotId == null)
			return null;

		Lot lot = lotRepo.getOneById(lotId, Lot.class);

		if (lot == null)
			return null;

		// 在此增加一个逻辑：由于lot数量在收/发货之后修改了，导致isDelivered标志出错
		// 更新批次的IsDelivered标志

		DetachedCriteria dc = DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId()))
				.add(Restrictions.eq("IsHidden", false)).setProjection(Projections.sum("Quantity"));
		BigDecimal sumQuantity = DecimalUtil
				.nullToZero((BigDecimal) storageRepo.getHibernateTemplate().findByCriteria(dc).get(0));

		lot.setQuantityDelivered(sumQuantity);
		/*
		 * 使用新的统一方法 lot.IsDelivered = (lot.QuantityDelivered >= lot.QuantityLess
		 * && lot.QuantityDelivered <= lot.QuantityMore);
		 **/
		List<Lot> lots = setDelivery4Lot_New(lot);

		lotRepo.SaveOrUpdate(lot);
		UpdateFuturesSpread_New(lotId); // 更新期货调期成本（此成本需要作为最终成本的一部分）
		UpdateLotPriceByLotId_New(lot);
		UpdatePriceAndHedgeFlag4Lot_New(lotId);
		UpdateDeliveryStatus_New(lots);// 更新拆分批次的交货状态
		return lot;
	}

	private boolean UpdatePriceAndHedgeFlag4Lot_New(String lotId) {

		if (lotId == null)
			return false;
		Lot lot = lotRepo.getOneById(lotId, Lot.class);

		return UpdatePriceAndHedgeFlag4Lot2_New(lot);

	}

	// 更新批次及拆分批次的点价/保值数量及状态
	private boolean UpdatePriceAndHedgeFlag4Lot2_New(Lot lot) {

		if (lot == null)
			return false;
		RefUtil ref = new RefUtil();
		BigDecimal QuantityPriced = BigDecimal.ZERO;
		BigDecimal QuantityHedged = BigDecimal.ZERO;
		boolean IsPriced;
		if (lot.getMajorType().equals(MajorType.Fix)) {
			IsPriced = true;
			QuantityPriced = lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal();
		} else {
			IsPriced = IsPriced4Lot(com.smm.ctrm.util.BeanUtils.copy(lot), ref);
			QuantityPriced = ref.getQuantityPriced();

		}
		Boolean IsHedged = IsHedged4Lot(com.smm.ctrm.util.BeanUtils.copy(lot), ref);
		QuantityHedged = ref.getQuantityHedged();
		lot.setIsPriced(IsPriced);
		lot.setIsHedged(IsHedged);
		lot.setQuantityHedged(QuantityHedged);
		lot.setQuantityPriced(QuantityPriced);

		lotRepo.SaveOrUpdate(lot);

		return true;

	}

	/**
	 * 更新点价合同 --- 主体价格部分
	 * 
	 * @param lot
	 */
	private void UpdateLotPriceByLotId_New(Lot lot) {

		/*
		 * 业务说明： 早先的设计，只是更新批次的价格。改进后，同时更新批次的 (1) 杂费的冲销标志。IsFeeEliminated。 (2)
		 * 杂费的单价。
		 */
		if (lot == null)
			return;
		/*
		 * List<Fee> fees = feeRepo.GetQueryable(Fee.class)
		 * .where(DetachedCriteria.forClass(Fee.class).add(Restrictions.eq(
		 * "LotId", lot.getId()))).toList(); // 来自发票的实际费用 List<Invoice> funds =
		 * invoiceRepo.GetQueryable(Invoice.class)
		 * .where(DetachedCriteria.forClass(Invoice.class).add(Restrictions.eq(
		 * "LotId", lot.getId()))).toList();
		 */
		// 按检验批次登记的实际费用
		UpdateLotFees_New(lot);
		DetachedCriteria pricingDc = DetachedCriteria.forClass(Pricing.class);
		pricingDc.add(Restrictions.eqOrIsNull("LotId", lot.getId()));
		pricingDc
				.setProjection(Projections.projectionList()
						.add(Projections.sqlProjection("sum(Quantity * Fee) as FeeQuantity",
								new String[] { "FeeQuantity" }, new Type[] { StandardBasicTypes.BIG_DECIMAL }))
						.add(Projections.sqlProjection("sum(Quantity * Premium) as PremiumQuantity",
								new String[] { "PremiumQuantity" }, new Type[] { StandardBasicTypes.BIG_DECIMAL }))
						.add(Projections.sqlProjection("sum(Quantity * Major) as MajorQuantity",
								new String[] { "MajorQuantity" }, new Type[] { StandardBasicTypes.BIG_DECIMAL })))
				.setResultTransformer(CriteriaSpecification.ALIAS_TO_ENTITY_MAP);
		Map<String, BigDecimal> sumPricingMap = (Map<String, BigDecimal>) pricingRepo.getHibernateTemplate()
				.findByCriteria(pricingDc).get(0);
		// int symbol = lot.getSpotDirection().equals(SpotType.Purchase) ? 1 :
		// -1; //正负号
		BigDecimal quantityPriced = DecimalUtil.nullToZero((BigDecimal) pricingRepo.getHibernateTemplate()
				.findByCriteria(DetachedCriteria.forClass(Pricing.class)
						.add(Restrictions.eq("PriceTiming", PriceTiming.Onschedule))
						.setProjection(Projections.sum("Quantity")))
				.get(0));// 全部已点价的数量
		BigDecimal amount4Major = DecimalUtil.nullToZero(sumPricingMap.get("MajorQuantity")); // 按市场价格计算的批次的金额
		// BigDecimal amount4Premium =
		// DecimalUtil.nullToZero(sumPricingMap.get("PremiumQuantity"));//按升贴水计算的批次的金额
		BigDecimal amount4Fee = DecimalUtil.nullToZero(sumPricingMap.get("FeeQuantity"));// 该批次的全部杂费的金额
		// BigDecimal amount4Price = amount4Major.add(amount4Premium);//该批次的商品价格
		amount4Major = amount4Major.add(amount4Fee);
		// BigDecimal amount4Final = amount4Price.add(amount4Fee.multiply(new
		// BigDecimal(symbol)));
		// 该批次的最终金额。如果是采购，加上杂费金额；如果是销售，减去杂费金额。
		if (!lot.getMajorType().equals(MajorType.Fix)) {
			lot.setMajor(quantityPriced.compareTo(BigDecimal.ZERO) != 0 ? DecimalUtil.divideForPrice(amount4Major, quantityPriced)
					: BigDecimal.ZERO);
		}
		// 点价价格 ( = 市场价格 + 升贴水)
		lot.setPrice(DecimalUtil.nullToZero(lot.getMajor()).add(DecimalUtil.nullToZero(lot.getPremium())));
		// 最终的采购或销售价格 ( = 点价价格 +/- 杂费价格)
		lot.setFinal(lot.getSpotDirection().equals(SpotType.Purchase)
				? DecimalUtil.nullToZero(lot.getPrice()).add(DecimalUtil.nullToZero(lot.getFee()))
				: DecimalUtil.nullToZero(lot.getPrice()).subtract(DecimalUtil.nullToZero(lot.getFee())));
		lot.setQuantityPriced(quantityPriced);
		lot.setIsPriced(IsPriced4Lot(lot));
		lotRepo.SaveOrUpdate(lot);
		Contract contract = contractRepo.getOneById(lot.getContractId(), Contract.class);
		if (contract != null) {
			List<Grade> grades = new ArrayList<>();
			List<Grade> listvGrade = gradeRepo.GetQueryable(Grade.class)
					.where(DetachedCriteria.forClass(Grade.class).add(Restrictions.eq("ContractId", contract.getId())))
					.toList();
			for (Grade item : listvGrade) {
				grades.add(com.smm.ctrm.util.BeanUtils.copy(item));
			}
			contract.setGrades(grades);
			List<DischargingPriceDiff> priceDiffs = new ArrayList<>();
			List<DischargingPriceDiff> listvPriceDiffs = dischargingPriceDiffRepo
					.GetQueryable(DischargingPriceDiff.class).where(DetachedCriteria
							.forClass(DischargingPriceDiff.class).add(Restrictions.eq("ContractId", contract.getId())))
					.toList();
			if (listvPriceDiffs.size() > 0) {
				for (DischargingPriceDiff item : listvPriceDiffs) {
					priceDiffs.add(com.smm.ctrm.util.BeanUtils.copy(item));
				}
			}
			contract.setPriceDiffs(priceDiffs);
		}

		// 预计销售升贴水
		BigDecimal estPremium = BigDecimal.ZERO;
		if (StringUtils.isNotBlank(lot.getEstDischarging())) {
			EstPremiumSetup estPremiumSetup = estPremiumSetupRepo.GetQueryable(EstPremiumSetup.class)
					.where(DetachedCriteria.forClass(EstPremiumSetup.class)
							.add(Restrictions.eq("EstDischarging", lot.getEstDischarging())))
					.firstOrDefault();
			if (estPremiumSetup != null) {
				estPremium = DecimalUtil.nullToZero(estPremiumSetup.getEstPremium());
			}
		}
		List<Storage> noteByTest = storageRepo.GetQueryable(Storage.class)
				.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.isNotNull("LotId"))
						.add(Restrictions.eq("LotId", lot.getId())))
				.toList();
		for (Storage storage : noteByTest) {
			storage.setFee(DecimalUtil.nullToZero(lot.getFee()));
			storage.setEstimateFee(DecimalUtil.nullToZero(lot.getEstimateFee()));
			if (!storage.getIsMannalPremium()) {
				storage.setPremium(DecimalUtil.nullToZero(lot.getPremium()));
				// 重计算升贴水
				if (contract != null) {
					if (contract.getGrades() != null) {
						Grade g = null;
						for (Grade grade : contract.getGrades()) {
							if (grade.getSpecId() != null && grade.getSpecId().equalsIgnoreCase(storage.getSpecId())) {
								g = grade;
								break;
							}
						}
						if (g != null) {
							// 等级相关升贴水

							BigDecimal diff = BigDecimal.ZERO;

							switch (lot.getDeliveryTerm().trim().toUpperCase()) {
							case "FOB":
								storage.setPremium(DecimalUtil.nullToZero(g.getFOB()).add(diff));
								break;
							case "CIF":
								storage.setPremium(DecimalUtil.nullToZero(g.getCIF()).add(diff));
								break;
							case "EXW":
								storage.setPremium(DecimalUtil.nullToZero(g.getEXW()).add(diff));
								break;
							case "FCA":
								storage.setPremium(DecimalUtil.nullToZero(g.getFCA()).add(diff));
								break;
							default:
								break;
							}
						}
					}
					if (contract.getPriceDiffs() != null) {
						DischargingPriceDiff pdiff = null;
						for (DischargingPriceDiff dpd : contract.getPriceDiffs()) {
							if (dpd.getDischarging() != null && dpd.getDischarging().equals(lot.getDischarging())) {
								pdiff = dpd;
								break;
							}
						}
						if (pdiff != null) {
							storage.setPremium(storage.getPremium().add(pdiff.getPriceDiff()));
						}
					}
				}
			}
			// 预计销售升贴水
			storage.setPremium4EstSale(estPremium);
			storage.setMajor(DecimalUtil.nullToZero(lot.getMajor()));

			BigDecimal price = storage.getMajor().add(storage.getPremium());
			BigDecimal finalPrice = lot.getSpotDirection().equals(SpotType.Purchase)
					? DecimalUtil.nullToZero(price).add(DecimalUtil.nullToZero(storage.getRealFee()))
					: DecimalUtil.nullToZero(price).subtract(DecimalUtil.nullToZero(storage.getRealFee()));

			if (lot.getSpotDirection().equals(SpotType.Purchase)) {
				finalPrice = finalPrice.subtract(DecimalUtil.nullToZero(storage.getSpread4Initial()))
						.subtract(DecimalUtil.nullToZero(storage.getSpread4Qp()))
						.subtract(DecimalUtil.nullToZero(storage.getSpread4Lot())); // 20150715
																					// 价格增加调期成本
			} else {
				finalPrice = finalPrice.add(DecimalUtil.nullToZero(storage.getSpread4Initial()))
						.add(DecimalUtil.nullToZero(storage.getSpread4Qp()))
						.add(DecimalUtil.nullToZero(storage.getSpread4Lot())); // 20150715
																				// 价格增加调期成本
			}
			storage.setPrice(finalPrice);
			storage.setAmount(storage.getQuantity().multiply(storage.getPrice()));
			storage.setCurrency(lot.getCurrency());
			storageRepo.SaveOrUpdate(storage);
		}
	}

	// 按检验批次登记的实际费用
	private void UpdateLotFees_New(Lot lot) {

		if (lot == null)
			return;

		// #region 费用计算
		List<Fee> fees = new ArrayList<>();

		DetachedCriteria dc = DetachedCriteria.forClass(Fee.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));
		List<Fee> listvFee = feeRepo.GetQueryable(Fee.class).where(dc).toList();

		if (listvFee != null && listvFee.size() > 0)
			fees.addAll(listvFee);

		dc = DetachedCriteria.forClass(Invoice.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));
		dc.add(Restrictions.ne("PFA", InvoiceType.Final));
		dc.add(Restrictions.ne("PFA", InvoiceType.Adjust));
		dc.add(Restrictions.ne("PFA", InvoiceType.Provisional));
		dc.add(Restrictions.ne("PFA", InvoiceType.MultiLots));
		dc.add(Restrictions.ne("PFA", InvoiceType.SummaryNote));

		List<Invoice> funds = invoiceRepo.GetQueryable(Invoice.class).where(dc).toList();

		//// 按检验批次登记的实际费用
		dc = DetachedCriteria.forClass(Storage.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));
		List<Storage> noteByTest = storageRepo.GetQueryable(Storage.class).where(dc).toList();
		for (Storage storage : noteByTest) {
			Hibernate.initialize(storage.getSummaryFeesList());
		}
		// 来自信用证的费用

		// 按批次查询发票
		/*
		 * dc = DetachedCriteria.forClass(Invoice.class);
		 * dc.add(Restrictions.eq("LotId", lot.getId())); List<Invoice>
		 * invoiceList =
		 * invoiceRepo.GetQueryable(Invoice.class).where(dc).toList();
		 * 
		 * List<LC> Lcs = new ArrayList<>();
		 * 
		 * // 发票按LC(信用证) 分组 if (invoiceList != null && invoiceList.size() > 0) {
		 * Map<String, LC> lcMap = new HashMap<>(); for(Invoice invoice :
		 * invoiceList) { if(invoice.getLcId() == null || invoice.getLC() ==
		 * null) continue; lcMap.put(invoice.getLcId(), invoice.getLC()); }
		 * Lcs.addAll(lcMap.values()); }
		 */

		dc = DetachedCriteria.forClass(LC.class);
		dc.createAlias("Invoices", "invoices");
		// where.add(Restrictions.gt("Invoices.Count", 0));
		dc.add(Restrictions.eq("invoices.LotId", lot.getId()));
		List<LC> Lcs = this.lcRepo.GetQueryable(LC.class).where(dc).toList();
		List<LC> tmpLcs = new ArrayList<>();
		for (LC lc : Lcs) {
			if (lc.getInvoices() != null && lc.getInvoices().size() > 0) {
				tmpLcs.add(lc);
			}
		}
		Lcs = tmpLcs;

		List<String> feeCodeType = fees.stream().map(x -> x.getFeeCode()).collect(Collectors.toList());
		List<Invoice> temp = new ArrayList<>();
		for (Invoice f : funds) {
			if (!feeCodeType.contains(f.getFeeCode())) {
				temp.add(f);
			}
		}

		BigDecimal amountDone4Other = new BigDecimal(temp.stream().mapToDouble(x -> x.getAmount().doubleValue()).sum());

		// #region 信证中的费用
		BigDecimal lcfee = BigDecimal.ZERO;

		dc = DetachedCriteria.forClass(Legal.class);
		dc.add(Restrictions.eq("Code", "SM"));
		Legal sm = legalRepo.GetQueryable(Legal.class).where(dc).firstOrDefault();

		for (LC l : Lcs) {

			// 计算出该信用证中SM的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfSM = l.getInvoices().stream().filter(x -> x.getLegalId().equals(sm.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesSMOfA = invoicesOfSM.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesSMOfA.size() > 0) {
				for (Invoice i : invoicesSMOfA) {

					invoicesOfSM.removeAll(invoicesOfSM.stream().filter(x -> x.getAdjustId().equals(i.getId()))
							.collect(Collectors.toList()));
				}

			}

			// 计算出该信用证中BVI的发票（发票不包含被调整的临时发票）
			List<Invoice> invoicesOfBVI = l.getInvoices().stream().filter(x -> !x.getLegalId().equals(sm.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesBVIOfA = invoicesOfSM.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesBVIOfA.size() > 0) {
				for (Invoice i : invoicesBVIOfA) {
					invoicesOfBVI.removeAll(invoicesOfBVI.stream().filter(x -> x.getAdjustId().equals(i.getId()))
							.collect(Collectors.toList()));
				}
			}

			// 计算出信用证中该批次的发票
			List<Invoice> invoicesOfLot = l.getInvoices().stream().filter(x -> x.getLotId().equals(lot.getId()))
					.collect(Collectors.toList());
			List<Invoice> invoicesLotOfA = invoicesOfSM.stream().filter(x -> x.getPFA().equals(InvoiceType.Adjust))
					.collect(Collectors.toList());

			if (invoicesLotOfA.size() > 0) {
				for (Invoice i : invoicesLotOfA) {

					invoicesOfLot.removeAll(invoicesOfLot.stream().filter(x -> x.getAdjustId().equals(i.getId()))
							.collect(Collectors.toList()));
				}
			}
			if (lot.getLegalId().equals(sm.getId())) // 商贸
			{
				BigDecimal sumLot = new BigDecimal(
						invoicesOfLot.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

				BigDecimal sumSm = new BigDecimal(
						invoicesOfSM.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

				// 开证费用+承兑费
				lcfee = lcfee.add(l.getKzAmount().multiply(sumLot).divide(sumSm)
						.add(l.getCdAmount().multiply(sumLot).divide(sumSm)));
			} else // bvi
			{

				BigDecimal sumLot = new BigDecimal(
						invoicesOfLot.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
				BigDecimal sumBvi = new BigDecimal(
						invoicesOfBVI.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

				lcfee = lcfee.add(l.getTxAmount().multiply(sumLot).divide(sumBvi)
						.add(l.getYfAmount().multiply(sumLot).divide(sumBvi)));
			}

		}

		// #region 按批次检验费用,如果预估费用中没有该类型的检验费，则费用归集到其它类型

		// decimal? sumAmount = 0M, sumTransAmount = 0M, sumInsuAmount = 0M,
		// sumCost = 0M, sumOhters = 0M;
		// decimal? sumBankDocFee = 0M, sumBuyDocFee = 0M, sumDisputeFine = 0M,
		// sumHedgeFee = 0M;

		BigDecimal sumAmount = BigDecimal.ZERO;
		BigDecimal sumTransAmount = BigDecimal.ZERO;
		BigDecimal sumInsuAmount = BigDecimal.ZERO;
		BigDecimal sumCost = BigDecimal.ZERO;
		BigDecimal sumOhters = BigDecimal.ZERO;

		BigDecimal sumBankDocFee = BigDecimal.ZERO;
		BigDecimal sumBuyDocFee = BigDecimal.ZERO;
		BigDecimal sumDisputeFine = BigDecimal.ZERO;
		BigDecimal sumHedgeFee = BigDecimal.ZERO;
		// decimal? sumTransAmountEstimated = 0M; //bvi采购中，运输费
		List<Storage> storages = new ArrayList<>();

		for (Storage notefee : noteByTest) {
			List<Storage> storagess = noteByTest.stream().filter(x -> x.getId().equals(notefee.getId()))
					.collect(Collectors.toList());
			Storage notefeeCopy = null;
			if (storagess != null && storagess.size() > 0) {
				notefeeCopy = storagess.get(0);
			}
			notefeeCopy.setRealFee(BigDecimal.ZERO);
			if(notefeeCopy.getSummaryFeesList()!=null&&notefeeCopy.getSummaryFeesList().size()>0){
				for (SummaryFees summaryFee : notefeeCopy.getSummaryFeesList()) {

					dc = DetachedCriteria.forClass(Invoice.class);
					dc.add(Restrictions.eq("Id", summaryFee.getInvoiceId()));
					dc.add(Restrictions.eq("PFA", InvoiceType.SummaryNote));

					Invoice invoice = invoiceRepo.GetQueryable(Invoice.class).where(dc).firstOrDefault();

					if (invoice == null)
						continue;

					switch (invoice.getFeeCode()) {

					case FeeCode.Test: // 检验费
						sumAmount = sumAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.Test) < 0) {
							amountDone4Other = amountDone4Other.add(sumAmount);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.Transportation:// 运输费
						sumTransAmount = sumTransAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.Transportation) < 0) {
							amountDone4Other = amountDone4Other.add(sumTransAmount);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.Insurance:// 保险费
						sumInsuAmount = sumInsuAmount.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.Insurance) >= 0) {
							amountDone4Other = amountDone4Other.add(sumInsuAmount);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.Cost:// 资金成本
						sumCost = sumCost.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.Cost) < 0) {
							amountDone4Other = amountDone4Other.add(sumCost);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.BankDocumentsFee:// 银行文件费
						sumBankDocFee = sumBankDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.BankDocumentsFee) < 0) {
							amountDone4Other = amountDone4Other.add(sumBankDocFee);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.BuyDocumentsFee:// 采购文件费
						sumBankDocFee = sumBankDocFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.BuyDocumentsFee) < 0) {
							amountDone4Other = amountDone4Other.add(sumBankDocFee);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.DisputeFine:// 争议罚款费
						sumDisputeFine = sumDisputeFine.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.DisputeFine) < 0) {
							amountDone4Other = amountDone4Other.add(sumDisputeFine);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					case FeeCode.HedgeFee:// 套保费
						sumHedgeFee = sumHedgeFee.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						if (feeCodeType.indexOf(FeeCode.HedgeFee) < 0) {
							amountDone4Other = amountDone4Other.add(sumHedgeFee);
						}
						notefeeCopy.setRealFee(notefeeCopy.getRealFee().add(summaryFee.getPrice()));
						break;
					default:// 其他所有费用
						sumOhters = sumOhters.add(summaryFee.getPrice().multiply(notefee.getQuantity()));
						break;
					}

				}
			}
			

			storages.add(notefeeCopy);
		}

		for (Fee fee : fees) {

			fee.setAmountDone(new BigDecimal(funds.stream().filter(x -> x.getFeeCode().equals(fee.getFeeCode()))
					.mapToDouble(x -> x.getAmount().doubleValue()).sum()));

			if (fee.getFeeCode().equals(FeeCode.Cost)) // 资金成本（实际发生来自信用证）
			{
				fee.setAmountDone(fee.getAmountDone().add(lcfee).add(sumCost));
			} else if (fee.getFeeCode().equals(FeeCode.Test)) // 检验费时，实际费需加上按检验批次录入的检验费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumAmount));
			} else if (fee.getFeeCode().equals(FeeCode.Other)) // 其他费用
			{
				List<Fee> feess = fees.stream().filter(x -> x.getFeeCode().equals(FeeCode.Cost))
						.collect(Collectors.toList());
				if (feess != null && feess.size() > 0) {
					amountDone4Other = amountDone4Other.add(lcfee);
				}
				fee.setAmountDone(fee.getAmountDone().add(amountDone4Other).add(sumOhters));
			} else if (fee.getFeeCode().equals(FeeCode.Transportation)) // 运输费用
			{
				fee.setAmountDone(fee.getAmountDone().add(sumTransAmount));
			} else if (fee.getFeeCode().equals(FeeCode.Insurance)) // 保险费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumInsuAmount));
			} else if (fee.getFeeCode().equals(FeeCode.BankDocumentsFee)) // 银行文件费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumBankDocFee));
			} else if (fee.getFeeCode().equals(FeeCode.BuyDocumentsFee)) // 采购文件费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumBuyDocFee));
			} else if (fee.getFeeCode().equals(FeeCode.DisputeFine)) // 争议罚款费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumDisputeFine));
			} else if (fee.getFeeCode().equals(FeeCode.HedgeFee)) // 套保费
			{
				fee.setAmountDone(fee.getAmountDone().add(sumHedgeFee));
			}

			feeRepo.SaveOrUpdate(fee);

		}

		// 更批次对应的交付明细的实际费用
		BigDecimal realFeeOfStorage = BigDecimal.ZERO;
		String[] feeCodes = new String[] { FeeCode.Cost, FeeCode.Test, FeeCode.Transportation, FeeCode.Insurance,
				FeeCode.BankDocumentsFee, FeeCode.BuyDocumentsFee, FeeCode.DisputeFine, FeeCode.HedgeFee };
		for (String fee : feeCodes) {
			BigDecimal feeAmountDone = new BigDecimal(funds.stream().filter(x -> x.getFeeCode().equals(fee))
					.mapToDouble(x -> x.getAmount().doubleValue()).sum());

			if (fee == FeeCode.Cost) // 资金成本（实际发生来自信用证）
			{
				realFeeOfStorage = realFeeOfStorage.add(lcfee.add(feeAmountDone).divide(lot.getQuantity()));
			} else if (fee == FeeCode.Test) // 检验费时，实际费需加上按检验批次录入的检验费
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			} else if (fee == FeeCode.Transportation) // 运输费用
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			} else if (fee == FeeCode.Insurance) // 保险费
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			}
			if (fee == FeeCode.HedgeFee) // 套保费用
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			} else if (fee == FeeCode.BankDocumentsFee) // 银行文件费用
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			} else if (fee == FeeCode.BuyDocumentsFee) // 采购文件费用
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			} else if (fee == FeeCode.DisputeFine) // 争议罚款费
			{
				realFeeOfStorage = realFeeOfStorage.add(feeAmountDone.divide(lot.getQuantity()));
			}

		}

		for (Storage s : storages) {
			Storage tempStorage = storageRepo.getOneById(s.getId(), Storage.class);
			BigDecimal realFee = s.getRealFee().add(realFeeOfStorage);

			if (lot.getIsDelivered()) {

				tempStorage.setRealFee(realFee.multiply(lot.getQuantity()).divide(lot.getQuantityDelivered()));
			} else {

				tempStorage.setRealFee(realFee);
			}

			storageRepo.SaveOrUpdate(tempStorage);
		}

		// 批次数量具体是多少
		BigDecimal qty = BigDecimal.ZERO;

		if (lot.getIsDelivered() && lot.getQuantityDelivered() != null
				&& lot.getQuantityDelivered().compareTo(BigDecimal.ZERO) > 0) {

			qty = lot.getQuantityDelivered();
		} else {

			qty = lot.getQuantity();
		}

		// 各项杂费是否已经全部冲销，决定批次的IsFeeEliminated标志。IsFeeEliminated为true，是发票可以盈亏结算的前提条件之一。
		boolean isFeeEliminated = Boolean.TRUE;
		for (Fee fee : fees) {
			if (fee.getIsEliminated() == null || !fee.getIsEliminated()) {
				isFeeEliminated = Boolean.FALSE;
				break;
			}
		}
		lot.setIsFeeEliminated(isFeeEliminated);

		// ***********************************
		// 无论IsFeeEliminated是什么，计算出批次的杂费单价，均为/MT
		BigDecimal sumFee = BigDecimal.ZERO;

		for (Fee fee : fees) {

			if (fee.getIsEliminated()) {

				sumFee = sumFee.add(fee.getAmountDone());
			} else {

				sumFee = sumFee.add(fee.getAmountEstimated());
			}
		}

		BigDecimal sumEstimated = new BigDecimal(
				fees.stream().mapToDouble(x -> x.getAmountEstimated().doubleValue()).sum());
		BigDecimal sumDone = new BigDecimal(fees.stream().mapToDouble(x -> x.getAmountDone().doubleValue()).sum());
		lot.setEstimateFee(DecimalUtil.divideForQuantity(sumEstimated, qty)); // 预估
		lot.setFee(DecimalUtil.divideForQuantity(sumFee, qty));
		lot.setRealFee(DecimalUtil.divideForQuantity(sumDone, qty));// 实际发生

	}

	// 更新交付明细的Futures Spread
	private boolean UpdateFuturesSpread_New(String lotId) {

		if (lotId == null)
			return false;

		Lot lot = lotRepo.getOneById(lotId, Lot.class);

		if (lot == null)
			return false;
		// var lotOrginal = lot;
		// if (lot.IsSplitted) //拆分批次使用原始批次头寸计算
		// {
		// lotOrginal = Repository.GetQueryable<Lot>().FirstOrDefault(x => x.Id
		// == lot.SplitFromId);
		// if (lotOrginal==null) return false;
		// }
		List<Storage> storages = new ArrayList<>();

		DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));

		List<Storage> listvStorages = storageRepo.GetQueryable(Storage.class).where(dc).toList();

		if (listvStorages.size() > 0) {
			for (Storage item : listvStorages) {
				Storage obj = com.smm.ctrm.util.BeanUtils.copy(item);

				storages.add(obj);
			}

		}
		// var storages = Repository.GetQueryable<Storage>().Where(x => x.LotId
		// == LotId).ToList();
		if (storages.size() > 0) {
			List<Storage> spreadStorages = FuturesSpread2(lot, storages);

			for (Storage storage : spreadStorages) {
				storageRepo.SaveOrUpdate(storage);
			}
		}

		return true;

	}

	@Override
	public Date FormatDateAsYymmdd000000(Date dateTime) {
		if (dateTime == null) {
			return null;
		}
		calendar.setTime(dateTime);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 设置批次交付信息，使用主子批次已交付数量汇总（此返回数量不能作为已交付数量)
	 * 
	 * @param lot
	 * @return
	 */
	@Override
	public List<Lot> setDelivery4Lot_New(Lot lot) {

		if (lot == null || StringUtils.isBlank(lot.getId()))
			return null;

		Lot originalLot = lot;

		List<Lot> lots2 = new ArrayList<>();

		BigDecimal sumQuantity = DecimalUtil
				.nullToZero((BigDecimal) storageRepo.getHibernateTemplate()
						.findByCriteria(DetachedCriteria.forClass(Storage.class)
								.add(Restrictions.eq("LotId", originalLot.getId()))
								.add(Restrictions.eq("IsHidden", false)).setProjection(Projections.sum("Quantity")))
						.get(0));

		for (Lot lot1 : lots2) {
			sumQuantity = sumQuantity.add(DecimalUtil.nullToZero((BigDecimal) storageRepo.getHibernateTemplate()
					.findByCriteria(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot1.getId()))
							.add(Restrictions.eq("IsHidden", false)).setProjection(Projections.sum("Quantity")))
					.get(0)));
		}
		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(),
				(originalLot.getQuantityOriginal() == null ? originalLot.getQuantity()
						: originalLot.getQuantityOriginal()),
				originalLot.getMoreOrLess());

		boolean IsDeliveryed = DecimalUtil.nullToZero(lotQuantity.getQuantityLess()).compareTo(sumQuantity.abs()) <= 0
				&& sumQuantity.abs().compareTo(DecimalUtil.nullToZero(lotQuantity.getQuantityMore())) <= 0;

		List<Lot> lots4SplitsWithoutCurret = new ArrayList<>();
		for (Lot myLot : lots2) {
			if (myLot.getId().equalsIgnoreCase(lot.getId())) {
				lots4SplitsWithoutCurret.add(myLot);
			}
		}

		for (Lot lot4split : lots4SplitsWithoutCurret) {
			lot4split.setIsDelivered(IsDeliveryed);
		}
		lot.setIsDelivered(IsDeliveryed);
		return lots4SplitsWithoutCurret;

		// boolean IsDeliveryed = ((lot.getQuantityLess()?? 0) <=
		// Math.Abs(lot.getQuantityDelivered()) <= (lot.getQuantityMore()?? 0));

		// 检查发货数量是否在溢短装范围内
		// Boolean isDeliveryed =
		// lot.getQuantityDelivered().compareTo(lot.getQuantityLess()) >= 0
		// && lot.getQuantityDelivered().compareTo(lot.getQuantityMore()) <= 0;
		//
		// lot.setIsDelivered(isDeliveryed);

		// C# 代码中处理逻辑有问题

		// Lot originalLot = lot;
		//
		// List<Lot> lots2 = new ArrayList<>();
		//
		// //计算总数量
		// DetachedCriteria dc = DetachedCriteria.forClass(Storage.class);
		// dc.add(Restrictions.eq("LotId", originalLot.getId()));
		// dc.add(Restrictions.eq("IsHidden", false));
		//
		// List<Storage> storages =
		// storageRepo.GetQueryable(Storage.class).where(dc).toList();
		//
		// BigDecimal totalQuantity= BigDecimal.ZERO;
		//
		// if(storages!=null && storages.size()>0){
		//
		// totalQuantity = new BigDecimal(
		// storages.stream().mapToDouble(x->x.getQuantity().doubleValue()).sum());
		// }
	}

	// 设置批次交付信息，使用主子批次已交付数量汇总（此返回数量不能作为已交付数量)
	@Override
	public List<Lot> setInvoice4Lot_NewContract(Lot lot) {

		if (lot == null || StringUtils.isBlank(lot.getId()))
			return null;

		Lot originalLot = lot;
		List<Lot> lots2 = new ArrayList<>();
		List<String> lots2Ids = new ArrayList<>();
		for (Lot l : lots2) {
			lots2Ids.add(l.getId());
		}
		DetachedCriteria invoiceDc = DetachedCriteria.forClass(Invoice.class)
				.add(Restrictions.and(Restrictions.eq("LotId", originalLot.getId()),
						Restrictions.or(Restrictions.eq("PFA", InvoiceType.Final),
								Restrictions.in("PFA", InvoiceType.Adjust))))
				.setProjection(Projections.sum("Quantity"));
		BigDecimal sumQuantity = DecimalUtil
				.nullToZero((BigDecimal) invoiceRepo.getHibernateTemplate().findByCriteria(invoiceDc).get(0));
		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(),
				(originalLot.getQuantityOriginal() == null ? originalLot.getQuantity()
						: originalLot.getQuantityOriginal()),
				originalLot.getMoreOrLess());
		boolean isInvoiced = DecimalUtil.nullToZero(lotQuantity.getQuantityLess()).compareTo(sumQuantity.abs()) <= 0
				&& sumQuantity.abs().compareTo(lotQuantity.getQuantityMore()) <= 0;
		for (Lot l : lots2) {
			l.setIsInvoiced(isInvoiced);
		}
		lot.setIsInvoiced(isInvoiced);
		if (lot.getBrands() != null && lot.getBrands().size() > 0) {
			List<String> ids = new ArrayList<>();
			for (Brand brand : lot.getBrands()) {
				ids.add(brand.getId());
			}
			lot.setBrands(brandRepo.GetQueryable(Brand.class)
					.where(DetachedCriteria.forClass(Brand.class).add(Restrictions.in("Id", ids))).toList());
		}
		return lots2;
	}

	// 是否批次已完成点价
	@Override
	public Boolean IsPriced4Lot_New(Lot lot) {

		BigDecimal QuantityPriced = BigDecimal.ZERO;

		boolean isPriced;

		if (lot.getMajorType().equals(MajorType.Fix)) {
			isPriced = true;
			lot.setQuantityPriced(lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal());
		} else
			isPriced = IsPriced4Lot_New(lot, QuantityPriced);

		return isPriced;
	}

	// 是否批次已完成保值
	@Override
	public Boolean IsHedged4Lot_New(Lot lot) {

		RefUtil ref = new RefUtil();
		return IsHedged4Lot(lot, ref);
	}

	@Override
	public void UpdateDeliveryStatus_New(List<Lot> lots) {

		if (lots == null)
			return;

		for (Lot lot : lots) {
			vLot lottmp = vLotHibernateRepository.getOneById(lot.getId(), vLot.class);

			lottmp.setIsDelivered(lot.getIsDelivered());
			vLotHibernateRepository.SaveOrUpdate(lottmp);
		}

	}

	@Override
	public void UpdateInvoiceStatus_NewContract(List<Lot> lots) {

		if (lots == null)
			return;

		for (Lot lot : lots) {
			vLot lottmp = vLotHibernateRepository.getOneById(lot.getId(), vLot.class);

			if (lottmp != null) {
				lottmp.setIsDelivered(lot.getIsDelivered());
				vLotHibernateRepository.SaveOrUpdate(lottmp);
			}
		}

	}

	private boolean IsPriced4Lot_New(Lot lot, BigDecimal quantityPriced) {

		if (lot == null)
			return false;

		DetachedCriteria dc = DetachedCriteria.forClass(Pricing.class);
		dc.add(Restrictions.eq("LotId", lot.getId()));

		List<Pricing> pricings = pricingRepo.GetQueryable(Pricing.class).where(dc).toList();

		BigDecimal QuantityPriced = BigDecimal.ZERO;

		if (pricings != null && pricings.size() > 0) {

			QuantityPriced = new BigDecimal(pricings.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
		}

		quantityPriced = QuantityPriced;

		BigDecimal temp_quantity = lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal();

		QuantityMaL lotQuantity = getQuantityMoreorLess(lot.getMoreOrLessBasis(), temp_quantity, lot.getMoreOrLess());

		BigDecimal temp_qm = lotQuantity.getQuantityMore() == null ? BigDecimal.ZERO : lotQuantity.getQuantityMore();

		return (temp_qm.compareTo(QuantityPriced.abs()) <= 0 && QuantityPriced.abs().compareTo(temp_qm) <= 0);

	}

	@Override
	public List<Storage> SimplifyDataStorageList(List<Storage> storages) {

		if (storages == null || storages.size() == 0)
			return storages;

		return storages.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Position> SimplifyDataPositionList(List<Position> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Square> SimplifyDataSquareList(List<Square> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Pricing> SimplifyDataPricingList(List<Pricing> list) {

		if (list == null || list.size() == 0)
			return new ArrayList<>();

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<PricingRecord> SimplifyDataPricingRecordList(List<PricingRecord> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<CustomerTitle> SimplifyDataCustomerTitleList(List<CustomerTitle> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Fund> SimplifyDataFundList(List<Fund> list) {

		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Grade> SimplifyDataGradeList(List<Grade> list) {

		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Lot> SimplifyDataLotList(List<Lot> list) {

		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Pending> SimplifyDataPendingList(List<Pending> list) {

		if (list == null || list.size() == 0)
			return new ArrayList<>();

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Tip> SimplifyDataTipList(List<Tip> list) {
		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Invoice> InvoicesOfSplittedLotByLotId(String lotId) {

		if (lotId == null)
			return null;
		Lot lot = this.lotRepo.getOneById(lotId, Lot.class);

		Lot originalLot = lot;

		if (lot.getIsSplitted() != null && lot.getIsSplitted()) {
			originalLot = this.lotRepo.getOneById(lot.getSplitFromId(), Lot.class);// 原始批次
		}
		if (originalLot == null)
			return null;

		DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		List<Lot> lots2 = this.lotRepo.GetQueryable(Lot.class).where(where).toList(); // 拆分批次，//注意包含全部拆分批次

		List<String> lots2Ids = lots2.stream().map(Lot::getId).collect(Collectors.toList());

		where = DetachedCriteria.forClass(Invoice.class);

		where.add(Restrictions.or(Restrictions.eq("LotId", originalLot.getId()), Restrictions.in("LotId", lots2Ids)));

		return this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();
	}

	@Override
	public List<CustomerBalance> SimplifyDataCustomerBalanceList(List<CustomerBalance> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Square> SimplifySquareData(List<Square> list) {

		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Square4Broker> SimplifyDataSquare4BrokerList(List<Square4Broker> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<MarginFlow> SimplifyDataMarginFlowList(List<MarginFlow> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<LC> SimplifyDataLCList(List<LC> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Instrument> SimplifyDataInstrumentList(List<Instrument> list) {

		if (list == null || list.size() == 0)
			return null;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	@Override
	public List<Position4Broker> SimplifyDataPosition4BrokerList(List<Position4Broker> list) {

		if (list == null || list.size() == 0)
			return list;

		return list.stream().map(this::SimplifyData).collect(Collectors.toList());
	}

	// 获取最新的市场价格

	@Override
	public BigDecimal GetM2MPrice(String marketCode, String commodityId, String specId) {
		if (StringUtils.isBlank(marketCode))
			return BigDecimal.ZERO;
		String sql = "select market from Market market where lower(Code) = lower('" + marketCode.toLowerCase() + "')";
		List<Market> markets = marketRepository.ExecuteSql(sql, Market.class);
		if (markets.size() > 0) {
			return GetM2MPrice(markets.get(0).getId(), marketCode, commodityId, specId);
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	@Override
	public BigDecimal GetM2MPrice2(String marketCode, String commodityId, String specId,List<Market> markets,Map<String,List<?>> map)
	{
		if (StringUtils.isBlank(marketCode))
			return BigDecimal.ZERO;
		if(markets.size() == 0)
			return BigDecimal.ZERO;
		
		markets = markets.stream().filter(c->c.getCode().equalsIgnoreCase(marketCode)).collect(Collectors.toList());
		
		if (markets.size() > 0) {
			return GetM2MPrice2(markets.get(0).getId(), marketCode, commodityId, specId,map);
		} else {
			return BigDecimal.ZERO;
		}
	}
	
	@Override
	public Map<String,List<?>> GetLotM2mPrice(Map<String,List<String>> lot2Commditys)
	{
		Map<String,List<?>> map= new HashMap<String,List<?>>();
		
		for(Entry<String,List<String>> m:lot2Commditys.entrySet())
		{
			List<String> listComm = new ArrayList<String>();
			switch (m.getKey().toLowerCase()) {
				case "SFE":
					listComm = m.getValue();
					List<SFE> sfeList = new ArrayList<SFE>();
					if(listComm!=null && listComm.size() >0)
					{
						DetachedCriteria where = DetachedCriteria.forClass(SFE.class);
						where.add(Restrictions.in("CommodityId", listComm));
						sfeList = this.sfeRepo.GetQueryable(SFE.class).where(where).OrderBy(Order.desc("TradeDate"))
								.toList();
					}
					if(map.get(m.getKey().toLowerCase()) == null)
						map.put(m.getKey().toLowerCase(), sfeList);
					break;
				case "LME":
					listComm = m.getValue();
					List<LME> lmeList = new ArrayList<LME>();
					if(listComm!=null && listComm.size() >0)
					{
						DetachedCriteria where = DetachedCriteria.forClass(LME.class);
						where.add(Restrictions.in("CommodityId", listComm));
						lmeList = this.lmeRepo.GetQueryable(LME.class).where(where).OrderBy(Order.desc("TradeDate"))
								.toList();
					}
					
					if(map.get(m.getKey().toLowerCase()) == null)
						map.put(m.getKey().toLowerCase(), lmeList);
					break;
				default:
					listComm = m.getValue();
					List<DSME> dsmeList = new ArrayList<DSME>();
					if(listComm!=null && listComm.size() >0)
					{
						DetachedCriteria where = DetachedCriteria.forClass(DSME.class);
						where.add(Restrictions.in("CommodityId", listComm));
						dsmeList = this.dsmeRepo.GetQueryable(DSME.class).where(where).OrderBy(Order.desc("TradeDate"))
								.toList();
					}
					
					if(map.get(m.getKey().toLowerCase()) == null)
						map.put(m.getKey().toLowerCase(), dsmeList);
					
					break;
			}
		}
		
		return map;
	}

	public BigDecimal GetM2MPrice2(String majorMarketId, String marketCode, String commodityId, String specId,Map<String,List<?>> map) {

		if (StringUtils.isEmpty(marketCode))
			return BigDecimal.ZERO;

		BigDecimal currentMarketPrice;

		switch (marketCode) {
		case "SFE":
			List<SFE> list = (List<SFE>)map.get("SFE");
			SFE sfe = null;

			if (list != null && list.size() > 0)
				sfe = list.get(0);

			currentMarketPrice = (sfe == null) ? BigDecimal.ZERO : sfe.getPriceSettle();
			break;
		case "LME":

			List<LME> lmelist = (List<LME>)map.get("LME");
			LME lme = null;

			if (lmelist != null && lmelist.size() > 0)
				lme = lmelist.get(0);

			currentMarketPrice = (lme == null) ? BigDecimal.ZERO : lme.getCashSell();
			break;
		default:

			List<DSME> dsmelist = (List<DSME>)map.get("DSME");
			DSME dsme = null;

			if (dsmelist != null && dsmelist.size() > 0)
				dsme = dsmelist.get(0);

			currentMarketPrice = (dsme == null) ? BigDecimal.ZERO : dsme.getPriceAverage();
			break;
		}

		return currentMarketPrice.setScale(2, RoundingMode.HALF_UP);

	}
	@Override
	public BigDecimal GetM2MPrice(String majorMarketId, String marketCode, String commodityId, String specId) {

		if (StringUtils.isEmpty(marketCode))
			return BigDecimal.ZERO;

		BigDecimal currentMarketPrice;

		switch (marketCode) {
		case "SFE":

			DetachedCriteria where = DetachedCriteria.forClass(SFE.class);
			where.add(Restrictions.eq("CommodityId", commodityId));
			List<SFE> list = this.sfeRepo.GetQueryable(SFE.class).where(where).OrderBy(Order.desc("TradeDate"))
					.toList();

			SFE sfe = null;

			if (list != null && list.size() > 0)
				sfe = list.get(0);

			currentMarketPrice = (sfe == null) ? BigDecimal.ZERO : sfe.getPriceSettle();
			break;
		case "LME":

			where = DetachedCriteria.forClass(LME.class);
			where.add(Restrictions.eq("CommodityId", commodityId));
			List<LME> lmelist = this.lmeRepo.GetQueryable(LME.class).where(where).OrderBy(Order.desc("TradeDate"))
					.toList();

			LME lme = null;

			if (lmelist != null && lmelist.size() > 0)
				lme = lmelist.get(0);

			currentMarketPrice = (lme == null) ? BigDecimal.ZERO : lme.getCashSell();
			break;
		default:

			where = DetachedCriteria.forClass(DSME.class);
			where.add(Restrictions.eq("CommodityId", commodityId));
			List<DSME> dsmelist = this.dsmeRepo.GetQueryable(DSME.class).where(where).OrderBy(Order.desc("TradeDate"))
					.toList();

			DSME dsme = null;

			if (dsmelist != null && dsmelist.size() > 0)
				dsme = dsmelist.get(0);

			currentMarketPrice = (dsme == null) ? BigDecimal.ZERO : dsme.getPriceAverage();
			break;
		}

		return currentMarketPrice.setScale(2, RoundingMode.HALF_UP);

	}

	@Override
	public List<Lot> setInvoice4Lot(Lot lot) {

		if (lot == null || lot.getId() == null)
			return null;
		Lot originalLot = lot;

		// DetachedCriteria where = DetachedCriteria.forClass(Lot.class);
		// where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		// List<Lot> lots2 =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList(); //
		// 拆分批次，//注意包含全部拆分批次

		// List<String> lots2Ids =
		// lots2.stream().map(Lot::getId).collect(Collectors.toList());

		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);

		// if (lots2Ids != null && lots2Ids.size() > 0)
		// djc.add(Restrictions.in("LotId", lots2Ids));

		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.or(Restrictions.eq("PFA", ActionStatus.InvoiceType_Final),
				Restrictions.eq("PFA", ActionStatus.InvoiceType_Adjust)));

		List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Invoice i : invoices) {
			sumQuantity = sumQuantity.add(i.getQuantity());
		}
		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(),
				originalLot.getQuantityOriginal() != null ? originalLot.getQuantityOriginal()
						: originalLot.getQuantity(),
				originalLot.getMoreOrLess());

		boolean isInvoiced = (DecimalUtil.nullToZero(lotQuantity.getQuantityLess()).compareTo(sumQuantity.abs()) <= 0)
				&& (sumQuantity.abs().compareTo(lotQuantity.getQuantityMore()) <= 0);

		lot.setIsInvoiced(isInvoiced);

		if (lot.getBrands() != null && lot.getBrands().size() > 0) {
			List<Brand> tempBrands = new ArrayList<>();

			lot.getBrands().forEach(b -> {

				Brand temp = this.brandRepo.getOneById(b.getId(), Brand.class);

				if (temp != null) {
					tempBrands.add(temp);
				} else {
					tempBrands.add(b);
				}

			});

			lot.setBrands(tempBrands);

		}
		return new ArrayList<>();
	}

	@Override
	public Boolean IsPriced4Lot(Lot lot) {

		RefUtil ref = new RefUtil();

		boolean isPriced;

		if (lot.getMajorType().equals(MajorType.Fix)) {
			isPriced = true;
			lot.setQuantityPriced(lot.getQuantityOriginal() == null ? lot.getQuantity() : lot.getQuantityOriginal());
		} else
			isPriced = IsPriced4Lot(lot, ref);
		return isPriced;
	}

	@Override
	public Boolean IsHedged4Lot(Lot lot) {

		RefUtil ref = new RefUtil();
		return IsHedged4Lot(lot, ref);
	}

	@Override
	public List<Storage> FuturesSpread(Lot lot, List<Storage> storages, List<Position> positions) {
		try {
			if (lot.getLegalId() != null) {

				lot.setLegal(this.legalRepo.getOneById(lot.getLegalId(), Legal.class));
			}

			if (lot.getContractId() != null) {

				lot.setContract(this.contractRepo.getOneById(lot.getContractId(), Contract.class));

			}

			if (lot.getContract() != null && lot.getLegal() != null && storages.size() > 0) {
				if (positions != null && positions.size() > 0) {
					// #region 计算Spread

					// List<Legal> legals = this.legalRepo.GetList(Legal.class);

					// Legal Sm = legals.stream().filter(x ->
					// x.getCode().equals("SM")).collect(Collectors.toList())
					// .get(0);

					// Legal bvi = legals.stream().filter(x ->
					// x.getCode().equals("SB")).collect(Collectors.toList())
					// .get(0);

					List<Position> carryPositions = positions.stream().filter(Position::getIsCarry)
							.collect(Collectors.toList());

					List<CarryPosition> lstCarry = GetCarryPositions(carryPositions);

					if (lstCarry.size() > 0) {
						BigDecimal initialSpread = BigDecimal.ZERO, QpSpread = BigDecimal.ZERO;

						if (lot.getContract().getSpotDirection().equals(SpotType.Purchase)) // 采购
						{
							// #region SM 采购或者BVI采购（销售对象为客户非商贸），计算期货调期成本

							// QP Date 调期到 预计销售日期
							// 采购批次--保值头寸（卖出头寸QP）--调期头寸（买入QP--卖出预计销售日期）
							CarryPosition initialCarry = lstCarry.stream()
									.filter(x -> x.getPromptDate4Long().equals(lot.getQP())
											&& x.getPromptDate4Short().equals(lot.getEstimateSaleDate()))
									.collect(Collectors.toList()).get(0);

							if (initialCarry != null)
								initialSpread = initialCarry.getSpread();

							// 其他任何日期调期到 QP Date(可存在多条，需加权平均）
							// 采购批次--调期到到期日为QP Date的卖出头寸）
							List<CarryPosition> qpCarry = lstCarry.stream()
									.filter(x -> x.getPromptDate4Short().equals(lot.getQP()))
									.collect(Collectors.toList());

							BigDecimal sumSpread = new BigDecimal(qpCarry.stream()
									.mapToDouble(x -> x.getQuantity().multiply(x.getSpread()).doubleValue()).sum());

							BigDecimal sumQuantity = null;

							if (lot.getIsDelivered()) // 如果已经全部交付，使用交付数量合计
								sumQuantity = new BigDecimal(
										storages.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
							else
								sumQuantity = new BigDecimal(
										qpCarry.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());
							if (sumQuantity.compareTo(BigDecimal.ZERO) != 0 && sumQuantity != null)
								QpSpread = sumSpread.divide(sumQuantity);

							for (Storage storage : storages) {
								storage.setSpread4Initial(initialSpread);
								storage.setSpread4Qp(QpSpread);

								if (storage.getCounterpartyId() != null) {
									// #region 得到Lot Spread

									// 得到对手方
									DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
									where.add(Restrictions.eq("Id", storage.getCounterpartyId()));
									Storage counterStorage = this.storageRepo.GetQueryable(Storage.class).where(where)
											.firstOrDefault();

									if (counterStorage.getLotId() != null && counterStorage.getContractId() != null) {

										where = DetachedCriteria.forClass(Lot.class);
										where.add(Restrictions.eq("Id", counterStorage.getLotId()));
										Lot counterLot = this.lotRepo.GetQueryable(Lot.class).where(where).toList()
												.get(0);

										where = DetachedCriteria.forClass(Contract.class);
										where.add(Restrictions.eq("Id", counterStorage.getContractId()));
										// Contract counterContract =
										// this.contractRepo.GetQueryable(Contract.class)
										// .where(where).toList().get(0);

										if (counterLot != null) {
											// if (lot.LegalId == bvi.Id &&
											// counterContract.IsInternal)
											// {
											// //BVI采购并且对手方是商贸,此时Spread为0.
											// storage.Spread4Initial = null;
											// storage.Spread4Qp = null;
											// }
											// else
											// {
											// 采购批次的预计销售日期 调期到销售批次的QP Date
											// 20150722 调整，bvi采购的Spread保留，不再清零
											CarryPosition lotCarry = lstCarry.stream().filter(
													x -> x.getPromptDate4Long().equals(lot.getEstimateSaleDate())
															&& x.getPromptDate4Short().equals(counterLot.getQP()))
													.collect(Collectors.toList()).get(0);

											if (lotCarry != null)
												storage.setSpread4Lot(lotCarry.getSpread());
											// }
										}
									}

									// #endregion
								}

								// 点价价格（含升贴水） + 费用 -调期成本

								storage.setPrice(storage.getMajor().add(storage.getPremium()).add(storage.getRealFee())
										.subtract(storage.getSpread4Initial()).subtract(storage.getSpread4Qp())
										.subtract(storage.getSpread4Lot()));
							}

							// #endregion
						}
						if (lot.getContract().getSpotDirection().equals(SpotType.Sell)) // 销售
						{
							// #region sm销售，bvi对客户销售（bvi销售给sm不关联头寸）

							// 其他任何日期调期到 QP Date(可存在多条，需加权平均）
							// 销售批次--调期到到期日为QP Date的买入头寸）
							List<CarryPosition> qpCarry = lstCarry.stream()
									.filter(x -> x.getPromptDate4Long().equals(lot.getQP()))
									.collect(Collectors.toList());

							BigDecimal sumSpread = new BigDecimal(qpCarry.stream()
									.mapToDouble(x -> x.getQuantity().multiply(x.getSpread()).doubleValue()).sum());

							BigDecimal sumQuantity = new BigDecimal(
									qpCarry.stream().mapToDouble(x -> x.getQuantity().doubleValue()).sum());

							if (sumQuantity.compareTo(BigDecimal.ZERO) != 0 && sumQuantity != null)
								QpSpread = sumSpread.divide(sumQuantity);

							for (Storage storage : storages) {
								storage.setSpread4Qp(QpSpread);
								storage.setSpread4Initial(null);
								storage.setSpread4Lot(null);

								// 点价价格（含升贴水） - 费用 +调期成本
								storage.setPrice(storage.getMajor().add(storage.getPremium())
										.subtract(storage.getRealFee()).add(storage.getSpread4Initial())
										.add(storage.getSpread4Qp()).add(storage.getSpread4Lot()));
							}

							// #endregion
						}
					}

					// #endregion
				} else {
					if (lot.getContract().getSpotDirection().equals(SpotType.Purchase)) // 采购
					{
						for (Storage storage : storages) {
							storage.setSpread4Qp(null);
							storage.setSpread4Initial(null);
							storage.setSpread4Lot(null);
							storage.setPrice(DecimalUtil.nullToZero(storage.getMajor())
									.add(DecimalUtil.nullToZero(storage.getPremium()))
									.add(DecimalUtil.nullToZero(storage.getRealFee())));
						}
					}
					if (lot.getContract().getSpotDirection().equals(SpotType.Sell)) // 销售
					{
						for (Storage storage : storages) {
							storage.setSpread4Qp(null);
							storage.setSpread4Initial(null);
							storage.setSpread4Lot(null);
							storage.setPrice(
									DecimalUtil.nullToZero(storage.getMajor())
									.add(DecimalUtil.nullToZero(storage.getPremium()))
									.subtract(DecimalUtil.nullToZero(storage.getRealFee())));
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return storages;
	}

	@Override
	public List<CarryPosition> GetCarryPositions(List<Position> carryPositions) {
		// #region 生成一对头寸集合（调期头寸）

		List<Position> longPositions = carryPositions.stream().filter(x -> x.getLS().equals(LS.LONG))
				.collect(Collectors.toList());
		List<Position> shortPositions = carryPositions.stream().filter(x -> x.getLS().equals(LS.SHORT))
				.collect(Collectors.toList());

		List<CarryPosition> lstCarry = new ArrayList<>();

		for (Position longPosition : longPositions) {
			CarryPosition carry = new CarryPosition();
			carry.setCarryCounterPart(longPosition.getCarryCounterpart());
			carry.setPrice4Long(longPosition.getOurPrice());
			carry.setPromptDate4Long(longPosition.getPromptDate());
			carry.setSpread(longPosition.getSpread());
			carry.setCarryRef(longPosition.getCarryRef());
			carry.setQuantity(longPosition.getQuantity().abs());
			carry.setLong(longPosition);

			Position counter = shortPositions.stream()
					.filter(x -> x.getCarryCounterpart().equals(longPosition.getCarryCounterpart()))
					.collect(Collectors.toList()).get(0);

			if (counter != null) {
				carry.setPrice4Short(counter.getOurPrice());
				carry.setPromptDate4Short(counter.getPromptDate());
				carry.setShort(counter);
			}

			lstCarry.add(carry);
		}

		// #endregion

		return lstCarry;
	}

	@Override
	public Position4Broker SimplifyData(Position4Broker position) {

		if (position == null)
			return null;

		if (position.getMarket() != null) {
			position.setMarketCode(position.getMarket().getCode());
			position.setMarketName(position.getMarket().getName());
		}
		if (position.getCommodityId() != null) {
			position.setCommodityName(position.getCommodity().getName());
			position.setCommodityCode(position.getCommodity().getCode());
		}
		if (position.getLot() != null) {
			position.setFullNo(position.getLot().getFullNo());
		}
		if(position.getLegal()!=null) {
			position.setLegalCode(position.getLegal().getCode());
			position.setLegalName(position.getLegal().getName());		
		}
		if (position.getBroker() != null) {
			position.setBrokerName(position.getBroker().getName());
		}
		if(position.getTrader()!=null) {
			position.setTraderName(position.getTrader().getName());
		}
		if(StringUtils.isNotBlank(position.getInstrumentId())) {
			Instrument instrument = instrumentRepo.getOneById(position.getInstrumentId(), Instrument.class);
			if(instrument!=null){
				position.setInstrumentName(instrument.getName());
			}
		}

		/*
		position.setQuantity(
				FormatQuantity(position.getQuantity(), position.getCommodity(), position.getCommodityId()));
		position.setQuantityOriginal(
				FormatQuantity(position.getQuantityOriginal(), position.getCommodity(), position.getCommodityId()));
		position.setQuantityUnSquared(
				FormatQuantity(position.getQuantityUnSquared(), position.getCommodity(), position.getCommodityId()));
	 	*/
		
		return position;
	}

	@Override
	public Square4Broker SimplifyData(Square4Broker square) {

		if (square == null || square.getLong() == null || square.getShort() == null)
			return null;

		square.setPromptDate(square.getLong().getPromptDate());
		square.setQuantity(square.getLong().getQuantity());
		square.setMarketCode(square.getLong().getMarket().getCode());
		square.setMarketName(square.getLong().getMarket().getName());
		square.setCommodityCode(square.getLong().getCommodity().getCode());
		square.setCommodityName(square.getLong().getCommodity().getName());
		square.setBrokerName(square.getLong().getBroker().getName());

		square.setPriceLong(square.getLong().getOurPrice() == null ? BigDecimal.ZERO : square.getLong().getOurPrice());
		square.setRefLong(square.getLong().getOurRef());

		square.setPriceShort(
				square.getShort().getOurPrice() == null ? BigDecimal.ZERO : square.getShort().getOurPrice());
		square.setRefShort(square.getShort().getOurRef());

		/*
		square.setQuantity(FormatQuantity(square.getQuantity(), square.getCommodity(), square.getCommodityId()));
		square.setQuantityShort(
				FormatQuantity(square.getQuantityShort(), square.getCommodity(), square.getCommodityId()));
		square.setQuantityLong(
				FormatQuantity(square.getQuantityLong(), square.getCommodity(), square.getCommodityId()));
		 */
		return square;
	}

	public List<String> GetUserPermissionByUser(String userid) {
		// 获取当前用户的所有角色
		DetachedCriteria dc = DetachedCriteria.forClass(UserRole.class);
		dc.add(Restrictions.eq("UserId", userid));
		List<UserRole> curUserRole = this.userRoleRepo.GetQueryable(UserRole.class).where(dc).toCacheList();

		DetachedCriteria dc2 = DetachedCriteria.forClass(RolePermission.class);
		dc2.add(Restrictions.eq("IsQuery", true));
		List<RolePermission> rPs = this.rolePermissionRepo.GetQueryable(RolePermission.class).where(dc2).toCacheList();
		List<RolePermission> roles = new ArrayList<>();
		for (RolePermission rolePermission : rPs) {
			for (UserRole userRole : curUserRole) {
				if (rolePermission.getRoleId().equals(userRole.getRoleId())) {
					roles.add(rolePermission);
				}
			}
		}

		List<UserRole> rls = this.userRoleRepo.GetQueryable(UserRole.class).toCacheList();
		List<String> mySubUsersId = new ArrayList<>();
		for (UserRole ur : rls) {
			for (RolePermission cur : roles) {
				if (ur.getRoleId().equals(cur.getSubRoleId())) {
					mySubUsersId.add(ur.getUserId());
				}
			}
		}
		List<String> myUsersId = new ArrayList<>();
		for (UserRole ur : rls) {
			for (UserRole cur : curUserRole) {
				if (ur.getRoleId().equals(cur.getRoleId())) {
					myUsersId.add(ur.getUserId());
				}
			}
		}
		HashSet<String> s = new HashSet<>();
		s.addAll(mySubUsersId);
		s.addAll(myUsersId);
		return new ArrayList<>(s);
	}

	@Override
	public void quotationPrice(String commodityName, Map<String, BigDecimal> priceMap) {

		String mainmetalUrl = PropertiesUtil.getString("futures.quotation.mainmetal").replace("{0}",
				commodityName.toLowerCase());
		String mJson = HttpClientUtil.requestByGetMethod(mainmetalUrl);
		if (StringUtils.isNotBlank(mJson)) {
			Map<String, Object> map = JSONUtil.doConvertJson2Map(mJson);
			if (map.containsKey("code") && String.valueOf(map.get("code")).equals(CODE)) {
				String mainmeta = (String) map.get("data");
				String current = PropertiesUtil.getString("futures.quotation.current").replace("{0}", mainmeta);
				String cJson = HttpClientUtil.requestByGetMethod(current);
				if (StringUtils.isNotBlank(cJson)) {
					try {
						MainmetaResult mainmetaResult = (MainmetaResult) JSONUtil.doConvertStringToBean(cJson,
								MainmetaResult.class);
						if (mainmetaResult != null && mainmetaResult.getMainmeta().size() > 0) {
							Mainmeta m = mainmetaResult.getMainmeta().get(0);
							priceMap.put(commodityName, m.getLastPrice());
						}
					} catch (IOException e) {
						logger.error("获取期货行情出错:", e);
					}
				} else {
					logger.info("没有获取到行情数据.");
				}
			} else {
				logger.info("没有获取到主力金属.");
			}
		}

	}

	@Override
	public Criteria AddPermission(String userId, Criteria criteria, String fieldName) {
		List<String> userIds = GetUserPermissionByUser(userId);
		if (userIds.size() > 0) {
			criteria.add(Restrictions.in(fieldName, userIds));
		}
		return criteria;
	}

	@Override
	public List<String> GetSquare4BrokenIdByUsersId(List<String> createdId) {
		List<String> listId = null;
		if (createdId.size() > 0) {
			String ids = "";
			for (String p : createdId) {
				ids += "'" + p + "',";
			}
			ids = ids.substring(0, ids.length() - 1);
			StringBuffer sb = new StringBuffer();
			sb.append("select c.Id from  Physical.Position a ");
			sb.append(" inner join Physical.Position4Broker b on a.Id =b.OriginalId  ");
			sb.append(" inner join Physical.Square4Broker c on b.Id =c.ShortId");
			sb.append(" where a.CreatedId in(");
			sb.append(ids);
			sb.append(" )");
			sb.append(" union all");
			sb.append(" select c.Id from  Physical.Position a ");
			sb.append(" inner join Physical.Position4Broker b on a.Id =b.OriginalId  ");
			sb.append(" inner join Physical.Square4Broker c on b.Id =c.LongId");
			sb.append(" where a.CreatedId in(");
			sb.append(ids);
			sb.append(" )");

			listId = this.squareRepo.ExecuteCorrectlySql2(sb.toString());
		}
		return listId;
	}

	@Override
	public List<String> GetSquareIdByUsersId(List<String> createdId) {
		List<String> listId = null;
		if (createdId.size() > 0) {
			String ids = "";
			for (String p : createdId) {
				ids += "'" + p + "',";
			}
			ids = ids.substring(0, ids.length() - 1);
			StringBuffer sb = new StringBuffer();
			sb.append("select b.Id from Physical.Position  a ");
			sb.append(" inner join Physical.Square b on a.Id = b.ShortId where a.CreatedId in(");
			sb.append(ids);
			sb.append(" )");
			sb.append(" union all ");
			sb.append("select b.Id from Physical.Position  a ");
			sb.append(" inner join Physical.Square b on a.Id = b.LongId where a.CreatedId in(");
			sb.append(ids);
			sb.append(" )");

			listId = this.squareRepo.ExecuteCorrectlySql2(sb.toString());
		}
		return listId;
	}

	@Override
	public void SyncCustomerBalance(String customerId, String legalId, String commodityId) {
		DetachedCriteria dc = DetachedCriteria.forClass(Dictionary.class);
		dc.add(Restrictions.isNotNull("ParentId"));
		dc.add(Restrictions.eq("Code", "Currency"));
		dc.add(Restrictions.eq("IsHidden", false));
		List<Dictionary> currencies = this.dicRepo.GetQueryable(Dictionary.class).where(dc).toList();
		if (currencies == null || currencies.size() == 0) {
			return;//// 避免初始化时间没有币种、运行出错，加上这个检验。
		}

		for (Dictionary dictionary : currencies) {
			// 获取所有相关批次 客户、品种、抬头、未付款、已开票
			DetachedCriteria dc1 = DetachedCriteria.forClass(Lot.class);
			dc1.add(Restrictions.eq("CustomerId", customerId));
			dc1.add(Restrictions.eq("CommodityId", commodityId));
			dc1.add(Restrictions.eq("LegalId", legalId));
			dc1.add(Restrictions.eq("Currency", dictionary.getValue()));
			dc1.add(Restrictions.ne("IsFunded", true));
			dc1.add(Restrictions.eq("IsInvoiced", true));
			List<Lot> lots = this.lotRepo.GetQueryable(Lot.class).where(dc1).toList();
			// 计算发票总额
			DetachedCriteria dc2 = DetachedCriteria.forClass(Invoice.class);
			dc2.add(Restrictions.eq("CustomerId", customerId));
			dc2.add(Restrictions.eq("CommodityId", commodityId));
			dc2.add(Restrictions.eq("LegalId", legalId));
			dc2.add(Restrictions.eq("Currency", dictionary.getValue()));
			List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(dc2).toList();
			// 计算已收金额总额
			DetachedCriteria dc3 = DetachedCriteria.forClass(Fund.class);
			dc3.add(Restrictions.eq("CustomerId", customerId));
			dc3.add(Restrictions.eq("CommodityId", commodityId));
			dc3.add(Restrictions.eq("LegalId", legalId));
			dc3.add(Restrictions.eq("Currency", dictionary.getValue()));
			dc3.add(Restrictions.eq("IsExecuted", true));
			List<Fund> delivered = this.funRepo.GetQueryable(Fund.class).where(dc3).toList();
			if (lots == null || lots.size() == 0)
				continue;

			DetachedCriteria dc4 = DetachedCriteria.forClass(CustomerBalance.class);
			dc4.add(Restrictions.eq("CustomerId", customerId));
			dc4.add(Restrictions.eq("CommodityId", commodityId));
			dc4.add(Restrictions.eq("LegalId", legalId));
			dc4.add(Restrictions.eq("Currency", dictionary.getValue()));
			CustomerBalance customerBalance = this.customerBalanceRepo.GetQueryable(CustomerBalance.class).where(dc4)
					.firstOrDefault();
			if (customerBalance == null) {
				customerBalance = new CustomerBalance();
				customerBalance.setDueBalance(BigDecimal.ZERO);
				customerBalance.setPaidBalance(BigDecimal.ZERO);
				customerBalance.setLastBalance(BigDecimal.ZERO);
				customerBalance.setCustomerId(customerId);
				customerBalance.setLegalId(legalId);
				customerBalance.setCommodityId(commodityId);
				customerBalance.setInitBalance(BigDecimal.ZERO);
				customerBalance.setCurrency(dictionary.getValue());
				customerBalance.setCreatedAt(new Date());
				customerBalance.setCreatedBy("Robot");
			}
			BigDecimal dueBalance = BigDecimal.ZERO;
			BigDecimal paidBalance = BigDecimal.ZERO;
			BigDecimal sIAmount = BigDecimal.ZERO;
			BigDecimal bIAmount = BigDecimal.ZERO;
			if (invoices != null && invoices.size() > 0) {
				// 销售/采购发票总金额
				for (Lot a : lots) {
					for (Invoice b : invoices) {
						if (a.getId().equals(b.getLotId())) {
							if (a.getSpotDirection().equals("S")) {
								sIAmount = sIAmount.add(b.getAmount());
							}
							if (a.getSpotDirection().equals("B")) {
								bIAmount = bIAmount.add(b.getAmount());
							}
						}
					}
				}
				dueBalance = sIAmount.subtract(bIAmount);
			}
			BigDecimal sIAmount1 = BigDecimal.ZERO;
			BigDecimal bIAmount1 = BigDecimal.ZERO;
			if (delivered != null && delivered.size() > 0) {
				// 已收/已付总金额
				for (Lot a : lots) {
					for (Fund b : delivered) {
						if (a.getId().equals(b.getLotId())) {
							if (a.getSpotDirection().equals("S")) {
								sIAmount1 = sIAmount1.add(b.getAmount());
							}
							if (a.getSpotDirection().equals("B")) {
								bIAmount1 = bIAmount1.add(b.getAmount());
							}
						}
					}
				}
				paidBalance = bIAmount1.subtract(sIAmount1);

			}
			customerBalance.setDueBalance(dueBalance);
			customerBalance.setPaidBalance(paidBalance);
			customerBalance.setLastBalance(customerBalance.getInitBalance().add(dueBalance).add(paidBalance));
			this.customerBalanceRepo.SaveOrUpdate(customerBalance);
		}
	}

	@Override
	public List<CustomerBalance> txSyncCustomerBalance(String customerId, String legalId, String commodityId) {

		List<CustomerBalance> customerBalances = new ArrayList<>();

		DetachedCriteria dc = DetachedCriteria.forClass(Dictionary.class);
		dc.add(Restrictions.isNotNull("ParentId"));
		dc.add(Restrictions.eq("Code", "Currency"));
		dc.add(Restrictions.eq("IsHidden", false));
		List<Dictionary> currencies = this.dicRepo.GetQueryable(Dictionary.class).where(dc).toList();
		if (currencies == null || currencies.size() == 0) {
			return customerBalances;//// 避免初始化时间没有币种、运行出错，加上这个检验。
		}

		for (Dictionary dictionary : currencies) {
			// 获取所有相关批次 客户、品种、抬头、未付款、已开票
			DetachedCriteria dc1 = DetachedCriteria.forClass(Lot.class);
			dc1.add(Restrictions.eq("CustomerId", customerId));
			dc1.add(Restrictions.eq("CommodityId", commodityId));
			dc1.add(Restrictions.eq("LegalId", legalId));
			dc1.add(Restrictions.eq("Currency", dictionary.getValue()));
			dc1.add(Restrictions.ne("IsFunded", true));
			dc1.add(Restrictions.eq("IsInvoiced", true));
			List<Lot> lots = this.lotRepo.GetQueryable(Lot.class).where(dc1).toList();
			// 计算发票总额
			DetachedCriteria dc2 = DetachedCriteria.forClass(Invoice.class);
			dc2.add(Restrictions.eq("CustomerId", customerId));
			dc2.add(Restrictions.eq("CommodityId", commodityId));
			dc2.add(Restrictions.eq("LegalId", legalId));
			dc2.add(Restrictions.eq("Currency", dictionary.getValue()));
			List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(dc2).toList();
			// 计算已收金额总额
			DetachedCriteria dc3 = DetachedCriteria.forClass(Fund.class);
			dc3.add(Restrictions.eq("CustomerId", customerId));
			dc3.add(Restrictions.eq("CommodityId", commodityId));
			dc3.add(Restrictions.eq("LegalId", legalId));
			dc3.add(Restrictions.eq("Currency", dictionary.getValue()));
			dc3.add(Restrictions.eq("IsExecuted", true));
			List<Fund> delivered = this.funRepo.GetQueryable(Fund.class).where(dc3).toList();
			if (lots == null || lots.size() == 0)
				continue;

			DetachedCriteria dc4 = DetachedCriteria.forClass(CustomerBalance.class);
			dc4.add(Restrictions.eq("CustomerId", customerId));
			dc4.add(Restrictions.eq("CommodityId", commodityId));
			dc4.add(Restrictions.eq("LegalId", legalId));
			dc4.add(Restrictions.eq("Currency", dictionary.getValue()));
			CustomerBalance customerBalance = this.customerBalanceRepo.GetQueryable(CustomerBalance.class).where(dc4)
					.firstOrDefault();
			if (customerBalance == null) {
				customerBalance = new CustomerBalance();
				customerBalance.setDueBalance(BigDecimal.ZERO);
				customerBalance.setPaidBalance(BigDecimal.ZERO);
				customerBalance.setLastBalance(BigDecimal.ZERO);
				customerBalance.setCustomerId(customerId);
				customerBalance.setLegalId(legalId);
				customerBalance.setCommodityId(commodityId);
				customerBalance.setInitBalance(BigDecimal.ZERO);
				customerBalance.setCurrency(dictionary.getValue());
				customerBalance.setCreatedAt(new Date());
				customerBalance.setCreatedBy("Robot");
			}
			BigDecimal dueBalance = BigDecimal.ZERO;
			BigDecimal paidBalance = BigDecimal.ZERO;
			BigDecimal sIAmount = BigDecimal.ZERO;
			BigDecimal bIAmount = BigDecimal.ZERO;
			if (invoices != null && invoices.size() > 0) {
				// 销售/采购发票总金额
				for (Lot a : lots) {
					for (Invoice b : invoices) {
						if (a.getId().equals(b.getLotId())) {
							if (a.getSpotDirection().equals("S")) {
								sIAmount = sIAmount.add(b.getAmount());
							}
							if (a.getSpotDirection().equals("B")) {
								bIAmount = bIAmount.add(b.getAmount());
							}
						}
					}
				}
				dueBalance = sIAmount.subtract(bIAmount);
			}
			BigDecimal sIAmount1 = BigDecimal.ZERO;
			BigDecimal bIAmount1 = BigDecimal.ZERO;
			if (delivered != null && delivered.size() > 0) {
				// 已收/已付总金额
				for (Lot a : lots) {
					for (Fund b : delivered) {
						if (a.getId().equals(b.getLotId())) {
							if (a.getSpotDirection().equals("S")) {
								sIAmount1 = sIAmount1.add(b.getAmount());
							}
							if (a.getSpotDirection().equals("B")) {
								bIAmount1 = bIAmount1.add(b.getAmount());
							}
						}
					}
				}
				paidBalance = bIAmount1.subtract(sIAmount1);

			}
			customerBalance.setDueBalance(dueBalance);
			customerBalance.setPaidBalance(paidBalance);
			customerBalance.setLastBalance(customerBalance.getInitBalance().add(dueBalance).add(paidBalance));
			customerBalances.add(customerBalance);
		}
		return customerBalances;
	}

	@Override
	public Fund SimplifyDataFund(Fund fund) {

		if (fund == null)
			return null;
		Fund obj = com.smm.ctrm.util.BeanUtils.copy(fund);

		if (fund.getLot() != null)
			obj.setFullNo(fund.getLot().getFullNo());
		if (fund.getCustomer() != null)
			obj.setCustomerName(fund.getCustomer().getName());
		if (fund.getCustomerTitle() != null)
			obj.setCustomerTitleName(fund.getCustomerTitle().getName());
		if (fund.getLegal() != null)
			obj.setLegalName(fund.getLegal().getName());
		if (fund.getInvoice() != null) {
			obj.setInvoiceQuantity(fund.getInvoice().getQuantity());
			obj.setInvoiceAmount(fund.getInvoice().getAmount());
			obj.setInvoiceDueDate(fund.getInvoice().getDueDate());
			obj.setInvoiceTradeDate(fund.getInvoice().getTradeDate());
			obj.setInvoiceNo(fund.getInvoice().getInvoiceNo());
		}
		if (fund.getCommodity() != null) {
			obj.setCommodityCode(fund.getCommodity().getCode());
			obj.setCommodityName(fund.getCommodity().getName());
			obj.setUnit(fund.getCommodity().getUnit());
		}
		/*
		obj.setQuantity(FormatQuantity(obj.getQuantity(), fund.getCommodity(), fund.getCommodityId()));
		obj.setInvoiceQuantity(FormatQuantity(obj.getInvoiceQuantity(), fund.getCommodity(), fund.getCommodityId()));
 		*/
		// obj.setInvoice(SimplifyData(fund.getInvoice()));
		if (fund.getBankReceipt() != null)
			obj.setBankReceipt(fund.getBankReceipt());
		return obj;
	}

	public Invoice SimplifyDataInvoice(Invoice invoice) {
		if (invoice == null)
			return null;
		if (invoice.getLot() != null) {
			invoice.setFullNo(invoice.getLot().getFullNo());
		}
		// 增加此处的原因是，因为增加了一个发票包含多个批次交付明细的情形
		else {
			if (invoice.getContract() != null)
				invoice.setFullNo(invoice.getContract().getHeadNo());
		}

		if (invoice.getCustomer() != null)
			invoice.setCustomerName(invoice.getCustomer().getName());
		if (invoice.getCustomerTitle() != null)
			invoice.setCustomerTitleName(invoice.getCustomerTitle().getName());
		if (invoice.getLegal() != null)
			invoice.setLegalName(invoice.getLegal().getName());

		if (invoice.getCommodity() != null) {
			invoice.setCommodityCode(invoice.getCommodity().getCode());
			invoice.setCommodityName(invoice.getCommodity().getName());
			invoice.setUnit(invoice.getCommodity().getUnit());
		}

		/*
		invoice.setQuantity(FormatQuantity(invoice.getQuantity(), invoice.getCommodity(), invoice.getCommodityId()));
		invoice.setQuantityDrafted(
				FormatQuantity(invoice.getQuantityDrafted(), invoice.getCommodity(), invoice.getCommodityId()));
	 	*/
		Invoice obj = com.smm.ctrm.util.BeanUtils.copy(invoice);
		// if (invoice.getInvoiceGrades() != null)
		// obj.setInvoiceGrades(SimplifyData(invoice.getInvoiceGrades()));
		// if (invoice.getStorages() != null)
		// obj.setStorages(SimplifyData(invoice.getStorages()));
		// if (invoice.getNotices()!=null)
		// obj.setNotices(SimplifyData(invoice.getNotices()));

		return obj;
	}

	/**
	 * 判断当前批次是否有做过业务上的更新 点/拆/票/资/保
	 * 
	 * @param lotId
	 * @return
	 */
	@Override
	public boolean IsUpdateByLot(String lotId) {
		boolean rivalIsUpdate = false;
		Lot lot = lotRepo.getOneById(lotId, Lot.class);
		List<Invoice> invoice = getLotRefEntity(lot.getId(), Invoice.class);
		List<Fund> fund = getLotRefEntity(lot.getId(), Fund.class);
		List<Position> position = getLotRefEntity(lot.getId(), Position.class);
		List<Pricing> pricing = getLotRefEntity(lot.getId(), Pricing.class);
		if (invoice != null && fund != null && position != null && pricing != null && !lot.getIsSplitLot()
				&& !lot.getIsOriginalLot()) {
			if (invoice.size() > 0 || fund.size() > 0 || position.size() > 0 || pricing.size() > 0) {
				rivalIsUpdate = true;
			}
		}
		return rivalIsUpdate;
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> getLotRefEntity(String lotId, Class<T> clazz) {
		DetachedCriteria dc = DetachedCriteria.forClass(Invoice.class).add(Restrictions.eq("LotId", lotId));
		return (List<T>) lotRepo.getHibernateTemplate().findByCriteria(dc);
	}

	/**
	 * 重置当前合同的所有相关的运输明细的状态
	 * 
	 * @param contract
	 * @return
	 */
	@Override
	public ActionResult<String> ResetStorageByContract(Contract contract) {
		// 1、如果销售订单相关货物的标记还原（a、来源的来源IsBorrow=true b、来源的IsBorrow=true IsIn=false
		// c、当前明细删除）
		// 2、如果采购订单相关货物的标记还原（a、来源的countpartid2=null b、当前明细删除）
		try {
			List<Lot> lots = lotRepo.GetQueryable(Lot.class)
					.where(DetachedCriteria.forClass(Lot.class).add(Restrictions.eq("ContractId", contract.getId())))
					.toList();
			if (contract.getSpotDirection().equals(SpotType.Sell)) {
				for (Lot lot : lots) {
					List<Storage> storages = storageRepo.GetQueryable(Storage.class)
							.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId())))
							.toList();
					for (Storage storage : storages) {
						// 更新来源
						if (storage.getCounterpartyId3() != null) {
							Storage countparty3Storage = storageRepo.getOneById(storage.getCounterpartyId3(),
									Storage.class);
							if (countparty3Storage != null) {
								countparty3Storage.setIsBorrow(true);
								countparty3Storage.setIsIn(false);
								countparty3Storage.setCounterpartyId2(null);
								storageRepo.SaveOrUpdate(countparty3Storage);
								// 更新来源的来源
								Storage sourceCountparty3Storage = storageRepo
										.getOneById(countparty3Storage.getCounterpartyId3(), Storage.class);
								if (sourceCountparty3Storage != null) {
									sourceCountparty3Storage.setIsBorrow(true);
									storageRepo.SaveOrUpdate(sourceCountparty3Storage);
								}
							}
						}
						// 更新当前
						storage.setIsDeleted(true);
						storageRepo.SaveOrUpdate(storage);
					}
				}
			} else if (contract.getSpotDirection().equals(SpotType.Purchase)) {
				for (Lot lot : lots) {
					List<Storage> storages = storageRepo.GetQueryable(Storage.class)
							.where(DetachedCriteria.forClass(Storage.class).add(Restrictions.eq("LotId", lot.getId())))
							.toList();
					for (Storage storage : storages) {
						// 更新来源
						if (storage.getCounterpartyId3() != null) {
							Storage countparty3Storage = storageRepo.getOneById(storage.getCounterpartyId3(),
									Storage.class);
							if (countparty3Storage != null) {
								countparty3Storage.setCounterpartyId2(null);
								storageRepo.SaveOrUpdate(countparty3Storage);
							}
						}
						// 更新当前
						storage.setIsDeleted(true);
						storageRepo.SaveOrUpdate(storage);
					}
				}
			}
			return new ActionResult<>(true, MessageCtrm.ModifySuccess);
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return new ActionResult<>(false, MessageCtrm.ModifyFail + ex.getMessage());
		}

	}

	@Override
	public Boolean CanLotIsFunded_New(String lotId) {

		Lot curLot = this.lotRepo.getOneById(lotId, Lot.class);

		if (curLot == null)
			return false;

		if (curLot.getIsInvoiced()) {
			// 单批次发票
			DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
			where.add(Restrictions.eq("LotId", lotId));
			where.add(Restrictions.eq("Is4MultiLots", false));
			where.add(Restrictions.or(Restrictions.eq("PFA", "F"), Restrictions.eq("PFA", "A")));

			List<Invoice> invoiceDoneForSingle = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

			if (invoiceDoneForSingle.size() > 0
					&& invoiceDoneForSingle.stream().anyMatch(x -> x.getIsExecuted() == false)) {
				return false;
			}
			// 多批次发票
			List<Invoice> invoiceDoneForMulti = GetMultilotInvoicesByLotId(lotId).stream()
					.filter(invoice -> invoice.getPFA().equals("F") || invoice.getPFA().equals("A"))
					.collect(Collectors.toList());

			if (invoiceDoneForMulti.size() > 0
					&& invoiceDoneForMulti.stream().anyMatch(x -> x.getIsExecuted() == false)) {
				return false;
			}

			if (invoiceDoneForSingle.size() > 0 || invoiceDoneForMulti.size() > 0) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Lot> setInvoice4Lot_NewInvoice(Lot lot, Invoice invoice) {
		if (lot == null || lot.getId() == null)
			return null;
		Lot originalLot = lot;

		// where.add(Restrictions.eq("SplitFromId", originalLot.getId()));

		// List<Lot> lots2 =
		// this.lotRepo.GetQueryable(Lot.class).where(where).toList(); //
		// 拆分批次，//注意包含全部拆分批次

		// List<String> lots2Ids =
		// lots2.stream().map(Lot::getId).collect(Collectors.toList());

		// if (lots2Ids != null && lots2Ids.size() > 0)
		// djc.add(Restrictions.in("LotId", lots2Ids));
		DetachedCriteria where = DetachedCriteria.forClass(Invoice.class);
		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.or(Restrictions.eq("PFA", ActionStatus.InvoiceType_Final),
				Restrictions.eq("PFA", ActionStatus.InvoiceType_Adjust)));

		List<Invoice> invoices = this.invoiceRepo.GetQueryable(Invoice.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Invoice i : invoices) {
			sumQuantity = sumQuantity.add(i.getQuantity());
		}

		BigDecimal _Quantity = originalLot.getQuantityOriginal() == null ? originalLot.getQuantity()
				: originalLot.getQuantityOriginal();

		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(), _Quantity,
				originalLot.getMoreOrLess());

		boolean isInvoiced = DecimalUtil.nullToZero(lotQuantity.getQuantityLess()).compareTo(sumQuantity.abs()) <= 0
				&& sumQuantity.abs().compareTo(DecimalUtil.nullToZero(lotQuantity.getQuantityMore())) <= 0;

		// yunsq 2016年6月27日 21:20:29 添加对收票的控制
		if ((invoice.getPFA().equals(InvoiceType.Final) || invoice.getPFA().equals(InvoiceType.Adjust))
				&& invoice.getMT().equals(MT4Invoice.Make))
			isInvoiced = false;

		lot.setIsInvoiced(isInvoiced);

		return null;
	}

	@Override
	public boolean UpdateLotFeesByLotId_New(String lotid) {
		if (lotid == null)
			return false;

		Lot lot = this.lotRepo.getOneById(lotid, Lot.class);

		UpdateLotFees(lot);

		return true;

	}

	@Override
	public QuantityMaL CalculateQuantityOfLotDeliveryed_New(Lot lot) {

		if (lot == null)
			return null;
		Lot originalLot = lot;

		DetachedCriteria where = DetachedCriteria.forClass(Storage.class);
		where.add(Restrictions.eq("LotId", originalLot.getId()));
		where.add(Restrictions.eq("IsHidden", false));

		List<Storage> storages = this.storageRepo.GetQueryable(Storage.class).where(where).toList();

		BigDecimal sumQuantity = BigDecimal.ZERO;
		for (Storage s : storages) {
			sumQuantity = sumQuantity.add(s.getQuantity());
		}

		BigDecimal _Quantity = originalLot.getQuantityOriginal() == null ? originalLot.getQuantity()
				: originalLot.getQuantityOriginal();

		QuantityMaL lotQuantity = getQuantityMoreorLess(originalLot.getMoreOrLessBasis(), _Quantity,
				originalLot.getMoreOrLess());

		lotQuantity.setQuantityDeliveryed(sumQuantity);

		return lotQuantity;
	}

	@Override
	public void UpdateInvoiceStatus_NewInvoice(List<Lot> lots) {
		if (lots == null)
			return;

		for (Lot lot : lots) {
			Lot lottmp = lotRepo.getOneById(lot.getId(), Lot.class);

			if (lottmp != null) {
				lottmp.setIsInvoiced(lot.getIsInvoiced());
				lotRepo.SaveOrUpdate(lottmp);
			}
		}

	}

	private static final char[] cnNumber = { '零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖' };
	private static final char[] cnUnit = { '分', '角', '元', '拾', '佰', '仟', '万', '拾', '佰', '仟', '亿', '拾', '佰', '仟', '兆',
			'拾', '佰', '仟' };
	/**
	 * 金额小写转大写
	 * 
	 * @param num
	 * @return
	 */
	@Override
	public String MoneyLowerToLarge(BigDecimal num) {
		String MoneyString = num.toPlainString();
		String[] tmpString = MoneyString.split("\\.");
		String intString = MoneyString; // 默认为整数
		String decString = ""; // 保存小数部分字串
		String rmbCapital = ""; // 保存中文大写字串
		int k;
		int j;
		int n;
		if (tmpString.length > 1) {
			intString = tmpString[0]; // 取整数部分
			decString = tmpString[1]; // 取小数部分
		}
		decString += "00";
		decString = decString.substring(0, 2); // 保留两位小数位
		intString += decString;

		try {
			k = intString.length() - 1;
			if (k > 0 && k < 18) {
				for (int i = 0; i <= k; i++) {
					j = (int) intString.charAt(i) - 48;
					n = i + 1 >= k ? (int) intString.charAt(k) - 48 : (int) intString.charAt(i + 1) - 48;
					if (j == 0) {
						if (k - i == 2 || k - i == 6 || k - i == 10 || k - i == 14) {
							rmbCapital += cnUnit[k - i];
						} else {
							if (n != 0) {
								rmbCapital += cnNumber[j];
							}
						}
					} else {
						rmbCapital = rmbCapital + cnNumber[j] + cnUnit[k - i];
					}
				}

				rmbCapital = rmbCapital.replace("兆亿万", "兆");
				rmbCapital = rmbCapital.replace("兆亿", "兆");
				rmbCapital = rmbCapital.replace("亿万", "亿");
				rmbCapital = trimStart(rmbCapital, "元");
				rmbCapital = trimStart(rmbCapital, "零");

				return rmbCapital;
			} else {
				RuntimeException ex = new RuntimeException("超出转换范围时，返回零长字串");
				logger.error(ex.getMessage(), ex);
				return "";
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage(), ex);
			return ""; // 含有非数值字符时，返回零长字串
		}
	}

	private String trimStart(String str, String trim) {
		if (trim.length() != 1) {
			throw new RuntimeException(MessageCtrm.ParamError);
		}
		while (true) {
			if (str.startsWith(trim)) {
				str = str.substring(1);
			} else {
				break;
			}
		}
		return str;
	}

	@Override
	public List<CInvoice> SimplifyDataInvoicePager(List<CInvoice> invoices) {
		for (int i = 0; i < invoices.size(); i++) {
			CInvoice invoice = invoices.get(i);
			if (invoice == null)
				continue;

			if (invoice.getLot() != null) {
				invoice.setFullNo(invoice.getLot().getFullNo());
			} else {
				// 增加此处的原因是，因为增加了一个发票包含多个批次交付明细的情形
				if (invoice.getContract() != null)
					invoice.setFullNo(invoice.getContract().getHeadNo());
			}

			if (invoice.getCustomer() != null)
				invoice.setCustomerName(invoice.getCustomer().getName());

			if (invoice.getCustomerTitle() != null)
				invoice.setCustomerTitleName(invoice.getCustomerTitle().getName());

			if (invoice.getLegal() != null)
				invoice.setLegalName(invoice.getLegal().getName());

			if (invoice.getCommodity() != null) {
				invoice.setCommodityCode(invoice.getCommodity().getCode());
				invoice.setCommodityName(invoice.getCommodity().getName());
				invoice.setUnit(invoice.getCommodity().getUnit());
			}

			/*
			invoice.setQuantity(
					FormatQuantity(invoice.getQuantity(), invoice.getCommodity(), invoice.getCommodityId()));
			invoice.setQuantityDrafted(
					FormatQuantity(invoice.getQuantityDrafted(), invoice.getCommodity(), invoice.getCommodityId()));
		 	*/

			CInvoice obj = com.smm.ctrm.util.BeanUtils.copy(invoice);

			for (CStorage s : invoice.getStorages()) {
				s.setBviSource(null);
			}
			for (CStorage s : invoice.getNotices()) {
				s.setBviSource(null);
			}
			obj.setStorages(invoice.getStorages());
			obj.setNotices(invoice.getNotices());
			invoices.set(i, obj);
		}
		return invoices;
	}

	// 生成二维码
	@Override
	public String CreateCode_Simple(String content) {
		String qrCodeFilePath = "";
		QrCodePrintUtil util = new QrCodePrintUtil();
		BufferedImage bim = util.getQR_CODEBufferedImage(content, BarcodeFormat.QR_CODE, 300, 300,
				util.getDecodeHintType());
		/*
		 * List<String> list = new ArrayList<>(); list.add("1.1");
		 * list.add("1.2"); list.add("1.3"); list.add("1.4"); bim =
		 * util.addContent_QRCode(bim, "", list);
		 */

		try {
			// ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// baos.flush();
			// ImageIO.write(bim, "png", baos);

			// 二维码生成的路径，但是实际项目中，我们是把这生成的二维码显示到界面上的，因此下面的折行代码可以注释掉
			// 可以看到这个方法最终返回的是这个二维码的imageBase64字符串
			// 前端用 <img src="data:image/png;base64,${imageBase64QRCode}"/>
			// 其中${imageBase64QRCode}对应二维码的imageBase64字符串
			String directory = System.getProperty("smm.ctrm") + "OutPdfs";
			String filepath = directory + "/QrCode_" + DateUtil.doFormatDate(new Date(), "yyyymmddhhmmssSSS") + ".jpg";
			File directoryFile = new File(directory);
			if (!directoryFile.exists()) {
				directoryFile.mkdirs();
			}
			ImageIO.write(bim, "jpg", new File(filepath));
			// String imageBase64QRCode =
			// Base64.byteArrayToBase64(baos.toByteArray());
			// baos.close();
			qrCodeFilePath = filepath;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return qrCodeFilePath;

	}

	/**
	 * 为库存管理而生
	 */
	public List<C2Storage> SimplifyDataStorageHolding(List<C2Storage> storages,boolean mark) {
		
		int size=storages.size();
		
		for (int i = 0; i < size; i++) {
			if (storages.get(0) == null)
				return null;

			C2Storage obj = storages.get(i);
			C2Storage storage = com.smm.ctrm.util.BeanUtils.copy(obj);
			storage.setQuantityInvoiced(obj.getQuantity()); // QuantityInvoiced
			if (obj.getPrice() != null && BigDecimal.ZERO.compareTo(obj.getPrice()) != 0) {
				storage.setAmount(storage.getQuantityInvoiced().multiply(obj.getPrice()));
			} else {
				storage.setAmount(storage.getQuantityInvoiced()
						.multiply(obj.getPriceProvisional() != null ? obj.getPriceProvisional() : BigDecimal.ZERO));
			}

			if (obj.getLot() != null) {
				storage.setFullNo(obj.getLot().getFullNo());
				storage.setLotDischarging(obj.getLot().getDischarging());
				storage.setLotEstimateSaleDate(obj.getLot().getEstimateSaleDate());
				storage.setDeliveryTermOfLot(obj.getLot().getDeliveryTerm());
			}
			if (obj.getCustomer() != null) {
				storage.setCustomerName(obj.getCustomer().getName());
			}
			if (obj.getLegal() != null) {
				storage.setLegalName(obj.getLegal().getName());
			}
			if (obj.getBrand() != null) {
				storage.setBrandName(obj.getBrand().getName());
			}
			if (obj.getWarehouse() != null) {
				storage.setWarehouseName(obj.getWarehouse().getName());
			}
			if (obj.getCommodity() != null) {
				storage.setQuantityPerLot(obj.getCommodity().getQuantityPerLot());
				storage.setDigits(obj.getCommodity().getDigits());
				storage.setCommodityCode(obj.getCommodity().getCode());
				storage.setCommodityName(obj.getCommodity().getName());
				storage.setUnit(obj.getCommodity().getUnit());
			}
			/*
			// 格式化数量
			storage.setQuantity(FormatQuantity(obj.getQuantity(), obj.getCommodity(), obj.getCommodityId()));
			storage.setGross(FormatQuantity(obj.getGross(), obj.getCommodity(), obj.getCommodityId()));
			storage.setUnCxQuantity(FormatQuantity(obj.getUnCxQuantity(), obj.getCommodity(), obj.getCommodityId()));
			storage.setGrossAtFactory(
					FormatQuantity(obj.getGrossAtFactory(), obj.getCommodity(), obj.getCommodityId()));
			storage.setDiff(FormatQuantity(obj.getDiff(), obj.getCommodity(), obj.getCommodityId()));
			storage.setStorageQuantity(
					FormatQuantity(storage.getGross().subtract(new BigDecimal(obj.getBundles() * 0.003f)),
							obj.getCommodity(), obj.getCommodityId()));
			*/
			if (obj.getCustomer() != null)
				storage.setCustomer(com.smm.ctrm.util.BeanUtils.copy(obj.getCustomer()));

			if (obj.getLot() != null)
				storage.setLot(com.smm.ctrm.util.BeanUtils.copy(obj.getLot()));

			if (mark&&storage.getUnCxQuantity() != null) {
				storage.setQuantity(storage.getUnCxQuantity());
			}
			
			storages.set(i, storage);
		}
		return storages;
	}
}
