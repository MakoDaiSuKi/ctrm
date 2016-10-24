package com.smm.ctrm.dto.res;

public class ActionStatus {
	
	public final static String SUCCESS = "success";
    public final static String ERROR = "error";


    //期货类型
    public final static String Futures = "F";
    public final static String Average = "A";
    public final static String Option = "O";



    //期货买卖方向
    public final static String LS_LONG = "L";
    public final static String LS_SHORT = "S";


    //价格类型
    public final static String MajorType_Fix = "F";
    public final static String MajorType_Pricing = "P";
    public final static String MajorType_Average = "A";


    //价格类型--升贴水
    public final static String PremiumType_Fix = "F";
    public final static String PremiumType_Average = "A";


    //发票类型 
    public final static String InvoiceType_MultiLots = "L";
    public final static String InvoiceType_Final = "F";
    public final static String InvoiceType_Provisional = "P";
    public final static String InvoiceType_Adjust = "A";
    public final static String InvoiceType_Misc = "M";
    public final static String InvoiceType_Note = "N";
    public final static String InvoiceType_SummaryNote = "SN";


    //现货买卖类型
    public final static String SpotType_Purchase = "B";
    public final static String SpotType_Sell = "S";


    //通用的业务状态标志
    public final static int Status_Draft = -9;
    public final static int Status_Pending = 0;
    public final static int Status_Agreed = 1;
    public final static int Status_Deny = -1;
    public final static int Status_Close = 8;
    public final static int Status_Cancel = 9;


    //费用类型 --- 杂费
    public final static String FeeCode_Transportation = "1";
    public final static String FeeCode_Test = "2";
    public final static String FeeCode_Custom = "3";
    public final static String FeeCode_Bank = "4";
    public final static String FeeCode_Inventory = "5";
    public final static String FeeCode_Insurance = "6";
    public final static String FeeCode_Cost = "7";
    public final static String FeeCode_Goods = "9";
    public final static String FeeCode_Other = "10";
    public final static String FeeCode_BankDocumentsFee = "11";
    public final static String FeeCode_BuyDocumentsFee = "12";
    public final static String FeeCode_HedgeFee = "13";
    public final static String FeeCode_DisputeFine = "14";
    public final static String FeeCode_NA = "-1";



    //资金出入的方向
    public final static String DC_Debit = "D";
    public final static String DC_Credit = "C";


    //附件类型
    public final static String AttachType_Customer = "Customer";
    public final static String AttachType_Contract = "Contract";
    public final static String AttachType_ContractDoc = "ContractDoc";
    public final static String AttachType_Storage = "Storage";
    public final static String AttachType_Invoice = "Invoice";
    public final static String AttachType_Fund = "Fund";
    public final static String AttachType_Pricing = "Pricing";
    public final static String AttachType_Position = "Position";
    public final static String AttachType_NA = "NA";
    public final static String AttachType_ReceiptShip = "ReceiptShip";
    

}
