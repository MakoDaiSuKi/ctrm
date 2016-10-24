package com.smm.ctrm.domain.Physical;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;

/**
 * Created by zhenghao on 2016/7/8.
 *
 *
 */
public class LotBase extends HibernateEntity {

    // 扩展成员: 用作报表
    @JsonProperty(value = "M2MPrice")
    private BigDecimal M2MPrice ;
    //价格差(实际的点价结果 - 市场价)
    @JsonProperty(value = "PriceDiff")
    private  BigDecimal PriceDiff ;

    //敞口
    @JsonProperty(value = "Exposure")
    private BigDecimal Exposure ;

    //未点数量
    @JsonProperty(value = "QuantityUnPriced")
    private  BigDecimal QuantityUnPriced ;

    //未开票数量
    @JsonProperty(value = "QuantityUnInvoiced")
    private  BigDecimal QuantityUnInvoiced ;
    /// <summary>
    /// 记录传递到前台的数量（用于后台判断数量是否发生变更）
    /// </summary>
    @JsonProperty(value = "QuantityBeforeChanged")
    private  BigDecimal QuantityBeforeChanged ;
    //endregion

    //region 扩展成员: 用作客户端的界面展示
    @JsonProperty(value = "")
    private  int Digits ;

    @JsonProperty(value = "CustomerName")
    private  String CustomerName ;

    @JsonProperty(value = "CustomerShortName")
    private  String CustomerShortName ;

    @JsonProperty(value = "TraderName")
    private  String TraderName ;

    @JsonProperty(value = "LegalCode")
    private  String LegalCode ;

    @JsonProperty(value = "LegalName")
    private  String LegalName ;

    @JsonProperty(value = "SpecName")
    private  String SpecName ;

    @JsonProperty(value = "OriginName")
    private  String OriginName ;

    @JsonProperty(value = "Unit")
    private  String Unit ;

    @JsonProperty(value = "MajorMarketName")
    private  String MajorMarketName ;
    //endregion

    /// <summary>
    /// 是否属于初始化业务数据
    /// </summary>
    @Column(name= "IsIniatiated")
    @JsonProperty(value = "IsIniatiated")
    private  Boolean IsIniatiated ;

    //region 专门用作Co或者其它矿石类的业务的属性
    /// <summary>
    /// 品位，百分比表示，e.g.0.66 = 66%
    /// </summary>
    @Column(name= "Grade")
    @JsonProperty(value = "Grade")
    private  BigDecimal Grade ;
    /// <summary>
    /// 计价折扣率，百分比表示，同上
    /// </summary>
    @Column(name= "Discount")
    @JsonProperty(value = "Discount")
    private  BigDecimal Discount ;
    //endregion


    //region 用于批次分拆

    ///// <summary>
    ///// 是否拆分出来的
    ///// </summary>
    //@Column(name= "IsSplitted")
    //private  Boolean IsSplitted ;
    ///// <summary>
    ///// 被拆分的来源批次
    ///// </summary>
    //@Column(name= "IsSourceOfSplitted")
    //private  Boolean IsSourceOfSplitted ;
    /// <summary>
    /// 是否回购订单
    /// </summary>
    @Column(name= "IsReBuy")
    @JsonProperty(value = "IsReBuy")
    private  Boolean IsReBuy ;
    /// <summary>
    /// 合同原始数量
    /// </summary>
    @Column(name= "QuantityOriginal")
    @JsonProperty(value = "QuantityOriginal")
    private  BigDecimal QuantityOriginal ;


    ///// <summary>
    ///// 分拆而来的批次Id
    ///// </summary>
    //@Column(name= "SplitFromId")
    //private  String SplitFromId ;

    /// <summary>
    /// 被批次拆分拆出的新批次
    /// </summary>
    @Column(name= "IsSplitLot")
    @JsonProperty(value = "IsSplitLot")
    private  Boolean IsSplitLot ;

    /// <summary>
    /// 批次拆分的原始批次
    /// </summary>
    @Column(name= "IsOriginalLot")
    @JsonProperty(value = "IsOriginalLot")
    private  Boolean IsOriginalLot ;

    /// <summary>
    /// 原始批次ID
    /// </summary>.
    @Column(name= "OriginalLotId")
    @JsonProperty(value = "OriginalLotId")
    private  String OriginalLotId ;


    //endregion


    //region 基本成员
    /// <summary>
    /// 计算溢短装率的基准{  OnQuantity, OnPercentage    }
    /// </summary>
    @Column(name= "MoreOrLessBasis")
    @JsonProperty(value = "MoreOrLessBasis")
    private  String MoreOrLessBasis ;

    /// <summary>
    /// 溢短装率的值，默认 = 0M
    /// </summary>
    @Column(name= "MoreOrLess")
    @JsonProperty(value = "MoreOrLess")
    private BigDecimal MoreOrLess ;

