package com.smm.probase.test.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.smm.ctrm.domain.Physical.Position;
import com.smm.ctrm.util.URLUtil;
import com.smm.probase.test.JunitTest;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhenghao on 2016/7/14.
 *
 */
public class PositionApiTest {

    private static Logger logger= Logger.getLogger(InvoiceApiTest.class);


    @Test
    public void GetCarryPostionsById(){

        String url= JunitTest.host + "api/Futures/Position/GetCarryPostionsById";

        String id="7B0EB939-85E8-437D-9602-A617010951D5";

        String result = URLUtil.post(url, id, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void GetCounterPartById(){

        String url= JunitTest.host + "api/Futures/Position/GetCounterPartById";

        String id="7B0EB939-85E8-437D-9602-A617010951D5";

        String result = URLUtil.post(url, id, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void UnPosition2Lot(){

        String url= JunitTest.host + "api/Futures/Position/UnPosition2Lot";

        Map<String,String> params = new HashMap<>();

        params.put("Id","DEBCA537-791A-43BC-AAF9-A4E5010ADC6C");

        String result = URLUtil.post(url, params, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }


    @Test
    public void getM2MPrice(){

        String url= JunitTest.host + "api/Futures/Position/getM2MPrice";

        Map<String,String> params = new HashMap<>();
        params.put("TradeDate","2016-07-14T00:00:00");
        params.put("PromptDate","2016-07-15T00:00:00");
        params.put("CommodityId","217036A2-86F7-4D94-8D4D-A3840083D06F");

        String result = URLUtil.post(url, params, JunitTest.getToken());

        JSONObject obj = JSON.parseObject(result);

        logger.info(obj);

        boolean success = Boolean.parseBoolean(obj.getString("Success"));

        Assert.isTrue(success);

    }




}
