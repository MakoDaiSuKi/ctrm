
package com.smm.ctrm.domain.apiClient;

public class Api
{

		///#region   Maintain: Ftech
	public static class FetchExchange
	{
		public static String Save = "api/Maintain/FetchExchange/Save";
		public static String Delete = "api/Maintain/FetchExchange/Delete";
		public static String GetById = "api/Maintain/FetchExchange/GetById";
		public static String Pager = "api/Maintain/FetchExchange/Pager";
	}

	public static class FetchCalendar
	{
		// 根据现有的市场行情数据，生成全部的交易日历
		public static String GenerateCalendars = "api/Maintain/FetchCalendar/GenerateCalendars";
		public static String SaveVm = "api/Maintain/FetchCalendar/SaveVm";
		public static String Save = "api/Maintain/FetchCalendar/Save";
		public static String Delete = "api/Maintain/FetchCalendar/Delete";
		public static String GetVmById = "api/Maintain/FetchCalendar/GetVmById";
		public static String GetById = "api/Maintain/FetchCalendar/GetById";
		public static String Pager = "api/Maintain/FetchCalendar/Pager";
	}

	public static class FetchLME
	{
		public static String Save = "api/Maintain/FetchLME/Save";
		public static String Delete = "api/Maintain/FetchLME/Delete";
		public static String GetById = "api/Maintain/FetchLME/GetById";
		public static String Pager = "api/Maintain/FetchLME/Pager";
	}

	public static class FetchSFE
	{
		public static String Save = "api/Maintain/FetchSFE/Save";
		public static String Delete = "api/Maintain/FetchSFE/Delete";
		public static String GetById = "api/Maintain/FetchSFE/GetById";
		public static String Pager = "api/Maintain/FetchSFE/Pager";
	}

	public static class FetchDSME
	{
		public static String Save = "api/Maintain/FetchDSME/Save";
		public static String Delete = "api/Maintain/FetchDSME/Delete";
		public static String GetById = "api/Maintain/FetchDSME/GetById";
		public static String Pager = "api/Maintain/FetchDSME/Pager";
	}


		///#endregion


		///#region 仅用于开发人员使用
	public static class Develop
	{
		public static String Menus = "api/Develop/DevMenu/Resources";
		public static String SaveMenu = "api/Develop/DevMenu/SaveMenu";
		public static String DeleteMenu = "api/Develop/DevMenu/DeleteMenu"; //删除单个记录
		public static String DeleteMenus = "api/Develop/DevMenu/DeleteMenus"; //删除多个记录
		public static String MenuById = "api/Develop/DevMenu/MenuById";

		public static String Resources = "api/Develop/DevMenu/Resources";
		public static String SaveButton = "api/Develop/DevMenu/SaveButton";
		public static String ButtonById = "api/Develop/DevMenu/ButtonById";
	}

		///#endregion


		///#region 审批有关的工作流

	public static class Pending
	{
		//申请审批。同时适用于合同、付款的审核提交。注意：只用于提交，从草稿变为待审状态
		public static String Ask4Approve = "api/Physical/Pending/Ask4Approve";

		//查看提交审核时的详细信息
		public static String GetById = "api/Physical/Pending/GetById";

		//获取待审业务的列表
		public static String CustomerPendingPager = "api/Physical/Pending/CustomerPendingPager";
		public static String ContractPendingPager = "api/Physical/Pending/ContractPendingPager";
		public static String PaymentPendingPager = "api/Physical/Pending/PaymentPendingPager";

		//获取指定CustomerId / ContractId / FundId的已经提交的申请审核记录
		public static String PendingsByCustomerId = "api/Physical/Pending/PendingsByCustomerId";
		public static String PendingsByContractId = "api/Physical/Pending/PendingsByContractId";
		public static String PendingsByFundId = "api/Physical/Pending/PendingsByFundId";

		//撤销申请
		public static String CancelByContractId = "api/Physical/Pending/CancelByContractId";
		public static String CancelByContract = "api/Physical/Pending/CancelByContract";

		public static String CancelByCustomerId = "api/Physical/Pending/CancelByCustomerId";
		public static String CancelByCustomer = "api/Physical/Pending/CancelByCustomer";

		public static String CancelByFundId = "api/Physical/Pending/CancelByFundId";
		public static String CancelByFund = "api/Physical/Pending/CancelByFund";
	}

	public static class Approve
	{
		//提交审核结果
		public static String ApproveCustomer4Winform = "api/Physical/Approve/ApproveCustomer4Winform";
		public static String ApproveContract4Winform = "api/Physical/Approve/ApproveContract4Winform";
		public static String ApprovePayment4Winform = "api/Physical/Approve/ApprovePayment4Winform";

		//获取指定CustomerId / ContractId / FundId的已经审核的记录
		public static String GetApproveHistoryByCustomerId = "api/Physical/Approve/GetApproveHistoryByCustomerId";
		public static String GetApproveHistoryByContractId = "api/Physical/Approve/GetApproveHistoryByContractId";
		public static String GetApproveHistoryByFundId = "api/Physical/Approve/GetApproveHistoryByFundId";
	}

		///#endregion


		///#region 只用于Android的api
	public static class Android
	{
		//android - 获取待审核客户的列表、详细信息和已审历史、以及审核
		public static String CustomerPending4App = "api/Physical/Pending/CustomerPending4App";
		public static String CustomerByPendingId = "api/Physical/Pending/CustomerByPendingId";
		public static String ApproveCustomer4App = "api/Physical/Approve/ApproveCustomer4App";

		//android - 获取待审核合同的列表、详细信息和已审历史、以及审核
		public static String ContractPending4App = "api/Physical/Pending/ContractPending4App";
		public static String ContractByPendingId = "api/Physical/Pending/ContractByPendingId";
		public static String ApproveContract4App = "api/Physical/Approve/ApproveContract4App";

		//android - 获取待审订单的列表、详细信息和已审历史、以及审核
		public static String FundPending4App = "api/Physical/Pending/FundPending4App";
		public static String FundByPendingId = "api/Physical/Pending/FundByPendingId";
		public static String ApproveFund4App = "api/Physical/Approve/ApproveFund4App";
	}


		///#endregion


		///#region 客户: 资金余额, 银行设置
	public static class CustomerBalance
	{

		public static String ScheduledUpdateCustomerBalance = "api/Partner/CustomerBalance/ScheduledUpdateCustomerBalance";

		public static String GetById = "api/Partner/CustomerBalance/GetById";
		public static String AmendInitBalance = "api/Partner/CustomerBalance/AmendInitBalance";

		public static String ImportInitCustomerBalance = "api/Partner/CustomerBalance/ImportInitCustomerBalance";
		public static String PagerDetail = "api/Partner/CustomerBalance/PagerDetail";
		public static String PagerSummary = "api/Partner/CustomerBalance/PagerSummary";
		public static String PagerDetail2 = "api/Partner/CustomerBalance/PagerDetail2";

		public static String Delete = "api/Partner/CustomerBalance/Delete";
		public static String DeleteCustomerBalances = "api/Partner/CustomerBalance/DeleteCustomerBalances";

		public static String Reconciliation = "api/Partner/CustomerBalance/Reconciliation";
	}

	public static class CustomerBank
	{
		public static String GetById = "api/Partner/CustomerBank/GetById";
		public static String Save = "api/Partner/CustomerBank/Save";
		public static String Delete = "api/Partner/CustomerBank/Delete";
		public static String CustomerBanks = "api/Partner/CustomerBank/CustomerBanks";
		public static String GetCustomerBanksByCustomerId = "api/Partner/CustomerBank/GetCustomerBanksByCustomerId";

		public static String GetCustomerBanksByCustomerTitleId = "api/Partner/CustomerBank/GetCustomerBanksByCustomerTitleId";
	}

		///#endregion


		///#region 通用

	public static class Common
	{
		//附件
		public static String UploadFile = "api/Basis/Common/UploadFile";
		public static String ReplaceFile = "api/Basis/Common/ReplaceFile";
		public static String CreateOutDocument = "api/Basis/Common/CreateOutDocument";
		public static String OutDocumentAddAttachment = "api/Basis/Common/OutDocumentAddAttachment";
		public static String GetOutDocument = "api/Basis/Common/GetAttachments";
		public static String GetAttachments = "api/Basis/Common/GetAttachments";
		public static String DownloadFileById = "api/Basis/Common/DownloadFileById";
		public static String DeleteAttachmentById = "api/Basis/Common/DeleteAttachmentById";
		public static String DeleteAttachmentByBillId = "api/Basis/Common/DeleteAttachmentByBillId"; //根据业务单据删除相关的所有附件
		//
		public static String Bvi2Sm = "api/Basis/Common/Bvi2Sm";