    /// <summary>
    /// 只应用于简单的业务场景
    /// </summary>
    @Column(name= "IsQuantityConfirmed")
    @JsonProperty(value = "IsQuantityConfirmed")
    private  Boolean IsQuantityConfirmed ;

    /// <summary>
    /// 背景色
    /// </summary>
    @Column(name= "MarkColor")
    @JsonProperty(value = "MarkColor")
    private  Integer MarkColor ;

    //region 用于送到api，但是不需要写入数据表的成员
    /// <summary>
    /// 是否允许覆盖合同编号
    /// </summary>
    @JsonProperty(value = "IsAllowOverrideContractNo")
    private  Boolean IsAllowOverrideContractNo ;

    //endregion

    /// <summary>
    /// 该批次的实际的开（收）票的日期
    /// </summary>
    @Column(name= "DateInvoiced")
    @JsonProperty(value = "DateInvoiced")
    private Date DateInvoiced ;

    /// <summary>
    /// 该批次的开（收）票的截止日期
    /// </summary>
    @Column(name= "DateDueInvoice")
    @JsonProperty(value = "DateDueInvoice")
    private  Date DateDueInvoice ;

    /// <summary>
    ///  备注
    /// </summary>
    @Column(name= "Comments", length = 2000)
    @JsonProperty(value = "Comments")
    private  String Comments ;

    /// <summary>
    /// 完整编号{CUP20141223/0, CUP20141220/10, CUP20141220/20,.....}
    /// </summary>
    @Column(name= "FullNo")
    @JsonProperty(value = "FullNo")
    private  String FullNo ;

    /// <summary>
    /// 合同头编号。headNo = commodity + P/S + serialNo, 类似“CUP140702001”
    /// </summary>
    @Column(name= "HeadNo", length = 64)
    @JsonProperty(value = "HeadNo")
    private  String HeadNo ;

    @Column(name= "PrefixNo", length = 64)
    @JsonProperty(value = "PrefixNo")
    private  String PrefixNo ;

    /// <summary>
    /// 流水号,类似“140702001”
    /// </summary>
    @Column(name= "SerialNo", length = 64)
    @JsonProperty(value = "SerialNo")
    private  String SerialNo ;

    /// <summary>
    /// 批次编号{0, 10,20,.....}
    /// </summary>
    @Column(name= "LotNo")
    @JsonProperty(value = "LotNo")
    private  int LotNo ;

    /// <summary>
    ///  批次文档号
    /// </summary>
    @Column(name= "DocumentNo", length = 128)
    @JsonProperty(value = "DocumentNo")
    private  String DocumentNo ;

    /// <summary>
    /// 盈亏
    /// </summary>
    @Column(name= "PnL")
    @JsonProperty(value = "PnL")
    private  BigDecimal PnL ;

    /// <summary>
    /// 合同数量
    /// </summary>
    @Column(name= "Quantity")
    @JsonProperty(value = "Quantity")
    private  BigDecimal Quantity ;

    /// <summary>
    /// 约定交付的最低数量
    /// </summary>
    @Column(name= "QuantityLess")
    @JsonProperty(value = "QuantityLess")
    private  BigDecimal QuantityLess ;

    /// <summary>
    /// 约定交付的最多数量
    /// </summary>
    @Column(name= "QuantityMore")
    @JsonProperty(value = "QuantityMore")
    private  BigDecimal QuantityMore ;

    /// <summary>
    /// 实际的交付数量
    /// </summary>
    @Column(name= "QuantityDelivered")
    @JsonProperty(value = "QuantityDelivered")
    private  BigDecimal QuantityDelivered ;

    /// <summary>
    /// 名义的交付数量
    /// </summary>
    //@Column(name= "QuantityDeliveredNotional")
    //private  BigDecimal QuantityDeliveredNotional ;

    /// <summary>
    /// 已经开票的净重之和
    /// </summary>
    @Column(name= "QuantityInvoiced")
    @JsonProperty(value = "QuantityInvoiced")
    private  BigDecimal QuantityInvoiced ;

    /// <summary>
    /// 直接从批次付款的申请付款的净重之和
    /// </summary>
    @Column(name= "QuantityFunded")
    @JsonProperty(value = "QuantityFunded")
    private  BigDecimal QuantityFunded ;

    /// <summary>
    /// 已经开票的金额之和
    /// </summary>
    @Column(name= "AmountInvoiced")
    @JsonProperty(value = "AmountInvoiced")
    private  BigDecimal AmountInvoiced ;

    /// <summary>
    /// 直接从批次付款的已经付款的金额之和
    /// </summary>
    @Column(name= "AmountFunded")
    @JsonProperty(value = "AmountFunded")
    private  BigDecimal AmountFunded ;

