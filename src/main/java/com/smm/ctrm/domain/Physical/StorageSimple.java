
package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
import com.smm.ctrm.domain.Basis.Brand;
import com.smm.ctrm.domain.Basis.Commodity;
import com.smm.ctrm.domain.Basis.Customer;
import com.smm.ctrm.domain.Basis.Legal;
import com.smm.ctrm.domain.Basis.Origin;
import com.smm.ctrm.domain.Basis.Spec;
import com.smm.ctrm.domain.Basis.Warehouse;

@Entity
@Table(name = "Storage", schema = "Physical")
public class StorageSimple extends HibernateEntity {
	private static final long serialVersionUID = 1461832991345L;
	/**
	 * 仅是为了发票之用途
	 */
	@Column(name = "QuantityInvoiced")
	@JsonProperty(value = "QuantityInvoiced")
	private BigDecimal QuantityInvoiced;
	/**
	 *
	 */
	@Column(name = "Premium")
	@JsonProperty(value = "Premium")
	private BigDecimal Premium;
	/**
	 * 预计销售升贴水
	 */
	@Column(name = "Premium4EstSale")
	@JsonProperty(value = "Premium4EstSale")
	private BigDecimal Premium4EstSale;
	/**
	 *
	 */
	@Column(name = "Major")
	@JsonProperty(value = "Major")
	private BigDecimal Major;
	/**
	 *
	 */
	@Column(name = "PriceProvisional")
	@JsonProperty(value = "PriceProvisional")
	private BigDecimal PriceProvisional;
	/**
	 *
	 */
	@Column(name = "Amount")
	@JsonProperty(value = "Amount")
	private BigDecimal Amount;
	/**
	 * 是否手工修改升贴水（如果此标记为true，此手工输入为准）
	 */
	@Column(name = "IsMannalPremium")
	@JsonProperty(value = "IsMannalPremium")
	private Boolean IsMannalPremium;
	/**
	 * 采购月
	 */
	@Column(name = "PurchaseMonth")
	@JsonProperty(value = "PurchaseMonth", defaultValue = "0")
	private Integer PurchaseMonth;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "QuantityPerLot", defaultValue = "0")
	private Integer QuantityPerLot;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Digits", defaultValue = "0")
	private Integer Digits;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FullNo")
	private String FullNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerName")
	private String CustomerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "EstCustomerName")
	private String EstCustomerName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Lot4FullNo")
	private String Lot4FullNo;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "LegalName")
	private String LegalName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityName")
	private String CommodityName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CommodityCode")
	private String CommodityCode;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "Unit")
	private String Unit;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "SpecName")
	private String SpecName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "WarehouseName")
	private String WarehouseName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "FullNoOfCounterparty")
	private String FullNoOfCounterparty;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "CustomerNameOfCounterparty")
	private String CustomerNameOfCounterparty;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "SourceProjectName")
	private String SourceProjectName;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "DeliveryTermOfLot")
	private String DeliveryTermOfLot;

	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "SplitCount")
	private BigDecimal SplitCount;

	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "IsSelect")
	private Boolean IsSelect;

	/**
	 * 发票号，批次盈亏结算显示用
	 */
	@Transient
	@JsonProperty(value = "InvoiceNo")
	private String InvoiceNo;

	/**
	 * 入库货物批次号，批次盈亏结算显示用
	 */
	@Transient
	@JsonProperty(value = "LotNo")
	private String LotNo;

	/**
	 * 是否退货
	 */
	@Column(name = "IsReturn")
	@JsonProperty(value = "IsReturn")
	private Boolean IsReturn;
	/**
	 * 退货时间
	 */
	@Column(name = "ReturnTime")
	@JsonProperty(value = "ReturnTime")
	private Date ReturnTime;
	/**
	 * 退货的用户ID
	 */
	@Column(name = "ReturnUserId")
	@JsonProperty(value = "ReturnUserId")
	private String ReturnUserId;
	/**
	 * 根据仓库净重计算的金额
	 */
	@Transient
	@JsonProperty(value = "Amount2")
	private BigDecimal Amount2;
	/**
	 * 批次交付地点（到达地点）
	 */
	@Transient
	@JsonProperty(value = "LotDischarging")
	private String LotDischarging;
	/**
	 * 批次预计销售日期
	 */
	@Transient
	@JsonProperty(value = "LotEstimateSaleDate")
	private Date LotEstimateSaleDate;
	/**
	 * 订单（采购/销售）
	 */
	@Transient
	@JsonProperty(value = "ContractSpotDirection")
	private String ContractSpotDirection;
	/**
	 * Initial Spread
	 */
	@Column(name = "Spread4Initial")
	@JsonProperty(value = "Spread4Initial")
	private BigDecimal Spread4Initial;
	/**
	 * QP Spread
	 */
	@Column(name = "Spread4Qp")
	@JsonProperty(value = "Spread4Qp")
	private BigDecimal Spread4Qp;
	/**
	 * Lot Spread
	 */
	@Column(name = "Spread4Lot")
	@JsonProperty(value = "Spread4Lot")
	private BigDecimal Spread4Lot;
	/**
	 * 销售头寸保值均价（SM)
	 */
	@Column(name = "Future2")
	@JsonProperty(value = "Future2")
	private BigDecimal Future2;
	/**
	 * 采购头寸保值均价+Spread（SM)
	 */
	@Column(name = "Future3")
	@JsonProperty(value = "Future3")
	private BigDecimal Future3;
	/**
	 * 采购现货点价价格（SM)
	 */
	@Column(name = "SpotPrice3")
	@JsonProperty(value = "SpotPrice3")
	private BigDecimal SpotPrice3;
	/**
	 * 采购现货升贴水（SM)
	 */
	@Column(name = "Premium3")
	@JsonProperty(value = "Premium3")
	private BigDecimal Premium3;
	/**
	 * 采购现货实际费用（SM)
	 */
	@Column(name = "RealFee3")
	@JsonProperty(value = "RealFee3")
	private BigDecimal RealFee3;
	/**
	 * 销售现货点价价格（BVI)
	 */
	@Column(name = "SpotPrice4")
	@JsonProperty(value = "SpotPrice4")
	private BigDecimal SpotPrice4;
	/**
	 * 销售现货升贴水（BVI)
	 */
	@Column(name = "Premium4")
	@JsonProperty(value = "Premium4")
	private BigDecimal Premium4;
	/**
	 * 销售实际费用（BVI)
	 */
	@Column(name = "RealFee4")
	@JsonProperty(value = "RealFee4")
	private BigDecimal RealFee4;
	/**
	 * 采购头寸保值均价（BVI)
	 */
	@Column(name = "Future5")
	@JsonProperty(value = "Future5")
	private BigDecimal Future5;
	/**
	 * 采购现货点价价格（BVI)
	 */
	@Column(name = "SpotPrice5")
	@JsonProperty(value = "SpotPrice5")
	private BigDecimal SpotPrice5;
	/**
	 * 采购现货升贴水（BVI)
	 */
	@Column(name = "Premium5")
	@JsonProperty(value = "Premium5")
	private BigDecimal Premium5;
	/**
	 * 采购现货实际费用（BVI)
	 */
	@Column(name = "RealFee5")
	@JsonProperty(value = "RealFee5")
	private BigDecimal RealFee5;
	/**
	 * 单位盈亏
	 */
	@Column(name = "PnLUnit")
	@JsonProperty(value = "PnLUnit")
	private BigDecimal PnLUnit;
	/**
	 * 盈亏
	 */
	@Column(name = "PnL4Storage")
	@JsonProperty(value = "PnL4Storage")
	private BigDecimal PnL4Storage;
	/**
	 * 业务日期
	 */
	@Column(name = "TradeDate")
	@JsonProperty(value = "TradeDate")
	private Date TradeDate;
	/**
	 * { T=入库, M=出库} 【这不是属于数据字典】
	 */
	@Column(name = "MT", length = 1)
	@JsonProperty(value = "MT")
	private String MT;
	/**
	 *
	 */
	@Column(name = "CardNo", length = 64)
	@JsonProperty(value = "CardNo")
	private String CardNo;
	/**
	 * 纸质单据编号，对于
	 */
	@Column(name = "StorageNo", length = 64)
	@JsonProperty(value = "StorageNo")
	private String StorageNo;
	/**
	 * { BL, WR, GL, TL, NA}(Dict)
	 */
	@Column(name = "StorageType", length = 30)
	@JsonProperty(value = "StorageType")
	private String StorageType;
	/**
	 *
	 */
	@Column(name = "TransitStatus", length = 64)
	@JsonProperty(value = "TransitStatus")
	private String TransitStatus;
	/**
	 * 产品
	 */
	@Column(name = "Product")
	@JsonProperty(value = "Product")
	private String Product;
	/**
	 * 毛重。默认就是仓库的毛重，一般不太需要这个值。
	 */
	@Column(name = "Gross")
	@JsonProperty(value = "Gross")
	private BigDecimal Gross;
	/**
	 *
	 */
	@Transient
	@JsonProperty(value = "StorageQuantity")
	private BigDecimal StorageQuantity;
	/**
	 * 净重，相当于实际出入库的数量
	 */
	@Column(name = "Quantity")
	@JsonProperty(value = "Quantity")
	private BigDecimal Quantity;
	/**
	 * 未冲销重量
	 */
	@Column(name = "UnCxQuantity")
	@JsonProperty(value = "UnCxQuantity")
	private BigDecimal UnCxQuantity;
	/**
	 * 包括了Fee，综合成本
	 */
	@Column(name = "Price")
	@JsonProperty(value = "Price")
	private BigDecimal Price;
	/**
	 * 综合成本，包括在Price中
	 */
	@Column(name = "Fee")
	@JsonProperty(value = "Fee")
	private BigDecimal Fee;
	/**
	 * 各种杂费(实际发生)
	 */
	@Column(name = "RealFee")
	@JsonProperty(value = "RealFee")
	private BigDecimal RealFee;
	/**
	 * 各种杂费(预估)
	 */
	@Column(name = "EstimateFee")
	@JsonProperty(value = "EstimateFee")
	private BigDecimal EstimateFee;
	/**
	 *
	 */
	@Column(name = "Currency", length = 3)
	@JsonProperty(value = "Currency")
	private String Currency;
	/**
	 * 是否初始化导入
	 */
	@Column(name = "IsVirtual")
	@JsonProperty(value = "IsVirtual")
	private Boolean IsVirtual;
	/**
	 * 包装
	 */
	@Column(name = "Package", length = 64)
	@JsonProperty(value = "Package")
	private String Package;
	/**
	 * 船名
	 */
	@Column(name = "Vessel", length = 64)
	@JsonProperty(value = "Vessel")
	private String Vessel;
	/**
	 * 装运地点
	 */
	@Column(name = "Loading")
	@JsonProperty(value = "Loading")
	private String Loading;
	/**
	 * 中转港
	 */
	@Column(name = "Intermediate")
	@JsonProperty(value = "Intermediate")
	private String Intermediate;
	/**
	 * 卸货地点
	 */
	@Column(name = "Discharging")
	@JsonProperty(value = "Discharging")
	private String Discharging;
	/**
	 * 提单日期/装运日期
	 */
	@Column(name = "BLDate")
	@JsonProperty(value = "BLDate")
	private Date BLDate;
	/**
	 * 装运日期登记/调整日期
	 */
	@Column(name = "BLDate4Update")
	@JsonProperty(value = "BLDate4Update")
	private Date BLDate4Update;
	/**
	 *
	 */
	@Column(name = "ETD")
	@JsonProperty(value = "ETD")
	private Date ETD;
	/**
	 *
	 */
	@Column(name = "ETA")
	@JsonProperty(value = "ETA")
	private Date ETA;
	/**
	 *
	 */
	@Column(name = "ATD")
	@JsonProperty(value = "ATD")
	private Date ATD;
	/**
	 *
	 */
	@Column(name = "ATA")
	@JsonProperty(value = "ATA")
	private Date ATA;
	/**
	 *
	 */
	@Column(name = "Comments", length = 2000)
	@JsonProperty(value = "Comments")
	private String Comments;
	/**
	 * 是否已出库。对于出库记录，IsOut永远为TRUE
	 */
	@Column(name = "IsOut")
	@JsonProperty(value = "IsOut")
	private Boolean IsOut;
	/**
	 * 是否初始化导入
	 */
	@Column(name = "IsInitiated")
	@JsonProperty(value = "IsInitiated")
	private Boolean IsInitiated;
	/**
	 * 是否被分拆
	 */
	@Column(name = "IsSplitted")
	@JsonProperty(value = "IsSplitted")
	private Boolean IsSplitted;
	/**
	 * 是否已经用于盈亏结算
	 */
	@Column(name = "IsSettled")
	@JsonProperty(value = "IsSettled")
	private Boolean IsSettled;
	/**
	 *
	 */
	@Column(name = "IsInvoiced")
	@JsonProperty(value = "IsInvoiced")
	private Boolean IsInvoiced;
	/**
	 * 是否月末的会计过账
	 */
	@Column(name = "IsAccounted")
	@JsonProperty(value = "IsAccounted")
	private Boolean IsAccounted;
	/**
	 *
	 */
	@Column(name = "IsNoticed")
	@JsonProperty(value = "IsNoticed")
	private Boolean IsNoticed;
	/**
	 * 计算溢短装率的基准{ OnQuantity, OnPercentage }
	 */
	@Column(name = "MoreOrLessBasis")
	@JsonProperty(value = "MoreOrLessBasis")
	private String MoreOrLessBasis;
	/**
	 * 溢短装率的值，默认 = 0M
	 */
	@Column(name = "MoreOrLess")
	@JsonProperty(value = "MoreOrLess")
	private BigDecimal MoreOrLess;
	/**
	 * 通知货量单号
	 */
	@Column(name = "NoticeBillNo")
	@JsonProperty(value = "NoticeBillNo")
	private String NoticeBillNo;
	/**
	 * 是否已冲销
	 */
	@Column(name = "IsReversed")
	@JsonProperty(value = "IsReversed")
	private Boolean IsReversed;
	/**
	 * 回购的货物只有在开票后才是真实回购
	 */
	@Column(name = "IsReBuy")
	@JsonProperty(value = "IsReBuy")
	private Boolean IsReBuy;
	/**
	 * 品牌名称
	 */
	@Column(name = "BrandName")
	@JsonProperty(value = "BrandName")
	private String BrandName;
	/**
	 * 车辆位置
	 */
	@Column(name = "LocationStatus")
	@JsonProperty(value = "LocationStatus")
	private String LocationStatus;
	/**
	 * 序号
	 */
	@Column(name = "No")
	@JsonProperty(value = "No")
	private String No;
	/**
	 * 项目名称
	 */
	@Column(name = "ProjectName")
	@JsonProperty(value = "ProjectName")
	private String ProjectName;
	/**
	 * 来源项目名称（此名称不可更改）
	 */
	@Column(name = "ProjectName2")
	@JsonProperty(value = "ProjectName2")
	private String ProjectName2;
	/**
	 * 卡车号
	 */
	@Column(name = "TruckNo")
	@JsonProperty(value = "TruckNo")
	private String TruckNo;
	/**
	 * 拖车号
	 */
	@Column(name = "Trailer")
	@JsonProperty(value = "Trailer")
	private String Trailer;
	/**
	 * 装车时间
	 */
	@Column(name = "LoadDate")
	@JsonProperty(value = "LoadDate")
	private Date LoadDate;
	/**
	 * 离境时间
	 */
	@Column(name = "DepartureDate")
	@JsonProperty(value = "DepartureDate")
	private Date DepartureDate;
	/**
	 * 件数 = 捆数
	 */
	@Column(name = "Bundles")
	@JsonProperty(value = "Bundles")
	private Integer Bundles;
	/**
	 * 出厂毛重
	 */
	@Column(name = "GrossAtFactory")
	@JsonProperty(value = "GrossAtFactory")
	private BigDecimal GrossAtFactory;
	/**
	 * 误差 = 出厂毛重 - 仓库毛重
	 */
	@Column(name = "Diff")
	@JsonProperty(value = "Diff")
	private BigDecimal Diff;
	/**
	 * 误差比 = 误差 / 出厂毛重
	 */
	@Column(name = "DiffRate")
	@JsonProperty(value = "DiffRate")
	private BigDecimal DiffRate;
	/**
	 * 对于BVI从工厂买入，IsIn=true表示已经与采购合同关联；反之，则未关联
	 */
	@Column(name = "IsIn", nullable = true)
	@JsonProperty(value = "IsIn")
	private Boolean IsIn;
	/**
	 * 是否拆分出来的
	 */
	@Column(name = "IsMerged")
	@JsonProperty(value = "IsMerged")
	private Boolean IsMerged;
	/**
	 * 检验日期
	 */
	@Column(name = "TestDate")
	@JsonProperty(value = "TestDate")
	private Date TestDate;
	/**
	 * 检验编号
	 */
	@Column(name = "TestNo")
	@JsonProperty(value = "TestNo")
	private String TestNo;
	/**
	 * 等级
	 */
	@Column(name = "Grade")
	@JsonProperty(value = "Grade")
	private String Grade;
	/**
	 * 等级ID
	 */
	@Column(name = "GradeId")
	@JsonProperty(value = "GradeId")
	private String GradeId;
	/**
	 * 交付方式
	 */
	@Column(name = "DeliveryTerm")
	@JsonProperty(value = "DeliveryTerm")
	private String DeliveryTerm;
	/**
	 * 是否属于工厂的商品运输明细
	 */
	@Column(name = "IsFactory")
	@JsonProperty(value = "IsFactory")
	private Boolean IsFactory;
	/**
	 * 内部实体
	 */
	@Column(name = "LegalId")
	@JsonProperty(value = "LegalId")
	private String LegalId;
	// @JsonBackReference("Storage_Legal")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Legal.class)
	// @JoinColumn(name = "LegalId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name = "none"))
	// @NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Legal")
	private Legal Legal;
	/**
	 * 最原始的来自工厂的SourceId
	 */
	@Column(name = "BviSourceId")
	@JsonProperty(value = "BviSourceId")
	private String BviSourceId;
	// @JsonBackReference("Storage_BviSource")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "BviSourceId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "BviSource")
	private Storage BviSource;
	/**
	 * 记录这个值的目的，是为了备查。
	 */
	@Column(name = "OriginalId")
	@JsonProperty(value = "OriginalId")
	private String OriginalId;
	// @JsonBackReference("Storage_Original")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "OriginalId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Original")
	private Storage Original;
	/**
	 * 只有分拆的出入库单，才有这个值
	 */
	@Column(name = "SourceId")
	@JsonProperty(value = "SourceId")
	private String SourceId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	@JoinColumn(name = "SourceId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Source")
	private Storage Source;
	/**
	 * 通知货量ID
	 */
	@Column(name = "NoticeStorageId")
	@JsonProperty(value = "NoticeStorageId")
	private String NoticeStorageId;
	// @JsonBackReference("Storage_NoticeStorage")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "NoticeStorageId", insertable = false, updatable =
	// false, foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "NoticeStorage")
	private Storage NoticeStorage;
	/**
	 * 对手方的出入库记录的标识
	 */
	@Column(name = "CounterpartyId")
	@JsonProperty(value = "CounterpartyId")
	private String CounterpartyId;
	// @JsonBackReference("Storage_Counterparty")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "CounterpartyId", insertable = false, updatable =
	// false, foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Counterparty")
	private Storage Counterparty;
	/**
	 * 2016-01-26 记录下游的Storage.Id
	 */
	@Column(name = "CounterpartyId2")
	@JsonProperty(value = "CounterpartyId2")
	private String CounterpartyId2;
	// @JsonBackReference("Storage_Counterparty2")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "CounterpartyId2", insertable = false, updatable =
	// false, foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Counterparty2")
	private Storage Counterparty2;
	/**
	 * 记录上游的Storage.Id
	 */
	@Column(name = "CounterpartyId3")
	@JsonProperty(value = "CounterpartyId3")
	private String CounterpartyId3;
	// @JsonBackReference("Storage_Counterparty3")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "CounterpartyId3", insertable = false, updatable =
	// false, foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Counterparty3")
	private Storage Counterparty3;
	/**
	 * 属于哪个批次的提单
	 */
	@Column(name = "LotId")
	@JsonProperty(value = "LotId")
	private String LotId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@Fetch(FetchMode.JOIN)
	@JoinColumn(name = "LotId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot")
	private LotSimple Lot;
	/**
	 * 属于哪个合同的提单
	 */
	@Column(name = "ContractId")
	@JsonProperty(value = "ContractId")
	private String ContractId;
	// @JsonBackReference("Storage_Contract")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Contract.class)
	// @JoinColumn(name = "ContractId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name = "none"))
	// @NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Contract")
	private Contract Contract;
	/**
	 * 品种
	 */
	@Column(name = "CommodityId")
	@JsonProperty(value = "CommodityId")
	private String CommodityId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Commodity.class)
	@JoinColumn(name = "CommodityId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Commodity")
	private Commodity Commodity;
	/**
	 * 品牌
	 */
	@Column(name = "BrandId")
	@JsonProperty(value = "BrandId")
	private String BrandId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Brand.class)
	@JoinColumn(name = "BrandId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Brand")
	private Brand Brand;
	/**
	 * 仓库
	 */
	@Column(name = "WarehouseId")
	@JsonProperty(value = "WarehouseId")
	private String WarehouseId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Warehouse.class)
	@JoinColumn(name = "WarehouseId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Warehouse")
	private Warehouse Warehouse;
	/**
	 * 规格
	 */
	@Column(name = "SpecId")
	@JsonProperty(value = "SpecId")
	private String SpecId;
	// @JsonBackReference("Storage_Spec")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Spec.class)
	// @JoinColumn(name = "SpecId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name = "none"))
	// @NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Spec")
	private Spec Spec;
	/**
	 * 原产地
	 */
	@Column(name = "OriginId")
	@JsonProperty(value = "OriginId")
	private String OriginId;
	// @JsonBackReference("Storage_Origin")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Origin.class)
	// @JoinColumn(name = "OriginId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name = "none"))
	// @NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Origin")
	private Origin Origin;
	/**
	 * 交易对手 = 客户或者供应商
	 */
	@Column(name = "CustomerId")
	@JsonProperty(value = "CustomerId")
	private String CustomerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "CustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Customer")
	private Customer Customer;
	/**
	 * 预计销售客户（供用户在交付明细中输入）
	 */
	@Column(name = "EstCustomerId")
	@JsonProperty(value = "EstCustomerId")
	private String EstCustomerId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Customer.class)
	@JoinColumn(name = "EstCustomerId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "EstCustomer")
	private Customer EstCustomer;
	/**
	 * 预计销售批次（供用户在交付明细中输入）
	 */
	@Column(name = "Lot4EstSaleId")
	@JsonProperty(value = "Lot4EstSaleId")
	private String Lot4EstSaleId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Lot.class)
	@JoinColumn(name = "Lot4EstSaleId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@NotFound(action = NotFoundAction.IGNORE)
	@JsonProperty(value = "Lot4EstSale")
	private LotSimple Lot4EstSale;
	/**
	 * 
	 */
	@Transient
	@JsonProperty(value = "MergedStorages")
	private List<Storage> MergedStorages;
	/**
	 * 通知货量关联的交付明细
	 */
	@Transient
	@JsonProperty(value = "NoticedStorages")
	private List<Storage> NoticedStorages;
	/**
	 * 多对多：杂费合计 - 商品明细
	 */
	// @ManyToMany(fetch = FetchType.EAGER, targetEntity = SummaryFees.class)
	// @JoinTable(name = "SummFeeStorage", schema = "Physical", joinColumns = {
	// @JoinColumn(name = "StorageId") }, inverseJoinColumns = {
	// @JoinColumn(name = "SummaryFeesId") }, foreignKey = @ForeignKey(name =
	// "none"))
	// @Cascade(CascadeType.ALL)
	// @Fetch(FetchMode.SUBSELECT)
	// @NotFound(action = NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "SummaryFeesList")
	private List<SummaryFees> SummaryFeesList;
	/**
	 * 只有被合并的，才有这个值。合并以后，不再在列表出现。
	 */
	@Column(name = "MergeId")
	@JsonProperty(value = "MergeId")
	private String MergeId;
	// @JsonBackReference("Storage_Merge")
	// @ManyToOne(fetch = FetchType.EAGER, targetEntity = Storage.class)
	// @JoinColumn(name = "MergeId", insertable = false, updatable = false,
	// foreignKey = @ForeignKey(name="none"))
	// @NotFound(action=NotFoundAction.IGNORE)
	@Transient
	@JsonProperty(value = "Merge")
	private Storage Merge;
	/**
	 * 创建者Id
	 */
	@Column(name = "CreatedId")
	@JsonProperty(value = "CreatedId")
	private String CreatedId;
	/**
	 * 更新者Id
	 */
	@Column(name = "UpdatedId")
	@JsonProperty(value = "UpdatedId")
	private String UpdatedId;
	/**
	 * 按发票结算
	 */
	@Column(name = "PnLId")
	@JsonProperty(value = "PnLId")
	private String PnLId;
	// @JsonBackReference("Storage_InvoicePnL")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = InvoicePnL.class)
	@JoinColumn(name = "InvoicePnLId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "InvoicePnL")
	private InvoicePnL InvoicePnL;
	/**
	 * 按批次结算
	 */
	@Column(name = "LotPnLId")
	@JsonProperty(value = "LotPnLId")
	private String LotPnLId;
	// @JsonBackReference("Storage_LotPnL")
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = LotPnL.class)
	@JoinColumn(name = "LotPnLId", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "none"))
	@JsonProperty(value = "LotPnL")
	private LotPnL LotPnL;

	/**
	 * 货物来源
	 */
	@Column(name = "GoodsSource")
	@JsonProperty(value = "GoodsSource")
	private String GoodsSource;

	/**
	 * 是否借用
	 */
	@Column(name = "IsBorrow")
	@JsonProperty(value = "IsBorrow")
	private Boolean IsBorrow;

	/**
	 * 商品明细所属实体名
	 */
	@Column(name = "RefName")
	@JsonProperty(value = "RefName")
	private String RefName;

	/**
	 * 商品明细所属实体Id
	 */
	@Column(name = "RefId")
	@JsonProperty(value = "RefId")
	private String RefId;

	/**
	 * 重量单位
	 */
	@Transient
	@JsonProperty(value = "QuantityUnit")
	private String QuantityUnit;

	public BigDecimal getQuantityInvoiced() {
		return QuantityInvoiced;
	}

	public void setQuantityInvoiced(BigDecimal QuantityInvoiced) {
		this.QuantityInvoiced = QuantityInvoiced;
	}

	public BigDecimal getPremium() {
		return Premium;
	}

	public void setPremium(BigDecimal Premium) {
		this.Premium = Premium;
	}

	public BigDecimal getPremium4EstSale() {
		return Premium4EstSale;
	}

	public void setPremium4EstSale(BigDecimal Premium4EstSale) {
		this.Premium4EstSale = Premium4EstSale;
	}

	public BigDecimal getMajor() {
		return Major;
	}

	public void setMajor(BigDecimal Major) {
		this.Major = Major;
	}

	public BigDecimal getPriceProvisional() {
		return PriceProvisional;
	}

	public void setPriceProvisional(BigDecimal PriceProvisional) {
		this.PriceProvisional = PriceProvisional;
	}

	public BigDecimal getAmount() {
		return Amount;
	}

	public void setAmount(BigDecimal Amount) {
		this.Amount = Amount;
	}

	public Boolean getIsMannalPremium() {
		return IsMannalPremium;
	}

	public void setIsMannalPremium(Boolean IsMannalPremium) {
		this.IsMannalPremium = IsMannalPremium;
	}

	public Integer getPurchaseMonth() {
		return PurchaseMonth;
	}

	public void setPurchaseMonth(Integer PurchaseMonth) {
		this.PurchaseMonth = PurchaseMonth;
	}

	public Integer getQuantityPerLot() {
		return QuantityPerLot;
	}

	public void setQuantityPerLot(Integer QuantityPerLot) {
		this.QuantityPerLot = QuantityPerLot;
	}

	public Integer getDigits() {
		return Digits;
	}

	public void setDigits(Integer Digits) {
		this.Digits = Digits;
	}

	public String getFullNo() {
		return FullNo;
	}

	public void setFullNo(String FullNo) {
		this.FullNo = FullNo;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String CustomerName) {
		this.CustomerName = CustomerName;
	}

	public String getEstCustomerName() {
		return EstCustomerName;
	}

	public void setEstCustomerName(String EstCustomerName) {
		this.EstCustomerName = EstCustomerName;
	}

	public String getLot4FullNo() {
		return Lot4FullNo;
	}

	public void setLot4FullNo(String Lot4FullNo) {
		this.Lot4FullNo = Lot4FullNo;
	}

	public String getLegalName() {
		return LegalName;
	}

	public void setLegalName(String LegalName) {
		this.LegalName = LegalName;
	}

	public String getCommodityName() {
		return CommodityName;
	}

	public void setCommodityName(String CommodityName) {
		this.CommodityName = CommodityName;
	}

	public String getCommodityCode() {
		return CommodityCode;
	}

	public void setCommodityCode(String CommodityCode) {
		this.CommodityCode = CommodityCode;
	}

	public String getUnit() {
		return Unit;
	}

	public void setUnit(String Unit) {
		this.Unit = Unit;
	}

	public String getSpecName() {
		return SpecName;
	}

	public void setSpecName(String SpecName) {
		this.SpecName = SpecName;
	}

	public String getWarehouseName() {
		return WarehouseName;
	}

	public void setWarehouseName(String WarehouseName) {
		this.WarehouseName = WarehouseName;
	}

	public String getFullNoOfCounterparty() {
		return FullNoOfCounterparty;
	}

	public void setFullNoOfCounterparty(String FullNoOfCounterparty) {
		this.FullNoOfCounterparty = FullNoOfCounterparty;
	}

	public String getCustomerNameOfCounterparty() {
		return CustomerNameOfCounterparty;
	}

	public void setCustomerNameOfCounterparty(String CustomerNameOfCounterparty) {
		this.CustomerNameOfCounterparty = CustomerNameOfCounterparty;
	}

	public String getSourceProjectName() {
		return SourceProjectName;
	}

	public void setSourceProjectName(String SourceProjectName) {
		this.SourceProjectName = SourceProjectName;
	}

	public String getDeliveryTermOfLot() {
		return DeliveryTermOfLot;
	}

	public void setDeliveryTermOfLot(String DeliveryTermOfLot) {
		this.DeliveryTermOfLot = DeliveryTermOfLot;
	}

	public Boolean getIsReturn() {
		return IsReturn;
	}

	public void setIsReturn(Boolean IsReturn) {
		this.IsReturn = IsReturn;
	}

	public Date getReturnTime() {
		return ReturnTime;
	}

	public void setReturnTime(Date ReturnTime) {
		this.ReturnTime = ReturnTime;
	}

	public String getReturnUserId() {
		return ReturnUserId;
	}

	public void setReturnUserId(String ReturnUserId) {
		this.ReturnUserId = ReturnUserId;
	}

	public BigDecimal getAmount2() {
		return Amount2;
	}

	public void setAmount2(BigDecimal Amount2) {
		this.Amount2 = Amount2;
	}

	public String getLotDischarging() {
		return LotDischarging;
	}

	public void setLotDischarging(String LotDischarging) {
		this.LotDischarging = LotDischarging;
	}

	public Date getLotEstimateSaleDate() {
		return LotEstimateSaleDate;
	}

	public void setLotEstimateSaleDate(Date LotEstimateSaleDate) {
		this.LotEstimateSaleDate = LotEstimateSaleDate;
	}

	public String getContractSpotDirection() {
		return ContractSpotDirection;
	}

	public void setContractSpotDirection(String ContractSpotDirection) {
		this.ContractSpotDirection = ContractSpotDirection;
	}

	public BigDecimal getSpread4Initial() {
		return Spread4Initial;
	}

	public void setSpread4Initial(BigDecimal Spread4Initial) {
		this.Spread4Initial = Spread4Initial;
	}

	public BigDecimal getSpread4Qp() {
		return Spread4Qp;
	}

	public void setSpread4Qp(BigDecimal Spread4Qp) {
		this.Spread4Qp = Spread4Qp;
	}

	public BigDecimal getSpread4Lot() {
		return Spread4Lot;
	}

	public void setSpread4Lot(BigDecimal Spread4Lot) {
		this.Spread4Lot = Spread4Lot;
	}

	public BigDecimal getFuture2() {
		return Future2;
	}

	public void setFuture2(BigDecimal Future2) {
		this.Future2 = Future2;
	}

	public BigDecimal getFuture3() {
		return Future3;
	}

	public void setFuture3(BigDecimal Future3) {
		this.Future3 = Future3;
	}

	public BigDecimal getSpotPrice3() {
		return SpotPrice3;
	}

	public void setSpotPrice3(BigDecimal SpotPrice3) {
		this.SpotPrice3 = SpotPrice3;
	}

	public BigDecimal getPremium3() {
		return Premium3;
	}

	public void setPremium3(BigDecimal Premium3) {
		this.Premium3 = Premium3;
	}

	public BigDecimal getRealFee3() {
		return RealFee3;
	}

	public void setRealFee3(BigDecimal RealFee3) {
		this.RealFee3 = RealFee3;
	}

	public BigDecimal getSpotPrice4() {
		return SpotPrice4;
	}

	public void setSpotPrice4(BigDecimal SpotPrice4) {
		this.SpotPrice4 = SpotPrice4;
	}

	public BigDecimal getPremium4() {
		return Premium4;
	}

	public void setPremium4(BigDecimal Premium4) {
		this.Premium4 = Premium4;
	}

	public BigDecimal getRealFee4() {
		return RealFee4;
	}

	public void setRealFee4(BigDecimal RealFee4) {
		this.RealFee4 = RealFee4;
	}

	public BigDecimal getFuture5() {
		return Future5;
	}

	public void setFuture5(BigDecimal Future5) {
		this.Future5 = Future5;
	}

	public BigDecimal getSpotPrice5() {
		return SpotPrice5;
	}

	public void setSpotPrice5(BigDecimal SpotPrice5) {
		this.SpotPrice5 = SpotPrice5;
	}

	public BigDecimal getPremium5() {
		return Premium5;
	}

	public void setPremium5(BigDecimal Premium5) {
		this.Premium5 = Premium5;
	}

	public BigDecimal getRealFee5() {
		return RealFee5;
	}

	public void setRealFee5(BigDecimal RealFee5) {
		this.RealFee5 = RealFee5;
	}

	public BigDecimal getPnLUnit() {
		return PnLUnit;
	}

	public void setPnLUnit(BigDecimal PnLUnit) {
		this.PnLUnit = PnLUnit;
	}

	public BigDecimal getPnL4Storage() {
		return PnL4Storage;
	}

	public void setPnL4Storage(BigDecimal PnL4Storage) {
		this.PnL4Storage = PnL4Storage;
	}

	public Date getTradeDate() {
		return TradeDate;
	}

	public void setTradeDate(Date TradeDate) {
		this.TradeDate = TradeDate;
	}

	public String getMT() {
		return MT;
	}

	public void setMT(String MT) {
		this.MT = MT;
	}

	public String getCardNo() {
		return CardNo;
	}

	public void setCardNo(String CardNo) {
		this.CardNo = CardNo;
	}

	public String getStorageNo() {
		return StorageNo;
	}

	public void setStorageNo(String StorageNo) {
		this.StorageNo = StorageNo;
	}

	public String getStorageType() {
		return StorageType;
	}

	public void setStorageType(String StorageType) {
		this.StorageType = StorageType;
	}

	public String getTransitStatus() {
		return TransitStatus;
	}

	public void setTransitStatus(String TransitStatus) {
		this.TransitStatus = TransitStatus;
	}

	public String getProduct() {
		return Product;
	}

	public void setProduct(String Product) {
		this.Product = Product;
	}

	public BigDecimal getGross() {
		return Gross;
	}

	public void setGross(BigDecimal Gross) {
		this.Gross = Gross;
	}

	public BigDecimal getStorageQuantity() {
		return StorageQuantity;
	}

	public void setStorageQuantity(BigDecimal StorageQuantity) {
		this.StorageQuantity = StorageQuantity;
	}

	public BigDecimal getQuantity() {
		return Quantity;
	}

	public void setQuantity(BigDecimal Quantity) {
		this.Quantity = Quantity;
	}

	public BigDecimal getUnCxQuantity() {
		return UnCxQuantity;
	}

	public void setUnCxQuantity(BigDecimal UnCxQuantity) {
		this.UnCxQuantity = UnCxQuantity;
	}

	public BigDecimal getPrice() {
		return Price;
	}

	public void setPrice(BigDecimal Price) {
		this.Price = Price;
	}

	public BigDecimal getFee() {
		return Fee;
	}

	public void setFee(BigDecimal Fee) {
		this.Fee = Fee;
	}

	public BigDecimal getRealFee() {
		return RealFee;
	}

	public void setRealFee(BigDecimal RealFee) {
		this.RealFee = RealFee;
	}

	public BigDecimal getEstimateFee() {
		return EstimateFee;
	}

	public void setEstimateFee(BigDecimal EstimateFee) {
		this.EstimateFee = EstimateFee;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String Currency) {
		this.Currency = Currency;
	}

	public Boolean getIsVirtual() {
		return IsVirtual;
	}

	public void setIsVirtual(Boolean IsVirtual) {
		this.IsVirtual = IsVirtual;
	}

	public String getPackage() {
		return Package;
	}

	public void setPackage(String Package) {
		this.Package = Package;
	}

	public String getVessel() {
		return Vessel;
	}

	public void setVessel(String Vessel) {
		this.Vessel = Vessel;
	}

	public String getLoading() {
		return Loading;
	}

	public void setLoading(String Loading) {
		this.Loading = Loading;
	}

	public String getIntermediate() {
		return Intermediate;
	}

	public void setIntermediate(String Intermediate) {
		this.Intermediate = Intermediate;
	}

	public String getDischarging() {
		return Discharging;
	}

	public void setDischarging(String Discharging) {
		this.Discharging = Discharging;
	}

	public Date getBLDate() {
		return BLDate;
	}

	public void setBLDate(Date BLDate) {
		this.BLDate = BLDate;
	}

	public Date getBLDate4Update() {
		return BLDate4Update;
	}

	public void setBLDate4Update(Date BLDate4Update) {
		this.BLDate4Update = BLDate4Update;
	}

	public Date getETD() {
		return ETD;
	}

	public void setETD(Date ETD) {
		this.ETD = ETD;
	}

	public Date getETA() {
		return ETA;
	}

	public void setETA(Date ETA) {
		this.ETA = ETA;
	}

	public Date getATD() {
		return ATD;
	}

	public void setATD(Date ATD) {
		this.ATD = ATD;
	}

	public Date getATA() {
		return ATA;
	}

	public void setATA(Date ATA) {
		this.ATA = ATA;
	}

	public String getComments() {
		return Comments;
	}

	public void setComments(String Comments) {
		this.Comments = Comments;
	}

	public Boolean getIsOut() {
		return IsOut;
	}

	public void setIsOut(Boolean IsOut) {
		this.IsOut = IsOut;
	}

	public Boolean getIsInitiated() {
		return IsInitiated;
	}

	public void setIsInitiated(Boolean IsInitiated) {
		this.IsInitiated = IsInitiated;
	}

	public Boolean getIsSplitted() {
		return IsSplitted;
	}

	public void setIsSplitted(Boolean IsSplitted) {
		this.IsSplitted = IsSplitted;
	}

	public Boolean getIsSettled() {
		return IsSettled;
	}

	public void setIsSettled(Boolean IsSettled) {
		this.IsSettled = IsSettled;
	}

	public Boolean getIsInvoiced() {
		return IsInvoiced;
	}

	public void setIsInvoiced(Boolean IsInvoiced) {
		this.IsInvoiced = IsInvoiced;
	}

	public Boolean getIsAccounted() {
		return IsAccounted;
	}

	public void setIsAccounted(Boolean IsAccounted) {
		this.IsAccounted = IsAccounted;
	}

	public Boolean getIsNoticed() {
		return IsNoticed;
	}

	public void setIsNoticed(Boolean IsNoticed) {
		this.IsNoticed = IsNoticed;
	}

	public String getMoreOrLessBasis() {
		return MoreOrLessBasis;
	}

	public void setMoreOrLessBasis(String MoreOrLessBasis) {
		this.MoreOrLessBasis = MoreOrLessBasis;
	}

	public BigDecimal getMoreOrLess() {
		return MoreOrLess;
	}

	public void setMoreOrLess(BigDecimal MoreOrLess) {
		this.MoreOrLess = MoreOrLess;
	}

	public String getNoticeBillNo() {
		return NoticeBillNo;
	}

	public void setNoticeBillNo(String NoticeBillNo) {
		this.NoticeBillNo = NoticeBillNo;
	}

	public Boolean getIsReversed() {
		return IsReversed;
	}

	public void setIsReversed(Boolean IsReversed) {
		this.IsReversed = IsReversed;
	}

	public Boolean getIsReBuy() {
		return IsReBuy;
	}

	public void setIsReBuy(Boolean IsReBuy) {
		this.IsReBuy = IsReBuy;
	}

	public String getBrandName() {
		return BrandName;
	}

	public void setBrandName(String BrandName) {
		this.BrandName = BrandName;
	}

	public String getLocationStatus() {
		return LocationStatus;
	}

	public void setLocationStatus(String LocationStatus) {
		this.LocationStatus = LocationStatus;
	}

	public String getNo() {
		return No;
	}

	public void setNo(String No) {
		this.No = No;
	}

	public String getProjectName() {
		return ProjectName;
	}

	public void setProjectName(String ProjectName) {
		this.ProjectName = ProjectName;
	}

	public String getProjectName2() {
		return ProjectName2;
	}

	public void setProjectName2(String ProjectName2) {
		this.ProjectName2 = ProjectName2;
	}

	public String getTruckNo() {
		return TruckNo;
	}

	public void setTruckNo(String TruckNo) {
		this.TruckNo = TruckNo;
	}

	public String getTrailer() {
		return Trailer;
	}

	public void setTrailer(String Trailer) {
		this.Trailer = Trailer;
	}

	public Date getLoadDate() {
		return LoadDate;
	}

	public void setLoadDate(Date LoadDate) {
		this.LoadDate = LoadDate;
	}

	public Date getDepartureDate() {
		return DepartureDate;
	}

	public void setDepartureDate(Date DepartureDate) {
		this.DepartureDate = DepartureDate;
	}

	public Integer getBundles() {
		return Bundles;
	}

	public void setBundles(Integer Bundles) {
		this.Bundles = Bundles;
	}

	public BigDecimal getGrossAtFactory() {
		return GrossAtFactory;
	}

	public void setGrossAtFactory(BigDecimal GrossAtFactory) {
		this.GrossAtFactory = GrossAtFactory;
	}

	public BigDecimal getDiff() {
		return Diff;
	}

	public void setDiff(BigDecimal Diff) {
		this.Diff = Diff;
	}

	public BigDecimal getDiffRate() {
		return DiffRate;
	}

	public void setDiffRate(BigDecimal DiffRate) {
		this.DiffRate = DiffRate;
	}

	public Boolean getIsIn() {
		return IsIn;
	}

	public void setIsIn(Boolean IsIn) {
		this.IsIn = IsIn;
	}

	public Boolean getIsMerged() {
		return IsMerged;
	}

	public void setIsMerged(Boolean IsMerged) {
		this.IsMerged = IsMerged;
	}

	public Date getTestDate() {
		return TestDate;
	}

	public void setTestDate(Date TestDate) {
		this.TestDate = TestDate;
	}

	public String getTestNo() {
		return TestNo;
	}

	public void setTestNo(String TestNo) {
		this.TestNo = TestNo;
	}

	public String getGrade() {
		return Grade;
	}

	public void setGrade(String Grade) {
		this.Grade = Grade;
	}

	public String getGradeId() {
		return GradeId;
	}

	public void setGradeId(String GradeId) {
		this.GradeId = GradeId;
	}

	public String getDeliveryTerm() {
		return DeliveryTerm;
	}

	public void setDeliveryTerm(String DeliveryTerm) {
		this.DeliveryTerm = DeliveryTerm;
	}

	public Boolean getIsFactory() {
		return IsFactory;
	}

	public void setIsFactory(Boolean IsFactory) {
		this.IsFactory = IsFactory;
	}

	public String getLegalId() {
		return LegalId;
	}

	public void setLegalId(String LegalId) {
		this.LegalId = LegalId;
	}

	public Legal getLegal() {
		return Legal;
	}

	public void setLegal(Legal Legal) {
		this.Legal = Legal;
	}

	public String getBviSourceId() {
		return BviSourceId;
	}

	public void setBviSourceId(String BviSourceId) {
		this.BviSourceId = BviSourceId;
	}

	public Storage getBviSource() {
		return BviSource;
	}

	public void setBviSource(Storage BviSource) {
		this.BviSource = BviSource;
	}

	public String getOriginalId() {
		return OriginalId;
	}

	public void setOriginalId(String OriginalId) {
		this.OriginalId = OriginalId;
	}

	public Storage getOriginal() {
		return Original;
	}

	public void setOriginal(Storage Original) {
		this.Original = Original;
	}

	public String getSourceId() {
		return SourceId;
	}

	public void setSourceId(String SourceId) {
		this.SourceId = SourceId;
	}

	public Storage getSource() {
		return Source;
	}

	public void setSource(Storage Source) {
		this.Source = Source;
	}

	public String getNoticeStorageId() {
		return NoticeStorageId;
	}

	public void setNoticeStorageId(String NoticeStorageId) {
		this.NoticeStorageId = NoticeStorageId;
	}

	public Storage getNoticeStorage() {
		return NoticeStorage;
	}

	public void setNoticeStorage(Storage NoticeStorage) {
		this.NoticeStorage = NoticeStorage;
	}

	public String getCounterpartyId() {
		return CounterpartyId;
	}

	public void setCounterpartyId(String CounterpartyId) {
		this.CounterpartyId = CounterpartyId;
	}

	public Storage getCounterparty() {
		return Counterparty;
	}

	public void setCounterparty(Storage Counterparty) {
		this.Counterparty = Counterparty;
	}

	public Storage getCounterparty2() {
		return Counterparty2;
	}

	public void setCounterparty2(Storage Counterparty2) {
		this.Counterparty2 = Counterparty2;
	}

	public String getCounterpartyId3() {
		return CounterpartyId3;
	}

	public void setCounterpartyId3(String CounterpartyId3) {
		this.CounterpartyId3 = CounterpartyId3;
	}

	public Storage getCounterparty3() {
		return Counterparty3;
	}

	public void setCounterparty3(Storage Counterparty3) {
		this.Counterparty3 = Counterparty3;
	}

	public String getLotId() {
		return LotId;
	}

	public void setLotId(String LotId) {
		this.LotId = LotId;
	}

	public LotSimple getLot() {
		return Lot;
	}

	public void setLot(LotSimple Lot) {
		this.Lot = Lot;
	}

	public String getContractId() {
		return ContractId;
	}

	public void setContractId(String ContractId) {
		this.ContractId = ContractId;
	}

	public Contract getContract() {
		return Contract;
	}

	public void setContract(Contract Contract) {
		this.Contract = Contract;
	}

	public String getCommodityId() {
		return CommodityId;
	}

	public void setCommodityId(String CommodityId) {
		this.CommodityId = CommodityId;
	}

	public Commodity getCommodity() {
		return Commodity;
	}

	public void setCommodity(Commodity Commodity) {
		this.Commodity = Commodity;
	}

	public String getBrandId() {
		return BrandId;
	}

	public void setBrandId(String BrandId) {
		this.BrandId = BrandId;
	}

	public Brand getBrand() {
		return Brand;
	}

	public void setBrand(Brand Brand) {
		this.Brand = Brand;
	}

	public String getWarehouseId() {
		return WarehouseId;
	}

	public void setWarehouseId(String WarehouseId) {
		this.WarehouseId = WarehouseId;
	}

	public Warehouse getWarehouse() {
		return Warehouse;
	}

	public void setWarehouse(Warehouse Warehouse) {
		this.Warehouse = Warehouse;
	}

	public String getSpecId() {
		return SpecId;
	}

	public void setSpecId(String SpecId) {
		this.SpecId = SpecId;
	}

	public Spec getSpec() {
		return Spec;
	}

	public void setSpec(Spec Spec) {
		this.Spec = Spec;
	}

	public String getOriginId() {
		return OriginId;
	}

	public void setOriginId(String OriginId) {
		this.OriginId = OriginId;
	}

	public Origin getOrigin() {
		return Origin;
	}

	public void setOrigin(Origin Origin) {
		this.Origin = Origin;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String CustomerId) {
		this.CustomerId = CustomerId;
	}

	public Customer getCustomer() {
		return Customer;
	}

	public void setCustomer(Customer Customer) {
		this.Customer = Customer;
	}

	public String getEstCustomerId() {
		return EstCustomerId;
	}

	public void setEstCustomerId(String EstCustomerId) {
		this.EstCustomerId = EstCustomerId;
	}

	public Customer getEstCustomer() {
		return EstCustomer;
	}

	public void setEstCustomer(Customer EstCustomer) {
		this.EstCustomer = EstCustomer;
	}

	public String getLot4EstSaleId() {
		return Lot4EstSaleId;
	}

	public void setLot4EstSaleId(String Lot4EstSaleId) {
		this.Lot4EstSaleId = Lot4EstSaleId;
	}

	public LotSimple getLot4EstSale() {
		return Lot4EstSale;
	}

	public void setLot4EstSale(LotSimple Lot4EstSale) {
		this.Lot4EstSale = Lot4EstSale;
	}

	public List<Storage> getMergedStorages() {
		return MergedStorages;
	}

	public void setMergedStorages(List<Storage> MergedStorages) {
		this.MergedStorages = MergedStorages;
	}

	public List<Storage> getNoticedStorages() {
		return NoticedStorages;
	}

	public void setNoticedStorages(List<Storage> NoticedStorages) {
		this.NoticedStorages = NoticedStorages;
	}

	public List<SummaryFees> getSummaryFeesList() {
		return SummaryFeesList;
	}

	public void setSummaryFeesList(List<SummaryFees> SummaryFeesList) {
		this.SummaryFeesList = SummaryFeesList;
	}

	public String getMergeId() {
		return MergeId;
	}

	public void setMergeId(String MergeId) {
		this.MergeId = MergeId;
	}

	public Storage getMerge() {
		return Merge;
	}

	public void setMerge(Storage Merge) {
		this.Merge = Merge;
	}

	public String getCreatedId() {
		return CreatedId;
	}

	public void setCreatedId(String CreatedId) {
		this.CreatedId = CreatedId;
	}

	public String getUpdatedId() {
		return UpdatedId;
	}

	public void setUpdatedId(String UpdatedId) {
		this.UpdatedId = UpdatedId;
	}

	public String getPnLId() {
		return PnLId;
	}

	public void setPnLId(String PnLId) {
		this.PnLId = PnLId;
	}

	public InvoicePnL getInvoicePnL() {
		return InvoicePnL;
	}

	public void setInvoicePnL(InvoicePnL InvoicePnL) {
		this.InvoicePnL = InvoicePnL;
	}

	public String getLotPnLId() {
		return LotPnLId;
	}

	public void setLotPnLId(String LotPnLId) {
		this.LotPnLId = LotPnLId;
	}

	public LotPnL getLotPnL() {
		return LotPnL;
	}

	public void setLotPnL(LotPnL LotPnL) {
		this.LotPnL = LotPnL;
	}

	public BigDecimal getSplitCount() {
		return SplitCount;
	}

	public void setSplitCount(BigDecimal splitCount) {
		SplitCount = splitCount;
	}

	public Boolean getIsSelect() {
		return IsSelect == null ? Boolean.FALSE : IsSelect;
	}

	public void setIsSelect(Boolean isSelect) {
		IsSelect = isSelect;
	}

	public String getGoodsSource() {
		return GoodsSource;
	}

	public void setGoodsSource(String GoodsSource) {
		this.GoodsSource = GoodsSource;
	}

	public Boolean getIsBorrow() {
		return IsBorrow;
	}

	public void setIsBorrow(Boolean isBorrow) {
		IsBorrow = isBorrow;
	}

	public String getCounterpartyId2() {
		return CounterpartyId2;
	}

	public void setCounterpartyId2(String counterpartyId2) {
		CounterpartyId2 = counterpartyId2;
	}

	public String getRefName() {
		return RefName;
	}

	public void setRefName(String refName) {
		RefName = refName;
	}

	public String getRefId() {
		return RefId;
	}

	public void setRefId(String refId) {
		RefId = refId;
	}

	public String getInvoiceNo() {
		return InvoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		InvoiceNo = invoiceNo;
	}

	public String getLotNo() {
		return LotNo;
	}

	public void setLotNo(String lotNo) {
		LotNo = lotNo;
	}

	public String getQuantityUnit() {
		return QuantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		QuantityUnit = quantityUnit;
	}

}