		/** 
		 入参：     string[]，包括MarketId  + CommodityId
		 返回类型： "ActionResult string"
		 
		*/
		public static String GetM2MPriceByMarketIdAndCommodityId = "api/Maintain/Quotation/GetM2MPriceByMarketIdAndCommodityId";

		/** 
		 入参：     CommodityId
		 返回类型： "ActionResult string"
		 返回指定品种的、默认  期货  市场的最新价格
		 
		*/
		public static String GetFuturesM2MPriceByCommodityId = "api/Maintain/Quotation/GetFuturesM2MPriceByCommodityId";

		/** 
		 入参：     CommodityId
		 返回类型： "ActionResult string"
		 返回指定品种的、默认  现货  市场的最新价格
		 
		*/
		public static String GetSpotM2MPriceByCommodityId = "api/Maintain/Quotation/GetSpotM2MPriceByCommodityId";

		/** 
		 返回待审状态
		 
		*/
		public static String CheckHasVerifyData = "api/Basis/Common/CheckHasVerifyData";
		/** 
		 返回待审情况
		 
		*/
		public static String GetVerifyMessages = "api/Basis/Common/GetVerifyMessages";
	}


		///#endregion


		///#region Dictionary

	public static class Dictionary
	{
		/** 
		 外部文档
		 
		*/
		public static class OutDocument
		{
			public static String OutResourceType = "OutResourceType"; //外部文档类别

			public static String OutDoc_Certificate = "OutDoc_Certificate"; //证明文件
			public static String OutDoc_Packing_List = "OutDoc_Packing_List"; //箱单
			public static String OutDoc_COO = "OutDoc_COO"; //COO
			public static String OutDoc_Others = "OutDoc_Others"; //其他
			public static String OutDoc_Invoice = "OutDoc_Invoice"; //发票

			public static String Contract_Approve = "Contract_Approve"; //合同审批模板

			public static String Allocate_Fund_Bvi = "Allocate_Fund_Bvi"; //拨款申请-BVI
			public static String Allocate_Fund_SM = "Allocate_Fund_SM"; //拨款申请-SM
			public static String Allocate_Fund_XYC = "Allocate_Fund_XYC"; //拨款申请-XYC

			public static String OutDoc_PriceConfirm = "Pricing_Confirm"; //点价确认
			public static String OutDoc_RiskManage = "Risk_Manage"; //风险管理远期交易申请书
		}


		public static class Category
		{
			//自定义发票的分类
			public static String UInvoiceCode = "UInvoiceCode"; //客户的所有制类型

			public static String CorporationOwnType = "CorporationOwnType"; //客户的所有制类型
			public static String CorporationTerminalType = "CorporationTerminalType"; //客户终端类型
			public static String CooperationType = "CooperationType"; //客户的合作类型
			public static String CorporationEntryType = "CorporationEntryType"; //客户准入类型
			public static String Currency = "Currency"; //币种
			public static String DeliveryTerm = "DeliveryTerm"; //交付条款
			public static String CP = "CP"; //期权类型 {C = Call, P = Put}
			public static String ContractStatus = "ContractStatus"; //合同状态
			public static String CalBasis = "CalBasis";
			public static String DC = "DC"; //借贷方向
			public static String DeliveryType = "DeliveryType"; //交付的类型
			public static String ForwardType = "ForwardType"; //期货类型：期货、均价、期权
			public static String ForwardPurpose = "ForwardPurpose"; //用途（目的）：保值、投机
			public static String FundStatus = "FundStatus"; //客户准入类型
			public static String HedgeStrategy = "HedgeStrategy"; //保值策略
			public static String InvoiceType = "InvoiceType";
			public static String LCType = "LCType";
			public static String LS = "LS"; //买卖
			public static String LMEBasis = "LMEBasis";
			public static String MajorType = "MajorType"; //主价类型 {........}
			public static String MajorBasis = "MajorBasis"; //主价基准 {........}
			public static String MarketType = "MarketType";
			public static String MoreOrLessBasis = "MoreOrLessBasis"; //计算溢短装率的基准{OnQuantity, OnPercentage}


			public static String FeeBasis = "FeeBasis"; //按批次的各项杂费的计算基准{OnQuantity = 按数量, OnAmount = 按金额, OnValue = 绝对值}

			public static String EtaDuaration = "EtaDuaration";
			public static String EtaPrice = "EtaPrice";

			public static String PricingType = "PricingType"; //定价类型：固定、均价、点价
			public static String PaymentTerm = "PaymentTerm"; //收付款条款
			public static String PaymentType = "PaymentType"; //收付款的类型
			public static String PremiumType = "PremiumType";
			public static String PremiumBasis = "PremiumBasis";
			public static String PnLSettleType = "PLSettleType";
			public static String PricingTiming = "PricingTiming";

			public static String Prefix4Contract = "Prefix4Contract"; //合同编号前缀的规则
			public static String Prefix4Invoice = "Prefix4Invoice"; //发票编号前缀的规则
			public static String Serial4Contract = "Serial4Contract"; //合同流水号
			public static String Serial4Lot = "Serial4Lot"; //批次流水号
			public static String Serial4Invoice = "Serial4Invoice"; //发票流水号

			public static String PayPerLotOrInvoice = "PayPerLotOrInvoice"; //付款依据：按合同、按发票
			public static String Pricer = "Pricer"; //点价方
			public static String SFEBasis = "SFEBasis";
			public static String SpotType = "SpotType"; //现货业务类型{内贸， 外贸, 转口,...}
			public static String SpotDirection = "SpotDirection"; //现货业务方向{B, S}
			public static String SFEOCS = "SFEOCS"; //上海头寸的开、平今、平仓的分类
			public static String StorageType = "StorageType";
			public static String SimpleOrStandardInvoice = "SimpleOrStandardInvoice"; //发票方式：简单、标准
			public static String WeightUnit = "WeightUnit";

			public static String NaQuantity = "NaQuantity";
			public static String TestOrg = "TestOrg";
			public static String TransitStatus = "TransitStatus";

			public static String OutResourceDocType = "OutResourceDocType"; //外部文档类型

			public static String QuantityUnit = "QuantityUnit"; //数量单位
		}

		public static String Save = "api/Basis/Dictionary/Save";
		public static String GetById = "api/Basis/Dictionary/GetById";
		public static String Delete = "api/Basis/Dictionary/Delete";
		public static String Dictionaries = "api/Basis/Dictionary/Dictionaries"; //返回不带分页列表 用于前台的调用
		public static String BackDictionaries = "api/Basis/Dictionary/BackDictionaries"; //返回不带分页列表（显示/隐藏）用于后台调用
		public static String MoveUp = "api/Basis/Dictionary/MoveUp";
		public static String MoveDown = "api/Basis/Dictionary/MoveDown";
		public static String Cats = "api/Basis/Dictionary/Cats"; //字典类别的列表
		public static String ItemsByCatId = "api/Basis/Dictionary/ItemsByCatId";
		public static String AllDictValue = "api/Basis/Dictionary/AllDictValue";
		public static String SaveCat = "api/Basis/Dictionary/SaveCat";
		public static String SaveItem = "api/Basis/Dictionary/SaveItem";
		public static String DeleteCat = "api/Basis/Dictionary/DeleteCat";
		public static String DeleteItem = "api/Basis/Dictionary/DeleteItem";
	}


		///#endregion


		///#region   Auth

	public static class DataRule
	{
		public static String Save = "";
		public static String Delete = "";
		public static String GetById = "";
		public static String DataRules = "";

	}

	public static class FeildRule
	{
		public static String Save = "";
		public static String Delete = "";
		public static String GetById = "";
		public static String FeildRules = "";

	}

	public static class Legal
	{
		public static String Save = "api/Auth/Legal/Save";
		public static String Delete = "api/Auth/Legal/Delete";
		public static String GetById = "api/Auth/Legal/GetById";
		public static String Legals = "api/Auth/Legal/Legals";
		public static String MoveUp = "api/Auth/Legal/MoveUp";
		public static String MoveDown = "api/Auth/Legal/MoveDown";

		/** 
		 入参：     string
		 返回类型： "ActionResult Legal"
		 
		*/
		public static String ModifyDefaultLegal = "api/Auth/Legal/ModifyDefaultLegal";
	}