    /// <summary>
    /// 直接从批次付款的申请付款的金额之和
    /// </summary>
    @Column(name= "AmountFundedDraft")
    @JsonProperty(value = "AmountFundedDraft")
    private  BigDecimal AmountFundedDraft ;

    /// <summary>
    /// 名义的发票数量
    /// </summary>
    //@Column(name= "QuantityInvoicedNotional")
    //private  BigDecimal QuantityInvoicedNotional ;

    /// <summary>
    /// 已经点价的数量之和
    /// </summary>
    @Column(name= "QuantityPriced")
    @JsonProperty(value = "QuantityPriced")
    private  BigDecimal QuantityPriced ;

    /// <summary>
    /// 已经保值的数量之和
    /// </summary>
    @Column(name= "QuantityHedged")
    @JsonProperty(value = "QuantityHedged")
    private  BigDecimal QuantityHedged ;
    /// <summary>
    /// 头寸价格（保值价格），均价
    /// </summary>
    @Column(name= "HedgedPrice")
    @JsonProperty(value = "HedgedPrice")
    private  BigDecimal HedgedPrice ;
    /// <summary>
    /// 可多选品牌，逗号隔开
    /// </summary>
    @Column(name= "BrandIds",length = 4000)
    @JsonProperty(value = "BrandIds")
    private  String BrandIds ;

    @Column(name= "BrandNames", length = 128)
    @JsonProperty(value = "BrandNames")
    private  String BrandNames ;
    //endregion

    //region 备用的字段。用于“货物计划”时会用到。
            ///// <summary>
            ///// 已经计划的、分配或被分配的数量之和
            ///// </summary>
            //@Column(name= "QuantityAllocated")
            //private  BigDecimal QuantityAllocated ;

            ///// <summary>
            ///// 分配或被分配后的余数
            ///// </summary>
            //@Column(name= "QuantityUnAllocated")
            //private  BigDecimal QuantityUnAllocated ;
            //endregion

    //region 业务执行有关

    /// <summary>
    /// 现货交易类型，eg {采购:B，销售:S，转口:I，生产，...} (Dict)
    /// </summary>
    @Column(name= "SpotDirection", length = 2)
    @JsonProperty(value = "SpotDirection")
    private  String SpotDirection ;

    @Column(name= "Status")
    @JsonProperty(value = "Status")
    private  int Status ;

    /// <summary>
    /// 应收应付的余额
    /// IsStoraged = true时， DueBalance才 = QuantityStoraged * Price
    /// IsStoraged = false时，DueBalance才 = Quantity * Price
    /// </summary>
    @Column(name= "DueBalance")
    @JsonProperty(value = "DueBalance")
    private  BigDecimal DueBalance ;

    /// <summary>
    /// 已经发生的余额
    /// </summary>
    @Column(name= "PaidBalance")
    @JsonProperty(value = "PaidBalance")
    private  BigDecimal PaidBalance ;

    /// <summary>
    /// 最新余额 = 应收应付 - 已经发生
    /// </summary>
    @Column(name= "LastBalance")
    @JsonProperty(value = "LastBalance")
    private  BigDecimal LastBalance ;

    /// <summary>
    /// 收发货的标识
    /// </summary>
    @Column(name= "IsDelivered")
    @JsonProperty(value = "IsDelivered")
    private  Boolean IsDelivered ;

    /// <summary>
    /// 收或者开，发票的标识
    /// </summary>
    @Column(name= "IsInvoiced")
    @JsonProperty(value = "IsInvoiced")
    private  Boolean IsInvoiced ;

    /// <summary>
    /// 保值的标识。如果是FALSE，表示存在敞口。
    /// </summary>
    @Column(name= "IsHedged")
    @JsonProperty(value = "IsHedged")
    private  Boolean IsHedged ;

    /// <summary>
    /// 收付款的标识
    /// </summary>
    @Column(name= "IsFunded")
    @JsonProperty(value = "IsFunded")
    private  Boolean IsFunded ;

    /// <summary>
    /// 盈亏结算的标识。完成盈亏结算 不等于 会计记账
    /// </summary>
    @Column(name= "IsSettled")
    @JsonProperty(value = "IsSettled")
    private  Boolean IsSettled ;

    /// <summary>
    /// 会计记账的标识
    /// </summary>
    @Column(name= "IsAccounted")
    @JsonProperty(value = "IsAccounted")
    private  Boolean IsAccounted ;
    //endregion

    //region 价格有关
    /// <summary>
    /// 点价的标识
    /// </summary>
    @Column(name= "IsPriced")
    @JsonProperty(value = "IsPriced")
    private  Boolean IsPriced ;

    /// <summary>
    /// 各项杂费是否已完成冲销
    /// </summary>
    @Column(name= "IsFeeEliminated")
    @JsonProperty(value = "IsFeeEliminated")
    private  Boolean IsFeeEliminated ;

