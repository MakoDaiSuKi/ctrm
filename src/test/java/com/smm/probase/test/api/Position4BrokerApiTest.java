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
 * Created by zhenghao on 2016/7/13.
 *
 */
public class Position4BrokerApiTest {


    private static Logger logger= Logger.getLogger(InvoiceApiTest.class);


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


    @Test
    public void GenerateBrokerPosition(){

        String url= JunitTest.host + "api/Futures/Position4Broker/GenerateBrokerPosition";

        String result = URLUtil.post(url, null, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }

    @Test
    public void Square(){

        String url= JunitTest.host + "api/Futures/Position4Broker/Square";

        String result = URLUtil.post(url, null, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void Delete(){

        String url= JunitTest.host + "api/Futures/Position4Broker/Delete";

        String id="0DD70AEE-C8FC-47C9-9D3F-A642014AC0F4";

//        id="1D91BDC4-175E-411A-BE69-A63B01244752";

        String result = URLUtil.post(url, id, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }




}