	public static class Org
	{
		public static String Save = "api/Auth/Org/Save";
		public static String Delete = "api/Auth/Org/Delete";
		public static String GetById = "api/Auth/Org/GetById";
		public static String Orgs = "api/Auth/Org/Orgs";
		public static String Pager = "api/Auth/Org/Pager";
	}

	public static class Division
	{
		public static String Save = "api/Auth/Division/Save";
		public static String Delete = "api/Auth/Division/Delete";
		public static String GetById = "api/Auth/Division/GetById";
		public static String Divisions = "api/Auth/Division/Divisions";
		public static String MoveUp = "api/Auth/Division/MoveUp";
		public static String MoveDown = "api/Auth/Division/MoveDown";
		public static String GetDivisionsByOrgId = "api/Auth/Division/GetDivisionsByOrgId";
	}

	public static class Resource
	{
		public static String Resources = "api/Auth/Resource/Resources";
		public static String GetResourcesByRoleId = "api/Auth/Resource/GetResourcesByRoleId";
	}

	public static class Menu
	{
		public static String Menus = "api/Auth/Menu/Resources";
	}

	public static class Role
	{
		public static String Save = "api/Auth/Role/Save";
		public static String AllocateUsers2Role = "api/Auth/Role/AllocateUsers2Role";
		public static String AllocateMenus2Role = "api/Auth/Role/AllocateMenus2Role";

		public static String Delete = "api/Auth/Role/Delete";
		public static String Roles = "api/Auth/Role/Roles";
		public static String GetById = "api/Auth/Role/GetById";
		public static String GetByIdDetail = "api/Auth/Role/GetByIdDetail";
		public static String GetByIdIncUsers = "api/Auth/Role/GetByIdIncUsers";
		public static String GetByIdIncMenus = "api/Auth/Role/GetByIdIncMenus";
	}

	public static class User
	{
		public static String AccountsToBeAudit4App = "api/Auth/User/AccountsToBeAudit4App";
		public static String Save = "api/Auth/User/Save";
		public static String Delete = "api/Auth/User/Delete";
		public static String GetById = "api/Auth/User/GetById";
		public static String Users = "api/Auth/User/Users";
		public static String Pager = "api/Auth/User/Pager";

		//交易账号的列表
		public static String Traders = "api/Auth/User/Traders";
		public static String ApproversOfContract = "api/Auth/User/ApproversOfContract";
		public static String ApproversOfPayment = "api/Auth/User/ApproversOfPayment";
		public static String ApproversOfCustomer = "api/Auth/User/ApproversOfCustomer";

		public static String GetMenusByUserId = "api/Auth/User/GetMenusByUserId";
		public static String GetUsersByRoleId = "api/Auth/User/GetUsersByRoleId";
		public static String ChangePassword = "api/Auth/User/ChangePassword";
		public static String ResetPassword = "api/Auth/User/ResetPassword";
	}

	public static class UserSet
	{
		public static String Save = "api/Auth/UserSet/Save";
		public static String MyUserSet = "api/Auth/UserSet/MyUserSet";
	}


		///#endregion


		///#region   Basis
	public static class GlobalSet
	{
		public static String Save = "api/Basis/GlobalSet/Save";
		public static String MyGlobalSet = "api/Basis/GlobalSet/MyGlobalSet";
		public static String InitDatabase = "api/Basis/GlobalSet/InitDatabase";
	}



	public static class Preset
	{
		//todo: Preset的api还没有定义
	}

	public static class Account
	{
		public static String Save = "api/Basis/Account/Save";
		public static String Delete = "api/Basis/Account/Delete";
		public static String GetById = "api/Basis/Account/GetById";
		public static String Accounts = "api/Basis/Account/Accounts";
		public static String MoveUp = "api/Basis/Account/MoveUp";
		public static String MoveDown = "api/Basis/Account/MoveDown";
		public static String BackAccounts = "api/Basis/Account/BackAccounts";
	}

	public static class Area
	{
		public static String Save = "api/Basis/Area/Save";
		public static String Delete = "api/Basis/Area/Delete";
		public static String GetById = "api/Basis/Area/GetById";
		public static String Areas = "api/Basis/Area/Areas";
		public static String MoveUp = "api/Basis/Area/MoveUp";
		public static String MoveDown = "api/Basis/Area/MoveDown";
		public static String BackAreas = "api/Basis/Area/BackAreas";
	}
	public static class FeeSetup
	{
		public static String FeeSetups = "api/Basis/FeeSetup/FeeSetups";
		public static String FeeSetupsByFeeType = "api/Basis/FeeSetup/FeeSetupsByFeeType";
		public static String Save = "api/Basis/FeeSetup/Save";
		public static String Delete = "api/Basis/FeeSetup/Delete";
	}
	public static class EstPremiumSetup
	{
		public static String EstPremiumSetups = "api/Basis/EstPremiumSetup/EstPremiumSetups";
		public static String Save = "api/Basis/EstPremiumSetup/Save";
		public static String Delete = "api/Basis/EstPremiumSetup/Delete";
	}
	public static class Bank
	{
		public static String Save = "api/Basis/Bank/Save";
		public static String Delete = "api/Basis/Bank/Delete";
		public static String GetById = "api/Basis/Bank/GetById";
		public static String Banks = "api/Basis/Bank/Banks";
		public static String BackBanks = "api/Basis/Bank/BackBanks";
	}

	public static class Brand
	{
		public static String Save = "api/Basis/Brand/Save";
		public static String Delete = "api/Basis/Brand/Delete";
		public static String GetById = "api/Basis/Brand/GetById";
		public static String Brands = "api/Basis/Brand/Brands";
		public static String BrandsByCommodityId = "api/Basis/Brand/BrandsByCommodityId";
		public static String BackBrands = "api/Basis/Brand/BackBrands";
	}

	public static class Commodity
	{
		public static String Save = "api/Basis/Commodity/Save";
		public static String Delete = "api/Basis/Commodity/Delete";
		public static String GetById = "api/Basis/Commodity/GetById";
		public static String Commodities = "api/Basis/Commodity/Commodities";
		public static String BackCommodities = "api/Basis/Commodity/BackCommodities";
	}

	/** 
	 内部台头 - 银行账号：对应表
	 
	*/
	public static class LegalBank
	{
		public static String Save = "api/Basis/LegalBank/Save";
		public static String Delete = "api/Basis/LegalBank/Delete";
		public static String GetById = "api/Basis/LegalBank/GetById";
		public static String LegalBanks = "api/Basis/LegalBank/LegalBanks";
		public static String LegalBanksByLegalId = "api/Basis/LegalBank/LegalBanksByLegalId";
		public static String BackLegalBanks = "api/Basis/LegalBank/BackLegalBanks";
	}

	public static class Market
	{
		public static String Save = "api/Basis/Market/Save";
		public static String Delete = "api/Basis/Market/Delete";
		public static String GetById = "api/Basis/Market/GetById";
		public static String MoveUp = "api/Basis/Market/MoveUp";
		public static String MoveDown = "api/Basis/Market/MoveDown";
		public static String Markets = "api/Basis/Market/Markets"; //返回全部的市场
		public static String SpotMarkets = "api/Basis/Market/SpotMarkets"; //仅返回现货市场
		public static String FuturesMarkets = "api/Basis/Market/FuturesMarkets"; //仅返回期货市场
		public static String BackMarkets = "api/Basis/Market/BackMarkets";
		public static String BackSpotMarkets = "api/Basis/Market/BackSpotMarkets";
		public static String BackFuturesMarkets = "api/Basis/Market/BackFuturesMarkets";

	}

	public static class Origin
	{
		public static String Save = "api/Basis/Origin/Save";
		public static String Delete = "api/Basis/Origin/Delete";
		public static String GetById = "api/Basis/Origin/GetById";
		public static String Origins = "api/Basis/Origin/Origins";
		public static String MoveUp = "api/Basis/Origin/MoveUp";
		public static String MoveDown = "api/Basis/Origin/MoveDown";
		public static String OriginsByCommodityId = "api/Basis/Origin/OriginsByCommodityId";
		public static String BackOrigins = "api/Basis/Origin/BackOrigins";
	}

	public static class Param
	{
		public static String Save = "";
		public static String Delete = "";
	}

	public static class Period
	{
		public static String Save = "api/Basis/Period/Save";
		public static String Delete = "api/Basis/Period/Delete";
		public static String GetById = "api/Basis/Period/GetById";
		public static String Periods = "api/Basis/Period/Periods";
		public static String Set2Current = "api/Basis/Period/Set2Current";
		public static String BackPeriods = "api/Basis/Period/BackPeriods";
	}