    /// <summary>
    /// 点价方式 {PAF + FA的组合模式}
    /// </summary>
    @Column(name= "PricingType", length = 30)
    @JsonProperty(value = "PricingType")
    private  String PricingType ;

    /// <summary>
    /// 最终价格 Final = Price + Fee，其中：Price = Major + Premium
    /// </summary>
    @Column(name= "Final")
    @JsonProperty(value = "Final")
    private  BigDecimal Final ;

    /// <summary>
    /// 商品价格 Price = Major + Premium
    /// </summary>
    @Column(name= "Price")
    @JsonProperty(value = "Price")
    private  BigDecimal Price ;

    /// <summary>
    /// （1）主要价格部分：= 固定价价格，或者是点价、或者是均价计算后得到的结果
    /// </summary>
    @Column(name= "Major")
    @JsonProperty(value = "Major")
    private  BigDecimal Major ;

    /// <summary>
    /// （2）升贴水部分：= 固定升贴水，或者是均价升贴水。均价升贴水是由系统自动计算得到的结果。
    /// </summary>
    @Column(name= "Premium")
    @JsonProperty(value = "Premium")
    private  BigDecimal Premium ;

    /// <summary>
    /// （3）各种杂费(未发生取预估，已发生取实际）：不论原始状态如何，都是折算成 ?/MT 的值
    /// </summary>
    @Column(name= "Fee")
    @JsonProperty(value = "Fee")
    private  BigDecimal Fee ;
    /// <summary>
    /// （3.1）各种杂费(实际发生)：都是折算成 ?/MT 的值
    /// </summary>
    @Column(name= "RealFee")
    @JsonProperty(value = "RealFee")
    private  BigDecimal RealFee ;
    /// <summary>
    /// （3.2）各种杂费(预估)：都是折算成 ?/MT 的值
    /// </summary>
    @Column(name= "EstimateFee")
    @JsonProperty(value = "EstimateFee")
    private  BigDecimal EstimateFee ;
    /// <summary>
    /// 货币(Dict)
    /// </summary>
    @Column(name= "Currency", length = 3)
    @JsonProperty(value = "Currency")
    private  String Currency ;

    /// <summary>
    ///  商品名称
    /// </summary>
    @Column(name= "Product", length = 64)
    @JsonProperty(value = "Product")
    private  String Product ;

    /// <summary>
    /// 预计销售日期（QP）
    /// </summary>
    @Column(name= "EstimateSaleDate")
    @JsonProperty(value = "EstimateSaleDate")
    private  Date EstimateSaleDate ;

    //endregion

    //region 按照ETA船期点价
    /// <summary>
    /// 是否按照ETA船期点价
    /// </summary>
    @Column(name= "IsEtaPricing")
    @JsonProperty(value = "IsEtaPricing")
    private  Boolean IsEtaPricing ;

    /// <summary>
    /// ｛M-3, M-2, M-1, M, M+1, M+2, M+3｝M=到货月份
    /// </summary>
    @Column(name= "EtaDuaration")
    @JsonProperty(value = "EtaDuaration")
    private  String EtaDuaration ;

    /// <summary>
    /// {0=正常的月均价, 1=月连续5天均价中的最低价}
    /// </summary>
    @Column(name= "EtaPrice")
    @JsonProperty(value = "EtaPrice")
    private  String EtaPrice ;
    //endregion

    //region (1)构成主要价格的几个要素
    /// <summary>
    /// 点价类型
    /// { 点价 = P(PRICING)，均价 = A(AVERAGE)，固定价 = F(FIX)} (dict)
    /// </summary>
    @Column(name= "MajorType", length = 30)
    @JsonProperty(value = "MajorType")
    private  String MajorType ;

    /// <summary>
    ///｛上期所日结算价 = SETTLE,上期所日加权平均价 = AVERAGE，LME现货价 = CASH, LME3月价格 = 3M｝(dict)
    /// </summary>
    @Column(name= "MajorBasis", length = 30)
    @JsonProperty(value = "MajorBasis")
    private  String MajorBasis ;

    @Column(name= "MajorStartDate")
    @JsonProperty(value = "MajorStartDate")
    private  Date MajorStartDate ;

    @Column(name= "MajorEndDate")
    @JsonProperty(value = "MajorEndDate")
    private  Date MajorEndDate ;


    /// <summary>
    /// 主价点价时的所需天数
    /// </summary>
    @Column(name= "MajorDays")
    @JsonProperty(value = "MajorDays")
    private  int MajorDays ;
    /// <summary>
    /// 主价点价的每天数量
    /// </summary>
    @Column(name= "QtyPerMainDay")
    @JsonProperty(value = "QtyPerMainDay")
    private  BigDecimal QtyPerMainDay ;
    //endregion

