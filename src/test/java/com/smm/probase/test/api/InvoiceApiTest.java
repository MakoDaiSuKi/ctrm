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
 * Created by zhenghao on 2016/7/6.
 *
 */
public class InvoiceApiTest {


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

        params.put("PageSize","20");
        params.put("PageIndex","1");
        params.put("SortBy","CreatedAt");
        params.put("OrderBy","desc");

    }


    @Test
    public void Pager(){

        String url= JunitTest.host + "api/Finance/Invoice/Pager";

        String result = URLUtil.post(url, new Parameter(), JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }
}