	public static class Instrument
	{
		public static String Save = "api/Basis/Instrument/Save";
		public static String Delete = "api/Basis/Instrument/Delete";
		public static String GetById = "api/Basis/Instrument/GetById";
		public static String Pager = "api/Basis/Instrument/Pager";
		public static String BackInstruments = "api/Basis/Instrument/BackInstruments";
		public static String Instruments = "api/Basis/Instrument/Instruments";
	}

	public static class PricingShortcut
	{
		public static String Save = "api/Basis/PricingShortcut/Save";
		public static String Delete = "api/Basis/PricingShortcut/Delete";
		public static String GetById = "api/Basis/PricingShortcut/GetById";
		public static String PricingShortcuts = "api/Basis/PricingShortcut/PricingShortcuts";
		public static String MoveUp = "api/Basis/PricingShortcut/MoveUp";
		public static String MoveDown = "api/Basis/PricingShortcut/MoveDown";
		public static String BackPricingShortcuts = "api/Basis/PricingShortcut/BackPricingShortcuts";
	}

	public static class Product
	{
		public static String Save = "api/Basis/Product/Save";
		public static String Delete = "api/Basis/Product/Delete";
		public static String GetById = "api/Basis/Product/GetById";
		public static String Products = "api/Basis/Product/Products";
		public static String MoveUp = "api/Basis/Product/MoveUp";
		public static String MoveDown = "api/Basis/Product/MoveDown";
		public static String ProductsByCommodityId = "api/Basis/Product/ProductsByCommodityId"; //返回指定品种的产品列表
		public static String BackProducts = "api/Basis/Product/BackProducts";
	}

	public static class Remark
	{
		public static String Save = "api/Basis/Remark/Save";
		public static String Delete = "api/Basis/Remark/Delete";
		public static String GetById = "api/Basis/Remark/GetById";
		public static String Remarks = "api/Basis/Remark/Remarks";
		public static String BackRemarks = "api/Basis/Remark/BackRemarks";
	}

	public static class Spec
	{
		public static String Save = "api/Basis/Spec/Save";
		public static String Delete = "api/Basis/Spec/Delete";
		public static String GetById = "api/Basis/Spec/GetById";
		public static String Specs = "api/Basis/Spec/Specs";
		public static String MoveUp = "api/Basis/Spec/MoveUp";
		public static String MoveDown = "api/Basis/Spec/MoveDown";
		public static String SpecsByCommodityId = "api/Basis/Spec/SpecsByCommodityId";
		public static String BackSpecs = "api/Basis/Spec/BackSpecs";
	}

	public static class Warehouse
	{
		public static String Save = "api/Basis/Warehouse/Save";
		public static String Delete = "api/Basis/Warehouse/Delete";
		public static String GetById = "api/Basis/Warehouse/GetById";
		public static String Warehouses = "api/Basis/Warehouse/Warehouses";
		public static String MoveUp = "api/Basis/Warehouse/MoveUp";
		public static String MoveDown = "api/Basis/Warehouse/MoveDown";
		public static String BackWarehouses = "api/Basis/Warehouse/BackWarehouses";
	}


		///#endregion


		///#region   Finance
	public static class Invoice
	{
		public static String InvoicePnLById = "api/Finance/Invoice/InvoicePnLById";
		public static String Save = "api/Finance/Invoice/Save"; //标准发票
		public static String Save4MultiLots = "api/Finance/Invoice/Save4MultiLots"; //非标准发票，同时包括多个批次
		public static String Save4SummaryNoteFee = "api/Finance/Invoice/Save4SummaryNoteFee"; //保存Note费用合计

		public static String Delete = "api/Finance/Invoice/Delete";
		public static String GetById = "api/Finance/Invoice/GetById";
		public static String Pager = "api/Finance/Invoice/Pager";
		public static String PagerViaSql = "api/Finance/Invoice/PagerViaSql"; //根据sql语句过滤
		public static String InvoicesByContractId = "api/Finance/Invoice/InvoicesByContractId";
		public static String InvoicesByLotId = "api/Finance/Invoice/InvoicesByLotId";
		public static String InvoicesByStorageId = "api/Finance/Invoice/InvoicesByStorageId"; //多批次发票
		public static String InvoicesByStorageId2 = "api/Finance/Invoice/InvoicesByStorageId2"; //不区分多批次单批次
		public static String InvoicesByCustomerId = "api/Finance/Invoice/InvoicesByCustomerId"; //获取指定客户的全部发票
		public static String InvoiceSettleTrial = "api/Finance/Invoice/InvoiceSettleTrial"; //发票盈亏结算 - 试算
		public static String InvoiceSettleOfficial = "api/Finance/Invoice/InvoiceSettleOfficial"; //发票盈亏结算 - 正式
		public static String InvoiceTradeReport = "api/Finance/Invoice/InvoiceTradeReport";
		public static String GetMaxSerilNo = "api/Finance/Invoice/GetMaxSerilNo";
		public static String IsInvoiceNoDuplicate = "api/Finance/Invoice/IsInvoiceNoDuplicate";
		//计算父子批次总共的发票数量(其中去掉了)
		public static String CalculateQuantityOfInvoice = "api/Finance/Invoice/CalculateQuantityOfInvoice";
		public static String InvoicesOfSplittedLotByLotId = "api/Finance/Invoice/InvoicesOfSplittedLotByLotId";
		public static String InvalidInvoiceById = "api/Finance/Invoice/InvalidInvoiceById";
	}

	public static class InvoiceGrade
	{
		public static String Save = "api/Finance/InvoiceGrade/Save"; //标准发票
		public static String Delete = "api/Finance/InvoiceGrade/Delete";
		public static String GetById = "api/Finance/InvoiceGrade/GetById";
		public static String GetByInvoiceId = "api/Finance/InvoiceGrade/GetByInvoiceId";
	}

	public static class Fee
	{
		public static String Save = "api/Finance/Fee/Save";
		public static String Delete = "api/Finance/Fee/Delete";
		public static String GetById = "api/Finance/Fee/GetById";
		public static String FeesByLotId = "api/Finance/Fee/FeesByLotId";
	}

	public static class SummaryFees
	{
		public static String Save = "api/Finance/SummaryFees/Save";
		public static String Delete = "api/Finance/SummaryFees/Delete";
		public static String GetById = "api/Finance/SummaryFees/GetById";
		public static String SummaryFeesByInvoiceId = "api/Finance/SummaryFees/SummaryFeesByInvoiceId";
	}

	public static class BankBalance
	{
		public static String Save = "api/Finance/BankBalance/Save";
		public static String Delete = "api/Finance/BankBalance/Delete";
		public static String GetById = "api/Finance/BankBalance/GetById";
		public static String GetBankBalancesByTradeDate = "api/Finance/BankBalance/GetBankBalancesByTradeDate";
		public static String BankBalances = "api/Finance/BankBalance/BankBalances";
		public static String Pager = "api/Finance/BankBalance/Pager";
	}

	public static class Fund
	{
		public static String Receive = "api/Finance/Fund/Receive";
		public static String Payment = "api/Finance/Fund/Payment";
		public static String Delete = "api/Finance/Fund/Delete";
		public static String GetById = "api/Finance/Fund/GetById";
		public static String FundsByContractId = "api/Finance/Fund/FundsByContractId";
		public static String FundsByLotId = "api/Finance/Fund/FundsByLotId";
		public static String FundsByInvoiceId = "api/Finance/Fund/FundsByInvoiceId";
		public static String Pager = "api/Finance/Fund/Pager";

		//特别处理付款申请
		public static String CreatePaymentDraft = "api/Finance/Fund/CreatePaymentDraft";

		//确认收付的完成标志，指实际上是否已经收款或者付款
		public static String PaymentConfirmed = "api/Finance/Fund/PaymentConfirmed";
	}

	public static class LC
	{
		public static String Import1By1 = "api/Finance/LC/Import1By1";
		public static String Save = "api/Finance/LC/Save";
		public static String Delete = "api/Finance/LC/Delete";
		public static String GetById = "api/Finance/LC/GetById";
		public static String LCs = "api/Finance/LC/LCs";
		public static String Pager = "api/Finance/LC/Pager";
		public static String ImportByExcel = "api/Finance/LC/ImportByExcel";

	}


		///#endregion


		///#region   Futures

	public static class Commission
	{
		public static String Save = "api/Futures/Commission/Save";
		public static String Delete = "api/Futures/Commission/Delete";
		public static String GetById = "api/Futures/Commission/GetById";
		public static String Commissions = "api/Futures/Commission/Commissions";
	}

	public static class Hedge
	{
		public static String Save = "api/Futures/Hedge/Save";