    //region (2)构成均价升贴水的几个要素
    /// <summary>
    /// 升贴水为均价点价时的所需天数
    /// </summary>
    @Column(name= "PremiumDays")
    @JsonProperty(value = "PremiumDays")
    private  int PremiumDays ;
    /// <summary>
    /// 均价点价的每天数量
    /// </summary>
    @Column(name= "QtyPerPremiumDay")
    @JsonProperty(value = "QtyPerPremiumDay")
    private  BigDecimal QtyPerPremiumDay ;

    /// <summary>
    /// { 固定升贴水 = F(FIX)，均价升贴水 = A(AVERAGE)}(dict)
    /// </summary>
    @Column(name= "PremiumType", length = 30)
    @JsonProperty(value = "PremiumType")
    private  String PremiumType ;
    /// <summary>
    /// { 最低价的均价 = LOW,  最高价的均价 = HIGH, 均价 = AVERAGE}(dict)
    /// </summary>
    @Column(name= "PremiumBasis", length = 30)
    @JsonProperty(value = "PremiumBasis")
    private  String PremiumBasis ;


    @Column(name= "PremiumStartDate")
    @JsonProperty(value = "PremiumStartDate")
    private  Date PremiumStartDate ;



    @Column(name= "PremiumEndDate")
    @JsonProperty(value = "PremiumEndDate")
    private  Date PremiumEndDate ;
    //endregion

    //region 辅助字段
    @Column(name= "DeliveryTerm")
    @JsonProperty(value = "DeliveryTerm")
    private  String DeliveryTerm ;
    /// <summary>
    /// 装运地点
    /// </summary>
    @Column(name= "Loading", length = 64)
    @JsonProperty(value = "Loading")
    private  String Loading ;

    /// <summary>
    /// 卸货地点
    /// </summary>
    @Column(name= "Discharging", length = 64)
    @JsonProperty(value = "Discharging")
    private  String Discharging ;
    /// <summary>
    /// 预计销售地点
    /// </summary>
    @Column(name= "EstDischarging", length = 64)
    @JsonProperty(value = "EstDischarging")
    private  String EstDischarging ;

    /// <summary>
    /// 估计出发日期
    /// </summary>
    @Column(name= "ETD")
    @JsonProperty(value = "ETD")
    private  Date ETD ;

    /// <summary>
    /// 估计到达日期
    /// </summary>
    @Column(name= "ETA")
    @JsonProperty(value = "ETA")
    private  Date ETA ;

    /// <summary>
    /// 实际出发日期
    /// </summary>
    @Column(name= "ATD")
    @JsonProperty(value = "ATD")
    private  Date ATD ;

    /// <summary>
    /// 实际到达日期
    /// </summary>
    @Column(name= "ATA")
    @JsonProperty(value = "ATA")
    private  Date ATA ;

    /// <summary>
    ///
    /// </summary>
    @Column(name= "CommentsLot", length = 2000)
    @JsonProperty(value = "CommentsLot")
    private  String CommentsLot ;

    /// <summary>
    /// 结算日期
    /// </summary>
    @Column(name= "QP")
    @JsonProperty(value = "QP")
    private  Date QP ;

    /// <summary>
    /// 结算日期 --- 原始的日期。在新增记录时，COPY一个值给这个字段，供将来可能需要的查询
    /// </summary>
    @Column(name= "OriginalQP")
    @JsonProperty(value = "OriginalQP")
    private  Date OriginalQP ;
    //endregion


    public BigDecimal getM2MPrice() {
        return M2MPrice;
    }

    public void setM2MPrice(BigDecimal m2MPrice) {
        M2MPrice = m2MPrice;
    }

    public BigDecimal getPriceDiff() {
        return PriceDiff;
    }

    public void setPriceDiff(BigDecimal priceDiff) {
        PriceDiff = priceDiff;
    }

    public BigDecimal getExposure() {
        return Exposure;
    }

    public void setExposure(BigDecimal exposure) {
        Exposure = exposure;
    }

    public BigDecimal getQuantityUnPriced() {
        return QuantityUnPriced;
    }

    public void setQuantityUnPriced(BigDecimal quantityUnPriced) {
        QuantityUnPriced = quantityUnPriced;
    }

    public BigDecimal getQuantityUnInvoiced() {
        return QuantityUnInvoiced;
    }

    public void setQuantityUnInvoiced(BigDecimal quantityUnInvoiced) {
        QuantityUnInvoiced = quantityUnInvoiced;
    }

    public BigDecimal getQuantityBeforeChanged() {
        return QuantityBeforeChanged;
    }

    public void setQuantityBeforeChanged(BigDecimal quantityBeforeChanged) {
        QuantityBeforeChanged = quantityBeforeChanged;
    }

    public int getDigits() {
        return Digits;
    }

