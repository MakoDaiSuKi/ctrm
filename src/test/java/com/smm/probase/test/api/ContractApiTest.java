package com.smm.probase.test.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smm.ctrm.util.URLUtil;
import com.smm.probase.test.JunitTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghao on 2016/7/1.
 *
 *
 */
public class ContractApiTest {


    private static Logger logger= Logger.getLogger(LotApiTest.class);

    private static Map<String,String> params = new HashMap<>();

    static {

        params.put("Keyword","BCUSB140194");
        params.put("Currency","USD");
        params.put("SpotType","外贸");
        params.put("Statuses","1");
        params.put("LegalIds","8C706B1C-B4D1-420A-9EA8-A3EE00CF40AA,98DB488D-BFA9-4FFF-A143-A3EE00CF57F2");
        params.put("CreatedId","98D251F4-9CDB-4231-9F8B-A43A00F6D73E");

        params.put("CommodityId","217036A2-86F7-4D94-8D4D-A3840083D06F");
        params.put("LegalId","8C706B1C-B4D1-420A-9EA8-A3EE00CF40AA");
        params.put("SpotDirection","B");

        params.put("StartDate", "2013-12-03T00:00:00");
        params.put("EndDate", "2015-12-03T00:00:00");

        params.put("PageSize","10");
        params.put("PageIndex","1");
        params.put("SortBy","CreatedAt");
        params.put("OrderBy","desc");

        params.put("IsConfirm","true");
    }



    private static Map<String,String> params1 = new HashMap<>();

    static {

        params1.put("Currency","");
        params1.put("SpotType","");
        params1.put("SpotDirection",null);
        params1.put("IsConfirm",null);
        params1.put("CreatedId",null);
        params1.put("TraderId",null);
        params1.put("Statuses","");

        params1.put("LegalId", null);
        params1.put("LegalIds", "");
        params1.put("CommodityId", null);
        params1.put("TradeDate", null);
        params1.put("StartDate", null);
        params1.put("EndDate", null);

        params1.put("Keyword", "BCUSB1601");

        params1.put("PageSize","10");
        params1.put("PageIndex","1");
        params1.put("SortBy","HeadNo");
        params1.put("OrderBy","Descending");

    }


    private static Map<String,String> params2 = new HashMap<>();

    static {

        params2.put("Currency","");
        params2.put("SpotType","");
        params2.put("SpotDirection",null);
        params2.put("IsConfirm",null);
        params2.put("CreatedId",null);
        params2.put("TraderId",null);
        params2.put("Statuses","");

        params2.put("LegalId", null);
        params2.put("LegalIds", "");
        params2.put("CommodityId", null);
        params2.put("TradeDate", null);
        params2.put("StartDate", null);
        params2.put("EndDate", null);

        params2.put("Keyword", "");

        params2.put("PageSize","10");
        params2.put("PageIndex","1");
        params2.put("SortBy","HeadNo");
        params2.put("OrderBy","Descending");

    }


    private static Map<String,String> params3 = new HashMap<>();

    static {

        params3.put("Currency","");
        params3.put("SpotType","");
        params3.put("SpotDirection",null);
        params3.put("IsConfirm",null);
        params3.put("CreatedId",null);
        params3.put("TraderId",null);
        params3.put("Statuses","");

        params3.put("LegalId", null);
        params3.put("LegalIds", "");
        params3.put("CommodityId", null);
        params3.put("TradeDate", null);
        params3.put("StartDate", null);
        params3.put("EndDate", null);

        params3.put("Keyword", "");

        params3.put("PageSize","50");
        params3.put("PageIndex","1");
        params3.put("SortBy",null);
        params3.put("OrderBy",null);

    }


    @Test
    public void Pager(){

        String url= JunitTest.host + "api/Physical/Contract/Pager";

        String result = URLUtil.post(url, params3, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void Pager4Glue(){

        String url= JunitTest.host + "api/Physical/Contract/Pager4Glue";

        String result = URLUtil.post(url,null,JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);
    }
}