		//根据头寸标识，删除一条hedge记录
		/** 
		 入参: string
		 出参: ActionResult string
		 
		*/
		public static String DeleteByPositionId = "api/Futures/Hedge/DeleteByPositionId";

		/** 
		 入参: T Hedge
		 出参: ActionResult string
		 
		*/
		public static String Allocate2Lot = "api/Futures/Hedge/Allocate2Lot";

		/** 
		 入参: string:PositionId
		 出参: T Hedge
		 
		*/
		public static String GetByPositionId = "api/Futures/Hedge/GetByPositionId";
	}

	public static class Margin
	{
		public static String Save = "api/Futures/Margin/Save";
		public static String Delete = "api/Futures/Margin/Delete";
		public static String GetById = "api/Futures/Margin/GetById";
		public static String Margins = "api/Futures/Margin/Margins";
	}
	/** 
	 期货保证金
	 
	*/
	public static class MarginFlow
	{
		public static String Save = "api/Futures/MarginFlow/Save";
		public static String Delete = "api/Futures/MarginFlow/Delete";
		public static String GetById = "api/Futures/MarginFlow/GetById";
		public static String MarginFlows = "api/Futures/MarginFlow/MarginFlows";
		public static String Pager = "api/Futures/MarginFlow/Pager";
	}
	public static class Portfolio
	{
		public static String Save = "api/Futures/Portfolio/Save";
		public static String Delete = "api/Futures/Portfolio/Delete";
		public static String GetById = "api/Futures/Portfolio/GetById";
		public static String Portfolios = "api/Futures/Portfolio/Portfolios";
	}

	public static class Position
	{
		public static String PositionsByContractId = "api/Futures/Position/PositionsByContractId";
		public static String PositionsByLotId = "api/Futures/Position/PositionsByLotId";
		//当需要发票结算时，获取该发票的全部的头寸
		public static String PositionsBothWayByInvoiceId = "api/Futures/Position/PositionsBothWayByInvoiceId";
		public static String SplitPosition = "api/Futures/Position/SplitPosition";
		public static String Save = "api/Futures/Position/Save";
		public static String SaveVirtualSwapPosition = "api/Futures/Position/SaveVirtualSwapPosition";
		public static String GetVirtualSwapPositionByCounterpartId = "api/Futures/Position/GetVirtualSwapPositionByCounterpartId";

		public static String Delete = "api/Futures/Position/Delete";
		public static String GetById = "api/Futures/Position/GetById";
		public static String GetCounterPartById = "api/Futures/Position/GetCounterPartById";
		public static String Pager = "api/Futures/Position/Pager";
		public static String PagerM2M = "api/Futures/Position/PagerM2M";
		public static String getM2MPrice = "api/Futures/Position/getM2MPrice";
		public static String PagerAvailable4Invoice = "api/Finance/Invoice/PagerAvailable4Invoice";
		public static String PagerAvailable4Lot = "api/Futures/Position/PagerAvailable4Lot";
		public static String CreateVirtual = "api/Futures/Position/CreateVirtual";
		public static String CreateVirtual2 = "api/Futures/Position/CreateVirtual2";
		public static String Square = "api/Futures/Position/Square";
		public static String ImportLMEsViaList = "api/Futures/Position/ImportLMEsViaList";
		public static String ImportLMEsViaStream = "api/Futures/Position/ImportLMEsViaStream";
		public static String PagerLME = "api/Futures/Position/PagerLME";
		public static String PagerSFE = "api/Futures/Position/PagerSFE";
		public static String UpdateAveragePositionById = "api/Futures/Position/UpdateAveragePositionById";
		public static String Position2Lot = "api/Futures/Position/Position2Lot";
		public static String UnPosition2Lot = "api/Futures/Position/UnPosition2Lot";
		public static String GetCarryPostionsById = "api/Futures/Position/GetCarryPostionsById";
	}
	public static class Position4Broker
	{
		public static String Save = "api/Futures/Position4Broker/Save";
		public static String Delete = "api/Futures/Position4Broker/Delete";
		public static String GetById = "api/Futures/Position4Broker/GetById";
		public static String Pager = "api/Futures/Position4Broker/Pager";
		public static String Square = "api/Futures/Position4Broker/Square";
		public static String GenerateBrokerPosition = "api/Futures/Position4Broker/GenerateBrokerPosition";
	}
	public static class Square4Broker
	{
		public static String Save = "api/Futures/Square4Broker/Save";
		public static String Delete = "api/Futures/Square4Broker/Delete";
		public static String GetById = "api/Futures/Square4Broker/GetById";
		public static String Pager = "api/Futures/Square4Broker/Pager";
		public static String Square = "api/Futures/Square4Broker/Square";
	}
	public static class PositionSquared
	{
		public static String Save = "api/Futures/Square/Save";
		public static String Delete = "api/Futures/Square/Delete";
		public static String GetById = "api/Futures/Square/GetById";
		public static String SqaurePositionMannually = "api/Futures/Square/SqaureMannually";
		public static String SqaurePositionAutomatically = "api/Futures/Square/SqaureAutomatically";
		public static String Pager = "api/Futures/Square/Pager";
	}

	public static class Pricing
	{
		public static String SavePricingScheduled = "api/Futures/Pricing/SavePricingScheduled"; //正常点价
		public static String SavePricingExtended = "api/Futures/Pricing/SavePricingExtended"; //改期点价
		public static String SplitPricing = "api/Futures/Pricing/SplitPricing";
		public static String Delete = "api/Futures/Pricing/Delete";
		public static String GetById = "api/Futures/Pricing/GetById";
		public static String Pricings = "api/Futures/Pricing/Pricings";
		public static String Pager = "api/Futures/Pricing/Pager";
		public static String PricingRecordsByContractId = "api/Futures/Pricing/PricingRecordsByContractId";
		public static String PricingRecordsByLotId = "api/Futures/Pricing/PricingRecordsByLotId";
		public static String UpdatePriceById = "api/Futures/Pricing/UpdatePriceById";
		public static String UpdateLotPriceByLotId = "api/Futures/Pricing/UpdateLotPriceByLotId";

		public static String PricingByContractId = "api/Futures/Pricing/PricingByContractId";
		public static String PricingByLotId = "api/Futures/Pricing/PricingByLotId";
	}


		///#endregion


		///#region   Log

	public static class ExceptionLog
	{
		public static String SaveExceptionLogAnonymous = "api/Auth/Login/Save";
		public static String Save = "api/Basis/ExceptionLog/Save";
		public static String Pager = "api/Basis/ExceptionLog/Pager";
		public static String LogById = "api/Basis/ExceptionLog/LogById";
		public static String DeleteLogs = "api/Basis/ExceptionLog/DeleteLogs";
	}

	public static class MessageLog
	{
		public static String Save = "api/Basis/MessageLog/Save";
		public static String Pager = "api/Basis/MessageLog/Pager";
		public static String LogById = "api/Basis/MessageLog/LogById";
		public static String DeleteLogs = "api/Basis/MessageLog/DeleteLogs";
	}


		///#endregion


		///#region   Maintain

	public static class Calendar
	{
		public static String Save = "api/Maintain/Calendar/Save";
		public static String Delete = "api/Maintain/Calendar/Delete";
		public static String GetById = "api/Maintain/Calendar/GetById";
		public static String Calenders = "api/Maintain/Calendar/Calenders";
		public static String Pager = "api/Maintain/Calendar/Pager";
	}

	public static class Exchange
	{
		public static String Save = "api/Maintain/Exchange/Save";
		public static String Delete = "api/Maintain/Exchange/Delete";
		public static String GetById = "api/Maintain/Exchange/GetById";
		public static String Exchanges = "api/Maintain/Exchange/Exchanges";
		public static String Pager = "api/Maintain/Exchange/Pager";
	}

	public static class Reuter
	{
		public static String Import1By1 = "api/Maintain/Reuter/Import1By1";
		public static String Pager = "api/Maintain/Reuter/Pager";
	}

	public static class LME
	{
		public static String Sync = "api/Maintain/LME/Sync"; //在服务层直接远程的接口，同步LME市场数据
		public static String Save = "api/Maintain/LME/Save";
		public static String Delete = "api/Maintain/LME/Delete";
		public static String GetById = "api/Maintain/LME/GetById";
		public static String LMEs = "api/Maintain/LME/LMEs";
		public static String Pager = "api/Maintain/LME/Pager";
	}