    public void setDigits(int digits) {
        Digits = digits;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerShortName() {
        return CustomerShortName;
    }

    public void setCustomerShortName(String customerShortName) {
        CustomerShortName = customerShortName;
    }

    public String getTraderName() {
        return TraderName;
    }

    public void setTraderName(String traderName) {
        TraderName = traderName;
    }

    public String getLegalCode() {
        return LegalCode;
    }

    public void setLegalCode(String legalCode) {
        LegalCode = legalCode;
    }

    public String getLegalName() {
        return LegalName;
    }

    public void setLegalName(String legalName) {
        LegalName = legalName;
    }

    public String getSpecName() {
        return SpecName;
    }

    public void setSpecName(String specName) {
        SpecName = specName;
    }

    public String getOriginName() {
        return OriginName;
    }

    public void setOriginName(String originName) {
        OriginName = originName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getMajorMarketName() {
        return MajorMarketName;
    }

    public void setMajorMarketName(String majorMarketName) {
        MajorMarketName = majorMarketName;
    }

    public Boolean isIniatiated() {
        return IsIniatiated;
    }

    public void setIsIniatiated(Boolean isIniatiated) {
        IsIniatiated = isIniatiated;
    }

    public BigDecimal getGrade() {
        return Grade;
    }

    public void setGrade(BigDecimal grade) {
        Grade = grade;
    }

    public BigDecimal getDiscount() {
        return Discount;
    }

    public void setDiscount(BigDecimal discount) {
        Discount = discount;
    }

    public Boolean isReBuy() {
        return IsReBuy;
    }

    public void setIsReBuy(Boolean isReBuy) {
        IsReBuy = isReBuy;
    }

    public BigDecimal getQuantityOriginal() {
        return QuantityOriginal;
    }

    public void setQuantityOriginal(BigDecimal quantityOriginal) {
        QuantityOriginal = quantityOriginal;
    }

    public Boolean isSplitLot() {
        return IsSplitLot;
    }

    public void setIsSplitLot(Boolean isSplitLot) {
        IsSplitLot = isSplitLot;
    }

    public Boolean isOriginalLot() {
        return IsOriginalLot;
    }

    public void setIsOriginalLot(Boolean isOriginalLot) {
        IsOriginalLot = isOriginalLot;
    }

    public String getOriginalLotId() {
        return OriginalLotId;
    }

    public void setOriginalLotId(String originalLotId) {
        OriginalLotId = originalLotId;
    }

    public String getMoreOrLessBasis() {
        return MoreOrLessBasis;
    }

    public void setMoreOrLessBasis(String moreOrLessBasis) {
        MoreOrLessBasis = moreOrLessBasis;
    }

    public BigDecimal getMoreOrLess() {
        return MoreOrLess;
    }

    public void setMoreOrLess(BigDecimal moreOrLess) {
        MoreOrLess = moreOrLess;
    }

    public Boolean isQuantityConfirmed() {
        return IsQuantityConfirmed;
    }

    public void setIsQuantityConfirmed(Boolean isQuantityConfirmed) {
        IsQuantityConfirmed = isQuantityConfirmed;
    }

    public Integer getMarkColor() {
        return MarkColor;
    }

    public void setMarkColor(Integer markColor) {
        MarkColor = markColor;
    }

    public Boolean isAllowOverrideContractNo() {
        return IsAllowOverrideContractNo;
    }

    public void setIsAllowOverrideContractNo(Boolean isAllowOverrideContractNo) {
        IsAllowOverrideContractNo = isAllowOverrideContractNo;
    }

    public Date getDateInvoiced() {
        return DateInvoiced;
    }

    public void setDateInvoiced(Date dateInvoiced) {
        DateInvoiced = dateInvoiced;
    }

    public Date getDateDueInvoice() {
        return DateDueInvoice;
    }

    public void setDateDueInvoice(Date dateDueInvoice) {
        DateDueInvoice = dateDueInvoice;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getFullNo() {
        return FullNo;
    }

    public void setFullNo(String fullNo) {
        FullNo = fullNo;
    }

    public String getHeadNo() {
        return HeadNo;
    }

    public void setHeadNo(String headNo) {
        HeadNo = headNo;
    }

    public String getPrefixNo() {
        return PrefixNo;
    }

    public void setPrefixNo(String prefixNo) {
        PrefixNo = prefixNo;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public int getLotNo() {
        return LotNo;
    }

    public void setLotNo(int lotNo) {
        LotNo = lotNo;
    }

    public String getDocumentNo() {
        return DocumentNo;
    }

    public void setDocumentNo(String documentNo) {
        DocumentNo = documentNo;
    }

    public BigDecimal getPnL() {
        return PnL;
    }

    public void setPnL(BigDecimal pnL) {
        PnL = pnL;
    }

    public BigDecimal getQuantity() {
        return Quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        Quantity = quantity;
    }

    public BigDecimal getQuantityLess() {
        return QuantityLess;
    }

    public void setQuantityLess(BigDecimal quantityLess) {
        QuantityLess = quantityLess;
    }

    public BigDecimal getQuantityMore() {
        return QuantityMore;
    }

    public void setQuantityMore(BigDecimal quantityMore) {
        QuantityMore = quantityMore;
    }

    public BigDecimal getQuantityDelivered() {
        return QuantityDelivered;
    }

    public void setQuantityDelivered(BigDecimal quantityDelivered) {
        QuantityDelivered = quantityDelivered;
    }

    public BigDecimal getQuantityInvoiced() {
        return QuantityInvoiced;
    }

    public void setQuantityInvoiced(BigDecimal quantityInvoiced) {
        QuantityInvoiced = quantityInvoiced;
    }

    public BigDecimal getQuantityFunded() {
        return QuantityFunded;
    }

    public void setQuantityFunded(BigDecimal quantityFunded) {
        QuantityFunded = quantityFunded;
    }

    public BigDecimal getAmountInvoiced() {
        return AmountInvoiced;
    }

    public void setAmountInvoiced(BigDecimal amountInvoiced) {
        AmountInvoiced = amountInvoiced;
    }

    public BigDecimal getAmountFunded() {
        return AmountFunded;
    }

    public void setAmountFunded(BigDecimal amountFunded) {
        AmountFunded = amountFunded;
    }

    public BigDecimal getAmountFundedDraft() {
        return AmountFundedDraft;
    }

    public void setAmountFundedDraft(BigDecimal amountFundedDraft) {
        AmountFundedDraft = amountFundedDraft;
    }

    public BigDecimal getQuantityPriced() {
        return QuantityPriced;
    }

    public void setQuantityPriced(BigDecimal quantityPriced) {
        QuantityPriced = quantityPriced;
    }

    public BigDecimal getQuantityHedged() {
        return QuantityHedged;
    }

    public void setQuantityHedged(BigDecimal quantityHedged) {
        QuantityHedged = quantityHedged;
    }

    public BigDecimal getHedgedPrice() {
        return HedgedPrice;
    }

    public void setHedgedPrice(BigDecimal hedgedPrice) {
        HedgedPrice = hedgedPrice;
    }

    public String getBrandIds() {
        return BrandIds;
    }

    public void setBrandIds(String brandIds) {
        BrandIds = brandIds;
    }

    public String getBrandNames() {
        return BrandNames;
    }

    public void setBrandNames(String brandNames) {
        BrandNames = brandNames;
    }

    public String getSpotDirection() {
        return SpotDirection;
    }

    public void setSpotDirection(String spotDirection) {
        SpotDirection = spotDirection;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public BigDecimal getDueBalance() {
        return DueBalance;
    }

    public void setDueBalance(BigDecimal dueBalance) {
        DueBalance = dueBalance;
    }

    public BigDecimal getPaidBalance() {
        return PaidBalance;
    }

    public void setPaidBalance(BigDecimal paidBalance) {
        PaidBalance = paidBalance;
    }

    public BigDecimal getLastBalance() {
        return LastBalance;
    }

    public void setLastBalance(BigDecimal lastBalance) {
        LastBalance = lastBalance;
    }

    public Boolean isDelivered() {
        return IsDelivered;
    }

    public void setIsDelivered(Boolean isDelivered) {
        IsDelivered = isDelivered;
    }

    public Boolean isInvoiced() {
        return IsInvoiced;
    }

    public void setIsInvoiced(Boolean isInvoiced) {
        IsInvoiced = isInvoiced;
    }

    public Boolean isHedged() {
        return IsHedged;
    }

    public void setIsHedged(Boolean isHedged) {
        IsHedged = isHedged;
    }

    public Boolean isFunded() {
        return IsFunded;
    }

    public void setIsFunded(Boolean isFunded) {
        IsFunded = isFunded;
    }

    public Boolean isSettled() {
        return IsSettled;
    }

    public void setIsSettled(Boolean isSettled) {
        IsSettled = isSettled;
    }

    public Boolean isAccounted() {
        return IsAccounted;
    }

    public void setIsAccounted(Boolean isAccounted) {
        IsAccounted = isAccounted;
    }

    public Boolean isPriced() {
        return IsPriced;
    }

    public void setIsPriced(Boolean isPriced) {
        IsPriced = isPriced;
    }

    public Boolean isFeeEliminated() {
        return IsFeeEliminated;
    }

    public void setIsFeeEliminated(Boolean isFeeEliminated) {
        IsFeeEliminated = isFeeEliminated;
    }

    public String getPricingType() {
        return PricingType;
    }

    public void setPricingType(String pricingType) {
        PricingType = pricingType;
    }

    public BigDecimal getFinal() {
        return Final;
    }

    public void setFinal(BigDecimal aFinal) {
        Final = aFinal;
    }

    public BigDecimal getPrice() {
        return Price;
    }

    public void setPrice(BigDecimal price) {
        Price = price;
    }

    public BigDecimal getMajor() {
        return Major;
    }

    public void setMajor(BigDecimal major) {
        Major = major;
    }

    public BigDecimal getPremium() {
        return Premium;
    }

    public void setPremium(BigDecimal premium) {
        Premium = premium;
    }

    public BigDecimal getFee() {
        return Fee;
    }

    public void setFee(BigDecimal fee) {
        Fee = fee;
    }

    public BigDecimal getRealFee() {
        return RealFee;
    }

    public void setRealFee(BigDecimal realFee) {
        RealFee = realFee;
    }

    public BigDecimal getEstimateFee() {
        return EstimateFee;
    }

    public void setEstimateFee(BigDecimal estimateFee) {
        EstimateFee = estimateFee;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getProduct() {
        return Product;
    }

    public void setProduct(String product) {
        Product = product;
    }

    public Date getEstimateSaleDate() {
        return EstimateSaleDate;
    }

    public void setEstimateSaleDate(Date estimateSaleDate) {
        EstimateSaleDate = estimateSaleDate;
    }

    public Boolean isEtaPricing() {
        return IsEtaPricing;
    }

    public void setIsEtaPricing(Boolean isEtaPricing) {
        IsEtaPricing = isEtaPricing;
    }

    public String getEtaDuaration() {
        return EtaDuaration;
    }

    public void setEtaDuaration(String etaDuaration) {
        EtaDuaration = etaDuaration;
    }

    public String getEtaPrice() {
        return EtaPrice;
    }

    public void setEtaPrice(String etaPrice) {
        EtaPrice = etaPrice;
    }

    public String getMajorType() {
        return MajorType;
    }

    public void setMajorType(String majorType) {
        MajorType = majorType;
    }

    public String getMajorBasis() {
        return MajorBasis;
    }

    public void setMajorBasis(String majorBasis) {
        MajorBasis = majorBasis;
    }

    public Date getMajorStartDate() {
        return MajorStartDate;
    }

    public void setMajorStartDate(Date majorStartDate) {
        MajorStartDate = majorStartDate;
    }

    public Date getMajorEndDate() {
        return MajorEndDate;
    }

    public void setMajorEndDate(Date majorEndDate) {
        MajorEndDate = majorEndDate;
    }

    public int getMajorDays() {
        return MajorDays;
    }

    public void setMajorDays(int majorDays) {
        MajorDays = majorDays;
    }

    public BigDecimal getQtyPerMainDay() {
        return QtyPerMainDay;
    }

    public void setQtyPerMainDay(BigDecimal qtyPerMainDay) {
        QtyPerMainDay = qtyPerMainDay;
    }

    public int getPremiumDays() {
        return PremiumDays;
    }

    public void setPremiumDays(int premiumDays) {
        PremiumDays = premiumDays;
    }

    public BigDecimal getQtyPerPremiumDay() {
        return QtyPerPremiumDay;
    }

    public void setQtyPerPremiumDay(BigDecimal qtyPerPremiumDay) {
        QtyPerPremiumDay = qtyPerPremiumDay;
    }

    public String getPremiumType() {
        return PremiumType;
    }

    public void setPremiumType(String premiumType) {
        PremiumType = premiumType;
    }

    public String getPremiumBasis() {
        return PremiumBasis;
    }

    public void setPremiumBasis(String premiumBasis) {
        PremiumBasis = premiumBasis;
    }

    public Date getPremiumStartDate() {
        return PremiumStartDate;
    }

    public void setPremiumStartDate(Date premiumStartDate) {
        PremiumStartDate = premiumStartDate;
    }

    public Date getPremiumEndDate() {
        return PremiumEndDate;
    }

    public void setPremiumEndDate(Date premiumEndDate) {
        PremiumEndDate = premiumEndDate;
    }

    public String getDeliveryTerm() {
        return DeliveryTerm;
    }

    public void setDeliveryTerm(String deliveryTerm) {
        DeliveryTerm = deliveryTerm;
    }

    public String getLoading() {
        return Loading;
    }

    public void setLoading(String loading) {
        Loading = loading;
    }

    public String getDischarging() {
        return Discharging;
    }

    public void setDischarging(String discharging) {
        Discharging = discharging;
    }

    public String getEstDischarging() {
        return EstDischarging;
    }

    public void setEstDischarging(String estDischarging) {
        EstDischarging = estDischarging;
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

    public String getCommentsLot() {
        return CommentsLot;
    }

    public void setCommentsLot(String commentsLot) {
        CommentsLot = commentsLot;
    }

    public Date getQP() {
        return QP;
    }

    public void setQP(Date QP) {
        this.QP = QP;
    }

    public Date getOriginalQP() {
        return OriginalQP;
    }

    public void setOriginalQP(Date originalQP) {
        OriginalQP = originalQP;
    }
}
