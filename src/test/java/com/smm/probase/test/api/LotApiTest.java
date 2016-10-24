package com.smm.probase.test.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smm.ctrm.domain.Physical.Lot;
import com.smm.ctrm.domain.Physical.QPRecord;
import com.smm.ctrm.util.URLUtil;
import com.smm.probase.test.JunitTest;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghao on 2016/6/30.
 *
 *
 */
//@RunWith(SpringJUnit4ClassRunner.class)  //注明测试类运行者
//@ContextConfiguration(locations = { "classpath:dispatcher-servlet.xml", "classpath:spring-hibernate.xml" }) //加载spring配置文件
//@Transactional  //测试结束后，所有数据库变更将自动回滚
public class LotApiTest {

    private static Logger logger= Logger.getLogger(LotApiTest.class);


    private static Map<String,String> params = new HashMap<>();

    static {

        params.put("Keyword","BCUSB140175/80");
        params.put("CreatedBy","陈振忠");
        params.put("Currency","USD");
        params.put("SpotType","外贸");
        params.put("Statuses","1");
        params.put("LegalIds","8C706B1C-B4D1-420A-9EA8-A3EE00CF40AA,98DB488D-BFA9-4FFF-A143-A3EE00CF57F2");

        params.put("StartDate","2013-12-31T00:00:00");
        params.put("EndDate","2016-12-31T00:00:00");

        params.put("IsInvoiced","true");
        params.put("IsDelivered","true");
        params.put("SpotDirection","");
        params.put("IsAverageLotOnly","true");
        params.put("AverageAtCurrentDuration","false");
        params.put("RePricing","true");

        params.put("PageSize","10");
        params.put("PageIndex","1");
        params.put("SortBy","CreatedAt");
        params.put("OrderBy","desc");

    }



    private static Map<String,String> params1 = new HashMap<>();
    static {

        params1.put("Currency","");
        params1.put("SpotType","");
        params1.put("Statuses","");
        params1.put("LegalIds","d0b94b87-194c-4ea6-b00a-a42100ec1339, 98db488d-bfa9-4fff-a143-a3ee00cf57f2, 8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa");
        params1.put("MarkColor","0");

        params1.put("CustomerId",null);
        params1.put("TraderId",null);
        params1.put("LegalId",null);
        params1.put("SpotDirection","");


        params1.put("IsPriced",null);
        params1.put("IsInvoiced",null);
        params1.put("IsDelivered",null);
        params1.put("IsHedged",null);
        params1.put("TradeDate",null);
        params1.put("StartDate",null);
        params1.put("EndDate",null);


        params1.put("IsAverageLotOnly","false");
        params1.put("AverageAtCurrentDuration","false");
        params1.put("RePricing","false");
        params1.put("CurrentPageOnly",null);
        params1.put("ReCalculatePL",null);

        params1.put("MajorType",null);
        params1.put("PremiumType",null);
        params1.put("CreatedBy","");
        params1.put("Keyword","");

        params1.put("PageSize","100");
        params1.put("PageIndex","1");
        params1.put("SortBy","HeadNo");
        params1.put("OrderBy","Descending");

    }


    private static Map<String,String> params2 = new HashMap<>();
    static {

        params2.put("Currency","");
        params2.put("SpotType","");
        params2.put("Statuses","");
        params2.put("LegalIds","");
        params2.put("MarkColor","0");

        params2.put("CustomerId",null);
        params2.put("TraderId",null);
        params2.put("LegalId",null);
        params2.put("SpotDirection","");


        params2.put("IsPriced",null);
        params2.put("IsInvoiced","false");
        params2.put("IsDelivered","false");
        params2.put("IsHedged",null);
        params2.put("TradeDate",null);
        params2.put("StartDate",null);
        params2.put("EndDate",null);

        params2.put("IsAverageLotOnly","true");
        params2.put("AverageAtCurrentDuration","true");
        params2.put("RePricing","false");
        params2.put("CurrentPageOnly",null);
        params2.put("ReCalculatePL",null);

        params2.put("MajorType",null);
        params2.put("PremiumType",null);
        params2.put("CreatedBy","");
        params2.put("Keyword","balsb160039");

        params2.put("PageSize","10");
        params2.put("PageIndex","1");
        params2.put("SortBy","HeadNo");
        params2.put("OrderBy","Descending");

    }


