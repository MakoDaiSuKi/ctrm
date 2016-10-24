

package com.smm.ctrm.domain.Basis;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.smm.ctrm.domain.HibernateEntity;
@Entity
@Table(name = "GlobalSet", schema="Basis")
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GlobalSet extends HibernateEntity {
	private static final long serialVersionUID = 1461832991323L;
	/**
	 * {yyNo, yyMMddNo}
	 */
	@Column(name = "Prefix4Contract")
	@JsonProperty(value = "Prefix4Contract")
	private String Prefix4Contract;
	/**
	 * {Year, Day}
	 */
	@Column(name = "Serial4Contract")
	@JsonProperty(value = "Serial4Contract")
	private String Serial4Contract;
	/**
	 * 合同流水编号的长度
	 */
	@Column(name = "Len4ContractSerialNo")
	@JsonProperty(value = "Len4ContractSerialNo")
	private Integer Len4ContractSerialNo;
	/**
	 * {yyNo, yyMMddNo}
	 */
	@Column(name = "Prefix4Invoice")
	@JsonProperty(value = "Prefix4Invoice")
	private String Prefix4Invoice;
	/**
	 * {Year, Day}
	 */
	@Column(name = "Serial4Invoice")
	@JsonProperty(value = "Serial4Invoice")
	private String Serial4Invoice;
	/**
	 * 合同流水编号的长度
	 */
	@Column(name = "Len4InvoiceSerialNo")
	@JsonProperty(value = "Len4InvoiceSerialNo")
	private Integer Len4InvoiceSerialNo;
	/**
	 * 上传文件存放在服务器的相对路径
	 */
	@Column(name = "ServerUploadPath")
	@JsonProperty(value = "ServerUploadPath")
	private String ServerUploadPath;
	/**
	 * 生成的外部文档存放在服务器的相对路径
	 */
	@Column(name = "ServerOutDocumentPath")
	@JsonProperty(value = "ServerOutDocumentPath")
	private String ServerOutDocumentPath;
	/**
	 * 域外部文档模板存放路径
	 */
	@Column(name = "DomainOutDocTemplateFilePath")
	@JsonProperty(value = "DomainOutDocTemplateFilePath")
	private String DomainOutDocTemplateFilePath;
	/**
	 * 域外部文档存放路径
	 */
	@Column(name = "DomainOutDocumentPath")
	@JsonProperty(value = "DomainOutDocumentPath")
	private String DomainOutDocumentPath;
	/**
	 * 生成的外部文档存放在服务器的相对路径
	 */
	@Column(name = "ServerOutDocTemplateFilePath")
	@JsonProperty(value = "ServerOutDocTemplateFilePath")
	private String ServerOutDocTemplateFilePath;
	/**
	 * 默认MTM市场
	 */
	@Column(name = "DefaultMarketId")
	@JsonProperty(value = "DefaultMarketId")
	private String DefaultMarketId;
	/**
	 * 相同品牌才允许发货
	 */
	@Column(name = "IsSameBrandSelectableOnly")
	@JsonProperty(value = "IsSameBrandSelectableOnly")
	private Boolean IsSameBrandSelectableOnly;
	/**
	 * 收发货的数量偏差，超过该偏差不允许发货
	 */
	@Column(name = "DeliveryDisparity")
	@JsonProperty(value = "DeliveryDisparity")
	private BigDecimal DeliveryDisparity;
	/**
	 * 初始化日期
	 */
	@Column(name = "InitDate")
	@JsonProperty(value = "InitDate")
	private Date InitDate;
	/**
	 * 盈亏结算类型
	 */
	@Column(name = "PnLSettleType")
	@JsonProperty(value = "PnLSettleType")
	private String PnLSettleType;
	/**
	 * 默认计算币种
	 */
	@Column(name = "DefaultCurrency")
	@JsonProperty(value = "DefaultCurrency")
	private String DefaultCurrency;
	/**
	 * 默认汇率（每一美元可以得到的人民币）
	 */
	@Column(name = "CurrencyRate")
	@JsonProperty(value = "CurrencyRate")
	private BigDecimal CurrencyRate;
	/**
	 * 初始化完成的标志
	 */
	@Column(name = "InitCompleted")
	@JsonProperty(value = "InitCompleted")
	private Boolean InitCompleted;
	/**
	 * 按什么进行付款 {C = 按客户付款, L = 按批次付款, I = 按发票付款}
	 */
	@Column(name = "PayPerLotOrInvoice")
	@JsonProperty(value = "PayPerLotOrInvoice")
	private String PayPerLotOrInvoice;
	/**
	 *
	 */
	@Column(name = "OrgId")
	@JsonProperty(value = "OrgId")
	private String OrgId;
	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Org.class)
	@JoinColumn(name = "OrgId", insertable = false, updatable = false, foreignKey = @ForeignKey(name="none"))
	@Fetch(FetchMode.SELECT)
	@NotFound(action=NotFoundAction.IGNORE)
	@JsonProperty(value = "Org")
	private Org Org;
	
	public String getPrefix4Contract(){
		return Prefix4Contract;
	}
	public void setPrefix4Contract(String Prefix4Contract){
		this.Prefix4Contract=Prefix4Contract;
	}
	
	public String getSerial4Contract(){
		return Serial4Contract;
	}
	public void setSerial4Contract(String Serial4Contract){
		this.Serial4Contract=Serial4Contract;
	}
	@JsonProperty(defaultValue="0")
	public Integer getLen4ContractSerialNo(){
		return Len4ContractSerialNo;
	}
	public void setLen4ContractSerialNo(Integer Len4ContractSerialNo){
		this.Len4ContractSerialNo=Len4ContractSerialNo;
	}
	
	public String getPrefix4Invoice(){
		return Prefix4Invoice;
	}
	public void setPrefix4Invoice(String Prefix4Invoice){
		this.Prefix4Invoice=Prefix4Invoice;
	}
	
	public String getSerial4Invoice(){
		return Serial4Invoice;
	}
	public void setSerial4Invoice(String Serial4Invoice){
		this.Serial4Invoice=Serial4Invoice;
	}
	
	public Integer getLen4InvoiceSerialNo(){
		return Len4InvoiceSerialNo;
	}
	public void setLen4InvoiceSerialNo(Integer Len4InvoiceSerialNo){
		this.Len4InvoiceSerialNo=Len4InvoiceSerialNo;
	}
	
	public String getServerUploadPath(){
		return ServerUploadPath;
	}
	public void setServerUploadPath(String ServerUploadPath){
		this.ServerUploadPath=ServerUploadPath;
	}
	
	public String getServerOutDocumentPath(){
		return ServerOutDocumentPath;
	}
	public void setServerOutDocumentPath(String ServerOutDocumentPath){
		this.ServerOutDocumentPath=ServerOutDocumentPath;
	}
	
	public String getDomainOutDocTemplateFilePath(){
		return DomainOutDocTemplateFilePath;
	}
	public void setDomainOutDocTemplateFilePath(String DomainOutDocTemplateFilePath){
		this.DomainOutDocTemplateFilePath=DomainOutDocTemplateFilePath;
	}
	
	public String getDomainOutDocumentPath(){
		return DomainOutDocumentPath;
	}
	public void setDomainOutDocumentPath(String DomainOutDocumentPath){
		this.DomainOutDocumentPath=DomainOutDocumentPath;
	}
	
	public String getServerOutDocTemplateFilePath(){
		return ServerOutDocTemplateFilePath;
	}
	public void setServerOutDocTemplateFilePath(String ServerOutDocTemplateFilePath){
		this.ServerOutDocTemplateFilePath=ServerOutDocTemplateFilePath;
	}
	
	public String getDefaultMarketId(){
		return DefaultMarketId;
	}
	public void setDefaultMarketId(String DefaultMarketId){
		this.DefaultMarketId=DefaultMarketId;
	}
	
	public Boolean getIsSameBrandSelectableOnly(){
		return IsSameBrandSelectableOnly;
	}
	public void setIsSameBrandSelectableOnly(Boolean IsSameBrandSelectableOnly){
		this.IsSameBrandSelectableOnly=IsSameBrandSelectableOnly;
	}
	
	public BigDecimal getDeliveryDisparity(){
		return DeliveryDisparity;
	}
	public void setDeliveryDisparity(BigDecimal DeliveryDisparity){
		this.DeliveryDisparity=DeliveryDisparity;
	}
	
	public Date getInitDate(){
		return InitDate;
	}
	public void setInitDate(Date InitDate){
		this.InitDate=InitDate;
	}
	
	public String getPnLSettleType(){
		return PnLSettleType;
	}
	public void setPnLSettleType(String PnLSettleType){
		this.PnLSettleType=PnLSettleType;
	}
	
	public String getDefaultCurrency(){
		return DefaultCurrency;
	}
	public void setDefaultCurrency(String DefaultCurrency){
		this.DefaultCurrency=DefaultCurrency;
	}
	
	public BigDecimal getCurrencyRate(){
		return CurrencyRate;
	}
	public void setCurrencyRate(BigDecimal CurrencyRate){
		this.CurrencyRate=CurrencyRate;
	}
	
	public Boolean getInitCompleted(){
		return InitCompleted;
	}
	public void setInitCompleted(Boolean InitCompleted){
		this.InitCompleted=InitCompleted;
	}
	
	public String getPayPerLotOrInvoice(){
		return PayPerLotOrInvoice;
	}
	public void setPayPerLotOrInvoice(String PayPerLotOrInvoice){
		this.PayPerLotOrInvoice=PayPerLotOrInvoice;
	}
	
	public String getOrgId(){
		return OrgId;
	}
	public void setOrgId(String OrgId){
		this.OrgId=OrgId;
	}
	
	public Org getOrg(){
		return Org;
	}
	public void setOrg(Org Org){
		this.Org=Org;
	}

}