	public static class SFE
	{
		public static String Save = "api/Maintain/SFE/Save";
		public static String Delete = "api/Maintain/SFE/Delete";
		public static String GetById = "api/Maintain/SFE/GetById";
		public static String SFEs = "api/Maintain/SFE/SFEs";
		public static String Pager = "api/Maintain/SFE/Pager";
	}

	public static class DSME
	{
		public static String Save = "api/Maintain/DSME/Save";
		public static String Delete = "api/Maintain/DSME/Delete";
		public static String GetById = "api/Maintain/DSME/GetById";
		public static String Domestics = "api/Maintain/DSME/Domestics";
		public static String Pager = "api/Maintain/DSME/Pager";
	}

	public static class LMB
	{
		public static String Save = "api/Maintain/LMB/Save";
		public static String Delete = "api/Maintain/LMB/Delete";
		public static String GetById = "api/Maintain/LMB/GetById";
		public static String Domestics = "api/Maintain/LMB/Domestics";
		public static String Pager = "api/Maintain/LMB/Pager";
	}


		///#endregion


		///#region   Partner

	public static class Customer
	{
		public static String GetById = "api/Partner/Customer/GetById";
		public static String GetByCustomerName = "api/Partner/Customer/GetByCustomerName";
		public static String Pager = "api/Partner/Customer/Pager";
		public static String Save = "api/Partner/Customer/Save";
		public static String Delete = "api/Partner/Customer/Delete/";
		public static String InternalCustomersOnly = "api/Partner/Customer/InternalCustomersOnly";
		public static String CustomersIncInternals = "api/Partner/Customer/CustomersIncInternals";
		public static String CustomersExcInternals = "api/Partner/Customer/CustomersExcInternals";

		//todo: 批量导入客户，期初时使用（空方法）
		public static String ImportCustomers = "api/Partner/Customer/ImportCustomers";
		//todo:空方法
		public static String Top10CustomerByTurnover = "api/Partner/Customer/Top10CustomerByTurnover";
		public static String Top10CustomerByProfit = "api/Partner/Customer/Top10CustomerByProfit";

	}

	public static class Liaison
	{
		public static String Save = "api/Partner/Liaison/Save";
		public static String Delete = "api/Partner/Liaison/Delete";
		public static String GetById = "api/Partner/Liaison/GetById";
		public static String Liaisons = "api/Partner/Liaison/Liaisons";

		/** 
		 入参：     string
		 返回类型： ActionResult IList Liaison
		 
		*/
		public static String GetLiaisonsByCustomerId = "api/Partner/Liaison/GetLiaisonsByCustomerId";
	}

	public static class Broker
	{
		public static String Save = "api/Partner/Broker/Save";
		public static String Delete = "api/Partner/Broker/Delete";
		public static String GetById = "api/Partner/Broker/GetById";
		public static String Brokers = "api/Partner/Broker/Brokers";
		public static String BackBrokers = "api/Partner/Broker/BackBrokers";
	}

	public static class CustomerTitle
	{
		public static String Save = "api/Partner/CustomerTitle/Save";
		public static String Delete = "api/Partner/CustomerTitle/Delete";
		public static String GetById = "api/Partner/CustomerTitle/GetById";
		public static String CustomerTitles = "api/Partner/CustomerTitle/CustomerTitles";

		/** 
		 入参：     string
		 返回类型： ActionResult IList 
		 
		*/
		public static String GetTitlesByCustomerId = "api/Partner/CustomerTitle/GetTitlesByCustomerId";
	}


		///#endregion


		///#region Spot

	public static class Contract
	{
		//特殊的业务，
		//新增时，同时创建Bvi的销售合同和Sm的采购合同
		//修改时，只修改Bvi的销售合同，不修改对应的"商贸采购合同"
		public static String SaveHead4BviToSm = "api/Physical/Contract/SaveHead4BviToSm";

		//特殊的业务，只修改"商贸采购合同"本身，不修改其它的信息        
		public static String SaveHead4SmToBvi = "api/Physical/Contract/SaveHead4SmToBvi";

		public static String SaveHeadOfContractRegular = "api/Physical/Contract/SaveHeadOfContractRegular";
		public static String Delete = "api/Physical/Contract/Delete";
		public static String GetById = "api/Physical/Contract/GetById";
		public static String ContractViewById = "api/Physical/Contract/ContractViewById";
		public static String Pager = "api/Physical/Contract/Pager";
		public static String ContractsByCustomerId = "api/Physical/Contract/ContractsByCustomerId";
		public static String GetMaxSerialNo = "api/Physical/Contract/GetMaxSerialNo"; //获取最大的序列编号

		//只是为了绑定下拉框
		public static String Pager4Glue = "api/Physical/Contract/Pager4Glue";
	}

	public static class Lot
	{
		//SplitLot
		public static String SplitLot = "api/Physical/Lot/SplitLot";

		//分配点价结果
		public static String AllocatePricing = "api/Physical/Lot/AllocatePricing";
		//移除点价结果
		public static String RemovePricing = "api/Physical/Lot/RemovePricing";
		//分配保值头寸
		public static String AllocatePosition = "api/Physical/Lot/AllocatePosition";
		//移除保值头寸
		public static String RemovePosition = "api/Physical/Lot/RemovePosition";

		public static String SaveLot4BviToSm = "api/Physical/Lot/SaveLot4BviToSm";

		public static String MarkColor = "api/Physical/Lot/MarkColor";
		public static String ClearColor = "api/Physical/Lot/ClearColor";
		//保存长单合同的头部
		public static String SaveHeadOfContractRegular = "api/Physical/Contract/SaveHeadOfContractRegular";
		//保存长单合同的批次
		public static String SaveLotOfContractRegular = "api/Physical/Lot/SaveLotOfContractRegular";
		//删除长单合同的某个批次
		public static String DeleteLotOfContractRegular = "api/Physical/Lot/DeleteLotOfContractRegular";
		//通用：保存一个批次，不作特殊处理
		public static String SaveLot = "api/Physical/Lot/SaveLot";
		//保存临单合同(头部+批次)
		public static String SaveContractProvisional = "api/Physical/Lot/SaveContractProvisional";
		//删除临单合同(头部和批次)
		public static String DeleteContractProvisional = "api/Physical/Lot/DeleteContractProvisional";

		//修改初始化的批次的执行结果，由于导入时可能有错，允许之后人工修改
		public static String ModifyExecuted4Initiated = "api/Physical/Lot/ModifyExecuted4Initiated";

		public static String GetById = "api/Physical/Lot/GetById";
		public static String LotViewById = "api/Physical/Lot/LotViewById";
		public static String Lots = "api/Physical/Lot/Lots";
		public static String Pager = "api/Physical/Lot/Pager";
		public static String PagerAgreed = "api/Physical/Lot/PagerAgreed";
		public static String Pager4Glue = "api/Physical/Lot/Pager4Glue";
		public static String Lot4MTMQuery = "api/Physical/Lot/Lot4MTMQuery";
		public static String Lot4MTMQueryNew = "api/Physical/Lot/Lot4MTMQueryNew";
		public static String Lot4MTMQueryNew2 = "api/Physical/Lot/Lot4MTMQueryNew2"; //批次盈亏结算含回购
		public static String Lot4Fees = "api/Physical/Lot/Lot4Fees"; //批次费用一览
		public static String Lot4MTMQuery2 = "api/Physical/Lot/Lot4MTMQuery2";
		public static String Lot4MTMQuery3 = "api/Physical/Lot/Lot4MTMQuery3";
		public static String GetLotsByLotId = "api/Physical/Lot/GetLotsByLotId";
		public static String GetLotsByCustomerId = "api/Physical/Lot/LotsByCustomerId"; //获取指定客户的全部待收款和待付款的批次
		public static String GetInvoicesByCustomerId = "api/Physical/Lot/InvoicesByCustomerId"; //获取指定客户的全部待收款和待付款的批次

		public static String SaveStorageOuts = "api/Physical/Lot/SaveStorageOuts";

		//根据合同标识，取得所有批次的列表
		public static String GetLotsByContractId = "api/Physical/Lot/GetLotsByContractId";

		//根据合同标识，取得唯一的一个批次信息
		public static String GetFirstLotByContractId = "api/Physical/Lot/GetFirstLotByContractId";

		public static String UpdateLotInvoicFlagById = "api/Physical/Lot/UpdateLotInvoicFlagById";
							//只是用来更新开票的标志，除了JHC，可能还会有别的用户、有此要求

		public static String AllLotsWithParams = "api/Physical/Lot/AllLotsWithParams"; //有参数，但是返回符合条件的全部记录

		//api还没有实现
		public static String UpdateLotPlById = "api/Physical/Lot/UpdateLotPlById"; //更新（计算）批次的盈亏