    @Test
    public void Pager(){

        String url= JunitTest.host + "api/Physical/Lot/Pager";

        String result = URLUtil.post(url,params1,JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void Pager4Glue(){

        String url= JunitTest.host + "api/Physical/Lot/Pager4Glue";

        String result = URLUtil.post(url,null,JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);
    }



    @Test
    public void PagerAgreed(){

        String url= JunitTest.host + "api/Physical/Lot/PagerAgreed";

        String result = URLUtil.post(url,params,JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);
    }


    @Test
    public void PagerQPRecord(){

        String url= JunitTest.host + "api/Physical/Lot/PagerQPRecord";

        String lotId="C96FD127-A0DB-48F1-9B44-A4E30108A2E6";

        String result = URLUtil.post(url,lotId,JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);
    }


    @Test
    public void SaveQPRecord(){

        String url= JunitTest.host + "api/Physical/Lot/SaveQPRecord";

        String parameter="{\"LotId\":\"ddcd7c10-49c6-4545-b530-a63c00fce50e\",\"Lot\":{\"CounterpartId\":null,\"Brands\":null,\"ContractId\":\"d1e53d80-ce9c-4318-ab20-a63a013dad51\",\"Contract\":{\"TraderId\":\"52fe236d-6b08-4007-8820-2e5ffddc20fc\",\"Trader\":{\"OrgId\":\"603da613-c2d2-4c2e-8a0c-a3890184ec42\",\"Org\":null,\"DivisionId\":\"b54cd566-9b28-4f9c-a7ac-a3e500c0de00\",\"Division\":null,\"Name\":\"qi\",\"Password\":\"e10adc3949ba59abbe56e057f20f883e\",\"Account\":\"qi\",\"StaffNo\":null,\"Alias\":null,\"Email\":null,\"Mobile\":null,\"IsLocked\":false,\"LastLogin\":null,\"IsTrader\":true,\"IsCustomerTerminator\":false,\"IsContractTerminator\":false,\"IsPaymentTerminator\":false,\"IsInvoiceTerminator\":false,\"IsReceiptShipTerminator\":false,\"IsAudited\":false,\"IsSysAdmin\":true,\"Roles\":null,\"Id\":\"52fe236d-6b08-4007-8820-2e5ffddc20fc\",\"Version\":4,\"IsHidden\":false,\"CreatedBy\":\"管理员\",\"CreatedAt\":\"2016-06-16T18:49:56\",\"UpdatedBy\":\"qi\",\"UpdatedAt\":\"2016-06-22T19:06:56\",\"DeletedBy\":null,\"DeletedAt\":null},\"LegalId\":\"8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa\",\"Legal\":{\"OrgId\":\"603da613-c2d2-4c2e-8a0c-a3890184ec42\",\"Org\":null,\"Name\":\"集团公司\",\"Code\":\"SB\",\"Address\":null,\"Comments\":\"\",\"OrderIndex\":1,\"IsInternalCustomer\":true,\"CreatedId\":\"17293b7c-e3aa-4572-a3a5-a4e200a86762\",\"UpdatedId\":\"2eee9fdc-293f-4599-b3b1-a3890184ec56\",\"CustomerId\":\"1fd45513-7c7a-4829-b54e-a5f600c14c84\",\"CustomerName\":\"集团公司-内部客户\",\"IsExchangeGoods\":true,\"Id\":\"8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa\",\"Version\":19,\"IsHidden\":false,\"CreatedBy\":\"管理员\",\"CreatedAt\":\"2014-11-25T12:34:35\",\"UpdatedBy\":\"管理员\",\"UpdatedAt\":\"2016-06-27T09:27:03\",\"DeletedBy\":null,\"DeletedAt\":null},\"CustomerId\":\"2c52e62a-d396-4d7b-90b1-a41f00b121f4\",\"Customer\":{\"AreaId\":null,\"Area\":null,\"CreatedId\":null,\"Created\":null,\"UpdatedId\":null,\"Updated\":null,\"AreaName\":null,\"Pendings\":null,\"IsIniatiated\":true,\"IsInternalCustomer\":false,\"Name\":\"测试客户1\",\"CnName\":\"测试客户1\",\"EnName\":\"\",\"ShortName\":\"\",\"Country\":\"瑞士\",\"RegisteredCapital\":null,\"TradingLicenseNo\":null,\"OrgLicenseNo\":null,\"TaxVerifyNo\":null,\"TaxLicenseNo\":null,\"Code\":null,\"Type\":null,\"OwnerType\":null,\"CooperationType\":null,\"TerminalType\":\"销售客户\",\"EntryType\":null,\"Liaison\":\"\",\"Email\":null,\"Tel\":\"\",\"Fax\":\"\",\"Address\":\"上海市大庄管理区婴泊路\",\"Zip\":null,\"LogoUrl\":null,\"Status\":1,\"IsApproved\":true,\"Id\":\"2c52e62a-d396-4d7b-90b1-a41f00b121f4\",\"Version\":2,\"IsHidden\":false,\"CreatedBy\":\"Imported\",\"CreatedAt\":\"2015-01-13T10:44:55\",\"UpdatedBy\":null,\"UpdatedAt\":null,\"DeletedBy\":null,\"DeletedAt\":null},\"CustomerTitleId\":\"1acfd41e-b68b-4442-b64c-a41f00b121f9\",\"CustomerTitle\":null,\"CommodityId\":\"217036a2-86f7-4d94-8d4d-a3840083d06f\",\"Commodity\":{\"Name\":\"铜\",\"Code\":\"CU\",\"Unit\":\"吨\",\"Digits\":3,\"Digits4Price\":2,\"QuantityPerLot\":25,\"OrderIndex\":1,\"Comments\":\"\",\"IsFobiddenSyncLme\":true,\"Id\":\"217036a2-86f7-4d94-8d4d-a3840083d06f\",\"Version\":19,\"IsHidden\":false,\"CreatedBy\":\"系统管理员\",\"CreatedAt\":\"2014-08-11T07:59:55\",\"UpdatedBy\":\"管理员\",\"UpdatedAt\":\"2016-06-20T13:19:03\",\"DeletedBy\":null,\"DeletedAt\":null},\"Lots\":null,\"CreatedId\":\"52fe236d-6b08-4007-8820-2e5ffddc20fc\",\"Created\":null,\"UpdatedId\":null,\"FileInfoUploads\":null,\"Ingredients\":null,\"Type\":null,\"MaterielArrivalPlans\":null,\"MaterieCostAccount\":null,\"Grades\":null,\"PriceDiffs\":null,\"Pendings\":null,\"LegalCode\":null,\"LegalName\":null,\"CommodityCode\":null,\"CommodityName\":null,\"CustomerName\":null,\"CustomerTitleName\":null,\"TraderName\":null,\"Invoices\":null,\"Funds\":null,\"Storages\":null,\"Positions\":null,\"PricingRecords\":null,\"Unit\":null,\"RivalOrderID\":null,\"IsPriced\":false,\"IsInternal\":false,\"IsReBuy\":false,\"IsIniatiated\":true,\"CounterpartId\":null,\"ContractAmendId\":null,\"WithHold\":false,\"QuantityPriced\":null,\"HeadNo\":\"SCUSB160256\",\"Prefix\":\"SCUSB16\",\"SerialNo\":\"0256\",\"Suffix\":null,\"Product\":\"NA\",\"DocumentNo\":\"\",\"Quantity\":1000.00000,\"QuantityOfLots\":300.00000,\"Currency\":\"CNY\",\"SpotDirection\":\"S\",\"SpotType\":\"内贸\",\"DeliveryTerm\":\"\",\"PaymentTerm\":\"\",\"DueDays\":0,\"DueDate\":null,\"TradeDate\":\"2016-07-05T00:00:00\",\"Comments\":\"\",\"NeedFiles\":\"\",\"NaQuantity\":\"出厂净重为最终重量\",\"TestOrg\":\"双方协商\",\"Pricer\":\"us\",\"Term\":null,\"DeliveryStartDate\":null,\"DeliveryEndDate\":null,\"IsProvisional\":false,\"HedgeRatio\":80.00000,\"Status\":1,\"IsApproved\":false,\"Id\":\"d1e53d80-ce9c-4318-ab20-a63a013dad51\",\"Version\":4,\"IsHidden\":false,\"CreatedBy\":\"qi\",\"CreatedAt\":\"2016-07-05T19:16:37\",\"UpdatedBy\":null,\"UpdatedAt\":\"2016-07-07T15:20:45\",\"DeletedBy\":null,\"DeletedAt\":null},\"LegalId\":\"8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa\",\"Legal\":{\"OrgId\":\"603da613-c2d2-4c2e-8a0c-a3890184ec42\",\"Org\":null,\"Name\":\"集团公司\",\"Code\":\"SB\",\"Address\":null,\"Comments\":\"\",\"OrderIndex\":1,\"IsInternalCustomer\":true,\"CreatedId\":\"17293b7c-e3aa-4572-a3a5-a4e200a86762\",\"UpdatedId\":\"2eee9fdc-293f-4599-b3b1-a3890184ec56\",\"CustomerId\":\"1fd45513-7c7a-4829-b54e-a5f600c14c84\",\"CustomerName\":\"集团公司-内部客户\",\"IsExchangeGoods\":true,\"Id\":\"8c706b1c-b4d1-420a-9ea8-a3ee00cf40aa\",\"Version\":19,\"IsHidden\":false,\"CreatedBy\":\"管理员\",\"CreatedAt\":\"2014-11-25T12:34:35\",\"UpdatedBy\":\"管理员\",\"UpdatedAt\":\"2016-06-27T09:27:03\",\"DeletedBy\":null,\"DeletedAt\":null},\"CustomerId\":\"2c52e62a-d396-4d7b-90b1-a41f00b121f4\",\"Customer\":{\"AreaId\":null,\"Area\":null,\"CreatedId\":null,\"Created\":null,\"UpdatedId\":null,\"Updated\":null,\"AreaName\":null,\"Pendings\":null,\"IsIniatiated\":true,\"IsInternalCustomer\":false,\"Name\":\"测试客户1\",\"CnName\":\"测试客户1\",\"EnName\":\"\",\"ShortName\":\"\",\"Country\":\"瑞士\",\"RegisteredCapital\":null,\"TradingLicenseNo\":null,\"OrgLicenseNo\":null,\"TaxVerifyNo\":null,\"TaxLicenseNo\":null,\"Code\":null,\"Type\":null,\"OwnerType\":null,\"CooperationType\":null,\"TerminalType\":\"销售客户\",\"EntryType\":null,\"Liaison\":\"\",\"Email\":null,\"Tel\":\"\",\"Fax\":\"\",\"Address\":\"上海市大庄管理区婴泊路\",\"Zip\":null,\"LogoUrl\":null,\"Status\":1,\"IsApproved\":true,\"Id\":\"2c52e62a-d396-4d7b-90b1-a41f00b121f4\",\"Version\":2,\"IsHidden\":false,\"CreatedBy\":\"Imported\",\"CreatedAt\":\"2015-01-13T10:44:55\",\"UpdatedBy\":null,\"UpdatedAt\":null,\"DeletedBy\":null,\"DeletedAt\":null},\"CommodityId\":\"217036a2-86f7-4d94-8d4d-a3840083d06f\",\"Commodity\":null,\"SpecId\":\"84bab78a-4c60-488b-b464-a38401153944\",\"Spec\":null,\"OriginId\":\"eee9ebe0-4f3a-4ef0-a555-a38400dc7a21\",\"Origin\":null,\"WarehouseId\":null,\"Warehouse\":null,\"MajorMarketId\":null,\"MajorMarket\":null,\"PremiumMarketId\":\"799d4860-53da-4032-98e6-a3880078bb3a\",\"PremiumMarket\":null,\"Invoices\":null,\"Funds\":null,\"Storages\":null,\"Positions\":null,\"Pricings\":null,\"PricingRecords\":null,\"Notes\":null,\"CreatedId\":\"52fe236d-6b08-4007-8820-2e5ffddc20fc\",\"UpdatedId\":null,\"M2MPrice\":null,\"PriceDiff\":null,\"Exposure\":null,\"QuantityUnPriced\":null,\"QuantityUnInvoiced\":null,\"QuantityBeforeChanged\":null,\"Digits\":0,\"CustomerName\":null,\"CustomerShortName\":null,\"TraderName\":null,\"LegalCode\":null,\"LegalName\":null,\"SpecName\":null,\"OriginName\":null,\"Unit\":null,\"MajorMarketName\":null,\"IsIniatiated\":true,\"Grade\":0.00000,\"Discount\":0.00000,\"IsReBuy\":false,\"QuantityOriginal\":100.00000,\"IsSplitLot\":false,\"IsOriginalLot\":false,\"OriginalLotId\":null,\"MoreOrLessBasis\":\"OnPercentage\",\"MoreOrLess\":10.00000,\"IsQuantityConfirmed\":false,\"MarkColor\":0,\"IsAllowOverrideContractNo\":false,\"DateInvoiced\":null,\"DateDueInvoice\":null,\"Comments\":\"\",\"FullNo\":\"SCUSB160256/30\",\"HeadNo\":\"SCUSB160256\",\"PrefixNo\":null,\"SerialNo\":\"0256\",\"LotNo\":30,\"DocumentNo\":\"\",\"PnL\":null,\"Quantity\":100.00000,\"QuantityLess\":90.00000,\"QuantityMore\":110.00000,\"QuantityDelivered\":0.00000,\"QuantityInvoiced\":null,\"QuantityFunded\":null,\"AmountInvoiced\":null,\"AmountFunded\":null,\"AmountFundedDraft\":null,\"QuantityPriced\":100.00000,\"QuantityHedged\":0.00000,\"HedgedPrice\":null,\"BrandIds\":\"5c5a30a3-b27f-45ed-a155-a56700f9d910,a119ef38-b151-4519-bfd8-a3ee00deb267,3b1dd59e-2bc6-41d3-b93f-a3fc01567d43\",\"BrandNames\":\"金升,CIMCO,三尖\",\"SpotDirection\":\"S\",\"Status\":1,\"DueBalance\":null,\"PaidBalance\":null,\"LastBalance\":null,\"IsDelivered\":false,\"IsInvoiced\":false,\"IsHedged\":false,\"IsFunded\":false,\"IsSettled\":false,\"IsAccounted\":false,\"IsPriced\":true,\"IsFeeEliminated\":false,\"PricingType\":\"F+F\",\"Final\":20.00000,\"Price\":20.00000,\"Major\":10.00000,\"Premium\":10.00000,\"Fee\":0.00000,\"RealFee\":0.00000,\"EstimateFee\":0.00000,\"Currency\":\"CNY\",\"Product\":\"NA\",\"EstimateSaleDate\":\"2016-07-07T00:00:00\",\"IsEtaPricing\":false,\"EtaDuaration\":null,\"EtaPrice\":null,\"MajorType\":\"F\",\"MajorBasis\":null,\"MajorStartDate\":null,\"MajorEndDate\":null,\"MajorDays\":0,\"QtyPerMainDay\":null,\"PremiumDays\":0,\"QtyPerPremiumDay\":null,\"PremiumType\":\"F\",\"PremiumBasis\":\"A\",\"PremiumStartDate\":null,\"PremiumEndDate\":null,\"DeliveryTerm\":\"\",\"Loading\":\"\",\"Discharging\":\"\",\"EstDischarging\":\"\",\"ETD\":null,\"ETA\":null,\"ATD\":null,\"ATA\":null,\"CommentsLot\":null,\"QP\":\"2016-07-23T00:00:00\",\"OriginalQP\":null,\"Id\":\"ddcd7c10-49c6-4545-b530-a63c00fce50e\",\"Version\":21,\"IsHidden\":false,\"CreatedBy\":\"qi\",\"CreatedAt\":\"2016-07-07T15:20:45\",\"UpdatedBy\":\"zhenghao\",\"UpdatedAt\":\"2016-07-08T09:11:00\",\"DeletedBy\":null,\"DeletedAt\":null},\"TradeDate\":\"2016-01-07T00:00:00\",\"CurrentQP\":\"2016-01-20T00:00:00\",\"RevisedQP\":\"2016-07-23T00:00:00\",\"CurrentQPPrice\":0.00,\"RevisedQPPrice\":0.00,\"PriceDiff\":0.00,\"IsInitial\":false,\"CreatedId\":\"52fe236d-6b08-4007-8820-2e5ffddc20fc\",\"UpdatedId\":\"789d2be6-3e3b-49e8-bc5d-8f692e46eda8\",\"Id\":\"a838aa3d-55dc-4076-bff6-361689cbe7d8\",\"Version\":2,\"IsHidden\":false,\"CreatedBy\":\"qi\",\"CreatedAt\":\"2016-07-07T15:24:05\",\"UpdatedBy\":\"zhenghao\",\"UpdatedAt\":\"2016-07-07T15:55:15\",\"DeletedBy\":null,\"DeletedAt\":null}";

        String result = URLUtil.post(url,JSON.parse(parameter),JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);


    }



    @Test
    public void SaveLotOfContractRegularNew(){

        String url= JunitTest.host + "api/Physical/Lot/SaveLotOfContractRegularNew";

        String result = URLUtil.post(url,new Lot(),JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);


    }


}