		public static String ConfirmLotQuantityDelivered = "api/Physical/Lot/ConfirmLotQuantityDelivered";

		//修改QP
		public static String SaveQPRecord = "api/Physical/Lot/SaveQPRecord";
		//查询历史QP
		public static String PagerQPRecord = "api/Physical/Lot/PagerQPRecord";
		//删除QP记录
		public static String DeleteQPRecord = "api/Physical/Lot/DeleteQPRecord";
		/** 
		 生成预估费用
		 
		*/
		public static String GenerateFees = "api/Physical/Lot/GenerateFees";
		/** 
		 删除费用
		 
		*/
		public static String DeleteFeesByLotId = "api/Physical/Lot/DeleteFeesByLotId";
		/** 
		 更新资金标记
		 
		*/
		public static String UpdateLotIsFunded = "api/Physical/Lot/UpdateLotIsFunded";
		/** 
		 更新费用
		 
		*/
		public static String UpdateLotFee = "api/Physical/Lot/UpdateLotFee";

		public static String SplitLotQuantity = "api/Physical/Lot/SplitLotQuantity";

		public static String LotSettleTrial = "api/Physical/Lot/LotSettleTrial"; //发票盈亏结算 - 试算
		public static String LotSettleOfficial = "api/Physical/Lot/LotSettleOfficial"; //发票盈亏结算 - 正式
		public static String LotPnLById = "api/Physical/Lot/LotPnLById";
		/** 
		 更新保值均价
		 
		*/
		public static String UpdateLotHedgedPrice = "api/Physical/Lot/UpdateLotHedgedPrice";
		/** 
		 更新Spread（Storage）
		 
		*/
		public static String UpdateLotSpread = "api/Physical/Lot/UpdateLotSpread";
		//计算批次的发货数量
		public static String CalculateDeliveryedQuantity = "api/Physical/Lot/CalculateDeliveryedQuantity";

		public static String SaveAccountDateByLot = "api/Physical/Lot/SaveAccountDateByLot";
	}

	public static class Tip
	{
		public static String Save = "api/Physical/Tips/Save";
		public static String Delete = "api/Physical/Tips/Delete";
		public static String GetById = "api/Physical/Tips/GetById";
		public static String Notes = "api/Physical/Tips/Notes";
		public static String TipsByLotId = "api/Physical/Tips/TipsByLotId"; //获取指定批次的全部笔记
	}

	public static class Grade
	{
		public static String Save = "api/Physical/Grade/Save";
		public static String Delete = "api/Physical/Grade/Delete";
		public static String GetById = "api/Physical/Grade/GetById";
		public static String Notes = "api/Physical/Grade/Grades";
		public static String GradesByContractId = "api/Physical/Grade/GradesByContractId";
		public static String GradesByInvoiceId = "api/Physical/Grade/GradesByInvoiceId";
	}
	public static class DischargingPriceDiff
	{
		public static String Save = "api/Physical/DischargingPriceDiff/Save";
		public static String Delete = "api/Physical/DischargingPriceDiff/Delete";
		public static String GetById = "api/Physical/DischargingPriceDiff/GetById";
		public static String PriceDiffsByContractId = "api/Physical/DischargingPriceDiff/PriceDiffsByContractId";
	}
	public static class Storage
	{
		//当前的库存
		public static String Holding = "api/Inventory/Storage/Holding";

		//待确认入库的工厂商品运输明细
		public static String PagerFactories = "api/Inventory/Storage/PagerFactories";

		//已经确认入库、可供Bvi出库的商品明细（商品明细 = 交付记录）
		public static String PagerBvi4Out = "api/Inventory/Storage/PagerBvi4Out";
		//BVI已经发货给商贸的商品明细（商品明细 = 交付记录）
		public static String PagerBviMaked = "api/Inventory/Storage/PagerBviMaked";
		//仍然可以供开票的商品明细（商品明细 = 交付记录），历为商品不可以重复开票
		public static String PagerAvailable4Invoice = "api/Inventory/Storage/PagerAvailable4Invoice";
		public static String CreateStorageInAndOut = "api/Inventory/Storage/CreateStorageInAndOut";
		public static String DeleteStorageById = "api/Inventory/Storage/DeleteStorageById";
		public static String DeleteMultiStorages = "api/Inventory/Storage/DeleteMultiStorages";
		public static String Save = "api/Inventory/Storage/Save";
		/** 
		 保存通知货量
		 
		*/
		public static String SaveNoticeStorage = "api/Inventory/Storage/SaveNoticeStorage";

		public static String GetById = "api/Inventory/Storage/GetById";
		public static String GetByNoticeStorageId = "api/Inventory/Storage/GetByNoticeStorageId";

		public static String Pager = "api/Inventory/Storage/Pager";
		public static String StoragesByCustomerId = "api/Inventory/Storages/StoragesByCustomerId"; //获取指定客户的全部出入库明细
		public static String StoragesBySummaryFeesId = "api/Inventory/Storage/StoragesBySummaryFeesId"; //获取指定合计费用ID的全部出入库明细

		//导入期初库存
		public static String ImportInitStorages = "api/Inventory/Storage/ImportInitStorages"; //上传初始化的数据列表(list)

		//只返回可用数量大于0的在库列表
		public static String StoragesAvailable = "api/Inventory/Storage/StoragesAvailable"; //上传初始化的数据列表(list)

		//取得指定批次的出库记录
		public static String StorageOutsByLotId = "api/Inventory/Storage/StorageOutsByLotId";

		//取得指定批次的入库记录
		public static String StorageInsByLotId = "api/Inventory/Storage/StorageInsByLotId";

		//取得指定订单的出库入记录
		public static String StoragesByContractId = "api/Inventory/Storage/StoragesByContractId";

		//取得指定批次的出库入记录
		public static String StoragesByLotId = "api/Inventory/Storage/StoragesByLotId";

		//取得指定项目名称全部出入库记录(包含分拆数据)
		public static String StoragesByProjectName = "api/Inventory/Storage/StoragesByProjectName";
		public static String SourceStoragesById = "api/Inventory/Storage/SourceStoragesById";
		public static String BviSourceStoragesById = "api/Inventory/Storage/BviSourceStoragesById";
		public static String MergeSourceStoragesById = "api/Inventory/Storage/MergeSourceStoragesById";
		//分拆
		public static String SplitStorage = "api/Inventory/Storage/SplitStorage";

		//修改实数
		public static String ReviseQuantity = "api/Inventory/Storage/ReviseQuantity";

		//回补虚拟的库存
		public static String FillBack = "api/Inventory/Storage/FillBack";

		//工厂的货：同时保存多个收货记录的检验结果
		public static String SaveFactories = "api/Inventory/Storage/SaveFactories";

		//工厂的货：导入工厂提供的商品运输明细，到表Storage
		public static String ImportFactory = "api/Inventory/Storage/ImportFactory";

		//工厂的货：SM或SM从工厂提供的商品运输明细中选择
		public static String TakeStoragesFromFactory = "api/Inventory/Storage/TakeStoragesFromFactory";

		//SM: 商贸收货，从BVI发货记录中复制、成为商贸的收货记录
		public static String CopyStorageInsFromBviOuts = "api/Inventory/Storage/CopyStorageInsFromBviOuts";
		//回购
		public static String CopyStorageInsFromOuts = "api/Inventory/Storage/CopyStorageInsFromOuts";

		//Bvi：BVI收货，一次只可以解除一个对应关系
		public static String DeleteFactoryIns = "api/Inventory/Storage/DeleteFactoryIns";
		//Bvi：BVI收货，只移除关联
		public static String RemoveFactoryIns = "api/Inventory/Storage/RemoveFactoryIns";

		//标准：一次可以发货多个
		public static String CreateStorageOuts = "api/Inventory/Storage/CreateStorageOuts";

		//标准方法：批量删除收货记录
		public static String DeleteStorageIns = "api/Inventory/Storage/DeleteStorageIns";

		//标准方法：批量删除发货记录
		public static String DeleteStorageOuts = "api/Inventory/Storage/DeleteStorageOuts";

		//标准方法：批量退货
		public static String ReturnStorage = "api/Inventory/Storage/ReturnStorage";

		//标准：多个收货记录合并为一条，现实中可能很少用
		public static String MergeBvis = "api/Inventory/Storage/MergeBvis";
		//撤消合并
		public static String MergeCancel = "api/Inventory/Storage/MergeCancel";

		//批量修改非关键信息
		public static String AmendNonKeyInfo = "api/Inventory/Storage/AmendNonKeyInfo";

	}

		///#endregion


		///#region   Report


	public static class Report
	{
		//客户业务和资金明细对账表 both of below have been done
		public static String PagerCustomerBalance4OneCustomer = "api/Report/CustomerReport/PagerCustomerBalance4OneCustomer";
		public static String PagerCustomerBalance4MultiCustomers = "api/Report/CustomerReport/PagerCustomerBalance4MultiCustomers";

		//报表：已经点价、但是没有开票的销售合同 done
		public static String R1Report = "api/Report/SpotReport/R1Report";

		//报表：暂定价、已付款的发票敞口统计 undone
		public static String R2Report = "api/Report/SpotReport/R2Report";

		//报表：保值头寸列表_未关联现货
		public static String R3Report = "api/Report/SpotReport/R3Report";

		//报表：保值头寸列表_已关联合同未关联发票
		public static String R4Report = "api/Report/SpotReport/R4Report";

		//报表：均价合同的点价完成情况
		public static String R5Report = "api/Report/SpotReport/R5Report";

		//报表：本周必须完成点价的合同的列表
		public static String R6Report = "api/Report/SpotReport/R6Report";

		//报表：暂定价发票的动态盈亏评估
		public static String R7Report = "api/Report/SpotReport/R7Report";

		//报表：暂定价发票的动态盈亏评估
		public static String R8Report = "api/Report/SpotReport/R8Report";

		//报表：存在点价或保值敞口的现货列表
		public static String R11Report = "api/Report/SpotReport/R11Report";

		//报表：点价和保值敞口汇总表
		public static String R12Report = "api/Report/SpotReport/R12Report";

		//报表：保值日期与预计销售日期不一致的批次列表
		public static String R13Report = "api/Report/SpotReport/R13Report";

		//报表：SPBOOK
		public static String R14Report = "api/Report/SpotReport/R14Report";

		//报表：交易报告
		public static String TradeReportSale = "api/Report/SpotReport/TradeReportSale";

		//报表：套期保值交易审批单
		public static String PositionApproveReport = "api/Report/SpotReport/PositionApproveReport";

		//报表：套期保值详情表
		public static String PositionDetailReport = "api/Report/SpotReport/PositionDetailReport";

		//报表：持仓汇总表
		public static String DailyPositionReportSave = "api/Report/SpotReport/DailyPositionReportSave";
		public static String DailyPositionReportSearch = "api/Report/SpotReport/DailyPositionReportSearch";

		//报表：预计销售日未调保值列表
		public static String EstUnHedgeReport = "api/Report/SpotReport/EstUnHedgeReport";

		public static class ModelPnL
		{
			public static String GetReportPnLSpotOneOne = "";
			public static String GetReportPLSpotOneManyPerBuy = "";
			public static String GetReportPnLSpotOneManyPerSell = "";
		}

		public static String FundBalanceMonthly = "api/Report/Report/FundBalanceMonthly";
		public static String StorageQuantity = "api/Report/Report/StorageQuantity";
		public static String StorageAmount = "api/Report/Report/StorageQuantity";
	}


	public static class RptCustomerBalanceL2Entity
	{
		//todo api未实现
		public static String Pager = "api/Report/RptCustomerBalanceL2Entity/Pager";
	}

	//todo: -- zyx
	public static class SettleDaily
	{
		public static String GetById = "api/Report/SettleDaily/GetById";
		public static String Pager = "api/Report/SettleDaily/Pager";
		public static String Save = "api/Report/SettleDaily/Save";

		/** 
		 入参：     string
		 返回类型： "ActionResult IList"
		 
		*/
		public static String SettleDailies = "api/Report/SettleDaily/SettleDailies";
	}


	public static class ZrSettleMonthly
	{
		public static String GetById = "api/Report/ZrSettleMonthly/GetById";
		public static String Pager = "api/Report/ZrSettleMonthly/Pager";
		public static String Save = "api/Report/ZrSettleMonthly/Save";

		/** 
		 入参：     string
		 返回类型： "ActionResult IList"
		 
		*/
		public static String ZrSettleMonthlies = "api/Report/ZrSettleMonthly/ZrSettleMonthlies";
	}

	public static class SettleYearly
	{
		public static String GetById = "api/Report/SettleYearly/GetById";
		public static String Pager = "api/Report/SettleYearly/Pager";
		public static String Save = "api/Report/SettleYearly/Save";
		public static String Delete = "api/Report/SettleYearly/Delete/";

		/** 
		 入参：     
		 返回类型： 
		 
		*/
		public static String SettleYearlies = "api/Report/SettleYearly/SettleYearlies";
	}


		///#endregion


		///#region Logs

	public static class Logs
	{
		public static String ExceptionLogsPager = "api/Basis/Common/ExceptionLogsPager";
	}


		///#endregion


		///#region 配置文件的参数

	private static String _serviceUrl = "";

	public static String getServiceUrl()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_serviceUrl))
		{
			_serviceUrl = ApiServerConfiguration.Get("ServiceUrl");
		}

		return _serviceUrl;
	}
	public static void setServiceUrl(String value)
	{
		ApiServerConfiguration.Set("ServiceUrl", value);
		_serviceUrl = value;
	}

	private static String _servicePort = "";

	public static String getServicePort()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_servicePort))
		{
			_servicePort = ApiServerConfiguration.Get("ServicePort");
		}

		return _servicePort;
	}
	public static void setServicePort(String value)
	{
		ApiServerConfiguration.Set("ServicePort", value);
		_servicePort = value;
	}
	private static String _serviceDomain = "";

	public static String getServiceDomain()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_serviceDomain))
		{
			_serviceDomain = ApiServerConfiguration.Get("ServiceDomain");
		}

		return _serviceDomain;
	}
	public static void setServiceDomain(String value)
	{
		ApiServerConfiguration.Set("ServiceDomain", value);
		_serviceDomain = value;
	}

		//get { return ApiServerConfiguration.Get("ServiceDomain"); }

	private static String _updateUrl = "";

	public static String getUpdateUrl()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_updateUrl))
		{
			_updateUrl = ApiServerConfiguration.Get("UpdateUrl");
		}

		return _updateUrl;
	}
	public static void setUpdateUrl(String value)
	{
		ApiServerConfiguration.Set("UpdateUrl", value);
		_updateUrl = value;
	}


	private static String _updatePort = "";

	public static String getUpdatePort()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_updatePort))
		{
			_updatePort = ApiServerConfiguration.Get("UpdatePort");
		}

		return _updatePort;
	}
	public static void setUpdatePort(String value)
	{
		ApiServerConfiguration.Set("UpdatePort", value);
		_updatePort = value;
	}

	private static String _updateDomain = "";

	public static String getUpdateDomain()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_updateDomain))
		{
			_updateDomain = ApiServerConfiguration.Get("UpdateDomain");
		}

		return _updateDomain;
	}
	public static void setUpdateDomain(String value)
	{
		ApiServerConfiguration.Set("UpdateDomain", value);
		_updateDomain = value;
	}

		//get { return ApiServerConfiguration.Get("UpdateDomain"); }

	private static String _syncUrl = "";

	public static String getSyncUrl()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_syncUrl))
		{
			_syncUrl = ApiServerConfiguration.Get("SyncUrl");
		}

		return _syncUrl;
	}
	public static void setSyncUrl(String value)
	{
		ApiServerConfiguration.Set("SyncUrl", value);
		_syncUrl = value;
	}


	private static String _syncPort = "";

	public static String getSyncPort()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_syncPort))
		{
			_syncPort = ApiServerConfiguration.Get("SyncPort");
		}

		return _syncPort;
	}
	public static void setSyncPort(String value)
	{
		ApiServerConfiguration.Set("SyncPort", value);
		_syncPort = value;
	}

	private static String _syncDomain = "";

	public static String getSyncDomain()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_syncDomain))
		{
			_syncDomain = ApiServerConfiguration.Get("SyncDomain");
		}

		return _syncDomain;
	}
	public static void setSyncDomain(String value)
	{
		ApiServerConfiguration.Set("SyncDomain", value);
		_syncDomain = value;
	}

		//get { return ApiServerConfiguration.Get("UpdateDomain"); }

	private static String _language = "";
	public static String getLanguage()
	{
		if (DotNetToJavaStringHelper.isNullOrEmpty(_language))
		{
			_language = ApiServerConfiguration.Get("Language");
		}

		return _language;
	}
	public static void setLanguage(String value)
	{
		ApiServerConfiguration.Set("Language", value);
		_language = value;
	}

		///#endregion

}